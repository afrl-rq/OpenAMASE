// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.math;

/**
 * Stores values for a 1-dimensional table (single independent variable)
 * @author AFRL/RQQD
 */
public class Table {
    
    private final double[] xvals;
    private final double[] yvals;
    
    /**
     * Creates a new table.  The length of x and y values must be equal.
     * The independent variables must be monotonic for interpolation to 
     * work.
     * 
     * @param xvals a collection of independent variables
     * @param yvals a collection of dependent variables
     * 
     */
    public Table(double[] xvals, double[] yvals) {
        this.xvals = xvals;
        this.yvals = yvals;
    }
    
    /** 
     * returns the independent variables 
     */
    public double[] getXs() {
        return xvals;
    }
    
    /** 
     * returns the dependent variables 
     */
    public double[] getYs() {
        return yvals;
    }
    
    /**
     * Performs a linear interpolation on the contained data
     * @param xval interpolation value.  
     * @return the value at the given point in the function, using linear
     * interpolation.
     */
    public double interp(double xval) {
        return MathUtils.linterp(xvals, yvals, xval);
    }
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */