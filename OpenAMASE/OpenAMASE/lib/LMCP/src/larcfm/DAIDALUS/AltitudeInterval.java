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
 Interval consisting of the lower and upper altitudes (MSL?) that will result in a WellClear violation within the detection window 
*/
public class AltitudeInterval extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 4;

    public static final String SERIES_NAME = "DAIDALUS";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4918292825567417683L;
    public static final int SERIES_VERSION = 1;


    private static final String TYPE_NAME = "AltitudeInterval";

    private static final String FULL_LMCP_TYPE_NAME = "larcfm.DAIDALUS.AltitudeInterval";

    /**  array containing the lower and upper altitude of detected WellClear violation in meters (Units: meters)*/
    @LmcpType("real64")
    protected double[] Altitude = new double[2];

    
    public AltitudeInterval() {
    }



    public AltitudeInterval clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            AltitudeInterval newObj = new AltitudeInterval();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public double[] getAltitude() {
        return Altitude;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        for(int i=0; i<Altitude.length; i++){
            Altitude[i] = LMCPUtil.getReal64(in);
        }

    }

    public void pack(OutputStream out) throws IOException {
        for(int i=0; i<Altitude.length; i++){
            LMCPUtil.putReal64(out, Altitude[i]);
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
        buf.append( ws + "<AltitudeInterval Series=\"DAIDALUS\">\n");
        buf.append( ws + "  <Altitude>\n");
        for (int i=0; i<Altitude.length; i++) {
        buf.append( ws + "  <real64>" + String.valueOf(Altitude[i]) + "</real64>\n");
        }
        buf.append( ws + "  </Altitude>\n");
        buf.append( ws + "</AltitudeInterval>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        AltitudeInterval o = (AltitudeInterval) anotherObj;
         if (!java.util.Arrays.equals(Altitude, o.Altitude)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
