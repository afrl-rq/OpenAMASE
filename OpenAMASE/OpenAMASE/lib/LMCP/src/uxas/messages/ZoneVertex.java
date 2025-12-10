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
 A position reporting data structure in xy plane of flat earth local coords
*/
public class ZoneVertex extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 1;

    public static final String SERIES_NAME = "ALERTS";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4705211930599686144L;
    public static final int SERIES_VERSION = 1;


    private static final String TYPE_NAME = "ZoneVertex";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.ZoneVertex";

    /** (Units: None)*/
    @LmcpType("real64")
    protected double east = 0;
    /** (Units: None)*/
    @LmcpType("real64")
    protected double north = 0;

    
    public ZoneVertex() {
    }

    public ZoneVertex(double east, double north){
        this.east = east;
        this.north = north;
    }


    public ZoneVertex clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            ZoneVertex newObj = new ZoneVertex();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /** (Units: None)*/
    public double getEast() { return east; }

    /** (Units: None)*/
    public ZoneVertex setEast( double val ) {
        east = val;
        return this;
    }

    /** (Units: None)*/
    public double getNorth() { return north; }

    /** (Units: None)*/
    public ZoneVertex setNorth( double val ) {
        north = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        east = LMCPUtil.getReal64(in);

        north = LMCPUtil.getReal64(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putReal64(out, east);
        LMCPUtil.putReal64(out, north);

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
        buf.append( ws + "<ZoneVertex Series=\"ALERTS\">\n");
        buf.append( ws + "  <east>" + String.valueOf(east) + "</east>\n");
        buf.append( ws + "  <north>" + String.valueOf(north) + "</north>\n");
        buf.append( ws + "</ZoneVertex>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        ZoneVertex o = (ZoneVertex) anotherObj;
        if (east != o.east) return false;
        if (north != o.north) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)east;
        hash += 31 * (int)north;

        return hash + super.hashCode();
    }
    
}
