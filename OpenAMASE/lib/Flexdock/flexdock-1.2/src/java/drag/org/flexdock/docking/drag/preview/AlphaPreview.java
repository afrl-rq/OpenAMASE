// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/* Copyright (c) 2004 Ismail Degani, Christopher M Butler
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
OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.flexdock.docking.drag.preview;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Map;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.drag.effects.DefaultPreview;

public class AlphaPreview extends DefaultPreview {

    public static final float DEFAULT_ALPHA = 0.5f;
    public static final Color DEFAULT_COLOR = Color.WHITE;
    public static final Color DEFAULT_BORDER = Color.BLACK;

    public static final AlphaPreview BLUE = new AlphaPreview(Color.BLACK, Color.BLUE.brighter().brighter().brighter().brighter(), .2f, true);
    public static final AlphaPreview BLACK = new AlphaPreview(Color.BLACK, Color.BLACK.brighter().brighter().brighter().brighter(), .25f, true);

    private float previewAlpha;
    private Color previewColor;
    private Color borderColor;
    private boolean immutable;

    public AlphaPreview() {
        this(DEFAULT_BORDER, DEFAULT_COLOR, DEFAULT_ALPHA, false);
    }

    public AlphaPreview(Color border, Color fill, float alpha) {
        this(border, fill, alpha, false);
    }

    public AlphaPreview(Color border, Color fill, float alpha, boolean immutable) {
        setBorderColor(border);
        setPreviewColor(fill);
        setAlpha(alpha);
        this.immutable = immutable;
    }

    public void setPreviewColor(Color color) {
        if(!immutable)
            previewColor = color==null? DEFAULT_COLOR: color;
    }

    public void setAlpha(float alpha) {
        if(!immutable) {
            alpha = Math.max(0, alpha);
            alpha = Math.min(alpha, 1f);
            previewAlpha = alpha;
        }
    }

    public void setBorderColor(Color color) {
        if(!immutable)
            borderColor = color==null? DEFAULT_BORDER: color;
    }

    public void drawPreview(Graphics2D g, Polygon p, Dockable dockable, Map dragInfo) {
        Rectangle rect = p.getBounds();

        g.setColor(borderColor);
        g.draw3DRect(rect.x, rect.y, rect.width-1, rect.height-1, false);
        Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, previewAlpha);
        g.setComposite(composite);
        g.setColor(previewColor);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }

}
