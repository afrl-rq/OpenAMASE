// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.entity.modules;

import avtas.amase.entity.AutopilotCommands;
import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.GimbalConfiguration;
import afrl.cmasi.GimbalStareAction;
import afrl.cmasi.GoToWaypointAction;
import afrl.cmasi.Location3D;
import afrl.cmasi.LoiterAction;
import afrl.cmasi.LoiterDirection;
import afrl.cmasi.MissionCommand;
import afrl.cmasi.NavigationMode;
import afrl.cmasi.PayloadAction;
import afrl.cmasi.VehicleAction;
import afrl.cmasi.VehicleActionCommand;
import afrl.cmasi.perceive.EntityPerception;
import afrl.cmasi.perceive.TrackEntityAction;
import avtas.amase.entity.EntityModule;
import avtas.amase.util.CmasiUtils;
import avtas.util.NavUtils;
import java.util.HashMap;
import avtas.xml.Element;
import avtas.xml.XMLUtil;

/**
 * Manages target tracking actions.
 */
public class TrackControl extends EntityModule {

    /**
     * Defines the modes for the tracker. When a track action is received, the
     * track mode changes to pending. When the track is actively underway, the
     * mode is active. When a track has been acquired but lost, the mode
     * switches to lost. If there is no track action, then the mode is off.
     */
    static enum TrackMode {

        PENDING,
        ACTIVE,
        LOST,
        OFF
    }
    private double currentSimTime;
    private double trackTimeOut = 0;
    TrackMode trackMode = TrackMode.OFF;
    //private ArrayList<TrackTargetAction> standingTracks = new ArrayList<TrackTargetAction>();
    private HashMap<Long, EntityPerception> EntityPerceptions = new HashMap<>();
    private TrackEntityAction currentTrack = null;
    private AirVehicleConfiguration aircraftConfig = null;
    private long trackingGimbal = 0;

    /**
     * Creates a new instance of TrackControl
     */
    public TrackControl() {
    }

    /**
     * Checks for new track actions
     */
    public void step(double timestep_sec, double simtime_sec) {
        currentSimTime += timestep_sec;
        if (trackMode != TrackMode.OFF && trackMode != TrackMode.LOST) {
            updateTrack();
        }
    }

    public void initialize(Element xmlElement) {
        trackTimeOut = XMLUtil.getDouble(xmlElement, "TrackTimeout", trackTimeOut);
    }

    @Override
    public void modelEventOccurred(Object object) {
        if (object instanceof MissionCommand) {
            clearTrackAction();
        }
        else if (object instanceof VehicleActionCommand) {
            VehicleActionCommand cmd = (VehicleActionCommand) object;

            for (VehicleAction va : cmd.getVehicleActionList()) {
                if (va instanceof TrackEntityAction) {
                    setupTrackAction((TrackEntityAction) va);
                } // if a navigational action is received, then break the track mode
                else if (!(va instanceof PayloadAction)) {
                    clearTrackAction();
                }
            }
        }
        else if (object instanceof AirVehicleState) {
            // if this module is tracking, then report the tracking mode as active
            if (trackMode == TrackMode.ACTIVE) {
                ((AirVehicleState) object).setMode(NavigationMode.TargetTrack);
            }
        }
        else if (object instanceof TrackEntityAction) {
            setupTrackAction((TrackEntityAction) object);
        }
        else if (object instanceof AirVehicleConfiguration) {
            this.aircraftConfig = (AirVehicleConfiguration) object;
        }
    }

    @Override
    public void applicationEventOccurred(Object event) {
        if (event instanceof EntityPerception) {
            EntityPerception det = (EntityPerception) event;
            if (det.getPerceiverID() == data.id.asLong() || det.getPerceiverID() == 0) {
                EntityPerceptions.put(det.getPerceivedEntityID(), det);
            }
        }
    }

    void setupTrackAction(TrackEntityAction action) {
        currentTrack = action;
        trackMode = TrackMode.PENDING;

        // store the gimbal ID of the sensor (camera) designated for tracking
        GimbalConfiguration gc = CmasiUtils.getGimbalForPayload(currentTrack.getSensorID(), aircraftConfig);
        if (gc != null) {
            this.trackingGimbal = gc.getPayloadID();
        }
        else {
            this.trackingGimbal = 0;
        }
    }

    void clearTrackAction() {
        currentTrack = null;
        trackMode = TrackMode.OFF;
        data.trackOn.setValue(false);
    }

    void resumeNavigation() {
        if (currentTrack != null) {
            trackMode = TrackMode.LOST;
            data.trackOn.setValue(false);
            if (currentTrack.getReturnToWaypoint() > 0) {
                GoToWaypointAction gwp = new GoToWaypointAction(currentTrack.getReturnToWaypoint());
                fireModelEvent(gwp);
            }
            else {
                fireModelEvent(createLoiterAction());
            }
        }
    }

    /**
     * tries to turn the vehicle directly at the target. If the track is too
     * old, then this method returns false, otherwise, it sets the commands for
     * the vehicle autopilot and returns true.
     *
     * @param currentAction the track to update
     */
    private void updateTrack() {
        EntityPerception det = getPerception(currentTrack.getEntityID());
        if (trackMode == TrackMode.OFF || trackMode == TrackMode.LOST) {
            return;
        }

        if (det != null) {

            if (currentSimTime - det.getTimeLastSeen() > trackTimeOut) {
                if (trackMode == trackMode.ACTIVE) {
                    resumeNavigation();
                }
                return;
            }
            trackMode = TrackMode.ACTIVE;

            data.trackOn.setValue(true);
            data.autopilotCommands.navMode.setValue(NavigationMode.TargetTrack);

            data.autopilotCommands.verticalCmdType.setValue(AutopilotCommands.VerticalCommandType.AltitudeMSL);
            data.autopilotCommands.lateralCmdType.setValue(AutopilotCommands.LateralCommandType.Heading);

            data.targ_lat.setValue(Math.toRadians(det.getLocation().getLatitude()));
            data.targ_lon.setValue(Math.toRadians(det.getLocation().getLongitude()));
            //data.targ_alt.setValue(det.getTargetLocation().getAltitude());

            double psiVehicleTgt = NavUtils.headingBetween(data.lat.asDouble(), data.lon.asDouble(),
                    Math.toRadians(det.getLocation().getLatitude()),
                    Math.toRadians(det.getLocation().getLongitude()));
            data.autopilotCommands.cmdHdg.setValue(psiVehicleTgt);

            // fire an event to slew the gimbal to the target location
            if (trackingGimbal != 0) {
                GimbalStareAction gsa = new GimbalStareAction(trackingGimbal, det.getLocation().clone(), 0);
                fireModelEvent(gsa);
            }
        }
    }

    private EntityPerception getPerception(long targetId) {
        EntityPerception det = EntityPerceptions.get(targetId);
        if (det != null) {
            return det;
        }
        return null;
    }

    /**
     * creates and returns a loiter action that loiters around the current
     * aircraft location at the current aircraft altitude.
     */
    LoiterAction createLoiterAction() {

        final Location3D loiterLoc = new Location3D();
        loiterLoc.setAltitude((float) data.alt.asDouble());
        loiterLoc.setLatitude(Math.toDegrees(data.lat.asDouble()));
        loiterLoc.setLongitude(Math.toDegrees(data.lon.asDouble()));

        // assume that we loiter at 1/2 of the max bank angle
        double phi = 0.5 * data.autopilotCommands.maxBank.asDouble();
        phi = phi == 0 ? 0.35 : phi;

        final double speed = data.u.asDouble();

        final double radius = speed * speed / 9.8 / Math.tan(phi);

        LoiterAction loiterAction = new LoiterAction();
        loiterAction.setAirspeed((float) speed);
        loiterAction.setDirection(LoiterDirection.CounterClockwise);
        loiterAction.setDuration(0); // indefinite
        loiterAction.setRadius((float) radius);
        loiterAction.setLocation(loiterLoc);

        return loiterAction;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */