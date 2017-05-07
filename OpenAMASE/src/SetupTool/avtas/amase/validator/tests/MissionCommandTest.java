// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.validator.tests;

import avtas.amase.validator.TestResult;
import avtas.amase.validator.Test;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.KeepInZone;
import afrl.cmasi.KeepOutZone;
import afrl.cmasi.MissionCommand;
import afrl.cmasi.Waypoint;
import avtas.amase.validator.TestResult.ResultType;
import avtas.lmcp.LMCPObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class MissionCommandTest extends Test {

    ArrayList<KeepOutZone> keepOutList;
    ArrayList<KeepInZone> keepInList;

    /** Creates a new instance of Test */
    public MissionCommandTest() {
        name = "Mission Command Validation";
    }

    @Override
    public void runTest(LMCPObject o, List<LMCPObject> objects, int index, List<TestResult> testResults) {


        //For each MissionCommand object within the LMCPObject list...
        if (o instanceof MissionCommand) {
            MissionCommand mc = (MissionCommand) o;

            //check that a valid airvehicle configuration exists
            //get the stall speed
            boolean found = false;
            long id = mc.getVehicleID();
            for (int i=0; i<index; i++) {
                LMCPObject temp = objects.get(i);
                if (temp instanceof AirVehicleConfiguration) {
                    AirVehicleConfiguration avc = (AirVehicleConfiguration) temp;
                    if (avc.getID() == id) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                testResults.add(new TestResult(this, o, ResultType.Error, "No AirVehicleConfiguration was found for this MissionCommand object"));
            }

            //Iterate through the waypointlist
            for (Waypoint w : mc.getWaypointList()) {

                //Verify that the airspeed is valid
                if (w.getSpeed() <= 0) {
                    testResults.add(new TestResult(this, o, ResultType.Error,
                            "Waypoint airspeed must be greater than zero"));
                }
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */