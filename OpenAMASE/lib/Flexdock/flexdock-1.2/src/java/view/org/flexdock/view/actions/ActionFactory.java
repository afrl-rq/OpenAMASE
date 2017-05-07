// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.view.actions;

public class ActionFactory {

    public static DefaultCloseAction createCloseAction() {
        return new DefaultCloseAction();
    }

    public static DefaultDisplayAction createDisplayAction() {
        return new DefaultDisplayAction();
    }

    public static DefaultPinAction createPinAction() {
        return new DefaultPinAction();
    }

}
