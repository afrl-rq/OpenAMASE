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
 DAIDALUS configuration parameters 
*/
public class DAIDALUSConfiguration extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 10;

    public static final String SERIES_NAME = "DAIDALUS";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4918292825567417683L;
    public static final int SERIES_VERSION = 1;


    private static final String TYPE_NAME = "DAIDALUSConfiguration";

    private static final String FULL_LMCP_TYPE_NAME = "larcfm.DAIDALUS.DAIDALUSConfiguration";

    /**  Entity Id that generated this message (Units: None)*/
    @LmcpType("uint32")
    protected long EntityId = 0L;
    /**  Time horizon of all DAIDALUS functions (Units: seconds)*/
    @LmcpType("real64")
    protected double LookAheadTime = 0;
    /**  Relative maximum horizontal direction maneuver to the left of current ownship direction (Units: degrees)*/
    @LmcpType("real64")
    protected double LeftTrack = 0;
    /**  Relative maximum horizontal direction maneuver to the right of current ownship direction (Units: degrees)*/
    @LmcpType("real64")
    protected double RightTrack = 0;
    /**  Absolute maximum horizontal speed maneuver (Units: meters per second)*/
    @LmcpType("real64")
    protected double MaxGroundSpeed = 0;
    /**  Absolute minimum horizontal speed maneuver (Units: meters per second)*/
    @LmcpType("real64")
    protected double MinGroundSpeed = 0;
    /**  Absolute maximum vertical speed maneuver (Units: meters per second)*/
    @LmcpType("real64")
    protected double MaxVerticalSpeed = 0;
    /**  Absolute minimum verrtical speed maneuver (Units: meters per second)*/
    @LmcpType("real64")
    protected double MinVerticalSpeed = 0;
    /**  Absolute maximum altitude maneuver (Units: meters)*/
    @LmcpType("real64")
    protected double MaxAltitude = 0;
    /**  Absolute minimum altitude maneuver (Units: meters)*/
    @LmcpType("real64")
    protected double MinAltitude = 0;
    /**  Granularity of horizontal direction maneuvers (Units: degrees)*/
    @LmcpType("real64")
    protected double TrackStep = 0;
    /**  Granularity of horizontal speed maneuvers (Units: meters per second)*/
    @LmcpType("real64")
    protected double GroundSpeedStep = 0;
    /**  Granularity of vertical speed maneuvers (Units: meters per second)*/
    @LmcpType("real64")
    protected double VerticalSpeedStep = 0;
    /**  Granularity of altitude maneuvers (Units: meters)*/
    @LmcpType("real64")
    protected double AltitudeStep = 0;
    /**  Horizontal acceleration used in the computation of horizontal speed maneuvers (Units: meters per second per second)*/
    @LmcpType("real64")
    protected double HorizontalAcceleration = 0;
    /**  Vertical accelereation used in the computation of vertical speed maneuvers (Units: G)*/
    @LmcpType("real64")
    protected double VerticalAcceleration = 0;
    /**  Turn rate used in the computation of horizontal direction maneuvers (Units: degrees per second)*/
    @LmcpType("real64")
    protected double TurnRate = 0;
    /**  Bank angle used in the computation of horizontal direction maneuvers (Units: degrees)*/
    @LmcpType("real64")
    protected double BankAngle = 0;
    /**  Vertical rate used in the computation of altitude maneuvers (Units: meters per second)*/
    @LmcpType("real64")
    protected double VerticalRate = 0;
    /**  Time delay to stabilize recovery maneuvers (Units: seconds)*/
    @LmcpType("real64")
    protected double RecoveryStabilityTime = 0;
    /**  Enable computation of horizontal direction recovery maneuvers (Units: None)*/
    @LmcpType("bool")
    protected boolean isRecoveryTrackBands;
    /**  Enable computation of horzontal speed recovery maneuvers (Units: None)*/
    @LmcpType("bool")
    protected boolean isRecoveryGroundSpeedBands;
    /**  Enable computation of vertical speed recovery maneuvers (Units: None)*/
    @LmcpType("bool")
    protected boolean isRecoveryVerticalSpeedBands;
    /**  Enable computation of altitude recovery maneuvers (Units: None)*/
    @LmcpType("bool")
    protected boolean isRecoveryAltitudeBands;
    /**  Enable computation of collision avoidance maneuvers (Units: None)*/
    @LmcpType("bool")
    protected boolean isCollisionAvoidanceBands;
    /**  Factor to reduce minimum horizontal/vertial recovery separation when computing avoidance maneuvers (Units: None)*/
    @LmcpType("bool")
    protected boolean CollisionAvoidanceBandsFactor;
    /**  Horizontal NMAC (Units: meters)*/
    @LmcpType("real64")
    protected double HorizontalNMAC = 0;
    /**  Minimum horizontal separation used in the computation of recovery maneuvers (Units: meters)*/
    @LmcpType("real64")
    protected double MinHorizontalRecovery = 0;
    /**  Vertical NMAC (Units: meters)*/
    @LmcpType("real64")
    protected double VerticalNMAC = 0;
    /**  Minimum vertical separation used in the computation of recovery maneuvers(Units: meters)*/
    @LmcpType("real64")
    protected double MinVerticalRecovery = 0;
    /**  Threshold relative to ownship horizontal direction for the computation of horizontal contours (Units: meters)*/
    @LmcpType("real64")
    protected double HorizontalContourThreshold = 0;
    /**  Threshold for the horizontal distance component of Well-Clear Volume (Units: meters)*/
    @LmcpType("real64")
    protected double DTHR = 0;
    /**  Thershold for the vertical distance component of Well-Clear Volume (Units: meters)*/
    @LmcpType("real64")
    protected double ZTHR = 0;
    /**  Threshold for time component of Well-Clear Volume (Units: seconds)*/
    @LmcpType("real64")
    protected double TTHR = 0;
    /**  Number of RTCA alert levels desired for reporting (Units: None)*/
    @LmcpType("uint16")
    protected int RTCAAlertLevels = 0;
    /**  Alert time for preventative alert (Units: seconds)*/
    @LmcpType("real64")
    protected double AlertTime1 = 0;
    /**  Early alert time for the preventativve alert (Units: seconds)*/
    @LmcpType("real64")
    protected double EarlyAlertTime1 = 0;
    /**  Alert time for the corrective alert (Units: seconds)*/
    @LmcpType("real64")
    protected double AlertTime2 = 0;
    /**  Early alert time for the corrective alert (Units: seconds)*/
    @LmcpType("real64")
    protected double EarlyAlertTime2 = 0;
    /**  Alert time for the warning alert (Units: seconds)*/
    @LmcpType("real64")
    protected double AlertTime3 = 0;
    /**  Early alert time for the warning alert (Units: seconds)*/
    @LmcpType("real64")
    protected double EarlyAlertTime3 = 0;
    /**  Hoziontal dectection type (Units: None)*/
    @LmcpType("string")
    protected String HorizontalDetectionType = "";

    
    public DAIDALUSConfiguration() {
    }

    public DAIDALUSConfiguration(long EntityId, double LookAheadTime, double LeftTrack, double RightTrack, double MaxGroundSpeed, double MinGroundSpeed, double MaxVerticalSpeed, double MinVerticalSpeed, double MaxAltitude, double MinAltitude, double TrackStep, double GroundSpeedStep, double VerticalSpeedStep, double AltitudeStep, double HorizontalAcceleration, double VerticalAcceleration, double TurnRate, double BankAngle, double VerticalRate, double RecoveryStabilityTime, boolean isRecoveryTrackBands, boolean isRecoveryGroundSpeedBands, boolean isRecoveryVerticalSpeedBands, boolean isRecoveryAltitudeBands, boolean isCollisionAvoidanceBands, boolean CollisionAvoidanceBandsFactor, double HorizontalNMAC, double MinHorizontalRecovery, double VerticalNMAC, double MinVerticalRecovery, double HorizontalContourThreshold, double DTHR, double ZTHR, double TTHR, int RTCAAlertLevels, double AlertTime1, double EarlyAlertTime1, double AlertTime2, double EarlyAlertTime2, double AlertTime3, double EarlyAlertTime3, String HorizontalDetectionType){
        this.EntityId = EntityId;
        this.LookAheadTime = LookAheadTime;
        this.LeftTrack = LeftTrack;
        this.RightTrack = RightTrack;
        this.MaxGroundSpeed = MaxGroundSpeed;
        this.MinGroundSpeed = MinGroundSpeed;
        this.MaxVerticalSpeed = MaxVerticalSpeed;
        this.MinVerticalSpeed = MinVerticalSpeed;
        this.MaxAltitude = MaxAltitude;
        this.MinAltitude = MinAltitude;
        this.TrackStep = TrackStep;
        this.GroundSpeedStep = GroundSpeedStep;
        this.VerticalSpeedStep = VerticalSpeedStep;
        this.AltitudeStep = AltitudeStep;
        this.HorizontalAcceleration = HorizontalAcceleration;
        this.VerticalAcceleration = VerticalAcceleration;
        this.TurnRate = TurnRate;
        this.BankAngle = BankAngle;
        this.VerticalRate = VerticalRate;
        this.RecoveryStabilityTime = RecoveryStabilityTime;
        this.isRecoveryTrackBands = isRecoveryTrackBands;
        this.isRecoveryGroundSpeedBands = isRecoveryGroundSpeedBands;
        this.isRecoveryVerticalSpeedBands = isRecoveryVerticalSpeedBands;
        this.isRecoveryAltitudeBands = isRecoveryAltitudeBands;
        this.isCollisionAvoidanceBands = isCollisionAvoidanceBands;
        this.CollisionAvoidanceBandsFactor = CollisionAvoidanceBandsFactor;
        this.HorizontalNMAC = HorizontalNMAC;
        this.MinHorizontalRecovery = MinHorizontalRecovery;
        this.VerticalNMAC = VerticalNMAC;
        this.MinVerticalRecovery = MinVerticalRecovery;
        this.HorizontalContourThreshold = HorizontalContourThreshold;
        this.DTHR = DTHR;
        this.ZTHR = ZTHR;
        this.TTHR = TTHR;
        this.RTCAAlertLevels = RTCAAlertLevels;
        this.AlertTime1 = AlertTime1;
        this.EarlyAlertTime1 = EarlyAlertTime1;
        this.AlertTime2 = AlertTime2;
        this.EarlyAlertTime2 = EarlyAlertTime2;
        this.AlertTime3 = AlertTime3;
        this.EarlyAlertTime3 = EarlyAlertTime3;
        this.HorizontalDetectionType = HorizontalDetectionType;
    }


    public DAIDALUSConfiguration clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            DAIDALUSConfiguration newObj = new DAIDALUSConfiguration();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Entity Id that generated this message (Units: None)*/
    public long getEntityId() { return EntityId; }

    /**  Entity Id that generated this message (Units: None)*/
    public DAIDALUSConfiguration setEntityId( long val ) {
        EntityId = val;
        return this;
    }

    /**  Time horizon of all DAIDALUS functions (Units: seconds)*/
    public double getLookAheadTime() { return LookAheadTime; }

    /**  Time horizon of all DAIDALUS functions (Units: seconds)*/
    public DAIDALUSConfiguration setLookAheadTime( double val ) {
        LookAheadTime = val;
        return this;
    }

    /**  Relative maximum horizontal direction maneuver to the left of current ownship direction (Units: degrees)*/
    public double getLeftTrack() { return LeftTrack; }

    /**  Relative maximum horizontal direction maneuver to the left of current ownship direction (Units: degrees)*/
    public DAIDALUSConfiguration setLeftTrack( double val ) {
        LeftTrack = val;
        return this;
    }

    /**  Relative maximum horizontal direction maneuver to the right of current ownship direction (Units: degrees)*/
    public double getRightTrack() { return RightTrack; }

    /**  Relative maximum horizontal direction maneuver to the right of current ownship direction (Units: degrees)*/
    public DAIDALUSConfiguration setRightTrack( double val ) {
        RightTrack = val;
        return this;
    }

    /**  Absolute maximum horizontal speed maneuver (Units: meters per second)*/
    public double getMaxGroundSpeed() { return MaxGroundSpeed; }

    /**  Absolute maximum horizontal speed maneuver (Units: meters per second)*/
    public DAIDALUSConfiguration setMaxGroundSpeed( double val ) {
        MaxGroundSpeed = val;
        return this;
    }

    /**  Absolute minimum horizontal speed maneuver (Units: meters per second)*/
    public double getMinGroundSpeed() { return MinGroundSpeed; }

    /**  Absolute minimum horizontal speed maneuver (Units: meters per second)*/
    public DAIDALUSConfiguration setMinGroundSpeed( double val ) {
        MinGroundSpeed = val;
        return this;
    }

    /**  Absolute maximum vertical speed maneuver (Units: meters per second)*/
    public double getMaxVerticalSpeed() { return MaxVerticalSpeed; }

    /**  Absolute maximum vertical speed maneuver (Units: meters per second)*/
    public DAIDALUSConfiguration setMaxVerticalSpeed( double val ) {
        MaxVerticalSpeed = val;
        return this;
    }

    /**  Absolute minimum verrtical speed maneuver (Units: meters per second)*/
    public double getMinVerticalSpeed() { return MinVerticalSpeed; }

    /**  Absolute minimum verrtical speed maneuver (Units: meters per second)*/
    public DAIDALUSConfiguration setMinVerticalSpeed( double val ) {
        MinVerticalSpeed = val;
        return this;
    }

    /**  Absolute maximum altitude maneuver (Units: meters)*/
    public double getMaxAltitude() { return MaxAltitude; }

    /**  Absolute maximum altitude maneuver (Units: meters)*/
    public DAIDALUSConfiguration setMaxAltitude( double val ) {
        MaxAltitude = val;
        return this;
    }

    /**  Absolute minimum altitude maneuver (Units: meters)*/
    public double getMinAltitude() { return MinAltitude; }

    /**  Absolute minimum altitude maneuver (Units: meters)*/
    public DAIDALUSConfiguration setMinAltitude( double val ) {
        MinAltitude = val;
        return this;
    }

    /**  Granularity of horizontal direction maneuvers (Units: degrees)*/
    public double getTrackStep() { return TrackStep; }

    /**  Granularity of horizontal direction maneuvers (Units: degrees)*/
    public DAIDALUSConfiguration setTrackStep( double val ) {
        TrackStep = val;
        return this;
    }

    /**  Granularity of horizontal speed maneuvers (Units: meters per second)*/
    public double getGroundSpeedStep() { return GroundSpeedStep; }

    /**  Granularity of horizontal speed maneuvers (Units: meters per second)*/
    public DAIDALUSConfiguration setGroundSpeedStep( double val ) {
        GroundSpeedStep = val;
        return this;
    }

    /**  Granularity of vertical speed maneuvers (Units: meters per second)*/
    public double getVerticalSpeedStep() { return VerticalSpeedStep; }

    /**  Granularity of vertical speed maneuvers (Units: meters per second)*/
    public DAIDALUSConfiguration setVerticalSpeedStep( double val ) {
        VerticalSpeedStep = val;
        return this;
    }

    /**  Granularity of altitude maneuvers (Units: meters)*/
    public double getAltitudeStep() { return AltitudeStep; }

    /**  Granularity of altitude maneuvers (Units: meters)*/
    public DAIDALUSConfiguration setAltitudeStep( double val ) {
        AltitudeStep = val;
        return this;
    }

    /**  Horizontal acceleration used in the computation of horizontal speed maneuvers (Units: meters per second per second)*/
    public double getHorizontalAcceleration() { return HorizontalAcceleration; }

    /**  Horizontal acceleration used in the computation of horizontal speed maneuvers (Units: meters per second per second)*/
    public DAIDALUSConfiguration setHorizontalAcceleration( double val ) {
        HorizontalAcceleration = val;
        return this;
    }

    /**  Vertical accelereation used in the computation of vertical speed maneuvers (Units: G)*/
    public double getVerticalAcceleration() { return VerticalAcceleration; }

    /**  Vertical accelereation used in the computation of vertical speed maneuvers (Units: G)*/
    public DAIDALUSConfiguration setVerticalAcceleration( double val ) {
        VerticalAcceleration = val;
        return this;
    }

    /**  Turn rate used in the computation of horizontal direction maneuvers (Units: degrees per second)*/
    public double getTurnRate() { return TurnRate; }

    /**  Turn rate used in the computation of horizontal direction maneuvers (Units: degrees per second)*/
    public DAIDALUSConfiguration setTurnRate( double val ) {
        TurnRate = val;
        return this;
    }

    /**  Bank angle used in the computation of horizontal direction maneuvers (Units: degrees)*/
    public double getBankAngle() { return BankAngle; }

    /**  Bank angle used in the computation of horizontal direction maneuvers (Units: degrees)*/
    public DAIDALUSConfiguration setBankAngle( double val ) {
        BankAngle = val;
        return this;
    }

    /**  Vertical rate used in the computation of altitude maneuvers (Units: meters per second)*/
    public double getVerticalRate() { return VerticalRate; }

    /**  Vertical rate used in the computation of altitude maneuvers (Units: meters per second)*/
    public DAIDALUSConfiguration setVerticalRate( double val ) {
        VerticalRate = val;
        return this;
    }

    /**  Time delay to stabilize recovery maneuvers (Units: seconds)*/
    public double getRecoveryStabilityTime() { return RecoveryStabilityTime; }

    /**  Time delay to stabilize recovery maneuvers (Units: seconds)*/
    public DAIDALUSConfiguration setRecoveryStabilityTime( double val ) {
        RecoveryStabilityTime = val;
        return this;
    }

    /**  Enable computation of horizontal direction recovery maneuvers (Units: None)*/
    public boolean getIsRecoveryTrackBands() { return isRecoveryTrackBands; }

    /**  Enable computation of horizontal direction recovery maneuvers (Units: None)*/
    public DAIDALUSConfiguration setIsRecoveryTrackBands( boolean val ) {
        isRecoveryTrackBands = val;
        return this;
    }

    /**  Enable computation of horzontal speed recovery maneuvers (Units: None)*/
    public boolean getIsRecoveryGroundSpeedBands() { return isRecoveryGroundSpeedBands; }

    /**  Enable computation of horzontal speed recovery maneuvers (Units: None)*/
    public DAIDALUSConfiguration setIsRecoveryGroundSpeedBands( boolean val ) {
        isRecoveryGroundSpeedBands = val;
        return this;
    }

    /**  Enable computation of vertical speed recovery maneuvers (Units: None)*/
    public boolean getIsRecoveryVerticalSpeedBands() { return isRecoveryVerticalSpeedBands; }

    /**  Enable computation of vertical speed recovery maneuvers (Units: None)*/
    public DAIDALUSConfiguration setIsRecoveryVerticalSpeedBands( boolean val ) {
        isRecoveryVerticalSpeedBands = val;
        return this;
    }

    /**  Enable computation of altitude recovery maneuvers (Units: None)*/
    public boolean getIsRecoveryAltitudeBands() { return isRecoveryAltitudeBands; }

    /**  Enable computation of altitude recovery maneuvers (Units: None)*/
    public DAIDALUSConfiguration setIsRecoveryAltitudeBands( boolean val ) {
        isRecoveryAltitudeBands = val;
        return this;
    }

    /**  Enable computation of collision avoidance maneuvers (Units: None)*/
    public boolean getIsCollisionAvoidanceBands() { return isCollisionAvoidanceBands; }

    /**  Enable computation of collision avoidance maneuvers (Units: None)*/
    public DAIDALUSConfiguration setIsCollisionAvoidanceBands( boolean val ) {
        isCollisionAvoidanceBands = val;
        return this;
    }

    /**  Factor to reduce minimum horizontal/vertial recovery separation when computing avoidance maneuvers (Units: None)*/
    public boolean getCollisionAvoidanceBandsFactor() { return CollisionAvoidanceBandsFactor; }

    /**  Factor to reduce minimum horizontal/vertial recovery separation when computing avoidance maneuvers (Units: None)*/
    public DAIDALUSConfiguration setCollisionAvoidanceBandsFactor( boolean val ) {
        CollisionAvoidanceBandsFactor = val;
        return this;
    }

    /**  Horizontal NMAC (Units: meters)*/
    public double getHorizontalNMAC() { return HorizontalNMAC; }

    /**  Horizontal NMAC (Units: meters)*/
    public DAIDALUSConfiguration setHorizontalNMAC( double val ) {
        HorizontalNMAC = val;
        return this;
    }

    /**  Minimum horizontal separation used in the computation of recovery maneuvers (Units: meters)*/
    public double getMinHorizontalRecovery() { return MinHorizontalRecovery; }

    /**  Minimum horizontal separation used in the computation of recovery maneuvers (Units: meters)*/
    public DAIDALUSConfiguration setMinHorizontalRecovery( double val ) {
        MinHorizontalRecovery = val;
        return this;
    }

    /**  Vertical NMAC (Units: meters)*/
    public double getVerticalNMAC() { return VerticalNMAC; }

    /**  Vertical NMAC (Units: meters)*/
    public DAIDALUSConfiguration setVerticalNMAC( double val ) {
        VerticalNMAC = val;
        return this;
    }

    /**  Minimum vertical separation used in the computation of recovery maneuvers(Units: meters)*/
    public double getMinVerticalRecovery() { return MinVerticalRecovery; }

    /**  Minimum vertical separation used in the computation of recovery maneuvers(Units: meters)*/
    public DAIDALUSConfiguration setMinVerticalRecovery( double val ) {
        MinVerticalRecovery = val;
        return this;
    }

    /**  Threshold relative to ownship horizontal direction for the computation of horizontal contours (Units: meters)*/
    public double getHorizontalContourThreshold() { return HorizontalContourThreshold; }

    /**  Threshold relative to ownship horizontal direction for the computation of horizontal contours (Units: meters)*/
    public DAIDALUSConfiguration setHorizontalContourThreshold( double val ) {
        HorizontalContourThreshold = val;
        return this;
    }

    /**  Threshold for the horizontal distance component of Well-Clear Volume (Units: meters)*/
    public double getDTHR() { return DTHR; }

    /**  Threshold for the horizontal distance component of Well-Clear Volume (Units: meters)*/
    public DAIDALUSConfiguration setDTHR( double val ) {
        DTHR = val;
        return this;
    }

    /**  Thershold for the vertical distance component of Well-Clear Volume (Units: meters)*/
    public double getZTHR() { return ZTHR; }

    /**  Thershold for the vertical distance component of Well-Clear Volume (Units: meters)*/
    public DAIDALUSConfiguration setZTHR( double val ) {
        ZTHR = val;
        return this;
    }

    /**  Threshold for time component of Well-Clear Volume (Units: seconds)*/
    public double getTTHR() { return TTHR; }

    /**  Threshold for time component of Well-Clear Volume (Units: seconds)*/
    public DAIDALUSConfiguration setTTHR( double val ) {
        TTHR = val;
        return this;
    }

    /**  Number of RTCA alert levels desired for reporting (Units: None)*/
    public int getRTCAAlertLevels() { return RTCAAlertLevels; }

    /**  Number of RTCA alert levels desired for reporting (Units: None)*/
    public DAIDALUSConfiguration setRTCAAlertLevels( int val ) {
        RTCAAlertLevels = val;
        return this;
    }

    /**  Alert time for preventative alert (Units: seconds)*/
    public double getAlertTime1() { return AlertTime1; }

    /**  Alert time for preventative alert (Units: seconds)*/
    public DAIDALUSConfiguration setAlertTime1( double val ) {
        AlertTime1 = val;
        return this;
    }

    /**  Early alert time for the preventativve alert (Units: seconds)*/
    public double getEarlyAlertTime1() { return EarlyAlertTime1; }

    /**  Early alert time for the preventativve alert (Units: seconds)*/
    public DAIDALUSConfiguration setEarlyAlertTime1( double val ) {
        EarlyAlertTime1 = val;
        return this;
    }

    /**  Alert time for the corrective alert (Units: seconds)*/
    public double getAlertTime2() { return AlertTime2; }

    /**  Alert time for the corrective alert (Units: seconds)*/
    public DAIDALUSConfiguration setAlertTime2( double val ) {
        AlertTime2 = val;
        return this;
    }

    /**  Early alert time for the corrective alert (Units: seconds)*/
    public double getEarlyAlertTime2() { return EarlyAlertTime2; }

    /**  Early alert time for the corrective alert (Units: seconds)*/
    public DAIDALUSConfiguration setEarlyAlertTime2( double val ) {
        EarlyAlertTime2 = val;
        return this;
    }

    /**  Alert time for the warning alert (Units: seconds)*/
    public double getAlertTime3() { return AlertTime3; }

    /**  Alert time for the warning alert (Units: seconds)*/
    public DAIDALUSConfiguration setAlertTime3( double val ) {
        AlertTime3 = val;
        return this;
    }

    /**  Early alert time for the warning alert (Units: seconds)*/
    public double getEarlyAlertTime3() { return EarlyAlertTime3; }

    /**  Early alert time for the warning alert (Units: seconds)*/
    public DAIDALUSConfiguration setEarlyAlertTime3( double val ) {
        EarlyAlertTime3 = val;
        return this;
    }

    /**  Hoziontal dectection type (Units: None)*/
    public String getHorizontalDetectionType() { return HorizontalDetectionType; }

    /**  Hoziontal dectection type (Units: None)*/
    public DAIDALUSConfiguration setHorizontalDetectionType( String val ) {
        HorizontalDetectionType = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 276; // accounts for primitive types
        size += LMCPUtil.sizeOfString(HorizontalDetectionType);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        EntityId = LMCPUtil.getUint32(in);

        LookAheadTime = LMCPUtil.getReal64(in);

        LeftTrack = LMCPUtil.getReal64(in);

        RightTrack = LMCPUtil.getReal64(in);

        MaxGroundSpeed = LMCPUtil.getReal64(in);

        MinGroundSpeed = LMCPUtil.getReal64(in);

        MaxVerticalSpeed = LMCPUtil.getReal64(in);

        MinVerticalSpeed = LMCPUtil.getReal64(in);

        MaxAltitude = LMCPUtil.getReal64(in);

        MinAltitude = LMCPUtil.getReal64(in);

        TrackStep = LMCPUtil.getReal64(in);

        GroundSpeedStep = LMCPUtil.getReal64(in);

        VerticalSpeedStep = LMCPUtil.getReal64(in);

        AltitudeStep = LMCPUtil.getReal64(in);

        HorizontalAcceleration = LMCPUtil.getReal64(in);

        VerticalAcceleration = LMCPUtil.getReal64(in);

        TurnRate = LMCPUtil.getReal64(in);

        BankAngle = LMCPUtil.getReal64(in);

        VerticalRate = LMCPUtil.getReal64(in);

        RecoveryStabilityTime = LMCPUtil.getReal64(in);

        isRecoveryTrackBands = LMCPUtil.getBool(in);

        isRecoveryGroundSpeedBands = LMCPUtil.getBool(in);

        isRecoveryVerticalSpeedBands = LMCPUtil.getBool(in);

        isRecoveryAltitudeBands = LMCPUtil.getBool(in);

        isCollisionAvoidanceBands = LMCPUtil.getBool(in);

        CollisionAvoidanceBandsFactor = LMCPUtil.getBool(in);

        HorizontalNMAC = LMCPUtil.getReal64(in);

        MinHorizontalRecovery = LMCPUtil.getReal64(in);

        VerticalNMAC = LMCPUtil.getReal64(in);

        MinVerticalRecovery = LMCPUtil.getReal64(in);

        HorizontalContourThreshold = LMCPUtil.getReal64(in);

        DTHR = LMCPUtil.getReal64(in);

        ZTHR = LMCPUtil.getReal64(in);

        TTHR = LMCPUtil.getReal64(in);

        RTCAAlertLevels = LMCPUtil.getUint16(in);

        AlertTime1 = LMCPUtil.getReal64(in);

        EarlyAlertTime1 = LMCPUtil.getReal64(in);

        AlertTime2 = LMCPUtil.getReal64(in);

        EarlyAlertTime2 = LMCPUtil.getReal64(in);

        AlertTime3 = LMCPUtil.getReal64(in);

        EarlyAlertTime3 = LMCPUtil.getReal64(in);

        HorizontalDetectionType = LMCPUtil.getString(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putUint32(out, EntityId);
        LMCPUtil.putReal64(out, LookAheadTime);
        LMCPUtil.putReal64(out, LeftTrack);
        LMCPUtil.putReal64(out, RightTrack);
        LMCPUtil.putReal64(out, MaxGroundSpeed);
        LMCPUtil.putReal64(out, MinGroundSpeed);
        LMCPUtil.putReal64(out, MaxVerticalSpeed);
        LMCPUtil.putReal64(out, MinVerticalSpeed);
        LMCPUtil.putReal64(out, MaxAltitude);
        LMCPUtil.putReal64(out, MinAltitude);
        LMCPUtil.putReal64(out, TrackStep);
        LMCPUtil.putReal64(out, GroundSpeedStep);
        LMCPUtil.putReal64(out, VerticalSpeedStep);
        LMCPUtil.putReal64(out, AltitudeStep);
        LMCPUtil.putReal64(out, HorizontalAcceleration);
        LMCPUtil.putReal64(out, VerticalAcceleration);
        LMCPUtil.putReal64(out, TurnRate);
        LMCPUtil.putReal64(out, BankAngle);
        LMCPUtil.putReal64(out, VerticalRate);
        LMCPUtil.putReal64(out, RecoveryStabilityTime);
        LMCPUtil.putBool(out, isRecoveryTrackBands);
        LMCPUtil.putBool(out, isRecoveryGroundSpeedBands);
        LMCPUtil.putBool(out, isRecoveryVerticalSpeedBands);
        LMCPUtil.putBool(out, isRecoveryAltitudeBands);
        LMCPUtil.putBool(out, isCollisionAvoidanceBands);
        LMCPUtil.putBool(out, CollisionAvoidanceBandsFactor);
        LMCPUtil.putReal64(out, HorizontalNMAC);
        LMCPUtil.putReal64(out, MinHorizontalRecovery);
        LMCPUtil.putReal64(out, VerticalNMAC);
        LMCPUtil.putReal64(out, MinVerticalRecovery);
        LMCPUtil.putReal64(out, HorizontalContourThreshold);
        LMCPUtil.putReal64(out, DTHR);
        LMCPUtil.putReal64(out, ZTHR);
        LMCPUtil.putReal64(out, TTHR);
        LMCPUtil.putUint16(out, RTCAAlertLevels);
        LMCPUtil.putReal64(out, AlertTime1);
        LMCPUtil.putReal64(out, EarlyAlertTime1);
        LMCPUtil.putReal64(out, AlertTime2);
        LMCPUtil.putReal64(out, EarlyAlertTime2);
        LMCPUtil.putReal64(out, AlertTime3);
        LMCPUtil.putReal64(out, EarlyAlertTime3);
        LMCPUtil.putString(out, HorizontalDetectionType);

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
        buf.append( ws + "<DAIDALUSConfiguration Series=\"DAIDALUS\">\n");
        buf.append( ws + "  <EntityId>" + String.valueOf(EntityId) + "</EntityId>\n");
        buf.append( ws + "  <LookAheadTime>" + String.valueOf(LookAheadTime) + "</LookAheadTime>\n");
        buf.append( ws + "  <LeftTrack>" + String.valueOf(LeftTrack) + "</LeftTrack>\n");
        buf.append( ws + "  <RightTrack>" + String.valueOf(RightTrack) + "</RightTrack>\n");
        buf.append( ws + "  <MaxGroundSpeed>" + String.valueOf(MaxGroundSpeed) + "</MaxGroundSpeed>\n");
        buf.append( ws + "  <MinGroundSpeed>" + String.valueOf(MinGroundSpeed) + "</MinGroundSpeed>\n");
        buf.append( ws + "  <MaxVerticalSpeed>" + String.valueOf(MaxVerticalSpeed) + "</MaxVerticalSpeed>\n");
        buf.append( ws + "  <MinVerticalSpeed>" + String.valueOf(MinVerticalSpeed) + "</MinVerticalSpeed>\n");
        buf.append( ws + "  <MaxAltitude>" + String.valueOf(MaxAltitude) + "</MaxAltitude>\n");
        buf.append( ws + "  <MinAltitude>" + String.valueOf(MinAltitude) + "</MinAltitude>\n");
        buf.append( ws + "  <TrackStep>" + String.valueOf(TrackStep) + "</TrackStep>\n");
        buf.append( ws + "  <GroundSpeedStep>" + String.valueOf(GroundSpeedStep) + "</GroundSpeedStep>\n");
        buf.append( ws + "  <VerticalSpeedStep>" + String.valueOf(VerticalSpeedStep) + "</VerticalSpeedStep>\n");
        buf.append( ws + "  <AltitudeStep>" + String.valueOf(AltitudeStep) + "</AltitudeStep>\n");
        buf.append( ws + "  <HorizontalAcceleration>" + String.valueOf(HorizontalAcceleration) + "</HorizontalAcceleration>\n");
        buf.append( ws + "  <VerticalAcceleration>" + String.valueOf(VerticalAcceleration) + "</VerticalAcceleration>\n");
        buf.append( ws + "  <TurnRate>" + String.valueOf(TurnRate) + "</TurnRate>\n");
        buf.append( ws + "  <BankAngle>" + String.valueOf(BankAngle) + "</BankAngle>\n");
        buf.append( ws + "  <VerticalRate>" + String.valueOf(VerticalRate) + "</VerticalRate>\n");
        buf.append( ws + "  <RecoveryStabilityTime>" + String.valueOf(RecoveryStabilityTime) + "</RecoveryStabilityTime>\n");
        buf.append( ws + "  <isRecoveryTrackBands>" + String.valueOf(isRecoveryTrackBands) + "</isRecoveryTrackBands>\n");
        buf.append( ws + "  <isRecoveryGroundSpeedBands>" + String.valueOf(isRecoveryGroundSpeedBands) + "</isRecoveryGroundSpeedBands>\n");
        buf.append( ws + "  <isRecoveryVerticalSpeedBands>" + String.valueOf(isRecoveryVerticalSpeedBands) + "</isRecoveryVerticalSpeedBands>\n");
        buf.append( ws + "  <isRecoveryAltitudeBands>" + String.valueOf(isRecoveryAltitudeBands) + "</isRecoveryAltitudeBands>\n");
        buf.append( ws + "  <isCollisionAvoidanceBands>" + String.valueOf(isCollisionAvoidanceBands) + "</isCollisionAvoidanceBands>\n");
        buf.append( ws + "  <CollisionAvoidanceBandsFactor>" + String.valueOf(CollisionAvoidanceBandsFactor) + "</CollisionAvoidanceBandsFactor>\n");
        buf.append( ws + "  <HorizontalNMAC>" + String.valueOf(HorizontalNMAC) + "</HorizontalNMAC>\n");
        buf.append( ws + "  <MinHorizontalRecovery>" + String.valueOf(MinHorizontalRecovery) + "</MinHorizontalRecovery>\n");
        buf.append( ws + "  <VerticalNMAC>" + String.valueOf(VerticalNMAC) + "</VerticalNMAC>\n");
        buf.append( ws + "  <MinVerticalRecovery>" + String.valueOf(MinVerticalRecovery) + "</MinVerticalRecovery>\n");
        buf.append( ws + "  <HorizontalContourThreshold>" + String.valueOf(HorizontalContourThreshold) + "</HorizontalContourThreshold>\n");
        buf.append( ws + "  <DTHR>" + String.valueOf(DTHR) + "</DTHR>\n");
        buf.append( ws + "  <ZTHR>" + String.valueOf(ZTHR) + "</ZTHR>\n");
        buf.append( ws + "  <TTHR>" + String.valueOf(TTHR) + "</TTHR>\n");
        buf.append( ws + "  <RTCAAlertLevels>" + String.valueOf(RTCAAlertLevels) + "</RTCAAlertLevels>\n");
        buf.append( ws + "  <AlertTime1>" + String.valueOf(AlertTime1) + "</AlertTime1>\n");
        buf.append( ws + "  <EarlyAlertTime1>" + String.valueOf(EarlyAlertTime1) + "</EarlyAlertTime1>\n");
        buf.append( ws + "  <AlertTime2>" + String.valueOf(AlertTime2) + "</AlertTime2>\n");
        buf.append( ws + "  <EarlyAlertTime2>" + String.valueOf(EarlyAlertTime2) + "</EarlyAlertTime2>\n");
        buf.append( ws + "  <AlertTime3>" + String.valueOf(AlertTime3) + "</AlertTime3>\n");
        buf.append( ws + "  <EarlyAlertTime3>" + String.valueOf(EarlyAlertTime3) + "</EarlyAlertTime3>\n");
        buf.append( ws + "  <HorizontalDetectionType>" + String.valueOf(HorizontalDetectionType) + "</HorizontalDetectionType>\n");
        buf.append( ws + "</DAIDALUSConfiguration>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        DAIDALUSConfiguration o = (DAIDALUSConfiguration) anotherObj;
        if (EntityId != o.EntityId) return false;
        if (LookAheadTime != o.LookAheadTime) return false;
        if (LeftTrack != o.LeftTrack) return false;
        if (RightTrack != o.RightTrack) return false;
        if (MaxGroundSpeed != o.MaxGroundSpeed) return false;
        if (MinGroundSpeed != o.MinGroundSpeed) return false;
        if (MaxVerticalSpeed != o.MaxVerticalSpeed) return false;
        if (MinVerticalSpeed != o.MinVerticalSpeed) return false;
        if (MaxAltitude != o.MaxAltitude) return false;
        if (MinAltitude != o.MinAltitude) return false;
        if (TrackStep != o.TrackStep) return false;
        if (GroundSpeedStep != o.GroundSpeedStep) return false;
        if (VerticalSpeedStep != o.VerticalSpeedStep) return false;
        if (AltitudeStep != o.AltitudeStep) return false;
        if (HorizontalAcceleration != o.HorizontalAcceleration) return false;
        if (VerticalAcceleration != o.VerticalAcceleration) return false;
        if (TurnRate != o.TurnRate) return false;
        if (BankAngle != o.BankAngle) return false;
        if (VerticalRate != o.VerticalRate) return false;
        if (RecoveryStabilityTime != o.RecoveryStabilityTime) return false;
        if (isRecoveryTrackBands != o.isRecoveryTrackBands) return false;
        if (isRecoveryGroundSpeedBands != o.isRecoveryGroundSpeedBands) return false;
        if (isRecoveryVerticalSpeedBands != o.isRecoveryVerticalSpeedBands) return false;
        if (isRecoveryAltitudeBands != o.isRecoveryAltitudeBands) return false;
        if (isCollisionAvoidanceBands != o.isCollisionAvoidanceBands) return false;
        if (CollisionAvoidanceBandsFactor != o.CollisionAvoidanceBandsFactor) return false;
        if (HorizontalNMAC != o.HorizontalNMAC) return false;
        if (MinHorizontalRecovery != o.MinHorizontalRecovery) return false;
        if (VerticalNMAC != o.VerticalNMAC) return false;
        if (MinVerticalRecovery != o.MinVerticalRecovery) return false;
        if (HorizontalContourThreshold != o.HorizontalContourThreshold) return false;
        if (DTHR != o.DTHR) return false;
        if (ZTHR != o.ZTHR) return false;
        if (TTHR != o.TTHR) return false;
        if (RTCAAlertLevels != o.RTCAAlertLevels) return false;
        if (AlertTime1 != o.AlertTime1) return false;
        if (EarlyAlertTime1 != o.EarlyAlertTime1) return false;
        if (AlertTime2 != o.AlertTime2) return false;
        if (EarlyAlertTime2 != o.EarlyAlertTime2) return false;
        if (AlertTime3 != o.AlertTime3) return false;
        if (EarlyAlertTime3 != o.EarlyAlertTime3) return false;
        if (HorizontalDetectionType == null && o.HorizontalDetectionType != null) return false;
        if ( HorizontalDetectionType!= null && !HorizontalDetectionType.equals(o.HorizontalDetectionType)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)EntityId;
        hash += 31 * (int)LookAheadTime;
        hash += 31 * (int)LeftTrack;
        hash += 31 * (int)RightTrack;
        hash += 31 * (int)MaxGroundSpeed;

        return hash + super.hashCode();
    }
    
}
