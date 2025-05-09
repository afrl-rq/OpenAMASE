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
 Optional task progress query 
*/
public class TaskProgressRequest extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 25;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "TaskProgressRequest";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.TaskProgressRequest";

    /**  Task progress request ID for properly matching corresponding response (Units: None)*/
    @LmcpType("int64")
    protected long RequestID = 0L;
    /**  Task ID to report current progress (Units: None)*/
    @LmcpType("int64")
    protected long TaskID = 0L;

    
    public TaskProgressRequest() {
    }

    public TaskProgressRequest(long RequestID, long TaskID){
        this.RequestID = RequestID;
        this.TaskID = TaskID;
    }


    public TaskProgressRequest clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            TaskProgressRequest newObj = new TaskProgressRequest();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Task progress request ID for properly matching corresponding response (Units: None)*/
    public long getRequestID() { return RequestID; }

    /**  Task progress request ID for properly matching corresponding response (Units: None)*/
    public TaskProgressRequest setRequestID( long val ) {
        RequestID = val;
        return this;
    }

    /**  Task ID to report current progress (Units: None)*/
    public long getTaskID() { return TaskID; }

    /**  Task ID to report current progress (Units: None)*/
    public TaskProgressRequest setTaskID( long val ) {
        TaskID = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        RequestID = LMCPUtil.getInt64(in);

        TaskID = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, RequestID);
        LMCPUtil.putInt64(out, TaskID);

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
        buf.append( ws + "<TaskProgressRequest Series=\"UXTASK\">\n");
        buf.append( ws + "  <RequestID>" + String.valueOf(RequestID) + "</RequestID>\n");
        buf.append( ws + "  <TaskID>" + String.valueOf(TaskID) + "</TaskID>\n");
        buf.append( ws + "</TaskProgressRequest>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        TaskProgressRequest o = (TaskProgressRequest) anotherObj;
        if (RequestID != o.RequestID) return false;
        if (TaskID != o.TaskID) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
