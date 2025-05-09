// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package test;

import avtas.lmcp.LMCPObject;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Connects to the simulation and sends a fake mission command to every UAV that is requested in the plan request.
 */
public class TestClient extends Thread {

    /** simulation TCP port to connect to */
    private static int port = 11041;
    /** address of the server */
    private static String host = "localhost";

    public TestClient() {
    }

    @Override
    public void run() {
        try {
            // connect to the server
            Socket socket = connect(host, port);
            sendMessages(socket.getOutputStream());
            while(true) {
                Thread.sleep(1000);
            }

        } catch (Exception ex) {
            Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessages(OutputStream out) throws Exception {
        LMCPObject o = null;
         o = new larcfm.DAIDALUS.GroundHeadingInterval();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new larcfm.DAIDALUS.GroundSpeedInterval();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new larcfm.DAIDALUS.VerticalSpeedInterval();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new larcfm.DAIDALUS.AltitudeInterval();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new larcfm.DAIDALUS.GroundHeadingRecoveryInterval();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new larcfm.DAIDALUS.GroundSpeedRecoveryInterval();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new larcfm.DAIDALUS.VerticalSpeedRecoveryInterval();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new larcfm.DAIDALUS.AltitudeRecoveryInterval();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new larcfm.DAIDALUS.WellClearViolationIntervals();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new larcfm.DAIDALUS.DAIDALUSConfiguration();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.AssignmentCoordinatorTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.RendezvousTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.PlanningState();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.AssignmentCoordination();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.CoordinatedAutomationRequest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.TaskAutomationRequest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.TaskAutomationResponse();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.UniqueAutomationRequest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.UniqueAutomationResponse();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.SensorFootprintRequests();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.FootprintRequest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.SensorFootprint();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.SensorFootprintResponse();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.TaskImplementationRequest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.TaskImplementationResponse();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.AssignmentCostMatrix();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.TaskOptionCost();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.TaskAssignment();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.TaskAssignmentSummary();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.TaskOption();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.TaskPlanOptions();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.TaskPause();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.TaskResume();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.TaskProgress();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.TaskProgressRequest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.TaskInitialized();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.TaskActive();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.TaskComplete();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.task.CancelTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.AbstractGeometry();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.KeyValuePair();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.Location3D();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.PayloadAction();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.PayloadConfiguration();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.PayloadState();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.VehicleAction();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.Task();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.SearchTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.AbstractZone();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.EntityConfiguration();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.FlightProfile();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.AirVehicleConfiguration();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.EntityState();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.AirVehicleState();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.Wedge();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.AreaSearchTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.CameraAction();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.CameraConfiguration();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.GimballedPayloadState();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.CameraState();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.Circle();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.GimbalAngleAction();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.GimbalConfiguration();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.GimbalScanAction();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.GimbalStareAction();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.GimbalState();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.GoToWaypointAction();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.KeepInZone();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.KeepOutZone();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.LineSearchTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.NavigationAction();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.LoiterAction();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.LoiterTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.Waypoint();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.MissionCommand();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.MustFlyTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.OperatorSignal();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.OperatingRegion();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.AutomationRequest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.PointSearchTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.Polygon();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.Rectangle();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.RemoveTasks();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.ServiceStatus();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.SessionStatus();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.VehicleActionCommand();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.VideoStreamAction();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.VideoStreamConfiguration();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.VideoStreamState();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.AutomationResponse();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.RemoveZones();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.RemoveEntities();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.FlightDirectorAction();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.WeatherReport();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.FollowPathCommand();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.PathWaypoint();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.StopMovementAction();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.WaypointTransfer();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.PayloadStowAction();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.vehicles.GroundVehicleConfiguration();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.vehicles.GroundVehicleState();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.vehicles.SurfaceVehicleConfiguration();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.vehicles.SurfaceVehicleState();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.vehicles.StationarySensorConfiguration();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.vehicles.StationarySensorState();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.ZoneVertex();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.Position2D();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.ZoneViolation();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.ActiveZoneViolation();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.ImminentZoneViolation();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.ProcessedZone();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.PowerConfiguration();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.RadioConfiguration();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.RadioTowerConfiguration();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.RadioState();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.RadioTowerState();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.ImpactPayloadConfiguration();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.DeployImpactPayload();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.PowerPlantState();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.BatchRoutePlanRequest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.BatchRoutePlanResponse();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.TaskTimingPair();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.BatchSummaryRequest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.BatchSummaryResponse();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.TaskSummary();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.VehicleSummary();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.SpeedAltPair();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.ImpactAutomationRequest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.ImpactAutomationResponse();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.PointOfInterest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.LineOfInterest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.AreaOfInterest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.ImpactPointSearchTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.PatternSearchTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.AngledAreaSearchTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.ImpactLineSearchTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.WatchTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.MultiVehicleWatchTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.CommRelayTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.CordonTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.BlockadeTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.EscortTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.ConfigurationRequest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.WaterReport();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.WaterZone();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.impact.PayloadDropTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.route.GraphNode();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.route.GraphEdge();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.route.GraphRegion();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.route.RouteConstraints();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.route.RouteRequest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.route.RoutePlanRequest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.route.RoutePlan();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.route.RoutePlanResponse();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.route.RouteResponse();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.route.EgressRouteRequest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.route.EgressRouteResponse();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.route.RoadPointsConstraints();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.route.RoadPointsRequest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.route.RoadPointsResponse();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.VideoRecord();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.StartupComplete();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.CreateNewService();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.KillService();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.IncrementWaypoint();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.SafeHeadingAction();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.EntityLocation();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.BandwidthTest();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.BandwidthReceiveReport();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.SubTaskExecution();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.SubTaskAssignment();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.AutopilotKeepAlive();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.OnboardStatusReport();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.EntityJoin();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.EntityExit();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.SimulationTimeStepAcknowledgement();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new uxas.messages.uxnative.SpeedOverrideAction();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.perceive.EntityPerception();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.perceive.TrackEntityAction();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
          o = new afrl.cmasi.perceive.TrackEntityTask();
         out.write(avtas.lmcp.LMCPFactory.packMessage(o, true));
 
    }


    /** tries to connect to the server.  If there is a problem (such as the server not running yet) it
     *  pauses, then tries again.  If the server quits and restarts, this method is called by the thread
     *  in order to re-establish communication.
     * @param host
     * @param port
     * @return
     */
    public Socket connect(String host, int port) {
        Socket socket = null;
        try {
            socket = new Socket(host, port);
        } catch (UnknownHostException ex) {
            System.err.println("Host Unknown. Quitting");
            System.exit(0);
        } catch (IOException ex) {
            System.err.println("Could not Connect to " + host + ":" + port + ".  Trying again...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex1) {
                Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return connect(host, port);
        }
        System.out.println("Connected to " + host + ":" + port);
        return socket;
    }

    public static void main(String[] args) {
        new TestClient().start();
    }
}
