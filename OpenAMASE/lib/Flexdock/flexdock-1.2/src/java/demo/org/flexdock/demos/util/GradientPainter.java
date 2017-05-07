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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

/**
 * @author Christopher Butler
 */
public class GradientPainter {
    private Color startColor;
    private Color midColor;


    public GradientPainter(Color start, Color mid) {
        startColor = start;
        midColor = mid;
    }

    public void paintGradient(JComponent comp, Graphics g) {
        int h = comp.getHeight();
        int w = comp.getWidth();
        int mid = w/2;

        Color bgColor = comp.getBackground();
        Color start = startColor==null? bgColor: startColor;
        Color middle = midColor==null? bgColor: midColor;

        GradientPaint firstHalf = new GradientPaint(0, 0, start, mid, 0, middle);
        GradientPaint secondHalf = new GradientPaint(mid, 0, middle, w, 0, bgColor);

        Graphics2D g2 = (Graphics2D)g;
        g2.setPaint(firstHalf);
        g2.fillRect(0, 0, mid, h);
        g2.setPaint(secondHalf);
        g2.fillRect(mid-1, 0, mid, h);
    }

    public Color getMidColor() {
        return midColor;
    }
    public void setMidColor(Color midColor) {
        this.midColor = midColor;
    }
    public Color getStartColor() {
        return startColor;
    }
    public void setStartColor(Color startColor) {
        this.startColor = startColor;
    }
}
