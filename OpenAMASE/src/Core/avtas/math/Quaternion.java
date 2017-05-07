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
import javax.swing.JFrame;

/**
 *  Defines a quaternion and associated operations.
 * @author AFRL/RQQD
 */
public class Quaternion {

    private double eo;
    private double ex;
    private double ey;
    private double ez = 0.0;
    private double rnorm = 1.;

    /** Creates a new instance of Quaternion.
     * @param   psi     The heading angle.
     * @param   theta   The pitch angle.
     * @param   phi     The roll angle.
     */
    public Quaternion(double psi, double theta, double phi) {
        initialize(psi, theta, phi);
    }
    /**
     * Creates a new instance of Quaternion.  Inputs are the rotation angle, e0,
     * and the three vector elements describing the rotation axis (ex, ey, ez).
     * @param e0    The rotation angle.
     * @param ex    The rotation axis x component.
     * @param ey    The rotation axis y component.
     * @param ez    The rotation axis z component.
     */
    public Quaternion(double e0, double ex, double ey, double ez) {
        this.eo = e0;
        this.ex = ex;
        this.ey = ey;
        this.ez = ez;
    }

    /**
     * Creates a new instance of Quaternion from a 3-Vector containing the Euler angles.
     *
     * @param euler     The vector containing the euler angles.
     */
    public Quaternion(Vector3 euler) {
        initialize(euler.get1(), euler.get2(), euler.get3());
    }

    /**
     * Creates a clone of this Quaternion and returns it.
     * @return      The clone of this Quaternion.
     */
    public Quaternion clone() {
        return new Quaternion(eo, ex, ey, ez);
    }

    /**
     * Get the psi Euler angle from the Quaternion components.
     *
     * @return      The heading angle.
     */
    public double getPsi() {
        double m11 = eo * eo + ex * ex - ey * ey - ez * ez;
        double m12 = 2.0 * (eo * ez + ex * ey);
//        if (m11 == 0.) {
//            return 0.5 * Math.PI;
//        }
//        else
        return atan2(m12, m11);
    //return  atan2( 2. * (eo * ez + ex * ey), eo * eo + ex * ex - ey * ey - ez * ez );
    }

    /**
     * Get the theta Euler angle from the Quaternion components.
     * @return      The pitch angle.
     */
    public double getTheta() {
        return asin(2. * (eo * ey - ex * ez));
    }

    /**
     * Get the phi Euler angle from the Quaternion components.
     * @return      The roll angle.
     */
    public double getPhi() {
        return atan2(2. * (eo * ex + ey * ez), eo * eo + ez * ez - ex * ex - ey * ey);
    }

    /** Sets the euler angles in the passed Euler object.  Uses the set methods
     * for each angle.
     */
    public void getEulers(Euler toFill) {
        normalize();
        toFill.setPsi(getPsi());
        toFill.setTheta(getTheta());
        toFill.setPhi(getPhi());
//        
    // First normalize the 4-vector
//        double norm = magnitude();
//        if (norm == 0.0)
//            return;
//        
//        double rnorm = 1.0/norm;
//        double q1 = rnorm*eo;
//        double q2 = rnorm*ex;
//        double q3 = rnorm*ey;
//        double q4 = rnorm*ez;
//        
//        // Now compute the transformation matrix.
//        double q1q1 = q1*q1;
//        double q2q2 = q2*q2;
//        double q3q3 = q3*q3;
//        double q4q4 = q4*q4;
//        double q1q2 = q1*q2;
//        double q1q3 = q1*q3;
//        double q1q4 = q1*q4;
//        double q2q3 = q2*q3;
//        double q2q4 = q2*q4;
//        double q3q4 = q3*q4;
//        
//        double m11 = q1q1 + q2q2 - q3q3 - q4q4;
//        double m12 = 2.0*(q2q3 + q1q4);
//        double m13 = 2.0*(q2q4 - q1q3);
//        double m21 = 2.0*(q2q3 - q1q4);
//        double m22 = q1q1 - q2q2 + q3q3 - q4q4;
//        double m23 = 2.0*(q3q4 + q1q2);
//        double m31 = 2.0*(q2q4 + q1q3);
//        double m32 = 2.0*(q3q4 - q1q2);
//        double m33 = q1q1 - q2q2 - q3q3 + q4q4;
//        
//        // Compute the Euler-angles
//        if (m33 == 0.0)
//            toFill.setPhi(0.5*Math.PI);
//        else
//            toFill.setPhi(atan2(m23, m33));
//        
//        if (m13 < -1.0)
//            toFill.setTheta(0.5*Math.PI);
//        else if (1.0 < m13)
//            toFill.setTheta(-0.5*Math.PI);
//        else
//            toFill.setTheta(asin(-m13));
//        
//        if (m11 == 0.0)
//            toFill.setPsi( 0.5*Math.PI );
//        else {
//            //double psi = atan2(m12, m11);
//            //if (psi < 0.0)
//            //    psi += 2*Math.PI;
//            toFill.setPsi( atan2(m12, m11) );
//        }
    }

    /**
     * Gets deltas in the x, y and z directions given the body axis velocities u, v and w.
     * @param u     Velocity along the body x axis.
     * @param v     Velocity along the body y axis.
     * @param w     Velocity along the body z axis.
     * @return      An array containing the x, y and z deltas
     */
    public double[] getDxDyDz(double u, double v, double w) {
        double a11 = ex * ex + eo * eo - ey * ey - ez * ez;
        double a12 = 2.0 * (ex * ey - ez * eo);
        double a13 = 2.0 * (ex * ez + ey * eo);
        double a21 = 2.0 * (ex * ey + ez * eo);
        double a22 = ey * ey + eo * eo - ex * ex - ez * ez;
        double a23 = 2.0 * (ey * ez - ex * eo);
        double a31 = 2.0 * (ex * ez - ey * eo);
        double a32 = 2.0 * (ey * ez + ex * eo);
        double a33 = ez * ez + eo * eo - ex * ex - ey * ey;

        return new double[]{
            a11 * u + a12 * v + a13 * w,
            a21 * u + a22 * v + a23 * w,
            a31 * u + a32 * v + a33 * w
        };

    }
    /**
     * Gets the body axis velocities given deltas in x, y and z.
     * @param dx    The change in the x direction.
     * @param dy    The change in the y direction.
     * @param dz    The change in the z direction.
     * @return      An array containing the body axis velocites u, v and w.
     */
    public double[] getUVW(double dx, double dy, double dz) {
        double a11 = ex * ex + eo * eo - ey * ey - ez * ez;
        double a21 = 2.0 * (ex * ey - ez * eo);
        double a31 = 2.0 * (ex * ez + ey * eo);
        double a12 = 2.0 * (ex * ey + ez * eo);
        double a22 = ey * ey + eo * eo - ex * ex - ez * ez;
        double a32 = 2.0 * (ey * ez - ex * eo);
        double a13 = 2.0 * (ex * ez - ey * eo);
        double a23 = 2.0 * (ey * ez + ex * eo);
        double a33 = ez * ez + eo * eo - ex * ex - ey * ey;

        return new double[]{
            a11 * dx + a12 * dy + a13 * dz,
            a21 * dx + a22 * dy + a23 * dz,
            a31 * dx + a32 * dy + a33 * dz
        };
    }

    /** Fills a quaternion with this quaternion's derivative.  The quaternion
     * derivative is computed using the body axis roll rates p, q and r.
     * @param toFill        The quaternion to fill.
     * @param p             The angular rate about the body x axis.
     * @param q             The angular rate about the body y axis.
     * @param r             The angular rate about the bosy z axis.
     */
    public void getQdot(Quaternion toFill, double p, double q, double r) {

        double k = 1;
        double lambda = k * (1 - (eo * eo + ex * ex + ey * ey + ez * ez));

        toFill.eo = -0.5 * (ex * p + ey * q + ez * r) + lambda * eo;
        toFill.ex = 0.5 * (eo * p + ey * r - ez * q) + lambda * ex;
        toFill.ey = 0.5 * (eo * q + ez * p - ex * r) + lambda * ey;
        toFill.ez = 0.5 * (eo * r + ex * q - ey * p) + lambda * ez;
    }

    /**
     * Initialize this Quaternion given the Euler angles.
     * @param psi
     * @param theta
     * @param phi
     */
    public void initialize(double psi, double theta, double phi) {
        double c_psi = cos(psi / 2.0);
        double s_psi = sin(psi / 2.0);
        double c_theta = cos(theta / 2.0);
        double s_theta = sin(theta / 2.0);
        double c_phi = cos(phi / 2.0);
        double s_phi = sin(phi / 2.0);

        eo = c_phi * c_theta * c_psi + s_phi * s_theta * s_psi;
        ex = s_phi * c_theta * c_psi - c_phi * s_theta * s_psi;
        ey = c_phi * s_theta * c_psi + s_phi * c_theta * s_psi;
        ez = c_phi * c_theta * s_psi - s_phi * s_theta * c_psi;

    }
    /**
     * Normalizes this Quaternion.
     */
    public void normalize() {
        rnorm = magnitude();
        if (rnorm == 0) {
            return;
        }
        rnorm = 1. / rnorm;

        eo *= rnorm;
        ex *= rnorm;
        ey *= rnorm;
        ez *= rnorm;
    }
    /**
     * Computes the magnitude of this Quaternion.
     * @return      This quaternion's magnitude.
     */
    public double magnitude() {
        return sqrt(eo * eo + ex * ex + ey * ey + ez * ez);
    }

    /**
     * Adds this Quaternion to the input Quaternion.
     * @param q     The input quaternion.
     * @return      This Quaternion.
     */
    public Quaternion add(Quaternion q) {
        eo += q.getEo();
        ex += q.getEx();
        ey += q.getEy();
        ez += q.getEz();

        return this;
    }
    /**
     * Gets the conjugate of this Quaternion
     * @return      A new Quaternion with the conjugate.
     */
    public Quaternion getConjugate() {
        return new Quaternion(eo, -ex, -ey, -ez);
    }

    /**
     * Multiplies this quaternion with the input quaternion.
     * @param q     The input quaternion
     * @return      This quaternion.
     */
    public Quaternion multiply(Quaternion q) {
        double qeo = q.getEo();
        double qex = q.getEx();
        double qey = q.getEy();
        double qez = q.getEz();

        double tmp_eo = eo * qeo - ex * qex - ey * qey - ez * qez;
        double tmp_ex = eo * qex + ex * qeo + ey * qez - ez * qey;
        double tmp_ey = eo * qey - ex * qez + ey * qeo + ez * qex;
        double tmp_ez = eo * qez + ex * qey - ey * qex + ez * qeo;

        this.eo = tmp_eo;
        this.ex = tmp_ex;
        this.ey = tmp_ey;
        this.ez = tmp_ez;

        return this;
    }

    /**
     * Multiply the two input quaternions together and return a new Quaternion with the result.
     * @param q1    The first input quaternion.
     * @param q2    The second input quaternion.
     * @return      The resulting quaternion.
     */
    public static Quaternion multiply(Quaternion q1, Quaternion q2) {

        double tmp_eo = q1.eo * q2.eo - q1.ex * q2.ex - q1.ey * q2.ey - q1.ez * q2.ez;
        double tmp_ex = q1.eo * q2.ex + q1.ex * q2.eo + q1.ey * q2.ez - q1.ez * q2.ey;
        double tmp_ey = q1.eo * q2.ey - q1.ex * q2.ez + q1.ey * q2.eo + q1.ez * q2.ex;
        double tmp_ez = q1.eo * q2.ez + q1.ex * q2.ey - q1.ey * q2.ex + q1.ez * q2.eo;

        return new Quaternion(tmp_eo, tmp_ex, tmp_ey, tmp_ez);
    }

    /**
     * Element-wise multiplication of this quaternion by a scalar.
     * @param d     The multiplication factor.
     * @return      This quaternion.
     */
    public Quaternion multiply(double d) {
        eo *= d;
        ex *= d;
        ey *= d;
        ez *= d;
        return this;
    }

    /**
     * Converts this quaternion to a string representation.
     * @return      A string representing this quaternion.
     */
    public String toString() {
        return "Quaternion: [ " + eo + " " + ex + " " + ey + " " + ez + " ]";
    }

    /**
     * Gets the eo element of this quaternion.
     * @return      The eo element.
     */
    public double getEo() {
        return eo;
    }

    /**
     * Gets the ex element of this quaternion.
     * @return      The ex element.
     */
    public double getEx() {
        return ex;
    }

    /**
     * Gets the ey element of this quaternion.
     * @return      The ey element.
     */
    public double getEy() {
        return ey;
    }

    /**
     * Gets the ez element of this quaternion.
     * @return      The ez element.
     */
    public double getEz() {
        return ez;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */