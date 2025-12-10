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
            Describes the outer boundary of operations for the entire UAV team or a subset of the vehicle team.        
*/
public class KeepInZone extends afrl.cmasi.AbstractZone {
    
    public static final int LMCP_TYPE = 29;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "KeepInZone";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.KeepInZone";


    
    public KeepInZone() {
    }



    public KeepInZone clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            KeepInZone newObj = new KeepInZone();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     


    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);

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
        buf.append( ws + "<KeepInZone Series=\"CMASI\">\n");
        buf.append( ws + "  <ZoneID>" + String.valueOf(ZoneID) + "</ZoneID>\n");
        buf.append( ws + "  <MinAltitude>" + String.valueOf(MinAltitude) + "</MinAltitude>\n");
        buf.append( ws + "  <MinAltitudeType>" + String.valueOf(MinAltitudeType) + "</MinAltitudeType>\n");
        buf.append( ws + "  <MaxAltitude>" + String.valueOf(MaxAltitude) + "</MaxAltitude>\n");
        buf.append( ws + "  <MaxAltitudeType>" + String.valueOf(MaxAltitudeType) + "</MaxAltitudeType>\n");
        buf.append( ws + "  <AffectedAircraft>\n");
        for (int i=0; i<AffectedAircraft.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AffectedAircraft.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AffectedAircraft>\n");
        buf.append( ws + "  <StartTime>" + String.valueOf(StartTime) + "</StartTime>\n");
        buf.append( ws + "  <EndTime>" + String.valueOf(EndTime) + "</EndTime>\n");
        buf.append( ws + "  <Padding>" + String.valueOf(Padding) + "</Padding>\n");
        buf.append( ws + "  <Label>" + String.valueOf(Label) + "</Label>\n");
        if (Boundary!= null){
           buf.append( ws + "  <Boundary>\n");
           buf.append( ( Boundary.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Boundary>\n");
        }
        buf.append( ws + "</KeepInZone>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        KeepInZone o = (KeepInZone) anotherObj;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
