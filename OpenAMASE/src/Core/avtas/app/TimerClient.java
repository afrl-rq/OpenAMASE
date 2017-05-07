// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.app;

/**
 * An interface for various timers.  
 * @author AFRL/RQQD
 */
public interface TimerClient {
   
    public static enum TimerState {
        Running, Paused, Reset;
    }
    
    /**
     * Updates this client.
     * @param timestep incremental timestep (seconds)
     * @param sim_time current simulation time (seconds)
     */
    public void step(double timestep, double sim_time);
    
    /** 
     * Notifies of a change in the timer state, such as pausing or resetting.
     * @param state new state of the timer
     * @param sim_time current simulation time (seconds)
     */
    public void timerStateChanged(TimerState state, double sim_time);
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */