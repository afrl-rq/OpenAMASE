// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages.task;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Description of a sensor footprint 
*/
public class SensorFootprint extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 12;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "SensorFootprint";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.SensorFootprint";

    /**  Response ID that matches corresponding request ID. Note that multiple responses may be generated from a single request (such as multiple cameras which all meet the specification). Each response must contain the matching request ID in it's FootprintResponseID field. (Units: None)*/
    @LmcpType("int64")
    protected long FootprintResponseID = 0L;
    /**  Vehicle for which the footprint calculation applies (Units: None)*/
    @LmcpType("int64")
    protected long VehicleID = 0L;
    /**  Camera from which settings (such as zoom and field of view) were used in the footprint calculation (Units: None)*/
    @LmcpType("int64")
    protected long CameraID = 0L;
    /**  Gimbal from which parameters such as allowable steering angles were used in the footprint calculation (Units: None)*/
    @LmcpType("int64")
    protected long GimbalID = 0L;
    /**  The selected horizontal field of view of the camera necessary to get the reported footprint (equivalent to zoom) (Units: degrees)*/
    @LmcpType("real32")
    protected float HorizontalFOV = (float)0;
    /**  Altitude above ground level for which the footprint was calculated (Units: meters)*/
    @LmcpType("real32")
    protected float AglAltitude = (float)0;
    /**  Commanded gimbal elevation to achieve sensor footprint (Units: degrees)*/
    @LmcpType("real32")
    protected float GimbalElevation = (float)0;
    /**  Aspect ratio (width/height) of the camera image used for footprint calculation. Note: combine with horizontal field of view to determine camera vertical field of view. (Units: None)*/
    @LmcpType("real32")
    protected float AspectRatio = (float)0;
    /**  Ground sample distance obtained with this sensor footprint (Units: meters/pixel)*/
    @LmcpType("real32")
    protected float AchievedGSD = (float)0;
    /**  Sensor wavelength used applicable to this footprint calculation (Units: None)*/
    @LmcpType("WavelengthBand")
    protected afrl.cmasi.WavelengthBand CameraWavelength = afrl.cmasi.WavelengthBand.AllAny;
    /**  Distance out front of the entity to the leading edge (Units: meters)*/
    @LmcpType("real32")
    protected float HorizontalToLeadingEdge = (float)0;
    /**  Distance out front of the entity to the trailing edge (Units: meters)*/
    @LmcpType("real32")
    protected float HorizontalToTrailingEdge = (float)0;
    /**  Distance out front of the entity to the center of the footprint (Units: meters)*/
    @LmcpType("real32")
    protected float HorizontalToCenter = (float)0;
    /**  Width of the footprint at the vertical center (Units: meters)*/
    @LmcpType("real32")
    protected float WidthCenter = (float)0;
    /**  At the prescribed settings (e.g. altitude, elevation angle, etc) the distance from the camera to the center of the footprint (Units: meters)*/
    @LmcpType("real32")
    protected float SlantRangeToCenter = (float)0;

    
    public SensorFootprint() {
    }

    public SensorFootprint(long FootprintResponseID, long VehicleID, long CameraID, long GimbalID, float HorizontalFOV, float AglAltitude, float GimbalElevation, float AspectRatio, float AchievedGSD, afrl.cmasi.WavelengthBand CameraWavelength, float HorizontalToLeadingEdge, float HorizontalToTrailingEdge, float HorizontalToCenter, float WidthCenter, float SlantRangeToCenter){
        this.FootprintResponseID = FootprintResponseID;
        this.VehicleID = VehicleID;
        this.CameraID = CameraID;
        this.GimbalID = GimbalID;
        this.HorizontalFOV = HorizontalFOV;
        this.AglAltitude = AglAltitude;
        this.GimbalElevation = GimbalElevation;
        this.AspectRatio = AspectRatio;
        this.AchievedGSD = AchievedGSD;
        this.CameraWavelength = CameraWavelength;
        this.HorizontalToLeadingEdge = HorizontalToLeadingEdge;
        this.HorizontalToTrailingEdge = HorizontalToTrailingEdge;
        this.HorizontalToCenter = HorizontalToCenter;
        this.WidthCenter = WidthCenter;
        this.SlantRangeToCenter = SlantRangeToCenter;
    }


    public SensorFootprint clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            SensorFootprint newObj = new SensorFootprint();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Response ID that matches corresponding request ID. Note that multiple responses may be generated from a single request (such as multiple cameras which all meet the specification). Each response must contain the matching request ID in it's FootprintResponseID field. (Units: None)*/
    public long getFootprintResponseID() { return FootprintResponseID; }

    /**  Response ID that matches corresponding request ID. Note that multiple responses may be generated from a single request (such as multiple cameras which all meet the specification). Each response must contain the matching request ID in it's FootprintResponseID field. (Units: None)*/
    public SensorFootprint setFootprintResponseID( long val ) {
        FootprintResponseID = val;
        return this;
    }

    /**  Vehicle for which the footprint calculation applies (Units: None)*/
    public long getVehicleID() { return VehicleID; }

    /**  Vehicle for which the footprint calculation applies (Units: None)*/
    public SensorFootprint setVehicleID( long val ) {
        VehicleID = val;
        return this;
    }

    /**  Camera from which settings (such as zoom and field of view) were used in the footprint calculation (Units: None)*/
    public long getCameraID() { return CameraID; }

    /**  Camera from which settings (such as zoom and field of view) were used in the footprint calculation (Units: None)*/
    public SensorFootprint setCameraID( long val ) {
        CameraID = val;
        return this;
    }

    /**  Gimbal from which parameters such as allowable steering angles were used in the footprint calculation (Units: None)*/
    public long getGimbalID() { return GimbalID; }

    /**  Gimbal from which parameters such as allowable steering angles were used in the footprint calculation (Units: None)*/
    public SensorFootprint setGimbalID( long val ) {
        GimbalID = val;
        return this;
    }

    /**  The selected horizontal field of view of the camera necessary to get the reported footprint (equivalent to zoom) (Units: degrees)*/
    public float getHorizontalFOV() { return HorizontalFOV; }

    /**  The selected horizontal field of view of the camera necessary to get the reported footprint (equivalent to zoom) (Units: degrees)*/
    public SensorFootprint setHorizontalFOV( float val ) {
        HorizontalFOV = val;
        return this;
    }

    /**  Altitude above ground level for which the footprint was calculated (Units: meters)*/
    public float getAglAltitude() { return AglAltitude; }

    /**  Altitude above ground level for which the footprint was calculated (Units: meters)*/
    public SensorFootprint setAglAltitude( float val ) {
        AglAltitude = val;
        return this;
    }

    /**  Commanded gimbal elevation to achieve sensor footprint (Units: degrees)*/
    public float getGimbalElevation() { return GimbalElevation; }

    /**  Commanded gimbal elevation to achieve sensor footprint (Units: degrees)*/
    public SensorFootprint setGimbalElevation( float val ) {
        GimbalElevation = val;
        return this;
    }

    /**  Aspect ratio (width/height) of the camera image used for footprint calculation. Note: combine with horizontal field of view to determine camera vertical field of view. (Units: None)*/
    public float getAspectRatio() { return AspectRatio; }

    /**  Aspect ratio (width/height) of the camera image used for footprint calculation. Note: combine with horizontal field of view to determine camera vertical field of view. (Units: None)*/
    public SensorFootprint setAspectRatio( float val ) {
        AspectRatio = val;
        return this;
    }

    /**  Ground sample distance obtained with this sensor footprint (Units: meters/pixel)*/
    public float getAchievedGSD() { return AchievedGSD; }

    /**  Ground sample distance obtained with this sensor footprint (Units: meters/pixel)*/
    public SensorFootprint setAchievedGSD( float val ) {
        AchievedGSD = val;
        return this;
    }

    /**  Sensor wavelength used applicable to this footprint calculation (Units: None)*/
    public afrl.cmasi.WavelengthBand getCameraWavelength() { return CameraWavelength; }

    /**  Sensor wavelength used applicable to this footprint calculation (Units: None)*/
    public SensorFootprint setCameraWavelength( afrl.cmasi.WavelengthBand val ) {
        CameraWavelength = val;
        return this;
    }

    /**  Distance out front of the entity to the leading edge (Units: meters)*/
    public float getHorizontalToLeadingEdge() { return HorizontalToLeadingEdge; }

    /**  Distance out front of the entity to the leading edge (Units: meters)*/
    public SensorFootprint setHorizontalToLeadingEdge( float val ) {
        HorizontalToLeadingEdge = val;
        return this;
    }

    /**  Distance out front of the entity to the trailing edge (Units: meters)*/
    public float getHorizontalToTrailingEdge() { return HorizontalToTrailingEdge; }

    /**  Distance out front of the entity to the trailing edge (Units: meters)*/
    public SensorFootprint setHorizontalToTrailingEdge( float val ) {
        HorizontalToTrailingEdge = val;
        return this;
    }

    /**  Distance out front of the entity to the center of the footprint (Units: meters)*/
    public float getHorizontalToCenter() { return HorizontalToCenter; }

    /**  Distance out front of the entity to the center of the footprint (Units: meters)*/
    public SensorFootprint setHorizontalToCenter( float val ) {
        HorizontalToCenter = val;
        return this;
    }

    /**  Width of the footprint at the vertical center (Units: meters)*/
    public float getWidthCenter() { return WidthCenter; }

    /**  Width of the footprint at the vertical center (Units: meters)*/
    public SensorFootprint setWidthCenter( float val ) {
        WidthCenter = val;
        return this;
    }

    /**  At the prescribed settings (e.g. altitude, elevation angle, etc) the distance from the camera to the center of the footprint (Units: meters)*/
    public float getSlantRangeToCenter() { return SlantRangeToCenter; }

    /**  At the prescribed settings (e.g. altitude, elevation angle, etc) the distance from the camera to the center of the footprint (Units: meters)*/
    public SensorFootprint setSlantRangeToCenter( float val ) {
        SlantRangeToCenter = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 76; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        FootprintResponseID = LMCPUtil.getInt64(in);

        VehicleID = LMCPUtil.getInt64(in);

        CameraID = LMCPUtil.getInt64(in);

        GimbalID = LMCPUtil.getInt64(in);

        HorizontalFOV = LMCPUtil.getReal32(in);

        AglAltitude = LMCPUtil.getReal32(in);

        GimbalElevation = LMCPUtil.getReal32(in);

        AspectRatio = LMCPUtil.getReal32(in);

        AchievedGSD = LMCPUtil.getReal32(in);

        CameraWavelength = afrl.cmasi.WavelengthBand.unpack( in );

        HorizontalToLeadingEdge = LMCPUtil.getReal32(in);

        HorizontalToTrailingEdge = LMCPUtil.getReal32(in);

        HorizontalToCenter = LMCPUtil.getReal32(in);

        WidthCenter = LMCPUtil.getReal32(in);

        SlantRangeToCenter = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, FootprintResponseID);
        LMCPUtil.putInt64(out, VehicleID);
        LMCPUtil.putInt64(out, CameraID);
        LMCPUtil.putInt64(out, GimbalID);
        LMCPUtil.putReal32(out, HorizontalFOV);
        LMCPUtil.putReal32(out, AglAltitude);
        LMCPUtil.putReal32(out, GimbalElevation);
        LMCPUtil.putReal32(out, AspectRatio);
        LMCPUtil.putReal32(out, AchievedGSD);
        CameraWavelength.pack(out);
        LMCPUtil.putReal32(out, HorizontalToLeadingEdge);
        LMCPUtil.putReal32(out, HorizontalToTrailingEdge);
        LMCPUtil.putReal32(out, HorizontalToCenter);
        LMCPUtil.putReal32(out, WidthCenter);
        LMCPUtil.putReal32(out, SlantRangeToCenter);

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
        buf.append( ws + "<SensorFootprint Series=\"UXTASK\">\n");
        buf.append( ws + "  <FootprintResponseID>" + String.valueOf(FootprintResponseID) + "</FootprintResponseID>\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <CameraID>" + String.valueOf(CameraID) + "</CameraID>\n");
        buf.append( ws + "  <GimbalID>" + String.valueOf(GimbalID) + "</GimbalID>\n");
        buf.append( ws + "  <HorizontalFOV>" + String.valueOf(HorizontalFOV) + "</HorizontalFOV>\n");
        buf.append( ws + "  <AglAltitude>" + String.valueOf(AglAltitude) + "</AglAltitude>\n");
        buf.append( ws + "  <GimbalElevation>" + String.valueOf(GimbalElevation) + "</GimbalElevation>\n");
        buf.append( ws + "  <AspectRatio>" + String.valueOf(AspectRatio) + "</AspectRatio>\n");
        buf.append( ws + "  <AchievedGSD>" + String.valueOf(AchievedGSD) + "</AchievedGSD>\n");
        buf.append( ws + "  <CameraWavelength>" + String.valueOf(CameraWavelength) + "</CameraWavelength>\n");
        buf.append( ws + "  <HorizontalToLeadingEdge>" + String.valueOf(HorizontalToLeadingEdge) + "</HorizontalToLeadingEdge>\n");
        buf.append( ws + "  <HorizontalToTrailingEdge>" + String.valueOf(HorizontalToTrailingEdge) + "</HorizontalToTrailingEdge>\n");
        buf.append( ws + "  <HorizontalToCenter>" + String.valueOf(HorizontalToCenter) + "</HorizontalToCenter>\n");
        buf.append( ws + "  <WidthCenter>" + String.valueOf(WidthCenter) + "</WidthCenter>\n");
        buf.append( ws + "  <SlantRangeToCenter>" + String.valueOf(SlantRangeToCenter) + "</SlantRangeToCenter>\n");
        buf.append( ws + "</SensorFootprint>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        SensorFootprint o = (SensorFootprint) anotherObj;
        if (FootprintResponseID != o.FootprintResponseID) return false;
        if (VehicleID != o.VehicleID) return false;
        if (CameraID != o.CameraID) return false;
        if (GimbalID != o.GimbalID) return false;
        if (HorizontalFOV != o.HorizontalFOV) return false;
        if (AglAltitude != o.AglAltitude) return false;
        if (GimbalElevation != o.GimbalElevation) return false;
        if (AspectRatio != o.AspectRatio) return false;
        if (AchievedGSD != o.AchievedGSD) return false;
        if (CameraWavelength != o.CameraWavelength) return false;
        if (HorizontalToLeadingEdge != o.HorizontalToLeadingEdge) return false;
        if (HorizontalToTrailingEdge != o.HorizontalToTrailingEdge) return false;
        if (HorizontalToCenter != o.HorizontalToCenter) return false;
        if (WidthCenter != o.WidthCenter) return false;
        if (SlantRangeToCenter != o.SlantRangeToCenter) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)HorizontalFOV;

        return hash + super.hashCode();
    }
    
}
