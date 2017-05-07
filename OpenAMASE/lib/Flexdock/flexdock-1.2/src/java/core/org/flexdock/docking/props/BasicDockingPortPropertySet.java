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

import java.util.Map;

import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.RegionChecker;
import org.flexdock.util.TypedHashtable;

/**
 * @author Christopher Butler
 */
@SuppressWarnings(value = { "serial" })
public class BasicDockingPortPropertySet extends TypedHashtable implements DockingPortPropertySet, DockingConstants {

    public static String getRegionInsetKey(String region) {
        if(NORTH_REGION.equals(region))
            return REGION_SIZE_NORTH;
        if(SOUTH_REGION.equals(region))
            return REGION_SIZE_SOUTH;
        if(EAST_REGION.equals(region))
            return REGION_SIZE_EAST;
        if(WEST_REGION.equals(region))
            return REGION_SIZE_WEST;
        return null;
    }

    public BasicDockingPortPropertySet() {
        super();
    }

    public BasicDockingPortPropertySet(int initialCapacity) {
        super(initialCapacity);
    }

    public BasicDockingPortPropertySet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public BasicDockingPortPropertySet(Map t) {
        super(t);
    }







    public RegionChecker getRegionChecker() {
        return (RegionChecker)get(REGION_CHECKER);
    }

    public Boolean isSingleTabsAllowed() {
        return getBoolean(SINGLE_TABS);
    }

    public Integer getTabPlacement() {
        return getInt(TAB_PLACEMENT);
    }

    public Float getRegionInset(String region) {
        String key = getRegionInsetKey(region);
        return key==null? null: (Float)get(key);
    }


    public void setRegionChecker(RegionChecker checker) {
        put(REGION_CHECKER, checker);
    }

    public void setSingleTabsAllowed(boolean allowed) {
        put(SINGLE_TABS, allowed);
    }

    public void setTabPlacement(int placement) {
        put(TAB_PLACEMENT, placement);
    }

    public void setRegionInset(String region, float inset) {
        String key = getRegionInsetKey(region);
        if(key!=null) {
            put(key, new Float(inset));
        }
    }
}
