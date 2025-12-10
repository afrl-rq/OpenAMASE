// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package larcfm.DAIDALUS;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Interval consisting of the lower and upper speeds that will exit a WellClear violation in the minimum time 
*/
public class GroundSpeedRecoveryInterval extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 6;

    public static final String SERIES_NAME = "DAIDALUS";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4918292825567417683L;
    public static final int SERIES_VERSION = 1;


    private static final String TYPE_NAME = "GroundSpeedRecoveryInterval";

    private static final String FULL_LMCP_TYPE_NAME = "larcfm.DAIDALUS.GroundSpeedRecoveryInterval";

    /**  array containing lower and upper speeds of Recovery bands in meters per second (Units: meters/second)*/
    @LmcpType("real64")
    protected double[] RecoveryGroundSpeeds = new double[2];

    
    public GroundSpeedRecoveryInterval() {
    }



    public GroundSpeedRecoveryInterval clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            GroundSpeedRecoveryInterval newObj = new GroundSpeedRecoveryInterval();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public double[] getRecoveryGroundSpeeds() {
        return RecoveryGroundSpeeds;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        for(int i=0; i<RecoveryGroundSpeeds.length; i++){
            RecoveryGroundSpeeds[i] = LMCPUtil.getReal64(in);
        }

    }

    public void pack(OutputStream out) throws IOException {
        for(int i=0; i<RecoveryGroundSpeeds.length; i++){
            LMCPUtil.putReal64(out, RecoveryGroundSpeeds[i]);
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
        buf.append( ws + "<GroundSpeedRecoveryInterval Series=\"DAIDALUS\">\n");
        buf.append( ws + "  <RecoveryGroundSpeeds>\n");
        for (int i=0; i<RecoveryGroundSpeeds.length; i++) {
        buf.append( ws + "  <real64>" + String.valueOf(RecoveryGroundSpeeds[i]) + "</real64>\n");
        }
        buf.append( ws + "  </RecoveryGroundSpeeds>\n");
        buf.append( ws + "</GroundSpeedRecoveryInterval>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        GroundSpeedRecoveryInterval o = (GroundSpeedRecoveryInterval) anotherObj;
         if (!java.util.Arrays.equals(RecoveryGroundSpeeds, o.RecoveryGroundSpeeds)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
