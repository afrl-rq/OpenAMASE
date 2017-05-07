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
import afrl.cmasi.PointSearchTask;
import afrl.cmasi.SearchTask;
import afrl.cmasi.WavelengthBand;
import avtas.map.graphics.MapMarker;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

/**
 * Performs point search coverage analysis.
 * @author AFRL/RQQD
 */
public class PointSearchHighlight extends MapMarker implements SearchGraphic {

    private PointSearchTask task = null;
    private SearchPixel[][] pixMap = new SearchPixel[1][1];

    /**
     * Constructs a new point search highlighter for the specified search task.
     * @param task The search task to manage.
     */
    public PointSearchHighlight(PointSearchTask task) {
        this.task = task;
        this.pixMap[0][0] = new SearchPixel(task.getSearchLocation().getLatitude(),
                task.getSearchLocation().getLongitude());
        setLat(task.getSearchLocation().getLatitude());
        setLon(task.getSearchLocation().getLongitude());
        initialize();
    }

    /** {@inheritDoc} */
    public SearchPixel[][] getPixels() {
        return pixMap;
    }

    /** {@inheritDoc} */
    public void initialize() {
        pixMap[0][0].seen = false;
        pixMap[0][0].timeFirstSeen = 0;
        pixMap[0][0].totalTimeSeen = 0;
        setFill(FILL_COLOR);
        setPainter(FILL_COLOR, 1);
        setMarkerShape(new Ellipse2D.Float(0, 0, 10, 10));
        setVisible(false);
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
        SearchPixel p = pixMap[0][0];
        if (!p.seen && p.inSensorFOV(footprint)) {

            if (model.computeGSD(avs, p, aglAlt) > task.getGroundSampleDistance()) {
                return;
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
                setVisible(true);
            }
        }
    }

    /** {@inheritDoc} */
    public SearchTask getTask() {
        return task;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */