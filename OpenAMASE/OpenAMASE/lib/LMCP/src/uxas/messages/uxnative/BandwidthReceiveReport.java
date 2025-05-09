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
 Report for a received message containing positions of both entities and the payload size 
*/
public class BandwidthReceiveReport extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 9;

    public static final String SERIES_NAME = "UXNATIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149751333668345413L;
    public static final int SERIES_VERSION = 9;


    private static final String TYPE_NAME = "BandwidthReceiveReport";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.uxnative.BandwidthReceiveReport";

    /**  Entity from which the message was sent. A valid BandwidthReceiveReport must define EntitySender (null not allowed). (Units: None)*/
    @LmcpType("EntityLocation")
    protected uxas.messages.uxnative.EntityLocation EntitySender = new uxas.messages.uxnative.EntityLocation();
    /**  Entity which received the message. A valid BandwidthReceiveReport must define EntityReceiver (null not allowed). (Units: None)*/
    @LmcpType("EntityLocation")
    protected uxas.messages.uxnative.EntityLocation EntityReceiver = new uxas.messages.uxnative.EntityLocation();
    /**  Size of the message that was received (Units: None)*/
    @LmcpType("uint32")
    protected long TransferPayloadSize = 0L;

    
    public BandwidthReceiveReport() {
    }

    public BandwidthReceiveReport(uxas.messages.uxnative.EntityLocation EntitySender, uxas.messages.uxnative.EntityLocation EntityReceiver, long TransferPayloadSize){
        this.EntitySender = EntitySender;
        this.EntityReceiver = EntityReceiver;
        this.TransferPayloadSize = TransferPayloadSize;
    }


    public BandwidthReceiveReport clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            BandwidthReceiveReport newObj = new BandwidthReceiveReport();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Entity from which the message was sent. A valid BandwidthReceiveReport must define EntitySender (null not allowed). (Units: None)*/
    public uxas.messages.uxnative.EntityLocation getEntitySender() { return EntitySender; }

    /**  Entity from which the message was sent. A valid BandwidthReceiveReport must define EntitySender (null not allowed). (Units: None)*/
    public BandwidthReceiveReport setEntitySender( uxas.messages.uxnative.EntityLocation val ) {
        EntitySender = val;
        return this;
    }

    /**  Entity which received the message. A valid BandwidthReceiveReport must define EntityReceiver (null not allowed). (Units: None)*/
    public uxas.messages.uxnative.EntityLocation getEntityReceiver() { return EntityReceiver; }

    /**  Entity which received the message. A valid BandwidthReceiveReport must define EntityReceiver (null not allowed). (Units: None)*/
    public BandwidthReceiveReport setEntityReceiver( uxas.messages.uxnative.EntityLocation val ) {
        EntityReceiver = val;
        return this;
    }

    /**  Size of the message that was received (Units: None)*/
    public long getTransferPayloadSize() { return TransferPayloadSize; }

    /**  Size of the message that was received (Units: None)*/
    public BandwidthReceiveReport setTransferPayloadSize( long val ) {
        TransferPayloadSize = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 4; // accounts for primitive types
        size += LMCPUtil.sizeOf(EntitySender);
        size += LMCPUtil.sizeOf(EntityReceiver);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
            EntitySender = (uxas.messages.uxnative.EntityLocation) LMCPUtil.getObject(in);
            EntityReceiver = (uxas.messages.uxnative.EntityLocation) LMCPUtil.getObject(in);
        TransferPayloadSize = LMCPUtil.getUint32(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putObject(out, EntitySender);
        LMCPUtil.putObject(out, EntityReceiver);
        LMCPUtil.putUint32(out, TransferPayloadSize);

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
        buf.append( ws + "<BandwidthReceiveReport Series=\"UXNATIVE\">\n");
        if (EntitySender!= null){
           buf.append( ws + "  <EntitySender>\n");
           buf.append( ( EntitySender.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </EntitySender>\n");
        }
        if (EntityReceiver!= null){
           buf.append( ws + "  <EntityReceiver>\n");
           buf.append( ( EntityReceiver.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </EntityReceiver>\n");
        }
        buf.append( ws + "  <TransferPayloadSize>" + String.valueOf(TransferPayloadSize) + "</TransferPayloadSize>\n");
        buf.append( ws + "</BandwidthReceiveReport>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        BandwidthReceiveReport o = (BandwidthReceiveReport) anotherObj;
        if (EntitySender == null && o.EntitySender != null) return false;
        if ( EntitySender!= null && !EntitySender.equals(o.EntitySender)) return false;
        if (EntityReceiver == null && o.EntityReceiver != null) return false;
        if ( EntityReceiver!= null && !EntityReceiver.equals(o.EntityReceiver)) return false;
        if (TransferPayloadSize != o.TransferPayloadSize) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)TransferPayloadSize;

        return hash + super.hashCode();
    }
    
}
