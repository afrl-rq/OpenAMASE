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
import avtas.map.util.WGS84;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;


/**
 *
 * @author AFRL/RQQD
 */
public class MapArc extends MapGraphic {

    double degCenterLat;
    double degCenterLon;
    int degStartAngle;
    int degExtent;
    double degEastRadius;
    double degNorthRadius;
    int x1, y1, dx, dy;
    private double meterRadius;
    private int outlineType = Arc2D.OPEN;

    public MapArc() {
        this(0, 0, 0, 0, 0);
    }

    /** creates a MapArc that is circular in shape
     * 
     */
    public MapArc(double degLatCenter, double degLonCenter, double degStartAngle, double degExtent, double meterRadius) {
        setCenter(degLatCenter, degLonCenter);
        setExtents(degStartAngle, degExtent);
        setRadius(meterRadius);
    }

    public void setCenter(double degCenterLat, double degCenterLon) {
        this.degCenterLat = degCenterLat;
        this.degCenterLon = degCenterLon;
        //setProjected(false);
    }

    public void setExtents(double degStartAngle, double degExtent) {
        this.degStartAngle = (int) (90 - degStartAngle);
        this.degExtent = (int) -degExtent;
    }

    public void setRadius(double meterRadius) {
        this.meterRadius = meterRadius;
        this.degNorthRadius = Math.toDegrees(meterRadius / WGS84.polarRadius);
        this.degEastRadius = WGS84.getDeltaLon(degCenterLat, meterRadius);
        //setProjected(false);
    }

    public double getRadius() {
        return meterRadius;
    }

    /**
     * @return angle in degrees of the start of the arc (clockwise from North)
     */
    public double getStartAngle() {
        return degStartAngle;
    }


    /**
     * @return angular extent of the arc (clockwise of start angle)
     */
    public double getArcExtent() {
        return degExtent;
    }

    /**
     *
     * @return Latitude of center point in degrees
     */
    public double getCenterLat() {
        return degCenterLat;
    }

    /**
     *
     * @return Longitude of center point in degrees
     */
    public double getCenterLon() {
        return degCenterLon;
    }
    
    /**
     * Sets the type of arc that is drawn.  Use values from {@link Arc2D} (e.g. OPEN, CHORD, or PIE)
     * @param type 
     */
    public void setOutlineType(int type) {
        this.outlineType = type;
    }

//    public void paint(Graphics2D g2) {
//        if (projected) {
//            g2.setStroke(getStroke());
//            g2.setPaint(getColor());
//            g2.drawArc((x1 - dx), (y1 - dy), (2 * dx), (2 * dy), degStartAngle, degExtent);
//        }
//    }

    public void project(Proj proj) {

        setProjected(false);

        if (proj == null) {
            return;
        }

        dx = (int) (proj.getPixPerLon() * degEastRadius);
        dy = (int) (proj.getPixPerLat() * degNorthRadius);
        x1 = (int) proj.getX(degCenterLon);
        y1 = (int) proj.getY(degCenterLat);

        Arc2D.Double screenArc = new Arc2D.Double((x1 - dx), (y1 - dy), (2 * dx), (2 * dy), 
                degStartAngle, degExtent, outlineType);

        setScreenShape(screenArc);
        setProjected(true);

    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */