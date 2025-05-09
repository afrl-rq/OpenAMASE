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
 Indicates the power plant configuration carried by a vehicle 
*/
public class PowerConfiguration extends afrl.cmasi.PayloadConfiguration {
    
    public static final int LMCP_TYPE = 1;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "PowerConfiguration";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.PowerConfiguration";

    /**  Power generation system (used for acoustic signature) (Units: None)*/
    @LmcpType("PowerPlant")
    protected afrl.impact.PowerPlant NominalPowerConfiguration = afrl.impact.PowerPlant.Gasoline;

    
    public PowerConfiguration() {
    }

    public PowerConfiguration(long PayloadID, String PayloadKind, afrl.impact.PowerPlant NominalPowerConfiguration){
        this.PayloadID = PayloadID;
        this.PayloadKind = PayloadKind;
        this.NominalPowerConfiguration = NominalPowerConfiguration;
    }


    public PowerConfiguration clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            PowerConfiguration newObj = new PowerConfiguration();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Power generation system (used for acoustic signature) (Units: None)*/
    public afrl.impact.PowerPlant getNominalPowerConfiguration() { return NominalPowerConfiguration; }

    /**  Power generation system (used for acoustic signature) (Units: None)*/
    public PowerConfiguration setNominalPowerConfiguration( afrl.impact.PowerPlant val ) {
        NominalPowerConfiguration = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 4; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        NominalPowerConfiguration = afrl.impact.PowerPlant.unpack( in );


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        NominalPowerConfiguration.pack(out);

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
        buf.append( ws + "<PowerConfiguration Series=\"IMPACT\">\n");
        buf.append( ws + "  <NominalPowerConfiguration>" + String.valueOf(NominalPowerConfiguration) + "</NominalPowerConfiguration>\n");
        buf.append( ws + "  <PayloadID>" + String.valueOf(PayloadID) + "</PayloadID>\n");
        buf.append( ws + "  <PayloadKind>" + String.valueOf(PayloadKind) + "</PayloadKind>\n");
        buf.append( ws + "  <Parameters>\n");
        for (int i=0; i<Parameters.size(); i++) {
            buf.append( Parameters.get(i) == null ? ( ws + "    <null/>\n") : (Parameters.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Parameters>\n");
        buf.append( ws + "</PowerConfiguration>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        PowerConfiguration o = (PowerConfiguration) anotherObj;
        if (NominalPowerConfiguration != o.NominalPowerConfiguration) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
