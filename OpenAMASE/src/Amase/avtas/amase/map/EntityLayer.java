// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.amase.map;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.EntityConfiguration;
import afrl.cmasi.EntityState;
import afrl.cmasi.RemoveEntities;

import avtas.amase.objtree.ObjectTree;
import avtas.amase.ui.AircraftColors;
import avtas.amase.ui.IconManager;
import avtas.amase.scenario.ScenarioEvent;
import avtas.amase.scenario.ScenarioState;
import avtas.amase.setup.SelectObjectEvent;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import avtas.map.graphics.MapGraphic;
import avtas.map.layers.GraphicsLayer;
import avtas.util.WindowUtils;
import avtas.xml.Element;
import avtas.xml.XMLUtil;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * Shows entities (defined by {@link EntityConfiguration}) and aircraft
 * ({@link AirVehicleConfiguration})
 *
 * @author AFRL/RQQD
 */
public class EntityLayer extends GraphicsLayer<MapGraphic> implements AppEventListener {

    boolean showEntities = true;
    boolean showAircraft = true;
    List<Long> excludes = new ArrayList<>();

    public EntityLayer() {
        AppEventManager.getDefaultEventManager().addListener(this);
    }

    @Override
    public void setConfiguration(Element node) {
        super.setConfiguration(node);
        showEntities = XMLUtil.getBool(node, "ShowEntities", showEntities);
        showAircraft = XMLUtil.getBool(node, "ShowAircraft", showAircraft);
        List<Element> excludeEls = XMLUtil.getChildren(node, "Excludes");
        for (Element el : excludeEls) {
            if (el.getName().equals("ID")) {
                excludes.add(Long.valueOf(el.getText()));
            }
        }

    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof AirVehicleConfiguration && showAircraft) {
            AirVehicleConfiguration ec = (AirVehicleConfiguration) event;
            if (excludes.contains(ec.getID())) {
                return;
            }
            Color color = AircraftColors.getColor(ec.getID());

            EntityGraphic g = new EntityGraphic(ec, IconManager.getIcon(ec), color);

            MapGraphic oldGraphic = getByRefObject(ec.getID());
            if (oldGraphic != null) {
                remove(oldGraphic);
            }

            add(g);
            refresh();

        } else if (event instanceof AirVehicleState) {
            AirVehicleState avs = (AirVehicleState) event;
            EntityGraphic eg = (EntityGraphic) getByRefObject(avs.getID());
            if (eg == null) {
//                AirVehicleConfiguration avc = ScenarioState.getAirVehicleConfig(avs.getID());
//                if (avc != null) {
//                    eventOccurred(avc);
//                    eventOccurred(event);
//                    return;
//                }
            } else {
                eg.update(avs);
                refresh();
            }
        } else if (event instanceof EntityConfiguration && showEntities) {
            EntityConfiguration ec = (EntityConfiguration) event;

            if (excludes.contains(ec.getID())) {
                return;
            }

            Color color = AircraftColors.getDefaultColor();

            EntityGraphic g = new EntityGraphic(ec, IconManager.getIcon(ec), color);

            MapGraphic oldGraphic = getByRefObject(ec.getID());
            if (oldGraphic != null) {
                remove(oldGraphic);
            }
            if (g != null) {
                add(g);
                refresh();
            }

        } else if (event instanceof EntityState) {
            EntityState es = (EntityState) event;
            EntityGraphic eg = (EntityGraphic) getByRefObject(es.getID());
            if (eg == null) {
//                EntityConfiguration avc = ScenarioState.getEntityConfig(es.getID());
//                if (avc != null) {
//                    eventOccurred(avc);
//                    eventOccurred(event);
//                    return;
//                }
            } else {
                eg.update(es);
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
            refresh();
        }
    }

    @Override
    public void addPopupMenuItems(javax.swing.JPopupMenu menu, MouseEvent e, double lat, double lon) {
        for (final MapGraphic g : this) {
            if (g.onEdge(e.getX(), e.getY(), 4) || g.contains(e.getPoint())) {
                menu.add(new JMenuItem(new AbstractAction("Entity " + g.getRefObject()) {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        JTabbedPane pane = new JTabbedPane();
                        long id = (Long) g.getRefObject();
                        Object ec = ScenarioState.getEntityConfig(id);
                        Object es = null;
                        if (ec != null) {
                            es = ScenarioState.getEntityState(id);
                        } else {
                            ec = ScenarioState.getAirVehicleConfig(id);
                            es = ScenarioState.getAirVehicleState(id);
                        }

                        ObjectTree tree = new ObjectTree(ec);
                        tree.setEditable(false);
                        pane.addTab("Config", new JScrollPane(tree));
                        tree = new ObjectTree(es);
                        tree.setEditable(false);
                        pane.addTab("State", new JScrollPane(tree));

                        WindowUtils.showPlainDialog(getMap(), pane, "Entity", false);

                    }

                }));
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e, double lat, double lon) {
        for (final MapGraphic g : this) {
            if (g.onEdge(e.getX(), e.getY(), 4) || g.contains(e.getPoint())) {
                long id = (Long) g.getRefObject();

                Object ec = ScenarioState.getEntityConfig(id);
                if (ec == null) {
                    ec = ScenarioState.getAirVehicleConfig(id);
                }
                
                AppEventManager.getDefaultEventManager().fireEvent(new SelectObjectEvent(ec));
            } 
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */