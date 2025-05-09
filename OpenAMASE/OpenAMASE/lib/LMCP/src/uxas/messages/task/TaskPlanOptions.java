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
 Summary of available options to complete this task 
*/
public class TaskPlanOptions extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 21;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "TaskPlanOptions";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.TaskPlanOptions";

    /**  ID that matches this message with the appropriate unique automation request (Units: None)*/
    @LmcpType("int64")
    protected long CorrespondingAutomationRequestID = 0L;
    /**  Task ID (Units: None)*/
    @LmcpType("int64")
    protected long TaskID = 0L;
    /**  Process algebra string encoding all of the different options (Units: None)*/
    @LmcpType("string")
    protected String Composition = "";
    /**  List of options. NOTE for computational reasons, the larger the number of options, the much greater the computation (Units: None)*/
    @LmcpType("TaskOption")
    protected java.util.ArrayList<uxas.messages.task.TaskOption> Options = new java.util.ArrayList<uxas.messages.task.TaskOption>();

    
    public TaskPlanOptions() {
    }

    public TaskPlanOptions(long CorrespondingAutomationRequestID, long TaskID, String Composition){
        this.CorrespondingAutomationRequestID = CorrespondingAutomationRequestID;
        this.TaskID = TaskID;
        this.Composition = Composition;
    }


    public TaskPlanOptions clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            TaskPlanOptions newObj = new TaskPlanOptions();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID that matches this message with the appropriate unique automation request (Units: None)*/
    public long getCorrespondingAutomationRequestID() { return CorrespondingAutomationRequestID; }

    /**  ID that matches this message with the appropriate unique automation request (Units: None)*/
    public TaskPlanOptions setCorrespondingAutomationRequestID( long val ) {
        CorrespondingAutomationRequestID = val;
        return this;
    }

    /**  Task ID (Units: None)*/
    public long getTaskID() { return TaskID; }

    /**  Task ID (Units: None)*/
    public TaskPlanOptions setTaskID( long val ) {
        TaskID = val;
        return this;
    }

    /**  Process algebra string encoding all of the different options (Units: None)*/
    public String getComposition() { return Composition; }

    /**  Process algebra string encoding all of the different options (Units: None)*/
    public TaskPlanOptions setComposition( String val ) {
        Composition = val;
        return this;
    }

    public java.util.ArrayList<uxas.messages.task.TaskOption> getOptions() {
        return Options;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types
        size += LMCPUtil.sizeOfString(Composition);
        size += 2;
        size += LMCPUtil.sizeOfList(Options);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        CorrespondingAutomationRequestID = LMCPUtil.getInt64(in);

        TaskID = LMCPUtil.getInt64(in);

        Composition = LMCPUtil.getString(in);

        Options.clear();
        int Options_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Options_len; i++){
        Options.add( (uxas.messages.task.TaskOption) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, CorrespondingAutomationRequestID);
        LMCPUtil.putInt64(out, TaskID);
        LMCPUtil.putString(out, Composition);
        LMCPUtil.putUint16(out, Options.size());
        for(int i=0; i<Options.size(); i++){
            LMCPUtil.putObject(out, Options.get(i));
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
        buf.append( ws + "<TaskPlanOptions Series=\"UXTASK\">\n");
        buf.append( ws + "  <CorrespondingAutomationRequestID>" + String.valueOf(CorrespondingAutomationRequestID) + "</CorrespondingAutomationRequestID>\n");
        buf.append( ws + "  <TaskID>" + String.valueOf(TaskID) + "</TaskID>\n");
        buf.append( ws + "  <Composition>" + String.valueOf(Composition) + "</Composition>\n");
        buf.append( ws + "  <Options>\n");
        for (int i=0; i<Options.size(); i++) {
            buf.append( Options.get(i) == null ? ( ws + "    <null/>\n") : (Options.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Options>\n");
        buf.append( ws + "</TaskPlanOptions>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        TaskPlanOptions o = (TaskPlanOptions) anotherObj;
        if (CorrespondingAutomationRequestID != o.CorrespondingAutomationRequestID) return false;
        if (TaskID != o.TaskID) return false;
        if (Composition == null && o.Composition != null) return false;
        if ( Composition!= null && !Composition.equals(o.Composition)) return false;
         if (!Options.equals( o.Options)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
