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
 * Implements a do-nothing {@link ContextListener}.
 * @author AFRL/RQQD
 */
public class ContextAdapter implements ContextListener{

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
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