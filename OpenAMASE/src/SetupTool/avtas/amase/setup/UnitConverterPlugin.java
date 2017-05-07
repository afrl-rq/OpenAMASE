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
import avtas.data.UnitConverterPanel;
import avtas.util.WindowUtils;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

/**
 * Provides a simple converter between units
 * @author AFRL/RQQD
 */
public class UnitConverterPlugin extends AmasePlugin{

    public UnitConverterPlugin() {
        setPluginName("Unit Converter");
    }

    @Override
    public void getMenus(final JMenuBar menubar) {
        JMenu menu = WindowUtils.getMenu(menubar, "Tools");
        menu.add(new AbstractAction("Unit Converter") {

            @Override
            public void actionPerformed(ActionEvent e) {
                WindowUtils.showPlainDialog(JOptionPane.getFrameForComponent(menubar), new UnitConverterPanel(), "Unit Converter", false);
            }
        });
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */