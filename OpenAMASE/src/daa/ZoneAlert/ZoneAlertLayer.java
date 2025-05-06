// ====================================================================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Autonomous Controls Branch
// 
// Copyright (c) 2018 Government of the United State of America, as represented by the Secretary of the Air Force.
// No copyright is claimed in the United States under Title 17, U.S. Code.  All Other Rights Reserved.
// ====================================================================================================================

package ZoneAlert;

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

//debugging
import java.io.*;

public class ZoneAlertLayer extends GraphicsLayer<MapGraphic> implements AppEventListener {
    private Map<Long, MapLine> IDtoLine = new HashMap<>();
    private Map<Long, MapText> IDtoText = new HashMap<>();
    private Map<Long, Location3D> CurrentLoc = new HashMap<>();
    private ArrayList<Long> IZV = new ArrayList<Long>();
    private ArrayList<Long> AZV = new ArrayList<Long>();
    private ArrayList<Long> IZV2 = new ArrayList<Long>();
    private ArrayList<Long> AZV2 = new ArrayList<Long>();

    
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
            long id = proc.getID();
	    if (IZV2.contains(id) || AZV2.contains(id)){
			IZV2.remove(id);
			AZV2.remove(id);
	    } else
		if (IZV.contains(id) || AZV.contains(id)) {
		    if (IZV.contains(id)) {
			IZV.remove(id);
		    }
		    if (AZV.contains(id)) {
			AZV.remove(id);
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
            System.out.println("Imminent Zone Violation from AMASE");
	    ImminentZoneViolation proc = (ImminentZoneViolation) event;
	    long vID = proc.getVehicleID();
	    double TTI = proc.getTimeToIntercept();
	    double lat = proc.getInterceptPosition().getEast(); //change latitude
	    double lon = proc.getInterceptPosition().getNorth(); //change longitude
	    AirVehicleState airVehicleState = ScenarioState.getAirVehicleState(vID);
	    if (!IZV.contains(vID)){
		IZV.add(vID);
	    }
	    if (!IZV2.contains(vID)){
		IZV2.add(vID);
	    }
	    if (airVehicleState != null) {
                getList().remove(IDtoLine.get(vID));
                getList().remove(IDtoText.get(vID));
                IDtoLine.remove(vID);
                IDtoText.remove(vID);
		Location3D location = airVehicleState.getLocation();                            
                MapLine violationLine = new MapLine(location.getLatitude(), location.getLongitude(), lat, lon);
		MapText violationTime = new MapText();
		violationLine = new MapLine(location.getLatitude(), location.getLongitude(), lat, lon);
                violationLine.setPainter(Color.WHITE, 1);
		violationTime.setColor(Color.BLACK);
		violationTime.setFill(Color.WHITE);
		violationTime.setHorizontalAlignment(SwingConstants.CENTER);
		violationTime.setLatLon(location.getLatitude(), location.getLongitude());
		violationTime.setText(Integer.toString((int) TTI));
		violationLine.setVisible(true);
		violationTime.setVisible(true);
		IDtoLine.put(vID, violationLine);
		IDtoText.put(vID, violationTime);
	    }
	    AircraftColors.makeNewColor(vID, Color.YELLOW);
	}
	else if (event instanceof ActiveZoneViolation){
            System.out.println("Active Zone Violation from AMASE");
	    ActiveZoneViolation proc = (ActiveZoneViolation) event;
	    long vID = proc.getVehicleID();
            if (IDtoLine.containsKey(vID)) {
                IDtoLine.get(vID).setVisible(false);
		}
            if (IDtoText.containsKey(vID)) {
                IDtoText.get(vID).setVisible(false);
            }
	    if (!AZV.contains(vID)){
		AZV.add(vID);
	    }
	    if (!AZV2.contains(vID)){
		AZV2.add(vID);
	    }
	AircraftColors.makeNewColor(vID, Color.RED);
	}
	else if (event instanceof SessionStatus) {
            
            for (Map.Entry<Long, MapLine> entry : IDtoLine.entrySet()){
		getList().add(entry.getValue());
            }
            for(Map.Entry<Long, MapText> entry : IDtoText.entrySet()){
		getList().add(entry.getValue());
            }   
	    project(getProjection());
        }
    }
}

/* Distribution A. Approved for public release.
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */
