// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Jul 6, 2005
 */
package org.flexdock.demos.util;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

/**
 * @author Christopher Butler
 */
public class Titlebar extends JLabel {

    public Titlebar() {
        super();
        init();
    }

    public Titlebar(String text) {
        super(text);
        init();
    }

    public Titlebar(String text, Color bgColor) {
        super(text);
        init();
        setBackground(bgColor);
    }

    private void init() {
        setOpaque(true);
        setBorder(new EmptyBorder(2, 4, 2, 2));
    }

    protected void paintBorder(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        g.setColor(getBackground().brighter());
        g.drawLine(0, 0, w, 0);
        g.drawLine(0, 0, 0, h);

        g.setColor(getBackground().darker());
        g.drawLine(0, h, w, h);
    }

    public void setTitle(String title) {
        if(title==null)
            title = "";
        title = title.trim();
        setText(title);
    }
}
