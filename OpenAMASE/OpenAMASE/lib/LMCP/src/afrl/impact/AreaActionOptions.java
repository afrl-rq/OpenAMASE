// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.impact;


import avtas.lmcp.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**  Fusion Area Actions */
public enum AreaActionOptions {

    /**  Object of interest created  */
    Created(0),
    /**  Object of interest destroyed  */
    Destroyed(1),
    /**  Object of interest modified  */
    Modified(2);


    private final int val;

    /** creates a new enum of the specified value */
    AreaActionOptions(int val) {
        this.val = val;
    }

    /** returns the set value for this enum */
    public int getValue() {
        return val;
    }

    /** packs this enum into the LMCP buffer */
    public void pack(OutputStream out) throws IOException { LMCPUtil.putInt32(out, getValue()); }

    /** creates an enum for the value in the LMCP buffer */
    public static AreaActionOptions unpack(InputStream in) throws IOException{
        return getEnum( LMCPUtil.getInt32(in) );
    }

    /** returns a new instance of this enum that matches the passed value (null if value is not known) */
    public static AreaActionOptions getEnum(int val) {
        switch(val) {
            case 0 : return Created;
            case 1 : return Destroyed;
            case 2 : return Modified;
            default: return Created;

        }
    }
}
