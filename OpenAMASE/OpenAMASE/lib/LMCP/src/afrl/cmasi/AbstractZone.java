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
 Base Type for all zones (keep-in and keep-out zones) 
*/
public class AbstractZone extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 10;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "AbstractZone";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.AbstractZone";

    /**  A globally unique reference number for this zone (Units: None)*/
    @LmcpType("int64")
    protected long ZoneID = 0L;
    /**  Lower altitude bound for the zone (Units: meter)*/
    @LmcpType("real32")
    protected float MinAltitude = (float)0;
    /**  Altitude type for min altitude (Units: None)*/
    @LmcpType("AltitudeType")
    protected afrl.cmasi.AltitudeType MinAltitudeType = afrl.cmasi.AltitudeType.AGL;
    /**  Maximum altitude for operations (Units: meter)*/
    @LmcpType("real32")
    protected float MaxAltitude = (float)0;
    /**  Altitude type for max altitude (Units: None)*/
    @LmcpType("AltitudeType")
    protected afrl.cmasi.AltitudeType MaxAltitudeType = afrl.cmasi.AltitudeType.MSL;
    /**  A list of aircraft IDs that this zone applies to. If the list is empty, then it is assumed that the boundary applies to all aircraft. (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> AffectedAircraft = new java.util.ArrayList<Long>();
    /**  Time at which this zone becomes active. Time datum is defined by the application, but unless otherwise specified is milliseconds since 1 Jan 1970 (Units: milliseconds)*/
    @LmcpType("int64")
    protected long StartTime = 0L;
    /**  Time at which this zone becomes inactive. Time datum is defined by the application, but unless otherwise specified is milliseconds since 1 Jan 1970 (Units: milliseconds)*/
    @LmcpType("int64")
    protected long EndTime = 0L;
    /**  Buffer to add/subtract around the border of the zone (Units: meters)*/
    @LmcpType("real32")
    protected float Padding = (float)0;
    /**  Optional label for this zone (Units: None)*/
    @LmcpType("string")
    protected String Label = "";
    /**  Geometry object describing the boundary. This boundary is 2-dimensional. The zone boundary is defined as an extrusion of this boundary from MinAltitude to MaxAltitude. A valid zone must define a boundary (null not allowed). (Units: None)*/
    @LmcpType("AbstractGeometry")
    protected afrl.cmasi.AbstractGeometry Boundary = new afrl.cmasi.AbstractGeometry();

    
    public AbstractZone() {
    }

    public AbstractZone(long ZoneID, float MinAltitude, afrl.cmasi.AltitudeType MinAltitudeType, float MaxAltitude, afrl.cmasi.AltitudeType MaxAltitudeType, long StartTime, long EndTime, float Padding, String Label, afrl.cmasi.AbstractGeometry Boundary){
        this.ZoneID = ZoneID;
        this.MinAltitude = MinAltitude;
        this.MinAltitudeType = MinAltitudeType;
        this.MaxAltitude = MaxAltitude;
        this.MaxAltitudeType = MaxAltitudeType;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.Padding = Padding;
        this.Label = Label;
        this.Boundary = Boundary;
    }


    public AbstractZone clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            AbstractZone newObj = new AbstractZone();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  A globally unique reference number for this zone (Units: None)*/
    public long getZoneID() { return ZoneID; }

    /**  A globally unique reference number for this zone (Units: None)*/
    public AbstractZone setZoneID( long val ) {
        ZoneID = val;
        return this;
    }

    /**  Lower altitude bound for the zone (Units: meter)*/
    public float getMinAltitude() { return MinAltitude; }

    /**  Lower altitude bound for the zone (Units: meter)*/
    public AbstractZone setMinAltitude( float val ) {
        MinAltitude = val;
        return this;
    }

    /**  Altitude type for min altitude (Units: None)*/
    public afrl.cmasi.AltitudeType getMinAltitudeType() { return MinAltitudeType; }

    /**  Altitude type for min altitude (Units: None)*/
    public AbstractZone setMinAltitudeType( afrl.cmasi.AltitudeType val ) {
        MinAltitudeType = val;
        return this;
    }

    /**  Maximum altitude for operations (Units: meter)*/
    public float getMaxAltitude() { return MaxAltitude; }

    /**  Maximum altitude for operations (Units: meter)*/
    public AbstractZone setMaxAltitude( float val ) {
        MaxAltitude = val;
        return this;
    }

    /**  Altitude type for max altitude (Units: None)*/
    public afrl.cmasi.AltitudeType getMaxAltitudeType() { return MaxAltitudeType; }

    /**  Altitude type for max altitude (Units: None)*/
    public AbstractZone setMaxAltitudeType( afrl.cmasi.AltitudeType val ) {
        MaxAltitudeType = val;
        return this;
    }

    public java.util.ArrayList<Long> getAffectedAircraft() {
        return AffectedAircraft;
    }

    /**  Time at which this zone becomes active. Time datum is defined by the application, but unless otherwise specified is milliseconds since 1 Jan 1970 (Units: milliseconds)*/
    public long getStartTime() { return StartTime; }

    /**  Time at which this zone becomes active. Time datum is defined by the application, but unless otherwise specified is milliseconds since 1 Jan 1970 (Units: milliseconds)*/
    public AbstractZone setStartTime( long val ) {
        StartTime = val;
        return this;
    }

    /**  Time at which this zone becomes inactive. Time datum is defined by the application, but unless otherwise specified is milliseconds since 1 Jan 1970 (Units: milliseconds)*/
    public long getEndTime() { return EndTime; }

    /**  Time at which this zone becomes inactive. Time datum is defined by the application, but unless otherwise specified is milliseconds since 1 Jan 1970 (Units: milliseconds)*/
    public AbstractZone setEndTime( long val ) {
        EndTime = val;
        return this;
    }

    /**  Buffer to add/subtract around the border of the zone (Units: meters)*/
    public float getPadding() { return Padding; }

    /**  Buffer to add/subtract around the border of the zone (Units: meters)*/
    public AbstractZone setPadding( float val ) {
        Padding = val;
        return this;
    }

    /**  Optional label for this zone (Units: None)*/
    public String getLabel() { return Label; }

    /**  Optional label for this zone (Units: None)*/
    public AbstractZone setLabel( String val ) {
        Label = val;
        return this;
    }

    /**  Geometry object describing the boundary. This boundary is 2-dimensional. The zone boundary is defined as an extrusion of this boundary from MinAltitude to MaxAltitude. A valid zone must define a boundary (null not allowed). (Units: None)*/
    public afrl.cmasi.AbstractGeometry getBoundary() { return Boundary; }

    /**  Geometry object describing the boundary. This boundary is 2-dimensional. The zone boundary is defined as an extrusion of this boundary from MinAltitude to MaxAltitude. A valid zone must define a boundary (null not allowed). (Units: None)*/
    public AbstractZone setBoundary( afrl.cmasi.AbstractGeometry val ) {
        Boundary = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 44; // accounts for primitive types
        
        size += 2 + 8 * AffectedAircraft.size();
        size += LMCPUtil.sizeOfString(Label);
        size += LMCPUtil.sizeOf(Boundary);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ZoneID = LMCPUtil.getInt64(in);

        MinAltitude = LMCPUtil.getReal32(in);

        MinAltitudeType = afrl.cmasi.AltitudeType.unpack( in );

        MaxAltitude = LMCPUtil.getReal32(in);

        MaxAltitudeType = afrl.cmasi.AltitudeType.unpack( in );

        AffectedAircraft.clear();
        int AffectedAircraft_len = LMCPUtil.getUint16(in);
        for(int i=0; i<AffectedAircraft_len; i++){
            AffectedAircraft.add(LMCPUtil.getInt64(in));
        }
        StartTime = LMCPUtil.getInt64(in);

        EndTime = LMCPUtil.getInt64(in);

        Padding = LMCPUtil.getReal32(in);

        Label = LMCPUtil.getString(in);

            Boundary = (afrl.cmasi.AbstractGeometry) LMCPUtil.getObject(in);

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ZoneID);
        LMCPUtil.putReal32(out, MinAltitude);
        MinAltitudeType.pack(out);
        LMCPUtil.putReal32(out, MaxAltitude);
        MaxAltitudeType.pack(out);
        LMCPUtil.putUint16(out, AffectedAircraft.size());
        for(int i=0; i<AffectedAircraft.size(); i++){
            LMCPUtil.putInt64(out, AffectedAircraft.get(i));
        }
        LMCPUtil.putInt64(out, StartTime);
        LMCPUtil.putInt64(out, EndTime);
        LMCPUtil.putReal32(out, Padding);
        LMCPUtil.putString(out, Label);
        LMCPUtil.putObject(out, Boundary);

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
        buf.append( ws + "<AbstractZone Series=\"CMASI\">\n");
        buf.append( ws + "  <ZoneID>" + String.valueOf(ZoneID) + "</ZoneID>\n");
        buf.append( ws + "  <MinAltitude>" + String.valueOf(MinAltitude) + "</MinAltitude>\n");
        buf.append( ws + "  <MinAltitudeType>" + String.valueOf(MinAltitudeType) + "</MinAltitudeType>\n");
        buf.append( ws + "  <MaxAltitude>" + String.valueOf(MaxAltitude) + "</MaxAltitude>\n");
        buf.append( ws + "  <MaxAltitudeType>" + String.valueOf(MaxAltitudeType) + "</MaxAltitudeType>\n");
        buf.append( ws + "  <AffectedAircraft>\n");
        for (int i=0; i<AffectedAircraft.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AffectedAircraft.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AffectedAircraft>\n");
        buf.append( ws + "  <StartTime>" + String.valueOf(StartTime) + "</StartTime>\n");
        buf.append( ws + "  <EndTime>" + String.valueOf(EndTime) + "</EndTime>\n");
        buf.append( ws + "  <Padding>" + String.valueOf(Padding) + "</Padding>\n");
        buf.append( ws + "  <Label>" + String.valueOf(Label) + "</Label>\n");
        if (Boundary!= null){
           buf.append( ws + "  <Boundary>\n");
           buf.append( ( Boundary.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Boundary>\n");
        }
        buf.append( ws + "</AbstractZone>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        AbstractZone o = (AbstractZone) anotherObj;
        if (ZoneID != o.ZoneID) return false;
        if (MinAltitude != o.MinAltitude) return false;
        if (MinAltitudeType != o.MinAltitudeType) return false;
        if (MaxAltitude != o.MaxAltitude) return false;
        if (MaxAltitudeType != o.MaxAltitudeType) return false;
         if (!AffectedAircraft.equals( o.AffectedAircraft)) return false;
        if (StartTime != o.StartTime) return false;
        if (EndTime != o.EndTime) return false;
        if (Padding != o.Padding) return false;
        if (Label == null && o.Label != null) return false;
        if ( Label!= null && !Label.equals(o.Label)) return false;
        if (Boundary == null && o.Boundary != null) return false;
        if ( Boundary!= null && !Boundary.equals(o.Boundary)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)MinAltitude;
        hash += 31 * (int)MaxAltitude;

        return hash + super.hashCode();
    }
    
}
