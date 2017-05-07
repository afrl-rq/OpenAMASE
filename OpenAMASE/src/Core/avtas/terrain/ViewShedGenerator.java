// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.terrain;

import avtas.util.NavUtils;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author AFRL/RQQD
 */
public class ViewShedGenerator {

     /**
     * Creates a coverage map for the requested area.  The coverage map is specified using a range and
     * a center location.  It uses Rhumb lines to determine the extents.
     *
     * @param lat center latitude in degrees
     * @param lon center longitude in degrees
     * @param range maximum range for computation in meters
     * @param h1 start height in meters above the ellipsoid
     * @param h2 end height in meters above ground
     * @param level DTED level to use for interpolation
     * @param observer an observer object that accepts progress updates (can be null)
     * @return an array of coverage (true for points in line-of-sight of the center point)
     */
    public static boolean[][] getLOSCoverage(double lat, double lon, double range, double h1, double h2, int level,
            ViewShedObserver observer) {
        return getLOSCoverage(TerrainService.getCache(), lat, lon, range, h1, h2, level, observer);
    }

    /**
     * Creates a coverage map for the requested area.  The coverage map is specified using a range and
     * a center location.  It uses Rhumb lines to determine the extents.
     *
     * The returned array is of [col][row] points from the upper right hand corner of the requested area.
     *
     * @param lat center latitude in degrees
     * @param lon center longitude in degrees
     * @param range maximum range for computation in meters
     * @param h1 start height in meters above the ellipsoid
     * @param h2 end height in meters above ground
     * @param level DTED level to use for interpolation
     * @param observer an observer object that accepts progress updates (can be null)
     * @return an array of coverage (true for points in line-of-sight of the center point)
     */
    public static boolean[][] getLOSCoverage(DTEDCache cache, double lat, double lon, double range, double h1, double h2, int level,
            ViewShedObserver observer) {

        double radLat = Math.toRadians(lat);
        double radLon = Math.toRadians(lon);

        // get the upper-left and lower right [lat,lon] points from the center point
        double[] ul = NavUtils.getLatLon(radLat, radLon, range, Math.toRadians(-45));
        double[] lr = NavUtils.getLatLon(radLat, radLon, range, Math.toRadians(135));

        // get the heights for all the points in the rectangle from upper-left to lower-right
        short[][] elevs = cache.getElevations(Math.toDegrees(ul[0]), Math.toDegrees(ul[1]), Math.toDegrees(lr[0]),
                Math.toDegrees(lr[1]), DTEDTile.getPostSpacing(level));

        int center_x = elevs.length/2;
        int center_y = elevs[0].length/2;

        // for ellipsoid calculations, semi-major and semi-minor axis
        double a = center_x, b = center_y;

        // get the slope value between the center point and every other point in the ellipse.
        // slope values are used later to determine whether terrain obstructs the view.
        // these slopes do not include end height since they are computed for terrain obscuration
        // tests
        double[][] slopes = new double[elevs.length][elevs[0].length];

        for (int i = 0; i < elevs.length; i++) {
            short[] col = elevs[i];
            for (int j = 0; j < col.length; j++) {
                double h = col[j];
                double dist = range * Math.hypot( (i - center_x) / a, (j - center_y) / b);
                if (dist > range) {
                    continue;
                }
                double arc_dist = dist / NavUtils.EARTH_EQ_RADIUS_M;
                // compute the reduced height due to earth curvature
                h = NavUtils.getApparentHeight(arc_dist, h);
                // set the reduced height for later computation
                col[j] = (short) h;

                slopes[i][j] = (h1 - h) / dist;
            }
        }

        // form a visibility map that has the same dimensions as the elevation map.  For every point in
        // the map, first test if it is in range.  If so, form the path from the point to the center and
        // test against all of the terrain slope values.
        boolean[][] vismap = new boolean[elevs.length][elevs[0].length];
        for (int i = 0; i < elevs.length; i++) {
            short[] col = elevs[i];
            if (observer != null) {
                observer.viewshedUpdate(i / (float) elevs.length * 100);
            }
            pixelloop:
            for (int j = 0; j < col.length; j++) {

                double rel_x = i - center_x;
                double rel_y = j - center_y;
                double steps = Math.hypot( rel_x, rel_y);

                double sinAz = rel_y / steps;
                double cosAz = rel_x / steps;

                // determine the approximate distance from the center point
                double dist = range * Math.hypot( rel_x / a, rel_y / b);

                // if outside of the range, then mark the point as not seen, to
                // avoid extra computation
                if ( dist > range) {
                    vismap[i][j] = false;
                    continue pixelloop;
                }

                // we need to find the slope including the end-height requirement
                // add little value to the height to overcome the curvature effect of
                // adjacent posts
                double slope = (h1 - elevs[i][j] - (h2 + 1)) / dist;

                // test for slopes greater than the end point slope.  If one is found,
                // then the point is not in view of the center location.
                // this starts at the center point and moves towards the point of interest
                double x=i, y=j;
                for (int k=(int)steps-1; k>=0; k--) {
                    x = i - k * cosAz;
                    y = j - k * sinAz;
                    if (slopes[(int) x][(int) y] < slope) {
                        vismap[i][j] = false;
                        continue pixelloop;
                    }
                }
                // if we get to here, then the point is visible
                vismap[i][j] = true;
            }
        }

        return vismap;
    }

    /**
     * Creates an image of a visibility map.  The image is transparent for points that are false
     * (not visible) in the visibility map and are filled with the specified color for points
     * that are visible.  The returned image has a size according to the size of the input
     * visibility map array.
     *
     * @param visibilityMap an array of visibility calculation points
     * @param color the color to use for visible points
     * @return an image corresponding to the visibility map
     */
    public static BufferedImage createImage(boolean[][] visibilityMap, Color color) {

        BufferedImage img = new BufferedImage(visibilityMap.length, visibilityMap[0].length, BufferedImage.TYPE_INT_ARGB);
        int rgb = color.getRGB();

        for (int i = 0; i < visibilityMap.length; i++) {
            boolean[] col = visibilityMap[i];
            for (int j = 0; j < col.length; j++) {
                if (col[j]) {
                    img.setRGB(i, img.getHeight() - j - 1, rgb);
                }
            }
        }

        return img;
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */
