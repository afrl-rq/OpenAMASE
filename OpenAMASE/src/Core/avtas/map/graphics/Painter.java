// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.graphics;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

/**
 * Controls the painting of graphics onto the map surface.  The painter can be
 * used to create advanced stroking, coloring, and filling operations.
 *
 * @author AFRL/RQQD
 */
public abstract class Painter {

    /** returns a painter controlled by color and line width of 1.
     */
    public static Painter createPainter(Paint color) {
        return new DefaultPainter(color, 1f);
    }

    /** returns a painter specified by the given by color and line width
     */
    public static Painter createPainter(Paint color, float lineWidth) {
        return new DefaultPainter(color, lineWidth);
    }

    /** returns a painter specified by the given by color and line stroke
     */
    public static Painter createPainter(Paint color, Stroke lineStroke) {
        return new DefaultPainter(color, lineStroke);
    }

    /** returns a painter that draws shapes with an outline.
     */
    public static Painter createOutlinePainter(Paint lineColor, Paint outlineColor,
            float lineWidth) {
        return new OutlinePainter(lineColor, outlineColor, lineWidth);
    }

    public abstract void paint(Graphics2D g, Shape s);

    public static class DefaultPainter extends Painter {

        public Paint color;
        public Stroke stroke;

        public DefaultPainter(Paint color, float lineWidth) {
            this.color = color;
            stroke = new BasicStroke(lineWidth);
        }

        public DefaultPainter(Paint color, Stroke lineStroke) {
            this.color = color;
            stroke = lineStroke;
        }

        @Override
        public void paint(Graphics2D g, Shape s) {
            g.setPaint(color);
            g.setStroke(stroke);
            g.draw(s);
        }
    }

    private static class OutlinePainter extends Painter {

        private Paint lineColor;
        private Paint outlineColor;
        private Stroke lineStroke;
        private Stroke outlineStroke;

        /** creaates a painter with an outline surrounding the shape.
         *
         * @param lineColor  The paint of the shape
         * @param outlineColor the paint of the outline
         * @param lineWidth the width of the shape stroke.  The outline will
         * be 1/2 the stroke width on each side of the shape
         */
        public OutlinePainter(Paint lineColor, Paint outlineColor,
                float lineWidth) {
            this.lineColor = lineColor;
            this.outlineColor = outlineColor;
            this.lineStroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
            this.outlineStroke = new BasicStroke(lineWidth*2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        }

        @Override
        public void paint(Graphics2D g, Shape s) {
            g.setPaint(outlineColor);
            g.setStroke(outlineStroke);
            g.draw(s);
            g.setStroke(lineStroke);
            g.setPaint(lineColor);
            g.draw(s);
        }
    }


    // some pre-defined strokes

    /** A simple dash stroke */
    public static BasicStroke BasicDashStroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_BEVEL, 1, new float[]{5,5}, 0);

    /** Creates a simple dashed stroke with the given phase, in pixels */
    public static BasicStroke createDashStroke(float on, float off) {
        return new BasicStroke(1, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_BEVEL, 1, new float[]{on, off}, 0);
    }
}



/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */