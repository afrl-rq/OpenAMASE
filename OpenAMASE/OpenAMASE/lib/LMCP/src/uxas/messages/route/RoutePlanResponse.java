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
 Route plan response: list of all fulfilled route requests 
*/
public class RoutePlanResponse extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 8;

    public static final String SERIES_NAME = "ROUTE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5931053054693474304L;
    public static final int SERIES_VERSION = 4;


    private static final String TYPE_NAME = "RoutePlanResponse";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.route.RoutePlanResponse";

    /**  Response ID matching ID from request ({@link RoutePlanRequest})(Units: None)*/
    @LmcpType("int64")
    protected long ResponseID = 0L;
    /**  Associated Task ID (0 if no associated task) that this set of responses corresponds to (Units: None)*/
    @LmcpType("int64")
    protected long AssociatedTaskID = 0L;
    /**  Vehicle that was considered during planning (Units: None)*/
    @LmcpType("int64")
    protected long VehicleID = 0L;
    /**  Operating region that was considered during planning (Units: None)*/
    @LmcpType("int64")
    protected long OperatingRegion = 0L;
    /**  List of all responses for this vehicle + operating region situation (Units: None)*/
    @LmcpType("RoutePlan")
    protected java.util.ArrayList<uxas.messages.route.RoutePlan> RouteResponses = new java.util.ArrayList<uxas.messages.route.RoutePlan>();

    
    public RoutePlanResponse() {
    }

    public RoutePlanResponse(long ResponseID, long AssociatedTaskID, long VehicleID, long OperatingRegion){
        this.ResponseID = ResponseID;
        this.AssociatedTaskID = AssociatedTaskID;
        this.VehicleID = VehicleID;
        this.OperatingRegion = OperatingRegion;
    }


    public RoutePlanResponse clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            RoutePlanResponse newObj = new RoutePlanResponse();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Response ID matching ID from request ({@link RoutePlanRequest})(Units: None)*/
    public long getResponseID() { return ResponseID; }

    /**  Response ID matching ID from request ({@link RoutePlanRequest})(Units: None)*/
    public RoutePlanResponse setResponseID( long val ) {
        ResponseID = val;
        return this;
    }

    /**  Associated Task ID (0 if no associated task) that this set of responses corresponds to (Units: None)*/
    public long getAssociatedTaskID() { return AssociatedTaskID; }

    /**  Associated Task ID (0 if no associated task) that this set of responses corresponds to (Units: None)*/
    public RoutePlanResponse setAssociatedTaskID( long val ) {
        AssociatedTaskID = val;
        return this;
    }

    /**  Vehicle that was considered during planning (Units: None)*/
    public long getVehicleID() { return VehicleID; }

    /**  Vehicle that was considered during planning (Units: None)*/
    public RoutePlanResponse setVehicleID( long val ) {
        VehicleID = val;
        return this;
    }

    /**  Operating region that was considered during planning (Units: None)*/
    public long getOperatingRegion() { return OperatingRegion; }

    /**  Operating region that was considered during planning (Units: None)*/
    public RoutePlanResponse setOperatingRegion( long val ) {
        OperatingRegion = val;
        return this;
    }

    public java.util.ArrayList<uxas.messages.route.RoutePlan> getRouteResponses() {
        return RouteResponses;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 32; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(RouteResponses);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ResponseID = LMCPUtil.getInt64(in);

        AssociatedTaskID = LMCPUtil.getInt64(in);

        VehicleID = LMCPUtil.getInt64(in);

        OperatingRegion = LMCPUtil.getInt64(in);

        RouteResponses.clear();
        int RouteResponses_len = LMCPUtil.getUint16(in);
        for(int i=0; i<RouteResponses_len; i++){
        RouteResponses.add( (uxas.messages.route.RoutePlan) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ResponseID);
        LMCPUtil.putInt64(out, AssociatedTaskID);
        LMCPUtil.putInt64(out, VehicleID);
        LMCPUtil.putInt64(out, OperatingRegion);
        LMCPUtil.putUint16(out, RouteResponses.size());
        for(int i=0; i<RouteResponses.size(); i++){
            LMCPUtil.putObject(out, RouteResponses.get(i));
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
        buf.append( ws + "<RoutePlanResponse Series=\"ROUTE\">\n");
        buf.append( ws + "  <ResponseID>" + String.valueOf(ResponseID) + "</ResponseID>\n");
        buf.append( ws + "  <AssociatedTaskID>" + String.valueOf(AssociatedTaskID) + "</AssociatedTaskID>\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <OperatingRegion>" + String.valueOf(OperatingRegion) + "</OperatingRegion>\n");
        buf.append( ws + "  <RouteResponses>\n");
        for (int i=0; i<RouteResponses.size(); i++) {
            buf.append( RouteResponses.get(i) == null ? ( ws + "    <null/>\n") : (RouteResponses.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </RouteResponses>\n");
        buf.append( ws + "</RoutePlanResponse>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        RoutePlanResponse o = (RoutePlanResponse) anotherObj;
        if (ResponseID != o.ResponseID) return false;
        if (AssociatedTaskID != o.AssociatedTaskID) return false;
        if (VehicleID != o.VehicleID) return false;
        if (OperatingRegion != o.OperatingRegion) return false;
         if (!RouteResponses.equals( o.RouteResponses)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
