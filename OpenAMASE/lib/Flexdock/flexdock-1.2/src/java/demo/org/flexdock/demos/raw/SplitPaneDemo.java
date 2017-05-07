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
import javax.swing.border.LineBorder;

import org.flexdock.demos.util.DemoUtility;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.defaults.DefaultDockingPort;
import org.flexdock.docking.defaults.AbstractDockable;

public class SplitPaneDemo extends JPanel implements DockingConstants {
    private JLabel titlebar;
    private Dockable dockableImpl;

    public SplitPaneDemo(String title) {
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
        titlebar.setBounds(in.left, in.top, getWidth()-in.left-in.right, 25);
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
            return SplitPaneDemo.this;
        }
    }

    private static JPanel createContentPane() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.add(buildDockingPort("North"), BorderLayout.NORTH);
        p.add(buildDockingPort("South"), BorderLayout.SOUTH);
        p.add(buildDockingPort("East"), BorderLayout.EAST);
        p.add(buildDockingPort("West"), BorderLayout.WEST);
        p.add(createDockingPort(), BorderLayout.CENTER);
        return p;
    }

    private static DefaultDockingPort buildDockingPort(String desc) {
        // create the DockingPort
        DefaultDockingPort port = createDockingPort();

        // create the Dockable panel
        SplitPaneDemo spd = new SplitPaneDemo(desc);
        DockingManager.registerDockable(spd.getDockable());

        // dock the panel and return the DockingPort
        port.dock(spd.getDockable(), CENTER_REGION);
        return port;
    }

    private static DefaultDockingPort createDockingPort() {
        DefaultDockingPort port = new DefaultDockingPort();
        port.setBackground(Color.gray);
        port.setPreferredSize(new Dimension(100, 100));
        port.getDockingProperties().setSingleTabsAllowed(true);
        port.setTabsAsDragSource(true);
        return port;
    }

    public static void main(String[] args) {
        DockingManager.setSingleTabsAllowed(true);

        JFrame f = new JFrame("Split Docking Demo");
        f.setContentPane(createContentPane());
        f.setSize(600, 400);
        DemoUtility.setCloseOperation(f);
        f.setVisible(true);
    }
}
