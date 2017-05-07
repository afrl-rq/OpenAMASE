// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.setup.map;

import afrl.cmasi.AltitudeType;
import afrl.cmasi.Location3D;
import avtas.map.MapLayer;
import avtas.map.Proj;
import avtas.util.WindowUtils;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

/**
 * Enables copying {@link Location3D} and {@link Location3D} objects from the map
 * @author AFRL/RQQD
 */
public class LocationCopyTool extends MapLayer{

    @Override
    public void paint(Graphics2D g) {
        // don't paint anything
    }

    @Override
    public void project(Proj proj) {
        // don't project anything
    }

    @Override
    public void addPopupMenuItems(JPopupMenu menu, MouseEvent e, final double lat, final double lon) {
        menu.add(new AbstractAction("Copy Location3D") {

            @Override
            public void actionPerformed(ActionEvent e) {
                Location3D loc = new Location3D(lat, lon, 0, AltitudeType.MSL);
                WindowUtils.copyToClipboard(loc.toXML("  "));
            }
        });
        menu.add(new AbstractAction("Copy Location3D") {

            @Override
            public void actionPerformed(ActionEvent e) {
                Location3D loc = new Location3D(lat, lon, 0, AltitudeType.MSL);
                WindowUtils.copyToClipboard(loc.toXML("  "));
            }
        });
    }
    
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */