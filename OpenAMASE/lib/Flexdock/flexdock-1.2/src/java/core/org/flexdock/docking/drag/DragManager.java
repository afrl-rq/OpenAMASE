// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 14, 2005
 */
package org.flexdock.docking.drag;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.DockingStrategy;
import org.flexdock.docking.drag.effects.EffectsManager;
import org.flexdock.docking.event.DockingEvent;
import org.flexdock.docking.floating.policy.FloatPolicyManager;
import org.flexdock.event.EventManager;
import org.flexdock.util.DockingUtility;

/**
 * @author Christopher Butler
 *
 */
public class DragManager extends MouseAdapter implements MouseMotionListener {
    private static final String DRAG_CONTEXT = "DragManager.DRAG_CONTEXT";
    private static final Object LOCK = new Object();
    private static DragOperation currentDragOperation;

    private Dockable dockable;
    private DragPipeline pipeline;
    private boolean enabled;
    private Point dragOrigin;
    private HashMap dragContext;

    public static void prime() {
        // execute static initializer to preload resources
        EffectsManager.prime();
    }

    public DragManager(Dockable dockable) {
        this.dockable = dockable;
    }

    public void mousePressed(MouseEvent e) {
        if(dockable==null || dockable.getDockingProperties().isDockingEnabled()==Boolean.FALSE)
            enabled = false;
        else {
            toggleDragContext(true);
            enabled = !isDragCanceled(dockable, e);
        }
    }

    public void mouseDragged(MouseEvent evt) {
        if(!enabled)
            return;

        if(dragOrigin==null)
            dragOrigin = evt.getPoint();

        if(pipeline==null || !pipeline.isOpen()) {
            if(passedDragThreshold(evt))
                openPipeline(evt);
            else
                evt.consume();
        } else
            pipeline.processDragEvent(evt);
    }

    private boolean passedDragThreshold(MouseEvent evt) {
        double distance = dragOrigin.distance(evt.getPoint());
        float threshold = dockable.getDockingProperties().getDragThreshold().floatValue();
        return distance > threshold;
    }

    private void openPipeline(MouseEvent evt) {
        DragOperation token = new DragOperation(dockable.getComponent(), dragOrigin, evt);
        token.setDragListener(this);
        // initialize listeners on the drag-source
        initializeListenerCaching(token);

        DragPipeline pipeline = new DragPipeline();
        this.pipeline = pipeline;
        pipeline.open(token);
    }

    public void mouseMoved(MouseEvent e) {
        // doesn't do anything
    }

    public void mouseReleased(MouseEvent e) {
        if(pipeline==null || dockable.getDockingProperties().isDockingEnabled()==Boolean.FALSE)
            return;

        finishDrag(dockable, pipeline.getDragToken(), e);
        if(pipeline!=null)
            pipeline.close();
        toggleDragContext(false);
        dragOrigin = null;
        pipeline = null;
    }


    protected void finishDrag(Dockable dockable, DragOperation token, MouseEvent mouseEvt) {
        DockingStrategy docker = DockingManager.getDockingStrategy(dockable);
        DockingPort currentPort = DockingUtility.getParentDockingPort(dockable);
        DockingPort targetPort = token.getTargetPort();
        String region = token.getTargetRegion();

        // remove the listeners from the drag-source and all the old ones back in
        restoreCachedListeners(token);

        // issue a DockingEvent to allow any listeners the chance to cancel the operation.
        DockingEvent evt = new DockingEvent(dockable, currentPort, targetPort, DockingEvent.DROP_STARTED, mouseEvt, getDragContext());
        evt.setRegion(region);
        evt.setOverWindow(token.isOverWindow());
//		EventManager.notifyDockingMonitor(dockable, evt);
        EventManager.dispatch(evt, dockable);


        // attempt to complete the docking operation
        if(!evt.isConsumed())
            docker.dock(dockable, targetPort, region, token);
    }



















    private static void initializeListenerCaching(DragOperation token) {
        // it's easier for us if we remove the MouseMostionListener associated with the dragSource
        // before dragging, so normally we'll try to do that.  However, if developers really want to
        // keep them in there, then they can implement the Dockable interface for their dragSource and
        // let mouseMotionListenersBlockedWhileDragging() return false
//		if (!dockableImpl.mouseMotionListenersBlockedWhileDragging())
//			return;

        Component dragSrc = token.getDragSource();
        EventListener[] cachedListeners = dragSrc.getListeners(MouseMotionListener.class);
        token.setCachedListeners(cachedListeners);
        DragManager dragListener = token.getDragListener();

        // remove all of the MouseMotionListeners
        for (int i = 0; i < cachedListeners.length; i++) {
            dragSrc.removeMouseMotionListener((MouseMotionListener) cachedListeners[i]);
        }
        // then, re-add the DragManager
        if(dragListener!=null)
            dragSrc.addMouseMotionListener(dragListener);
    }

    private static void restoreCachedListeners(DragOperation token) {
        Component dragSrc = token.getDragSource();
        EventListener[] cachedListeners = token.getCachedListeners();
        DragManager dragListener = token.getDragListener();

        // remove the pipeline listener
        if(dragListener!=null)
            dragSrc.removeMouseMotionListener(dragListener);

        // now, re-add all of the original MouseMotionListeners
        for (int i = 0; i < cachedListeners.length; i++)
            dragSrc.addMouseMotionListener((MouseMotionListener) cachedListeners[i]);
    }

    private static boolean isDragCanceled(Dockable dockable, MouseEvent trigger) {
        DockingPort port = DockingUtility.getParentDockingPort(dockable);
        Map dragContext = getDragContext(dockable);
        DockingEvent evt = new DockingEvent(dockable, port, null, DockingEvent.DRAG_STARTED, trigger, dragContext);
        EventManager.dispatch(evt, dockable);
        return evt.isConsumed();
    }

    public static Map getDragContext(Dockable dockable) {
        Object obj = dockable==null? null: dockable.getClientProperty(DRAG_CONTEXT);
        return obj instanceof Map? (Map)obj: null;
    }

    private void toggleDragContext(boolean add) {
        if(add) {
            if(dragContext==null) {
                dragContext = new HashMap();
                dockable.putClientProperty(DRAG_CONTEXT, dragContext);
            }
        } else {
            if(dragContext!=null) {
                dragContext.clear();
                dragContext = null;
            }
            dockable.putClientProperty(DRAG_CONTEXT, null);
        }
    }

    private Map getDragContext() {
        return getDragContext(dockable);
    }

    public static boolean isFloatingAllowed(Dockable dockable) {
        return FloatPolicyManager.isFloatingAllowed(dockable);
    }

    public static DragOperation getCurrentDragOperation() {
        synchronized(LOCK) {
            return currentDragOperation;
        }
    }

    static void setCurrentDragOperation(DragOperation operation) {
        synchronized(LOCK) {
            currentDragOperation = operation;
        }
    }


}
