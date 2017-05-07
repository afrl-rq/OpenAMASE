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

/**
 * @author Christopher Butler
 */
public interface RegistrationListener extends org.flexdock.event.RegistrationListener {
    public void perspectiveAdded(RegistrationEvent evt);
    public void dockableAdded(RegistrationEvent evt);

    public void perspectiveRemoved(RegistrationEvent evt);
    public void dockableRemoved(RegistrationEvent evt);
}
