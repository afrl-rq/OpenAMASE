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
 Describes the current state of the power system 
*/
public class PowerPlantState extends afrl.cmasi.PayloadState {
    
    public static final int LMCP_TYPE = 8;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "PowerPlantState";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.PowerPlantState";

    /**  The active power plant (Units: None)*/
    @LmcpType("PowerPlant")
    protected afrl.impact.PowerPlant ActivePowerPlant = afrl.impact.PowerPlant.Gasoline;

    
    public PowerPlantState() {
    }

    public PowerPlantState(long PayloadID, afrl.impact.PowerPlant ActivePowerPlant){
        this.PayloadID = PayloadID;
        this.ActivePowerPlant = ActivePowerPlant;
    }


    public PowerPlantState clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            PowerPlantState newObj = new PowerPlantState();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The active power plant (Units: None)*/
    public afrl.impact.PowerPlant getActivePowerPlant() { return ActivePowerPlant; }

    /**  The active power plant (Units: None)*/
    public PowerPlantState setActivePowerPlant( afrl.impact.PowerPlant val ) {
        ActivePowerPlant = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 4; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        ActivePowerPlant = afrl.impact.PowerPlant.unpack( in );


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        ActivePowerPlant.pack(out);

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
        buf.append( ws + "<PowerPlantState Series=\"IMPACT\">\n");
        buf.append( ws + "  <ActivePowerPlant>" + String.valueOf(ActivePowerPlant) + "</ActivePowerPlant>\n");
        buf.append( ws + "  <PayloadID>" + String.valueOf(PayloadID) + "</PayloadID>\n");
        buf.append( ws + "  <Parameters>\n");
        for (int i=0; i<Parameters.size(); i++) {
            buf.append( Parameters.get(i) == null ? ( ws + "    <null/>\n") : (Parameters.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Parameters>\n");
        buf.append( ws + "</PowerPlantState>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        PowerPlantState o = (PowerPlantState) anotherObj;
        if (ActivePowerPlant != o.ActivePowerPlant) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
