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
 Egress route request: find all egress routes (nearby intersections) from a point 
*/
public class EgressRouteRequest extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 10;

    public static final String SERIES_NAME = "ROUTE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5931053054693474304L;
    public static final int SERIES_VERSION = 4;


    private static final String TYPE_NAME = "EgressRouteRequest";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.route.EgressRouteRequest";

    /**  Request ID for correlating with response (Units: None)*/
    @LmcpType("int64")
    protected long RequestID = 0L;
    /**  Location from which to calculate the routes. A valid EgressRouteRequest must define StartLocation (null not allowed). (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D StartLocation = new afrl.cmasi.Location3D();
    /**  The radius of the area of concern (Units: meters)*/
    @LmcpType("real32")
    protected float Radius = (float)60;

    
    public EgressRouteRequest() {
    }

    public EgressRouteRequest(long RequestID, afrl.cmasi.Location3D StartLocation, float Radius){
        this.RequestID = RequestID;
        this.StartLocation = StartLocation;
        this.Radius = Radius;
    }


    public EgressRouteRequest clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            EgressRouteRequest newObj = new EgressRouteRequest();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Request ID for correlating with response (Units: None)*/
    public long getRequestID() { return RequestID; }

    /**  Request ID for correlating with response (Units: None)*/
    public EgressRouteRequest setRequestID( long val ) {
        RequestID = val;
        return this;
    }

    /**  Location from which to calculate the routes. A valid EgressRouteRequest must define StartLocation (null not allowed). (Units: None)*/
    public afrl.cmasi.Location3D getStartLocation() { return StartLocation; }

    /**  Location from which to calculate the routes. A valid EgressRouteRequest must define StartLocation (null not allowed). (Units: None)*/
    public EgressRouteRequest setStartLocation( afrl.cmasi.Location3D val ) {
        StartLocation = val;
        return this;
    }

    /**  The radius of the area of concern (Units: meters)*/
    public float getRadius() { return Radius; }

    /**  The radius of the area of concern (Units: meters)*/
    public EgressRouteRequest setRadius( float val ) {
        Radius = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 12; // accounts for primitive types
        size += LMCPUtil.sizeOf(StartLocation);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        RequestID = LMCPUtil.getInt64(in);

            StartLocation = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        Radius = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, RequestID);
        LMCPUtil.putObject(out, StartLocation);
        LMCPUtil.putReal32(out, Radius);

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
        buf.append( ws + "<EgressRouteRequest Series=\"ROUTE\">\n");
        buf.append( ws + "  <RequestID>" + String.valueOf(RequestID) + "</RequestID>\n");
        if (StartLocation!= null){
           buf.append( ws + "  <StartLocation>\n");
           buf.append( ( StartLocation.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </StartLocation>\n");
        }
        buf.append( ws + "  <Radius>" + String.valueOf(Radius) + "</Radius>\n");
        buf.append( ws + "</EgressRouteRequest>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        EgressRouteRequest o = (EgressRouteRequest) anotherObj;
        if (RequestID != o.RequestID) return false;
        if (StartLocation == null && o.StartLocation != null) return false;
        if ( StartLocation!= null && !StartLocation.equals(o.StartLocation)) return false;
        if (Radius != o.Radius) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)Radius;

        return hash + super.hashCode();
    }
    
}
