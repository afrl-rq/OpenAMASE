// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 18, 2005
 */
package org.flexdock.dockbar.event;

import java.util.EventListener;

/**
 * @author Christopher Butler
 */
public interface DockbarListener extends EventListener {

    //comment public is redundant in interfaces since all of the methods are by default public

    void dockableExpanded(DockbarEvent evt);
    void dockableLocked(DockbarEvent evt);
    void dockableCollapsed(DockbarEvent evt);

    void minimizeStarted(DockbarEvent evt);
    void minimizeCompleted(DockbarEvent evt);

    static class Stub implements DockbarListener {

        public void dockableExpanded(DockbarEvent evt) {}

        public void dockableLocked(DockbarEvent evt) {}

        public void dockableCollapsed(DockbarEvent evt) {}

        public void minimizeStarted(DockbarEvent evt) {}

        public void minimizeCompleted(DockbarEvent evt) {}

    }

}
