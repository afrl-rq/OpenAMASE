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
 Indicates the IMPACT specific payloads carried by an entity 
*/
public class ImpactPayloadConfiguration extends afrl.cmasi.PayloadConfiguration {
    
    public static final int LMCP_TYPE = 6;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "ImpactPayloadConfiguration";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.ImpactPayloadConfiguration";

    /**  Payloads available for this entity (Units: None)*/
    @LmcpType("ImpactPayloadType")
    protected java.util.ArrayList<afrl.impact.ImpactPayloadType> AvailablePayloads = new java.util.ArrayList<afrl.impact.ImpactPayloadType>();

    
    public ImpactPayloadConfiguration() {
    }

    public ImpactPayloadConfiguration(long PayloadID, String PayloadKind){
        this.PayloadID = PayloadID;
        this.PayloadKind = PayloadKind;
    }


    public ImpactPayloadConfiguration clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            ImpactPayloadConfiguration newObj = new ImpactPayloadConfiguration();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public java.util.ArrayList<afrl.impact.ImpactPayloadType> getAvailablePayloads() {
        return AvailablePayloads;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types
        
        size += 2 + 4 * AvailablePayloads.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        AvailablePayloads.clear();
        int AvailablePayloads_len = LMCPUtil.getUint16(in);
        for(int i=0; i<AvailablePayloads_len; i++){
        AvailablePayloads.add(afrl.impact.ImpactPayloadType.unpack( in ));

        }

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putUint16(out, AvailablePayloads.size());
        for(int i=0; i<AvailablePayloads.size(); i++){
            AvailablePayloads.get(i).pack(out);
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
        buf.append( ws + "<ImpactPayloadConfiguration Series=\"IMPACT\">\n");
        buf.append( ws + "  <AvailablePayloads>\n");
        for (int i=0; i<AvailablePayloads.size(); i++) {
        buf.append( ws + "  <ImpactPayloadType>" + String.valueOf(AvailablePayloads.get(i)) + "</ImpactPayloadType>\n");
        }
        buf.append( ws + "  </AvailablePayloads>\n");
        buf.append( ws + "  <PayloadID>" + String.valueOf(PayloadID) + "</PayloadID>\n");
        buf.append( ws + "  <PayloadKind>" + String.valueOf(PayloadKind) + "</PayloadKind>\n");
        buf.append( ws + "  <Parameters>\n");
        for (int i=0; i<Parameters.size(); i++) {
            buf.append( Parameters.get(i) == null ? ( ws + "    <null/>\n") : (Parameters.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Parameters>\n");
        buf.append( ws + "</ImpactPayloadConfiguration>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        ImpactPayloadConfiguration o = (ImpactPayloadConfiguration) anotherObj;
         if (!AvailablePayloads.equals( o.AvailablePayloads)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
