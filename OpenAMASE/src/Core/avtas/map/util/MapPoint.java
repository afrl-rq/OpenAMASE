// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.util;

/**
 * Base class for all coordinates.  The mapping toolkit works natively in lat/lon and all other
 * coordinate types are translated to lat/lon for projection.
 * 
 * @author AFRL/RQQD
 */
public class MapPoint {
    
    double degLat = 0;
    double degLon = 0;
    
    public MapPoint() {
    }
    
    /** creates a lat/lon coordinate using radians */
    public MapPoint(double degLat, double degLon) {
        this.degLat = degLat;
        this.degLon = degLon;
    }

    public static MapPoint Radians(double radLat, double radLon) {
        return new MapPoint(Math.toDegrees(radLat), Math.toDegrees(radLon));
    }
    
    public static MapPoint Degrees(double degLat, double degLon) {
        return new MapPoint( degLat, degLon);
    }
    
    public double getLat() {
        return degLat;
    }
    
    public double getLon() {
        return degLon;
    }

    public double getRadLat() {
        return Math.toRadians(degLat);
    }

    public double getRadLon() {
        return Math.toRadians(degLon);
    }

    protected void setLat(double degLat) {
        this.degLat = degLat;
    }

    protected void setLon(double degLon) {
        this.degLon = degLon;
    }

    protected void setRadLat(double radLat) {
        this.degLat = Math.toDegrees(radLat);
    }

    protected void setRadLon(double radLon) {
        this.degLon = Math.toDegrees(radLon);
    }

    @Override
    public String toString() {
        return "MapPoint: [ Lat: " + degLat + ", Lon: " + degLon + " ]";
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */