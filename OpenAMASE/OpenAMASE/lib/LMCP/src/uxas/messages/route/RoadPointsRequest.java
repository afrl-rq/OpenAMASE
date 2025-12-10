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
 Route plan request: find route from the current vehicle position to a point of interest or world location 
*/
public class RoadPointsRequest extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 13;

    public static final String SERIES_NAME = "ROUTE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5931053054693474304L;
    public static final int SERIES_VERSION = 4;


    private static final String TYPE_NAME = "RoadPointsRequest";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.route.RoadPointsRequest";

    /**  Request ID for correlating with response (Units: None)*/
    @LmcpType("int64")
    protected long RequestID = 0L;
    /**  List of all requests for this vehicle + operating region situation (Units: None)*/
    @LmcpType("RoadPointsConstraints")
    protected java.util.ArrayList<uxas.messages.route.RoadPointsConstraints> RoadPointsRequests = new java.util.ArrayList<uxas.messages.route.RoadPointsConstraints>();

    
    public RoadPointsRequest() {
    }

    public RoadPointsRequest(long RequestID){
        this.RequestID = RequestID;
    }


    public RoadPointsRequest clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            RoadPointsRequest newObj = new RoadPointsRequest();
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
    public RoadPointsRequest setRequestID( long val ) {
        RequestID = val;
        return this;
    }

    public java.util.ArrayList<uxas.messages.route.RoadPointsConstraints> getRoadPointsRequests() {
        return RoadPointsRequests;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(RoadPointsRequests);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        RequestID = LMCPUtil.getInt64(in);

        RoadPointsRequests.clear();
        int RoadPointsRequests_len = LMCPUtil.getUint16(in);
        for(int i=0; i<RoadPointsRequests_len; i++){
        RoadPointsRequests.add( (uxas.messages.route.RoadPointsConstraints) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, RequestID);
        LMCPUtil.putUint16(out, RoadPointsRequests.size());
        for(int i=0; i<RoadPointsRequests.size(); i++){
            LMCPUtil.putObject(out, RoadPointsRequests.get(i));
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
        buf.append( ws + "<RoadPointsRequest Series=\"ROUTE\">\n");
        buf.append( ws + "  <RequestID>" + String.valueOf(RequestID) + "</RequestID>\n");
        buf.append( ws + "  <RoadPointsRequests>\n");
        for (int i=0; i<RoadPointsRequests.size(); i++) {
            buf.append( RoadPointsRequests.get(i) == null ? ( ws + "    <null/>\n") : (RoadPointsRequests.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </RoadPointsRequests>\n");
        buf.append( ws + "</RoadPointsRequest>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        RoadPointsRequest o = (RoadPointsRequest) anotherObj;
        if (RequestID != o.RequestID) return false;
         if (!RoadPointsRequests.equals( o.RoadPointsRequests)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
