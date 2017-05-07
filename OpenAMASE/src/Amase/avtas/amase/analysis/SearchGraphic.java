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
import afrl.cmasi.SearchTask;
import java.awt.Color;
import java.awt.geom.Path2D;

/**
 * The <code>SearchGraphic</code> interface provides a framework for both drawing the progress and gathering data of a specific <code>SearchTask</code>.
 * 
 * @author AFRL/RQQD
 */
public interface SearchGraphic {

    static Color FILL_COLOR = new Color(255, 255, 0, 100);


    /**
     * Returns the search pixel data for this module.
     */
    public abstract SearchPixel[][] getPixels();

    /**
     * Initializes the search module, clearing all recorded data. This is
     * typically called when initializing or resetting a scenario or search task.
     */
    public abstract void initialize();

    /**
     * Performs search highlighting and data recording for the search task for the
     * given air vehicle and sensor data.
     * @param avs The air vehicle state of the air vehicle performing the search task.
     * @param model The camera model to be evaluated
     * @param aglAlt height above ground of the aircraft (meters)
     */
    public abstract void processSensor(AirVehicleState avs, CameraModel model, double aglAlt);

    /**
     * Returns the search task for this module.
     */
    public abstract SearchTask getTask();

    /**
     * Contains information for a location (pixel) on the map related to searching,
     * such as: if the location has been seen by a sensor, when it was last seen,
     * and for how long. Also contains methods for determining if a sensor can
     * see the location.
     */
    public static class SearchPixel {

        double lat, lon;
        boolean seen = false;
        double timeFirstSeen = 0;
        double totalTimeSeen = 0;

        public SearchPixel(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        /**
         * Determines whether this <code>SearchPixel</code> is within the given
         * sensor's field of view.
         */
        public boolean inSensorFOV(Path2D footprint) {
            return (footprint.contains(lon, lat));
        }
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */