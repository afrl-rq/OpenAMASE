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
 Task cancel message 
*/
public class CancelTask extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 29;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "CancelTask";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.CancelTask";

    /**  List of vehicle IDs that are directed to cancel tasks. If empty, all vehicles currently on a listed task will be directed to cancel. (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> Vehicles = new java.util.ArrayList<Long>();
    /**  Task ID(s) to be removed from task lists of indicated vehicles. If empty, then only the current task is removed. (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> CanceledTasks = new java.util.ArrayList<Long>();

    
    public CancelTask() {
    }



    public CancelTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            CancelTask newObj = new CancelTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public java.util.ArrayList<Long> getVehicles() {
        return Vehicles;
    }

    public java.util.ArrayList<Long> getCanceledTasks() {
        return CanceledTasks;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types
        
        size += 2 + 8 * Vehicles.size();
        
        size += 2 + 8 * CanceledTasks.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        Vehicles.clear();
        int Vehicles_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Vehicles_len; i++){
            Vehicles.add(LMCPUtil.getInt64(in));
        }
        CanceledTasks.clear();
        int CanceledTasks_len = LMCPUtil.getUint16(in);
        for(int i=0; i<CanceledTasks_len; i++){
            CanceledTasks.add(LMCPUtil.getInt64(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putUint16(out, Vehicles.size());
        for(int i=0; i<Vehicles.size(); i++){
            LMCPUtil.putInt64(out, Vehicles.get(i));
        }
        LMCPUtil.putUint16(out, CanceledTasks.size());
        for(int i=0; i<CanceledTasks.size(); i++){
            LMCPUtil.putInt64(out, CanceledTasks.get(i));
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
        buf.append( ws + "<CancelTask Series=\"UXTASK\">\n");
        buf.append( ws + "  <Vehicles>\n");
        for (int i=0; i<Vehicles.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(Vehicles.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </Vehicles>\n");
        buf.append( ws + "  <CanceledTasks>\n");
        for (int i=0; i<CanceledTasks.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(CanceledTasks.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </CanceledTasks>\n");
        buf.append( ws + "</CancelTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        CancelTask o = (CancelTask) anotherObj;
         if (!Vehicles.equals( o.Vehicles)) return false;
         if (!CanceledTasks.equals( o.CanceledTasks)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
