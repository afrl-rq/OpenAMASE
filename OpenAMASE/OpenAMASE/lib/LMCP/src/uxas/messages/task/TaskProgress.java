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
 Reports the progress of the task 
*/
public class TaskProgress extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 24;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "TaskProgress";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.TaskProgress";

    /**  Response ID that matches the task progress request (Units: None)*/
    @LmcpType("int64")
    protected long ResponseID = 0L;
    /**  Task ID (Units: None)*/
    @LmcpType("int64")
    protected long TaskID = 0L;
    /**  Percentage of progress on task (Units: None)*/
    @LmcpType("real32")
    protected float PercentComplete = (float)0;
    /**  Entities that are working this task (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> EntitiesEngaged = new java.util.ArrayList<Long>();

    
    public TaskProgress() {
    }

    public TaskProgress(long ResponseID, long TaskID, float PercentComplete){
        this.ResponseID = ResponseID;
        this.TaskID = TaskID;
        this.PercentComplete = PercentComplete;
    }


    public TaskProgress clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            TaskProgress newObj = new TaskProgress();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Response ID that matches the task progress request (Units: None)*/
    public long getResponseID() { return ResponseID; }

    /**  Response ID that matches the task progress request (Units: None)*/
    public TaskProgress setResponseID( long val ) {
        ResponseID = val;
        return this;
    }

    /**  Task ID (Units: None)*/
    public long getTaskID() { return TaskID; }

    /**  Task ID (Units: None)*/
    public TaskProgress setTaskID( long val ) {
        TaskID = val;
        return this;
    }

    /**  Percentage of progress on task (Units: None)*/
    public float getPercentComplete() { return PercentComplete; }

    /**  Percentage of progress on task (Units: None)*/
    public TaskProgress setPercentComplete( float val ) {
        PercentComplete = val;
        return this;
    }

    public java.util.ArrayList<Long> getEntitiesEngaged() {
        return EntitiesEngaged;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 20; // accounts for primitive types
        
        size += 2 + 8 * EntitiesEngaged.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ResponseID = LMCPUtil.getInt64(in);

        TaskID = LMCPUtil.getInt64(in);

        PercentComplete = LMCPUtil.getReal32(in);

        EntitiesEngaged.clear();
        int EntitiesEngaged_len = LMCPUtil.getUint16(in);
        for(int i=0; i<EntitiesEngaged_len; i++){
            EntitiesEngaged.add(LMCPUtil.getInt64(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ResponseID);
        LMCPUtil.putInt64(out, TaskID);
        LMCPUtil.putReal32(out, PercentComplete);
        LMCPUtil.putUint16(out, EntitiesEngaged.size());
        for(int i=0; i<EntitiesEngaged.size(); i++){
            LMCPUtil.putInt64(out, EntitiesEngaged.get(i));
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
        buf.append( ws + "<TaskProgress Series=\"UXTASK\">\n");
        buf.append( ws + "  <ResponseID>" + String.valueOf(ResponseID) + "</ResponseID>\n");
        buf.append( ws + "  <TaskID>" + String.valueOf(TaskID) + "</TaskID>\n");
        buf.append( ws + "  <PercentComplete>" + String.valueOf(PercentComplete) + "</PercentComplete>\n");
        buf.append( ws + "  <EntitiesEngaged>\n");
        for (int i=0; i<EntitiesEngaged.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(EntitiesEngaged.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </EntitiesEngaged>\n");
        buf.append( ws + "</TaskProgress>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        TaskProgress o = (TaskProgress) anotherObj;
        if (ResponseID != o.ResponseID) return false;
        if (TaskID != o.TaskID) return false;
        if (PercentComplete != o.PercentComplete) return false;
         if (!EntitiesEngaged.equals( o.EntitiesEngaged)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)PercentComplete;

        return hash + super.hashCode();
    }
    
}
