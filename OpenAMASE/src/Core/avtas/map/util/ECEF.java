// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.util;

import avtas.util.NavUtils;



/**
 * Translates LLA (lat, lon, alt) to/from Earth-Centered, Earth-Fixed coordinates.  This is a coordinate
 * system with the origin at the center of the earth.  The ECEF_x-axis intercepts the
 * junction of the prime meridian and the equator.  The Z-axis is through the north
 * pole and the ECEF y-axis forms a right-hand rule.
 * 
 * Algorithms are based on "Datum Transformations of GPS Positions", μ-blox ag 
 * Gloriastrasse 35, CH-8092 Zürich, Switzerland (http://www.u-blox.ch)
 * 
 * Conversion to/from ECEF is done using the WGS84 datum.
 * 
 * @author AFRL/RQQD
 */
public class ECEF {

    static final double a = NavUtils.EARTH_EQ_RADIUS_M;
    static final double b = NavUtils.POLAR_RADIUS_M;
    static final double ff = 298.257224;
    static final double e = Math.sqrt( (a * a + b * b) / ( a * a) );
    static final double ep = Math.sqrt((a * a + b * b) / (b * b));

    /**
     * Converts ECEF coordinates to lat, lon, alt
     * @param x ECEF x coordinate (meters)
     * @param y ECEF y coordinate (meters)
     * @param z ECEF z coordinate (meters)
     * @return array of [lat, lon, alt]  (lat, lon in radians, alt in meters)
     */
    protected static double[] toLatLon(double x, double y, double z) {

 
        double lambda = Math.atan2(y, x);
        double p = Math.sqrt(x * x + y * y);

        double theta = Math.atan2(z * a, p * b);
        double sintheta = Math.sin(theta);
        double costheta = Math.cos(theta);

        double phi = Math.atan2(z + ep * ep * b * sintheta * sintheta * sintheta,
                p - e * e * a * costheta * costheta * costheta);

        double sinphi = Math.sin(phi);
        double cosphi = Math.cos(phi);
        double N = a / Math.sqrt(1 - e * e * sinphi * sinphi );
        double h = p / cosphi - N;

        return new double[] {phi, lambda, h};
    }

    /**
     * Converts a LLA point to ECEF
     * @param lat latitude (radians)
     * @param lon longitude (radians)
     * @param alt altitude (meters)
     * @return ECEF location [x,y,z] in meters
     */
    public static double[] fromLatLon(double lat, double lon, double alt) {

        double sinlat = Math.sin(lat);
        double coslat = Math.cos(lat);
        double sinlon = Math.sin(lon);
        double coslon = Math.cos(lon);

        double N = a / Math.sqrt(1 - e * e * sinlat * sinlat);
        double x = (N + alt) * coslat * coslon;
        double y = (N + alt) * coslat * sinlon;
        double z = (N * b * b / (a * a) + alt) * sinlat;

        return new double[] {x, y, z};
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */