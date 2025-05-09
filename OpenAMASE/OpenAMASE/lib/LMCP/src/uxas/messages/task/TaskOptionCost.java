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
 Single task option cost for a particular vehicle 
*/
public class TaskOptionCost extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 17;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "TaskOptionCost";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.TaskOptionCost";

    /**  Corresponding Vehicle ID (Units: None)*/
    @LmcpType("int64")
    protected long VehicleID = 0L;
    /**  Initial task ID (if zero, corresponds to current vehicle location) (Units: None)*/
    @LmcpType("int64")
    protected long IntialTaskID = 0L;
    /**  Initial task option (Units: None)*/
    @LmcpType("int64")
    protected long IntialTaskOption = 0L;
    /**  Destination task ID (Units: None)*/
    @LmcpType("int64")
    protected long DestinationTaskID = 0L;
    /**  Destination task option (Units: None)*/
    @LmcpType("int64")
    protected long DestinationTaskOption = 0L;
    /**  Timing corresponding to travel between ('InitialTask' using 'InitialTaskOption') and ('DestinationTask' using 'DestinationTaskOption'). If time is less than zero, no feasible path exists between tasks. (Units: milliseconds)*/
    @LmcpType("int64")
    protected long TimeToGo = 0L;

    
    public TaskOptionCost() {
    }

    public TaskOptionCost(long VehicleID, long IntialTaskID, long IntialTaskOption, long DestinationTaskID, long DestinationTaskOption, long TimeToGo){
        this.VehicleID = VehicleID;
        this.IntialTaskID = IntialTaskID;
        this.IntialTaskOption = IntialTaskOption;
        this.DestinationTaskID = DestinationTaskID;
        this.DestinationTaskOption = DestinationTaskOption;
        this.TimeToGo = TimeToGo;
    }


    public TaskOptionCost clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            TaskOptionCost newObj = new TaskOptionCost();
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
    public TaskOptionCost setVehicleID( long val ) {
        VehicleID = val;
        return this;
    }

    /**  Initial task ID (if zero, corresponds to current vehicle location) (Units: None)*/
    public long getIntialTaskID() { return IntialTaskID; }

    /**  Initial task ID (if zero, corresponds to current vehicle location) (Units: None)*/
    public TaskOptionCost setIntialTaskID( long val ) {
        IntialTaskID = val;
        return this;
    }

    /**  Initial task option (Units: None)*/
    public long getIntialTaskOption() { return IntialTaskOption; }

    /**  Initial task option (Units: None)*/
    public TaskOptionCost setIntialTaskOption( long val ) {
        IntialTaskOption = val;
        return this;
    }

    /**  Destination task ID (Units: None)*/
    public long getDestinationTaskID() { return DestinationTaskID; }

    /**  Destination task ID (Units: None)*/
    public TaskOptionCost setDestinationTaskID( long val ) {
        DestinationTaskID = val;
        return this;
    }

    /**  Destination task option (Units: None)*/
    public long getDestinationTaskOption() { return DestinationTaskOption; }

    /**  Destination task option (Units: None)*/
    public TaskOptionCost setDestinationTaskOption( long val ) {
        DestinationTaskOption = val;
        return this;
    }

    /**  Timing corresponding to travel between ('InitialTask' using 'InitialTaskOption') and ('DestinationTask' using 'DestinationTaskOption'). If time is less than zero, no feasible path exists between tasks. (Units: milliseconds)*/
    public long getTimeToGo() { return TimeToGo; }

    /**  Timing corresponding to travel between ('InitialTask' using 'InitialTaskOption') and ('DestinationTask' using 'DestinationTaskOption'). If time is less than zero, no feasible path exists between tasks. (Units: milliseconds)*/
    public TaskOptionCost setTimeToGo( long val ) {
        TimeToGo = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 48; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        VehicleID = LMCPUtil.getInt64(in);

        IntialTaskID = LMCPUtil.getInt64(in);

        IntialTaskOption = LMCPUtil.getInt64(in);

        DestinationTaskID = LMCPUtil.getInt64(in);

        DestinationTaskOption = LMCPUtil.getInt64(in);

        TimeToGo = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, VehicleID);
        LMCPUtil.putInt64(out, IntialTaskID);
        LMCPUtil.putInt64(out, IntialTaskOption);
        LMCPUtil.putInt64(out, DestinationTaskID);
        LMCPUtil.putInt64(out, DestinationTaskOption);
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
        buf.append( ws + "<TaskOptionCost Series=\"UXTASK\">\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <IntialTaskID>" + String.valueOf(IntialTaskID) + "</IntialTaskID>\n");
        buf.append( ws + "  <IntialTaskOption>" + String.valueOf(IntialTaskOption) + "</IntialTaskOption>\n");
        buf.append( ws + "  <DestinationTaskID>" + String.valueOf(DestinationTaskID) + "</DestinationTaskID>\n");
        buf.append( ws + "  <DestinationTaskOption>" + String.valueOf(DestinationTaskOption) + "</DestinationTaskOption>\n");
        buf.append( ws + "  <TimeToGo>" + String.valueOf(TimeToGo) + "</TimeToGo>\n");
        buf.append( ws + "</TaskOptionCost>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        TaskOptionCost o = (TaskOptionCost) anotherObj;
        if (VehicleID != o.VehicleID) return false;
        if (IntialTaskID != o.IntialTaskID) return false;
        if (IntialTaskOption != o.IntialTaskOption) return false;
        if (DestinationTaskID != o.DestinationTaskID) return false;
        if (DestinationTaskOption != o.DestinationTaskOption) return false;
        if (TimeToGo != o.TimeToGo) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
