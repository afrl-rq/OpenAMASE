// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.analysis;

import afrl.cmasi.AirVehicleState;
import afrl.cmasi.LineSearchTask;
import afrl.cmasi.SearchTask;
import afrl.cmasi.WavelengthBand;
import avtas.util.NavUtils;
import avtas.map.graphics.MapGraphicsList;
import avtas.map.graphics.MapLine;
import java.awt.BasicStroke;
import java.awt.geom.Path2D;

/**
 * The <code>LinearSearchHighlight</code> class manages the highlighting of a
 * linear search path.
 *
 * @author AFRL/RQQD
 */
public class LinearSearchHighlight extends MapGraphicsList<MapGraphicsList<MapLine>> implements SearchGraphic {

    
    private SearchPixel[][] pixelMap;
    private SearchTask task;

    private int MAX_DIVISIONS = 10;

    /**
     * Constructs a new linear search highlighter for the specified search task
     * with the given grid cell size.
     * @param cellSize The size of each grid cell in meters.
     * @param task The search task to manage.
     */
    public LinearSearchHighlight(double cellSize, LineSearchTask task) {
        this.task = task;
        if (task.getPointList().size() < 2) {
            return;
        }
        clear();
        pixelMap = new SearchPixel[task.getPointList().size() - 1][];

        double loc1_lat = Math.toRadians(task.getPointList().get(0).getLatitude());
        double loc1_lon = Math.toRadians(task.getPointList().get(0).getLongitude());
        double loc2_lat, loc2_lon;

        for (int i = 0; i < task.getPointList().size() - 1; i++) {
            loc2_lat = Math.toRadians(task.getPointList().get(i + 1).getLatitude());
            loc2_lon = Math.toRadians(task.getPointList().get(i + 1).getLongitude());

            //double radHdg = NavUtils.headingBetween(loc1_lat, loc1_lon, loc2_lat, loc2_lon);
            double dist = NavUtils.distance(loc1_lat, loc1_lon, loc2_lat, loc2_lon);
            int numSteps = (int) (dist / cellSize) + 1;
            numSteps = Math.min(numSteps, MAX_DIVISIONS);
            //double distStep = dist / numSteps;
            double dlat = Math.toDegrees(loc2_lat - loc1_lat) / numSteps;
            double dlon = Math.toDegrees(loc2_lon - loc1_lon) / numSteps;

            pixelMap[i] = new SearchPixel[numSteps];
            double lat1 = Math.toDegrees(loc1_lat);
            double lon1 = Math.toDegrees(loc1_lon);
            double lon2, lat2;

            add(new MapGraphicsList<MapLine>());

            for (int j = 0; j < numSteps; j++) {
                lat2 = lat1 + dlat;
                lon2 = lon1 + dlon;
                MapLine pt = new MapLine(lat1, lon1, lat2, lon2);
                pt.setVisible(false);
                get(i).add(pt);
                pixelMap[i][j] = new SearchPixel(lat1, lon1);
                lon1 = lon2;
                lat1 = lat2;
            }

            loc1_lat = loc2_lat;
            loc1_lon = loc2_lon;

        }

        setFill(FILL_COLOR);
        setPainter(FILL_COLOR, 4);
    }

     /** {@inheritDoc} */
    public void initialize() {
        //setVisible(false);
        for (SearchPixel[] pp : pixelMap) {
            for (SearchPixel p : pp) {
                p.seen = false;
                p.timeFirstSeen = 0;
                p.totalTimeSeen = 0;
            }
            for (MapGraphicsList<MapLine> mg : this) {
                for (MapLine mp : mg) {
                    mp.setVisible(false);
                }
            }
        }
    }

     /** {@inheritDoc} */
    public void processSensor(AirVehicleState avs, CameraModel model, double aglAlt) {

        // don't do any further testing if the wavelength band doesn't match
        if (!task.getDesiredWavelengthBands().contains(model.getCameraConfig().getSupportedWavelengthBand()) 
            || task.getDesiredWavelengthBands().contains(WavelengthBand.AllAny)) {
            return;
        }

        Path2D footprint = model.getFootprint(avs);
        if (footprint == null) {
            return;
        }

        for (int i = 0; i < pixelMap.length; i++) {
            for (int j = 0; j < pixelMap[i].length; j++) {
                SearchPixel p = pixelMap[i][j];
                if (!p.seen && p.inSensorFOV(footprint)) {

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
                        get(i).get(j).setVisible(true);
                        p.seen = true;
                    }
                }
            }
        }
    }

     /** {@inheritDoc} */
    public SearchPixel[][] getPixels() {
        return pixelMap;
    }

     /** {@inheritDoc} */
    public SearchTask getTask() {
        return task;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */