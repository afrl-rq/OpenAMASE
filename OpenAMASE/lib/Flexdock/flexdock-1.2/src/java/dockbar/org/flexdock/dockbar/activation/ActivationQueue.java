// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 21, 2005
 */
package org.flexdock.dockbar.activation;

import org.flexdock.dockbar.DockbarManager;



/**
 * @author Christopher Butler
 */
public class ActivationQueue extends Thread {
    private DockbarManager manager;
    private Animation deactivation;
    private Runnable postDeactivate;
    private Animation activation;
    private Runnable postActivate;

    public ActivationQueue(DockbarManager mgr, Animation deactivation, Runnable r1, Animation activation, Runnable r2) {
        manager = mgr;
        this.deactivation = deactivation;
        this.postDeactivate = r1;
        this.activation = activation;
        this.postActivate = r2;
    }


    public void run() {
        manager.setAnimating(true);
        if(deactivation!=null)
            deactivation.run();
        postDeactivate.run();
        if(activation!=null)
            activation.run();
        postActivate.run();
        manager.setAnimating(false);
    }



}
