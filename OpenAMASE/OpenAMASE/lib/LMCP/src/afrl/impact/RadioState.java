// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.impact;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Indicates the radio state 
*/
public class RadioState extends afrl.cmasi.PayloadState {
    
    public static final int LMCP_TYPE = 4;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "RadioState";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.RadioState";

    /**  Whether the radio is enabled (Units: None)*/
    @LmcpType("bool")
    protected boolean Enabled = true;
    /**  Whether the vehicle is in communications range (Units: None)*/
    @LmcpType("bool")
    protected boolean InRange;

    
    public RadioState() {
    }

    public RadioState(long PayloadID, boolean Enabled, boolean InRange){
        this.PayloadID = PayloadID;
        this.Enabled = Enabled;
        this.InRange = InRange;
    }


    public RadioState clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            RadioState newObj = new RadioState();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Whether the radio is enabled (Units: None)*/
    public boolean getEnabled() { return Enabled; }

    /**  Whether the radio is enabled (Units: None)*/
    public RadioState setEnabled( boolean val ) {
        Enabled = val;
        return this;
    }

    /**  Whether the vehicle is in communications range (Units: None)*/
    public boolean getInRange() { return InRange; }

    /**  Whether the vehicle is in communications range (Units: None)*/
    public RadioState setInRange( boolean val ) {
        InRange = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 2; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        Enabled = LMCPUtil.getBool(in);

        InRange = LMCPUtil.getBool(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putBool(out, Enabled);
        LMCPUtil.putBool(out, InRange);

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
        buf.append( ws + "<RadioState Series=\"IMPACT\">\n");
        buf.append( ws + "  <Enabled>" + String.valueOf(Enabled) + "</Enabled>\n");
        buf.append( ws + "  <InRange>" + String.valueOf(InRange) + "</InRange>\n");
        buf.append( ws + "  <PayloadID>" + String.valueOf(PayloadID) + "</PayloadID>\n");
        buf.append( ws + "  <Parameters>\n");
        for (int i=0; i<Parameters.size(); i++) {
            buf.append( Parameters.get(i) == null ? ( ws + "    <null/>\n") : (Parameters.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Parameters>\n");
        buf.append( ws + "</RadioState>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        RadioState o = (RadioState) anotherObj;
        if (Enabled != o.Enabled) return false;
        if (InRange != o.InRange) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
