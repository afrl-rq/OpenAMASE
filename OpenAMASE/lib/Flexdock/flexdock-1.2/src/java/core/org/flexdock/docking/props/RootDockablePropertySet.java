// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 22, 2005
 */
package org.flexdock.docking.props;

import java.util.HashSet;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.RegionChecker;

/**
 * @author Christopher Butler
 */
@SuppressWarnings(value = { "serial" })
public class RootDockablePropertySet extends BasicDockablePropertySet {
    private static final Float DEFAULT_REGION_INSETS = new Float(RegionChecker.DEFAULT_REGION_SIZE);
    private static final Float DEFAULT_SIBLING_INSETS = new Float(DockingManager.getDefaultSiblingSize());
    public static final Float DEFAULT_DRAG_THRESHOLD = new Float(4);
    public static final Float DEFAULT_PREVIEW_SIZE = new Float(0.3);

    private HashSet constraints;

    public RootDockablePropertySet(Dockable dockable) {
        super(5, dockable);
        constraints = new HashSet(5);

        constrain(DESCRIPTION, "null");
        constrain(DOCKING_ENABLED, Boolean.TRUE);
        constrain(MOUSE_MOTION_DRAG_BLOCK, Boolean.TRUE);
        constrain(ACTIVE, Boolean.FALSE);

        constrain(REGION_SIZE_NORTH, DEFAULT_REGION_INSETS);
        constrain(REGION_SIZE_SOUTH, DEFAULT_REGION_INSETS);
        constrain(REGION_SIZE_EAST, DEFAULT_REGION_INSETS);
        constrain(REGION_SIZE_WEST, DEFAULT_REGION_INSETS);

        constrain(SIBLING_SIZE_NORTH, DEFAULT_SIBLING_INSETS);
        constrain(SIBLING_SIZE_SOUTH, DEFAULT_SIBLING_INSETS);
        constrain(SIBLING_SIZE_EAST, DEFAULT_SIBLING_INSETS);
        constrain(SIBLING_SIZE_WEST, DEFAULT_SIBLING_INSETS);

        constrain(TERRITORY_BLOCKED_NORTH, Boolean.FALSE);
        constrain(TERRITORY_BLOCKED_SOUTH, Boolean.FALSE);
        constrain(TERRITORY_BLOCKED_EAST, Boolean.FALSE);
        constrain(TERRITORY_BLOCKED_WEST, Boolean.FALSE);
        constrain(TERRITORY_BLOCKED_CENTER, Boolean.FALSE);

        constrain(DRAG_THRESHOLD, DEFAULT_DRAG_THRESHOLD);
        constrain(PREVIEW_SIZE, DEFAULT_PREVIEW_SIZE);
    }

    public void constrain(Object key, Object value) {
        if(key!=null && value!=null) {
            put(key, value);
            constraints.add(key);
        }
    }


    public synchronized Object remove(Object key) {
        return constraints.contains(key)? null: super.remove(key);
    }
}
