// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/* Copyright (c) 2004 Andreas Ernst

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in the
Software without restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
Software, and to permit persons to whom the Software is furnished to do so, subject
to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. */

package org.flexdock.docking.floating.frames;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.io.Serializable;

import javax.swing.JLayeredPane;

/**
 * @author Andreas Ernst
 * @author Christopher Butler
 */
@SuppressWarnings(value = { "serial" })
public class RootPaneLayout implements LayoutManager2, Serializable {
    private RootPane pane;
    // implement

    RootPaneLayout(RootPane pane) {
        this.pane = pane;
    }
    public Dimension preferredLayoutSize(Container parent) {
        Insets insets = pane.getInsets();

        int preferredWidth = 0;
        int preferredHeight = 0;

        // content pane
        Container contentPane = pane.getContentPane();
        if (contentPane != null) {
            preferredWidth = contentPane.getPreferredSize().width;
            preferredHeight = contentPane.getPreferredSize().height;
        } // if
        else {
            preferredWidth = parent.getSize().width;
            preferredHeight = parent.getSize().height;
        } // else

        // title

        // done

        return new Dimension(preferredWidth + insets.left + insets.right,
                             preferredHeight + insets.top + insets.bottom);
    }


    public Dimension minimumLayoutSize(Container parent) {
        Insets insets = pane.getInsets();

        int minimumWidth = 0;
        int minimumHeight = 0;

        // content pane
        Container contentPane = pane.getContentPane();
        if (contentPane != null) {
            minimumWidth = contentPane.getMinimumSize().width;
            minimumHeight = contentPane.getMinimumSize().height;
        } // if
        else {
            minimumWidth = parent.getSize().width;
            minimumHeight = parent.getSize().height;
        } // else
        // done

        return new Dimension(minimumWidth + insets.left + insets.right,
                             minimumHeight + insets.top + insets.bottom);
    }

    public Dimension maximumLayoutSize(Container target) {
        Dimension rd, mbd;
        Insets i = pane.getInsets();

        mbd = new Dimension(0, 0);

        Container contentPane = pane.getContentPane();
        if (contentPane != null) {
            rd = contentPane.getMaximumSize();
        } else {
            // This is silly, but should stop an overflow error
            rd = new Dimension(Integer.MAX_VALUE,
                               Integer.MAX_VALUE - i.top - i.bottom - mbd.height - 1);
        }

        return new Dimension(Math.min(rd.width, mbd.width) + i.left + i.right,
                             rd.height + mbd.height + i.top + i.bottom);
    }

    // layout engine...

    public void layoutContainer(Container parent) {
        Rectangle bounds = parent.getBounds();
        Insets insets = pane.getInsets();

        // substract insets
        int w = bounds.width - insets.right - insets.left;
        int h = bounds.height - insets.top - insets.bottom;

        JLayeredPane layeredPane = pane.getLayeredPane();
        if (layeredPane != null)
            layeredPane.setBounds(insets.left, insets.top, w, h); // x, y, w, h

        Component glassPane = pane.getGlassPane();
        if (glassPane != null)
            glassPane.setBounds(insets.left, insets.top, w, h);

        // Note: This is laying out the children in the layeredPane,
        // technically, these are not our children.

        int contentY = 0;
        Container contentPane = pane.getContentPane();
        if (contentPane != null)
            contentPane.setBounds(0, contentY, w, h - contentY); // x, y, w, h
    }

    // more...

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public void addLayoutComponent(Component comp, Object constraints) {
    }

    public float getLayoutAlignmentX(Container target) {
        return 0.0f;
    }

    public float getLayoutAlignmentY(Container target) {
        return 0.0f;
    }

    public void invalidateLayout(Container target) {
    }
}
