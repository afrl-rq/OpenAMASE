// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Feb 28, 2005
 */
package org.flexdock.plaf.theme;

import java.awt.Graphics;

import javax.swing.JComponent;

import org.flexdock.plaf.FlexViewComponentUI;

/**
 * @author Christopher Butler
 */
public class ViewUI extends FlexViewComponentUI {

    public void installUI(JComponent c) {
        super.installUI(c);
    }

    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
    }

    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
    }

    public void initializeCreationParameters() {

    }

    public String getPreferredTitlebarUI() {
        return creationParameters.getString(UIFactory.TITLEBAR_KEY);
    }
}
