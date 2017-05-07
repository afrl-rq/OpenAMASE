// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 27, 2005
 */
package org.flexdock.docking.event.hierarchy;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.util.RootWindow;

/**
 * @author Christopher Butler
 * @author Karl Schaefer
 */
public class DockingPortTracker implements HierarchyListener {
    private static final DockingPortTracker SINGLETON = new DockingPortTracker();
    private static WeakHashMap TRACKERS_BY_WINDOW = new WeakHashMap();
    private static WeakHashMap DOCKING_PORTS = new WeakHashMap();
    private static final Object NULL = new Object();


    public static HierarchyListener getInstance() {
        return SINGLETON;
    }

    public static void remove(Component c) {
        RootWindow window = RootWindow.getRootContainer(c);
        if (window != null) {
            synchronized(TRACKERS_BY_WINDOW) {
                TRACKERS_BY_WINDOW.remove(window);
            }
        }
    }

    public static RootDockingPortInfo getRootDockingPortInfo(Component c) {
        RootWindow window = RootWindow.getRootContainer(c);
        return getRootDockingPortInfo(window);
    }

    public static RootDockingPortInfo getRootDockingPortInfo(RootWindow window) {
        if(window==null)
            return null;

        RootDockingPortInfo info = (RootDockingPortInfo)TRACKERS_BY_WINDOW.get(window);
        if(info==null) {
            synchronized(TRACKERS_BY_WINDOW) {
                info = new RootDockingPortInfo(window);
                TRACKERS_BY_WINDOW.put(window, info);
            }
        }
        return info;
    }

    public static DockingPort findById(String portId) {
        if(portId==null)
            return null;

        synchronized(TRACKERS_BY_WINDOW) {
            for(Iterator it=TRACKERS_BY_WINDOW.values().iterator(); it.hasNext();) {
                RootDockingPortInfo info = (RootDockingPortInfo)it.next();
                DockingPort port = info.getPort(portId);
                if(port!=null)
                    return port;
            }
        }
        return null;
    }

    private static RootDockingPortInfo findInfoByPort(DockingPort port) {
        if(port==null)
            return null;

        synchronized(TRACKERS_BY_WINDOW) {
            for(Iterator it=TRACKERS_BY_WINDOW.values().iterator(); it.hasNext();) {
                RootDockingPortInfo info = (RootDockingPortInfo)it.next();
                if(info.contains(port))
                    return info;
            }
        }
        return null;
    }

    /**
     * Returns the {@code DockingPort} for {@code comp}. If {@code comp} is
     * {@code null}, then this method returns {@code null}.
     *
     * @param comp
     *            the component for which to find the root docking port.
     * @return the eldest docking port for {@code comp}, or {@code null} if
     *         {@code comp} is {@code null} or has no {@code DockingPort}
     *         ancestor.
     */
    public static DockingPort findByWindow(Component comp) {
        Component c = comp;
        DockingPort port = null;

        while (c != null) {
            if (c instanceof DockingPort) {
                port = (DockingPort) c;
            }

            c = c.getParent();
        }

        if (port == null) {
            port = findByWindow(RootWindow.getRootContainer(comp));
        }

        return port;
    }


    public static DockingPort findByWindow(RootWindow window) {
        RootDockingPortInfo info = getRootDockingPortInfo(window);
        if(info==null)
            return null;

        return info.getPort(0);
    }


    public static void updateIndex(DockingPort port) {
        if(port==null)
            return;

        synchronized(DOCKING_PORTS) {
            DOCKING_PORTS.put(port, NULL);
        }

        RootDockingPortInfo info = findInfoByPort(port);
        if(info!=null) {
            info.remove(port);
            info.add(port);
        }
    }



    private boolean isParentChange(HierarchyEvent evt) {
        if(evt.getID()!=HierarchyEvent.HIERARCHY_CHANGED || evt.getChangeFlags()!=HierarchyEvent.PARENT_CHANGED)
            return false;
        return true;
    }

    private boolean isRemoval(HierarchyEvent evt) {
        return evt.getChanged().getParent()==null;
    }


    public void hierarchyChanged(HierarchyEvent evt) {
        // only work with DockingPorts
        if(!(evt.getSource() instanceof DockingPort))
            return;

        // we don't want to work with sub-ports
        DockingPort port = (DockingPort)evt.getSource();
        if(!port.isRoot())
            return;

        // only work with parent-change events
        if(!isParentChange(evt))
            return;

        // root-ports are tracked by window.  if we can't find a parent window, then we
        // can track the dockingport.
        Container changedParent = evt.getChangedParent();
        RootWindow window = RootWindow.getRootContainer(changedParent);
        if(window==null)
            return;

        boolean removal = isRemoval(evt);
        if(removal)
            dockingPortRemoved(window, port);
        else
            dockingPortAdded(window, port);
    }

    public void dockingPortAdded(RootWindow window, DockingPort port) {
        RootDockingPortInfo info = getRootDockingPortInfo(window);
        if(info!=null)
            info.add(port);
    }

    public void dockingPortRemoved(RootWindow window, DockingPort port) {
        RootDockingPortInfo info = getRootDockingPortInfo(window);
        if(info!=null)
            info.remove(port);
    }

    public static Set getDockingWindows() {
        synchronized(TRACKERS_BY_WINDOW) {
            return new HashSet(TRACKERS_BY_WINDOW.keySet());
        }
    }

    public static Set getDockingPorts() {
        Set globalSet = new HashSet();
        synchronized(DOCKING_PORTS) {
            for(Iterator it=DOCKING_PORTS.keySet().iterator(); it.hasNext();) {
                Object obj = it.next();
                globalSet.add(obj);
            }
        }
        return globalSet;
    }

    public static Set getRootDockingPorts() {
        HashSet rootSet = new HashSet();
        Set globalSet = getDockingPorts();

        for(Iterator it=globalSet.iterator(); it.hasNext();) {
            DockingPort port = (DockingPort)it.next();
            if(port.isRoot())
                rootSet.add(port);
        }
        return rootSet;
    }

    public static DockingPort getRootDockingPort(Dockable dockable) {
        if(dockable==null || !DockingManager.isDocked(dockable))
            return null;

        DockingPort port = dockable.getDockingPort();
        Container parent = ((Component)port).getParent();
        while(!isWindowRoot(parent)) {
            if(parent instanceof DockingPort)
                port = (DockingPort)parent;
            parent = parent.getParent();
        }

        return port;
    }

    private static boolean isWindowRoot(Component comp) {
        return comp instanceof Window || comp instanceof Applet;
    }
}
