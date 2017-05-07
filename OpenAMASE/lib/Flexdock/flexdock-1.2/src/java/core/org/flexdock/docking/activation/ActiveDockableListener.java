// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 18, 2005
 */
package org.flexdock.docking.activation;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.util.DockingUtility;
import org.flexdock.util.SwingUtility;

/**
 * @author Christopher Butler
 */
public class ActiveDockableListener implements DockingConstants, PropertyChangeListener, ChangeListener, AWTEventListener {
    private static final ActiveDockableListener SINGLETON = new ActiveDockableListener();
    private static HashSet PROP_EVENTS = new HashSet();

    static {
        primeImpl();
    }

    public static void prime() {
    }

    private static void primeImpl() {
        PROP_EVENTS.add(PERMANENT_FOCUS_OWNER);
        PROP_EVENTS.add(ACTIVE_WINDOW);

        EventQueue.invokeLater(new Runnable() {
                public void run() {
                    KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                    focusManager.addPropertyChangeListener(SINGLETON);
                }
            });

        Toolkit.getDefaultToolkit().addAWTEventListener(SINGLETON, AWTEvent.MOUSE_EVENT_MASK);
    }

    public static ActiveDockableListener getInstance() {
        return SINGLETON;
    }

    private ActiveDockableListener() {
    }

    public void eventDispatched(AWTEvent event) {
        //catch all mousePressed events
        if(event.getID()!=MouseEvent.MOUSE_PRESSED)
            return;

        MouseEvent evt = (MouseEvent)event;

        if (evt.getSource() instanceof Component) {
            Component c = (Component) evt.getSource();

            // check to see if the event was targeted at the deepest component at the current
            // mouse loaction
            Container  container = c instanceof Container? (Container)c: null;
            if(container!=null && container.getComponentCount()>1) {
                // if not, find the deepest component
                Point p = evt.getPoint();
                c = SwingUtilities.getDeepestComponentAt(c, p.x, p.y);
            }

            // request activation of the dockable that encloses this component
            ActiveDockableTracker.requestDockableActivation(c);
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String pName = evt.getPropertyName();
        if(!PROP_EVENTS.contains(pName))
            return;

        Component oldVal = SwingUtility.toComponent(evt.getOldValue());
        Component newVal = SwingUtility.toComponent(evt.getNewValue());
        boolean switchTo = newVal!=null;

        if(ACTIVE_WINDOW.equals(pName))
            handleWindowChange(evt, oldVal, newVal, switchTo);
        else
            handleFocusChange(evt, oldVal, newVal, switchTo);
    }

    private void handleWindowChange(PropertyChangeEvent evt, Component oldVal, Component newVal, boolean activate) {
        // notify the ActiveDockableTracker of the window change
        ActiveDockableTracker.windowActivated(newVal);

        Component srcComponent = activate? newVal: oldVal;
        ActiveDockableTracker tracker = ActiveDockableTracker.getTracker(srcComponent);
        if(tracker!=null)
            tracker.setActive(activate);
    }

    private void handleFocusChange(PropertyChangeEvent evt, Component oldVal, Component newVal, boolean switchTo) {
        if(!switchTo)
            return;

        if(newVal instanceof JTabbedPane)
            newVal = ((JTabbedPane)newVal).getSelectedComponent();
        activateComponent(newVal);
    }

    private void activateComponent(Component c) {
        Dockable dockable = DockingUtility.getAncestorDockable(c);
        if(dockable==null)
            return;

        ActiveDockableTracker tracker = ActiveDockableTracker.getTracker(dockable.getComponent());
        if(tracker!=null) {
            tracker.setActive(dockable);
        }
    }


    public void stateChanged(ChangeEvent e) {
        Object obj = e.getSource();
        if(obj instanceof JTabbedPane) {
            JTabbedPane pane = (JTabbedPane)obj;
            Component c = pane.getSelectedComponent();
            Dockable dockable = DockingManager.getDockable(c);
            if(dockable!=null) {
                activateComponent(dockable.getComponent());
                udpateTabChangeFocus(dockable);
            }
        }
    }

    private void udpateTabChangeFocus(final Dockable dockable) {
        KeyboardFocusManager mgr = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        Dockable focusParent = DockingUtility.getAncestorDockable(mgr.getFocusOwner());
        if(focusParent==null || focusParent==dockable)
            return;

        // the current focusParent-dockable is different than the currently active dockable.
        // we'll need to update the focus component
        final Component comp = dockable.getComponent();
        final Component deep = SwingUtilities.getDeepestComponentAt(comp, comp.getWidth()/2, comp.getHeight()/2);
        // invokeLater because the new tab may not yet be showing, meaning the enumeration of its
        // focus-cycle will return empty.  the parent dockable in the new tab must be showing.
        EventQueue.invokeLater(new Runnable() {
                public void run() {
                    ActiveDockableTracker.focusDockable(deep, dockable, true);
                }
            });

    }

}
