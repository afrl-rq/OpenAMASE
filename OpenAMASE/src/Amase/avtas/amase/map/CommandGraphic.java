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
import afrl.cmasi.VehicleAction;
import afrl.cmasi.VehicleActionCommand;
import afrl.cmasi.Waypoint;
import avtas.amase.util.CmasiUtils;
import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapGraphicsList;
import avtas.map.graphics.MapMarker;
import avtas.map.graphics.MapPoly;
import avtas.map.graphics.Painter;
import avtas.util.NavUtils;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * A graphic that displays mission commands, including waypoints and loiters.
 *
 * @author AFRL/RQQD
 */
public class CommandGraphic extends MapGraphicsList<MapGraphic> {

    private Color color = Color.BLACK;
    final Painter outlinePainter;
    MapGraphicsList<MapMarker> wpList = new MapGraphicsList<MapMarker>();
    MapGraphicsList<LoiterGraphic> loiterList = new MapGraphicsList<LoiterGraphic>();
    MapPoly wpLine = new MapPoly();
    MapGraphicsList<DirectionArrow> routeDirList = new MapGraphicsList<DirectionArrow>();

    public CommandGraphic(MissionCommand mc, Color color) {
        this.color = color;
        outlinePainter = Painter.createOutlinePainter(color, Color.WHITE, 1.5f);
        add(wpLine);
        add(routeDirList);
        add(wpList);
        add(loiterList);
        setName(mc.getClass().getSimpleName() + " " + mc.getCommandID());
        drawRoute(mc);
    }

    public CommandGraphic(VehicleActionCommand vac, Color color) {
        this.color = color;
        outlinePainter = Painter.createOutlinePainter(color, Color.WHITE, 1.5f);
        //add(wpLine);
        //add(routeDirList);
        //add(wpList);
        add(loiterList);
        setName(vac.getClass().getSimpleName() + " " + vac.getCommandID());

        for (VehicleAction va : vac.getVehicleActionList()) {
            if (va instanceof LoiterAction) {
                loiterList.add(drawLoiter((LoiterAction) va));
            }
        }
    }

    /**
     * Adds waypoints, route and loiter areas to the mission list, set the
     * display parameters for these objects (color and stroke type)
     *
     * This is called from
     * <code>eventOccured</code> when a mission command is given
     *
     * @param mcm mission command
     */
    private void drawRoute(MissionCommand mc) {

        for (int i = 0; i < mc.getWaypointList().size(); i++) {

            Waypoint wp = mc.getWaypointList().get(i);
            MapMarker mp = null;
            if (wp.getNumber() == mc.getFirstWaypoint()) {
                mp = new StartMarker(wp.getLatitude(), wp.getLongitude());
            }
            else {
                mp = new MapMarker(wp.getLatitude(), wp.getLongitude());
                mp.setMarkerShape(new Rectangle2D.Double(0, 0, 5, 5));
            }
            mp.setPainter(Color.WHITE, 1);
            mp.setFill(color);
            mp.setRefObject(wp);
            mp.setName("Waypoint " + wp.getNumber());
            wpList.add(mp);

            for (int j = 0; j < wp.getVehicleActionList().size(); j++) {
                if (wp.getVehicleActionList().get(j) instanceof LoiterAction) {
                    loiterList.add(drawLoiter((LoiterAction) wp.getVehicleActionList().get(j)));
                }
            }
        }
        // draw waypoint line 
        Waypoint wp = CmasiUtils.getWaypoint(mc, mc.getFirstWaypoint());

        ArrayList<Long> visitedPoints = new ArrayList<Long>();
        if (wp != null) {
            wpLine.addPoint(wp.getLatitude(), wp.getLongitude());
            while (wp != null && !visitedPoints.contains(wp.getNumber())) {
                visitedPoints.add(wp.getNumber());
                Waypoint nextWp = CmasiUtils.getWaypoint(mc, wp.getNextWaypoint());
                if (nextWp == null || nextWp == wp) {
                    break;
                }
                wpLine.addPoint(nextWp.getLatitude(), nextWp.getLongitude());

                routeDirList.add(new DirectionArrow(wp.getLatitude(), wp.getLongitude(),
                        nextWp.getLatitude(), nextWp.getLongitude()));
                wp = nextWp;
            }
        }

        wpLine.setPainter(outlinePainter);
        wpLine.setRefObject(mc);
        wpLine.setName(mc.getClass().getSimpleName() + " " + mc.getCommandID());
        routeDirList.setPainter(Color.WHITE, 1);
        routeDirList.setFill(color);

    }

    private LoiterGraphic drawLoiter(LoiterAction loiterAction) {
        LoiterGraphic lg = new LoiterGraphic(loiterAction);
        lg.setPainter(outlinePainter);
        return lg;
    }

    static class StartMarker extends MapMarker {

        static final int size = 7;

        public StartMarker(double lat, double lon) {
            super(lat, lon);
            Arc2D circle = new Arc2D.Double(0, 0, size, size,
                    0, 360, Arc2D.CHORD);
            setMarkerShape(circle);
        }
    }

    static class DirectionArrow extends MapMarker {

        static final int size = 7;
        static final Path2D triangle = new Path2D.Double();

        static {
            triangle.moveTo(0, size);
            triangle.lineTo(size / 2., 0);
            triangle.lineTo(size, size);
            triangle.closePath();
        }

        public DirectionArrow(double lat1, double lon1, double lat2, double lon2) {
            super(0.5 * (lat1 + lat2), 0.5 * (lon1 + lon2));
            double hdg = NavUtils.headingBetween(Math.toRadians(lat1),
                    Math.toRadians(lon1), Math.toRadians(lat2), Math.toRadians(lon2));
            setMarkerShape(triangle.createTransformedShape(
                    AffineTransform.getRotateInstance(hdg, 0.5 * size, 0.5 * size)));
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */