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
 * Description of a planet ellipsoid.
 * 
 * source: "Datum Transformations of GPS Positions", μ-blox ag 
 * Gloriastrasse 35, CH-8092 Zürich, Switzerland (http://www.u-blox.ch)
 * 
 * @author AFRL/RQQD
 */
public enum Ellipsoid {

    Airy(6377563.396, 6356256.909, 299.324965),
    Airy_Modified(6377340.189, 6356034.448, 299.324965),
    Australian_National(6378160.000, 6356774.719, 298.250000),
    Bessel_1841(6377397.155, 6356078.963, 299.152813),
    Bessel_1841_Namibia(6377483.865, 6356165.383, 299.152813),
    Clarke_1866(6378206.400, 6356583.800, 294.978698),
    Clarke_1880(6378249.145, 6356514.870, 293.465000),
    Everest_Sabah_Sarawak(6377298.556, 6356097.550, 300.801700),
    Everest_1830(6377276.345, 6356075.413, 300.801700),
    Everest_1948(6377304.063, 6356103.039, 300.801700),
    Everest_1956(6377301.243, 6356100.228, 300.801700),
    Everest_1969(6377295.664, 6356094.668, 300.801700),
    Fischer_1960(6378166.000, 6356784.284, 298.300000),
    Fischer_1960_Modified(6378155.000, 6356773.320, 298.300000),
    Fischer_1968(6378150.000, 6356768.337, 298.300000),
    GRS_80(6378137.000, 6356752.314, 298.257222),
    Helmert_1906(6378200.000, 6356818.170, 298.300000),
    Hough(6378270.000, 6356794.343, 297.000000),
    International(6378388.000, 6356911.946, 297.000000),
    Krassovsky(6378245.000, 6356863.019, 298.300000),
    SGS_85(6378136.000, 6356751.302, 298.257000),
    South_American_1969(6378160.000, 6356774.719, 298.250000),
    WGS_60(6378165.000, 6356783.287, 298.300000),
    WGS_66(6378145.000, 6356759.769, 298.250000),
    WGS_72(6378135.000, 6356750.520, 298.260000),
    WGS_84(6378137.000, 6356752.314, 298.257224);
    
    private final double a, b, ff, ecc;

    Ellipsoid(double a, double b, double ff) {
        this.a = a;
        this.b = b;
        this.ff = ff;
        this.ecc = Math.sqrt( (a * a + b * b) / ( a * a) );
    }
    
    /** returns the semi-major axis (polar radius) */
    public double getA() {
        return a;
    }
    
    /** returns the semi-minor axis (equatorial radius) */
    public double getB() {
        return b;
    }
    
    /** returns the ellipsoid flattening value */
    public double getF() {
        return ff;
    }
    
    /** returns the eccentricity, sqrt((a*a + b*b) / (a*a)) */
    public double getEcc() {
        return ecc;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */