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
 Entity Exit 
*/
public class EntityExit extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 15;

    public static final String SERIES_NAME = "UXNATIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149751333668345413L;
    public static final int SERIES_VERSION = 9;


    private static final String TYPE_NAME = "EntityExit";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.uxnative.EntityExit";

    /**  ID of entity exiting network (Units: None)*/
    @LmcpType("int64")
    protected long EntityID = 0L;
    /**  Label of entity exiting network (Units: None)*/
    @LmcpType("string")
    protected String Label = "";

    
    public EntityExit() {
    }

    public EntityExit(long EntityID, String Label){
        this.EntityID = EntityID;
        this.Label = Label;
    }


    public EntityExit clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            EntityExit newObj = new EntityExit();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID of entity exiting network (Units: None)*/
    public long getEntityID() { return EntityID; }

    /**  ID of entity exiting network (Units: None)*/
    public EntityExit setEntityID( long val ) {
        EntityID = val;
        return this;
    }

    /**  Label of entity exiting network (Units: None)*/
    public String getLabel() { return Label; }

    /**  Label of entity exiting network (Units: None)*/
    public EntityExit setLabel( String val ) {
        Label = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += LMCPUtil.sizeOfString(Label);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        EntityID = LMCPUtil.getInt64(in);

        Label = LMCPUtil.getString(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, EntityID);
        LMCPUtil.putString(out, Label);

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
        buf.append( ws + "<EntityExit Series=\"UXNATIVE\">\n");
        buf.append( ws + "  <EntityID>" + String.valueOf(EntityID) + "</EntityID>\n");
        buf.append( ws + "  <Label>" + String.valueOf(Label) + "</Label>\n");
        buf.append( ws + "</EntityExit>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        EntityExit o = (EntityExit) anotherObj;
        if (EntityID != o.EntityID) return false;
        if (Label == null && o.Label != null) return false;
        if ( Label!= null && !Label.equals(o.Label)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
