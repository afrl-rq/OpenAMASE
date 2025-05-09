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
            Used to send operator interaction information to automation system during task execution        
*/
public class OperatorSignal extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 38;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "OperatorSignal";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.OperatorSignal";

    /**  Content of signal (e.g. button clicked) (Units: None)*/
    @LmcpType("KeyValuePair")
    protected java.util.ArrayList<afrl.cmasi.KeyValuePair> Signals = new java.util.ArrayList<afrl.cmasi.KeyValuePair>();

    
    public OperatorSignal() {
    }



    public OperatorSignal clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            OperatorSignal newObj = new OperatorSignal();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public java.util.ArrayList<afrl.cmasi.KeyValuePair> getSignals() {
        return Signals;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(Signals);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        Signals.clear();
        int Signals_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Signals_len; i++){
        Signals.add( (afrl.cmasi.KeyValuePair) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putUint16(out, Signals.size());
        for(int i=0; i<Signals.size(); i++){
            LMCPUtil.putObject(out, Signals.get(i));
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
        buf.append( ws + "<OperatorSignal Series=\"CMASI\">\n");
        buf.append( ws + "  <Signals>\n");
        for (int i=0; i<Signals.size(); i++) {
            buf.append( Signals.get(i) == null ? ( ws + "    <null/>\n") : (Signals.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Signals>\n");
        buf.append( ws + "</OperatorSignal>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        OperatorSignal o = (OperatorSignal) anotherObj;
         if (!Signals.equals( o.Signals)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
