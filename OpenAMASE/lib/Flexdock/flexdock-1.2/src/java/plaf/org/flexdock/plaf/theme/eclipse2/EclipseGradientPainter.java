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
package org.flexdock.plaf.theme.eclipse2;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import org.flexdock.plaf.resources.paint.DefaultPainter;

/**
 * @author Claudio Romano
 */
public class EclipseGradientPainter extends DefaultPainter {

    public void paint(Graphics g, int width, int height, boolean active, JComponent titlebar) {
        float center = width / 1.3f;

        GradientPaint firstHalf;
        if( active)
            firstHalf = new GradientPaint(0, 0, getBackgroundColorActive(), center, 0, getBackgroundColorInactive());
        else
            firstHalf = new GradientPaint(0, 0, getBackgroundColorInactive(), center, 0, getBackgroundColorInactive());

        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(firstHalf);
        g2.fillRect(0, 0, width, height);
    }
}
