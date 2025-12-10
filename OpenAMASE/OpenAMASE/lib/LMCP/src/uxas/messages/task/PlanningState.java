// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages.task;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 The state of an entity to be used as the initial planning state when         constructing assignments/plans. 
*/
public class PlanningState extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 3;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "PlanningState";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.PlanningState";

    /**  Identifier of the entitiy (Units: None)*/
    @LmcpType("int64")
    protected long EntityID = 0L;
    /**  Position of this entity for the plan. A valid PlanningState must define PlanningPosition (null not allowed). (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D PlanningPosition = new afrl.cmasi.Location3D();
    /**  Heading of this entity for the plan (Units: None)*/
    @LmcpType("real32")
    protected float PlanningHeading = (float)0;

    
    public PlanningState() {
    }

    public PlanningState(long EntityID, afrl.cmasi.Location3D PlanningPosition, float PlanningHeading){
        this.EntityID = EntityID;
        this.PlanningPosition = PlanningPosition;
        this.PlanningHeading = PlanningHeading;
    }


    public PlanningState clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            PlanningState newObj = new PlanningState();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Identifier of the entitiy (Units: None)*/
    public long getEntityID() { return EntityID; }

    /**  Identifier of the entitiy (Units: None)*/
    public PlanningState setEntityID( long val ) {
        EntityID = val;
        return this;
    }

    /**  Position of this entity for the plan. A valid PlanningState must define PlanningPosition (null not allowed). (Units: None)*/
    public afrl.cmasi.Location3D getPlanningPosition() { return PlanningPosition; }

    /**  Position of this entity for the plan. A valid PlanningState must define PlanningPosition (null not allowed). (Units: None)*/
    public PlanningState setPlanningPosition( afrl.cmasi.Location3D val ) {
        PlanningPosition = val;
        return this;
    }

    /**  Heading of this entity for the plan (Units: None)*/
    public float getPlanningHeading() { return PlanningHeading; }

    /**  Heading of this entity for the plan (Units: None)*/
    public PlanningState setPlanningHeading( float val ) {
        PlanningHeading = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 12; // accounts for primitive types
        size += LMCPUtil.sizeOf(PlanningPosition);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        EntityID = LMCPUtil.getInt64(in);

            PlanningPosition = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        PlanningHeading = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, EntityID);
        LMCPUtil.putObject(out, PlanningPosition);
        LMCPUtil.putReal32(out, PlanningHeading);

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
        buf.append( ws + "<PlanningState Series=\"UXTASK\">\n");
        buf.append( ws + "  <EntityID>" + String.valueOf(EntityID) + "</EntityID>\n");
        if (PlanningPosition!= null){
           buf.append( ws + "  <PlanningPosition>\n");
           buf.append( ( PlanningPosition.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </PlanningPosition>\n");
        }
        buf.append( ws + "  <PlanningHeading>" + String.valueOf(PlanningHeading) + "</PlanningHeading>\n");
        buf.append( ws + "</PlanningState>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        PlanningState o = (PlanningState) anotherObj;
        if (EntityID != o.EntityID) return false;
        if (PlanningPosition == null && o.PlanningPosition != null) return false;
        if ( PlanningPosition!= null && !PlanningPosition.equals(o.PlanningPosition)) return false;
        if (PlanningHeading != o.PlanningHeading) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)PlanningHeading;

        return hash + super.hashCode();
    }
    
}
