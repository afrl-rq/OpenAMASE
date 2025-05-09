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
 Start/end locations and headings and cost for implementing the task from this configuration 
*/
public class TaskOption extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 20;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "TaskOption";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.TaskOption";

    /**  Task ID (Units: None)*/
    @LmcpType("int64")
    protected long TaskID = 0L;
    /**  ID for this option (Units: None)*/
    @LmcpType("int64")
    protected long OptionID = 0L;
    /**  Eligible entities for completing this option with identical cost to complete. If list is empty, then all vehicles are assumed to be eligible. (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> EligibleEntities = new java.util.ArrayList<Long>();
    /**  Cost to complete option in terms of time (given in milliseconds) (Units: milliseconds)*/
    @LmcpType("int64")
    protected long Cost = 0L;
    /**  Start location entering the option. A valid TaskOption must define StartLocation (null not allowed). (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D StartLocation = new afrl.cmasi.Location3D();
    /**  Start heading entering the option (Units: degrees)*/
    @LmcpType("real32")
    protected float StartHeading = (float)0;
    /**  Ending location for this option. A valid TaskOption must define EndLocation (null not allowed). (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D EndLocation = new afrl.cmasi.Location3D();
    /**  Ending heading for this option (Units: degrees)*/
    @LmcpType("real32")
    protected float EndHeading = (float)0;

    
    public TaskOption() {
    }

    public TaskOption(long TaskID, long OptionID, long Cost, afrl.cmasi.Location3D StartLocation, float StartHeading, afrl.cmasi.Location3D EndLocation, float EndHeading){
        this.TaskID = TaskID;
        this.OptionID = OptionID;
        this.Cost = Cost;
        this.StartLocation = StartLocation;
        this.StartHeading = StartHeading;
        this.EndLocation = EndLocation;
        this.EndHeading = EndHeading;
    }


    public TaskOption clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            TaskOption newObj = new TaskOption();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Task ID (Units: None)*/
    public long getTaskID() { return TaskID; }

    /**  Task ID (Units: None)*/
    public TaskOption setTaskID( long val ) {
        TaskID = val;
        return this;
    }

    /**  ID for this option (Units: None)*/
    public long getOptionID() { return OptionID; }

    /**  ID for this option (Units: None)*/
    public TaskOption setOptionID( long val ) {
        OptionID = val;
        return this;
    }

    public java.util.ArrayList<Long> getEligibleEntities() {
        return EligibleEntities;
    }

    /**  Cost to complete option in terms of time (given in milliseconds) (Units: milliseconds)*/
    public long getCost() { return Cost; }

    /**  Cost to complete option in terms of time (given in milliseconds) (Units: milliseconds)*/
    public TaskOption setCost( long val ) {
        Cost = val;
        return this;
    }

    /**  Start location entering the option. A valid TaskOption must define StartLocation (null not allowed). (Units: None)*/
    public afrl.cmasi.Location3D getStartLocation() { return StartLocation; }

    /**  Start location entering the option. A valid TaskOption must define StartLocation (null not allowed). (Units: None)*/
    public TaskOption setStartLocation( afrl.cmasi.Location3D val ) {
        StartLocation = val;
        return this;
    }

    /**  Start heading entering the option (Units: degrees)*/
    public float getStartHeading() { return StartHeading; }

    /**  Start heading entering the option (Units: degrees)*/
    public TaskOption setStartHeading( float val ) {
        StartHeading = val;
        return this;
    }

    /**  Ending location for this option. A valid TaskOption must define EndLocation (null not allowed). (Units: None)*/
    public afrl.cmasi.Location3D getEndLocation() { return EndLocation; }

    /**  Ending location for this option. A valid TaskOption must define EndLocation (null not allowed). (Units: None)*/
    public TaskOption setEndLocation( afrl.cmasi.Location3D val ) {
        EndLocation = val;
        return this;
    }

    /**  Ending heading for this option (Units: degrees)*/
    public float getEndHeading() { return EndHeading; }

    /**  Ending heading for this option (Units: degrees)*/
    public TaskOption setEndHeading( float val ) {
        EndHeading = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 32; // accounts for primitive types
        
        size += 2 + 8 * EligibleEntities.size();
        size += LMCPUtil.sizeOf(StartLocation);
        size += LMCPUtil.sizeOf(EndLocation);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        TaskID = LMCPUtil.getInt64(in);

        OptionID = LMCPUtil.getInt64(in);

        EligibleEntities.clear();
        int EligibleEntities_len = LMCPUtil.getUint16(in);
        for(int i=0; i<EligibleEntities_len; i++){
            EligibleEntities.add(LMCPUtil.getInt64(in));
        }
        Cost = LMCPUtil.getInt64(in);

            StartLocation = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        StartHeading = LMCPUtil.getReal32(in);

            EndLocation = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        EndHeading = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, TaskID);
        LMCPUtil.putInt64(out, OptionID);
        LMCPUtil.putUint16(out, EligibleEntities.size());
        for(int i=0; i<EligibleEntities.size(); i++){
            LMCPUtil.putInt64(out, EligibleEntities.get(i));
        }
        LMCPUtil.putInt64(out, Cost);
        LMCPUtil.putObject(out, StartLocation);
        LMCPUtil.putReal32(out, StartHeading);
        LMCPUtil.putObject(out, EndLocation);
        LMCPUtil.putReal32(out, EndHeading);

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
        buf.append( ws + "<TaskOption Series=\"UXTASK\">\n");
        buf.append( ws + "  <TaskID>" + String.valueOf(TaskID) + "</TaskID>\n");
        buf.append( ws + "  <OptionID>" + String.valueOf(OptionID) + "</OptionID>\n");
        buf.append( ws + "  <EligibleEntities>\n");
        for (int i=0; i<EligibleEntities.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(EligibleEntities.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </EligibleEntities>\n");
        buf.append( ws + "  <Cost>" + String.valueOf(Cost) + "</Cost>\n");
        if (StartLocation!= null){
           buf.append( ws + "  <StartLocation>\n");
           buf.append( ( StartLocation.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </StartLocation>\n");
        }
        buf.append( ws + "  <StartHeading>" + String.valueOf(StartHeading) + "</StartHeading>\n");
        if (EndLocation!= null){
           buf.append( ws + "  <EndLocation>\n");
           buf.append( ( EndLocation.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </EndLocation>\n");
        }
        buf.append( ws + "  <EndHeading>" + String.valueOf(EndHeading) + "</EndHeading>\n");
        buf.append( ws + "</TaskOption>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        TaskOption o = (TaskOption) anotherObj;
        if (TaskID != o.TaskID) return false;
        if (OptionID != o.OptionID) return false;
         if (!EligibleEntities.equals( o.EligibleEntities)) return false;
        if (Cost != o.Cost) return false;
        if (StartLocation == null && o.StartLocation != null) return false;
        if ( StartLocation!= null && !StartLocation.equals(o.StartLocation)) return false;
        if (StartHeading != o.StartHeading) return false;
        if (EndLocation == null && o.EndLocation != null) return false;
        if ( EndLocation!= null && !EndLocation.equals(o.EndLocation)) return false;
        if (EndHeading != o.EndHeading) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
