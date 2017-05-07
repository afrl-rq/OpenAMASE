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

import afrl.cmasi.AreaSearchTask;
import afrl.cmasi.LineSearchTask;
import afrl.cmasi.SearchTask;
import afrl.cmasi.Task;
import avtas.amase.validator.TestResult.ResultType;
import avtas.lmcp.LMCPObject;
import java.util.List;

/**
 *
 *
 */
public class SearchTaskTest extends Test {

    /** Creates a new instance of Test */
    public SearchTaskTest() {
        name = "Search Task Validation";
    }

    @Override
    public void runTest(LMCPObject o, List<LMCPObject> objects, int index, List<TestResult> testResults) {

        //For each SearchTask...
        if (o instanceof SearchTask) {
            SearchTask st = (SearchTask) o;

            //unique ID
            for (int i=0; i<index; i++) {
                LMCPObject lo = objects.get(i);
                if (lo instanceof Task) {
                    Task temp = (Task) lo;
                    if (temp != st && temp.getTaskID() == st.getTaskID()) {
                        testResults.add(new TestResult(this, o, ResultType.Error, "SearchTask ID is not unique"));
                        
                    }
                }
            }

            //Valid Priority
            if (st.getPriority() < 0 || st.getPriority() > 1) {
                testResults.add(new TestResult(this, o, ResultType.Error, "Invalid search priority value"));
                
            }

            //Search Type and search points
            if (st instanceof AreaSearchTask) {
                AreaSearchTask ast = (AreaSearchTask) st;
                String geomCheck = GeometryCheck.geometryCheck(ast.getSearchArea());
                if (!geomCheck.isEmpty()) {
                    testResults.add(new TestResult(this, o, ResultType.Error,
                            "Geometry Error: " + geomCheck));
                    
                }
            }
            else if (st instanceof LineSearchTask) {
                LineSearchTask lst = (LineSearchTask) st;
                if (lst.getPointList().size() < 3) {
                    testResults.add(new TestResult(this, o, ResultType.Warning,
                            "Insufficient number of search points for a line search"));
                    
                }
            }

        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */