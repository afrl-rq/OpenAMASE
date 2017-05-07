// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.image;

import avtas.map.graphics.MapGraphic;
import avtas.map.Proj;
import avtas.map.util.WorldBounds;
import avtas.map.util.WorldMath;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.io.File;

/**
 *
 * @author AFRL/RQQD
 */
public class MapScaledImage extends MapGraphic {

    File imgFile = null;
    boolean dynamicLoad = false;
    boolean checkBounds = true;
    int imgWidth = 0;
    int imgHeight = 0;
    Image image = null;
    protected double ullat = 0;
    protected double ullon = 0;
    protected double dlon = 0;
    protected double dlat = 0;

    //AffineTransform xform = null;
    public MapScaledImage() {
    }

    public MapScaledImage(Image img, double ullat, double ullon, double lrlat, double lrlon) {

        setWorldBounds(ullat, ullon, lrlat, lrlon);
        setImage(img);

    }

    public MapScaledImage(File imgFile, WorldBounds bounds, int width, int height) {
        this(imgFile, bounds.getNorthLat(), bounds.getWestLon(), bounds.getSouthLat(), bounds.getEastLon(),
                width, height);
    }
    
    public MapScaledImage(File imgFile, WorldBounds bounds) {
        this(imgFile, bounds.getNorthLat(), bounds.getWestLon(), bounds.getSouthLat(), bounds.getEastLon(),
                0, 0);
    }
    
    public MapScaledImage(File imgFile, double ullat, double ullon, double lrlat, double lrlon) {
        this(imgFile, ullat, ullon, lrlat, lrlon, 0, 0);
    }

    public MapScaledImage(File imgFile, double ullat, double ullon, double lrlat, double lrlon, int width, int height) {

        setWorldBounds(ullat, ullon, lrlat, lrlon);

        imgWidth = width;
        imgHeight = height;

        this.imgFile = imgFile;
        this.dynamicLoad = true;

    }

    public void setImage(Image img) {
        this.image = img;
        if (img != null) {
            imgWidth = img.getWidth(null);
            imgHeight = img.getHeight(null);
        }
    }

    public Image getImage() {
        return this.image;
    }

    public void setImageFile(File file, int height, int width) {
        imgFile = file;
        this.imgHeight = height;
        this.imgWidth = width;
    }

    public void setWorldBounds(double ullat, double ullon, double lrlat, double lrlon) {
        this.ullat = ullat;
        this.ullon = ullon;
        dlon = Math.abs(WorldMath.wrapLon(lrlon - ullon));
        dlat = ullat - lrlat;
    }

    public void checkBounds(boolean check) {
        this.checkBounds = check;
    }

    public Image loadImage() {
        if (imgFile != null && image == null) {
            try {
                image = ImageTracker.getImage(imgFile);
                if (image != null && (imgHeight == 0 || imgWidth == 0)) {
                    this.imgHeight = image.getHeight(null);
                    this.imgWidth = image.getWidth(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return image;
    }

    public void unloadImage() {
        if (imgFile != null && image != null) {
            ImageTracker.unload(imgFile);
            image = null;
        }
    }

    public void project(Proj proj) {
        setProjected(false);

        if (proj == null) {
            return;
        }

        if (WorldMath.inView(proj, ullat, ullon, ullat - dlat, ullon + dlon)) {

            setScreenShape(new Rectangle2D.Double(proj.getX(ullon), proj.getY(ullat),
                    proj.getPixPerLon() * dlon, proj.getPixPerLat() * dlat));

            if (dynamicLoad) {
                image = loadImage();
            }
            if (image != null) {
                setProjected(true);
            } else {
                setProjected(false);
            }
        } else {
            if (dynamicLoad) {
                unloadImage();
            }
            setProjected(false);
        }

    }

    public void paint(Graphics2D g2) {
        if (isProjected()) {
            Rectangle bounds = getBounds();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.drawImage(image, bounds.x, bounds.y, (int) bounds.getMaxX(), (int) bounds.getMaxY(),
                    0, 0, imgWidth, imgHeight, null);
        }

    }

    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */