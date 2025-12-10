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
 Task complete message 
*/
public class TaskComplete extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 28;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "TaskComplete";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.TaskComplete";

    /**  Task ID that was just completed (Units: None)*/
    @LmcpType("int64")
    protected long TaskID = 0L;
    /**  Entity IDs that completed the task (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> EntitiesInvolved = new java.util.ArrayList<Long>();
    /**  Time that this task was completed. (Units: milliseconds since 1 Jan 1970)*/
    @LmcpType("int64")
    protected long TimeTaskCompleted = 0L;

    
    public TaskComplete() {
    }

    public TaskComplete(long TaskID, long TimeTaskCompleted){
        this.TaskID = TaskID;
        this.TimeTaskCompleted = TimeTaskCompleted;
    }


    public TaskComplete clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            TaskComplete newObj = new TaskComplete();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Task ID that was just completed (Units: None)*/
    public long getTaskID() { return TaskID; }

    /**  Task ID that was just completed (Units: None)*/
    public TaskComplete setTaskID( long val ) {
        TaskID = val;
        return this;
    }

    public java.util.ArrayList<Long> getEntitiesInvolved() {
        return EntitiesInvolved;
    }

    /**  Time that this task was completed. (Units: milliseconds since 1 Jan 1970)*/
    public long getTimeTaskCompleted() { return TimeTaskCompleted; }

    /**  Time that this task was completed. (Units: milliseconds since 1 Jan 1970)*/
    public TaskComplete setTimeTaskCompleted( long val ) {
        TimeTaskCompleted = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types
        
        size += 2 + 8 * EntitiesInvolved.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        TaskID = LMCPUtil.getInt64(in);

        EntitiesInvolved.clear();
        int EntitiesInvolved_len = LMCPUtil.getUint16(in);
        for(int i=0; i<EntitiesInvolved_len; i++){
            EntitiesInvolved.add(LMCPUtil.getInt64(in));
        }
        TimeTaskCompleted = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, TaskID);
        LMCPUtil.putUint16(out, EntitiesInvolved.size());
        for(int i=0; i<EntitiesInvolved.size(); i++){
            LMCPUtil.putInt64(out, EntitiesInvolved.get(i));
        }
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
        buf.append( ws + "<TaskComplete Series=\"UXTASK\">\n");
        buf.append( ws + "  <TaskID>" + String.valueOf(TaskID) + "</TaskID>\n");
        buf.append( ws + "  <EntitiesInvolved>\n");
        for (int i=0; i<EntitiesInvolved.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(EntitiesInvolved.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </EntitiesInvolved>\n");
        buf.append( ws + "  <TimeTaskCompleted>" + String.valueOf(TimeTaskCompleted) + "</TimeTaskCompleted>\n");
        buf.append( ws + "</TaskComplete>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        TaskComplete o = (TaskComplete) anotherObj;
        if (TaskID != o.TaskID) return false;
         if (!EntitiesInvolved.equals( o.EntitiesInvolved)) return false;
        if (TimeTaskCompleted != o.TimeTaskCompleted) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
