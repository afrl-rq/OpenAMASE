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
package org.flexdock.event;

import java.util.EventObject;

import org.flexdock.docking.DockingConstants;

/**
 * @author Christopher Butler
 */
@SuppressWarnings(value = { "serial" })
public class Event extends EventObject implements DockingConstants {
    private int eventType;

    /**
     * An event object.
     *
     * @param src
     *            the source of the event.
     * @param evtType
     *            the type of the event.
     */
    public Event(Object src, int evtType) {
        super(src);
        eventType = evtType;
    }

    public int getEventType() {
        return eventType;
    }
}
