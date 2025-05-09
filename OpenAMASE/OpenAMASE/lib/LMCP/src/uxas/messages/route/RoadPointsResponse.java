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
 Resonse to RoadPointsRequest: list of all fulfilled road points requests 
*/
public class RoadPointsResponse extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 14;

    public static final String SERIES_NAME = "ROUTE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5931053054693474304L;
    public static final int SERIES_VERSION = 4;


    private static final String TYPE_NAME = "RoadPointsResponse";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.route.RoadPointsResponse";

    /**  Response ID matching ID from request ({@link RoadPointsRequest}) (Units: None)*/
    @LmcpType("int64")
    protected long ResponseID = 0L;
    /**  Corresponding road points respones, returned as LineOfInterest messages (Units: None)*/
    @LmcpType("LineOfInterest")
    protected java.util.ArrayList<afrl.impact.LineOfInterest> RoadPointsResponses = new java.util.ArrayList<afrl.impact.LineOfInterest>();

    
    public RoadPointsResponse() {
    }

    public RoadPointsResponse(long ResponseID){
        this.ResponseID = ResponseID;
    }


    public RoadPointsResponse clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            RoadPointsResponse newObj = new RoadPointsResponse();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Response ID matching ID from request ({@link RoadPointsRequest}) (Units: None)*/
    public long getResponseID() { return ResponseID; }

    /**  Response ID matching ID from request ({@link RoadPointsRequest}) (Units: None)*/
    public RoadPointsResponse setResponseID( long val ) {
        ResponseID = val;
        return this;
    }

    public java.util.ArrayList<afrl.impact.LineOfInterest> getRoadPointsResponses() {
        return RoadPointsResponses;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(RoadPointsResponses);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ResponseID = LMCPUtil.getInt64(in);

        RoadPointsResponses.clear();
        int RoadPointsResponses_len = LMCPUtil.getUint16(in);
        for(int i=0; i<RoadPointsResponses_len; i++){
        RoadPointsResponses.add( (afrl.impact.LineOfInterest) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ResponseID);
        LMCPUtil.putUint16(out, RoadPointsResponses.size());
        for(int i=0; i<RoadPointsResponses.size(); i++){
            LMCPUtil.putObject(out, RoadPointsResponses.get(i));
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
        buf.append( ws + "<RoadPointsResponse Series=\"ROUTE\">\n");
        buf.append( ws + "  <ResponseID>" + String.valueOf(ResponseID) + "</ResponseID>\n");
        buf.append( ws + "  <RoadPointsResponses>\n");
        for (int i=0; i<RoadPointsResponses.size(); i++) {
            buf.append( RoadPointsResponses.get(i) == null ? ( ws + "    <null/>\n") : (RoadPointsResponses.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </RoadPointsResponses>\n");
        buf.append( ws + "</RoadPointsResponse>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        RoadPointsResponse o = (RoadPointsResponse) anotherObj;
        if (ResponseID != o.ResponseID) return false;
         if (!RoadPointsResponses.equals( o.RoadPointsResponses)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
