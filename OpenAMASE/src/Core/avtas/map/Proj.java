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
 *
 * @author AFRL/RQQD
 */
public interface Proj extends Cloneable{

    /** returns a copy of this projection */
    public Proj clone();

    /**
     * returns the center latitude of this projection in degrees
     */
    public double getCenterLat();

    /**
     * returns the center longitude of this projection in degrees
     */
    public double getCenterLon();

    /** returns the height of the projection in pixels */
    public int getHeight();
    
    /** returns the width of the projection in pixels */
    public int getWidth();

    /**
     * returns num pixels per degree of Latitude
     */
    public double getPixPerLat();
    
    /**
     * returns num pixels per deg of Longitude
     */
    public double getPixPerLon();

    /**
     * performs inverse method, accounting for rotation
     */
    public double getLon(double x, double y);
    
    /**
     * performs inverse method, accounting for rotation
     */
    public double getLat(double x, double y);


    /** returns the northern-most latitude before rotation */
    public double getNorthLat();

    /** returns the southern-nmost latitude before rotation */
    public double getSouthLat();

    /** returns the western-most longitude before rotation */
    public double getWestLon();
    
    /** returns the eastern-most longitude before rotation */
    public double getEastLon();

    /**
     * sets the center of the projection in degrees
     */
    public void setCenter(double lat, double lon);

    /**
     * sets the projection size in pixels
     */
    public void setSize(int width, int height);
    
    /** returns the x-screen coordinate for the given latitude (before rotation)
     * 
     * @param lon longitude in degrees
     * @return x-screen coordinate in pixels (before rotation)
     */
    public double getX(double lon);
    
    /** returns the y-screen coordinate for the given latitude (before rotation) 
     * 
     * @param lat latitude in degrees
     * @return y-screen coordinate in pixels (before rotation)
     */
    public double getY(double lat);
    
    /** returns the rotation in radians.  Some projections do not implement rotation. */
    public double getRotation();
    
    /** 
     * sets the rotation value for a projection.  Rotation is used for doing inverse operations
     * and interacting with swing components.
     * @param rotRad rotation in radians.
     */
    public void setRotation(double rotRad);
    
    /** sets the longitudinal width (x-direction) A projection may ignore this. 
     * 
     * @param degLon number of degrees longitude to be in view in an unrotated projection
     */
    public void setLonWidth(double degLon);
    

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */