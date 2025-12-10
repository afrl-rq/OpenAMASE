// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.cmasi;


import avtas.lmcp.LMCPObject;
import java.util.Arrays;

public class SeriesEnum implements avtas.lmcp.LMCPEnum {
 
    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static String[] name_list = new String[]{
        "AbstractGeometry",
        "KeyValuePair",
        "Location3D",
        "PayloadAction",
        "PayloadConfiguration",
        "PayloadState",
        "VehicleAction",
        "Task",
        "SearchTask",
        "AbstractZone",
        "EntityConfiguration",
        "FlightProfile",
        "AirVehicleConfiguration",
        "EntityState",
        "AirVehicleState",
        "Wedge",
        "AreaSearchTask",
        "CameraAction",
        "CameraConfiguration",
        "GimballedPayloadState",
        "CameraState",
        "Circle",
        "GimbalAngleAction",
        "GimbalConfiguration",
        "GimbalScanAction",
        "GimbalStareAction",
        "GimbalState",
        "GoToWaypointAction",
        "KeepInZone",
        "KeepOutZone",
        "LineSearchTask",
        "NavigationAction",
        "LoiterAction",
        "LoiterTask",
        "Waypoint",
        "MissionCommand",
        "MustFlyTask",
        "OperatorSignal",
        "OperatingRegion",
        "AutomationRequest",
        "PointSearchTask",
        "Polygon",
        "Rectangle",
        "RemoveTasks",
        "ServiceStatus",
        "SessionStatus",
        "VehicleActionCommand",
        "VideoStreamAction",
        "VideoStreamConfiguration",
        "VideoStreamState",
        "AutomationResponse",
        "RemoveZones",
        "RemoveEntities",
        "FlightDirectorAction",
        "WeatherReport",
        "FollowPathCommand",
        "PathWaypoint",
        "StopMovementAction",
        "WaypointTransfer",
        "PayloadStowAction"
    };

    public long getSeriesNameAsLong() { return SERIES_NAME_ID; }

    public String getSeriesName() { return SERIES_NAME; }

    public int getSeriesVersion() { return SERIES_VERSION; }

    public String getName(long type) {
        switch ((int) type) {
            case 1: return "AbstractGeometry";
            case 2: return "KeyValuePair";
            case 3: return "Location3D";
            case 4: return "PayloadAction";
            case 5: return "PayloadConfiguration";
            case 6: return "PayloadState";
            case 7: return "VehicleAction";
            case 8: return "Task";
            case 9: return "SearchTask";
            case 10: return "AbstractZone";
            case 11: return "EntityConfiguration";
            case 12: return "FlightProfile";
            case 13: return "AirVehicleConfiguration";
            case 14: return "EntityState";
            case 15: return "AirVehicleState";
            case 16: return "Wedge";
            case 17: return "AreaSearchTask";
            case 18: return "CameraAction";
            case 19: return "CameraConfiguration";
            case 20: return "GimballedPayloadState";
            case 21: return "CameraState";
            case 22: return "Circle";
            case 23: return "GimbalAngleAction";
            case 24: return "GimbalConfiguration";
            case 25: return "GimbalScanAction";
            case 26: return "GimbalStareAction";
            case 27: return "GimbalState";
            case 28: return "GoToWaypointAction";
            case 29: return "KeepInZone";
            case 30: return "KeepOutZone";
            case 31: return "LineSearchTask";
            case 32: return "NavigationAction";
            case 33: return "LoiterAction";
            case 34: return "LoiterTask";
            case 35: return "Waypoint";
            case 36: return "MissionCommand";
            case 37: return "MustFlyTask";
            case 38: return "OperatorSignal";
            case 39: return "OperatingRegion";
            case 40: return "AutomationRequest";
            case 41: return "PointSearchTask";
            case 42: return "Polygon";
            case 43: return "Rectangle";
            case 44: return "RemoveTasks";
            case 45: return "ServiceStatus";
            case 46: return "SessionStatus";
            case 47: return "VehicleActionCommand";
            case 48: return "VideoStreamAction";
            case 49: return "VideoStreamConfiguration";
            case 50: return "VideoStreamState";
            case 51: return "AutomationResponse";
            case 52: return "RemoveZones";
            case 53: return "RemoveEntities";
            case 54: return "FlightDirectorAction";
            case 55: return "WeatherReport";
            case 56: return "FollowPathCommand";
            case 57: return "PathWaypoint";
            case 58: return "StopMovementAction";
            case 59: return "WaypointTransfer";
            case 60: return "PayloadStowAction";

        }
        
        return "";
    }

    public long getType(String name) {
       if ( name.equals("AbstractGeometry")) return 1;
       if ( name.equals("KeyValuePair")) return 2;
       if ( name.equals("Location3D")) return 3;
       if ( name.equals("PayloadAction")) return 4;
       if ( name.equals("PayloadConfiguration")) return 5;
       if ( name.equals("PayloadState")) return 6;
       if ( name.equals("VehicleAction")) return 7;
       if ( name.equals("Task")) return 8;
       if ( name.equals("SearchTask")) return 9;
       if ( name.equals("AbstractZone")) return 10;
       if ( name.equals("EntityConfiguration")) return 11;
       if ( name.equals("FlightProfile")) return 12;
       if ( name.equals("AirVehicleConfiguration")) return 13;
       if ( name.equals("EntityState")) return 14;
       if ( name.equals("AirVehicleState")) return 15;
       if ( name.equals("Wedge")) return 16;
       if ( name.equals("AreaSearchTask")) return 17;
       if ( name.equals("CameraAction")) return 18;
       if ( name.equals("CameraConfiguration")) return 19;
       if ( name.equals("GimballedPayloadState")) return 20;
       if ( name.equals("CameraState")) return 21;
       if ( name.equals("Circle")) return 22;
       if ( name.equals("GimbalAngleAction")) return 23;
       if ( name.equals("GimbalConfiguration")) return 24;
       if ( name.equals("GimbalScanAction")) return 25;
       if ( name.equals("GimbalStareAction")) return 26;
       if ( name.equals("GimbalState")) return 27;
       if ( name.equals("GoToWaypointAction")) return 28;
       if ( name.equals("KeepInZone")) return 29;
       if ( name.equals("KeepOutZone")) return 30;
       if ( name.equals("LineSearchTask")) return 31;
       if ( name.equals("NavigationAction")) return 32;
       if ( name.equals("LoiterAction")) return 33;
       if ( name.equals("LoiterTask")) return 34;
       if ( name.equals("Waypoint")) return 35;
       if ( name.equals("MissionCommand")) return 36;
       if ( name.equals("MustFlyTask")) return 37;
       if ( name.equals("OperatorSignal")) return 38;
       if ( name.equals("OperatingRegion")) return 39;
       if ( name.equals("AutomationRequest")) return 40;
       if ( name.equals("PointSearchTask")) return 41;
       if ( name.equals("Polygon")) return 42;
       if ( name.equals("Rectangle")) return 43;
       if ( name.equals("RemoveTasks")) return 44;
       if ( name.equals("ServiceStatus")) return 45;
       if ( name.equals("SessionStatus")) return 46;
       if ( name.equals("VehicleActionCommand")) return 47;
       if ( name.equals("VideoStreamAction")) return 48;
       if ( name.equals("VideoStreamConfiguration")) return 49;
       if ( name.equals("VideoStreamState")) return 50;
       if ( name.equals("AutomationResponse")) return 51;
       if ( name.equals("RemoveZones")) return 52;
       if ( name.equals("RemoveEntities")) return 53;
       if ( name.equals("FlightDirectorAction")) return 54;
       if ( name.equals("WeatherReport")) return 55;
       if ( name.equals("FollowPathCommand")) return 56;
       if ( name.equals("PathWaypoint")) return 57;
       if ( name.equals("StopMovementAction")) return 58;
       if ( name.equals("WaypointTransfer")) return 59;
       if ( name.equals("PayloadStowAction")) return 60;

       
       return -1;
    }

    public LMCPObject getInstance(long type) {
        switch ((int) type) {
            case 1: return new AbstractGeometry();
            case 2: return new KeyValuePair();
            case 3: return new Location3D();
            case 4: return new PayloadAction();
            case 5: return new PayloadConfiguration();
            case 6: return new PayloadState();
            case 7: return new VehicleAction();
            case 8: return new Task();
            case 9: return new SearchTask();
            case 10: return new AbstractZone();
            case 11: return new EntityConfiguration();
            case 12: return new FlightProfile();
            case 13: return new AirVehicleConfiguration();
            case 14: return new EntityState();
            case 15: return new AirVehicleState();
            case 16: return new Wedge();
            case 17: return new AreaSearchTask();
            case 18: return new CameraAction();
            case 19: return new CameraConfiguration();
            case 20: return new GimballedPayloadState();
            case 21: return new CameraState();
            case 22: return new Circle();
            case 23: return new GimbalAngleAction();
            case 24: return new GimbalConfiguration();
            case 25: return new GimbalScanAction();
            case 26: return new GimbalStareAction();
            case 27: return new GimbalState();
            case 28: return new GoToWaypointAction();
            case 29: return new KeepInZone();
            case 30: return new KeepOutZone();
            case 31: return new LineSearchTask();
            case 32: return new NavigationAction();
            case 33: return new LoiterAction();
            case 34: return new LoiterTask();
            case 35: return new Waypoint();
            case 36: return new MissionCommand();
            case 37: return new MustFlyTask();
            case 38: return new OperatorSignal();
            case 39: return new OperatingRegion();
            case 40: return new AutomationRequest();
            case 41: return new PointSearchTask();
            case 42: return new Polygon();
            case 43: return new Rectangle();
            case 44: return new RemoveTasks();
            case 45: return new ServiceStatus();
            case 46: return new SessionStatus();
            case 47: return new VehicleActionCommand();
            case 48: return new VideoStreamAction();
            case 49: return new VideoStreamConfiguration();
            case 50: return new VideoStreamState();
            case 51: return new AutomationResponse();
            case 52: return new RemoveZones();
            case 53: return new RemoveEntities();
            case 54: return new FlightDirectorAction();
            case 55: return new WeatherReport();
            case 56: return new FollowPathCommand();
            case 57: return new PathWaypoint();
            case 58: return new StopMovementAction();
            case 59: return new WaypointTransfer();
            case 60: return new PayloadStowAction();

        }

        return null;
    }

    public java.util.Collection<String> getAllTypes() {
        return Arrays.asList(name_list);
    }



}
