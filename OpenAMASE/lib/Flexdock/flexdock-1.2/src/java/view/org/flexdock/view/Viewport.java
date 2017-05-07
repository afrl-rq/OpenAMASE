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
package org.flexdock.view;

import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTabbedPane;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.activation.ActiveDockableListener;
import org.flexdock.docking.defaults.DefaultDockingPort;
import org.flexdock.docking.defaults.StandardBorderManager;

/**
 * @author Christopher Butler
 */
public class Viewport extends DefaultDockingPort implements DockingConstants {

    protected HashSet blockedRegions;

    static {
        DockingManager.setDockingStrategy(Viewport.class, View.VIEW_DOCKING_STRATEGY);
    }

    public Viewport() {
        super();
        blockedRegions = new HashSet(5);
        setBorderManager(new StandardBorderManager());
    }

    public Viewport(String portId) {
        super(portId);
        blockedRegions = new HashSet(5);
        setBorderManager(new StandardBorderManager());
    }

    public void setRegionBlocked(String region, boolean isBlocked) {
        if(isValidDockingRegion(region)) {
            if(isBlocked) {
                blockedRegions.add(region);
            } else {
                blockedRegions.remove(region);
            }
        }
    }

    public boolean isDockingAllowed(Component comp, String region) {
        // if we're already blocked, then no need to interrogate
        // the components in this dockingport
        boolean blocked = !super.isDockingAllowed(comp, region);
        if(blocked)
            return false;

        // check to see if the region itself has been blocked for some reason
        if(blockedRegions.contains(region))
            return false;

        // by default, allow docking in non-CENTER regions
        if(!CENTER_REGION.equals(region))
            return true;

        // allow docking in the CENTER if there's nothing already there,
        // or if there's no Dockable associated with the component there
        Dockable dockable = getCenterDockable();
        if(dockable==null)
            return true;

        // otherwise, only allow docking in the CENTER if the dockable
        // doesn't mind
        return !dockable.getDockingProperties().isTerritoryBlocked(region).booleanValue();
    }

    public boolean dock(Dockable dockable) {
        return dock(dockable, CENTER_REGION);
    }

    protected JTabbedPane createTabbedPane() {
        JTabbedPane pane = super.createTabbedPane();
        pane.addChangeListener(ActiveDockableListener.getInstance());
        return pane;
    }

    public Set getViewset() {
        // return ALL views, recursing to maximum depth
        return getDockableSet(-1, 0, View.class);
    }

    public Set getViewset(int depth) {
        // return all views, including subviews up to the specified depth
        return getDockableSet(depth, 0, View.class);
    }

    protected String paramString() {
        return "id=" + getPersistentId() + "," + super.paramString();
    }
}
