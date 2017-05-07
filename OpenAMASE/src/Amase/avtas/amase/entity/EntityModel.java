// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.amase.entity;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.PayloadConfiguration;
import afrl.cmasi.SpeedType;
import afrl.cmasi.EntityConfiguration;
import afrl.cmasi.EntityState;
import avtas.amase.scenario.ScenarioState;
import avtas.app.AppEventManager;
import avtas.app.AppEventManager.EventWrapper;
import avtas.lmcp.LMCPObject;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * Container class for a single entity model. The EntityModel maintains a list
 * of {@link EntityModule}s that control vehicle dynamics, command and control,
 * and sensor actions. <br>
 *
 * The model is configured at class construction time using an
 * {@link EntityConfiguration} object.
 *
 * An EntityModel is usually constructed by {@link EntityControl} for each
 * aircraft in the scenario. EntityModels are maintained by
 * {@link EntityControl} and are informed of updates through passages of LMCP
 * messages that are relevant to the model. <br> This class maintains a
 * {@link EntityData} object that contains common variables that are accessed by
 * all of the {@link EntityModule} maintained by this class.
 *
 *
 * @author AFRL/RQQD
 */
public class EntityModel {

    //private PropertyMap propertyMap = new PropertyMap();
    protected List<EntityModule> moduleList = new ArrayList<>();
    protected EntityData data;
    //protected Property simTimeProp;
    protected long entityId;
    // if set to false, then the model does not update
    protected boolean calculate = false;
    protected EntityState internalState = null;
    private AppEventManager eventManager = null;
    private EntityConfiguration configuration = null;
    ArrayDeque<AppEventManager.EventWrapper> eventQueue = new ArrayDeque<>();
    boolean isDispatching = false;
    AppEventManager modelEventManager = new AppEventManager();

    /**
     * Constructs a new model and loads the default Modules in place.
     *
     * @param entity the configuration for this aircraft
     */
    public EntityModel(EntityConfiguration entity) {
        data = new EntityData();
        this.entityId = entity.getID();
        data.id.setValue(entityId);
        this.configuration = entity;

        fireModelEvent(entity);

        // for each contained payload, fire an event so that loaded modules can 
        // configure themselves
        for (PayloadConfiguration pc : entity.getPayloadConfigurationList()) {
            getData().setPayloadConfig(pc);
            fireModelEvent(pc);
        }

    }

    /**
     * Sets the initial location, orientation, and air data state of this entity
     * model.
     *
     * @param state The state to set for this vehicle.
     */
    public void setInitialState(EntityState state) {
        internalState = state.clone();

        EntityState es = (EntityState) state;

        data.psi.setValue(Math.toRadians(es.getHeading()));
        data.theta.setValue(Math.toRadians(es.getPitch()));
        data.phi.setValue(Math.toRadians(es.getRoll()));
        data.u.setValue(es.getU());
        data.v.setValue(es.getV());
        data.w.setValue(es.getW());
        data.udot.setValue(es.getUdot());
        data.vdot.setValue(es.getVdot());
        data.wdot.setValue(es.getWdot());
        data.p.setValue(es.getP());
        data.q.setValue(es.getQ());
        data.r.setValue(es.getR());
        data.lat.setValue(Math.toRadians(es.getLocation().getLatitude()));
        data.lon.setValue(Math.toRadians(es.getLocation().getLongitude()));
        data.alt.setValue(es.getLocation().getAltitude());
        data.energy_remaining.setValue(es.getEnergyAvailable());

        data.autopilotCommands.cmdSpeed.setValue(es.getU());
        data.autopilotCommands.speedCmdType.setValue(SpeedType.Groundspeed);
        data.autopilotCommands.cmdAlt.setValue(es.getLocation().getAltitude());
        data.autopilotCommands.cmdHdg.setValue(Math.toRadians(es.getHeading()));

        data.autopilotCommands.currentWaypoint.setValue(-1);

        if (state instanceof AirVehicleState) {
            AirVehicleState avs = (AirVehicleState) internalState;

            getData().autopilotCommands.cmdSpeed.setValue(avs.getAirspeed());
            getData().autopilotCommands.speedCmdType.setValue(SpeedType.Airspeed);
            getData().autopilotCommands.cmdAlt.setValue(avs.getLocation().getAltitude());
            getData().autopilotCommands.cmdHdg.setValue(Math.toRadians(avs.getHeading()));

        }

        for (EntityModule m : moduleList) {
            m.modelEventOccurred(internalState);
        }
        calculate = true;
    }

    /**
     * Informs the model that it is being removed from the simulation. This is a
     * pass-through method that iterates through the individual modules.
     */
    public void shutdown() {
        for (EntityModule fm : moduleList) {
            fm.shutdown();
            eventManager.removeListener(fm.appListener);
        }
    }

    /**
     * sets a reference to the application-wide event manager so modules can
     * fire application-wide events
     *
     * @param mgr the event manager to assign to this model
     */
    public void setEventManager(AppEventManager mgr) {
        this.eventManager = mgr;
    }

    /**
     * returns a reference to the application-wide event manager in order to
     * fire application-wide events
     *
     * @return the event manager assigned to this model
     */
    public AppEventManager getEventManger() {
        return eventManager;
    }

    /**
     * Updates the model. This propagates through all of the modules in the
     * model's module list.
     *
     * @param time_step change in simulation time (seconds)
     * @param simtime_sec current simulation time (seconds)
     */
    public void update(double time_step, double simtime_sec) {
        if (!calculate) {
            return;
        }
        for (EntityModule m : moduleList) {
            m.step(time_step, simtime_sec);
        }

    }

    /**
     * Notifies modules of the simulation entering a paused state.
     */
    public void pause() {
        for (EntityModule m : moduleList) {
            m.pause();
        }
    }

    /**
     * Convenience function for getting to the listing of variables common to
     * all of the model's modules.
     *
     * @return the property map containing common variables in this model.
     */
    public EntityData getData() {
        return data;
    }

    /**
     * Called by {@link EntityControl} to get the current state of the aircraft
     *
     * @return the current state.
     */
    public EntityState getState() {

        if (internalState == null) {
            return null;
        }

        if (internalState instanceof EntityState) {
            EntityState es = (EntityState) internalState;

            es.setID(entityId);

            es.getLocation().setAltitude((float) data.alt.asDouble());
            es.getLocation().setLatitude(Math.toDegrees(data.lat.asDouble()));
            es.getLocation().setLongitude(Math.toDegrees(data.lon.asDouble()));
            es.setU((float) data.u.asDouble());
            es.setV((float) data.v.asDouble());
            es.setW((float) data.w.asDouble());

            es.setUdot((float) data.udot.asDouble());
            es.setVdot((float) data.vdot.asDouble());
            es.setWdot((float) data.wdot.asDouble());

            es.setHeading((float) Math.toDegrees(data.psi.asDouble()));
            es.setPitch((float) Math.toDegrees(data.theta.asDouble()));
            es.setRoll((float) Math.toDegrees(data.phi.asDouble()));

            es.setEnergyAvailable((float) data.energy_remaining.asDouble());

            es.setP((float) data.p.asDouble());
            es.setQ((float) data.q.asDouble());
            es.setR((float) data.r.asDouble());

            es.setTime((long) (ScenarioState.getTime() * 1000));

        }
        if (internalState instanceof AirVehicleState) {

            AirVehicleState avs = (AirVehicleState) internalState;

            avs.setVerticalSpeed((float) -data.vdown.asDouble());
            avs.setActualEnergyRate((float) data.energy_rate.asDouble());

            avs.setMode(data.autopilotCommands.navMode.getValue());
        }

        // passes the state message through the modules so that each module can modify
        // or read the message.
        for (EntityModule m : moduleList) {
            m.modelEventOccurred(internalState);
        }

        return internalState.clone();
    }

    /**
     * Called to inform this model of a new LMCP object. This is a pass-through
     * method that informs each {@link EntityModule} of the event.
     *
     * @param event The new event
     */
    public void fireModelEvent(Object event) {
        fireModelEvent(event, null);
    }

    /**
     * Called to inform this model of a new LMCP object. This is a pass-through
     * method that informs each {@link EntityModule} of the event.
     *
     * @param event The new event
     * @param source source of the event. Can be null. Events do not go back to
     * the source.
     */
    public void fireModelEvent(Object event, Object source) {

        eventQueue.add(new EventWrapper(event, source));

        if (isDispatching) {
            return;
        }
        isDispatching = true;

        while (!eventQueue.isEmpty()) {
            EventWrapper eventWrapper = eventQueue.pollFirst();
            if (eventWrapper != null) {
                for (EntityModule m : moduleList) {
                    if (eventWrapper.source != m) {
                        m.modelEventOccurred(event);
                    }
                }
            }
        }

        isDispatching = false;
    }

    public long getID() {
        return entityId;
    }

    /**
     * Get the entity/aircraft configuration object for this entity
     *
     * @return an {@link EntityConfiguration} or
     * {@link AirVehicleConfiguration}.
     */
    public EntityConfiguration getConfiguration() {
        return configuration;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */