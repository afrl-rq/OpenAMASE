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
import afrl.cmasi.AltitudeType;
import afrl.cmasi.CameraAction;
import afrl.cmasi.CameraConfiguration;
import afrl.cmasi.CameraState;
import afrl.cmasi.FOVOperationMode;
import afrl.cmasi.GimbalConfiguration;
import afrl.cmasi.GimbalState;
import afrl.cmasi.Location3D;
import afrl.cmasi.PayloadState;
import afrl.cmasi.VehicleAction;
import afrl.cmasi.VehicleActionCommand;
import avtas.amase.entity.EntityModule;
import avtas.app.UserExceptions;
import avtas.data.Property;
import avtas.math.Quaternion;
import avtas.terrain.TerrainService;
import avtas.util.NavUtils;
import avtas.xml.Element;

/**
 * Performs camera actions as commanded by a MissionCommand or
 * VehicleActionCommand object.
 *
 * @author AFRL/RQQD
 */
public class CameraControl extends EntityModule {

    private CameraConfiguration cameraConfig;
    private CameraState cameraState;
    private GimbalState gimbalState;
    private CameraAction currentAction = null;
    private double aspectRatio = 0;
    private long gimbalID = 0;
    private Property maxGSD = new Property("maxGSD", 3);

    /**
     * Creates a new sensor control module. A SensorConfiguration configures
     * this module to obey the limits of the sensor it is modeling.
     *
     * @param gc the configuration for this sensor
     */
    public CameraControl(CameraConfiguration cc, GimbalConfiguration gc) {
        
        if (gc == null) {
            UserExceptions.showError(this, "Bad Configuration data for entity " + getModel().getID() + 
                    ".  Camera " + cc.getPayloadID() + " is not attached to a gimbal" , null);
        }
        
        this.cameraConfig = cc;
        this.cameraState = new CameraState();
        this.gimbalID = gc.getPayloadID();

        gimbalState = new GimbalState();
        gimbalState.setPayloadID(gc.getPayloadID());
        if (gc.getIsAzimuthClamped()) {
            gimbalState.setAzimuth(gc.getMinAzimuth());
        }
        if (gc.getIsElevationClamped()) {
            gimbalState.setElevation(gc.getMinElevation());
        }

        aspectRatio = cc.getVideoStreamHorizontalResolution() / (double) cc.getVideoStreamVerticalResolution();
        cameraState.setHorizontalFieldOfView(cc.getMinHorizontalFieldOfView()
                + (cc.getMaxHorizontalFieldOfView() - cc.getMinHorizontalFieldOfView()) / 2f);
        cameraState.setPayloadID(cc.getPayloadID());
    }

    /**
     * updates the sensor action if there is a current action commanded for this
     * sensor.
     *
     * @param timestep_sec The simulation timestep in seconds.
     */
    @Override
    public void step(double timestep_sec, double simtime_sec) {
        if (currentAction != null) {
            cameraState.setHorizontalFieldOfView(currentAction.getHorizontalFieldOfView());
            checkBounds(cameraState);
            currentAction = null;
        }
    }

    /**
     * Sets up this module.
     *
     * @param xmlElement xml configuration data for this module
     */
    @Override
    public void initialize(Element xmlElement) {
        currentAction = null;
    }

    /**
     * checks azimuth, elevation and fields-of-view to make sure they are within
     * the limits of the sensor configuration. The state message is changed if
     * the current limits are outside the bounds.
     *
     * @param state The state message to check and change if necessary
     */
    private void checkBounds(CameraState state) {

        if (cameraConfig.getFieldOfViewMode() == FOVOperationMode.Discrete) {
            double fov = state.getHorizontalFieldOfView();
            double mindif = Float.MAX_VALUE;
            int mindif_index = 0;
            for (int i = 0; i < cameraConfig.getDiscreteHorizontalFieldOfViewList().size(); i++) {
                double dif = Math.abs(state.getHorizontalFieldOfView() - cameraConfig.getDiscreteHorizontalFieldOfViewList().get(i));
                if (dif < mindif) {
                    mindif = dif;
                    mindif_index = i;
                }
            }
            state.setHorizontalFieldOfView(cameraConfig.getDiscreteHorizontalFieldOfViewList().get(mindif_index));
        } else {
            if (state.getHorizontalFieldOfView() > cameraConfig.getMaxHorizontalFieldOfView()) {
                state.setHorizontalFieldOfView(cameraConfig.getMaxHorizontalFieldOfView());
            }
            if (state.getHorizontalFieldOfView() < cameraConfig.getMinHorizontalFieldOfView()) {
                state.setHorizontalFieldOfView(cameraConfig.getMinHorizontalFieldOfView());
            }
        }
        state.setVerticalFieldOfView((float) (state.getHorizontalFieldOfView() / aspectRatio));
    }

    @Override
    public void modelEventOccurred(Object object) {
        if (object instanceof AirVehicleState) {

            // if this is the initial AirVehicleState, then the camera state from this object should be loaded from the AirVehicleState,
            // otherwise, load the current camera state into the message. Also update the current gimbal state with the one from this
            // object.
            AirVehicleState avs = (AirVehicleState) object;
            boolean contained = false;

            for (PayloadState s : avs.getPayloadStateList()) {
                if (s instanceof CameraState) {
                    CameraState cs = (CameraState) s;
                    if (cs.getPayloadID() == cameraConfig.getPayloadID()) {
                        this.cameraState = cs;
                        contained = true;
                    }
                }
                if (s instanceof GimbalState && s.getPayloadID() == gimbalID) {
                    this.gimbalState = (GimbalState) s;
                }
            }

            if (!contained) {
                avs.getPayloadStateList().add(cameraState);
            }

            if (cameraState != null) {
                checkBounds(cameraState);
                cameraState.setCenterpoint(null); //forces update of computed centerpoint
                updateFootprint(cameraState, gimbalState, (AirVehicleState) object, aspectRatio);
            }


        } else if (object instanceof VehicleActionCommand) {
            for (VehicleAction va : ((VehicleActionCommand) object).getVehicleActionList()) {
                if (va instanceof CameraAction) {
                    if (((CameraAction) va).getPayloadID() == cameraConfig.getPayloadID()) {
                        currentAction = (CameraAction) va;
                    }
                }
            }
        } else if (object instanceof CameraAction) {
            CameraAction ca = (CameraAction) object;
            if (ca.getPayloadID() == cameraConfig.getPayloadID()) {
                currentAction = ca;
            }
        }
    }

    public static void updateFootprint(CameraState cameraState, GimbalState gimbalState, AirVehicleState vehicleState, double aspectRatio) {

        if (gimbalState == null || cameraState == null) {
            return;
        }
        // copy values from Gimbal State into CameraState
        cameraState.setAzimuth(gimbalState.getAzimuth());
        cameraState.setElevation(gimbalState.getElevation());
        cameraState.setRotation(gimbalState.getRotation());
        cameraState.setPointingMode(gimbalState.getPointingMode());

        double meterAlt = vehicleState.getLocation().getAltitude();
        double phi = Math.toRadians(vehicleState.getRoll());
        double theta = Math.toRadians(vehicleState.getPitch());
        double psi = Math.toRadians(vehicleState.getHeading());
        double radLat = Math.toRadians(vehicleState.getLocation().getLatitude());
        double radLon = Math.toRadians(vehicleState.getLocation().getLongitude());

        // get the height above terrain
        //double elev = DTEDStaticCache.getElevation(vehicleState.getLocation().getLatitude(), vehicleState.getLocation().getLongitude());
        //meterAlt = meterAlt - elev;

        // clamp the maximum distance based on the max Ground Sample Distance (GSD) specified
        //double maxDist = maxGSD.asDouble()
        //        / Math.sin(Math.toRadians(cameraState.getHorizontalFieldOfView()) / cameraConfig.getVideoStreamHorizontalResolution());

        double maxDist = NavUtils.distanceToHorizon(meterAlt);

        double epsilonX = Math.toRadians(gimbalState.getAzimuth());
        double epsilonY = Math.toRadians(gimbalState.getRotation());
        double epsilonZ = Math.toRadians(gimbalState.getElevation());
        double fovX = Math.toRadians(cameraState.getHorizontalFieldOfView() / 2d);
        double fovY = fovX / aspectRatio;
        if (fovY == 0) {
            fovY = fovX * 0.75;
        }

        Quaternion sensorQ = new Quaternion(epsilonX, epsilonZ, epsilonY);
        Quaternion bodyQ = new Quaternion(psi, theta, phi);

        //takes the body quaternion at the current euler state and multiplies it by the
        //sensor boresight quaternion to find the inertial frame quaternion that defines the
        //boresight of the sensor.
        Quaternion center = Quaternion.multiply(bodyQ, sensorQ);


        //find the intercept point of the center ray and the ground.  Accesses terrain elevation (if available)
        double[] lla = TerrainService.getInterceptPoint(vehicleState.getLocation().getLatitude(), vehicleState.getLocation().getLongitude(),
                meterAlt, center.getPsi(), center.getTheta(), maxDist, 1);
        // this becomes the height above ground for sensor ray calculations
        meterAlt = meterAlt - lla[2];

        //perceived location of the camera footprint center
        if (cameraState.getCenterpoint() == null) {
        	cameraState.setCenterpoint(new Location3D(lla[0], lla[1], (float) lla[2], AltitudeType.MSL));
        }

        cameraState.getFootprint().clear();
        cameraState.getFootprint().add(getLocation(center, radLat, radLon, meterAlt, -fovX, -fovY, maxDist));
        cameraState.getFootprint().add(getLocation(center, radLat, radLon, meterAlt, fovX, -fovY, maxDist));
        cameraState.getFootprint().add(getLocation(center, radLat, radLon, meterAlt, fovX, fovY, maxDist));
        cameraState.getFootprint().add(getLocation(center, radLat, radLon, meterAlt, -fovX, fovY, maxDist));
    }

    /**
     * Computes the location of the sensor footprint in degrees (lat, lon). This
     * creates a unit vector that describes moving away from the origin with a
     * azimuth (body referenced) offset and a elevation (body referenced)
     * offset. It forms a ray that is multiplied by the origin height to find an
     * intercept with the terrain. Assumes a flat earth, constant height terrain
     * equal to the origin height.
     *
     * @param q quaternion describing the angle of rotation
     * @param radLat latitude of origin in radians
     * @param radLon longitude of origin in radians
     * @param fovX the field-of-view in the x-direction (positive to the right)
     * @param fovY the field-of-view in the y-direction (positive upwards)
     * @param maxdist_meter maximum distance to draw the footprint in case the
     * footprint does not intersect the ground or intersects far away.
     * @return location of the sensor footprint in degrees (lat, lon)
     */
    protected static Location3D getLocation(Quaternion q, double radLat, double radLon, double meterAlt,
            double fovX, double fovY, double maxdist_meter) {

        double[] dxdydz = q.getDxDyDz( 1, Math.tan(fovX), Math.tan(fovY) );

        double dist = meterAlt / dxdydz[2];
        dist = (dist < 0 || dist > maxdist_meter) ? maxdist_meter : dist;

        dxdydz[0] = dxdydz[0] * dist;
        dxdydz[1] = dxdydz[1] * dist;

        double[] sensorLoc = NavUtils.simpleLatLon(radLat, radLon, 0, dxdydz[0], dxdydz[1]);
        Location3D loc = new Location3D();
        loc.setLatitude(Math.toDegrees(sensorLoc[0]));
        loc.setLongitude(Math.toDegrees(sensorLoc[1]));
        loc.setAltitude(0);
        return loc;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */
