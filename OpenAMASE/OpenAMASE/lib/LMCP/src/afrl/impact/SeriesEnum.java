// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.impact;


import avtas.lmcp.LMCPObject;
import java.util.Arrays;

public class SeriesEnum implements avtas.lmcp.LMCPEnum {
 
    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static String[] name_list = new String[]{
        "PowerConfiguration",
        "RadioConfiguration",
        "RadioTowerConfiguration",
        "RadioState",
        "RadioTowerState",
        "ImpactPayloadConfiguration",
        "DeployImpactPayload",
        "PowerPlantState",
        "BatchRoutePlanRequest",
        "BatchRoutePlanResponse",
        "TaskTimingPair",
        "BatchSummaryRequest",
        "BatchSummaryResponse",
        "TaskSummary",
        "VehicleSummary",
        "SpeedAltPair",
        "ImpactAutomationRequest",
        "ImpactAutomationResponse",
        "PointOfInterest",
        "LineOfInterest",
        "AreaOfInterest",
        "ImpactPointSearchTask",
        "PatternSearchTask",
        "AngledAreaSearchTask",
        "ImpactLineSearchTask",
        "WatchTask",
        "MultiVehicleWatchTask",
        "CommRelayTask",
        "CordonTask",
        "BlockadeTask",
        "EscortTask",
        "ConfigurationRequest",
        "WaterReport",
        "WaterZone",
        "PayloadDropTask"
    };

    public long getSeriesNameAsLong() { return SERIES_NAME_ID; }

    public String getSeriesName() { return SERIES_NAME; }

    public int getSeriesVersion() { return SERIES_VERSION; }

    public String getName(long type) {
        switch ((int) type) {
            case 1: return "PowerConfiguration";
            case 2: return "RadioConfiguration";
            case 3: return "RadioTowerConfiguration";
            case 4: return "RadioState";
            case 5: return "RadioTowerState";
            case 6: return "ImpactPayloadConfiguration";
            case 7: return "DeployImpactPayload";
            case 8: return "PowerPlantState";
            case 9: return "BatchRoutePlanRequest";
            case 10: return "BatchRoutePlanResponse";
            case 11: return "TaskTimingPair";
            case 12: return "BatchSummaryRequest";
            case 13: return "BatchSummaryResponse";
            case 14: return "TaskSummary";
            case 15: return "VehicleSummary";
            case 16: return "SpeedAltPair";
            case 17: return "ImpactAutomationRequest";
            case 18: return "ImpactAutomationResponse";
            case 19: return "PointOfInterest";
            case 20: return "LineOfInterest";
            case 21: return "AreaOfInterest";
            case 22: return "ImpactPointSearchTask";
            case 23: return "PatternSearchTask";
            case 24: return "AngledAreaSearchTask";
            case 25: return "ImpactLineSearchTask";
            case 26: return "WatchTask";
            case 27: return "MultiVehicleWatchTask";
            case 28: return "CommRelayTask";
            case 29: return "CordonTask";
            case 30: return "BlockadeTask";
            case 31: return "EscortTask";
            case 32: return "ConfigurationRequest";
            case 33: return "WaterReport";
            case 34: return "WaterZone";
            case 35: return "PayloadDropTask";

        }
        
        return "";
    }

    public long getType(String name) {
       if ( name.equals("PowerConfiguration")) return 1;
       if ( name.equals("RadioConfiguration")) return 2;
       if ( name.equals("RadioTowerConfiguration")) return 3;
       if ( name.equals("RadioState")) return 4;
       if ( name.equals("RadioTowerState")) return 5;
       if ( name.equals("ImpactPayloadConfiguration")) return 6;
       if ( name.equals("DeployImpactPayload")) return 7;
       if ( name.equals("PowerPlantState")) return 8;
       if ( name.equals("BatchRoutePlanRequest")) return 9;
       if ( name.equals("BatchRoutePlanResponse")) return 10;
       if ( name.equals("TaskTimingPair")) return 11;
       if ( name.equals("BatchSummaryRequest")) return 12;
       if ( name.equals("BatchSummaryResponse")) return 13;
       if ( name.equals("TaskSummary")) return 14;
       if ( name.equals("VehicleSummary")) return 15;
       if ( name.equals("SpeedAltPair")) return 16;
       if ( name.equals("ImpactAutomationRequest")) return 17;
       if ( name.equals("ImpactAutomationResponse")) return 18;
       if ( name.equals("PointOfInterest")) return 19;
       if ( name.equals("LineOfInterest")) return 20;
       if ( name.equals("AreaOfInterest")) return 21;
       if ( name.equals("ImpactPointSearchTask")) return 22;
       if ( name.equals("PatternSearchTask")) return 23;
       if ( name.equals("AngledAreaSearchTask")) return 24;
       if ( name.equals("ImpactLineSearchTask")) return 25;
       if ( name.equals("WatchTask")) return 26;
       if ( name.equals("MultiVehicleWatchTask")) return 27;
       if ( name.equals("CommRelayTask")) return 28;
       if ( name.equals("CordonTask")) return 29;
       if ( name.equals("BlockadeTask")) return 30;
       if ( name.equals("EscortTask")) return 31;
       if ( name.equals("ConfigurationRequest")) return 32;
       if ( name.equals("WaterReport")) return 33;
       if ( name.equals("WaterZone")) return 34;
       if ( name.equals("PayloadDropTask")) return 35;

       
       return -1;
    }

    public LMCPObject getInstance(long type) {
        switch ((int) type) {
            case 1: return new PowerConfiguration();
            case 2: return new RadioConfiguration();
            case 3: return new RadioTowerConfiguration();
            case 4: return new RadioState();
            case 5: return new RadioTowerState();
            case 6: return new ImpactPayloadConfiguration();
            case 7: return new DeployImpactPayload();
            case 8: return new PowerPlantState();
            case 9: return new BatchRoutePlanRequest();
            case 10: return new BatchRoutePlanResponse();
            case 11: return new TaskTimingPair();
            case 12: return new BatchSummaryRequest();
            case 13: return new BatchSummaryResponse();
            case 14: return new TaskSummary();
            case 15: return new VehicleSummary();
            case 16: return new SpeedAltPair();
            case 17: return new ImpactAutomationRequest();
            case 18: return new ImpactAutomationResponse();
            case 19: return new PointOfInterest();
            case 20: return new LineOfInterest();
            case 21: return new AreaOfInterest();
            case 22: return new ImpactPointSearchTask();
            case 23: return new PatternSearchTask();
            case 24: return new AngledAreaSearchTask();
            case 25: return new ImpactLineSearchTask();
            case 26: return new WatchTask();
            case 27: return new MultiVehicleWatchTask();
            case 28: return new CommRelayTask();
            case 29: return new CordonTask();
            case 30: return new BlockadeTask();
            case 31: return new EscortTask();
            case 32: return new ConfigurationRequest();
            case 33: return new WaterReport();
            case 34: return new WaterZone();
            case 35: return new PayloadDropTask();

        }

        return null;
    }

    public java.util.Collection<String> getAllTypes() {
        return Arrays.asList(name_list);
    }



}
