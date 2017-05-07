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
import afrl.cmasi.AirVehicleState;
import avtas.amase.validator.TestResult.ResultType;
import avtas.lmcp.LMCPObject;
import java.util.List;

/**
 *
 *
 */
public class AirVehicleStateTest extends Test {

    /** Creates a new instance of Test */
    public AirVehicleStateTest() {
        name = "Air Vehicle State Validation";
    }

    /****
     * This method is called by the LMCPValidator class when testing is initiated, and passes a TestResult object
     * via the notifyResult method to report the results for the individual test.  If all tests are successfully passed,
     * the TestResult object reports this.
     * 
     * LMCPObject[] list is the complete set of LMCP Objects contained within the xml file to be validated.
     * 
     * After each test is completed, this method should call the notifyResult method (defined in the parent class "Test")
     * to notify all registered listeners of the test result. If no failures or warnings are detected, 
     * the TestResult object passed to the notifyResult method should indicate that the object being tested
     * has successfully passed all of the individual validation tests.
     * 
     * For proper display of results, it is suggested that this method must do the following:
     * --check and handle the case of a null LMCPObject List 
     * --iterate throught the LMCPObject list to extract relevant objects, and run appropriate tests
     * --mange the contents of the "testObjectList", which should include all of the LMCP objects used for each validation test.
     * --create an appropriate TestResult object for each individual test included in this method.  The TestResult object must include
     *   the unique ID for this set of tests, a string that provides the name of this test, the overall result of the test (as a ResultType
     *   object) and a brief textual description of the test result as a string.  These parameters are used to construct the human-readable
     *   output.
     ****/
    @Override
    public void runTest(LMCPObject o, List<LMCPObject> objects, int index, List<TestResult> testResults) {


        //For each AirVehicleState within the object list....
        if (o instanceof AirVehicleState) {
            AirVehicleState avs = (AirVehicleState) o;

            //Check that the vehicle position is defined
            if (avs.getLocation() == null) {
                testResults.add(new TestResult(this, o, ResultType.Error, "Position is null or not defined"));
            }

            //verify existence of a matching AirVehicleConfiguration and
            //get the stall speed
            boolean found = false;
            long id = avs.getID();
            float stallspeed = 0;
            for (int i=0; i<index; i++) {
                LMCPObject lo = objects.get(i);
                if (lo instanceof AirVehicleConfiguration) {
                    AirVehicleConfiguration avc = (AirVehicleConfiguration) lo;
                    if (avc.getID() == id) {
                        stallspeed = avc.getMinimumSpeed();
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                testResults.add(new TestResult(this, o, ResultType.Error,
                        "No AirVehicleConfiguration was not found for this AirVehicleState"));
                return;
            }

            //Check that airspeed is greater than stall speed
            if (avs.getAirspeed() < stallspeed) {
                testResults.add(new TestResult(this, o, ResultType.Warning, "Airspeed is less than stall speed"));
            }

            //Check that the airspeed is greater than 0
            if (avs.getAirspeed() <= 0) {
                testResults.add(new TestResult(this, o, ResultType.Warning, "Airspeed is zero"));
            }

            //Check that the energy available is greater than zero
            if (avs.getEnergyAvailable() <= 0) {
                testResults.add(new TestResult(this, o, ResultType.Warning, "Energy available is zero"));
            }

            //Verify that the sensor list is not empty
            if (avs.getPayloadStateList().isEmpty()) {
                testResults.add(new TestResult(this, o, ResultType.Warning, "No payload states were found"));
            }

            //Verifiy that the current way point is defined
            if (avs.getCurrentWaypoint() < 0) {
                testResults.add(new TestResult(this, o, ResultType.Warning, "Current waypoint is invalid"));
            }

        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */