// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Jun 2, 2005
 */
package org.flexdock.view.model;

import javax.swing.JToggleButton;

import org.flexdock.docking.DockingManager;
import org.flexdock.docking.state.DockingState;
import org.flexdock.view.View;

/**
 * @author Christopher Butler
 */
public class ViewButtonModel extends JToggleButton.ToggleButtonModel {
    private String viewId;

    public String getViewId() {
        return viewId;
    }
    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    protected View getView() {
        return View.getInstance(getViewId());
    }

    protected synchronized DockingState getDockingState() {
        return DockingManager.getDockingState(getViewId());
    }
}
