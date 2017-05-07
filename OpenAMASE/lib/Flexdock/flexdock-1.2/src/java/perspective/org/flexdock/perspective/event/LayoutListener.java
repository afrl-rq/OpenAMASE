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

/**
 * @author Christopher Butler
 */
public interface LayoutListener extends EventListener {
    public void layoutApplied(LayoutEvent evt);
    public void layoutEmptied(LayoutEvent evt);
    public void dockableHidden(LayoutEvent evt);
    public void dockableDisplayed(LayoutEvent evt);
}
