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


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Commands an aircraft to start flying towards a given waypoint in its waypoint list.  This action depends on an        aircraft already having the given waypoint in its current list.  A {@link MissionCommand} should be sent if new waypoints        are required.  The aircraft will remain in waypoint mode and will continue to fly its linked-list waypoint route until        otherwise commanded.        
*/
public class GoToWaypointAction extends afrl.cmasi.NavigationAction {
    
    public static final int LMCP_TYPE = 28;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "GoToWaypointAction";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.GoToWaypointAction";

    /**  Waypoint number to which the aircraft should fly. (Units: None)*/
    @LmcpType("int64")
    protected long WaypointNumber = 0L;

    
    public GoToWaypointAction() {
    }

    public GoToWaypointAction(long WaypointNumber){
        this.WaypointNumber = WaypointNumber;
    }


    public GoToWaypointAction clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            GoToWaypointAction newObj = new GoToWaypointAction();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Waypoint number to which the aircraft should fly. (Units: None)*/
    public long getWaypointNumber() { return WaypointNumber; }

    /**  Waypoint number to which the aircraft should fly. (Units: None)*/
    public GoToWaypointAction setWaypointNumber( long val ) {
        WaypointNumber = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        WaypointNumber = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, WaypointNumber);

    }

    public int getLMCPType() { return LMCP_TYPE; }

    public String getLMCPSeriesName() { return SERIES_NAME; }

    public long getLMCPSeriesNameAsLong() { return SERIES_NAME_ID; }

    public int getLMCPSeriesVersion() { return SERIES_VERSION; };

    public String getLMCPTypeName() { return TYPE_NAME; }

    public String getFullLMCPTypeName() { return FULL_LMCP_TYPE_NAME; }

    public String toString() {
        return toXML("");
    }

    public String toXML(String ws) {
        StringBuffer buf = new StringBuffer();
        buf.append( ws + "<GoToWaypointAction Series=\"CMASI\">\n");
        buf.append( ws + "  <WaypointNumber>" + String.valueOf(WaypointNumber) + "</WaypointNumber>\n");
        buf.append( ws + "  <AssociatedTaskList>\n");
        for (int i=0; i<AssociatedTaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTaskList>\n");
        buf.append( ws + "</GoToWaypointAction>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        GoToWaypointAction o = (GoToWaypointAction) anotherObj;
        if (WaypointNumber != o.WaypointNumber) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
