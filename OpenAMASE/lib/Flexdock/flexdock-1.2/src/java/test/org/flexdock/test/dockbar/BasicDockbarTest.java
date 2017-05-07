// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 15, 2005
 */
package org.flexdock.test.dockbar;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.view.View;

/**
 * @author Bobby Rosenberger
 */
public class BasicDockbarTest {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch(Exception e) {
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        View view = createView();
        //Create and set up the window.
        JFrame frame = new JFrame("Basic Dockbar Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        //Grab the contentpane and add elements
        Container cp = frame.getContentPane();
        cp.setLayout(new FlowLayout());
        // push the buttons 20px down from the top
        ((JComponent)cp).setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton leftButton = new JButton("Pin Left");
        JButton bottomButton = new JButton("Pin Bottom");
        JButton rightButton = new JButton("Pin Right");

        leftButton.addActionListener(createMinimizeAction(DockingConstants.LEFT));
        bottomButton.addActionListener(createMinimizeAction(DockingConstants.BOTTOM));
        rightButton.addActionListener(createMinimizeAction(DockingConstants.RIGHT));

        cp.add(leftButton);
        cp.add(bottomButton);
        cp.add(rightButton);

        // Display the window.
        frame.setVisible(true);
    }

    private static ActionListener createMinimizeAction(final int edge) {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                View view = createView();
                DockingManager.setMinimized(view, true, edge);
            }
        };
    }

    private static int viewCount = 0;

    private static View createView() {
        String id = "test.view." + viewCount;
        String txt = "Test View " + viewCount;
        viewCount++;
        return new View(id, txt);
    }
}