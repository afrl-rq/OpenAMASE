// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.demos.maximizing;

import javax.swing.JTabbedPane;
import javax.swing.border.Border;

import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.defaults.DefaultDockingPort;
import org.flexdock.docking.defaults.DefaultDockingStrategy;
import org.flexdock.docking.defaults.StandardBorderManager;
import org.flexdock.plaf.common.border.ShadowBorder;

public class MyDockingPort extends DefaultDockingPort {

    static {
        initStatic();
    }

    private static void initStatic() {
        DockingManager.setDockingStrategy(MyDockingPort.class, new MyDockingStrategy());
    }

    public MyDockingPort() {
        this(new ShadowBorder());
    }

    public MyDockingPort(Border portletBorder) {
        super();
        if (portletBorder != null) {
            setBorderManager(new StandardBorderManager(portletBorder));
        }
    }

    protected JTabbedPane createTabbedPane() {
        JTabbedPane tabbed = super.createTabbedPane();
        tabbed.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        return tabbed;
    }

    // ***************

    private static class MyDockingStrategy extends DefaultDockingStrategy {
        protected DockingPort createDockingPortImpl(DockingPort base) {
            return new MyDockingPort();
        }


    }

}