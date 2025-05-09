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
 Base type for payloads 
*/
public class PayloadConfiguration extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 5;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "PayloadConfiguration";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.PayloadConfiguration";

    /**  The unique id for this payload item. (Units: None)*/
    @LmcpType("int64")
    protected long PayloadID = 0L;
    /**  A string uniquely identifying the kind of payload item (primarily to be used for debugging purposes). (Units: None)*/
    @LmcpType("string")
    protected String PayloadKind = "";
    /**  Optional parameters associated with payload configuration (Units: None)*/
    @LmcpType("KeyValuePair")
    protected java.util.ArrayList<afrl.cmasi.KeyValuePair> Parameters = new java.util.ArrayList<afrl.cmasi.KeyValuePair>();

    
    public PayloadConfiguration() {
    }

    public PayloadConfiguration(long PayloadID, String PayloadKind){
        this.PayloadID = PayloadID;
        this.PayloadKind = PayloadKind;
    }


    public PayloadConfiguration clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            PayloadConfiguration newObj = new PayloadConfiguration();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The unique id for this payload item. (Units: None)*/
    public long getPayloadID() { return PayloadID; }

    /**  The unique id for this payload item. (Units: None)*/
    public PayloadConfiguration setPayloadID( long val ) {
        PayloadID = val;
        return this;
    }

    /**  A string uniquely identifying the kind of payload item (primarily to be used for debugging purposes). (Units: None)*/
    public String getPayloadKind() { return PayloadKind; }

    /**  A string uniquely identifying the kind of payload item (primarily to be used for debugging purposes). (Units: None)*/
    public PayloadConfiguration setPayloadKind( String val ) {
        PayloadKind = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.KeyValuePair> getParameters() {
        return Parameters;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += LMCPUtil.sizeOfString(PayloadKind);
        size += 2;
        size += LMCPUtil.sizeOfList(Parameters);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        PayloadID = LMCPUtil.getInt64(in);

        PayloadKind = LMCPUtil.getString(in);

        Parameters.clear();
        int Parameters_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Parameters_len; i++){
        Parameters.add( (afrl.cmasi.KeyValuePair) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, PayloadID);
        LMCPUtil.putString(out, PayloadKind);
        LMCPUtil.putUint16(out, Parameters.size());
        for(int i=0; i<Parameters.size(); i++){
            LMCPUtil.putObject(out, Parameters.get(i));
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
        buf.append( ws + "<PayloadConfiguration Series=\"CMASI\">\n");
        buf.append( ws + "  <PayloadID>" + String.valueOf(PayloadID) + "</PayloadID>\n");
        buf.append( ws + "  <PayloadKind>" + String.valueOf(PayloadKind) + "</PayloadKind>\n");
        buf.append( ws + "  <Parameters>\n");
        for (int i=0; i<Parameters.size(); i++) {
            buf.append( Parameters.get(i) == null ? ( ws + "    <null/>\n") : (Parameters.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Parameters>\n");
        buf.append( ws + "</PayloadConfiguration>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        PayloadConfiguration o = (PayloadConfiguration) anotherObj;
        if (PayloadID != o.PayloadID) return false;
        if (PayloadKind == null && o.PayloadKind != null) return false;
        if ( PayloadKind!= null && !PayloadKind.equals(o.PayloadKind)) return false;
         if (!Parameters.equals( o.Parameters)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
