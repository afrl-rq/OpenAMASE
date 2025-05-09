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
         Base class for an action to be taken by a vehicle.  This is usually used as an object inside of         {@link Waypoint} to describe the action that a vehicle is to take upon reaching a waypoint.  This is         used by child types to perform something meaningful.<br/>         
*/
public class VehicleAction extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 7;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "VehicleAction";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.VehicleAction";

    /**  A list of tasks that are associated with this action. A length of zero denotes no associated tasks. This field is for analysis purposes. The automation service should associate a list of tasks with each action to enable analysis of the allocation of tasks to vehicles.
    *<br/> (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> AssociatedTaskList = new java.util.ArrayList<Long>();

    
    public VehicleAction() {
    }



    public VehicleAction clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            VehicleAction newObj = new VehicleAction();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public java.util.ArrayList<Long> getAssociatedTaskList() {
        return AssociatedTaskList;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types
        
        size += 2 + 8 * AssociatedTaskList.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        AssociatedTaskList.clear();
        int AssociatedTaskList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<AssociatedTaskList_len; i++){
            AssociatedTaskList.add(LMCPUtil.getInt64(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putUint16(out, AssociatedTaskList.size());
        for(int i=0; i<AssociatedTaskList.size(); i++){
            LMCPUtil.putInt64(out, AssociatedTaskList.get(i));
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
        buf.append( ws + "<VehicleAction Series=\"CMASI\">\n");
        buf.append( ws + "  <AssociatedTaskList>\n");
        for (int i=0; i<AssociatedTaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTaskList>\n");
        buf.append( ws + "</VehicleAction>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        VehicleAction o = (VehicleAction) anotherObj;
         if (!AssociatedTaskList.equals( o.AssociatedTaskList)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
