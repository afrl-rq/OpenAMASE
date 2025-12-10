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
         Location is defined as a point in the world using the WGS84 specification for latitude         and longitude.         
*/
public class Location3D extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 3;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "Location3D";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.Location3D";

    /**  Latitude (Units: degree)*/
    @LmcpType("real64")
    protected double Latitude = 0;
    /**  Longitude (Units: degree)*/
    @LmcpType("real64")
    protected double Longitude = 0;
    /**  Altitude for this waypoint (Units: meter)*/
    @LmcpType("real32")
    protected float Altitude = (float)0;
    /**  Altitude type for specified altitude (Units: None)*/
    @LmcpType("AltitudeType")
    protected afrl.cmasi.AltitudeType AltitudeType = afrl.cmasi.AltitudeType.MSL;

    
    public Location3D() {
    }

    public Location3D(double Latitude, double Longitude, float Altitude, afrl.cmasi.AltitudeType AltitudeType){
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Altitude = Altitude;
        this.AltitudeType = AltitudeType;
    }


    public Location3D clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            Location3D newObj = new Location3D();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Latitude (Units: degree)*/
    public double getLatitude() { return Latitude; }

    /**  Latitude (Units: degree)*/
    public Location3D setLatitude( double val ) {
        Latitude = val;
        return this;
    }

    /**  Longitude (Units: degree)*/
    public double getLongitude() { return Longitude; }

    /**  Longitude (Units: degree)*/
    public Location3D setLongitude( double val ) {
        Longitude = val;
        return this;
    }

    /**  Altitude for this waypoint (Units: meter)*/
    public float getAltitude() { return Altitude; }

    /**  Altitude for this waypoint (Units: meter)*/
    public Location3D setAltitude( float val ) {
        Altitude = val;
        return this;
    }

    /**  Altitude type for specified altitude (Units: None)*/
    public afrl.cmasi.AltitudeType getAltitudeType() { return AltitudeType; }

    /**  Altitude type for specified altitude (Units: None)*/
    public Location3D setAltitudeType( afrl.cmasi.AltitudeType val ) {
        AltitudeType = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 24; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        Latitude = LMCPUtil.getReal64(in);

        Longitude = LMCPUtil.getReal64(in);

        Altitude = LMCPUtil.getReal32(in);

        AltitudeType = afrl.cmasi.AltitudeType.unpack( in );


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putReal64(out, Latitude);
        LMCPUtil.putReal64(out, Longitude);
        LMCPUtil.putReal32(out, Altitude);
        AltitudeType.pack(out);

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
        buf.append( ws + "<Location3D Series=\"CMASI\">\n");
        buf.append( ws + "  <Latitude>" + String.valueOf(Latitude) + "</Latitude>\n");
        buf.append( ws + "  <Longitude>" + String.valueOf(Longitude) + "</Longitude>\n");
        buf.append( ws + "  <Altitude>" + String.valueOf(Altitude) + "</Altitude>\n");
        buf.append( ws + "  <AltitudeType>" + String.valueOf(AltitudeType) + "</AltitudeType>\n");
        buf.append( ws + "</Location3D>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        Location3D o = (Location3D) anotherObj;
        if (Latitude != o.Latitude) return false;
        if (Longitude != o.Longitude) return false;
        if (Altitude != o.Altitude) return false;
        if (AltitudeType != o.AltitudeType) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)Latitude;
        hash += 31 * (int)Longitude;
        hash += 31 * (int)Altitude;

        return hash + super.hashCode();
    }
    
}
