// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.entity.modules;

import afrl.cmasi.AirVehicleState;
import afrl.cmasi.FlightDirectorAction;
import afrl.cmasi.MissionCommand;
import afrl.cmasi.NavigationMode;
import afrl.cmasi.SpeedType;
import afrl.cmasi.VehicleAction;
import afrl.cmasi.VehicleActionCommand;
import avtas.amase.entity.EntityModule;
import avtas.data.Unit;
import avtas.lmcp.LMCPObject;
import java.util.ArrayList;

/**
 * A class for implementing basic autopilot functions. The FlightDirector takes
 * speed, altitude, and heading information to command the aircraft.
 *
 * @author AFRL/RQQD
 */
public class FlightDirector extends EntityModule {

    long currentCommandID = 0;
    boolean flightDirectorOn = false;

    public FlightDirector() {
    }


    /**
     * Controls the FlightDirector. If the LMCPObject is a MissionCommand and
     * that MissionCommand contains a FLightDirectorAction, then the flight
     * director commands the autopilot according to its contents.
     *
     * @param object LMCP object sent by the flight model
     */
    @Override
    public void modelEventOccurred(Object object) {
        if (object instanceof VehicleActionCommand) {
            flightDirectorOn = false;
            VehicleActionCommand vc = (VehicleActionCommand) object;
            ArrayList<VehicleAction> actions = vc.getVehicleActionList();
            for (VehicleAction a : actions) {
                if (a instanceof FlightDirectorAction) {
                    currentCommandID = vc.getCommandID();
                    flightDirectorOn = true;
                    setFlightDirector((FlightDirectorAction) a);
                }
            }
        } else if (object instanceof MissionCommand) {
            flightDirectorOn = false;
        } else if (object instanceof FlightDirectorAction) {
            setFlightDirector((FlightDirectorAction) object);
        } else if (object instanceof AirVehicleState) {
            if (flightDirectorOn) {
                AirVehicleState avs = (AirVehicleState) object;
                avs.setMode(NavigationMode.FlightDirector);
                avs.setCurrentCommand(currentCommandID);
            }
        }
    }

    /**
     * Sets the autopilot parameters according to the values in the
     * FlightDirectorAction. The autopilot automatically turns off Waypoint
     * following and sets the bank and pitch limits. 
     *
     * @param fda FlightDirectorAction containing autopilot commands.
     */
    public void setFlightDirector(FlightDirectorAction fda) {
        data.autopilotCommands.navMode.setValue(NavigationMode.FlightDirector);
        if (fda.getAltitude() != 0) {
            data.autopilotCommands.cmdAlt.setValue(fda.getAltitude());
        }
        data.autopilotCommands.cmdHdg.setValue(Unit.DEGREES.convertTo(fda.getHeading(), Unit.RADIANS));
        if (fda.getSpeed() != 0) {
            data.autopilotCommands.cmdSpeed.setValue(fda.getSpeed());
            data.autopilotCommands.speedCmdType.setValue(SpeedType.Airspeed);
        }
        data.autopilotCommands.cmdVertSpeed.setValue(fda.getClimbRate());
        //data.autopilotCommands.maxVsUp = fda.getClimbRate();
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */