// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.setup.map;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.AltitudeType;
import afrl.cmasi.EntityConfiguration;
import afrl.cmasi.EntityState;
import afrl.cmasi.Location3D;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import avtas.amase.ui.AircraftColors;
import avtas.amase.setup.RemoveObjectsEvent;
import avtas.amase.setup.ToolbarEvent;
import avtas.amase.setup.SelectObjectEvent;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import avtas.lmcp.LMCPObject;
import avtas.map.MapMouseListener;
import avtas.map.MapPopupListener;
import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapGraphicsList;
import avtas.map.layers.GraphicsLayer;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import avtas.xml.Element;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

/**
 * Implements a Map Layer that allows users to drag aircraft around.
 *
 * @author AFRL/RQQD
 */
public class EntityLayer extends GraphicsLayer<MapGraphic> implements MapMouseListener,
        AppEventListener, MapPopupListener {

    MapGraphicsList<EntitySetupGraphic> avGraphicList = new MapGraphicsList<EntitySetupGraphic>();
    private AppEventManager eventManager = null;
    private ToolbarEvent toolbarEvent = null;

    public EntityLayer() {
        eventManager = AppEventManager.getDefaultEventManager();
        eventManager.addListener(this);
        add(avGraphicList);
    }

    protected void addAircraft(AirVehicleConfiguration avc) {
        for (EntitySetupGraphic g : avGraphicList) {
            if (g.getAirVehicleConfig() == avc || g.getId() == avc.getID()) {
                g.setConfiguration(avc);
                if (g.getAirVehicleState() != null) {
                    g.update(g.getAirVehicleState());
                }
                refresh();
                return;
            }
        }

        EntitySetupGraphic g = new EntitySetupGraphic(avc, AircraftColors.getColor(avc.getID()));
        setupPropertyChangeListener(g);
        avGraphicList.add(g);
        refresh();
    }

    protected void updateAircraft(AirVehicleState avs) {
        for (EntitySetupGraphic g : avGraphicList) {
            if (g.getAirVehicleConfig() != null) {
                if (g.getAirVehicleConfig().getID() == avs.getID()) {
                    g.update(avs);
                    refresh();
                }
                else if (g.getAirVehicleState() == avs) {
                    g.update(avs);
                    g.getAirVehicleConfig().setID(avs.getID());
                    g.setConfiguration(g.getAirVehicleConfig());
                    refresh();
                }
            }
        }
    }

    protected void addEntity(EntityConfiguration ec) {
        for (EntitySetupGraphic g : avGraphicList) {
            if (g.getEntityConfig() == ec || g.getId() == ec.getID()) {
                g.setConfiguration(ec);
                if (g.getEntityState() != null) {
                    g.update(g.getEntityState());
                }
                refresh();
                return;
            }
        }

        EntitySetupGraphic g = new EntitySetupGraphic(ec, Color.WHITE);
        setupPropertyChangeListener(g);
        avGraphicList.add(g);
        refresh();
    }

    protected void updateEntity(EntityState es) {
        for (EntitySetupGraphic g : avGraphicList) {
            if (g.getEntityConfig() != null) {
                if (g.getEntityConfig().getID() == es.getID()) {
                    g.update(es);
                    refresh();
                }
                else if (g.getEntityState() == es) {
                    g.update(es);
                    g.getEntityConfig().setID(es.getID());
                    g.setConfiguration(g.getEntityConfig());
                    refresh();
                }
            }
        }
    }

    void setupPropertyChangeListener(EntitySetupGraphic g) {
        g.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                switch (evt.getPropertyName()) {
                    case EntitySetupGraphic.CONFIG_CHANGED:
                        if (eventManager != null) {
                            EntitySetupGraphic g = (EntitySetupGraphic) evt.getNewValue();
                            if (g.getAirVehicleConfig() != null) {
                                eventManager.fireEvent(g.getAirVehicleConfig());
                                eventManager.fireEvent(new SelectObjectEvent(g.getAirVehicleConfig()));
                            }
                            else if (g.getEntityConfig() != null) {
                                eventManager.fireEvent(g.getEntityConfig());
                                eventManager.fireEvent(new SelectObjectEvent(g.getEntityConfig()));
                            }
                        }
                        break;
                    case EntitySetupGraphic.STATE_CHANGED:
                        if (eventManager != null) {
                            EntitySetupGraphic g = (EntitySetupGraphic) evt.getNewValue();
                            if (g.getAirVehicleState() != null) {
                                eventManager.fireEvent(g.getAirVehicleState());
                                eventManager.fireEvent(new SelectObjectEvent(g.getAirVehicleState()));
                            }
                            else if (g.getEntityState() != null) {
                                eventManager.fireEvent(g.getEntityState());
                                eventManager.fireEvent(new SelectObjectEvent(g.getEntityState()));
                            }
                        }
                        break;
                    case EntitySetupGraphic.DELETE_AIRCRAFT:
                        if (eventManager != null) {
                            EntitySetupGraphic g = (EntitySetupGraphic) evt.getNewValue();
                            if (g.getAirVehicleConfig() != null)
                                eventManager.fireEvent(new RemoveObjectsEvent(g.getAirVehicleConfig(), g.getAirVehicleState()));
                            else
                                eventManager.fireEvent(new RemoveObjectsEvent(g.getEntityConfig(), g.getEntityState()));
                        }
                        break;
                    case EntitySetupGraphic.SELECT_AIRCRAFT:
                        EntitySetupGraphic g = (EntitySetupGraphic) evt.getNewValue();
                        if (g.getAirVehicleConfig() != null)
                            eventManager.fireEvent(new SelectObjectEvent(g.getAirVehicleConfig()));
                        else
                            eventManager.fireEvent(new SelectObjectEvent(g.getEntityConfig()));
                        break;
                }
            }
        });
    }

    @Override
    public void setConfiguration(Element node) {
        //TrailGraphic.NUMPOINTS = XMLUtil.getInt(node, "TrailLength", TrailGraphic.NUMPOINTS);
        avGraphicList.clear();
    }

    @Override
    public void mouseMoved(MouseEvent e, double lat, double lon) {
        for (EntitySetupGraphic g : avGraphicList) {
            g.mouseMoved(e, lat, lon);
            if (e.isConsumed()) {
                break;
            }
        }
        refresh();
    }

    @Override
    public void mouseClicked(MouseEvent e, double lat, double lon) {
        if (toolbarEvent != null) {
            if (toolbarEvent.getObject() instanceof AirVehicleConfiguration) {
                AirVehicleConfiguration avc = (AirVehicleConfiguration) toolbarEvent.getObject();
                AirVehicleState avs = new AirVehicleState();
                avs.setLocation(new Location3D(lat, lon, 0, AltitudeType.MSL));
                createNewAircraft(avc, avs);
                toolbarEvent = null;
                if (eventManager != null) {
                    eventManager.fireEvent(new SelectObjectEvent(avc));
                }
            }
            else if (toolbarEvent.getObject() instanceof EntityConfiguration) {
                EntityConfiguration ec = (EntityConfiguration) toolbarEvent.getObject();
                EntityState es = new EntityState();
                es.setLocation(new Location3D(lat, lon, 0, AltitudeType.AGL));
                createNewEntity(ec, es);
                toolbarEvent = null;
                if (eventManager != null) {
                    eventManager.fireEvent(new SelectObjectEvent(ec));
                }
            }
        }
        else {
            for (EntitySetupGraphic g : avGraphicList) {
                g.mouseClicked(e, lat, lon);
            }
        }
        refresh();
    }

    @Override
    public void mouseDragged(MouseEvent e, double lat, double lon) {
        for (EntitySetupGraphic g : avGraphicList) {
            g.mouseDragged(e, lat, lon);
            if (e.isConsumed()) {
                break;
            }
        }
        refresh();
    }

    @Override
    public void mousePressed(MouseEvent e, double lat, double lon) {
        if (toolbarEvent == null) {
            for (EntitySetupGraphic g : avGraphicList) {
                g.mousePressed(e, lat, lon);
                if (e.isConsumed()) {
                    break;
                }
            }
        }
        refresh();
    }

    @Override
    public void mouseReleased(MouseEvent e, double lat, double lon) {
        for (EntitySetupGraphic g : avGraphicList) {
            g.mouseReleased(e, lat, lon);
            if (e.isConsumed()) {
                break;
            }
        }
        refresh();
    }

    @Override
    public void eventOccurred(Object evt) {

        if (evt instanceof AirVehicleConfiguration) {
            addAircraft((AirVehicleConfiguration) evt);
        }
        else if (evt instanceof AirVehicleState) {
            updateAircraft((AirVehicleState) evt);
        }
        else if (evt instanceof EntityConfiguration) {
            addEntity((EntityConfiguration) evt);
        }
        else if (evt instanceof EntityState) {
            updateEntity((EntityState) evt);
        }
        else if (evt instanceof SessionStatus) {
            if (((SessionStatus) evt).getState() == SimulationStatusType.Reset) {
                avGraphicList.clear();
            }
        }
        else if (evt instanceof ToolbarEvent) {
            ToolbarEvent tbEvent = (ToolbarEvent) evt;
            if (tbEvent.getObject() instanceof AirVehicleConfiguration) {
                this.toolbarEvent = tbEvent;
            }
            else if (tbEvent.getObject() instanceof EntityConfiguration) {
                this.toolbarEvent = tbEvent;
            }
            else if (tbEvent.getObject() == null) {
                this.toolbarEvent = null;
            }
        }
        else if (evt instanceof SelectObjectEvent) {
            Object obj = ((SelectObjectEvent) evt).getObject();
            if (obj instanceof LMCPObject) {
                for (EntitySetupGraphic g : avGraphicList) {
                    if (obj == g.getAirVehicleConfig() || obj == g.getAirVehicleState()
                            || obj == g.getEntityConfig() || obj == g.getEntityState()) {
                        g.setSelected(true);
                    }
                    else {
                        g.setSelected(false);
                    }
                }
            }
            refresh();
        }
        else if (evt instanceof RemoveObjectsEvent) {
            RemoveObjectsEvent roe = (RemoveObjectsEvent) evt;

            for (Object obj : roe.getObjects()) {
                for (EntitySetupGraphic g : avGraphicList) {
                    if (obj == g.getAirVehicleConfig() || obj == g.getAirVehicleState()
                            || obj == g.getEntityConfig() || obj == g.getEntityState()) {
                        avGraphicList.remove(g);
                        refresh();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void addPopupMenuItems(JPopupMenu menu, MouseEvent e, double lat, double lon) {

        for (EntitySetupGraphic g : avGraphicList) {
            if (g.getBounds() != null && g.getBounds().contains(e.getPoint())) {
                g.addPopupMenuItems(menu, e, lat, lon);
            }
        }

        final JMenu newAcItem = new JMenu("Add New Aircraft");
        createNewAircraftMenu(newAcItem, lat, lon);
        menu.add(newAcItem);

        final JMenu newEntityItem = new JMenu("Add New Entity");
        createNewEntityMenu(newEntityItem, lat, lon);
        menu.add(newEntityItem);
    }

    private void createNewAircraft(AirVehicleConfiguration avc, AirVehicleState avs) {
        TreeSet<Long> ids = new TreeSet<Long>();
        for (EntitySetupGraphic g : avGraphicList) {
            ids.add(g.getId());
        }
        long id = ids.isEmpty() ? 1 : ids.last() + 1;

        avc.setID(id);
        avs.setID(id);
        if (eventManager != null) {
            eventManager.fireEvent(avc);
            eventManager.fireEvent(avs);
        }

    }

    private void createNewEntity(EntityConfiguration ec, EntityState es) {
        TreeSet<Long> ids = new TreeSet<Long>();
        for (EntitySetupGraphic g : avGraphicList) {
            ids.add(g.getId());
        }
        long id = ids.isEmpty() ? 1 : ids.last() + 1;

        ec.setID(id);
        es.setID(id);
        if (eventManager != null) {
            eventManager.fireEvent(ec);
            eventManager.fireEvent(es);
        }

    }

    private void createNewAircraftMenu(JMenu newAcItem, final double lat, final double lon) {

        newAcItem.add(new JMenuItem(new AbstractAction("<New>") {
            @Override
            public void actionPerformed(ActionEvent e) {
                AirVehicleConfiguration avc = new AirVehicleConfiguration();
                AirVehicleState avs = new AirVehicleState();
                avs.getLocation().setLatitude(lat);
                avs.getLocation().setLongitude(lon);
                createNewAircraft(avc, avs);
            }
        }));

        for (final EntitySetupGraphic g : avGraphicList) {
            final AirVehicleConfiguration avc = g.getAirVehicleConfig();
            if (avc != null) {
                newAcItem.add(new JMenuItem(new AbstractAction("Based on " + avc.getLabel() + " (" + avc.getID() + ")") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        AirVehicleConfiguration newAvc = avc.clone();
                        AirVehicleState newAvs = g.getAirVehicleState();
                        if (newAvs == null) {
                            newAvs = new AirVehicleState();
                        }
                        newAvs = newAvs.clone();
                        newAvs.getLocation().setLatitude(lat);
                        newAvs.getLocation().setLongitude(lon);
                        createNewAircraft(newAvc, newAvs);
                    }
                }));
            }
        }
    }

    private void createNewEntityMenu(JMenu newAcItem, final double lat, final double lon) {

        newAcItem.add(new JMenuItem(new AbstractAction("<New>") {
            @Override
            public void actionPerformed(ActionEvent e) {
                EntityConfiguration avc = new EntityConfiguration();
                EntityState avs = new EntityState();
                avs.getLocation().setLatitude(lat);
                avs.getLocation().setLongitude(lon);
                createNewEntity(avc, avs);
            }
        }));

        for (final EntitySetupGraphic g : avGraphicList) {
            final EntityConfiguration avc = g.getEntityConfig();
            if (avc != null) {
                newAcItem.add(new JMenuItem(new AbstractAction("Based on " + avc.getLabel() + " (" + avc.getID() + ")") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        EntityConfiguration newAvc = avc.clone();
                        EntityState newAvs = g.getEntityState();
                        if (newAvs == null) {
                            newAvs = new EntityState();
                        }
                        newAvs = newAvs.clone();
                        newAvs.getLocation().setLatitude(lat);
                        newAvs.getLocation().setLongitude(lon);
                        createNewEntity(newAvc, newAvs);
                    }
                }));
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */