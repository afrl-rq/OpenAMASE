// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages.route;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Start/end points and associated heading constraints for a route request 
*/
public class RouteConstraints extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 4;

    public static final String SERIES_NAME = "ROUTE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5931053054693474304L;
    public static final int SERIES_VERSION = 4;


    private static final String TYPE_NAME = "RouteConstraints";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.route.RouteConstraints";

    /**  ID denoting this set of route constraints (Units: None)*/
    @LmcpType("int64")
    protected long RouteID = 0L;
    /**  Location from which the planned route will start. A valid RouteConstraints message must define StartLocation (null not allowed). (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D StartLocation = new afrl.cmasi.Location3D();
    /**  Heading of entity at the start of the route (Units: degrees)*/
    @LmcpType("real32")
    protected float StartHeading = (float)0;
    /**  If "true" the heading value in StartHeading must be used to start the route. If not, any starting heading can be used. (Units: None)*/
    @LmcpType("bool")
    protected boolean UseStartHeading = true;
    /**  Location to which the planned route will end. A valid RouteConstraints message must define EndLocation (null not allowed). (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D EndLocation = new afrl.cmasi.Location3D();
    /**  Heading of entity at the end of the route (Units: degrees)*/
    @LmcpType("real32")
    protected float EndHeading = (float)0;
    /**  If "true" the heading value in EndHeading must be used to end the route. If not, any ending heading can be used. (Units: None)*/
    @LmcpType("bool")
    protected boolean UseEndHeading = true;

    
    public RouteConstraints() {
    }

    public RouteConstraints(long RouteID, afrl.cmasi.Location3D StartLocation, float StartHeading, boolean UseStartHeading, afrl.cmasi.Location3D EndLocation, float EndHeading, boolean UseEndHeading){
        this.RouteID = RouteID;
        this.StartLocation = StartLocation;
        this.StartHeading = StartHeading;
        this.UseStartHeading = UseStartHeading;
        this.EndLocation = EndLocation;
        this.EndHeading = EndHeading;
        this.UseEndHeading = UseEndHeading;
    }


    public RouteConstraints clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            RouteConstraints newObj = new RouteConstraints();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID denoting this set of route constraints (Units: None)*/
    public long getRouteID() { return RouteID; }

    /**  ID denoting this set of route constraints (Units: None)*/
    public RouteConstraints setRouteID( long val ) {
        RouteID = val;
        return this;
    }

    /**  Location from which the planned route will start. A valid RouteConstraints message must define StartLocation (null not allowed). (Units: None)*/
    public afrl.cmasi.Location3D getStartLocation() { return StartLocation; }

    /**  Location from which the planned route will start. A valid RouteConstraints message must define StartLocation (null not allowed). (Units: None)*/
    public RouteConstraints setStartLocation( afrl.cmasi.Location3D val ) {
        StartLocation = val;
        return this;
    }

    /**  Heading of entity at the start of the route (Units: degrees)*/
    public float getStartHeading() { return StartHeading; }

    /**  Heading of entity at the start of the route (Units: degrees)*/
    public RouteConstraints setStartHeading( float val ) {
        StartHeading = val;
        return this;
    }

    /**  If "true" the heading value in StartHeading must be used to start the route. If not, any starting heading can be used. (Units: None)*/
    public boolean getUseStartHeading() { return UseStartHeading; }

    /**  If "true" the heading value in StartHeading must be used to start the route. If not, any starting heading can be used. (Units: None)*/
    public RouteConstraints setUseStartHeading( boolean val ) {
        UseStartHeading = val;
        return this;
    }

    /**  Location to which the planned route will end. A valid RouteConstraints message must define EndLocation (null not allowed). (Units: None)*/
    public afrl.cmasi.Location3D getEndLocation() { return EndLocation; }

    /**  Location to which the planned route will end. A valid RouteConstraints message must define EndLocation (null not allowed). (Units: None)*/
    public RouteConstraints setEndLocation( afrl.cmasi.Location3D val ) {
        EndLocation = val;
        return this;
    }

    /**  Heading of entity at the end of the route (Units: degrees)*/
    public float getEndHeading() { return EndHeading; }

    /**  Heading of entity at the end of the route (Units: degrees)*/
    public RouteConstraints setEndHeading( float val ) {
        EndHeading = val;
        return this;
    }

    /**  If "true" the heading value in EndHeading must be used to end the route. If not, any ending heading can be used. (Units: None)*/
    public boolean getUseEndHeading() { return UseEndHeading; }

    /**  If "true" the heading value in EndHeading must be used to end the route. If not, any ending heading can be used. (Units: None)*/
    public RouteConstraints setUseEndHeading( boolean val ) {
        UseEndHeading = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 18; // accounts for primitive types
        size += LMCPUtil.sizeOf(StartLocation);
        size += LMCPUtil.sizeOf(EndLocation);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        RouteID = LMCPUtil.getInt64(in);

            StartLocation = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        StartHeading = LMCPUtil.getReal32(in);

        UseStartHeading = LMCPUtil.getBool(in);

            EndLocation = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        EndHeading = LMCPUtil.getReal32(in);

        UseEndHeading = LMCPUtil.getBool(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, RouteID);
        LMCPUtil.putObject(out, StartLocation);
        LMCPUtil.putReal32(out, StartHeading);
        LMCPUtil.putBool(out, UseStartHeading);
        LMCPUtil.putObject(out, EndLocation);
        LMCPUtil.putReal32(out, EndHeading);
        LMCPUtil.putBool(out, UseEndHeading);

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
        buf.append( ws + "<RouteConstraints Series=\"ROUTE\">\n");
        buf.append( ws + "  <RouteID>" + String.valueOf(RouteID) + "</RouteID>\n");
        if (StartLocation!= null){
           buf.append( ws + "  <StartLocation>\n");
           buf.append( ( StartLocation.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </StartLocation>\n");
        }
        buf.append( ws + "  <StartHeading>" + String.valueOf(StartHeading) + "</StartHeading>\n");
        buf.append( ws + "  <UseStartHeading>" + String.valueOf(UseStartHeading) + "</UseStartHeading>\n");
        if (EndLocation!= null){
           buf.append( ws + "  <EndLocation>\n");
           buf.append( ( EndLocation.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </EndLocation>\n");
        }
        buf.append( ws + "  <EndHeading>" + String.valueOf(EndHeading) + "</EndHeading>\n");
        buf.append( ws + "  <UseEndHeading>" + String.valueOf(UseEndHeading) + "</UseEndHeading>\n");
        buf.append( ws + "</RouteConstraints>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        RouteConstraints o = (RouteConstraints) anotherObj;
        if (RouteID != o.RouteID) return false;
        if (StartLocation == null && o.StartLocation != null) return false;
        if ( StartLocation!= null && !StartLocation.equals(o.StartLocation)) return false;
        if (StartHeading != o.StartHeading) return false;
        if (UseStartHeading != o.UseStartHeading) return false;
        if (EndLocation == null && o.EndLocation != null) return false;
        if ( EndLocation!= null && !EndLocation.equals(o.EndLocation)) return false;
        if (EndHeading != o.EndHeading) return false;
        if (UseEndHeading != o.UseEndHeading) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)StartHeading;

        return hash + super.hashCode();
    }
    
}
