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
 Message to enable onboard processor to send commands to the autopilot, and the gimbal 
*/
public class AutopilotKeepAlive extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 12;

    public static final String SERIES_NAME = "UXNATIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149751333668345413L;
    public static final int SERIES_VERSION = 9;


    private static final String TYPE_NAME = "AutopilotKeepAlive";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.uxnative.AutopilotKeepAlive";

    /**  Enable communications between UxAS and the autopilot (Units: None)*/
    @LmcpType("bool")
    protected boolean AutopilotEnabled = true;
    /**  Allow UxAS to send speed commands to the autopilot (Units: None)*/
    @LmcpType("bool")
    protected boolean SpeedAuthorized = true;
    /**  Enable communications between UxAS and the Gimbal. Note: this does not effect the video stream. (Units: None)*/
    @LmcpType("bool")
    protected boolean GimbalEnabled = true;
    /**  Time that this message was sent (Units: milliseconds since 1 Jan 1970)*/
    @LmcpType("int64")
    protected long TimeSent = 0L;
    /**  Overrides speed to this value if greater than zero (Units: meters/sec)*/
    @LmcpType("real32")
    protected float SpeedOverride = (float)-1.0;
    /**  Overrides altitude to this value (MSL) if greater than zero (Units: meters MSL)*/
    @LmcpType("real32")
    protected float AltOverride = (float)-1.0;

    
    public AutopilotKeepAlive() {
    }

    public AutopilotKeepAlive(boolean AutopilotEnabled, boolean SpeedAuthorized, boolean GimbalEnabled, long TimeSent, float SpeedOverride, float AltOverride){
        this.AutopilotEnabled = AutopilotEnabled;
        this.SpeedAuthorized = SpeedAuthorized;
        this.GimbalEnabled = GimbalEnabled;
        this.TimeSent = TimeSent;
        this.SpeedOverride = SpeedOverride;
        this.AltOverride = AltOverride;
    }


    public AutopilotKeepAlive clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            AutopilotKeepAlive newObj = new AutopilotKeepAlive();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Enable communications between UxAS and the autopilot (Units: None)*/
    public boolean getAutopilotEnabled() { return AutopilotEnabled; }

    /**  Enable communications between UxAS and the autopilot (Units: None)*/
    public AutopilotKeepAlive setAutopilotEnabled( boolean val ) {
        AutopilotEnabled = val;
        return this;
    }

    /**  Allow UxAS to send speed commands to the autopilot (Units: None)*/
    public boolean getSpeedAuthorized() { return SpeedAuthorized; }

    /**  Allow UxAS to send speed commands to the autopilot (Units: None)*/
    public AutopilotKeepAlive setSpeedAuthorized( boolean val ) {
        SpeedAuthorized = val;
        return this;
    }

    /**  Enable communications between UxAS and the Gimbal. Note: this does not effect the video stream. (Units: None)*/
    public boolean getGimbalEnabled() { return GimbalEnabled; }

    /**  Enable communications between UxAS and the Gimbal. Note: this does not effect the video stream. (Units: None)*/
    public AutopilotKeepAlive setGimbalEnabled( boolean val ) {
        GimbalEnabled = val;
        return this;
    }

    /**  Time that this message was sent (Units: milliseconds since 1 Jan 1970)*/
    public long getTimeSent() { return TimeSent; }

    /**  Time that this message was sent (Units: milliseconds since 1 Jan 1970)*/
    public AutopilotKeepAlive setTimeSent( long val ) {
        TimeSent = val;
        return this;
    }

    /**  Overrides speed to this value if greater than zero (Units: meters/sec)*/
    public float getSpeedOverride() { return SpeedOverride; }

    /**  Overrides speed to this value if greater than zero (Units: meters/sec)*/
    public AutopilotKeepAlive setSpeedOverride( float val ) {
        SpeedOverride = val;
        return this;
    }

    /**  Overrides altitude to this value (MSL) if greater than zero (Units: meters MSL)*/
    public float getAltOverride() { return AltOverride; }

    /**  Overrides altitude to this value (MSL) if greater than zero (Units: meters MSL)*/
    public AutopilotKeepAlive setAltOverride( float val ) {
        AltOverride = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 19; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        AutopilotEnabled = LMCPUtil.getBool(in);

        SpeedAuthorized = LMCPUtil.getBool(in);

        GimbalEnabled = LMCPUtil.getBool(in);

        TimeSent = LMCPUtil.getInt64(in);

        SpeedOverride = LMCPUtil.getReal32(in);

        AltOverride = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putBool(out, AutopilotEnabled);
        LMCPUtil.putBool(out, SpeedAuthorized);
        LMCPUtil.putBool(out, GimbalEnabled);
        LMCPUtil.putInt64(out, TimeSent);
        LMCPUtil.putReal32(out, SpeedOverride);
        LMCPUtil.putReal32(out, AltOverride);

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
        buf.append( ws + "<AutopilotKeepAlive Series=\"UXNATIVE\">\n");
        buf.append( ws + "  <AutopilotEnabled>" + String.valueOf(AutopilotEnabled) + "</AutopilotEnabled>\n");
        buf.append( ws + "  <SpeedAuthorized>" + String.valueOf(SpeedAuthorized) + "</SpeedAuthorized>\n");
        buf.append( ws + "  <GimbalEnabled>" + String.valueOf(GimbalEnabled) + "</GimbalEnabled>\n");
        buf.append( ws + "  <TimeSent>" + String.valueOf(TimeSent) + "</TimeSent>\n");
        buf.append( ws + "  <SpeedOverride>" + String.valueOf(SpeedOverride) + "</SpeedOverride>\n");
        buf.append( ws + "  <AltOverride>" + String.valueOf(AltOverride) + "</AltOverride>\n");
        buf.append( ws + "</AutopilotKeepAlive>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        AutopilotKeepAlive o = (AutopilotKeepAlive) anotherObj;
        if (AutopilotEnabled != o.AutopilotEnabled) return false;
        if (SpeedAuthorized != o.SpeedAuthorized) return false;
        if (GimbalEnabled != o.GimbalEnabled) return false;
        if (TimeSent != o.TimeSent) return false;
        if (SpeedOverride != o.SpeedOverride) return false;
        if (AltOverride != o.AltOverride) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)SpeedOverride;

        return hash + super.hashCode();
    }
    
}
