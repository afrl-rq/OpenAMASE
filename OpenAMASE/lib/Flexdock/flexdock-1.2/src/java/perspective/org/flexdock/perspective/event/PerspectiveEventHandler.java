// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on May 14, 2005
 */
package org.flexdock.perspective.event;

import java.util.EventListener;



import org.flexdock.event.Event;
import org.flexdock.event.EventHandler;

/**
 * @author Christopher Butler
 */
public class PerspectiveEventHandler extends EventHandler {

    private static final PerspectiveEventHandler SINGLETON = new PerspectiveEventHandler();

    public static PerspectiveEventHandler getInstance() {
        return SINGLETON;
    }

    private PerspectiveEventHandler() {

    }

    public boolean acceptsEvent(Event evt) {
        return evt instanceof PerspectiveEvent;
    }

    public boolean acceptsListener(EventListener listener) {
        return listener instanceof PerspectiveListener;
    }

    public void handleEvent(Event evt, EventListener listener, int eventType) {
        PerspectiveEvent event = (PerspectiveEvent)evt;
        PerspectiveListener consumer = (PerspectiveListener)listener;
        switch(eventType) {
        case PerspectiveEvent.CHANGED:
            consumer.perspectiveChanged(event);
            break;
        case PerspectiveEvent.RESET:
            consumer.perspectiveReset(event);
            break;
        default:
            break;
        }
    }

    public PerspectiveListener[] getListeners() {
        synchronized(globalListeners) {
            return (PerspectiveListener[])globalListeners.toArray(new PerspectiveListener[0]);
        }
    }
}
