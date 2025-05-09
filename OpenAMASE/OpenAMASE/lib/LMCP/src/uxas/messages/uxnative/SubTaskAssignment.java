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
 Command an entity to perform a complete task assignment for the entire team and then execute own role 
*/
public class SubTaskAssignment extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 11;

    public static final String SERIES_NAME = "UXNATIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149751333668345413L;
    public static final int SERIES_VERSION = 9;


    private static final String TYPE_NAME = "SubTaskAssignment";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.uxnative.SubTaskAssignment";

    /**  List of tasks that current team should complete (Units: None)*/
    @LmcpType("Task")
    protected java.util.ArrayList<afrl.cmasi.Task> SubTasks = new java.util.ArrayList<afrl.cmasi.Task>();
    /**  Neighboring entities to consider (Units: None)*/
    @LmcpType("EntityState")
    protected java.util.ArrayList<afrl.cmasi.EntityState> Neighbors = new java.util.ArrayList<afrl.cmasi.EntityState>();

    
    public SubTaskAssignment() {
    }



    public SubTaskAssignment clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            SubTaskAssignment newObj = new SubTaskAssignment();
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

    public java.util.ArrayList<afrl.cmasi.EntityState> getNeighbors() {
        return Neighbors;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(SubTasks);
        size += 2;
        size += LMCPUtil.sizeOfList(Neighbors);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        SubTasks.clear();
        int SubTasks_len = LMCPUtil.getUint16(in);
        for(int i=0; i<SubTasks_len; i++){
        SubTasks.add( (afrl.cmasi.Task) LMCPUtil.getObject(in));
        }
        Neighbors.clear();
        int Neighbors_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Neighbors_len; i++){
        Neighbors.add( (afrl.cmasi.EntityState) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putUint16(out, SubTasks.size());
        for(int i=0; i<SubTasks.size(); i++){
            LMCPUtil.putObject(out, SubTasks.get(i));
        }
        LMCPUtil.putUint16(out, Neighbors.size());
        for(int i=0; i<Neighbors.size(); i++){
            LMCPUtil.putObject(out, Neighbors.get(i));
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
        buf.append( ws + "<SubTaskAssignment Series=\"UXNATIVE\">\n");
        buf.append( ws + "  <SubTasks>\n");
        for (int i=0; i<SubTasks.size(); i++) {
            buf.append( SubTasks.get(i) == null ? ( ws + "    <null/>\n") : (SubTasks.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </SubTasks>\n");
        buf.append( ws + "  <Neighbors>\n");
        for (int i=0; i<Neighbors.size(); i++) {
            buf.append( Neighbors.get(i) == null ? ( ws + "    <null/>\n") : (Neighbors.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Neighbors>\n");
        buf.append( ws + "</SubTaskAssignment>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        SubTaskAssignment o = (SubTaskAssignment) anotherObj;
         if (!SubTasks.equals( o.SubTasks)) return false;
         if (!Neighbors.equals( o.Neighbors)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
