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

/**  Describes waypoint modes */
public enum NavigationMode {

    /**  standard waypoint following mode  */
    Waypoint(0),
    /**  loiter mode, vehicle in constant orbit  */
    Loiter(1),
    /**  flight director, vehicle following specific heading and spedd commands without regard to waypoints  */
    FlightDirector(2),
    /**  track target, vehicle adjusts path to maintain current target track  */
    TargetTrack(3),
    /**  follow leader, vehicle path is maintained to remain in formation with a leader  */
    FollowLeader(4),
    /**  lost comm, vehicle is out of comm range and should be executing lost-comm behavior  */
    LostComm(5);


    private final int val;

    /** creates a new enum of the specified value */
    NavigationMode(int val) {
        this.val = val;
    }

    /** returns the set value for this enum */
    public int getValue() {
        return val;
    }

    /** packs this enum into the LMCP buffer */
    public void pack(OutputStream out) throws IOException { LMCPUtil.putInt32(out, getValue()); }

    /** creates an enum for the value in the LMCP buffer */
    public static NavigationMode unpack(InputStream in) throws IOException{
        return getEnum( LMCPUtil.getInt32(in) );
    }

    /** returns a new instance of this enum that matches the passed value (null if value is not known) */
    public static NavigationMode getEnum(int val) {
        switch(val) {
            case 0 : return Waypoint;
            case 1 : return Loiter;
            case 2 : return FlightDirector;
            case 3 : return TargetTrack;
            case 4 : return FollowLeader;
            case 5 : return LostComm;
            default: return Waypoint;

        }
    }
}
