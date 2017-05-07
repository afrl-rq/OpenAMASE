// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on May 26, 2005
 */
package org.flexdock.dockbar;

import java.awt.Component;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.state.MinimizationManager;

/**
 * @author Christopher Butler
 */
public class MinimizationAdapter implements MinimizationManager {

    static {
        init();
    }

    private static void init() {
        // make sure DockbarManager is initialized
        Class c = DockbarManager.class;
    }

    public boolean close(Dockable dockable) {
        DockbarManager mgr = DockbarManager.getCurrent(dockable);
        return mgr==null? false: mgr.remove(dockable);
    }

    public void preview(Dockable dockable, boolean locked) {
        DockbarManager.activate(dockable, true);
    }

    public void setMinimized(Dockable dockable, boolean minimizing, Component window, int edge) {
        DockbarManager mgr = DockbarManager.getInstance(window);
        if(mgr==null)
            return;

        // if minimizing, send to the dockbar
        if(minimizing) {
            if(edge==MinimizationManager.UNSPECIFIED_LAYOUT_CONSTRAINT)
                mgr.minimize(dockable);
            else
                mgr.minimize(dockable, edge);
        }
        // otherwise, restore from the dockbar
        else
            mgr.restore(dockable);
    }
}
