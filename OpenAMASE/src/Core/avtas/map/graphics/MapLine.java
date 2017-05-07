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
 * Creates a two-point line
 *
 * @author AFRL/RQQD
 */
public class MapLine extends MapGraphic {

    double lat1 = 0;
    double lon1 = 0;
    double dlon = 0;
    double dlat = 0;
    int x1 = 0;
    int y1 = 0;
    int x2 = 0;
    int y2 = 0;
    private double lon2;
    private double lat2;

    public MapLine() {
        this(0, 0, 0, 0);
    }

    /** Creates a new instance of MapLine */
    public MapLine(double lat1_deg, double lon1_deg, double lat2_deg, double lon2_deg) {
        setLine(lat1_deg, lon1_deg, lat2_deg, lon2_deg);
    }

    public void setLine(double lat1_deg, double lon1_deg, double lat2_deg, double lon2_deg) {
        this.lat1 = lat1_deg;
        this.lon1 = lon1_deg;
        this.lat2 = lat2_deg;
        this.lon2 = lon2_deg;

        dlat = lat2_deg - lat1_deg;
        dlon = lon2_deg - lon1_deg;

        if (Math.abs(dlon) > 180) {
            dlon = dlon - 360 * Math.signum(dlon);
        }

        //setProjected(false);
    }

    public void setStartPt(double degLat, double degLon) {
        setLine(degLat, degLon, lat2, lon2);
    }

    public void setEndPt(double degLat, double degLon) {
        setLine(lat1, lon1, degLat, degLon);
    }

    public double getStartLat() {
        return lat1;
    }

    public double getStartLon() {
        return lon1;
    }

    public double getEndLat() {
        return lat2;
    }

    public double getEndLon() {
        return lon2;
    }

    @Override
    public void project(Proj proj) {

        setProjected(false);

        if (proj == null) {
            return;
        }

        x1 = (int) proj.getX(lon1);
        y1 = (int) proj.getY(lat1);
        x2 = x1 + (int) (proj.getPixPerLon() * dlon);
        y2 = y1 - (int) (proj.getPixPerLat() * dlat);
        setScreenShape(new Line2D.Double(x1, y1, x2, y2));
        setProjected(true);
    }

    public boolean intersects(int x, int y) {
        return Math.abs((x - x1) * (y2 - y1) - (x2 - x1) * (y - y1)) < 2;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */