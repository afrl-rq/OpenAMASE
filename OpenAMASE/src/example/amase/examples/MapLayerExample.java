// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package amase.examples;

import afrl.cmasi.AirVehicleState;
import afrl.cmasi.SessionStatus;
import avtas.amase.scenario.ScenarioState;
import avtas.amase.util.CmasiNavUtils;
import avtas.app.AppEventListener;
import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapLine;
import avtas.map.graphics.MapText;
import avtas.map.layers.GraphicsLayer;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.SwingConstants;

/**
 * Demonstrates an example map layer. In this example, we draw a line between
 * two aircraft, denoting the distance between the two.
 *
 * @author AFRL/RQQD
 */
public class MapLayerExample extends GraphicsLayer<MapGraphic> implements AppEventListener {

    MapLine line = new MapLine();
    MapText text = new MapText();
    JCheckBoxMenuItem showMenu;

    public MapLayerExample() {

        getList().add(line);
        getList().add(text);
        line.setPainter(Color.WHITE, 1);
        text.setColor(Color.WHITE);
        text.setFill(Color.BLACK);
        text.setHorizontalAlignment(SwingConstants.CENTER);


        // initialize the popup menu item
        showMenu = new JCheckBoxMenuItem("Show Relative Distance", true);

        showMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(showMenu.isSelected());
                //line.setVisible(showMenu.isSelected());
                //text.setVisible(showMenu.isSelected());
            }
        });
    }

    @Override
    public void eventOccurred(Object event) {

        if (event instanceof SessionStatus) {
            updateGraphics();
        }
    }

    /**
     * Here we draw our graphics that show a line between the two aircraft.  This shows some
     * of the functions that are available in 2D map graphics.
     */
    protected void updateGraphics() {
        AirVehicleState state1 = ScenarioState.getAirVehicleState(1L);
        AirVehicleState state2 = ScenarioState.getAirVehicleState(2L);

        if (state1 != null && state2 != null) {
            line.setVisible(true);
            text.setVisible(true);

            line.setLine(state1.getLocation().getLatitude(), state1.getLocation().getLongitude(),
                    state2.getLocation().getLatitude(), state2.getLocation().getLongitude());
            text.setLatLon(0.5 * (state1.getLocation().getLatitude() + state2.getLocation().getLatitude()),
                    0.5 * (state1.getLocation().getLongitude() + state2.getLocation().getLongitude()));

            text.setText(Integer.toString((int) CmasiNavUtils.distance(state1.getLocation(), state2.getLocation())));

        } else {
            line.setVisible(false);
            text.setVisible(false);
        }
        
        // tells the layer to reproject the graphics after their properties have changed
        project(line, text);
    }

    @Override
    public void addPopupMenuItems(javax.swing.JPopupMenu menu, MouseEvent e, double lat, double lon) {
        menu.add(showMenu);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */