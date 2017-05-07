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

import org.flexdock.docking.Dockable;
import org.flexdock.event.Event;

/**
 * @author Christopher Butler
 */
public class DockbarEvent extends Event {
    public static final int EXPANDED = 0;
    public static final int LOCKED = 1;
    public static final int COLLAPSED = 2;

    public static final int MINIMIZE_STARTED = 10;
    public static final int MINIMIZE_COMPLETED = 11;

    private int edge;
    private boolean consumed;

    public DockbarEvent(Dockable dockable, int type, int edge) {
        super(dockable, type);
        this.edge = edge;
    }

    public int getEdge() {
        return edge;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void consume() {
        consumed = true;
    }
}
