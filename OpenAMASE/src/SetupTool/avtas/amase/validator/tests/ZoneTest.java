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
import afrl.cmasi.AbstractZone;
import avtas.amase.validator.TestResult.ResultType;
import avtas.lmcp.LMCPObject;
import java.util.List;

/**
 *
 *
 */
public class ZoneTest extends Test {

    /** Creates a new instance of Test */
    public ZoneTest() {
        name = "No Fly Zone Validation";
    }

    @Override
    public void runTest(LMCPObject o, List<LMCPObject> objects, int index, List<TestResult> testResults) {

        //For each NoFlyZone...
        if (o instanceof AbstractZone) {
            AbstractZone z = (AbstractZone) o;

            String zoneType = o.getClass().getSimpleName();

            //unique ID
            for (int i=0; i<index; i++) {
                LMCPObject lo = objects.get(i);
                if (lo instanceof AbstractZone) {
                    AbstractZone temp = (AbstractZone) lo;
                    if (temp != z && temp.getZoneID() == z.getZoneID()) {
                        testResults.add(new TestResult(this, o, ResultType.Error, zoneType + " ID is not unique"));
                    }
                }
            }

            //check zone shape validity
            String geomCheck = GeometryCheck.geometryCheck(z.getBoundary());
            if (!geomCheck.isEmpty()) {
                testResults.add(new TestResult(this, o, ResultType.Error,
                        "Geometry Error: " + geomCheck));

            }

            //Verify that the min and max altitudes are defined and valid
            float min = z.getMinAltitude();
            float max = z.getMaxAltitude();
            if (max <= min) {
                testResults.add(new TestResult(this, o, ResultType.Error, "Maximum altitude is less than or equal to minimum altitude"));

            }
            if (max == 0) {
                testResults.add(new TestResult(this, o, ResultType.Warning, "Maximum altitude is zero"));

            }


        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */