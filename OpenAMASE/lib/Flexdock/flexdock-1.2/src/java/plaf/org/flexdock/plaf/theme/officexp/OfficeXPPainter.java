// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on 22.03.2005
 */
package org.flexdock.plaf.theme.officexp;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import org.flexdock.plaf.resources.paint.DefaultPainter;
import org.flexdock.util.SwingUtility;

/**
 * @author Claudio Romano
 */
public class OfficeXPPainter extends DefaultPainter {
    public static final String GRADIENT_COLOR = "gradient.color";
    public static final String GRADIENT_COLOR_ACTIVE = "gradient.color.active";


    public void paint(Graphics g, int width, int height, boolean active, JComponent titlebar) {
        int center = (int)(height / 1.2);

        Color backgroundColor = getBackgroundColor(active);
        Color gradColor = getGradientColor(active);

        GradientPaint gradientPaint;
        if( active)
            gradientPaint = new GradientPaint(0, 0, gradColor, 0, center, backgroundColor);
        else
            gradientPaint = new GradientPaint(0, 0, backgroundColor, 0, center, gradColor);

        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(gradientPaint);
        g2.fillRect(0, 0, width, height);

    }

    protected Color getGradientColor(boolean active) {
        Color color = active ? painterResource.getColor( GRADIENT_COLOR_ACTIVE) : painterResource.getColor( GRADIENT_COLOR);
        return color == null ? SwingUtility.darker(getBackgroundColor( active), 0.75) : color;
    }

}
