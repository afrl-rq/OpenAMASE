// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.validator.tests;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.EntityConfiguration;
import avtas.amase.validator.Test;
import avtas.amase.validator.TestResult;
import avtas.amase.validator.TestResult.ResultType;
import avtas.lmcp.LMCPObject;
import java.util.List;

/**
 * Tests for the existence of a duplicated ID for vehicles (CMASI) or entities.
 * @author AFRL/RQQD
 */
public class UniqueIdTest extends Test{

    @Override
    public void runTest(LMCPObject testObj, List<LMCPObject> objects, int index, List<TestResult> testResults) {
        long id = -1;
        if (testObj instanceof AirVehicleConfiguration) {
            id = ((AirVehicleConfiguration) testObj).getID();
        }
        else if (testObj instanceof EntityConfiguration) {
            id = ((EntityConfiguration) testObj).getID();
        }
        else {
            return;
        }
        for (int i=0; i<index; i++) {
            LMCPObject o = objects.get(i);
            if (o instanceof AirVehicleConfiguration) {
                AirVehicleConfiguration avc = (AirVehicleConfiguration) o; 
                if (avc.getID() == id) {
                    testResults.add(new TestResult(this, testObj, ResultType.Warning, " Duplicated Entity/Vehicle ID"));
                }
            } 
            else if (o instanceof EntityConfiguration) {
                EntityConfiguration avc = (EntityConfiguration) o;
                if (avc.getID() == id) {
                    testResults.add(new TestResult(this, testObj, ResultType.Warning, " Duplicated Entity/Vehicle ID"));
                }
            } 
        }
    }
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */