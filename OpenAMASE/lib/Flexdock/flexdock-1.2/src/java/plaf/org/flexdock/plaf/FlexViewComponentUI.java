// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 1, 2005
 */
package org.flexdock.plaf;

import javax.swing.plaf.ComponentUI;

/**
 * @author Christopher Butler
 */
public abstract class FlexViewComponentUI extends ComponentUI implements IFlexViewComponentUI {
    protected PropertySet creationParameters;

    public PropertySet getCreationParameters() {
        return creationParameters;
    }

    public void setCreationParameters(PropertySet creationParameters) {
        this.creationParameters = creationParameters;
        initializeCreationParameters();
    }

    public abstract void initializeCreationParameters();
}
