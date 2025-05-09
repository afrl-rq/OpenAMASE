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
 Summary information that describes a particular way to carry out the task.        In general, multiple task summaries for each task will be sent corresponding to        each eligible vehicle completing the task. In the case where a task is completed        by multiple vehicles simultaneously, then each TaskSummary will include the set of        vehicles that would be used to complete the task. 
*/
public class TaskSummary extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 14;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "TaskSummary";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.TaskSummary";

    /**  ID of task in consideration (Units: None)*/
    @LmcpType("int64")
    protected long TaskID = 0L;
    /**  Summary of information for each vehicle used simultaneously to complete this task. If there are zero performing vehicles, then 'TaskID' cannot be completed with any set of available vehicles in the system. (Units: None)*/
    @LmcpType("VehicleSummary")
    protected java.util.ArrayList<afrl.impact.VehicleSummary> PerformingVehicles = new java.util.ArrayList<afrl.impact.VehicleSummary>();
    /**  Task cannot be completed as specified and is using best effort. Estimated percent achieved while using best effort strategy. (Units: %)*/
    @LmcpType("real32")
    protected float BestEffort = (float)100.0;

    
    public TaskSummary() {
    }

    public TaskSummary(long TaskID, float BestEffort){
        this.TaskID = TaskID;
        this.BestEffort = BestEffort;
    }


    public TaskSummary clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            TaskSummary newObj = new TaskSummary();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID of task in consideration (Units: None)*/
    public long getTaskID() { return TaskID; }

    /**  ID of task in consideration (Units: None)*/
    public TaskSummary setTaskID( long val ) {
        TaskID = val;
        return this;
    }

    public java.util.ArrayList<afrl.impact.VehicleSummary> getPerformingVehicles() {
        return PerformingVehicles;
    }

    /**  Task cannot be completed as specified and is using best effort. Estimated percent achieved while using best effort strategy. (Units: %)*/
    public float getBestEffort() { return BestEffort; }

    /**  Task cannot be completed as specified and is using best effort. Estimated percent achieved while using best effort strategy. (Units: %)*/
    public TaskSummary setBestEffort( float val ) {
        BestEffort = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 12; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(PerformingVehicles);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        TaskID = LMCPUtil.getInt64(in);

        PerformingVehicles.clear();
        int PerformingVehicles_len = LMCPUtil.getUint16(in);
        for(int i=0; i<PerformingVehicles_len; i++){
        PerformingVehicles.add( (afrl.impact.VehicleSummary) LMCPUtil.getObject(in));
        }
        BestEffort = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, TaskID);
        LMCPUtil.putUint16(out, PerformingVehicles.size());
        for(int i=0; i<PerformingVehicles.size(); i++){
            LMCPUtil.putObject(out, PerformingVehicles.get(i));
        }
        LMCPUtil.putReal32(out, BestEffort);

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
        buf.append( ws + "<TaskSummary Series=\"IMPACT\">\n");
        buf.append( ws + "  <TaskID>" + String.valueOf(TaskID) + "</TaskID>\n");
        buf.append( ws + "  <PerformingVehicles>\n");
        for (int i=0; i<PerformingVehicles.size(); i++) {
            buf.append( PerformingVehicles.get(i) == null ? ( ws + "    <null/>\n") : (PerformingVehicles.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </PerformingVehicles>\n");
        buf.append( ws + "  <BestEffort>" + String.valueOf(BestEffort) + "</BestEffort>\n");
        buf.append( ws + "</TaskSummary>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        TaskSummary o = (TaskSummary) anotherObj;
        if (TaskID != o.TaskID) return false;
         if (!PerformingVehicles.equals( o.PerformingVehicles)) return false;
        if (BestEffort != o.BestEffort) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)BestEffort;

        return hash + super.hashCode();
    }
    
}
