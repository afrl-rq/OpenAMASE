// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.layers;

import avtas.map.MapLayer;
import avtas.map.Proj;
import avtas.map.graphics.MapGraphicsList;
import avtas.map.image.ImageCache;
import avtas.map.image.MapScaledImage;
import avtas.map.util.WorldBounds;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;

/**
 *
 * @author AFRL/RQQD
 */
public class ImageLayer extends MapLayer {

    //private MapGraphicsList<MapScaledImage> rasterList = new MapGraphicsList<MapScaledImage>();
    private ImageCache imageCache = new ImageCache();

    /** creates an ImageLayer with a new ImageCache */
    public ImageLayer() {
    }

    public void addImage(MapScaledImage image) {
        imageCache.addImage(image);
    }

    /** adds a raster image to the list.  */
    public MapScaledImage addImage(WorldBounds worldBounds, File imageFile) {
        int[] wh = ImageCache.getImageSize(imageFile);
        MapScaledImage raster = new MapScaledImage(imageFile, worldBounds, wh[0], wh[1]);
        addImage(raster);
        return raster;
    }

    public MapScaledImage addImage(double degNorthLat, double degWestLon, File imageFile,
            double radPerPixX, double radPerPixY) {
        int[] wh = ImageCache.getImageSize(imageFile);
        double offx = wh[0] * radPerPixX;
        double offy = wh[1] * radPerPixY;
        WorldBounds bounds = new WorldBounds(degNorthLat, degWestLon, offx, offy);
        MapScaledImage raster = new MapScaledImage(imageFile, bounds, wh[0], wh[1]);
        addImage(raster);
        return raster;
    }

    /**
     * Sets the minimum scale for images to load.  If the projection zooms out farther than the minimum
     * scale specified, no image will load for display.
     *
     * @param pixPerLon minimum number of pixels per deg lon for this cache to display images.
     */
    public void setMinScale(double pixPerLon) {
        imageCache.setMinScale(pixPerLon);
    }

    /**
     * Returns the minimum scale for images to load.  If the projection zooms out farther than the minimum
     * scale specified, no image will load for display..
     * @return the minimum scale value, in pixels per degree longitude.
     */
    public double getMinScale() {
        return imageCache.getMinScale();
    }

    /**
     * Sets the maximum scale for images to load.  If the projection zooms in closer than the maximum
     * scale specified, no image will load for display.
     *
     * @param pixPerLon maximum number of pixels per deg lon for this cache to display images.
     */
    public void setMaxScale(double pixPerLon) {
        imageCache.setMaxScale(pixPerLon);
    }

    /**
     * Returns the maximum scale for images to load.  If the projection zooms in closer than the maximum
     * scale specified, no image will load for display.
     * @return maximum number of pixels per deg lon for this cache to display images.
     */
    public double getMaxScale() {
        return imageCache.getMaxScale();
    }

    @Override
    public void project(Proj view) {
        imageCache.project(view);
    }

    @Override
    public void paint(Graphics2D g) {
        imageCache.paint(g);
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */