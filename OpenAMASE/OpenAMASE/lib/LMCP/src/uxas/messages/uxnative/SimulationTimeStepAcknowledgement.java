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
 Message for indicating that UxAS has handled an EntityState update             which allows an external simulation to pause until received 
*/
public class SimulationTimeStepAcknowledgement extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 16;

    public static final String SERIES_NAME = "UXNATIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149751333668345413L;
    public static final int SERIES_VERSION = 9;


    private static final String TYPE_NAME = "SimulationTimeStepAcknowledgement";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.uxnative.SimulationTimeStepAcknowledgement";

    /**  Vehicle for which the entity state update was handled (Units: None)*/
    @LmcpType("int64")
    protected long VehicleID = 0L;
    /**  Associated time from the handled entity state (Units: milliseconds)*/
    @LmcpType("int64")
    protected long ReportedTime = 0L;

    
    public SimulationTimeStepAcknowledgement() {
    }

    public SimulationTimeStepAcknowledgement(long VehicleID, long ReportedTime){
        this.VehicleID = VehicleID;
        this.ReportedTime = ReportedTime;
    }


    public SimulationTimeStepAcknowledgement clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            SimulationTimeStepAcknowledgement newObj = new SimulationTimeStepAcknowledgement();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Vehicle for which the entity state update was handled (Units: None)*/
    public long getVehicleID() { return VehicleID; }

    /**  Vehicle for which the entity state update was handled (Units: None)*/
    public SimulationTimeStepAcknowledgement setVehicleID( long val ) {
        VehicleID = val;
        return this;
    }

    /**  Associated time from the handled entity state (Units: milliseconds)*/
    public long getReportedTime() { return ReportedTime; }

    /**  Associated time from the handled entity state (Units: milliseconds)*/
    public SimulationTimeStepAcknowledgement setReportedTime( long val ) {
        ReportedTime = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        VehicleID = LMCPUtil.getInt64(in);

        ReportedTime = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, VehicleID);
        LMCPUtil.putInt64(out, ReportedTime);

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
        buf.append( ws + "<SimulationTimeStepAcknowledgement Series=\"UXNATIVE\">\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <ReportedTime>" + String.valueOf(ReportedTime) + "</ReportedTime>\n");
        buf.append( ws + "</SimulationTimeStepAcknowledgement>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        SimulationTimeStepAcknowledgement o = (SimulationTimeStepAcknowledgement) anotherObj;
        if (VehicleID != o.VehicleID) return false;
        if (ReportedTime != o.ReportedTime) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
