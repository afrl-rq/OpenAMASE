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
 * A class that defines a 3-Vector and implements associated operations such as
 * addition, subtraction, cross product and dot product.
 * @author AFRL/RQQD
 */
public class Vector3 {
    
    protected double a1;
    protected double a2;
    protected double a3;
    
    /** Creates a new instance of Vector3 from three input doubles representing
     * the three vector components.
     * @param   a1      The first vector component.
     * @param   a2      The second vector component.
     * @param   a3      The third vector component.
     */
    public Vector3(double a1, double a2, double a3) {
        set1(a1);
        set2(a2);
        set3(a3);
    }

    /** Creates a Vector3 using the values from an array.  The array must be at least
     * a length of 3.
     * @param vals
     */
    public Vector3(double[] vals) {
        this(vals[0], vals[1], vals[2]);
    }
    /**
     * Creates a Vector3 by initializing all components to zero.
     */
    public Vector3() {
        this(0, 0, 0);
    }
    /**
     * Creates and returns a clone of this vector.
     * @return      The clone of this vector.
     */
    public Vector3 clone() {
        return new Vector3(a1, a2, a3);
    }
    /**
     * Get the first vector component.
     * @return      The first vector component.
     */
    public double get1() { return a1; }
    /**
     * Get the second vector component.
     * @return      The second vector component.
     */
    public double get2() { return a2; }
    /**
     * Get the third vector component.
     * @return      The third vector component
     */
    public double get3() { return a3; }
    /**
     * Set the first vector component.
     * @param val       The desired value for the first vector component.
     */
    public void set1( double val ) { a1 = val; }
    /**
     * Set the second vector component.
     * @param val       The desired value for the second vector component.
     */
    public void set2( double val ) { a2 = val; }
    /**
     * Set the third vector component.
     * @param val       The desired value for the third vector component.
     */
    public void set3( double val ) { a3 = val; }
    
    /**
     * Get a vector element based on the row number.  Returns a1 if the row value input
     * is less than or equal to 1.  Returns a3 if the row value input is greater than
     * or equal to 3.  ** What's the purpose of this method? **
     * @param row       The row value.
     * @return          The vector component.
     */
    public double get(int row) {
        return row <= 1 ? a1 : row == 2 ? a2 : a3;
    }

    /**
     * Sets the vector component based on the row number.  ** What's the purpose of this method? **
     * @param row       The row value.
     * @param val       The desired vector component value.
     */
    public void set(int row, double val) {
        switch(row) {
            case 1 : set1(val); break;
            case 2 : set2(val); break;
            case 3 : set3(val); break;
        }
    }
    
    public void normalize() {
        double norm = magnitude();
        if (norm != 0 && norm != 1) {
            a1 = a1 / norm;
            a2 = a2 / norm;
            a3 = a3 / norm;
        }
    }
    
    /**
     * Defines the cross product operation between this vector and another one.
     * @param v     The other vector.
     */
    public void cross(Vector3 v) {
        
        double val1 = get2() * v.get3() - a3 * v.get2();
        double val2 = a3 * v.get1() - a1 * v.get3();
        double val3 = a1 * v.get2() - a2 * v.get1();
        set1(val1);
        set2(val2);
        set3(val3);
        
    }
    /**
     * Defines the cross product operation between two input vectors.
     * @param v1        The first vector.
     * @param v2        The second vector.
     * @return          The resulting Vector3 object
     */
    public static Vector3 cross(Vector3 v1, Vector3 v2) {
        return new Vector3(
                v1.a2 * v2.a3 - v1.a3 * v2.a2,
                v1.a3 * v2.a1 - v1.a1 * v2.a3,
                v1.a1 * v2.a2 - v1.a2 * v2.a1
                );
    }
    /**
     * Multiplies this vector by the input scalar value.
     * @param val       The scalar value.
     */
    public void multiply(double val) {
        set1( a1 * val );
        set2( a2 * val );
        set3( a3 * val );
    }
    /**
     * Multiplies the input vector by the input scalar.
     * @param v     The input vector.
     * @param val   The input scalar.
     * @return      The resulting <code>Vector3</code> object.
     */
    public static Vector3 multiply(Vector3 v, double val) {
        return new Vector3( v.get1() * val, v.get2() * val,  v.get3() * val );
    }
    /**
     * Defines an element-wise multiplication operation between this vector and another one.  Modifies
     * this vector with the result.
     * @param v
     */
    public void multiply(Vector3 v) {
        set1( a1 * v.get1() );
        set2( a2 * v.get2() );
        set3( a3 * v.get3() );
    }
    /**
     * Defines the element-wise multiplication operation for two input vectors.  Returns the result.
     * @param v1        The first input vector.
     * @param v2        The second input vector.
     * @return          The result.
     */
    public static Vector3 multiply(Vector3 v1, Vector3 v2) {
        return new Vector3( v1.a1 * v2.a1, v1.a2 * v2.a2,  v1.a3 * v2.a3 );
    }
    /**
     * Computes the dot product between this vector and the input vector.
     * @param v     The input vector.
     * @return      The dot product.
     */
    public double dot(Vector3 v) {
        return v.a1*a1 + v.a2*a2 + v.a3*a3;
    }

    public static double dot(Vector3 v1, Vector3 v2) {
        return v1.a1*v2.a1 + v1.a2*v2.a2 + v1.a3*v2.a3;
    }
    /**
     * Add a scalar value to each component of this vector.
     * @param val       The scalar value.
     */
    public void add(double val) {
        set1( a1 + val );
        set2( a2 + val );
        set3( a3 + val );
    }
    /**
     * Add a vector to this vector.  The result is stored in this vector.
     * @param v     The input vector to be added to this vector.
     */
    public void add(Vector3 v) {
        set1( a1 + v.get1() );
        set2( a2 + v.get2() );
        set3( a3 + v.get3() );
    }
    /**
     * Add a scalar to the input vector and return the result.
     * @param v1        The input vector.
     * @param val       The scalar to be added.
     * @return          The Vector3 object with the result.
     */
    public static Vector3 add(Vector3 v1, double val) {
        return new Vector3( v1.a1 + val, v1.a2 + val,  v1.a3 + val );
    }
    /**
     * Add two input vectors and return the result.
     * @param v1        The first vector.
     * @param v2        The second vector.
     * @return          The resulting vector.
     */
    public static Vector3 add(Vector3 v1, Vector3 v2) {
        return new Vector3( v1.a1 + v2.a1, v1.a2 + v2.a2,  v1.a3 + v2.a3 );
    }
    /**
     * Subtract the input scalar from each vector component.
     * @param val       The scalar value.
     * @return          This vector with the result.
     */
    public Vector3 subtract(double val) {
        set1( a1 - val );
        set2( a2 - val );
        set3( a3 - val );
        return this;
    }
    /**
     * Subtract an input vector from this vector.  The result is stored in this vector.
     * @param v     The input vector
     */
    public void subtract(Vector3 v) {
        set1( a1 - v.get1() );
        set2( a2 - v.get2() );
        set3( a3 - v.get3() );
    }
    /**
     * Element-wise subtraction of the scalar value from the vector.
     * @param v1        The vector.
     * @param val       The scalar value.
     * @return          A new Vector3 object with the result.
     */
    public static Vector3 subtract(Vector3 v1, double val) {
        return new Vector3( v1.a1 - val, v1.a2 - val,  v1.a3 - val );
    }
    /**
     * Subtract the two input vectors.
     * @param v1        The first vector.
     * @param v2        The second vector.
     * @return          The resulting Vector3 object.
     */
    public static Vector3 subtract(Vector3 v1, Vector3 v2) {
        return new Vector3( v1.a1 - v2.a1, v1.a2 - v2.a2,  v1.a3 - v2.a3 );
    }
    
//    public void multiply(Matrix33 mat) {
//        a1 = a1 * mat.a11 + a1 * mat.a22 + a1 * mat.a13;
//        a2 = a1 * mat.a21 + a2 * mat.a22 + a3 * mat.a23;
//        a3 = a1 * mat.a31 + a2 * mat.a32 + a3 * mat.a33;
//    }
    
    /** performs operation v times mat */
//    public static Vector3 multiply(Vector3 v, Matrix33 mat) {
//        return new Vector3(
//                v.get1() * mat.a11 + v.get2() * mat.a12 + v.get3() * mat.a13,
//                v.get1() * mat.a21 + v.get2() * mat.a22 + v.get3() * mat.a23,
//                v.get1() * mat.a31 + v.get2() * mat.a32 + v.get3() * mat.a33
//                );
//    }
    /**
     * Compute the magnitude of the vector.
     * @return      The vector magnitude.
     */
    public double magnitude() {
        return Math.sqrt( a1 * a1 + a2 * a2 + a3 * a3 );
    }
    /**
     * Compute the magnitude of a vector given its three components.
     * @param a1        The first component.
     * @param a2        The second component.
     * @param a3        The third component.
     * @return          The vector magnitude/
     */
    public static double magnitude(double a1, double a2, double a3) {
        return Math.sqrt( a1 * a1 + a2 * a2 + a3 * a3 );
    }
    /**
     * Creates a string representation for the vector.
     * @return      The string containing the vector elements.
     */
    public String toString() {
        return new String( "[ " + a1 + ", " + a2 + ", " + a3 + "]");
    }
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */