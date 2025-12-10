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
 Representation of the assignment of entities to a task 
*/
public class TaskAssignment extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 18;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "TaskAssignment";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.TaskAssignment";

    /**  Task ID (Units: None)*/
    @LmcpType("int64")
    protected long TaskID = 0L;
    /**  Option ID that was selected for this task (Units: None)*/
    @LmcpType("int64")
    protected long OptionID = 0L;
    /**  Vehicle that is assigned to this task (Units: None)*/
    @LmcpType("int64")
    protected long AssignedVehicle = 0L;
    /**  Time before which this task cannot begin (Units: milliseconds since 1 Jan 1970)*/
    @LmcpType("int64")
    protected long TimeThreshold = 0L;
    /**  Time that this task is assigned to be completed. (Units: milliseconds since 1 Jan 1970)*/
    @LmcpType("int64")
    protected long TimeTaskCompleted = 0L;

    
    public TaskAssignment() {
    }

    public TaskAssignment(long TaskID, long OptionID, long AssignedVehicle, long TimeThreshold, long TimeTaskCompleted){
        this.TaskID = TaskID;
        this.OptionID = OptionID;
        this.AssignedVehicle = AssignedVehicle;
        this.TimeThreshold = TimeThreshold;
        this.TimeTaskCompleted = TimeTaskCompleted;
    }


    public TaskAssignment clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            TaskAssignment newObj = new TaskAssignment();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Task ID (Units: None)*/
    public long getTaskID() { return TaskID; }

    /**  Task ID (Units: None)*/
    public TaskAssignment setTaskID( long val ) {
        TaskID = val;
        return this;
    }

    /**  Option ID that was selected for this task (Units: None)*/
    public long getOptionID() { return OptionID; }

    /**  Option ID that was selected for this task (Units: None)*/
    public TaskAssignment setOptionID( long val ) {
        OptionID = val;
        return this;
    }

    /**  Vehicle that is assigned to this task (Units: None)*/
    public long getAssignedVehicle() { return AssignedVehicle; }

    /**  Vehicle that is assigned to this task (Units: None)*/
    public TaskAssignment setAssignedVehicle( long val ) {
        AssignedVehicle = val;
        return this;
    }

    /**  Time before which this task cannot begin (Units: milliseconds since 1 Jan 1970)*/
    public long getTimeThreshold() { return TimeThreshold; }

    /**  Time before which this task cannot begin (Units: milliseconds since 1 Jan 1970)*/
    public TaskAssignment setTimeThreshold( long val ) {
        TimeThreshold = val;
        return this;
    }

    /**  Time that this task is assigned to be completed. (Units: milliseconds since 1 Jan 1970)*/
    public long getTimeTaskCompleted() { return TimeTaskCompleted; }

    /**  Time that this task is assigned to be completed. (Units: milliseconds since 1 Jan 1970)*/
    public TaskAssignment setTimeTaskCompleted( long val ) {
        TimeTaskCompleted = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 40; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        TaskID = LMCPUtil.getInt64(in);

        OptionID = LMCPUtil.getInt64(in);

        AssignedVehicle = LMCPUtil.getInt64(in);

        TimeThreshold = LMCPUtil.getInt64(in);

        TimeTaskCompleted = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, TaskID);
        LMCPUtil.putInt64(out, OptionID);
        LMCPUtil.putInt64(out, AssignedVehicle);
        LMCPUtil.putInt64(out, TimeThreshold);
        LMCPUtil.putInt64(out, TimeTaskCompleted);

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
        buf.append( ws + "<TaskAssignment Series=\"UXTASK\">\n");
        buf.append( ws + "  <TaskID>" + String.valueOf(TaskID) + "</TaskID>\n");
        buf.append( ws + "  <OptionID>" + String.valueOf(OptionID) + "</OptionID>\n");
        buf.append( ws + "  <AssignedVehicle>" + String.valueOf(AssignedVehicle) + "</AssignedVehicle>\n");
        buf.append( ws + "  <TimeThreshold>" + String.valueOf(TimeThreshold) + "</TimeThreshold>\n");
        buf.append( ws + "  <TimeTaskCompleted>" + String.valueOf(TimeTaskCompleted) + "</TimeTaskCompleted>\n");
        buf.append( ws + "</TaskAssignment>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        TaskAssignment o = (TaskAssignment) anotherObj;
        if (TaskID != o.TaskID) return false;
        if (OptionID != o.OptionID) return false;
        if (AssignedVehicle != o.AssignedVehicle) return false;
        if (TimeThreshold != o.TimeThreshold) return false;
        if (TimeTaskCompleted != o.TimeTaskCompleted) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
