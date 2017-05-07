// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.graphics;

import avtas.util.NavUtils;
import avtas.map.Proj;
import avtas.map.util.WorldMath;
import java.awt.geom.Path2D;

/**
 * Creates a rectangular shape on the map.  Rectangle is defined as a shape that connects the four
 * corner points that are computed (or declared) in [lat, lon] coordinates.  <b>Caution</b> should be 
 * used in cases of large rectangles that also include a rotational component.  In this case, the
 * rectangle sill suffer from error due to straight-line (rhumb) calculations.
 *
 * @author AFRL/RQQD
 */
public class MapRect extends MapGraphic {

    double[] ne, se, nw, sw;
    double rotation_deg = 0;
    double degCenterLat = 0;
    double degCenterLon = 0;
    double dist_to_corner = 0;
    private double width = 0;
    private double height = 0;

    public MapRect() {
        this(0, 0, 0, 0);
    }

    /** Creates a rectangle bounds using 4-corners
     *
     * @param degNLat northern latitude of the rectangle
     * @param degWLon western longitude of the rectangle
     * @param degSLat southern latitude of the rectangle
     * @param degELon eastern longitude of the rectangle
     */
    public MapRect(double degNLat, double degWLon, double degSLat, double degELon) {
        setRectFromCorners(degNLat, degWLon, degSLat, degELon);
    }

    /**
     * Creates a rectangle using a center point, width, height, and rotation values.
     *
     * @param degCenterLat latitude point of the rectangle's centroid
     * @param degCenterLon longitude point of the rectangle's centroid
     * @param meterWidth width of the rectangle in meters
     * @param meterHeight height of the rectangle in meters
     * @param degRotation rotation angle of the rectangle in degrees, positive East of North
     */
    public MapRect(double degCenterLat, double degCenterLon, double meterWidth, double meterHeight,
            double degRotation) {
        setRectFromCenter(degCenterLat, degCenterLon, meterWidth, meterHeight, degRotation);
    }

    @Override
    public void project(Proj proj) {
        Path2D shape = new Path2D.Double();
        shape.moveTo(proj.getX(nw[1]), proj.getY(nw[0]));
        shape.lineTo(proj.getX(ne[1]), proj.getY(ne[0]));
        shape.lineTo(proj.getX(se[1]), proj.getY(se[0]));
        shape.lineTo(proj.getX(sw[1]), proj.getY(sw[0]));
        shape.closePath();
        setScreenShape(shape);
        setProjected(true);
    }

    /**
     * Sets the rectangle bounds using a center point, width, height, and rotation values.
     *
     * @param degCenterLat latitude point of the rectangle's centroid
     * @param degCenterLon longitude point of the rectangle's centroid
     * @param meterWidth width of the rectangle in meters
     * @param meterHeight height of the rectangle in meters
     * @param degRotation rotation angle of the rectangle in degrees, positive East of North
     */
    public void setRectFromCenter(double degCenterLat, double degCenterLon, double meterWidth, double meterHeight,
            double degRotation) {

        compute(degCenterLat, degCenterLon, meterWidth, meterHeight, degRotation);

    }

    /**
     * Sets the rectangle based on the distance from the current center point and
     * any of the four corner points.  This maintains the current rotation angle.
     * @param cornerLat latitude of corner point in degrees
     * @param cornerLon longitude of corner point in degrees
     */
    public void setByCornerPoint(double cornerLat, double cornerLon) {

        double radCLon = Math.toRadians(degCenterLon);
        double radCLat = Math.toRadians(degCenterLat);
        double radPLat = Math.toRadians(cornerLat);
        double radPLon = Math.toRadians(cornerLon);

        // get the azimuth between centerpoint and the new point
        double radAz = NavUtils.headingBetween(radCLat, radCLon, radPLat, radPLon);
        // subtract the rotation angle of the rectangle
        radAz -= Math.toRadians(rotation_deg);

        // if az is 90, 270 deg, then all of the distance goes to width, if az is
        // 0, 180 deg then all distance goes to height
        double dist = NavUtils.distance(radCLat, radCLon, radPLat, radPLon);
        this.width = 2.0 * dist * Math.abs(Math.sin(radAz));
        this.height = 2.0 * dist * Math.abs(Math.cos(radAz));

        compute(degCenterLat, degCenterLon, width, height, rotation_deg);
    }

    /** Internal method to perform computation for a rectangle with height and width specified. */
    void compute(double degCenterLat, double degCenterLon, double meterWidth, double meterHeight, double degRotation) {

        this.degCenterLat = degCenterLat;
        this.degCenterLon = degCenterLon;
        this.rotation_deg = degRotation;
        this.width = meterWidth;
        this.height = meterHeight;

        double radCLon = Math.toRadians(degCenterLon);
        double radCLat = Math.toRadians(degCenterLat);

        double az = Math.toRadians(rotation_deg);
        double corner = Math.atan2(meterWidth, meterHeight);
        double dist_m = Math.hypot(0.5 * meterWidth, 0.5 * meterHeight);


        ne = NavUtils.getLatLon(radCLat, radCLon, dist_m, corner + az);
        nw = NavUtils.getLatLon(radCLat, radCLon, dist_m, -corner + az);
        se = NavUtils.getLatLon(radCLat, radCLon, dist_m, Math.PI - corner + az);
        sw = NavUtils.getLatLon(radCLat, radCLon, dist_m, Math.PI + corner + az);

        ne[0] = Math.toDegrees(ne[0]);
        ne[1] = Math.toDegrees(ne[1]);
        nw[0] = Math.toDegrees(nw[0]);
        nw[1] = Math.toDegrees(nw[1]);
        se[0] = Math.toDegrees(se[0]);
        se[1] = Math.toDegrees(se[1]);
        sw[0] = Math.toDegrees(sw[0]);
        sw[1] = Math.toDegrees(sw[1]);

    }

    /** Sets the rectangle bounds using 4-corners
     *
     * @param degLat1 latitude of first corner
     * @param degLon1 longitude of first corner
     * @param degLat2 latitude of second corner
     * @param degLon2 longitude of second corner
     */
    public void setRectFromCorners(double degLat1, double degLon1, double degLat2, double degLon2) {

        double nlat = Math.max(degLat1, degLat2);
        double dlat = Math.abs(degLat1 - degLat2);
        double wlon = Math.min(degLon1, degLon2);
        double dlon = Math.abs(degLon1 - degLon2);

        this.degCenterLat = nlat - 0.5 * dlat;
        this.degCenterLon = wlon + 0.5 * dlon;
        this.rotation_deg = 0;
        this.width = NavUtils.getRadius(Math.toRadians(degCenterLat)) * Math.toRadians(dlon) * Math.cos(Math.toRadians(degCenterLat));
        this.height = NavUtils.POLAR_RADIUS_M * Math.toRadians(dlat);

        this.ne = new double[]{nlat, wlon + dlon};
        this.nw = new double[]{nlat, wlon};
        this.sw = new double[]{nlat - dlat, wlon};
        this.se = new double[]{nlat - dlat, wlon + dlon};

    }

    /** returns the upper-right coordinate in degrees [lat,lon] */
    public double[] getUR() {
        return ne;
    }

    /** returns the lower-left coordinate in degrees [lat,lon] */
    public double[] getLL() {
        return sw;
    }

    /** returns the upper-left coordinate in degrees [lat,lon] */
    public double[] getUL() {
        return nw;
    }

    /** returns the lower right coordinate in degrees [lat,lon] */
    public double[] getLR() {
        return se;
    }

    /** returns the width of the rectangle at the center point (spherical earth approximation) */
    public double getWidth() {
        return width;
    }

    /** returns the height of the rectangle at the center point (spherical earth approximation) */
    public double getHeight() {
        return height;
    }

    /** Sets the width of the rectangle in meters */
    public void setWidth(double width) {
        compute(degCenterLat, degCenterLon, width, height, rotation_deg);
    }

    /** Sets the height of the rectangle in meters */
    public void setHeight(double height) {
        compute(degCenterLat, degCenterLon, width, height, rotation_deg);
    }

    /** returns the rotation value for this rectangle in degrees east of north */
    public double getRotation() {
        return rotation_deg;
    }

    /** Sets the rotation of the rectangle (degrees, East of North) */
    public void setRotation(double degRot) {
        this.rotation_deg = degRot;
        compute(degCenterLat, degCenterLon, width, height, degRot);
    }

    public void setCenter(double degCenterLat, double degCenterLon) {
        this.degCenterLat = degCenterLat;
        this.degCenterLon = degCenterLon;
        compute(degCenterLat, degCenterLon, width, height, rotation_deg);
    }

    public double getCenterLat() {
        return degCenterLat;
    }

    public double getCenterLon() {
        return degCenterLon;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */