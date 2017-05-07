// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.graphics;

import avtas.util.WindowUtils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 * Implements a shape for the end point of a line
 *
 * @author AFRL/RQQD
 */
public class Decoration {

    public static enum DecorationType {

        OpenArrow,
        ClosedArrow,
        Round,
        FixedSquare,
        RotatedSquare,
        Flat;
    }
    protected int size;
    protected DecorationType type;
    Shape shape;
    boolean filled = false;
    boolean rotate = true;

    public Decoration() {
        this(DecorationType.OpenArrow, 5);
    }

    public Decoration(DecorationType type, int size) {
        setSize(size);
        setType(type);
    }

    public void setSize(int size) {
        this.size = size;
        if (shape != null) {
            double scale = size / shape.getBounds2D().getWidth();
            shape = AffineTransform.getScaleInstance(scale, scale).createTransformedShape(shape);
        }
    }

    public int getSize() {
        return size;
    }

    public void setType(DecorationType type) {
        this.type = type;
        rotate = true;
        switch (type) {
            case OpenArrow:
                Path2D path = new Path2D.Double();
                path.moveTo(-0.5, 0.5);
                path.lineTo(0, 0);
                path.lineTo(0.5, 0.5);
                shape = path;
                break;
            case ClosedArrow:
                Path2D path2 = new Path2D.Double();
                path2.moveTo(-0.25, 0.5);
                path2.lineTo(0, 0);
                path2.lineTo(0.25, 0.5);
                path2.lineTo(0, 0.25);
                path2.closePath();
                shape = path2;
                break;
            case Round:
                shape = new Ellipse2D.Double(-0.5, -0.5, 1, 1);
                rotate = false;
                break;
            case RotatedSquare:
                shape = new Rectangle2D.Double(-0.5, -0.5, 1, 1);
                break;
            case FixedSquare:
                shape = new Rectangle2D.Double(-0.5, -0.5, 1, 1);
                rotate = false;
                break;
            case Flat:
                shape = new Line2D.Double(-0.5, 0, 0.5, 0);
                break;
            default:
                setType(DecorationType.OpenArrow);
                return;
        }
        setSize(getSize());
    }

    public DecorationType getType() {
        return type;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public boolean isFilled() {
        return filled;
    }

    public Shape getShape() {
        return shape;
    }

    public void draw(Graphics2D g, Painter painter, Paint fill, double x, double y, double orient_rad) {
        if (shape != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.translate(x, y);

            if (rotate) {
                g2.rotate(-orient_rad);
            }

            if (filled && fill != null) {
                g2.setPaint(fill);
                g2.fill(shape);
            }

            painter.paint(g2, shape);

            g2.dispose();
        }
    }

    public void draw(Graphics2D g, Paint lineColor, Paint fill, double x, double y, double orient_rad) {
        if (shape != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.translate(x, y);

            if (rotate) {
                g2.rotate(-orient_rad);
            }

            if (filled && fill != null) {
                g2.setPaint(fill);
                g2.fill(shape);
            }

            g2.setPaint(lineColor);
            g2.draw(shape);

            g2.dispose();
        }
    }

    public void drawStartPoint(Graphics2D g, Painter painter, Paint fill, Shape shape) {
        double[] coords = new double[6];

        PathIterator it = shape.getPathIterator(null, 5);
        if (!it.isDone()) {
            it.currentSegment(coords);
            if (!it.isDone()) {
                double x = coords[0];
                double y = coords[1];
                it.next();
                it.currentSegment(coords);
                double angle = Math.atan2(coords[0] - x, coords[1] - y);
                draw(g, painter, fill, x, y, angle);
            }
        }
    }

    public void drawEndPoint(Graphics2D g, Painter painter, Paint fill, Shape shape) {
        double[] last = new double[6];
        double[] curr = new double[6];

        PathIterator it = shape.getPathIterator(null, 5);
        while (!it.isDone()) {
            System.arraycopy(curr, 0, last, 0, 6);
            if (it.currentSegment(curr) == PathIterator.SEG_CLOSE) {
                return;
            }
            it.next();
        }

        double angle = Math.atan2(last[0] - curr[0], last[1] - curr[1]);
        draw(g, painter, fill, curr[0], curr[1], angle);


    }

    public static void draw(Graphics2D g, double x1, double y1, double x2, double y2, Decoration start, Decoration end) {
        draw(g, g.getColor(), g.getColor(), x1, y1, x2, y2, start, end);
    }

    public static void draw(Graphics2D g, Paint lineColor, Paint fillColor,
            double x1, double y1, double x2, double y2, Decoration start, Decoration end) {
        double angle = Math.atan2(x2 - x1, y2 - y1);
        if (start != null) {
            start.draw(g, g.getColor(), g.getColor(), x1, y1, angle);
        }
        if (end != null) {
            end.draw(g, g.getColor(), g.getColor(), x2, y2, angle + Math.PI);
        }
    }

    public static void main(String[] args) {

        final Decoration dec = new Decoration(DecorationType.ClosedArrow, 20);
        dec.filled = true;

        final Line2D line = new Line2D.Double(10, 10, 300, 200);

        final Painter painter = Painter.createPainter(Color.BLUE);

        final JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                g.clearRect(0, 0, getWidth(), getHeight());
                Graphics2D g2 = (Graphics2D) g;

                painter.paint(g2, line);

                //Decoration.draw(g2, line.getX1(), line.getY1(), line.getX2(), line.getY2(), dec, dec);
                dec.drawStartPoint(g2, painter, Color.BLACK, line);
                dec.drawEndPoint(g2, painter, null, line);
            }
        };

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                line.setLine(10, 10, e.getX(), e.getY());
                panel.repaint();
            }
        });


        WindowUtils.showApplicationWindow(panel).setSize(640, 480);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */