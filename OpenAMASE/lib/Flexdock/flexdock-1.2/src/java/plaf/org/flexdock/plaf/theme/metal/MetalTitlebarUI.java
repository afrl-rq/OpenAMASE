// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on 06.04.2005
 */
package org.flexdock.plaf.theme.metal;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.plaf.metal.MetalLookAndFeel;

import org.flexdock.plaf.theme.TitlebarUI;
import org.flexdock.view.Button;
import org.flexdock.view.Titlebar;

/**
 * @author Claudio Romano
 */
public class MetalTitlebarUI extends TitlebarUI {

    protected void paintBackground(Graphics g, Titlebar titlebar) {
        Rectangle paintArea = getPaintRect( titlebar);
        g.translate(paintArea.x, paintArea.y);
        g.setColor(getBackgroundColor( titlebar.isActive()));
        g.fillRect(0, 0, paintArea.width, paintArea.height);
        g.translate(-paintArea.x, -paintArea.y);

        Rectangle paintAreaer = getPainterRect( g, titlebar);
        g.translate(paintAreaer.x, paintAreaer.y);
        painter.paint(g, paintAreaer.width, paintAreaer.height, titlebar.isActive(), titlebar);
        g.translate(-paintAreaer.x, -paintAreaer.y);
    }

    protected Color getBackgroundColor(boolean active) {
        return active ? MetalLookAndFeel.getPrimaryControl() : MetalLookAndFeel.getControl();
    }

    protected int getPainterX(Graphics g, Titlebar titlebar) {
        int paintX = getTextLocation( getIconRect( titlebar));
        Rectangle2D rect = g.getFontMetrics(super.getFont()).getStringBounds(titlebar.getText(),g);
        paintX += (int)rect.getWidth();
        paintX +=10;
        return paintX;
    }

    protected int getPainterWidth(Graphics g, Titlebar titlebar) {
        int buttonWidth = 0;
        Component[] c = titlebar.getComponents();
        for (int i = 0; i < c.length; i++) {
            if (!(c[i] instanceof Button))
                continue;

            Button b = (Button) c[i];
            buttonWidth = b.getHeight();
            break;
        }

        int paintY = (getButtonMargin() + buttonWidth) * 2;
        paintY += 5;
        return paintY;
    }

    private Rectangle getPainterRect( Graphics g, Titlebar titlebar) {
        Rectangle painterRectangle = getPaintRect( titlebar);
        painterRectangle.x = getPainterX( g, titlebar);

        painterRectangle.width = titlebar.getWidth()-painterRectangle.x;
        painterRectangle.width -= getPainterWidth( g, titlebar);

        painterRectangle.y +=3;
        painterRectangle.height -= 6;
        return painterRectangle;
    }
}
