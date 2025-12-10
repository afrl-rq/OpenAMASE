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
 Egress route response: returns egress routes (nearby intersections) from a point 
*/
public class EgressRouteResponse extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 11;

    public static final String SERIES_NAME = "ROUTE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5931053054693474304L;
    public static final int SERIES_VERSION = 4;


    private static final String TYPE_NAME = "EgressRouteResponse";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.route.EgressRouteResponse";

    /**  Response ID matching ID from request ({@link EgressRouteRequest}) (Units: None)*/
    @LmcpType("int64")
    protected long ResponseID = 0L;
    /**  The route locations (Units: None)*/
    @LmcpType("Location3D")
    protected java.util.ArrayList<afrl.cmasi.Location3D> NodeLocations = new java.util.ArrayList<afrl.cmasi.Location3D>();
    /**  The orientations (Units: degrees)*/
    @LmcpType("real32")
    protected java.util.ArrayList<Float> Headings = new java.util.ArrayList<Float>();

    
    public EgressRouteResponse() {
    }

    public EgressRouteResponse(long ResponseID){
        this.ResponseID = ResponseID;
    }


    public EgressRouteResponse clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            EgressRouteResponse newObj = new EgressRouteResponse();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Response ID matching ID from request ({@link EgressRouteRequest}) (Units: None)*/
    public long getResponseID() { return ResponseID; }

    /**  Response ID matching ID from request ({@link EgressRouteRequest}) (Units: None)*/
    public EgressRouteResponse setResponseID( long val ) {
        ResponseID = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.Location3D> getNodeLocations() {
        return NodeLocations;
    }

    public java.util.ArrayList<Float> getHeadings() {
        return Headings;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(NodeLocations);
        
        size += 2 + 4 * Headings.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ResponseID = LMCPUtil.getInt64(in);

        NodeLocations.clear();
        int NodeLocations_len = LMCPUtil.getUint16(in);
        for(int i=0; i<NodeLocations_len; i++){
        NodeLocations.add( (afrl.cmasi.Location3D) LMCPUtil.getObject(in));
        }
        Headings.clear();
        int Headings_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Headings_len; i++){
            Headings.add(LMCPUtil.getReal32(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ResponseID);
        LMCPUtil.putUint16(out, NodeLocations.size());
        for(int i=0; i<NodeLocations.size(); i++){
            LMCPUtil.putObject(out, NodeLocations.get(i));
        }
        LMCPUtil.putUint16(out, Headings.size());
        for(int i=0; i<Headings.size(); i++){
            LMCPUtil.putReal32(out, Headings.get(i));
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
        buf.append( ws + "<EgressRouteResponse Series=\"ROUTE\">\n");
        buf.append( ws + "  <ResponseID>" + String.valueOf(ResponseID) + "</ResponseID>\n");
        buf.append( ws + "  <NodeLocations>\n");
        for (int i=0; i<NodeLocations.size(); i++) {
            buf.append( NodeLocations.get(i) == null ? ( ws + "    <null/>\n") : (NodeLocations.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </NodeLocations>\n");
        buf.append( ws + "  <Headings>\n");
        for (int i=0; i<Headings.size(); i++) {
        buf.append( ws + "  <real32>" + String.valueOf(Headings.get(i)) + "</real32>\n");
        }
        buf.append( ws + "  </Headings>\n");
        buf.append( ws + "</EgressRouteResponse>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        EgressRouteResponse o = (EgressRouteResponse) anotherObj;
        if (ResponseID != o.ResponseID) return false;
         if (!NodeLocations.equals( o.NodeLocations)) return false;
         if (!Headings.equals( o.Headings)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
