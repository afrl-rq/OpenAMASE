// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.analysis;

import avtas.app.AppEventListener;
import avtas.xml.Element;
/**
 * Interface used as a basis for all components that functionality to {@link AnalysisManager}.
 *
 * @author AFRL/RQQD
 */
public interface AnalysisClient extends AppEventListener{
    
    /** Defines the ideal time step between analysis updates.  This is used to prevent 
     *  unnecessarily frequent checking in the analysis modules.  (seconds)
     */
    static double CHECK_TIME = 0.5;

    /**
     * Gets the current analysis report for this module in XML format.
     * @return The first element of the XML document representing the analysis
     * report.
     */
    public Element getAnalysisReportXML();

    public void resetAnalysis();

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */