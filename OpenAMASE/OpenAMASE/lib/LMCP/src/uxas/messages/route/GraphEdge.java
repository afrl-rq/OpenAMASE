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
 Defines an edge for a geo-spatial graph. An edge must connect two nodes defined by        GraphNodes. Each edge consists of a set of waypoints connecting the two endpoints.        All edges are considered bi-directional (i.e. one-way roads are not defined) 
*/
public class GraphEdge extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 2;

    public static final String SERIES_NAME = "ROUTE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5931053054693474304L;
    public static final int SERIES_VERSION = 4;


    private static final String TYPE_NAME = "GraphEdge";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.route.GraphEdge";

    /**  Unique ID for the edge (Units: None)*/
    @LmcpType("int64")
    protected long EdgeID = 0L;
    /**  GraphNode ID which defines the start point of the edge (Units: None)*/
    @LmcpType("int64")
    protected long StartNode = 0L;
    /**  GraphNode ID which defines the end point of the edge (Units: None)*/
    @LmcpType("int64")
    protected long EndNode = 0L;
    /**  Optional waypoints describing physical edge in lat/lon coordinates (Units: None)*/
    @LmcpType("Location3D")
    protected java.util.ArrayList<afrl.cmasi.Location3D> Waypoints = new java.util.ArrayList<afrl.cmasi.Location3D>();

    
    public GraphEdge() {
    }

    public GraphEdge(long EdgeID, long StartNode, long EndNode){
        this.EdgeID = EdgeID;
        this.StartNode = StartNode;
        this.EndNode = EndNode;
    }


    public GraphEdge clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            GraphEdge newObj = new GraphEdge();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Unique ID for the edge (Units: None)*/
    public long getEdgeID() { return EdgeID; }

    /**  Unique ID for the edge (Units: None)*/
    public GraphEdge setEdgeID( long val ) {
        EdgeID = val;
        return this;
    }

    /**  GraphNode ID which defines the start point of the edge (Units: None)*/
    public long getStartNode() { return StartNode; }

    /**  GraphNode ID which defines the start point of the edge (Units: None)*/
    public GraphEdge setStartNode( long val ) {
        StartNode = val;
        return this;
    }

    /**  GraphNode ID which defines the end point of the edge (Units: None)*/
    public long getEndNode() { return EndNode; }

    /**  GraphNode ID which defines the end point of the edge (Units: None)*/
    public GraphEdge setEndNode( long val ) {
        EndNode = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.Location3D> getWaypoints() {
        return Waypoints;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 24; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(Waypoints);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        EdgeID = LMCPUtil.getInt64(in);

        StartNode = LMCPUtil.getInt64(in);

        EndNode = LMCPUtil.getInt64(in);

        Waypoints.clear();
        int Waypoints_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Waypoints_len; i++){
        Waypoints.add( (afrl.cmasi.Location3D) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, EdgeID);
        LMCPUtil.putInt64(out, StartNode);
        LMCPUtil.putInt64(out, EndNode);
        LMCPUtil.putUint16(out, Waypoints.size());
        for(int i=0; i<Waypoints.size(); i++){
            LMCPUtil.putObject(out, Waypoints.get(i));
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
        buf.append( ws + "<GraphEdge Series=\"ROUTE\">\n");
        buf.append( ws + "  <EdgeID>" + String.valueOf(EdgeID) + "</EdgeID>\n");
        buf.append( ws + "  <StartNode>" + String.valueOf(StartNode) + "</StartNode>\n");
        buf.append( ws + "  <EndNode>" + String.valueOf(EndNode) + "</EndNode>\n");
        buf.append( ws + "  <Waypoints>\n");
        for (int i=0; i<Waypoints.size(); i++) {
            buf.append( Waypoints.get(i) == null ? ( ws + "    <null/>\n") : (Waypoints.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Waypoints>\n");
        buf.append( ws + "</GraphEdge>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        GraphEdge o = (GraphEdge) anotherObj;
        if (EdgeID != o.EdgeID) return false;
        if (StartNode != o.StartNode) return false;
        if (EndNode != o.EndNode) return false;
         if (!Waypoints.equals( o.Waypoints)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
