// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map;

/**
 * Can be used by maps and other geographically aware applications to issue application-wide events when users select a point.
 *
 * @author AFRL/RQQD
 */
public class GeoPointSelected {

    double lat, lon, elev;

    public GeoPointSelected(double latitude_deg, double longitude_deg, double elevation_meters) {
        this.lat = latitude_deg;
        this.lon = longitude_deg;
        this.elev = elevation_meters;
    }

    public GeoPointSelected(double latitude_deg, double longitude_deg) {
        this.lat = latitude_deg;
        this.lon = longitude_deg;
    }

    /**
     * @return latitude in degrees
     */
    public double getLatitude() {
        return lat;
    }

    /**
     *
     * @return longitude in degrees
     */
    public double getLongitude() {
        return lon;
    }

    /**
     *
     * @return elevation in meters (optional)
     */
    public double getElevation() {
        return elev;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */