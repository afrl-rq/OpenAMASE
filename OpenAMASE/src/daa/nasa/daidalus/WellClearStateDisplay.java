// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package nasa.daidalus;



import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import larcfm.DAIDALUS.*;

import avtas.app.AppEventManager;
import avtas.amase.scenario.ScenarioEvent;
import avtas.amase.ui.*;
import avtas.amase.AmasePlugin;
import avtas.amase.scenario.ScenarioState;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AFRL/RQQD
 * Note - Based off of VehicleStateDisplay
 */
public class WellClearStateDisplay extends AmasePlugin {
    
    private Map<Long, WellClearState> idToVehicleState = new HashMap<>();
    
    private AppEventManager dataMgr = null;
    HashMap<Long,WellClearPanel> vehicleMap = new HashMap<>();
    JPanel statusListPanel = new JPanel();
    JScrollPane scrollpane;
    
    public WellClearStateDisplay() {
        
        setPluginName("Well Clear Status");
        
        dataMgr = AppEventManager.getDefaultEventManager();

        statusListPanel.setLayout(new BoxLayout(statusListPanel, BoxLayout.LINE_AXIS));
        JPanel tmp = new JPanel(new BorderLayout());
        tmp.add(statusListPanel, BorderLayout.CENTER);
        tmp.add(Box.createGlue(), BorderLayout.EAST);
        scrollpane = new JScrollPane(tmp);

        scrollpane.setBorder(new EmptyBorder(0,0,0,0));

    }

    public void initScenario() {
        statusListPanel.removeAll();
        vehicleMap.clear();
        statusListPanel.add(new JSeparator(JSeparator.VERTICAL));
    }

    public void eventOccurred(Object event) {
        if (event instanceof ScenarioEvent) {
            initScenario();
            return;
        }
        
        if (event instanceof AirVehicleConfiguration) {
            AirVehicleConfiguration avc = (AirVehicleConfiguration) event;
            WellClearPanel wcPanel = vehicleMap.get(avc.getID());
            if (wcPanel == null) {
                wcPanel = new WellClearPanel(avc, AircraftColors.getColor(avc.getID()));
                vehicleMap.put(avc.getID(), wcPanel );
                statusListPanel.add(wcPanel);
                statusListPanel.add(new JSeparator(JSeparator.VERTICAL));
            }
        }
        else if (event instanceof DAIDALUSConfiguration) {
            DAIDALUSConfiguration daidalusCfg = (DAIDALUSConfiguration) event;
            Long id = daidalusCfg.getEntityId();

            WellClearState state;
            if (idToVehicleState.containsKey(id)) {
                state = idToVehicleState.get(id);
            } else {
                state = new WellClearState();
            }            
            
            // Update the object
            state.setDAIDALUSConfiguration(daidalusCfg);
            idToVehicleState.put(id, state);
            
            WellClearPanel wcPanel = vehicleMap.get(daidalusCfg.getEntityId());
            if (wcPanel != null) {
                wcPanel.update(daidalusCfg, state);
            }            
        }
        else if (event instanceof AirVehicleState) {
            AirVehicleState avs = (AirVehicleState) event;
            WellClearPanel wcPanel = vehicleMap.get(avs.getID());
            if (wcPanel != null) {
                wcPanel.update(avs);
            }
        }
        else if (event instanceof WellClearViolationIntervals) {        
            WellClearViolationIntervals wcvIntvl = (WellClearViolationIntervals) event;
            Long id = wcvIntvl.getEntityId();

            WellClearState state;
            if (idToVehicleState.containsKey(id)) {
                state = idToVehicleState.get(id);
            } else {
                state = new WellClearState();
            }
            
            state.setBands((WellClearViolationIntervals) event, ScenarioState.getTime());
            idToVehicleState.put(id, state);
            
            
            WellClearPanel wcPanel = vehicleMap.get(wcvIntvl.getEntityId());
            if (wcPanel != null) {
                wcPanel.update(state);
            }
        }
        
    }

    @Override
    public Component getGui() {
        return scrollpane;
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */