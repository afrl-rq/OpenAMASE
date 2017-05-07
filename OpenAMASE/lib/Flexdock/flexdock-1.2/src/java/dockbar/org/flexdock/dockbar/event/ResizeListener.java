// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 18, 2005
 */
package org.flexdock.dockbar.event;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.flexdock.dockbar.DockbarManager;
import org.flexdock.dockbar.ViewPane;
import org.flexdock.dockbar.layout.DockbarLayout;
import org.flexdock.dockbar.layout.DockbarLayoutManager;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.props.DockablePropertySet;
import org.flexdock.docking.state.MinimizationManager;
import org.flexdock.util.RootWindow;

/**
 * @author Christopher Butler
 */
public class ResizeListener extends MouseAdapter implements MouseMotionListener {
    private DockbarManager manager;
    private Dockable dockable;

    private JPanel dragGlassPane;
    private Component cachedGlassPane;
    private RootWindow rootWindow;

    public ResizeListener(DockbarManager mgr) {
        manager = mgr;
        dragGlassPane = new JPanel();
        dragGlassPane.setOpaque(false);
    }

    public void mouseMoved(MouseEvent e) {
        // noop
    }

    public void mousePressed(MouseEvent e) {
        dockable = manager.getActiveDockable();
        rootWindow = manager.getWindow();
        cachedGlassPane = rootWindow.getGlassPane();
        rootWindow.setGlassPane(dragGlassPane);
        dragGlassPane.setCursor(manager.getResizeCursor());
        dragGlassPane.setVisible(true);
        manager.setDragging(true);
    }

    public void mouseReleased(MouseEvent e) {
        dockable = null;
        dragGlassPane.setVisible(false);
        manager.setDragging(false);

        if(rootWindow!=null && cachedGlassPane!=null) {
            rootWindow.setGlassPane(cachedGlassPane);
            cachedGlassPane = null;
            rootWindow = null;
        }
    }

    public void mouseDragged(MouseEvent e) {
        if(dockable!=null)
            handleResizeEvent(e);
    }

    private void handleResizeEvent(MouseEvent me) {
        ViewPane viewPane = manager.getViewPane();
        Point p = SwingUtilities.convertPoint((Component)me.getSource(), me.getPoint(), viewPane.getParent());
        Rectangle viewArea = DockbarLayoutManager.getManager().getViewArea(manager, dockable);

        p.x = Math.max(p.x, 0);
        p.x = Math.min(p.x, viewArea.width);
        p.y = Math.max(p.y, 0);
        p.y = Math.min(p.y, viewArea.height);

        int orientation = manager.getActiveEdge();
        int loc = orientation==MinimizationManager.LEFT || orientation==MinimizationManager.RIGHT? p.x: p.y;
        int dim = orientation==MinimizationManager.LEFT || orientation==MinimizationManager.RIGHT? viewArea.width: viewArea.height;

        if(orientation==MinimizationManager.RIGHT || orientation==MinimizationManager.BOTTOM)
            loc = dim - loc;

        float percent = (float)loc/(float)dim;
        float minPercent = (float)DockbarLayout.MINIMUM_VIEW_SIZE/(float)dim;
        percent = Math.max(percent, minPercent);

        DockablePropertySet props = dockable.getDockingProperties();
        props.setPreviewSize(percent);
        manager.revalidate();
    }

}
