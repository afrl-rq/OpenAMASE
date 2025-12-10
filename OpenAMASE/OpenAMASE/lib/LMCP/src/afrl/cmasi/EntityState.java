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
 Reported state for an entity in the system  
*/
public class EntityState extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 14;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "EntityState";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.EntityState";

    /**  A unique ID for this entity. IDs should be greater than zero (Units: None)*/
    @LmcpType("int64")
    protected long ID = 0L;
    /**  Velocity in the body x-direction (postive out nose) (Units: meter/sec)*/
    @LmcpType("real32")
    protected float u = (float)0;
    /**  Velocity in the body y-direction (positive out right wing) (Units: meter/sec)*/
    @LmcpType("real32")
    protected float v = (float)0;
    /**  Velocity in the body z-direction (positve downward) (Units: meter/sec)*/
    @LmcpType("real32")
    protected float w = (float)0;
    /**  Acceleration in the body x-direction (postive out nose) (Units: meter/sec/sec)*/
    @LmcpType("real32")
    protected float udot = (float)0;
    /**  Acceleration in the body y-direction (positive out right wing) (Units: meter/sec/sec)*/
    @LmcpType("real32")
    protected float vdot = (float)0;
    /**  Acceleration in the body z-direction (positve downward) (Units: meter/sec/sec)*/
    @LmcpType("real32")
    protected float wdot = (float)0;
    /**  Angle between true North and the projection of the body x-axis in the North-East plane. (Units: degree)*/
    @LmcpType("real32")
    protected float Heading = (float)0;
    /**  Pitch of vehicle around body y-axis (positive upwards) (Units: degree)*/
    @LmcpType("real32")
    protected float Pitch = (float)0;
    /**  Roll angle of the vehicle around body x-axis (positive right wing down) (Units: degree)*/
    @LmcpType("real32")
    protected float Roll = (float)0;
    /**  roll-rate of vehicle (angular velocity around body x-axis). Positive right-wing down. (Units: degree/sec)*/
    @LmcpType("real32")
    protected float p = (float)0;
    /**  pitch rate of the vehicle (angular velocity around body y-axis). Positive nose-up.(Units: degree/sec)*/
    @LmcpType("real32")
    protected float q = (float)0;
    /**  yaw rate of the vehicle (angular velocity around body z-axis). Positive nose right. (Units: degree/sec)*/
    @LmcpType("real32")
    protected float r = (float)0;
    /**  Course/Groundtrack angle of the entity referenced to true North (Units: degrees)*/
    @LmcpType("real32")
    protected float Course = (float)0;
    /**  Current entity ground speed (Units: m/s)*/
    @LmcpType("real32")
    protected float Groundspeed = (float)0;
    /**  The perceived entity location. A valid EntityState must include Location (null not allowed) (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D Location = new afrl.cmasi.Location3D();
    /**  The available energy remaining, expressed in terms of the percentage of maximum capacity (Units: %)*/
    @LmcpType("real32")
    protected float EnergyAvailable = (float)0;
    /**  The consumption rate of available energy, expressed in terms of the percentage of maximum capacity used per second. (Units: %/sec)*/
    @LmcpType("real32")
    protected float ActualEnergyRate = (float)0;
    /**  A list of states for any onboard payloads (Units: None)*/
    @LmcpType("PayloadState")
    protected java.util.ArrayList<afrl.cmasi.PayloadState> PayloadStateList = new java.util.ArrayList<afrl.cmasi.PayloadState>();
    /**  The ID of the current waypoint. Only valid if the vehicle is in waypoint following mode. (Units: None)*/
    @LmcpType("int64")
    protected long CurrentWaypoint = 0L;
    /**  Current command (VehicleActionCommand or MissionCommand) being executed. A value of zero denotes no command being executed, or that a command without an set identifier (CommandID) is being executed. (Units: None)*/
    @LmcpType("int64")
    protected long CurrentCommand = 0L;
    /**  The current mode for this vehicle. (Units: None)*/
    @LmcpType("NavigationMode")
    protected afrl.cmasi.NavigationMode Mode = afrl.cmasi.NavigationMode.Waypoint;
    /**  Tasks that this entity is currently executing. An empty list indicates no associated tasks. The task number should coincide with the task number in the task request. For instance, if a waypoint is associated with a search task, then the task number associated with that search should be included in this list. (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> AssociatedTasks = new java.util.ArrayList<Long>();
    /**  time stamp of this data. Time datum is defined by the application, but unless otherwise specified is milliseconds since 1 Jan 1970 (Units: millisecond)*/
    @LmcpType("int64")
    protected long Time = 0L;
    /**  A list that maps keys to values for the inclusion of extra, custom information about this entity (Units: None)*/
    @LmcpType("KeyValuePair")
    protected java.util.ArrayList<afrl.cmasi.KeyValuePair> Info = new java.util.ArrayList<afrl.cmasi.KeyValuePair>();

    
    public EntityState() {
    }

    public EntityState(long ID, float u, float v, float w, float udot, float vdot, float wdot, float Heading, float Pitch, float Roll, float p, float q, float r, float Course, float Groundspeed, afrl.cmasi.Location3D Location, float EnergyAvailable, float ActualEnergyRate, long CurrentWaypoint, long CurrentCommand, afrl.cmasi.NavigationMode Mode, long Time){
        this.ID = ID;
        this.u = u;
        this.v = v;
        this.w = w;
        this.udot = udot;
        this.vdot = vdot;
        this.wdot = wdot;
        this.Heading = Heading;
        this.Pitch = Pitch;
        this.Roll = Roll;
        this.p = p;
        this.q = q;
        this.r = r;
        this.Course = Course;
        this.Groundspeed = Groundspeed;
        this.Location = Location;
        this.EnergyAvailable = EnergyAvailable;
        this.ActualEnergyRate = ActualEnergyRate;
        this.CurrentWaypoint = CurrentWaypoint;
        this.CurrentCommand = CurrentCommand;
        this.Mode = Mode;
        this.Time = Time;
    }


    public EntityState clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            EntityState newObj = new EntityState();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  A unique ID for this entity. IDs should be greater than zero (Units: None)*/
    public long getID() { return ID; }

    /**  A unique ID for this entity. IDs should be greater than zero (Units: None)*/
    public EntityState setID( long val ) {
        ID = val;
        return this;
    }

    /**  Velocity in the body x-direction (postive out nose) (Units: meter/sec)*/
    public float getU() { return u; }

    /**  Velocity in the body x-direction (postive out nose) (Units: meter/sec)*/
    public EntityState setU( float val ) {
        u = val;
        return this;
    }

    /**  Velocity in the body y-direction (positive out right wing) (Units: meter/sec)*/
    public float getV() { return v; }

    /**  Velocity in the body y-direction (positive out right wing) (Units: meter/sec)*/
    public EntityState setV( float val ) {
        v = val;
        return this;
    }

    /**  Velocity in the body z-direction (positve downward) (Units: meter/sec)*/
    public float getW() { return w; }

    /**  Velocity in the body z-direction (positve downward) (Units: meter/sec)*/
    public EntityState setW( float val ) {
        w = val;
        return this;
    }

    /**  Acceleration in the body x-direction (postive out nose) (Units: meter/sec/sec)*/
    public float getUdot() { return udot; }

    /**  Acceleration in the body x-direction (postive out nose) (Units: meter/sec/sec)*/
    public EntityState setUdot( float val ) {
        udot = val;
        return this;
    }

    /**  Acceleration in the body y-direction (positive out right wing) (Units: meter/sec/sec)*/
    public float getVdot() { return vdot; }

    /**  Acceleration in the body y-direction (positive out right wing) (Units: meter/sec/sec)*/
    public EntityState setVdot( float val ) {
        vdot = val;
        return this;
    }

    /**  Acceleration in the body z-direction (positve downward) (Units: meter/sec/sec)*/
    public float getWdot() { return wdot; }

    /**  Acceleration in the body z-direction (positve downward) (Units: meter/sec/sec)*/
    public EntityState setWdot( float val ) {
        wdot = val;
        return this;
    }

    /**  Angle between true North and the projection of the body x-axis in the North-East plane. (Units: degree)*/
    public float getHeading() { return Heading; }

    /**  Angle between true North and the projection of the body x-axis in the North-East plane. (Units: degree)*/
    public EntityState setHeading( float val ) {
        Heading = val;
        return this;
    }

    /**  Pitch of vehicle around body y-axis (positive upwards) (Units: degree)*/
    public float getPitch() { return Pitch; }

    /**  Pitch of vehicle around body y-axis (positive upwards) (Units: degree)*/
    public EntityState setPitch( float val ) {
        Pitch = val;
        return this;
    }

    /**  Roll angle of the vehicle around body x-axis (positive right wing down) (Units: degree)*/
    public float getRoll() { return Roll; }

    /**  Roll angle of the vehicle around body x-axis (positive right wing down) (Units: degree)*/
    public EntityState setRoll( float val ) {
        Roll = val;
        return this;
    }

    /**  roll-rate of vehicle (angular velocity around body x-axis). Positive right-wing down. (Units: degree/sec)*/
    public float getP() { return p; }

    /**  roll-rate of vehicle (angular velocity around body x-axis). Positive right-wing down. (Units: degree/sec)*/
    public EntityState setP( float val ) {
        p = val;
        return this;
    }

    /**  pitch rate of the vehicle (angular velocity around body y-axis). Positive nose-up.(Units: degree/sec)*/
    public float getQ() { return q; }

    /**  pitch rate of the vehicle (angular velocity around body y-axis). Positive nose-up.(Units: degree/sec)*/
    public EntityState setQ( float val ) {
        q = val;
        return this;
    }

    /**  yaw rate of the vehicle (angular velocity around body z-axis). Positive nose right. (Units: degree/sec)*/
    public float getR() { return r; }

    /**  yaw rate of the vehicle (angular velocity around body z-axis). Positive nose right. (Units: degree/sec)*/
    public EntityState setR( float val ) {
        r = val;
        return this;
    }

    /**  Course/Groundtrack angle of the entity referenced to true North (Units: degrees)*/
    public float getCourse() { return Course; }

    /**  Course/Groundtrack angle of the entity referenced to true North (Units: degrees)*/
    public EntityState setCourse( float val ) {
        Course = val;
        return this;
    }

    /**  Current entity ground speed (Units: m/s)*/
    public float getGroundspeed() { return Groundspeed; }

    /**  Current entity ground speed (Units: m/s)*/
    public EntityState setGroundspeed( float val ) {
        Groundspeed = val;
        return this;
    }

    /**  The perceived entity location. A valid EntityState must include Location (null not allowed) (Units: None)*/
    public afrl.cmasi.Location3D getLocation() { return Location; }

    /**  The perceived entity location. A valid EntityState must include Location (null not allowed) (Units: None)*/
    public EntityState setLocation( afrl.cmasi.Location3D val ) {
        Location = val;
        return this;
    }

    /**  The available energy remaining, expressed in terms of the percentage of maximum capacity (Units: %)*/
    public float getEnergyAvailable() { return EnergyAvailable; }

    /**  The available energy remaining, expressed in terms of the percentage of maximum capacity (Units: %)*/
    public EntityState setEnergyAvailable( float val ) {
        EnergyAvailable = val;
        return this;
    }

    /**  The consumption rate of available energy, expressed in terms of the percentage of maximum capacity used per second. (Units: %/sec)*/
    public float getActualEnergyRate() { return ActualEnergyRate; }

    /**  The consumption rate of available energy, expressed in terms of the percentage of maximum capacity used per second. (Units: %/sec)*/
    public EntityState setActualEnergyRate( float val ) {
        ActualEnergyRate = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.PayloadState> getPayloadStateList() {
        return PayloadStateList;
    }

    /**  The ID of the current waypoint. Only valid if the vehicle is in waypoint following mode. (Units: None)*/
    public long getCurrentWaypoint() { return CurrentWaypoint; }

    /**  The ID of the current waypoint. Only valid if the vehicle is in waypoint following mode. (Units: None)*/
    public EntityState setCurrentWaypoint( long val ) {
        CurrentWaypoint = val;
        return this;
    }

    /**  Current command (VehicleActionCommand or MissionCommand) being executed. A value of zero denotes no command being executed, or that a command without an set identifier (CommandID) is being executed. (Units: None)*/
    public long getCurrentCommand() { return CurrentCommand; }

    /**  Current command (VehicleActionCommand or MissionCommand) being executed. A value of zero denotes no command being executed, or that a command without an set identifier (CommandID) is being executed. (Units: None)*/
    public EntityState setCurrentCommand( long val ) {
        CurrentCommand = val;
        return this;
    }

    /**  The current mode for this vehicle. (Units: None)*/
    public afrl.cmasi.NavigationMode getMode() { return Mode; }

    /**  The current mode for this vehicle. (Units: None)*/
    public EntityState setMode( afrl.cmasi.NavigationMode val ) {
        Mode = val;
        return this;
    }

    public java.util.ArrayList<Long> getAssociatedTasks() {
        return AssociatedTasks;
    }

    /**  time stamp of this data. Time datum is defined by the application, but unless otherwise specified is milliseconds since 1 Jan 1970 (Units: millisecond)*/
    public long getTime() { return Time; }

    /**  time stamp of this data. Time datum is defined by the application, but unless otherwise specified is milliseconds since 1 Jan 1970 (Units: millisecond)*/
    public EntityState setTime( long val ) {
        Time = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.KeyValuePair> getInfo() {
        return Info;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 100; // accounts for primitive types
        size += LMCPUtil.sizeOf(Location);
        size += 2;
        size += LMCPUtil.sizeOfList(PayloadStateList);
        
        size += 2 + 8 * AssociatedTasks.size();
        size += 2;
        size += LMCPUtil.sizeOfList(Info);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ID = LMCPUtil.getInt64(in);

        u = LMCPUtil.getReal32(in);

        v = LMCPUtil.getReal32(in);

        w = LMCPUtil.getReal32(in);

        udot = LMCPUtil.getReal32(in);

        vdot = LMCPUtil.getReal32(in);

        wdot = LMCPUtil.getReal32(in);

        Heading = LMCPUtil.getReal32(in);

        Pitch = LMCPUtil.getReal32(in);

        Roll = LMCPUtil.getReal32(in);

        p = LMCPUtil.getReal32(in);

        q = LMCPUtil.getReal32(in);

        r = LMCPUtil.getReal32(in);

        Course = LMCPUtil.getReal32(in);

        Groundspeed = LMCPUtil.getReal32(in);

            Location = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        EnergyAvailable = LMCPUtil.getReal32(in);

        ActualEnergyRate = LMCPUtil.getReal32(in);

        PayloadStateList.clear();
        int PayloadStateList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<PayloadStateList_len; i++){
        PayloadStateList.add( (afrl.cmasi.PayloadState) LMCPUtil.getObject(in));
        }
        CurrentWaypoint = LMCPUtil.getInt64(in);

        CurrentCommand = LMCPUtil.getInt64(in);

        Mode = afrl.cmasi.NavigationMode.unpack( in );

        AssociatedTasks.clear();
        int AssociatedTasks_len = LMCPUtil.getUint16(in);
        for(int i=0; i<AssociatedTasks_len; i++){
            AssociatedTasks.add(LMCPUtil.getInt64(in));
        }
        Time = LMCPUtil.getInt64(in);

        Info.clear();
        int Info_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Info_len; i++){
        Info.add( (afrl.cmasi.KeyValuePair) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ID);
        LMCPUtil.putReal32(out, u);
        LMCPUtil.putReal32(out, v);
        LMCPUtil.putReal32(out, w);
        LMCPUtil.putReal32(out, udot);
        LMCPUtil.putReal32(out, vdot);
        LMCPUtil.putReal32(out, wdot);
        LMCPUtil.putReal32(out, Heading);
        LMCPUtil.putReal32(out, Pitch);
        LMCPUtil.putReal32(out, Roll);
        LMCPUtil.putReal32(out, p);
        LMCPUtil.putReal32(out, q);
        LMCPUtil.putReal32(out, r);
        LMCPUtil.putReal32(out, Course);
        LMCPUtil.putReal32(out, Groundspeed);
        LMCPUtil.putObject(out, Location);
        LMCPUtil.putReal32(out, EnergyAvailable);
        LMCPUtil.putReal32(out, ActualEnergyRate);
        LMCPUtil.putUint16(out, PayloadStateList.size());
        for(int i=0; i<PayloadStateList.size(); i++){
            LMCPUtil.putObject(out, PayloadStateList.get(i));
        }
        LMCPUtil.putInt64(out, CurrentWaypoint);
        LMCPUtil.putInt64(out, CurrentCommand);
        Mode.pack(out);
        LMCPUtil.putUint16(out, AssociatedTasks.size());
        for(int i=0; i<AssociatedTasks.size(); i++){
            LMCPUtil.putInt64(out, AssociatedTasks.get(i));
        }
        LMCPUtil.putInt64(out, Time);
        LMCPUtil.putUint16(out, Info.size());
        for(int i=0; i<Info.size(); i++){
            LMCPUtil.putObject(out, Info.get(i));
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
        buf.append( ws + "<EntityState Series=\"CMASI\">\n");
        buf.append( ws + "  <ID>" + String.valueOf(ID) + "</ID>\n");
        buf.append( ws + "  <u>" + String.valueOf(u) + "</u>\n");
        buf.append( ws + "  <v>" + String.valueOf(v) + "</v>\n");
        buf.append( ws + "  <w>" + String.valueOf(w) + "</w>\n");
        buf.append( ws + "  <udot>" + String.valueOf(udot) + "</udot>\n");
        buf.append( ws + "  <vdot>" + String.valueOf(vdot) + "</vdot>\n");
        buf.append( ws + "  <wdot>" + String.valueOf(wdot) + "</wdot>\n");
        buf.append( ws + "  <Heading>" + String.valueOf(Heading) + "</Heading>\n");
        buf.append( ws + "  <Pitch>" + String.valueOf(Pitch) + "</Pitch>\n");
        buf.append( ws + "  <Roll>" + String.valueOf(Roll) + "</Roll>\n");
        buf.append( ws + "  <p>" + String.valueOf(p) + "</p>\n");
        buf.append( ws + "  <q>" + String.valueOf(q) + "</q>\n");
        buf.append( ws + "  <r>" + String.valueOf(r) + "</r>\n");
        buf.append( ws + "  <Course>" + String.valueOf(Course) + "</Course>\n");
        buf.append( ws + "  <Groundspeed>" + String.valueOf(Groundspeed) + "</Groundspeed>\n");
        if (Location!= null){
           buf.append( ws + "  <Location>\n");
           buf.append( ( Location.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Location>\n");
        }
        buf.append( ws + "  <EnergyAvailable>" + String.valueOf(EnergyAvailable) + "</EnergyAvailable>\n");
        buf.append( ws + "  <ActualEnergyRate>" + String.valueOf(ActualEnergyRate) + "</ActualEnergyRate>\n");
        buf.append( ws + "  <PayloadStateList>\n");
        for (int i=0; i<PayloadStateList.size(); i++) {
            buf.append( PayloadStateList.get(i) == null ? ( ws + "    <null/>\n") : (PayloadStateList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </PayloadStateList>\n");
        buf.append( ws + "  <CurrentWaypoint>" + String.valueOf(CurrentWaypoint) + "</CurrentWaypoint>\n");
        buf.append( ws + "  <CurrentCommand>" + String.valueOf(CurrentCommand) + "</CurrentCommand>\n");
        buf.append( ws + "  <Mode>" + String.valueOf(Mode) + "</Mode>\n");
        buf.append( ws + "  <AssociatedTasks>\n");
        for (int i=0; i<AssociatedTasks.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTasks.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTasks>\n");
        buf.append( ws + "  <Time>" + String.valueOf(Time) + "</Time>\n");
        buf.append( ws + "  <Info>\n");
        for (int i=0; i<Info.size(); i++) {
            buf.append( Info.get(i) == null ? ( ws + "    <null/>\n") : (Info.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Info>\n");
        buf.append( ws + "</EntityState>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        EntityState o = (EntityState) anotherObj;
        if (ID != o.ID) return false;
        if (u != o.u) return false;
        if (v != o.v) return false;
        if (w != o.w) return false;
        if (udot != o.udot) return false;
        if (vdot != o.vdot) return false;
        if (wdot != o.wdot) return false;
        if (Heading != o.Heading) return false;
        if (Pitch != o.Pitch) return false;
        if (Roll != o.Roll) return false;
        if (p != o.p) return false;
        if (q != o.q) return false;
        if (r != o.r) return false;
        if (Course != o.Course) return false;
        if (Groundspeed != o.Groundspeed) return false;
        if (Location == null && o.Location != null) return false;
        if ( Location!= null && !Location.equals(o.Location)) return false;
        if (EnergyAvailable != o.EnergyAvailable) return false;
        if (ActualEnergyRate != o.ActualEnergyRate) return false;
         if (!PayloadStateList.equals( o.PayloadStateList)) return false;
        if (CurrentWaypoint != o.CurrentWaypoint) return false;
        if (CurrentCommand != o.CurrentCommand) return false;
        if (Mode != o.Mode) return false;
         if (!AssociatedTasks.equals( o.AssociatedTasks)) return false;
        if (Time != o.Time) return false;
         if (!Info.equals( o.Info)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)u;
        hash += 31 * (int)v;
        hash += 31 * (int)w;
        hash += 31 * (int)udot;

        return hash + super.hashCode();
    }
    
}
