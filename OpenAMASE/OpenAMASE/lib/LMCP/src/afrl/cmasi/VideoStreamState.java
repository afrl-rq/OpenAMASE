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
 Describes the current state of a video stream. 
*/
public class VideoStreamState extends afrl.cmasi.PayloadState {
    
    public static final int LMCP_TYPE = 50;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "VideoStreamState";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.VideoStreamState";

    /**  The PayloadID of the sensor which is active on this video stream.. (Units: None)*/
    @LmcpType("int64")
    protected long ActiveSensor = 0L;

    
    public VideoStreamState() {
    }

    public VideoStreamState(long PayloadID, long ActiveSensor){
        this.PayloadID = PayloadID;
        this.ActiveSensor = ActiveSensor;
    }


    public VideoStreamState clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            VideoStreamState newObj = new VideoStreamState();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The PayloadID of the sensor which is active on this video stream.. (Units: None)*/
    public long getActiveSensor() { return ActiveSensor; }

    /**  The PayloadID of the sensor which is active on this video stream.. (Units: None)*/
    public VideoStreamState setActiveSensor( long val ) {
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
        ActiveSensor = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, ActiveSensor);

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
        buf.append( ws + "<VideoStreamState Series=\"CMASI\">\n");
        buf.append( ws + "  <ActiveSensor>" + String.valueOf(ActiveSensor) + "</ActiveSensor>\n");
        buf.append( ws + "  <PayloadID>" + String.valueOf(PayloadID) + "</PayloadID>\n");
        buf.append( ws + "  <Parameters>\n");
        for (int i=0; i<Parameters.size(); i++) {
            buf.append( Parameters.get(i) == null ? ( ws + "    <null/>\n") : (Parameters.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Parameters>\n");
        buf.append( ws + "</VideoStreamState>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        VideoStreamState o = (VideoStreamState) anotherObj;
        if (ActiveSensor != o.ActiveSensor) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
