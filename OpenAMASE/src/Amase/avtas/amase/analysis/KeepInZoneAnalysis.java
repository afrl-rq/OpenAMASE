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
import avtas.app.AppEventManager;
import avtas.amase.scenario.ScenarioEvent;
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.KeepInZone;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import avtas.lmcp.LMCPObject;
import java.awt.geom.Path2D;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Vector;
import avtas.xml.Element;

/**
 * The <code>KeepInZoneAnalysis</code> class records information about
 * excursions from Keep In Zones.
 *
 * @author AFRL/RQQD
 */
public class KeepInZoneAnalysis implements AnalysisClient {

    private Vector<KeepInZone> zoneList = new Vector<KeepInZone>();
    private Element topNode = new Element("KeepInZoneAnalysis");
    private Hashtable<IncursionKey, Element> excursionMap = new Hashtable<IncursionKey, Element>();
    private static DecimalFormat format = new DecimalFormat("#.###");
    double time = 0;
    private double lastTime = 0;

    public KeepInZoneAnalysis() {
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
        excursionMap.clear();
        topNode = new Element("KeepInZoneAnalysis");
    }

    /** {@inheritDoc} */
    public void eventOccurred(Object evt) {

        if (evt instanceof KeepInZone) {
            zoneList.add((KeepInZone) evt);
        }
        else if (evt instanceof AirVehicleState) {
            if (time - lastTime < CHECK_TIME && time != lastTime) {
                return;
            }
            this.lastTime = time;
            AirVehicleState avs = (AirVehicleState) evt;
            for (KeepInZone zone : zoneList) {
                checkForIntersection(avs, zone);
            }
        }
        else if (evt instanceof SessionStatus) {
            SessionStatus ss = (SessionStatus) evt;
            time = ss.getScenarioTime() / 1000d;
            if (ss.getState() == SimulationStatusType.Reset) {
                lastTime = 0;
                initScenario();
            }
        }
    }

    /**
     * Checks if <code>avs</code> has crossed into <code>kiz</code>. If it has,
     * the method updates the XML analysis report with the new incursion.
     * <br>
     * In general, this is not called directly, but is called from {@link #eventOccurred(Object) }
     * @param avs The air vehicle state to be checked.
     * @param kiz keep in zone to be checked
     */
    public void checkForIntersection(AirVehicleState avs, KeepInZone kiz) {

        // if this is a aircraft specific zone, then check if it applies to the given aircraft
        if (!kiz.getAffectedAircraft().isEmpty() && !kiz.getAffectedAircraft().contains(avs.getID())) {
            return;
        }

        // if this zone isn't active right now, then don't do further checking
        if (kiz.getStartTime() > time || ( kiz.getEndTime() != 0 && kiz.getEndTime() < time) ) {
            return;
        }

        boolean intersects = checkIntersection(avs, kiz);

        IncursionKey key = new IncursionKey(avs.getID(), kiz.getZoneID());
        Element el = excursionMap.get(key);

        if (!intersects) {
            if (el == null) {
                el = createExcursionNode(avs, kiz);
                excursionMap.put(key, el);
                topNode.add(el);
            }
        }
        else if (el != null) {
            excursionMap.remove(key);
            el.setAttribute("TotalTime", format.format(avs.getTime() - XMLUtil.getDoubleAttr(el, "TimeOut", 0)));
        }

    }

    /**
     * Returns <code>true</code> if <code>avs</code> is inside zone.
     * <br>
     * In general, this is not called directly, but is called from {@link #checkForIntersection(AirVehicleState avs, KeepInZone kiz) }
     * @param avs The air vehicle state to be checked.
     * @param zone The zone to be checked.
     * @return <code>true</code> if <code>avs</code> is inside <code>nfz</code>,
     * <code>false</code> otherwise.
     */
    public static boolean checkIntersection(AirVehicleState avs, KeepInZone zone) {
        boolean intersected = false;
        //does this zone apply to this aircraft
        if (zone.getAffectedAircraft().isEmpty() || zone.getAffectedAircraft().contains(avs.getID())) {
            Path2D path = CmasiUtils.convertPoly(zone.getBoundary());
            if ((avs.getLocation().getAltitude() < zone.getMaxAltitude()) && (avs.getLocation().getAltitude() > zone.getMinAltitude())) {
                if (path.contains(avs.getLocation().getLongitude(), avs.getLocation().getLatitude())) {
                    intersected = true;
                }
            }
        }
        return intersected;
    }

    /**
     * Creates a new XML element recording an excursion from a keep-in zone.
     * <br>
     * @param avs The air vehicle state that made the incursion.
     * @param kiz The keep in zone incurred into.
     * @return The new <code>Element</code>.
     */
    public Element createExcursionNode(AirVehicleState avs, KeepInZone kiz) {
        Element el = new Element("Excursion");
        el.setAttribute("Vehicle", String.valueOf(avs.getID()));
        el.setAttribute("KeepInZone", String.valueOf(kiz.getZoneID()));
        el.setAttribute("TimeOut", format.format(avs.getTime()));
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