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
 Indicator for use of an IMPACT payload 
*/
public class DeployImpactPayload extends afrl.cmasi.VehicleAction {
    
    public static final int LMCP_TYPE = 7;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "DeployImpactPayload";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.DeployImpactPayload";

    /**  IMPACT vehicle from which to deploy payload (Units: None)*/
    @LmcpType("int64")
    protected long VehicleID = 0L;
    /**  Payload to deploy (Units: None)*/
    @LmcpType("ImpactPayloadType")
    protected afrl.impact.ImpactPayloadType DeployedPayload = afrl.impact.ImpactPayloadType.Unknown;
    /**  Entity to which the payload will be deployed. If zero, then payload is assumed deployed at the current position of the deploying vehicle. (Units: None)*/
    @LmcpType("int64")
    protected long TargetEntityID = 0L;

    
    public DeployImpactPayload() {
    }

    public DeployImpactPayload(long VehicleID, afrl.impact.ImpactPayloadType DeployedPayload, long TargetEntityID){
        this.VehicleID = VehicleID;
        this.DeployedPayload = DeployedPayload;
        this.TargetEntityID = TargetEntityID;
    }


    public DeployImpactPayload clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            DeployImpactPayload newObj = new DeployImpactPayload();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  IMPACT vehicle from which to deploy payload (Units: None)*/
    public long getVehicleID() { return VehicleID; }

    /**  IMPACT vehicle from which to deploy payload (Units: None)*/
    public DeployImpactPayload setVehicleID( long val ) {
        VehicleID = val;
        return this;
    }

    /**  Payload to deploy (Units: None)*/
    public afrl.impact.ImpactPayloadType getDeployedPayload() { return DeployedPayload; }

    /**  Payload to deploy (Units: None)*/
    public DeployImpactPayload setDeployedPayload( afrl.impact.ImpactPayloadType val ) {
        DeployedPayload = val;
        return this;
    }

    /**  Entity to which the payload will be deployed. If zero, then payload is assumed deployed at the current position of the deploying vehicle. (Units: None)*/
    public long getTargetEntityID() { return TargetEntityID; }

    /**  Entity to which the payload will be deployed. If zero, then payload is assumed deployed at the current position of the deploying vehicle. (Units: None)*/
    public DeployImpactPayload setTargetEntityID( long val ) {
        TargetEntityID = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 20; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        VehicleID = LMCPUtil.getInt64(in);

        DeployedPayload = afrl.impact.ImpactPayloadType.unpack( in );

        TargetEntityID = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, VehicleID);
        DeployedPayload.pack(out);
        LMCPUtil.putInt64(out, TargetEntityID);

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
        buf.append( ws + "<DeployImpactPayload Series=\"IMPACT\">\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <DeployedPayload>" + String.valueOf(DeployedPayload) + "</DeployedPayload>\n");
        buf.append( ws + "  <TargetEntityID>" + String.valueOf(TargetEntityID) + "</TargetEntityID>\n");
        buf.append( ws + "  <AssociatedTaskList>\n");
        for (int i=0; i<AssociatedTaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTaskList>\n");
        buf.append( ws + "</DeployImpactPayload>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        DeployImpactPayload o = (DeployImpactPayload) anotherObj;
        if (VehicleID != o.VehicleID) return false;
        if (DeployedPayload != o.DeployedPayload) return false;
        if (TargetEntityID != o.TargetEntityID) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
