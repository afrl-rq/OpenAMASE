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

/**  Designates how an entity should treat a path following command.  */
public enum TravelMode {

    /**  A path is traversed from beginning to end, then the entity stops  */
    SinglePass(0),
    /**  Upon reaching the end of a route, the entity travels the waypoint list in reverse-direction.  */
    ReverseCourse(1),
    /**  Upon reaching the end of a route, the entity travels to the first point and repeats the route.  */
    Loop(2);


    private final int val;

    /** creates a new enum of the specified value */
    TravelMode(int val) {
        this.val = val;
    }

    /** returns the set value for this enum */
    public int getValue() {
        return val;
    }

    /** packs this enum into the LMCP buffer */
    public void pack(OutputStream out) throws IOException { LMCPUtil.putInt32(out, getValue()); }

    /** creates an enum for the value in the LMCP buffer */
    public static TravelMode unpack(InputStream in) throws IOException{
        return getEnum( LMCPUtil.getInt32(in) );
    }

    /** returns a new instance of this enum that matches the passed value (null if value is not known) */
    public static TravelMode getEnum(int val) {
        switch(val) {
            case 0 : return SinglePass;
            case 1 : return ReverseCourse;
            case 2 : return Loop;
            default: return SinglePass;

        }
    }
}
