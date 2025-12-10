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
            Description of a video stream transmitted by the aircraft (specifically, what sensors            are available to be transmitted via the stream).        
*/
public class VideoStreamConfiguration extends afrl.cmasi.PayloadConfiguration {
    
    public static final int LMCP_TYPE = 49;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "VideoStreamConfiguration";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.VideoStreamConfiguration";

    /**  List the PayloadID of each sensor that is multiplexed into this stream. The {@link VideoStreamAction} message selects which of these sensors is currently active on the stream. (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> AvailableSensorList = new java.util.ArrayList<Long>();

    
    public VideoStreamConfiguration() {
    }

    public VideoStreamConfiguration(long PayloadID, String PayloadKind){
        this.PayloadID = PayloadID;
        this.PayloadKind = PayloadKind;
    }


    public VideoStreamConfiguration clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            VideoStreamConfiguration newObj = new VideoStreamConfiguration();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public java.util.ArrayList<Long> getAvailableSensorList() {
        return AvailableSensorList;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types
        
        size += 2 + 8 * AvailableSensorList.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        AvailableSensorList.clear();
        int AvailableSensorList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<AvailableSensorList_len; i++){
            AvailableSensorList.add(LMCPUtil.getInt64(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putUint16(out, AvailableSensorList.size());
        for(int i=0; i<AvailableSensorList.size(); i++){
            LMCPUtil.putInt64(out, AvailableSensorList.get(i));
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
        buf.append( ws + "<VideoStreamConfiguration Series=\"CMASI\">\n");
        buf.append( ws + "  <AvailableSensorList>\n");
        for (int i=0; i<AvailableSensorList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AvailableSensorList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AvailableSensorList>\n");
        buf.append( ws + "  <PayloadID>" + String.valueOf(PayloadID) + "</PayloadID>\n");
        buf.append( ws + "  <PayloadKind>" + String.valueOf(PayloadKind) + "</PayloadKind>\n");
        buf.append( ws + "  <Parameters>\n");
        for (int i=0; i<Parameters.size(); i++) {
            buf.append( Parameters.get(i) == null ? ( ws + "    <null/>\n") : (Parameters.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Parameters>\n");
        buf.append( ws + "</VideoStreamConfiguration>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        VideoStreamConfiguration o = (VideoStreamConfiguration) anotherObj;
         if (!AvailableSensorList.equals( o.AvailableSensorList)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
