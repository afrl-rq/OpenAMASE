// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.cmasi;


import avtas.lmcp.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**  Describes available pointing modes for a gimbal */
public enum GimbalPointingMode {

    /**  Unknown pointing mode or unavailable  */
    Unknown(0),
    /**  angles are commanded relative to the vehicle  */
    AirVehicleRelativeAngle(1),
    /**  angle rates are commanded relative to the vehicle  */
    AirVehicleRelativeSlewRate(2),
    /**  gimbal aimed at an inertial location  */
    LatLonSlaved(3),
    /**  gimbal rates are calculated relative to the inertial coordinate system  */
    InertialRelativeSlewRate(4),
    /**  gimbal continuously scans in a pre-determined pattern  */
    Scan(5),
    /**  indicates that the gimbal is stowed  */
    Stowed(6);


    private final int val;

    /** creates a new enum of the specified value */
    GimbalPointingMode(int val) {
        this.val = val;
    }

    /** returns the set value for this enum */
    public int getValue() {
        return val;
    }

    /** packs this enum into the LMCP buffer */
    public void pack(OutputStream out) throws IOException { LMCPUtil.putInt32(out, getValue()); }

    /** creates an enum for the value in the LMCP buffer */
    public static GimbalPointingMode unpack(InputStream in) throws IOException{
        return getEnum( LMCPUtil.getInt32(in) );
    }

    /** returns a new instance of this enum that matches the passed value (null if value is not known) */
    public static GimbalPointingMode getEnum(int val) {
        switch(val) {
            case 0 : return Unknown;
            case 1 : return AirVehicleRelativeAngle;
            case 2 : return AirVehicleRelativeSlewRate;
            case 3 : return LatLonSlaved;
            case 4 : return InertialRelativeSlewRate;
            case 5 : return Scan;
            case 6 : return Stowed;
            default: return Unknown;

        }
    }
}
