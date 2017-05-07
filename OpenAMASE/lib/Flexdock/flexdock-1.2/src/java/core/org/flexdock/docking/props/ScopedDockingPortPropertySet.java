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

import org.flexdock.docking.RegionChecker;

/**
 * @author Christopher Butler
 */
public class ScopedDockingPortPropertySet extends BasicDockingPortPropertySet implements ScopedMap {
    public static final RootDockingPortPropertySet ROOT_PROPS = new RootDockingPortPropertySet();
    public static final List DEFAULTS = new ArrayList(0);
    public static final List GLOBALS = new ArrayList(0);

    private ArrayList locals;

    public ScopedDockingPortPropertySet() {
        super();
        init();
    }

    public ScopedDockingPortPropertySet(int initialCapacity) {
        super(initialCapacity);
        init();
    }

    public ScopedDockingPortPropertySet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        init();
    }

    public ScopedDockingPortPropertySet(Map t) {
        super(t);
        init();
    }

    protected void init() {
        locals = new ArrayList(1);
        locals.add(this);
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

    public RegionChecker getRegionChecker() {
        return (RegionChecker)PropertyManager.getProperty(REGION_CHECKER, this);
    }

    public Float getRegionInset(String region) {
        String key = getRegionInsetKey(region);
        return key==null? null: (Float)PropertyManager.getProperty(key, this);
    }

    public Integer getTabPlacement() {
        return (Integer)PropertyManager.getProperty(TAB_PLACEMENT, this);
    }

    public Boolean isSingleTabsAllowed() {
        return (Boolean)PropertyManager.getProperty(SINGLE_TABS, this);
    }
}
