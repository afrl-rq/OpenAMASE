// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Aug 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.flexdock.dockbar.layout;

import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import org.flexdock.dockbar.DockbarManager;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.state.MinimizationManager;
import org.flexdock.util.RootWindow;

/**
 * @author Christopher Butler
 */
public class DockbarLayoutManager {
    private static final Object LOCK = new Object();
    private static final DockbarLayoutManager DEFAULT_INSTANCE = new DockbarLayoutManager();
    private static DockbarLayoutManager viewAreaManager = DEFAULT_INSTANCE;

    public static DockbarLayoutManager getManager() {
        synchronized(LOCK) {
            return viewAreaManager;
        }
    }

    public static void setManager(DockbarLayoutManager mgr) {
        synchronized(LOCK) {
            viewAreaManager = mgr==null? DEFAULT_INSTANCE: mgr;
        }
    }

    public Rectangle getViewArea(DockbarManager mgr, Dockable dockable) {
        if(mgr==null)
            return new Rectangle(0, 0, 0, 0);

        Rectangle leftBar = mgr.getLeftBar().getBounds();
        Rectangle bottomBar = mgr.getBottomBar().getBounds();
        Rectangle rightBar = mgr.getRightBar().getBounds();
        return new Rectangle(leftBar.x + leftBar.width, leftBar.y, bottomBar.width-leftBar.width-rightBar.width, leftBar.height);
    }

    public Rectangle getLayoutArea(DockbarManager mgr) {
        Rectangle rect = new Rectangle();
        RootWindow window = mgr==null? null: mgr.getWindow();
        if(window==null)
            return rect;

        JLayeredPane layeredPane = window.getLayeredPane();

        Component leftEdge = getEdgeGuide(mgr, MinimizationManager.LEFT);
        Component rightEdge = getEdgeGuide(mgr, MinimizationManager.RIGHT);
        Component bottomEdge = getEdgeGuide(mgr, MinimizationManager.BOTTOM);
        Component topEdge = getEdgeGuide(mgr, MinimizationManager.TOP);


        Rectangle leftBounds = SwingUtilities.convertRectangle(leftEdge.getParent(), leftEdge.getBounds(), layeredPane);
        Rectangle rightBounds = SwingUtilities.convertRectangle(rightEdge.getParent(), rightEdge.getBounds(), layeredPane);
        Rectangle bottomBounds = SwingUtilities.convertRectangle(bottomEdge.getParent(), bottomEdge.getBounds(), layeredPane);
        Rectangle topBounds = SwingUtilities.convertRectangle(topEdge.getParent(), topEdge.getBounds(), layeredPane);

        int rightX = rightBounds.x + rightBounds.width;
        int bottomY = bottomBounds.y + bottomBounds.height;

        //TODO: There is some a flaw we're not accounting for here.  We're assuming that
        // with the various different edge-guide components we're using, the leftEdge will
        // actually be to the left, rightEdge will actually be to the right, and so on.
        // If the user does something unreasonable like specify a rightEdge component that is
        // actually to the left of their leftEdge, then we're going to end up with some wacky,
        // unpredictable results.

        rect.x = leftBounds.x;
        rect.y = topBounds.y;
        rect.width = rightX - rect.x;
        rect.height = bottomY - rect.y;
        return rect;
    }

    public JComponent getEdgeGuide(DockbarManager mgr, int edge) {
        // default behavior is to return the contentPane for all edges
        RootWindow window = mgr==null? null: mgr.getWindow();
        Component comp = window==null? null: window.getContentPane();
        return comp instanceof JComponent? (JComponent)comp: null;
    }
}
