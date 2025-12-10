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
 Message for reporting onboard status of UxAS 
*/
public class OnboardStatusReport extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 13;

    public static final String SERIES_NAME = "UXNATIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149751333668345413L;
    public static final int SERIES_VERSION = 9;


    private static final String TYPE_NAME = "OnboardStatusReport";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.uxnative.OnboardStatusReport";

    /**  ID of vehicle for status report (Units: None)*/
    @LmcpType("int64")
    protected long VehicleID = 0L;
    /**  List of entities in communication with vehicle (from join/exit message traffic) (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> ConnectedEntities = new java.util.ArrayList<Long>();
    /**  Current task list being carried out by vehicle (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> CurrentTaskList = new java.util.ArrayList<Long>();
    /**  Entity has valid state (Units: None)*/
    @LmcpType("bool")
    protected boolean ValidState = false;
    /**  Entity is authorized to act (Units: None)*/
    @LmcpType("bool")
    protected boolean ValidAuthorization = false;
    /**  Entity is authorized to change speed (Units: None)*/
    @LmcpType("bool")
    protected boolean SpeedAuthorization = false;
    /**  Entity is authorized to change payload/gimbal (Units: None)*/
    @LmcpType("bool")
    protected boolean GimbalAuthorization = false;
    /**  Time of last entity state message received (Units: milliseconds since 1 Jan 1970)*/
    @LmcpType("int64")
    protected long VehicleTime = 0L;

    
    public OnboardStatusReport() {
    }

    public OnboardStatusReport(long VehicleID, boolean ValidState, boolean ValidAuthorization, boolean SpeedAuthorization, boolean GimbalAuthorization, long VehicleTime){
        this.VehicleID = VehicleID;
        this.ValidState = ValidState;
        this.ValidAuthorization = ValidAuthorization;
        this.SpeedAuthorization = SpeedAuthorization;
        this.GimbalAuthorization = GimbalAuthorization;
        this.VehicleTime = VehicleTime;
    }


    public OnboardStatusReport clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            OnboardStatusReport newObj = new OnboardStatusReport();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID of vehicle for status report (Units: None)*/
    public long getVehicleID() { return VehicleID; }

    /**  ID of vehicle for status report (Units: None)*/
    public OnboardStatusReport setVehicleID( long val ) {
        VehicleID = val;
        return this;
    }

    public java.util.ArrayList<Long> getConnectedEntities() {
        return ConnectedEntities;
    }

    public java.util.ArrayList<Long> getCurrentTaskList() {
        return CurrentTaskList;
    }

    /**  Entity has valid state (Units: None)*/
    public boolean getValidState() { return ValidState; }

    /**  Entity has valid state (Units: None)*/
    public OnboardStatusReport setValidState( boolean val ) {
        ValidState = val;
        return this;
    }

    /**  Entity is authorized to act (Units: None)*/
    public boolean getValidAuthorization() { return ValidAuthorization; }

    /**  Entity is authorized to act (Units: None)*/
    public OnboardStatusReport setValidAuthorization( boolean val ) {
        ValidAuthorization = val;
        return this;
    }

    /**  Entity is authorized to change speed (Units: None)*/
    public boolean getSpeedAuthorization() { return SpeedAuthorization; }

    /**  Entity is authorized to change speed (Units: None)*/
    public OnboardStatusReport setSpeedAuthorization( boolean val ) {
        SpeedAuthorization = val;
        return this;
    }

    /**  Entity is authorized to change payload/gimbal (Units: None)*/
    public boolean getGimbalAuthorization() { return GimbalAuthorization; }

    /**  Entity is authorized to change payload/gimbal (Units: None)*/
    public OnboardStatusReport setGimbalAuthorization( boolean val ) {
        GimbalAuthorization = val;
        return this;
    }

    /**  Time of last entity state message received (Units: milliseconds since 1 Jan 1970)*/
    public long getVehicleTime() { return VehicleTime; }

    /**  Time of last entity state message received (Units: milliseconds since 1 Jan 1970)*/
    public OnboardStatusReport setVehicleTime( long val ) {
        VehicleTime = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 20; // accounts for primitive types
        
        size += 2 + 8 * ConnectedEntities.size();
        
        size += 2 + 8 * CurrentTaskList.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        VehicleID = LMCPUtil.getInt64(in);

        ConnectedEntities.clear();
        int ConnectedEntities_len = LMCPUtil.getUint16(in);
        for(int i=0; i<ConnectedEntities_len; i++){
            ConnectedEntities.add(LMCPUtil.getInt64(in));
        }
        CurrentTaskList.clear();
        int CurrentTaskList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<CurrentTaskList_len; i++){
            CurrentTaskList.add(LMCPUtil.getInt64(in));
        }
        ValidState = LMCPUtil.getBool(in);

        ValidAuthorization = LMCPUtil.getBool(in);

        SpeedAuthorization = LMCPUtil.getBool(in);

        GimbalAuthorization = LMCPUtil.getBool(in);

        VehicleTime = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, VehicleID);
        LMCPUtil.putUint16(out, ConnectedEntities.size());
        for(int i=0; i<ConnectedEntities.size(); i++){
            LMCPUtil.putInt64(out, ConnectedEntities.get(i));
        }
        LMCPUtil.putUint16(out, CurrentTaskList.size());
        for(int i=0; i<CurrentTaskList.size(); i++){
            LMCPUtil.putInt64(out, CurrentTaskList.get(i));
        }
        LMCPUtil.putBool(out, ValidState);
        LMCPUtil.putBool(out, ValidAuthorization);
        LMCPUtil.putBool(out, SpeedAuthorization);
        LMCPUtil.putBool(out, GimbalAuthorization);
        LMCPUtil.putInt64(out, VehicleTime);

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
        buf.append( ws + "<OnboardStatusReport Series=\"UXNATIVE\">\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <ConnectedEntities>\n");
        for (int i=0; i<ConnectedEntities.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(ConnectedEntities.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </ConnectedEntities>\n");
        buf.append( ws + "  <CurrentTaskList>\n");
        for (int i=0; i<CurrentTaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(CurrentTaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </CurrentTaskList>\n");
        buf.append( ws + "  <ValidState>" + String.valueOf(ValidState) + "</ValidState>\n");
        buf.append( ws + "  <ValidAuthorization>" + String.valueOf(ValidAuthorization) + "</ValidAuthorization>\n");
        buf.append( ws + "  <SpeedAuthorization>" + String.valueOf(SpeedAuthorization) + "</SpeedAuthorization>\n");
        buf.append( ws + "  <GimbalAuthorization>" + String.valueOf(GimbalAuthorization) + "</GimbalAuthorization>\n");
        buf.append( ws + "  <VehicleTime>" + String.valueOf(VehicleTime) + "</VehicleTime>\n");
        buf.append( ws + "</OnboardStatusReport>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        OnboardStatusReport o = (OnboardStatusReport) anotherObj;
        if (VehicleID != o.VehicleID) return false;
         if (!ConnectedEntities.equals( o.ConnectedEntities)) return false;
         if (!CurrentTaskList.equals( o.CurrentTaskList)) return false;
        if (ValidState != o.ValidState) return false;
        if (ValidAuthorization != o.ValidAuthorization) return false;
        if (SpeedAuthorization != o.SpeedAuthorization) return false;
        if (GimbalAuthorization != o.GimbalAuthorization) return false;
        if (VehicleTime != o.VehicleTime) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
