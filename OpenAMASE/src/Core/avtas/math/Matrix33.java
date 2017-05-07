// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.math;

import static java.lang.Math.*;

/**
 * A class that defines operations for a 3 x 3 matrix.
 * @author AFRL/RQQD
 */
public class Matrix33 {
    
    //protected double a11, a12, a13, a21, a22, a23, a31, a32, a33;
    protected double[] a = new double[9];
    
    /** Creates a new instance of Matrix33.  If the array provided is not of length
     *  9, the array for the matrix is not initialized.
     *  @param  a   The array that defines the values stored in the matrix.
     */
    public Matrix33(double[] a) {
        if (a.length != 9) return;
        this.a[0] = a[0];
        this.a[1] = a[1];
        this.a[2] = a[2];
        this.a[3] = a[3];
        this.a[4] = a[4];
        this.a[5] = a[5];
        this.a[6] = a[6];
        this.a[7] = a[7];
        this.a[8] = a[8];
    }
    
    public Matrix33() {
    }

    /**
     * Creates a clone of this matrix and returns it.
     *
     * @return      A clone of this matrix.
     */
    public Matrix33 clone() {
        Matrix33 mat = new Matrix33( a );
        return mat;
    }
    /**
     * Set the element in the a11 position of the matrix.
     *
     * @param val       The desired value.
     */
    public void set11(double val) { a[0] = val; }
    /**
     * Set the element in the a12 position of the matrix.
     *
     * @param val       The desired value.
     */
    public void set12(double val) { a[1] = val; }
    /** 
     * Set the element in the a13 position of the matrix.
     * 
     * @param val       The desired value.
     */
    public void set13(double val) { a[2] = val; }
    /**
     * Set the element in the a21 position of the matrix.
     *
     * @param val       The desired value.
     */
    public void set21(double val) { a[3] = val; }
    /**
     * Set the element in the a22 position of the matrix.
     *
     * @param val       The desired value.
     */
    public void set22(double val) { a[4] = val; }
    /**
     * Set the element in the a23 position of the matrix.
     *
     * @param val       The desired value.
     */
    public void set23(double val) { a[5] = val; }
    /**
     * Set the element in the a31 position of the matrix.
     *
     * @param val       The desired value.
     */
    public void set31(double val) { a[6] = val; }
    /**
     * Set the element in the a32 position of the matrix.
     *
     * @param val       The desired value.
     */
    public void set32(double val) { a[7] = val; }
    /**
     * Set the element in the a33 position of the matrix.
     *
     * @param val       The desired value.
     */
    public void set33(double val) { a[8] = val; }
    /**
     * Get the a11 element of the matrix.
     *
     * @return          The a11 element
     */
    public double get11() { return a[0]; }
    /**
     * Get the a12 element of the matrix.
     *
     * @return          The a12 element
     */
    public double get12() { return a[1]; }
    /**
     * Get the a13 element of the matrix.
     *
     * @return          The a13 element
     */
    public double get13() { return a[2]; }
    /**
     * Get the a21 element of the matrix.
     *
     * @return          The a21 element
     */
    public double get21() { return a[3]; }
    /**
     * Get the a22 element of the matrix.
     *
     * @return          The a22 element
     */
    public double get22() { return a[4]; }
    /**
     * Get the a23 element of the matrix.
     *
     * @return          The a23 element
     */
    public double get23() { return a[5]; }
    /**
     * Get the a31 element of the matrix.
     *
     * @return          The a31 element
     */
    public double get31() { return a[6]; }
    /**
     * Get the a32 element of the matrix.
     *
     * @return          The a32 element
     */
    public double get32() { return a[7]; }
    /**
     * Get the a33 element of the matrix.
     *
     * @return          The a33 element
     */
    public double get33() { return a[8]; }

    /**
     * Set an element in the matrix based on the row and column indices provided.
     * If either the row or column index is less than 1 or greater than 3, the method
     * returns.
     *
     * @param row   The matrix row.
     * @param col   The matrix column.
     * @param val   The desired value.
     */
    public void set(int row, int col, double val) {
        if (row < 1 || row > 3) return;
        if (col < 1 || col > 3) return;
        
        a[ (row - 1) * 3 + (col - 1) ] = val;
    }

    /**
     * Set an entire row in the matrix using the three values provided.  If either
     * the row or the column index is less than 1 or greater than 3, the method
     * returns.
     * @param row       The matrix row.
     * @param val1      The first value.
     * @param val2      The second value.
     * @param val3      The third value.
     */
    public void setRow(int row, double val1, double val2, double val3) {
        if (row < 1 || row > 3) return;
        row = (row - 1) * 3;
        a[row] = val1;
        a[row+1] = val2;
        a[row+2] = val3;
    }

    /**
     * Get the matrix element located at the row and column indices provided.
     *
     * @param row   The matrix row index.
     * @param col   The matrix column index.
     * @return      The value of the element at this location.
     */
    public double get(int row, int col) {
        return a[ (row-1) * 3 + col-1];
    }
    
    /**
     * Multiply this matrix, element-wise, by a scalar value.
     * @param val  The multiplication factor.
     */
    public void multiply(double val) {
        a[0] *= val;
        a[1] *= val;
        a[2] *= val;
        a[3] *= val;
        a[4] *= val;
        a[5] *= val;
        a[6] *= val;
        a[7] *= val;
        a[8] *= val;
    }

    /**
     * Multiply a given matrix by a given multiplicative factor.
     * @param mat       The input matrix.
     * @param val       The input scalar value.
     * @return          The matrix, multiplied by the scalar.
     */
    public static Matrix33 multiply(Matrix33 mat, double val) {
        mat.a[0] *= val;
        mat.a[1] *= val;
        mat.a[2] *= val;
        mat.a[3] *= val;
        mat.a[4] *= val;
        mat.a[5] *= val;
        mat.a[6] *= val;
        mat.a[7] *= val;
        mat.a[8] *= val;
        
        return mat;
    }

    /**
     * Multiply the given 3 x 3 matrix by the given 3 x 1 vector.
     * @param mat       The 3 x 3 matrix.
     * @param v         The 3 x 1 vector.
     * @return          The resulting 1 x 3 vector.
     */
    public static Vector3 multiply( Matrix33 mat, Vector3 v) {
        
        return new Vector3( mat.a[0] * v.get1() + mat.a[1]  * v.get2() + mat.a[2] * v.get3(),
                mat.a[3] * v.get1() + mat.a[4] * v.get2() + mat.a[5] * v.get3(),
                mat.a[6] * v.get1() + mat.a[7] * v.get2() + mat.a[8] * v.get3());
    }
    /**
     * Transposes this matrix by cloning it and re-assigning the matrix elements
     * using the cloned matrix.
     */
    public void transpose() {
        Matrix33 tmp = clone();
        a[1] = tmp.get21();
        a[2] = tmp.get31();
        a[3] = tmp.get12();
        a[5] = tmp.get32();
        a[6] = tmp.get13();
        a[7] = tmp.get23();
    }
    /**
     * Transposes the given 3 x 3 matrix and returns the transposed matrix.
     * @param mat       The input matrix.
     * @return          The transposed matrix.
     */
    public static Matrix33 transpose( Matrix33 mat ) {
        Matrix33 tmp = mat.clone();
        
        tmp.a[1] = mat.a[3];
        tmp.a[2] = mat.a[6];
        tmp.a[3] = mat.a[1];
        tmp.a[5] = mat.a[7];
        tmp.a[6] = mat.a[2];
        tmp.a[7] = mat.a[5];
        
        return tmp;
    }
    /**
     * Computes the inverse of the given 3 x 3 matrix.
     * @param mat   The input matrix.
     * @return      A new matrix equal to the inverse of the original matrix.
     */
    public static Matrix33 inverse( Matrix33 mat) {
        
        double a11 = mat.a[4] * mat.a[8] - mat.a[5] * mat.a[7];
        double a12 = mat.a[2] * mat.a[7] - mat.a[1] * mat.a[8];
        double a13 = mat.a[1] * mat.a[5] - mat.a[2] * mat.a[4];
        double a21 = mat.a[5] * mat.a[6] - mat.a[3] * mat.a[8];
        double a22 = mat.a[0] * mat.a[8] - mat.a[2] * mat.a[6];
        double a23 = mat.a[2] * mat.a[3] - mat.a[0] * mat.a[5];
        double a31 = mat.a[3] * mat.a[7] - mat.a[4] * mat.a[6];
        double a32 = mat.a[1] * mat.a[6] - mat.a[0] * mat.a[7];
        double a33 = mat.a[0] * mat.a[4] - mat.a[1] * mat.a[3];
        
        double det = mat.a[0] * mat.a[4] * mat.a[8] - mat.a[0] * mat.a[5] * mat.a[7] 
                - mat.a[1] * mat.a[3] * mat.a[8] + mat.a[1] * mat.a[5] * mat.a[6]
                + mat.a[2] * mat.a[3] * mat.a[7] - mat.a[2] * mat.a[4] * mat.a[6];
        
        if (det == 0) System.out.println("Matrix error.  Det = 0");
        
        Matrix33 ret = new Matrix33( new double[] {a11, a12, a13, a21, a22, a23, a31, a32, a33} );
        ret.multiply( 1. / det);
        
        return ret;
    }

    /**
     * Converts the matrix elements to a string using square bracket notation.
     * @return      The string representing the matrix.
     */
    public String toString() {
        return "[ " + a[0] + " " + a[1] + " " + a[2] + " ]\n[ " + a[3] + " " + a[4] + " " + a[5] 
               + " ]\n[ " + a[6] + " " + a[7] + " " + a[8] + " ]"; 
    }
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */