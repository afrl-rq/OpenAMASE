// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.entity.modules;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.FlightProfile;
import afrl.cmasi.WeatherReport;
import avtas.amase.entity.EntityModule;
import avtas.data.Unit;
import avtas.math.Euler;
import avtas.math.Vector3;
import avtas.terrain.TerrainService;
import avtas.util.NavUtils;
import static java.lang.Math.*;

/**
 * A simple flight module that uses the Flight Configurations for a vehicle to establish operation points
 * and performs interpolation between points to determine flight characteristics.
 * <p>
 * the module maintains a list of {@link FlightProfile}s that describe the performance
 * of the aircraft.
 * 
 * @author AFRL/RQQD
 */
public class KinematicFlight extends EntityModule {

    private double maxBankRate = Math.toRadians(60);
    private double maxPitchRate = Math.toRadians(90);
    private double g = 9.8;
    private double northWind = 0;
    private double eastWind = 0;
    private double headingGain = 8;
    private static double MAX_ACCEL = 10;
    private AirVehicleConfiguration airVehicleConfig = null;
    boolean calculate = true;
    
    /**
     * Keeps a list of this aircraft flight capabilities
     */
    protected ProfileManager profileManager = new ProfileManager();

    public KinematicFlight() {
    }

    /**
     * Performs the computations for moving the aircraft through the air using the following process:
     * <ol>
     *  <li>Interpolate the performance profile using the current flight conditions.  See {@link ProfileManager}
     *      for more information.
     *  <li>increment bank angle based on the difference between commanded and current heading,
     *  <li>Calculate the change in heading for the current bank angle,
     *  <li>Adjust pitch angle according to the interpolated profile,
     *  <li>Compute change in speed based on difference between commanded and current speed,
     *  <li>Compute change in location (lat, lon, alt) including wind effect, 
     *  <li>Calculate fuel usage
     * </ol>
     * The acceleration is limited based on what is specified in the interpolated flight profile.
     * <p>
     * Bank angle rate, and pitch angle rate are limited to 60 deg/sec and 10 deg/sec, respectively.
     * 
     * @param timestep_sec change in simulation time in seconds.
     */
    @Override
    public void step(double timestep_sec, double simtime_sec) {

        // if the aircraft is below terrain, don't compute
        double terrainAlt = TerrainService.getElevation(Math.toDegrees(data.lat.asDouble()), Math.toDegrees(data.lon.asDouble()));
        if (data.alt.asDouble() - terrainAlt <= 0) {
            return;
        }

        double speed = data.u.asDouble();

        // set the vertical speed
        double dalt = data.autopilotCommands.cmdAlt.asDouble() - data.alt.asDouble();
        double vs = dalt;
        if (data.autopilotCommands.cmdVertSpeed.asDouble() != 0) {
            vs = data.autopilotCommands.cmdVertSpeed.asDouble();
        }

        // interpolated flight profile
        FlightProfile targetProf = profileManager.getProfile(speed, vs);
        data.autopilotCommands.maxBank.setValue(targetProf.getMaxBankAngle());

        // now limit vertical speed to the rate specified in the profile
        vs = min(Math.abs(targetProf.getVerticalSpeed()), Math.abs(vs)) * Math.signum(vs);

        // if fuel is zero, then don't update position (Vehicle is dead)
        if (data.energy_remaining.asDouble() <= 0) {
            return;
        }

        // change in heading is difference between commanded heading and current heading
        double dhdg = Unit.boundPi(data.autopilotCommands.cmdHdg.asDouble() - atan2(data.veast.asDouble(), data.vnorth.asDouble()));
        if (Double.isNaN(dhdg)) dhdg = 0;

        // bank angle set based on heading difference, limited to max bank angle specified in the profile
        double maxPhi = Math.toRadians(targetProf.getMaxBankAngle());
        data.autopilotCommands.maxBank.setValue(maxPhi);
        double phi = maxPhi * (1 - Math.exp(-headingGain * abs(dhdg))) * signum(dhdg);
        double dphi = phi - data.phi.asDouble();


        // sets the change in bank angle to allow for smooth bank transition
        dphi = min(abs(dphi), maxBankRate * timestep_sec) * signum(dphi);
        phi = data.phi.asDouble() + dphi;
        data.phi.setValue(phi);

        // using the turn circle equation, computes the change in heading for the given bank
        if (abs(phi) > 0 && speed > 0) {
            data.psi.setValue(Unit.boundPi(data.psi.asDouble() + g
                    * sqrt(pow(1. / cos(phi), 2) - 1) / speed * timestep_sec * signum(phi)));
            //data.psi.setValue(Unit.boundPi(data.psi.asDouble() + g * tan(phi) / speed * timestep_sec * signum(phi)));     
        }

        // the load factor is estimated assuming straight-level turns
        data.g.setValue(1 / Math.cos(phi));

        //set pitch according to the profile
        double dtheta = toRadians(targetProf.getPitchAngle()) - data.theta.asDouble();
        dtheta = min(abs(dtheta), maxPitchRate * timestep_sec) * signum(dtheta);
        data.theta.setValue(data.theta.asDouble() + dtheta);
        double cosTheta = cos(data.theta.asDouble());
        
        
        // set the body rates
        Euler e = new Euler(data.psi.asDouble(), data.theta.asDouble(), data.phi.asDouble());
        Vector3 pqr = e.getPQR(new Vector3(dhdg/timestep_sec, dtheta/timestep_sec, dphi/timestep_sec));
        data.p.setValue(pqr.get1());
        data.q.setValue(pqr.get2());
        data.r.setValue(pqr.get3());

        // adjust the speed
        double du = data.autopilotCommands.cmdSpeed.asDouble() - data.u.asDouble();
        if (du > 0) {
            du = min(du, MAX_ACCEL);
        }
        else {
            du = max(du, -MAX_ACCEL);
        }
        data.u.setValue(data.u.asDouble() + du * timestep_sec);

        // if computed u is greater than max speed, set to max speed for this aircraft
        data.u.setValue(Math.min(data.u.asDouble(), airVehicleConfig.getMaximumSpeed()));
        // if computed u is less than min speed, set to min speed for this aircraft
        data.u.setValue(Math.max(data.u.asDouble(), airVehicleConfig.getMinimumSpeed()));

        data.vnorth.setValue(speed * cosTheta * cos(data.psi.asDouble()) - northWind);
        data.veast.setValue(speed * cosTheta * sin(data.psi.asDouble()) - eastWind);
        data.vdown.setValue(-vs);

        // update the position
        data.alt.setValue(data.alt.asDouble() + vs * timestep_sec);
        data.lat.setValue(NavUtils.getLat(data.lat.asDouble(), timestep_sec * data.vnorth.asDouble(), data.alt.asDouble()));
        data.lon.setValue(NavUtils.getLon(data.lat.asDouble(), data.lon.asDouble(), timestep_sec * data.veast.asDouble(),
                data.alt.asDouble()));

        // update the fuel expenditure
        data.energy_remaining.setValue(data.energy_remaining.asDouble() - targetProf.getEnergyRate() * timestep_sec);
        if (data.energy_remaining.asDouble() < 0) {
            data.energy_remaining.setValue(0);
        }
        data.energy_rate.setValue(targetProf.getEnergyRate());
    }

    

    public void modelEventOccurred(Object object) {

        if (object instanceof AirVehicleConfiguration) {
            this.airVehicleConfig =(AirVehicleConfiguration) object;
            this.profileManager.setProfiles(airVehicleConfig);
            if (airVehicleConfig.getNominalFlightProfile() == null) {
                calculate = false;
            }
        }
        else if(object instanceof WeatherReport) {
            WeatherReport wr = (WeatherReport) object;
            northWind = wr.getWindSpeed() * cos(Math.toRadians(wr.getWindDirection()));
            eastWind = wr.getWindSpeed() * sin(Math.toRadians(wr.getWindDirection()));
            System.out.println("AMASE Kinematic Model - Adding Wind Disturbances");
            System.out.println("North: " + northWind);
            System.out.println("East: " + eastWind);

        }
        else if (object instanceof AirVehicleState) {
            AirVehicleState avs = (AirVehicleState) object;
            double groundspeed = Math.hypot(data.vnorth.asDouble() - northWind, data.veast.asDouble() - eastWind);
            avs.setGroundspeed((float) groundspeed);
            avs.setAirspeed( (float) data.u.asDouble());
            avs.setCourse((float) Math.toDegrees(Math.atan2(data.veast.asDouble() - eastWind, data.vnorth.asDouble() - northWind)));
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */