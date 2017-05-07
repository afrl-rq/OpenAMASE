// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.plaf.common.border;

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

import javax.swing.border.LineBorder;
import java.awt.*;

public class RoundedLineBorder extends LineBorder {
    // instance data

    private int mArcWidthHeight;
    private boolean mFilled;

    // constructor

    public RoundedLineBorder(Color color, int arcWidthHeight) {
        this(color, arcWidthHeight, 1);
    }

    public RoundedLineBorder(Color color, int arcWidthHeight, int thickness) {
        super(color, thickness);

        mArcWidthHeight = arcWidthHeight;
    }

    // public

    public void setFilled(boolean filled) {
        mFilled = filled;
    }

    // override

    public void paintBorder(Component component, Graphics g, int i, int j, int k, int l) {
        Graphics2D graphics2d = (Graphics2D) g;

        Object obj = graphics2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color color = graphics2d.getColor();
        graphics2d.setColor(lineColor);

        if ( mFilled)
            graphics2d.fillRoundRect(i, j, k - 1, l - 1, mArcWidthHeight, mArcWidthHeight);
        else
            for (int i1 = 0; i1 < thickness; i1++)
                graphics2d.drawRoundRect(i + i1, j + i1, k - i1 - i1 - 1, l - i1 - i1 - 1, mArcWidthHeight, mArcWidthHeight);

        graphics2d.setColor(color);
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, obj);
    }
}