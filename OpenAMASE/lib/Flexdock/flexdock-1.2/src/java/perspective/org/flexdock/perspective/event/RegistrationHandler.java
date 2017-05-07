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

import java.util.EventListener;

import org.flexdock.docking.Dockable;
import org.flexdock.event.Event;
import org.flexdock.event.EventHandler;
import org.flexdock.perspective.Perspective;

/**
 * @author Christopher Butler
 */
public class RegistrationHandler extends EventHandler {

    public boolean acceptsEvent(Event evt) {
        return evt instanceof RegistrationEvent;
    }

    public boolean acceptsListener(EventListener listener) {
        return listener instanceof RegistrationListener;
    }
    public void handleEvent(Event evt, EventListener listener, int eventType) {

        RegistrationEvent event = (RegistrationEvent)evt;
        RegistrationListener consumer = (RegistrationListener)listener;

        switch(eventType) {
        case RegistrationEvent.REGISTERED:
            register(event, consumer);
            break;
        case RegistrationEvent.UNREGISTERED:
            unregister(event, consumer);
            break;
        }
    }

    private void register(RegistrationEvent evt, RegistrationListener listener) {
        if(evt.getSource() instanceof Perspective)
            listener.perspectiveAdded(evt);
        else if(evt.getSource() instanceof Dockable)
            listener.dockableAdded(evt);
    }

    private void unregister(RegistrationEvent evt, RegistrationListener listener) {
        if(evt.getSource() instanceof Perspective)
            listener.perspectiveRemoved(evt);
        else if(evt.getSource() instanceof Dockable)
            listener.dockableRemoved(evt);
    }
}
