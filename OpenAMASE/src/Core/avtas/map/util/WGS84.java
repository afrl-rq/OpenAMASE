// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.util;

import avtas.data.Unit;
import static java.lang.Math.*;

/**
 *
 * @author AFRL/RQQD
 * 
 *
 */
public class WGS84 {

    private static double a = 6378137.0; // radius at the equator in meters
    private static double f = 0.003352810665;
    private static double b = 6356752.31424518;  // polar radius in meters
    private static double epsilon = 0.0818191908426;
    /** polar circumference in meters */
    public static double polarCircum = 2 * PI * b;
    /** polar radius in meters */
    public static double polarRadius = b;
    /** equatorial circumference in meters */
    public static double eqCircum = a * 2 * PI;
    /** equatorial radius in meters */
    public static double eqRadius = a;

    /** returns the east-west radius of the planet in meters at a given latitude in radians
     */
    public static double getRadius(double degLat) {
        double sinlat = Math.sin(Math.toRadians(degLat));
        return a / sqrt(1 - epsilon * epsilon * sinlat * sinlat);
    }

    /** computes the distance in longitude for a distance in the normal (east-west) direction
     *  at sea level
     * @param degLat Latitude in degrees
     * @param meterDistance Distance to travel in meters
     * @return the distance in degrees longitude
     */
    public static double getDeltaLon(double degLat, double meterDistance) {
        return toDegrees(meterDistance / getRadius(degLat) / cos(toRadians(degLat)));
    }

    /** returns the east-west circumference of the planet in meters at a given latitude in degrees
     */
    public static double getCircum(double lat) {
        return 2 * PI * getRadius(lat);
    }

    /** returns WGS84 distance and azimuth between two points 
     * 
     * Algorithm from:
     * T. Vincenty, "Direct and Inverse Solutions of Geodesics on the Ellipsoid
     * with Application of Nested Equations", Survey Review, vol. 23, no. 176,
     * April 1975, pp 88-93
     */
    public static double[] distance(double radLat1, double radLon1, double radLat2, double radLon2) {
        double L = radLon2 - radLon1;
        double U1 = Math.atan((1 - f) * Math.tan(radLat1));
        double U2 = Math.atan((1 - f) * Math.tan(radLat2));
        double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
        double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);
        double lambda = L, lambdaP;
        int iterLimit = 100;
        double sinLambda, sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM, C, cosLambda;
        do {
            sinLambda = Math.sin(lambda);
            cosLambda = Math.cos(lambda);
            sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda) + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
            cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
            sigma = Math.atan2(sinSigma, cosSigma);
            sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
            cosSqAlpha = 1 - sinAlpha * sinAlpha;
            cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
            if (Double.isNaN(cos2SigmaM)) {
                cos2SigmaM = 0;  // at the equator, set to zero
            }
            C = f / 16. * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
            lambdaP = lambda;
            lambda = L + (1 - C) * f * sinAlpha * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
        } while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);
        if (iterLimit == 0) {
            return new double[]{Double.NaN, 0};  // formula failed to converge
        }
        double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double deltaSigma = B * sinSigma * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
        double s = b * A * (sigma - deltaSigma);

        // initial heading from point 1 to point 2
        double alpha1 = atan2(cos(U2) * sin(lambda) , (cos(U1) * sin(U2) - sin(U1) * cos(U2) * cos(lambda)));

        return new double[]{s, alpha1};
    }

    /** 
     * Returns a Point based on location of first point, distance in meters and 
     * azimuth in degrees
     *
     * Algorithm from:
     * T. Vincenty, "Direct and Inverse Solutions of Geodesics on the Ellipsoid
     * with Application of Nested Equations", Survey Review, vol. 23, no. 176,
     * April 1975, pp 88-93
     */
    public static double[] getPoint(double radLat, double radLon, double distance, double azimuth) {
        double s = distance;
        double alpha1 = azimuth;
        double sinAlpha1 = Math.sin(alpha1);
        double cosAlpha1 = Math.cos(alpha1);

        double tanU1 = (1 - f) * Math.tan(radLat);
        double cosU1 = 1 / Math.sqrt((1 + tanU1 * tanU1)), sinU1 = tanU1 * cosU1;
        double sigma1 = Math.atan2(tanU1, cosAlpha1);
        double sinAlpha = cosU1 * sinAlpha1;
        double cosSqAlpha = 1 - sinAlpha * sinAlpha;
        double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double cos2SigmaM = 0, sinSigma = 0, cosSigma = 0, deltaSigma = 0;

        double sigma = s / (b * A), sigmaP = 2 * Math.PI;
        while (Math.abs(sigma - sigmaP) > 1e-12) {
            cos2SigmaM = Math.cos(2 * sigma1 + sigma);
            sinSigma = Math.sin(sigma);
            cosSigma = Math.cos(sigma);
            deltaSigma = B * sinSigma * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)
                    - B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
            sigmaP = sigma;
            sigma = s / (b * A) + deltaSigma;
        }

        double tmp = sinU1 * sinSigma - cosU1 * cosSigma * cosAlpha1;
        double lat2 = Math.atan2(sinU1 * cosSigma + cosU1 * sinSigma * cosAlpha1,
                (1 - f) * Math.sqrt(sinAlpha * sinAlpha + tmp * tmp));
        double lambda = Math.atan2(sinSigma * sinAlpha1, cosU1 * cosSigma - sinU1 * sinSigma * cosAlpha1);
        double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
        double L = lambda - (1 - C) * f * sinAlpha
                * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));

        return new double[]{lat2, radLon + L};
    }


    public static void main(String[] args) {

        //System.out.println( getPoint( new MapPoint2D(37, 121), Unit.NM.convertTo(60, Unit.METER) *1.41, 45) );
        double[] res = distance(toRadians(38.1), toRadians(121), toRadians(38), toRadians(120));
        System.out.println(Unit.METER.convertTo(res[0], Unit.NM) + ", az: " + res[1]);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */