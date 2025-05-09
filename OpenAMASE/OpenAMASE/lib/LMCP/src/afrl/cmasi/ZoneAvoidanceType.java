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

/**  Describes types of keep-out zones */
public enum ZoneAvoidanceType {

    /**  zone corresponds to a physical boundary (e.g. terrain, buildings)  */
    Physical(1),
    /**  zone corresponds to a regulatory boundary (e.g. flight control corridor)  */
    Regulatory(2),
    /**  zone corresponds to an area that is sensitive to acoustic intrusion  */
    Acoustic(3),
    /**  zone contains a threat */
    Threat(4),
    /**  zone defines an area that is sensitive to visual detection.  The threshold of visual detection                is not defined by this standard. */
    Visual(5);


    private final int val;

    /** creates a new enum of the specified value */
    ZoneAvoidanceType(int val) {
        this.val = val;
    }

    /** returns the set value for this enum */
    public int getValue() {
        return val;
    }

    /** packs this enum into the LMCP buffer */
    public void pack(OutputStream out) throws IOException { LMCPUtil.putInt32(out, getValue()); }

    /** creates an enum for the value in the LMCP buffer */
    public static ZoneAvoidanceType unpack(InputStream in) throws IOException{
        return getEnum( LMCPUtil.getInt32(in) );
    }

    /** returns a new instance of this enum that matches the passed value (null if value is not known) */
    public static ZoneAvoidanceType getEnum(int val) {
        switch(val) {
            case 1 : return Physical;
            case 2 : return Regulatory;
            case 3 : return Acoustic;
            case 4 : return Threat;
            case 5 : return Visual;
            default: return Physical;

        }
    }
}
