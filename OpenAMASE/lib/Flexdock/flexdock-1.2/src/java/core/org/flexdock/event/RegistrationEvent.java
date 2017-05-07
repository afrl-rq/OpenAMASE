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


/**
 * @author Christopher Butler
 */
public class RegistrationEvent extends Event {
    public static final int REGISTERED = 0;
    public static final int UNREGISTERED = 1;

    private Object owner;

    public RegistrationEvent(Object src, Object owner, int evtType) {
        super(src, evtType);
        this.owner = owner;
    }

    public RegistrationEvent(Object src, Object owner, boolean registered) {
        this(src, owner, registered? REGISTERED: UNREGISTERED);
    }

    public Object getOwner() {
        return owner;
    }
}
