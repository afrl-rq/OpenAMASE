// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 22, 2005
 */
package org.flexdock.dockbar.event;

import java.awt.Point;



import org.flexdock.dockbar.DockbarManager;
import org.flexdock.docking.Dockable;
import org.flexdock.event.EventManager;
import org.flexdock.util.Utilities;

/**
 * @author Christopher Butler
 */
public class ActivationListener {

    private DockbarManager manager;
    private Deactivator deactivator;
    private boolean enabled;
    private boolean mouseOver;


    public ActivationListener(DockbarManager mgr) {
        manager = mgr;
        setEnabled(true);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isActive() {
        return manager.isActive() && !manager.isAnimating() && !manager.isDragging();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAvailable() {
        return isEnabled() && isActive();
    }

    public boolean isViewpaneLocked() {
        return manager.getViewPane().isLocked();
    }

    private boolean isOverDockbars(Point mousePoint) {
        return manager.getLeftBar().getBounds().contains(mousePoint)
               || manager.getRightBar().getBounds().contains(mousePoint)
               || manager.getBottomBar().getBounds().contains(mousePoint);
    }



    public void mouseEntered(Point mousePoint) {
        if(mouseOver)
            return;

        mouseOver = true;
        if(deactivator!=null)
            deactivator.setEnabled(false);
        deactivator = null;
    }

    public void mouseExited(Point mousePoint) {
        if(!mouseOver)
            return;

        mouseOver = false;
        if(!isOverDockbars(mousePoint)) {
            deactivator = new Deactivator(manager.getActiveDockableId());
            deactivator.setEnabled(true);
            deactivator.start();
        }
    }

    public void mousePressed(Point mousePoint, boolean mouseOver) {
        if(mouseOver) {
            if(!isViewpaneLocked()) {
                lockViewpane();
            }
        } else {
            if(!isOverDockbars(mousePoint)) {
                manager.setActiveDockable((String)null);
            }
        }
    }

    public void lockViewpane() {
        manager.getViewPane().setLocked(true);
        dispatchDockbarEvent(DockbarEvent.LOCKED);
    }


    private void dispatchDockbarEvent(int type) {
        Dockable dockable = manager.getActiveDockable();
        int edge = manager.getActiveEdge();
        DockbarEvent evt = new DockbarEvent(dockable, type, edge);
        EventManager.dispatch(evt);
    }

    private class Deactivator extends Thread {
        private String dockableId;
        private boolean enabled;

        private Deactivator(String id) {
            dockableId = id;
            enabled = true;
        }

        private synchronized void setEnabled(boolean b) {
            enabled = b;
        }

        private synchronized boolean isEnabled() {
            return enabled;
        }

        public void run() {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                System.err.println("Exception: " +e.getMessage());
                e.printStackTrace();
            }

            if(isEnabled() && !Utilities.isChanged(dockableId, manager.getActiveDockableId()) &&
                    !isViewpaneLocked())
                manager.setActiveDockable((String)null);
        }

    }

    public boolean isMouseOver() {
        return mouseOver;
    }

}
