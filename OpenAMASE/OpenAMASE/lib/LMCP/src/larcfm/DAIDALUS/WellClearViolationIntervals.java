// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package larcfm.DAIDALUS;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 List of  intervals that will result in a WellClear violation within the detection window 
*/
public class WellClearViolationIntervals extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 9;

    public static final String SERIES_NAME = "DAIDALUS";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4918292825567417683L;
    public static final int SERIES_VERSION = 1;


    private static final String TYPE_NAME = "WellClearViolationIntervals";

    private static final String FULL_LMCP_TYPE_NAME = "larcfm.DAIDALUS.WellClearViolationIntervals";

    /**  List of Entities involved in Well Clear Violations (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> EntityList = new java.util.ArrayList<Long>();
    /**  List of times to violation (Units: seconds)*/
    @LmcpType("real64")
    protected java.util.ArrayList<Double> TimeToViolationList = new java.util.ArrayList<Double>();
    /**  List of alert levels for intruders (Units: None)*/
    @LmcpType("real64")
    protected java.util.ArrayList<Double> AlertLevelList = new java.util.ArrayList<Double>();
    /**  Entity Id that generated this message (Units: None)*/
    @LmcpType("uint32")
    protected long EntityId = 0L;
    /**  Ownship Heading (Units: degrees)*/
    @LmcpType("real64")
    protected double CurrentHeading = 0;
    /**  Ownship Ground Speed (Units: m/s)*/
    @LmcpType("real64")
    protected double CurrentGoundSpeed = 0;
    /**  Ownship Vertical Speed (Units: m/s)*/
    @LmcpType("real64")
    protected double CurrentVerticalSpeed = 0;
    /**  Ownship Altitude (Units: m)*/
    @LmcpType("real64")
    protected double CurrentAltitude = 0;
    /**  Ownship Latitude (Units: degrees)*/
    @LmcpType("real64")
    protected double CurrentLatitude = 0;
    /**  Ownship Longitude (Units: degrees)*/
    @LmcpType("real64")
    protected double CurrentLongitude = 0;
    /**  Time of the current detection (Units: seconds)*/
    @LmcpType("real64")
    protected double CurrentTime = 0;
    /**  List of ground heading intervals that will result in a WellClear violation within the detection window (Units: None)*/
    @LmcpType("GroundHeadingInterval")
    protected java.util.ArrayList<larcfm.DAIDALUS.GroundHeadingInterval> WCVGroundHeadingIntervals = new java.util.ArrayList<larcfm.DAIDALUS.GroundHeadingInterval>();
    /**  List of the region types associated with detected ground heading violations (Units: None)*/
    @LmcpType("BandsRegion")
    protected java.util.ArrayList<larcfm.DAIDALUS.BandsRegion> WCVGroundHeadingRegions = new java.util.ArrayList<larcfm.DAIDALUS.BandsRegion>();
    /**  List of ground speed intervals that will result in a WellClear violation within the detection window (Units: None)*/
    @LmcpType("GroundSpeedInterval")
    protected java.util.ArrayList<larcfm.DAIDALUS.GroundSpeedInterval> WCVGroundSpeedIntervals = new java.util.ArrayList<larcfm.DAIDALUS.GroundSpeedInterval>();
    /**  List of the region types associated with detected ground speed violations (Units: None)*/
    @LmcpType("BandsRegion")
    protected java.util.ArrayList<larcfm.DAIDALUS.BandsRegion> WCVGroundSpeedRegions = new java.util.ArrayList<larcfm.DAIDALUS.BandsRegion>();
    /**  List of vertical speed intervals that will result in a WellClear violation within the detection window (Units: None)*/
    @LmcpType("VerticalSpeedInterval")
    protected java.util.ArrayList<larcfm.DAIDALUS.VerticalSpeedInterval> WCVVerticalSpeedIntervals = new java.util.ArrayList<larcfm.DAIDALUS.VerticalSpeedInterval>();
    /**  List of the region types associated with the detected vertical speed violation (Units: None)*/
    @LmcpType("BandsRegion")
    protected java.util.ArrayList<larcfm.DAIDALUS.BandsRegion> WCVVerticalSpeedRegions = new java.util.ArrayList<larcfm.DAIDALUS.BandsRegion>();
    /**  List of altitude intervals that will result in a WellClear violation within the detection window (Units: None)*/
    @LmcpType("AltitudeInterval")
    protected java.util.ArrayList<larcfm.DAIDALUS.AltitudeInterval> WCVAlitudeIntervals = new java.util.ArrayList<larcfm.DAIDALUS.AltitudeInterval>();
    /**  List of the region types associated with the detected altitude violations (Units: None)*/
    @LmcpType("BandsRegion")
    protected java.util.ArrayList<larcfm.DAIDALUS.BandsRegion> WCVAltitudeRegions = new java.util.ArrayList<larcfm.DAIDALUS.BandsRegion>();
    /**  List of the Recovery ground heading intervals that will exit a WellClear violation in the minimum time (Units: None)*/
    @LmcpType("GroundHeadingRecoveryInterval")
    protected java.util.ArrayList<larcfm.DAIDALUS.GroundHeadingRecoveryInterval> RecoveryGroundHeadingIntervals = new java.util.ArrayList<larcfm.DAIDALUS.GroundHeadingRecoveryInterval>();
    /**  List of the Recovery ground speed intervals that will exit a WellClear violation in the minimum time (Units: None)*/
    @LmcpType("GroundSpeedRecoveryInterval")
    protected java.util.ArrayList<larcfm.DAIDALUS.GroundSpeedRecoveryInterval> RecoveryGroundSpeedIntervals = new java.util.ArrayList<larcfm.DAIDALUS.GroundSpeedRecoveryInterval>();
    /**  List of the Recovery vertical speed intervals that will exit a WellClear violation in the minimum time (Units: None)*/
    @LmcpType("VerticalSpeedRecoveryInterval")
    protected java.util.ArrayList<larcfm.DAIDALUS.VerticalSpeedRecoveryInterval> RecoveryVerticalSpeedIntervals = new java.util.ArrayList<larcfm.DAIDALUS.VerticalSpeedRecoveryInterval>();
    /**  List of the Recovery altitude intervals that will exit a WellCLear violation in the minimum time (Units: None)*/
    @LmcpType("AltitudeRecoveryInterval")
    protected java.util.ArrayList<larcfm.DAIDALUS.AltitudeRecoveryInterval> RecoveryAltitudeIntervals = new java.util.ArrayList<larcfm.DAIDALUS.AltitudeRecoveryInterval>();

    
    public WellClearViolationIntervals() {
    }

    public WellClearViolationIntervals(long EntityId, double CurrentHeading, double CurrentGoundSpeed, double CurrentVerticalSpeed, double CurrentAltitude, double CurrentLatitude, double CurrentLongitude, double CurrentTime){
        this.EntityId = EntityId;
        this.CurrentHeading = CurrentHeading;
        this.CurrentGoundSpeed = CurrentGoundSpeed;
        this.CurrentVerticalSpeed = CurrentVerticalSpeed;
        this.CurrentAltitude = CurrentAltitude;
        this.CurrentLatitude = CurrentLatitude;
        this.CurrentLongitude = CurrentLongitude;
        this.CurrentTime = CurrentTime;
    }


    public WellClearViolationIntervals clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            WellClearViolationIntervals newObj = new WellClearViolationIntervals();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public java.util.ArrayList<Long> getEntityList() {
        return EntityList;
    }

    public java.util.ArrayList<Double> getTimeToViolationList() {
        return TimeToViolationList;
    }

    public java.util.ArrayList<Double> getAlertLevelList() {
        return AlertLevelList;
    }

    /**  Entity Id that generated this message (Units: None)*/
    public long getEntityId() { return EntityId; }

    /**  Entity Id that generated this message (Units: None)*/
    public WellClearViolationIntervals setEntityId( long val ) {
        EntityId = val;
        return this;
    }

    /**  Ownship Heading (Units: degrees)*/
    public double getCurrentHeading() { return CurrentHeading; }

    /**  Ownship Heading (Units: degrees)*/
    public WellClearViolationIntervals setCurrentHeading( double val ) {
        CurrentHeading = val;
        return this;
    }

    /**  Ownship Ground Speed (Units: m/s)*/
    public double getCurrentGoundSpeed() { return CurrentGoundSpeed; }

    /**  Ownship Ground Speed (Units: m/s)*/
    public WellClearViolationIntervals setCurrentGoundSpeed( double val ) {
        CurrentGoundSpeed = val;
        return this;
    }

    /**  Ownship Vertical Speed (Units: m/s)*/
    public double getCurrentVerticalSpeed() { return CurrentVerticalSpeed; }

    /**  Ownship Vertical Speed (Units: m/s)*/
    public WellClearViolationIntervals setCurrentVerticalSpeed( double val ) {
        CurrentVerticalSpeed = val;
        return this;
    }

    /**  Ownship Altitude (Units: m)*/
    public double getCurrentAltitude() { return CurrentAltitude; }

    /**  Ownship Altitude (Units: m)*/
    public WellClearViolationIntervals setCurrentAltitude( double val ) {
        CurrentAltitude = val;
        return this;
    }

    /**  Ownship Latitude (Units: degrees)*/
    public double getCurrentLatitude() { return CurrentLatitude; }

    /**  Ownship Latitude (Units: degrees)*/
    public WellClearViolationIntervals setCurrentLatitude( double val ) {
        CurrentLatitude = val;
        return this;
    }

    /**  Ownship Longitude (Units: degrees)*/
    public double getCurrentLongitude() { return CurrentLongitude; }

    /**  Ownship Longitude (Units: degrees)*/
    public WellClearViolationIntervals setCurrentLongitude( double val ) {
        CurrentLongitude = val;
        return this;
    }

    /**  Time of the current detection (Units: seconds)*/
    public double getCurrentTime() { return CurrentTime; }

    /**  Time of the current detection (Units: seconds)*/
    public WellClearViolationIntervals setCurrentTime( double val ) {
        CurrentTime = val;
        return this;
    }

    public java.util.ArrayList<larcfm.DAIDALUS.GroundHeadingInterval> getWCVGroundHeadingIntervals() {
        return WCVGroundHeadingIntervals;
    }

    public java.util.ArrayList<larcfm.DAIDALUS.BandsRegion> getWCVGroundHeadingRegions() {
        return WCVGroundHeadingRegions;
    }

    public java.util.ArrayList<larcfm.DAIDALUS.GroundSpeedInterval> getWCVGroundSpeedIntervals() {
        return WCVGroundSpeedIntervals;
    }

    public java.util.ArrayList<larcfm.DAIDALUS.BandsRegion> getWCVGroundSpeedRegions() {
        return WCVGroundSpeedRegions;
    }

    public java.util.ArrayList<larcfm.DAIDALUS.VerticalSpeedInterval> getWCVVerticalSpeedIntervals() {
        return WCVVerticalSpeedIntervals;
    }

    public java.util.ArrayList<larcfm.DAIDALUS.BandsRegion> getWCVVerticalSpeedRegions() {
        return WCVVerticalSpeedRegions;
    }

    public java.util.ArrayList<larcfm.DAIDALUS.AltitudeInterval> getWCVAlitudeIntervals() {
        return WCVAlitudeIntervals;
    }

    public java.util.ArrayList<larcfm.DAIDALUS.BandsRegion> getWCVAltitudeRegions() {
        return WCVAltitudeRegions;
    }

    public java.util.ArrayList<larcfm.DAIDALUS.GroundHeadingRecoveryInterval> getRecoveryGroundHeadingIntervals() {
        return RecoveryGroundHeadingIntervals;
    }

    public java.util.ArrayList<larcfm.DAIDALUS.GroundSpeedRecoveryInterval> getRecoveryGroundSpeedIntervals() {
        return RecoveryGroundSpeedIntervals;
    }

    public java.util.ArrayList<larcfm.DAIDALUS.VerticalSpeedRecoveryInterval> getRecoveryVerticalSpeedIntervals() {
        return RecoveryVerticalSpeedIntervals;
    }

    public java.util.ArrayList<larcfm.DAIDALUS.AltitudeRecoveryInterval> getRecoveryAltitudeIntervals() {
        return RecoveryAltitudeIntervals;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 60; // accounts for primitive types
        
        size += 2 + 8 * EntityList.size();
        
        size += 2 + 8 * TimeToViolationList.size();
        
        size += 2 + 8 * AlertLevelList.size();
        size += 2;
        size += LMCPUtil.sizeOfList(WCVGroundHeadingIntervals);
        
        size += 2 + 4 * WCVGroundHeadingRegions.size();
        size += 2;
        size += LMCPUtil.sizeOfList(WCVGroundSpeedIntervals);
        
        size += 2 + 4 * WCVGroundSpeedRegions.size();
        size += 2;
        size += LMCPUtil.sizeOfList(WCVVerticalSpeedIntervals);
        
        size += 2 + 4 * WCVVerticalSpeedRegions.size();
        size += 2;
        size += LMCPUtil.sizeOfList(WCVAlitudeIntervals);
        
        size += 2 + 4 * WCVAltitudeRegions.size();
        size += 2;
        size += LMCPUtil.sizeOfList(RecoveryGroundHeadingIntervals);
        size += 2;
        size += LMCPUtil.sizeOfList(RecoveryGroundSpeedIntervals);
        size += 2;
        size += LMCPUtil.sizeOfList(RecoveryVerticalSpeedIntervals);
        size += 2;
        size += LMCPUtil.sizeOfList(RecoveryAltitudeIntervals);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        EntityList.clear();
        int EntityList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<EntityList_len; i++){
            EntityList.add(LMCPUtil.getInt64(in));
        }
        TimeToViolationList.clear();
        int TimeToViolationList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<TimeToViolationList_len; i++){
            TimeToViolationList.add(LMCPUtil.getReal64(in));
        }
        AlertLevelList.clear();
        int AlertLevelList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<AlertLevelList_len; i++){
            AlertLevelList.add(LMCPUtil.getReal64(in));
        }
        EntityId = LMCPUtil.getUint32(in);

        CurrentHeading = LMCPUtil.getReal64(in);

        CurrentGoundSpeed = LMCPUtil.getReal64(in);

        CurrentVerticalSpeed = LMCPUtil.getReal64(in);

        CurrentAltitude = LMCPUtil.getReal64(in);

        CurrentLatitude = LMCPUtil.getReal64(in);

        CurrentLongitude = LMCPUtil.getReal64(in);

        CurrentTime = LMCPUtil.getReal64(in);

        WCVGroundHeadingIntervals.clear();
        int WCVGroundHeadingIntervals_len = LMCPUtil.getUint16(in);
        for(int i=0; i<WCVGroundHeadingIntervals_len; i++){
        WCVGroundHeadingIntervals.add( (larcfm.DAIDALUS.GroundHeadingInterval) LMCPUtil.getObject(in));
        }
        WCVGroundHeadingRegions.clear();
        int WCVGroundHeadingRegions_len = LMCPUtil.getUint16(in);
        for(int i=0; i<WCVGroundHeadingRegions_len; i++){
        WCVGroundHeadingRegions.add(larcfm.DAIDALUS.BandsRegion.unpack( in ));

        }
        WCVGroundSpeedIntervals.clear();
        int WCVGroundSpeedIntervals_len = LMCPUtil.getUint16(in);
        for(int i=0; i<WCVGroundSpeedIntervals_len; i++){
        WCVGroundSpeedIntervals.add( (larcfm.DAIDALUS.GroundSpeedInterval) LMCPUtil.getObject(in));
        }
        WCVGroundSpeedRegions.clear();
        int WCVGroundSpeedRegions_len = LMCPUtil.getUint16(in);
        for(int i=0; i<WCVGroundSpeedRegions_len; i++){
        WCVGroundSpeedRegions.add(larcfm.DAIDALUS.BandsRegion.unpack( in ));

        }
        WCVVerticalSpeedIntervals.clear();
        int WCVVerticalSpeedIntervals_len = LMCPUtil.getUint16(in);
        for(int i=0; i<WCVVerticalSpeedIntervals_len; i++){
        WCVVerticalSpeedIntervals.add( (larcfm.DAIDALUS.VerticalSpeedInterval) LMCPUtil.getObject(in));
        }
        WCVVerticalSpeedRegions.clear();
        int WCVVerticalSpeedRegions_len = LMCPUtil.getUint16(in);
        for(int i=0; i<WCVVerticalSpeedRegions_len; i++){
        WCVVerticalSpeedRegions.add(larcfm.DAIDALUS.BandsRegion.unpack( in ));

        }
        WCVAlitudeIntervals.clear();
        int WCVAlitudeIntervals_len = LMCPUtil.getUint16(in);
        for(int i=0; i<WCVAlitudeIntervals_len; i++){
        WCVAlitudeIntervals.add( (larcfm.DAIDALUS.AltitudeInterval) LMCPUtil.getObject(in));
        }
        WCVAltitudeRegions.clear();
        int WCVAltitudeRegions_len = LMCPUtil.getUint16(in);
        for(int i=0; i<WCVAltitudeRegions_len; i++){
        WCVAltitudeRegions.add(larcfm.DAIDALUS.BandsRegion.unpack( in ));

        }
        RecoveryGroundHeadingIntervals.clear();
        int RecoveryGroundHeadingIntervals_len = LMCPUtil.getUint16(in);
        for(int i=0; i<RecoveryGroundHeadingIntervals_len; i++){
        RecoveryGroundHeadingIntervals.add( (larcfm.DAIDALUS.GroundHeadingRecoveryInterval) LMCPUtil.getObject(in));
        }
        RecoveryGroundSpeedIntervals.clear();
        int RecoveryGroundSpeedIntervals_len = LMCPUtil.getUint16(in);
        for(int i=0; i<RecoveryGroundSpeedIntervals_len; i++){
        RecoveryGroundSpeedIntervals.add( (larcfm.DAIDALUS.GroundSpeedRecoveryInterval) LMCPUtil.getObject(in));
        }
        RecoveryVerticalSpeedIntervals.clear();
        int RecoveryVerticalSpeedIntervals_len = LMCPUtil.getUint16(in);
        for(int i=0; i<RecoveryVerticalSpeedIntervals_len; i++){
        RecoveryVerticalSpeedIntervals.add( (larcfm.DAIDALUS.VerticalSpeedRecoveryInterval) LMCPUtil.getObject(in));
        }
        RecoveryAltitudeIntervals.clear();
        int RecoveryAltitudeIntervals_len = LMCPUtil.getUint16(in);
        for(int i=0; i<RecoveryAltitudeIntervals_len; i++){
        RecoveryAltitudeIntervals.add( (larcfm.DAIDALUS.AltitudeRecoveryInterval) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putUint16(out, EntityList.size());
        for(int i=0; i<EntityList.size(); i++){
            LMCPUtil.putInt64(out, EntityList.get(i));
        }
        LMCPUtil.putUint16(out, TimeToViolationList.size());
        for(int i=0; i<TimeToViolationList.size(); i++){
            LMCPUtil.putReal64(out, TimeToViolationList.get(i));
        }
        LMCPUtil.putUint16(out, AlertLevelList.size());
        for(int i=0; i<AlertLevelList.size(); i++){
            LMCPUtil.putReal64(out, AlertLevelList.get(i));
        }
        LMCPUtil.putUint32(out, EntityId);
        LMCPUtil.putReal64(out, CurrentHeading);
        LMCPUtil.putReal64(out, CurrentGoundSpeed);
        LMCPUtil.putReal64(out, CurrentVerticalSpeed);
        LMCPUtil.putReal64(out, CurrentAltitude);
        LMCPUtil.putReal64(out, CurrentLatitude);
        LMCPUtil.putReal64(out, CurrentLongitude);
        LMCPUtil.putReal64(out, CurrentTime);
        LMCPUtil.putUint16(out, WCVGroundHeadingIntervals.size());
        for(int i=0; i<WCVGroundHeadingIntervals.size(); i++){
            LMCPUtil.putObject(out, WCVGroundHeadingIntervals.get(i));
        }
        LMCPUtil.putUint16(out, WCVGroundHeadingRegions.size());
        for(int i=0; i<WCVGroundHeadingRegions.size(); i++){
            WCVGroundHeadingRegions.get(i).pack(out);
        }
        LMCPUtil.putUint16(out, WCVGroundSpeedIntervals.size());
        for(int i=0; i<WCVGroundSpeedIntervals.size(); i++){
            LMCPUtil.putObject(out, WCVGroundSpeedIntervals.get(i));
        }
        LMCPUtil.putUint16(out, WCVGroundSpeedRegions.size());
        for(int i=0; i<WCVGroundSpeedRegions.size(); i++){
            WCVGroundSpeedRegions.get(i).pack(out);
        }
        LMCPUtil.putUint16(out, WCVVerticalSpeedIntervals.size());
        for(int i=0; i<WCVVerticalSpeedIntervals.size(); i++){
            LMCPUtil.putObject(out, WCVVerticalSpeedIntervals.get(i));
        }
        LMCPUtil.putUint16(out, WCVVerticalSpeedRegions.size());
        for(int i=0; i<WCVVerticalSpeedRegions.size(); i++){
            WCVVerticalSpeedRegions.get(i).pack(out);
        }
        LMCPUtil.putUint16(out, WCVAlitudeIntervals.size());
        for(int i=0; i<WCVAlitudeIntervals.size(); i++){
            LMCPUtil.putObject(out, WCVAlitudeIntervals.get(i));
        }
        LMCPUtil.putUint16(out, WCVAltitudeRegions.size());
        for(int i=0; i<WCVAltitudeRegions.size(); i++){
            WCVAltitudeRegions.get(i).pack(out);
        }
        LMCPUtil.putUint16(out, RecoveryGroundHeadingIntervals.size());
        for(int i=0; i<RecoveryGroundHeadingIntervals.size(); i++){
            LMCPUtil.putObject(out, RecoveryGroundHeadingIntervals.get(i));
        }
        LMCPUtil.putUint16(out, RecoveryGroundSpeedIntervals.size());
        for(int i=0; i<RecoveryGroundSpeedIntervals.size(); i++){
            LMCPUtil.putObject(out, RecoveryGroundSpeedIntervals.get(i));
        }
        LMCPUtil.putUint16(out, RecoveryVerticalSpeedIntervals.size());
        for(int i=0; i<RecoveryVerticalSpeedIntervals.size(); i++){
            LMCPUtil.putObject(out, RecoveryVerticalSpeedIntervals.get(i));
        }
        LMCPUtil.putUint16(out, RecoveryAltitudeIntervals.size());
        for(int i=0; i<RecoveryAltitudeIntervals.size(); i++){
            LMCPUtil.putObject(out, RecoveryAltitudeIntervals.get(i));
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
        buf.append( ws + "<WellClearViolationIntervals Series=\"DAIDALUS\">\n");
        buf.append( ws + "  <EntityList>\n");
        for (int i=0; i<EntityList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(EntityList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </EntityList>\n");
        buf.append( ws + "  <TimeToViolationList>\n");
        for (int i=0; i<TimeToViolationList.size(); i++) {
        buf.append( ws + "  <real64>" + String.valueOf(TimeToViolationList.get(i)) + "</real64>\n");
        }
        buf.append( ws + "  </TimeToViolationList>\n");
        buf.append( ws + "  <AlertLevelList>\n");
        for (int i=0; i<AlertLevelList.size(); i++) {
        buf.append( ws + "  <real64>" + String.valueOf(AlertLevelList.get(i)) + "</real64>\n");
        }
        buf.append( ws + "  </AlertLevelList>\n");
        buf.append( ws + "  <EntityId>" + String.valueOf(EntityId) + "</EntityId>\n");
        buf.append( ws + "  <CurrentHeading>" + String.valueOf(CurrentHeading) + "</CurrentHeading>\n");
        buf.append( ws + "  <CurrentGoundSpeed>" + String.valueOf(CurrentGoundSpeed) + "</CurrentGoundSpeed>\n");
        buf.append( ws + "  <CurrentVerticalSpeed>" + String.valueOf(CurrentVerticalSpeed) + "</CurrentVerticalSpeed>\n");
        buf.append( ws + "  <CurrentAltitude>" + String.valueOf(CurrentAltitude) + "</CurrentAltitude>\n");
        buf.append( ws + "  <CurrentLatitude>" + String.valueOf(CurrentLatitude) + "</CurrentLatitude>\n");
        buf.append( ws + "  <CurrentLongitude>" + String.valueOf(CurrentLongitude) + "</CurrentLongitude>\n");
        buf.append( ws + "  <CurrentTime>" + String.valueOf(CurrentTime) + "</CurrentTime>\n");
        buf.append( ws + "  <WCVGroundHeadingIntervals>\n");
        for (int i=0; i<WCVGroundHeadingIntervals.size(); i++) {
            buf.append( WCVGroundHeadingIntervals.get(i) == null ? ( ws + "    <null/>\n") : (WCVGroundHeadingIntervals.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </WCVGroundHeadingIntervals>\n");
        buf.append( ws + "  <WCVGroundHeadingRegions>\n");
        for (int i=0; i<WCVGroundHeadingRegions.size(); i++) {
        buf.append( ws + "  <BandsRegion>" + String.valueOf(WCVGroundHeadingRegions.get(i)) + "</BandsRegion>\n");
        }
        buf.append( ws + "  </WCVGroundHeadingRegions>\n");
        buf.append( ws + "  <WCVGroundSpeedIntervals>\n");
        for (int i=0; i<WCVGroundSpeedIntervals.size(); i++) {
            buf.append( WCVGroundSpeedIntervals.get(i) == null ? ( ws + "    <null/>\n") : (WCVGroundSpeedIntervals.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </WCVGroundSpeedIntervals>\n");
        buf.append( ws + "  <WCVGroundSpeedRegions>\n");
        for (int i=0; i<WCVGroundSpeedRegions.size(); i++) {
        buf.append( ws + "  <BandsRegion>" + String.valueOf(WCVGroundSpeedRegions.get(i)) + "</BandsRegion>\n");
        }
        buf.append( ws + "  </WCVGroundSpeedRegions>\n");
        buf.append( ws + "  <WCVVerticalSpeedIntervals>\n");
        for (int i=0; i<WCVVerticalSpeedIntervals.size(); i++) {
            buf.append( WCVVerticalSpeedIntervals.get(i) == null ? ( ws + "    <null/>\n") : (WCVVerticalSpeedIntervals.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </WCVVerticalSpeedIntervals>\n");
        buf.append( ws + "  <WCVVerticalSpeedRegions>\n");
        for (int i=0; i<WCVVerticalSpeedRegions.size(); i++) {
        buf.append( ws + "  <BandsRegion>" + String.valueOf(WCVVerticalSpeedRegions.get(i)) + "</BandsRegion>\n");
        }
        buf.append( ws + "  </WCVVerticalSpeedRegions>\n");
        buf.append( ws + "  <WCVAlitudeIntervals>\n");
        for (int i=0; i<WCVAlitudeIntervals.size(); i++) {
            buf.append( WCVAlitudeIntervals.get(i) == null ? ( ws + "    <null/>\n") : (WCVAlitudeIntervals.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </WCVAlitudeIntervals>\n");
        buf.append( ws + "  <WCVAltitudeRegions>\n");
        for (int i=0; i<WCVAltitudeRegions.size(); i++) {
        buf.append( ws + "  <BandsRegion>" + String.valueOf(WCVAltitudeRegions.get(i)) + "</BandsRegion>\n");
        }
        buf.append( ws + "  </WCVAltitudeRegions>\n");
        buf.append( ws + "  <RecoveryGroundHeadingIntervals>\n");
        for (int i=0; i<RecoveryGroundHeadingIntervals.size(); i++) {
            buf.append( RecoveryGroundHeadingIntervals.get(i) == null ? ( ws + "    <null/>\n") : (RecoveryGroundHeadingIntervals.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </RecoveryGroundHeadingIntervals>\n");
        buf.append( ws + "  <RecoveryGroundSpeedIntervals>\n");
        for (int i=0; i<RecoveryGroundSpeedIntervals.size(); i++) {
            buf.append( RecoveryGroundSpeedIntervals.get(i) == null ? ( ws + "    <null/>\n") : (RecoveryGroundSpeedIntervals.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </RecoveryGroundSpeedIntervals>\n");
        buf.append( ws + "  <RecoveryVerticalSpeedIntervals>\n");
        for (int i=0; i<RecoveryVerticalSpeedIntervals.size(); i++) {
            buf.append( RecoveryVerticalSpeedIntervals.get(i) == null ? ( ws + "    <null/>\n") : (RecoveryVerticalSpeedIntervals.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </RecoveryVerticalSpeedIntervals>\n");
        buf.append( ws + "  <RecoveryAltitudeIntervals>\n");
        for (int i=0; i<RecoveryAltitudeIntervals.size(); i++) {
            buf.append( RecoveryAltitudeIntervals.get(i) == null ? ( ws + "    <null/>\n") : (RecoveryAltitudeIntervals.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </RecoveryAltitudeIntervals>\n");
        buf.append( ws + "</WellClearViolationIntervals>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        WellClearViolationIntervals o = (WellClearViolationIntervals) anotherObj;
         if (!EntityList.equals( o.EntityList)) return false;
         if (!TimeToViolationList.equals( o.TimeToViolationList)) return false;
         if (!AlertLevelList.equals( o.AlertLevelList)) return false;
        if (EntityId != o.EntityId) return false;
        if (CurrentHeading != o.CurrentHeading) return false;
        if (CurrentGoundSpeed != o.CurrentGoundSpeed) return false;
        if (CurrentVerticalSpeed != o.CurrentVerticalSpeed) return false;
        if (CurrentAltitude != o.CurrentAltitude) return false;
        if (CurrentLatitude != o.CurrentLatitude) return false;
        if (CurrentLongitude != o.CurrentLongitude) return false;
        if (CurrentTime != o.CurrentTime) return false;
         if (!WCVGroundHeadingIntervals.equals( o.WCVGroundHeadingIntervals)) return false;
         if (!WCVGroundHeadingRegions.equals( o.WCVGroundHeadingRegions)) return false;
         if (!WCVGroundSpeedIntervals.equals( o.WCVGroundSpeedIntervals)) return false;
         if (!WCVGroundSpeedRegions.equals( o.WCVGroundSpeedRegions)) return false;
         if (!WCVVerticalSpeedIntervals.equals( o.WCVVerticalSpeedIntervals)) return false;
         if (!WCVVerticalSpeedRegions.equals( o.WCVVerticalSpeedRegions)) return false;
         if (!WCVAlitudeIntervals.equals( o.WCVAlitudeIntervals)) return false;
         if (!WCVAltitudeRegions.equals( o.WCVAltitudeRegions)) return false;
         if (!RecoveryGroundHeadingIntervals.equals( o.RecoveryGroundHeadingIntervals)) return false;
         if (!RecoveryGroundSpeedIntervals.equals( o.RecoveryGroundSpeedIntervals)) return false;
         if (!RecoveryVerticalSpeedIntervals.equals( o.RecoveryVerticalSpeedIntervals)) return false;
         if (!RecoveryAltitudeIntervals.equals( o.RecoveryAltitudeIntervals)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)EntityId;
        hash += 31 * (int)CurrentHeading;

        return hash + super.hashCode();
    }
    
}
