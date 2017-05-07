// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 2, 2005
 */
package org.flexdock.docking.event;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JTabbedPane;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.drag.DragManager;

/**
 * @author Christopher Butler
 */
public class TabbedDragListener extends MouseAdapter implements MouseMotionListener {

    private DragManager dragListener;

    public void mouseDragged(MouseEvent me) {
        if(dragListener!=null)
            dragListener.mouseDragged(me);
    }

    public void mouseReleased(MouseEvent me) {
        if(dragListener!=null)
            dragListener.mouseReleased(me);
        dragListener = null;
    }

    public void mousePressed(MouseEvent me) {
        if(!(me.getSource() instanceof JTabbedPane)) {
            dragListener = null;
            return;
        }

        JTabbedPane pane = (JTabbedPane)me.getSource();
        Point p = me.getPoint();
        int tabIndex = pane.indexAtLocation(p.x, p.y);
        if(tabIndex==-1) {
            dragListener = null;
            return;
        }

        Dockable dockable = DockingManager.getDockable(pane.getComponentAt(tabIndex));
        dragListener = DockingManager.getDragListener(dockable);
        if(dragListener!=null)
            dragListener.mousePressed(me);
    }

    public void mouseMoved(MouseEvent me) {
        // does nothing
    }

//  private void redispatchToDockable(MouseEvent me) {
////if(!tabsAsDragSource || dockable==null)
////return;
//
////Component dragSrc = dockable.getInitiator();
////MouseEvent evt = SwingUtilities.convertMouseEvent((Component)me.getSource(), me, dragSrc);
////dragSrc.dispatchEvent(evt);
//  }

}
