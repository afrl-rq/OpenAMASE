// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.demos.raw.elegant;

import javax.swing.JComponent;

import org.flexdock.demos.util.DockingStubTitlepane;
import org.flexdock.demos.util.GradientTitlebar;
import org.flexdock.demos.util.Titlebar;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;


public class ElegantPanel extends DockingStubTitlepane {
    private Dockable dockable;

    public ElegantPanel(String title) {
        super(title, title);
        DockingManager.registerDockable(this);
    }

    public void dock(ElegantPanel otherPanel) {
        DockingManager.dock(otherPanel, this);
    }

    public void dock(ElegantPanel otherPanel, String region) {
        DockingManager.dock(otherPanel, this, region);
    }

    public void dock(ElegantPanel otherPanel, String region, float ratio) {
        DockingManager.dock(otherPanel, this, region, ratio);
    }

    protected JComponent createContentPane() {
        return null;
    }

    protected Titlebar createTitlebar(String title) {
        return new GradientTitlebar(title);
    }
}
