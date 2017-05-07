// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map;

import avtas.map.util.WorldMath;

/**
 *
 * @author AFRL/RQQD
 */
public class LLProj implements Proj{
    private double northLatDeg;
    private double southLatDeg;
    private double eastLonDeg;
    private double westLonDeg;
    private int width;
    private int height;
    private double lat_height, lon_width;
    private double pix_per_lat, pix_per_lon;
    
    public LLProj(double northLatDeg, double southLatDeg, double westLonDeg, double eastLonDeg, 
            int width, int height) {
        compute(northLatDeg, southLatDeg, westLonDeg, eastLonDeg, width, height);
    }

    public Proj clone() {
        try {
            return (Proj) super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
    
    private void compute(double northLatDeg, double southLatDeg, double westLonDeg, double eastLonDeg, 
            int width, int height) {
        this.northLatDeg = northLatDeg;
        this.southLatDeg = southLatDeg;
        this.eastLonDeg = eastLonDeg;
        this.westLonDeg = westLonDeg;
        this.width = width;
        this.height = height;
        
        lat_height = Math.abs(northLatDeg - southLatDeg);
        lon_width = Math.abs( WorldMath.wrapLon( (eastLonDeg - westLonDeg) ) );
        
        pix_per_lat = height / lat_height;
        pix_per_lon = width / lon_width;
    }

    public double getCenterLat() {
        return northLatDeg - lat_height / 2d;
    }

    public double getCenterLon() {
        return WorldMath.wrapLon( westLonDeg + lon_width / 2d);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public double getPixPerLat() {
        return pix_per_lat;
    }

    public double getPixPerLon() {
        return pix_per_lon;
    }

    public double getLon(double x, double y) {
        return WorldMath.wrapLon( westLonDeg + x / pix_per_lon );
    }

    public double getLat(double x, double y) {
        return southLatDeg + y / pix_per_lat;
    }

    public double getNorthLat() {
        return northLatDeg;
    }

    public double getSouthLat() {
        return southLatDeg;
    }

    public double getWestLon() {
        return westLonDeg;
    }

    public double getEastLon() {
        return eastLonDeg;
    }

    public void setCenter(double lat, double lon) {
        compute(lat + lat_height/2d, lat-lat_height/2d, lon - lon_width/2d, 
                lon+ lon_width/2d , width, height);
    }

    public void setSize(int width, int height) {
        compute(northLatDeg, southLatDeg, westLonDeg, eastLonDeg, width, height);
    }

    public double getX(double lon) {
        return WorldMath.wrapLon(lon- westLonDeg) * pix_per_lon;
    }

    public double getY(double lat) {
        return (northLatDeg - lat) * pix_per_lat; 
    }

    public double getRotation() {
        return 0;
    }

    public void setRotation(double rotRad) {
        
    }

    public void setLonWidth(double lon_width) {
        if (lon_width < 0.0001) return;
        double centerLat = getCenterLat();
        double centerLon = getCenterLon();
        double factor = lon_width / this.lon_width;
        compute(centerLat + (factor * lat_height)/2d, centerLat - (factor * lat_height)/2d, 
                centerLon - lon_width/2d, centerLon + lon_width/2d,
                width, height);
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */