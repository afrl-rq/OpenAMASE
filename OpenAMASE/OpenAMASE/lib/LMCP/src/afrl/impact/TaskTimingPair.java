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


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Single task/vehicle timing information capturing the estimated time for        the specified vehicle to travel to or between tasks 
*/
public class TaskTimingPair extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 11;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "TaskTimingPair";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.TaskTimingPair";

    /**  Corresponding Vehicle ID (Units: None)*/
    @LmcpType("int64")
    protected long VehicleID = 0L;
    /**  Initial task ID (if zero, corresponds to current vehicle location) (Units: None)*/
    @LmcpType("int64")
    protected long InitialTaskID = 0L;
    /**  Normalized progress along initial task (0.0 .. 1.0) (Units: None)*/
    @LmcpType("real32")
    protected float InitialTaskPercentage = (float)0;
    /**  Destination task ID (Units: None)*/
    @LmcpType("int64")
    protected long DestinationTaskID = 0L;
    /**  Timing corresponding to travel between tasks. If time is less than zero, no feasible path exists between tasks. If 'DestinationTaskID' is equal to 'InitialTaskID' then 'TimeToGo' is the time to complete the task when performed by 'VehicleID' (Units: milliseconds)*/
    @LmcpType("int64")
    protected long TimeToGo = 0L;

    
    public TaskTimingPair() {
    }

    public TaskTimingPair(long VehicleID, long InitialTaskID, float InitialTaskPercentage, long DestinationTaskID, long TimeToGo){
        this.VehicleID = VehicleID;
        this.InitialTaskID = InitialTaskID;
        this.InitialTaskPercentage = InitialTaskPercentage;
        this.DestinationTaskID = DestinationTaskID;
        this.TimeToGo = TimeToGo;
    }


    public TaskTimingPair clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            TaskTimingPair newObj = new TaskTimingPair();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Corresponding Vehicle ID (Units: None)*/
    public long getVehicleID() { return VehicleID; }

    /**  Corresponding Vehicle ID (Units: None)*/
    public TaskTimingPair setVehicleID( long val ) {
        VehicleID = val;
        return this;
    }

    /**  Initial task ID (if zero, corresponds to current vehicle location) (Units: None)*/
    public long getInitialTaskID() { return InitialTaskID; }

    /**  Initial task ID (if zero, corresponds to current vehicle location) (Units: None)*/
    public TaskTimingPair setInitialTaskID( long val ) {
        InitialTaskID = val;
        return this;
    }

    /**  Normalized progress along initial task (0.0 .. 1.0) (Units: None)*/
    public float getInitialTaskPercentage() { return InitialTaskPercentage; }

    /**  Normalized progress along initial task (0.0 .. 1.0) (Units: None)*/
    public TaskTimingPair setInitialTaskPercentage( float val ) {
        InitialTaskPercentage = val;
        return this;
    }

    /**  Destination task ID (Units: None)*/
    public long getDestinationTaskID() { return DestinationTaskID; }

    /**  Destination task ID (Units: None)*/
    public TaskTimingPair setDestinationTaskID( long val ) {
        DestinationTaskID = val;
        return this;
    }

    /**  Timing corresponding to travel between tasks. If time is less than zero, no feasible path exists between tasks. If 'DestinationTaskID' is equal to 'InitialTaskID' then 'TimeToGo' is the time to complete the task when performed by 'VehicleID' (Units: milliseconds)*/
    public long getTimeToGo() { return TimeToGo; }

    /**  Timing corresponding to travel between tasks. If time is less than zero, no feasible path exists between tasks. If 'DestinationTaskID' is equal to 'InitialTaskID' then 'TimeToGo' is the time to complete the task when performed by 'VehicleID' (Units: milliseconds)*/
    public TaskTimingPair setTimeToGo( long val ) {
        TimeToGo = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 36; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        VehicleID = LMCPUtil.getInt64(in);

        InitialTaskID = LMCPUtil.getInt64(in);

        InitialTaskPercentage = LMCPUtil.getReal32(in);

        DestinationTaskID = LMCPUtil.getInt64(in);

        TimeToGo = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, VehicleID);
        LMCPUtil.putInt64(out, InitialTaskID);
        LMCPUtil.putReal32(out, InitialTaskPercentage);
        LMCPUtil.putInt64(out, DestinationTaskID);
        LMCPUtil.putInt64(out, TimeToGo);

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
        buf.append( ws + "<TaskTimingPair Series=\"IMPACT\">\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <InitialTaskID>" + String.valueOf(InitialTaskID) + "</InitialTaskID>\n");
        buf.append( ws + "  <InitialTaskPercentage>" + String.valueOf(InitialTaskPercentage) + "</InitialTaskPercentage>\n");
        buf.append( ws + "  <DestinationTaskID>" + String.valueOf(DestinationTaskID) + "</DestinationTaskID>\n");
        buf.append( ws + "  <TimeToGo>" + String.valueOf(TimeToGo) + "</TimeToGo>\n");
        buf.append( ws + "</TaskTimingPair>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        TaskTimingPair o = (TaskTimingPair) anotherObj;
        if (VehicleID != o.VehicleID) return false;
        if (InitialTaskID != o.InitialTaskID) return false;
        if (InitialTaskPercentage != o.InitialTaskPercentage) return false;
        if (DestinationTaskID != o.DestinationTaskID) return false;
        if (TimeToGo != o.TimeToGo) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)InitialTaskPercentage;

        return hash + super.hashCode();
    }
    
}
