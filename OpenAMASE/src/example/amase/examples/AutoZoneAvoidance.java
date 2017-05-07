// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package amase.examples;

import afrl.cmasi.AirVehicleState;
import afrl.cmasi.FlightDirectorAction;
import afrl.cmasi.KeepOutZone;
import afrl.cmasi.Location3D;
import afrl.cmasi.Location3D;
import afrl.cmasi.RemoveZones;
import afrl.cmasi.VehicleActionCommand;
import avtas.amase.entity.EntityModule;
import avtas.amase.util.CmasiNavUtils;
import avtas.amase.util.CmasiUtils;
import avtas.data.Unit;
import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.Map;

/**
 * In this example, an aircraft will be commanded to turn 180 degrees if it
 * enters a {@link KeepOutZone}.
 *
 * @author AFRL/RQQD
 */
public class AutoZoneAvoidance extends EntityModule {

    // stores all of the keep out zones in the scenario
    Map<Long, KeepOutZone> zoneMap = new HashMap<>();

    public AutoZoneAvoidance() {
    }

    @Override
    public void modelEventOccurred(Object event) {
        if (event instanceof AirVehicleState) {
            checkZone((AirVehicleState) event);
        }
    }

    @Override
    public void applicationEventOccurred(Object event) {
        if (event instanceof KeepOutZone) {
            KeepOutZone koz = (KeepOutZone) event;
            zoneMap.put(koz.getZoneID(), koz);
        } else if (event instanceof RemoveZones) {
            RemoveZones removeEvent = (RemoveZones) event;
            for (Long id : removeEvent.getZoneList()) {
                zoneMap.remove(id);
            }
        }
    }

    protected void checkZone(AirVehicleState avs) {

        // my position
        Location3D loc = avs.getLocation();

        // the amount of adjusted heading (used later)
        double deltaHeading = 0;

        for (KeepOutZone zone : zoneMap.values()) {
            // this is a utility function that converts CMASI geometries to java 
            // 2D geometries. We will use this to do point-in-poly testing
            Path2D boundary = CmasiUtils.convertPoly(zone.getBoundary());

            if (boundary == null) {
                continue;
            }

            // if we are outside the altitude bounds of the zone, then ignore it
            if (zone.getMinAltitude() > loc.getAltitude() || zone.getMaxAltitude() < loc.getAltitude()) {
                continue;
            }

            // project our future state based on the current trajectory.  This uses the
            // CmasiNavUtils tool (which is based on NavUtils), an AMASE development
            // kit tool to assist with navigational calculations.
            // note the use of ground speed and ground track (available in AirVehicleState)
            // use a look ahead time to do the intersection test.
            double lookAheadTime = 10; // seconds      

            Location3D projected_pt;

            projected_pt = CmasiNavUtils.getPoint(loc, avs.getGroundspeed() * lookAheadTime, avs.getCourse());

            while (boundary.contains(projected_pt.getLongitude(), projected_pt.getLatitude()) && deltaHeading < 360) {

                deltaHeading += 10;
                projected_pt = CmasiNavUtils.getPoint(loc, avs.getGroundspeed() * lookAheadTime, avs.getCourse() + deltaHeading);

            }

        }

        // we have a new path, so execute the new heading
        if (deltaHeading != 0) {
            executeManeuver(avs, deltaHeading);
        }
    }

    private void executeManeuver(AirVehicleState avs, double deltaHeading) {
        //this.exitingZone = zone;

        // turn the aircraft to the opposite heading.  Use a bounding
        // function to make sure the commanded angle is [0..360]
        double newHeading = Unit.bound360(avs.getHeading() + deltaHeading);

        // fill out an action that requests a new trajectory.  This uses the modified
        // heading but also introduces the data map.  Each model has a map of values 
        // describing the state and other related info.  Here, the autopilot commands
        // are read from the data map to build the action.
        FlightDirectorAction action = new FlightDirectorAction();
        action.setAltitude((float) getData().autopilotCommands.cmdAlt.asDouble());
        action.setHeading((float) newHeading);
        action.setSpeed((float) getData().autopilotCommands.cmdSpeed.asDouble());
        action.setSpeedType(getData().autopilotCommands.speedCmdType.getValue());

        // actions are put into commands that are issued for the aircraft.
        VehicleActionCommand command = new VehicleActionCommand();
        command.getVehicleActionList().add(action);
        command.setVehicleID(avs.getID());
        command.setCommandID(1);

        // fire the new command twice.  Inform the application and the rest of the model
        // of the command.  Other modules will use the command to steer the aircraft.  
        // But the application also needs to know of the command for logging, display, or 
        // other purposes.
        fireApplicationEvent(command);
        fireModelEvent(command);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */