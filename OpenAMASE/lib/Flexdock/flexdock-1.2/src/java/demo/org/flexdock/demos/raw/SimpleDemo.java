// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.demos.raw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.flexdock.demos.util.DemoUtility;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.defaults.DefaultDockingPort;

public class SimpleDemo extends JFrame implements DockingConstants {
    public static void main(String[] args) {
        JFrame f = new SimpleDemo();
        f.setSize(600, 400);
        DemoUtility.setCloseOperation(f);
        f.setVisible(true);
    }

    public SimpleDemo() {
        super("Simple Docking Demo");
        setContentPane(createContentPane());
    }

    private JPanel createContentPane() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.add(buildDockingPort(Color.blue, "Blue"), BorderLayout.NORTH);
        p.add(buildDockingPort(Color.red, "Red"), BorderLayout.SOUTH);
        p.add(buildDockingPort(Color.green, "Green"), BorderLayout.EAST);
        p.add(buildDockingPort(Color.yellow, "Yellow"), BorderLayout.WEST);
        p.add(createDockingPort(), BorderLayout.CENTER);
        return p;
    }

    private DefaultDockingPort buildDockingPort(Color color, String desc) {
        // create the DockingPort
        DefaultDockingPort port = createDockingPort();

        // create and register the Dockable panel
        JPanel p = new JPanel();
        p.setBackground(color);
        p.add(new JLabel("Drag Me"));
        DockingManager.registerDockable(p, desc);

        // dock the panel and return the DockingPort
        port.dock(p, CENTER_REGION);
        return port;
    }

    private DefaultDockingPort createDockingPort() {
        DefaultDockingPort port = new DefaultDockingPort();
        port.setBackground(Color.ORANGE);
        port.setPreferredSize(new Dimension(100, 100));
        return port;
    }
}
