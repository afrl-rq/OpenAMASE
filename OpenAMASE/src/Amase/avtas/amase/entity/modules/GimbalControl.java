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
import afrl.cmasi.GimbalAngleAction;
import afrl.cmasi.GimbalConfiguration;
import afrl.cmasi.GimbalPointingMode;
import afrl.cmasi.GimbalScanAction;
import afrl.cmasi.GimbalStareAction;
import afrl.cmasi.GimbalState;
import afrl.cmasi.Location3D;
import afrl.cmasi.PayloadState;
import afrl.cmasi.VehicleAction;
import afrl.cmasi.VehicleActionCommand;
import avtas.amase.entity.EntityModule;
import avtas.data.Unit;
import avtas.math.Quaternion;
import avtas.util.NavUtils;
import avtas.terrain.TerrainService;
import java.util.ArrayList;
import avtas.xml.Element;

/**
 * Performs gimbal actions as commanded by a MissionCommand or VehicleActionCommand object.
 * 
 * @author AFRL/RQQD
 */
public class GimbalControl extends EntityModule {

    protected GimbalConfiguration gimbalConfig;
    protected GimbalState gimbalState;
    protected VehicleAction currentAction = null;
    protected ScanTracker scanTracker = null;

    /**
     * Creates a new sensor control module.  A SensorConfiguration configures this module to obey
     * the limits of the sensor it is modeling.
     *
     * @param gc the configuration for this sensor
     */
    public GimbalControl(GimbalConfiguration gc) {
        this.gimbalConfig = gc;
        this.gimbalState = new GimbalState();

        //init the sensor state as the middle of the limits for the gimbal.
        gimbalState.setAzimuth(gc.getMinAzimuth() + (gc.getMaxAzimuth() - gc.getMinAzimuth()) / 2f);
        gimbalState.setElevation(gc.getMinElevation() + (gc.getMaxElevation() - gc.getMinElevation()) / 2f);
        gimbalState.setPayloadID(gc.getPayloadID());
    }

    /** updates the sensor action if there is a current action commanded for this sensor.
     *
     * @param timestep_sec  The simulation timestep in seconds.
     */
    public void step(double timestep_sec, double simtime_sec) {
        if (currentAction instanceof GimbalAngleAction) {
            GimbalAngleAction ga = (GimbalAngleAction) currentAction;
            slew(ga.getAzimuth(), ga.getElevation(), timestep_sec);
        } else if (currentAction instanceof GimbalStareAction) {
            GimbalStareAction ga = (GimbalStareAction) currentAction;
            updateStare(ga.getStarepoint(), timestep_sec);
        } else if (currentAction instanceof GimbalScanAction) {
            if (scanTracker == null) {
                scanTracker = new ScanTracker((GimbalScanAction) currentAction);
            }
        }
        if (scanTracker != null) {
            scanTracker.update(timestep_sec);
        }

        checkBounds(gimbalState);
    }

    @Override
    public void initialize(Element xmlElement) {
        currentAction = null;
    }

    /** slews the sensor to the commanded azimuth and elevation.
     * 
     * @param degCmdAz commanded azimuth in degrees.
     * @param degCmdEl commanded elevation in degrees.
     * @param timestep_sec timestep in seconds.
     * @param azSlewRate the rate that the sensor is to slew in the azimuthal direction (deg/sec).
     * @param elSlewRate the rate that the sensor is to slew in the elevational direction (deg/sec).
     * @return true if the sensor has reached its commanded scan point
     */
    private boolean slew(double degCmdAz, double degCmdEl, double timestep_sec,
            double azSlewRate, double elSlewRate) {

        // if this is a full 360 sensor, then set the azimuth difference to the small angle
        double azDiff = degCmdAz - gimbalState.getAzimuth();
        if ( Unit.bound180(gimbalConfig.getMaxAzimuth() - gimbalConfig.getMinAzimuth()) ==0 ) {
            azDiff = Unit.bound180(azDiff);
        }
        
        double elDiff = degCmdEl - gimbalState.getElevation();

        azSlewRate = Math.min(azSlewRate, gimbalConfig.getMaxAzimuthSlewRate());
        elSlewRate = Math.min(elSlewRate, gimbalConfig.getMaxElevationSlewRate());

        if (Math.abs(azDiff) < azSlewRate * timestep_sec) {
            azDiff = 0;
            gimbalState.setAzimuth((float) degCmdAz);
        } else {
            azSlewRate = azSlewRate * Math.signum(azDiff);
            gimbalState.setAzimuth((float) Unit.bound180((gimbalState.getAzimuth() + azSlewRate * timestep_sec)));
        }

        if (Math.abs(elDiff) < elSlewRate * timestep_sec) {
            elDiff = 0;
            gimbalState.setElevation((float) degCmdEl);
        } else {
            elSlewRate = elSlewRate * Math.signum(elDiff);
            gimbalState.setElevation((float) Unit.bound180((gimbalState.getElevation() + elSlewRate * timestep_sec)));
        }

        if (azDiff == 0 && elDiff == 0) {
            return true;
        }
        return false;
    }

    /** checks azimuth, elevation and fields-of-view to make sure they are within the limits of the
     *  sensor configuration.  The state message is changed if the current limits are outside the bounds.
     * @param state The state message to check and change if necessary
     */
    private void checkBounds(GimbalState state) {
        
        state.setAzimuth(Math.min(Math.max(gimbalConfig.getMinAzimuth(), (float) state.getAzimuth()), gimbalConfig.getMaxAzimuth()));
        state.setElevation(Math.min(Math.max(gimbalConfig.getMinElevation(), (float) state.getElevation()), gimbalConfig.getMaxElevation()));
        
        
        if (currentAction instanceof GimbalAngleAction || currentAction == null) {
            state.setPointingMode(GimbalPointingMode.AirVehicleRelativeAngle);
        } else if (currentAction instanceof GimbalStareAction) {
            state.setPointingMode(GimbalPointingMode.LatLonSlaved);
        } else if (currentAction instanceof GimbalScanAction) {
            state.setPointingMode(GimbalPointingMode.Scan);
        }
    }

    /** 
     * Slews the sensor to the commanded azimuth and elevation.
     * 
     * @param radCmdAz commanded azimuth in degrees.
     * @param radCmdEl commanded elevation in degrees.
     * @param timestep_sec timestep in seconds.
     */
    private void slew(double degCmdAz, double degCmdEl, double timestep_sec) {
        slew(degCmdAz, degCmdEl, timestep_sec, gimbalConfig.getMaxAzimuthSlewRate(),
                gimbalConfig.getMaxElevationSlewRate());

    }

    /** points the sensor at a geographic location.
     * 
     * @param sp location that the sensor is to look at
     * @param timestep_sec timestep in seconds (for slew control)
     */
    public void updateStare(Location3D sp, double timestep_sec) {

        double tgtDist = NavUtils.distance(getData().lat.asDouble(), getData().lon.asDouble(),
                Math.toRadians(sp.getLatitude()), Math.toRadians(sp.getLongitude()));
        double tgtAz = NavUtils.headingBetween(getData().lat.asDouble(), getData().lon.asDouble(),
                Math.toRadians(sp.getLatitude()), Math.toRadians(sp.getLongitude()));

        double nDist = tgtDist * Math.cos(tgtAz);
        double eDist = tgtDist * Math.sin(tgtAz);
        double height = getData().alt.asDouble() - sp.getAltitude();
        if (sp.getAltitude() == 0) {
            height -= TerrainService.getElevation(sp.getLatitude(), sp.getLongitude());
            // code to compute depression angle due to curvature.  this math needs to be checked.
            double arcDist = NavUtils.arcDistance(data.lat.asDouble(), data.lon.asDouble(), Math.toRadians(sp.getLatitude()), Math.toRadians(sp.getLongitude()));
            height -= NavUtils.EARTH_EQ_RADIUS_M * (1 - Math.cos(arcDist)) * Math.cos(arcDist);
        }

        Quaternion q = new Quaternion(getData().psi.asDouble(), -getData().theta.asDouble(), -getData().phi.asDouble());
        double[] bodyVector = q.getUVW(nDist, eDist, -height);

        double sensorAz = Math.atan2(bodyVector[1], bodyVector[0]);
        double projVect = Math.sqrt(bodyVector[0] * bodyVector[0] + bodyVector[1] * bodyVector[1]);
        double sensorEl = Math.atan2(bodyVector[2], projVect);

        slew(Math.toDegrees(sensorAz), Math.toDegrees(sensorEl), timestep_sec);
    }


    /** {@inheritDoc} */
    public void modelEventOccurred(Object object) {
        if (object instanceof AirVehicleState) {
            // if the air vehicle state contains a gimbal state, set this gimbal state to the one in the
            // message, otherwise load the message with this state.  this allows the initial condition
            // of the gimbal to be set according to the initial air vehicle state message
            ArrayList<PayloadState> list = ((AirVehicleState) object).getPayloadStateList();
            boolean contained = false;
            for (PayloadState ps : list) {
                if (ps != null && ps.getPayloadID() == gimbalConfig.getPayloadID()) {
                    this.gimbalState = (GimbalState) ps;
                    contained = true;
                }
            }
            if (!contained) {
                list.add(gimbalState);
            }
            if (gimbalState != null) {
                checkBounds(gimbalState);
            }

        } else if (object instanceof VehicleActionCommand) {
            for (VehicleAction va : ((VehicleActionCommand) object).getVehicleActionList()) {
                modelEventOccurred(va);
            }
        } else if (object instanceof VehicleAction) {
            if (object instanceof GimbalAngleAction) {
                if (((GimbalAngleAction) object).getPayloadID() == gimbalConfig.getPayloadID()) {
                    currentAction = (VehicleAction) object;
                }
            } else if (object instanceof GimbalStareAction) {
                if (((GimbalStareAction) object).getPayloadID() == gimbalConfig.getPayloadID()) {
                    currentAction = (VehicleAction) object;
                }
            } else if (object instanceof GimbalScanAction) {
                if (((GimbalScanAction) object).getPayloadID() == gimbalConfig.getPayloadID()) {
                    currentAction = (VehicleAction) object;
                    scanTracker = new ScanTracker((GimbalScanAction) object);
                }
            }
        }
    }

    /** sets up a control to move the sensor along a box shaped scan pattern.
     *  It uses the {@link GimbalScanAction} parameters to control sensor movement.
     */
    protected class ScanTracker {

        GimbalScanAction ssa;
        int currentCycle = 0;
        int currentCorner = 0;

        public ScanTracker(GimbalScanAction ssa) {
            this.ssa = ssa;
        }

        public void update(double timestep_sec) {
            if (ssa.getCycles() > 0 && currentCycle > ssa.getCycles()) {
                return;
            }
            switch (currentCorner) {
                case 0:
                    if (slew(ssa.getStartAzimuth(), ssa.getStartElevation(), timestep_sec,
                            ssa.getAzimuthSlewRate(), ssa.getElevationSlewRate())) {
                        currentCorner++;
                    }
                    break;
                case 1:
                    if (slew(ssa.getStartAzimuth(), ssa.getEndElevation(), timestep_sec,
                            ssa.getAzimuthSlewRate(), ssa.getElevationSlewRate())) {
                        currentCorner++;
                    }
                    break;
                case 2:
                    if (slew(ssa.getEndAzimuth(), ssa.getEndElevation(), timestep_sec,
                            ssa.getAzimuthSlewRate(), ssa.getElevationSlewRate())) {
                        currentCorner++;
                    }
                    break;
                case 3:
                    if (slew(ssa.getEndAzimuth(), ssa.getStartElevation(), timestep_sec,
                            ssa.getAzimuthSlewRate(), ssa.getElevationSlewRate())) {
                        currentCorner = 0;
                        currentCycle++;
                    }
                    break;

            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */