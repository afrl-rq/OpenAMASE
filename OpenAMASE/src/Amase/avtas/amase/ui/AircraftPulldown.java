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
import afrl.cmasi.RemoveEntities;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import avtas.amase.scenario.ScenarioState;
import avtas.app.AppEventListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JToggleButton;

/**
 * Provides a button interface for selecting aircraft.
 * @author AFRL/RQQD
 */
public class AircraftPulldown extends JComboBox implements AppEventListener {

    HashMap<Long, JToggleButton> acButtons = new HashMap<Long, JToggleButton>();
    List<ActionListener> listeners = new ArrayList<ActionListener>();
    ButtonGroup bg = new ButtonGroup();
    DefaultComboBoxModel<AirVehicleConfiguration> model = new DefaultComboBoxModel<>();

    public AircraftPulldown() {
        //AppEventManager.getDefaultEventManager().addListener(this);
        setRenderer(new AircraftListRenderer());
        
        setModel(model);
        
        for (AirVehicleConfiguration avc : ScenarioState.getAllAirVehicleConfigs()) {
            eventOccurred(avc);
        }
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof AirVehicleConfiguration) {
            AirVehicleConfiguration avc = (AirVehicleConfiguration) event;
            for (int i=0; i<getItemCount(); i++) {
                if ( ((AirVehicleConfiguration) getItemAt(i)).getID() == avc.getID() ) {
                    model.removeElementAt(i);
                    model.addElement(avc);
                    return;
                }
            }
            addItem(event);
        }
        else if (event instanceof RemoveEntities) {
            for (Long vehId : ((RemoveEntities) event).getEntityList()) {
                AirVehicleConfiguration avc = ScenarioState.getAirVehicleConfig(vehId);
                model.removeElement(avc);
            }
        }
        else if (event instanceof SessionStatus) {
            if ( ((SessionStatus) event).getState() == SimulationStatusType.Reset )
            removeAllItems();
        }
    }

    public AirVehicleConfiguration getSelectedAircraft() {
        return (AirVehicleConfiguration) getSelectedItem();
    }

    public static class AircraftListRenderer extends DefaultListCellRenderer {

        JLabel l = new JLabel();

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof AirVehicleConfiguration) {
                AirVehicleConfiguration avc = (AirVehicleConfiguration) value;
                Color uavColor = AircraftColors.getColor(avc.getID());
                String text = String.valueOf(avc.getID());
                if (!avc.getLabel().isEmpty()) {
                    text += " " + avc.getLabel();
                }
                
                Image img = IconTools.getFilledImage(IconManager.getIcon(avc), 18, 18, 2, label.getForeground(), uavColor);
                label.setIcon(new ImageIcon(img));
                label.setText(text);
            }
            return label;
        }
    }

    public static void main(String[] args) {
        AircraftPulldown panel = new AircraftPulldown();
        JFrame f = new JFrame();
        f.add(panel);

        AirVehicleConfiguration avc = new AirVehicleConfiguration();
        avc.setID(1);
        avc.setLabel("test 1");
        panel.eventOccurred(avc);
        
        avc = new AirVehicleConfiguration();
        avc.setID(2);
        avc.setLabel("test 2");
        panel.eventOccurred(avc);
        
        avc = new AirVehicleConfiguration();
        avc.setID(3);
        avc.setLabel("test 3");
        panel.eventOccurred(avc);

        panel.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                System.out.println(e.getItem());
            }
        });

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */