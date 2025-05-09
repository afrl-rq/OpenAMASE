// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Indicates a zone that has been processed to detect zone violations. Such zones             do not refer to thedeclared zones as this zone is the result of padding zones             and merging them where they overlap in 2D space 
*/
public class ProcessedZone extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 6;

    public static final String SERIES_NAME = "ALERTS";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4705211930599686144L;
    public static final int SERIES_VERSION = 1;


    private static final String TYPE_NAME = "ProcessedZone";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.ProcessedZone";

    /**  The ID given to the processed zone(Units: None)*/
    @LmcpType("int64")
    protected long ZoneID = 0L;
    /**  If the processed zone is keep in or keep out(Units: None)*/
    @LmcpType("bool")
    protected boolean KeepIn;
    /**  The vertices of the processed zone in flat x,y space(Units: None)*/
    @LmcpType("ZoneVertex")
    protected java.util.ArrayList<uxas.messages.ZoneVertex> Vertices = new java.util.ArrayList<uxas.messages.ZoneVertex>();

    
    public ProcessedZone() {
    }

    public ProcessedZone(long ZoneID, boolean KeepIn){
        this.ZoneID = ZoneID;
        this.KeepIn = KeepIn;
    }


    public ProcessedZone clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            ProcessedZone newObj = new ProcessedZone();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The ID given to the processed zone(Units: None)*/
    public long getZoneID() { return ZoneID; }

    /**  The ID given to the processed zone(Units: None)*/
    public ProcessedZone setZoneID( long val ) {
        ZoneID = val;
        return this;
    }

    /**  If the processed zone is keep in or keep out(Units: None)*/
    public boolean getKeepIn() { return KeepIn; }

    /**  If the processed zone is keep in or keep out(Units: None)*/
    public ProcessedZone setKeepIn( boolean val ) {
        KeepIn = val;
        return this;
    }

    public java.util.ArrayList<uxas.messages.ZoneVertex> getVertices() {
        return Vertices;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 9; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(Vertices);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ZoneID = LMCPUtil.getInt64(in);

        KeepIn = LMCPUtil.getBool(in);

        Vertices.clear();
        int Vertices_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Vertices_len; i++){
        Vertices.add( (uxas.messages.ZoneVertex) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ZoneID);
        LMCPUtil.putBool(out, KeepIn);
        LMCPUtil.putUint16(out, Vertices.size());
        for(int i=0; i<Vertices.size(); i++){
            LMCPUtil.putObject(out, Vertices.get(i));
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
        buf.append( ws + "<ProcessedZone Series=\"ALERTS\">\n");
        buf.append( ws + "  <ZoneID>" + String.valueOf(ZoneID) + "</ZoneID>\n");
        buf.append( ws + "  <KeepIn>" + String.valueOf(KeepIn) + "</KeepIn>\n");
        buf.append( ws + "  <Vertices>\n");
        for (int i=0; i<Vertices.size(); i++) {
            buf.append( Vertices.get(i) == null ? ( ws + "    <null/>\n") : (Vertices.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Vertices>\n");
        buf.append( ws + "</ProcessedZone>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        ProcessedZone o = (ProcessedZone) anotherObj;
        if (ZoneID != o.ZoneID) return false;
        if (KeepIn != o.KeepIn) return false;
         if (!Vertices.equals( o.Vertices)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
