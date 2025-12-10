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
 Batch summary request whereby vehicle-to-task information is requested 
*/
public class BatchSummaryRequest extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 12;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "BatchSummaryRequest";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.BatchSummaryRequest";

    /**  Request ID for correlating with response (Units: None)*/
    @LmcpType("int64")
    protected long RequestID = 0L;
    /**  An array of vehicles (by ID) to consider when planning (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> Vehicles = new java.util.ArrayList<Long>();
    /**  List of task IDs to be planned to (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> TaskList = new java.util.ArrayList<Long>();
    /**  string containing the relationship between requested tasks. If empty, all tasks are to be completed in any order. The format of the string is specific to the automation service. This relationship string is the mechanism for incorporating task precedence, priority, timing, etc. (Units: None)*/
    @LmcpType("string")
    protected String TaskRelationships = "";
    /**  List of task percentages along task to plan from (Units: None)*/
    @LmcpType("real32")
    protected java.util.ArrayList<Float> InterTaskPercentage = new java.util.ArrayList<Float>();
    /**  Operating region to be considered during planning (Units: None)*/
    @LmcpType("int64")
    protected long OperatingRegion = 0L;

    
    public BatchSummaryRequest() {
    }

    public BatchSummaryRequest(long RequestID, String TaskRelationships, long OperatingRegion){
        this.RequestID = RequestID;
        this.TaskRelationships = TaskRelationships;
        this.OperatingRegion = OperatingRegion;
    }


    public BatchSummaryRequest clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            BatchSummaryRequest newObj = new BatchSummaryRequest();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Request ID for correlating with response (Units: None)*/
    public long getRequestID() { return RequestID; }

    /**  Request ID for correlating with response (Units: None)*/
    public BatchSummaryRequest setRequestID( long val ) {
        RequestID = val;
        return this;
    }

    public java.util.ArrayList<Long> getVehicles() {
        return Vehicles;
    }

    public java.util.ArrayList<Long> getTaskList() {
        return TaskList;
    }

    /**  string containing the relationship between requested tasks. If empty, all tasks are to be completed in any order. The format of the string is specific to the automation service. This relationship string is the mechanism for incorporating task precedence, priority, timing, etc. (Units: None)*/
    public String getTaskRelationships() { return TaskRelationships; }

    /**  string containing the relationship between requested tasks. If empty, all tasks are to be completed in any order. The format of the string is specific to the automation service. This relationship string is the mechanism for incorporating task precedence, priority, timing, etc. (Units: None)*/
    public BatchSummaryRequest setTaskRelationships( String val ) {
        TaskRelationships = val;
        return this;
    }

    public java.util.ArrayList<Float> getInterTaskPercentage() {
        return InterTaskPercentage;
    }

    /**  Operating region to be considered during planning (Units: None)*/
    public long getOperatingRegion() { return OperatingRegion; }

    /**  Operating region to be considered during planning (Units: None)*/
    public BatchSummaryRequest setOperatingRegion( long val ) {
        OperatingRegion = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types
        
        size += 2 + 8 * Vehicles.size();
        
        size += 2 + 8 * TaskList.size();
        size += LMCPUtil.sizeOfString(TaskRelationships);
        
        size += 2 + 4 * InterTaskPercentage.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        RequestID = LMCPUtil.getInt64(in);

        Vehicles.clear();
        int Vehicles_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Vehicles_len; i++){
            Vehicles.add(LMCPUtil.getInt64(in));
        }
        TaskList.clear();
        int TaskList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<TaskList_len; i++){
            TaskList.add(LMCPUtil.getInt64(in));
        }
        TaskRelationships = LMCPUtil.getString(in);

        InterTaskPercentage.clear();
        int InterTaskPercentage_len = LMCPUtil.getUint16(in);
        for(int i=0; i<InterTaskPercentage_len; i++){
            InterTaskPercentage.add(LMCPUtil.getReal32(in));
        }
        OperatingRegion = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, RequestID);
        LMCPUtil.putUint16(out, Vehicles.size());
        for(int i=0; i<Vehicles.size(); i++){
            LMCPUtil.putInt64(out, Vehicles.get(i));
        }
        LMCPUtil.putUint16(out, TaskList.size());
        for(int i=0; i<TaskList.size(); i++){
            LMCPUtil.putInt64(out, TaskList.get(i));
        }
        LMCPUtil.putString(out, TaskRelationships);
        LMCPUtil.putUint16(out, InterTaskPercentage.size());
        for(int i=0; i<InterTaskPercentage.size(); i++){
            LMCPUtil.putReal32(out, InterTaskPercentage.get(i));
        }
        LMCPUtil.putInt64(out, OperatingRegion);

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
        buf.append( ws + "<BatchSummaryRequest Series=\"IMPACT\">\n");
        buf.append( ws + "  <RequestID>" + String.valueOf(RequestID) + "</RequestID>\n");
        buf.append( ws + "  <Vehicles>\n");
        for (int i=0; i<Vehicles.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(Vehicles.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </Vehicles>\n");
        buf.append( ws + "  <TaskList>\n");
        for (int i=0; i<TaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(TaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </TaskList>\n");
        buf.append( ws + "  <TaskRelationships>" + String.valueOf(TaskRelationships) + "</TaskRelationships>\n");
        buf.append( ws + "  <InterTaskPercentage>\n");
        for (int i=0; i<InterTaskPercentage.size(); i++) {
        buf.append( ws + "  <real32>" + String.valueOf(InterTaskPercentage.get(i)) + "</real32>\n");
        }
        buf.append( ws + "  </InterTaskPercentage>\n");
        buf.append( ws + "  <OperatingRegion>" + String.valueOf(OperatingRegion) + "</OperatingRegion>\n");
        buf.append( ws + "</BatchSummaryRequest>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        BatchSummaryRequest o = (BatchSummaryRequest) anotherObj;
        if (RequestID != o.RequestID) return false;
         if (!Vehicles.equals( o.Vehicles)) return false;
         if (!TaskList.equals( o.TaskList)) return false;
        if (TaskRelationships == null && o.TaskRelationships != null) return false;
        if ( TaskRelationships!= null && !TaskRelationships.equals(o.TaskRelationships)) return false;
         if (!InterTaskPercentage.equals( o.InterTaskPercentage)) return false;
        if (OperatingRegion != o.OperatingRegion) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
