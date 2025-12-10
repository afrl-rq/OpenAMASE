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
 Single vehicle-to-task summary information 
*/
public class VehicleSummary extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 15;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "VehicleSummary";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.VehicleSummary";

    /**  ID of vehicle considered to complete 'DestinationTaskID' (Units: None)*/
    @LmcpType("int64")
    protected long VehicleID = 0L;
    /**  ID of task for which 'VehicleID' is considered to perform (Units: None)*/
    @LmcpType("int64")
    protected long DestinationTaskID = 0L;
    /**  ID of task from which 'VehicleID' is considered leave from. If zero, from current position of vehicle (Units: None)*/
    @LmcpType("int64")
    protected long InitialTaskID = 0L;
    /**  Percentage along initial task that vehicle should plan from (Units: None)*/
    @LmcpType("real32")
    protected float InitialTaskPercentage = (float)0;
    /**  Estimated time from start of task to task-percentage location (Units: milliseconds)*/
    @LmcpType("int64")
    protected long EstimateTimeToTaskPercentage = 0L;
    /**  Travel time from the current vehicle location to the start of the task. (Units: milliseconds)*/
    @LmcpType("int64")
    protected long TimeToArrive = 0L;
    /**  Time for this task to be completed by the specified vehicle (Units: milliseconds)*/
    @LmcpType("int64")
    protected long TimeOnTask = 0L;
    /**  Energy remaining for vehicle after task has been completed, expressed in terms of the percentage of maximum capacity (Units: %)*/
    @LmcpType("real32")
    protected float EnergyRemaining = (float)0;
    /**  Flag for indicating that the vehicle will leave communication range either enroute or during the task (Units: None)*/
    @LmcpType("bool")
    protected boolean BeyondCommRange = false;
    /**  Flag for indicating that the vehicle will conflict with ROZ enroute or during the task (Units: None)*/
    @LmcpType("bool")
    protected boolean ConflictsWithROZ = false;
    /**  IDs of ROZs that the vehicle will conflict with (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> ROZIDs = new java.util.ArrayList<Long>();
    /**  The list of waypoints associated with this mission task. Waypoints are linked, but the waypoint list may contain waypoints that are not necessarily linked. Multiple linked routes may be sent in a single waypoint list. Waypoints are not necessarily ordered in the list. (Units: None)*/
    @LmcpType("Waypoint")
    protected java.util.ArrayList<afrl.cmasi.Waypoint> WaypointList = new java.util.ArrayList<afrl.cmasi.Waypoint>();
    /**  ID of the first waypoint in the plan. (Units: None)*/
    @LmcpType("int64")
    protected long FirstWaypoint = 0L;

    
    public VehicleSummary() {
    }

    public VehicleSummary(long VehicleID, long DestinationTaskID, long InitialTaskID, float InitialTaskPercentage, long EstimateTimeToTaskPercentage, long TimeToArrive, long TimeOnTask, float EnergyRemaining, boolean BeyondCommRange, boolean ConflictsWithROZ, long FirstWaypoint){
        this.VehicleID = VehicleID;
        this.DestinationTaskID = DestinationTaskID;
        this.InitialTaskID = InitialTaskID;
        this.InitialTaskPercentage = InitialTaskPercentage;
        this.EstimateTimeToTaskPercentage = EstimateTimeToTaskPercentage;
        this.TimeToArrive = TimeToArrive;
        this.TimeOnTask = TimeOnTask;
        this.EnergyRemaining = EnergyRemaining;
        this.BeyondCommRange = BeyondCommRange;
        this.ConflictsWithROZ = ConflictsWithROZ;
        this.FirstWaypoint = FirstWaypoint;
    }


    public VehicleSummary clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            VehicleSummary newObj = new VehicleSummary();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID of vehicle considered to complete 'DestinationTaskID' (Units: None)*/
    public long getVehicleID() { return VehicleID; }

    /**  ID of vehicle considered to complete 'DestinationTaskID' (Units: None)*/
    public VehicleSummary setVehicleID( long val ) {
        VehicleID = val;
        return this;
    }

    /**  ID of task for which 'VehicleID' is considered to perform (Units: None)*/
    public long getDestinationTaskID() { return DestinationTaskID; }

    /**  ID of task for which 'VehicleID' is considered to perform (Units: None)*/
    public VehicleSummary setDestinationTaskID( long val ) {
        DestinationTaskID = val;
        return this;
    }

    /**  ID of task from which 'VehicleID' is considered leave from. If zero, from current position of vehicle (Units: None)*/
    public long getInitialTaskID() { return InitialTaskID; }

    /**  ID of task from which 'VehicleID' is considered leave from. If zero, from current position of vehicle (Units: None)*/
    public VehicleSummary setInitialTaskID( long val ) {
        InitialTaskID = val;
        return this;
    }

    /**  Percentage along initial task that vehicle should plan from (Units: None)*/
    public float getInitialTaskPercentage() { return InitialTaskPercentage; }

    /**  Percentage along initial task that vehicle should plan from (Units: None)*/
    public VehicleSummary setInitialTaskPercentage( float val ) {
        InitialTaskPercentage = val;
        return this;
    }

    /**  Estimated time from start of task to task-percentage location (Units: milliseconds)*/
    public long getEstimateTimeToTaskPercentage() { return EstimateTimeToTaskPercentage; }

    /**  Estimated time from start of task to task-percentage location (Units: milliseconds)*/
    public VehicleSummary setEstimateTimeToTaskPercentage( long val ) {
        EstimateTimeToTaskPercentage = val;
        return this;
    }

    /**  Travel time from the current vehicle location to the start of the task. (Units: milliseconds)*/
    public long getTimeToArrive() { return TimeToArrive; }

    /**  Travel time from the current vehicle location to the start of the task. (Units: milliseconds)*/
    public VehicleSummary setTimeToArrive( long val ) {
        TimeToArrive = val;
        return this;
    }

    /**  Time for this task to be completed by the specified vehicle (Units: milliseconds)*/
    public long getTimeOnTask() { return TimeOnTask; }

    /**  Time for this task to be completed by the specified vehicle (Units: milliseconds)*/
    public VehicleSummary setTimeOnTask( long val ) {
        TimeOnTask = val;
        return this;
    }

    /**  Energy remaining for vehicle after task has been completed, expressed in terms of the percentage of maximum capacity (Units: %)*/
    public float getEnergyRemaining() { return EnergyRemaining; }

    /**  Energy remaining for vehicle after task has been completed, expressed in terms of the percentage of maximum capacity (Units: %)*/
    public VehicleSummary setEnergyRemaining( float val ) {
        EnergyRemaining = val;
        return this;
    }

    /**  Flag for indicating that the vehicle will leave communication range either enroute or during the task (Units: None)*/
    public boolean getBeyondCommRange() { return BeyondCommRange; }

    /**  Flag for indicating that the vehicle will leave communication range either enroute or during the task (Units: None)*/
    public VehicleSummary setBeyondCommRange( boolean val ) {
        BeyondCommRange = val;
        return this;
    }

    /**  Flag for indicating that the vehicle will conflict with ROZ enroute or during the task (Units: None)*/
    public boolean getConflictsWithROZ() { return ConflictsWithROZ; }

    /**  Flag for indicating that the vehicle will conflict with ROZ enroute or during the task (Units: None)*/
    public VehicleSummary setConflictsWithROZ( boolean val ) {
        ConflictsWithROZ = val;
        return this;
    }

    public java.util.ArrayList<Long> getROZIDs() {
        return ROZIDs;
    }

    public java.util.ArrayList<afrl.cmasi.Waypoint> getWaypointList() {
        return WaypointList;
    }

    /**  ID of the first waypoint in the plan. (Units: None)*/
    public long getFirstWaypoint() { return FirstWaypoint; }

    /**  ID of the first waypoint in the plan. (Units: None)*/
    public VehicleSummary setFirstWaypoint( long val ) {
        FirstWaypoint = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 66; // accounts for primitive types
        
        size += 2 + 8 * ROZIDs.size();
        size += 2;
        size += LMCPUtil.sizeOfList(WaypointList);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        VehicleID = LMCPUtil.getInt64(in);

        DestinationTaskID = LMCPUtil.getInt64(in);

        InitialTaskID = LMCPUtil.getInt64(in);

        InitialTaskPercentage = LMCPUtil.getReal32(in);

        EstimateTimeToTaskPercentage = LMCPUtil.getInt64(in);

        TimeToArrive = LMCPUtil.getInt64(in);

        TimeOnTask = LMCPUtil.getInt64(in);

        EnergyRemaining = LMCPUtil.getReal32(in);

        BeyondCommRange = LMCPUtil.getBool(in);

        ConflictsWithROZ = LMCPUtil.getBool(in);

        ROZIDs.clear();
        int ROZIDs_len = LMCPUtil.getUint16(in);
        for(int i=0; i<ROZIDs_len; i++){
            ROZIDs.add(LMCPUtil.getInt64(in));
        }
        WaypointList.clear();
        int WaypointList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<WaypointList_len; i++){
        WaypointList.add( (afrl.cmasi.Waypoint) LMCPUtil.getObject(in));
        }
        FirstWaypoint = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, VehicleID);
        LMCPUtil.putInt64(out, DestinationTaskID);
        LMCPUtil.putInt64(out, InitialTaskID);
        LMCPUtil.putReal32(out, InitialTaskPercentage);
        LMCPUtil.putInt64(out, EstimateTimeToTaskPercentage);
        LMCPUtil.putInt64(out, TimeToArrive);
        LMCPUtil.putInt64(out, TimeOnTask);
        LMCPUtil.putReal32(out, EnergyRemaining);
        LMCPUtil.putBool(out, BeyondCommRange);
        LMCPUtil.putBool(out, ConflictsWithROZ);
        LMCPUtil.putUint16(out, ROZIDs.size());
        for(int i=0; i<ROZIDs.size(); i++){
            LMCPUtil.putInt64(out, ROZIDs.get(i));
        }
        LMCPUtil.putUint16(out, WaypointList.size());
        for(int i=0; i<WaypointList.size(); i++){
            LMCPUtil.putObject(out, WaypointList.get(i));
        }
        LMCPUtil.putInt64(out, FirstWaypoint);

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
        buf.append( ws + "<VehicleSummary Series=\"IMPACT\">\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <DestinationTaskID>" + String.valueOf(DestinationTaskID) + "</DestinationTaskID>\n");
        buf.append( ws + "  <InitialTaskID>" + String.valueOf(InitialTaskID) + "</InitialTaskID>\n");
        buf.append( ws + "  <InitialTaskPercentage>" + String.valueOf(InitialTaskPercentage) + "</InitialTaskPercentage>\n");
        buf.append( ws + "  <EstimateTimeToTaskPercentage>" + String.valueOf(EstimateTimeToTaskPercentage) + "</EstimateTimeToTaskPercentage>\n");
        buf.append( ws + "  <TimeToArrive>" + String.valueOf(TimeToArrive) + "</TimeToArrive>\n");
        buf.append( ws + "  <TimeOnTask>" + String.valueOf(TimeOnTask) + "</TimeOnTask>\n");
        buf.append( ws + "  <EnergyRemaining>" + String.valueOf(EnergyRemaining) + "</EnergyRemaining>\n");
        buf.append( ws + "  <BeyondCommRange>" + String.valueOf(BeyondCommRange) + "</BeyondCommRange>\n");
        buf.append( ws + "  <ConflictsWithROZ>" + String.valueOf(ConflictsWithROZ) + "</ConflictsWithROZ>\n");
        buf.append( ws + "  <ROZIDs>\n");
        for (int i=0; i<ROZIDs.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(ROZIDs.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </ROZIDs>\n");
        buf.append( ws + "  <WaypointList>\n");
        for (int i=0; i<WaypointList.size(); i++) {
            buf.append( WaypointList.get(i) == null ? ( ws + "    <null/>\n") : (WaypointList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </WaypointList>\n");
        buf.append( ws + "  <FirstWaypoint>" + String.valueOf(FirstWaypoint) + "</FirstWaypoint>\n");
        buf.append( ws + "</VehicleSummary>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        VehicleSummary o = (VehicleSummary) anotherObj;
        if (VehicleID != o.VehicleID) return false;
        if (DestinationTaskID != o.DestinationTaskID) return false;
        if (InitialTaskID != o.InitialTaskID) return false;
        if (InitialTaskPercentage != o.InitialTaskPercentage) return false;
        if (EstimateTimeToTaskPercentage != o.EstimateTimeToTaskPercentage) return false;
        if (TimeToArrive != o.TimeToArrive) return false;
        if (TimeOnTask != o.TimeOnTask) return false;
        if (EnergyRemaining != o.EnergyRemaining) return false;
        if (BeyondCommRange != o.BeyondCommRange) return false;
        if (ConflictsWithROZ != o.ConflictsWithROZ) return false;
         if (!ROZIDs.equals( o.ROZIDs)) return false;
         if (!WaypointList.equals( o.WaypointList)) return false;
        if (FirstWaypoint != o.FirstWaypoint) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)InitialTaskPercentage;

        return hash + super.hashCode();
    }
    
}
