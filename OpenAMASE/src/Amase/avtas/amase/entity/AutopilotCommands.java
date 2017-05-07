// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.entity;

import afrl.cmasi.NavigationMode;
import afrl.cmasi.SpeedType;
import afrl.cmasi.Waypoint;
import avtas.data.Property;
import avtas.data.PropertyMap;
import java.util.List;

/**
 * Describes the state of an autopilot.
 *
 * @author AFRL/RQQD
 */
public class AutopilotCommands {

    private final PropertyMap map;

    public AutopilotCommands(PropertyMap map) {
        this.map = map;

        cmdVertSpeed = map.get("aircraft/autopilot/cmd_vert_speed");

        cmdSpeed = map.get("aircraft/autopilot/cmd_speed");

        cmdAlt = map.get("aircraft/autopilot/cmd_alt");

        cmdHdg = map.get("aircraft/autopilot/cmd_hdg");
        
        cmdBank = map.get("aircraft/autopilot/cmd_bank");
        
        bankrate_cmd = map.get("aircraft/autopilot/cmd_bank_rate");
        
        vert_accel_cmd = map.get("aircraft/autopilot/vert_accel_cmd");

        maxBank = map.get("aircraft/autopilot/max_bank", 0.5*Math.PI);

        maxBankRate = map.get("aircraft/autopilot/max_bank_rate", Math.PI);

        maxVsUp = map.get("aircraft/autopilot/max_vs_up");

        maxPitchUp = map.get("aircraft/autopilot/max_pitch_up");

        maxVsDown = map.get("aircraft/autopilot/max_vs_down");

        maxPitchDown = map.get("aircraft/autopilot/max_pitch_down");
        
        maxSpeed = map.get("aircraft/autopilot/max_speed");
        
        minSpeed = map.get("aircraft/autopilot/min_speed");

        waypointList = map.get("aircraft/autopilot/route_following/waypoint_list", null);

        firstWaypoint = map.get("aircraft/autopilot/route_following/first_waypoint", -1);

        currentWaypoint = map.get("aircraft/autopilot/route_following/current_waypoint", -1);

        waypointReached = map.get("aircraft/autopilot/route_following/waypoint_reached", -1);


        lateralCmdType = map.put("aircraft/autopilot/lateral_command_type", LateralCommandType.Heading);
        verticalCmdType = map.put("aircraft/autopilot/vertical_command_type", VerticalCommandType.AltitudeMSL);
        speedCmdType = map.put("aircraft/autopilot/speed_command_type", SpeedType.Airspeed);
        navMode = map.put("aircraft/autopilot/navigation_mode", NavigationMode.FlightDirector);
        
        autopilotOn = map.get("aircraft/autopilot/autopilot_on", true);

    }

    /**
     * Describes modes for lateral commands
     */
    public static enum LateralCommandType {

        Heading,
        BankAngle,
        BankRate
    }

    /**
     * Describes different modes for vertical commands
     */
    public static enum VerticalCommandType {

        /**
         * constant height above the terrain
         */
        AltitudeAboveGround,
        /**
         * constant height above the mean sea level
         */
        AltitudeMSL,
        /**
         * speed in the inertial plan vertical axis
         */
        VerticalSpeed,
        /**
         * Acceleration in the inertial vertical axis (Nz)
         */
        VerticalAccel
    }
    
    /** Indicates the state of autopilot control. (on = true) */
    public final Property<Boolean> autopilotOn;

    /**
     * Current lateral command type
     */
    public final Property<LateralCommandType> lateralCmdType;
    /**
     * Current vertical command type
     */
    public final Property<VerticalCommandType> verticalCmdType;
    /**
     * Current speed command type
     */
    public final Property<SpeedType> speedCmdType;;
    /**
     * Current navigation mode
     */
    public Property<NavigationMode> navMode;
    /**
     * commanded speed in meters per second
     */
    public final Property cmdSpeed;
    /**
     * commanded heading in radians.
     */
    public final Property cmdHdg;
    /**
     * commanded bank angle in radians (right-hand convention)
     */
    public final Property cmdBank;
    /**
     * commanded bank rate in radians/second
     */
    public final Property bankrate_cmd;
    /**
     * commanded altitude above mean sea level in meters
     */
    public final Property cmdAlt;
    /**
     * commanded vertical speed in meters per second
     */
    public final Property cmdVertSpeed;
    /**
     * commanded vertical acceleration in meters per second squared
     */
    public final Property vert_accel_cmd;
    /**
     * Maximum allowed airspeed in meters/sec
     */
    public final Property maxSpeed;
    /**
     * Minimum allowed airspeed in meters/sec
     */
    public final Property minSpeed;
    /**
     * max bank angle (rad)
     */
    public final Property maxBank;
    /**
     * max bank angle rate (rad/sec)
     */
    public final Property maxBankRate;
    /**
     * max upwards vertical speed (mps)
     */
    public final Property maxVsUp;
    /**
     * max up pitch angle (rad)
     */
    public final Property maxPitchUp;
    /**
     * max descent vertical speed (mps)
     */
    public final Property maxVsDown;
    /**
     * max down pitch angle (rad)
     */
    public final Property maxPitchDown;
    /**
     * the list of waypoints for the vehicle to follow
     */
    public final Property<List<Waypoint>> waypointList;
    /**
     * first waypoint in the waypoint list
     */
    public final Property firstWaypoint;
    /**
     * current waypoint to follow
     */
    public final Property currentWaypoint;
    /**
     * the last waypoint that was reached by the aircraft
     */
    public final Property waypointReached;
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */