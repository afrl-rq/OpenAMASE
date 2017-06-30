// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.entity.modules;

import afrl.cmasi.*;
import avtas.amase.entity.EntityModule;
import avtas.amase.util.CmasiUtils;
import avtas.data.Unit;
import avtas.util.NavUtils;
import static java.lang.Math.*;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.util.Random;

/**
 * Implements a set of simple waypoint following routines. The Waypoint Follower
 * uses information in a {@link afrl.cmasi.MissionCommand MissionCommand}
 * message to set the waypoint list and control the turning type. See the UAVSIM
 * documentation for details on different turn types.
 *
 * @author AFRL/RQQD
 */
public class WaypointFollower extends EntityModule {

    public static final int WAYPOINT_FOLLOW_MODE = 8;
    Waypoint currentWp = null;
    Waypoint pastWp = null;
    Waypoint nextWp = null;
    MissionCommand mission = null;
    double turnRadiusMeter = 1000;
    public static final double GRAVITY = 9.8;
    int ctr = 0;
    boolean approvedPlansOnly = false;
    // some parameters for waypoint following geometry below
    /**
     * azimuth between past wp and current wp, in radians
     */
    double az_past_curr = 0;
    /**
     * azimuth between next wp and current wp in radians. (back course from next
     * waypoint)
     */
    double az_next_curr = 0;
    /**
     * distance between past wp and current wp in meters
     */
    double dist_past_curr = 0;
    /**
     * distance between current wp and next wp in meters
     */
    double dist_curr_next = 0;
    // a place to store all of the lat,lons in radians
    double curr_wp_lat = 0, curr_wp_lon = 0, next_wp_lat = 0, next_wp_lon = 0,
            past_wp_lat = 0, past_wp_lon = 0;

    /* Sriram Sankaranarayanan: Can we add GPS uncertainty to this controller?
     */
    boolean gps_fuzz_enabled;
    Random r;
    boolean debug_messages;

    public WaypointFollower() {
        /* Sriram Sankaranarayanan: Setting this to true. We will add roughly 10 meters or so according to a Gaussian random variable. */
        this.gps_fuzz_enabled = true;
        this.debug_messages=false;
        r = new Random();
    }

    @Override
    public void initialize(Element xmlElement) {
        this.approvedPlansOnly = XMLUtil.getBool(xmlElement, "RequireApprovedCommands", approvedPlansOnly);
    }

    /**
     * Updates the navigation based on the current waypoint or advances the
     * waypoint if necessary.
     *
     * @param timestep_sec simulation timestep in seconds.
     * @param simtime_sec current simulation time in seconds
     */
    @Override
    public void step(double timestep_sec, double simtime_sec) {
        if (data.autopilotCommands.navMode.getValue() != NavigationMode.Waypoint) {
            return;
        }

        // if the current waypoint number does not match the one in the property map, then
        // attempt to load the waypoint and return so that waypoint null checking can occur on 
        // next frame.
        if (currentWp == null || currentWp.getNumber() != data.autopilotCommands.currentWaypoint.asInteger()) {
            incrementWaypoint(data.autopilotCommands.currentWaypoint.asInteger());
            return;
        }

        double entity_lat = data.lat.asDouble();
        double entity_long = data.lon.asDouble();

        if (this.gps_fuzz_enabled) {
            /* --
                Sriram Sankaranarayanan:
                Add a gaussian offset to the latitute and longitude
                  We will assume a standard deviation of 4 meters for the error in latitude and longitude.
                  We will also assume that these errors are independent. Both of these are questionable
                  and if at all GPS fuzzing matters, we can change these assumptions to be more realistic.
             */

            double w1 = 4.0 * r.nextGaussian();
            double w2 = 4.0 * r.nextGaussian();
            double alt = data.alt.asDouble();

            entity_long = NavUtils.getLon(entity_lat, entity_long, w2, alt);
            entity_lat = NavUtils.getLat(entity_lat, w1, alt);
            if (this.debug_messages) {
                System.out.println(" Fuzzing Latitute : " + data.lat.asDouble() + "---> " + entity_lat);
                System.out.println(" Fuzzing Longitude: " + data.lon.asDouble() + "---> " + entity_long);
            }
        }


        double dist = NavUtils.distance(entity_lat, entity_long, curr_wp_lat, curr_wp_lon);

        // factor the altitude into the distance
        dist = Math.hypot(dist, currentWp.getAltitude() - data.alt.asDouble());

        double az = NavUtils.headingBetween(entity_lat, entity_long, curr_wp_lat, curr_wp_lon);
        
        // estimated nominal turn radius based on speed, bank angle
        turnRadiusMeter = pow(data.u.asDouble(), 2) / (GRAVITY * tan(data.autopilotCommands.maxBank.asDouble()));

        if (currentWp.getTurnType() == TurnType.FlyOver) {
            computeTurnPast(dist, az);
            computeReturnToRoute(az, entity_lat, entity_long);
        }
        else {
            computeTurnShort(dist, az);
            computeReturnToRoute(az, entity_lat, entity_long);
        }

        // compute the commanded alt to acheive a smooth transition through the waypoint
        if (dist != 0) {
            double cmdVs = (data.autopilotCommands.cmdAlt.asDouble() - data.alt.asDouble()) / dist * data.u.asDouble();
            data.autopilotCommands.cmdVertSpeed.setValue(cmdVs);
        }

        // make sure alt and speed are set
        data.autopilotCommands.cmdAlt.setValue(currentWp.getAltitude());
        data.autopilotCommands.cmdSpeed.setValue(currentWp.getSpeed());
        data.autopilotCommands.speedCmdType.setValue(SpeedType.Airspeed);

    }

    /**
     * Sets the waypoint follower on/off. This is usually done automatically by
     * the MissionCommand message.
     *
     * @param isEnabled sets the waypoint following on (true) or off (false)
     */
    public void setWpFollow(boolean isEnabled) {
        data.autopilotCommands.navMode.setValue(NavigationMode.Waypoint);
    }

    /**
     * Algorithm for doing a simple turn-past style turn. Called by the step
     * method.
     *
     * @param dist distance to the waypoint in meters
     * @param az azimuth to the waypoint in radians
     */
    public void computeTurnPast(double dist, double az) {
        double hdgDiff = Math.abs(Unit.boundPi(data.psi.asDouble() - az));

        boolean isClose = dist < turnRadiusMeter;
        boolean isBehind = Math.abs(hdgDiff) > Math.PI / 2.;

        if (isClose && isBehind) {
            incrementWaypoint(currentWp.getNextWaypoint());
        }
        else {
            data.autopilotCommands.cmdHdg.setValue(az);
        }
    }

    /**
     * computes a turn-short style turn. When the vehicle is within a certain
     * radius of the waypoint, the next waypoint is selected.
     *
     * @param dist distance remaining to waypoint (meters)
     * @param az azimuth from current vehicle location to current waypoint
     * (radians)
     * @return true if the vehicle is currently in its transition turn from one
     * waypoint to the next
     */
    private boolean computeTurnShort(double dist, double az) {
        if (nextWp == null) {
            computeTurnPast(dist, az);
            return false;
        }
        //double az_next = NavUtils.headingBetween(toRadians(nextWp.getLatitude()), toRadians(nextWp.getLongitude()),
        //        toRadians(currentWp.getLatitude()), toRadians(currentWp.getLongitude()));
        double az_diff = az_next_curr - az;

        if (dist_curr_next < turnRadiusMeter) {
            az_diff = 0;
        }

        // distance to start the turn is based on the point where the turn circle lies tangent to 
        // the angle formed between the past, current, and next waypoints.
        double distToTurn = abs(turnRadiusMeter / tan(0.5 * az_diff));


        if (distToTurn > turnRadiusMeter * 2) {
            distToTurn = 2 * turnRadiusMeter;
        }
        if (Double.isNaN(distToTurn)) {
            distToTurn = 0;
        }

        if (dist < distToTurn && dist < turnRadiusMeter * 2.) {
            incrementWaypoint(currentWp.getNextWaypoint());
            return true;
        }
        else {
            data.autopilotCommands.cmdHdg.setValue(az);
            return false;
        }

    }

    /**
     * computes a path that returns the vehicle to the route line described by a
     * straight line between the past and current waypoints.
     *
     * @param az azimuth from current vehicle location to current waypoint
     * (radians)
     */
    public void computeReturnToRoute(double az, double entity_lat, double entity_long) {
        if (pastWp != null && currentWp != null) {

            // limit for distance to perform a route-tracking maneuver.
            double turn_limit = 2 * turnRadiusMeter;

            // if there isn't enough room to do a return-to-route manuever, then 
            // jump out.
            if (dist_past_curr < turnRadiusMeter) {
                return;
            }

            // current distance and azimuth to the route line.
            double[] distAz = NavUtils.distanceToLine(past_wp_lat, past_wp_lon, curr_wp_lat, curr_wp_lon,
                    entity_lat, entity_long);


            // if the vehicle is greater than turn limit away from the route line, fly a perpendicular
            // path to the route, otherwise, gently turn to meet the route line at the route heading

            if (distAz[0] > turn_limit) {
                data.autopilotCommands.cmdHdg.setValue(distAz[1]);
            }
            else {
                // smoothly blend between the perpendicular to the route line and parallel with the route line
                double dist_norm = distAz[0] / turn_limit;
                double psi = Unit.boundPi( distAz[1] + Unit.boundPi(az_past_curr - distAz[1]) * (1 - dist_norm) );
                data.autopilotCommands.cmdHdg.setValue(psi);
            }
        }
    }

    /**
     * sets the current, past, and next waypoints.
     */
    public void incrementWaypoint(long nextWpNum) {
        if (currentWp != null) {
            data.autopilotCommands.waypointReached.setValue(currentWp.getNumber());
            for (VehicleAction va : currentWp.getVehicleActionList()) {
                fireModelEvent(va);
            }

        }
        else {
            data.autopilotCommands.waypointReached.setValue(0);
        }
        if (mission == null) {
            currentWp = null;
            nextWp = null;
            pastWp = null;
        }
        else {
            Waypoint tmp = CmasiUtils.getWaypoint(mission, nextWpNum);
            if (tmp != currentWp) {
                pastWp = currentWp;
            }
            currentWp = tmp;
            nextWp = currentWp == null ? null : CmasiUtils.getWaypoint(mission, currentWp.getNextWaypoint());
        }
        if (currentWp == null) {
            currentWp = pastWp;
        }
        if (currentWp != null) {

            data.autopilotCommands.currentWaypoint.setValue(currentWp.getNumber());
            data.autopilotCommands.cmdAlt.setValue(currentWp.getAltitude());
            data.autopilotCommands.cmdSpeed.setValue(currentWp.getSpeed());
        }

        // compute all of the parameters related to waypoints.  This avoids doing computations in
        // every step


        if (currentWp != null) {
            this.curr_wp_lat = Math.toRadians(currentWp.getLatitude());
            this.curr_wp_lon = Math.toRadians(currentWp.getLongitude());
        }
        if (pastWp != null) {
            this.past_wp_lat = Math.toRadians(pastWp.getLatitude());
            this.past_wp_lon = Math.toRadians(pastWp.getLongitude());
        }
        if (nextWp != null) {
            this.next_wp_lat = Math.toRadians(nextWp.getLatitude());
            this.next_wp_lon = Math.toRadians(nextWp.getLongitude());
        }

        if (pastWp != null && currentWp != null) {
            this.az_past_curr = NavUtils.headingBetween(past_wp_lat, past_wp_lon, curr_wp_lat, curr_wp_lon);
            this.dist_past_curr = NavUtils.distance(past_wp_lat, past_wp_lon, curr_wp_lat, curr_wp_lon);
        }
        if (currentWp != null && nextWp != null) {
            this.az_next_curr = NavUtils.headingBetween(next_wp_lat, next_wp_lon, curr_wp_lat, curr_wp_lon);
            this.dist_curr_next = NavUtils.distance(curr_wp_lat, curr_wp_lon, next_wp_lat, next_wp_lon);
        }
    }

    @Override
    public void modelEventOccurred(Object object) {
        if (object instanceof MissionCommand) {
            MissionCommand mc = (MissionCommand) object;
            if (approvedPlansOnly && mc.getStatus() != CommandStatusType.Approved) {
                return;
            }
            currentWp = null;
            pastWp = null;
            nextWp = null;
            // only update mission command if it contains new waypoints
            if (mc.getWaypointList().size() > 0) {
                this.mission = mc;
                setWpFollow(true);
                incrementWaypoint(mc.getFirstWaypoint());
                if (currentWp != null) {
                    data.autopilotCommands.navMode.setValue(NavigationMode.Waypoint);
                }
                else {
                    data.autopilotCommands.navMode.setValue(NavigationMode.FlightDirector);
                }
            }
        }
        else if (object instanceof AirVehicleState) {
            AirVehicleState state = (AirVehicleState) object;
            if (data.autopilotCommands.navMode.getValue() == NavigationMode.Waypoint) {
                if (currentWp != null) {
                    state.setMode(NavigationMode.Waypoint);
                    state.getAssociatedTasks().clear();
                    if (mission != null) {
                        state.setCurrentCommand(mission.getCommandID());
                    }
                    for (long tmp : currentWp.getAssociatedTasks()) {
                        state.getAssociatedTasks().add(tmp);
                    }
                }
                else {
                    state.setMode(NavigationMode.FlightDirector);
                }
                state.setCurrentWaypoint(data.autopilotCommands.currentWaypoint.asInteger());
            }
        }
        else if (object instanceof VehicleActionCommand) {
            VehicleActionCommand vac = (VehicleActionCommand) object;
            for (VehicleAction va : vac.getVehicleActionList()) {
                if (va instanceof GoToWaypointAction) {
                    GoToWaypointAction wa = (GoToWaypointAction) va;
                    if (mission != null) {
                        currentWp = null;
                        setWpFollow(true);
                        incrementWaypoint(wa.getWaypointNumber());
                    }
                }

            }
        }
        else if (object instanceof GoToWaypointAction) {
            GoToWaypointAction wa = (GoToWaypointAction) object;
            if (mission != null) {
                currentWp = null;
                setWpFollow(true);
                incrementWaypoint(wa.getWaypointNumber());
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */