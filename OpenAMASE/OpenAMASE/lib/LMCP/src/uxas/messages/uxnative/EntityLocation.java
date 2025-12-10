// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages.uxnative;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Simple location broadcast from entities for tracking 
*/
public class EntityLocation extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 7;

    public static final String SERIES_NAME = "UXNATIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149751333668345413L;
    public static final int SERIES_VERSION = 9;


    private static final String TYPE_NAME = "EntityLocation";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.uxnative.EntityLocation";

    /**  Entity ID (Units: None)*/
    @LmcpType("int64")
    protected long EntityID = 0L;
    /**  Current location of entity. A valid EntityLocation must define Position (null not allowed) (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D Position = new afrl.cmasi.Location3D();
    /**  Corresponding time in milli-seconds since 1 Jan 1970 (Units: milliseconds)*/
    @LmcpType("int64")
    protected long Time = 0L;

    
    public EntityLocation() {
    }

    public EntityLocation(long EntityID, afrl.cmasi.Location3D Position, long Time){
        this.EntityID = EntityID;
        this.Position = Position;
        this.Time = Time;
    }


    public EntityLocation clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            EntityLocation newObj = new EntityLocation();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Entity ID (Units: None)*/
    public long getEntityID() { return EntityID; }

    /**  Entity ID (Units: None)*/
    public EntityLocation setEntityID( long val ) {
        EntityID = val;
        return this;
    }

    /**  Current location of entity. A valid EntityLocation must define Position (null not allowed) (Units: None)*/
    public afrl.cmasi.Location3D getPosition() { return Position; }

    /**  Current location of entity. A valid EntityLocation must define Position (null not allowed) (Units: None)*/
    public EntityLocation setPosition( afrl.cmasi.Location3D val ) {
        Position = val;
        return this;
    }

    /**  Corresponding time in milli-seconds since 1 Jan 1970 (Units: milliseconds)*/
    public long getTime() { return Time; }

    /**  Corresponding time in milli-seconds since 1 Jan 1970 (Units: milliseconds)*/
    public EntityLocation setTime( long val ) {
        Time = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types
        size += LMCPUtil.sizeOf(Position);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        EntityID = LMCPUtil.getInt64(in);

            Position = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        Time = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, EntityID);
        LMCPUtil.putObject(out, Position);
        LMCPUtil.putInt64(out, Time);

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
        buf.append( ws + "<EntityLocation Series=\"UXNATIVE\">\n");
        buf.append( ws + "  <EntityID>" + String.valueOf(EntityID) + "</EntityID>\n");
        if (Position!= null){
           buf.append( ws + "  <Position>\n");
           buf.append( ( Position.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Position>\n");
        }
        buf.append( ws + "  <Time>" + String.valueOf(Time) + "</Time>\n");
        buf.append( ws + "</EntityLocation>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        EntityLocation o = (EntityLocation) anotherObj;
        if (EntityID != o.EntityID) return false;
        if (Position == null && o.Position != null) return false;
        if ( Position!= null && !Position.equals(o.Position)) return false;
        if (Time != o.Time) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
