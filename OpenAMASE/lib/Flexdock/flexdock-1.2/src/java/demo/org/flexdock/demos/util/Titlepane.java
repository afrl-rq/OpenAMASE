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
package org.flexdock.demos.util;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * @author Christopher Butler
 */
public class Titlepane extends JPanel {
    private Titlebar titlebar;
    private JComponent contentPane;

    public Titlepane(String title) {
        setLayout(new BorderLayout());

        titlebar = createTitlebar(title);
        add(titlebar, BorderLayout.NORTH);
        setContentPane(createContentPane());
    }

    public String getTitle() {
        return titlebar.getText();
    }

    public void setTitle(String title) {
        titlebar.setTitle(title);
    }

    public JLabel getTitlebar() {
        return titlebar;
    }

    protected Titlebar createTitlebar(String title) {
        return new Titlebar(title, new Color(183, 201, 217));
    }

    public void setContentPane(JComponent comp) {
        if(contentPane!=null)
            remove(contentPane);
        if(comp!=null)
            add(comp, BorderLayout.CENTER);
        contentPane = comp;
    }

    protected JComponent createContentPane() {
        JPanel pane = new JPanel();
        pane.setBorder(new LineBorder(Color.DARK_GRAY));
        pane.setBackground(Color.WHITE);
        return pane;
    }
}
