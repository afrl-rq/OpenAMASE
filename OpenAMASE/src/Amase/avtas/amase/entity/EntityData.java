// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.entity;

import afrl.cmasi.EntityConfiguration;
import afrl.cmasi.EntityState;
import afrl.cmasi.PayloadConfiguration;
import afrl.cmasi.PayloadState;
import avtas.data.Property;
import avtas.data.PropertyMap;
import avtas.lmcp.LMCPObject;
import avtas.util.NavUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Stores model data for use by entity modules.
 */
public class EntityData extends PropertyMap {

    /**
     * entity latitude (rad)
     */
    public Property lat = get("state/latitude");
    /**
     * entity longitude (rad)
     */
    public Property lon = get("state/longitude");
    /**
     * entity altitude above sea level (meter)
     */
    public Property alt = get("state/altitude");
    /**
     * entity mass (kg)
     */
    public Property mass = get("aircraft/mass");
    /**
     * Inertial axis orientation, in radians
     * <ul>
     * <li>psi: (heading) positive East of North
     * <li>theta: (pitch) Positive up
     * <li>phi: (roll) Positive right-wing down
     * </ul>
     */
    public Property psi = get("state/psi"), theta = get("state/theta"), phi = get("state/phi");
    /**
     * Body axis angular rates (in radians per sec). Uses the P-Q-R rotations
     * corresponding to a yaw-pitch-roll order of rotation
     */
    public Property p = get("state/p"), q = get("state/q"), r = get("state/r");
    /**
     * Body-axis accelerations in meters per second squared. Uses U-V-W
     * convention with right-hand rule. (udot = nose, vdot = out right-wing,
     * wdot = down)
     */
    public Property pdot = get("state/pdot"), qdot = get("state/qdot"), rdot = get("state/rdot");
    /**
     * Body axis velocity in meters per second. Uses right-hand rule. (u = nose,
     * v = right-wing, w = down)
     */
    public Property u = get("state/u"), v = get("state/v"), w = get("state/w");
    /**
     * Body-axis accelerations in meters per second squared. Uses U-V-W
     * convention with right-hand rule. (udot = nose, vdot = out right-wing,
     * wdot = down)
     */
    public Property udot = get("state/udot"), vdot = get("state/vdot"), wdot = get("state/wdot");
    /**
     * Inertial-frame speeds in meters/sec.
     */
    public Property vnorth = get("state/vnorth"), veast = get("state/veast"), vdown = get("state/vdown");
    /**
     * The unique ID for this aircraft or entity.
     */
    public Property id = get("amase/id");
    /**
     * terrain altitude at location of vehicle (meter)
     */
    public Property alt_terrain = get("environ/terrain_elev");
    /**
     * Energy remaining in the vehicle. Units are not specified. Typical usage
     * is to make this number fractional (0..1) based on some model of fuel or
     * battery life at start of the scenario.
     */
    public Property energy_remaining = get("power/energy_remaining");
    /**
     * The rate at which energy is expended in units/second. The units are the
     * same as those used in {@link #energy_remaining}.
     */
    public Property energy_rate = get("power/energy_rate");
    /**
     * current normal acceleration on the aircraft (body z-axis) in m/s^2
     */
    public Property g = get("state/g");
    // target tracking data
    /**
     * target track enabled *
     */
    public Property trackOn = get("amase/track_on");
    /**
     * target lat (rad) *
     */
    public Property targ_lat = get("amase/target/lat");
    /**
     * target lon (rad) *
     */
    public Property targ_lon = get("amase/target/lon");
    /**
     * target alt msl (m)*
     */
    public Property targ_alt = get("amase/target/alt");
    /**
     * track target age out *
     */
    public Property trackAgeOut = get("amase/trackAgeOut");
    /**
     * The current autopilot mode for this entity. This is aircraft-centric, but
     * can apply to other entity types as well
     */
    public final AutopilotCommands autopilotCommands = new AutopilotCommands(this);
    /**
     * Contains aerodynamic and aircraft properties.
     */
    public final AeroProperties aeroProperties = new AeroProperties(this);
    
    private final HashMap<Long, PayloadConfiguration> payloadConfigurations = new HashMap<>();
    private final HashMap<Long, PayloadState> payloadStates = new HashMap<>();
    public EntityConfiguration config = null;
    public EntityState currentState = null;

    /*  creates a new EntityData object and initializes all of the data references */
    public EntityData() {
    }

    /**
     * @return the payloadConfigurations
     */
    public List<PayloadConfiguration> getPayloadConfigurations() {
        return new ArrayList<>(payloadConfigurations.values());
    }

    /**
     * Returns a configuration with the given ID, or null if none exists.
     */
    public PayloadConfiguration getPayloadConfig(long id) {
        return payloadConfigurations.get(id);
    }

    /**
     * Sets a configuration for a payload.
     */
    public void setPayloadConfig(PayloadConfiguration config) {
        payloadConfigurations.put(config.getPayloadID(), config);
    }

    /**
     * @return the payloadStates
     */
    public List<PayloadState> getPayloadStates() {
        return new ArrayList<>(payloadStates.values());
    }

    /**
     * Returns the current state for a given payload, or null if none exists
     */
    public PayloadState getPayloadState(long id) {
        return payloadStates.get(id);
    }

    /**
     * Sets the current payload state. This replaces the state that is currently
     * in the map.
     */
    public void setPayloadState(PayloadState payloadState) {
        this.payloadStates.put(payloadState.getPayloadID(), payloadState);
    }

    /**
     * @return the configuration. This is either an AirVehicleConfiguration or
     * an EntityConfiguration.
     */
    public LMCPObject getConfiguration() {
        return config;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */
