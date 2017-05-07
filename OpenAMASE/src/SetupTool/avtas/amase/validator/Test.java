// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.validator;

import avtas.lmcp.LMCPObject;
import java.util.List;

/**
 *
 *
 */
public abstract class Test {

    LMCPObject[] list = null;
    public String name = "No Description Available.";

    /** Creates a new instance of Test */
    public Test() {
    }

    

    /****
     * This method is called by the Validator plugin when testing is initiated, and passes TestResult objects
     * to report the results for the individual test.  If all tests are successfully passed,
     * the TestResult object reports this.
     * 
     * @param testObj The object to be tested
     * @param list the list of LMCP events that have been previously created
     * 
     ****/
    public abstract void runTest(LMCPObject testObj, List<LMCPObject> list, int index, List<TestResult> resultList);

    public String getDescription() {
        return this.name;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */