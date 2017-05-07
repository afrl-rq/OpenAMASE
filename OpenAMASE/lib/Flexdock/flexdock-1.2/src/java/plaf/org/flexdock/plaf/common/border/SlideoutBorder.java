// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 18, 2005
 */
package org.flexdock.plaf.common.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.border.Border;

import org.flexdock.docking.DockingConstants;

/**
 * @author Christopher Butler
 */
public class SlideoutBorder implements Border, DockingConstants {
    private int orientation;
    public static final Color WIN32_GRAY = new Color(212, 208, 200);

    public SlideoutBorder() {
    }

    public Insets getBorderInsets(Component c) {
        Insets insets = new Insets(0, 0, 0, 0);
        switch(orientation) {
        case LEFT:
            insets.right = 2;
            break;
        case RIGHT:
            insets.left = 2;
            break;
        case TOP:
            insets.bottom = 2;
            break;
        case BOTTOM:
            insets.top = 2;
            break;
        }
        return insets;
    }


    public boolean isBorderOpaque() {
        return true;
    }
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Color outer = Color.BLACK;
        Color inner = Color.GRAY;
        outer = WIN32_GRAY;
        inner = Color.WHITE;

        Color base = g.getColor();

        int w = c.getWidth();
        int h = c.getHeight();

        Point outer1 = new Point(0, 0);
        Point outer2 = new Point(0, 0);
        Point inner1 = new Point(0, 0);
        Point inner2 = new Point(0, 0);

        switch(orientation) {
        case LEFT:
            outer1.setLocation(w-1, 0);
            outer2.setLocation(w-1, h-1);
            inner1.setLocation(w-2, 0);
            inner2.setLocation(w-2, h-1);
            break;
        case RIGHT:
            outer1.setLocation(0, 0);
            outer2.setLocation(0, h-1);
            inner1.setLocation(1, 0);
            inner2.setLocation(1, h-1);
            break;
        case TOP:
            outer1.setLocation(0, h-1);
            outer2.setLocation(w-1, h-1);
            inner1.setLocation(0, h-2);
            inner2.setLocation(w-1, h-2);
            break;
        case BOTTOM:
            outer1.setLocation(0, 0);
            outer2.setLocation(w-1, 0);
            inner1.setLocation(0, 1);
            inner2.setLocation(w-1, 1);
            break;
        }

        g.setColor(outer);
        g.drawLine(outer1.x, outer1.y, outer2.x, outer2.y);
        g.setColor(inner);
        g.drawLine(inner1.x, inner1.y, inner2.x, inner2.y);

        g.setColor(base);
    }

    public int getOrientation() {
        return orientation;
    }
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}
