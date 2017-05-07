// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.entity.modules;

import afrl.cmasi.EntityState;
import afrl.cmasi.FollowPathCommand;
import afrl.cmasi.PathWaypoint;
import avtas.amase.entity.EntityModule;
import avtas.terrain.TerrainService;
import avtas.util.NavUtils;
import avtas.xml.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author AFRL/RQQD
 */
public class EntityPathFollower extends EntityModule {

    private FollowPathCommand cmd = null;
    List<PathWaypoint> waypointList = null;
    PathWaypoint currentWaypoint = null;
    private double simTime = 0;
    boolean waiting = false;
    double currentWait = 0;
    double wpRadLat = 0, wpRadLon = 0;
    int currentWpNum = 0;
    // maximum acceleration/deceleration in meters per sec
    double maxAccel = 1.0;

    public EntityPathFollower() {
    }

    @Override
    public void step(double timestep_sec, double simtime_sec) {
        simTime += timestep_sec;
        if (cmd == null) {
            return;
        }
        if (currentWaypoint != null) {
            updatePosition(timestep_sec);
        }
    }

    @Override
    public void initialize(Element xmlElement) {
        this.cmd = null;
        this.waypointList = null;
        this.currentWaypoint = null;
        this.currentWpNum = 0;

    }

    @Override
    public void modelEventOccurred(Object object) {
        if (object instanceof FollowPathCommand) {
            this.cmd = (FollowPathCommand) object;
            this.waypointList = buildRoute(cmd);
            if (waypointList.size() > 0) {
                this.currentWaypoint = this.waypointList.get(0);
                wpRadLat = Math.toRadians(currentWaypoint.getLatitude());
                wpRadLon = Math.toRadians(currentWaypoint.getLongitude());
                currentWpNum = 0;
            }
        } else if (object instanceof EntityState) {
            EntityState es = (EntityState) object;
            data.lat.setValue(Math.toRadians(es.getLocation().getLatitude()));
            data.lon.setValue(Math.toRadians(es.getLocation().getLongitude()));

            // clamp the entity to the terrain
            double terrHeight = TerrainService.getElevation(Math.toDegrees(data.lat.asDouble()), Math.toDegrees(data.lon.asDouble()));
            if (data.alt.asDouble() < terrHeight) {
                data.alt.setValue(terrHeight);
            }
        }
    }

    public void updatePosition(double timestep_sec) {

        double radLat = data.lat.asDouble();
        double radLon = data.lon.asDouble();

        if (currentWaypoint == null) {
            return;
        }

        if (simTime < cmd.getStartTime() || (simTime > cmd.getStopTime() && cmd.getStopTime() != 0)) {
            return;
        }

        // if waiting, wait for some time, then go to the next waypoint
        if (waiting) {
            currentWait += timestep_sec;
            // set the next waypoint if done waiting
            if (currentWait >= currentWaypoint.getPauseTime()) {
                setNextWaypoint();
                currentWait = 0;
                waiting = false;
            }
            return;
        }

        // if the target is close to the waypoint, then snap to that point and start waiting
        // the closeness is a factor of the timestep and speed.
        double meterDist = NavUtils.distance(radLat, radLon, wpRadLat, wpRadLon);

        if (meterDist < 2.0 * timestep_sec * currentWaypoint.getSpeed()) {
            data.lat.setValue(wpRadLat);
            data.lon.setValue(wpRadLon);
            data.theta.setValue(0);
            waiting = true;
            return;
        }

        // only accelerate/decelerate as fast as the maximum acceleration
        double udiff = currentWaypoint.getSpeed() - data.u.asDouble();
        double accel = Math.min(Math.abs(udiff), maxAccel * timestep_sec);
        data.u.setValue(data.u.asDouble() + accel * Math.signum(udiff));

        // point toward the waypoint and update the state data
        data.psi.setValue(NavUtils.headingBetween(radLat, radLon, wpRadLat, wpRadLon));
        data.vnorth.setValue(Math.cos(data.psi.asDouble()) * data.u.asDouble());
        data.veast.setValue(Math.sin(data.psi.asDouble()) * data.u.asDouble());

        // update the position 
        double[] ll = NavUtils.getLatLon(radLat, radLon, data.u.asDouble() * timestep_sec, data.psi.asDouble());
        data.lat.setValue(ll[0]);
        data.lon.setValue(ll[1]);

        double terrHeight = TerrainService.getElevation(Math.toDegrees(data.lat.asDouble()), Math.toDegrees(data.lon.asDouble()));
        //if (data.alt.asDouble() < terrHeight) {
            data.alt.setValue(terrHeight);
        //}

        // if the waypoint has a non-zero altitude, then change altitude to try to meet the waypoint.
        // orient the entity to point at the waypoint while climbing
        if (currentWaypoint.getAltitude() != 0) {
            double hdiff = Math.max(terrHeight, currentWaypoint.getAltitude()) - data.alt.asDouble();
            double climbAngle = Math.atan2(hdiff, meterDist);

            data.theta.setValue(climbAngle);
            data.vdown.setValue(-currentWaypoint.getSpeed() * Math.sin(climbAngle));
            data.alt.setValue(data.alt.asDouble() - data.vdown.asDouble() * timestep_sec);
        }



    }

    public void setNextWaypoint() {

        if (currentWaypoint == null) {
            return;
        }

        currentWpNum++;

        if (currentWpNum >= waypointList.size()) {
            switch (cmd.getRepeatMode()) {
                case ReverseCourse:
                    Collections.reverse(waypointList);
                    currentWpNum = 0;
                case Loop:
                    currentWpNum = 0;
                    break;
                default:
                    return;
            }
        }

        currentWaypoint = waypointList.get(currentWpNum);
        wpRadLat = Math.toRadians(currentWaypoint.getLatitude());
        wpRadLon = Math.toRadians(currentWaypoint.getLongitude());
        data.vnorth.setValue(Math.cos(data.psi.asDouble()) * currentWaypoint.getSpeed());
        data.veast.setValue(Math.sin(data.psi.asDouble()) * currentWaypoint.getSpeed());
        data.u.setValue(currentWaypoint.getSpeed());
        data.psi.setValue(NavUtils.headingBetween(data.lat.asDouble(), data.lon.asDouble(), wpRadLat, wpRadLon));
        data.theta.setValue(0);
    }

    static List<PathWaypoint> buildRoute(FollowPathCommand cmd) {
        List<PathWaypoint> list = new ArrayList<PathWaypoint>();
        PathWaypoint wp = getWaypoint(cmd.getWaypointList(), cmd.getFirstWaypoint());
        while (wp != null) {
            list.add(wp);
            wp = getWaypoint(cmd.getWaypointList(), wp.getNextWaypoint());
        }
        return list;
    }

    static PathWaypoint getWaypoint(List<PathWaypoint> list, long wpNum) {
        for (PathWaypoint wp : list) {
            if (wp.getNumber() == wpNum) {
                return wp;
            }
        }
        return null;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */