// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map;

import static java.lang.Math.*;
import static avtas.map.util.WorldMath.*;

/**
 * This projection implements a semi-Mercator view.  A constant pixel-per-degree ratio is maintained in each axis.  The
 * longitude width is specified by the user and the latitude height is set so that at the center of the view, one pixel in
 * the north-south direction has the same anglular change as one pixel in the east-west direction.
 * 
 * @author AFRL/RQQD
 */
public class EqualRect implements Proj, Cloneable {
    
    private double center_lat = 0;
    private double center_lon = 0;
    
    /** number of pixels per degree in latitude and longitude */
    private double pix_per_lon = 0, pix_per_lat = 0;
   
    /** the number of degrees in longitude visible on a non-rotated map */
    private double lonScale = 0;
    
    /** size of the projection in pixels */
    private int width = 0, height = 0;
    
    /** the rotation of the map in counter-clockwise radians */
    private double rotation = 0;
    
    // sine and cosine of rotation (initially non-rotated)
    private double srot = 0;
    private double crot = 1.0; 
    
    /** pixel value of the map center */
    private double center_x = 0, center_y = 0;
    
    /** the absolute limits of the map projection, accounting for extra coverage needed for rotation */
    private double eastLon = 0, westLon = 0, northLat = 0, southLat = 0;
    
    /** the width of the projection in degrees */
    private double latheight = 0, lonwidth = 0;
    
    /**
     * Creates a new instance of Proj2D
     */
    public EqualRect(double center_lat, double center_lon, int width, int height, double num_deg_x) {
        compute(center_lat, center_lon, num_deg_x, width, height);
    }

    public Proj clone() {
        try {
            return (Proj) super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    /** sets the center of the projection in degrees */
    public void setCenter(double lat, double lon) { compute(lat, lon, lonScale, width, height); }
    
    /** sets the projection size in pixels */
    public void setSize(int width, int height) { compute(center_lat, center_lon, lonScale, width, height); }
    
    /** sets the longitudinal lonWidth of the map.  The lonWidth is based on the number of degrees visible in the x-direction of the map.  The
     *  y-lonWidth is based on the latitude of the center point.
     */
    public void setLonWidth(double degLon) { 
        if (degLon > 360) degLon = 360;
        compute(center_lat, center_lon, degLon, width, height); 
    }
    
    /** recomputes this projection based on latitude, longitude (degrees), lonWidth (degrees longitude showing),
     *  and the width and height in pixels.
     */
    public void compute(double lat, double lon, double lonWidth, int width, int height) {
        
        this.width = width;
        this.height = height;
        this.lonScale = lonWidth;
        this.center_lat = lat;
        this.center_lon = wrapLon(lon);
        
        center_x = width/2f;
        center_y = height/2f;
        
        pix_per_lon = width / lonWidth;
        pix_per_lat = pix_per_lon / cos( toRadians(lat) );
        
        //pix_per_lat = pix_per_lon / cos(lat);
        
        //double maxDim = max( height, width) * 0.707;
        
        // do a cartesian axis transformation based on the current rotation angle
        // if the map is rotated, then the absolute bounds for lat, lon are based on the 
        // corners of the map, not the sides.
        this.lonwidth = (width * crot + height * srot) / pix_per_lon;
        this.latheight =(height * crot + width * srot) / pix_per_lat;

        westLon = wrapLon( center_lon - 0.5 * lonwidth);
        eastLon = wrapLon( center_lon + 0.5 * lonwidth);
        southLat = center_lat - 0.5 * latheight;
        northLat = center_lat + 0.5 * latheight;
    }
    
    /** get the x-value of a given longitude (does not consider rotation) */
    public double getX( double lon) {
        return center_x + ( lon - center_lon ) * pix_per_lon;
    }
    
    /** get the y value of a given latitude (does not consider rotation) */
    public double getY( double lat) {
        return center_y - ( lat - center_lat) * pix_per_lat;
    }
    
    /** performs inverse method, accounting for rotation */
    public double getLat(double x, double y) {
        return center_lat + ( ( x - center_x ) * sin(-rotation) + (center_y - y) * cos(-rotation) ) / pix_per_lat;
    }
    
    /** performs inverse method, accounting for rotation */
    public double getLon(double x, double y) {
        double lon = center_lon + ( ( x - center_x ) * cos(-rotation) - (center_y - y) * sin(-rotation) ) / pix_per_lon;
        return wrapLon(lon);
    }
    
    /** returns the center latitude of this projection in degrees */
    public double getCenterLat() {  return center_lat; }
    
    /** returns the center longitude of this projection in degrees */
    public double getCenterLon() {  return center_lon; }
    
    
    public double getEastLon() { return eastLon;}
    
    public double getWestLon() { return westLon; }
    
    public double getNorthLat() { return northLat; }
    
    public double getSouthLat() { return southLat; }
    
    /** returns num pixels per deg of Lon */
    public double getPixPerLon() { return pix_per_lon; }
    
    /** returns num pixels per degree of Lat */
    public double getPixPerLat() { return pix_per_lat; }
    
    public int getWidth() { return width; }
    
    public int getHeight() { return height; }
    
    /**
     *  set rotation in counter-clockwise radians.  This is just for reference.  The Proj2D does not do rotation computations
     *  It is assumed that the graphics manager (map) handles that.
     */
    public void setRotation(double radians_rot) {  
        this.rotation = radians_rot;  
        crot = Math.abs(Math.cos(rotation));
        srot = Math.abs(Math.sin(rotation));
    }
    
    /** returns rotation in counter-clockwise radians */
    public double getRotation() { return rotation; }
    
    public String toString() {
        return "Proj: [ " + String.valueOf(center_lat) + ", " + String.valueOf(center_lon) +
                ", " + width + ", " + height + " ]";
    }
    
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */