// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

/**
 * Provides a stripe Paint object for filling and painting Java2D objects.  This
 * class allows the user to specify horizontal, vertical, 45 degree forward slant,
 * and 45 degree backward slant.  Colors and widths are also user specified.
 * @author AFRL/RQQD
 */
public class StripePaint implements Paint {

    static final int SIZE = 10;
    TexturePaint texPaint;

    /** Describes the orientation of the stripes. */
    public static enum Orientation {

        Horizontal(90), Vertical(0), ForwardSlant(45), BackwardSlant(-45);
        double angle;

        Orientation(int angle) {
            this.angle = Math.toRadians(angle);
        }
    }

    /**
     * Creates a stripe paint. 
     * 
     * @param color1 color of the first stripe. (null = transparent)
     * @param width1 width of the first stripe, in pixels
     * @param color2 color of the second stripe.  (null = transparent)
     * @param width2 width of the second stripe in pixels
     * @param orient orientation of the stripes.
     */
    public StripePaint(Color color1, int width1, Color color2, int width2, Orientation orient) {

        if (color1 == null) color1 = new Color(0, 0, 0, 0);
        if (color2 == null) color2 = new Color(0, 0, 0, 0);

        double rotation = orient.angle;

        int w, h;
        if (orient == Orientation.Horizontal) {
            w = 1;
            h = width1 + width2;
        }
        else if (orient == Orientation.Vertical) {
            w = width1 + width2;
            h = 1;
        }
        else {
            // this is only practical for angles that do not approach 0 or 90.
            // a generalized solution means that the texture grows to infinity in either
            // length or width as the angle approaches 0 or 90 degrees.
            // can be used around 60 degrees without a large texture produced.
            w = (int) Math.round(1.0 / Math.abs(Math.cos(rotation)) * (width1 + width2));
            h = (int) Math.round(1.0 / Math.abs(Math.sin(rotation)) * (width1 + width2));
        }


        // don't add an alpha channel if we are using Opaque colors.
        int imgType;
        if (color1.getAlpha() == 255 && color2.getAlpha() == 255) {
            imgType = BufferedImage.TYPE_INT_ARGB;
        }
        else {
            imgType = BufferedImage.TYPE_INT_ARGB_PRE;
        }
        
        BufferedImage img = new BufferedImage(w, h, imgType);

        texPaint = new TexturePaint(img, new Rectangle(0, 0, w, h));

        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        //g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

        g.rotate(rotation, w/2., h/2.);

        
        g.setColor(color1);
        g.fillRect(0, -h, width1, h*3);
        g.fillRect(width1+width2, -h, width1, h*3);
        g.fillRect(-width1-width2, -h, width1, h*3);
        
        g.setColor(color2);
        g.fillRect(width1, -h, width2, h*3);
        g.fillRect(2*width1 + width2, -h, width2, h*3);
        g.fillRect(-width2, -h, width2, h*3);

        g.dispose();


    }

    @Override
    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
        return texPaint.createContext(cm, deviceBounds, userBounds, null, hints);
    }

    @Override
    public int getTransparency() {
        return texPaint.getTransparency();
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */