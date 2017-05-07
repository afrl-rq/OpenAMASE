// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.math;

import java.util.List;


/**
 * Utility class that defines math functions for array operations (addition, subtraction
 * division and multiplication) and interpolation functions.
 * @author default
 */
public class MathUtils  {
    
    public final static int MULTIPLY = 1;
    public final static int DIVIDE = 2;
    public final static int ADD = 3;
    public final static int SUBTRACT = 4;

    
    
    /** Creates a new instance of MathArrayUtils
     */
    public MathUtils() {
    }
    
    /** multiplies each element in the given array by the scalar value 
     */
    public static void multiply(double[] array, double scalar) {
        for (int i=0; i<array.length; i++) {
            array[i] = array[i]*scalar;
        }
    }
    
    /** divides each element in the given array by the scalar value 
     */
    public static void divide(double[] array, double scalar) {
        for (int i=0; i<array.length; i++) {
            array[i] = array[i]/scalar;
        }
    }
    
    /** 
     * adds the scalar value from each element in the given array.
     */
    public static void add(double[] array, double scalar) {
        for (int i=0; i<array.length; i++) {
            array[i] = array[i]+scalar;
        }
    }
    
    /** 
     * subtracts the scalar value from each element in the given array.
     */
    public static void subtract(double[] array, double scalar) {
        for (int i=0; i<array.length; i++) {
            array[i] = array[i]-scalar;
        }
    }
    
    /** raises each element in the given array by the scalar value and returns a 
        new array.
     */
    public static void power(double[] array, double power) {
        for (int i=0; i<array.length; i++) {
            array[i] = Math.pow(array[i], power);
        }
    }

    /**
     * Finds the two indices for values in an array, where the first index is the greatest
     * lower bound for the value, x, and the second is the smallest upper bound. If all
     * values in the array are less than x, the last two indices in the array are
     * returned.  If the first value in the array is greater than x, the first two
     * indices are returned.  This assumes that the array of values is monotonically
     * increasing.
     *
     * @param   xs      The array of x values.
     * @param   x       The given x value.
     * @return          The indices for the smallest upper bound and the greatest lower bound.
     */
    public static int[] interpIndicies( double[] xs, double x) {
        int ind0 = -1;
        int ind1 = -1;
        for ( int i=0; i<xs.length; i++ ) {
            if (xs[i] > x) {
                ind0 = i-1;
                ind1 = i;
                break;
            }
        }
        if (ind1 == -1) {
            ind0 = xs.length-2;
            ind1 = xs.length-1;
        }
        if (ind0 == -1) {
            ind1 = 1;
            ind0 = 0;
        }
        return new int[] { ind0, ind1 };
    } 

    /**
     * Computes a simple linear interpolation between two points.
     * @param x1    The x value for the first interpolation point.
     * @param y1    The y value for the first interpolation point.
     * @param x2    The x value for the second interpolation point.
     * @param y2    The y value for the second interpolation point.
     * @param x     The x value for the new point.
     * @return      The y value for the new point.
     */
    public static double linterp( double x1, double y1, double x2, double y2, double x) {
        double slope = (x - x1) / ( x2 - x1);
        double y = y1 + slope * ( y2 - y1 );
        
        return y;
    }
    
    /**
     * Computes a linear interpolation given an array of x and y values and a value, x.
     * If there are fewer x values than y values, or there are fewer than two y values,
     * the method prints out an error message.  <br>What happens then?</br>
     *
     * @param xs    Array of x values.
     * @param ys    Array of y values.
     * @param x     The point of interest.
     * @return      The new y value.
     */
    public static double linterp( double[] xs, double[] ys, double x ) {
        if (xs.length < ys.length || ys.length < 2) {
            System.out.println("not enough values");
        }
        int[] ind = interpIndicies( xs, x);
        
        return linterp( xs[ind[0]], ys[ind[0]], xs[ind[1]], ys[ind[1]], x);
    }

    /** Computes an ordinary least squares linear fit to the given data.  Least squares approximation
     * computes a line that minimizes the square of the difference between the line and the interpolated
     * points.  This technique is taken from http://www.wikipedia.org "Ordinary least squares" page.
     * <br>
     * This method computes an equation y = b * x + a.
     *
     * @param xvals x-values to interpolate
     * @param yvals y-values to interpolate
     * @return an array of least squares coefficients according to the above equation, and the R squared
     * value.  [a, b, R^2]
     */
    public static double[] leastSquares(double[] xvals, double[] yvals) {

        if (xvals.length != yvals.length) {
            return null;
        }

        double sumx = 0;
        double sumx2 = 0;
        double sumy = 0;
        double sumxy = 0;
        double n = xvals.length;

        for (int i=0; i<n; i++) {
            double x = xvals[i];
            double y = yvals[i];
            sumx += x;
            sumx2 += x*x;
            sumy += y;
            sumxy += x*y;
        }
        double xbar = sumx / n;
        double ybar = sumy / n;

        double b = (n * sumxy - sumx * sumy ) / ( n * sumx2 - sumx * sumx );
        double a = ybar - b * xbar;

        // process for R squared.  Sum the squares of the difference bwtween the
        // interpolated y-values and average y, actual y values and average y.
        // the quotient of the interpolated and actual is R-squared.
        double yhatdiff = 0;
        double ydiff = 0;
        for (int i = 0; i < n; i++) {
            yhatdiff += Math.pow(a + b * xvals[i] - ybar, 2);
            ydiff += Math.pow(yvals[i] - ybar, 2);
        }

        double R2 = yhatdiff / ydiff;

        return new double[] { a, b, R2};
    }
    
    /**
     * Returns the arithmetic mean of a list of values.
     * @param values
     * @return 
     */
    public static double average(double... values) {
        double total = 0;
        for (double val : values) {
            total += val;
        }
        return total/values.length;
    }
    
    
    /**
     * Returns the largest value of the set.
     * @param values
     * @return 
     */
    public static double max(double... values) {
        double max = Double.NEGATIVE_INFINITY;
        for (double val : values) {
            if (val > max) {
                max = val;
            }
        }
        return max;
    }
}



class Set {
    double key;
    Object value;
    
    public Set( double key, Object value) {
        this.key = key;
        this.value = value;
    }
    
    public boolean equals(Object o) {
        if (o instanceof Set) {
            if ( ((Set) o).key == key) return true;
        }
        return false;
    } 
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */