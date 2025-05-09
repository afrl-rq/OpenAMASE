// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.cmasi.perceive;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
             This puts an entity into a "track" mode, where the vehicle commands itself and its onboard sensors to attempt to             keep a target in view. If multiple sensors are to be used simultaneously, multiple track actions should be sent.        
*/
public class TrackEntityAction extends afrl.cmasi.VehicleAction {
    
    public static final int LMCP_TYPE = 2;

    public static final String SERIES_NAME = "PERCEIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5784119745305990725L;
    public static final int SERIES_VERSION = 1;


    private static final String TYPE_NAME = "TrackEntityAction";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.perceive.TrackEntityAction";

    /**  The unique identifier of the target to be tracked. (Units: None)*/
    @LmcpType("uint32")
    protected long EntityID = 0L;
    /**  The unique identifier of the sensor that is to be actively steered to track the target. (Units: None)*/
    @LmcpType("uint32")
    protected long SensorID = 0L;
    /**  The waypoint that this vehicle is to return to when complete (or lost track) (Units: None)*/
    @LmcpType("uint32")
    protected long ReturnToWaypoint = 0L;

    
    public TrackEntityAction() {
    }

    public TrackEntityAction(long EntityID, long SensorID, long ReturnToWaypoint){
        this.EntityID = EntityID;
        this.SensorID = SensorID;
        this.ReturnToWaypoint = ReturnToWaypoint;
    }


    public TrackEntityAction clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            TrackEntityAction newObj = new TrackEntityAction();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The unique identifier of the target to be tracked. (Units: None)*/
    public long getEntityID() { return EntityID; }

    /**  The unique identifier of the target to be tracked. (Units: None)*/
    public TrackEntityAction setEntityID( long val ) {
        EntityID = val;
        return this;
    }

    /**  The unique identifier of the sensor that is to be actively steered to track the target. (Units: None)*/
    public long getSensorID() { return SensorID; }

    /**  The unique identifier of the sensor that is to be actively steered to track the target. (Units: None)*/
    public TrackEntityAction setSensorID( long val ) {
        SensorID = val;
        return this;
    }

    /**  The waypoint that this vehicle is to return to when complete (or lost track) (Units: None)*/
    public long getReturnToWaypoint() { return ReturnToWaypoint; }

    /**  The waypoint that this vehicle is to return to when complete (or lost track) (Units: None)*/
    public TrackEntityAction setReturnToWaypoint( long val ) {
        ReturnToWaypoint = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 12; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        EntityID = LMCPUtil.getUint32(in);

        SensorID = LMCPUtil.getUint32(in);

        ReturnToWaypoint = LMCPUtil.getUint32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putUint32(out, EntityID);
        LMCPUtil.putUint32(out, SensorID);
        LMCPUtil.putUint32(out, ReturnToWaypoint);

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
        buf.append( ws + "<TrackEntityAction Series=\"PERCEIVE\">\n");
        buf.append( ws + "  <EntityID>" + String.valueOf(EntityID) + "</EntityID>\n");
        buf.append( ws + "  <SensorID>" + String.valueOf(SensorID) + "</SensorID>\n");
        buf.append( ws + "  <ReturnToWaypoint>" + String.valueOf(ReturnToWaypoint) + "</ReturnToWaypoint>\n");
        buf.append( ws + "  <AssociatedTaskList>\n");
        for (int i=0; i<AssociatedTaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTaskList>\n");
        buf.append( ws + "</TrackEntityAction>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        TrackEntityAction o = (TrackEntityAction) anotherObj;
        if (EntityID != o.EntityID) return false;
        if (SensorID != o.SensorID) return false;
        if (ReturnToWaypoint != o.ReturnToWaypoint) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)EntityID;
        hash += 31 * (int)SensorID;
        hash += 31 * (int)ReturnToWaypoint;

        return hash + super.hashCode();
    }
    
}
