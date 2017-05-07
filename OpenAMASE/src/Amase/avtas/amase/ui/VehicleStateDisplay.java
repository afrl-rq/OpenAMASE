// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.ui;



import java.util.HashMap;
import javax.swing.JPanel;
import avtas.app.AppEventManager;
import avtas.amase.scenario.ScenarioEvent;
import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import avtas.amase.AmasePlugin;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author AFRL/RQQD
 */
public class VehicleStateDisplay extends AmasePlugin {
    
    private AppEventManager dataMgr = null;
    HashMap<Long,VehiclePanel> vehicleMap = new HashMap<>();
    JPanel statusListPanel = new JPanel();
    JScrollPane scrollpane;
    
    public VehicleStateDisplay() {
        
        setPluginName("Vehicle Status");
        
        dataMgr = AppEventManager.getDefaultEventManager();

        statusListPanel.setLayout(new BoxLayout(statusListPanel, BoxLayout.Y_AXIS));
        JPanel tmp = new JPanel(new BorderLayout());
        tmp.add(statusListPanel, BorderLayout.NORTH);
        tmp.add(Box.createGlue(), BorderLayout.CENTER);
        scrollpane = new JScrollPane(tmp);

        scrollpane.setBorder(new EmptyBorder(0,0,0,0));

    }

    public void initScenario() {
        statusListPanel.removeAll();
        vehicleMap.clear();
        statusListPanel.add(new JSeparator(JSeparator.HORIZONTAL));
    }

    public void eventOccurred(Object evt) {
        if (evt instanceof ScenarioEvent) {
            initScenario();
            return;
        }
        if (evt instanceof AirVehicleConfiguration) {
            AirVehicleConfiguration avc = (AirVehicleConfiguration) evt;
            VehiclePanel vPanel = vehicleMap.get(avc.getID());
            if (vPanel == null) {
                vPanel = new VehiclePanel(avc, AircraftColors.getColor(avc.getID()));
                vehicleMap.put(avc.getID(), vPanel );
                statusListPanel.add(vPanel);
                statusListPanel.add(new JSeparator(JSeparator.HORIZONTAL));
            }
        }
        else if (evt instanceof AirVehicleState) {
            AirVehicleState avs = (AirVehicleState) evt;
            VehiclePanel vPanel = vehicleMap.get(avs.getID());
            if (vPanel != null) {
                vPanel.update(avs);
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