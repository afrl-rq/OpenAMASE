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
 An action for controlling which sensor is being transmitted on a video stream. 
*/
public class VideoStreamAction extends afrl.cmasi.VehicleAction {
    
    public static final int LMCP_TYPE = 48;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "VideoStreamAction";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.VideoStreamAction";

    /**  A unique id for the video stream. (Units: None)*/
    @LmcpType("int32")
    protected int VideoStreamID = 0;
    /**  The PayloadID of the sensor which should be activated on this video stream. (Units: None)*/
    @LmcpType("int32")
    protected int ActiveSensor = 0;

    
    public VideoStreamAction() {
    }

    public VideoStreamAction(int VideoStreamID, int ActiveSensor){
        this.VideoStreamID = VideoStreamID;
        this.ActiveSensor = ActiveSensor;
    }


    public VideoStreamAction clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            VideoStreamAction newObj = new VideoStreamAction();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  A unique id for the video stream. (Units: None)*/
    public int getVideoStreamID() { return VideoStreamID; }

    /**  A unique id for the video stream. (Units: None)*/
    public VideoStreamAction setVideoStreamID( int val ) {
        VideoStreamID = val;
        return this;
    }

    /**  The PayloadID of the sensor which should be activated on this video stream. (Units: None)*/
    public int getActiveSensor() { return ActiveSensor; }

    /**  The PayloadID of the sensor which should be activated on this video stream. (Units: None)*/
    public VideoStreamAction setActiveSensor( int val ) {
        ActiveSensor = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        VideoStreamID = LMCPUtil.getInt32(in);

        ActiveSensor = LMCPUtil.getInt32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt32(out, VideoStreamID);
        LMCPUtil.putInt32(out, ActiveSensor);

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
        buf.append( ws + "<VideoStreamAction Series=\"CMASI\">\n");
        buf.append( ws + "  <VideoStreamID>" + String.valueOf(VideoStreamID) + "</VideoStreamID>\n");
        buf.append( ws + "  <ActiveSensor>" + String.valueOf(ActiveSensor) + "</ActiveSensor>\n");
        buf.append( ws + "  <AssociatedTaskList>\n");
        for (int i=0; i<AssociatedTaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTaskList>\n");
        buf.append( ws + "</VideoStreamAction>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        VideoStreamAction o = (VideoStreamAction) anotherObj;
        if (VideoStreamID != o.VideoStreamID) return false;
        if (ActiveSensor != o.ActiveSensor) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)VideoStreamID;
        hash += 31 * (int)ActiveSensor;

        return hash + super.hashCode();
    }
    
}
