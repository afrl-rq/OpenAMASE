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
import avtas.util.NavUtils;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 * Creates an ellipse
 *
 * @author AFRL/RQQD
 */
public class MapEllipse extends MapGraphic {

    double x, y, lat, lon;
    int width = 0;
    int height = 0;
    //double ewDist = 0;
    //double nsDist = 0;
    double meterRadiusX = 0;
    double meterRadiusY = 0;
    double rotation_rad = 0;
    Ellipse2D ellipse = new Ellipse2D.Double();
    int xdist = 0;
    
    double dxNorth, dxEast, dyNorth, dyEast;

    /** Creates a new instance of MapEllipse */
    public MapEllipse() {
    }

    /** creates a circle at given point (degrees) and with given radii (meters). */
    public MapEllipse(double lat, double lon, double radiusX, double radiusY) {
        this.meterRadiusX = radiusX;
        this.meterRadiusY = radiusY;
        this.rotation_rad = 0;
        this.lat = lat;
        this.lon = lon;
        compute();
    }
    /** creates a circle at given point (degrees) with given radii (meters) and rotation (degrees). */
    public MapEllipse(double lat, double lon, double radiusX, double radiusY, double rotation_deg) {
        this.meterRadiusX = radiusX;
        this.meterRadiusY = radiusY;
        this.rotation_rad = Math.toRadians(rotation_deg);
        this.lat = lat;
        this.lon = lon;
        compute();
    }

    /**
     * Sets the center point of the shape in world coordinates
     * 
     * @param lat latitude of center point in degrees
     * @param lon longitude of center point in degrees
     */
    public void setCenter(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        compute();
    }

   /**
     *
     * @return latitude of the center point in degrees
     */
    public double getCenterLat() {
        return lat;
    }

    /**
     *
     * @return longitude of centerpoint in degrees
     */
    public double getCenterLon() {
        return lon;
    }

    /**
     * Sets the rotation in degrees (Clockwise, East of North).
     * @param rotation_deg 
     */
    public void setRotation(double rotation_deg) {
        this.rotation_rad = Math.toRadians(rotation_deg);
        compute();
    }

    /**
     * Returns the angle of rotation in degrees (Clockwise, East of North).
     */
    public double getRotation() {
        return Math.toDegrees(rotation_rad);
    }
    
    

    /**
     * Sets the radius in the X and Y directions
     * @param radius_meters_x radius in meters in the x-axis (East/West if no rotation)
     * @param radius_meters_y radius in meters in the y-axis (North/South if no rotation)
     */
    public void setRadius(double radius_meters_x, double radius_meters_y) {
        this.meterRadiusX = radius_meters_x;
        this.meterRadiusY = radius_meters_y;
        compute();
    }

    /**
     *
     * @return North-South radius in meters
     */
    public double getRadiusY() {
        return meterRadiusY;
    }

    /**
     *
     * @return East-West radius in meters
     */
    public double getRadiusX() {
        return meterRadiusX;
    }
    
    protected void compute() {
        
        dxEast = WGS84.getDeltaLon(lat, meterRadiusX * Math.cos(rotation_rad));
        dxNorth = Math.toDegrees(meterRadiusX * Math.sin(rotation_rad) / NavUtils.POLAR_RADIUS_M);
        
        dyEast = Math.toDegrees(meterRadiusY * Math.sin(rotation_rad) / NavUtils.POLAR_RADIUS_M);
        dyNorth = WGS84.getDeltaLon(lat, meterRadiusY * Math.cos(rotation_rad));
    }

    public void project(Proj proj) {

        setProjected(false);

        if (proj == null) {
            return;
        }

        x = proj.getX(lon);
        y = proj.getY(lat);
        
        double w = Math.hypot(dxEast * proj.getPixPerLon() , dxNorth * proj.getPixPerLat());
        double h = Math.hypot(dyEast * proj.getPixPerLon() , dyNorth * proj.getPixPerLat());

        ellipse.setFrame((int) (x - w ), (int) y - h , 2*w, 2*h);
        setScreenShape(ellipse);
        setProjected(true);
    }
    

    @Override
    public void paint(Graphics2D g) {
        if (isProjected()) {
            g.rotate(rotation_rad, x, y);
            super.paint(g);
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */