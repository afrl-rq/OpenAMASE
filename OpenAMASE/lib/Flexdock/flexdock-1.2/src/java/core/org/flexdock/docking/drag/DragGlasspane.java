// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.docking.drag;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.drag.effects.DragPreview;
import org.flexdock.docking.drag.effects.EffectsManager;
import org.flexdock.util.NestedComponents;
import org.flexdock.util.RootWindow;

@SuppressWarnings(value = { "serial" })
public class DragGlasspane extends JComponent implements DockingConstants {

    private NestedComponents currentDropTargets;
    private Component cachedGlassPane;
    private RootWindow rootWindow;
    private Runnable postPainter;
    private DragPreview previewDelegate;
    private boolean previewInit;
    private Polygon previewPoly;
    private DragOperation currentDragToken;

    public DragGlasspane() {
        setLayout(null);
    }

    public Component getCachedGlassPane() {
        return cachedGlassPane;
    }

    public RootWindow getRootWindow() {
        return rootWindow;
    }

    public void setCachedGlassPane(Component cachedGlassPane) {
        this.cachedGlassPane = cachedGlassPane;
    }

    public void setRootWindow(RootWindow rootWindow) {
        this.rootWindow = rootWindow;
    }

    private NestedComponents getDropTargets(DragOperation token) {
        Container c = rootWindow.getContentPane();
        Point currMouse = token.getCurrentMouse(c);
        Component deep = SwingUtilities.getDeepestComponentAt(c, currMouse.x, currMouse.y);
        return NestedComponents.find(deep, Dockable.class, DockingPort.class);
    }

    public void processDragEvent(DragOperation token) {
        currentDragToken = token;
        NestedComponents dropTargets = getDropTargets(token);

        // if there is no cover, and we're not transitioning away from one,
        // then invoke postPaint() and return
        if(currentDropTargets==null && dropTargets==null) {
            deferPostPaint();
            return;
        }

        String region = null;

        // don't immediately redraw the rubberband when switching covers
        // or regions
        setPostPainter(null);

        // now, assign the currentCover to the new one and repaint
        currentDropTargets = dropTargets;
        DockingPort port = dropTargets==null? null: (DockingPort)dropTargets.parent;
        // this is the dockable we're currently hovered over, not the one
        // being dragged
        Dockable hover = getHoverDockable(dropTargets);

        Point mousePoint = token.getCurrentMouse((Component)port);
        region = findRegion(port, hover, mousePoint);
        // set the target dockable
        token.setTarget(port, region);

        // create the preview-polygon
        createPreviewPolygon(token, port, hover,  region);

        // repaint
        repaint();
    }

    private String findRegion(DockingPort hoverPort, Dockable hoverDockable, Point mousePoint) {
        if(hoverPort==null)
            return UNKNOWN_REGION;

        if(hoverDockable!=null)
            return hoverPort.getRegion(mousePoint);

        // apparently, we're not hovered over a valid dockable.  either the dockingport
        // is empty, or it already contains a non-dockable component.  if it's empty, then
        // we can dock into it.  otherwise, we need to short-circuit the docking operation.
        Component docked = hoverPort.getDockedComponent();
        // if 'docked' is null, then the port is empty and we can dock
        if(docked==null)
            return hoverPort.getRegion(mousePoint);

        // the port contains a non-dockable component.  we can't dock
        return UNKNOWN_REGION;
    }

    private Dockable getHoverDockable(NestedComponents nest) {
        Component c = nest==null? null: nest.child;
        if(c instanceof Dockable)
            return (Dockable)c;
        return DockingManager.getDockable(c);
    }

    protected void createPreviewPolygon(DragOperation token, DockingPort port, Dockable hover, String region) {
        DragPreview preview = getPreviewDelegate(token.getDockable(), port);
        if(preview==null)
            previewPoly = null;
        else {
            Map dragContext = getDragContext(token);
            previewPoly = preview.createPreviewPolygon(token.getDockable(), port, hover, region, this, dragContext);
        }
    }

    public void clear() {
        if(currentDropTargets!=null) {
            currentDropTargets = null;
        }
        repaint();
    }

    public void paint(Graphics g) {
        paintComponentImpl(g);
        postPaint(g);
    }

    protected void postPaint(Graphics g) {
        if(postPainter!=null)
            postPainter.run();
        postPainter = null;
    }

    protected DragPreview getPreviewDelegate(Component dockable, DockingPort port) {
        if(!previewInit) {
            Dockable d = DockingManager.getDockable(dockable);
            previewDelegate = EffectsManager.getPreview(d, port);
            previewInit = true;
        }
        return previewDelegate;
    }


    private void deferPostPaint() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                postPaint(getGraphics());
            }
        });
    }


    void setPostPainter(Runnable painter) {
        postPainter = painter;
    }

    protected void paintComponentImpl(Graphics g) {
        if(currentDragToken!=null && previewDelegate!=null && previewPoly!=null) {
            Dockable dockable = currentDragToken.getDockableReference();
            Map dragInfo = getDragContext(currentDragToken);
            previewDelegate.drawPreview((Graphics2D)g, previewPoly, dockable, dragInfo);
        }
    }

//	private boolean match(Object o1, Object o2) {
//		if(o1==o2)
//			return true;
//		return o1==null? false: o1.equals(o2);
//	}

    private Map getDragContext(DragOperation token) {
        if(token==null)
            return null;

        Dockable dockable = token.getDockableReference();
        return DragManager.getDragContext(dockable);
    }

}
