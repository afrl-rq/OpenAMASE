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
 Route response: list of all fulfilled route requests for a complete        list of vehicles (see {@link RouteRequest})
*/
public class RouteResponse extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 9;

    public static final String SERIES_NAME = "ROUTE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5931053054693474304L;
    public static final int SERIES_VERSION = 4;


    private static final String TYPE_NAME = "RouteResponse";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.route.RouteResponse";

    /**  Response ID matching ID from request ({@link RouteRequest}) (Units: None)*/
    @LmcpType("int64")
    protected long ResponseID = 0L;
    /**  Corresponding route responses for all requested vehicles (Units: None)*/
    @LmcpType("RoutePlanResponse")
    protected java.util.ArrayList<uxas.messages.route.RoutePlanResponse> Routes = new java.util.ArrayList<uxas.messages.route.RoutePlanResponse>();

    
    public RouteResponse() {
    }

    public RouteResponse(long ResponseID){
        this.ResponseID = ResponseID;
    }


    public RouteResponse clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            RouteResponse newObj = new RouteResponse();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Response ID matching ID from request ({@link RouteRequest}) (Units: None)*/
    public long getResponseID() { return ResponseID; }

    /**  Response ID matching ID from request ({@link RouteRequest}) (Units: None)*/
    public RouteResponse setResponseID( long val ) {
        ResponseID = val;
        return this;
    }

    public java.util.ArrayList<uxas.messages.route.RoutePlanResponse> getRoutes() {
        return Routes;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(Routes);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ResponseID = LMCPUtil.getInt64(in);

        Routes.clear();
        int Routes_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Routes_len; i++){
        Routes.add( (uxas.messages.route.RoutePlanResponse) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ResponseID);
        LMCPUtil.putUint16(out, Routes.size());
        for(int i=0; i<Routes.size(); i++){
            LMCPUtil.putObject(out, Routes.get(i));
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
        buf.append( ws + "<RouteResponse Series=\"ROUTE\">\n");
        buf.append( ws + "  <ResponseID>" + String.valueOf(ResponseID) + "</ResponseID>\n");
        buf.append( ws + "  <Routes>\n");
        for (int i=0; i<Routes.size(); i++) {
            buf.append( Routes.get(i) == null ? ( ws + "    <null/>\n") : (Routes.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Routes>\n");
        buf.append( ws + "</RouteResponse>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        RouteResponse o = (RouteResponse) anotherObj;
        if (ResponseID != o.ResponseID) return false;
         if (!Routes.equals( o.Routes)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
