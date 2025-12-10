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
 Route request: macro route request for a list of available vehicles 
*/
public class RouteRequest extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 5;

    public static final String SERIES_NAME = "ROUTE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5931053054693474304L;
    public static final int SERIES_VERSION = 4;


    private static final String TYPE_NAME = "RouteRequest";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.route.RouteRequest";

    /**  Request ID for correlating with response (Units: None)*/
    @LmcpType("int64")
    protected long RequestID = 0L;
    /**  Associated Task ID (0 if no associated task) for this set of requests (Units: None)*/
    @LmcpType("int64")
    protected long AssociatedTaskID = 0L;
    /**  Vehicles to consider when planning. If list is empty, route planner plans for all known vehicles. (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> VehicleID = new java.util.ArrayList<Long>();
    /**  Operating region to be considered during planning (Units: None)*/
    @LmcpType("int64")
    protected long OperatingRegion = 0L;
    /**  List of all requests for this vehicle set + operating region situation (Units: None)*/
    @LmcpType("RouteConstraints")
    protected java.util.ArrayList<uxas.messages.route.RouteConstraints> RouteRequests = new java.util.ArrayList<uxas.messages.route.RouteConstraints>();
    /**  Request that planner only return costs of routes rather than complete waypoint plans (Units: None)*/
    @LmcpType("bool")
    protected boolean IsCostOnlyRequest = true;

    
    public RouteRequest() {
    }

    public RouteRequest(long RequestID, long AssociatedTaskID, long OperatingRegion, boolean IsCostOnlyRequest){
        this.RequestID = RequestID;
        this.AssociatedTaskID = AssociatedTaskID;
        this.OperatingRegion = OperatingRegion;
        this.IsCostOnlyRequest = IsCostOnlyRequest;
    }


    public RouteRequest clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            RouteRequest newObj = new RouteRequest();
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
    public RouteRequest setRequestID( long val ) {
        RequestID = val;
        return this;
    }

    /**  Associated Task ID (0 if no associated task) for this set of requests (Units: None)*/
    public long getAssociatedTaskID() { return AssociatedTaskID; }

    /**  Associated Task ID (0 if no associated task) for this set of requests (Units: None)*/
    public RouteRequest setAssociatedTaskID( long val ) {
        AssociatedTaskID = val;
        return this;
    }

    public java.util.ArrayList<Long> getVehicleID() {
        return VehicleID;
    }

    /**  Operating region to be considered during planning (Units: None)*/
    public long getOperatingRegion() { return OperatingRegion; }

    /**  Operating region to be considered during planning (Units: None)*/
    public RouteRequest setOperatingRegion( long val ) {
        OperatingRegion = val;
        return this;
    }

    public java.util.ArrayList<uxas.messages.route.RouteConstraints> getRouteRequests() {
        return RouteRequests;
    }

    /**  Request that planner only return costs of routes rather than complete waypoint plans (Units: None)*/
    public boolean getIsCostOnlyRequest() { return IsCostOnlyRequest; }

    /**  Request that planner only return costs of routes rather than complete waypoint plans (Units: None)*/
    public RouteRequest setIsCostOnlyRequest( boolean val ) {
        IsCostOnlyRequest = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 25; // accounts for primitive types
        
        size += 2 + 8 * VehicleID.size();
        size += 2;
        size += LMCPUtil.sizeOfList(RouteRequests);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        RequestID = LMCPUtil.getInt64(in);

        AssociatedTaskID = LMCPUtil.getInt64(in);

        VehicleID.clear();
        int VehicleID_len = LMCPUtil.getUint16(in);
        for(int i=0; i<VehicleID_len; i++){
            VehicleID.add(LMCPUtil.getInt64(in));
        }
        OperatingRegion = LMCPUtil.getInt64(in);

        RouteRequests.clear();
        int RouteRequests_len = LMCPUtil.getUint16(in);
        for(int i=0; i<RouteRequests_len; i++){
        RouteRequests.add( (uxas.messages.route.RouteConstraints) LMCPUtil.getObject(in));
        }
        IsCostOnlyRequest = LMCPUtil.getBool(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, RequestID);
        LMCPUtil.putInt64(out, AssociatedTaskID);
        LMCPUtil.putUint16(out, VehicleID.size());
        for(int i=0; i<VehicleID.size(); i++){
            LMCPUtil.putInt64(out, VehicleID.get(i));
        }
        LMCPUtil.putInt64(out, OperatingRegion);
        LMCPUtil.putUint16(out, RouteRequests.size());
        for(int i=0; i<RouteRequests.size(); i++){
            LMCPUtil.putObject(out, RouteRequests.get(i));
        }
        LMCPUtil.putBool(out, IsCostOnlyRequest);

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
        buf.append( ws + "<RouteRequest Series=\"ROUTE\">\n");
        buf.append( ws + "  <RequestID>" + String.valueOf(RequestID) + "</RequestID>\n");
        buf.append( ws + "  <AssociatedTaskID>" + String.valueOf(AssociatedTaskID) + "</AssociatedTaskID>\n");
        buf.append( ws + "  <VehicleID>\n");
        for (int i=0; i<VehicleID.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(VehicleID.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </VehicleID>\n");
        buf.append( ws + "  <OperatingRegion>" + String.valueOf(OperatingRegion) + "</OperatingRegion>\n");
        buf.append( ws + "  <RouteRequests>\n");
        for (int i=0; i<RouteRequests.size(); i++) {
            buf.append( RouteRequests.get(i) == null ? ( ws + "    <null/>\n") : (RouteRequests.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </RouteRequests>\n");
        buf.append( ws + "  <IsCostOnlyRequest>" + String.valueOf(IsCostOnlyRequest) + "</IsCostOnlyRequest>\n");
        buf.append( ws + "</RouteRequest>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        RouteRequest o = (RouteRequest) anotherObj;
        if (RequestID != o.RequestID) return false;
        if (AssociatedTaskID != o.AssociatedTaskID) return false;
         if (!VehicleID.equals( o.VehicleID)) return false;
        if (OperatingRegion != o.OperatingRegion) return false;
         if (!RouteRequests.equals( o.RouteRequests)) return false;
        if (IsCostOnlyRequest != o.IsCostOnlyRequest) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
