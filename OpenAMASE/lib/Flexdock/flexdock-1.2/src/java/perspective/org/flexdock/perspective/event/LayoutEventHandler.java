// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on May 18, 2005
 */
package org.flexdock.perspective.event;

import java.util.EventListener;

import org.flexdock.event.Event;
import org.flexdock.event.EventHandler;
import org.flexdock.perspective.Layout;

/**
 * @author Christopher Butler
 */
public class LayoutEventHandler extends EventHandler {

    public boolean acceptsEvent(Event evt) {
        return evt instanceof LayoutEvent;
    }
    public boolean acceptsListener(EventListener listener) {
        return listener instanceof LayoutListener;
    }

    public void handleEvent(Event evt, EventListener listener, int eventType) {
        LayoutEvent event = (LayoutEvent)evt;
        LayoutListener consumer = (LayoutListener)listener;

        switch(eventType) {
        case LayoutEvent.DOCKABLE_HIDDEN:
            consumer.dockableHidden(event);
            break;
        case LayoutEvent.DOCKABLE_RESTORED:
            consumer.dockableDisplayed(event);
            break;
        case LayoutEvent.LAYOUT_APPLIED:
            consumer.layoutApplied(event);
            break;
        case LayoutEvent.LAYOUT_EMPTIED:
            consumer.layoutEmptied(event);
            break;
        }
    }

    public EventListener[] getListeners(Object eventTarget) {
        return eventTarget instanceof Layout?
               ((Layout)eventTarget).getListeners(): null;
    }
}
