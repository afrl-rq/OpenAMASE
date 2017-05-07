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
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.CameraAction;
import afrl.cmasi.CameraConfiguration;
import afrl.cmasi.GimbalConfiguration;
import afrl.cmasi.GimbalStareAction;
import afrl.cmasi.LoiterAction;
import afrl.cmasi.LoiterDirection;
import afrl.cmasi.LoiterType;
import afrl.cmasi.PointSearchTask;
import afrl.cmasi.Task;
import afrl.cmasi.VehicleActionCommand;
import avtas.app.UserExceptions;
import avtas.amase.AmasePlugin;
import avtas.amase.scenario.ScenarioEvent;
import avtas.amase.scenario.ScenarioState;
import avtas.amase.util.CmasiNavUtils;
import avtas.amase.util.CmasiUtils;
import avtas.app.AppEventManager;
//import avtas.automation.support.PlanningUtils;
import avtas.util.NavUtils;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * An example plugin that assigns tasks to aircraft based on distance to the 
 * task and capabilities of the aircraft.
 * 
 * @author AFRL/RQQD
 */
public class TaskAllocator extends AmasePlugin {

    JPanel panel;
    JButton assignTaskButton;

    public TaskAllocator() {
        setPluginName("Simple Task Allocator");
        setupGui();
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof ScenarioEvent) {
            initScenario((ScenarioEvent) event);
        }
        else if (event instanceof PointSearchTask) {
            assignTaskButton.setEnabled(true);
        }
    }

    protected void initScenario(ScenarioEvent scenarioEvent) {
        System.out.println("New Scenario loaded.");
        assignTaskButton.setEnabled(false);
    }

    @Override
    public Component getGui() {
        return panel;
    }

    protected void setupGui() {
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        assignTaskButton = new JButton("Assign Task");
        panel.add(assignTaskButton);

        // set the button to be disabled until a task is loaded in the scenario
        assignTaskButton.setEnabled(false);

        assignTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignTask();
            }
        });
    }

    private void assignTask() {

        // get a list of the current state of all aircraft in the scenario
        List<AirVehicleState> states = ScenarioState.getAllAirVehicleStates();


        List<Long> eligibleAircraft = new ArrayList<>();
        for (AirVehicleState avs : states) {
            eligibleAircraft.add(avs.getID());
        }


        List<Task> tasks = ScenarioState.getAllTasks();

        // a map of distance to target
        TreeMap<Double, Long> distanceMap = new TreeMap<>();

        for (Task task : tasks) {
            if (task instanceof PointSearchTask) {
                boolean assigned = false;
                distanceMap.clear();
                while (!assigned) {
                    PointSearchTask pointSearch = (PointSearchTask) task;

                    for (long id : eligibleAircraft) {
                        AirVehicleState avs = ScenarioState.getAirVehicleState(id);
                        if (avs != null) {
                            double distance = CmasiNavUtils.distance(pointSearch.getSearchLocation(), avs.getLocation());
                            distanceMap.put(distance, id);
                        }
                    }
                    
                    // if no aircraft are left, then we can't make any more assignments.
                    if (eligibleAircraft.isEmpty()) {
                        return;
                    }

                    // tree maps order naturally least to greatest.  Grab the least (smallest distance) entry
                    long assignedAircraft = distanceMap.firstEntry().getValue();

                    // attempt to assign the task to the aircraft
                    boolean pass = setupAction(assignedAircraft, pointSearch);

                    // if the task was assigned, then remove the aircraft from the list of eligible candiates
                    if (pass) {
                        eligibleAircraft.remove(assignedAircraft);
                        assigned = true;
                    }
                }
            }
        }
    }

    protected boolean setupAction(long vehicleId, PointSearchTask task) {
        AirVehicleConfiguration avc = ScenarioState.getAirVehicleConfig(vehicleId);


        // inform the user is errors occur.  Amase has a built-in system for notifying the user.

        if (avc == null) {
            UserExceptions.showWarning("Cannot command task " + task.getTaskID() + " to aircraft " + vehicleId);
            return false;
        }

        if (avc.getNominalFlightProfile() == null) {
            UserExceptions.showWarning("Aircraft " + vehicleId + " Has no nominal flight profile.");
            return false;
        }

        // we are going to loiter around the point and stare at it.  Set up the radius of the 
        // loiter based on the aircraft capabilities.
        double bank = Math.toRadians(avc.getNominalFlightProfile().getMaxBankAngle());
        float airspeed = avc.getNominalFlightProfile().getAirspeed();

        // This is a standard equation for a level coordinated turn.  The 1.5 factor makes the turn 
        // large enough to ensure a gental loiter around the target
        double radius = 1.5 * airspeed * airspeed / (NavUtils.getG() * Math.tan(bank));

        // Now we setup the commands.  A loiter action places the loiter around the target.  
        // See the CMASI documentation for the field definitions.
        LoiterAction action = new LoiterAction();
        action.setLocation(task.getSearchLocation());
        action.setAirspeed(airspeed);
        action.setRadius((float) radius);
        action.setLoiterType(LoiterType.Circular);
        action.setDirection(LoiterDirection.CounterClockwise);
        action.setDuration(-1);

        // we also have to set up an action to point the camera at the task.  
        // iterating through the payload list will find us a camera that is valid fot the task
        CameraConfiguration camera = null;
        List<CameraConfiguration> cameraList = CmasiUtils.getPayloadsByType(CameraConfiguration.class, avc.getPayloadConfigurationList());
        for (CameraConfiguration cc : cameraList) {
            if (task.getDesiredWavelengthBands().contains(cc.getSupportedWavelengthBand())) {
                camera = cc;
                break;
            }
        }
        
        // if no valud camera exists, then display a warning to the user
        if (camera == null) {
            UserExceptions.showWarning("Cannot command task " + task.getTaskID() + " to aircraft " + vehicleId + ". No valid camera.");
            return false;
        }


        GimbalConfiguration gimbal = CmasiUtils.getGimbalForPayload(camera.getPayloadID(), avc);
        if (gimbal == null) {
            UserExceptions.showWarning("Cannot command task " + task.getTaskID() + " to aircraft " + vehicleId);
            return false;
        }
        GimbalStareAction stareAction = new GimbalStareAction();
        stareAction.setPayloadID(gimbal.getPayloadID());
        stareAction.setStarepoint(task.getSearchLocation());

        CameraAction cameraAction = new CameraAction();
        cameraAction.setPayloadID(camera.getPayloadID());

        // zoom the camera all the way out
        cameraAction.setHorizontalFieldOfView((float) camera.getMaxHorizontalFieldOfView());


        // now that we have the commands setup, we add them all to a single VehicleActionCommand, that is dispatched to
        // the entire simulation.  The Aircraft (or model of the aircraft) will receive the command and execute it.
        VehicleActionCommand command = new VehicleActionCommand();
        command.setVehicleID(vehicleId);
        command.getVehicleActionList().add(action);
        command.getVehicleActionList().add(cameraAction);
        command.getVehicleActionList().add(stareAction);

        AppEventManager.getDefaultEventManager().fireEvent(command);
        return true;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */