// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Jun 8, 2005
 */
package org.flexdock.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.flexdock.docking.DockingManager;
import org.flexdock.view.View;

/**
 * @author Christopher Butler
 */
public class DefaultDisplayAction extends ViewAction {

    public DefaultDisplayAction() {

    }

    public DefaultDisplayAction(String viewId) {
        setViewId(viewId);
        View view = View.getInstance(viewId);
        if(view!=null)
            putValue(Action.NAME, view.getTitle());
    }

    public void actionPerformed(View view, ActionEvent evt) {
        DockingManager.display(view);
    }

}
