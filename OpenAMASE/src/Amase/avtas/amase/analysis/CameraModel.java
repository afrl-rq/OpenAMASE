// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.analysis;

import avtas.amase.analysis.SearchGraphic.SearchPixel;
import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.CameraConfiguration;
import afrl.cmasi.CameraState;
import afrl.cmasi.GimbalConfiguration;
import afrl.cmasi.GimbalState;
import avtas.amase.util.CmasiUtils;
import avtas.util.NavUtils;
import java.awt.geom.Path2D;

/**
 * Creates a camera model that has the camera and gimbal information in one place.
 * @author AFRL/RQQD
 */
public class CameraModel {

    private AirVehicleConfiguration vehicleConfig = null;
    private CameraConfiguration cameraConfig = null;
    private CameraState cameraState = null;
    private GimbalConfiguration gimbalConfig = null;
    private GimbalState gimbalState = null;
    private Path2D footprint = null;
    private long cameraID = 0;
    private long vehicleID = 0;

    public CameraModel(AirVehicleConfiguration avc, CameraConfiguration cc, GimbalConfiguration gc) {
        this.vehicleConfig = avc;
        setCameraConfig(cc);
        setGimbalConfig(gc);
        this.cameraID = cc.getPayloadID();
        this.vehicleID = avc.getID();
    }

    /**
     * @return the cameraConfig
     */
    public CameraConfiguration getCameraConfig() {
        return cameraConfig;
    }

    /**
     * @param cameraConfig the cameraConfig to set
     */
    public void setCameraConfig(CameraConfiguration cameraConfig) {
        this.cameraConfig = cameraConfig;
        cameraID = cameraConfig.getPayloadID();
    }

    /**
     * @return the cameraState
     */
    public CameraState getCameraState() {
        return cameraState;
    }

    /**
     * @param cameraState the cameraState to set
     */
    public void setCameraState(CameraState cameraState) {
        this.cameraState = cameraState;
        this.footprint = CmasiUtils.convertPoly(cameraState.getFootprint());
    }

    /**
     * @return the gimbalConfig
     */
    public GimbalConfiguration getGimbalConfig() {
        return gimbalConfig;
    }

    /**
     * @param gimbalConfig the gimbalConfig to set
     */
    public void setGimbalConfig(GimbalConfiguration gimbalConfig) {
        this.gimbalConfig = gimbalConfig;
    }

    /**
     * @return the gimbalState
     */
    public GimbalState getGimbalState() {
        return gimbalState;
    }

    /**
     * @param gimbalState the gimbalState to set
     */
    public void setGimbalState(GimbalState gimbalState) {
        this.gimbalState = gimbalState;
    }

    /**
     * @return the footprint
     */
    public Path2D getFootprint() {
        return footprint;
    }

    /** Makes a first-pass attempt to determine the ground sample distance for a given camera against a
     *  given point.  This uses the current field-of-view and the horizontal resolution of the sensor.
     *  It returns a centerline computation (i.e. looking through the center of the sensor)
     * @param avs the current air vehicle state (for distance computation)
     * @param p the search pixel that should be considered.
     * @param altAgl altitude above ground level
     * @return the best-case ground sample distance.  This is the distance of ground that one pixel covers.
     */
    public double computeGSD(AirVehicleState avs, SearchPixel p, double altAgl) {

        // distance from aircraft to point on ground
        double dist = NavUtils.distance(Math.toRadians(avs.getLocation().getLatitude()),
                Math.toRadians(avs.getLocation().getLongitude()),
                Math.toRadians(p.lat), Math.toRadians(p.lon));
        // make that a 3d distance
        dist = Math.sqrt(dist * dist + altAgl * altAgl);

        // angle created for one pixel (fov / num pixels)
        double alpha = Math.toRadians(cameraState.getHorizontalFieldOfView()) / cameraConfig.getVideoStreamHorizontalResolution();

        // approx distance that is covered by the pixel (horizontal)  This is the ground sample distance
        return dist * Math.sin(alpha);
    }

    /**
     * @return the CameraID
     */
    public long getCameraID() {
        return cameraID;
    }

    /**
     * @return the vehicleConfig
     */
    public AirVehicleConfiguration getVehicleConfig() {
        return vehicleConfig;
    }

    /**
     * @param vehicleConfig the vehicleConfig to set
     */
    public void setVehicleConfig(AirVehicleConfiguration vehicleConfig) {
        this.vehicleConfig = vehicleConfig;
    }

    /**
     * @return the vehicleID
     */
    public long getID() {
        return vehicleID;
    }

    public static class CameraIndex {

        public CameraIndex(long vehicleId, long cameraId) {
            this.vehicleId = vehicleId;
            this.cameraId = cameraId;
        }
        public long vehicleId = 0;
        public long cameraId = 0;
    }

    public Path2D getFootprint(AirVehicleState vehicleState) {
        if (cameraState != null) {
            if (cameraState.getFootprint() != null) {
                return CmasiUtils.convertPoly(cameraState.getFootprint());
            }
            else {
                return CmasiUtils.getFootprint(gimbalState, cameraConfig, cameraState, vehicleState);
            }
        }
        return null;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */