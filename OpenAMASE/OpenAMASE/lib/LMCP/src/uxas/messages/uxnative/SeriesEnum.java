// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages.uxnative;


import avtas.lmcp.LMCPObject;
import java.util.Arrays;

public class SeriesEnum implements avtas.lmcp.LMCPEnum {
 
    public static final String SERIES_NAME = "UXNATIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149751333668345413L;
    public static final int SERIES_VERSION = 9;


    private static String[] name_list = new String[]{
        "VideoRecord",
        "StartupComplete",
        "CreateNewService",
        "KillService",
        "IncrementWaypoint",
        "SafeHeadingAction",
        "EntityLocation",
        "BandwidthTest",
        "BandwidthReceiveReport",
        "SubTaskExecution",
        "SubTaskAssignment",
        "AutopilotKeepAlive",
        "OnboardStatusReport",
        "EntityJoin",
        "EntityExit",
        "SimulationTimeStepAcknowledgement",
        "SpeedOverrideAction"
    };

    public long getSeriesNameAsLong() { return SERIES_NAME_ID; }

    public String getSeriesName() { return SERIES_NAME; }

    public int getSeriesVersion() { return SERIES_VERSION; }

    public String getName(long type) {
        switch ((int) type) {
            case 1: return "VideoRecord";
            case 2: return "StartupComplete";
            case 3: return "CreateNewService";
            case 4: return "KillService";
            case 5: return "IncrementWaypoint";
            case 6: return "SafeHeadingAction";
            case 7: return "EntityLocation";
            case 8: return "BandwidthTest";
            case 9: return "BandwidthReceiveReport";
            case 10: return "SubTaskExecution";
            case 11: return "SubTaskAssignment";
            case 12: return "AutopilotKeepAlive";
            case 13: return "OnboardStatusReport";
            case 14: return "EntityJoin";
            case 15: return "EntityExit";
            case 16: return "SimulationTimeStepAcknowledgement";
            case 17: return "SpeedOverrideAction";

        }
        
        return "";
    }

    public long getType(String name) {
       if ( name.equals("VideoRecord")) return 1;
       if ( name.equals("StartupComplete")) return 2;
       if ( name.equals("CreateNewService")) return 3;
       if ( name.equals("KillService")) return 4;
       if ( name.equals("IncrementWaypoint")) return 5;
       if ( name.equals("SafeHeadingAction")) return 6;
       if ( name.equals("EntityLocation")) return 7;
       if ( name.equals("BandwidthTest")) return 8;
       if ( name.equals("BandwidthReceiveReport")) return 9;
       if ( name.equals("SubTaskExecution")) return 10;
       if ( name.equals("SubTaskAssignment")) return 11;
       if ( name.equals("AutopilotKeepAlive")) return 12;
       if ( name.equals("OnboardStatusReport")) return 13;
       if ( name.equals("EntityJoin")) return 14;
       if ( name.equals("EntityExit")) return 15;
       if ( name.equals("SimulationTimeStepAcknowledgement")) return 16;
       if ( name.equals("SpeedOverrideAction")) return 17;

       
       return -1;
    }

    public LMCPObject getInstance(long type) {
        switch ((int) type) {
            case 1: return new VideoRecord();
            case 2: return new StartupComplete();
            case 3: return new CreateNewService();
            case 4: return new KillService();
            case 5: return new IncrementWaypoint();
            case 6: return new SafeHeadingAction();
            case 7: return new EntityLocation();
            case 8: return new BandwidthTest();
            case 9: return new BandwidthReceiveReport();
            case 10: return new SubTaskExecution();
            case 11: return new SubTaskAssignment();
            case 12: return new AutopilotKeepAlive();
            case 13: return new OnboardStatusReport();
            case 14: return new EntityJoin();
            case 15: return new EntityExit();
            case 16: return new SimulationTimeStepAcknowledgement();
            case 17: return new SpeedOverrideAction();

        }

        return null;
    }

    public java.util.Collection<String> getAllTypes() {
        return Arrays.asList(name_list);
    }



}
