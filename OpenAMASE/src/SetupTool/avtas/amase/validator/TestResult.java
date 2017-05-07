// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.validator;

import avtas.amase.validator.Test;
import avtas.lmcp.LMCPObject;
import java.util.ArrayList;

/**
 *
 *
 */
public class TestResult {

    Test source = null;
    String resultMessage = "";
    ResultType result;
    LMCPObject testObject = null;

    public static enum ResultType {
        Warning,
        Error,
    }

    /**
     * Creates a new instance of TestResult
     */
    public TestResult() {
    }

    public TestResult(Test source, LMCPObject object, ResultType r, String message) {
        this.source = source;
        resultMessage = message;
        result = r;
        this.testObject = object;
    }

    public Test getTest() {
        return source;
    }

    public String getMessage() {
        return resultMessage;
    }

    public LMCPObject getTestObject() {
        return testObject;
    }

    @Override
    public String toString() {
        String resultString = "";
        resultString += "Test : " + source.getDescription() + "\n";

        resultString += "Result: " + result + "\n";
        resultString += "Message: " + resultMessage + "\n";
        if (testObject != null) {
            resultString += "-------------Test Object:-------------\n";
            resultString += testObject.toString();
        }
        resultString += "------------------------------------------------------------------------\n";
        return resultString;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */