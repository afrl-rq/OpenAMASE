// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages.uxnative;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Command entity to execute subtasks either in strict order or determined by optimization.        Replaces all current sub-tasks being executed. In addtion to executing the sub-tasks, the        mission command used to complete the sub-tasks is returned. 
*/
public class SubTaskExecution extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 10;

    public static final String SERIES_NAME = "UXNATIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149751333668345413L;
    public static final int SERIES_VERSION = 9;


    private static final String TYPE_NAME = "SubTaskExecution";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.uxnative.SubTaskExecution";

    /**  List of tasks that current entity should complete (Units: None)*/
    @LmcpType("Task")
    protected java.util.ArrayList<afrl.cmasi.Task> SubTasks = new java.util.ArrayList<afrl.cmasi.Task>();
    /**  Enable strict ordering (Units: None)*/
    @LmcpType("bool")
    protected boolean StrictOrder = false;

    
    public SubTaskExecution() {
    }

    public SubTaskExecution(boolean StrictOrder){
        this.StrictOrder = StrictOrder;
    }


    public SubTaskExecution clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            SubTaskExecution newObj = new SubTaskExecution();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public java.util.ArrayList<afrl.cmasi.Task> getSubTasks() {
        return SubTasks;
    }

    /**  Enable strict ordering (Units: None)*/
    public boolean getStrictOrder() { return StrictOrder; }

    /**  Enable strict ordering (Units: None)*/
    public SubTaskExecution setStrictOrder( boolean val ) {
        StrictOrder = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 1; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(SubTasks);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        SubTasks.clear();
        int SubTasks_len = LMCPUtil.getUint16(in);
        for(int i=0; i<SubTasks_len; i++){
        SubTasks.add( (afrl.cmasi.Task) LMCPUtil.getObject(in));
        }
        StrictOrder = LMCPUtil.getBool(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putUint16(out, SubTasks.size());
        for(int i=0; i<SubTasks.size(); i++){
            LMCPUtil.putObject(out, SubTasks.get(i));
        }
        LMCPUtil.putBool(out, StrictOrder);

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
        buf.append( ws + "<SubTaskExecution Series=\"UXNATIVE\">\n");
        buf.append( ws + "  <SubTasks>\n");
        for (int i=0; i<SubTasks.size(); i++) {
            buf.append( SubTasks.get(i) == null ? ( ws + "    <null/>\n") : (SubTasks.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </SubTasks>\n");
        buf.append( ws + "  <StrictOrder>" + String.valueOf(StrictOrder) + "</StrictOrder>\n");
        buf.append( ws + "</SubTaskExecution>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        SubTaskExecution o = (SubTaskExecution) anotherObj;
         if (!SubTasks.equals( o.SubTasks)) return false;
        if (StrictOrder != o.StrictOrder) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
