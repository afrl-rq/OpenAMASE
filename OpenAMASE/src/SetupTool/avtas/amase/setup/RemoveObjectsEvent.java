// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.setup;

import avtas.lmcp.LMCPObject;
import java.util.Collection;

/**
 * Notifies interested components that multiple objects should be removed from the scenario event list.
 * This event is usually used to notify the SetupEventBridge that an XML entry with the
 * underlying LMCP object should be removed from the scenario.
 *
 * @author AFRL/RQQD
 */
public class RemoveObjectsEvent {

    public final Object[] objects;

    public RemoveObjectsEvent(Object... objsToRemove) {
        this.objects = objsToRemove;
    }
    
    public RemoveObjectsEvent(Collection<? extends Object> objsToRemove) {
        this.objects = objsToRemove.toArray(new Object[]{});
    }
    
    public Object[] getObjects() {
        return objects;
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */