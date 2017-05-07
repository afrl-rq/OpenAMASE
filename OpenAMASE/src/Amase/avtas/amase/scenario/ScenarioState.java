// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.scenario;

import afrl.cmasi.AbstractZone;
import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.EntityConfiguration;
import afrl.cmasi.EntityState;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import afrl.cmasi.Task;
import avtas.lmcp.LMCPObject;
import avtas.xml.Element;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Monitors the AMASE scenario and provides easy access to scenario objects.
 * Other application components can get scenario data by accessing the static
 * methods.
 * <br/>
 * This class is meant to be a singleton in any simulation. Creating new
 * instances does not affect the underlying implementation, but an instance must
 * be created prior to any scenario events occurring.
 * <br/>
 * Throughout the scenario, LMCP events are stored in a common list by time, so
 * any LMCP event may be retrieved through the static methods in this class.
 * <br/>
 * Common singleton types, such as
 * {@link AirVehicleConfiguration}, {@link AbstractZone}, etc, are also stored
 * in maps, and can be retrieved using their reference IDs. States for aircraft
 * and entities are stored in maps and may also be retrieved by ID. For states,
 * only the latest state is stored in the map. State history can be accessed
 * through the scenario event access methods.
 *
 * @author AFRL/RQQD
 * @version 1.0
 */
public class ScenarioState {

    static protected final HashMap<Long, AirVehicleConfiguration> aircraftConfigs = new HashMap<>();
    static protected final HashMap<Long, AirVehicleState> aircraftStates = new HashMap<>();
    static protected final HashMap<Long, AbstractZone> zones = new HashMap<>();
    static protected final HashMap<Long, Task> tasks = new HashMap<>();
    static protected final HashMap<Long, EntityState> entityStates = new HashMap<>();
    static protected final HashMap<Long, EntityConfiguration> entityConfigs = new HashMap<>();
    static protected final List<EventWrapper> scenarioHistory = new ArrayList<>();
    static protected final List<Long> aircraftOrder = new ArrayList<>();
    static protected SessionStatus currentStatus = null;
    static protected Element scenarioElement = null;
    static protected File sourceFile = null;
    static protected double time_sec = 0;

    public static void setScenario(Element scenario, File sourceFile) {
        ScenarioState.scenarioElement = scenario;
        ScenarioState.sourceFile = sourceFile;
    }

    public static void processLMCP(LMCPObject event, double time) {

        if (event instanceof SessionStatus) {
            SessionStatus ss = (SessionStatus) event;
            if (ss.getState() == SimulationStatusType.Reset) {
                clearData();
            }
            currentStatus = ss;
            time_sec = ss.getScenarioTime() / 1000d;
        } else {

            if (event instanceof AirVehicleState) {
                AirVehicleState avs = (AirVehicleState) event;
                aircraftStates.put(avs.getID(), avs);
            } else if (event instanceof AirVehicleConfiguration) {
                AirVehicleConfiguration avc = (AirVehicleConfiguration) event;
                aircraftConfigs.put(avc.getID(), avc);
                if (!aircraftOrder.contains(avc.getID())) {
                    aircraftOrder.add(avc.getID());
                }
            } else if (event instanceof AbstractZone) {
                AbstractZone z = (AbstractZone) event;
                zones.put(z.getZoneID(), z);
            } else if (event instanceof Task) {
                Task t = (Task) event;
                tasks.put(t.getTaskID(), t);
            } else if (event instanceof EntityConfiguration) {
                EntityConfiguration ec = (EntityConfiguration) event;
                entityConfigs.put(ec.getID(), ec);
            } else if (event instanceof EntityState) {
                EntityState es = (EntityState) event;
                entityStates.put(es.getID(), es);
            }

            scenarioHistory.add(new EventWrapper(time, (LMCPObject) event));

        }
    }

    public static void clearData() {
        aircraftConfigs.clear();
        aircraftStates.clear();
        zones.clear();
        tasks.clear();
        entityConfigs.clear();
        entityStates.clear();
        aircraftOrder.clear();

        scenarioHistory.clear();

        sourceFile = null;
        scenarioElement = null;
    }

    /**
     * Returns the config for an aircraft with the given ID, or null if none is
     * found.
     */
    public static AirVehicleConfiguration getAirVehicleConfig(Long refId) {
        return aircraftConfigs.get(refId);
    }

    public static List<AirVehicleConfiguration> getAllAirVehicleConfigs() {
        return new ArrayList<>(aircraftConfigs.values());
    }

    /**
     * Returns the latest state for an aircraft with the given ID, or null if
     * none is found.
     */
    public static AirVehicleState getAirVehicleState(Long refId) {
        return aircraftStates.get(refId);
    }
    
    /**
     * Returns the state for an aircraft with the given ID closest in time, 
     * without being greater, to that requested. Returns null if there is no state available
     * at the given time.
     */
    public static AirVehicleState getAirVehicleState(Long refId, double scenarioTime) {

        int index = 0;
        for (int i=0; i<scenarioHistory.size(); i++) {
            EventWrapper ew = scenarioHistory.get(i);
            if (ew.time >= scenarioTime) {
                index = i;
                break;
            }
        }
        for (int i=index; i>=0; i--) {
            EventWrapper ew = scenarioHistory.get(i);
            if (ew.event instanceof AirVehicleState) {
                AirVehicleState avs = (AirVehicleState) ew.event;
                if (avs.getID() == refId) {
                    return avs;
                }
            } 
        }
        return null;
    }

    /**
     * Returns the latest state for all aircraft.
     */
    public static List<AirVehicleState> getAllAirVehicleStates() {
        return new ArrayList<>(aircraftStates.values());
    }

    /**
     * returns the task with the given ID, or null if none is found.
     */
    public static Task getTask(Long refId) {
        return tasks.get(refId);
    }

    public static List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    /**
     * returns the zone with the given ID, or null if none is found.
     */
    public static AbstractZone getZone(Long refId) {
        return zones.get(refId);
    }

    public static List<AbstractZone> getAllZones() {
        return new ArrayList<>(zones.values());
    }

    /**
     * Returns the config for an entity with the given ID, or null if none is
     * found.
     */
    public static EntityConfiguration getEntityConfig(Long refId) {
        return entityConfigs.get(refId);
    }

    public static List<EntityConfiguration> getAllEntityConfigs() {
        return new ArrayList<>(entityConfigs.values());
    }

    /**
     * Returns the latest state for an entity with the given ID, or null if none
     * is found.
     */
    public static EntityState getEntityState(Long refId) {
        return entityStates.get(refId);
    }

    public static List<EntityState> getAllEntityStates() {
        return new ArrayList<>(entityStates.values());
    }
    
    /**
     * Returns the state for an entity with the given ID closest in time, 
     * without being greater, to that requested. Returns null if there is no state available
     * at the given time.
     */
    public static EntityState getAirEntityState(Long refId, double scenarioTime) {

        int index = 0;
        for (int i=0; i<scenarioHistory.size(); i++) {
            EventWrapper ew = scenarioHistory.get(i);
            if (ew.time >= scenarioTime) {
                index = i;
                break;
            }
        }
        for (int i=index; i>=0; i--) {
            EventWrapper ew = scenarioHistory.get(i);
            if (ew.event instanceof EntityState) {
                EntityState es = (EntityState) ew.event;
                if (es.getID() == refId) {
                    return es;
                }
            } 
        }
        return null;
    }

    /**
     * Returns a list of aircraft IDs, in the order they were introduced in the
     * scenario. This is helpful for building GUI tools, setting colors, etc.
     *
     * @return a list of aircraft IDs, in the order they were introduced in the
     * scenario.
     */
    public static List<Long> getAirVehicleOrder() {
        return Collections.unmodifiableList(aircraftOrder);
    }

//    // methods to access events in the scenario
//    /**
//     * Returns all LMCPObject events (except SessionStatus) that have occurred.
//     * Warning: this may return a very large list, and may exceed memory
//     *
//     * @return a list of events
//     */
//    public static List<LMCPObject> getAllEvents() {
//        List<LMCPObject> retList = new ArrayList<>();
//        for (EventWrapper ew : scenarioHistory) {
//            retList.add(ew.event);
//        }
//        return retList;
//    }

    /**
     * Returns a view of the events. This shows LMCP objects with associated
     * time values in the correct order.
     *
     * @return a view of the internal event list.
     */
    public static List<EventWrapper> getEventList() {
        return scenarioHistory;
    }

    /**
     * Returns a list of all scenario events that occur between the requested
     * times
     *
     * @param starttime min time for the event (at or above)
     * @param endtime max time for the event (at or below)
     * @return a list of events
     */
    public static List<LMCPObject> getEventsByTime(double starttime, double endtime) {
        List<LMCPObject> retList = new ArrayList<>();
        for (EventWrapper ew : scenarioHistory) {
            if (ew.time < starttime) {
                continue;
            }
            if (ew.time > endtime) {
                break;
            }
            retList.add(ew.event);
        }
        return retList;
    }

    /**
     * Returns a list of all scenario events that are of the requested type
     * (exact class or a subclass) requested time
     * @param  type class type to obtain.  This will include all subtypes as well.
     * @return a list of events
     */
    public static List<? extends LMCPObject> getAllEvents(Class<?> type) {
        List<LMCPObject> retList = new ArrayList<>();
        for (EventWrapper ew : scenarioHistory) {
            if (type.isInstance(ew.event)) {
                retList.add(ew.event);
            }
        }
        return retList;
    }

    /**
     * Returns the full XML element that was used to configure the scenario.
     */
    public static Element getScenario() {
        return scenarioElement;
    }

    /**
     * Returns the source file for the scenario, or null if none was set.
     */
    public static File getSourceFile() {
        return sourceFile;
    }

    /**
     * Sets the source file for the scenario. Note that this does not set the
     * scenario element. That should be set via
     * {@link #setScenario(avtas.xml.Element, java.io.File)} or 
     * {@link #setScenarioElement(avtas.xml.Element)}.
     */
    public static void setSourceFile(File sourceFile) {
        ScenarioState.sourceFile = sourceFile;
    }

    /**
     * Sets the source element for the scenario. Note that this does not set the
     * scenario file. That should be set via
     * {@link #setScenario(avtas.xml.Element, java.io.File)} or
     * {@link #setSourceFile(java.io.File)}.
     */
    public static void setScenarioElement(Element scenarioElement) {
        ScenarioState.scenarioElement = scenarioElement;
    }
    
    /**
     * returns the current scenario time, in seconds. 
     */
    public static double getTime() {
        return time_sec;
    }
    
    /**
     * Sets the current scenario time in seconds.
     * @param time_sec 
     */
    public static void setTime(double time_sec) {
        ScenarioState.time_sec = time_sec;
    }
    
    /**
     * Returns the last recorded status for the session.  Can be null.
     */
    public SessionStatus getCurrentStatus() {
        return currentStatus;
    }
    
    
    

    /**
     * A class that wraps an LMCP Event and the time at which the event occurred
     * in the simulation.
     */
    public static class EventWrapper implements Serializable {

        public LMCPObject event;
        public double time;

        public EventWrapper(double time, LMCPObject event) {
            this.event = event;
            this.time = time;
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */