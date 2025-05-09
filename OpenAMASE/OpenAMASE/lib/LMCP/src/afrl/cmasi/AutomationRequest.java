// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.cmasi;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
            This is the primary message for requesting action from the automation service. The automation service is to use the list of            vehicles, tasks (with associated task relationships), keep-out zones, and keep-in zones to produce {@link MissionCommands}            for the vehicles requested. The automation service should only produce commands for the vehicles requested, but is not            required to produce plans for all vehicles. If multiple {@link AutomationRequest} messages are sent to the automation service, only            the last message will be considered, i.e. a new plan request supercedes all previous ones.        
*/
public class AutomationRequest extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 40;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "AutomationRequest";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.AutomationRequest";

    /**  List of entities to consider when planning. (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> EntityList = new java.util.ArrayList<Long>();
    /**  list of task IDs (defined by {@link Task} messages) to be planned for by the automation service (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> TaskList = new java.util.ArrayList<Long>();
    /**  string containing the relationship between requested tasks. If empty, all tasks are to be completed in any order. The format of the string is specific to the automation service. This relationship string is the mechanism for incorporating task precedence, priority, timing, etc. (Units: None)*/
    @LmcpType("string")
    protected String TaskRelationships = "";
    /**  Operating region ID to be considered during planning (Units: None)*/
    @LmcpType("int64")
    protected long OperatingRegion = 0L;
    /**  Denotes that that the planner should restart any tasks that have been performed or are currently being performed. This is useful in situations when a task request contains tasks that have been requested previously, and the operator wishes to restart the mission plans from the beginning. (Units: None)*/
    @LmcpType("bool")
    protected boolean RedoAllTasks = false;

    
    public AutomationRequest() {
    }

    public AutomationRequest(String TaskRelationships, long OperatingRegion, boolean RedoAllTasks){
        this.TaskRelationships = TaskRelationships;
        this.OperatingRegion = OperatingRegion;
        this.RedoAllTasks = RedoAllTasks;
    }


    public AutomationRequest clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            AutomationRequest newObj = new AutomationRequest();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public java.util.ArrayList<Long> getEntityList() {
        return EntityList;
    }

    public java.util.ArrayList<Long> getTaskList() {
        return TaskList;
    }

    /**  string containing the relationship between requested tasks. If empty, all tasks are to be completed in any order. The format of the string is specific to the automation service. This relationship string is the mechanism for incorporating task precedence, priority, timing, etc. (Units: None)*/
    public String getTaskRelationships() { return TaskRelationships; }

    /**  string containing the relationship between requested tasks. If empty, all tasks are to be completed in any order. The format of the string is specific to the automation service. This relationship string is the mechanism for incorporating task precedence, priority, timing, etc. (Units: None)*/
    public AutomationRequest setTaskRelationships( String val ) {
        TaskRelationships = val;
        return this;
    }

    /**  Operating region ID to be considered during planning (Units: None)*/
    public long getOperatingRegion() { return OperatingRegion; }

    /**  Operating region ID to be considered during planning (Units: None)*/
    public AutomationRequest setOperatingRegion( long val ) {
        OperatingRegion = val;
        return this;
    }

    /**  Denotes that that the planner should restart any tasks that have been performed or are currently being performed. This is useful in situations when a task request contains tasks that have been requested previously, and the operator wishes to restart the mission plans from the beginning. (Units: None)*/
    public boolean getRedoAllTasks() { return RedoAllTasks; }

    /**  Denotes that that the planner should restart any tasks that have been performed or are currently being performed. This is useful in situations when a task request contains tasks that have been requested previously, and the operator wishes to restart the mission plans from the beginning. (Units: None)*/
    public AutomationRequest setRedoAllTasks( boolean val ) {
        RedoAllTasks = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 9; // accounts for primitive types
        
        size += 2 + 8 * EntityList.size();
        
        size += 2 + 8 * TaskList.size();
        size += LMCPUtil.sizeOfString(TaskRelationships);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        EntityList.clear();
        int EntityList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<EntityList_len; i++){
            EntityList.add(LMCPUtil.getInt64(in));
        }
        TaskList.clear();
        int TaskList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<TaskList_len; i++){
            TaskList.add(LMCPUtil.getInt64(in));
        }
        TaskRelationships = LMCPUtil.getString(in);

        OperatingRegion = LMCPUtil.getInt64(in);

        RedoAllTasks = LMCPUtil.getBool(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putUint16(out, EntityList.size());
        for(int i=0; i<EntityList.size(); i++){
            LMCPUtil.putInt64(out, EntityList.get(i));
        }
        LMCPUtil.putUint16(out, TaskList.size());
        for(int i=0; i<TaskList.size(); i++){
            LMCPUtil.putInt64(out, TaskList.get(i));
        }
        LMCPUtil.putString(out, TaskRelationships);
        LMCPUtil.putInt64(out, OperatingRegion);
        LMCPUtil.putBool(out, RedoAllTasks);

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
        buf.append( ws + "<AutomationRequest Series=\"CMASI\">\n");
        buf.append( ws + "  <EntityList>\n");
        for (int i=0; i<EntityList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(EntityList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </EntityList>\n");
        buf.append( ws + "  <TaskList>\n");
        for (int i=0; i<TaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(TaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </TaskList>\n");
        buf.append( ws + "  <TaskRelationships>" + String.valueOf(TaskRelationships) + "</TaskRelationships>\n");
        buf.append( ws + "  <OperatingRegion>" + String.valueOf(OperatingRegion) + "</OperatingRegion>\n");
        buf.append( ws + "  <RedoAllTasks>" + String.valueOf(RedoAllTasks) + "</RedoAllTasks>\n");
        buf.append( ws + "</AutomationRequest>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        AutomationRequest o = (AutomationRequest) anotherObj;
         if (!EntityList.equals( o.EntityList)) return false;
         if (!TaskList.equals( o.TaskList)) return false;
        if (TaskRelationships == null && o.TaskRelationships != null) return false;
        if ( TaskRelationships!= null && !TaskRelationships.equals(o.TaskRelationships)) return false;
        if (OperatingRegion != o.OperatingRegion) return false;
        if (RedoAllTasks != o.RedoAllTasks) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
