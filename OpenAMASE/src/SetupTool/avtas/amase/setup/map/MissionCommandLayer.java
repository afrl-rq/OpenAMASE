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
import afrl.cmasi.Location3D;
import afrl.cmasi.LoiterAction;
import afrl.cmasi.MissionCommand;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import afrl.cmasi.VehicleAction;
import afrl.cmasi.VehicleActionCommand;
import afrl.cmasi.Waypoint;
import avtas.amase.map.LoiterGraphic;
import avtas.amase.objtree.ObjectTree;
import avtas.amase.ui.AircraftColors;
import avtas.amase.util.CmasiUtils;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import avtas.util.ReflectionUtils;
import avtas.map.MapMouseListener;
import avtas.map.MapPopupListener;
import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapGraphicsList;
import avtas.map.graphics.MapMarker;
import avtas.map.graphics.MapPoly;
import avtas.map.layers.GraphicsLayer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 *
 * @author AFRL/RQQD
 */
public class MissionCommandLayer extends GraphicsLayer<MapGraphic> implements AppEventListener,
        MapMouseListener, MapPopupListener {

    //HashMap<Long, MapGraphicsList<MapGraphic>> graphicsMap = new HashMap<Long, MapGraphicsList<MapGraphic>>();
    MapGraphicsList<MapGraphicsList> missionList = new MapGraphicsList<MapGraphicsList>();
    List<Long> vehicleIdList = new ArrayList<Long>();
    private AppEventManager eventManager;
    //private MissionCommand activeMission = null;
    private MapGraphic dragGraphic = null;
    private Object dragCommand = null;

    public MissionCommandLayer() {
        this.eventManager = AppEventManager.getDefaultEventManager();
        add(missionList);
    }

    public void eventOccurred(Object event) {
        if (event instanceof AirVehicleConfiguration) {
            AirVehicleConfiguration avc = (AirVehicleConfiguration) event;
            if (!vehicleIdList.contains(avc.getID())) {
                vehicleIdList.add(avc.getID());
            }
        }

        if (event instanceof MissionCommand) {
            MissionCommand mc = (MissionCommand) event;
            MapGraphic g = missionList.getByRefObject(event);
            if (g != null) {
                missionList.remove(g);
            }
            MapGraphicsList routeGraphic = drawRoute(mc);
            missionList.add(routeGraphic);
            refresh();
        }
        if (event instanceof VehicleActionCommand) {
            VehicleActionCommand vc = (VehicleActionCommand) event;
            MapGraphic g = missionList.getByRefObject(event);
            if (g != null) {
                missionList.remove(g);
            }
            MapGraphicsList routeGraphic = drawAction(vc);
            missionList.add(routeGraphic);
            refresh();
        }
        if (event instanceof SessionStatus) {
            if (((SessionStatus) event).getState() == SimulationStatusType.Reset) {
                missionList.clear();
                vehicleIdList.clear();
                refresh();
            }
        }

    }

    protected MapGraphicsList drawAction(VehicleActionCommand vc) {
        MapGraphicsList<MapGraphic> missionGraphic = new MapGraphicsList();
        missionGraphic.setRefObject(vc);
        drawActions(missionGraphic, vc.getVehicleActionList());
        missionGraphic.setPainter(AircraftColors.getColor(vc.getVehicleID()), 1);
        refresh();
        return missionGraphic;
    }

    protected MapGraphicsList drawRoute(MissionCommand mc) {

        MapGraphicsList<MapGraphic> missionGraphic = new MapGraphicsList();
        missionGraphic.setRefObject(mc);

        for (int i = 0; i < mc.getWaypointList().size(); i++) {
            Waypoint wp = mc.getWaypointList().get(i);
            MapMarker mp = new MapMarker(wp.getLatitude(), wp.getLongitude());
            mp.setRefObject(wp);
            mp.setName("Waypoint: " + wp.getNumber());
            mp.setFill(Color.WHITE);
            missionGraphic.add(mp);
            drawActions(missionGraphic, wp.getVehicleActionList());
        }
        // draw waypoint line
        Waypoint wp = CmasiUtils.getWaypoint(mc, mc.getFirstWaypoint());
        int numWps = mc.getWaypointList().size();
        int i = 0;
        if (wp != null) {
            MapPoly polyGraphic = new MapPoly();
            polyGraphic.addPoint(wp.getLatitude(), wp.getLongitude());
            //polyGraphic.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
            missionGraphic.add(0, polyGraphic);
            polyGraphic.setRefObject(mc);

            while (wp != null && i < numWps) {
                Waypoint nextWp = CmasiUtils.getWaypoint(mc, wp.getNextWaypoint());
                if (nextWp == null || nextWp == wp) {
                    break;
                }
                polyGraphic.addPoint(nextWp.getLatitude(), nextWp.getLongitude());
                wp = nextWp;
                i++;
            }
        }

        missionGraphic.setPainter(AircraftColors.getColor(mc.getVehicleID()), 1);
        missionGraphic.setRefObject(mc);
        refresh();
        return missionGraphic;
    }

    protected void drawActions(MapGraphicsList<MapGraphic> missionGraphic, List<VehicleAction> actionList) {
        for (int j = 0; j < actionList.size(); j++) {
            if (actionList.get(j) instanceof LoiterAction) {
                LoiterAction a = (LoiterAction) actionList.get(j);
                LoiterGraphic lg = new LoiterGraphic(a);
                lg.setRefObject(a);
                missionGraphic.add(lg);
            }
        }
    }

    // mouse dragging functions
    public void mouseMoved(MouseEvent e, double lat, double lon) {
    }

    public void mouseClicked(MouseEvent e, double lat, double lon) {
    }

    public void mouseDragged(MouseEvent e, double lat, double lon) {
        if (dragGraphic instanceof MapMarker) {
            ((MapMarker) dragGraphic).setLat(lat);
            ((MapMarker) dragGraphic).setLon(lon);
            if (dragGraphic.getRefObject() instanceof Location3D) {
                Location3D loc = (Location3D) dragGraphic.getRefObject();
                loc.setLatitude(lat);
                loc.setLongitude(lon);
            }
        }

        eventOccurred(dragCommand);
    }

    @Override
    public void mousePressed(MouseEvent e, double lat, double lon) {
        dragGraphic = getLocationGraphic(missionList, e.getX(), e.getY());
        dragCommand = getAssociatedObject(dragGraphic, MissionCommand.class, VehicleActionCommand.class);
    }

    @Override
    public void mouseReleased(MouseEvent e, double lat, double lon) {
        if (dragCommand != null && eventManager != null) {
            eventManager.fireEvent(dragCommand, this);
        }
        dragGraphic = null;
        dragCommand = null;
    }

    @Override
    public void addPopupMenuItems(javax.swing.JPopupMenu menu, MouseEvent e, final double lat, final double lon) {
        JMenu addWpMenu = new JMenu("Add Waypoint");
        menu.add(addWpMenu);

        for (Long id : vehicleIdList) {

            MissionCommand tmp_cmd = null;
            for (MapGraphic g : missionList) {
                if (g.getRefObject() instanceof MissionCommand) {
                    tmp_cmd = (MissionCommand) g.getRefObject();
                    if (tmp_cmd.getVehicleID() != id) {
                        tmp_cmd = null;
                        continue;
                    } else {
                        break;
                    }
                }
            }

            if (tmp_cmd == null) {
                tmp_cmd = new MissionCommand();
                tmp_cmd.setVehicleID(id);
            }

            final MissionCommand mc = tmp_cmd;

            JMenuItem wpMenu = new JMenuItem("to Vehicle " + mc.getVehicleID());
            wpMenu.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Waypoint wp = getLastWaypoint(mc);
                    Waypoint newWp = null;
                    if (wp != null) {
                        newWp = wp.clone();
                    } else {
                        newWp = new Waypoint();
                    }
                    newWp.setLatitude(lat);
                    newWp.setLongitude(lon);
                    newWp.setNumber(getUniqueWaypointNumber(mc));
                    if (wp == null) {
                        mc.setFirstWaypoint(newWp.getNumber());
                    } else {
                        wp.setNextWaypoint(newWp.getNumber());
                    }
                    mc.getWaypointList().add(newWp);
                    if (eventManager != null) {
                        eventManager.fireEvent(mc, null);
                    }
                }
            });
            addWpMenu.add(wpMenu);
        }

        MapGraphic g = getLocationGraphic(missionList, e.getX(), e.getY());

        if (g != null) {

            Location3D loc = (Location3D) g.getRefObject();

            if (loc instanceof Waypoint) {

                final Waypoint wp = (Waypoint) loc;
                final MissionCommand mc = (MissionCommand) g.getParent().getRefObject();

                JMenuItem delMenu = new JMenuItem("Delete Waypoint");
                delMenu.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {

                        ArrayList<Waypoint> affectedWps = getByNextWaypoint(mc, wp.getNumber());
                        for (Waypoint awp : affectedWps) {
                            awp.setNextWaypoint(wp.getNextWaypoint());
                        }
                        mc.getWaypointList().remove(wp);
                        if (eventManager != null) {
                            eventManager.fireEvent(mc, null);
                        }
                    }
                });
                menu.add(delMenu);

                JMenu missionAltMenu = new JMenu("Set Mission Alt (m): ");
                final JFormattedTextField altField = new JFormattedTextField(0);
                altField.setColumns(10);
                missionAltMenu.add(altField);
                altField.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for (Waypoint wp : mc.getWaypointList()) {
                            wp.setAltitude(((Number) altField.getValue()).floatValue());
                        }
                        if (eventManager != null) {
                            eventManager.fireEvent(mc, null);
                        }
                    }
                });
                menu.add(missionAltMenu);

                final JMenuItem editWpItem = new JMenuItem("Edit Waypoint " + wp.getNumber());
                editWpItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        Object newWp = ObjectTree.showEditWindow(wp.clone(), editWpItem, "Edit Waypoint " + wp.getNumber());
                        if (newWp != null) {
                            ReflectionUtils.copyFields(newWp, wp);
                            if (eventManager != null) {
                                eventManager.fireEvent(mc, null);
                            }
                        }
                    }
                });
                menu.add(editWpItem);

                final JMenuItem editMissionItem = new JMenuItem("Edit Mission Command " + mc.getVehicleID());
                editMissionItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        Object newMC = ObjectTree.showEditWindow(mc.clone(), editMissionItem, "Edit MissionCommand " + mc.getVehicleID());
                        if (newMC != null && eventManager != null) {
                            eventManager.fireEvent(newMC, null);
                        }

                    }
                });
                menu.add(editMissionItem);

                // if this is a loiter action, then allow edit of loiter
            } else if (getAssociatedObject(g, LoiterAction.class) != null) {

                final LoiterAction la = (LoiterAction) getAssociatedObject(g, LoiterAction.class);
                final Object command = getAssociatedObject(g, MissionCommand.class, VehicleActionCommand.class);

                final JMenuItem editLoiterItem = new JMenuItem("Edit Loiter Action");
                editLoiterItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        Object obj = ObjectTree.showEditWindow(la.clone(), editLoiterItem, "Edit Loiter Action ");
                        if (obj != null && eventManager != null) {
                            ReflectionUtils.copyFields(obj, la);
                            eventManager.fireEvent(command, null);
                        }
                    }
                });
                menu.add(editLoiterItem);
            }
        }
    }

    protected static Waypoint getLastWaypoint(MissionCommand mc) {
        int numTraversed = 0;
        Waypoint wp = CmasiUtils.getWaypoint(mc, mc.getFirstWaypoint());
        if (wp == null) {
            return null;
        }
        while (wp != null && wp.getNextWaypoint() != 0 && numTraversed < mc.getWaypointList().size()) {
            numTraversed++;
            wp = CmasiUtils.getWaypoint(mc, wp.getNextWaypoint());
        }
        return wp;
    }

    protected static ArrayList<Waypoint> getByNextWaypoint(MissionCommand mc, long nextWpNum) {
        ArrayList<Waypoint> retList = new ArrayList<Waypoint>();
        for (Waypoint wp : mc.getWaypointList()) {
            if (wp.getNextWaypoint() == nextWpNum) {
                retList.add(wp);
            }
        }
        return retList;
    }

    /**
     * Returns the lowest number that does not correspond to a waypoint ID in the mission command.
     * @param mc
     */
    protected static long getUniqueWaypointNumber(MissionCommand mc) {
        long val = 1;
        while (CmasiUtils.getWaypoint(mc, val) != null) {
            val++;
        }
        return val;
    }

    /** searches the graphics until a graphic with Location3D as a reference object is found.
     *  returns null if none is found.
     */
    protected MapGraphic getLocationGraphic(MapGraphicsList list, int x, int y) {
        List<MapGraphic> hitlist = list.getGraphicsWithin(x, y, 2);
        for (int i = 0; i < hitlist.size(); i++) {
            if (hitlist.get(i).getRefObject() instanceof Location3D) {
                return hitlist.get(i);
            }
            if (hitlist.get(i) instanceof MapGraphicsList) {
                MapGraphic g = getLocationGraphic((MapGraphicsList) hitlist.get(i), x, y);
                if (g != null) {
                    return g;
                }
            }
        }
        return null;
    }

    /**
     * Traverses the graphic hierarchy until a reference object of the requested
     * type is found.
     * is found.  If none is found, this returns null.
     * @param g graphic from which to get the assocated object
     * @param classes list of class types to match
     * @return the first object that matches a class type, or null if none is found.
     */
    protected Object getAssociatedObject(MapGraphic g, Class... classes) {
        List<Class> classList = Arrays.asList(classes);
        while (g != null) {
            Object ref = g.getRefObject();
            if (classList.contains(ref.getClass())) {
                return ref;
            }
            g = g.getParent();
        }
        return null;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */