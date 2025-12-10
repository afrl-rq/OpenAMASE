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
 Task Active message. Sent when task becomes active 
*/
public class TaskActive extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 27;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "TaskActive";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.TaskActive";

    /**  Task ID that was just activated (Units: None)*/
    @LmcpType("int64")
    protected long TaskID = 0L;
    /**  Entity ID that activated the task (Units: None)*/
    @LmcpType("int64")
    protected long EntityID = 0L;
    /**  Time that this task was activated. (Units: milliseconds since 1 Jan 1970)*/
    @LmcpType("int64")
    protected long TimeTaskActivated = 0L;

    
    public TaskActive() {
    }

    public TaskActive(long TaskID, long EntityID, long TimeTaskActivated){
        this.TaskID = TaskID;
        this.EntityID = EntityID;
        this.TimeTaskActivated = TimeTaskActivated;
    }


    public TaskActive clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            TaskActive newObj = new TaskActive();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Task ID that was just activated (Units: None)*/
    public long getTaskID() { return TaskID; }

    /**  Task ID that was just activated (Units: None)*/
    public TaskActive setTaskID( long val ) {
        TaskID = val;
        return this;
    }

    /**  Entity ID that activated the task (Units: None)*/
    public long getEntityID() { return EntityID; }

    /**  Entity ID that activated the task (Units: None)*/
    public TaskActive setEntityID( long val ) {
        EntityID = val;
        return this;
    }

    /**  Time that this task was activated. (Units: milliseconds since 1 Jan 1970)*/
    public long getTimeTaskActivated() { return TimeTaskActivated; }

    /**  Time that this task was activated. (Units: milliseconds since 1 Jan 1970)*/
    public TaskActive setTimeTaskActivated( long val ) {
        TimeTaskActivated = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 24; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        TaskID = LMCPUtil.getInt64(in);

        EntityID = LMCPUtil.getInt64(in);

        TimeTaskActivated = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, TaskID);
        LMCPUtil.putInt64(out, EntityID);
        LMCPUtil.putInt64(out, TimeTaskActivated);

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
        buf.append( ws + "<TaskActive Series=\"UXTASK\">\n");
        buf.append( ws + "  <TaskID>" + String.valueOf(TaskID) + "</TaskID>\n");
        buf.append( ws + "  <EntityID>" + String.valueOf(EntityID) + "</EntityID>\n");
        buf.append( ws + "  <TimeTaskActivated>" + String.valueOf(TimeTaskActivated) + "</TimeTaskActivated>\n");
        buf.append( ws + "</TaskActive>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        TaskActive o = (TaskActive) anotherObj;
        if (TaskID != o.TaskID) return false;
        if (EntityID != o.EntityID) return false;
        if (TimeTaskActivated != o.TimeTaskActivated) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
