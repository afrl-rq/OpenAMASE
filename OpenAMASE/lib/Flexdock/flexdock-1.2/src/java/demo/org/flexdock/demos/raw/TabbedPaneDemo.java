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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;

import org.flexdock.demos.util.DemoUtility;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.defaults.DefaultDockingPort;
import org.flexdock.docking.defaults.AbstractDockable;


public class TabbedPaneDemo extends JPanel implements DockingConstants {
    private JLabel titlebar;
    private Dockable dockableImpl;

    public TabbedPaneDemo(String title) {
        super();
        titlebar = createTitlebar(" " + title);
        add(titlebar);
        setBorder(new LineBorder(Color.black));
        dockableImpl = new DockableImpl();
    }

    private JLabel createTitlebar(String title) {
        JLabel lbl = new JLabel(title);
        lbl.setForeground(Color.white);
        lbl.setBackground(Color.blue);
        lbl.setOpaque(true);
        return lbl;
    }

    public String getTitle() {
        return titlebar.getText().trim();
    }

    public void doLayout() {
        Insets in = getInsets();
        titlebar.setBounds(in.left, in.top, getWidth() - in.left - in.right, 25);
    }

    private Dockable getDockable() {
        return dockableImpl;
    }

    private class DockableImpl extends AbstractDockable {
        private DockableImpl() {
            super("dockable." + getTitle());
            // the titlebar will the the 'hot' component that initiates dragging
            getDragSources().add(titlebar);
            setTabText(getTitle());
        }

        public Component getComponent() {
            return TabbedPaneDemo.this;
        }
    }




    private static JPanel createContentPane() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.add(buildDockingPort("North"), BorderLayout.NORTH);
        p.add(buildDockingPort("South"), BorderLayout.SOUTH);
        p.add(buildDockingPort("East"), BorderLayout.EAST);
        p.add(buildDockingPort("West"), BorderLayout.WEST);
        p.add(createDockingPort("Center"), BorderLayout.CENTER);
        return p;
    }

    private static DefaultDockingPort buildDockingPort(String desc) {
        // create the DockingPort
        DefaultDockingPort port = createDockingPort(desc);

        // create the Dockable panel
        TabbedPaneDemo cd = new TabbedPaneDemo(desc);
        DockingManager.registerDockable(cd.getDockable());

        // dock the panel and return the DockingPort
        port.dock(cd.getDockable(), CENTER_REGION);
        return port;
    }

    private static int getTabPosition(String desc) {
        if ("North".equals(desc))
            return JTabbedPane.TOP;
        if ("South".equals(desc))
            return JTabbedPane.BOTTOM;
        if ("East".equals(desc))
            return JTabbedPane.RIGHT;
        if ("West".equals(desc))
            return JTabbedPane.LEFT;
        return JTabbedPane.TOP;
    }

    private static DefaultDockingPort createDockingPort(String desc) {
        DefaultDockingPort port = new DefaultDockingPort();
        port.setBackground(Color.gray);
        port.setPreferredSize(new Dimension(200, 100));
        port.getDockingProperties().setTabPlacement(getTabPosition(desc));
        return port;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Custom Conatainers Docking Demo");
        f.setContentPane(createContentPane());
        f.setSize(600, 400);
        DemoUtility.setCloseOperation(f);
        f.setVisible(true);
    }



}
