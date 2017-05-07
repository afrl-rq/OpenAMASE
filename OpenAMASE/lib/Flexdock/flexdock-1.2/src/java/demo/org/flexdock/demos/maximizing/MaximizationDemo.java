// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.demos.maximizing;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.flexdock.demos.util.DemoUtility;
import org.flexdock.demos.view.ViewDemo;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.util.SwingUtility;

import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;

public class MaximizationDemo {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        boolean loaded = configureDocking();

        JFrame frame = new JFrame("FlexPortalMaximized");

        frame.setContentPane(createContentPane(loaded));

        DemoUtility.setCloseOperation(frame);

        frame.setSize(500, 500);
        SwingUtility.centerOnScreen(frame);
        // frame.pack();
        frame.setVisible(true);
    }

    private static boolean configureDocking() {
        DockingManager.setFloatingEnabled(false);
        return false;
    }

    private static JComponent createContentPane(boolean loaded) {

        MyDockingPort dockingPort = new MyDockingPort();

        Dockable topComp = createFramePanel("Top");
        DockingManager.registerDockable(topComp);
        DockingManager.dock(topComp, (DockingPort) dockingPort, DockingConstants.CENTER_REGION);

        Dockable south = createFramePanel("South");
        DockingManager.registerDockable(south);
        DockingManager.dock(south, topComp, DockingConstants.SOUTH_REGION, 0.3f);

        Dockable west = createFramePanel("West");
        DockingManager.registerDockable(west);
        DockingManager.dock(west, topComp, DockingConstants.WEST_REGION, 0.5f);

        Dockable l2South = createFramePanel("South of West");
        DockingManager.registerDockable(l2South);
        DockingManager.dock(l2South, west, DockingConstants.SOUTH_REGION, 0.33f);

        Dockable east = createFramePanel("East");
        DockingManager.registerDockable(east);
        DockingManager.dock(east, topComp, DockingConstants.EAST_REGION, 0.2f);

        return dockingPort;
    }

    private static Dockable createFramePanel(String title) {
        JLabel label = new JLabel("Content of " + title);
        JButton maxButton = createButton(createIcon("maximize.gif"));
        JToolBar toolbar = createPortletToolbar(maxButton);
        SimpleInternalFrame sif = new SimpleInternalFrame(title, toolbar, label);
        final Dockable dockable = new DockableSimpleInternalFrame(sif);

        maxButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Calling DockingManager to maximize: " + dockable);
                DockingManager.toggleMaximized(dockable);
            }
        });

        return dockable;
    }

    private static JButton createButton(Icon icon) {
        JButton button = new JButton(icon);
        button.setFocusable(false);
        return button;
    }

    private static JToolBar createPortletToolbar(JButton maxButton) {
        JToolBar toolbar = new JToolBar();
        toolbar.add(maxButton);
        toolbar.setFloatable(false);
        toolbar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);

        return toolbar;
    }

    private static Icon createIcon(String icon) {
        return new ImageIcon(createImageImpl(icon));
    }

    private static Image createImageImpl(String resourceName) {
        URL iconURL = MaximizationDemo.class.getResource(resourceName);
        if (iconURL == null) {
            throw new RuntimeException("Could not find: " + resourceName);
        }
        return Toolkit.getDefaultToolkit().createImage(iconURL);
    }

}