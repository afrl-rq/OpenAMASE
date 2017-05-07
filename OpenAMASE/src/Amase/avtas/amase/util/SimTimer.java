// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.util;

import avtas.amase.scenario.ScenarioEvent;
import avtas.app.AppEventManager;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import avtas.amase.AmasePlugin;
import avtas.amase.scenario.ScenarioState;
import avtas.amase.ui.TimerGui;
import avtas.app.Context;
import avtas.app.Timer;
import avtas.app.TimerClient;
import avtas.util.WindowUtils;
import avtas.xml.XMLUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import avtas.xml.Element;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Implements a simple simulation timer. This class uses the
 * {@link avtas.app.Timer} class as the underlying timer mechanism. 
 * This controls the timing signals and publishes CMASI SessionStatus 
 * messages as appropriate.
 *
 * This class is a singleton. There should only be one in the context.
 *
 * Frame rate can be configured through the configuration file using FrameRate
 * node, in frames per second Publish rate can be configured through the
 * configuration file using PublishRate node in times per second Initial time
 * (start time) can be configured through the scenario files using
 * ScenarioData/StartTime node in seconds
 *
 * @author AFRL/RQQD
 */
public class SimTimer extends AmasePlugin {

    public static final byte STATUS_STOP = 0;
    public static final byte STATUS_RUNNING = 1;
    public static final byte STATUS_PAUSE = 2;
    public static final byte STATUS_RESET = 3;
    private static double secStartTime = 0;
    /**
     * current status
     */
    private static SimulationStatusType status = SimulationStatusType.Paused;
    private static SessionStatus pauseStatus = null;
    private static SessionStatus runStatus = null;
    /**
     * the LMCP AppEventManager for sending LMCP events
     */
    private static AppEventManager mgr = null;
    /**
     * the simulation timestep (seconds)
     */
    private static double framestep;
    /**
     * the simulation time step for publishing SimStatus messages
     */
    private static double publishTime;
    /**
     * the simulation time of the last publish of a status message
     */
    private static double secLastPublish;
    /**
     * the simulation time that the scenario is to end (the timer will stop)
     */
    private static double endTime = Double.MAX_VALUE;
    /**
     * flag denoting that the timer is in a pauses state after sending a
     * PlanRequest and is awaiting a MissionCommand message.
     */
    private static boolean waitingMissionCmd = false;

    private static avtas.app.Timer timer;
    private static SimTimer instance = null;

    public SimTimer() {
        this(30, 2);
    }

    public SimTimer(int framerate, int publish_rate) {
        
        instance = this;

        timer = Timer.getDefaultTimer();
        setFramerate(framerate);
        setPublishRate(publish_rate);

        mgr = AppEventManager.getDefaultEventManager();
        timer.pause();

        pauseStatus = new SessionStatus();
        runStatus = new SessionStatus();

    }

    public void resetScenario(SessionStatus reset) {
        
        // get the scenario start time (seconds since epoch)
        double startTime = reset.getStartTime();
        pauseStatus = reset;
        
        timer.reset(startTime);
        

        
        // offset the end time of the scenario based on the start time.
        endTime = endTime + startTime;

        // set up the running status message
        runStatus.setStartTime( (long) (startTime * 1000) );
        runStatus.setState(SimulationStatusType.Running);
        
        timer.setTime(startTime);

        status = SimulationStatusType.Reset;
        secLastPublish = 0;
    }

    /**
     * sets the frame rate for the timer. This is the rate at which all modules
     * are updated by the timer.
     *
     * @param frames_per_sec
     */
    public static void setFramerate(double frames_per_sec) {
        framestep = 1. / frames_per_sec;
        if (timer.getInternalRate() < frames_per_sec) {
            timer.setInternalRate((int) frames_per_sec);
        }
        timer.addClient(instance, framestep);
        
    }

    public static double getFramerate() {
        return 1. / framestep;
    }

    /**
     * sets the rate at which SimStatus messages are sent by the timer.
     *
     * @param pubs_per_sec number of times per second that SimStatus messages
     * are sent
     */
    public static void setPublishRate(double pubs_per_sec) {
        publishTime = 1. / pubs_per_sec;
    }

    public static double getPublishRate() {
        return 1. / publishTime;
    }

    void publish() {
        if (status == SimulationStatusType.Running) {
            runStatus.setScenarioTime( (long) (timer.getTime()*1000));
            runStatus.setRealTimeMultiple((float) timer.getRuntimeMultiple());
            mgr.fireEvent(runStatus, this);
        }
        else {
            pauseStatus.setScenarioTime( (long) (timer.getTime()*1000));
            pauseStatus.setRealTimeMultiple((float) timer.getRuntimeMultiple());
            pauseStatus.setState(status);
            pauseStatus.setStartTime( (long) (secStartTime*1000));
            mgr.fireEvent(pauseStatus, this);
        }
    }

    /**
     * returns the current simulation time in seconds
     */
    public static double getTime() {
        return timer.getTime();
    }

    /**
     * starts (or resumes) this timer
     */
    public static void go() {
        status = SimulationStatusType.Running;
        timer.go();

    }

    /**
     * run full speed until the specified time is reached. This leaves the timer
     * in a paused state.  This method blocks until the timer is finished.
     */
    public static void go(double secEndTime) {
        status = SimulationStatusType.Running;
        timer.runDirectly(secEndTime);
        status = SimulationStatusType.Paused;
        instance.publish();
    }

    /**
     * pauses the timer
     */
    public static void pause() {
        timer.pause();
        status = SimulationStatusType.Paused;
        instance.publish();
    }

    /**
     * stops the timer. This should be used only in cases when the timer is not
     * going to be run again.
     */
    public static void stop() {
        timer.reset(0);
        status = SimulationStatusType.Stopped;
        instance.publish();
    }

    /**
     * sets the real-time multiple ( a ratio of target simulation time step to
     * wallclock time step.
     *
     * @param mult the ratio of simulation-to-wallclock time.
     */
    public static void setRealtimeMultiple(double mult) {
        timer.setRuntimeRate(mult);
    }

    public static double getRealtimeMultiple() {
        return timer.getRuntimeMultiple();
    }

    public static SimulationStatusType getStatus() {
        return status;
    }

    public static boolean isWaitingForPlan() {
        return waitingMissionCmd;
    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
        initialize(xml, cmdParams);
    }

    @Override
    public void applicationPeerAdded(Object peer) {
        if (peer instanceof TimerClient) {
            timer.addClient((TimerClient) peer, framestep);
        }
    }

    @Override
    public void applicationPeerRemoved(Object peer) {
        if (peer instanceof TimerClient) {
            timer.removeClient((TimerClient) peer);
        }
    }

    public void eventOccurred(Object evt) {
        if (evt instanceof ScenarioEvent) {
            ScenarioEvent scenEvent = (ScenarioEvent) evt;
            // set the end time (if it is specified in the file) 
            endTime = XMLUtil.getDouble(scenEvent.getXML(), "ScenarioData/ScenarioDuration", 1E9);
        }
        else if (evt instanceof SessionStatus) {
            SessionStatus ss = (SessionStatus) evt;
            if (ss.getState() == SimulationStatusType.Reset) {
                resetScenario(ss);
            }
            timer.setTime(ss.getScenarioTime() / 1000d);
        }
    }

    @Override
    public void getMenus(final JMenuBar menubar) {
        JMenu timerMenu = WindowUtils.getMenu(menubar, "Simulation");
        JMenuItem item = new JMenuItem("Timer Options");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TimerGui.showDialog(JOptionPane.getFrameForComponent(menubar), SimTimer.this);
            }
        });
        timerMenu.add(item);
    }

    @Override
    public JPanel getSettingsPanel() {
        return new TimerGui(this);
    }

    public void initialize(Element node, String[] args) {
        double framerate = XMLUtil.getDouble(node, "FrameRate", 1. / framestep);
        SimTimer.framestep = 1. / framerate;
        double publishRate = XMLUtil.getDouble(node, "PublishRate", 1. / publishTime);
        SimTimer.publishTime = 1. / publishRate;
    }

    @Override
    public void step(double timestep, double sim_time) {
        
        ScenarioState.setTime(sim_time);
        
        if (sim_time - secLastPublish > publishTime) {
            secLastPublish = sim_time;
            publish();
        }
        if (sim_time >= endTime && timer.isRunning()) {
            status = SimulationStatusType.Stopped;
            timer.pause();
            publish();
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */