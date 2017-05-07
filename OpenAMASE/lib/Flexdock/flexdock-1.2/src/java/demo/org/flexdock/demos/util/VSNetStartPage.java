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
package org.flexdock.demos.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;

import org.flexdock.util.ResourceManager;

/**
 * @author Christopher Butler
 */
public class VSNetStartPage extends JPanel {
    private Icon miscIcons;
    private Image tabsImg;
    private Color tabRunBG;
    private Color contentBG1;
    private Color contentBG2;
    private Color tableBG1;
    private Font labelFont;

    private JButton newProjButton;
    private JButton openProjButton;
    private JPanel tablePane;
    private JPanel contentPane;
    private JTabbedPane tabbedPane;

    public VSNetStartPage() {
        super(new BorderLayout(0, 0));
        init();
    }

    private void init() {
        initAttribs();

        newProjButton = new JButton("New Project");
        openProjButton = new JButton("Open Project");

        tablePane = createTablePane();

        contentPane = createContentPane();
        contentPane.add(tablePane);
        contentPane.add(newProjButton);
        contentPane.add(openProjButton);

        tabbedPane = createTabbedPane();
        tabbedPane.addTab("Start Page", contentPane);
        tabbedPane.setBorder(null);

        add(tabbedPane, BorderLayout.CENTER);
        setBorder(new LineBorder(Color.GRAY, 1));
    }

    private void initAttribs() {
        miscIcons = ResourceManager.createIcon("org/flexdock/demos/view/ms_misc_icons001.png");
        tabsImg = ResourceManager.createImage("org/flexdock/demos/view/ms_tabs001.png");
        tabRunBG = new Color(247, 243, 233);
        contentBG1 = new Color(246, 246, 246);
        contentBG2 = new Color(102, 153, 204);
        tableBG1 = new Color(154, 154, 143);
        labelFont = new Font("Dialog", Font.BOLD, 11);
    }

    private JPanel createTablePane() {
        return new JPanel() {
            protected void paintComponent(Graphics g) {
                g.setColor(tableBG1);
                g.fillRect(0, 0, getWidth(), 20);
                g.setColor(VSNetStartPage.this.getBackground());
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g.setColor(Color.BLACK);
                g.setFont(labelFont);
                g.drawString("Name", 5, 15);
                g.drawString("Modified", 350, 15);
            }
        };
    }

    private JPanel createContentPane() {
        return new JPanel(null) {
            public void doLayout() {
                int tableH = getHeight() - 120 - 55;
                tableH = Math.max(tableH, 25);
                tablePane.setBounds(12, 120, 475, tableH);

                int buttonY = 120 + tableH + 18;
                Dimension d = newProjButton.getPreferredSize();
                newProjButton.setBounds(12, buttonY, d.width, d.height);
                openProjButton.setBounds(24 + d.width, buttonY, openProjButton.getPreferredSize().width, d.height);
            }

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = getWidth();

                Color origC = g.getColor();
                Font origF = g.getFont();

                g.setColor(Color.WHITE);
                g.fillRect(0, 0, w, getHeight());

                g.setColor(contentBG1);
                g.fillRect(0, 0, w, 48);
                g.setColor(contentBG2);
                g.fillRect(0, 48, w, 23);
                g.drawImage(tabsImg, 0, 0, null, this);

                g.setColor(Color.BLACK);
                g.setFont(labelFont);
                g.drawString("Open an Existing Project", 12, 100);

                g.setFont(origF);
                g.setColor(origC);
            }
        };
    }

    private JTabbedPane createTabbedPane() {
        return new JTabbedPane(JTabbedPane.TOP) {
            protected void paintComponent(Graphics g) {
                Color orig = g.getColor();
                Rectangle tabBounds = getBoundsAt(0);
                int tabLowerY = tabBounds.y + tabBounds.height;

                g.setColor(tabRunBG);
                g.fillRect(0, 0, getWidth(), tabLowerY);

                int iconX = getWidth() - miscIcons.getIconWidth();
                int iconY = (tabLowerY) / 2 - miscIcons.getIconHeight() / 2 + 1;
                miscIcons.paintIcon(this, g, iconX, iconY);

                g.setColor(orig);
                super.paintComponent(g);

                g.setColor(Color.WHITE);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g.drawRect(1, tabLowerY - 1, getWidth() - 3, getHeight() - tabLowerY - 1);
                g.setColor(orig);
            }
        };
    }


    public JButton getNewProjButton() {
        return newProjButton;
    }
    public JButton getOpenProjButton() {
        return openProjButton;
    }
}
