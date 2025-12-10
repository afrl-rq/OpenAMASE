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
 A command to "direct" the aircraft on a particular vector. This can be passed directly to the            autopilot, or be directed to the operator as the current command to follow. 
*/
public class FlightDirectorAction extends afrl.cmasi.NavigationAction {
    
    public static final int LMCP_TYPE = 54;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "FlightDirectorAction";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.FlightDirectorAction";

    /**  Speed to fly (Units: meter/sec)*/
    @LmcpType("real32")
    protected float Speed = (float)0;
    /**  type of speed to fly (Units: None)*/
    @LmcpType("SpeedType")
    protected afrl.cmasi.SpeedType SpeedType = afrl.cmasi.SpeedType.Airspeed;
    /**  True heading to fly (Units: degree)*/
    @LmcpType("real32")
    protected float Heading = (float)0;
    /**  Altitude to maintain. (Units: meter)*/
    @LmcpType("real32")
    protected float Altitude = (float)0;
    /**  Altitude type for specified altitude (Units: None)*/
    @LmcpType("AltitudeType")
    protected afrl.cmasi.AltitudeType AltitudeType = afrl.cmasi.AltitudeType.MSL;
    /**  target climb/descent rate for changing altitude (Units: meter/sec)*/
    @LmcpType("real32")
    protected float ClimbRate = (float)0;

    
    public FlightDirectorAction() {
    }

    public FlightDirectorAction(float Speed, afrl.cmasi.SpeedType SpeedType, float Heading, float Altitude, afrl.cmasi.AltitudeType AltitudeType, float ClimbRate){
        this.Speed = Speed;
        this.SpeedType = SpeedType;
        this.Heading = Heading;
        this.Altitude = Altitude;
        this.AltitudeType = AltitudeType;
        this.ClimbRate = ClimbRate;
    }


    public FlightDirectorAction clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            FlightDirectorAction newObj = new FlightDirectorAction();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Speed to fly (Units: meter/sec)*/
    public float getSpeed() { return Speed; }

    /**  Speed to fly (Units: meter/sec)*/
    public FlightDirectorAction setSpeed( float val ) {
        Speed = val;
        return this;
    }

    /**  type of speed to fly (Units: None)*/
    public afrl.cmasi.SpeedType getSpeedType() { return SpeedType; }

    /**  type of speed to fly (Units: None)*/
    public FlightDirectorAction setSpeedType( afrl.cmasi.SpeedType val ) {
        SpeedType = val;
        return this;
    }

    /**  True heading to fly (Units: degree)*/
    public float getHeading() { return Heading; }

    /**  True heading to fly (Units: degree)*/
    public FlightDirectorAction setHeading( float val ) {
        Heading = val;
        return this;
    }

    /**  Altitude to maintain. (Units: meter)*/
    public float getAltitude() { return Altitude; }

    /**  Altitude to maintain. (Units: meter)*/
    public FlightDirectorAction setAltitude( float val ) {
        Altitude = val;
        return this;
    }

    /**  Altitude type for specified altitude (Units: None)*/
    public afrl.cmasi.AltitudeType getAltitudeType() { return AltitudeType; }

    /**  Altitude type for specified altitude (Units: None)*/
    public FlightDirectorAction setAltitudeType( afrl.cmasi.AltitudeType val ) {
        AltitudeType = val;
        return this;
    }

    /**  target climb/descent rate for changing altitude (Units: meter/sec)*/
    public float getClimbRate() { return ClimbRate; }

    /**  target climb/descent rate for changing altitude (Units: meter/sec)*/
    public FlightDirectorAction setClimbRate( float val ) {
        ClimbRate = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 24; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        Speed = LMCPUtil.getReal32(in);

        SpeedType = afrl.cmasi.SpeedType.unpack( in );

        Heading = LMCPUtil.getReal32(in);

        Altitude = LMCPUtil.getReal32(in);

        AltitudeType = afrl.cmasi.AltitudeType.unpack( in );

        ClimbRate = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putReal32(out, Speed);
        SpeedType.pack(out);
        LMCPUtil.putReal32(out, Heading);
        LMCPUtil.putReal32(out, Altitude);
        AltitudeType.pack(out);
        LMCPUtil.putReal32(out, ClimbRate);

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
        buf.append( ws + "<FlightDirectorAction Series=\"CMASI\">\n");
        buf.append( ws + "  <Speed>" + String.valueOf(Speed) + "</Speed>\n");
        buf.append( ws + "  <SpeedType>" + String.valueOf(SpeedType) + "</SpeedType>\n");
        buf.append( ws + "  <Heading>" + String.valueOf(Heading) + "</Heading>\n");
        buf.append( ws + "  <Altitude>" + String.valueOf(Altitude) + "</Altitude>\n");
        buf.append( ws + "  <AltitudeType>" + String.valueOf(AltitudeType) + "</AltitudeType>\n");
        buf.append( ws + "  <ClimbRate>" + String.valueOf(ClimbRate) + "</ClimbRate>\n");
        buf.append( ws + "  <AssociatedTaskList>\n");
        for (int i=0; i<AssociatedTaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTaskList>\n");
        buf.append( ws + "</FlightDirectorAction>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        FlightDirectorAction o = (FlightDirectorAction) anotherObj;
        if (Speed != o.Speed) return false;
        if (SpeedType != o.SpeedType) return false;
        if (Heading != o.Heading) return false;
        if (Altitude != o.Altitude) return false;
        if (AltitudeType != o.AltitudeType) return false;
        if (ClimbRate != o.ClimbRate) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)Speed;
        hash += 31 * (int)Heading;
        hash += 31 * (int)Altitude;

        return hash + super.hashCode();
    }
    
}
