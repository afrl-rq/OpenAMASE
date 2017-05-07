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

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;

/**
 * @author Christopher Butler
 */
public abstract class EventHandler {
    protected ArrayList globalListeners = new ArrayList();

    /**
     * Tests a given event to determine if this handler can handle that event.
     *
     * @param evt
     *            the event to test.
     * @return {@code true} if this handler handles the event, {@code false}
     *         otherwise.
     */
    public abstract boolean acceptsEvent(Event evt);

    public abstract boolean acceptsListener(EventListener listener);

    public abstract void handleEvent(Event evt, EventListener listener,
                                     int eventType);

    public void addListener(EventListener listener) {
        synchronized (globalListeners) {
            if (listener != null)
                globalListeners.add(listener);
        }
    }

    public void removeListener(EventListener listener) {
        synchronized (globalListeners) {
            if (listener != null)
                globalListeners.remove(listener);
        }
    }

    /**
     * This method handles all of the events. First passing each event to
     * {@code handleEvent(Event, EventListener, int)} for every registered
     * listener in the {@link #globalListeners} list. Then, it passes the event
     * to each of the target listeners passed in via {@code targets}.
     *
     * @param evt
     *            the event to process.
     * @param targets
     *            the local listeners to pass the event to.
     */
    public void handleEvent(Event evt, Object[] targets) {
        if (evt == null)
            return;

        int evtType = evt.getEventType();

        // allow all globally registered listeners to handle the event first
        for (Iterator it = globalListeners.iterator(); it.hasNext();) {
            EventListener listener = (EventListener) it.next();
            handleEvent(evt, listener, evtType);
        }

        // if there were no specified targets for the event, then we can quit
        // now
        if (targets == null)
            return;

        // for each of the targets, get their local event listeners
        // and dispatch the event to them
        for (int i = 0; i < targets.length; i++) {
            // get the local event listeners
            EventListener[] targetListeners = targets[i] == null ? null
                                              : getListeners(targets[i]);
            if (targetListeners == null)
                continue;

            // for each local event listener, dispatch the event
            for (int j = 0; j < targetListeners.length; j++) {
                EventListener listener = targetListeners[j];
                if (listener != null && acceptsListener(listener)) {
                    handleEvent(evt, listener, evtType);
                }
            }
        }
    }

    public EventListener[] getListeners(Object eventTarget) {
        return null;
    }
}
