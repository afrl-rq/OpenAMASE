// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 17, 2005
 */
package org.flexdock.docking.props;

import java.util.List;
import java.util.Map;

/**
 * @author Christopher Butler
 */
public interface ScopedMap {
    public Map getRoot();
    public List getDefaults();
    public List getLocals();
    public List getGlobals();
}
