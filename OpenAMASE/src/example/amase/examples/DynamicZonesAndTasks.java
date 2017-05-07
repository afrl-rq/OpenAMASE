// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package amase.examples;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AltitudeType;
import afrl.cmasi.AreaSearchTask;
import afrl.cmasi.Circle;
import afrl.cmasi.KeepOutZone;
import afrl.cmasi.Location3D;
import afrl.cmasi.Rectangle;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import afrl.cmasi.ZoneAvoidanceType;
import avtas.amase.AmasePlugin;

/**
 * Demonstrates the injection of CMASI-based objects into a scenario based on
 * events in the scenario.
 *
 * @author AFRL/RQQD
 */
public class DynamicZonesAndTasks extends AmasePlugin {

    boolean zoneSent = false;

    public DynamicZonesAndTasks() {
        setPluginName("Dynamic Zones and Tasks");
    }

    /**
     * This is a typical way to respond to events.  We filter 
     * based on type and handle types we are interested.  The 
     * java instanceof keyword is helpful here, because we can 
     * do a quick check on the type and only cast the value when 
     * we are interested in the event.
     * @param event 
     */
    @Override
    public void eventOccurred(Object event) {
        /**
         * first we check if this is a SessionStatus message. here we check
         * for two conditions. If the scenario is being reset then we note that
         * the zone has not been sent. If the time meets or exceeds the desired
         * time for publishing the zone, then we send it out. We then flip a
         * boolean value denoting that the zone has been sent.
         */
        if (event instanceof SessionStatus) {
            SessionStatus status = (SessionStatus) event;
            if (status.getState() == SimulationStatusType.Reset) {
                zoneSent = false;
            }
            else if (!zoneSent && status.getScenarioTime() > 5000) {
                sendZone();
                zoneSent = true;
            }
        }
        /*
         * Shows how we respond to an application event by creating a task. In
         * this case, we publish an AreaSearchTask in response to seeing an
         * aircraft ID of 3 created in the scenario. 
         */
        else if (event instanceof AirVehicleConfiguration) {
            AirVehicleConfiguration avc = (AirVehicleConfiguration) event;
            if (avc.getID() == 3) {
                sendTask();
            }
        }
    }


    @Override
    public void step(double timestep, double sim_time) {
        // if you want to use a higher resolution on time, then 
        // implement the logic to send the zone event here. 
        // AMASE generally sends SessionStatus messages out at a 
        // lower frequency than the timer updates.
    }

    void sendTask() {

        // create a basic search task
        AreaSearchTask task = new AreaSearchTask();
        task.setGroundSampleDistance(1.0f);
        task.setDwellTime(4);
        task.setLabel("My Test Task");
        task.setTaskID(1);

        // set an area for the search task.  CMASI supports Rectangle,
        // Polygon, and Circle.  
        Rectangle area = new Rectangle();

        area.setCenterPoint(new Location3D(1.513, -132.53, 0, AltitudeType.MSL));
        area.setWidth(1000);
        area.setHeight(2000);
        task.setSearchArea(area);

        // tells the plugin to fire the task to the rest of the application
        fireEvent(task);
    }

    void sendZone() {

        // create a new CMASI zone.  In this case, a KeepOutZone.
        KeepOutZone zone = new KeepOutZone();
        zone.setZoneID(1);
        zone.setLabel("Test Zone");
        // set this to note that the zone exists for airspace management.
        zone.setZoneType(ZoneAvoidanceType.Regulatory);

        Circle circle = new Circle();
        circle.setCenterPoint(new Location3D(1.51, -132.5, 0, AltitudeType.MSL));
        circle.setRadius(2000);
        zone.setBoundary(circle);

        // tells the plugin to fire the task to the rest of the application
        fireEvent(zone);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */