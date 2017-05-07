// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.graphics;

import avtas.map.Proj;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * An implementation of text that can be drawn on the map in screen coordinates.  This
 * allows text to stay in a fixed location irregardless of the current map projection.
 * @author AFRL/RQQD
 */
public class ScreenText extends MapGraphic{

    int yoff = 0;
    int xoff = 0;
    double x = 0;
    double y = 0;
    JLabel label = new JLabel();

    public ScreenText() {
    }

    public ScreenText(String text) {
        this(0, 0, text);
    }

    public ScreenText(int x, int y, String text) {
        this(x, y, text, 0, 0);
    }

    public ScreenText(int x, int y, String text, int offset_x, int offset_y) {
        this.xoff = offset_x;
        this.yoff = offset_y;
        setScreenLocation(x, y);

        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);

        setText(text);
    }

    public void setText(String text) {

        label.setText(text);
        int width = label.getPreferredSize().width;
        int height = label.getPreferredSize().height;
        int anchorX, anchorY;

        switch (label.getHorizontalAlignment()) {
            case SwingConstants.RIGHT:
                anchorX = -width;
                break;
            case SwingConstants.CENTER:
                anchorX = -width / 2;
                break;
            default:
                anchorX = 0;
        }

        switch (label.getVerticalAlignment()) {
            case SwingConstants.BOTTOM:
                anchorY = -height;
                break;
            case SwingConstants.CENTER:
                anchorY = -height / 2;
                break;
            default:
                anchorY = 0;
        }

        label.setBounds(new Rectangle(anchorX, anchorY, width, height));

        setProjected(false);
    }

    /** Sets the text location relative to the parent map.
     *
     * @param x horizontal direction right of the upper left-hand corner
     * @param y vertical direction, down from the upper left-hand corner
     */
    public void setScreenLocation(int x, int y) {
        this.x = x;
        this.y = y;
        setProjected(false);
    }

    public Font getFont() {
        return label.getFont();
    }

    public void setColor(Paint color) {
        label.setForeground((Color) color);
    }

    public Paint getColor() {
        return label.getForeground();
    }

    public void setFont(Font font) {
        if (font != null) {
            label.setFont(font);
            setText(label.getText());
        }

    }

    public void setOffset(int x, int y) {
        xoff = x;
        yoff = y;
        setText(label.getText());
    }

    public void setHorizontalAlignment(int align) {
        label.setHorizontalAlignment(align);
        setText(label.getText());
    }

    public void setVerticalAlignment(int align) {
        label.setVerticalAlignment(align);
        setText(label.getText());
    }

    public void paint(Graphics2D g2) {
        if (isVisible()) {
            Rectangle lb = label.getBounds();
            g2.translate(x, y);
            g2.translate(xoff, yoff);

            g2.translate(lb.x, lb.y);
            label.paint(g2);

        }
    }

    public void project(Proj proj) {
        setProjected(true);
    }

    @Override
    public Rectangle getBounds() {
        if (isProjected()) {
            Rectangle r = label.getBounds();
            r.translate((int) x + xoff, (int) y + yoff);
            return r;
        }
        return null;
    }



    @Override
    public boolean intersects(Rectangle2D otherShape) {
        return otherShape == null ? false : otherShape.intersects(x, y, label.getWidth(), label.getHeight());
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */