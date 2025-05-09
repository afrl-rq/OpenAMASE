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
 Individual route plan 
*/
public class RoutePlan extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 7;

    public static final String SERIES_NAME = "ROUTE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5931053054693474304L;
    public static final int SERIES_VERSION = 4;


    private static final String TYPE_NAME = "RoutePlan";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.route.RoutePlan";

    /**  ID denoting this plan corresponding with requested route constraint pair (Units: None)*/
    @LmcpType("int64")
    protected long RouteID = 0L;
    /**  Waypoints that connect the start location with the end location. Empty if only costs were requested (Units: None)*/
    @LmcpType("Waypoint")
    protected java.util.ArrayList<afrl.cmasi.Waypoint> Waypoints = new java.util.ArrayList<afrl.cmasi.Waypoint>();
    /**  Time cost of route. If less than zero, a planning error has occurred (Units: milliseconds)*/
    @LmcpType("int64")
    protected long RouteCost = -1L;
    /**  Error messages, if applicable (Units: None)*/
    @LmcpType("KeyValuePair")
    protected java.util.ArrayList<afrl.cmasi.KeyValuePair> RouteError = new java.util.ArrayList<afrl.cmasi.KeyValuePair>();

    
    public RoutePlan() {
    }

    public RoutePlan(long RouteID, long RouteCost){
        this.RouteID = RouteID;
        this.RouteCost = RouteCost;
    }


    public RoutePlan clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            RoutePlan newObj = new RoutePlan();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID denoting this plan corresponding with requested route constraint pair (Units: None)*/
    public long getRouteID() { return RouteID; }

    /**  ID denoting this plan corresponding with requested route constraint pair (Units: None)*/
    public RoutePlan setRouteID( long val ) {
        RouteID = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.Waypoint> getWaypoints() {
        return Waypoints;
    }

    /**  Time cost of route. If less than zero, a planning error has occurred (Units: milliseconds)*/
    public long getRouteCost() { return RouteCost; }

    /**  Time cost of route. If less than zero, a planning error has occurred (Units: milliseconds)*/
    public RoutePlan setRouteCost( long val ) {
        RouteCost = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.KeyValuePair> getRouteError() {
        return RouteError;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(Waypoints);
        size += 2;
        size += LMCPUtil.sizeOfList(RouteError);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        RouteID = LMCPUtil.getInt64(in);

        Waypoints.clear();
        int Waypoints_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Waypoints_len; i++){
        Waypoints.add( (afrl.cmasi.Waypoint) LMCPUtil.getObject(in));
        }
        RouteCost = LMCPUtil.getInt64(in);

        RouteError.clear();
        int RouteError_len = LMCPUtil.getUint16(in);
        for(int i=0; i<RouteError_len; i++){
        RouteError.add( (afrl.cmasi.KeyValuePair) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, RouteID);
        LMCPUtil.putUint16(out, Waypoints.size());
        for(int i=0; i<Waypoints.size(); i++){
            LMCPUtil.putObject(out, Waypoints.get(i));
        }
        LMCPUtil.putInt64(out, RouteCost);
        LMCPUtil.putUint16(out, RouteError.size());
        for(int i=0; i<RouteError.size(); i++){
            LMCPUtil.putObject(out, RouteError.get(i));
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
        buf.append( ws + "<RoutePlan Series=\"ROUTE\">\n");
        buf.append( ws + "  <RouteID>" + String.valueOf(RouteID) + "</RouteID>\n");
        buf.append( ws + "  <Waypoints>\n");
        for (int i=0; i<Waypoints.size(); i++) {
            buf.append( Waypoints.get(i) == null ? ( ws + "    <null/>\n") : (Waypoints.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Waypoints>\n");
        buf.append( ws + "  <RouteCost>" + String.valueOf(RouteCost) + "</RouteCost>\n");
        buf.append( ws + "  <RouteError>\n");
        for (int i=0; i<RouteError.size(); i++) {
            buf.append( RouteError.get(i) == null ? ( ws + "    <null/>\n") : (RouteError.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </RouteError>\n");
        buf.append( ws + "</RoutePlan>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        RoutePlan o = (RoutePlan) anotherObj;
        if (RouteID != o.RouteID) return false;
         if (!Waypoints.equals( o.Waypoints)) return false;
        if (RouteCost != o.RouteCost) return false;
         if (!RouteError.equals( o.RouteError)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
