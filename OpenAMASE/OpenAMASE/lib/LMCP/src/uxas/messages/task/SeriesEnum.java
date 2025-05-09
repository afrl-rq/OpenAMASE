// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages.task;


import avtas.lmcp.LMCPObject;
import java.util.Arrays;

public class SeriesEnum implements avtas.lmcp.LMCPEnum {
 
    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static String[] name_list = new String[]{
        "AssignmentCoordinatorTask",
        "RendezvousTask",
        "PlanningState",
        "AssignmentCoordination",
        "CoordinatedAutomationRequest",
        "TaskAutomationRequest",
        "TaskAutomationResponse",
        "UniqueAutomationRequest",
        "UniqueAutomationResponse",
        "SensorFootprintRequests",
        "FootprintRequest",
        "SensorFootprint",
        "SensorFootprintResponse",
        "TaskImplementationRequest",
        "TaskImplementationResponse",
        "AssignmentCostMatrix",
        "TaskOptionCost",
        "TaskAssignment",
        "TaskAssignmentSummary",
        "TaskOption",
        "TaskPlanOptions",
        "TaskPause",
        "TaskResume",
        "TaskProgress",
        "TaskProgressRequest",
        "TaskInitialized",
        "TaskActive",
        "TaskComplete",
        "CancelTask"
    };

    public long getSeriesNameAsLong() { return SERIES_NAME_ID; }

    public String getSeriesName() { return SERIES_NAME; }

    public int getSeriesVersion() { return SERIES_VERSION; }

    public String getName(long type) {
        switch ((int) type) {
            case 1: return "AssignmentCoordinatorTask";
            case 2: return "RendezvousTask";
            case 3: return "PlanningState";
            case 4: return "AssignmentCoordination";
            case 5: return "CoordinatedAutomationRequest";
            case 6: return "TaskAutomationRequest";
            case 7: return "TaskAutomationResponse";
            case 8: return "UniqueAutomationRequest";
            case 9: return "UniqueAutomationResponse";
            case 10: return "SensorFootprintRequests";
            case 11: return "FootprintRequest";
            case 12: return "SensorFootprint";
            case 13: return "SensorFootprintResponse";
            case 14: return "TaskImplementationRequest";
            case 15: return "TaskImplementationResponse";
            case 16: return "AssignmentCostMatrix";
            case 17: return "TaskOptionCost";
            case 18: return "TaskAssignment";
            case 19: return "TaskAssignmentSummary";
            case 20: return "TaskOption";
            case 21: return "TaskPlanOptions";
            case 22: return "TaskPause";
            case 23: return "TaskResume";
            case 24: return "TaskProgress";
            case 25: return "TaskProgressRequest";
            case 26: return "TaskInitialized";
            case 27: return "TaskActive";
            case 28: return "TaskComplete";
            case 29: return "CancelTask";

        }
        
        return "";
    }

    public long getType(String name) {
       if ( name.equals("AssignmentCoordinatorTask")) return 1;
       if ( name.equals("RendezvousTask")) return 2;
       if ( name.equals("PlanningState")) return 3;
       if ( name.equals("AssignmentCoordination")) return 4;
       if ( name.equals("CoordinatedAutomationRequest")) return 5;
       if ( name.equals("TaskAutomationRequest")) return 6;
       if ( name.equals("TaskAutomationResponse")) return 7;
       if ( name.equals("UniqueAutomationRequest")) return 8;
       if ( name.equals("UniqueAutomationResponse")) return 9;
       if ( name.equals("SensorFootprintRequests")) return 10;
       if ( name.equals("FootprintRequest")) return 11;
       if ( name.equals("SensorFootprint")) return 12;
       if ( name.equals("SensorFootprintResponse")) return 13;
       if ( name.equals("TaskImplementationRequest")) return 14;
       if ( name.equals("TaskImplementationResponse")) return 15;
       if ( name.equals("AssignmentCostMatrix")) return 16;
       if ( name.equals("TaskOptionCost")) return 17;
       if ( name.equals("TaskAssignment")) return 18;
       if ( name.equals("TaskAssignmentSummary")) return 19;
       if ( name.equals("TaskOption")) return 20;
       if ( name.equals("TaskPlanOptions")) return 21;
       if ( name.equals("TaskPause")) return 22;
       if ( name.equals("TaskResume")) return 23;
       if ( name.equals("TaskProgress")) return 24;
       if ( name.equals("TaskProgressRequest")) return 25;
       if ( name.equals("TaskInitialized")) return 26;
       if ( name.equals("TaskActive")) return 27;
       if ( name.equals("TaskComplete")) return 28;
       if ( name.equals("CancelTask")) return 29;

       
       return -1;
    }

    public LMCPObject getInstance(long type) {
        switch ((int) type) {
            case 1: return new AssignmentCoordinatorTask();
            case 2: return new RendezvousTask();
            case 3: return new PlanningState();
            case 4: return new AssignmentCoordination();
            case 5: return new CoordinatedAutomationRequest();
            case 6: return new TaskAutomationRequest();
            case 7: return new TaskAutomationResponse();
            case 8: return new UniqueAutomationRequest();
            case 9: return new UniqueAutomationResponse();
            case 10: return new SensorFootprintRequests();
            case 11: return new FootprintRequest();
            case 12: return new SensorFootprint();
            case 13: return new SensorFootprintResponse();
            case 14: return new TaskImplementationRequest();
            case 15: return new TaskImplementationResponse();
            case 16: return new AssignmentCostMatrix();
            case 17: return new TaskOptionCost();
            case 18: return new TaskAssignment();
            case 19: return new TaskAssignmentSummary();
            case 20: return new TaskOption();
            case 21: return new TaskPlanOptions();
            case 22: return new TaskPause();
            case 23: return new TaskResume();
            case 24: return new TaskProgress();
            case 25: return new TaskProgressRequest();
            case 26: return new TaskInitialized();
            case 27: return new TaskActive();
            case 28: return new TaskComplete();
            case 29: return new CancelTask();

        }

        return null;
    }

    public java.util.Collection<String> getAllTypes() {
        return Arrays.asList(name_list);
    }



}
