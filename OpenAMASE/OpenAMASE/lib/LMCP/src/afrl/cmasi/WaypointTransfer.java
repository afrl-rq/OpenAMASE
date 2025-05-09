// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.cmasi;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Handles the transfer of waypoints.  This does not affect the current navigation of the              aircraft.  This can be used to transfer, or request the transfer of, waypoints. 
*/
public class WaypointTransfer extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 59;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "WaypointTransfer";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.WaypointTransfer";

    /**  ID of the entity assoicated with the waypoints (Units: None)*/
    @LmcpType("int64")
    protected long EntityID = 0L;
    /**  A list of wayppoints to transfer. This may be empty if the transfer type is "RequestWaypoints" or "ClearWaypoints" (Units: None)*/
    @LmcpType("Waypoint")
    protected java.util.ArrayList<afrl.cmasi.Waypoint> Waypoints = new java.util.ArrayList<afrl.cmasi.Waypoint>();
    /**  describes the transfer action to take (Units: None)*/
    @LmcpType("WaypointTransferMode")
    protected afrl.cmasi.WaypointTransferMode TransferMode = afrl.cmasi.WaypointTransferMode.AddWaypoints;

    
    public WaypointTransfer() {
    }

    public WaypointTransfer(long EntityID, afrl.cmasi.WaypointTransferMode TransferMode){
        this.EntityID = EntityID;
        this.TransferMode = TransferMode;
    }


    public WaypointTransfer clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            WaypointTransfer newObj = new WaypointTransfer();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID of the entity assoicated with the waypoints (Units: None)*/
    public long getEntityID() { return EntityID; }

    /**  ID of the entity assoicated with the waypoints (Units: None)*/
    public WaypointTransfer setEntityID( long val ) {
        EntityID = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.Waypoint> getWaypoints() {
        return Waypoints;
    }

    /**  describes the transfer action to take (Units: None)*/
    public afrl.cmasi.WaypointTransferMode getTransferMode() { return TransferMode; }

    /**  describes the transfer action to take (Units: None)*/
    public WaypointTransfer setTransferMode( afrl.cmasi.WaypointTransferMode val ) {
        TransferMode = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 12; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(Waypoints);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        EntityID = LMCPUtil.getInt64(in);

        Waypoints.clear();
        int Waypoints_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Waypoints_len; i++){
        Waypoints.add( (afrl.cmasi.Waypoint) LMCPUtil.getObject(in));
        }
        TransferMode = afrl.cmasi.WaypointTransferMode.unpack( in );


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, EntityID);
        LMCPUtil.putUint16(out, Waypoints.size());
        for(int i=0; i<Waypoints.size(); i++){
            LMCPUtil.putObject(out, Waypoints.get(i));
        }
        TransferMode.pack(out);

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
        buf.append( ws + "<WaypointTransfer Series=\"CMASI\">\n");
        buf.append( ws + "  <EntityID>" + String.valueOf(EntityID) + "</EntityID>\n");
        buf.append( ws + "  <Waypoints>\n");
        for (int i=0; i<Waypoints.size(); i++) {
            buf.append( Waypoints.get(i) == null ? ( ws + "    <null/>\n") : (Waypoints.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Waypoints>\n");
        buf.append( ws + "  <TransferMode>" + String.valueOf(TransferMode) + "</TransferMode>\n");
        buf.append( ws + "</WaypointTransfer>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        WaypointTransfer o = (WaypointTransfer) anotherObj;
        if (EntityID != o.EntityID) return false;
         if (!Waypoints.equals( o.Waypoints)) return false;
        if (TransferMode != o.TransferMode) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
