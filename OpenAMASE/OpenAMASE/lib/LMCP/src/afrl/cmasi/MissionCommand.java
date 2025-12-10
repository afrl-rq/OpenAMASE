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
            A mission to be performed by a vehicle.  This sends a new list of waypoints to an aircraft and commands the aircraft            to start flying the mission starting at the specified "FirstWaypoint".  A Mission command is made up of a linked            list of waypoints.  However, the waypoint list may contain other waypoints that are not linked to the main route path            that begins with "FirstWaypoint".<br/>            <i>Note on end-of-mission behavior: </i> The last waypoint in the linked route list should include some terminal            behavior. For instance, the last waypoint should be directed with itself as the "Next Waypoint" or there should be            an indefinite loiter assigned in the last waypoint's "VehicleActionList".<br/>            VehicleActionLists are performed immediately when MissionCommand is received, e.g. camera pointed toward first waypoint.        
*/
public class MissionCommand extends afrl.cmasi.VehicleActionCommand {
    
    public static final int LMCP_TYPE = 36;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "MissionCommand";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.MissionCommand";

    /**  The list of waypoints associated with this mission task. Waypoints are linked, but the waypoint list may contain waypoints that are not necessarily linked. Multiple linked routes may be sent in a single waypoint list. Waypoints are not necessarily ordered in the list. (Units: None)*/
    @LmcpType("Waypoint")
    protected java.util.ArrayList<afrl.cmasi.Waypoint> WaypointList = new java.util.ArrayList<afrl.cmasi.Waypoint>();
    /**  ID of the first waypoint to follow. (Units: None)*/
    @LmcpType("int64")
    protected long FirstWaypoint = 0L;

    
    public MissionCommand() {
    }

    public MissionCommand(long CommandID, long VehicleID, afrl.cmasi.CommandStatusType Status, long FirstWaypoint){
        this.CommandID = CommandID;
        this.VehicleID = VehicleID;
        this.Status = Status;
        this.FirstWaypoint = FirstWaypoint;
    }


    public MissionCommand clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            MissionCommand newObj = new MissionCommand();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public java.util.ArrayList<afrl.cmasi.Waypoint> getWaypointList() {
        return WaypointList;
    }

    /**  ID of the first waypoint to follow. (Units: None)*/
    public long getFirstWaypoint() { return FirstWaypoint; }

    /**  ID of the first waypoint to follow. (Units: None)*/
    public MissionCommand setFirstWaypoint( long val ) {
        FirstWaypoint = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(WaypointList);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        WaypointList.clear();
        int WaypointList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<WaypointList_len; i++){
        WaypointList.add( (afrl.cmasi.Waypoint) LMCPUtil.getObject(in));
        }
        FirstWaypoint = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putUint16(out, WaypointList.size());
        for(int i=0; i<WaypointList.size(); i++){
            LMCPUtil.putObject(out, WaypointList.get(i));
        }
        LMCPUtil.putInt64(out, FirstWaypoint);

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
        buf.append( ws + "<MissionCommand Series=\"CMASI\">\n");
        buf.append( ws + "  <WaypointList>\n");
        for (int i=0; i<WaypointList.size(); i++) {
            buf.append( WaypointList.get(i) == null ? ( ws + "    <null/>\n") : (WaypointList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </WaypointList>\n");
        buf.append( ws + "  <FirstWaypoint>" + String.valueOf(FirstWaypoint) + "</FirstWaypoint>\n");
        buf.append( ws + "  <CommandID>" + String.valueOf(CommandID) + "</CommandID>\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <VehicleActionList>\n");
        for (int i=0; i<VehicleActionList.size(); i++) {
            buf.append( VehicleActionList.get(i) == null ? ( ws + "    <null/>\n") : (VehicleActionList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </VehicleActionList>\n");
        buf.append( ws + "  <Status>" + String.valueOf(Status) + "</Status>\n");
        buf.append( ws + "</MissionCommand>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        MissionCommand o = (MissionCommand) anotherObj;
         if (!WaypointList.equals( o.WaypointList)) return false;
        if (FirstWaypoint != o.FirstWaypoint) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
