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
 A container for key/value pairs.  Keys and values can be any valid string. 
*/
public class KeyValuePair extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 2;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "KeyValuePair";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.KeyValuePair";

    /**  A key (name) for the property (Units: None)*/
    @LmcpType("string")
    protected String Key = "";
    /**  A value for the property (Units: None)*/
    @LmcpType("string")
    protected String Value = "";

    
    public KeyValuePair() {
    }

    public KeyValuePair(String Key, String Value){
        this.Key = Key;
        this.Value = Value;
    }


    public KeyValuePair clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            KeyValuePair newObj = new KeyValuePair();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  A key (name) for the property (Units: None)*/
    public String getKey() { return Key; }

    /**  A key (name) for the property (Units: None)*/
    public KeyValuePair setKey( String val ) {
        Key = val;
        return this;
    }

    /**  A value for the property (Units: None)*/
    public String getValue() { return Value; }

    /**  A value for the property (Units: None)*/
    public KeyValuePair setValue( String val ) {
        Value = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types
        size += LMCPUtil.sizeOfString(Key);
        size += LMCPUtil.sizeOfString(Value);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        Key = LMCPUtil.getString(in);

        Value = LMCPUtil.getString(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putString(out, Key);
        LMCPUtil.putString(out, Value);

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
        buf.append( ws + "<KeyValuePair Series=\"CMASI\">\n");
        buf.append( ws + "  <Key>" + String.valueOf(Key) + "</Key>\n");
        buf.append( ws + "  <Value>" + String.valueOf(Value) + "</Value>\n");
        buf.append( ws + "</KeyValuePair>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        KeyValuePair o = (KeyValuePair) anotherObj;
        if (Key == null && o.Key != null) return false;
        if ( Key!= null && !Key.equals(o.Key)) return false;
        if (Value == null && o.Value != null) return false;
        if ( Value!= null && !Value.equals(o.Value)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
