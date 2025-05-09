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
            Provides information regarding the state of a simulation or real-world.  <br/>        
*/
public class SessionStatus extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 46;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "SessionStatus";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.SessionStatus";

    /**  The current state of the session (Units: None)*/
    @LmcpType("SimulationStatusType")
    protected afrl.cmasi.SimulationStatusType State = afrl.cmasi.SimulationStatusType.Stopped;
    /**  The simulation or scenario start time. This is absolute time in milliseconds since epoch (1 Jan 1970 00:00 GMT). If this field is zero, then no start time is specfied and each sim component is to use the first receipt of this Struct with a SimStatus of "Running" as the start time. (Units: millisecond)*/
    @LmcpType("int64")
    protected long StartTime = 0L;
    /**  The current time in scenario time . This is the internal time for the simulation, not the absolute time since epoch, as in "StartTime". The internal simualation time is based on the start time and the real-time muiltiple, which can change throughout the simulation. (Units: millisecond)*/
    @LmcpType("int64")
    protected long ScenarioTime = 0L;
    /**  The ratio of simulation time to real time. Values greater than 1.0 denote faster than real-time. Values less than 0.0 have no meaning. (Units: None)*/
    @LmcpType("real32")
    protected float RealTimeMultiple = (float)0;
    /**  A field to store command-line style parameters used to initialize or modify an execution. (Units: None)*/
    @LmcpType("KeyValuePair")
    protected java.util.ArrayList<afrl.cmasi.KeyValuePair> Parameters = new java.util.ArrayList<afrl.cmasi.KeyValuePair>();

    
    public SessionStatus() {
    }

    public SessionStatus(afrl.cmasi.SimulationStatusType State, long StartTime, long ScenarioTime, float RealTimeMultiple){
        this.State = State;
        this.StartTime = StartTime;
        this.ScenarioTime = ScenarioTime;
        this.RealTimeMultiple = RealTimeMultiple;
    }


    public SessionStatus clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            SessionStatus newObj = new SessionStatus();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The current state of the session (Units: None)*/
    public afrl.cmasi.SimulationStatusType getState() { return State; }

    /**  The current state of the session (Units: None)*/
    public SessionStatus setState( afrl.cmasi.SimulationStatusType val ) {
        State = val;
        return this;
    }

    /**  The simulation or scenario start time. This is absolute time in milliseconds since epoch (1 Jan 1970 00:00 GMT). If this field is zero, then no start time is specfied and each sim component is to use the first receipt of this Struct with a SimStatus of "Running" as the start time. (Units: millisecond)*/
    public long getStartTime() { return StartTime; }

    /**  The simulation or scenario start time. This is absolute time in milliseconds since epoch (1 Jan 1970 00:00 GMT). If this field is zero, then no start time is specfied and each sim component is to use the first receipt of this Struct with a SimStatus of "Running" as the start time. (Units: millisecond)*/
    public SessionStatus setStartTime( long val ) {
        StartTime = val;
        return this;
    }

    /**  The current time in scenario time . This is the internal time for the simulation, not the absolute time since epoch, as in "StartTime". The internal simualation time is based on the start time and the real-time muiltiple, which can change throughout the simulation. (Units: millisecond)*/
    public long getScenarioTime() { return ScenarioTime; }

    /**  The current time in scenario time . This is the internal time for the simulation, not the absolute time since epoch, as in "StartTime". The internal simualation time is based on the start time and the real-time muiltiple, which can change throughout the simulation. (Units: millisecond)*/
    public SessionStatus setScenarioTime( long val ) {
        ScenarioTime = val;
        return this;
    }

    /**  The ratio of simulation time to real time. Values greater than 1.0 denote faster than real-time. Values less than 0.0 have no meaning. (Units: None)*/
    public float getRealTimeMultiple() { return RealTimeMultiple; }

    /**  The ratio of simulation time to real time. Values greater than 1.0 denote faster than real-time. Values less than 0.0 have no meaning. (Units: None)*/
    public SessionStatus setRealTimeMultiple( float val ) {
        RealTimeMultiple = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.KeyValuePair> getParameters() {
        return Parameters;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 24; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(Parameters);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        State = afrl.cmasi.SimulationStatusType.unpack( in );

        StartTime = LMCPUtil.getInt64(in);

        ScenarioTime = LMCPUtil.getInt64(in);

        RealTimeMultiple = LMCPUtil.getReal32(in);

        Parameters.clear();
        int Parameters_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Parameters_len; i++){
        Parameters.add( (afrl.cmasi.KeyValuePair) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        State.pack(out);
        LMCPUtil.putInt64(out, StartTime);
        LMCPUtil.putInt64(out, ScenarioTime);
        LMCPUtil.putReal32(out, RealTimeMultiple);
        LMCPUtil.putUint16(out, Parameters.size());
        for(int i=0; i<Parameters.size(); i++){
            LMCPUtil.putObject(out, Parameters.get(i));
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
        buf.append( ws + "<SessionStatus Series=\"CMASI\">\n");
        buf.append( ws + "  <State>" + String.valueOf(State) + "</State>\n");
        buf.append( ws + "  <StartTime>" + String.valueOf(StartTime) + "</StartTime>\n");
        buf.append( ws + "  <ScenarioTime>" + String.valueOf(ScenarioTime) + "</ScenarioTime>\n");
        buf.append( ws + "  <RealTimeMultiple>" + String.valueOf(RealTimeMultiple) + "</RealTimeMultiple>\n");
        buf.append( ws + "  <Parameters>\n");
        for (int i=0; i<Parameters.size(); i++) {
            buf.append( Parameters.get(i) == null ? ( ws + "    <null/>\n") : (Parameters.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Parameters>\n");
        buf.append( ws + "</SessionStatus>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        SessionStatus o = (SessionStatus) anotherObj;
        if (State != o.State) return false;
        if (StartTime != o.StartTime) return false;
        if (ScenarioTime != o.ScenarioTime) return false;
        if (RealTimeMultiple != o.RealTimeMultiple) return false;
         if (!Parameters.equals( o.Parameters)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)RealTimeMultiple;

        return hash + super.hashCode();
    }
    
}
