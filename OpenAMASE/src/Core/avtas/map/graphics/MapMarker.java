// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.graphics;

import java.awt.Rectangle;
import java.awt.Shape;
import avtas.map.Proj;
import java.awt.geom.AffineTransform;

/**
 * @author AFRL/RQQD
 */
public class MapMarker extends MapGraphic {

//    double lat = 0;
//    double lon = 0;
    int x = 0;
    int y = 0;
    double lat = 0;
    double lon = 0;
    int type = 0;
    int xoff = 3;
    int yoff = 3;
    public static int TYPE_SQUARE = 0;
    public static int TYPE_CIRCLE = 1;
    public static int TYPE_DOT = 2;
    Shape marker = new Rectangle(6, 6);

    /** Creates a new instance of MapMarker */
    public MapMarker() {
        this(0, 0);
    }

    public MapMarker(double lat, double lon) {
        this(lat, lon, 6);
    }

    public MapMarker(double lat, double lon, int markerSize) {
        this.lat = lat;
        this.lon = lon;

        xoff = markerSize / 2;
        yoff = xoff;
        marker = new Rectangle(markerSize, markerSize);
        marker.getBounds().translate(xoff, yoff);
        setProjected(false);
    }

    public void setMarkerShape(Shape shape) {
        this.marker = shape;
        xoff = marker.getBounds().width / 2;
        yoff = marker.getBounds().height / 2;
        //marker.getBounds().translate(xoff, yoff);
        setProjected(false);
    }

//    public void paint(Graphics2D g2) {
//
//        if (!isProjected() || !isVisible()) {
//            return;
//        }
//
//        g2.setPaint(getColor());
//        g2.setStroke(getStroke());
//        g2.translate(x - xoff, y - yoff);
//
//        g2.draw(marker);
//        if (getFill() != null) {
//            g2.setPaint(getFill());
//            g2.fill(marker);
//        }
//        g2.translate(-x + xoff, -y + yoff);
//
//        if (isSelected()) {
//            g2.setPaint(getSelectedColor());
//            g2.draw(getBounds());
//        }
//    }

    @Override
    public void project(Proj proj) {
        setProjected(false);

        if (proj == null || marker == null) {
            return;
        }

        x = (int) proj.getX(lon);
        y = (int) proj.getY(lat);

        //getBounds().setLocation(x - xoff, y - yoff);
        AffineTransform xform = AffineTransform.getTranslateInstance(x - xoff, y - yoff);
        setScreenShape(xform.createTransformedShape(marker));

        setProjected(true);
    }

    /** returns the latitude in degrees */
    public double getLat() {
        return lat;
    }

    public void setLat(double lat_deg) {
        this.lat = lat_deg;
        setProjected(false);
    }

    /** returns the longitude in degrees */
    public double getLon() {
        return lon;
    }

    public void setLon(double lon_deg) {
        this.lon = lon_deg;
        setProjected(false);
    }

    public boolean intersects(int x, int y) {
        return Math.abs(x - this.x) < 2 || Math.abs(y - this.y) < 2;
    }

    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
}
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */