// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 10, 2005
 */
package org.flexdock.docking.event;

/**
 * @author Christopher Butler
 */
public interface DockingMonitor {

    void addDockingListener(DockingListener listener);

    void removeDockingListener(DockingListener listener);

    DockingListener[] getDockingListeners();

}
