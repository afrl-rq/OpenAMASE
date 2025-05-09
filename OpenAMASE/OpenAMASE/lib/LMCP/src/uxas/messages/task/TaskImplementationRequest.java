// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages.task;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Request to generate waypoints enroute to the task as well as to complete a task.        From a given start position and heading, a task must plan enroute waypoints to reach        the task and then append task waypoints for computing the task and then report final        position and heading of the vehicle. 
*/
public class TaskImplementationRequest extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 14;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "TaskImplementationRequest";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.TaskImplementationRequest";

    /**  Request ID for correlating to matching response (Units: None)*/
    @LmcpType("int64")
    protected long RequestID = 0L;
    /**  This task implementation request is part of fulfilling a unique automation request (Units: None)*/
    @LmcpType("int64")
    protected long CorrespondingAutomationRequestID = 0L;
    /**  Starting waypoint ID that task must use when building response. Note that Plan Builder reserves all values greater than 1,000,000,000. Therefore all *internal* task waypoints must be less than 1e9 and can be obtained from the reported waypoint number by "waypoint->getNumber()%1e9" .(Units: None)*/
    @LmcpType("int64")
    protected long StartingWaypointID = 0L;
    /**  Assigned vehicle ID (Units: None)*/
    @LmcpType("int64")
    protected long VehicleID = 0L;
    /**  Initial position of entity before task. A valid TaskImplementationRequest must define StartPosition (null not allowed). (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D StartPosition = new afrl.cmasi.Location3D();
    /**  Initial heading of entity before task (Units: degrees)*/
    @LmcpType("real32")
    protected float StartHeading = (float)0;
    /**  Time when vehicle is at the starting location (Units: milliseconds since 1 Jan 1970)*/
    @LmcpType("int64")
    protected long StartTime = 0L;
    /**  ID for full region in which entity should plan (Units: None)*/
    @LmcpType("int64")
    protected long RegionID = 0L;
    /**  Task ID to be completed (Units: None)*/
    @LmcpType("int64")
    protected long TaskID = 0L;
    /**  Using option ID to complete this task (Units: None)*/
    @LmcpType("int64")
    protected long OptionID = 0L;
    /**  Time before which this task cannot begin (Units: milliseconds since 1 Jan 1970)*/
    @LmcpType("int64")
    protected long TimeThreshold = 0L;
    /**  Predicted locations and headings of all other entities at the point in the mission when this option is to be conducted. (Units: None)*/
    @LmcpType("PlanningState")
    protected java.util.ArrayList<uxas.messages.task.PlanningState> NeighborLocations = new java.util.ArrayList<uxas.messages.task.PlanningState>();

    
    public TaskImplementationRequest() {
    }

    public TaskImplementationRequest(long RequestID, long CorrespondingAutomationRequestID, long StartingWaypointID, long VehicleID, afrl.cmasi.Location3D StartPosition, float StartHeading, long StartTime, long RegionID, long TaskID, long OptionID, long TimeThreshold){
        this.RequestID = RequestID;
        this.CorrespondingAutomationRequestID = CorrespondingAutomationRequestID;
        this.StartingWaypointID = StartingWaypointID;
        this.VehicleID = VehicleID;
        this.StartPosition = StartPosition;
        this.StartHeading = StartHeading;
        this.StartTime = StartTime;
        this.RegionID = RegionID;
        this.TaskID = TaskID;
        this.OptionID = OptionID;
        this.TimeThreshold = TimeThreshold;
    }


    public TaskImplementationRequest clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            TaskImplementationRequest newObj = new TaskImplementationRequest();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Request ID for correlating to matching response (Units: None)*/
    public long getRequestID() { return RequestID; }

    /**  Request ID for correlating to matching response (Units: None)*/
    public TaskImplementationRequest setRequestID( long val ) {
        RequestID = val;
        return this;
    }

    /**  This task implementation request is part of fulfilling a unique automation request (Units: None)*/
    public long getCorrespondingAutomationRequestID() { return CorrespondingAutomationRequestID; }

    /**  This task implementation request is part of fulfilling a unique automation request (Units: None)*/
    public TaskImplementationRequest setCorrespondingAutomationRequestID( long val ) {
        CorrespondingAutomationRequestID = val;
        return this;
    }

    /**  Starting waypoint ID that task must use when building response. Note that Plan Builder reserves all values greater than 1,000,000,000. Therefore all *internal* task waypoints must be less than 1e9 and can be obtained from the reported waypoint number by "waypoint->getNumber()%1e9" .(Units: None)*/
    public long getStartingWaypointID() { return StartingWaypointID; }

    /**  Starting waypoint ID that task must use when building response. Note that Plan Builder reserves all values greater than 1,000,000,000. Therefore all *internal* task waypoints must be less than 1e9 and can be obtained from the reported waypoint number by "waypoint->getNumber()%1e9" .(Units: None)*/
    public TaskImplementationRequest setStartingWaypointID( long val ) {
        StartingWaypointID = val;
        return this;
    }

    /**  Assigned vehicle ID (Units: None)*/
    public long getVehicleID() { return VehicleID; }

    /**  Assigned vehicle ID (Units: None)*/
    public TaskImplementationRequest setVehicleID( long val ) {
        VehicleID = val;
        return this;
    }

    /**  Initial position of entity before task. A valid TaskImplementationRequest must define StartPosition (null not allowed). (Units: None)*/
    public afrl.cmasi.Location3D getStartPosition() { return StartPosition; }

    /**  Initial position of entity before task. A valid TaskImplementationRequest must define StartPosition (null not allowed). (Units: None)*/
    public TaskImplementationRequest setStartPosition( afrl.cmasi.Location3D val ) {
        StartPosition = val;
        return this;
    }

    /**  Initial heading of entity before task (Units: degrees)*/
    public float getStartHeading() { return StartHeading; }

    /**  Initial heading of entity before task (Units: degrees)*/
    public TaskImplementationRequest setStartHeading( float val ) {
        StartHeading = val;
        return this;
    }

    /**  Time when vehicle is at the starting location (Units: milliseconds since 1 Jan 1970)*/
    public long getStartTime() { return StartTime; }

    /**  Time when vehicle is at the starting location (Units: milliseconds since 1 Jan 1970)*/
    public TaskImplementationRequest setStartTime( long val ) {
        StartTime = val;
        return this;
    }

    /**  ID for full region in which entity should plan (Units: None)*/
    public long getRegionID() { return RegionID; }

    /**  ID for full region in which entity should plan (Units: None)*/
    public TaskImplementationRequest setRegionID( long val ) {
        RegionID = val;
        return this;
    }

    /**  Task ID to be completed (Units: None)*/
    public long getTaskID() { return TaskID; }

    /**  Task ID to be completed (Units: None)*/
    public TaskImplementationRequest setTaskID( long val ) {
        TaskID = val;
        return this;
    }

    /**  Using option ID to complete this task (Units: None)*/
    public long getOptionID() { return OptionID; }

    /**  Using option ID to complete this task (Units: None)*/
    public TaskImplementationRequest setOptionID( long val ) {
        OptionID = val;
        return this;
    }

    /**  Time before which this task cannot begin (Units: milliseconds since 1 Jan 1970)*/
    public long getTimeThreshold() { return TimeThreshold; }

    /**  Time before which this task cannot begin (Units: milliseconds since 1 Jan 1970)*/
    public TaskImplementationRequest setTimeThreshold( long val ) {
        TimeThreshold = val;
        return this;
    }

    public java.util.ArrayList<uxas.messages.task.PlanningState> getNeighborLocations() {
        return NeighborLocations;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 76; // accounts for primitive types
        size += LMCPUtil.sizeOf(StartPosition);
        size += 2;
        size += LMCPUtil.sizeOfList(NeighborLocations);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        RequestID = LMCPUtil.getInt64(in);

        CorrespondingAutomationRequestID = LMCPUtil.getInt64(in);

        StartingWaypointID = LMCPUtil.getInt64(in);

        VehicleID = LMCPUtil.getInt64(in);

            StartPosition = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        StartHeading = LMCPUtil.getReal32(in);

        StartTime = LMCPUtil.getInt64(in);

        RegionID = LMCPUtil.getInt64(in);

        TaskID = LMCPUtil.getInt64(in);

        OptionID = LMCPUtil.getInt64(in);

        TimeThreshold = LMCPUtil.getInt64(in);

        NeighborLocations.clear();
        int NeighborLocations_len = LMCPUtil.getUint16(in);
        for(int i=0; i<NeighborLocations_len; i++){
        NeighborLocations.add( (uxas.messages.task.PlanningState) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, RequestID);
        LMCPUtil.putInt64(out, CorrespondingAutomationRequestID);
        LMCPUtil.putInt64(out, StartingWaypointID);
        LMCPUtil.putInt64(out, VehicleID);
        LMCPUtil.putObject(out, StartPosition);
        LMCPUtil.putReal32(out, StartHeading);
        LMCPUtil.putInt64(out, StartTime);
        LMCPUtil.putInt64(out, RegionID);
        LMCPUtil.putInt64(out, TaskID);
        LMCPUtil.putInt64(out, OptionID);
        LMCPUtil.putInt64(out, TimeThreshold);
        LMCPUtil.putUint16(out, NeighborLocations.size());
        for(int i=0; i<NeighborLocations.size(); i++){
            LMCPUtil.putObject(out, NeighborLocations.get(i));
        }

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
        buf.append( ws + "<TaskImplementationRequest Series=\"UXTASK\">\n");
        buf.append( ws + "  <RequestID>" + String.valueOf(RequestID) + "</RequestID>\n");
        buf.append( ws + "  <CorrespondingAutomationRequestID>" + String.valueOf(CorrespondingAutomationRequestID) + "</CorrespondingAutomationRequestID>\n");
        buf.append( ws + "  <StartingWaypointID>" + String.valueOf(StartingWaypointID) + "</StartingWaypointID>\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        if (StartPosition!= null){
           buf.append( ws + "  <StartPosition>\n");
           buf.append( ( StartPosition.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </StartPosition>\n");
        }
        buf.append( ws + "  <StartHeading>" + String.valueOf(StartHeading) + "</StartHeading>\n");
        buf.append( ws + "  <StartTime>" + String.valueOf(StartTime) + "</StartTime>\n");
        buf.append( ws + "  <RegionID>" + String.valueOf(RegionID) + "</RegionID>\n");
        buf.append( ws + "  <TaskID>" + String.valueOf(TaskID) + "</TaskID>\n");
        buf.append( ws + "  <OptionID>" + String.valueOf(OptionID) + "</OptionID>\n");
        buf.append( ws + "  <TimeThreshold>" + String.valueOf(TimeThreshold) + "</TimeThreshold>\n");
        buf.append( ws + "  <NeighborLocations>\n");
        for (int i=0; i<NeighborLocations.size(); i++) {
            buf.append( NeighborLocations.get(i) == null ? ( ws + "    <null/>\n") : (NeighborLocations.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </NeighborLocations>\n");
        buf.append( ws + "</TaskImplementationRequest>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        TaskImplementationRequest o = (TaskImplementationRequest) anotherObj;
        if (RequestID != o.RequestID) return false;
        if (CorrespondingAutomationRequestID != o.CorrespondingAutomationRequestID) return false;
        if (StartingWaypointID != o.StartingWaypointID) return false;
        if (VehicleID != o.VehicleID) return false;
        if (StartPosition == null && o.StartPosition != null) return false;
        if ( StartPosition!= null && !StartPosition.equals(o.StartPosition)) return false;
        if (StartHeading != o.StartHeading) return false;
        if (StartTime != o.StartTime) return false;
        if (RegionID != o.RegionID) return false;
        if (TaskID != o.TaskID) return false;
        if (OptionID != o.OptionID) return false;
        if (TimeThreshold != o.TimeThreshold) return false;
         if (!NeighborLocations.equals( o.NeighborLocations)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
