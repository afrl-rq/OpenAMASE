// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.graphics;

import avtas.map.Proj;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 * Creates a ray, a line defined by a start point and a heading value.
 * The length of the ray is set by the number of screen pixels.
 *
 * @author AFRL/RQQD
 */
public class MapRay extends MapGraphic {

    double lat1 = 0;
    double lon1 = 0;
    double dlon = 0;
    double dlat = 0;
    int x1 = 0;
    int y1 = 0;
    int x2 = 0;
    int y2 = 0;
    int numPixels = 0;
    double heading_deg = 0;

    public MapRay() {
        this(0, 0, 0, 0);
    }

    /** Creates a new instance of MapLine */
    public MapRay(double lat1_deg, double lon1_deg, int numPixels, double heading_deg) {
        setRay(lat1_deg, lon1_deg, numPixels, heading_deg);
    }

    public void setRay(double lat1_deg, double lon1_deg, int numPixels, double heading_deg) {
        this.lat1 = lat1_deg;
        this.lon1 = lon1_deg;
        this.numPixels = numPixels;
        this.heading_deg = heading_deg;

        //setProjected(false);
    }

    public void setStartPt(double degLat, double degLon) {
        setRay(degLat, degLon, numPixels, heading_deg);
    }

    public double getStartLat() {
        return lat1;
    }

    public double getStartLon() {
        return lon1;
    }

    /** returns the number of pixels for the ray */
    public int getLength() {
        return numPixels;
    }
    
    /** Sets the ray length in pixels. */
    public void setLength(int numPixels) {
        setRay(lat1, lon1, numPixels, heading_deg);
    }
    
    /** degrees heading for the ray */
    public double getHeading() {
        return heading_deg;
    }

    /** Sets the heading of the ray in degrees. */
    public void setHeading(double heading_deg) {
        setRay(lat1, lon1, numPixels, heading_deg);
    }
    
    

    public void project(Proj proj) {

        setProjected(false);

        if (proj == null) {
            return;
        }
        
        double radHeading = Math.toRadians(heading_deg);

        x1 = (int) proj.getX(lon1);
        y1 = (int) proj.getY(lat1);
        x2 = x1 + (int) ( numPixels * Math.sin(radHeading));
        y2 = y1 - (int) ( numPixels * Math.cos(radHeading));
        setScreenShape(new Line2D.Double(x2, y2, x1, y1));
        setProjected(true);
    }

    public boolean intersects(int x, int y) {
        return Math.abs((x - x1) * (y2 - y1) - (x2 - x1) * (y - y1)) < 2;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */