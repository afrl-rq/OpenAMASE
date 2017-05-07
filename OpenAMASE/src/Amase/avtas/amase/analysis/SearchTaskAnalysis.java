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
import avtas.app.Context;
import avtas.app.ContextListener;
import avtas.terrain.TerrainService;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import avtas.xml.Element;

/**
 * Collects and presents information on the viewing of search areas, lines, and points.  
 * <br>
 * This should be used in conjuction with an <code>AnalysisManager</code> to collect statistics on a scenario.
 * 
 * The grid resolution can be adjusted in the simulation configuration file by setting the following XML node:
 * <Simulation><Analysis><SearchTaskAnalyis><GridResolution>  The value is floating-point meters resolution of the grid
 * square used in breaking down a search area or line.  
 * 
 * @author AFRL/RQQD
 */
public class SearchTaskAnalysis implements AnalysisClient, ContextListener {

    private double resolutionMeter = 20;
    private DecimalFormat format = new DecimalFormat("#.##");
    HashMap<CameraModel.CameraIndex, CameraModel> cameraMap = new HashMap<>();
    HashMap<Long, SearchGraphic> graphicMap = new HashMap<>();
    double time, lastTime = 0;

    /**
     * Initializes the analysis module, clearing all recorded data. This is
     * typically called when initializing or resetting a scenario.
     */
    public void initScenario() {
        graphicMap.clear();
    }
    
    

    /** {@inheritDoc} */
    @Override
    public void eventOccurred(Object evt) {
        if (evt instanceof SearchTask) {
            SearchTask task = (SearchTask) evt;

            graphicMap.remove( task.getTaskID() );

            //if (!contains(task.getTaskID())) {
                //Area Searches
                if (task instanceof AreaSearchTask) {
                    AreaSearchHighlight g = new AreaSearchHighlight(resolutionMeter, (AreaSearchTask) task);
                    graphicMap.put(task.getTaskID(), g);
                    g.setRefObject(task.getTaskID());
                }
                //Line Searches
                else if (task instanceof LineSearchTask) {
                    LinearSearchHighlight g = new LinearSearchHighlight(resolutionMeter, (LineSearchTask) task);
                    graphicMap.put(task.getTaskID(), g);
                    g.setRefObject(task.getTaskID());
                }
                //Point Search
                else if (task instanceof PointSearchTask) {
                    PointSearchHighlight g = new PointSearchHighlight((PointSearchTask) task);
                    graphicMap.put(task.getTaskID(), g);
                    g.setRefObject(task.getTaskID());
                }
            //}
        }
        else if (evt instanceof AirVehicleState) {
            if (time - lastTime < CHECK_TIME && time != lastTime) {
                return;
            }
            this.lastTime = time;
            AirVehicleState avs = (AirVehicleState) evt;
            checkSensors(avs);
        }
        else if (evt instanceof AirVehicleConfiguration) {
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
        }
        else if (evt instanceof SessionStatus) {
            SessionStatus ss = (SessionStatus) evt;
            this.time = ss.getScenarioTime() / 1000d;
        }
    }

    /**
     * Checks if any sensors attached to <code>avs</code> are currently in range
     * of a search task and, if so, updates that task accordingly.
     * <br>
     * In general, this is not called directly, but is called from {@link #eventOccured(AppEvent evt) }
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
                for (SearchGraphic g : graphicMap.values()) {
                    g.processSensor(avs, model, aglAlt);
                }
            }
        }
    }

    /**
     * Gets the XML analysis report as a string.
     * @return XML analysis report in string format.
     */
    public String getAnalysisReport() {
        try {
            return getAnalysisReportXML().toXML();
        } catch (Exception ex) {
            Logger.getLogger(SearchTaskAnalysis.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    /** {@inheritDoc} */
    @Override
    public Element getAnalysisReportXML() {

        Element el = new Element("SearchTaskAnalysis");

        for (SearchGraphic g : graphicMap.values()) {

            SearchGraphic sg = (SearchGraphic) g;
            AreaSearchHighlight.SearchPixel[][] pixels = sg.getPixels();
            int numPix = 0;
            int pixSeen = 0;
            double timeSeen = 0;
            for (int i = 0; i < pixels.length; i++) {
                for (int j = 0; j < pixels[i].length; j++) {
                    AreaSearchHighlight.SearchPixel searchPixel = pixels[i][j];
                    if (searchPixel != null) {
                        numPix++;
                        timeSeen += searchPixel.totalTimeSeen;
                        if (searchPixel.seen) {
                            pixSeen++;
                        }
                    }
                }
            }
            double pct = (numPix > 0 ? (double) pixSeen / (double) numPix : 0) * 100;
            if (sg instanceof AreaSearchHighlight) {
                Element subNode = new Element("SearchArea");
                el.add(subNode);
                subNode.setAttribute("ID", String.valueOf(sg.getTask().getTaskID()));
                subNode = (Element) subNode.add(new Element("CoveragePercent"));
                subNode.setText(format.format(pct));
            } else if (sg instanceof LinearSearchHighlight) {
                Element subNode = new Element("SearchLine");
                el.add(subNode);
                subNode.setAttribute("ID", String.valueOf(sg.getTask().getTaskID()));
                subNode = (Element) subNode.add(new Element("CoveragePercent"));
                subNode.setText(format.format(pct));
            } else if (sg instanceof PointSearchHighlight) {
                Element subNode = new Element("SearchPoint");
                el.add(subNode);
                subNode.setAttribute("ID", String.valueOf(sg.getTask().getTaskID()));
                subNode = (Element) subNode.add(new Element("TimeSeenSec"));
                subNode.setText(String.valueOf(format.format(timeSeen)));
            }
        }
        return el;
    }

    @Override
    public void resetAnalysis() {
        initScenario();
    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
        resolutionMeter = XMLUtil.getDouble(xml, "Analysis/SearchTaskAnalysis/GridResolution", resolutionMeter);
    }

    @Override
    public void applicationPeerAdded(Object peer) {
    }

    @Override
    public void applicationPeerRemoved(Object peer) {
    }

    @Override
    public void initializeComplete() {
    }

    @Override
    public boolean requestShutdown() {
        return true;
    }

    @Override
    public void shutdown() {
        
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */