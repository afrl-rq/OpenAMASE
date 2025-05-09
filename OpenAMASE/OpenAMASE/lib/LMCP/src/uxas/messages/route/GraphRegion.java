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
 Defines a complete geo-spatial graph 
*/
public class GraphRegion extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 3;

    public static final String SERIES_NAME = "ROUTE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5931053054693474304L;
    public static final int SERIES_VERSION = 4;


    private static final String TYPE_NAME = "GraphRegion";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.route.GraphRegion";

    /**  ID for full region data structure (Units: None)*/
    @LmcpType("int64")
    protected long ID = 0L;
    /**  List of all nodes that represent the network (note 2^24 max) (Units: None)*/
    @LmcpType("GraphNode")
    protected java.util.ArrayList<uxas.messages.route.GraphNode> NodeList = new java.util.ArrayList<uxas.messages.route.GraphNode>();
    /**  List of all edges that represent the network (note 2^24 max) (Units: None)*/
    @LmcpType("GraphEdge")
    protected java.util.ArrayList<uxas.messages.route.GraphEdge> EdgeList = new java.util.ArrayList<uxas.messages.route.GraphEdge>();

    
    public GraphRegion() {
    }

    public GraphRegion(long ID){
        this.ID = ID;
    }


    public GraphRegion clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            GraphRegion newObj = new GraphRegion();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID for full region data structure (Units: None)*/
    public long getID() { return ID; }

    /**  ID for full region data structure (Units: None)*/
    public GraphRegion setID( long val ) {
        ID = val;
        return this;
    }

    public java.util.ArrayList<uxas.messages.route.GraphNode> getNodeList() {
        return NodeList;
    }

    public java.util.ArrayList<uxas.messages.route.GraphEdge> getEdgeList() {
        return EdgeList;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += 4;
        size += LMCPUtil.sizeOfList(NodeList);
        size += 4;
        size += LMCPUtil.sizeOfList(EdgeList);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ID = LMCPUtil.getInt64(in);

        NodeList.clear();
        long NodeList_len = LMCPUtil.getUint32(in);
        for(int i=0; i<NodeList_len; i++){
        NodeList.add( (uxas.messages.route.GraphNode) LMCPUtil.getObject(in));
        }
        EdgeList.clear();
        long EdgeList_len = LMCPUtil.getUint32(in);
        for(int i=0; i<EdgeList_len; i++){
        EdgeList.add( (uxas.messages.route.GraphEdge) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ID);
        LMCPUtil.putUint32(out, NodeList.size());
        for(int i=0; i<NodeList.size(); i++){
            LMCPUtil.putObject(out, NodeList.get(i));
        }
        LMCPUtil.putUint32(out, EdgeList.size());
        for(int i=0; i<EdgeList.size(); i++){
            LMCPUtil.putObject(out, EdgeList.get(i));
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
        buf.append( ws + "<GraphRegion Series=\"ROUTE\">\n");
        buf.append( ws + "  <ID>" + String.valueOf(ID) + "</ID>\n");
        buf.append( ws + "  <NodeList>\n");
        for (int i=0; i<NodeList.size(); i++) {
            buf.append( NodeList.get(i) == null ? ( ws + "    <null/>\n") : (NodeList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </NodeList>\n");
        buf.append( ws + "  <EdgeList>\n");
        for (int i=0; i<EdgeList.size(); i++) {
            buf.append( EdgeList.get(i) == null ? ( ws + "    <null/>\n") : (EdgeList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </EdgeList>\n");
        buf.append( ws + "</GraphRegion>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        GraphRegion o = (GraphRegion) anotherObj;
        if (ID != o.ID) return false;
         if (!NodeList.equals( o.NodeList)) return false;
         if (!EdgeList.equals( o.EdgeList)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
