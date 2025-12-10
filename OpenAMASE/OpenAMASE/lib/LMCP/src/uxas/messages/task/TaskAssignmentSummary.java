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
 A completed assignment consisting of an <b>ordered</b> list of tasks 
*/
public class TaskAssignmentSummary extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 19;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "TaskAssignmentSummary";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.TaskAssignmentSummary";

    /**  ID that matches this summary with the appropriate unique automation request (Units: None)*/
    @LmcpType("int64")
    protected long CorrespondingAutomationRequestID = 0L;
    /**  Operating region which was considered during this assignment (Units: None)*/
    @LmcpType("int64")
    protected long OperatingRegion = 0L;
    /**  Ordered list of tasks to be completed (Units: None)*/
    @LmcpType("TaskAssignment")
    protected java.util.ArrayList<uxas.messages.task.TaskAssignment> TaskList = new java.util.ArrayList<uxas.messages.task.TaskAssignment>();

    
    public TaskAssignmentSummary() {
    }

    public TaskAssignmentSummary(long CorrespondingAutomationRequestID, long OperatingRegion){
        this.CorrespondingAutomationRequestID = CorrespondingAutomationRequestID;
        this.OperatingRegion = OperatingRegion;
    }


    public TaskAssignmentSummary clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            TaskAssignmentSummary newObj = new TaskAssignmentSummary();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID that matches this summary with the appropriate unique automation request (Units: None)*/
    public long getCorrespondingAutomationRequestID() { return CorrespondingAutomationRequestID; }

    /**  ID that matches this summary with the appropriate unique automation request (Units: None)*/
    public TaskAssignmentSummary setCorrespondingAutomationRequestID( long val ) {
        CorrespondingAutomationRequestID = val;
        return this;
    }

    /**  Operating region which was considered during this assignment (Units: None)*/
    public long getOperatingRegion() { return OperatingRegion; }

    /**  Operating region which was considered during this assignment (Units: None)*/
    public TaskAssignmentSummary setOperatingRegion( long val ) {
        OperatingRegion = val;
        return this;
    }

    public java.util.ArrayList<uxas.messages.task.TaskAssignment> getTaskList() {
        return TaskList;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(TaskList);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        CorrespondingAutomationRequestID = LMCPUtil.getInt64(in);

        OperatingRegion = LMCPUtil.getInt64(in);

        TaskList.clear();
        int TaskList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<TaskList_len; i++){
        TaskList.add( (uxas.messages.task.TaskAssignment) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, CorrespondingAutomationRequestID);
        LMCPUtil.putInt64(out, OperatingRegion);
        LMCPUtil.putUint16(out, TaskList.size());
        for(int i=0; i<TaskList.size(); i++){
            LMCPUtil.putObject(out, TaskList.get(i));
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
        buf.append( ws + "<TaskAssignmentSummary Series=\"UXTASK\">\n");
        buf.append( ws + "  <CorrespondingAutomationRequestID>" + String.valueOf(CorrespondingAutomationRequestID) + "</CorrespondingAutomationRequestID>\n");
        buf.append( ws + "  <OperatingRegion>" + String.valueOf(OperatingRegion) + "</OperatingRegion>\n");
        buf.append( ws + "  <TaskList>\n");
        for (int i=0; i<TaskList.size(); i++) {
            buf.append( TaskList.get(i) == null ? ( ws + "    <null/>\n") : (TaskList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </TaskList>\n");
        buf.append( ws + "</TaskAssignmentSummary>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        TaskAssignmentSummary o = (TaskAssignmentSummary) anotherObj;
        if (CorrespondingAutomationRequestID != o.CorrespondingAutomationRequestID) return false;
        if (OperatingRegion != o.OperatingRegion) return false;
         if (!TaskList.equals( o.TaskList)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
