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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

/**
 * @author Andreas Ernst
 * @author Christopher Butler
 *
 */
public class RootPaneBorder implements Border {

    public Insets getBorderInsets(Component c) {
        return new Insets(3, 3, 3, 3);
    }

    // implement Border

    public boolean isBorderOpaque() {
        return false;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        RootPane pane = (RootPane)c;

        Color background = pane.getBackground();
        Color darker = pane.getBackground().darker();
        Color evenDarker = darker.darker();
        Color brighter = pane.getBackground().brighter();

        g.setColor(background);

        // brighter top

        g.drawLine(x, y, x + width, y);
        g.drawLine(x, y + 2, x + width, y + 2);
        g.setColor(brighter);
        g.drawLine(x + 1, y + 1, x + width - 1, y + 1);

        // left

        g.setColor(background);
        g.drawLine(x, y + 1, x, y + height);
        g.drawLine(x + 2, y + 2, x + 2, y + height);

        g.setColor(brighter);
        g.drawLine(x + 1, y + 2, x + 1, y + height);

        // bottom

        g.setColor(background);
        g.drawLine(x + 2, y + height - 3, x + width - 4, y + height - 3);
        g.setColor(darker);
        g.drawLine(x + 1, y + height - 2, x + width - 2, y + height - 2);
        g.setColor(evenDarker);
        g.drawLine(x, y + height - 1, x + width, y + height - 1);

        // right

        g.setColor(background);
        g.drawLine(x + width - 3, y + 2, x + width - 3, y + height - 4);
        g.setColor(darker);
        g.drawLine(x + width - 2, y + 1, x + width - 2, y + height - 2);
        g.setColor(evenDarker);
        g.drawLine(x + width - 1, y, x + width - 1, y + height);
    }
}
