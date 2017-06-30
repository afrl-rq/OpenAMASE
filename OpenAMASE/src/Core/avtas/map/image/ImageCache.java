// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.image;

import avtas.map.Proj;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

/**
 * Creates a single cache for storing images.  The cache tracks a list of source images.  This works with
 * MapScaledImage types.
 * 
 */
public class ImageCache {

    List<MapScaledImage> imageList = new ArrayList<MapScaledImage>();
    MapScaledImage buffer = null;
    boolean ready = false;
    int maxPixInMemory = 50 * 1024 * 1024;
    int pixInMemory = 0;
    double maxPixPerLon = 1E9;
    double minPixPerLon = 0;
    private boolean outOfZoomBounds;

    public ImageCache() {
    }

    /**
     * Initializes the image cache with an limit on the amount of memory this cache should consume at any time.
     * This tries to unload images when the images pixels occupy memory exceeding the specified limit.
     * @param maxMemory maxiumum memory to occupy, in megabytes.
     */
    public ImageCache(int maxMemory) {
        this.maxPixInMemory = maxMemory * 1024 * 1024 / 4;
    }

    /**
     * Adds an image to the cache.
     * @param image the image to be added
     */
    public void addImage(MapScaledImage image) {
        imageList.add(image);
    }

    /**  Adds an image for known corner points
     *
     * @param imageSrc image to be added
     * @param degNorthLat northern-most latitude of the image (deg)
     * @param degWestLon western-most longitude of the image (deg)
     * @param degSouthLat southern-most latitude of the image (deg)
     * @param degEastLon eastern-most longitude of the image (deg)
     */
    public void addImage(File imageSrc, double degNorthLat, double degWestLon, double degSouthLat, double degEastLon) {
        int[] wh = getImageSize(imageSrc);
        imageList.add(new MapScaledImage(imageSrc, degNorthLat, degWestLon, degSouthLat, degEastLon, wh[0], wh[1]));
    }

    /**
     * Sets the minimum scale for images to load.  If the projection zooms out farther than the minimum
     * scale specified, no image will load for display.
     *
     * @param pixPerLon minimum number of pixels per deg lon for this cache to display images.
     */
    public void setMinScale(double pixPerLon) {
        this.minPixPerLon = pixPerLon;
    }

    /**
     * Returns the minimum scale for images to load.  If the projection zooms out farther than the minimum
     * scale specified, no image will load for display..
     * @return the minimum scale value, in pixels per degree longitude.
     */
    public double getMinScale() {
        return minPixPerLon;
    }

    /**
     * Sets the maximum scale for images to load.  If the projection zooms in closer than the maximum
     * scale specified, no image will load for display.
     *
     * @param pixPerLon maximum number of pixels per deg lon for this cache to display images.
     */
    public void setMaxScale(double pixPerLon) {
        this.maxPixPerLon = pixPerLon;
    }

    /**
     * Returns the maximum scale for images to load.  If the projection zooms in closer than the maximum
     * scale specified, no image will load for display.
     * @return maximum number of pixels per deg lon for this cache to display images.
     */
    public double getMaxScale() {
        return maxPixPerLon;
    }

    /** Prepares this set of images to be painted on to an intermediate surface and returns
     *  a MapScaledImage containing the consolidated image.  The returned prepared image
     * will be of the width and height specified, with the projection center matching the
     * center of the image.
     *
     * @param view  current view for painting
     * @param width desired width for the prepared image
     * @param height desired height for the prepared image
     */
    public MapScaledImage getPreparedImage(Proj view, int width, int height) {

        if (view.getPixPerLon() > maxPixPerLon || view.getPixPerLon() < minPixPerLon) {
            return null;
        }

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        double lonwidth = 0.5 * width / view.getPixPerLon();
        double latheight = 0.5 * height / view.getPixPerLat();
        MapScaledImage tmpRaster = new MapScaledImage(img, view.getCenterLat() + latheight,
                view.getCenterLon() - lonwidth, view.getCenterLat() - latheight, view.getCenterLon() + lonwidth);

        Graphics2D g = img.createGraphics();

        double offset = view.getPixPerLon() * 360 * Math.signum(view.getCenterLon());

        for (MapScaledImage r : imageList) {
            r.project(view);
            if (r.isProjected()) {
                r.loadImage();
                pixInMemory += r.getImage().getWidth(null) * r.getImage().getHeight(null);
                r.paint(g);
                g.translate(offset, 0);
                r.paint(g);
                g.translate(-offset, 0);
            }
            if (pixInMemory > maxPixInMemory) {
                r.unloadImage();
            }
        }

        tmpRaster.project(view);

        return tmpRaster;
    }

    public void project(Proj proj) {
        if (proj.getPixPerLon() > maxPixPerLon || proj.getPixPerLon() < minPixPerLon) {
            outOfZoomBounds = true;
            return;
        }
        outOfZoomBounds = false;
        for (MapScaledImage img : imageList) {
            img.project(proj);
        }

    }

    public void paint(Graphics2D g) {
        if (!outOfZoomBounds) {
            for (MapScaledImage img : imageList) {
                img.paint(g);
            }
        }
    }

    /** returns [width, height] of the image being considered or [0,0] if a problem is encountered */
    public static int[] getImageSize(File imageSrc) {
        String suffix = imageSrc.getName().substring(imageSrc.getName().lastIndexOf(".") + 1);
        Iterator<ImageReader> it = ImageIO.getImageReadersBySuffix(suffix);
        if (it.hasNext()) {
            ImageReader reader = it.next();
            try {
                reader.setInput(ImageIO.createImageInputStream(imageSrc));
                return new int[]{reader.getWidth(0), reader.getHeight(0)};
            } catch (Exception ex) {
                ex.printStackTrace(); System.exit(1);
            }
        }
        return new int[]{0, 0};
    }

    Iterable<MapScaledImage> getImages() {
        return imageList;
    }

    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        System.out.println(infoflags);
        return false;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */