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
         A set of parameter describing the operations of a vehicle.  This can be used to set routing preferences         for a particular vehicle.  The "Name" field can be used to describe a given condition, such as "cruise" or "climb".         
*/
public class FlightProfile extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 12;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "FlightProfile";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.FlightProfile";

    /**  The unique name for this configuration (Units: None)*/
    @LmcpType("string")
    protected String Name = "";
    /**  True Airspeed (Units: meter/sec)*/
    @LmcpType("real32")
    protected float Airspeed = (float)0;
    /**  The pitch angle of the aircraft in this flight condition(assuming zero bank) (Units: degree)*/
    @LmcpType("real32")
    protected float PitchAngle = (float)0;
    /**  Vertical speed (positive upwards) of the vehicle (Units: meter/sec)*/
    @LmcpType("real32")
    protected float VerticalSpeed = (float)0;
    /**  The maximum angle that this vehicle will bank (Units: degree)*/
    @LmcpType("real32")
    protected float MaxBankAngle = (float)0;
    /**  The consumption rate of available energy, expressed in terms of the percentage of maximum capacity used per second. (Units: %/sec)*/
    @LmcpType("real32")
    protected float EnergyRate = (float)0;

    
    public FlightProfile() {
    }

    public FlightProfile(String Name, float Airspeed, float PitchAngle, float VerticalSpeed, float MaxBankAngle, float EnergyRate){
        this.Name = Name;
        this.Airspeed = Airspeed;
        this.PitchAngle = PitchAngle;
        this.VerticalSpeed = VerticalSpeed;
        this.MaxBankAngle = MaxBankAngle;
        this.EnergyRate = EnergyRate;
    }


    public FlightProfile clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            FlightProfile newObj = new FlightProfile();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The unique name for this configuration (Units: None)*/
    public String getName() { return Name; }

    /**  The unique name for this configuration (Units: None)*/
    public FlightProfile setName( String val ) {
        Name = val;
        return this;
    }

    /**  True Airspeed (Units: meter/sec)*/
    public float getAirspeed() { return Airspeed; }

    /**  True Airspeed (Units: meter/sec)*/
    public FlightProfile setAirspeed( float val ) {
        Airspeed = val;
        return this;
    }

    /**  The pitch angle of the aircraft in this flight condition(assuming zero bank) (Units: degree)*/
    public float getPitchAngle() { return PitchAngle; }

    /**  The pitch angle of the aircraft in this flight condition(assuming zero bank) (Units: degree)*/
    public FlightProfile setPitchAngle( float val ) {
        PitchAngle = val;
        return this;
    }

    /**  Vertical speed (positive upwards) of the vehicle (Units: meter/sec)*/
    public float getVerticalSpeed() { return VerticalSpeed; }

    /**  Vertical speed (positive upwards) of the vehicle (Units: meter/sec)*/
    public FlightProfile setVerticalSpeed( float val ) {
        VerticalSpeed = val;
        return this;
    }

    /**  The maximum angle that this vehicle will bank (Units: degree)*/
    public float getMaxBankAngle() { return MaxBankAngle; }

    /**  The maximum angle that this vehicle will bank (Units: degree)*/
    public FlightProfile setMaxBankAngle( float val ) {
        MaxBankAngle = val;
        return this;
    }

    /**  The consumption rate of available energy, expressed in terms of the percentage of maximum capacity used per second. (Units: %/sec)*/
    public float getEnergyRate() { return EnergyRate; }

    /**  The consumption rate of available energy, expressed in terms of the percentage of maximum capacity used per second. (Units: %/sec)*/
    public FlightProfile setEnergyRate( float val ) {
        EnergyRate = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 20; // accounts for primitive types
        size += LMCPUtil.sizeOfString(Name);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        Name = LMCPUtil.getString(in);

        Airspeed = LMCPUtil.getReal32(in);

        PitchAngle = LMCPUtil.getReal32(in);

        VerticalSpeed = LMCPUtil.getReal32(in);

        MaxBankAngle = LMCPUtil.getReal32(in);

        EnergyRate = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putString(out, Name);
        LMCPUtil.putReal32(out, Airspeed);
        LMCPUtil.putReal32(out, PitchAngle);
        LMCPUtil.putReal32(out, VerticalSpeed);
        LMCPUtil.putReal32(out, MaxBankAngle);
        LMCPUtil.putReal32(out, EnergyRate);

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
        buf.append( ws + "<FlightProfile Series=\"CMASI\">\n");
        buf.append( ws + "  <Name>" + String.valueOf(Name) + "</Name>\n");
        buf.append( ws + "  <Airspeed>" + String.valueOf(Airspeed) + "</Airspeed>\n");
        buf.append( ws + "  <PitchAngle>" + String.valueOf(PitchAngle) + "</PitchAngle>\n");
        buf.append( ws + "  <VerticalSpeed>" + String.valueOf(VerticalSpeed) + "</VerticalSpeed>\n");
        buf.append( ws + "  <MaxBankAngle>" + String.valueOf(MaxBankAngle) + "</MaxBankAngle>\n");
        buf.append( ws + "  <EnergyRate>" + String.valueOf(EnergyRate) + "</EnergyRate>\n");
        buf.append( ws + "</FlightProfile>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        FlightProfile o = (FlightProfile) anotherObj;
        if (Name == null && o.Name != null) return false;
        if ( Name!= null && !Name.equals(o.Name)) return false;
        if (Airspeed != o.Airspeed) return false;
        if (PitchAngle != o.PitchAngle) return false;
        if (VerticalSpeed != o.VerticalSpeed) return false;
        if (MaxBankAngle != o.MaxBankAngle) return false;
        if (EnergyRate != o.EnergyRate) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)Airspeed;
        hash += 31 * (int)PitchAngle;
        hash += 31 * (int)VerticalSpeed;
        hash += 31 * (int)MaxBankAngle;

        return hash + super.hashCode();
    }
    
}
