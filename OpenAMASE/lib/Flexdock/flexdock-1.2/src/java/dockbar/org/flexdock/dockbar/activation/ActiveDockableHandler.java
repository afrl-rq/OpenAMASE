// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Aug 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.flexdock.dockbar.activation;

import org.flexdock.dockbar.event.DockbarEvent;
import org.flexdock.dockbar.event.DockbarListener;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.activation.ActiveDockableTracker;
import org.flexdock.util.DockingUtility;

/**
 * @author Christopher Butler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ActiveDockableHandler extends DockbarListener.Stub {

    public void dockableLocked(DockbarEvent evt) {
        Dockable d = (Dockable)evt.getSource();
        if(d!=null)
            ActiveDockableTracker.requestDockableActivation(d.getComponent());
    }

    public void minimizeStarted(DockbarEvent evt) {
        Dockable d = (Dockable)evt.getSource();
        // block minimization on floating views
        if(d!=null && DockingUtility.isFloating(d))
            evt.consume();
    }
}
