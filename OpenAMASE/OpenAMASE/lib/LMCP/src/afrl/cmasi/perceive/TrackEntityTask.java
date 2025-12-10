// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.cmasi.perceive;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
            A request to track a specific entity.  It is assumed that the tracked entity can move at any time during the scenario.        
*/
public class TrackEntityTask extends afrl.cmasi.Task {
    
    public static final int LMCP_TYPE = 3;

    public static final String SERIES_NAME = "PERCEIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5784119745305990725L;
    public static final int SERIES_VERSION = 1;


    private static final String TYPE_NAME = "TrackEntityTask";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.perceive.TrackEntityTask";

    /**  The ID of the target to be tracked. (Units: None)*/
    @LmcpType("uint32")
    protected long EntityID = 0L;
    /**  Required sensor modality for tracking the target (Units: None)*/
    @LmcpType("WavelengthBand")
    protected afrl.cmasi.WavelengthBand SensorModality = afrl.cmasi.WavelengthBand.AllAny;
    /**  The minimum ground sample distance that should be maintained for this target track operation. (Units: meter)*/
    @LmcpType("real32")
    protected float GroundSampleDistance = (float)0;

    
    public TrackEntityTask() {
    }

    public TrackEntityTask(long TaskID, String Label, float RevisitRate, short Priority, boolean Required, long EntityID, afrl.cmasi.WavelengthBand SensorModality, float GroundSampleDistance){
        this.TaskID = TaskID;
        this.Label = Label;
        this.RevisitRate = RevisitRate;
        this.Priority = Priority;
        this.Required = Required;
        this.EntityID = EntityID;
        this.SensorModality = SensorModality;
        this.GroundSampleDistance = GroundSampleDistance;
    }


    public TrackEntityTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            TrackEntityTask newObj = new TrackEntityTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The ID of the target to be tracked. (Units: None)*/
    public long getEntityID() { return EntityID; }

    /**  The ID of the target to be tracked. (Units: None)*/
    public TrackEntityTask setEntityID( long val ) {
        EntityID = val;
        return this;
    }

    /**  Required sensor modality for tracking the target (Units: None)*/
    public afrl.cmasi.WavelengthBand getSensorModality() { return SensorModality; }

    /**  Required sensor modality for tracking the target (Units: None)*/
    public TrackEntityTask setSensorModality( afrl.cmasi.WavelengthBand val ) {
        SensorModality = val;
        return this;
    }

    /**  The minimum ground sample distance that should be maintained for this target track operation. (Units: meter)*/
    public float getGroundSampleDistance() { return GroundSampleDistance; }

    /**  The minimum ground sample distance that should be maintained for this target track operation. (Units: meter)*/
    public TrackEntityTask setGroundSampleDistance( float val ) {
        GroundSampleDistance = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 12; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        EntityID = LMCPUtil.getUint32(in);

        SensorModality = afrl.cmasi.WavelengthBand.unpack( in );

        GroundSampleDistance = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putUint32(out, EntityID);
        SensorModality.pack(out);
        LMCPUtil.putReal32(out, GroundSampleDistance);

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
        buf.append( ws + "<TrackEntityTask Series=\"PERCEIVE\">\n");
        buf.append( ws + "  <EntityID>" + String.valueOf(EntityID) + "</EntityID>\n");
        buf.append( ws + "  <SensorModality>" + String.valueOf(SensorModality) + "</SensorModality>\n");
        buf.append( ws + "  <GroundSampleDistance>" + String.valueOf(GroundSampleDistance) + "</GroundSampleDistance>\n");
        buf.append( ws + "  <TaskID>" + String.valueOf(TaskID) + "</TaskID>\n");
        buf.append( ws + "  <Label>" + String.valueOf(Label) + "</Label>\n");
        buf.append( ws + "  <EligibleEntities>\n");
        for (int i=0; i<EligibleEntities.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(EligibleEntities.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </EligibleEntities>\n");
        buf.append( ws + "  <RevisitRate>" + String.valueOf(RevisitRate) + "</RevisitRate>\n");
        buf.append( ws + "  <Parameters>\n");
        for (int i=0; i<Parameters.size(); i++) {
            buf.append( Parameters.get(i) == null ? ( ws + "    <null/>\n") : (Parameters.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Parameters>\n");
        buf.append( ws + "  <Priority>" + String.valueOf(Priority) + "</Priority>\n");
        buf.append( ws + "  <Required>" + String.valueOf(Required) + "</Required>\n");
        buf.append( ws + "</TrackEntityTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        TrackEntityTask o = (TrackEntityTask) anotherObj;
        if (EntityID != o.EntityID) return false;
        if (SensorModality != o.SensorModality) return false;
        if (GroundSampleDistance != o.GroundSampleDistance) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)EntityID;
        hash += 31 * (int)GroundSampleDistance;

        return hash + super.hashCode();
    }
    
}
