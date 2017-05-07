// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.analysis;

import avtas.amase.util.CmasiUtils;
import avtas.xml.XMLUtil;
import avtas.amase.scenario.ScenarioEvent;
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.KeepOutZone;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import avtas.lmcp.LMCPObject;
import java.awt.geom.Path2D;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Vector;
import avtas.xml.Element;

/**
 * The <code>KeepOutZoneAnalysis</code> class records information about
 * incursions into NoFlyZones.
 *
 * @author AFRL/RQQD
 */
public class KeepOutZoneAnalysis implements AnalysisClient {

    private Vector<KeepOutZone> zoneList = new Vector<KeepOutZone>();
    private Element topNode = new Element("KeepOutZoneAnalysis");
    private Hashtable<IncursionKey, Element> incursionMap = new Hashtable<IncursionKey, Element>();
    double time = 0;
    private static DecimalFormat format = new DecimalFormat("#.###");
    private double lastTime = 0;

    public KeepOutZoneAnalysis() {
    }

    /**
     * Unimplemented.
     * @return Empty string.
     */
    public String getAnalysisReport() {
        return "";
    }

    /** {@inheritDoc} */
    public Element getAnalysisReportXML() {
        return topNode;
    }

    /**
     * Initializes the analysis module, clearing all recorded data. This is
     * typically called when initializing or resetting a scenario.
     */
    public void initScenario() {
        incursionMap.clear();
        topNode = new Element("KeepOutZoneAnalysis");
    }

    /** {@inheritDoc} */
    public void eventOccurred(Object evt) {

        if (evt instanceof KeepOutZone) {
            zoneList.add((KeepOutZone) evt);
        }
        else if (evt instanceof AirVehicleState) {
            
            if (time - lastTime < CHECK_TIME && time != lastTime) {
                return;
            }
            this.lastTime = time;
            
            AirVehicleState avs = (AirVehicleState) evt;
            for (KeepOutZone nfz : zoneList) {
                checkForIntersection(avs, nfz);
            }
        }
        else if (evt instanceof SessionStatus) {
            SessionStatus ss = (SessionStatus) evt;
            time = ss.getScenarioTime() / 1000d;
            if (ss.getState() == SimulationStatusType.Reset) {
                incursionMap.clear();
                lastTime = 0;
                topNode = new Element("KeepOutZoneAnalysis");
            }
        }
    }

    /**
     * Checks if <code>avs</code> has crossed into <code>nfz</code>. If it has,
     * the method updates the XML analysis report with the new incursion.
     * <br>
     * In general, this is not called directly, but is called from {@link #eventOccurred(Object) }
     * @param avs The air vehicle state to be checked.
     * @param nfz The no fly zone to be checked.
     */
    public void checkForIntersection(AirVehicleState avs, KeepOutZone nfz) {

        // if this is a aircraft specific zone, then check if it applies to the given aircraft
        if (!nfz.getAffectedAircraft().isEmpty() && !nfz.getAffectedAircraft().contains(avs.getID())) {
            return;
        }

        // if this zone isn't active right now, then don't do further checking
        if (nfz.getStartTime() > time || ( nfz.getEndTime() != 0 && nfz.getEndTime() < time) ) {
            return;
        }

        boolean intersects = checkIntersection(avs, nfz);


        IncursionKey key = new IncursionKey(avs.getID(), nfz.getZoneID());
        Element el = incursionMap.get(key);

        if (intersects) {
            if (el == null) {
                el = createIncursionNode(avs, nfz);
                incursionMap.put(key, el);
                topNode.add(el);
            }
        }
        else if (el != null) {
            incursionMap.remove(key);
            el.setAttribute("TotalTime", format.format(avs.getTime() - XMLUtil.getDoubleAttr(el, "TimeIn", 0)));
        }

    }

    /**
     * Returns <code>true</code> if <code>avs</code> is inside polygonal no fly
     * zone <code>nfz</code>, <code>false</code> otherwise.
     * <br>
     * In general, this is not called directly, but is called from {@link #checkForIntersection(afrl.cmasi.AirVehicleState, afrl.cmasi.KeepOutZone)  }
     * @param avs The air vehicle state to be checked.
     * @param nfz The no fly zone to be checked.
     * @return <code>true</code> if <code>avs</code> is inside <code>nfz</code>,
     * <code>false</code> otherwise.
     */
    public static boolean checkIntersection(AirVehicleState avs, KeepOutZone nfz) {
        boolean intersected = false;
        Path2D path = CmasiUtils.convertPoly(nfz.getBoundary());
        if ((avs.getLocation().getAltitude() < nfz.getMaxAltitude()) && (avs.getLocation().getAltitude() > nfz.getMinAltitude())) {
            if (path.contains(avs.getLocation().getLongitude(), avs.getLocation().getLatitude())) {
                intersected = true;
            }
        }
        return intersected;
    }

    /**
     * Creates a new XML element recording an incursion into a no fly zone.
     * <br>
     * In general, this is not called directly, but is called from {@link #checkForIntersection(afrl.cmasi.AirVehicleState, afrl.cmasi.KeepOutZone)  }
     * @param avs The air vehicle state that made the incursion.
     * @param nfz The no fly zone incurred into.
     * @return The new <code>Element</code>.
     */
    public Element createIncursionNode(AirVehicleState avs, KeepOutZone nfz) {
        Element el = new Element("Incursion");
        el.setAttribute("Vehicle", String.valueOf(avs.getID()));
        el.setAttribute("KeepOutZone", String.valueOf(nfz.getZoneID()));
        el.setAttribute("TimeIn", format.format(avs.getTime()));
        return el;
    }

    public void resetAnalysis() {
        initScenario();
    }

    /**
     * A simple, internal class for keeping track of air vehicle incursions
     * into no fly zones.
     */
    static class IncursionKey {

        long vehicleId;
        long zoneId;

        public IncursionKey(long vehicleId, long zoneId) {
            this.vehicleId = vehicleId;
            this.zoneId = zoneId;
        }

        @Override
        public int hashCode() {
            return (int) vehicleId + 31 * (int) zoneId;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof IncursionKey) {
                IncursionKey ev = (IncursionKey) obj;
                return (vehicleId == ev.vehicleId && zoneId == ev.zoneId);
            }
            return false;
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */