// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.util;

/**
 * Contains methods to assist in geometric calculations.
 *
 * @author AFRL/RQQD
 */
public class GeomUtils {

    /**
     * Returns the intersection point between two lines. Returns null if the
     * lines are parallel or the same line.
     *
     * @param ax1 first x - point on line 1
     * @param ay1 first y - point on line 1
     * @param ax2 second x - point on line 1
     * @param ay2 second y - point on line 1
     * @param bx1 first x - point on line 2
     * @param by1 first y - point on line 2
     * @param bx2 second x - point on line 2
     * @param by2 second y - point on line 2
     * @return the intersection point or null if the lines do not intersect
     */
    public static double[] getIntersectionOfLines(double ax1, double ay1, double ax2, double ay2, double bx1, double by1, double bx2, double by2) {
        double Ax = ax2 - ax1;
        double Ay = ay2 - ay1;
        double Bx = bx2 - bx1;
        double By = by2 - by1;

        double[] u = getU(ax1, ay1, Ax, Ay, bx1, by1, Bx, By);

        return new double[]{ax1 + Ax * u[0], ay1 + Ay * u[0]};
    }

    /**
     * Returns the nearest point on a line to a point.  This assumes the line extends to infinity.
     * @param ax1 x coordinate of the first point on the line
     * @param ay1 y coordinate of the first point on the line
     * @param ax2 x coordinate of the second point on the line
     * @param ay2 y coordinate of the second point on the line
     * @param px  x coordinate of the point of interest
     * @param py y coordinate of the point of interest
     * @return the nearest point on the line to the point of interest.
     */
    public static double[] getNearestPointOnLine(double ax1, double ay1, double ax2, double ay2, double px, double py) {
        double Ax = ax2 - ax1;
        double Ay = ay2 - ay1;
        double Bx = -Ay;
        double By = Ax;

        double[] u = getU(ax1, ay1, Ax, Ay, px, py, Bx, By);

        return new double[]{ax1 + Ax * u[0], ay1 + Ay * u[0]};
    }

    /**
     * Returns the nearest point on a line to a point.  This will return a point on the segment, or an end point of the 
     * segment.
     * @param ax1 x coordinate of the first point of the segment
     * @param ay1 y coordinate of the first point of the segment
     * @param ax2 x coordinate of the second point of the segment
     * @param ay2 y coordinate of the second point of the segment
     * @param px  x coordinate of the point of interest
     * @param py y coordinate of the point of interest
     * @return the nearest point on the segment to the point of interest.
     */
    public static double[] getNearestPointOnSegment(double ax1, double ay1, double ax2, double ay2, double px, double py) {
        double Ax = ax2 - ax1;
        double Ay = ay2 - ay1;
        double Bx = -Ay;
        double By = Ax;

        double[] u = getU(ax1, ay1, Ax, Ay, px, py, Bx, By);
        if (u[0] > 1.0D) {
            u[0] = 1.0D;
        }
        if (u[0] < 0.0D) {
            u[0] = 0.0D;
        }

        return new double[]{ax1 + Ax * u[0], ay1 + Ay * u[0]};
    }

    /**
     * Returns the intersection point between two line segment. Returns null if the
     * segments do not cross.
     *
     * @param ax1 x coordinate of the first point of segment a
     * @param ay1 y coordinate of the first point of segment a
     * @param ax2 x coordinate of the second point of segment a
     * @param ay2 y coordinate of the second point of segment a
     * @param bx1 x coordinate of the first point of segment b
     * @param by1 y coordinate of the first point of segment b
     * @param bx2 x coordinate of the second point of segment b
     * @param by2 y coordinate of the second point of segment b
     * @return the intersection point or null if the lines do not intersect
     */
    public static double[] getIntersectionOfSegments(double ax1, double ay1, double ax2, double ay2, double bx1, double by1, double bx2, double by2) {
        double Ax = ax2 - ax1;
        double Ay = ay2 - ay1;
        double Bx = bx2 - bx1;
        double By = by2 - by1;

        double[] uv = getUV(ax1, ay1, Ax, Ay, bx1, by1, Bx, By);

        if ((uv[0] < 0.0D) || (uv[0] > 1.0D) || (uv[1] < 0.0D) || (uv[1] > 1.0D)) {
            return null;
        }

        return new double[]{ax1 + Ax * uv[0], ay1 + Ay * uv[0]};
    }

    /**
     * Returns the intersection point between two rays. Returns null if the
     * rays do not cross.
     *
     * @param ax1 x coordinate of the first point of ray a
     * @param ay1 y coordinate of the first point of ray a
     * @param ax2 x coordinate of the second point of ray a
     * @param ay2 y coordinate of the second point of ray a
     * @param bx1 x coordinate of the first point of ray b
     * @param by1 y coordinate of the first point of ray b
     * @param bx2 x coordinate of the second point of ray b
     * @param by2 y coordinate of the second point of ray b
     * @return the intersection point or null if the lines do not intersect
     */
    public static double[] getIntersectionOfRays(double ax1, double ay1, double ax2, double ay2, double bx1, double by1, double bx2, double by2) {
        double Ax = ax2 - ax1;
        double Ay = ay2 - ay1;
        double Bx = bx2 - bx1;
        double By = by2 - by1;

        double[] uv = getUV(ax1, ay1, Ax, Ay, bx1, by1, Bx, By);

        if (uv == null || (uv[0] < 0.0D) || (uv[1] < 0.0D)) {
            return null;
        }

        return new double[]{ax1 + Ax * uv[0], ay1 + Ay * uv[0]};
    }

    protected static double[] getU(double ax, double ay, double Ax, double Ay, double bx, double by, double Bx, double By) {
        double denom = Ax * By - Ay * Bx;

        if (denom == 0.0D) {
            return null;
        }

        double u = (Bx * ay - Bx * by - By * ax + By * bx) / denom;

        return new double[]{u};
    }

    protected static double[] getUV(double ax, double ay, double Ax, double Ay, double bx, double by, double Bx, double By) {
        double denom = Ax * By - Ay * Bx;

        if (denom == 0.0D) {
            return null;
        }

        double v = (Ay * bx - Ay * ax + Ax * ay - Ax * by) / denom;
        double u = (Bx * ay - Bx * by - By * ax + By * bx) / denom;

        return new double[]{u, v};
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */