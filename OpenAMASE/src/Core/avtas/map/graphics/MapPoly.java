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
import avtas.util.NavUtils;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

/**
 *
 * @author AFRL/RQQD
 */
public class MapPoly extends MapGraphic {

    ArrayList<Double> lats = new ArrayList<Double>();
    ArrayList<Double> lons = new ArrayList<Double>();
    boolean isPolygon = false;

    /** Creates a new instance of MapPolyLine */
    public MapPoly() {
    }

    public MapPoly(double[] lats, double[] lons) {
        if (lats.length != lons.length) {
            return;
        }
        for (int i = 0; i < lats.length; i++) {
            addPoint(lats[i], lons[i]);
        }
    }

    public MapPoly(double[] latlons) {
        if (latlons.length < 2) {
            return;
        }
        for (int i = 0; i < latlons.length; i += 2) {
            addPoint(latlons[i], latlons[i + 1]);
        }
    }

    public void setPolygon(boolean isPolygon) {
        this.isPolygon = isPolygon;
    }

    public boolean isPolygon() {
        return isPolygon;
    }

    @Override
    public void project(Proj proj) {

        setProjected(false);

        if (proj == null) {
            return;
        }

        Path2D tmpPoly = new Path2D.Double();
        tmpPoly.reset();
        if (getNumPoints() > 0) {
            tmpPoly.moveTo(proj.getX(lons.get(0)), proj.getY(lats.get(0)));
        }
        else {
            tmpPoly.moveTo(0, 0);
        }
        for (int i = 1; i < getNumPoints(); i++) {
            tmpPoly.lineTo(proj.getX(lons.get(i)), proj.getY(lats.get(i)));
        }
        
        int w = proj.getWidth();
        int h = proj.getHeight();
        Rectangle b = tmpPoly.getBounds();
        
        setProjected(b.getMinX() < w && b.getMaxX() > 0 
                && b.getMinY() < h && b.getMaxY() > 0);
        
        if (isPolygon()) {
            tmpPoly.closePath();
        }
        setScreenShape(tmpPoly);


    }

    public void addPoint(double lat, double lon) {
        lons.add(lon);
        lats.add(lat);
    }

    /** only adds a point if the distance from the last point is greater than
     *  the meters distance specified.
     * @param lat
     * @param lon
     * @param meterDiff
     */
    public void addPoint(double lat, double lon, double meterDiff) {
        if (getNumPoints() == 0) {
            addPoint(lat, lon);
            return;
        }
        
        double dist = NavUtils.distance(Math.toRadians(lat), Math.toRadians(lon),
                Math.toRadians(lats.get(lats.size()-1)), 
                Math.toRadians(lons.get(lons.size()-1))); 

        // only add the point if there has been significant motion 
        if (dist > meterDiff) {
            lats.add(lat);
            lons.add(lon);
        }
        //setProjected(false);
    }

    public void insertPoint(double degLat, double degLon, int index) {
        if (index < 0 || index >= getNumPoints()) {
            addPoint(degLat, degLon);
        }
        else {
            lons.add(index, degLon);
            lats.add(index, degLat);
        }
        //setProjected(false);
    }

    public void updatePoint(double degLat, double degLon, int index) {
        if (index < 0 || index >= getNumPoints()) {
            return;
        }
        else {
            lons.set(index, degLon);
            lats.set(index, degLat);
        }
        //setProjected(false);
    }

    /** Returns the number of points in this polygon */
    public int getNumPoints() {
        return lons.size();
    }

    /** returns [lat, lon] in degrees for the requested point */
    public double[] getPoint(int index) {
        if (index >= 0 && index < getNumPoints()) {
            return new double[] {lats.get(index), lons.get(index)};
        }
        return null;
    }

    /**
     * Returns an array of latitude, longitude pairs for all points in the poly
     * @return array of lat, lons in degrees
     */
    public double[] getLatLons() {

        double[] ret = new double[getNumPoints()*2];
        for (int i=0; i<getNumPoints(); i++) {
            ret[i*2] = lats.get(i);
            ret[i*2+1] = lons.get(i);
        }
        return ret;
    }
    
    /** Returns the index of a point in the poly that has the shortest Euclidian distance in screen coordinates.
     * @param screenX 
     * @param screenY
     * @return index Point closest to the screen coordinates given, or -1 id the shape is not projected
     */
    public int getNearestPoint(double screenX, double screenY) {
        int nearest = -1;
        if (screenShape instanceof Path2D) {
            Path2D path = (Path2D) screenShape;
            int i=0;
            double dist = Double.POSITIVE_INFINITY;
            double[] coords = new double[6];
            for (PathIterator it = path.getPathIterator(null); !it.isDone(); ) {
                it.next();
                it.currentSegment(coords);
                double d = Math.hypot(screenX - coords[0], screenY - coords[1]);
                if (d < dist) {
                    dist = d;
                    nearest = i;
                }
                i++;
            }
        }
        return nearest;
    }

    /** returns the geographic center of the polygon as [latitude, longitude] in 
     *  degrees.
     * @return an array (length = 2) of latitude, longitude in degrees.
     */
    public double[] getCenter() {
        double clat = 0;
        double clon = 0;
        int n = getNumPoints();
        for(int i=0; i<n; i++) {
            clat += lats.get(i);
            clon += lons.get(i);
        }
        
        return new double[] {clat/n, clon/n};
        
    }

    public void deletePoint(int index) {
        if (index >=0 && index <getNumPoints()) {
            lons.remove(index);
            lats.remove(index);
        }
        //setProjected(false);
    }

    public void clear() {
        lats.clear();
        lons.clear();
        setProjected(false);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */