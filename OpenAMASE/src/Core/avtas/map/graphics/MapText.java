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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.UIManager;

/**
 *
 * @author AFRL/RQQD
 */
public class MapText extends MapGraphic {

    Proj proj;
    int yoff = 0;
    int xoff = 0;
    double x = 0;
    double y = 0;
    double lat = 0;
    double lon = 0;
    private double rotation = 0;
    double projRotation = 0;
    JLabel label = new JLabel();
    BufferedImage textImage = null;
    private boolean rotateWithMap = false;

    public MapText() {
    }

    public MapText(String text) {
        this(0, 0, text);
    }

    public MapText(double lat, double lon, String text) {
        this(lat, lon, text, 0, 0);
    }

    public MapText(double lat, double lon, String text, int offset_x, int offset_y) {
        this.xoff = offset_x;
        this.yoff = offset_y;
        setLatLon(lat, lon);

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


        if (label.getWidth() > 0 && label.getHeight() > 0) {
            BufferedImage tmpImage = new BufferedImage(label.getWidth(), label.getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D textGraphics = tmpImage.createGraphics();
            //textGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            //textGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            label.paint(textGraphics);
            textGraphics.dispose();
            textImage = tmpImage;
        }
        else {
            textImage = null;
        }

        //setProjected(false);
    }

    public void setLatLon(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        //setProjected(false);
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public Font getFont() {
        return label.getFont();
    }

    public void setColor(Paint color) {
        label.setForeground((Color) color);
        setText(label.getText());
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

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            if (textImage != null) {
                Rectangle lb = label.getBounds();

                g2.translate(x, y);
                g2.rotate(rotation + projRotation);
                g2.translate(xoff, yoff);

                if (getFill() != null) {
                    g2.setPaint(getFill());
                    g2.fill(lb);
                }
                g2.translate(lb.x, lb.y);
                //label.paint(g2);
                g2.drawImage(textImage, null, null);
            }

        }
    }

    public void project(Proj proj) {
        setProjected(false);

        if (proj != null) {
            x = proj.getX(lon);
            y = proj.getY(lat);
            projRotation = proj.getRotation();
            if (rotateWithMap) {
                rotation = -projRotation;
            }
            //getBounds().setLocation( (int) x, (int) y);
            setProjected(true);
            setScreenShape(getBounds());
        }
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

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public boolean rotatesWithMap() {
        return rotateWithMap;
    }

    public void setRotateWithMap(boolean rotateWithMap) {
        this.rotateWithMap = rotateWithMap;
    }

    @Override
    public boolean intersects(Rectangle2D otherShape) {
        return otherShape == null ? false : otherShape.intersects(x, y, label.getWidth(), label.getHeight());
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */