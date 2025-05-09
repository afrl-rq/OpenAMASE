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
            Description of capabilities of a camera (EO, LWIR, SWIR, MWIR, etc.). If a camera has multiple bands,            then a CameraConfiguration for each camera mode should be included in the PayloadConfigurationList.<br/>            <i>Note on Aspect ratio:</i> Aspect ratio is computed by taking the ratio of VideoStreamHorizontalResolution and            VideoStreamVerticalResolution.  It is assumed that the camera has a constant aspect ratio through all            fields-of-view and that the field-of-view aspect ratio is the same as the video stream aspect ratio.        
*/
public class CameraConfiguration extends afrl.cmasi.PayloadConfiguration {
    
    public static final int LMCP_TYPE = 19;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "CameraConfiguration";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.CameraConfiguration";

    /**  The supported wavelength band of this camera (Units: None)*/
    @LmcpType("WavelengthBand")
    protected afrl.cmasi.WavelengthBand SupportedWavelengthBand = afrl.cmasi.WavelengthBand.EO;
    /**  The manner in which field of views are commanded: Continuous (any FOV between MinHorizontalFieldOfView and MaxHorizontalFieldOfView can be commanded), or Discrete (the only supported FOVs are listed in DiscreteHorizontalFieldOfViewList). (Units: None)*/
    @LmcpType("FOVOperationMode")
    protected afrl.cmasi.FOVOperationMode FieldOfViewMode = afrl.cmasi.FOVOperationMode.Continuous;
    /**  The minimum horizontal field of view of the sensor. Only used if FieldOfViewMode is Continuous. (Units: degree)*/
    @LmcpType("real32")
    protected float MinHorizontalFieldOfView = (float)0;
    /**  The maximum horizontal field of view of the sensor. Only used if FieldOfViewMode is Continuous. (Units: degree)*/
    @LmcpType("real32")
    protected float MaxHorizontalFieldOfView = (float)0;
    /**  The horizontal field of views supported by the sensor. Only used if FieldOfViewMode is Discrete. (Units: degree)*/
    @LmcpType("real32")
    protected java.util.ArrayList<Float> DiscreteHorizontalFieldOfViewList = new java.util.ArrayList<Float>();
    /**  The number of horizontal pixels in the output live-motion video stream. (Units: pixel)*/
    @LmcpType("uint32")
    protected long VideoStreamHorizontalResolution = 0L;
    /**  The number of vertical pixels in the output live-motion video stream. (Units: pixel)*/
    @LmcpType("uint32")
    protected long VideoStreamVerticalResolution = 0L;

    
    public CameraConfiguration() {
    }

    public CameraConfiguration(long PayloadID, String PayloadKind, afrl.cmasi.WavelengthBand SupportedWavelengthBand, afrl.cmasi.FOVOperationMode FieldOfViewMode, float MinHorizontalFieldOfView, float MaxHorizontalFieldOfView, long VideoStreamHorizontalResolution, long VideoStreamVerticalResolution){
        this.PayloadID = PayloadID;
        this.PayloadKind = PayloadKind;
        this.SupportedWavelengthBand = SupportedWavelengthBand;
        this.FieldOfViewMode = FieldOfViewMode;
        this.MinHorizontalFieldOfView = MinHorizontalFieldOfView;
        this.MaxHorizontalFieldOfView = MaxHorizontalFieldOfView;
        this.VideoStreamHorizontalResolution = VideoStreamHorizontalResolution;
        this.VideoStreamVerticalResolution = VideoStreamVerticalResolution;
    }


    public CameraConfiguration clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            CameraConfiguration newObj = new CameraConfiguration();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The supported wavelength band of this camera (Units: None)*/
    public afrl.cmasi.WavelengthBand getSupportedWavelengthBand() { return SupportedWavelengthBand; }

    /**  The supported wavelength band of this camera (Units: None)*/
    public CameraConfiguration setSupportedWavelengthBand( afrl.cmasi.WavelengthBand val ) {
        SupportedWavelengthBand = val;
        return this;
    }

    /**  The manner in which field of views are commanded: Continuous (any FOV between MinHorizontalFieldOfView and MaxHorizontalFieldOfView can be commanded), or Discrete (the only supported FOVs are listed in DiscreteHorizontalFieldOfViewList). (Units: None)*/
    public afrl.cmasi.FOVOperationMode getFieldOfViewMode() { return FieldOfViewMode; }

    /**  The manner in which field of views are commanded: Continuous (any FOV between MinHorizontalFieldOfView and MaxHorizontalFieldOfView can be commanded), or Discrete (the only supported FOVs are listed in DiscreteHorizontalFieldOfViewList). (Units: None)*/
    public CameraConfiguration setFieldOfViewMode( afrl.cmasi.FOVOperationMode val ) {
        FieldOfViewMode = val;
        return this;
    }

    /**  The minimum horizontal field of view of the sensor. Only used if FieldOfViewMode is Continuous. (Units: degree)*/
    public float getMinHorizontalFieldOfView() { return MinHorizontalFieldOfView; }

    /**  The minimum horizontal field of view of the sensor. Only used if FieldOfViewMode is Continuous. (Units: degree)*/
    public CameraConfiguration setMinHorizontalFieldOfView( float val ) {
        MinHorizontalFieldOfView = val;
        return this;
    }

    /**  The maximum horizontal field of view of the sensor. Only used if FieldOfViewMode is Continuous. (Units: degree)*/
    public float getMaxHorizontalFieldOfView() { return MaxHorizontalFieldOfView; }

    /**  The maximum horizontal field of view of the sensor. Only used if FieldOfViewMode is Continuous. (Units: degree)*/
    public CameraConfiguration setMaxHorizontalFieldOfView( float val ) {
        MaxHorizontalFieldOfView = val;
        return this;
    }

    public java.util.ArrayList<Float> getDiscreteHorizontalFieldOfViewList() {
        return DiscreteHorizontalFieldOfViewList;
    }

    /**  The number of horizontal pixels in the output live-motion video stream. (Units: pixel)*/
    public long getVideoStreamHorizontalResolution() { return VideoStreamHorizontalResolution; }

    /**  The number of horizontal pixels in the output live-motion video stream. (Units: pixel)*/
    public CameraConfiguration setVideoStreamHorizontalResolution( long val ) {
        VideoStreamHorizontalResolution = val;
        return this;
    }

    /**  The number of vertical pixels in the output live-motion video stream. (Units: pixel)*/
    public long getVideoStreamVerticalResolution() { return VideoStreamVerticalResolution; }

    /**  The number of vertical pixels in the output live-motion video stream. (Units: pixel)*/
    public CameraConfiguration setVideoStreamVerticalResolution( long val ) {
        VideoStreamVerticalResolution = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 24; // accounts for primitive types
        
        size += 2 + 4 * DiscreteHorizontalFieldOfViewList.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        SupportedWavelengthBand = afrl.cmasi.WavelengthBand.unpack( in );

        FieldOfViewMode = afrl.cmasi.FOVOperationMode.unpack( in );

        MinHorizontalFieldOfView = LMCPUtil.getReal32(in);

        MaxHorizontalFieldOfView = LMCPUtil.getReal32(in);

        DiscreteHorizontalFieldOfViewList.clear();
        int DiscreteHorizontalFieldOfViewList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<DiscreteHorizontalFieldOfViewList_len; i++){
            DiscreteHorizontalFieldOfViewList.add(LMCPUtil.getReal32(in));
        }
        VideoStreamHorizontalResolution = LMCPUtil.getUint32(in);

        VideoStreamVerticalResolution = LMCPUtil.getUint32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        SupportedWavelengthBand.pack(out);
        FieldOfViewMode.pack(out);
        LMCPUtil.putReal32(out, MinHorizontalFieldOfView);
        LMCPUtil.putReal32(out, MaxHorizontalFieldOfView);
        LMCPUtil.putUint16(out, DiscreteHorizontalFieldOfViewList.size());
        for(int i=0; i<DiscreteHorizontalFieldOfViewList.size(); i++){
            LMCPUtil.putReal32(out, DiscreteHorizontalFieldOfViewList.get(i));
        }
        LMCPUtil.putUint32(out, VideoStreamHorizontalResolution);
        LMCPUtil.putUint32(out, VideoStreamVerticalResolution);

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
        buf.append( ws + "<CameraConfiguration Series=\"CMASI\">\n");
        buf.append( ws + "  <SupportedWavelengthBand>" + String.valueOf(SupportedWavelengthBand) + "</SupportedWavelengthBand>\n");
        buf.append( ws + "  <FieldOfViewMode>" + String.valueOf(FieldOfViewMode) + "</FieldOfViewMode>\n");
        buf.append( ws + "  <MinHorizontalFieldOfView>" + String.valueOf(MinHorizontalFieldOfView) + "</MinHorizontalFieldOfView>\n");
        buf.append( ws + "  <MaxHorizontalFieldOfView>" + String.valueOf(MaxHorizontalFieldOfView) + "</MaxHorizontalFieldOfView>\n");
        buf.append( ws + "  <DiscreteHorizontalFieldOfViewList>\n");
        for (int i=0; i<DiscreteHorizontalFieldOfViewList.size(); i++) {
        buf.append( ws + "  <real32>" + String.valueOf(DiscreteHorizontalFieldOfViewList.get(i)) + "</real32>\n");
        }
        buf.append( ws + "  </DiscreteHorizontalFieldOfViewList>\n");
        buf.append( ws + "  <VideoStreamHorizontalResolution>" + String.valueOf(VideoStreamHorizontalResolution) + "</VideoStreamHorizontalResolution>\n");
        buf.append( ws + "  <VideoStreamVerticalResolution>" + String.valueOf(VideoStreamVerticalResolution) + "</VideoStreamVerticalResolution>\n");
        buf.append( ws + "  <PayloadID>" + String.valueOf(PayloadID) + "</PayloadID>\n");
        buf.append( ws + "  <PayloadKind>" + String.valueOf(PayloadKind) + "</PayloadKind>\n");
        buf.append( ws + "  <Parameters>\n");
        for (int i=0; i<Parameters.size(); i++) {
            buf.append( Parameters.get(i) == null ? ( ws + "    <null/>\n") : (Parameters.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Parameters>\n");
        buf.append( ws + "</CameraConfiguration>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        CameraConfiguration o = (CameraConfiguration) anotherObj;
        if (SupportedWavelengthBand != o.SupportedWavelengthBand) return false;
        if (FieldOfViewMode != o.FieldOfViewMode) return false;
        if (MinHorizontalFieldOfView != o.MinHorizontalFieldOfView) return false;
        if (MaxHorizontalFieldOfView != o.MaxHorizontalFieldOfView) return false;
         if (!DiscreteHorizontalFieldOfViewList.equals( o.DiscreteHorizontalFieldOfViewList)) return false;
        if (VideoStreamHorizontalResolution != o.VideoStreamHorizontalResolution) return false;
        if (VideoStreamVerticalResolution != o.VideoStreamVerticalResolution) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)MinHorizontalFieldOfView;
        hash += 31 * (int)MaxHorizontalFieldOfView;

        return hash + super.hashCode();
    }
    
}
