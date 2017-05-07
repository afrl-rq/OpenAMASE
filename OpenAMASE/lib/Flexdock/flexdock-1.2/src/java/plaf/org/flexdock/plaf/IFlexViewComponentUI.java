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

/**
 * @author Christopher Butler
 */
public interface IFlexViewComponentUI extends XMLConstants {
    public static final String ICON_RESOURCE = "flexdock.button.icon.resource";

    public PropertySet getCreationParameters();
    public void setCreationParameters(PropertySet creationParameters);
    public void initializeCreationParameters();
}
