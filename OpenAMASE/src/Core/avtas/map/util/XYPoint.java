// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.util;

import avtas.util.NavUtils;
import avtas.map.util.WorldMath;



/**
 * Specifies a cartesian coordinate that is offset from a reference lat, lon.
 * Uses a spherical-earth calculation to determine offset lat, lon.
 * CAUTION: Error increases as the distance from the reference point increases.
 * 
 * @author AFRL/RQQD
 */
public class XYPoint extends MapPoint {
    
    private double len_x, len_y; //meters
    private MapPoint refLatLon = null;
    
    public XYPoint(double meters_x, double meters_y, MapPoint refLatLon) {
        this.refLatLon = refLatLon;
        setX(meters_x);
        setY(meters_y);
    }
    
    public void setX(double x) {
        this.len_x = x;
        super.setRadLon( NavUtils.getLon(refLatLon.getRadLat(), refLatLon.getRadLon(), len_x, 0) );
    }
    
    public void setY(double y) {
        this.len_y = y;
        super.setRadLat( NavUtils.getLat(refLatLon.getRadLat(), len_y, 0));
    }

    public double getX() {
        return len_x;
    }

    public double getY() {
        return len_y;
    }
    
    public String toString() {
        return "XYPoint: [" + len_x + ", " + len_y + "]";
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */