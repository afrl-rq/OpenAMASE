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
 Overrides speed commands from waypoints 
*/
public class SpeedOverrideAction extends afrl.cmasi.VehicleAction {
    
    public static final int LMCP_TYPE = 17;

    public static final String SERIES_NAME = "UXNATIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149751333668345413L;
    public static final int SERIES_VERSION = 9;


    private static final String TYPE_NAME = "SpeedOverrideAction";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.uxnative.SpeedOverrideAction";

    /**  ID of vehicle for which speed should be overridden (Units: None)*/
    @LmcpType("int64")
    protected long VehicleID = 0L;
    /**  Commanded Speed (Units: mps)*/
    @LmcpType("real32")
    protected float Speed = (float)0;

    
    public SpeedOverrideAction() {
    }

    public SpeedOverrideAction(long VehicleID, float Speed){
        this.VehicleID = VehicleID;
        this.Speed = Speed;
    }


    public SpeedOverrideAction clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            SpeedOverrideAction newObj = new SpeedOverrideAction();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID of vehicle for which speed should be overridden (Units: None)*/
    public long getVehicleID() { return VehicleID; }

    /**  ID of vehicle for which speed should be overridden (Units: None)*/
    public SpeedOverrideAction setVehicleID( long val ) {
        VehicleID = val;
        return this;
    }

    /**  Commanded Speed (Units: mps)*/
    public float getSpeed() { return Speed; }

    /**  Commanded Speed (Units: mps)*/
    public SpeedOverrideAction setSpeed( float val ) {
        Speed = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 12; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        VehicleID = LMCPUtil.getInt64(in);

        Speed = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, VehicleID);
        LMCPUtil.putReal32(out, Speed);

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
        buf.append( ws + "<SpeedOverrideAction Series=\"UXNATIVE\">\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <Speed>" + String.valueOf(Speed) + "</Speed>\n");
        buf.append( ws + "  <AssociatedTaskList>\n");
        for (int i=0; i<AssociatedTaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTaskList>\n");
        buf.append( ws + "</SpeedOverrideAction>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        SpeedOverrideAction o = (SpeedOverrideAction) anotherObj;
        if (VehicleID != o.VehicleID) return false;
        if (Speed != o.Speed) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)Speed;

        return hash + super.hashCode();
    }
    
}
