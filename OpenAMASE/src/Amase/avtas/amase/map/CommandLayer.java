// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.map;

import afrl.cmasi.LoiterAction;
import afrl.cmasi.MissionCommand;
import afrl.cmasi.RemoveEntities;

import afrl.cmasi.VehicleAction;
import afrl.cmasi.VehicleActionCommand;

import avtas.amase.objtree.ObjectTree;
import avtas.amase.ui.AircraftColors;
import avtas.amase.scenario.ScenarioEvent;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;

import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapGraphicsList;
import avtas.map.graphics.MapMarker;
import avtas.map.graphics.MapPoly;
import avtas.map.layers.GraphicsLayer;
import avtas.util.WindowUtils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

/**
 *
 * @author AFRL/RQQD
 */
public class CommandLayer extends GraphicsLayer<MapGraphic> implements AppEventListener {

    Stroke stroke = new BasicStroke(2f);

    public CommandLayer() {
        AppEventManager.getDefaultEventManager().addListener(this);
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof MissionCommand) {
            MissionCommand mc = (MissionCommand) event;

            Color color = AircraftColors.getColor(mc.getVehicleID());
            MapGraphic g = new CommandGraphic(mc, color);

            //g.setPaint(color);
            //g.setStroke(stroke);
            g.setRefObject(mc.getVehicleID());

            MapGraphic oldGraphic = getByRefObject(mc.getVehicleID());
            if (oldGraphic != null) {
                remove(oldGraphic);
            }

            add(g);
            refresh();

        } else if (event instanceof VehicleActionCommand) {
            VehicleActionCommand vc = (VehicleActionCommand) event;

            MapGraphicsList g = null;
            for (VehicleAction va : vc.getVehicleActionList()) {
                if (va instanceof LoiterAction) {
                    if (g == null) {
                        g = new MapGraphicsList();
                    }
                    g.add(new LoiterGraphic((LoiterAction) va));
                }
            }

            if (g != null) {

                Color color = AircraftColors.getColor(vc.getVehicleID());
                //g.setPainter(Painter.createOutlinePainter(color, Color.WHITE, 1.5f));
                g.setPainter(color, 1);
                g.setRefObject(vc.getVehicleID());

                MapGraphic oldGraphic = getByRefObject(vc.getVehicleID());
                if (oldGraphic != null) {
                    remove(oldGraphic);
                }

                add(g);
                refresh();
            }

        } else if (event instanceof ScenarioEvent) {
            clear();
            refresh();
        } else if (event instanceof RemoveEntities) {
            RemoveEntities re = (RemoveEntities) event;
            for (long id : re.getEntityList()) {
                int index = indexOf(getByRefObject(id));
                if (index != -1) {
                    remove(index);
                }
            }
        }
    }

    @Override
    public void addPopupMenuItems(javax.swing.JPopupMenu menu, MouseEvent e, double lat, double lon) {
        for (final MapGraphic g : this) {
            if ((g.contains(e.getPoint()) || g.onEdge(e.getX(), e.getY(), 4)) && g instanceof CommandGraphic) {
                for (final MapGraphic gg : (CommandGraphic) g) {
                    if (gg instanceof MapGraphicsList) {
                        for (MapGraphic ggg : (MapGraphicsList<MapGraphic>) gg) {
                            // waypoints and center of loiters
                            if (ggg.contains(e.getPoint()) && ggg instanceof MapMarker) {
                                menu.add(getViewItem(ggg.getRefObject(), ggg.getName()));
                            }
                        }
                    } // route paths
                    else if (gg.onEdge(e.getX(), e.getY(), 4) && gg instanceof MapPoly) {
                        menu.add(getViewItem(gg.getRefObject(), gg.getName()));
                    }
                }
            }
        }
    }

    JMenuItem getViewItem(final Object refObject, final String label) {
        return new JMenuItem(new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent e) {
                ObjectTree tree = new ObjectTree(refObject);
                tree.setEditable(false);
                WindowUtils.showPlainDialog(getMap(), new JScrollPane(tree),
                        refObject.getClass().getSimpleName());
            }
        });
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */