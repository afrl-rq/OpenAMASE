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
public class CameraState extends afrl.cmasi.GimballedPayloadState {
    
    public static final int LMCP_TYPE = 21;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "CameraState";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.CameraState";

    /**  The current horizontal field of view (in sensor axis). (Units: degree)*/
    @LmcpType("real32")
    protected float HorizontalFieldOfView = (float)0;
    /**  The current vertical field of view (in sensor axis). (Units: degree)*/
    @LmcpType("real32")
    protected float VerticalFieldOfView = (float)0;
    /**  The current sensor footprint of the camera represented as a polygon with n-vertices. If this field contains zero items then the footprint is unavailable or was not calculated. (Units: None)*/
    @LmcpType("Location3D")
    protected java.util.ArrayList<afrl.cmasi.Location3D> Footprint = new java.util.ArrayList<afrl.cmasi.Location3D>();
    /**  the current location according to the intersection of a ray along the center axis of the current camera field of view with the ground. If this field is null, then the location was not computed. (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D Centerpoint = null;

    
    public CameraState() {
    }

    public CameraState(long PayloadID, afrl.cmasi.GimbalPointingMode PointingMode, float Azimuth, float Elevation, float Rotation, float HorizontalFieldOfView, float VerticalFieldOfView, afrl.cmasi.Location3D Centerpoint){
        this.PayloadID = PayloadID;
        this.PointingMode = PointingMode;
        this.Azimuth = Azimuth;
        this.Elevation = Elevation;
        this.Rotation = Rotation;
        this.HorizontalFieldOfView = HorizontalFieldOfView;
        this.VerticalFieldOfView = VerticalFieldOfView;
        this.Centerpoint = Centerpoint;
    }


    public CameraState clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            CameraState newObj = new CameraState();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The current horizontal field of view (in sensor axis). (Units: degree)*/
    public float getHorizontalFieldOfView() { return HorizontalFieldOfView; }

    /**  The current horizontal field of view (in sensor axis). (Units: degree)*/
    public CameraState setHorizontalFieldOfView( float val ) {
        HorizontalFieldOfView = val;
        return this;
    }

    /**  The current vertical field of view (in sensor axis). (Units: degree)*/
    public float getVerticalFieldOfView() { return VerticalFieldOfView; }

    /**  The current vertical field of view (in sensor axis). (Units: degree)*/
    public CameraState setVerticalFieldOfView( float val ) {
        VerticalFieldOfView = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.Location3D> getFootprint() {
        return Footprint;
    }

    /**  the current location according to the intersection of a ray along the center axis of the current camera field of view with the ground. If this field is null, then the location was not computed. (Units: None)*/
    public afrl.cmasi.Location3D getCenterpoint() { return Centerpoint; }

    /**  the current location according to the intersection of a ray along the center axis of the current camera field of view with the ground. If this field is null, then the location was not computed. (Units: None)*/
    public CameraState setCenterpoint( afrl.cmasi.Location3D val ) {
        Centerpoint = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(Footprint);
        size += LMCPUtil.sizeOf(Centerpoint);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        HorizontalFieldOfView = LMCPUtil.getReal32(in);

        VerticalFieldOfView = LMCPUtil.getReal32(in);

        Footprint.clear();
        int Footprint_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Footprint_len; i++){
        Footprint.add( (afrl.cmasi.Location3D) LMCPUtil.getObject(in));
        }
            Centerpoint = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putReal32(out, HorizontalFieldOfView);
        LMCPUtil.putReal32(out, VerticalFieldOfView);
        LMCPUtil.putUint16(out, Footprint.size());
        for(int i=0; i<Footprint.size(); i++){
            LMCPUtil.putObject(out, Footprint.get(i));
        }
        LMCPUtil.putObject(out, Centerpoint);

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
        buf.append( ws + "<CameraState Series=\"CMASI\">\n");
        buf.append( ws + "  <HorizontalFieldOfView>" + String.valueOf(HorizontalFieldOfView) + "</HorizontalFieldOfView>\n");
        buf.append( ws + "  <VerticalFieldOfView>" + String.valueOf(VerticalFieldOfView) + "</VerticalFieldOfView>\n");
        buf.append( ws + "  <Footprint>\n");
        for (int i=0; i<Footprint.size(); i++) {
            buf.append( Footprint.get(i) == null ? ( ws + "    <null/>\n") : (Footprint.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Footprint>\n");
        if (Centerpoint!= null){
           buf.append( ws + "  <Centerpoint>\n");
           buf.append( ( Centerpoint.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Centerpoint>\n");
        }
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
        buf.append( ws + "</CameraState>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        CameraState o = (CameraState) anotherObj;
        if (HorizontalFieldOfView != o.HorizontalFieldOfView) return false;
        if (VerticalFieldOfView != o.VerticalFieldOfView) return false;
         if (!Footprint.equals( o.Footprint)) return false;
        if (Centerpoint == null && o.Centerpoint != null) return false;
        if ( Centerpoint!= null && !Centerpoint.equals(o.Centerpoint)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)HorizontalFieldOfView;
        hash += 31 * (int)VerticalFieldOfView;

        return hash + super.hashCode();
    }
    
}
