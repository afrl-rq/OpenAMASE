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
 Describes the current state of a camera. 
*/
public class GimballedPayloadState extends afrl.cmasi.PayloadState {
    
    public static final int LMCP_TYPE = 20;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "GimballedPayloadState";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.GimballedPayloadState";

    /**  Current pointing mode of the gimbal to which this payload is attached(Units: None)*/
    @LmcpType("GimbalPointingMode")
    protected afrl.cmasi.GimbalPointingMode PointingMode = afrl.cmasi.GimbalPointingMode.Unknown;
    /**  Current azimuth angle of the gimbal boresight (positive from vehicle x-axis). (Units: degree)*/
    @LmcpType("real32")
    protected float Azimuth = (float)0;
    /**  Current elevation angle of the gimbal boresight (positive from vehicle x-y plane). (Units: degree)*/
    @LmcpType("real32")
    protected float Elevation = (float)0;
    /**  Current rotation angle of the gimbal boresight (0 degrees aligned with aircraft normal, positive clockwise). (Units: degree)*/
    @LmcpType("real32")
    protected float Rotation = (float)0;

    
    public GimballedPayloadState() {
    }

    public GimballedPayloadState(long PayloadID, afrl.cmasi.GimbalPointingMode PointingMode, float Azimuth, float Elevation, float Rotation){
        this.PayloadID = PayloadID;
        this.PointingMode = PointingMode;
        this.Azimuth = Azimuth;
        this.Elevation = Elevation;
        this.Rotation = Rotation;
    }


    public GimballedPayloadState clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            GimballedPayloadState newObj = new GimballedPayloadState();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Current pointing mode of the gimbal to which this payload is attached(Units: None)*/
    public afrl.cmasi.GimbalPointingMode getPointingMode() { return PointingMode; }

    /**  Current pointing mode of the gimbal to which this payload is attached(Units: None)*/
    public GimballedPayloadState setPointingMode( afrl.cmasi.GimbalPointingMode val ) {
        PointingMode = val;
        return this;
    }

    /**  Current azimuth angle of the gimbal boresight (positive from vehicle x-axis). (Units: degree)*/
    public float getAzimuth() { return Azimuth; }

    /**  Current azimuth angle of the gimbal boresight (positive from vehicle x-axis). (Units: degree)*/
    public GimballedPayloadState setAzimuth( float val ) {
        Azimuth = val;
        return this;
    }

    /**  Current elevation angle of the gimbal boresight (positive from vehicle x-y plane). (Units: degree)*/
    public float getElevation() { return Elevation; }

    /**  Current elevation angle of the gimbal boresight (positive from vehicle x-y plane). (Units: degree)*/
    public GimballedPayloadState setElevation( float val ) {
        Elevation = val;
        return this;
    }

    /**  Current rotation angle of the gimbal boresight (0 degrees aligned with aircraft normal, positive clockwise). (Units: degree)*/
    public float getRotation() { return Rotation; }

    /**  Current rotation angle of the gimbal boresight (0 degrees aligned with aircraft normal, positive clockwise). (Units: degree)*/
    public GimballedPayloadState setRotation( float val ) {
        Rotation = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        PointingMode = afrl.cmasi.GimbalPointingMode.unpack( in );

        Azimuth = LMCPUtil.getReal32(in);

        Elevation = LMCPUtil.getReal32(in);

        Rotation = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        PointingMode.pack(out);
        LMCPUtil.putReal32(out, Azimuth);
        LMCPUtil.putReal32(out, Elevation);
        LMCPUtil.putReal32(out, Rotation);

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
        buf.append( ws + "<GimballedPayloadState Series=\"CMASI\">\n");
        buf.append( ws + "  <PointingMode>" + String.valueOf(PointingMode) + "</PointingMode>\n");
        buf.append( ws + "  <Azimuth>" + String.valueOf(Azimuth) + "</Azimuth>\n");
        buf.append( ws + "  <Elevation>" + String.valueOf(Elevation) + "</Elevation>\n");
        buf.append( ws + "  <Rotation>" + String.valueOf(Rotation) + "</Rotation>\n");
        buf.append( ws + "  <PayloadID>" + String.valueOf(PayloadID) + "</PayloadID>\n");
        buf.append( ws + "  <Parameters>\n");
        for (int i=0; i<Parameters.size(); i++) {
            buf.append( Parameters.get(i) == null ? ( ws + "    <null/>\n") : (Parameters.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Parameters>\n");
        buf.append( ws + "</GimballedPayloadState>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        GimballedPayloadState o = (GimballedPayloadState) anotherObj;
        if (PointingMode != o.PointingMode) return false;
        if (Azimuth != o.Azimuth) return false;
        if (Elevation != o.Elevation) return false;
        if (Rotation != o.Rotation) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)Azimuth;
        hash += 31 * (int)Elevation;
        hash += 31 * (int)Rotation;

        return hash + super.hashCode();
    }
    
}
