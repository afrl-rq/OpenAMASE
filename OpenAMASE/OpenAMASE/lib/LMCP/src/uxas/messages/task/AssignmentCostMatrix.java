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
 Matrix of costs consisting of:        (1) costs from initial vehicle positions to all task options; and        (2) costs from each task option to every other task option 
*/
public class AssignmentCostMatrix extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 16;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "AssignmentCostMatrix";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.AssignmentCostMatrix";

    /**  ID that matches this cost matrix with the appropriate unique automation request (Units: None)*/
    @LmcpType("int64")
    protected long CorrespondingAutomationRequestID = 0L;
    /**  Over-arching task relationship description (directly from automation request). A process algebra string with only task IDs. (Units: None)*/
    @LmcpType("string")
    protected String TaskLevelRelationship = "";
    /**  List of all tasks that this cost matrix includes (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> TaskList = new java.util.ArrayList<Long>();
    /**  Operating region that was used during matrix calculation (Units: None)*/
    @LmcpType("int64")
    protected long OperatingRegion = 0L;
    /**  Set of task-to-task timings for each requested vehicle. Assume 'T' max tasks [16], 'O' max options per task [8], 'V' max vehicles [16]: then max number of elements in matrix is 'V*T*O + (T*O)^2' [18432] (Units: None)*/
    @LmcpType("TaskOptionCost")
    protected java.util.ArrayList<uxas.messages.task.TaskOptionCost> CostMatrix = new java.util.ArrayList<uxas.messages.task.TaskOptionCost>();

    
    public AssignmentCostMatrix() {
    }

    public AssignmentCostMatrix(long CorrespondingAutomationRequestID, String TaskLevelRelationship, long OperatingRegion){
        this.CorrespondingAutomationRequestID = CorrespondingAutomationRequestID;
        this.TaskLevelRelationship = TaskLevelRelationship;
        this.OperatingRegion = OperatingRegion;
    }


    public AssignmentCostMatrix clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            AssignmentCostMatrix newObj = new AssignmentCostMatrix();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID that matches this cost matrix with the appropriate unique automation request (Units: None)*/
    public long getCorrespondingAutomationRequestID() { return CorrespondingAutomationRequestID; }

    /**  ID that matches this cost matrix with the appropriate unique automation request (Units: None)*/
    public AssignmentCostMatrix setCorrespondingAutomationRequestID( long val ) {
        CorrespondingAutomationRequestID = val;
        return this;
    }

    /**  Over-arching task relationship description (directly from automation request). A process algebra string with only task IDs. (Units: None)*/
    public String getTaskLevelRelationship() { return TaskLevelRelationship; }

    /**  Over-arching task relationship description (directly from automation request). A process algebra string with only task IDs. (Units: None)*/
    public AssignmentCostMatrix setTaskLevelRelationship( String val ) {
        TaskLevelRelationship = val;
        return this;
    }

    public java.util.ArrayList<Long> getTaskList() {
        return TaskList;
    }

    /**  Operating region that was used during matrix calculation (Units: None)*/
    public long getOperatingRegion() { return OperatingRegion; }

    /**  Operating region that was used during matrix calculation (Units: None)*/
    public AssignmentCostMatrix setOperatingRegion( long val ) {
        OperatingRegion = val;
        return this;
    }

    public java.util.ArrayList<uxas.messages.task.TaskOptionCost> getCostMatrix() {
        return CostMatrix;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types
        size += LMCPUtil.sizeOfString(TaskLevelRelationship);
        
        size += 2 + 8 * TaskList.size();
        size += 2;
        size += LMCPUtil.sizeOfList(CostMatrix);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        CorrespondingAutomationRequestID = LMCPUtil.getInt64(in);

        TaskLevelRelationship = LMCPUtil.getString(in);

        TaskList.clear();
        int TaskList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<TaskList_len; i++){
            TaskList.add(LMCPUtil.getInt64(in));
        }
        OperatingRegion = LMCPUtil.getInt64(in);

        CostMatrix.clear();
        int CostMatrix_len = LMCPUtil.getUint16(in);
        for(int i=0; i<CostMatrix_len; i++){
        CostMatrix.add( (uxas.messages.task.TaskOptionCost) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, CorrespondingAutomationRequestID);
        LMCPUtil.putString(out, TaskLevelRelationship);
        LMCPUtil.putUint16(out, TaskList.size());
        for(int i=0; i<TaskList.size(); i++){
            LMCPUtil.putInt64(out, TaskList.get(i));
        }
        LMCPUtil.putInt64(out, OperatingRegion);
        LMCPUtil.putUint16(out, CostMatrix.size());
        for(int i=0; i<CostMatrix.size(); i++){
            LMCPUtil.putObject(out, CostMatrix.get(i));
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
        buf.append( ws + "<AssignmentCostMatrix Series=\"UXTASK\">\n");
        buf.append( ws + "  <CorrespondingAutomationRequestID>" + String.valueOf(CorrespondingAutomationRequestID) + "</CorrespondingAutomationRequestID>\n");
        buf.append( ws + "  <TaskLevelRelationship>" + String.valueOf(TaskLevelRelationship) + "</TaskLevelRelationship>\n");
        buf.append( ws + "  <TaskList>\n");
        for (int i=0; i<TaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(TaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </TaskList>\n");
        buf.append( ws + "  <OperatingRegion>" + String.valueOf(OperatingRegion) + "</OperatingRegion>\n");
        buf.append( ws + "  <CostMatrix>\n");
        for (int i=0; i<CostMatrix.size(); i++) {
            buf.append( CostMatrix.get(i) == null ? ( ws + "    <null/>\n") : (CostMatrix.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </CostMatrix>\n");
        buf.append( ws + "</AssignmentCostMatrix>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        AssignmentCostMatrix o = (AssignmentCostMatrix) anotherObj;
        if (CorrespondingAutomationRequestID != o.CorrespondingAutomationRequestID) return false;
        if (TaskLevelRelationship == null && o.TaskLevelRelationship != null) return false;
        if ( TaskLevelRelationship!= null && !TaskLevelRelationship.equals(o.TaskLevelRelationship)) return false;
         if (!TaskList.equals( o.TaskList)) return false;
        if (OperatingRegion != o.OperatingRegion) return false;
         if (!CostMatrix.equals( o.CostMatrix)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
