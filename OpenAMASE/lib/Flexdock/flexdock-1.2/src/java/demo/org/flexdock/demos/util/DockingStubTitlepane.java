// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Jul 7, 2005
 */
package org.flexdock.demos.util;

import java.awt.Component;

import org.flexdock.docking.DockingStub;

/**
 * @author Christopher Butler
 */
public class DockingStubTitlepane extends Titlepane implements DockingStub {
    private String dockingId;

    public DockingStubTitlepane(String id, String title) {
        super(title);
        dockingId = id;
    }

    public Component getDragSource() {
        return getTitlebar();
    }

    public Component getFrameDragSource() {
        return getTitlebar();
    }

    public String getPersistentId() {
        return dockingId;
    }

    public String getTabText() {
        return getTitle();
    }
}
