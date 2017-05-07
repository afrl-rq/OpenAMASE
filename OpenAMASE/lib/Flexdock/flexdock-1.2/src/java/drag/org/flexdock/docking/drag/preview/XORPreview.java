// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.docking.drag.preview;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.util.Map;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.drag.effects.DefaultPreview;

public class XORPreview extends DefaultPreview {

    public void drawPreview(Graphics2D g, Polygon p, Dockable dockable, Map dragInfo) {
        float[] pattern = { 1.0f, 1.0f };
        Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f, pattern, 0f);
        g.setStroke(stroke);

        g.setColor(Color.BLACK);
        g.setXORMode(Color.WHITE);
        drawPolygon(g, p, 3);
    }

    private void drawPolygon(Graphics2D g, Polygon p, int thickness) {
        Point center = getCenterOfGravity(p);
        for(int i=0; i<thickness; i++) {
            g.drawPolygon(p);
            gravitate(p, center, 1);
        }
    }

    private void gravitate(Polygon p, Point center, int step) {
        int len = p.npoints;

        for(int i=0; i<len; i++) {
            int deltaX = center.x > p.xpoints[i]? step: -step;
            int deltaY = center.y > p.ypoints[i]? step: -step;
            p.xpoints[i] += deltaX;
            p.ypoints[i] += deltaY;
        }
    }

    private Point getCenterOfGravity(Polygon p) {
        int x = 0;
        int y = 0;
        int len = p.npoints;
        for(int i=0; i<len; i++) {
            x += p.xpoints[i];
            y += p.ypoints[i];
        }
        return new Point(x/len, y/len);
    }
}
