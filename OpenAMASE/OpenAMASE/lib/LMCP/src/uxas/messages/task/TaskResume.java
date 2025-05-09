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
 Resumes execution of a task from it's paused state 
*/
public class TaskResume extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 23;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "TaskResume";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.TaskResume";

    /**  Task ID (Units: None)*/
    @LmcpType("int64")
    protected long TaskID = 0L;
    /**  Instead of resuming from paused task state, re-start the complete task (Units: None)*/
    @LmcpType("bool")
    protected boolean RestartCompletely = false;
    /**  Instead of resuming from paused task state with the previously assigned entities, resume task with new assignment options. Only used when not null. (Units: None)*/
    @LmcpType("TaskAssignment")
    protected uxas.messages.task.TaskAssignment ReAssign = null;

    
    public TaskResume() {
    }

    public TaskResume(long TaskID, boolean RestartCompletely, uxas.messages.task.TaskAssignment ReAssign){
        this.TaskID = TaskID;
        this.RestartCompletely = RestartCompletely;
        this.ReAssign = ReAssign;
    }


    public TaskResume clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            TaskResume newObj = new TaskResume();
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
    public TaskResume setTaskID( long val ) {
        TaskID = val;
        return this;
    }

    /**  Instead of resuming from paused task state, re-start the complete task (Units: None)*/
    public boolean getRestartCompletely() { return RestartCompletely; }

    /**  Instead of resuming from paused task state, re-start the complete task (Units: None)*/
    public TaskResume setRestartCompletely( boolean val ) {
        RestartCompletely = val;
        return this;
    }

    /**  Instead of resuming from paused task state with the previously assigned entities, resume task with new assignment options. Only used when not null. (Units: None)*/
    public uxas.messages.task.TaskAssignment getReAssign() { return ReAssign; }

    /**  Instead of resuming from paused task state with the previously assigned entities, resume task with new assignment options. Only used when not null. (Units: None)*/
    public TaskResume setReAssign( uxas.messages.task.TaskAssignment val ) {
        ReAssign = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 9; // accounts for primitive types
        size += LMCPUtil.sizeOf(ReAssign);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        TaskID = LMCPUtil.getInt64(in);

        RestartCompletely = LMCPUtil.getBool(in);

            ReAssign = (uxas.messages.task.TaskAssignment) LMCPUtil.getObject(in);

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, TaskID);
        LMCPUtil.putBool(out, RestartCompletely);
        LMCPUtil.putObject(out, ReAssign);

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
        buf.append( ws + "<TaskResume Series=\"UXTASK\">\n");
        buf.append( ws + "  <TaskID>" + String.valueOf(TaskID) + "</TaskID>\n");
        buf.append( ws + "  <RestartCompletely>" + String.valueOf(RestartCompletely) + "</RestartCompletely>\n");
        if (ReAssign!= null){
           buf.append( ws + "  <ReAssign>\n");
           buf.append( ( ReAssign.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </ReAssign>\n");
        }
        buf.append( ws + "</TaskResume>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        TaskResume o = (TaskResume) anotherObj;
        if (TaskID != o.TaskID) return false;
        if (RestartCompletely != o.RestartCompletely) return false;
        if (ReAssign == null && o.ReAssign != null) return false;
        if ( ReAssign!= null && !ReAssign.equals(o.ReAssign)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
