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
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;



import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.defaults.DefaultDockingPort;
import org.flexdock.docking.drag.effects.EffectsManager;
import org.flexdock.docking.drag.effects.RubberBand;
import org.flexdock.util.RootWindow;
import org.flexdock.util.SwingUtility;
import org.flexdock.util.Utilities;

public class DragPipeline {

    private GlassPaneMonitor paneMonitor;
    private RootWindow[] windows;
    private HashMap rootWindowsByBounds;
    private DragGlasspane currentGlasspane;
    private DragGlasspane newGlassPane;
    private Rectangle[] windowBounds;
    private boolean heavyweightDockableSupportted;

    private boolean open;
    private DragOperation dragToken;
    private RubberBand rubberBand;

    public DragPipeline() {
        paneMonitor = new GlassPaneMonitor();
        rubberBand = EffectsManager.getRubberBand();
    }

    public boolean isOpen() {
        return open;
    }

    public void open(DragOperation token) {
        if(token==null)
            throw new NullPointerException("'token' parameter cannot be null.");

        if(EventQueue.isDispatchThread()) {
            openImpl(token);
            return;
        }


        final DragOperation dToken = token;
        try {
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    openImpl(dToken);
                }
            });
        } catch(Exception e) {
            System.err.println("Exception: " +e.getMessage());
        }
    }

    private void openImpl(DragOperation operation) {
        // check to see if we're going to support heavyweight dockables for this operation
        heavyweightDockableSupportted = Boolean.getBoolean(DockingConstants.HEAVYWEIGHT_DOCKABLES);

        this.dragToken = operation;

        // turn the current drag operation on
        setCurrentDragOperation(operation);

        windows = RootWindow.getVisibleWindows();

        windowBounds = new Rectangle[windows.length];
        rootWindowsByBounds = new HashMap();

        for(int i=0; i<windows.length; i++) {
            applyGlassPane(windows[i], createGlassPane());
            windowBounds[i] = windows[i].getBounds();
            rootWindowsByBounds.put(windowBounds[i], windows[i]);
        }

        // kill the rubberband if floating is not allowed
        if(!DragManager.isFloatingAllowed(operation.getDockableReference()))
            rubberBand = null;

        operation.start();
        open = true;
    }

    private DragGlasspane createGlassPane() {
        DragGlasspane pane = new DragGlasspane();
        pane.addMouseListener(paneMonitor);
        return pane;
    }

    private void applyGlassPane(RootWindow win, DragGlasspane pane) {
        pane.setRootWindow(win);
        pane.setCachedGlassPane(win.getGlassPane());
        win.setGlassPane(pane);
        pane.setVisible(true);
    }



    public void close() {
        if(!open)
            return;

        clearRubberBand();
        for(int i=0; i<windows.length; i++) {
            Component cmp = windows[i].getGlassPane();
            if(cmp instanceof DragGlasspane) {
                DragGlasspane pane = (DragGlasspane)cmp;
                pane.setVisible(false);
                cmp = pane.getCachedGlassPane();
//				pane.dispose();
                windows[i].setGlassPane(cmp);
                windows[i] = null;
            }
        }

        windowBounds = null;
        rootWindowsByBounds.clear();
        // turn the current drag operation off
        setCurrentDragOperation(null);
        open = false;
    }

    public void processDragEvent(MouseEvent me) {
        if(!open)
            return;

        if(EventQueue.isDispatchThread()) {
            processDragEventImpl(me);
            return;
        }

        final MouseEvent evt = me;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                processDragEventImpl(evt);
            }
        });
    }

    private void processDragEventImpl(MouseEvent me) {
        dragToken.updateMouse(me);

        if(heavyweightDockableSupportted)
            preprocessHeavyweightDockables();

        me.consume();

        // hide the rubber band
        clearRubberBand();

        // track whether or not we're currently over a window
        dragToken.setOverWindow(newGlassPane!=null);

        // if the glasspane hasn't changed, then reprocess on the current glasspane
        if(newGlassPane==currentGlasspane) {
            dontSwitchGlassPanes();
            return;
        }

        // process transitions from a glasspane to a null area
        if(newGlassPane==null) {
            transitionToNullArea();
            return;
        }

        // process transitions from null area to a glasspane
        if(currentGlasspane==null) {
            transitionFromNullArea(newGlassPane);
            return;
        }

        // otherwise, transition from one glasspane to another
        // clear out the old glasspane
        currentGlasspane.clear();
        // reassign to the new glasspane
        currentGlasspane = newGlassPane;
        // now process the new glasspane and redraw the rubberband
        Rectangle screenRect = dragToken.getDragRect(true);
        currentGlasspane.setPostPainter(getPostPainter(screenRect));
        currentGlasspane.processDragEvent(dragToken);
    }

    private void dontSwitchGlassPanes() {
        // just redraw the rubberband if there's no current glasspane
        Rectangle screenRect = dragToken.getDragRect(true);
        if(currentGlasspane==null) {
            drawRubberBand(screenRect);
            return;
        }


        // otherwise, process the drag event on the current glasspane
        // and repaint it.
        // TODO: Fix post-painter on unchanged glasspane.
//		currentGlasspane.setPostPainter(getPostPainter(screenRect));
        currentGlasspane.setPostPainter(null);
        currentGlasspane.processDragEvent(dragToken);
    }

    private void transitionToNullArea() {
        // set the new glasspane reference
        DragGlasspane pane = currentGlasspane;
        currentGlasspane = null;

        // clear out the old glasspane and redraw the rubberband
        Rectangle screenRect = dragToken.getDragRect(true);
        pane.setPostPainter(null);
        pane.clear();
    }

    private void transitionFromNullArea(DragGlasspane newGlassPane) {
        // set the new glasspane reference
        currentGlasspane = newGlassPane;

        // process the new glasspane
        Rectangle screenRect = dragToken.getDragRect(true);
        currentGlasspane.setPostPainter(null);
        currentGlasspane.processDragEvent(dragToken);
    }


    private void setCurrentGlassPane(DragGlasspane gp) {
        newGlassPane = gp;
    }


    private Runnable getPostPainter(final Rectangle rect) {
//		if(!ResourceManager.isWindowsPlatform())
//			return null;

        return new Runnable() {
            public void run() {
                deferRubberBandDrawing(rect);
//				drawRubberBand(rect);
            }
        };
    }
    private void deferRubberBandDrawing(final Rectangle rect) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                drawRubberBand(rect);
            }
        });
    }

    private void drawRubberBand(Rectangle rect) {
        paintRubberBand(rect);
    }


    private class GlassPaneMonitor extends MouseAdapter {
        public void mouseEntered(MouseEvent me) {
            Object obj = me.getSource();
            if(obj instanceof DragGlasspane) {
                setCurrentGlassPane((DragGlasspane)obj);
            }
        }

        public void mouseExited(MouseEvent me) {
            setCurrentGlassPane(null);
        }
    }

    public DragOperation getDragToken() {
        return dragToken;
    }

    private void clearRubberBand() {
        if(rubberBand!=null)
            rubberBand.clear();
    }

    private void paintRubberBand(Rectangle rect) {
        if(rubberBand!=null)
            rubberBand.paint(rect);
    }

    private void setCurrentDragOperation(DragOperation operation) {
        DragOperation current = DragManager.getCurrentDragOperation();
        if(operation==current)
            return;

        DockingPort srcPort = operation==null? current.getSourcePort(): operation.getSourcePort();
        DragManager.setCurrentDragOperation(operation);
        if(srcPort instanceof Component)
            SwingUtility.repaint((Component)srcPort);

        // TODO: We want to get rid of this code in the future.  I don't like
        // having a public setDragInProgress() method on the default docking port
        // and having to know to call it at this level.  If the default docking port
        // is interested in whether a drag is currently in progress, it should
        // register some type of listener and handle its personal business internally
        // with its own code.
        if(srcPort instanceof DefaultDockingPort) {
            DefaultDockingPort port = (DefaultDockingPort)srcPort;
            port.setDragInProgress(operation!=null);
        }
    }

    private void preprocessHeavyweightDockables() {
        RootWindow targetWindow = getTargetWindow();

        if(newGlassPane==null && targetWindow!=null) {
            Component gp = targetWindow.getGlassPane();
            if(gp instanceof DragGlasspane) {
                setCurrentGlassPane((DragGlasspane)gp);
            }
        }
    }

    private RootWindow getTargetWindow() {
        Point screenLoc = dragToken.getCurrentMouse(true);
        for(int i=0; i<windowBounds.length; i++) {
            if(windowBounds[i].contains(screenLoc))
                return (RootWindow)rootWindowsByBounds.get(windowBounds[i]);
        }
        return null;
    }


}