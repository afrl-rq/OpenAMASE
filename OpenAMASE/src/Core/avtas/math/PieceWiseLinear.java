// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.math;

import javax.swing.JFrame;

/**
 *  Defines a piece-wise linear polynomial and interpolation methods.s
 * @author AFRL/RQQD
 */
public class PieceWiseLinear {
    
    double[] x = new double[2];
    double[] y = new double[2];
    
    double[] m = new double[1];  //slope
    double[] b = new double[1];  //x=0 y-intercept
    
    /** Creates a new instance of Piece-wise polynomial. xvals must be monotonically
     * increasing.
     * @param xvals     The array of x values.
     * @param yvals     The array of y values.
     */
    public PieceWiseLinear(double[] xvals, double[] yvals) {
        if ( x == null || y == null ) return; 
        if ( x.length < 2 || y.length < 2 ) return;
        if ( x.length != y.length ) return;

        int num = xvals.length;
        x = xvals.clone();
        y = yvals.clone();
        m = new double[num-1];
        b = new double[num-1];
        
        computeLines();
    }
    /**
     * Computes the slopes and x-intercepts for each line in the polynomial.
     */
    private void computeLines() {
        for (int i=0; i < m.length; i++) {
            m[i] = ( y[i+1] - y[i] ) / ( x[i+1] - x[i]);
            b[i] = y[i] - m[i] * x[i];
        }
    }
    /**
     * Returns the bin for a given x value.  The bin number is defined as the lower
     * index for the two closest values in the x array that bound the given x value.
     * If the x value is upper bounded by every element in the array, 0 is returned.
     * If the x value is lower bounded by every element in the array, the n-2 index is
     * returned, where n is the length of the x array.
     *
     * @param xval      The x value to find the bin for.
     * @return          The bin that the given x value is in.
     */
    public int getBin( double xval ) {
        if( xval < x[0] || x.length == 1) return 0;
        
        for (int i=1; i<x.length; i++) {
            if (x[i] > xval) {
                return i-1;
            }
        }
        return x.length - 2;
    }
    /** Get a yval for a given bin number.  When would this be used instead of the
     * other <code>getVal</code> method?  The other method would ensure that the bin
     * corresponds to the 
     * @param   bin     The bin number for the x value.
     * @param   xval    The x value.
     */
    public double getVal(int bin, double xval) {
        //if(bin == 0) return y[0];
        //if(bin == m.length-1) return y[y.length-1];
        
        return m[bin] * xval + b[bin];
    }
    
    /** Get a yval by linear interpolation.
     * @param xval  The x value.
     */
    public double getVal( double xval) {
        if ( xval < x[0] ) return y[0];
        if ( xval > x[x.length-1] ) return y[x.length-1];
        int bin = getBin(xval);
        return getVal(bin, xval);
    }
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */