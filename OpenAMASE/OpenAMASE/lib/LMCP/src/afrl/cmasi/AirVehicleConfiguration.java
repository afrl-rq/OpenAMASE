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
            Provides information regarding a vehicle's configuration items.        
*/
public class AirVehicleConfiguration extends afrl.cmasi.EntityConfiguration {
    
    public static final int LMCP_TYPE = 13;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "AirVehicleConfiguration";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.AirVehicleConfiguration";

    /**  The minimum true airspeed that should be commanded for this aircraft (Units: meter/sec)*/
    @LmcpType("real32")
    protected float MinimumSpeed = (float)0;
    /**  The maximum true airspeed that should be commanded for this aircraft (Units: meter/sec)*/
    @LmcpType("real32")
    protected float MaximumSpeed = (float)0;
    /**  Describes the preferred operating mode for most planning purposes. This can be a max range or endurance speed profile, or any other operating point chosen by a user. This field may not be null. (Units: None)*/
    @LmcpType("FlightProfile")
    protected afrl.cmasi.FlightProfile NominalFlightProfile = new afrl.cmasi.FlightProfile();
    /**  A list of all flight configurations described for this vehicle. Each flight configuration specifies a different mode of flight, for instance: climb, cruise, dash, loiter. This list may be empty, and does not necessarily contain the nominal flight configuration. (Units: None)*/
    @LmcpType("FlightProfile")
    protected java.util.ArrayList<afrl.cmasi.FlightProfile> AlternateFlightProfiles = new java.util.ArrayList<afrl.cmasi.FlightProfile>();
    /**  A list of available loiter types for this aircraft (Units: None)*/
    @LmcpType("LoiterType")
    protected java.util.ArrayList<afrl.cmasi.LoiterType> AvailableLoiterTypes = new java.util.ArrayList<afrl.cmasi.LoiterType>();
    /**  A list of available turning modes for this aircraft (Units: None)*/
    @LmcpType("TurnType")
    protected java.util.ArrayList<afrl.cmasi.TurnType> AvailableTurnTypes = new java.util.ArrayList<afrl.cmasi.TurnType>();
    /**  Minimum MSL altitude that this aircraft is allowed to fly. This value should be treated as a regulatory or safety-of-flight parameter and therefore takes precedence over other requests. (Units: meter)*/
    @LmcpType("real32")
    protected float MinimumAltitude = (float)0;
    /**  Altitude type for min altitude (Units: None)*/
    @LmcpType("AltitudeType")
    protected afrl.cmasi.AltitudeType MinAltitudeType = afrl.cmasi.AltitudeType.AGL;
    /**  Maximum MSL altitude that this aircraft is allowed to fly. This value should be treated as a regulatory or safety-of-flight parameter and therefore takes precedence over other requests. (Units: meter)*/
    @LmcpType("real32")
    protected float MaximumAltitude = (float)1000000;
    /**  Altitude type for max altitude (Units: None)*/
    @LmcpType("AltitudeType")
    protected afrl.cmasi.AltitudeType MaxAltitudeType = afrl.cmasi.AltitudeType.MSL;

    
    public AirVehicleConfiguration() {
    }

    public AirVehicleConfiguration(long ID, String Affiliation, String EntityType, String Label, float NominalSpeed, float NominalAltitude, afrl.cmasi.AltitudeType NominalAltitudeType, float MinimumSpeed, float MaximumSpeed, afrl.cmasi.FlightProfile NominalFlightProfile, float MinimumAltitude, afrl.cmasi.AltitudeType MinAltitudeType, float MaximumAltitude, afrl.cmasi.AltitudeType MaxAltitudeType){
        this.ID = ID;
        this.Affiliation = Affiliation;
        this.EntityType = EntityType;
        this.Label = Label;
        this.NominalSpeed = NominalSpeed;
        this.NominalAltitude = NominalAltitude;
        this.NominalAltitudeType = NominalAltitudeType;
        this.MinimumSpeed = MinimumSpeed;
        this.MaximumSpeed = MaximumSpeed;
        this.NominalFlightProfile = NominalFlightProfile;
        this.MinimumAltitude = MinimumAltitude;
        this.MinAltitudeType = MinAltitudeType;
        this.MaximumAltitude = MaximumAltitude;
        this.MaxAltitudeType = MaxAltitudeType;
    }


    public AirVehicleConfiguration clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            AirVehicleConfiguration newObj = new AirVehicleConfiguration();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The minimum true airspeed that should be commanded for this aircraft (Units: meter/sec)*/
    public float getMinimumSpeed() { return MinimumSpeed; }

    /**  The minimum true airspeed that should be commanded for this aircraft (Units: meter/sec)*/
    public AirVehicleConfiguration setMinimumSpeed( float val ) {
        MinimumSpeed = val;
        return this;
    }

    /**  The maximum true airspeed that should be commanded for this aircraft (Units: meter/sec)*/
    public float getMaximumSpeed() { return MaximumSpeed; }

    /**  The maximum true airspeed that should be commanded for this aircraft (Units: meter/sec)*/
    public AirVehicleConfiguration setMaximumSpeed( float val ) {
        MaximumSpeed = val;
        return this;
    }

    /**  Describes the preferred operating mode for most planning purposes. This can be a max range or endurance speed profile, or any other operating point chosen by a user. This field may not be null. (Units: None)*/
    public afrl.cmasi.FlightProfile getNominalFlightProfile() { return NominalFlightProfile; }

    /**  Describes the preferred operating mode for most planning purposes. This can be a max range or endurance speed profile, or any other operating point chosen by a user. This field may not be null. (Units: None)*/
    public AirVehicleConfiguration setNominalFlightProfile( afrl.cmasi.FlightProfile val ) {
        NominalFlightProfile = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.FlightProfile> getAlternateFlightProfiles() {
        return AlternateFlightProfiles;
    }

    public java.util.ArrayList<afrl.cmasi.LoiterType> getAvailableLoiterTypes() {
        return AvailableLoiterTypes;
    }

    public java.util.ArrayList<afrl.cmasi.TurnType> getAvailableTurnTypes() {
        return AvailableTurnTypes;
    }

    /**  Minimum MSL altitude that this aircraft is allowed to fly. This value should be treated as a regulatory or safety-of-flight parameter and therefore takes precedence over other requests. (Units: meter)*/
    public float getMinimumAltitude() { return MinimumAltitude; }

    /**  Minimum MSL altitude that this aircraft is allowed to fly. This value should be treated as a regulatory or safety-of-flight parameter and therefore takes precedence over other requests. (Units: meter)*/
    public AirVehicleConfiguration setMinimumAltitude( float val ) {
        MinimumAltitude = val;
        return this;
    }

    /**  Altitude type for min altitude (Units: None)*/
    public afrl.cmasi.AltitudeType getMinAltitudeType() { return MinAltitudeType; }

    /**  Altitude type for min altitude (Units: None)*/
    public AirVehicleConfiguration setMinAltitudeType( afrl.cmasi.AltitudeType val ) {
        MinAltitudeType = val;
        return this;
    }

    /**  Maximum MSL altitude that this aircraft is allowed to fly. This value should be treated as a regulatory or safety-of-flight parameter and therefore takes precedence over other requests. (Units: meter)*/
    public float getMaximumAltitude() { return MaximumAltitude; }

    /**  Maximum MSL altitude that this aircraft is allowed to fly. This value should be treated as a regulatory or safety-of-flight parameter and therefore takes precedence over other requests. (Units: meter)*/
    public AirVehicleConfiguration setMaximumAltitude( float val ) {
        MaximumAltitude = val;
        return this;
    }

    /**  Altitude type for max altitude (Units: None)*/
    public afrl.cmasi.AltitudeType getMaxAltitudeType() { return MaxAltitudeType; }

    /**  Altitude type for max altitude (Units: None)*/
    public AirVehicleConfiguration setMaxAltitudeType( afrl.cmasi.AltitudeType val ) {
        MaxAltitudeType = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 24; // accounts for primitive types
        size += LMCPUtil.sizeOf(NominalFlightProfile);
        size += 2;
        size += LMCPUtil.sizeOfList(AlternateFlightProfiles);
        
        size += 2 + 4 * AvailableLoiterTypes.size();
        
        size += 2 + 4 * AvailableTurnTypes.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        MinimumSpeed = LMCPUtil.getReal32(in);

        MaximumSpeed = LMCPUtil.getReal32(in);

            NominalFlightProfile = (afrl.cmasi.FlightProfile) LMCPUtil.getObject(in);
        AlternateFlightProfiles.clear();
        int AlternateFlightProfiles_len = LMCPUtil.getUint16(in);
        for(int i=0; i<AlternateFlightProfiles_len; i++){
        AlternateFlightProfiles.add( (afrl.cmasi.FlightProfile) LMCPUtil.getObject(in));
        }
        AvailableLoiterTypes.clear();
        int AvailableLoiterTypes_len = LMCPUtil.getUint16(in);
        for(int i=0; i<AvailableLoiterTypes_len; i++){
        AvailableLoiterTypes.add(afrl.cmasi.LoiterType.unpack( in ));

        }
        AvailableTurnTypes.clear();
        int AvailableTurnTypes_len = LMCPUtil.getUint16(in);
        for(int i=0; i<AvailableTurnTypes_len; i++){
        AvailableTurnTypes.add(afrl.cmasi.TurnType.unpack( in ));

        }
        MinimumAltitude = LMCPUtil.getReal32(in);

        MinAltitudeType = afrl.cmasi.AltitudeType.unpack( in );

        MaximumAltitude = LMCPUtil.getReal32(in);

        MaxAltitudeType = afrl.cmasi.AltitudeType.unpack( in );


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putReal32(out, MinimumSpeed);
        LMCPUtil.putReal32(out, MaximumSpeed);
        LMCPUtil.putObject(out, NominalFlightProfile);
        LMCPUtil.putUint16(out, AlternateFlightProfiles.size());
        for(int i=0; i<AlternateFlightProfiles.size(); i++){
            LMCPUtil.putObject(out, AlternateFlightProfiles.get(i));
        }
        LMCPUtil.putUint16(out, AvailableLoiterTypes.size());
        for(int i=0; i<AvailableLoiterTypes.size(); i++){
            AvailableLoiterTypes.get(i).pack(out);
        }
        LMCPUtil.putUint16(out, AvailableTurnTypes.size());
        for(int i=0; i<AvailableTurnTypes.size(); i++){
            AvailableTurnTypes.get(i).pack(out);
        }
        LMCPUtil.putReal32(out, MinimumAltitude);
        MinAltitudeType.pack(out);
        LMCPUtil.putReal32(out, MaximumAltitude);
        MaxAltitudeType.pack(out);

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
        buf.append( ws + "<AirVehicleConfiguration Series=\"CMASI\">\n");
        buf.append( ws + "  <MinimumSpeed>" + String.valueOf(MinimumSpeed) + "</MinimumSpeed>\n");
        buf.append( ws + "  <MaximumSpeed>" + String.valueOf(MaximumSpeed) + "</MaximumSpeed>\n");
        if (NominalFlightProfile!= null){
           buf.append( ws + "  <NominalFlightProfile>\n");
           buf.append( ( NominalFlightProfile.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </NominalFlightProfile>\n");
        }
        buf.append( ws + "  <AlternateFlightProfiles>\n");
        for (int i=0; i<AlternateFlightProfiles.size(); i++) {
            buf.append( AlternateFlightProfiles.get(i) == null ? ( ws + "    <null/>\n") : (AlternateFlightProfiles.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </AlternateFlightProfiles>\n");
        buf.append( ws + "  <AvailableLoiterTypes>\n");
        for (int i=0; i<AvailableLoiterTypes.size(); i++) {
        buf.append( ws + "  <LoiterType>" + String.valueOf(AvailableLoiterTypes.get(i)) + "</LoiterType>\n");
        }
        buf.append( ws + "  </AvailableLoiterTypes>\n");
        buf.append( ws + "  <AvailableTurnTypes>\n");
        for (int i=0; i<AvailableTurnTypes.size(); i++) {
        buf.append( ws + "  <TurnType>" + String.valueOf(AvailableTurnTypes.get(i)) + "</TurnType>\n");
        }
        buf.append( ws + "  </AvailableTurnTypes>\n");
        buf.append( ws + "  <MinimumAltitude>" + String.valueOf(MinimumAltitude) + "</MinimumAltitude>\n");
        buf.append( ws + "  <MinAltitudeType>" + String.valueOf(MinAltitudeType) + "</MinAltitudeType>\n");
        buf.append( ws + "  <MaximumAltitude>" + String.valueOf(MaximumAltitude) + "</MaximumAltitude>\n");
        buf.append( ws + "  <MaxAltitudeType>" + String.valueOf(MaxAltitudeType) + "</MaxAltitudeType>\n");
        buf.append( ws + "  <ID>" + String.valueOf(ID) + "</ID>\n");
        buf.append( ws + "  <Affiliation>" + String.valueOf(Affiliation) + "</Affiliation>\n");
        buf.append( ws + "  <EntityType>" + String.valueOf(EntityType) + "</EntityType>\n");
        buf.append( ws + "  <Label>" + String.valueOf(Label) + "</Label>\n");
        buf.append( ws + "  <NominalSpeed>" + String.valueOf(NominalSpeed) + "</NominalSpeed>\n");
        buf.append( ws + "  <NominalAltitude>" + String.valueOf(NominalAltitude) + "</NominalAltitude>\n");
        buf.append( ws + "  <NominalAltitudeType>" + String.valueOf(NominalAltitudeType) + "</NominalAltitudeType>\n");
        buf.append( ws + "  <PayloadConfigurationList>\n");
        for (int i=0; i<PayloadConfigurationList.size(); i++) {
            buf.append( PayloadConfigurationList.get(i) == null ? ( ws + "    <null/>\n") : (PayloadConfigurationList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </PayloadConfigurationList>\n");
        buf.append( ws + "  <Info>\n");
        for (int i=0; i<Info.size(); i++) {
            buf.append( Info.get(i) == null ? ( ws + "    <null/>\n") : (Info.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Info>\n");
        buf.append( ws + "</AirVehicleConfiguration>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        AirVehicleConfiguration o = (AirVehicleConfiguration) anotherObj;
        if (MinimumSpeed != o.MinimumSpeed) return false;
        if (MaximumSpeed != o.MaximumSpeed) return false;
        if (NominalFlightProfile == null && o.NominalFlightProfile != null) return false;
        if ( NominalFlightProfile!= null && !NominalFlightProfile.equals(o.NominalFlightProfile)) return false;
         if (!AlternateFlightProfiles.equals( o.AlternateFlightProfiles)) return false;
         if (!AvailableLoiterTypes.equals( o.AvailableLoiterTypes)) return false;
         if (!AvailableTurnTypes.equals( o.AvailableTurnTypes)) return false;
        if (MinimumAltitude != o.MinimumAltitude) return false;
        if (MinAltitudeType != o.MinAltitudeType) return false;
        if (MaximumAltitude != o.MaximumAltitude) return false;
        if (MaxAltitudeType != o.MaxAltitudeType) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)MinimumSpeed;
        hash += 31 * (int)MaximumSpeed;

        return hash + super.hashCode();
    }
    
}
