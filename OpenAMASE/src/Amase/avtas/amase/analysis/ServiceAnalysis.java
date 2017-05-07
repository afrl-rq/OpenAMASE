// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.analysis;

import afrl.cmasi.AutomationRequest;
import afrl.cmasi.ServiceStatus;
import avtas.amase.scenario.ScenarioState;
import avtas.amase.util.SimTimer;
import avtas.xml.Element;

/**
 * Records information on computation time for CCA's connected to the simulation.
 *
 * @author AFRL/RQQD
 */
public class ServiceAnalysis implements AnalysisClient {

    Element retEl = null;
    private double wallClockStart_sec = 0;
    private double simTimeStart_sec = 0;

    /** {@inheritDoc} */
    @Override
    public Element getAnalysisReportXML() {
        return retEl;
    }

    

    /** {@inheritDoc} */
    public void eventOccurred(Object evt) {
        if (evt instanceof ServiceStatus) {

            ServiceStatus stat = (ServiceStatus) evt;
            if (stat.getPercentComplete() >= 100) {
                Element el = new Element("Computation");
                if (retEl == null) {
                    retEl = new Element("ComputationTime");
                }
                retEl.add(el);
                double realTime = System.currentTimeMillis() / 1E3 - wallClockStart_sec;
                double simTime = ScenarioState.getTime() - simTimeStart_sec;
                el.setAttribute("RealTime_sec", String.valueOf(realTime));
                el.setAttribute("SimTime_sec", String.valueOf(simTime));
            }
        }
        else if (evt instanceof AutomationRequest) {
            wallClockStart_sec = System.currentTimeMillis() / 1E3;
            simTimeStart_sec = ScenarioState.getTime();
        }
    }

    public void step(double timestep_sec, double simTime_sec) {
        
    }

    @Override
    public void resetAnalysis() {
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */