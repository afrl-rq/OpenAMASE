// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.entity;

import avtas.data.Property;
import avtas.data.PropertyMap;


public final class AeroProperties {
    protected final PropertyMap map;

    public AeroProperties(PropertyMap map) {
        this.map = map;
        rho = map.get("environ/rho");
        aero_x = map.get("aero/force/x");
        aero_y = map.get("aero/force/y");
        aero_z = map.get("aero/force/z");
        thrust_x = map.get("power/thrust/x");
        thrust_y = map.get("power/thrust/y");
        thrust_z = map.get("power/thrust/z");
        alpha = map.get("aero/alpha");
        beta = map.get("aero/beta");
        alpha_dot = map.get("aero/alpha_dot");
        beta_dot = map.get("aero/beta_dot");
        vinf = map.get("aero/vinf");
        mach = map.get("aero/mach");
        throttle = map.get("power/throttle");
        elevator = map.get("aircraft/controls/elevator");
        aileron = map.get("aircraft/controls/aileron");
        rudder = map.get("aircraft/controls/rudder");
    }
    /**
     * atmospheric density (kg/m3)
     */
    public final Property rho;
    /**
     * aerodynamic force on body x-axis
     */
    public final Property aero_x;
    /**
     * aerodynamic force on body y-axis
     */
    public final Property aero_y;
    /**
     * aerodynamic force on body z-axis
     */
    public final Property aero_z;
    /**
     * thrust force on body x-axis
     */
    public final Property thrust_x;
    /**
     * thrust force on body y-axis
     */
    public final Property thrust_y;
    /**
     * thrust force on body z-axis
     */
    public final Property thrust_z;
    /**
     * aerodynamic angle of attack (rad)
     */
    public final Property alpha;
    /**
     * aerodynamic angle of sideslip (rad)
     */
    public final Property beta;
    /**
     * change in aerodynamic angle of attack (rad/sec)
     */
    public final Property alpha_dot;
    /**
     * change in aerodynamic angle of attack (rad/sec)
     */
    public final Property beta_dot;
    /**
     * aerodynamic resultant velocity (mps)
     */
    public final Property vinf;
    /**
     * mach number
     */
    public final Property mach;
    /**
     * current throttle setting (normalized)
     */
    public final Property throttle;
    /**
     * elevator deflection (normalized)
     */
    public final Property elevator;
    /**
     * aileron deflection (normalized)
     */
    public final Property aileron;
    /**
     * rudder deflection (normalized)
     */
    public final Property rudder;
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */