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
import afrl.cmasi.AutomationRequest;
import afrl.cmasi.KeepInZone;
import afrl.cmasi.KeepOutZone;
import afrl.cmasi.OperatingRegion;
import avtas.amase.validator.TestResult.ResultType;
import avtas.lmcp.LMCPObject;
import java.util.HashMap;
import java.util.List;

/**
 *
 *
 */
public class AutomationRequestTest extends Test {

    /**
     * Creates a new instance of Test
     */
    public AutomationRequestTest() {
        name = "Plan Request Validation";
    }

    @Override
    public void runTest(LMCPObject o, List<LMCPObject> objects, int index, List<TestResult> testResults) {

        if (o instanceof AutomationRequest) {

            HashMap<Long, KeepInZone> kizMap = new HashMap<Long, KeepInZone>();
            HashMap<Long, KeepOutZone> kozMap = new HashMap<Long, KeepOutZone>();
            HashMap<Long, AirVehicleConfiguration> vehMap = new HashMap<Long, AirVehicleConfiguration>();
            HashMap<Long, OperatingRegion> operatingRegionMap = new HashMap<Long, OperatingRegion>();

            for (int i = 0; i < index; i++) {
                LMCPObject tmp = objects.get(i);
                if (tmp instanceof KeepOutZone) {
                    KeepOutZone koz = (KeepOutZone) tmp;
                    kozMap.put(koz.getZoneID(), koz);
                } else if (tmp instanceof KeepInZone) {
                    KeepInZone kiz = (KeepInZone) tmp;
                    kizMap.put(kiz.getZoneID(), kiz);
                } else if (tmp instanceof AirVehicleConfiguration) {
                    AirVehicleConfiguration avc = (AirVehicleConfiguration) tmp;
                    vehMap.put(avc.getID(), avc);
                } else if (tmp instanceof OperatingRegion) {
                    OperatingRegion or = (OperatingRegion) tmp;
                    operatingRegionMap.put(or.getID(), or);
                }
            }

            AutomationRequest pr = (AutomationRequest) o;

            for (long id : pr.getEntityList()) {
                if (vehMap.get(id) == null) {
                    testResults.add(new TestResult(this, o, ResultType.Error,
                            "No Configuration was found for Vehicle ID " + id));
                }
            }
            if (pr.getOperatingRegion() != 0 && !operatingRegionMap.containsValue(pr.getOperatingRegion())) {
                testResults.add(new TestResult(this, o, ResultType.Error,
                        "No Operating region was found for Vehicle ID " + pr.getOperatingRegion()));
            }

        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */