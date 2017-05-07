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

import java.util.Collection;
import java.util.EventListener;
import java.util.Iterator;
import java.util.Stack;


/**
 * @author Christopher Butler
 */
public class EventManager {
    private static final EventManager SINGLETON = new EventManager();
    private Stack handlers = new Stack();

    static {
        addHandler(new RegistrationHandler());
    }


    public static EventManager getInstance() {
        return SINGLETON;
    }

    private EventManager() {

    }


    public static void addHandler(EventHandler handler) {
        getInstance().addEventHandler(handler);
    }

    public static void removeHandler(EventHandler handler) {
        getInstance().removeEventHandler(handler);
    }

    public static void addListener(EventListener listener) {
        getInstance().addEventListener(listener);
    }

    public static void removeListener(EventListener listener) {
        getInstance().removeEventListener(listener);
    }











    public static void dispatch(Event evt) {
        getInstance().dispatchEvent(evt);
    }

    public static void dispatch(Event evt, Object target) {
        getInstance().dispatchEvent(evt, target);
    }

    public static void dispatch(Event evt, Object[] targets) {
        getInstance().dispatchEvent(evt, targets);
    }








    public void addEventHandler(EventHandler handler) {
        if(handler!=null)
            handlers.push(handler);
    }

    public void removeEventHandler(EventHandler handler) {
        if(handler!=null)
            handlers.remove(handler);
    }

    private EventHandler getHandler(Event evt) {
        for(Iterator it=handlers.iterator(); it.hasNext();) {
            EventHandler handler = (EventHandler)it.next();
            if(handler.acceptsEvent(evt))
                return handler;
        }
        return null;
    }

    private EventHandler getHandler(EventListener listener) {
        for(Iterator it=handlers.iterator(); it.hasNext();) {
            EventHandler handler = (EventHandler)it.next();
            if(handler.acceptsListener(listener))
                return handler;
        }
        return null;
    }

    public void addEventListener(EventListener listener) {
        EventHandler handler = listener==null? null: getHandler(listener);
        if(handler!=null)
            handler.addListener(listener);
    }

    public void removeEventListener(EventListener listener) {
        EventHandler handler = listener==null? null: getHandler(listener);
        if(handler!=null)
            handler.removeListener(listener);
    }


    public void dispatchEvent(Event evt) {
        dispatchEvent(evt, null);
    }

    public void dispatchEvent(Event evt, Object target) {
        Object[] targets = null;
        if(target instanceof Collection) {
            targets = ((Collection)target).toArray();
        } else if(target!=null) {
            targets = new Object[] {target};
        }

        dispatchEvent(evt, targets);

    }

    public void dispatchEvent(Event evt, Object[] targets) {
        EventHandler handler = evt==null? null: getHandler(evt);
        if(handler!=null)
            handler.handleEvent(evt, targets);
    }
}
