// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on May 17, 2005
 */
package org.flexdock.perspective.event;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.event.Event;
import org.flexdock.perspective.Layout;

/**
 * @author Christopher Butler
 */
public class LayoutEvent extends Event {
    public static final int LAYOUT_APPLIED = 0;
    public static final int LAYOUT_EMPTIED = 1;
    public static final int DOCKABLE_HIDDEN = 2;
    public static final int DOCKABLE_RESTORED = 3;

    private Layout oldLayout;
    private Dockable dockable;

    public LayoutEvent(Layout layout, Layout oldLayout, String dockableId, int evtType) {
        super(layout, evtType);
        this.oldLayout = oldLayout;
        dockable = DockingManager.getDockable(dockableId);
    }

    public Layout getOldLayout() {
        return oldLayout;
    }

    public Dockable getDockable() {
        return dockable;
    }
}
