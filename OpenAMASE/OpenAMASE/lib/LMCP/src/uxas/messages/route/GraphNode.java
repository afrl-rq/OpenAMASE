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
 Defines a base graph node for a geo-spatial graph. 
*/
public class GraphNode extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 1;

    public static final String SERIES_NAME = "ROUTE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5931053054693474304L;
    public static final int SERIES_VERSION = 4;


    private static final String TYPE_NAME = "GraphNode";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.route.GraphNode";

    /**  Unique ID for the node, used to build edges between nodes (Units: None)*/
    @LmcpType("int64")
    protected long NodeID = 0L;
    /**  Lat/Lon coordinates of node. A valid GraphNode must define Coordinates (null not allowed). (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D Coordinates = new afrl.cmasi.Location3D();
    /**  IDs of edges that have this node as an endpoint (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> AssociatedEdges = new java.util.ArrayList<Long>();

    
    public GraphNode() {
    }

    public GraphNode(long NodeID, afrl.cmasi.Location3D Coordinates){
        this.NodeID = NodeID;
        this.Coordinates = Coordinates;
    }


    public GraphNode clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            GraphNode newObj = new GraphNode();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Unique ID for the node, used to build edges between nodes (Units: None)*/
    public long getNodeID() { return NodeID; }

    /**  Unique ID for the node, used to build edges between nodes (Units: None)*/
    public GraphNode setNodeID( long val ) {
        NodeID = val;
        return this;
    }

    /**  Lat/Lon coordinates of node. A valid GraphNode must define Coordinates (null not allowed). (Units: None)*/
    public afrl.cmasi.Location3D getCoordinates() { return Coordinates; }

    /**  Lat/Lon coordinates of node. A valid GraphNode must define Coordinates (null not allowed). (Units: None)*/
    public GraphNode setCoordinates( afrl.cmasi.Location3D val ) {
        Coordinates = val;
        return this;
    }

    public java.util.ArrayList<Long> getAssociatedEdges() {
        return AssociatedEdges;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += LMCPUtil.sizeOf(Coordinates);
        
        size += 2 + 8 * AssociatedEdges.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        NodeID = LMCPUtil.getInt64(in);

            Coordinates = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        AssociatedEdges.clear();
        int AssociatedEdges_len = LMCPUtil.getUint16(in);
        for(int i=0; i<AssociatedEdges_len; i++){
            AssociatedEdges.add(LMCPUtil.getInt64(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, NodeID);
        LMCPUtil.putObject(out, Coordinates);
        LMCPUtil.putUint16(out, AssociatedEdges.size());
        for(int i=0; i<AssociatedEdges.size(); i++){
            LMCPUtil.putInt64(out, AssociatedEdges.get(i));
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
        buf.append( ws + "<GraphNode Series=\"ROUTE\">\n");
        buf.append( ws + "  <NodeID>" + String.valueOf(NodeID) + "</NodeID>\n");
        if (Coordinates!= null){
           buf.append( ws + "  <Coordinates>\n");
           buf.append( ( Coordinates.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Coordinates>\n");
        }
        buf.append( ws + "  <AssociatedEdges>\n");
        for (int i=0; i<AssociatedEdges.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedEdges.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedEdges>\n");
        buf.append( ws + "</GraphNode>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        GraphNode o = (GraphNode) anotherObj;
        if (NodeID != o.NodeID) return false;
        if (Coordinates == null && o.Coordinates != null) return false;
        if ( Coordinates!= null && !Coordinates.equals(o.Coordinates)) return false;
         if (!AssociatedEdges.equals( o.AssociatedEdges)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
