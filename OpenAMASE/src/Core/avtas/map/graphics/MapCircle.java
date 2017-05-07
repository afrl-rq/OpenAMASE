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
import java.awt.geom.Ellipse2D;

/**
 * Creates a circle
 *
 * @author AFRL/RQQD
 */
public class MapCircle extends MapGraphic {

    double x, y, lat, lon;
    int width = 0;
    int height = 0;
    double ewDist = 0;
    double nsDist = 0;
    double meterRadius = 0;
    //double rangeY = 0;
    double rotation = 0;
    Ellipse2D ellipse = new Ellipse2D.Double();
    int xdist = 0;

    /** Creates a new instance of MapEllipse */
    public MapCircle() {
    }

    /** creates a circle at given point and with given range (meters) */
    public MapCircle(double lat, double lon, double meterRadius) {
        setRadius(meterRadius);
        setCenter(lat, lon);
    }

    public void setCenter(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        ewDist = 2 * WGS84.getDeltaLon(lat, meterRadius);
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

    /** sets a circular radius
     *
     * @param radius_meters radius of the circle
     */
    public void setRadius(double radius_meters) {
        this.meterRadius = radius_meters;
        nsDist = 2.0 * radius_meters / WGS84.polarCircum * 360;
        ewDist = 2 * WGS84.getDeltaLon(lat, meterRadius);
    }

    /**
     *
     * @return radius in meters
     */
    public double getRadius() {
        return meterRadius;
    }

    public void project(Proj proj) {

        setProjected(false);

        if (proj == null) {
            return;
        }

        x = proj.getX(lon);
        y = proj.getY(lat);

        height = (int) (nsDist * proj.getPixPerLat());
        width = (int) (ewDist * proj.getPixPerLon());

        ellipse.setFrame((int) (x - width / 2), (int) y - height / 2, width, height);
        setScreenShape(ellipse);
        setProjected(true);
    }


}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */