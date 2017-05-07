// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.graphics;

import java.awt.Graphics2D;
import avtas.map.Proj;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/**
 *
 * @author AFRL/RQQD
 */
public class MapIcon extends MapGraphic {

    Proj proj;
    int yoff = 0;
    int xoff = 0;
    //int anchorX;
    //int anchorY;
    double x = 0;
    double y = 0;
    double lat = 0;
    double lon = 0;
    private double rotation = 0;
    double projRotation = 0;
    private boolean rotateWithMap = false;
    Image orig_image = null;
    //BufferedImage paintingImage = null;
    int width = 0, height = 0;

    public MapIcon() {
    }

    public MapIcon(Image image) {
        this(0, 0, image);
    }

    public MapIcon(double lat, double lon, Image image) {
        this(lat, lon, image, 0, 0);
    }

    public MapIcon(double lat, double lon, Image image, int offset_x, int offset_y) {
        this.xoff = offset_x;
        this.yoff = offset_y;
        setLatLon(lat, lon);
        setImage(image);
        setRotateWithMap(true);
    }

    public void setImage(Image image) {
        this.orig_image = image;

        if (image == null) return;

        width = image.getWidth(null);
        height = image.getHeight(null);

        //int dim = (int) Math.hypot(width, height) + 1;
        //paintingImage = new BufferedImage(dim, dim, BufferedImage.TYPE_INT_ARGB);

        setScreenShape(new Rectangle(width, height));
        setRotation(rotation);
    }

    public void setLatLon(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setOffset(int x, int y) {
        xoff = x;
        yoff = y;
        setImage(orig_image);
    }

    public void paint(Graphics2D g2) {
        
        if (!isProjected() || !isVisible()) {
            return;
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        //g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        //g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);


        g2.translate(x, y);

        if (!rotateWithMap) {
            g2.rotate(projRotation);
        }
        if (rotation != 0)
            g2.rotate(rotation);

        if (orig_image != null) {
            g2.translate(-width * 0.5, -height * 0.5);
            g2.drawImage(orig_image, 0, 0, null);
        }
    }

    public void project(Proj proj) {
        setProjected(false);

        if (proj != null && orig_image != null) {
            x = proj.getX(lon);
            y = proj.getY(lat);
            projRotation = proj.getRotation();

            setScreenShape(new Rectangle((int) (x - width / 2.), (int) (y - height / 2.), width, height));
            setProjected(true);
        }
    }

    /**
     * returns rotation in radians
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * sets the rotation in radians
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
//        if (Math.abs(rotation - this.rotation) > 0.001) {
//            this.rotation = rotation;
//            Graphics2D g2 = paintingImage.createGraphics();
//            g2.setComposite(AlphaComposite.Clear);
//            g2.setColor(Colors.TRANSPARENT);
//            g2.fillRect(0, 0, paintingImage.getWidth(), paintingImage.getHeight());
//            g2.setComposite(AlphaComposite.SrcOver);
//
//            g2.translate(0.5 * (paintingImage.getWidth()), 0.5 * (paintingImage.getHeight()));
//            g2.rotate(rotation);
//
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//
//            g2.translate(-width*0.5, -height*0.5);
//            g2.drawImage(orig_image, 0, 0, null);
//            g2.dispose();
//        }

    }

    public boolean rotatesWithMap() {
        return rotateWithMap;
    }

    public void setRotateWithMap(boolean rotateWithMap) {
        this.rotateWithMap = rotateWithMap;
    }

    public Image getImage() {
        return orig_image;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */