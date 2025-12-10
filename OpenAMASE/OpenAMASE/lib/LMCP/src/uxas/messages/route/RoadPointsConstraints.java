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
 Start/end points for a road points request 
*/
public class RoadPointsConstraints extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 12;

    public static final String SERIES_NAME = "ROUTE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5931053054693474304L;
    public static final int SERIES_VERSION = 4;


    private static final String TYPE_NAME = "RoadPointsConstraints";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.route.RoadPointsConstraints";

    /**  ID denoting this set of road points constraints (Units: None)*/
    @LmcpType("int64")
    protected long RoadPointsID = 0L;
    /**  Location from which the road points will start (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D StartLocation = new afrl.cmasi.Location3D();
    /**  Location to which theroad points will end (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D EndLocation = new afrl.cmasi.Location3D();

    
    public RoadPointsConstraints() {
    }

    public RoadPointsConstraints(long RoadPointsID, afrl.cmasi.Location3D StartLocation, afrl.cmasi.Location3D EndLocation){
        this.RoadPointsID = RoadPointsID;
        this.StartLocation = StartLocation;
        this.EndLocation = EndLocation;
    }


    public RoadPointsConstraints clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            RoadPointsConstraints newObj = new RoadPointsConstraints();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID denoting this set of road points constraints (Units: None)*/
    public long getRoadPointsID() { return RoadPointsID; }

    /**  ID denoting this set of road points constraints (Units: None)*/
    public RoadPointsConstraints setRoadPointsID( long val ) {
        RoadPointsID = val;
        return this;
    }

    /**  Location from which the road points will start (Units: None)*/
    public afrl.cmasi.Location3D getStartLocation() { return StartLocation; }

    /**  Location from which the road points will start (Units: None)*/
    public RoadPointsConstraints setStartLocation( afrl.cmasi.Location3D val ) {
        StartLocation = val;
        return this;
    }

    /**  Location to which theroad points will end (Units: None)*/
    public afrl.cmasi.Location3D getEndLocation() { return EndLocation; }

    /**  Location to which theroad points will end (Units: None)*/
    public RoadPointsConstraints setEndLocation( afrl.cmasi.Location3D val ) {
        EndLocation = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += LMCPUtil.sizeOf(StartLocation);
        size += LMCPUtil.sizeOf(EndLocation);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        RoadPointsID = LMCPUtil.getInt64(in);

            StartLocation = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
            EndLocation = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, RoadPointsID);
        LMCPUtil.putObject(out, StartLocation);
        LMCPUtil.putObject(out, EndLocation);

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
        buf.append( ws + "<RoadPointsConstraints Series=\"ROUTE\">\n");
        buf.append( ws + "  <RoadPointsID>" + String.valueOf(RoadPointsID) + "</RoadPointsID>\n");
        if (StartLocation!= null){
           buf.append( ws + "  <StartLocation>\n");
           buf.append( ( StartLocation.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </StartLocation>\n");
        }
        if (EndLocation!= null){
           buf.append( ws + "  <EndLocation>\n");
           buf.append( ( EndLocation.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </EndLocation>\n");
        }
        buf.append( ws + "</RoadPointsConstraints>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        RoadPointsConstraints o = (RoadPointsConstraints) anotherObj;
        if (RoadPointsID != o.RoadPointsID) return false;
        if (StartLocation == null && o.StartLocation != null) return false;
        if ( StartLocation!= null && !StartLocation.equals(o.StartLocation)) return false;
        if (EndLocation == null && o.EndLocation != null) return false;
        if ( EndLocation!= null && !EndLocation.equals(o.EndLocation)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
