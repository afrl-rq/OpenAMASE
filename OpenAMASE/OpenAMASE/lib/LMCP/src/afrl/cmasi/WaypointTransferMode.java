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

/**  Used in {@link WaypointTransfer} to indicate the type of waypoint exchange */
public enum WaypointTransferMode {

    /**  Requests download of waypoints from the aircraft  */
    RequestWaypoints(0),
    /**  Adds waypoints to the aircraft.  Waypoints with the same number are overwritten  */
    AddWaypoints(1),
    /**  Commands the aircraft to clear its waypoints  */
    ClearWaypoints(2),
    /**  Reports waypoints that are curretly in the aircraft computer  */
    ReportWaypoints(3);


    private final int val;

    /** creates a new enum of the specified value */
    WaypointTransferMode(int val) {
        this.val = val;
    }

    /** returns the set value for this enum */
    public int getValue() {
        return val;
    }

    /** packs this enum into the LMCP buffer */
    public void pack(OutputStream out) throws IOException { LMCPUtil.putInt32(out, getValue()); }

    /** creates an enum for the value in the LMCP buffer */
    public static WaypointTransferMode unpack(InputStream in) throws IOException{
        return getEnum( LMCPUtil.getInt32(in) );
    }

    /** returns a new instance of this enum that matches the passed value (null if value is not known) */
    public static WaypointTransferMode getEnum(int val) {
        switch(val) {
            case 0 : return RequestWaypoints;
            case 1 : return AddWaypoints;
            case 2 : return ClearWaypoints;
            case 3 : return ReportWaypoints;
            default: return RequestWaypoints;

        }
    }
}
