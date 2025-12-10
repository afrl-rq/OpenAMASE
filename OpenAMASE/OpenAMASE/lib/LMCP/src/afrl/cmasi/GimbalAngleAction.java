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
            An action for controlling the movement of a gimbal. Individual sensors on the gimbal are controlled            via the {@link CameraAction} message.<br/>            Azimuth is defined as the angle between the long axis of the aircraft and the sensor boresight, positive clockwise.<br/>            Elevation is defined as the angle between the aircraft long-lat plane and the sensor boresight, positive upwards.<br/>            Rotation is defined as rotation from the aircraft normal (Up), positive clockwise.<br/>        
*/
public class GimbalAngleAction extends afrl.cmasi.PayloadAction {
    
    public static final int LMCP_TYPE = 23;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "GimbalAngleAction";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.GimbalAngleAction";

    /**  The commanded azimuth angle (0 degrees out of nose, positive clockwise). (Units: degree)*/
    @LmcpType("real32")
    protected float Azimuth = (float)0;
    /**  The commanded elevation angle (0 degrees horizontal, positive upwards). (Units: degree)*/
    @LmcpType("real32")
    protected float Elevation = (float)0;
    /**  The commanded rotation angle (0 degrees aligned with aircraft normal, positive clockwise). (Units: degree)*/
    @LmcpType("real32")
    protected float Rotation = (float)0;

    
    public GimbalAngleAction() {
    }

    public GimbalAngleAction(long PayloadID, float Azimuth, float Elevation, float Rotation){
        this.PayloadID = PayloadID;
        this.Azimuth = Azimuth;
        this.Elevation = Elevation;
        this.Rotation = Rotation;
    }


    public GimbalAngleAction clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            GimbalAngleAction newObj = new GimbalAngleAction();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The commanded azimuth angle (0 degrees out of nose, positive clockwise). (Units: degree)*/
    public float getAzimuth() { return Azimuth; }

    /**  The commanded azimuth angle (0 degrees out of nose, positive clockwise). (Units: degree)*/
    public GimbalAngleAction setAzimuth( float val ) {
        Azimuth = val;
        return this;
    }

    /**  The commanded elevation angle (0 degrees horizontal, positive upwards). (Units: degree)*/
    public float getElevation() { return Elevation; }

    /**  The commanded elevation angle (0 degrees horizontal, positive upwards). (Units: degree)*/
    public GimbalAngleAction setElevation( float val ) {
        Elevation = val;
        return this;
    }

    /**  The commanded rotation angle (0 degrees aligned with aircraft normal, positive clockwise). (Units: degree)*/
    public float getRotation() { return Rotation; }

    /**  The commanded rotation angle (0 degrees aligned with aircraft normal, positive clockwise). (Units: degree)*/
    public GimbalAngleAction setRotation( float val ) {
        Rotation = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 12; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        Azimuth = LMCPUtil.getReal32(in);

        Elevation = LMCPUtil.getReal32(in);

        Rotation = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
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
        buf.append( ws + "<GimbalAngleAction Series=\"CMASI\">\n");
        buf.append( ws + "  <Azimuth>" + String.valueOf(Azimuth) + "</Azimuth>\n");
        buf.append( ws + "  <Elevation>" + String.valueOf(Elevation) + "</Elevation>\n");
        buf.append( ws + "  <Rotation>" + String.valueOf(Rotation) + "</Rotation>\n");
        buf.append( ws + "  <PayloadID>" + String.valueOf(PayloadID) + "</PayloadID>\n");
        buf.append( ws + "  <AssociatedTaskList>\n");
        for (int i=0; i<AssociatedTaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTaskList>\n");
        buf.append( ws + "</GimbalAngleAction>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        GimbalAngleAction o = (GimbalAngleAction) anotherObj;
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
