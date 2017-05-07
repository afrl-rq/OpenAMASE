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
import afrl.cmasi.CameraConfiguration;
import afrl.cmasi.PayloadConfiguration;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JToggleButton;

/**
 * Provides a button interface for selecting sensors on aircraft
 *
 * @author AFRL/RQQD
 */
public class SensorPulldown extends JComboBox implements AppEventListener {

    HashMap<Long, JToggleButton> acButtons = new HashMap<>();
    List<ActionListener> listeners = new ArrayList<>();
    ButtonGroup bg = new ButtonGroup();
    DefaultComboBoxModel<PayloadInfo> model = new DefaultComboBoxModel<>();

    public SensorPulldown() {
        //AppEventManager.getDefaultEventManager().addListener(this);
        setRenderer(new PayloadListRenderer());

        setModel(model);

        for (AirVehicleConfiguration avc : ScenarioState.getAllAirVehicleConfigs()) {
            eventOccurred(avc);
        }
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof AirVehicleConfiguration) {
            AirVehicleConfiguration avc = (AirVehicleConfiguration) event;
            for (int i = 0; i < getItemCount(); i++) {
                if (getItemAt(i) == null) {
                    continue;
                }
                if (((PayloadInfo) getItemAt(i)).owner.getID() == avc.getID()) {
                    model.removeElementAt(i);
                    return;
                }
            }
            for (PayloadConfiguration pc : avc.getPayloadConfigurationList()) {
                if (pc instanceof CameraConfiguration) {
                    addItem(new PayloadInfo(avc, (CameraConfiguration) pc));
                }
            }

        } else if (event instanceof RemoveEntities) {
            for (Long vehId : ((RemoveEntities) event).getEntityList()) {
                for (int i = model.getSize() - 1; i >= 0; i--) {
                    if (model.getElementAt(i).owner.getID() == vehId) {
                        model.removeElementAt(i);
                    }
                }
            }
        } else if (event instanceof SessionStatus) {
            if (((SessionStatus) event).getState() == SimulationStatusType.Reset) {
                removeAllItems();
                addItem(null);
            }
        }
    }

    public AirVehicleConfiguration getSelectedAircraft() {
        return getSelectedItem() != null ? ((PayloadInfo) getSelectedItem()).owner : null;
    }

    public CameraConfiguration getSelectedSensor() {
        return getSelectedItem() != null ? (CameraConfiguration) ((PayloadInfo) getSelectedItem()).payload : null;
    }

    public static class PayloadListRenderer extends DefaultListCellRenderer {

        JLabel l = new JLabel();

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof PayloadInfo) {
                PayloadInfo info = (PayloadInfo) value;
                Color uavColor = AircraftColors.getColor(info.owner.getID());
                StringBuilder text = new StringBuilder(String.valueOf(info.owner.getID()));
                if (!info.owner.getLabel().isEmpty()) {
                    text.append(" ").append(info.owner.getLabel());
                }
                text.append(" ").append(info.payload.getPayloadKind());

                text.append(" (").append((info.payload).getSupportedWavelengthBand()).append(")");

                Image img = IconTools.getFilledImage(IconManager.getIcon(info.owner), 18, 18, 2, label.getForeground(), uavColor);
                label.setIcon(new ImageIcon(img));
                label.setText(text.toString());
            }
            else if (value == null) {
                label.setText("No Sensor");
            }
            return label;
        }
    }

    /**
     * Contains information regarding the payload and it's owning platform ( an
     * AirVehicleConfiguration)
     */
    public static class PayloadInfo {

        public PayloadInfo(AirVehicleConfiguration owner, CameraConfiguration payload) {
            this.owner = owner;
            this.payload = payload;
        }

        AirVehicleConfiguration owner;
        CameraConfiguration payload;
    }

    public static void main(String[] args) {
        SensorPulldown panel = new SensorPulldown();
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