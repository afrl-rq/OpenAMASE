// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages.uxnative;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Start/Stop recording 
*/
public class VideoRecord extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 1;

    public static final String SERIES_NAME = "UXNATIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149751333668345413L;
    public static final int SERIES_VERSION = 9;


    private static final String TYPE_NAME = "VideoRecord";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.uxnative.VideoRecord";

    /**  Start [true] or stop [false] video recording (Units: None)*/
    @LmcpType("bool")
    protected boolean Record = false;

    
    public VideoRecord() {
    }

    public VideoRecord(boolean Record){
        this.Record = Record;
    }


    public VideoRecord clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            VideoRecord newObj = new VideoRecord();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Start [true] or stop [false] video recording (Units: None)*/
    public boolean getRecord() { return Record; }

    /**  Start [true] or stop [false] video recording (Units: None)*/
    public VideoRecord setRecord( boolean val ) {
        Record = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 1; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        Record = LMCPUtil.getBool(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putBool(out, Record);

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
        buf.append( ws + "<VideoRecord Series=\"UXNATIVE\">\n");
        buf.append( ws + "  <Record>" + String.valueOf(Record) + "</Record>\n");
        buf.append( ws + "</VideoRecord>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        VideoRecord o = (VideoRecord) anotherObj;
        if (Record != o.Record) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
