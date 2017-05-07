// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 26, 2005
 */
package org.flexdock.plaf.resources.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * @author Christopher Butler
 */
public class DefaultAction extends AbstractAction {
    public static final DefaultAction SINGLETON = new DefaultAction();

    public void actionPerformed(ActionEvent e) {
        // noop
    }
}
