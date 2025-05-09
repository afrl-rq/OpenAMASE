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
 Response to a final task implementation request (one for each vehicle assigned).        Note that in addition to calculating task waypoints that achieve that task purpose,        a task implementation also includes the enroute waypoints to get the vehicle(s) to         that task to be completed. Finally, an ending position and heading for the vehicle is        reported so that the next task in order will have a valid start position and heading. 
*/
public class TaskImplementationResponse extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 15;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "TaskImplementationResponse";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.TaskImplementationResponse";

    /**  Response ID that matches the initial request (Units: None)*/
    @LmcpType("int64")
    protected long ResponseID = 0L;
    /**  This task implementation response is part of fulfilling a unique automation request (Units: None)*/
    @LmcpType("int64")
    protected long CorrespondingAutomationRequestID = 0L;
    /**  Task ID (Units: None)*/
    @LmcpType("int64")
    protected long TaskID = 0L;
    /**  Option ID that was selected for this task (Units: None)*/
    @LmcpType("int64")
    protected long OptionID = 0L;
    /**  Vehicle ID (Units: None)*/
    @LmcpType("int64")
    protected long VehicleID = 0L;
    /**  Waypoints that implement this task for the indicated vehicle (Units: None)*/
    @LmcpType("Waypoint")
    protected java.util.ArrayList<afrl.cmasi.Waypoint> TaskWaypoints = new java.util.ArrayList<afrl.cmasi.Waypoint>();
    /**  Vehicle location when this task is complete. A valid TaskImplementationResponse must define FinalLocation (null not allowed). (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D FinalLocation = new afrl.cmasi.Location3D();
    /**  Vehicle heading when this task is complete (Units: degrees)*/
    @LmcpType("real32")
    protected float FinalHeading = (float)0;
    /**  Time when vehicle is at the final location (Units: milliseconds since 1 Jan 1970)*/
    @LmcpType("int64")
    protected long FinalTime = 0L;

    
    public TaskImplementationResponse() {
    }

    public TaskImplementationResponse(long ResponseID, long CorrespondingAutomationRequestID, long TaskID, long OptionID, long VehicleID, afrl.cmasi.Location3D FinalLocation, float FinalHeading, long FinalTime){
        this.ResponseID = ResponseID;
        this.CorrespondingAutomationRequestID = CorrespondingAutomationRequestID;
        this.TaskID = TaskID;
        this.OptionID = OptionID;
        this.VehicleID = VehicleID;
        this.FinalLocation = FinalLocation;
        this.FinalHeading = FinalHeading;
        this.FinalTime = FinalTime;
    }


    public TaskImplementationResponse clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            TaskImplementationResponse newObj = new TaskImplementationResponse();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Response ID that matches the initial request (Units: None)*/
    public long getResponseID() { return ResponseID; }

    /**  Response ID that matches the initial request (Units: None)*/
    public TaskImplementationResponse setResponseID( long val ) {
        ResponseID = val;
        return this;
    }

    /**  This task implementation response is part of fulfilling a unique automation request (Units: None)*/
    public long getCorrespondingAutomationRequestID() { return CorrespondingAutomationRequestID; }

    /**  This task implementation response is part of fulfilling a unique automation request (Units: None)*/
    public TaskImplementationResponse setCorrespondingAutomationRequestID( long val ) {
        CorrespondingAutomationRequestID = val;
        return this;
    }

    /**  Task ID (Units: None)*/
    public long getTaskID() { return TaskID; }

    /**  Task ID (Units: None)*/
    public TaskImplementationResponse setTaskID( long val ) {
        TaskID = val;
        return this;
    }

    /**  Option ID that was selected for this task (Units: None)*/
    public long getOptionID() { return OptionID; }

    /**  Option ID that was selected for this task (Units: None)*/
    public TaskImplementationResponse setOptionID( long val ) {
        OptionID = val;
        return this;
    }

    /**  Vehicle ID (Units: None)*/
    public long getVehicleID() { return VehicleID; }

    /**  Vehicle ID (Units: None)*/
    public TaskImplementationResponse setVehicleID( long val ) {
        VehicleID = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.Waypoint> getTaskWaypoints() {
        return TaskWaypoints;
    }

    /**  Vehicle location when this task is complete. A valid TaskImplementationResponse must define FinalLocation (null not allowed). (Units: None)*/
    public afrl.cmasi.Location3D getFinalLocation() { return FinalLocation; }

    /**  Vehicle location when this task is complete. A valid TaskImplementationResponse must define FinalLocation (null not allowed). (Units: None)*/
    public TaskImplementationResponse setFinalLocation( afrl.cmasi.Location3D val ) {
        FinalLocation = val;
        return this;
    }

    /**  Vehicle heading when this task is complete (Units: degrees)*/
    public float getFinalHeading() { return FinalHeading; }

    /**  Vehicle heading when this task is complete (Units: degrees)*/
    public TaskImplementationResponse setFinalHeading( float val ) {
        FinalHeading = val;
        return this;
    }

    /**  Time when vehicle is at the final location (Units: milliseconds since 1 Jan 1970)*/
    public long getFinalTime() { return FinalTime; }

    /**  Time when vehicle is at the final location (Units: milliseconds since 1 Jan 1970)*/
    public TaskImplementationResponse setFinalTime( long val ) {
        FinalTime = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 52; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(TaskWaypoints);
        size += LMCPUtil.sizeOf(FinalLocation);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ResponseID = LMCPUtil.getInt64(in);

        CorrespondingAutomationRequestID = LMCPUtil.getInt64(in);

        TaskID = LMCPUtil.getInt64(in);

        OptionID = LMCPUtil.getInt64(in);

        VehicleID = LMCPUtil.getInt64(in);

        TaskWaypoints.clear();
        int TaskWaypoints_len = LMCPUtil.getUint16(in);
        for(int i=0; i<TaskWaypoints_len; i++){
        TaskWaypoints.add( (afrl.cmasi.Waypoint) LMCPUtil.getObject(in));
        }
            FinalLocation = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        FinalHeading = LMCPUtil.getReal32(in);

        FinalTime = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ResponseID);
        LMCPUtil.putInt64(out, CorrespondingAutomationRequestID);
        LMCPUtil.putInt64(out, TaskID);
        LMCPUtil.putInt64(out, OptionID);
        LMCPUtil.putInt64(out, VehicleID);
        LMCPUtil.putUint16(out, TaskWaypoints.size());
        for(int i=0; i<TaskWaypoints.size(); i++){
            LMCPUtil.putObject(out, TaskWaypoints.get(i));
        }
        LMCPUtil.putObject(out, FinalLocation);
        LMCPUtil.putReal32(out, FinalHeading);
        LMCPUtil.putInt64(out, FinalTime);

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
        buf.append( ws + "<TaskImplementationResponse Series=\"UXTASK\">\n");
        buf.append( ws + "  <ResponseID>" + String.valueOf(ResponseID) + "</ResponseID>\n");
        buf.append( ws + "  <CorrespondingAutomationRequestID>" + String.valueOf(CorrespondingAutomationRequestID) + "</CorrespondingAutomationRequestID>\n");
        buf.append( ws + "  <TaskID>" + String.valueOf(TaskID) + "</TaskID>\n");
        buf.append( ws + "  <OptionID>" + String.valueOf(OptionID) + "</OptionID>\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <TaskWaypoints>\n");
        for (int i=0; i<TaskWaypoints.size(); i++) {
            buf.append( TaskWaypoints.get(i) == null ? ( ws + "    <null/>\n") : (TaskWaypoints.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </TaskWaypoints>\n");
        if (FinalLocation!= null){
           buf.append( ws + "  <FinalLocation>\n");
           buf.append( ( FinalLocation.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </FinalLocation>\n");
        }
        buf.append( ws + "  <FinalHeading>" + String.valueOf(FinalHeading) + "</FinalHeading>\n");
        buf.append( ws + "  <FinalTime>" + String.valueOf(FinalTime) + "</FinalTime>\n");
        buf.append( ws + "</TaskImplementationResponse>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        TaskImplementationResponse o = (TaskImplementationResponse) anotherObj;
        if (ResponseID != o.ResponseID) return false;
        if (CorrespondingAutomationRequestID != o.CorrespondingAutomationRequestID) return false;
        if (TaskID != o.TaskID) return false;
        if (OptionID != o.OptionID) return false;
        if (VehicleID != o.VehicleID) return false;
         if (!TaskWaypoints.equals( o.TaskWaypoints)) return false;
        if (FinalLocation == null && o.FinalLocation != null) return false;
        if ( FinalLocation!= null && !FinalLocation.equals(o.FinalLocation)) return false;
        if (FinalHeading != o.FinalHeading) return false;
        if (FinalTime != o.FinalTime) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
