// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 16, 2005
 */
package org.flexdock.docking.props;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.flexdock.docking.Dockable;


/**
 * @author Christopher Butler
 */
@SuppressWarnings(value = { "serial" })
public class ScopedDockablePropertySet extends BasicDockablePropertySet implements ScopedMap {

    public static final RootDockablePropertySet ROOT_PROPS = new RootDockablePropertySet(null);
    public static final List DEFAULTS = new ArrayList(0);
    public static final List GLOBALS = new ArrayList(0);
    private ArrayList locals;

    public ScopedDockablePropertySet(Dockable dockable) {
        this(6, dockable);
        init();
    }

    public ScopedDockablePropertySet(int initialCapacity, Dockable dockable) {
        super(initialCapacity, dockable);
        init();
    }

    public ScopedDockablePropertySet(int initialCapacity, float loadFactor, Dockable dockable) {
        super(initialCapacity, loadFactor, dockable);
        init();
    }

    public ScopedDockablePropertySet(Map t, Dockable dockable) {
        super(t, dockable);
        init();
    }

    public List getLocals() {
        return locals;
    }

    public List getDefaults() {
        return DEFAULTS;
    }

    public List getGlobals() {
        return GLOBALS;
    }

    public Map getRoot() {
        return ROOT_PROPS;
    }

    public String getDockableDesc() {
        return (String)PropertyManager.getProperty(DESCRIPTION, this);
    }

    public Boolean isDockingEnabled() {
        return (Boolean)PropertyManager.getProperty(DOCKING_ENABLED, this);
    }

    public Boolean isActive() {
        return (Boolean)PropertyManager.getProperty(ACTIVE, this);
    }

    public Boolean isMouseMotionListenersBlockedWhileDragging() {
        return (Boolean)PropertyManager.getProperty(MOUSE_MOTION_DRAG_BLOCK, this);
    }

    public Float getRegionInset(String region) {
        String key = getRegionInsetKey(region);
        return key==null? null: (Float)PropertyManager.getProperty(key, this);
    }

    public Float getSiblingSize(String region) {
        String key = getSiblingSizeKey(region);
        return key==null? null: (Float)PropertyManager.getProperty(key, this);
    }

    public Boolean isTerritoryBlocked(String region) {
        String key = getTerritoryBlockedKey(region);
        return key==null? null: (Boolean)PropertyManager.getProperty(key, this);
    }

    public Float getDragThreshold() {
        return (Float)PropertyManager.getProperty(DRAG_THRESHOLD, this);
    }

    public Float getPreviewSize() {
        return (Float)PropertyManager.getProperty(PREVIEW_SIZE, this);
    }

    private void init() {
        locals = new ArrayList(1);
        locals.add(this);
    }

}
