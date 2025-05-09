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

*/
public class Position2D extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 2;

    public static final String SERIES_NAME = "ALERTS";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4705211930599686144L;
    public static final int SERIES_VERSION = 1;


    private static final String TYPE_NAME = "Position2D";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.Position2D";

    /** (Units: None)*/
    @LmcpType("real64")
    protected double east = 0;
    /** (Units: None)*/
    @LmcpType("real64")
    protected double north = 0;

    
    public Position2D() {
    }

    public Position2D(double east, double north){
        this.east = east;
        this.north = north;
    }


    public Position2D clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            Position2D newObj = new Position2D();
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
    public Position2D setEast( double val ) {
        east = val;
        return this;
    }

    /** (Units: None)*/
    public double getNorth() { return north; }

    /** (Units: None)*/
    public Position2D setNorth( double val ) {
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
        buf.append( ws + "<Position2D Series=\"ALERTS\">\n");
        buf.append( ws + "  <east>" + String.valueOf(east) + "</east>\n");
        buf.append( ws + "  <north>" + String.valueOf(north) + "</north>\n");
        buf.append( ws + "</Position2D>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        Position2D o = (Position2D) anotherObj;
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
