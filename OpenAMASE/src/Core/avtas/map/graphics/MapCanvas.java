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
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 * Similar to MapRaster, except that the paintGraphics() method calls for an external draw of the graphics.
 * @author AFRL/RQQD
 */
public abstract class MapCanvas extends MapGraphic {

    double x = 0;
    double y = 0;
    double lat = 0;
    double lon = 0;
    private double rotation = 0;
    double projRotation = 0;
    private boolean rotateWithMap = false;

    /** calls for the graphics to be painted.  The graphics origin is centered around lat, lon */
    public abstract void paintGraphics(Graphics2D g);

    /** Should return the size of the drawable area (for interaction purposes) */
    public abstract Dimension getSize();

    public void paint(Graphics2D g2) {
        
        if (!isVisible()) {
            return;
        }

        g2.translate(x, y);
        if (!rotateWithMap) {
            g2.rotate(projRotation);
        }
        g2.rotate(rotation);

        paintGraphics(g2);

    }
    
    public void project(Proj proj) {
        setProjected(false);
        
        if (proj != null) {
            x = proj.getX(lon);
            y = proj.getY(lat);
            projRotation = proj.getRotation();
            setProjected(true);
        }
    }

    @Override
    public Rectangle getBounds() {
        if (isProjected() && isVisible()) {
            Rectangle r = new Rectangle(getSize());
            r.translate((int) (x - 0.5 * r.width), (int) (y - 0.5 * r.height));
            return r;
        }
        return null;
    }



    public void setLatLon(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    

    /** returns rotation in radians */
    public double getRotation() {
        return rotation;
    }

    /** sets the rotation in radians */
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public boolean rotatesWithMap() {
        return rotateWithMap;
    }

    public void setRotateWithMap(boolean rotateWithMap) {
        this.rotateWithMap = rotateWithMap;
    }

    @Override
    public boolean intersects(Rectangle2D otherShape) {
        Dimension size = getSize();
        return otherShape.intersects(x-size.width*0.5, y-size.height*0.5, size.width, size.height);
    }


}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */