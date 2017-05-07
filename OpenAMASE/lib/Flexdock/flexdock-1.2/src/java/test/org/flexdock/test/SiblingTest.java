// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 4, 2005
 */
package org.flexdock.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.flexdock.demos.util.VSNetStartPage;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.defaults.DefaultDockingStrategy;
import org.flexdock.util.SwingUtility;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;

/**
 * @author Christopher Butler
 */
public class SiblingTest extends JFrame implements DockingConstants {
    private JDialog siblingTestDialog;

    public static void main(String[] args) {
        SwingUtility.setPlaf("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        //		SwingUtility.setPlaf("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        //		SwingUtility.setPlaf("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                startup();
            }
        });
    }

    private static void startup() {
        JFrame f = new SiblingTest();
        f.setSize(800, 600);
        SwingUtility.centerOnScreen(f);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public SiblingTest() {
        super("Viewport Demo");
        setContentPane(createContentPane());
    }

    private JPanel createContentPane() {
        JPanel p = new JPanel(new BorderLayout(0, 0));
        p.setBorder(new EmptyBorder(5, 5, 5, 5));

        Viewport viewport = new Viewport();
        p.add(viewport, BorderLayout.CENTER);

        View startPage = createStartPage();
        View view1 = createView("solution.explorer", "Solution Explorer");
        View view2 = createView("task.list", "Task List");
        View view3 = createView("class.view", "Class View");
        View view4 = createView("message.log", "Message Log");

        viewport.dock(startPage);
        startPage.dock(view1, WEST_REGION, .3f);
        startPage.dock(view2, SOUTH_REGION, .3f);
        startPage.dock(view4, EAST_REGION, .3f);
        view1.dock(view3, SOUTH_REGION, .3f);

        return p;
    }

    private View createView(String id, String text) {
        View view = new View(id, text);
        view.addAction(CLOSE_ACTION);
        view.addAction(PIN_ACTION);

        JPanel p = new JPanel();
        //		p.setBackground(Color.WHITE);
        p.setBorder(new LineBorder(Color.GRAY, 1));

        JTextField t = new JTextField(text);
        t.setPreferredSize(new Dimension(100, 20));
        p.add(t);

        view.setContentPane(p);
        return view;
    }


    private JDialog getSiblingTestDialog() {
        if(siblingTestDialog==null) {
            siblingTestDialog = new JDialog(this, "Sibling Test");
            siblingTestDialog.setContentPane(new SiblingTestPanel());
        }
        return siblingTestDialog;
    }

    private View createStartPage() {

        String id = "startPage";

        VSNetStartPage page = new VSNetStartPage();
        page.getNewProjButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                JDialog dialog = getSiblingTestDialog();
                if(dialog.isVisible())
                    return;

                dialog.setVisible(true);
                dialog.pack();
                testSiblings();
            }
        });

        View view = new View(id, null, null);
        view.setTerritoryBlocked(CENTER_REGION, true);
        view.setTitlebar(null);
        view.setContentPane(page);

        return view;
    }

    private void testSiblings() {
        SiblingTestPanel panel = (SiblingTestPanel)getSiblingTestDialog().getContentPane();
        panel.sync();
    }




    private class SiblingTestPanel extends JPanel {
        private JComboBox dockableList;
        private JComboBox regionList;
        private JLabel siblingLabel;

        private SiblingTestPanel() {
            init();
        }

        private JComboBox getDockableList() {
            if (dockableList != null)
                return dockableList;

            ArrayList list = new ArrayList(DockingManager.getDockableIds());
            Collections.sort(list);
            String[] dockableIds = (String[]) list.toArray(new String[0]);
            dockableList = new JComboBox(dockableIds);
            return dockableList;
        }

        private JComboBox getRegionList() {
            if (regionList != null)
                return regionList;

            String[] regions = { NORTH_REGION, SOUTH_REGION, EAST_REGION, WEST_REGION };
            regionList = new JComboBox(regions);
            return regionList;
        }

        private JLabel getSiblingLabel() {
            if(siblingLabel==null)
                siblingLabel = new JLabel();
            return siblingLabel;
        }


        private void init() {
            setLayout(new GridBagLayout());
            setBorder(new EmptyBorder(5, 5, 5, 10));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(6, 6, 0, 0);
            gbc.gridx = GridBagConstraints.RELATIVE;
            gbc.gridy = 0;

            add(new JLabel("Dockable:"), gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            add(getDockableList(), gbc);

            gbc.gridy++;
            gbc.gridwidth = 1;
            add(new JLabel("Region:"), gbc);
            add(getRegionList(), gbc);

            gbc.gridy++;
            gbc.gridwidth = 1;
            add(new JLabel("Sibling:"), gbc);
            add(getSiblingLabel(), gbc);

            ItemListener syncher = new ItemListener() {
                public void itemStateChanged(ItemEvent evt) {
                    sync();
                }
            };
            getDockableList().addItemListener(syncher);
            getRegionList().addItemListener(syncher);
        }

        public void sync() {
            String viewId = (String)getDockableList().getSelectedItem();
            String region = (String)getRegionList().getSelectedItem();

            Dockable dockable = DockingManager.getDockable(viewId);
            Dockable sibling = DefaultDockingStrategy.getSibling(dockable, region);
            getSiblingLabel().setText(sibling==null? "null": sibling.toString());
        }
    }

}