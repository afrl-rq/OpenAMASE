// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 14, 2005
 */
package org.flexdock.dockbar;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.WeakHashMap;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import org.flexdock.dockbar.activation.ActivationQueue;
import org.flexdock.dockbar.activation.ActiveDockableHandler;
import org.flexdock.dockbar.activation.Animation;
import org.flexdock.dockbar.event.ActivationListener;
import org.flexdock.dockbar.event.DockablePropertyChangeHandler;
import org.flexdock.dockbar.event.DockbarEvent;
import org.flexdock.dockbar.event.DockbarEventHandler;
import org.flexdock.dockbar.event.DockbarListener;
import org.flexdock.dockbar.event.DockbarTracker;
import org.flexdock.dockbar.layout.DockbarLayout;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.floating.frames.DockingFrame;
import org.flexdock.docking.props.PropertyChangeListenerFactory;
import org.flexdock.docking.state.DockingPath;
import org.flexdock.docking.state.DockingState;
import org.flexdock.docking.state.MinimizationManager;
import org.flexdock.event.EventManager;
import org.flexdock.perspective.RestorationManager;
import org.flexdock.util.RootWindow;
import org.flexdock.util.Utilities;

/**
 * @author Christopher Butler
 * @author Bobby Rosenberger
 * @author Mateusz Szczap
 */
public class DockbarManager {
    private static final WeakHashMap MANAGERS_BY_WINDOW = new WeakHashMap();
    public static final Integer DOCKBAR_LAYER = new Integer(JLayeredPane.PALETTE_LAYER.intValue()-5);
    public static final int DEFAULT_EDGE = MinimizationManager.LEFT;

    private static String dockbarManagerClassName;

    private static DockbarManager currentManager;

    protected WeakReference windowRef;
    protected Dockbar leftBar;
    protected Dockbar rightBar;
    protected Dockbar bottomBar;
    protected ViewPane viewPane;

    protected DockbarLayout dockbarLayout;
    private ActivationListener activationListener;
    private HashMap dockables;

    private int activeEdge = MinimizationManager.UNSPECIFIED_LAYOUT_CONSTRAINT;
    private String activeDockableId;
    private boolean animating;
    private boolean dragging;


    static {
        Class c = DockingManager.class;
        EventManager.addHandler(new DockbarEventHandler());
        DockbarTracker.register();

        // setup to listen for Dockable property change events
        PropertyChangeListenerFactory.addFactory(new DockablePropertyChangeHandler.Factory());

        // update behavior of active Dockable changes
        EventManager.addListener(new ActiveDockableHandler());
    }

    public static DockbarManager getInstance(Component c) {
        RootWindow window = RootWindow.getRootContainer(c);
        return getInstance(window);
    }

    public static DockbarManager getInstance(RootWindow window) {
        if(window==null)
            return null;

        // DockingFrames should not be allowed to contain dockbars.
        // This may change in the future, but for now if our window is a
        // DockingFrame, reroute to its owner.
        Component root = window.getRootContainer();
        if(root instanceof DockingFrame) {
            root = ((DockingFrame)root).getOwner();
            return getInstance(root);
        }

        DockbarManager mgr = (DockbarManager)MANAGERS_BY_WINDOW.get(window);
        if(mgr==null) {
            mgr = createDockbarManager(window);
            synchronized(MANAGERS_BY_WINDOW) {
                MANAGERS_BY_WINDOW.put(window, mgr);
            }
            mgr.install();
        }

        if(currentManager==null)
            currentManager = mgr;

        return mgr;
    }

    /**
     * Creates a new DockbarManager instance. In the case that a dockbarManager class name
     * has been set the class will be instantiated by reflection. If no classname is set a
     * org.flexdock.dockbar.DockbarManager will be created.
     *
     * @param window RootWindow for which the DockbarManager will be created
     * @return new DockbarManager instance
     * @see DockbarManager#setDockbarManager(String)
     */
    private static DockbarManager createDockbarManager(RootWindow window) {
        if (dockbarManagerClassName == null)
            return new DockbarManager(window);

        DockbarManager mgr = null;
        try {
            Class clazz = Class.forName(dockbarManagerClassName);
            Constructor constructor = clazz.getConstructor(new Class[] {RootWindow.class});
            mgr = (DockbarManager)constructor.newInstance(new Object[] {window});
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return mgr;
    }

    /**
     * Sets a custom DockbarManager class which will be used to create new DockbarManager
     * instances.
     *
     * @param className  Classname of your custom DockbarManager.
     */
    public static void setDockbarManager(String className) {
        dockbarManagerClassName = className;
    }

    public static DockbarManager getCurrent(Dockable dockable) {
        if(dockable==null)
            return null;

        synchronized(MANAGERS_BY_WINDOW) {
            for(Iterator it=MANAGERS_BY_WINDOW.values().iterator(); it.hasNext();) {
                DockbarManager mgr = (DockbarManager)it.next();
                if(mgr.isOwner(dockable))
                    return mgr;
            }
        }
        return null;
    }

    public static void windowChanged(Component newWindow) {
        currentManager = getInstance(newWindow);
    }

    public static DockbarManager getCurrent() {
        return currentManager;
    }

    public static void addListener(DockbarListener listener) {
        EventManager.addListener(listener);
    }

    public static void activate(String dockableId, boolean locked) {
        Dockable dockable = DockingManager.getDockable(dockableId);
        activate(dockable, locked);
    }

    public static void activate(Dockable dockable, boolean locked) {
        if(dockable==null)
            return;

        DockbarManager mgr = getCurrent(dockable);
        if(mgr==null || !mgr.contains(dockable))
            return;

        mgr.setActiveDockable(dockable);
        if(locked)
            mgr.getActivationListener().lockViewpane();
    }



















    protected DockbarManager(RootWindow window) {
        dockbarLayout = new DockbarLayout(this);
        activationListener = new ActivationListener(this);

        leftBar = new Dockbar(this, MinimizationManager.LEFT);
        rightBar = new Dockbar(this, MinimizationManager.RIGHT);
        bottomBar = new StatusDockbar(this, MinimizationManager.BOTTOM);
        viewPane = new ViewPane(this);

        windowRef = new WeakReference(window);
        dockables = new HashMap();
    }

    public RootWindow getWindow() {
        return (RootWindow)windowRef.get();
    }


    protected void install() {
        RootWindow window = getWindow();
        if(window==null)
            return;

        JLayeredPane layerPane = window.getLayeredPane();
        boolean changed = install(leftBar, layerPane);
        changed = install(rightBar, layerPane) || changed;
        changed = install(bottomBar, layerPane) || changed;
        changed = install(viewPane, layerPane) || changed;

        if(changed) {
            layerPane.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent evt) {
                    if(evt.getSource() instanceof JLayeredPane)
                        revalidate();
                }
            });
        }
        revalidate();
    }

    private boolean install(Component c, JLayeredPane layerPane) {
        if(c.getParent()!=layerPane) {
            if(c.getParent()!=null)
                c.getParent().remove(c);
            layerPane.add(c, DOCKBAR_LAYER);
            return true;
        }
        return false;
    }







    public Dockbar getBottomBar() {
        return bottomBar;
    }

    public Dockbar getLeftBar() {
        return leftBar;
    }

    public Dockbar getRightBar() {
        return rightBar;
    }

    public ViewPane getViewPane() {
        return viewPane;
    }





    public void revalidate() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                validate();
            }
        });

    }

    public void validate() {
        toggleDockbars();
        dockbarLayout.layout();
        viewPane.revalidate();
    }



    private void toggleDockbars() {
        leftBar.setVisible(leftBar.getComponentCount()!=0);
        rightBar.setVisible(rightBar.getComponentCount()!=0);
        bottomBar.setVisible(bottomBar.getComponentCount()!=0);
    }




    private int findDockbarEdge(Dockable dockable) {
        RootWindow window = RootWindow.getRootContainer(dockable.getComponent());
        if(window==null)
            return DEFAULT_EDGE;

        // get the dockable component and it's containing content pane
        Component cmp = dockable.getComponent();
        Container contentPane = window.getContentPane();

        // get the bounds of the content pane and dockable, translating the dockable into the
        // content pane's axes
        Rectangle contentRect = new Rectangle(0, 0, contentPane.getWidth(), contentPane.getHeight());
        Rectangle dockRect = SwingUtilities.convertRectangle(cmp.getParent(), cmp.getBounds(), contentPane);

        // get the center of the dockable
        Point dockCenter = new Point(dockRect.x + (dockRect.width/2), dockRect.y + (dockRect.height/2));
        // get the center left, right, and bottom points
        Point leftCenter = new Point(0, contentRect.height/2);
        Point bottomCenter = new Point(contentRect.width/2, contentRect.height);
        Point rightCenter = new Point(contentRect.width, contentRect.height/2);

        // calculate the absolute distance from dockable center to each of the edge
        // center points.  whichever is the shortest, that is the edge the dockable is
        // 'closest' to and that will be the edge we'll return
        double min = Math.abs(dockCenter.distance(leftCenter));
        int edge = MinimizationManager.LEFT;
        double delta = Math.abs(dockCenter.distance(rightCenter));
        if(delta<min) {
            min = delta;
            edge = MinimizationManager.RIGHT;
        }
        delta = Math.abs(dockCenter.distance(bottomCenter));
        if(delta<min) {
            min = delta;
            edge = MinimizationManager.BOTTOM;
        }

        return edge;
    }

    public int getEdge(String dockableId) {
        Dockable dockable = DockingManager.getDockable(dockableId);
        return getEdge(dockable);
    }

    public int getEdge(Dockable dockable) {
        Dockbar dockbar = getDockbar(dockable);

        if(dockbar==leftBar)
            return MinimizationManager.LEFT;
        if(dockbar==rightBar)
            return MinimizationManager.RIGHT;
        if(dockbar==bottomBar)
            return MinimizationManager.BOTTOM;
        return MinimizationManager.UNSPECIFIED_LAYOUT_CONSTRAINT;
    }

    public Dockbar getDockbar(Dockable dockable) {
        if(dockable==null)
            return null;

        if(leftBar.contains(dockable))
            return leftBar;
        if(rightBar.contains(dockable))
            return rightBar;
        if(bottomBar.contains(dockable))
            return bottomBar;
        return null;
    }

    public Dockbar getDockbar(int edge) {
        edge = Dockbar.getValidOrientation(edge);
        switch(edge) {
        case MinimizationManager.RIGHT:
            return rightBar;
        case MinimizationManager.BOTTOM:
            return bottomBar;
        default:
            return leftBar;
        }
    }






    public void minimize(Dockable dockable) {
        if(dockable==null)
            return;

        int edge = DEFAULT_EDGE;
        RootWindow window = getWindow();
        if(window!=null && DockingManager.isDocked(dockable)) {
            edge = findDockbarEdge(dockable);
        }

        minimize(dockable, edge);
    }

    public void minimize(Dockable dockable, int edge) {
        if(dockable==null)
            return;

        if(isDockingCancelled(dockable, edge))
            return;

        // install the dockable
        edge = Dockbar.getValidOrientation(edge);
        install(dockable, edge);

        // store the dockable id
        dockables.put(dockable.getPersistentId(), new Integer(edge));

        // send event notification
        DockbarEvent evt = new DockbarEvent(dockable, DockbarEvent.MINIMIZE_COMPLETED, edge);
        EventManager.dispatch(evt);
    }

    public void reAdd(Dockable dockable) {
        // can't re-add if the dockable is null, or we already contain it
        if(dockable==null || contains(dockable))
            return;

        Integer edge = (Integer)dockables.get(dockable.getPersistentId());
        if(edge!=null)
            install(dockable, edge.intValue());
    }

    private void install(Dockable dockable, int edge) {
        Dockbar dockbar = getDockbar(edge);

        // undock the dockable
        DockingManager.undock(dockable);
        // place in the dockbar
        dockbar.dock(dockable);
        // make sure they can't drag the dockable while it's in the dockbar
        dockable.getDockingProperties().setDockingEnabled(false);
        // indicate that the dockable is minimized
        DockingState info = DockingManager.getLayoutManager().getDockingState(dockable);
        info.setMinimizedConstraint(edge);
        revalidate();
    }

    private boolean isDockingCancelled(Dockable dockable, int edge) {
        DockbarEvent evt = new DockbarEvent(dockable, DockbarEvent.MINIMIZE_STARTED, edge);
        EventManager.dispatch(evt);
        return evt.isConsumed();
    }


    public void restore(final Dockable dockable) {
        if(dockable == null) {
            return;
        }

        // now restore to the current layout
        final DockingState dockingState = DockingManager.getDockingState(dockable);
        final DockingPath dockingPath = dockingState.getPath();
        boolean restoreResult = false;

        if (dockingPath != null) {
            restoreResult = dockingPath.restore(dockable);
        } else {
            restoreResult = RestorationManager.getInstance().restore(dockable);
        }

        if (restoreResult) {
            // remove the dockable from the dockbar
            remove(dockable);
            // remove the dockable reference
            dockables.remove(dockable.getPersistentId());
        }
    }

    public boolean remove(Dockable dockable) {
        if(dockable==null)
            return false;

        if(getActiveDockable()==dockable)
            setActiveDockable((Dockable)null);

        Dockbar dockbar = getDockbar(dockable);
        if(dockbar==null)
            return false;

        dockbar.undock(dockable);
        // restore drag capability to the dockable after removing
        // from the dockbar
        dockable.getDockingProperties().setDockingEnabled(true);
        revalidate();
        return true;
    }


    public int getActiveEdge() {
        synchronized(this) {
            return activeEdge;
        }
    }

    private void setActiveEdge(int edge) {
        synchronized(this) {
            activeEdge = edge;
        }
    }

    private Dockbar getActiveDockbar() {
        int edge = getActiveEdge();
        switch(edge) {
        case MinimizationManager.TOP:
            return bottomBar;
        case MinimizationManager.RIGHT:
            return rightBar;
        default:
            return leftBar;
        }
    }

    public String getActiveDockableId() {
        synchronized(this) {
            return activeDockableId;
        }
    }

    private void setActiveDockableId(String id) {
        synchronized(this) {
            activeDockableId = id;
        }
    }

    public Dockable getActiveDockable() {
        String dockingId = getActiveDockableId();
        Dockable dockable = DockingManager.getDockable(dockingId);
        return dockable;
    }


    public Cursor getResizeCursor() {
        return viewPane.getResizeCursor();
    }

    public boolean isActive() {
        return getActiveDockable()!=null;
    }

    public void setActiveDockable(String dockableId) {
        Dockable dockable = DockingManager.getDockable(dockableId);
        setActiveDockable(dockable);
    }

    public void setActiveDockable(Dockable dockable) {
        // if we're not currently docked to any particular edge, then
        // we cannot activate the specified dockable.  instead, set the
        // active dockable to null
        final int newEdge = getEdge(dockable);
        if(newEdge==MinimizationManager.UNSPECIFIED_LAYOUT_CONSTRAINT)
            dockable = null;

        // check for dockable changes
        Dockable oldDockable = getActiveDockable();
        final String newDockableId = dockable==null? null: dockable.getPersistentId();
        String currentlyActiveId = getActiveDockableId();
        boolean changed = Utilities.isChanged(currentlyActiveId, newDockableId);
        // check for edge changes
        changed = changed || newEdge!=getActiveEdge();


        // if nothing has changed, then we're done
        if(changed) {
            viewPane.setLocked(false);
            setActiveEdge(newEdge);
            setActiveDockableId(newDockableId);
            startAnimation(oldDockable, dockable, newDockableId, newEdge);
        }
    }

    private void dispatchEvent(Dockable oldDockable, Dockable newDockable) {
        // dispatch to event listeners
        int evtType = DockbarEvent.EXPANDED;
        if(newDockable==null && oldDockable!=null) {
            newDockable = oldDockable;
            evtType = DockbarEvent.COLLAPSED;
        }

        if(newDockable!=null) {
            DockbarEvent evt = new DockbarEvent(newDockable, evtType, getActiveEdge());
            EventManager.dispatch(evt);
        }
    }

    private void startAnimation(final Dockable oldDockable, final Dockable newDockable, final String newDockableId, final int newEdge) {
        Animation deactivation = oldDockable==null? null: new Animation(this, true);
        Runnable updater1 = new Runnable() {
            public void run() {
                setActiveEdge(newEdge);
                setActiveDockableId(newDockableId);
                viewPane.updateOrientation();
                viewPane.updateContents();
            }
        };
        Animation activation = newDockableId==null? null: new Animation(this, false);
        Runnable updater2 = new Runnable() {
            public void run() {
                viewPane.setPrefSize(ViewPane.UNSPECIFIED_PREFERRED_SIZE);
                viewPane.updateOrientation();
                viewPane.updateContents();
                revalidate();

                // dispatch event notification
                dispatchEvent(oldDockable, newDockable);
            }
        };

        ActivationQueue queue = new ActivationQueue(this, deactivation, updater1, activation, updater2);
        queue.start();
    }

    public int getPreferredViewpaneSize() {
        return dockbarLayout.getDesiredViewpaneSize();
    }


    public boolean isAnimating() {
        return animating;
    }

    public void setAnimating(boolean animating) {
        this.animating = animating;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public ActivationListener getActivationListener() {
        return activationListener;
    }

    public boolean contains(Dockable dockable) {
        return getDockbar(dockable)!=null;
    }

    private boolean isOwner(Dockable dockable) {
        return dockable==null? false: dockables.containsKey(dockable.getPersistentId());
    }

    public DockbarLayout getLayout() {
        return dockbarLayout;
    }

}
