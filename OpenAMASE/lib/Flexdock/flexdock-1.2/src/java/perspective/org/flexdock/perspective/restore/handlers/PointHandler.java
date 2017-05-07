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
import java.awt.Container;
import java.awt.Point;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.state.DockingState;
import org.flexdock.perspective.RestorationManager;
import org.flexdock.util.NestedComponents;
import org.flexdock.util.RootWindow;

/**
 * Created on 2005-05-26
 *
 * @author <a href="mailto:marius@eleritec.net">Christopher Butler</a>
 * @version $Id: PointHandler.java,v 1.5 2005-06-20 23:55:48 marius Exp $
 */
public class PointHandler implements RestorationHandler, DockingConstants {

    public boolean restore(Dockable dockable, DockingState dockingState, Map context) {
        if(DockingManager.isDocked(dockable)) {
            return false;
        }

        Component owner = RestorationManager.getRestoreContainer(dockable);
        return restoreDockable(dockable, owner, dockingState);
    }

    private boolean restoreDockable(Dockable dockable, Component win, DockingState dockingState) {
        RootWindow window = RootWindow.getRootContainer(win);
        Container contentPane = window.getContentPane();

        Point dropPoint = getDropPoint(dockable, contentPane, dockingState);
        if(dropPoint==null)
            return false;

        Component deep = SwingUtilities.getDeepestComponentAt(contentPane, dropPoint.x, dropPoint.y);
        NestedComponents dropTargets = NestedComponents.find(deep, Dockable.class, DockingPort.class);

        DockingPort port = dropTargets==null? null: (DockingPort)dropTargets.parent;
        Point mousePoint = port==null? null: SwingUtilities.convertPoint(contentPane, dropPoint, (Component)port);
        String region = port==null? UNKNOWN_REGION: port.getRegion(mousePoint);

        return DockingManager.dock(dockable, port, region);
    }

    private Point getDropPoint(Dockable dockable, Container contentPane, DockingState dockingState) {
        if(!dockingState.hasCenterPoint())
            return null;

        float percentX = (float)dockingState.getCenterX()/100f;
        float percentY = (float)dockingState.getCenterY()/100f;

        Point dropPoint = new Point();
        dropPoint.x = Math.round((float)contentPane.getWidth() * percentX);
        dropPoint.y = Math.round((float)contentPane.getHeight() * percentY);

        return dropPoint;
    }

}
