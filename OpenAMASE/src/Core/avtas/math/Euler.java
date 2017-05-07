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
import java.nio.ByteBuffer;
import static avtas.math.Quaternion.*;

/**
 * A class that implements methods for Euler angle conversions.
 *
 * @author AFRL/RQQD
 */
public class Euler extends Vector3{
    
    
    //protected Float64 a1 = new Float64();
    //protected Float64 a2 = new Float64();
    //protected Float64 a3 = new Float64();

    /**
     * Creates a new instance of Euler from the given psi, theta and phi.
     *
     * @param   psi     The aircraft heading angle (rad)
     * @param   theta   The aircraft pitch angle (rad)
     * @param   phi     The aircraft roll angle (rad)
     */
    public Euler(double psi, double theta, double phi) {
        super();
        a1 = psi;
        a2 = theta;
        a3 = phi;
    }

    /**
     * Creates a new instance of Euler with psi, theta and phi all initialized to zero.
     */
    public Euler() {
        this(0, 0, 0);
    }

    /**
     * Get a change in inertial x, y and z from velocities in the body axis.
     * The conversion matrix comes from Nelson.
     * @param   uvw     Vector of body velocities.
     * @return          Vector of deltas in x, y and z.
     */
    public Vector3 getDxDyDz( Vector3 uvw) {
        double a11 = cos(a2)*cos(a1);
        double a12 = sin(a3)*sin(a2)*cos(a1) - cos(a3)*sin(a1);
        double a13 = cos(a3)*sin(a2)*cos(a1) + sin(a3)*sin(a1);
        double a21 = cos(a2)*sin(a1);
        double a22 = sin(a3)*sin(a2)*sin(a1) + cos(a3)*cos(a1);
        double a23 = cos(a3)*sin(a2)*sin(a1) - sin(a3)*cos(a1);
        double a31 = -sin(a2);
        double a32 = sin(a3)*cos(a2);
        double a33 = cos(a3)*cos(a2);
        return new Vector3 ( a11 * uvw.get1() + a12 * uvw.get2() + a13 * uvw.get3(), 
                          a21 * uvw.get1() + a22 * uvw.get2() + a23 * uvw.get3(),
                          a31 * uvw.get1() + a32 * uvw.get2() + a33 * uvw.get3() );

    }

    /**
     * Get body velocities from changes in inertial x, y and z.
     * 
     * @param toFill        Vector to fill with the body velocities.
     * @param dxdydz        Vector of changes in x, y and z.
     */
    public void getUVW( Vector3 toFill, Vector3 dxdydz) {
        double a11 = cos(a2)*cos(a1);
        double a21 = sin(a3)*sin(a2)*cos(a1) - cos(a3)*sin(a1);
        double a31 = cos(a3)*sin(a2)*cos(a1) + sin(a3)*sin(a1);
        double a12 = cos(a2)*sin(a1);
        double a22 = sin(a3)*sin(a2)*sin(a1) + cos(a3)*cos(a1);
        double a32 = cos(a3)*sin(a2)*sin(a1) - sin(a3)*cos(a1);
        double a13 = -sin(a2);
        double a23 = sin(a3)*cos(a2);
        double a33 = cos(a3)*cos(a2);
        
        //System.out.println("[ " + a11 + " " + a12 + " " + a13);
        //System.out.println(a21 + " " + a22 + " " + a23);
        //System.out.println(a31 + " " + a32 + " " + a33);
        
        toFill.set1 ( a11 * dxdydz.get1() + a12 * dxdydz.get2() + a13 * dxdydz.get3()); 
        toFill.set2 ( a21 * dxdydz.get1() + a22 * dxdydz.get2() + a23 * dxdydz.get3());
        toFill.set3 ( a31 * dxdydz.get1() + a32 * dxdydz.get2() + a33 * dxdydz.get3());

    }
    
    /**
     * Gets the change in Euler angles from the given roll, pitch and yaw rates
     * in the body axis.  Computes the matrix:
     * [ 1      sin(a3)tan(a2)      cos(a3)tan(a2)  ]
     * [ 0      cos(a3)                -sin(a3)           ]
     * [ 0      -sin(a3)/cos(a2)    cos(a3)/cos(a2) ]
     * @param p roll rate (rad/sec)
     * @param q pitch rate (rad/sec)
     * @param r yaw rate (rad/sec)
     */
    public Euler getDeltaEuler( double p, double q, double r ) {
        return new Euler (
            sin(a3) / cos(a2) * q + cos(a3) / cos(a2) * r,
            cos(a3) * q - sin(a3) * r,
            p + sin(a3) * tan(a2) * q + cos(a3) * tan(a2) * r );
    }
    
    /**
     * Returns the pqr vector for a given vector of delta [psi, theta, phi]
     *
     * @param eulerDot   Vector of Euler angle rates.
     * @return           Vector of body axis angular rates.
     */
    public Vector3 getPQR( Vector3 eulerDot) {
        return new Vector3( 
                eulerDot.get3() - eulerDot.get1() * sin(a2),
                eulerDot.get2() * cos(a3) + eulerDot.get1() * cos(a2) * sin(a3),
                -eulerDot.get2() * sin(a3) + eulerDot.get1() * cos(a2) * cos(a3));
    } 

    /**
     * Outputs a string with Euler angle values.
     *
     * @return      A string with the Euler angles.
     */
    public String toString() {
        return "Euler: psi = " + getPsi() + " theta = " + getTheta() + " phi = " + getPhi() ; 
    }

    /**
     * Gets the heading angle (rad).
     *
     * @return      The aircraft heading.
     */
    public double getPsi() {
        return a1;
    }

    /**
     * Sets the heading angle (rad).
     *
     * @param psi   The aircraft heading angle.
     */
    public void setPsi(double psi) {
        a1 = psi;
    }

    /**
     * Gets the aircraft pitch angle.
     *
     * @return  The aircraft pitch angle.
     */
    public double getTheta() {
        return a2;
    }

    /**
     * Sets the aircraft pitch angle (rad).
     *
     * @param theta     The aircraft pitch angle.
     */
    public void setTheta(double theta) {
        a2 = theta;
    }

    /**
     * Gets the aircraft roll angle (rad).
     *
     * @return      The aircraft roll angle.
     */
    public double getPhi() {
        return a3;
    }

    /**
     * Sets the aircraft roll angle (rad).
     * @param phi       The aircraft roll angle.
     */
    public void setPhi(double phi) {
        a3 = phi;
    }
    
   
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */