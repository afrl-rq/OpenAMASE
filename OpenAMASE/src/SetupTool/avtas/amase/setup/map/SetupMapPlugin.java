// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.setup.map;

import avtas.amase.map.MapPlugin;
import avtas.amase.scenario.ScenarioEvent;
import avtas.data.Unit;
import avtas.map.Proj;
import avtas.util.WindowUtils;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Extends the MapPlugin to enable appropriate scenario view handling in Setup
 * Tool applications
 *
 * @author AFRL/RQQD
 */
public class SetupMapPlugin extends MapPlugin {

    ScenarioEvent scenEvent = null;

    @Override
    public void initScenario(ScenarioEvent evt) {
        // if the AppEventManager loads a new file, then clear this layer.
        Element node = XMLUtil.getChild(evt.getXML(), "ScenarioData");
        this.scenEvent = evt;
        // setup the lat and long origin and zoom level
        if (node != null) {
            node = XMLUtil.getChild(node, "SimulationView");
            if (node != null) {
                double lat = XMLUtil.getDoubleAttr(node, "Latitude", 0);
                double lon = XMLUtil.getDoubleAttr(node, "Longitude", 0);
                double scale = XMLUtil.getDoubleAttr(node, "LongExtent", 180);

                if (lat != super.clat || lon != super.clon || super.scale != scale) {
                    super.clat = lat;
                    super.clon = lon;
                    super.scale = scale;
                    map.setCenter(lat, lon);
                    map.getProj().setLonWidth(scale);
                    map.project();
                }

            }
        }
    }

    @Override
    public void addPopupMenuItems(JPopupMenu menu, MouseEvent e, double lat, double lon) {
        super.addPopupMenuItems(menu, e, lat, lon); //To change body of generated methods, choose Tools | Templates.

        JMenuItem setViewAction = new JMenuItem(new AbstractAction("Set Scenario View") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getMap() != null && getMap().getProj() != null) {
                    if (scenEvent != null && scenEvent.getXML() != null) {
                        Element viewEl = XMLUtil.getOrAddElement(scenEvent.getXML(), "ScenarioData/SimulationView");
                        viewEl.setAttribute("Latitude", map.getProj().getCenterLat());
                        viewEl.setAttribute("Longitude", map.getProj().getCenterLon());
                        viewEl.setAttribute("LongExtent", Unit.bound180(map.getProj().getEastLon() - map.getProj().getWestLon()));

                        if (eventManager != null) {
                            eventManager.fireEvent(scenEvent);
                        }

                    }
                }
            }
        });

        menu.addSeparator();
        menu.add(setViewAction);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */