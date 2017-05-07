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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.EventListener;

import javax.swing.SwingUtilities;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;

public class DragOperation implements DockingConstants {
    public static final String DRAG_IMAGE = "DragOperation.DRAG_IMAGE";

    private Component dragSource;
    private Component dockable;
    private DockingPort parentDockingPort;
    private Point mouseOffset;
    private Point currentMouse;
    private EventListener[] cachedListeners;
    private DragManager dragListener;
    private DockingPort targetPort;
    private String targetRegion;
    private boolean overWindow;
    private boolean pseudoDrag;
    private long started;
    private Dockable dockableRef;
    private DockingPort sourcePort;


    public DragOperation(Component dockable, Point dragOrigin, MouseEvent evt) {
        if(dockable==null)
            throw new NullPointerException("'dockable' parameter cannot be null.");
        if(evt==null)
            throw new NullPointerException("'evt' parameter cannot be null.");
        if(!(evt.getSource() instanceof Component))
            throw new IllegalArgumentException("'evt.getSource()' must be an instance of java.awt.Component.");

        if(dragOrigin==null)
            dragOrigin = evt.getPoint();
        init(dockable, (Component)evt.getSource(), dragOrigin, false);
    }

    public DragOperation(Component dockable, Component dragSource, Point currentMouse) {
        init(dockable, dragSource, currentMouse, true);
    }

    private void init(Component dockable, Component dragSource, Point currentMouse, boolean fakeDrag) {
        this.dockable = dockable;
        this.dragSource = dragSource;
        this.currentMouse = currentMouse;
        mouseOffset = calculateMouseOffset(currentMouse);
        pseudoDrag = fakeDrag;
        if(!fakeDrag)
            parentDockingPort = (DockingPort)SwingUtilities.getAncestorOfClass(DockingPort.class, dockable);

        sourcePort = DockingManager.getDockingPort(dockable);
        started = -1;
    }

    private Point calculateMouseOffset(Point evtPoint) {
        if(evtPoint==null)
            return null;

        Point dockableLoc = dockable.getLocationOnScreen();
        SwingUtilities.convertPointToScreen(evtPoint, dragSource);
        Point offset = new Point();
        offset.x = dockableLoc.x - evtPoint.x;
        offset.y = dockableLoc.y - evtPoint.y;
        return offset;
    }

    public Component getDockable() {
        return dockable;
    }

    public Dockable getDockableReference() {
        if(dockableRef==null)
            dockableRef = DockingManager.getDockable(dockable);
        return dockableRef;
    }

    public Point getMouseOffset() {
        return (Point)mouseOffset.clone();
    }

    public void updateMouse(MouseEvent me) {
        if(me!=null && me.getSource()==dragSource)
            currentMouse = me.getPoint();
    }

    public Point getCurrentMouse() {
        return getCurrentMouse(false);
    }

    public Point getCurrentMouse(boolean relativeToScreen) {
        Point p = (Point)currentMouse.clone();
        if(relativeToScreen)
            SwingUtilities.convertPointToScreen(p, dragSource);
        return p;
    }

    public Rectangle getDragRect(boolean relativeToScreen) {
        Point p = getCurrentMouse(relativeToScreen);
        Point offset = getMouseOffset();
        p.x += offset.x;
        p.y += offset.y;

        Rectangle r = new Rectangle(getDragSize());
        r.setLocation(p);
        return r;

    }

    public Point getCurrentMouse(Component target) {
        if(target==null || !target.isVisible())
            return null;
        return SwingUtilities.convertPoint(dragSource, currentMouse, target);
    }

    public Dimension getDragSize() {
        return ((Component)dockable).getSize();
    }

    public Component getDragSource() {
        return dragSource;
    }

    public void setTarget(DockingPort port, String region) {
        targetPort = port;
        targetRegion = region==null? UNKNOWN_REGION: region;
    }

    public DockingPort getTargetPort() {
        return targetPort;
    }

    public String getTargetRegion() {
        return targetRegion;
    }

    public EventListener[] getCachedListeners() {
        return cachedListeners==null? new EventListener[0]: cachedListeners;
    }

    public void setCachedListeners(EventListener[] listeners) {
        cachedListeners = listeners;
    }

    public DragManager getDragListener() {
        return dragListener;
    }

    public void setDragListener(DragManager listener) {
        this.dragListener = listener;
    }

    public boolean isOverWindow() {
        return overWindow;
    }

    public void setOverWindow(boolean overWindow) {
        this.overWindow = overWindow;
    }

    public boolean isPseudoDrag() {
        return pseudoDrag;
    }

    public DockingPort getParentDockingPort() {
        return parentDockingPort;
    }

    public void start() {
        if(started==-1)
            started = System.currentTimeMillis();
    }

    public long getStartTime() {
        return started;
    }

    public DockingPort getSourcePort() {
        return sourcePort;
    }
}