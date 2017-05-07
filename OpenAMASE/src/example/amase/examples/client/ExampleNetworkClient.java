// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package amase.examples.client;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.CameraAction;
import afrl.cmasi.CameraConfiguration;
import afrl.cmasi.GimbalConfiguration;
import afrl.cmasi.GimbalStareAction;
import afrl.cmasi.LoiterAction;
import afrl.cmasi.LoiterDirection;
import afrl.cmasi.LoiterType;
import afrl.cmasi.PayloadConfiguration;
import afrl.cmasi.PointSearchTask;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import afrl.cmasi.VehicleActionCommand;
import avtas.amase.util.CmasiNavUtils;
import avtas.amase.util.CmasiUtils;
//import avtas.amase.util.CmasiUtils;
//import avtas.automation.support.PlanningUtils;
import avtas.lmcp.LMCPFactory;
import avtas.lmcp.LMCPObject;
import avtas.util.NavUtils;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author AFRL/RQQD
 */
public class ExampleNetworkClient {

    // create some maps to store states and configurations that will be used 
    // in the task allocation method
    HashMap<Long, AirVehicleState> stateMap = new HashMap<>();
    HashMap<Long, AirVehicleConfiguration> configMap = new HashMap<>();

    boolean running = true;
    Socket socket = null;
    ListenThread listenThread = null;

    public void messageReceived(LMCPObject object) {
        if (object instanceof AirVehicleConfiguration) {
            AirVehicleConfiguration avc = (AirVehicleConfiguration) object;
            configMap.put(avc.getID(), avc);
        } else if (object instanceof AirVehicleState) {
            AirVehicleState avs = (AirVehicleState) object;
            stateMap.put(avs.getID(), avs);
        } else if (object instanceof SessionStatus) {
            SessionStatus ss = (SessionStatus) object;
            if (ss.getState() == SimulationStatusType.Reset) {
                stateMap.clear();
                configMap.clear();
            }
        } else if (object instanceof PointSearchTask) {
            allocateTask((PointSearchTask) object);
        }
    }

    protected void allocateTask(PointSearchTask pointSearch) {

        List<Long> eligibleAircraft = new ArrayList<>();
        for (AirVehicleState avs : stateMap.values()) {
            eligibleAircraft.add(avs.getID());
        }

        // a map of distance to target
        TreeMap<Double, Long> distanceMap = new TreeMap<>();

        boolean assigned = false;
        distanceMap.clear();
        while (!assigned) {

            for (long id : eligibleAircraft) {
                AirVehicleState avs = stateMap.get(id);
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

    protected boolean setupAction(long vehicleId, PointSearchTask task) {
        AirVehicleConfiguration avc = configMap.get(vehicleId);
        AirVehicleState avs = stateMap.get(vehicleId);

        // inform the user is errors occur.  Amase has a built-in system for notifying the user.
        if (avc == null || avs == null) {
            System.out.println("Cannot command task " + task.getTaskID() + " to aircraft " + vehicleId);
            return false;
        }

        if (avc.getNominalFlightProfile() == null) {
            System.out.println("Aircraft " + vehicleId + " Has no nominal flight profile.");
            return false;
        }

        // we are going to loiter around the point and stare at it.  Set up the radius of the 
        // loiter based on the aircraft capabilities.
        double bank = Math.toRadians(avc.getNominalFlightProfile().getMaxBankAngle());
        float airspeed = avc.getNominalFlightProfile().getAirspeed();

        // This is a standard equation for a level coordinated turn.  The 1.5 factor makes the turn 
        // large enough to ensure a gental loiter around the target
        double radius = 1.5 * airspeed * airspeed / (NavUtils.getG() * Math.tan(bank));
        
        // We used a VehicleActionCommand to send actions to the aircraft.
        VehicleActionCommand command = new VehicleActionCommand();
        command.setVehicleID(vehicleId);

        // A loiter action places the loiter around the target.  
        // See the CMASI documentation for the field definitions.
        // note the "clone" use.  This ensures that changes made to the 
        // location object do not affect the search location.
        LoiterAction loiterAction = new LoiterAction();
        loiterAction.setLocation(task.getSearchLocation().clone());
        
        // make sure we stay at the current alitude
        loiterAction.getLocation().setAltitude(avs.getLocation().getAltitude());
        
        loiterAction.setAirspeed(airspeed);
        loiterAction.setRadius((float) radius);
        loiterAction.setLoiterType(LoiterType.Circular);
        loiterAction.setDirection(LoiterDirection.CounterClockwise);
        loiterAction.setDuration(-1);
        // add the loiter action to the list of actions sent.
        command.getVehicleActionList().add(loiterAction);

        // we also have to set up an action to point a camera at the task.  
        // here we use another utility class in AMASE to find all Gimbal payloads.  
        // since gimbals contain sensors.  Since this is a simple tutorial,
        // we will point all gimbals we find at the target.  (In a real system, you would perform
        // more sophisticated logic)
        for (PayloadConfiguration pc : avc.getPayloadConfigurationList()) {
            if (pc instanceof GimbalConfiguration) {
                GimbalConfiguration gc = (GimbalConfiguration) pc;
                GimbalStareAction stareAction = new GimbalStareAction();
                stareAction.setPayloadID(gc.getPayloadID());
                stareAction.setStarepoint(task.getSearchLocation());
                
                command.getVehicleActionList().add(stareAction);
            }
        }

        sendMessage(command);
        return true;
    }

    /**
     * Connects to an AMASE TCP server. Blocks until a connection is made.
     *
     * @param host host name of AMASE server
     * @param port port number for AMASE server
     */
    public void connect(String host, int port) {
        // first we try to connect a socket
        while (socket == null) {
            try {
                System.out.printf("Try to connect to %s:%d...", host, port);
                socket = new Socket(host, port);
            } catch (UnknownHostException ex) {
                Logger.getLogger(ExampleNetworkClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                try {
                    Thread.sleep(2000);
                    System.out.print("failed.\n");
                } catch (InterruptedException ex1) {
                    Logger.getLogger(ExampleNetworkClient.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        System.out.printf("connected\n");

        // once we succeed, we set up a listening thread on the socket
        listenThread = new ListenThread();
        listenThread.start();
    }

    /**
     * Sends an LMCP object to AMASE
     */
    public void sendMessage(LMCPObject message) {
        try {
            byte[] bytes = LMCPFactory.packMessage(message, true);
            socket.getOutputStream().write(bytes);
        } catch (Exception ex) {
            Logger.getLogger(ExampleNetworkClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class ListenThread extends Thread {

        @Override
        public void run() {
            while (running) {
                try {
                    // this is a blocking method.  As objects are available, they will
                    // be processed.
                    LMCPObject object = LMCPFactory.getObject(socket.getInputStream());

                    // tell the task allocator about the message
                    messageReceived(object);
                } catch (Exception ex) {
                    Logger.getLogger(ExampleNetworkClient.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        ExampleNetworkClient client = new ExampleNetworkClient();
        client.connect("localhost", 5555);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */