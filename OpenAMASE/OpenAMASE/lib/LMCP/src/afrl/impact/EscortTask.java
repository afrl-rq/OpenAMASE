// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.impact;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Task for targeting surveillance at an offset of a moving entity, for example to        scout ahead of a convoy. 
*/
public class EscortTask extends afrl.cmasi.SearchTask {
    
    public static final int LMCP_TYPE = 31;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "EscortTask";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.EscortTask";

    /**  ID of entity which will provide the basis for the escort. Tasked vehicle will attempt to stay 'StandoffDistance' ahead of this entity on its route. (Units: None)*/
    @LmcpType("int64")
    protected long SupportedEntityID = 0L;
    /**  ID of line which describes the route that the 'SupportedEntityID' is following. If the 'SupportedEntityID' is one of the controlled entities, such as a taskable ground vehicle, then 'RouteID' can be zero and this task will use current, known route for that vehicle. Otherwise, 'RouteID' must be an ID for a previously described '{@link LineOfInterest}'. (Units: None)*/
    @LmcpType("int64")
    protected long RouteID = 0L;
    /**  Waypoints that the supported entity is presumed to be following if 'RouteID' is zero (Units: None)*/
    @LmcpType("Waypoint")
    protected java.util.ArrayList<afrl.cmasi.Waypoint> PrescribedWaypoints = new java.util.ArrayList<afrl.cmasi.Waypoint>();
    /**  Distance ahead (positive) or behind (negative) that the vehicle will provide surveillance relative to 'SupportedEntityID's location on route 'RouteID' (Units: meters)*/
    @LmcpType("real32")
    protected float StandoffDistance = (float)100;

    
    public EscortTask() {
    }

    public EscortTask(long TaskID, String Label, float RevisitRate, short Priority, boolean Required, long DwellTime, float GroundSampleDistance, long SupportedEntityID, long RouteID, float StandoffDistance){
        this.TaskID = TaskID;
        this.Label = Label;
        this.RevisitRate = RevisitRate;
        this.Priority = Priority;
        this.Required = Required;
        this.DwellTime = DwellTime;
        this.GroundSampleDistance = GroundSampleDistance;
        this.SupportedEntityID = SupportedEntityID;
        this.RouteID = RouteID;
        this.StandoffDistance = StandoffDistance;
    }


    public EscortTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            EscortTask newObj = new EscortTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID of entity which will provide the basis for the escort. Tasked vehicle will attempt to stay 'StandoffDistance' ahead of this entity on its route. (Units: None)*/
    public long getSupportedEntityID() { return SupportedEntityID; }

    /**  ID of entity which will provide the basis for the escort. Tasked vehicle will attempt to stay 'StandoffDistance' ahead of this entity on its route. (Units: None)*/
    public EscortTask setSupportedEntityID( long val ) {
        SupportedEntityID = val;
        return this;
    }

    /**  ID of line which describes the route that the 'SupportedEntityID' is following. If the 'SupportedEntityID' is one of the controlled entities, such as a taskable ground vehicle, then 'RouteID' can be zero and this task will use current, known route for that vehicle. Otherwise, 'RouteID' must be an ID for a previously described '{@link LineOfInterest}'. (Units: None)*/
    public long getRouteID() { return RouteID; }

    /**  ID of line which describes the route that the 'SupportedEntityID' is following. If the 'SupportedEntityID' is one of the controlled entities, such as a taskable ground vehicle, then 'RouteID' can be zero and this task will use current, known route for that vehicle. Otherwise, 'RouteID' must be an ID for a previously described '{@link LineOfInterest}'. (Units: None)*/
    public EscortTask setRouteID( long val ) {
        RouteID = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.Waypoint> getPrescribedWaypoints() {
        return PrescribedWaypoints;
    }

    /**  Distance ahead (positive) or behind (negative) that the vehicle will provide surveillance relative to 'SupportedEntityID's location on route 'RouteID' (Units: meters)*/
    public float getStandoffDistance() { return StandoffDistance; }

    /**  Distance ahead (positive) or behind (negative) that the vehicle will provide surveillance relative to 'SupportedEntityID's location on route 'RouteID' (Units: meters)*/
    public EscortTask setStandoffDistance( float val ) {
        StandoffDistance = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 20; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(PrescribedWaypoints);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        SupportedEntityID = LMCPUtil.getInt64(in);

        RouteID = LMCPUtil.getInt64(in);

        PrescribedWaypoints.clear();
        int PrescribedWaypoints_len = LMCPUtil.getUint16(in);
        for(int i=0; i<PrescribedWaypoints_len; i++){
        PrescribedWaypoints.add( (afrl.cmasi.Waypoint) LMCPUtil.getObject(in));
        }
        StandoffDistance = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, SupportedEntityID);
        LMCPUtil.putInt64(out, RouteID);
        LMCPUtil.putUint16(out, PrescribedWaypoints.size());
        for(int i=0; i<PrescribedWaypoints.size(); i++){
            LMCPUtil.putObject(out, PrescribedWaypoints.get(i));
        }
        LMCPUtil.putReal32(out, StandoffDistance);

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
        buf.append( ws + "<EscortTask Series=\"IMPACT\">\n");
        buf.append( ws + "  <SupportedEntityID>" + String.valueOf(SupportedEntityID) + "</SupportedEntityID>\n");
        buf.append( ws + "  <RouteID>" + String.valueOf(RouteID) + "</RouteID>\n");
        buf.append( ws + "  <PrescribedWaypoints>\n");
        for (int i=0; i<PrescribedWaypoints.size(); i++) {
            buf.append( PrescribedWaypoints.get(i) == null ? ( ws + "    <null/>\n") : (PrescribedWaypoints.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </PrescribedWaypoints>\n");
        buf.append( ws + "  <StandoffDistance>" + String.valueOf(StandoffDistance) + "</StandoffDistance>\n");
        buf.append( ws + "  <DesiredWavelengthBands>\n");
        for (int i=0; i<DesiredWavelengthBands.size(); i++) {
        buf.append( ws + "  <WavelengthBand>" + String.valueOf(DesiredWavelengthBands.get(i)) + "</WavelengthBand>\n");
        }
        buf.append( ws + "  </DesiredWavelengthBands>\n");
        buf.append( ws + "  <DwellTime>" + String.valueOf(DwellTime) + "</DwellTime>\n");
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
        buf.append( ws + "</EscortTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        EscortTask o = (EscortTask) anotherObj;
        if (SupportedEntityID != o.SupportedEntityID) return false;
        if (RouteID != o.RouteID) return false;
         if (!PrescribedWaypoints.equals( o.PrescribedWaypoints)) return false;
        if (StandoffDistance != o.StandoffDistance) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)StandoffDistance;

        return hash + super.hashCode();
    }
    
}
