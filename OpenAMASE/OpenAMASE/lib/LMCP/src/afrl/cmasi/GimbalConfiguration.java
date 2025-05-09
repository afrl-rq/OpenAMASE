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
            Description of a gimbal's capabilities. A gimbal is defined as the mount of a sensor or set of sensors            that may or may not have freedom of motion (for instance, the fixed mounting of a camera would still be            considered a gimbal payload that must be defined).<br/>            Azimuth is defined as the angle between the long axis of the aircraft and the sensor boresight, positive clockwise.<br/>            Elevation is defined as the angle between the aircraft long-lat plane and the sensor boresight, positive upwards.<br/>            Rotation is defined as rotation from the aircraft normal (Up), positive clockwise.<br/>        
*/
public class GimbalConfiguration extends afrl.cmasi.PayloadConfiguration {
    
    public static final int LMCP_TYPE = 24;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "GimbalConfiguration";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.GimbalConfiguration";

    /**  The pointing modes supported by the gimbal (Units: None)*/
    @LmcpType("GimbalPointingMode")
    protected java.util.ArrayList<afrl.cmasi.GimbalPointingMode> SupportedPointingModes = new java.util.ArrayList<afrl.cmasi.GimbalPointingMode>();
    /**  Minimum azimuth that this sensor can slew in body axis (0 degrees out of nose, positive clockwise). If this is a fixed-azimuth sensor, then this should be set to the fixed azimuth value. (Units: degree)*/
    @LmcpType("real32")
    protected float MinAzimuth = (float)-180;
    /**  Maximum azimuth that this sensor can slew in body axis (0 degrees out of nose, positive clockwise). If this is a fixed-azimuth sensor, then this should be set to the fixed azimuth value. (Units: degree)*/
    @LmcpType("real32")
    protected float MaxAzimuth = (float)180;
    /**  Determines whether there are any limits on the azimuth of the gimbal. If this is set to false, then MinAzimuth and MaxAzimuth are not used, and the gimbal is capable of continuously spinning in a 360 degree circle without hitting any stops. (Units: None)*/
    @LmcpType("bool")
    protected boolean IsAzimuthClamped = false;
    /**  Minimum elevation that this sensor can slew in body axis (0 degrees horizontal, positive upwards). If this is a fixed-elevation sensor, then this should be set to the fixed elevation value. (Units: degree)*/
    @LmcpType("real32")
    protected float MinElevation = (float)-180;
    /**  Maximum elevation that this sensor can slew in body axis (0 degrees horizontal, positive upwards). If this is a fixed-elevation sensor, then this should be set to the fixed elevation value. (Units: degree)*/
    @LmcpType("real32")
    protected float MaxElevation = (float)180;
    /**  Determines whether there are any limits on the elevation of the gimbal. If this is set to false, then MinElevation and MaxElevation are not used, and the gimbal is capable of continuously spinning in a 360 degree circle without hitting any stops. (Units: None)*/
    @LmcpType("bool")
    protected boolean IsElevationClamped = false;
    /**  Minimum rotation that this sensor can slew in body axis (0 degrees aligned with aircraft normal, positive clockwise). If this is a fixed-rotation sensor, then this should be set to the fixed rotation value. (Units: degree)*/
    @LmcpType("real32")
    protected float MinRotation = (float)0;
    /**  Maximum rotation that this sensor can slew in body axis (0 degrees aligned with aircraft normal, positive clockwise). If this is a fixed-rotation sensor, then this should be set to the fixed rotation value. (Units: degree)*/
    @LmcpType("real32")
    protected float MaxRotation = (float)0;
    /**  Determines whether there are any limits on the rotation of the gimbal. If this is set to false, then MinRotation and MaxRotation are not used, and the gimbal is capable of continuously rotating in a 360 degree circle without hitting any stops. (Units: None)*/
    @LmcpType("bool")
    protected boolean IsRotationClamped = true;
    /**  Rate of maximum horizontal slew for this gimbal. (Units: degree/sec)*/
    @LmcpType("real32")
    protected float MaxAzimuthSlewRate = (float)0;
    /**  Rate of maximum vertical slew for this gimbal. (Units: degree/sec)*/
    @LmcpType("real32")
    protected float MaxElevationSlewRate = (float)0;
    /**  Rate of maximum rotation for this gimbal. (Units: degree/sec)*/
    @LmcpType("real32")
    protected float MaxRotationRate = (float)0;
    /**  Lists the PayloadID of each sensor physically located within the gimbal and that shares the same gimbal angles. It is assumed that all sensors are boresighted. (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> ContainedPayloadList = new java.util.ArrayList<Long>();

    
    public GimbalConfiguration() {
    }

    public GimbalConfiguration(long PayloadID, String PayloadKind, float MinAzimuth, float MaxAzimuth, boolean IsAzimuthClamped, float MinElevation, float MaxElevation, boolean IsElevationClamped, float MinRotation, float MaxRotation, boolean IsRotationClamped, float MaxAzimuthSlewRate, float MaxElevationSlewRate, float MaxRotationRate){
        this.PayloadID = PayloadID;
        this.PayloadKind = PayloadKind;
        this.MinAzimuth = MinAzimuth;
        this.MaxAzimuth = MaxAzimuth;
        this.IsAzimuthClamped = IsAzimuthClamped;
        this.MinElevation = MinElevation;
        this.MaxElevation = MaxElevation;
        this.IsElevationClamped = IsElevationClamped;
        this.MinRotation = MinRotation;
        this.MaxRotation = MaxRotation;
        this.IsRotationClamped = IsRotationClamped;
        this.MaxAzimuthSlewRate = MaxAzimuthSlewRate;
        this.MaxElevationSlewRate = MaxElevationSlewRate;
        this.MaxRotationRate = MaxRotationRate;
    }


    public GimbalConfiguration clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            GimbalConfiguration newObj = new GimbalConfiguration();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public java.util.ArrayList<afrl.cmasi.GimbalPointingMode> getSupportedPointingModes() {
        return SupportedPointingModes;
    }

    /**  Minimum azimuth that this sensor can slew in body axis (0 degrees out of nose, positive clockwise). If this is a fixed-azimuth sensor, then this should be set to the fixed azimuth value. (Units: degree)*/
    public float getMinAzimuth() { return MinAzimuth; }

    /**  Minimum azimuth that this sensor can slew in body axis (0 degrees out of nose, positive clockwise). If this is a fixed-azimuth sensor, then this should be set to the fixed azimuth value. (Units: degree)*/
    public GimbalConfiguration setMinAzimuth( float val ) {
        MinAzimuth = val;
        return this;
    }

    /**  Maximum azimuth that this sensor can slew in body axis (0 degrees out of nose, positive clockwise). If this is a fixed-azimuth sensor, then this should be set to the fixed azimuth value. (Units: degree)*/
    public float getMaxAzimuth() { return MaxAzimuth; }

    /**  Maximum azimuth that this sensor can slew in body axis (0 degrees out of nose, positive clockwise). If this is a fixed-azimuth sensor, then this should be set to the fixed azimuth value. (Units: degree)*/
    public GimbalConfiguration setMaxAzimuth( float val ) {
        MaxAzimuth = val;
        return this;
    }

    /**  Determines whether there are any limits on the azimuth of the gimbal. If this is set to false, then MinAzimuth and MaxAzimuth are not used, and the gimbal is capable of continuously spinning in a 360 degree circle without hitting any stops. (Units: None)*/
    public boolean getIsAzimuthClamped() { return IsAzimuthClamped; }

    /**  Determines whether there are any limits on the azimuth of the gimbal. If this is set to false, then MinAzimuth and MaxAzimuth are not used, and the gimbal is capable of continuously spinning in a 360 degree circle without hitting any stops. (Units: None)*/
    public GimbalConfiguration setIsAzimuthClamped( boolean val ) {
        IsAzimuthClamped = val;
        return this;
    }

    /**  Minimum elevation that this sensor can slew in body axis (0 degrees horizontal, positive upwards). If this is a fixed-elevation sensor, then this should be set to the fixed elevation value. (Units: degree)*/
    public float getMinElevation() { return MinElevation; }

    /**  Minimum elevation that this sensor can slew in body axis (0 degrees horizontal, positive upwards). If this is a fixed-elevation sensor, then this should be set to the fixed elevation value. (Units: degree)*/
    public GimbalConfiguration setMinElevation( float val ) {
        MinElevation = val;
        return this;
    }

    /**  Maximum elevation that this sensor can slew in body axis (0 degrees horizontal, positive upwards). If this is a fixed-elevation sensor, then this should be set to the fixed elevation value. (Units: degree)*/
    public float getMaxElevation() { return MaxElevation; }

    /**  Maximum elevation that this sensor can slew in body axis (0 degrees horizontal, positive upwards). If this is a fixed-elevation sensor, then this should be set to the fixed elevation value. (Units: degree)*/
    public GimbalConfiguration setMaxElevation( float val ) {
        MaxElevation = val;
        return this;
    }

    /**  Determines whether there are any limits on the elevation of the gimbal. If this is set to false, then MinElevation and MaxElevation are not used, and the gimbal is capable of continuously spinning in a 360 degree circle without hitting any stops. (Units: None)*/
    public boolean getIsElevationClamped() { return IsElevationClamped; }

    /**  Determines whether there are any limits on the elevation of the gimbal. If this is set to false, then MinElevation and MaxElevation are not used, and the gimbal is capable of continuously spinning in a 360 degree circle without hitting any stops. (Units: None)*/
    public GimbalConfiguration setIsElevationClamped( boolean val ) {
        IsElevationClamped = val;
        return this;
    }

    /**  Minimum rotation that this sensor can slew in body axis (0 degrees aligned with aircraft normal, positive clockwise). If this is a fixed-rotation sensor, then this should be set to the fixed rotation value. (Units: degree)*/
    public float getMinRotation() { return MinRotation; }

    /**  Minimum rotation that this sensor can slew in body axis (0 degrees aligned with aircraft normal, positive clockwise). If this is a fixed-rotation sensor, then this should be set to the fixed rotation value. (Units: degree)*/
    public GimbalConfiguration setMinRotation( float val ) {
        MinRotation = val;
        return this;
    }

    /**  Maximum rotation that this sensor can slew in body axis (0 degrees aligned with aircraft normal, positive clockwise). If this is a fixed-rotation sensor, then this should be set to the fixed rotation value. (Units: degree)*/
    public float getMaxRotation() { return MaxRotation; }

    /**  Maximum rotation that this sensor can slew in body axis (0 degrees aligned with aircraft normal, positive clockwise). If this is a fixed-rotation sensor, then this should be set to the fixed rotation value. (Units: degree)*/
    public GimbalConfiguration setMaxRotation( float val ) {
        MaxRotation = val;
        return this;
    }

    /**  Determines whether there are any limits on the rotation of the gimbal. If this is set to false, then MinRotation and MaxRotation are not used, and the gimbal is capable of continuously rotating in a 360 degree circle without hitting any stops. (Units: None)*/
    public boolean getIsRotationClamped() { return IsRotationClamped; }

    /**  Determines whether there are any limits on the rotation of the gimbal. If this is set to false, then MinRotation and MaxRotation are not used, and the gimbal is capable of continuously rotating in a 360 degree circle without hitting any stops. (Units: None)*/
    public GimbalConfiguration setIsRotationClamped( boolean val ) {
        IsRotationClamped = val;
        return this;
    }

    /**  Rate of maximum horizontal slew for this gimbal. (Units: degree/sec)*/
    public float getMaxAzimuthSlewRate() { return MaxAzimuthSlewRate; }

    /**  Rate of maximum horizontal slew for this gimbal. (Units: degree/sec)*/
    public GimbalConfiguration setMaxAzimuthSlewRate( float val ) {
        MaxAzimuthSlewRate = val;
        return this;
    }

    /**  Rate of maximum vertical slew for this gimbal. (Units: degree/sec)*/
    public float getMaxElevationSlewRate() { return MaxElevationSlewRate; }

    /**  Rate of maximum vertical slew for this gimbal. (Units: degree/sec)*/
    public GimbalConfiguration setMaxElevationSlewRate( float val ) {
        MaxElevationSlewRate = val;
        return this;
    }

    /**  Rate of maximum rotation for this gimbal. (Units: degree/sec)*/
    public float getMaxRotationRate() { return MaxRotationRate; }

    /**  Rate of maximum rotation for this gimbal. (Units: degree/sec)*/
    public GimbalConfiguration setMaxRotationRate( float val ) {
        MaxRotationRate = val;
        return this;
    }

    public java.util.ArrayList<Long> getContainedPayloadList() {
        return ContainedPayloadList;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 39; // accounts for primitive types
        
        size += 2 + 4 * SupportedPointingModes.size();
        
        size += 2 + 8 * ContainedPayloadList.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        SupportedPointingModes.clear();
        int SupportedPointingModes_len = LMCPUtil.getUint16(in);
        for(int i=0; i<SupportedPointingModes_len; i++){
        SupportedPointingModes.add(afrl.cmasi.GimbalPointingMode.unpack( in ));

        }
        MinAzimuth = LMCPUtil.getReal32(in);

        MaxAzimuth = LMCPUtil.getReal32(in);

        IsAzimuthClamped = LMCPUtil.getBool(in);

        MinElevation = LMCPUtil.getReal32(in);

        MaxElevation = LMCPUtil.getReal32(in);

        IsElevationClamped = LMCPUtil.getBool(in);

        MinRotation = LMCPUtil.getReal32(in);

        MaxRotation = LMCPUtil.getReal32(in);

        IsRotationClamped = LMCPUtil.getBool(in);

        MaxAzimuthSlewRate = LMCPUtil.getReal32(in);

        MaxElevationSlewRate = LMCPUtil.getReal32(in);

        MaxRotationRate = LMCPUtil.getReal32(in);

        ContainedPayloadList.clear();
        int ContainedPayloadList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<ContainedPayloadList_len; i++){
            ContainedPayloadList.add(LMCPUtil.getInt64(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putUint16(out, SupportedPointingModes.size());
        for(int i=0; i<SupportedPointingModes.size(); i++){
            SupportedPointingModes.get(i).pack(out);
        }
        LMCPUtil.putReal32(out, MinAzimuth);
        LMCPUtil.putReal32(out, MaxAzimuth);
        LMCPUtil.putBool(out, IsAzimuthClamped);
        LMCPUtil.putReal32(out, MinElevation);
        LMCPUtil.putReal32(out, MaxElevation);
        LMCPUtil.putBool(out, IsElevationClamped);
        LMCPUtil.putReal32(out, MinRotation);
        LMCPUtil.putReal32(out, MaxRotation);
        LMCPUtil.putBool(out, IsRotationClamped);
        LMCPUtil.putReal32(out, MaxAzimuthSlewRate);
        LMCPUtil.putReal32(out, MaxElevationSlewRate);
        LMCPUtil.putReal32(out, MaxRotationRate);
        LMCPUtil.putUint16(out, ContainedPayloadList.size());
        for(int i=0; i<ContainedPayloadList.size(); i++){
            LMCPUtil.putInt64(out, ContainedPayloadList.get(i));
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
        buf.append( ws + "<GimbalConfiguration Series=\"CMASI\">\n");
        buf.append( ws + "  <SupportedPointingModes>\n");
        for (int i=0; i<SupportedPointingModes.size(); i++) {
        buf.append( ws + "  <GimbalPointingMode>" + String.valueOf(SupportedPointingModes.get(i)) + "</GimbalPointingMode>\n");
        }
        buf.append( ws + "  </SupportedPointingModes>\n");
        buf.append( ws + "  <MinAzimuth>" + String.valueOf(MinAzimuth) + "</MinAzimuth>\n");
        buf.append( ws + "  <MaxAzimuth>" + String.valueOf(MaxAzimuth) + "</MaxAzimuth>\n");
        buf.append( ws + "  <IsAzimuthClamped>" + String.valueOf(IsAzimuthClamped) + "</IsAzimuthClamped>\n");
        buf.append( ws + "  <MinElevation>" + String.valueOf(MinElevation) + "</MinElevation>\n");
        buf.append( ws + "  <MaxElevation>" + String.valueOf(MaxElevation) + "</MaxElevation>\n");
        buf.append( ws + "  <IsElevationClamped>" + String.valueOf(IsElevationClamped) + "</IsElevationClamped>\n");
        buf.append( ws + "  <MinRotation>" + String.valueOf(MinRotation) + "</MinRotation>\n");
        buf.append( ws + "  <MaxRotation>" + String.valueOf(MaxRotation) + "</MaxRotation>\n");
        buf.append( ws + "  <IsRotationClamped>" + String.valueOf(IsRotationClamped) + "</IsRotationClamped>\n");
        buf.append( ws + "  <MaxAzimuthSlewRate>" + String.valueOf(MaxAzimuthSlewRate) + "</MaxAzimuthSlewRate>\n");
        buf.append( ws + "  <MaxElevationSlewRate>" + String.valueOf(MaxElevationSlewRate) + "</MaxElevationSlewRate>\n");
        buf.append( ws + "  <MaxRotationRate>" + String.valueOf(MaxRotationRate) + "</MaxRotationRate>\n");
        buf.append( ws + "  <ContainedPayloadList>\n");
        for (int i=0; i<ContainedPayloadList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(ContainedPayloadList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </ContainedPayloadList>\n");
        buf.append( ws + "  <PayloadID>" + String.valueOf(PayloadID) + "</PayloadID>\n");
        buf.append( ws + "  <PayloadKind>" + String.valueOf(PayloadKind) + "</PayloadKind>\n");
        buf.append( ws + "  <Parameters>\n");
        for (int i=0; i<Parameters.size(); i++) {
            buf.append( Parameters.get(i) == null ? ( ws + "    <null/>\n") : (Parameters.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Parameters>\n");
        buf.append( ws + "</GimbalConfiguration>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        GimbalConfiguration o = (GimbalConfiguration) anotherObj;
         if (!SupportedPointingModes.equals( o.SupportedPointingModes)) return false;
        if (MinAzimuth != o.MinAzimuth) return false;
        if (MaxAzimuth != o.MaxAzimuth) return false;
        if (IsAzimuthClamped != o.IsAzimuthClamped) return false;
        if (MinElevation != o.MinElevation) return false;
        if (MaxElevation != o.MaxElevation) return false;
        if (IsElevationClamped != o.IsElevationClamped) return false;
        if (MinRotation != o.MinRotation) return false;
        if (MaxRotation != o.MaxRotation) return false;
        if (IsRotationClamped != o.IsRotationClamped) return false;
        if (MaxAzimuthSlewRate != o.MaxAzimuthSlewRate) return false;
        if (MaxElevationSlewRate != o.MaxElevationSlewRate) return false;
        if (MaxRotationRate != o.MaxRotationRate) return false;
         if (!ContainedPayloadList.equals( o.ContainedPayloadList)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)MinAzimuth;
        hash += 31 * (int)MaxAzimuth;
        hash += 31 * (int)MinElevation;

        return hash + super.hashCode();
    }
    
}
