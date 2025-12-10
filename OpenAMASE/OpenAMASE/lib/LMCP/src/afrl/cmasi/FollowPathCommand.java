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
 A command to follow a set of ground waypoints.  This is analogous to a {@link MissionCommand} for aircraft. 
*/
public class FollowPathCommand extends afrl.cmasi.VehicleActionCommand {
    
    public static final int LMCP_TYPE = 56;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "FollowPathCommand";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.FollowPathCommand";

    /**  The first waypoint to follow. This should correspond to the number of a waypoint in the waypoint list. If this is zero, no waypoint will be followed. (Units: None)*/
    @LmcpType("int64")
    protected long FirstWaypoint = 0L;
    /**  A list of waypoints to follow (Units: None)*/
    @LmcpType("PathWaypoint")
    protected java.util.ArrayList<afrl.cmasi.PathWaypoint> WaypointList = new java.util.ArrayList<afrl.cmasi.PathWaypoint>();
    /**  Describes the start time for this action, in scenario time. If this field is zero, the action is completed immediately. (Units: milliseconds)*/
    @LmcpType("int64")
    protected long StartTime = 0L;
    /**  Describes the end time for this action, in scenario time. If this field is zero, it should be ignored, otherwise The entity will travel until the stop time is reached. (Units: milliseconds)*/
    @LmcpType("int64")
    protected long StopTime = 0L;
    /**  Describes how the entity should treat the end-of-path. Entities can complete the path once, or continuously.(Units: None)*/
    @LmcpType("TravelMode")
    protected afrl.cmasi.TravelMode RepeatMode = afrl.cmasi.TravelMode.SinglePass;

    
    public FollowPathCommand() {
    }

    public FollowPathCommand(long CommandID, long VehicleID, afrl.cmasi.CommandStatusType Status, long FirstWaypoint, long StartTime, long StopTime, afrl.cmasi.TravelMode RepeatMode){
        this.CommandID = CommandID;
        this.VehicleID = VehicleID;
        this.Status = Status;
        this.FirstWaypoint = FirstWaypoint;
        this.StartTime = StartTime;
        this.StopTime = StopTime;
        this.RepeatMode = RepeatMode;
    }


    public FollowPathCommand clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            FollowPathCommand newObj = new FollowPathCommand();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The first waypoint to follow. This should correspond to the number of a waypoint in the waypoint list. If this is zero, no waypoint will be followed. (Units: None)*/
    public long getFirstWaypoint() { return FirstWaypoint; }

    /**  The first waypoint to follow. This should correspond to the number of a waypoint in the waypoint list. If this is zero, no waypoint will be followed. (Units: None)*/
    public FollowPathCommand setFirstWaypoint( long val ) {
        FirstWaypoint = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.PathWaypoint> getWaypointList() {
        return WaypointList;
    }

    /**  Describes the start time for this action, in scenario time. If this field is zero, the action is completed immediately. (Units: milliseconds)*/
    public long getStartTime() { return StartTime; }

    /**  Describes the start time for this action, in scenario time. If this field is zero, the action is completed immediately. (Units: milliseconds)*/
    public FollowPathCommand setStartTime( long val ) {
        StartTime = val;
        return this;
    }

    /**  Describes the end time for this action, in scenario time. If this field is zero, it should be ignored, otherwise The entity will travel until the stop time is reached. (Units: milliseconds)*/
    public long getStopTime() { return StopTime; }

    /**  Describes the end time for this action, in scenario time. If this field is zero, it should be ignored, otherwise The entity will travel until the stop time is reached. (Units: milliseconds)*/
    public FollowPathCommand setStopTime( long val ) {
        StopTime = val;
        return this;
    }

    /**  Describes how the entity should treat the end-of-path. Entities can complete the path once, or continuously.(Units: None)*/
    public afrl.cmasi.TravelMode getRepeatMode() { return RepeatMode; }

    /**  Describes how the entity should treat the end-of-path. Entities can complete the path once, or continuously.(Units: None)*/
    public FollowPathCommand setRepeatMode( afrl.cmasi.TravelMode val ) {
        RepeatMode = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 28; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(WaypointList);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        FirstWaypoint = LMCPUtil.getInt64(in);

        WaypointList.clear();
        int WaypointList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<WaypointList_len; i++){
        WaypointList.add( (afrl.cmasi.PathWaypoint) LMCPUtil.getObject(in));
        }
        StartTime = LMCPUtil.getInt64(in);

        StopTime = LMCPUtil.getInt64(in);

        RepeatMode = afrl.cmasi.TravelMode.unpack( in );


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, FirstWaypoint);
        LMCPUtil.putUint16(out, WaypointList.size());
        for(int i=0; i<WaypointList.size(); i++){
            LMCPUtil.putObject(out, WaypointList.get(i));
        }
        LMCPUtil.putInt64(out, StartTime);
        LMCPUtil.putInt64(out, StopTime);
        RepeatMode.pack(out);

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
        buf.append( ws + "<FollowPathCommand Series=\"CMASI\">\n");
        buf.append( ws + "  <FirstWaypoint>" + String.valueOf(FirstWaypoint) + "</FirstWaypoint>\n");
        buf.append( ws + "  <WaypointList>\n");
        for (int i=0; i<WaypointList.size(); i++) {
            buf.append( WaypointList.get(i) == null ? ( ws + "    <null/>\n") : (WaypointList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </WaypointList>\n");
        buf.append( ws + "  <StartTime>" + String.valueOf(StartTime) + "</StartTime>\n");
        buf.append( ws + "  <StopTime>" + String.valueOf(StopTime) + "</StopTime>\n");
        buf.append( ws + "  <RepeatMode>" + String.valueOf(RepeatMode) + "</RepeatMode>\n");
        buf.append( ws + "  <CommandID>" + String.valueOf(CommandID) + "</CommandID>\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <VehicleActionList>\n");
        for (int i=0; i<VehicleActionList.size(); i++) {
            buf.append( VehicleActionList.get(i) == null ? ( ws + "    <null/>\n") : (VehicleActionList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </VehicleActionList>\n");
        buf.append( ws + "  <Status>" + String.valueOf(Status) + "</Status>\n");
        buf.append( ws + "</FollowPathCommand>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        FollowPathCommand o = (FollowPathCommand) anotherObj;
        if (FirstWaypoint != o.FirstWaypoint) return false;
         if (!WaypointList.equals( o.WaypointList)) return false;
        if (StartTime != o.StartTime) return false;
        if (StopTime != o.StopTime) return false;
        if (RepeatMode != o.RepeatMode) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
