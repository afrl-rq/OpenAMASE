// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 4, 2005
 */
package org.flexdock.docking.activation;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JTabbedPane;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.util.DockingUtility;
import org.flexdock.util.RootWindow;
import org.flexdock.util.SwingUtility;


/**
 * @author Christopher Butler
 */
public class ActiveDockableTracker {
    public static final String CURRENT_DOCKABLE = "ActiveDockableTracker.CURRENT_DOCKABLE";
    private static final String KEY = "ActiveDockableTracker.KEY";
//    private static final ActiveDockableTracker GLOBAL_TRACKER = new ActiveDockableTracker();
    private static ActiveDockableTracker currentTracker;
    private static final Object LOCK = new Object();
    private Dockable currentDockable;
    private PropertyChangeSupport changeSupport;


    static {
        initialize();
    }


    private static void initialize() {
        // make sure DockingManager has been fully initialized
        Class c = DockingManager.class;
    }


    public static ActiveDockableTracker getTracker(Component component) {
        RootWindow window = RootWindow.getRootContainer(component);
        return getTracker(window);
    }

    public static ActiveDockableTracker getCurrentTracker() {
        synchronized(LOCK) {
            if(currentTracker==null) {
                Window window = SwingUtility.getActiveWindow();
                if(window!=null) {
                    currentTracker = getTracker(window);
                }
            }
            return currentTracker;
        }
    }

    private static ActiveDockableTracker getTracker(RootWindow window) {
        if(window==null)
            return null;

        ActiveDockableTracker tracker = (ActiveDockableTracker)window.getClientProperty(KEY);

        if(tracker==null) {
            tracker = new ActiveDockableTracker();
            window.putClientProperty(KEY, tracker);
        }
        return tracker;
    }



    public static Dockable getActiveDockable() {
        ActiveDockableTracker tracker = getCurrentTracker();
        return tracker==null? null: tracker.currentDockable;
    }

    public static Dockable getActiveDockable(Component window) {
        ActiveDockableTracker tracker = getTracker(window);
        return tracker==null? null: tracker.currentDockable;
    }



    static void windowActivated(Component c) {
        RootWindow window = RootWindow.getRootContainer(c);
        ActiveDockableTracker tracker = getTracker(window);
        synchronized(LOCK) {
            currentTracker = tracker;
        }
    }

    public static void requestDockableActivation(Component c) {
        requestDockableActivation(c, false);
    }

    public static void requestDockableActivation(Component c, boolean forceChange) {
        if(c==null)
            return;

        Dockable dockable = DockingUtility.getAncestorDockable(c);
        if(dockable!=null) {
            requestDockableActivation(c, dockable, forceChange);
        }
    }

    public static void requestDockableActivation(final Component c, final Dockable dockable, final boolean forceChange) {
        if(c==null || dockable==null)
            return;

        // make sure the window is currently active
        SwingUtility.activateWindow(c);
	focusDockable(c, dockable, forceChange);
    }

    static void focusDockable(Component child, final Dockable parentDockable, boolean forceChange) {
        // if the dockable is already active, then leave it alone.
        // skip this check if they're trying to force a change
        if(!forceChange && parentDockable.getDockingProperties().isActive().booleanValue())
            return;

        Component parentComp = parentDockable.getComponent();
        Container focusRoot = parentComp instanceof Container? (Container)parentComp: null;
        Component focuser = focusRoot==null? null: SwingUtility.getNearestFocusableComponent(child, focusRoot);
        if(focuser==null)
            focuser = parentComp;

        /*
          requestDockableActivation is called when one clicks in the window (cf ActiveDockableListener.eventDispatched)
          and if there is a focusable component where the click occured, it gets the focus with the following
          requestFocus() (and it is called in an invokeLater).
          If an other Dockable had the focus before, the current Dockable must changed. But this changement is notify by a
          PropertyChangeEvent, so requestDockableActivation is called again (cf DockablePropertyChangeHandler.handleActivationChange).
          and the Dockable would request the focus !
          Conclusion: when the user click in the window, we have two concurrent threads which request focus for two differents
          components !

          Since forceChange is true only if called from the PropertyChangeHandler, the focus will be request only when
          forceChange is false, that avoids the two concurrent requestFocus().
        */

        if (!forceChange) {
	    final Component c = focuser;
	    c.addFocusListener(new FocusAdapter() {
		    public void focusGained(FocusEvent e) {
			if(!DockingUtility.isActive(parentDockable)) {
			    parentDockable.getDockingProperties().setActive(true);
			}
			c.removeFocusListener(this);
		    }
		});                         
	    c.requestFocusInWindow();
        }

        // if we're in a hidden tab, then bring the tab to the front
        if(parentComp.getParent() instanceof JTabbedPane) {
            JTabbedPane tabPane = (JTabbedPane)parentComp.getParent();
            int indx = tabPane.indexOfComponent(parentComp);
            if(indx!=tabPane.getSelectedIndex())
                tabPane.setSelectedIndex(indx);
        }
    }

    public ActiveDockableTracker() {
        changeSupport = new PropertyChangeSupport(this);
    }

    public void setActive(boolean b) {
        if (currentDockable == null || DockingManager.getDockable(currentDockable.getComponent()) == null) {
            return;
	}
        currentDockable.getDockingProperties().setActive(b);
    }

    public void setActive(Dockable dockable) {
        if (dockable != currentDockable) {
            Dockable oldValue = currentDockable;
            setActive(false);
            currentDockable = dockable;
            setActive(true);
            changeSupport.firePropertyChange(CURRENT_DOCKABLE, oldValue, dockable);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return changeSupport.getPropertyChangeListeners();
    }

    public boolean containsPropertyChangeListener(PropertyChangeListener listener) {
        PropertyChangeListener[] listeners = getPropertyChangeListeners();
        for(int i=0; i<listeners.length; i++) {
            if(listeners[i]==listener)
                return true;
        }
        return false;
    }

}
