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
package org.flexdock.view;

import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.flexdock.plaf.PlafManager;
import org.flexdock.view.model.ViewButtonModel;

/**
 * @author Christopher Butler
 */
public class Button extends JToggleButton {

    public static final String uiClassID = "Flexdock.titlebar.button";

    public Button(Action action) {
        setAction(action);
        setModel(new ViewButtonModel());
    }

    public void setModel(ButtonModel newModel) {
        ButtonModel oldModel = getModel();
        if(newModel!=null && oldModel!=null)
            newModel.setSelected(oldModel.isSelected());
        super.setModel(newModel);
    }

    public void updateUI() {
        setUI(PlafManager.getUI(this));
    }

    public String getUIClassID() {
        return uiClassID;
    }

    public View getView() {
        return (View)SwingUtilities.getAncestorOfClass(View.class, this);
    }

    public String getActionName() {
        Action action = getAction();
        if(action==null)
            return null;
        return (String)action.getValue(Action.NAME);
    }
}
