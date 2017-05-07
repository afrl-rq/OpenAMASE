// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.analysis;

import avtas.amase.util.CmasiUtils;
import afrl.cmasi.AirVehicleState;
import avtas.util.NavUtils;
import avtas.map.image.MapScaledImage;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import afrl.cmasi.AreaSearchTask;
import afrl.cmasi.Location3D;
import afrl.cmasi.SearchTask;
import afrl.cmasi.WavelengthBand;
import java.util.ArrayList;

/**
 * The <code>AreaSearchHighlight</code> class is used by the SearchTaskAnalysis
 * module to collect sensor coverage statistics on a search area, line, or point.
 * @author AFRL/RQQD
 */
public class AreaSearchHighlight extends MapScaledImage implements SearchGraphic {

    BufferedImage image;
    double nwlat, nwlon, selat, selon;
    Path2D shape;
    SearchPixel[][] pixelMap;
    int numLats, numLons;
    static float[] FILL_ARRAY = {255, 255, 0, 100};
    //static float[] FILL_ARRAY = FILL_COLOR.getRGBComponents(null);
    AreaSearchTask task;
    double cellSize;
    // if the search area is too large for the requested resolution, it sets the number 
    // of divisions to the max value
    static int MAXDIVISIONS = 50;

    /**
     * Constructs a new area search highlighter for the specified search task with
     * the given grid cell size.
     * @param cellSize The size of each grid cell in meters.
     * @param task The search task to manage.
     */
    public AreaSearchHighlight(double cellSize, AreaSearchTask task) {
        this.task = task;
        this.cellSize = cellSize;
        shape = CmasiUtils.convertPoly(task.getSearchArea());
        shape.setWindingRule(Path2D.WIND_EVEN_ODD);
        initialize();
    }

    /** {@inheritDoc } */
    public void initialize() {

        Rectangle2D bounds = shape.getBounds2D();
        double dlat = Math.toDegrees(cellSize / NavUtils.EARTH_EQ_RADIUS_M);
        double dlon = dlat * Math.cos(Math.toRadians(bounds.getCenterY()));

        numLons = (int) ((bounds.getWidth()) / dlon) + 1;
        numLats = (int) ((bounds.getHeight()) / dlat) + 1;

        if (numLats > MAXDIVISIONS) {
            numLats = MAXDIVISIONS;
            dlat = bounds.getHeight() / numLats;
        }

        if (numLons > MAXDIVISIONS) {
            numLons = MAXDIVISIONS;
            dlon = bounds.getWidth() / numLons;
        }

        pixelMap = new SearchPixel[numLons][numLats];
        image = new BufferedImage(numLons, numLats, BufferedImage.TYPE_INT_ARGB);

        super.setImage(image);
        super.setWorldBounds(bounds.getMaxY(), bounds.getMinX(), bounds.getMinY(), bounds.getMaxX());

        for (int i = 0; i < numLons; i++) {
            for (int j = 0; j < numLats; j++) {
                double pixLat = bounds.getMaxY() - dlat * (j - 0.5);
                double pixLon = bounds.getMinX() + dlon * (i + 0.5);
                Rectangle2D r = new Rectangle2D.Double();
                r.setFrameFromCenter(pixLon, pixLat, pixLon + dlon / 2., pixLat + dlat / 2.);

                if (shape.contains(r)) {
                    pixelMap[i][j] = new SearchPixel(pixLat, pixLon);
                }
            }
        }

    }

    /**
     * Returns <code>true</code> if the rectangle crosses the shape's boundary,
     * <code>false</code> otherwise.
     * @param points A collection of points representing a shape.
     * @param r The rectangle to be checked.
     * @return <code>true</code> if the rectangle crosses the shape's boundary,
     * <code>false</code> otherwise.
     */
    public static boolean intersects(ArrayList<Location3D> points, Rectangle2D r) {
        Location3D firstPt = points.get(0);
        Location3D secondPt = points.get(1);
        for (int i = 0; i < points.size() - 1; i++) {
            firstPt = points.get(i);
            secondPt = points.get(i + 1);
            if (r.intersectsLine(firstPt.getLongitude(), firstPt.getLatitude(), secondPt.getLongitude(), secondPt.getLatitude())) {
                return true;
            }
        }
        return false;
    }

    /** {@inheritDoc } */
    @Override
    public void processSensor(AirVehicleState avs, CameraModel model, double aglAlt) {

        Path2D footprint = model.getFootprint(avs);
        if (footprint == null) {
            return;
        }

        // don't do any further testing if the wavelength band doesn't match
        if (!task.getDesiredWavelengthBands().contains(model.getCameraConfig().getSupportedWavelengthBand()) 
            || task.getDesiredWavelengthBands().contains(WavelengthBand.AllAny)) {
            return;
        }

        if (!footprint.intersects(shape.getBounds2D())) return;

        for (int i = 0; i < pixelMap.length; i++) {
            SearchPixel[] searchPixels = pixelMap[i];
            for (int j = 0; j < searchPixels.length; j++) {
                SearchPixel p = searchPixels[j];
                if (p == null || p.seen) {
                    continue;
                }
                if (p.inSensorFOV(footprint)) {
                    
                    if (model.computeGSD(avs, p, aglAlt) > task.getGroundSampleDistance()) {
                        continue;
                    }

                    if (p.timeFirstSeen == 0) {
                        p.timeFirstSeen = avs.getTime();
                    }
                    // if its been a while (2x Dwell time) since this pixel has been seen, then
                    // set its first-time-seen value to the current time to "restart the clock"
                    if (avs.getTime() - p.timeFirstSeen > 2.0 * task.getDwellTime()) {
                        p.timeFirstSeen = avs.getTime();
                    }
                    p.totalTimeSeen = avs.getTime() - p.timeFirstSeen;
                    if (p.totalTimeSeen >= task.getDwellTime()) {
                        colorPixel(i, j);
                        p.seen = true;
                    }
                }
            }
        }
    }

    /**
     * Colors the specified pixel.
     * <br>
     * In general, this is not called directly, but is called from <code>processSensor</code>
     * @param i The x-axis coordinate of the pixel.
     * @param j The y-axis coordinate of the pixel.
     */
    private void colorPixel(int i, int j) {
        image.getRaster().setPixel(i, j, FILL_ARRAY);
    }

    /** {@inheritDoc } */
    public SearchPixel[][] getPixels() {
        return pixelMap;
    }

    /** {@inheritDoc } */
    public SearchTask getTask() {
        return task;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */