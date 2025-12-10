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
 Indicates a zone violation at a given time and position by a given vehicle 
*/
public class ZoneViolation extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 3;

    public static final String SERIES_NAME = "ALERTS";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4705211930599686144L;
    public static final int SERIES_VERSION = 1;


    private static final String TYPE_NAME = "ZoneViolation";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.ZoneViolation";

    /**  ID of the vehicle with imminent zone violation (Units: None)*/
    @LmcpType("int64")
    protected long VehicleID = 0L;
    /**  ID of the PROCESSED zone that will be imminently violated (Units: None)*/
    @LmcpType("int64")
    protected long ZoneID = 0L;
    /**  Type of Zone that will be violated (Units: None)*/
    @LmcpType("bool")
    protected boolean KeepIn;
    /**  Time of predicted violation in absolute clock time of milliseconds since 1970 (Units: None)*/
    @LmcpType("int64")
    protected long TimeToIntercept = 0L;
    /**  Position (absolute) of expected violation (Units: None)*/
    @LmcpType("Position2D")
    protected uxas.messages.Position2D InterceptPosition = new uxas.messages.Position2D();
    /**  Position (absolute) of expected violation in latitude and longitude(Units: None)*/
    @LmcpType("Position2D")
    protected uxas.messages.Position2D InterceptPositionLatLong = new uxas.messages.Position2D();

    
    public ZoneViolation() {
    }

    public ZoneViolation(long VehicleID, long ZoneID, boolean KeepIn, long TimeToIntercept, uxas.messages.Position2D InterceptPosition, uxas.messages.Position2D InterceptPositionLatLong){
        this.VehicleID = VehicleID;
        this.ZoneID = ZoneID;
        this.KeepIn = KeepIn;
        this.TimeToIntercept = TimeToIntercept;
        this.InterceptPosition = InterceptPosition;
        this.InterceptPositionLatLong = InterceptPositionLatLong;
    }


    public ZoneViolation clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            ZoneViolation newObj = new ZoneViolation();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID of the vehicle with imminent zone violation (Units: None)*/
    public long getVehicleID() { return VehicleID; }

    /**  ID of the vehicle with imminent zone violation (Units: None)*/
    public ZoneViolation setVehicleID( long val ) {
        VehicleID = val;
        return this;
    }

    /**  ID of the PROCESSED zone that will be imminently violated (Units: None)*/
    public long getZoneID() { return ZoneID; }

    /**  ID of the PROCESSED zone that will be imminently violated (Units: None)*/
    public ZoneViolation setZoneID( long val ) {
        ZoneID = val;
        return this;
    }

    /**  Type of Zone that will be violated (Units: None)*/
    public boolean getKeepIn() { return KeepIn; }

    /**  Type of Zone that will be violated (Units: None)*/
    public ZoneViolation setKeepIn( boolean val ) {
        KeepIn = val;
        return this;
    }

    /**  Time of predicted violation in absolute clock time of milliseconds since 1970 (Units: None)*/
    public long getTimeToIntercept() { return TimeToIntercept; }

    /**  Time of predicted violation in absolute clock time of milliseconds since 1970 (Units: None)*/
    public ZoneViolation setTimeToIntercept( long val ) {
        TimeToIntercept = val;
        return this;
    }

    /**  Position (absolute) of expected violation (Units: None)*/
    public uxas.messages.Position2D getInterceptPosition() { return InterceptPosition; }

    /**  Position (absolute) of expected violation (Units: None)*/
    public ZoneViolation setInterceptPosition( uxas.messages.Position2D val ) {
        InterceptPosition = val;
        return this;
    }

    /**  Position (absolute) of expected violation in latitude and longitude(Units: None)*/
    public uxas.messages.Position2D getInterceptPositionLatLong() { return InterceptPositionLatLong; }

    /**  Position (absolute) of expected violation in latitude and longitude(Units: None)*/
    public ZoneViolation setInterceptPositionLatLong( uxas.messages.Position2D val ) {
        InterceptPositionLatLong = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 25; // accounts for primitive types
        size += LMCPUtil.sizeOf(InterceptPosition);
        size += LMCPUtil.sizeOf(InterceptPositionLatLong);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        VehicleID = LMCPUtil.getInt64(in);

        ZoneID = LMCPUtil.getInt64(in);

        KeepIn = LMCPUtil.getBool(in);

        TimeToIntercept = LMCPUtil.getInt64(in);

            InterceptPosition = (uxas.messages.Position2D) LMCPUtil.getObject(in);
            InterceptPositionLatLong = (uxas.messages.Position2D) LMCPUtil.getObject(in);

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, VehicleID);
        LMCPUtil.putInt64(out, ZoneID);
        LMCPUtil.putBool(out, KeepIn);
        LMCPUtil.putInt64(out, TimeToIntercept);
        LMCPUtil.putObject(out, InterceptPosition);
        LMCPUtil.putObject(out, InterceptPositionLatLong);

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
        buf.append( ws + "<ZoneViolation Series=\"ALERTS\">\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <ZoneID>" + String.valueOf(ZoneID) + "</ZoneID>\n");
        buf.append( ws + "  <KeepIn>" + String.valueOf(KeepIn) + "</KeepIn>\n");
        buf.append( ws + "  <TimeToIntercept>" + String.valueOf(TimeToIntercept) + "</TimeToIntercept>\n");
        if (InterceptPosition!= null){
           buf.append( ws + "  <InterceptPosition>\n");
           buf.append( ( InterceptPosition.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </InterceptPosition>\n");
        }
        if (InterceptPositionLatLong!= null){
           buf.append( ws + "  <InterceptPositionLatLong>\n");
           buf.append( ( InterceptPositionLatLong.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </InterceptPositionLatLong>\n");
        }
        buf.append( ws + "</ZoneViolation>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        ZoneViolation o = (ZoneViolation) anotherObj;
        if (VehicleID != o.VehicleID) return false;
        if (ZoneID != o.ZoneID) return false;
        if (KeepIn != o.KeepIn) return false;
        if (TimeToIntercept != o.TimeToIntercept) return false;
        if (InterceptPosition == null && o.InterceptPosition != null) return false;
        if ( InterceptPosition!= null && !InterceptPosition.equals(o.InterceptPosition)) return false;
        if (InterceptPositionLatLong == null && o.InterceptPositionLatLong != null) return false;
        if ( InterceptPositionLatLong!= null && !InterceptPositionLatLong.equals(o.InterceptPositionLatLong)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
