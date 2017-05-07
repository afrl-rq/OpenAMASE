// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on 21.03.2005
 */
package org.flexdock.plaf.resources.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.border.AbstractBorder;
import javax.swing.plaf.ColorUIResource;

import org.flexdock.plaf.resources.ColorResourceHandler;
import org.flexdock.plaf.resources.ResourceHandler;

/**
 * @author Claudio Romano
 */
public class RoundedBorderResource extends ResourceHandler {
    private static final ColorUIResource DEFAULT_COLOR = new ColorUIResource(Color.BLACK);

    public Object getResource(String data) {
        //pattern should be "color"
        String[] args = getArgs(data);
        ColorUIResource lightColor = args.length==1? getColor(args[0]): DEFAULT_COLOR;

        return new RoundedBorder(lightColor);
    }

    private ColorUIResource getColor(String data) {
        ColorUIResource color = ColorResourceHandler.parseHexColor(data);
        return data==null? DEFAULT_COLOR: color;
    }

    public static class RoundedBorder extends AbstractBorder {
        private Color color;

        public RoundedBorder(Color color) {
            this.color = color;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(color);
            int y2 = y + height - 1;

            // draw horizontal lines
            g.drawLine(1, y, width - 2, y);
            g.drawLine(1, y2, width - 2, y2);

            // draw vertical lines
            g.drawLine(0, y + 1, 0, y2 - 1);
            g.drawLine(width - 1, y + 1, width - 1, y2 - 1);
        }
    }
}
