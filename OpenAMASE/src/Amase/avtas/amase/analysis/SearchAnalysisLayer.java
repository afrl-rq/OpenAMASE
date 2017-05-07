// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.analysis;

import avtas.xml.XMLUtil;
import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.AreaSearchTask;
import afrl.cmasi.CameraConfiguration;
import afrl.cmasi.CameraState;
import afrl.cmasi.GimbalConfiguration;
import afrl.cmasi.GimbalState;
import afrl.cmasi.LineSearchTask;
import afrl.cmasi.PayloadConfiguration;
import afrl.cmasi.PayloadState;
import afrl.cmasi.PointSearchTask;
import afrl.cmasi.SearchTask;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import avtas.amase.scenario.ScenarioState;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import avtas.terrain.TerrainService;
import avtas.map.graphics.MapGraphic;
import avtas.map.layers.GraphicsLayer;
import avtas.swing.UserNotice;
import java.util.HashMap;
import avtas.xml.Element;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 * Collects and presents information on the viewing of search areas, lines, and
 * points.
 * <br>
 * This should be used in conjuction with an
 * <code>AnalysisManager</code> to collect statistics on a scenario.
 *
 * The grid resolution can be adjusted in the simulation configuration file by
 * setting the following XML node:
 * <Simulation><Analysis><SearchTaskAnalyis><GridResolution> The value is
 * floating-point meters resolution of the grid square used in breaking down a
 * search area or line.
 *
 * @author AFRL/RQQD
 */
public class SearchAnalysisLayer extends GraphicsLayer<MapGraphic> implements AppEventListener {

    private double resolutionMeter = 20;
    HashMap<CameraModel.CameraIndex, CameraModel> cameraMap = new HashMap<>();
    //double time, lastTime = 0;
    boolean analysisActive = false;
    
    JCheckBoxMenuItem activeAnalysisMenu;

    public SearchAnalysisLayer() {
        AppEventManager.getDefaultEventManager().addListener(this);
        
        activeAnalysisMenu = new JCheckBoxMenuItem(new AbstractAction("Use Live Analysis") {
            @Override
            public void actionPerformed(ActionEvent e) {
                analysisActive = activeAnalysisMenu.isSelected();
            }
        });
    }

    @Override
    public void setConfiguration(Element node) {
        resolutionMeter = XMLUtil.getDouble(node, "GridResolution", resolutionMeter);
    }

    @Override
    public void addPopupMenuItems(JPopupMenu popmenu, MouseEvent e, double lat, double lon) {
        JMenu menu = new JMenu("Search Analysis Layer");
        popmenu.add(menu);

        menu.add(activeAnalysisMenu);
        menu.add(new AbstractAction("Set Grid Resolution") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ans = JOptionPane.showInputDialog(getMap(), "Set Search Grid Resolition (m)", resolutionMeter);
                if (ans != null) {
                    try {
                        resolutionMeter = Double.parseDouble(ans);
                    } catch (NumberFormatException ex) {
                    }
                }
            }
        });
        menu.add(new AbstractAction("Run Analysis") {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
                UserNotice notice = new UserNotice("Performing Search Analysis", getMap());
                notice.setVisible(true);
                analysisActive = true;
                for (ScenarioState.EventWrapper w : ScenarioState.getEventList()) {
                   
                    eventOccurred(w.event);
                }
                analysisActive = false;
                refresh();
                notice.setVisible(false);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eventOccurred(Object evt) {

        if (evt instanceof SearchTask) {
            SearchTask task = (SearchTask) evt;

            remove(getByRefObject(task.getTaskID()));

            //if (!contains(task.getTaskID())) {
            //Area Searches
            if (task instanceof AreaSearchTask) {
                AreaSearchHighlight g = new AreaSearchHighlight(resolutionMeter, (AreaSearchTask) task);
                add(g);
                g.setRefObject(task.getTaskID());
            } //Line Searches
            else if (task instanceof LineSearchTask) {
                LinearSearchHighlight g = new LinearSearchHighlight(resolutionMeter, (LineSearchTask) task);
                add(g);
                g.setRefObject(task.getTaskID());
            } //Point Search
            else if (task instanceof PointSearchTask) {
                PointSearchHighlight g = new PointSearchHighlight((PointSearchTask) task);
                add(g);
                g.setRefObject(task.getTaskID());
            }
        } else if (analysisActive && evt instanceof AirVehicleState) {

            AirVehicleState avs = (AirVehicleState) evt;
            checkSensors(avs);
            refresh();
            
        } else if (evt instanceof AirVehicleConfiguration) {
            AirVehicleConfiguration avc = (AirVehicleConfiguration) evt;
            for (PayloadConfiguration pc : avc.getPayloadConfigurationList()) {
                if (pc instanceof CameraConfiguration) {
                    CameraConfiguration cc = (CameraConfiguration) pc;
                    for (PayloadConfiguration pc2 : avc.getPayloadConfigurationList()) {
                        if (pc2 instanceof GimbalConfiguration) {
                            GimbalConfiguration gc = (GimbalConfiguration) pc2;
                            if (gc.getContainedPayloadList().contains(cc.getPayloadID())) {
                                CameraModel model = new CameraModel(avc, cc, gc);
                                cameraMap.put(new CameraModel.CameraIndex(avc.getID(), cc.getPayloadID()), model);
                            }
                        }
                    }
                }
            }
        } else if (evt instanceof SessionStatus) {
            SessionStatus ss = (SessionStatus) evt;
            if (ss.getState() == SimulationStatusType.Reset) {
                clear();
            }
        }
    }

    /**
     * Checks if any sensors attached to
     * <code>avs</code> are currently in range of a search task and, if so,
     * updates that task accordlingly.
     * <br>
     * In general, this is not called directly, but is called from {@link #eventOccured(AppEvent evt)
     * }
     *
     * @param avs The air vehicle state to be checked.
     */
    private void checkSensors(AirVehicleState avs) {

        double aglAlt = avs.getLocation().getAltitude()
                - TerrainService.getElevation(avs.getLocation().getLatitude(), avs.getLocation().getLongitude());

        for (CameraModel model : cameraMap.values()) {
            if (model.getID() == avs.getID()) {
                for (PayloadState ps : avs.getPayloadStateList()) {
                    if (ps instanceof CameraState) {
                        CameraState cs = (CameraState) ps;
                        if (model.getCameraID() == cs.getPayloadID()) {
                            model.setCameraState(cs);
                        }
                    }
                    if (ps instanceof GimbalState) {
                        GimbalState gs = (GimbalState) ps;
                        if (gs.getPayloadID() == model.getGimbalConfig().getPayloadID()) {
                            model.setGimbalState(gs);
                        }
                    }
                }
                for (MapGraphic g : this) {
                    if (g instanceof SearchGraphic) {
                        ((SearchGraphic) g).processSensor(avs, model, aglAlt);
                    }
                }
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */