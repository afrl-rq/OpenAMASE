// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Indicates a zone violation occuring at the time of a vehicle's state report
*/
public class ActiveZoneViolation extends uxas.messages.ZoneViolation {
    
    public static final int LMCP_TYPE = 4;

    public static final String SERIES_NAME = "ALERTS";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4705211930599686144L;
    public static final int SERIES_VERSION = 1;


    private static final String TYPE_NAME = "ActiveZoneViolation";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.ActiveZoneViolation";


    
    public ActiveZoneViolation() {
    }



    public ActiveZoneViolation clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            ActiveZoneViolation newObj = new ActiveZoneViolation();
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
        buf.append( ws + "<ActiveZoneViolation Series=\"ALERTS\">\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <ZoneID>" + String.valueOf(ZoneID) + "</ZoneID>\n");
        buf.append( ws + "  <KeepIn>" + String.valueOf(KeepIn) + "</KeepIn>\n");
        buf.append( ws + "  <TimeToIntercept>" + String.valueOf(TimeToIntercept) + "</TimeToIntercept>\n");
        if (InterceptPosition!= null){
           buf.append( ws + "  <InterceptPosition>\n");
           buf.append( ( InterceptPosition.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </InterceptPosition>\n");
        }
        if (InterceptPositionLatLong!= null){
           buf.append( ws + "  <InterceptPositionLatLong>\n");
           buf.append( ( InterceptPositionLatLong.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </InterceptPositionLatLong>\n");
        }
        buf.append( ws + "</ActiveZoneViolation>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        ActiveZoneViolation o = (ActiveZoneViolation) anotherObj;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
