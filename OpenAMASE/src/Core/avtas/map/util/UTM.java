// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.util;

import avtas.map.util.Ellipsoid;


/**
 * UTM coordinate.  UTM coordinates are spcecified by a cartesian offset for a given zone number.  
 * Internally, UTM coordinates are translated to MapPoint for use by the mapping API.
 * 
 * Transformations use the WGS 84 ellipsoid unless otherwise specified.
 * 
 * @author AFRL/RQQD
 */
public class UTM extends MapPoint {
    /** true if this is a northern hemisphere point */
    private double northing;
    private double easting;
    private int zoneNumber;
    private boolean isNorth = true;
    
    public static final Ellipsoid DEFAULT_ELLIPSOID = Ellipsoid.WGS_84;

    private UTM() {
    }

    /**
     * Constructs a new UTM instance.
     * 
     * @param northing The northing distance.
     * @param easting The easting distance.
     * @param zoneNumber The zone of the coordinate.
     * @param isNorth true if this is the northern hemisphere
     */
    public UTM(double northing, double easting, int zoneNumber, boolean isNorth) {
        this.northing = Math.rint(northing);
        this.easting = Math.rint(easting);
        this.zoneNumber = zoneNumber;
        this.isNorth = isNorth;
        toLatLon(this, DEFAULT_ELLIPSOID);
    }


    @Override
    public String toString() {
        return "UTM [zone = " + getZoneNumber() + ", " +
                "easting = " + getEasting() + ", northing = " + getNorthing() +
                ", hemisphere = " + (isNorth() ? 'N' : 'S') + "]";
    }

    /** converts a UTM point to MapPoint using the WGS-84 datum */
    public static MapPoint toLatLon(UTM utmpoint) {
        return toLatLon(utmpoint);
    }

    /** converts a MapPoint point to a UTM point using the WGS-84 datum */
    public static UTM toUTM(MapPoint llpoint) {
        UTM utm = new UTM();
        utm.toUTM(llpoint, Ellipsoid.WGS_84);
        return utm;
    }

    /**
     * Converts a MapPoint point to UTM for a given ellipsoid
     * 
     * Based on C++ code by Chuck Gantz (chuck.gantz@globalstar.com)
     * Equations from USGS Bulletin 1532
     * 
     * @param ellip the ellipsoid to use for creating Lat, Lon.
     */
    protected void toUTM(MapPoint llp, Ellipsoid ellip) {

        double lat = llp.getLat();
        double lon = llp.getLon();
        double a = ellip.getA();
        double eccSquared = ellip.getEcc() * ellip.getEcc();
        double k0 = 0.9996;

        double LongOrigin;
        double eccPrimeSquared;
        double N, T, C, A, M;

        double latRad = llp.getRadLat();
        double lonRad = llp.getRadLon();
        double lonOriginRad;
        int zoneNum;

        zoneNum = (int) ((lon + 180) / 6) + 1;

        //Make sure the longitude 180.00 is in Zone 60
        if (lon == 180) {
            zoneNum = 60;
        }

        // Special zone for Norway
        if (lat >= 56.0f && lat < 64.0f && lon >= 3.0f && lon < 12.0f) {
            zoneNum = 32;
        }

        // Special zones for Svalbard
        if (lat >= 72.0f && lat < 84.0f) {
            if (lon >= 0.0f && lon < 9.0f) {
                zoneNum = 31;
            } else if (lon >= 9.0f && lon < 21.0f) {
                zoneNum = 33;
            } else if (lon >= 21.0f && lon < 33.0f) {
                zoneNum = 35;
            } else if (lon >= 33.0f && lon < 42.0f) {
                zoneNum = 37;
            }
        }
        LongOrigin = (zoneNum - 1) * 6 - 180 + 3; //+3 puts origin in middle of zone
        lonOriginRad = Math.toRadians(LongOrigin);

        eccPrimeSquared = (eccSquared) / (1 - eccSquared);

        N = a / Math.sqrt(1 - eccSquared * Math.sin(latRad) * Math.sin(latRad));
        T = Math.tan(latRad) * Math.tan(latRad);
        C = eccPrimeSquared * Math.cos(latRad) * Math.cos(latRad);
        A = Math.cos(latRad) * (lonRad - lonOriginRad);

        M = a * ((1 - eccSquared / 4 - 3 * eccSquared * eccSquared / 64 - 5 * eccSquared * eccSquared * eccSquared / 256) * latRad - (3 * eccSquared / 8 + 3 * eccSquared * eccSquared / 32 + 45 * eccSquared * eccSquared * eccSquared / 1024) * Math.sin(2 * latRad) + (15 * eccSquared * eccSquared / 256 + 45 * eccSquared * eccSquared * eccSquared / 1024) * Math.sin(4 * latRad) - (35 * eccSquared * eccSquared * eccSquared / 3072) * Math.sin(6 * latRad));

        float UTMEasting = (float) (k0 * N * (A + (1 - T + C) * A * A * A / 6.0d + (5 - 18 * T + T * T + 72 * C - 58 * eccPrimeSquared) * A * A * A * A * A / 120.0d) + 500000.0d);

        float UTMNorthing = (float) (k0 * (M + N * Math.tan(latRad) * (A * A / 2 + (5 - T + 9 * C + 4 * C * C) * A * A * A * A / 24.0d + (61 - 58 * T + T * T + 600 * C - 330 * eccPrimeSquared) * A * A * A * A * A * A / 720.0d)));
        if (lat < 0.0f) {
            UTMNorthing += 10000000.0f; //10000000 meter offset for southern hemisphere
        }

        this.northing = Math.rint(UTMNorthing);
        this.easting = Math.rint(UTMEasting);
        this.zoneNumber = zoneNum;
        this.isNorth = lat >= 0;
    }

    /**
     * Converts UTM coords to lat/long given an ellipsoid.
     * 
     * Based on C++ code by Chuck Gantz (chuck.gantz@globalstar.com)
     * Equations from USGS Bulletin 1532
     * 
     * @param ellip the ellipsoid to use for creating Lat, Lon.
     */
    public void toLatLon(MapPoint latLon, Ellipsoid ellip) {

        double k0 = 0.9996;
        double a = ellip.getA();
        double eccSquared = ellip.getEcc() * ellip.getEcc();
        double eccPrimeSquared;
        double e1 = (1 - Math.sqrt(1 - eccSquared)) / (1 + Math.sqrt(1 - eccSquared));
        double N1, T1, C1, R1, D, M;
        double LongOrigin;
        double mu, phi1Rad;

        // remove 500,000 meter offset for longitude
        double x = getEasting() - 500000.0d;
        double y = getNorthing();

        //remove 10,000,000 meter offset used for southern hemisphere
        if (!isNorth()) {
            y -= 10000000.0d;
        }

        //There are 60 zones with zone 1 being at West -180 to -174
        LongOrigin = (getZoneNumber() - 1) * 6 - 180 + 3; 
        //+3 puts origin in middle of zone

        eccPrimeSquared = (eccSquared) / (1 - eccSquared);

        M = y / k0;
        mu = M / (a * (1 - eccSquared / 4 - 3 * eccSquared * eccSquared / 64 - 5 * eccSquared * eccSquared * eccSquared / 256));

        phi1Rad = mu + (3 * e1 / 2 - 27 * e1 * e1 * e1 / 32) * Math.sin(2 * mu) + (21 * e1 * e1 / 16 - 55 * e1 * e1 * e1 * e1 / 32) * Math.sin(4 * mu) + (151 * e1 * e1 * e1 / 96) * Math.sin(6 * mu);

        N1 = a / Math.sqrt(1 - eccSquared * Math.sin(phi1Rad) * Math.sin(phi1Rad));
        T1 = Math.tan(phi1Rad) * Math.tan(phi1Rad);
        C1 = eccPrimeSquared * Math.cos(phi1Rad) * Math.cos(phi1Rad);
        R1 = a * (1 - eccSquared) / Math.pow(1 - eccSquared * Math.sin(phi1Rad) * Math.sin(phi1Rad), 1.5);
        D = x / (N1 * k0);

        double Lat = phi1Rad - (N1 * Math.tan(phi1Rad) / R1) * (D * D / 2 - (5 + 3 * T1 + 10 * C1 - 4 * C1 * C1 - 9 * eccPrimeSquared) * D * D * D * D / 24 + (61 + 90 * T1 + 298 * C1 + 45 * T1 * T1 - 252 * eccPrimeSquared - 3 * C1 * C1) * D * D * D * D * D * D / 720);

        double Long = (D - (1 + 2 * T1 + C1) * D * D * D / 6 + (5 - 2 * C1 + 28 * T1 - 3 * C1 * C1 + 8 * eccPrimeSquared + 24 * T1 * T1) * D * D * D * D * D / 120) / Math.cos(phi1Rad);
        Long = LongOrigin + Math.toDegrees(Long);
        
        latLon.setRadLat(Lat);
        latLon.setLon(Long);
    }

    /** UTM Northing value in meters */
    public double getNorthing() {
        return northing;
    }

    /** UTM Easting value in meters */
    public double getEasting() {
        return easting;
    }

    /** UTM Zone Number */
    public int getZoneNumber() {
        return zoneNumber;
    }

    /** returns true if int the northern hemisphere */
    public boolean isNorth() {
        return isNorth;
    }
}



/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */