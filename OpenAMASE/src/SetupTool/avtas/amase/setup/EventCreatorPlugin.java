// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.setup;

import avtas.amase.AmasePlugin;
import avtas.amase.objtree.LmcpObjectActions;
import avtas.app.AppEventManager;
import avtas.lmcp.LMCPObject;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * Presents the user with a combobox that allows for the creation of AMASE events.
 * 
 * @author AFRL/RQQD
 */
public class EventCreatorPlugin extends AmasePlugin {
    
    JComboBox combobox = null;
    private AppEventManager eventManager = null;
    
    public EventCreatorPlugin() {
        setupCombobox();
        this.eventManager = AppEventManager.getDefaultEventManager();
    }

    @Override
    public void eventOccurred(Object event) {
    }

    @Override
    public Component getGui() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(combobox, BorderLayout.CENTER);
        panel.setBorder(new TitledBorder("Create a new LMCP event"));
        return panel;
    }
    
    
    /** sets up a combobox for creating new events based on the current library of LMCP objects */
    void setupCombobox() {
        combobox = new JComboBox();
        combobox.addItem("Add an Event");
        final ArrayList<Object> objects = LmcpObjectActions.getCompatibleTypes(LMCPObject.class);
        for (Object o : objects) {
            combobox.addItem(LmcpObjectActions.getDisplayString(o));
        }
        combobox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (eventManager != null && combobox.getSelectedIndex() != 0) {
                    Object o = ((LMCPObject) objects.get(combobox.getSelectedIndex()-1)).clone();
                    if (eventManager != null) {
                        eventManager.fireEvent(o);
                        eventManager.fireEvent(new SelectObjectEvent(o));
                    }
                }
            }
        });
    }
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */