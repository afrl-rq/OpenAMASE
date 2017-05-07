// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.util;


/**
 * Defines a rectangle in world coordinates (lat, lon)
 *
 * @author AFRL/RQQD
 */
public class WorldBounds {
    
    private double degCenterLat, degCenterLon, degLatHeight, degLonWidth;
    
    public WorldBounds() {
    }

    public WorldBounds(double degNorthLat, double degWestLon, double degLonWidth, double degLatHeight) {
        setRect(degNorthLat, degWestLon, degNorthLat - degLatHeight, degWestLon + degLonWidth);
    }

    public static WorldBounds fromCorners(double degNorthLat, double degWestLon, double degSouthLat, double degEastLon) {
        WorldBounds b =  new WorldBounds();
        b.setRect(degNorthLat, degWestLon, degSouthLat, degEastLon);
        return b;
    }

    public static WorldBounds fromCenter(double degLatCenter, double degLonCenter, double degLonWidth, double degLatHeight) {
        return new WorldBounds(degLatCenter + 0.5 * degLatHeight, degLonCenter - 0.5 * degLonWidth,
                degLonWidth, degLatHeight);
    }

    protected void setRect(double degNorthLat, double degWestLon, double degSouthLat, double degEastLon) {
        this.degCenterLat = WorldMath.normLat(0.5 * (degNorthLat + degSouthLat));
        this.degCenterLon = WorldMath.wrapLon(0.5 * (degWestLon + degEastLon));
        this.degLonWidth = Math.abs(WorldMath.wrapLon(degWestLon - degEastLon));
        this.degLatHeight = WorldMath.normLat(Math.abs(degNorthLat - degSouthLat));
    }

    public boolean intersects(double degLat, double degLon) {
        double dlat = Math.abs(WorldMath.normLat(degCenterLat - degLat));
        double dlon = Math.abs(WorldMath.wrapLon(degCenterLon - degLon));

        return dlat < 0.5 * degLatHeight && dlon < 0.5 * degLonWidth;
    }

    public boolean intersects(double degNorthLat, double degWestLon, double degSouthLat, double degEastLon) {
        double lh = 0.5 * this.degLatHeight;
        double lw = 0.5 * this.degLonWidth;

        return degSouthLat < this.degCenterLat + lh
                && degNorthLat > this.degCenterLat - lh
                && WorldMath.wrapLon(degWestLon - this.degCenterLon) < lw
                && WorldMath.wrapLon(degEastLon - this.degCenterLon) > -lw;
    }

    public boolean intersects(WorldBounds bounds) {
        return intersects(bounds.getNorthLat(), bounds.getWestLon(), bounds.getSouthLat(), bounds.getEastLon());
    }

    public double getNorthLat() {
        return degCenterLat + 0.5 * degLatHeight;
    }

    public double getSouthLat() {
        return degCenterLat - 0.5 * degLatHeight;
    }

    public double getWestLon() {
        return WorldMath.wrapLon(degCenterLon - 0.5 * degLonWidth);
    }

    public double getEastLon() {
        return WorldMath.wrapLon(degCenterLon + 0.5 * degLonWidth);
    }

    public double getLonWidth() {
        return degLonWidth;
    }

    public double getLatHeight() {
        return degLatHeight;
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */