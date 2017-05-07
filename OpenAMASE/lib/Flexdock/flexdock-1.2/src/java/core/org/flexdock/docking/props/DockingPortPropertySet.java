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

import org.flexdock.docking.RegionChecker;

/**
 * @author Christopher Butler
 */
public interface DockingPortPropertySet {
    public static final String REGION_CHECKER = "DockingPort.REGION_CHECKER";
    public static final String SINGLE_TABS = "DockingPort.SINGLE_TABS";
    public static final String TAB_PLACEMENT = "DockingPort.TAB_PLACEMENT";

    public static final String REGION_SIZE_NORTH = "DockingPort.REGION_SIZE_NORTH";
    public static final String REGION_SIZE_SOUTH = "DockingPort.REGION_SIZE_SOUTH";
    public static final String REGION_SIZE_EAST = "DockingPort.REGION_SIZE_EAST";
    public static final String REGION_SIZE_WEST = "DockingPort.REGION_SIZE_WEST";

    public RegionChecker getRegionChecker();

    public Boolean isSingleTabsAllowed();

    public Integer getTabPlacement();

    public Float getRegionInset(String region);

    public void setRegionChecker(RegionChecker checker);

    public void setSingleTabsAllowed(boolean allowed);

    public void setTabPlacement(int placement);

    public void setRegionInset(String region, float inset);

}
