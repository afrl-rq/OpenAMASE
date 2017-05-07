// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.util;

import avtas.map.Proj;

/**
 *
 * @author AFRL/RQQD
 */
public class WorldMath {

    /** returns true if the given rectangle intersects the projection */
    public static boolean inView(Proj proj, double ullat, double ullon, double lrlat, double lrlon) {
        double latHeight = proj.getHeight() / proj.getPixPerLat();
        double lonWidth = proj.getWidth() / proj.getPixPerLon();

        return lrlat <= proj.getCenterLat() + latHeight
                && ullat >= proj.getCenterLat() - latHeight
                && wrapLon(ullon - proj.getCenterLon()) <= lonWidth
                && wrapLon(lrlon - proj.getCenterLon()) >= -lonWidth;
    }

    /** wraps the longitude from -180...180 */
    public static double wrapLon(double lon) {
        return lon - Math.floor((lon + 180) / 360.) * 360;
    }

    /** wraps the latitude from -90..90 */
    public static double normLat(double lat) {
        return lat - Math.floor((lat + 90) / 180.) * 180;
    }

    /** returns the pixel location of the dateline for the unrotated projection */
    public static int getDateline(Proj proj) {
        return (int) (proj.getWidth() / 2 + wrapLon(180 - proj.getCenterLon()) * proj.getPixPerLon());
    }

    /** returns true if the dateline (-180, 180 deg longitude) is in view */
    public static boolean datelineInView(Proj proj) {
        return proj.getWestLon() > proj.getEastLon();
    }

    /**
     * Returns an array of points along the great circle line that connects [lat1, lon1] and
     * [lat2, lon2].
     *
     * @param lat1 start latitude (degrees)
     * @param lon1 start longitude (degrees)
     * @param lat2 end latitude (degrees)
     * @param lon2 end longitude (degrees)
     * @param numpoints number of points to interpolate between start and finish.
     * @return array of [lat, lon] points (length = numpoints * 2)
     */
    public static double[] getGreatCircleLine(double lat1, double lon1, double lat2, double lon2, int numpoints) {

        double cosLat1 = Math.cos(Math.toRadians(lat1));
        double sinLat1 = Math.sin(Math.toRadians(lat1));
        double cosLat2 = Math.cos(Math.toRadians(lat2));
        double sinLat2 = Math.sin(Math.toRadians(lat2));
        double lonDiff = Math.toRadians(lon2) - Math.toRadians(lon1);
        double lat2diff = (float) Math.sin(((Math.toRadians(lat2) - Math.toRadians(lat1)) / 2));
        double lon2diff = (float) Math.sin((lonDiff) / 2);

        // spherical distance
        double c = 2.0f * Math.asin( Math.sqrt(lat2diff * lat2diff + cosLat2 * cosLat1 * lon2diff * lon2diff));

        // spherical azimuth
        double az = Math.atan2(cosLat2 * Math.sin(lonDiff), (cosLat1 *  sinLat2 - sinLat1 * cosLat2 * Math.cos(lonDiff)));

        double cosAz =  Math.cos(az);
        double sinAz =  Math.sin(az);


        double[] points = new double[numpoints*2];
        points[0] = lat1;
        points[1] = lon1;

        double inc = c / numpoints;
        c = inc;
        for (int i = 2; i < numpoints*2; i += 2, c += inc) {

            float sinc = (float) Math.sin(c);
            float cosc = (float) Math.cos(c);

            // new lat
            points[i] = Math.asin(sinLat1 * cosc + cosLat1 * sinc * cosAz);
            // new lon
            points[i + 1] = Math.atan2(sinc * sinAz, cosLat1 * cosc - sinLat1 * sinc * cosAz);

            //convert to degrees
            points[i] = Math.toDegrees(points[i]);
            points[i+1] = Math.toDegrees(points[i+1]) + lon1;
        }

        return points;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */