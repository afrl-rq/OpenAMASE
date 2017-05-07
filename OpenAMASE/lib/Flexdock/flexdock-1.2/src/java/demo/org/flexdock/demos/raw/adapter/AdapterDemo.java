// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Jun 24, 2005
 */
package org.flexdock.demos.raw.adapter;

import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JFrame;

import org.flexdock.demos.util.DemoUtility;
import org.flexdock.demos.util.Titlepane;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.adapter.AdapterFactory;
import org.flexdock.docking.defaults.DefaultDockingPort;

/**
 * @author Christopher Butler
 */
public class AdapterDemo extends JFrame {
    private DefaultDockingPort port;

    public static void main(String[] args) {
        System.setProperty(AdapterFactory.ADAPTER_RESOURCE_KEY, "org/flexdock/demos/raw/adapter/docking-adapter.xml");
        DockingManager.setFloatingEnabled(true);

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }

    private static void createAndShowGui() {
        JFrame frame = new AdapterDemo();
        frame.setSize(500, 500);
        DemoUtility.setCloseOperation(frame);
        frame.setVisible(true);
    }

    public AdapterDemo() {
        super("Adapter Demo");


        setContentPane(createContentPane());
    }

    private Container createContentPane() {
        port = new DefaultDockingPort();
        Titlepane pane1 = new Titlepane("View 1");
        Titlepane pane2 = new Titlepane("View 2");
        Titlepane pane3 = new Titlepane("View 3");
        Titlepane pane4 = new Titlepane("View 4");
        Titlepane pane5 = new Titlepane("View 5");

        DockingManager.dock(pane1, (DockingPort)port);
        DockingManager.dock(pane2, pane1, DockingConstants.NORTH_REGION, 0.3f);
        DockingManager.dock(pane3, pane1, DockingConstants.SOUTH_REGION);
        DockingManager.dock(pane4, pane1, DockingConstants.EAST_REGION, 0.3f);
        DockingManager.dock(pane5, pane1, DockingConstants.WEST_REGION);

        return port;
    }

}
