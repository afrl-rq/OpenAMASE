// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on May 3, 2005
 */
package org.flexdock.docking.floating.frames;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import org.flexdock.util.SwingUtility;

/**
 * @author Christopher Butler
 */
public class FrameDragListener implements MouseListener, MouseMotionListener {
    private Point dragOffset;
    private DockingFrame frame;
    private boolean enabled;

    public FrameDragListener(DockingFrame frame) {
        this.frame = frame;
    }

    public void mouseMoved(MouseEvent e) {
        // noop
    }

    public void mousePressed(MouseEvent e) {
        dragOffset = e.getPoint();
        Component c = (Component)e.getSource();
        if(c!=frame)
            dragOffset = SwingUtilities.convertPoint(c, dragOffset, frame);
    }

    public void mouseDragged(MouseEvent e) {
        if(enabled) {
            Point loc = e.getPoint();
            SwingUtilities.convertPointToScreen(loc, (Component)e.getSource());
            SwingUtility.subtract(loc, dragOffset);
            frame.setLocation(loc);
        }
    }

    public void mouseClicked(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }

    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
