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
 * Enumeration of popular datums.  Shifts are in meters.
 * 
 * source: "Datum Transformations of GPS Positions", μ-blox ag 
 * Gloriastrasse 35, CH-8092 Zürich, Switzerland (http://www.u-blox.ch)
 * 
 * @author AFRL/RQQD
 * @version 0.5
 */
public enum Datum {

    NAD27_Alaska(Ellipsoid.Clarke_1866, -5., 135., 172.),
    NAD27_Alaska_Aleutian_Islands_E(Ellipsoid.Clarke_1866, -2, 152, 149),
    NAD27_Alaska_Aleutian_Islands_W(Ellipsoid.Clarke_1866, 2, 204, 105),
    NAD27_Bahamas(Ellipsoid.Clarke_1866, -4, 154, 178),
    NAD27_Bahamas_San_Salvador(Ellipsoid.Clarke_1866, 1, 140, 165),
    NAD27_Canada_Yukon(Ellipsoid.Clarke_1866, -7, 139, 181),
    NAD27_Canal_Zone(Ellipsoid.Clarke_1866, 0, 125, 201),
    NAD27_Central_America(Ellipsoid.Clarke_1866, 0, 125, 194),
    NAD27_Central_Canada(Ellipsoid.Clarke_1866, -9, 157, 184),
    NAD27_Cuba(Ellipsoid.Clarke_1866, -9, 152, 178),
    NAD27_East_Canada(Ellipsoid.Clarke_1866, -22, 160, 190),
    NAD27_East_of_Mississippi(Ellipsoid.Clarke_1866, -9, 161, 179),
    NAD27_Greenland(Ellipsoid.Clarke_1866, 11, 114, 195),
    NAD27_Gulf_of_Mexico(Ellipsoid.Clarke_1866, -3, 142, 183),
    NAD27_Mean_for_Canada(Ellipsoid.Clarke_1866, -10, 158, 187),
    NAD27_Mean_for_Conus(Ellipsoid.Clarke_1866, -8, 160, 176),
    NAD27_Mexico(Ellipsoid.Clarke_1866, -12, 130, 190),
    NAD27_Northwest_Canada(Ellipsoid.Clarke_1866, 4, 159, 188),
    NAD27_West_Canada(Ellipsoid.Clarke_1866, -7, 162, 188),
    NAD27_West_of_Mississippi(Ellipsoid.Clarke_1866, -8, 159, 175),
    NAD83_Alaska_Canada_Conus(Ellipsoid.GRS_80, 0, 0, 0),
    NAD83_Aleutian_Islands(Ellipsoid.GRS_80, -2, 0, 4),
    NAD83_Central_America_Mexico(Ellipsoid.GRS_80, 0, 0, 0),
    NAD83_Hawaii(Ellipsoid.GRS_80, 1, 1, -1),
    WGS_72 (Ellipsoid.WGS_72, 0, 0, 0),
    WGS_84 (Ellipsoid.WGS_84, 0, 0, 0);
    
    
    private final double dx,  dy,  dz;
    private final Ellipsoid ellipse;

    Datum(Ellipsoid ellip, double dx, double dy, double dz) {
        this.ellipse = ellip;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    /* x-axis shift from baseline in ECEF coordinates */
    public double getDx() {
        return dx;
    }

    /* y-axis shift from baseline in ECEF coordinates */
    public double getDy() {
        return dy;
    }

    /* z-axis shift from baseline in ECEF coordinates */
    public double getDz() {
        return dz;
    }
    
    /** returns the reference ellipsoid used in this datum */
    public Ellipsoid getEllipsoid() {
        return ellipse;
    }
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */