// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.entity.modules;

import afrl.cmasi.*;
import uxas.messages.uxnative.*;
import avtas.amase.entity.EntityModule;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * A loiter following class.  This handles orbit, racetrack, and figure-eight loiters
 * @author AFRL/RQQD
 */
public class LoiterControl extends EntityModule {

    double time = 0;
    protected ArrayDeque<LoiterAction> loiterList = new ArrayDeque<LoiterAction>();
    protected LoiterShape loiterShape = null;
    protected LoiterAction currentLoiter = null;
    protected boolean fromWaypoint = false;
    protected MissionCommand mc = null;
    protected long currentCommandID = 0;

    public LoiterControl() {
    }

    public void step(double timestep_sec, double simtime_sec) {
        
        if (data.autopilotCommands.navMode.getValue() != NavigationMode.Loiter) {
            return;
        }

        // attempt to load the next loiter action from the list.
        if (currentLoiter == null) {
            LoiterAction nextLoiter = loiterList.peek();
            if (nextLoiter != null) {
                setLoiter(loiterList.remove());
            } else {
                //data.orbitHoldOn = false;
                return;
            }

        }

        // check the loiter time against the current loiter action.
        double loiterTime = currentLoiter.getDuration();
        if (time > loiterTime && loiterTime > 0) {
            time = 0;
            currentLoiter = null;
            // trigger the waypoint following property to return to waypoint mode, 
            // if this was commanded from a waypoint.
            if (fromWaypoint) {
                data.autopilotCommands.navMode.setValue(NavigationMode.Waypoint);
            }
            return;
        } else {
            time += timestep_sec;
        }

        // set the commanded heading based on the current loiter.
        data.autopilotCommands.navMode.setValue(NavigationMode.Loiter);
        data.autopilotCommands.cmdHdg.setValue(loiterShape.compute(data.lat.asDouble(), data.lon.asDouble()));
    }
    


    public void setLoiter(LoiterAction a) {

        this.currentLoiter = a;

        loiterShape = new LoiterShape(a);

        time = 0;

        // setup the autopilot
        data.autopilotCommands.cmdAlt.setValue(a.getLocation().getAltitude());
        data.autopilotCommands.cmdSpeed.setValue(a.getAirspeed());
        data.autopilotCommands.speedCmdType.setValue(SpeedType.Airspeed);
        data.autopilotCommands.navMode.setValue(NavigationMode.Loiter);
    }

    public void modelEventOccurred(Object object) {
        if (object instanceof MissionCommand) {
            loiterList.clear();
            currentLoiter = null;
            //data.orbitHoldOn = false;
            currentCommandID = ((MissionCommand) object).getCommandID();

        } else if (object instanceof VehicleActionCommand) {
            VehicleActionCommand vc = (VehicleActionCommand) object;
            ArrayList<VehicleAction> actions = vc.getVehicleActionList();
            for (VehicleAction a : actions) {
                if (a instanceof LoiterAction) {
                    loiterList.clear();
                    currentLoiter = null;
                    currentCommandID = vc.getCommandID();
                    break;
                }
                else if ( !(a instanceof PayloadAction) && !(a instanceof SpeedOverrideAction) ) {
                    loiterList.clear();
                    currentLoiter = null;
                    data.autopilotCommands.navMode.setValue(NavigationMode.Loiter);
                }
            }
            for (VehicleAction a : actions) {
                if (a instanceof LoiterAction) {
                    currentLoiter = null;
                    loiterList.addLast((LoiterAction) a);
                    data.autopilotCommands.navMode.setValue(NavigationMode.Loiter);
                    fromWaypoint = false;
                }
            }
        } else if (object instanceof AirVehicleState) {
            if (data.autopilotCommands.navMode.getValue() == NavigationMode.Loiter) {
                AirVehicleState avs = (AirVehicleState) object;
                avs.setMode(NavigationMode.Loiter);
                avs.setCurrentCommand(currentCommandID);
            }
        } else if (object instanceof LoiterAction) {
            loiterList.add((LoiterAction) object);
            data.autopilotCommands.navMode.setValue(NavigationMode.Loiter);
            fromWaypoint = true;
        }
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */