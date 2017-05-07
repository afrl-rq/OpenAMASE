// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.app;

import avtas.app.TimerClient.TimerState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A facility for managing timed execution of multiple clients. This timer
 * implements a single thread to manage execution. Clients are added to the
 * timer at a requested update rate (time step). When the timer executes, each
 * client is updated based on the elapsed time since the client was last called.
 * Clients can request any update rate. At each step in the timer, a client is
 * called if the elapsed time since the last call to the client is greater than
 * or equal to the requested timestep for that client. For clients that are
 * faster than the internal timer step, those clients will be called (on
 * average) more than one time per timer iteration. For clients with a slower
 * update rate, multiple timer iterations may occur before the client is
 * called.<br/>
 *
 * <b>Example:</b><br/>
 * The timer is running at an internal rate of 100 Hz (10 ms step). A client is
 * added that requests updates at 30 Hz (33.3 ms step). Assuming that the timer
 * has a perfect 10 Hz <em>actual</em> step, the timer does not invoke the
 * client for the first three iterations. On the fourth iteration, the client is
 * invoked once, and a remainder of 6.7 ms is stored for the timer. (The time
 * remainder is added to every time the client is invoked for a total time
 * difference greater than the requested timestep for that client, so upon the
 * next timer iteration, the stored time difference is 16.7 ms, then 26.7 ms,
 * and so on). This means that the client is invoked on the third iteration, not
 * the forth, and the remainder stored for the client is 3.4 ms (36.7 - 33.3).
 * This continues until the timer is stopped.<br/>
 *
 * <b>A note regarding internal time-stepping:</b><br/>
 * The internal timing mechanism has been changed.  Time steps are now handled 
 * via a single that is created when the go() method is called. Timing is aligned
 * with wallclock time to ensure approximate alignment with real-time.  Timer internal
 * rate can now be set at any time. 
 *
 * @author AFRL/RQQD
 */
public class Timer {

    /**
     * Default, system-wide timer *
     */
    private static Timer defaultTimer = null;
    /**
     * A map of registered timers that can be accessed by name
     */
    static final HashMap<String, Timer> timerMap = new HashMap<String, Timer>();
    /**
     * current simulation time
     */
    double time = 0; // seconds

    /**
     * ideal internal clock update rate
     */
    int internal_rate = 100;
    /**
     * internal timing thread
     */
    Updater timerThread = null;

    boolean pause = true;

    /**
     * A list of objects that receiving timer events (timesteps and state
     * changes)
     */
    ArrayList<ClientEntry> clientList = new ArrayList<>();

    /**
     * multiple of wallclock time to run the timer
     */
    private double runtime_mult = 1;

    /**
     * actual runtime multiple. Can be less than the value set if execution
     * takes longer than the value set.
     */
    double actual_runtime_rate;

    /**
     * Used to send a pause or reset signal to clients if the state is set to
     * change by the user.
     */
    //TimerState waitingSignal = null;

    /**
     * Creates a new Timer. Override this constructor to change timer behavior,
     * such as the underlying mechanism for controlling time.
     */
    public Timer() {
        initTimer();
    }

    /**
     * Returns the default, system-wide timer.
     *
     * @return the default, system-wide timer
     */
    public static Timer getDefaultTimer() {
        if (defaultTimer == null) {
            defaultTimer = new Timer();
        }
        return defaultTimer;
    }

    /**
     * Sets the default timer.
     *
     * @param timer the new default timer. Null values are ignored.
     */
    public static void setDefaultTimer(Timer timer) {
        if (timer != null) {
            defaultTimer = timer;
        }
    }

    /**
     * Retrieves a timer stored with the given name. If there is no timer with
     * that name, then one is created and returned.
     *
     * @param name reference name for the timer
     * @return A timer that is stored under the given name, or a new timer if
     * one does not exist.
     */
    public static Timer getTimer(String name) {
        Timer timer = timerMap.get(name);
        if (timer == null) {
            timer = new Timer();
            timerMap.put(name, timer);
        }
        return timer;
    }

    /**
     * Returns true if the system has a registered timer with the given name.
     *
     * @param name a reference name for a stored timer
     * @return true if there is a stored timer with the given name
     */
    public static boolean timerRegistered(String name) {
        return timerMap.containsKey(name);
    }

    /**
     * Register a client with this timer. Clients are updated according to the
     * requested timestep. This method should only be called when the timer is
     * in a paused or stopped state. If the passed client is already registered
     * with this timer, then it is removed and re-added with the new timestep.
     *
     * @param client A client to add
     * @param timestep the requested timestep at which to update the client
     * (seconds)
     */
    public void addClient(TimerClient client, double timestep) {
        if (pause) {
            removeClient(client);
            clientList.add(new ClientEntry(timestep, client));
        }
    }

    /**
     * Removes the specified client from this timer, if it is registered. Only
     * call this when the timer is in a paused or stopped state. This method
     * returns false if it is called while the timer is running.
     *
     * @param client the client to remove
     * @return true if the requested client was registered to this timer.
     */
    public boolean removeClient(TimerClient client) {
        boolean contained = false;
        if (pause) {
            for (ListIterator<ClientEntry> it = clientList.listIterator(); it.hasNext();) {
                if (it.next().client == client) {
                    it.remove();
                    contained = true;
                }
            }
        }
        return contained;
    }

    /**
     * Commands the timer to start running indefinitely.
     */
    public void go() {

        // if already running, don't do anything
        if (timerThread != null && timerThread.isAlive()) {
            return;
        }

        timerThread = new Updater();
        pause = false;
        timerThread.start();
    }

    /**
     * Pauses the timer. This keeps the current simulation time.
     */
    public void pause() {

        // need to do this in a separate thread since the timer may be called from within the
        // timer thread
        new Thread() {
            public void run() {

                pause = true;

                // wait for the timer to stop
                if (timerThread != null) {
                    try {
                        timerThread.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                //publish the change in state
                for (int i = 0; i < clientList.size(); i++) {
                    clientList.get(i).client.timerStateChanged(TimerState.Paused, time);
                }

            }
        }.start();
    }

    /**
     * Stops the timer. This stops execution and sets simulation time to zero.
     * @param startTime
     */
    public void reset(final double startTime) {

        new Thread() {
            public void run() {
                pause = true;

                // wait for the timer to stop
                if (timerThread != null && timerThread.isAlive()) {
                    try {
                        timerThread.join(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                time = startTime;

                //publish the change in state
                for (int i = 0; i < clientList.size(); i++) {
                    clientList.get(i).client.timerStateChanged(TimerState.Reset, time);
                }
            }
        }.start();

    }

    /**
     * @return true if the timer thread is runnning (not paused or stopped)
     */
    public boolean isRunning() {
        return !pause;
    }

    /**
     * Returns the current simulation time (seconds)
     *
     * @return the current simulation time (seconds)
     */
    public double getTime() {
        return time;
    }

    /**
     * Sets the current simulation time.
     *
     * @param time time to set (seconds)
     */
    public void setTime(double time) {
        reset(time);
        this.time = time;
    }

    /**
     * Sets the runtime rate (as a multiple of real-time)
     *
     * @param mult
     */
    public void setRuntimeRate(double mult) {
        mult = Math.abs(mult);
        // don't let the multiple go to infinity.  1 million is enough
        if (mult > 1E6) {
            mult = 1E6;
        }
        this.runtime_mult = mult;
        this.actual_runtime_rate = mult;
    }

    /**
     * Returns the current runtime multiple (multiple of real-time)
     *
     * @return the current runtime multiple (multiple of real-time)
     */
    public double getRuntimeMultiple() {
        return runtime_mult;
    }

    /**
     * returns the last observed runtime rate in the timer. This may be less
     * than the runtime rate set by the user.
     *
     * @return actual runtime rate.
     */
    public double getActualRuntimeRate() {
        return actual_runtime_rate;
    }


    /**
     * Sets the target timestep for this timer. This is used when calling sleep.
     * Avoid large internal timesteps.
     *
     * @param rate rate of internal update (iter/sec).
     */
    public void setInternalRate(int rate) {
        this.internal_rate = rate;
        initTimer();
    }

    /**
     * Returns the rate of the internal timer thread (in iter/seconds)
     */
    public double getInternalRate() {
        return internal_rate;
    }

    /**
     * Runs the timer directly (on the current thread) until the requested time
     * has been reached. This method passes timestep to the clients using the
     * current value for the internal rate. The timer will be left in a paused
     * state at the end of the execution. The internal time is advanced as if
     * the timer were running normally.
     *
     * @param maxTime time in scenario seconds at which the timer should stop.
     */
    public void runDirectly(double maxTime) {

        //stop current execution , if any
        pause();

        double step = 1. / internal_rate;
        while (time < maxTime) {
            time += step;
            for (int i = 0; i < clientList.size(); i++) {
                clientList.get(i).step(step, time);
            }
        }
        pause();
    }

    protected void initTimer() {
//        pause = true;
//        if (internalTimer != null) {
//            internalTimer.cancel();
//            internalTimer.purge();
//        }
//        timerThread = new Updater();
//        internalTimer = new java.util.Timer();
//
//        pause();
//        long period = (int) (1. / internal_rate * 1000.);
//        internalTimer.scheduleAtFixedRate(timerThread, period, period);
    }

    protected class Updater extends Thread {

        long lastTime_nano = 0;
        long time_nano = 0;
        long num_iter = 0;
        double total_step;
        double step;
        double ideal_step;
        long wallclock_start_time = 0;
        long internal_time = 0;

        public Updater() {
        }

        @Override
        public void run() {

            try {

                wallclock_start_time = System.nanoTime();

                while (!pause) {

                    ideal_step = 1. / internal_rate;

                    internal_time += (long) (ideal_step * 1E9);

                    //the total step time is the actual time x the runtime rate
                    total_step = ideal_step * runtime_mult;

                    // each iteration passes the internal clock rate, unless the runtime rate is
                    // less than one
                    if (runtime_mult < 1) {
                        ideal_step *= runtime_mult;
                    }

                    step = 0;
                    num_iter = 0;

                    while (step < total_step) {
                        if (pause) {
                            return;
                        }
                        time += ideal_step;
                        step += ideal_step;
                        num_iter++;

                        for (int i = 0; i < clientList.size(); i++) {
                            clientList.get(i).step(ideal_step, time);
                        }
                    }
                    
                    if (pause) {
                        return;
                    }

                    // wait for the next execution 
                    double wallclock_time = System.nanoTime() - wallclock_start_time;
                    while (wallclock_time < internal_time) {
                        Thread.sleep( (long) ((internal_time - wallclock_time) * 1E-6) );
                        wallclock_time = System.nanoTime() - wallclock_start_time;
                    }

                }
            } catch (InterruptedException ex) {
            }

        }
    }

    /**
     * Container class for timer clients.
     */
    static class ClientEntry {

        double ideal_timestep;
        double remaining_iter = 0;
        TimerClient client;

        public ClientEntry(double timestep, TimerClient client) {
            this.ideal_timestep = timestep;
            this.client = client;
        }

        public void step(double timestep, double time) {
            // compute the number of iterations for this client.  If it is less than
            // 1, then save that value and return, otherwise, take the whole number
            // iterations, and step the client that number of times.  Save the remainder
            // for future execution.
            double num_iter = timestep / ideal_timestep + remaining_iter;
            remaining_iter = num_iter - (int) num_iter;

            for (int i = 1; i <= (int) num_iter; i++) {
                client.step(ideal_timestep, time);
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */