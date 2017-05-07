// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.analysis;

import afrl.cmasi.AirVehicleState;
import avtas.amase.scenario.ScenarioState;
import avtas.amase.util.SimTimer;
import java.text.DecimalFormat;
import avtas.xml.Element;

/**
 * The <code>MissionStatAnalysis</code> class logs basic information about the
 * mission and the state of assets at the end of a mission.
 * 
 * @author AFRL/RQQD
 */
public class MissionStatAnalysis implements AnalysisClient {
    
    
    double timeSec = 0;
    Element timeEl = new Element("MissionTime");
    Element energyNode = new Element("EnergyRemaining");
    Element mainNode = new Element("MissionStatistics");
    DecimalFormat formatter = new DecimalFormat("#.##");
    
    public MissionStatAnalysis() {
        mainNode.add(timeEl);
        mainNode.add(energyNode);
    }

     /** {@inheritDoc} */
    public Element getAnalysisReportXML() {
        timeEl.setText(formatter.format( ScenarioState.getTime()));
        energyNode.clear();
        for(AirVehicleState avs : ScenarioState.getAllAirVehicleStates()) {
            Element n = new Element("Energy");
            n.setAttribute("VehicleID", String.valueOf(avs.getID()));
            n.setAttribute("PercentRemaining", formatter.format(avs.getEnergyAvailable()));
            energyNode.add(n);
        }
        return mainNode;
    }

    public void resetAnalysis() {
    }

    @Override
    public void eventOccurred(Object event) {
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */