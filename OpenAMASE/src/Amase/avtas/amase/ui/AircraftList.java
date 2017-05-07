// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.ui;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import avtas.amase.scenario.ScenarioState;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import java.util.Arrays;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Provides a selectable list featuring aircraft that exist in the scenario
 *
 * @author AFRL/RQQD
 */
public class AircraftList extends JList<AirVehicleConfiguration> implements AppEventListener {
    
    DefaultListModel<AirVehicleConfiguration> model = new DefaultListModel<>();

    public AircraftList() {
        AppEventManager.getDefaultEventManager().addListener(this);
        setCellRenderer(new AircraftPulldown.AircraftListRenderer());
        setModel(model);
        
        for (AirVehicleConfiguration avc : ScenarioState.getAllAirVehicleConfigs()) {
            eventOccurred(avc);
        }
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof AirVehicleConfiguration) {
            AirVehicleConfiguration avc = (AirVehicleConfiguration) event;
            for (int i=0; i<model.getSize(); i++) {
                if (model.getElementAt(i).getID() == avc.getID()) {
                    model.setElementAt(avc, i);
                    return;
                }
            }
            model.addElement(avc);
        } else if (event instanceof SessionStatus) {
            if (((SessionStatus) event).getState() == SimulationStatusType.Reset) {
                model.removeAllElements();
            }
        }
    }
    
    
    public static void main(String[] args) {
        AircraftList panel = new AircraftList();
        JFrame f = new JFrame();
        f.add(panel);

        AirVehicleConfiguration avc = new AirVehicleConfiguration();
        avc.setID(1);
        avc.setLabel("test 1");
        ScenarioState.processLMCP(avc, 0);
        panel.eventOccurred(avc);
        
        avc = new AirVehicleConfiguration();
        avc.setID(2);
        avc.setLabel("test 2");
        ScenarioState.processLMCP(avc, 0);
        panel.eventOccurred(avc);
        
        avc = new AirVehicleConfiguration();
        avc.setID(3);
        avc.setLabel("test 3");
        ScenarioState.processLMCP(avc, 0);
        panel.eventOccurred(avc);

        panel.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                System.out.println( Arrays.toString( ((JList) e.getSource()).getSelectedIndices()) );
            }
        });

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }

    /**
     * removes all aircraft from the list.
     */
    public void clear() {
        model.removeAllElements();
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */