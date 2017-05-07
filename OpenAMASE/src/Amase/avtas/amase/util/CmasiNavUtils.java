// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.amase.util;

import afrl.cmasi.AltitudeType;
import afrl.cmasi.Location3D;
import avtas.terrain.TerrainService;
import avtas.util.NavUtils;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements {@link NavUtils} capabilities using CMASI object types
 *
 * @author AFRL/RQQD
 */
public class CmasiNavUtils {

    /**
     * Returns the location of a point that is the given distance from the
     * starting point at the given heading
     *
     * @param pt1 starting point
     * @param dist_m distance from point (meters)
     * @param hdg_deg true heading from initial point (degrees)
     * @return new point
     */
    public static Location3D getPoint(Location3D pt1, double dist_m, double hdg_deg) {
        double[] llp = NavUtils.getLatLon(radLat(pt1), radLon(pt1), dist_m, Math.toRadians(hdg_deg));
        return new Location3D(Math.toDegrees(llp[0]), Math.toDegrees(llp[1]), 0, AltitudeType.MSL);
    }

    /**
     * Returns the location of a point that is the given distance from the
     * starting point at the given heading
     *
     * @param pt1 starting point
     * @param pt2 ending point (can be null). The end point will be filled with
     * return values
     * @param dist_m distance from point (meters)
     * @param hdg_deg true heading from initial point (degrees)
     * @return pt2 with the values of [lat,lon] for the new point
     */
    public static Location3D getPoint(Location3D pt1, Location3D pt2, double dist_m, double hdg_deg) {
        if (pt2 == null) {
            pt2 = new Location3D();
        }
        double[] llp = NavUtils.getLatLon(radLat(pt1), radLon(pt1), dist_m, Math.toRadians(hdg_deg));
        pt2.setLatitude(Math.toDegrees(llp[0]));
        pt2.setLongitude(Math.toDegrees(llp[1]));
        return pt2;
    }

    /**
     * Returns the great-circle distance between two points
     *
     * @return distance in meters using great-circle calculation
     */
    public static double distance(Location3D pt1, Location3D pt2) {
        return NavUtils.distance(radLat(pt1), radLon(pt1), radLat(pt2), radLon(pt2));
    }

    /**
     * Returns the heading between two points using great-circle computation
     *
     * @return heading, in degrees [-180..180]
     */
    public static double headingBetween(Location3D pt1, Location3D pt2) {
        return Math.toDegrees(
                NavUtils.headingBetween(radLat(pt1), radLon(pt1), radLat(pt2), radLon(pt2)));
    }

    /**
     * Returns the arc distance between two points in degrees using great-circle
     * computations
     *
     * @return arc-distance in degrees
     */
    public static double arcDistance(Location3D pt1, Location3D pt2) {
        return Math.toDegrees(
                NavUtils.arcDistance(radLat(pt1), radLon(pt1), radLat(pt2), radLon(pt2)));
    }

    /**
     * Returns the "apparent" height of a second point as seen from the first.
     * This applies round-earth math to the given altitudes at each point to
     * calculate a depression angle and return the difference in height of the
     * second point from a flat plane that is oriented to the local reference
     * plane of the first point.
     *
     * @return difference in "apparent" height between the points, in meters.
     */
    public static double getApparentHeight(Location3D pt1, Location3D pt2) {
        double arc_dist = NavUtils.arcDistance(radLat(pt1), radLon(pt1), radLat(pt2), radLon(pt2));
        double height_diff = pt2.getAltitude() - pt1.getAltitude();
        return NavUtils.getApparentHeight(arc_dist, height_diff);
    }

    /**
     * Uses the {@link TerrainService} to find a point on the ground for a ray.
     * The accuracy of the intercept depends on the terrain data that is loaded
     * in the Terrain Service.
     *
     * @param startPt true location of the start point.
     * @param azimuth_rad true compass heading of the ray at the start point
     * (radians)
     * @param elevation_rad elevation with respect to the local inertial plane
     * at the start point, positive up from the horizon (radians)
     * @return the intercept point
     */
    public static Location3D getInterceptPoint(Location3D startPt, double azimuth_rad, double elevation_rad) {
        double[] loc = TerrainService.getInterceptPoint(startPt.getLatitude(), startPt.getLongitude(), startPt.getAltitude(),
                azimuth_rad, elevation_rad, 0, 2);

        return new Location3D(loc[0], loc[1], (float) loc[2], AltitudeType.MSL);
    }

    /**
     * Get the height of terrain above Mean Sea Level in meters. if terrain is
     * not loaded, this will return 0. This method accesses the
     * {@link TerrainService}.
     *
     * @param loc
     * @return height of terrain above sea level
     */
    public static double getElevation(Location3D loc) {
        return TerrainService.getElevation(loc.getLatitude(), loc.getLongitude());
    }

    /**
     * Converts coordinates to x-y coordinates using a WGS84 earth model. This
     * is accurate for points that are close to the center point.
     * This assumes that the first point is the origin.
     * The X-Y coordinate system has X-coordinates in the East-West axis (East
     * positive) and Y coordinates in the North-South axis (North positive)
     *
     * @param loc1 first point (origin)
     * @param loc2 second point
     * @return a point in X-Y coordinates
     */
    public static Point2D getXYPoint(Location3D loc1, Location3D loc2) {

        double clat = Math.toRadians(loc1.getLatitude());
        double clon = Math.toRadians(loc2.getLongitude());
        double ewRadius = NavUtils.getRadius(clat) * Math.cos(clat);

        double Narc = Math.toRadians(loc2.getLatitude()) - clat;
        double Earc = Math.toRadians(loc2.getLongitude()) - clon;
        Point2D xyPt = new Point2D.Double(Earc * ewRadius, Narc * NavUtils.EARTH_EQ_RADIUS_M);

        return xyPt;
    }

    /**
     * Converts coordinates to x-y coordinates using a WGS84 earth model. This
     * is accurate for points that are close to the center point.
     *
     * The X-Y coordinate system has X-coordinates in the East-West axis (East
     * positive) and Y coordinates in the North-South axis (North positive)
     *
     * @param srcPts the LatLonPoints to convert to X-Y
     * @param centerPt
     * @return A list of points
     */
    public static List<Point2D> getXYPoints(Location3D centerPt, Location3D... pts) {
        List<Point2D> retList = new ArrayList<>();
        double clat = Math.toRadians(centerPt.getLatitude());
        double clon = Math.toRadians(centerPt.getLongitude());
        double ewRadius = NavUtils.getRadius(clat) * Math.cos(clat);
        for (Location3D srcPt : pts) {
            double Narc = Math.toRadians(srcPt.getLatitude()) - clat;
            double Earc = Math.toRadians(srcPt.getLongitude()) - clon;
            Point2D xyPt = new Point2D.Double(Earc * ewRadius, Narc * NavUtils.EARTH_EQ_RADIUS_M);
            retList.add(xyPt);
        }
        return retList;
    }

    public static double radLat(Location3D loc) {
        return Math.toRadians(loc.getLatitude());
    }

    public static double radLon(Location3D loc) {
        return Math.toRadians(loc.getLongitude());
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */