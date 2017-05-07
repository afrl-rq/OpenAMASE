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
import avtas.amase.ui.AircraftColors;
import avtas.amase.ui.IconTools;
import avtas.amase.ui.IconManager;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

/**
 * Provides a button interface for selecting aircraft.
 *
 * @author AFRL/RQQD
 */
public class AircraftClickPanel extends JPanel implements AppEventListener {

    HashMap<AirVehicleConfiguration, JToggleButton> acButtons = new HashMap<>();
    List<ActionListener> listeners = new ArrayList<>();
    ButtonGroup bg = new ButtonGroup();
    
    int iconSize = 18;


    public AircraftClickPanel() {
        //AppEventManager.getDefaultEventManager().addListener(this);
    }

    public void addActionListener(ActionListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public boolean removeActionListener(ActionListener l) {
        return listeners.remove(l);
    }

    public int getIconSize() {
        return iconSize;
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
    }
    
    

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof AirVehicleConfiguration) {
            addButton((AirVehicleConfiguration) event);
        } else if (event instanceof SessionStatus) {
            if (((SessionStatus) event).getState() == SimulationStatusType.Reset) {
                Collection<JToggleButton> buts = acButtons.values();
                for (JToggleButton but : buts) {
                    remove(but);
                }
                buts.clear();
                bg = new ButtonGroup();
            }
        } 
    }

    public void addButton(AirVehicleConfiguration avc) {
        Color uavColor = AircraftColors.getColor(avc.getID());
        String label = String.valueOf(avc.getID());
        if (avc.getLabel() != null && !avc.getLabel().isEmpty()) {
            label = avc.getLabel();
        }
        Image icon = IconManager.getIcon(avc);

        JToggleButton button = new JToggleButton(label);
        
        Image img = IconTools.getFilledImage(icon, iconSize, iconSize, 2, button.getForeground(), uavColor);
        button.setIcon(new ImageIcon(img));

        button.setHorizontalTextPosition(SwingConstants.RIGHT);

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (ActionListener l : listeners) {
                    l.actionPerformed(e);
                }
            }
        });

        button.setActionCommand(String.valueOf(avc.getID()));
        button.setMargin(new Insets(0, 0, 0, 0));
        //button.setContentAreaFilled(false);
        bg.add(button);

        acButtons.put(avc, button);
        add(button);
    }
    
    /** returns the AirVehicleConfiguration that is currently selected, or null
        if none is selected.
    */
    public AirVehicleConfiguration getSelectedAircraft() {
        for (Entry<AirVehicleConfiguration, JToggleButton> e : acButtons.entrySet()) {
            if (e.getValue().isSelected()) {
                return e.getKey(); 
            }
        }
        return null;
    }

    public static void main(String[] args) {
        AircraftClickPanel panel = new AircraftClickPanel();
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

        panel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(e.getActionCommand());
            }
        });

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */