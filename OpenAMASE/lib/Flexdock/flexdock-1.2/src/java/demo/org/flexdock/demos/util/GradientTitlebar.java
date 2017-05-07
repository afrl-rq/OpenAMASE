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
import java.awt.Font;
import java.awt.Graphics;

/**
 * @author Christopher Butler
 */
public class GradientTitlebar extends Titlebar {
    public static final Color DEFAULT_MID_COLOR = new Color(168, 203, 239);
    public static final Color DEFAULT_START_COLOR = new Color(10, 36, 106);

    private GradientPainter gradient;

    public GradientTitlebar() {
        super();
        init(null, null);
    }

    public GradientTitlebar(String text) {
        super(text);
        init(null, null);
    }

    public GradientTitlebar(String text, Color start, Color mid) {
        super(text);
        init(start, mid);
    }

    private void init(Color start, Color mid) {
        setOpaque(false);
        gradient = new GradientPainter(start, mid);
        setStartColor(start);
        setMidColor(mid);
        setForeground(Color.WHITE);
        setFont(getFont().deriveFont(Font.PLAIN));
    }

    public void setStartColor(Color color) {
        gradient.setStartColor(color==null? DEFAULT_START_COLOR: color);
    }

    public void setMidColor(Color color) {
        gradient.setMidColor(color==null? DEFAULT_MID_COLOR: color);
    }

    protected void paintComponent(Graphics g) {
        gradient.paintGradient(this, g);
        super.paintComponent(g);
    }
}
