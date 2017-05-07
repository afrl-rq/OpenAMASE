// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.scenario;

import afrl.cmasi.SessionStatus;
import avtas.amase.AmasePlugin;
import avtas.amase.util.SimTimer;
import avtas.lmcp.LMCPObject;

/**
 * Provides functions to track scenario data that is published through a remote connection.
 * This tool updates {@link ScenarioState} based on LMCP messages received, including
 * {@link SessionStatus} messages.
 * @author AFRL/RQQD
 */
public class ExternalScenarioManager extends AmasePlugin{

    @Override
    public void eventOccurred(Object event) {

        if (event instanceof LMCPObject) {
            ScenarioState.processLMCP((LMCPObject) event, ScenarioState.getTime());
        }
        
    }
    
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */