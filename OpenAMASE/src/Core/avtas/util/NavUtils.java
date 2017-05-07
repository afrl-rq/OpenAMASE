// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.util;

import static java.lang.Math.*;
import avtas.math.Vector3;

/**
 * Utility functions for navigation computations.
 * @author AFRL/RQQD
 */
public class NavUtils {

    private static double epsilon = 0.0818191908426;
    public static double gravEq = 9.7803267714;
    public static double gravAvg = 9.80665;
    /** equatorial radius in meters */
    public static final double EARTH_EQ_RADIUS_M = 6378137.0;
    /** polar radius in meters */
    public static final double POLAR_RADIUS_M = 6356752.31424518;
    /** polar circumference in meters */
    public static final double POLAR_CIRCUM_M = 2 * Math.PI * POLAR_RADIUS_M;
    /** equatorial circumference in meters */
    public static final double EARTH_EQ_CIRCUM_M = EARTH_EQ_RADIUS_M * 2 * Math.PI;
    /** 1/2 Pi for use in computations */
    public static final double HALF_PI = Math.PI / 2d;

    /** returns the equivalent east-west radius of the planet in meters at a given latitude in radians.
     *  from: Rogers R. M. Applied Mathematics in Integrated Navigation Systems. 
     *  American Institute of Aeronautics and Astronautics, Inc., 2000. ISBN 1-56347-397-6
     */
    public static double getRadius(double radLat) {
        double sinlat = Math.sin(radLat);
        //double coslat = a * Math.cos(radLat);
        return EARTH_EQ_RADIUS_M / sqrt(1 - epsilon * epsilon * sinlat * sinlat);
        //return sqrt( (a*a*coslat*coslat + b*b*sinlat * sinlat) / ( coslat*coslat + sinlat*sinlat) );
    }

    /**
     * Returns radians [lat, lon] based on change in north, east position (meters) in the WGS84 datum.
     * @param   radLat      Current latitude in radians.
     * @param   radLon      Current longitude in radians.
     * @param   meterAlt    Altitude in meters.
     * @param   dyNorth     Positional change in the north direction.
     * @param   dxEast      Positional change in the east direction.
     * @return              An array with the new latitude and longitude.
     */
    public static double[] wgs84LatLon(double radLat, double radLon, double meterAlt, double dyNorth, double dxEast) {

        double divisor = sqrt(1 - epsilon * epsilon * sin(radLat) * sin(radLat));
        double rMeridian = EARTH_EQ_RADIUS_M * (1. - epsilon * epsilon) / pow(divisor, 3.0);
        double rNormal = EARTH_EQ_RADIUS_M / divisor;
        double dLat = dyNorth / (rMeridian + meterAlt);
        double dLon = dxEast / ((rNormal + meterAlt) * cos(radLat));
        return new double[]{radLat + dLat, radLon + dLon};
    }

    /** 
     * Returns radian lat, lon based on change in north, east position (meters) using spherical earth.
     * @param   radLat      Current latitude in radians.
     * @param   radLon      Current longitude in radians.
     * @param   meterAlt    Altitude in meters.
     * @param   dyNorth     Positional change in the north direction.
     * @param   dxEast      Positional change in the east direction.
     * @return              An array with the new latitude and longitude.
     */
    public static double[] simpleLatLon(double radLat, double radLon, double meterAlt, double dyNorth, double dxEast) {

        return new double[]{radLat + dyNorth / (EARTH_EQ_RADIUS_M + meterAlt),
                    radLon + dxEast / ((EARTH_EQ_RADIUS_M + meterAlt) * cos(radLat))};
    }

    /** Returns latitude in radians using WGS-84 earth.
     *  @param radLat       The current latitude in radians.
     *  @param dyNorth      The positional change in the north direction.
     *  @param meterAlt     Altitude in meters.
     *  @return             The new latitude.
     */
    public static double getLat(double radLat, double dyNorth, double meterAlt) {
        return radLat + dyNorth / (POLAR_RADIUS_M + meterAlt);
    }

    /** Returns longitude in radians using spherical earth. See http://en.wikipedia.org/wiki/Longitude.
     *  @param radLat       Current latitude in radians.
     *  @param radLon       Current longitude in radians.
     *  @param dxEast       Positional change in the east direction.
     *  @param meterAlt     Altitude in meters.
     *  @return             The new longitude.
     *
     *
     */
    public static double getLon(double radLat, double radLon, double dxEast, double meterAlt) {
        return radLon + dxEast / ((getRadius(radLat) + meterAlt) * cos(radLat));
    }

    /** convenience method to get a location from a given start point and turn radius
     *  
     * @param radLat - starting latitude in radians
     * @param radLon - starting longitude in radians
     * @param startHdg - beginning heading in radians
     * @param endHdg - end of heading in radians
     * @param mTurnRadius - radius of turn
     * @return array of [lat, lon] in radians
     */
    public static double[] getLocFromTurn(double radLat, double radLon, double startHdg, double endHdg, double mTurnRadius) {
        double radHdgDiff = endHdg - startHdg;
        double e = mTurnRadius * sin(-radHdgDiff);
        double n = mTurnRadius * (1 - cos(-radHdgDiff));

        double mDist = sqrt(e * e + n * n);
        double az = atan2(e, n);

        return getLatLon(radLat, radLon, mDist, az);

    }

    /** convenience method to get a new point from a distance (meters) and azimuth from another point. 
     *  Uses spherical Earth calculations.
     * 
     * @param radLat - starting latitude in radians
     * @param radLon - starting longitude in radians
     * @param mDist - meters distance to traverse
     * @param radAzimuth - direction of traversal
     * @return array of [lat, lon] in radians
     */
    public static double[] getLatLon(double radLat, double radLon, double mDist, double radAzimuth) {
        double radDist = mDist / EARTH_EQ_RADIUS_M;

        double coslat1 = Math.cos(radLat);
        double sinlat1 = Math.sin(radLat);
        double cosAz = Math.cos(radAzimuth);
        double sinAz = Math.sin(radAzimuth);
        double sinc = Math.sin(radDist);
        double cosc = Math.cos(radDist);

        return new double[]{asin(sinlat1 * cosc + coslat1 * sinc * cosAz),
                    atan2(sinc * sinAz, coslat1 * cosc - sinlat1 * sinc * cosAz) + radLon
                };
    }

    /** Calculates the distance in meters from lat1, lon1 to lat2, lon2
     *  based on the great circle computations
     * @param rad_lat1       The first latitude.
     * @param rad_lon1       The first longitude.
     * @param rad_lat2       The second latitude.
     * @param rad_lon2       The second longitude.
     * @return               The distance between the points.
     *  @return the distance in radians
     */
    public static double distance(double rad_lat1, double rad_lon1, double rad_lat2, double rad_lon2) {
        return arcDistance(rad_lat1, rad_lon1, rad_lat2, rad_lon2) * EARTH_EQ_RADIUS_M;
    }

    /** Calculates the arc distance in radians from lat1, lon1 to lat2, lon2
     *  based on great circle computations (http://en.wikipedia.org/wiki/Great-circle_distance)
     * @param rad_lat1       The first latitude.
     * @param rad_lon1       The first longitude.
     * @param rad_lat2       The second latitude.
     * @param rad_lon2       The second longitude.
     *  @return the distance in radians
     */
    public static double arcDistance(double rad_lat1, double rad_lon1, double rad_lat2, double rad_lon2) {
        double latdiff = sin(((rad_lat2 - rad_lat1) / 2.));
        double londiff = sin((rad_lon2 - rad_lon1) / 2.);
        double rval = sqrt((latdiff * latdiff) + cos(rad_lat1) * cos(rad_lat2) * (londiff * londiff));

        return 2.0 * asin(rval);
    }

    /** Returns the angle to the horizon at the given altitude.  Result is theta, positive upwards.  Assumes spherical earth.
     *  @param  meter_alt       The altitude of the point.
     *  @return                 The angle to the horizon.
     */
    public static double angleToHorizon(double meter_alt) {
        return -Math.acos(EARTH_EQ_RADIUS_M / (EARTH_EQ_RADIUS_M + meter_alt));
    }

    /** Returns the visual distance to the horizon in meters.  Assumes spherical earth.
     * @param   meter_alt       The altitude of the point (height above ellipsoid in meters).
     * @return                  The distance to the horizon from the point (meters).
     */
    public static double distanceToHorizon(double meter_alt) {
        return Math.sqrt(2.0 * EARTH_EQ_RADIUS_M * meter_alt + meter_alt * meter_alt);
    }

    /** Calculates the approx heading from lat1, lon1 to lat2, lon2 (radians)
     *  based on the great circle computations (http://en.wikipedia.org/wiki/Great-circle_navigation)
     *  @param  lat1        First latitude in radians.
     *  @param  lon1        First longitude in radians.
     *  @param  lat2        Second latitude in radians.
     *  @param  lon2        Second longitude in radians.
     *  @return the heading in the [-PI..PI] domain
     */
    public static double headingBetween(double lat1, double lon1, double lat2, double lon2) {
        double londiff = lon2 - lon1;
        double coslat = cos(lat2);
        return atan2(coslat * sin(londiff), (cos(lat1) * sin(lat2) - sin(lat1) * coslat * cos(londiff)));
    }

    /**
     * Returns the apparent height of an object at the given distance away.  This method uses a spherical earth
     * model to return the flat earth equivalent of a given height.  Height values should be given as normal to the
     * center of the earth at that location.
     * @param arcdist_rad arc distance from the origin in radians
     * @param height_m height above the spheroid in meters (normal to surface)
     * @return the apparent height of the object at the given arc distance
     */
    public static double getApparentHeight(double arcdist_rad, double height_m) {
        double carc = Math.cos(arcdist_rad);
        return height_m * carc - EARTH_EQ_RADIUS_M * (1 - carc);
    }

    /**
     * Returns the elevation angle between two geographic points, from the perspective
     * of the first point.  Angles are relative to the local tangent plane of the 
     * first point, positive upwards.
     * 
     * heights in meters, geographic points in radians
     * 
     * @return elevation angle between point 1 and point 2, in degrees
     */
    public static double getElevationAngle(double radLat1, double radLon1, double h1, double radLat2,
            double radLon2, double h2) {
        double arc = arcDistance(radLat1, radLon1, radLat2, radLon2);
        double d = (EARTH_EQ_RADIUS_M + h2) * Math.sin(arc);
        double a = (EARTH_EQ_RADIUS_M + h1) - (EARTH_EQ_RADIUS_M + h2) * cos(arc);
        return -atan2(a,  d);
    }

    /** Computes the distance between a point and a line defined by two points.
     *  based on computations for distance from a 2D line.
     *  This method first converts the points into Cartesian coordinates, using distances
     *  from [lat1, lon1].  the point [lat1, lon1] is set to the origin.  Point [lat2, lon2]
     *  is used as the second point on the line and [lat3, lon3] are the coordinates of the point
     *  of interest. <br>
     *  If the points [lat1, lon1] or [lat2, lon2] are closer than than the distance to the 
     *  projected line, then the distance to the closest coordinate pair is returned.
     * 
     *  WARNING: this is based on spherical earth calculations, so there may be errors over long distances.
     *  changes in the headingBetween() and distance() methods to account for ellipsoidal effects
     *  would also improve this calculation.
     * 
     * @param radLat1 first point in line definition (radians latitude)
     * @param radLon1 first point in line definition (radians longitude)
     * @param radLat2 second point in line definition (radians latitude)
     * @param radLon2 second point in line definition (radians longitude)
     * @param radLat3 latitude of point of interest
     * @param radLon3 longitude of point of interest
     * @return distance (in meters) to the nearest point in the line segment and
     * the azimuth (radians) to that point
     */
    public static double[] distanceToLine(double radLat1, double radLon1, double radLat2, double radLon2,
            double radLat3, double radLon3) {

        //use point 3 as the origin.  get x and y values for pt1, pt2
        double R = getRadius(radLat3) * cos(radLat3);
        double x1 = (radLon1 - radLon3) * R;
        double x2 = (radLon2 - radLon3) * R;
        double y1 = (radLat1 - radLat3) * POLAR_RADIUS_M;
        double y2 = (radLat2 - radLat3) * POLAR_RADIUS_M;

        // get distances from pt1 to point 2;
        double dx = x2 - x1;
        double dy = y2 - y1;

        // since pt3 is origin, x3 = 0, y3 = 0;
        // compute dot-product of line p3p and line p1p2
        // divide by norm squared of line p1p2
        double u = (-x1 * dx + -y1 * dy) / (dx * dx + dy * dy);

        double xp;
        double yp;

        // the two points describing the line are the same
        if (Double.isNaN(u)) {
            xp = x1;
            yp = y1;
        }
        else if (u < 0) {  // point of intersection is before p1
            xp = x1;
            yp = y1;
        }
        else if (u > 1) { // point of intersection is after p2
            xp = x2;
            yp = y2;
        }
        else {
            xp = x1 + u * dx;
            yp = y1 + u * dy;
        }

        // get the azimuth between p3 (origin) and p (intercept)
        // this is in east-of-north frame
        double az = atan2(xp, yp);

        return new double[]{hypot(xp, yp), az};
    }

    /** computes the point on a line defined by [lat1, lon1], [lat2, lon] that is closest to the
     *  point [lat3, lon3].  This performs the distanceToLine() method and then calls getLatLon()
     *  using the result.
     * 
     * @param radLat1 first point in line definition (radians latitude)
     * @param radLon1 first point in line definition (radians longitude)
     * @param radLat2 second point in line definition (radians latitude)
     * @param radLon2 second point in line definition (radians longitude)
     * @param radLat3 latitude of point of interest
     * @param radLon3 longitude of point of interest
     * 
     *  WARNING: this is based on spherical earth calculations, so there may be errors over long distances.
     *  changes in the headingBetween() and distance() methods to account for ellipsoidal effects
     *  would also improve this calculation.
     * 
     * @return [lat, lon] in radians of the closest point in the line.  If either end point is closer,
     *  it returns the closest end point.
     */
    public static double[] getPointOnLine(double radLat1, double radLon1, double radLat2, double radLon2,
            double radLat3, double radLon3) {

        double[] distAz = distanceToLine(radLat1, radLon1, radLat2, radLon2, radLat3, radLon3);
        return getLatLon(radLat3, radLon3, distAz[0], distAz[1]);
    }
    
    /**
     * Returns the closest point on the polyline from the origin point.  This method interpolates
     * between the points to return the closest point on a polyline described by the given points.
     * @param originLat latitude of the origin in radians
     * @param originLon longitude of the origin in radians
     * @param points a list of [lat,lon] pairs in radians
     * @return the closest interpolated point on the polyline, or the origin point if the poly is empty (radians [lat,lon])
     */
    public static double[] getClosestPointOnPoly(double originLat, double originLon, double... points ) {
        
        if (points.length < 2) {
            return new double[]{originLat, originLon};
        }
        
        if (points.length == 2) {
            return new double[]{points[0], points[1]};
        }
        
        double[] closestPt = null;
        double closestDist = Double.MAX_VALUE;
        
        
        double radLat2 = points[0];
        double radLon2 = points[1];
        
        double radLon1, radLat1;
        for (int i=2; i<points.length; i+=2) {
            radLat1 = radLat2;
            radLon1 = radLon2;
            radLat2 = points[i];
            radLon2 = points[i+1];
            
            double[] dist = NavUtils.distanceToLine(radLat1, radLon1, radLat2, radLon2, originLat, originLon);
            double[] pt = NavUtils.getLatLon(originLat, originLon, dist[0], dist[1]);
            
            if (dist[0] < closestDist) {
                closestPt = pt;
                closestDist = dist[0];
            } 
        }
        
        return closestPt;
        
    }

    /**
     * Computes local acceleration due to gravity value based on latitude
     *
     * @param lat
     * @return g
     */
    public static double getG(double lat) {
        double j = 0.00193185138638;
        double k = 0.006699437999013;
        double g = (gravEq) * ((1 + j * pow(sin(lat), 2)) / (sqrt(1 - k * pow(sin(lat), 2))));
        return g;
    }

    /**
     *
     * @return average value for local acceleration due to gravity
     */
    public static double getG() {
        return gravAvg;
    }

    // fills a Vector with the 3-d components of gravity based on current euler angles and grav force
    public static void getGravAccel(Vector3 toFill, double theta, double phi, double g) {

        toFill.set1(-g * sin(theta));
        toFill.set2(g * sin(phi) * cos(theta));
        toFill.set3(g * cos(theta) * cos(phi));

    }
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */