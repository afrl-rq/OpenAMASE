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
import afrl.cmasi.CameraConfiguration;
import afrl.cmasi.FlightProfile;
import afrl.cmasi.GimbalConfiguration;
import afrl.cmasi.PayloadConfiguration;
import avtas.amase.validator.TestResult.ResultType;
import avtas.lmcp.LMCPObject;
import java.util.List;

/**
 *
 *
 */
public class AirVehicleConfigurationTest extends Test {

    /** Creates a new instance of AirVehicleConfigurationValidation */
    public AirVehicleConfigurationTest() {
        name = "Air Vehicle Configuration Validation";
    }

    @Override
    public void runTest(LMCPObject o, List<LMCPObject> objects, int index, List<TestResult> testResults) {

        //For each AirVehicleConfiguration...
        if (o instanceof AirVehicleConfiguration) {
            AirVehicleConfiguration avc = (AirVehicleConfiguration) o;

            //Verify callsign is defined
            String callsign = avc.getLabel();
            if (callsign.equals("") || callsign.equals("null")) {
                testResults.add(new TestResult(this, o, ResultType.Warning, "Callsign is null or not defined"));
            }


            //Verify that the vehicle ID is valid
            if (avc.getID() <= 0) {
                testResults.add(new TestResult(this, o, ResultType.Error, "Invalid vehicle ID"));
            }

            //Verify that the stall speed is reasonable
            if (avc.getMinimumSpeed() <= 0) {
                testResults.add(new TestResult(this, o, ResultType.Error, "Invalid stall speed"));
            }

            //Verify that at least one FLightConfiguration is defined
            if (avc.getNominalFlightProfile() == null) {
                testResults.add(new TestResult(this, o, ResultType.Error, "No Flight Profile defined"));
            }
            else {
                FlightProfile nfp = avc.getNominalFlightProfile();
                if (nfp.getMaxBankAngle() <= 0) {
                    testResults.add(new TestResult(this, o, ResultType.Error, "Nominal Flight Profile has zero max bank angle"));
                }
                if (nfp.getAirspeed() <= 0) {
                    testResults.add(new TestResult(this, o, ResultType.Error, "Nominal Flight Profile has zero airspeed"));
                }
            }

            for (PayloadConfiguration pc : avc.getPayloadConfigurationList()) {
                if (pc instanceof CameraConfiguration) {
                    testCamera((CameraConfiguration) pc, testResults);
                }
                else if (pc instanceof GimbalConfiguration) {
                    testGimbal((GimbalConfiguration) pc, testResults);
                }
            }

        }
    }

    void testGimbal(GimbalConfiguration gc, List<TestResult> testResults) {
        if (gc.getMinAzimuth() > gc.getMaxAzimuth()) {
            testResults.add(new TestResult(this, gc, ResultType.Error, "Minimum azimuth is larger than the maximum azimuth"));
        }
        if (gc.getMinElevation() > gc.getMaxElevation()) {
            testResults.add(new TestResult(this, gc, ResultType.Error, "Minimum elevation is larger than the maximum elevation"));
        }
    }

    void testCamera(CameraConfiguration cc, List<TestResult> testResults) {

        //Verify azimuth FOV values
        if (cc.getMinHorizontalFieldOfView() > cc.getMaxHorizontalFieldOfView()) {
            testResults.add(new TestResult(this, cc, ResultType.Error, "Minimum azimuth FOV is larger than the maximum azimuth FOV"));
        }

        //Verify Horizontal and Vertical Pixels
        if (cc.getVideoStreamHorizontalResolution() == 0 || cc.getVideoStreamVerticalResolution() == 0) {
            testResults.add(new TestResult(this, cc, ResultType.Error, "Number of pixels must be greater than 0"));
        }

    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */