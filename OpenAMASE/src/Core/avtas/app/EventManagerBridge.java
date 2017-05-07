// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.app;

import avtas.xml.Element;

/**
 * A simple tool that places the default event manager into a {@link Context}.  
 * This enables objects to interact with the static default event manager in the 
 * traditional non-static way.
 * <br/>
 * Note that this is a stop-gap measure and that code should be changed to access the
 * static event manager references directly.
 * 
 * @see AppEventManager
 * 
 * @author AFRL/RQQD
 */
public class EventManagerBridge implements ContextListener{
    

    public EventManagerBridge() {  
    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
        context.addObject(AppEventManager.getDefaultEventManager());
    }

    @Override
    public void applicationPeerAdded(Object peer) {
    }

    @Override
    public void applicationPeerRemoved(Object peer) {
    }

    @Override
    public void initializeComplete() {
    }

    @Override
    public boolean requestShutdown() {
        return true;
    }

    @Override
    public void shutdown() {
    }

    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */