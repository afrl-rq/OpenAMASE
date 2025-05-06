// ====================================================================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Autonomous Controls Branch
// 
// Copyright (c) 2018 Government of the United State of America, as represented by the Secretary of the Air Force.
// No copyright is claimed in the United States under Title 17, U.S. Code.  All Other Rights Reserved.
// ====================================================================================================================

import avtas.amase.ui.AircraftColors;
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.Location3D;
import afrl.cmasi.SessionStatus;
import avtas.amase.scenario.ScenarioState;
//import avtas.amase.util.CmasiNavUtils;
import avtas.app.AppEventListener;
import avtas.map.graphics.*;
import avtas.map.layers.GraphicsLayer;
//import larcfm.DAIDALUS.*;  //Template for imports on messages
import uxas.messages.ImminentZoneViolation;
import uxas.messages.ActiveZoneViolation;
//import ProcessedZones;

import javax.swing.SwingConstants;

import java.awt.*;
import java.util.*;
import java.lang.Math;

public class ZoneAlertLayer extends GraphicsLayer<MapGraphic> implements AppEventListener {
    private Map<Integer, MapLine> IDtoLine = new HashMap<>();
    private Map<Integer, MapText> IDtoText = new HashMap<>();
    private Map<Integer, Location3D> CurrentLoc = new HashMap<>();
    private ArrayList<Integer> IZV = new ArrayList<Integer>();
    private ArrayList<Integer> AZV = new ArrayList<Integer>();
    private ArrayList<Integer> IZV2 = new ArrayList<Integer>();
    private ArrayList<Integer> AZV2 = new ArrayList<Integer>();

    
    @Override
    public void eventOccurred(Object event) {
	//This is where we interpret the messages to AMASE and unpack ZoneAlert
	//  information
	//  VehicleID, ZoneID, KeepIn, TimeToIntercept, InterceptPosition
	//  ZoneID is a ProcessedZone type, ProcessedZone message will contain
	//  ZoneID, KeepIn/Out (bool), and Vertices for display

	//Air Vehicle State:  Every state update, check whether the vehicle is
	//  imminent or active and check to see if that condition still holds.
	//  Any vehicle that has departed the active violation should return
	//  to default color.  Then, if a vehicle has gone two state updates
	//  without an imminent message, it should return to default state.
	//
	//  Also, grab current location of vehicle to use in line draw on
	//  imminent.
	if (event instanceof AirVehicleState){
	    AirVehicleState proc = (AirVehicleState) event;
            long procID = proc.getID();
            Integer id = (int)(long) procID;
	    if (IZV2.contains(id) || AZV2.contains(id)){
			IZV2.remove((Integer) id);
			AZV2.remove((Integer) id);
	    } else
		if (IZV.contains(id) || AZV.contains(id)) {
		    if (IZV.contains(id)) {
			IZV.remove((Integer) id);
		    }
		    if (AZV.contains(id)) {
			AZV.remove((Integer) id);
		    }
		} else {
		    //Make the IZV line and text invisible
		    if (IDtoLine.containsKey(id)) {
                            IDtoLine.get(id).setVisible(false);
			}
		    if (IDtoText.containsKey(id)) {
			    IDtoText.get(id).setVisible(false);
			}
		    AircraftColors.makeOriginalColor((int) id);
		}
	}
	//Imminent Zone violation:
	//  1.  Recover vehicle ID from message
	//  2.  Register vehicle ID as having received imminent zone violation
	//        this tick.
	//  3.  Check if vehicle is part of any active zone violations.
	//      3a.  If NOT, make vehicle COLOR YELLOW
	//  4.  Draw a line from the vehicle to the provided 2D position
	//        from the message  (This can result in multiple lines)
	//  5.  Display the provided time next to the vehicle* (ideally,
	//        next to the line)
	else if (event instanceof ImminentZoneViolation){
	    ImminentZoneViolation proc = (ImminentZoneViolation) event;
	    int vID = (int) proc.getVehicleID();
	    double TTI = proc.getTimeToIntercept();
	    double lat = proc.getInterceptPosition().getEast();
	    double lon = proc.getInterceptPosition().getNorth();
	    AirVehicleState airVehicleState = ScenarioState.getAirVehicleState(vID);
	    if (!IZV.contains(vID)){
		IZV.add(vID);
	    }
	    if (!IZV2.contains(vID)){
		IZV2.add(vID);
	    }
	    if (airVehicleState != null) {
		Location3D location = airVehicleState.getLocation();                            
                MapLine violationLine = new MapLine(location.getLatitude(), location.getLongitude(), lat, lon);
		MapText violationTime = new MapText();
		violationLine = new MapLine(location.getLatitude(), location.getLongitude(), lat, lon);
		violationTime.setColor(Color.WHITE);
		violationTime.setFill(Color.BLACK);
		violationTime.setHorizontalAlignment(SwingConstants.CENTER);
		violationTime.setLatLon(0.9 * location.getLatitude() + 0.1*lat, 0.9 * location.getLongitude + 0.1 * lon);
		violationTime.setText(Integer.toString((int) TTI));
		violationLine.setVisible(true);
		violationTime.setVisible(true);
		IDtoLine.put(vID, violationLine);
		IDtoText.put(vID, violationTime);
	    }
	    AircraftColors.makeNewColor(vID, Color.YELLOW);
	}
	else if (event instanceof ActiveZoneViolation){
	    ActiveZoneViolation proc = (ActiveZoneViolation) event;
	    int vID = (int) proc.getVehicleID;
	    AirVehicleState airVehicleState = ScenarioState.getAirVehicleState(vID);
	    if (!AZV.contains(vID)){
		AZV.add(vID);
	    }
	    if (! AZV.contains(vID)){
		AZV2.add(vID);
	    }
		AircraftColors.makeNewColor(vID, Color.RED);
	}
	else if (event instanceof SessionStatus) {
            
            for (Map.Entry<Integer, MapLine> entry : IDtoLine.entrySet()){
		getList().add(entry.getValue());
	    }
            for(Map.Entry<Integer, MapText> entry : IDtoText.entrySet()){
		getList().add(entry.getValue());
            }   
	    project(getProjection());
        }
    }
}

/* Distribution A. Approved for public release.
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */
