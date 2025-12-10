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
            A simple waypoint class for vehicle routing        
*/
public class Waypoint extends afrl.cmasi.Location3D {
    
    public static final int LMCP_TYPE = 35;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "Waypoint";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.Waypoint";

    /**  A unique waypoint number (Units: None)*/
    @LmcpType("int64")
    protected long Number = 0L;
    /**  The index of the next waypoint in the list. Consecutively numbered waypoints are <b>not</b> considered linked, the link must be explicitly stated in this field. (Units: None)*/
    @LmcpType("int64")
    protected long NextWaypoint = 0L;
    /**  Commanded speed for this waypoint. See SpeedType for defintion of this field. (Units: meter/sec)*/
    @LmcpType("real32")
    protected float Speed = (float)0;
    /**  Type of commanded speed (Units: None)*/
    @LmcpType("SpeedType")
    protected afrl.cmasi.SpeedType SpeedType = afrl.cmasi.SpeedType.Airspeed;
    /**  The commanded climb rate. Positive values upwards. For surface (ground and sea) entities, this value is ignored. (Units: meter/sec)*/
    @LmcpType("real32")
    protected float ClimbRate = (float)0;
    /**  The type of turn to execute (Units: None)*/
    @LmcpType("TurnType")
    protected afrl.cmasi.TurnType TurnType = afrl.cmasi.TurnType.TurnShort;
    /**  A list of actions to perform at this waypoint (Units: None)*/
    @LmcpType("VehicleAction")
    protected java.util.ArrayList<afrl.cmasi.VehicleAction> VehicleActionList = new java.util.ArrayList<afrl.cmasi.VehicleAction>();
    /**  A waypoint for contingency (e.g. lost-comm, alternate mission) operations. A value of zero denotes that no contingency point is specified. (Units: None)*/
    @LmcpType("int64")
    protected long ContingencyWaypointA = 0L;
    /**  A waypoint for contingency (e.g. lost-comm, alternate mission) operations. A value of zero denotes that no contingency point is specified. (Units: None)*/
    @LmcpType("int64")
    protected long ContingencyWaypointB = 0L;
    /**  A list of tasks that are associated with this waypoint. A length of zero denotes no associated tasks. This field is for analysis purposes. The automation service should associate a list of tasks with each waypoint to enable analysis of the allocation of tasks to vehicles. (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> AssociatedTasks = new java.util.ArrayList<Long>();

    
    public Waypoint() {
    }

    public Waypoint(double Latitude, double Longitude, float Altitude, afrl.cmasi.AltitudeType AltitudeType, long Number, long NextWaypoint, float Speed, afrl.cmasi.SpeedType SpeedType, float ClimbRate, afrl.cmasi.TurnType TurnType, long ContingencyWaypointA, long ContingencyWaypointB){
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Altitude = Altitude;
        this.AltitudeType = AltitudeType;
        this.Number = Number;
        this.NextWaypoint = NextWaypoint;
        this.Speed = Speed;
        this.SpeedType = SpeedType;
        this.ClimbRate = ClimbRate;
        this.TurnType = TurnType;
        this.ContingencyWaypointA = ContingencyWaypointA;
        this.ContingencyWaypointB = ContingencyWaypointB;
    }


    public Waypoint clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            Waypoint newObj = new Waypoint();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  A unique waypoint number (Units: None)*/
    public long getNumber() { return Number; }

    /**  A unique waypoint number (Units: None)*/
    public Waypoint setNumber( long val ) {
        Number = val;
        return this;
    }

    /**  The index of the next waypoint in the list. Consecutively numbered waypoints are <b>not</b> considered linked, the link must be explicitly stated in this field. (Units: None)*/
    public long getNextWaypoint() { return NextWaypoint; }

    /**  The index of the next waypoint in the list. Consecutively numbered waypoints are <b>not</b> considered linked, the link must be explicitly stated in this field. (Units: None)*/
    public Waypoint setNextWaypoint( long val ) {
        NextWaypoint = val;
        return this;
    }

    /**  Commanded speed for this waypoint. See SpeedType for defintion of this field. (Units: meter/sec)*/
    public float getSpeed() { return Speed; }

    /**  Commanded speed for this waypoint. See SpeedType for defintion of this field. (Units: meter/sec)*/
    public Waypoint setSpeed( float val ) {
        Speed = val;
        return this;
    }

    /**  Type of commanded speed (Units: None)*/
    public afrl.cmasi.SpeedType getSpeedType() { return SpeedType; }

    /**  Type of commanded speed (Units: None)*/
    public Waypoint setSpeedType( afrl.cmasi.SpeedType val ) {
        SpeedType = val;
        return this;
    }

    /**  The commanded climb rate. Positive values upwards. For surface (ground and sea) entities, this value is ignored. (Units: meter/sec)*/
    public float getClimbRate() { return ClimbRate; }

    /**  The commanded climb rate. Positive values upwards. For surface (ground and sea) entities, this value is ignored. (Units: meter/sec)*/
    public Waypoint setClimbRate( float val ) {
        ClimbRate = val;
        return this;
    }

    /**  The type of turn to execute (Units: None)*/
    public afrl.cmasi.TurnType getTurnType() { return TurnType; }

    /**  The type of turn to execute (Units: None)*/
    public Waypoint setTurnType( afrl.cmasi.TurnType val ) {
        TurnType = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.VehicleAction> getVehicleActionList() {
        return VehicleActionList;
    }

    /**  A waypoint for contingency (e.g. lost-comm, alternate mission) operations. A value of zero denotes that no contingency point is specified. (Units: None)*/
    public long getContingencyWaypointA() { return ContingencyWaypointA; }

    /**  A waypoint for contingency (e.g. lost-comm, alternate mission) operations. A value of zero denotes that no contingency point is specified. (Units: None)*/
    public Waypoint setContingencyWaypointA( long val ) {
        ContingencyWaypointA = val;
        return this;
    }

    /**  A waypoint for contingency (e.g. lost-comm, alternate mission) operations. A value of zero denotes that no contingency point is specified. (Units: None)*/
    public long getContingencyWaypointB() { return ContingencyWaypointB; }

    /**  A waypoint for contingency (e.g. lost-comm, alternate mission) operations. A value of zero denotes that no contingency point is specified. (Units: None)*/
    public Waypoint setContingencyWaypointB( long val ) {
        ContingencyWaypointB = val;
        return this;
    }

    public java.util.ArrayList<Long> getAssociatedTasks() {
        return AssociatedTasks;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 48; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(VehicleActionList);
        
        size += 2 + 8 * AssociatedTasks.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        Number = LMCPUtil.getInt64(in);

        NextWaypoint = LMCPUtil.getInt64(in);

        Speed = LMCPUtil.getReal32(in);

        SpeedType = afrl.cmasi.SpeedType.unpack( in );

        ClimbRate = LMCPUtil.getReal32(in);

        TurnType = afrl.cmasi.TurnType.unpack( in );

        VehicleActionList.clear();
        int VehicleActionList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<VehicleActionList_len; i++){
        VehicleActionList.add( (afrl.cmasi.VehicleAction) LMCPUtil.getObject(in));
        }
        ContingencyWaypointA = LMCPUtil.getInt64(in);

        ContingencyWaypointB = LMCPUtil.getInt64(in);

        AssociatedTasks.clear();
        int AssociatedTasks_len = LMCPUtil.getUint16(in);
        for(int i=0; i<AssociatedTasks_len; i++){
            AssociatedTasks.add(LMCPUtil.getInt64(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, Number);
        LMCPUtil.putInt64(out, NextWaypoint);
        LMCPUtil.putReal32(out, Speed);
        SpeedType.pack(out);
        LMCPUtil.putReal32(out, ClimbRate);
        TurnType.pack(out);
        LMCPUtil.putUint16(out, VehicleActionList.size());
        for(int i=0; i<VehicleActionList.size(); i++){
            LMCPUtil.putObject(out, VehicleActionList.get(i));
        }
        LMCPUtil.putInt64(out, ContingencyWaypointA);
        LMCPUtil.putInt64(out, ContingencyWaypointB);
        LMCPUtil.putUint16(out, AssociatedTasks.size());
        for(int i=0; i<AssociatedTasks.size(); i++){
            LMCPUtil.putInt64(out, AssociatedTasks.get(i));
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
        buf.append( ws + "<Waypoint Series=\"CMASI\">\n");
        buf.append( ws + "  <Number>" + String.valueOf(Number) + "</Number>\n");
        buf.append( ws + "  <NextWaypoint>" + String.valueOf(NextWaypoint) + "</NextWaypoint>\n");
        buf.append( ws + "  <Speed>" + String.valueOf(Speed) + "</Speed>\n");
        buf.append( ws + "  <SpeedType>" + String.valueOf(SpeedType) + "</SpeedType>\n");
        buf.append( ws + "  <ClimbRate>" + String.valueOf(ClimbRate) + "</ClimbRate>\n");
        buf.append( ws + "  <TurnType>" + String.valueOf(TurnType) + "</TurnType>\n");
        buf.append( ws + "  <VehicleActionList>\n");
        for (int i=0; i<VehicleActionList.size(); i++) {
            buf.append( VehicleActionList.get(i) == null ? ( ws + "    <null/>\n") : (VehicleActionList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </VehicleActionList>\n");
        buf.append( ws + "  <ContingencyWaypointA>" + String.valueOf(ContingencyWaypointA) + "</ContingencyWaypointA>\n");
        buf.append( ws + "  <ContingencyWaypointB>" + String.valueOf(ContingencyWaypointB) + "</ContingencyWaypointB>\n");
        buf.append( ws + "  <AssociatedTasks>\n");
        for (int i=0; i<AssociatedTasks.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTasks.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTasks>\n");
        buf.append( ws + "  <Latitude>" + String.valueOf(Latitude) + "</Latitude>\n");
        buf.append( ws + "  <Longitude>" + String.valueOf(Longitude) + "</Longitude>\n");
        buf.append( ws + "  <Altitude>" + String.valueOf(Altitude) + "</Altitude>\n");
        buf.append( ws + "  <AltitudeType>" + String.valueOf(AltitudeType) + "</AltitudeType>\n");
        buf.append( ws + "</Waypoint>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        Waypoint o = (Waypoint) anotherObj;
        if (Number != o.Number) return false;
        if (NextWaypoint != o.NextWaypoint) return false;
        if (Speed != o.Speed) return false;
        if (SpeedType != o.SpeedType) return false;
        if (ClimbRate != o.ClimbRate) return false;
        if (TurnType != o.TurnType) return false;
         if (!VehicleActionList.equals( o.VehicleActionList)) return false;
        if (ContingencyWaypointA != o.ContingencyWaypointA) return false;
        if (ContingencyWaypointB != o.ContingencyWaypointB) return false;
         if (!AssociatedTasks.equals( o.AssociatedTasks)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)Speed;
        hash += 31 * (int)ClimbRate;

        return hash + super.hashCode();
    }
    
}
