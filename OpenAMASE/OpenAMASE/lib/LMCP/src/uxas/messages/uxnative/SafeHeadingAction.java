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
 Translate from heading or heading-rate to a single leading waypoint that respects airspace constraints 
*/
public class SafeHeadingAction extends afrl.cmasi.VehicleAction {
    
    public static final int LMCP_TYPE = 6;

    public static final String SERIES_NAME = "UXNATIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149751333668345413L;
    public static final int SERIES_VERSION = 9;


    private static final String TYPE_NAME = "SafeHeadingAction";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.uxnative.SafeHeadingAction";

    /**  ID of vehicle which will be commanded to a safe waypoint that approximates the desired heading (Units: None)*/
    @LmcpType("int64")
    protected long VehicleID = 0L;
    /**  Operating region ID indicating airspace constraints that must be respected (Units: None)*/
    @LmcpType("int64")
    protected long OperatingRegion = 0L;
    /**  Lead-ahead distance for waypoint placement (Units: meters)*/
    @LmcpType("real32")
    protected float LeadAheadDistance = (float)1000.0;
    /**  Loiter radius on lead-ahead waypoint. If zero or negative, uses calcuated minimum turn radius from vehicle configuration (Units: meters)*/
    @LmcpType("real32")
    protected float LoiterRadius = (float)0.0;
    /**  Desired heading that vehicle should attemtpt to reach in degress from true north (Units: degrees)*/
    @LmcpType("real32")
    protected float DesiredHeading = (float)0;
    /**  Desired heading rate for the vehicle (Units: degrees/sec)*/
    @LmcpType("real32")
    protected float DesiredHeadingRate = (float)0;
    /**  Flag indicating selecting between heading (false) or heading rate (true) commands (Units: None)*/
    @LmcpType("bool")
    protected boolean UseHeadingRate = false;
    /**  Commanded Altitude valid if {@link UseAltitude} == true (Units: meter)*/
    @LmcpType("real32")
    protected float Altitude = (float)0;
    /**  Altitude type for specified altitude (Units: None)*/
    @LmcpType("AltitudeType")
    protected afrl.cmasi.AltitudeType AltitudeType = afrl.cmasi.AltitudeType.MSL;
    /**  Denotes whether altitude should be used in the safe heading action. If false, the {@link NominalAltitude} and {@link NominalType} from the {@link CMASI/EntityConfiguration} will be used. (Units: None)*/
    @LmcpType("bool")
    protected boolean UseAltitude = false;
    /**  Commanded Speed valid if {@link UseSpeed} == true (Units: mps)*/
    @LmcpType("real32")
    protected float Speed = (float)0;
    /**  Denotes whether speed should be used in the safe heading action. If false, the {@link NominalSpeed} from the {@link CMASI/EntityConfiguration} will be used. (Units: None)*/
    @LmcpType("bool")
    protected boolean UseSpeed = false;

    
    public SafeHeadingAction() {
    }

    public SafeHeadingAction(long VehicleID, long OperatingRegion, float LeadAheadDistance, float LoiterRadius, float DesiredHeading, float DesiredHeadingRate, boolean UseHeadingRate, float Altitude, afrl.cmasi.AltitudeType AltitudeType, boolean UseAltitude, float Speed, boolean UseSpeed){
        this.VehicleID = VehicleID;
        this.OperatingRegion = OperatingRegion;
        this.LeadAheadDistance = LeadAheadDistance;
        this.LoiterRadius = LoiterRadius;
        this.DesiredHeading = DesiredHeading;
        this.DesiredHeadingRate = DesiredHeadingRate;
        this.UseHeadingRate = UseHeadingRate;
        this.Altitude = Altitude;
        this.AltitudeType = AltitudeType;
        this.UseAltitude = UseAltitude;
        this.Speed = Speed;
        this.UseSpeed = UseSpeed;
    }


    public SafeHeadingAction clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            SafeHeadingAction newObj = new SafeHeadingAction();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID of vehicle which will be commanded to a safe waypoint that approximates the desired heading (Units: None)*/
    public long getVehicleID() { return VehicleID; }

    /**  ID of vehicle which will be commanded to a safe waypoint that approximates the desired heading (Units: None)*/
    public SafeHeadingAction setVehicleID( long val ) {
        VehicleID = val;
        return this;
    }

    /**  Operating region ID indicating airspace constraints that must be respected (Units: None)*/
    public long getOperatingRegion() { return OperatingRegion; }

    /**  Operating region ID indicating airspace constraints that must be respected (Units: None)*/
    public SafeHeadingAction setOperatingRegion( long val ) {
        OperatingRegion = val;
        return this;
    }

    /**  Lead-ahead distance for waypoint placement (Units: meters)*/
    public float getLeadAheadDistance() { return LeadAheadDistance; }

    /**  Lead-ahead distance for waypoint placement (Units: meters)*/
    public SafeHeadingAction setLeadAheadDistance( float val ) {
        LeadAheadDistance = val;
        return this;
    }

    /**  Loiter radius on lead-ahead waypoint. If zero or negative, uses calcuated minimum turn radius from vehicle configuration (Units: meters)*/
    public float getLoiterRadius() { return LoiterRadius; }

    /**  Loiter radius on lead-ahead waypoint. If zero or negative, uses calcuated minimum turn radius from vehicle configuration (Units: meters)*/
    public SafeHeadingAction setLoiterRadius( float val ) {
        LoiterRadius = val;
        return this;
    }

    /**  Desired heading that vehicle should attemtpt to reach in degress from true north (Units: degrees)*/
    public float getDesiredHeading() { return DesiredHeading; }

    /**  Desired heading that vehicle should attemtpt to reach in degress from true north (Units: degrees)*/
    public SafeHeadingAction setDesiredHeading( float val ) {
        DesiredHeading = val;
        return this;
    }

    /**  Desired heading rate for the vehicle (Units: degrees/sec)*/
    public float getDesiredHeadingRate() { return DesiredHeadingRate; }

    /**  Desired heading rate for the vehicle (Units: degrees/sec)*/
    public SafeHeadingAction setDesiredHeadingRate( float val ) {
        DesiredHeadingRate = val;
        return this;
    }

    /**  Flag indicating selecting between heading (false) or heading rate (true) commands (Units: None)*/
    public boolean getUseHeadingRate() { return UseHeadingRate; }

    /**  Flag indicating selecting between heading (false) or heading rate (true) commands (Units: None)*/
    public SafeHeadingAction setUseHeadingRate( boolean val ) {
        UseHeadingRate = val;
        return this;
    }

    /**  Commanded Altitude valid if {@link UseAltitude} == true (Units: meter)*/
    public float getAltitude() { return Altitude; }

    /**  Commanded Altitude valid if {@link UseAltitude} == true (Units: meter)*/
    public SafeHeadingAction setAltitude( float val ) {
        Altitude = val;
        return this;
    }

    /**  Altitude type for specified altitude (Units: None)*/
    public afrl.cmasi.AltitudeType getAltitudeType() { return AltitudeType; }

    /**  Altitude type for specified altitude (Units: None)*/
    public SafeHeadingAction setAltitudeType( afrl.cmasi.AltitudeType val ) {
        AltitudeType = val;
        return this;
    }

    /**  Denotes whether altitude should be used in the safe heading action. If false, the {@link NominalAltitude} and {@link NominalType} from the {@link CMASI/EntityConfiguration} will be used. (Units: None)*/
    public boolean getUseAltitude() { return UseAltitude; }

    /**  Denotes whether altitude should be used in the safe heading action. If false, the {@link NominalAltitude} and {@link NominalType} from the {@link CMASI/EntityConfiguration} will be used. (Units: None)*/
    public SafeHeadingAction setUseAltitude( boolean val ) {
        UseAltitude = val;
        return this;
    }

    /**  Commanded Speed valid if {@link UseSpeed} == true (Units: mps)*/
    public float getSpeed() { return Speed; }

    /**  Commanded Speed valid if {@link UseSpeed} == true (Units: mps)*/
    public SafeHeadingAction setSpeed( float val ) {
        Speed = val;
        return this;
    }

    /**  Denotes whether speed should be used in the safe heading action. If false, the {@link NominalSpeed} from the {@link CMASI/EntityConfiguration} will be used. (Units: None)*/
    public boolean getUseSpeed() { return UseSpeed; }

    /**  Denotes whether speed should be used in the safe heading action. If false, the {@link NominalSpeed} from the {@link CMASI/EntityConfiguration} will be used. (Units: None)*/
    public SafeHeadingAction setUseSpeed( boolean val ) {
        UseSpeed = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 47; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        VehicleID = LMCPUtil.getInt64(in);

        OperatingRegion = LMCPUtil.getInt64(in);

        LeadAheadDistance = LMCPUtil.getReal32(in);

        LoiterRadius = LMCPUtil.getReal32(in);

        DesiredHeading = LMCPUtil.getReal32(in);

        DesiredHeadingRate = LMCPUtil.getReal32(in);

        UseHeadingRate = LMCPUtil.getBool(in);

        Altitude = LMCPUtil.getReal32(in);

        AltitudeType = afrl.cmasi.AltitudeType.unpack( in );

        UseAltitude = LMCPUtil.getBool(in);

        Speed = LMCPUtil.getReal32(in);

        UseSpeed = LMCPUtil.getBool(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, VehicleID);
        LMCPUtil.putInt64(out, OperatingRegion);
        LMCPUtil.putReal32(out, LeadAheadDistance);
        LMCPUtil.putReal32(out, LoiterRadius);
        LMCPUtil.putReal32(out, DesiredHeading);
        LMCPUtil.putReal32(out, DesiredHeadingRate);
        LMCPUtil.putBool(out, UseHeadingRate);
        LMCPUtil.putReal32(out, Altitude);
        AltitudeType.pack(out);
        LMCPUtil.putBool(out, UseAltitude);
        LMCPUtil.putReal32(out, Speed);
        LMCPUtil.putBool(out, UseSpeed);

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
        buf.append( ws + "<SafeHeadingAction Series=\"UXNATIVE\">\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <OperatingRegion>" + String.valueOf(OperatingRegion) + "</OperatingRegion>\n");
        buf.append( ws + "  <LeadAheadDistance>" + String.valueOf(LeadAheadDistance) + "</LeadAheadDistance>\n");
        buf.append( ws + "  <LoiterRadius>" + String.valueOf(LoiterRadius) + "</LoiterRadius>\n");
        buf.append( ws + "  <DesiredHeading>" + String.valueOf(DesiredHeading) + "</DesiredHeading>\n");
        buf.append( ws + "  <DesiredHeadingRate>" + String.valueOf(DesiredHeadingRate) + "</DesiredHeadingRate>\n");
        buf.append( ws + "  <UseHeadingRate>" + String.valueOf(UseHeadingRate) + "</UseHeadingRate>\n");
        buf.append( ws + "  <Altitude>" + String.valueOf(Altitude) + "</Altitude>\n");
        buf.append( ws + "  <AltitudeType>" + String.valueOf(AltitudeType) + "</AltitudeType>\n");
        buf.append( ws + "  <UseAltitude>" + String.valueOf(UseAltitude) + "</UseAltitude>\n");
        buf.append( ws + "  <Speed>" + String.valueOf(Speed) + "</Speed>\n");
        buf.append( ws + "  <UseSpeed>" + String.valueOf(UseSpeed) + "</UseSpeed>\n");
        buf.append( ws + "  <AssociatedTaskList>\n");
        for (int i=0; i<AssociatedTaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTaskList>\n");
        buf.append( ws + "</SafeHeadingAction>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        SafeHeadingAction o = (SafeHeadingAction) anotherObj;
        if (VehicleID != o.VehicleID) return false;
        if (OperatingRegion != o.OperatingRegion) return false;
        if (LeadAheadDistance != o.LeadAheadDistance) return false;
        if (LoiterRadius != o.LoiterRadius) return false;
        if (DesiredHeading != o.DesiredHeading) return false;
        if (DesiredHeadingRate != o.DesiredHeadingRate) return false;
        if (UseHeadingRate != o.UseHeadingRate) return false;
        if (Altitude != o.Altitude) return false;
        if (AltitudeType != o.AltitudeType) return false;
        if (UseAltitude != o.UseAltitude) return false;
        if (Speed != o.Speed) return false;
        if (UseSpeed != o.UseSpeed) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)LeadAheadDistance;
        hash += 31 * (int)LoiterRadius;
        hash += 31 * (int)DesiredHeading;

        return hash + super.hashCode();
    }
    
}
