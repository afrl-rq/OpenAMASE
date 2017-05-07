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

import java.util.EventListener;


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
        RegistrationEvent regEvt = (RegistrationEvent)evt;
        RegistrationListener regListener = (RegistrationListener)listener;

        switch(eventType) {
        case RegistrationEvent.REGISTERED:
            regListener.registered(regEvt);
            break;
        case RegistrationEvent.UNREGISTERED:
            regListener.unregistered(regEvt);
            break;
        }
    }
}
