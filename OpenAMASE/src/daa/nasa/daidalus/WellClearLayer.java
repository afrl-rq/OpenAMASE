// ====================================================================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Autonomous Controls Branch
// 
// Copyright (c) 2018 Government of the United State of America, as represented by the Secretary of the Air Force.
// No copyright is claimed in the United States under Title 17, U.S. Code.  All Other Rights Reserved.
// ====================================================================================================================

package nasa.daidalus;

import afrl.cmasi.AirVehicleState;
import afrl.cmasi.Location3D;
import afrl.cmasi.SessionStatus;
import avtas.amase.scenario.ScenarioState;
import avtas.amase.util.CmasiNavUtils;
import avtas.app.AppEventListener;
import avtas.map.graphics.*;
import avtas.map.layers.GraphicsLayer;
import larcfm.DAIDALUS.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WellClearLayer extends GraphicsLayer<MapGraphic> implements AppEventListener {
    private Map<Long, WellClearState> idToVehicleState = new HashMap<>();

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof DAIDALUSConfiguration) {
            DAIDALUSConfiguration config = (DAIDALUSConfiguration) event;
            Long id = new Long(config.getEntityId());

            WellClearState state;
            if (idToVehicleState.containsKey(id)) {
                state = idToVehicleState.get(id);
            } else {
                state = new WellClearState();
            }            
            
            // Update the object
            state.setDAIDALUSConfiguration(config);
            idToVehicleState.put(id, state);
            
        } else if (event instanceof WellClearViolationIntervals) {
            WellClearViolationIntervals intervals = (WellClearViolationIntervals) event;
            Long id = new Long(intervals.getEntityId());

            WellClearState state;
            if (idToVehicleState.containsKey(id)) {
                state = idToVehicleState.get(id);
            } else {
                state = new WellClearState();
            }
            
            state.setBands((WellClearViolationIntervals) event, ScenarioState.getTime());
            idToVehicleState.put(id, state);
            
        } else if (event instanceof SessionStatus) {
            clear(); // remove all prior graphic objects

            // heading display
            // TODO: consider/handle wind
            for (Map.Entry<Long, WellClearState> entry : idToVehicleState.entrySet()) {
                WellClearState wellClearState = entry.getValue();
                if (wellClearState.isConfigured()) {
                    AirVehicleState airVehicleState = ScenarioState.getAirVehicleState(entry.getKey());

                    if (airVehicleState != null) {
	                    Location3D location = airVehicleState.getLocation();

	                    // show circle at DTRH
	                    final double radius_m = wellClearState.getConfig().getMinHorizontalRecovery(); // DTHR m
	                    MapCircle circle = new MapCircle(location.getLatitude(), location.getLongitude(), radius_m);
	                    circle.setPainter(Color.WHITE, 1);
	                    getList().add(circle);

	                    // show current heading
	                    Location3D endPoint = CmasiNavUtils.getPoint(location, radius_m, wellClearState.getCurrent(BandType.HEADING));
	                    MapLine line = new MapLine(location.getLatitude(), location.getLongitude(),
	                            endPoint.getLatitude(), endPoint.getLongitude());
	                    line.setPainter(Color.WHITE, 1);
	                    getList().add(line);

	                    // TODO: consider dropping 'expired' bands based on stored time
	                    if ((ScenarioState.getTime() - wellClearState.getMsgTime()) < 1) {

	                        // TODO: overlay green arc covering angular range being considered (i.e. [left_track, right_track])

	                        for (BandIntervals.Band band : wellClearState.getBands(BandType.HEADING)) {
	                            // TODO: check whether band enclosing true north is possible, and if handle correctly
	                            MapArc arc = new MapArc(location.getLatitude(), location.getLongitude(), band.lower,
	                                    band.upper - band.lower, radius_m);
	                            arc.setPainter(band.getColor(), 3);
	                            getList().add(arc);
	                        }
	                    }
                    }
                }
            }

            // TODO: displays for other band types

            // transform all graphics from world coordinates to screen coordinates
            project(getProjection());
        }
    }
}

/* Distribution A. Approved for public release.
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */