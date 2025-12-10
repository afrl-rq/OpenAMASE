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
 Message for measuring actual bandwidth between entities 
*/
public class BandwidthTest extends uxas.messages.uxnative.EntityLocation {
    
    public static final int LMCP_TYPE = 8;

    public static final String SERIES_NAME = "UXNATIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149751333668345413L;
    public static final int SERIES_VERSION = 9;


    private static final String TYPE_NAME = "BandwidthTest";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.uxnative.BandwidthTest";

    /**  Message ID to track message loss (Units: None)*/
    @LmcpType("int64")
    protected long MessageID = 0L;
    /**  Data payload for creating arbitrarily sized messages (Units: None)*/
    @LmcpType("string")
    protected String Payload = "";

    
    public BandwidthTest() {
    }

    public BandwidthTest(long EntityID, afrl.cmasi.Location3D Position, long Time, long MessageID, String Payload){
        this.EntityID = EntityID;
        this.Position = Position;
        this.Time = Time;
        this.MessageID = MessageID;
        this.Payload = Payload;
    }


    public BandwidthTest clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            BandwidthTest newObj = new BandwidthTest();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Message ID to track message loss (Units: None)*/
    public long getMessageID() { return MessageID; }

    /**  Message ID to track message loss (Units: None)*/
    public BandwidthTest setMessageID( long val ) {
        MessageID = val;
        return this;
    }

    /**  Data payload for creating arbitrarily sized messages (Units: None)*/
    public String getPayload() { return Payload; }

    /**  Data payload for creating arbitrarily sized messages (Units: None)*/
    public BandwidthTest setPayload( String val ) {
        Payload = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += LMCPUtil.sizeOfString(Payload);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        MessageID = LMCPUtil.getInt64(in);

        Payload = LMCPUtil.getString(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, MessageID);
        LMCPUtil.putString(out, Payload);

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
        buf.append( ws + "<BandwidthTest Series=\"UXNATIVE\">\n");
        buf.append( ws + "  <MessageID>" + String.valueOf(MessageID) + "</MessageID>\n");
        buf.append( ws + "  <Payload>" + String.valueOf(Payload) + "</Payload>\n");
        buf.append( ws + "  <EntityID>" + String.valueOf(EntityID) + "</EntityID>\n");
        if (Position!= null){
           buf.append( ws + "  <Position>\n");
           buf.append( ( Position.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Position>\n");
        }
        buf.append( ws + "  <Time>" + String.valueOf(Time) + "</Time>\n");
        buf.append( ws + "</BandwidthTest>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        BandwidthTest o = (BandwidthTest) anotherObj;
        if (MessageID != o.MessageID) return false;
        if (Payload == null && o.Payload != null) return false;
        if ( Payload!= null && !Payload.equals(o.Payload)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
