// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Copyright (c) 2005 FlexDock Development Team. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE.
 */
package org.flexdock.perspective.restore.handlers;

import java.awt.Component;
import java.util.Map;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.state.DockingState;
import org.flexdock.perspective.PerspectiveManager;

/**
 * Created on 2005-06-03
 *
 * @author <a href="mailto:marius@eleritec.net">Christopher Butler</a>
 * @version $Id: UnknownStateHandler.java,v 1.4 2005-06-15 16:03:46 marius Exp $
 */
public class UnknownStateHandler implements RestorationHandler, DockingConstants {

    private static final String[] REGIONS = {
        CENTER_REGION, WEST_REGION, EAST_REGION, SOUTH_REGION, NORTH_REGION
    };

    public boolean restore(Dockable dockable, DockingState info, Map context) {
        DockingPort port = PerspectiveManager.getMainDockingPort();
        if(port==null)
            return false;

        Component comp = port.getDockedComponent();
        if(comp==null)
            return dock(dockable, port);

        DockingInfo dockingInfo = getDeepestWest(port);
        if(dockingInfo.dockable==null)
            return dock(dockable, dockingInfo.port);
        return dock(dockable, dockingInfo.dockable);
    }

    private boolean dock(Dockable dockable, DockingPort port) {
        return dock(dockable, null, port);
    }

    private boolean dock(Dockable dockable, Dockable parent) {
        return dock(dockable, parent, null);
    }

    private boolean dock(Dockable dockable, Dockable parent, DockingPort port) {
        boolean ret = false;
        for(int i=0; i<REGIONS.length; i++) {
            if(parent==null) {
                ret = DockingManager.dock(dockable, port, REGIONS[i]);
            } else {
                ret = DockingManager.dock(dockable, parent, REGIONS[i]);
            }
            if(ret)
                return true;
        }
        return false;
    }

    private DockingInfo getDeepestWest(DockingPort port) {
        Component comp = port.getDockedComponent();
        if(comp instanceof JTabbedPane) {
            Dockable d = port.getDockable(CENTER_REGION);
            return new DockingInfo(d, port);
        }

        if(comp instanceof JSplitPane) {
            comp = ((JSplitPane)comp).getLeftComponent();
            if(comp instanceof DockingPort)
                return getDeepestWest((DockingPort)comp);
        }

        return new DockingInfo(DockingManager.getDockable(comp), port);
    }

    private static class DockingInfo {
        private Dockable dockable;
        private DockingPort port;

        private DockingInfo(Dockable d, DockingPort p) {
            dockable = d;
            port = p;
        }
    }
}
