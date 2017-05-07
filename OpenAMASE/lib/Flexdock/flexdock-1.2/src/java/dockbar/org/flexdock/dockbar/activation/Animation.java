// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 17, 2005
 */
package org.flexdock.dockbar.activation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;



import org.flexdock.dockbar.DockbarManager;
import org.flexdock.dockbar.ViewPane;



/**
 * @author Christopher Butler
 */
public class Animation implements Runnable, ActionListener {

    private static int ANIMATION_INTERVAL = 20;
    private static int TOTAL_FRAME_COUNT = 5;

    private DockbarManager dockManager;
    private Timer timer;
    private float frameDelta;
    private int frameCount;
    private boolean hiding;
    private Runnable next;
    private Object lock;

    public Animation(DockbarManager mgr, boolean hide) {
        dockManager = mgr;
        timer = new Timer(ANIMATION_INTERVAL, this);
        frameDelta = (100f/(float)getTotalFrameCount())/100f;
        hiding = hide;
        lock = new Object();
    }

    public void run() {
        timer.start();
        sleep();
    }

    public void actionPerformed(ActionEvent e) {
        resetViewpaneSize();
        dockManager.revalidate();
        if(frameCount==getTotalFrameCount()-1) {
            timer.stop();
            wakeUp();
        } else
            frameCount++;
    }

    private void resetViewpaneSize() {
        ViewPane viewPane = dockManager.getViewPane();
        int prefSize = dockManager.getPreferredViewpaneSize();

        if(frameCount==0)
            prefSize = getStartSize(prefSize);
        else if(frameCount==getTotalFrameCount()-1)
            prefSize = getEndSize(prefSize);
        else {
            int newSize = (int)((float)prefSize * (frameCount*frameDelta));
            prefSize = hiding? prefSize-newSize: newSize;
        }

        viewPane.setPrefSize(prefSize);
    }

    private int getStartSize(int prefSize) {
        if(hiding)
            return prefSize;
        return 0;
    }

    private int getEndSize(int prefSize) {
        if(hiding)
            return 0;
        return prefSize;
    }

    private int getTotalFrameCount() {
        return TOTAL_FRAME_COUNT;
    }

    public Runnable getNext() {
        return next;
    }
    public void setNext(Runnable next) {
        this.next = next;
    }

    private void sleep() {
        synchronized(lock) {
            try {
                lock.wait();
            } catch(InterruptedException e) {
                System.err.println("Exception: " +e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void wakeUp() {
        synchronized(lock) {
            lock.notifyAll();
        }
    }
}