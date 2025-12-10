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
 Data structure for associating a vehicle to a particular altitude and speed 
*/
public class SpeedAltPair extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 16;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "SpeedAltPair";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.SpeedAltPair";

    /**  ID of vehicle for which altitude and speed is attached (Units: None)*/
    @LmcpType("int64")
    protected long VehicleID = 0L;
    /**  ID of task for which altitude and speed is attached. If TaskID is zero, then alt/speed setting applies to all tasks in the automation request (Units: None)*/
    @LmcpType("int64")
    protected long TaskID = 0L;
    /**  The speed attached to this vehicle (Units: meter/sec)*/
    @LmcpType("real32")
    protected float Speed = (float)0;
    /**  Altitude attached to this vehicle (Units: meter)*/
    @LmcpType("real32")
    protected float Altitude = (float)0;
    /**  Altitude type for specified altitude (Units: None)*/
    @LmcpType("AltitudeType")
    protected afrl.cmasi.AltitudeType AltitudeType = afrl.cmasi.AltitudeType.AGL;

    
    public SpeedAltPair() {
    }

    public SpeedAltPair(long VehicleID, long TaskID, float Speed, float Altitude, afrl.cmasi.AltitudeType AltitudeType){
        this.VehicleID = VehicleID;
        this.TaskID = TaskID;
        this.Speed = Speed;
        this.Altitude = Altitude;
        this.AltitudeType = AltitudeType;
    }


    public SpeedAltPair clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            SpeedAltPair newObj = new SpeedAltPair();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID of vehicle for which altitude and speed is attached (Units: None)*/
    public long getVehicleID() { return VehicleID; }

    /**  ID of vehicle for which altitude and speed is attached (Units: None)*/
    public SpeedAltPair setVehicleID( long val ) {
        VehicleID = val;
        return this;
    }

    /**  ID of task for which altitude and speed is attached. If TaskID is zero, then alt/speed setting applies to all tasks in the automation request (Units: None)*/
    public long getTaskID() { return TaskID; }

    /**  ID of task for which altitude and speed is attached. If TaskID is zero, then alt/speed setting applies to all tasks in the automation request (Units: None)*/
    public SpeedAltPair setTaskID( long val ) {
        TaskID = val;
        return this;
    }

    /**  The speed attached to this vehicle (Units: meter/sec)*/
    public float getSpeed() { return Speed; }

    /**  The speed attached to this vehicle (Units: meter/sec)*/
    public SpeedAltPair setSpeed( float val ) {
        Speed = val;
        return this;
    }

    /**  Altitude attached to this vehicle (Units: meter)*/
    public float getAltitude() { return Altitude; }

    /**  Altitude attached to this vehicle (Units: meter)*/
    public SpeedAltPair setAltitude( float val ) {
        Altitude = val;
        return this;
    }

    /**  Altitude type for specified altitude (Units: None)*/
    public afrl.cmasi.AltitudeType getAltitudeType() { return AltitudeType; }

    /**  Altitude type for specified altitude (Units: None)*/
    public SpeedAltPair setAltitudeType( afrl.cmasi.AltitudeType val ) {
        AltitudeType = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 28; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        VehicleID = LMCPUtil.getInt64(in);

        TaskID = LMCPUtil.getInt64(in);

        Speed = LMCPUtil.getReal32(in);

        Altitude = LMCPUtil.getReal32(in);

        AltitudeType = afrl.cmasi.AltitudeType.unpack( in );


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, VehicleID);
        LMCPUtil.putInt64(out, TaskID);
        LMCPUtil.putReal32(out, Speed);
        LMCPUtil.putReal32(out, Altitude);
        AltitudeType.pack(out);

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
        buf.append( ws + "<SpeedAltPair Series=\"IMPACT\">\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <TaskID>" + String.valueOf(TaskID) + "</TaskID>\n");
        buf.append( ws + "  <Speed>" + String.valueOf(Speed) + "</Speed>\n");
        buf.append( ws + "  <Altitude>" + String.valueOf(Altitude) + "</Altitude>\n");
        buf.append( ws + "  <AltitudeType>" + String.valueOf(AltitudeType) + "</AltitudeType>\n");
        buf.append( ws + "</SpeedAltPair>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        SpeedAltPair o = (SpeedAltPair) anotherObj;
        if (VehicleID != o.VehicleID) return false;
        if (TaskID != o.TaskID) return false;
        if (Speed != o.Speed) return false;
        if (Altitude != o.Altitude) return false;
        if (AltitudeType != o.AltitudeType) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)Speed;
        hash += 31 * (int)Altitude;

        return hash + super.hashCode();
    }
    
}
