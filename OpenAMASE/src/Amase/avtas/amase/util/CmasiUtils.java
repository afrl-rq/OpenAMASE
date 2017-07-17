// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.util;

import afrl.cmasi.AbstractGeometry;
import afrl.cmasi.AbstractZone;
import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.CameraConfiguration;
import afrl.cmasi.CameraState;
import afrl.cmasi.Circle;
import afrl.cmasi.EntityState;
import afrl.cmasi.FlightProfile;
import afrl.cmasi.GimbalConfiguration;
import afrl.cmasi.GimbalState;
import afrl.cmasi.Location3D;
import afrl.cmasi.MissionCommand;
import afrl.cmasi.PayloadConfiguration;
import afrl.cmasi.PayloadState;
import afrl.cmasi.Polygon;
import afrl.cmasi.Rectangle;
import afrl.cmasi.Task;
import afrl.cmasi.VehicleAction;
import afrl.cmasi.VehicleActionCommand;
import afrl.cmasi.Waypoint;
import avtas.lmcp.LMCPObject;
import avtas.math.Quaternion;
import avtas.terrain.TerrainService;
import avtas.util.NavUtils;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilities to help with reading and creating CMASI-based objects.
 *
 * @author AFRL/RQQD
 */
public class CmasiUtils {

    /** converts a CMASI geometry to a Java Path2D in (lon, lat) coordinates. */
    public static Path2D convertPoly(List<? extends afrl.cmasi.Location3D> points) {
        Path2D path = new Path2D.Double();
        Location3D loc = null;
        if (points.size() > 0) {
            loc = points.get(0);
            path.moveTo(loc.getLongitude(), loc.getLatitude());
        }
        for (int i = 1; i < points.size(); i++) {
            loc = points.get(i);
            path.lineTo(loc.getLongitude(), loc.getLatitude());
        }
        return path;
    }

    /** converts a CMASI geometry to a Java Path2D in (lon, lat) coordinates. */
    public static Path2D convertPoly(AbstractGeometry geom) {
        Path2D path = convertPoly(getPoints(geom));
        path.closePath();
        return path;
    }

    public static ArrayList<Location3D> getPoints(AbstractGeometry geom) {
        if (geom.getClass() == AbstractGeometry.class) {
            return new ArrayList<Location3D>();
        }
        if (geom instanceof Polygon) {
            return ((Polygon) geom).getBoundaryPoints();
        }
        if (geom instanceof Rectangle) {
            ArrayList<Location3D> list = new ArrayList<Location3D>();
            Rectangle rect = (Rectangle) geom;
            Location3D loc = rect.getCenterPoint();
            
            double rotAngle = rect.getRotation();
            double hypot = Math.hypot(0.5 * rect.getWidth(), 0.5 * rect.getHeight());
            double cornerAngle = Math.toDegrees(Math.atan2(rect.getWidth(), rect.getHeight()));
            
            // traverse the rectangle and add points to the returned list
            list.add(CmasiNavUtils.getPoint(loc, hypot, rotAngle - cornerAngle));
            list.add(CmasiNavUtils.getPoint(loc, hypot, rotAngle + cornerAngle));
            list.add(CmasiNavUtils.getPoint(loc, hypot, rotAngle + 180 - cornerAngle));
            list.add(CmasiNavUtils.getPoint(loc, hypot, rotAngle + 180 + cornerAngle));

            return list;
        }
        if (geom instanceof Circle) {
            return getPoints((Circle) geom, 10);
        }
        return null;
    }

    

    /** Creates a polygonal shape from a circular shape by circumscribing
     *  an n-sided polygon.
     * @param numSides the number of sides in the created polygon.
     */
    public static ArrayList<Location3D> getPoints(Circle circle, int numSides) {
        double rp = circle.getRadius() / Math.cos(Math.PI / numSides);
        double dtheta = 2 * Math.PI / numSides;
        double radCenLat = Math.toRadians(circle.getCenterPoint().getLatitude());
        double radCenLon = Math.toRadians(circle.getCenterPoint().getLongitude());
        ArrayList<Location3D> retList = new ArrayList<Location3D>();

        for (int i = 0; i < numSides; i++) {
            double[] llp = NavUtils.getLatLon(radCenLat, radCenLon, rp, dtheta * i);
            Location3D loc = new Location3D();
            loc.setLatitude(Math.toDegrees(llp[0]));
            loc.setLongitude(Math.toDegrees(llp[1]));
            retList.add(loc);
        }

        return retList;
    }

    /** searches the mission command for a Waypoint with the given waypoint number.
     *
     * @param mc The mission command containing the desired waypoint
     * @param wpNumber The number of the desired waypoint
     * @return The waypoint, or null if none is found.
     */
    public static Waypoint getWaypoint(MissionCommand mc, long wpNumber) {
        for (Waypoint wp : mc.getWaypointList()) {
            if (wp.getNumber() == wpNumber) {
                return wp;
            }
        }
        return null;
    }

    /** returns a set of VehicleActions of the specified type (class) at the given waypoint.
     *
     * @param mc The missionCommand containing the waypoint
     * @param wpNumber The waypoint number
     * @param actionType The type (class) of the action that should be returned.  A class type
     * of "VehicleAction" will return all vehicleActions.
     * @return An array of VehicleActions of type "actionType" at the given waypoint, or an empty
     * array if the waypoint does not exist.
     */
    public static VehicleAction[] getActionsAtWaypoint(MissionCommand mc, long wpNumber, Class actionType) {
        Waypoint wp = getWaypoint(mc, wpNumber);
        if (wp != null) {
            return getActions(wp, actionType);
        }
        return null;
    }

    /** returns a set of VehicleActions of the specified type (class) at the given waypoint.
     *
     * @param wp The waypoint to extract actions from.
     * @param actionType The type (class) of the action that should be returned.  A class type
     * of "VehicleAction" will return all vehicleActions.
     * @return An array of VehicleActions of type "actionType" at the given waypoint, or an empty
     * array if the waypoint does not exist.
     */
    public static VehicleAction[] getActions(Waypoint wp, Class actionType) {
        List<VehicleAction> retList = new ArrayList<>();
        if (wp != null) {
            for (VehicleAction va : wp.getVehicleActionList()) {
                if (actionType.isInstance(va)) {
                    retList.add(va);
                }
            }
        }

        return retList.toArray(new VehicleAction[]{});
    }

    /** returns a set of VehicleActions of the specified type (class) in the mission command.
     *
     * @param vc The VehicleActionCommand containing the action list
     * @param actionType The type (class) of the action that should be returned.  A class type
     * of "VehicleAction" will return all vehicleActions.
     * @return An array of VehicleActions of type "actionType" in the VehicleActionList, or an empty
     * array if the waypoint does not exist.
     */
    public static VehicleAction[] getVehicleActions(VehicleActionCommand vc, Class actionType) {
        List<VehicleAction> retList = new ArrayList<>();
        for (VehicleAction va : vc.getVehicleActionList()) {
            if (actionType.isInstance(va)) {
                retList.add(va);
            }
        }
        return retList.toArray(new VehicleAction[]{});
    }

    /** Returns the payload configuration with the corresponding payload id or null if no payload with
     * the requested id is found.
     */
    public static PayloadConfiguration getPayloadConfig(long payloadId, AirVehicleConfiguration avc) {
        return getPayloadConfig(payloadId, avc.getPayloadConfigurationList());
    }
    
    /** Returns the payload configuration with the corresponding payload id or null if no payload with
     * the requested id is found.
     */
    public static PayloadConfiguration getPayloadConfig(long payloadId, List<PayloadConfiguration> payloadList) {
        for (PayloadConfiguration pc : payloadList) {
            if (pc.getPayloadID() == payloadId) {
                return pc;
            }
        }
        return null;
    }
    
    
    /**
     * Returns a list of all payload configurations that are of the requested type.  This returns
     * exact class matches as well as subclasses.
     * @param <T>
     * @param type class of the payload config
     * @param payloadList list from which to get configs
     * @return a new list of configs that are of the requested class (or subclass)
     */
    public static <T> List<T> getPayloadsByType(Class<T> type, List<PayloadConfiguration> payloadList) {
        List<T> retList = new ArrayList<T>();
        for (PayloadConfiguration pc : payloadList) {
            if (type.isInstance(pc)) {
                retList.add( (T) pc);
            }
        }
        return retList;
    }
    
    /**
     * Returns a list of all payload states that are of the requested type.  This returns
     * exact class matches as well as subclasses.
     * @param <T>
     * @param type class of the payload state
     * @param payloadList list from which to get states
     * @return a new list of states that are of the requested class (or subclass)
     */
    public static <T> List<T> getPayloadStatesByType(Class<T> type, List<PayloadState> payloadList) {
        List<T> retList = new ArrayList<T>();
        for (PayloadState ps : payloadList) {
            if (type.isInstance(ps)) {
                retList.add( (T) ps);
            }
        }
        return retList;
    }

    /**
     * Returns the gimbal that contains the given payload, or null if no gimbal is found.
     * @param payloadId the payload contained by the gimbal.
     * @return The gimbal that contains the payload (or null if no gimbal is found)
     */
    public static GimbalConfiguration getGimbalForPayload(long payloadId, AirVehicleConfiguration avc) {
        return getGimbalForPayload(payloadId, avc.getPayloadConfigurationList());
    }
    
    /**
     * Returns the gimbal that contains the given payload, or null if no gimbal is found.
     * @param payloadId the payload contained by the gimbal.
     * @return The gimbal that contains the payload (or null if no gimbal is found)
     */
    public static GimbalConfiguration getGimbalForPayload(long payloadId, List<PayloadConfiguration> payloadList) {
        for (PayloadConfiguration pc : payloadList) {
            if (pc instanceof GimbalConfiguration) {
                GimbalConfiguration gc = (GimbalConfiguration) pc;
                if (gc.getContainedPayloadList().contains(payloadId)) {
                    return gc;
                }
            }
        }
        return null;
    }

    /** Returns the payload state with the corresponding payload id or null if no payload with
     * the requested id is found.
     */
    public static PayloadState getPayloadState(long id, AirVehicleState avs) {
        return getPayloadState(id, avs.getPayloadStateList());
    }
    
    /** Returns the payload state with the corresponding payload id or null if no payload with
     * the requested id is found.
     */
    public static PayloadState getPayloadState(long id, List<PayloadState> payloadList) {
        for (PayloadState ps : payloadList) {
            if (ps.getPayloadID() == id) {
                return ps;
            }
        }
        return null;
    }

    /** returns the intercept point of the given quaternion assuming flat-plane earth */
    public static double[] getLocation(Quaternion q, Location3D origin, double maxdist_meter) {

        double[] dxdydz = q.getDxDyDz(1, 0, 0);
        if (dxdydz[2] < 0) {
            double val = maxdist_meter;
            dxdydz[0] = dxdydz[0] * val;
            dxdydz[1] = dxdydz[1] * val;
            dxdydz[2] = 0;
        } else {
            dxdydz[0] = dxdydz[0] * origin.getAltitude() / dxdydz[2];
            dxdydz[1] = dxdydz[1] * origin.getAltitude() / dxdydz[2];
            dxdydz[2] = dxdydz[2] * origin.getAltitude() / dxdydz[2];
        }
        double[] sensorLoc = NavUtils.simpleLatLon(Math.toRadians(origin.getLatitude()), Math.toRadians(origin.getLongitude()),
                origin.getAltitude(), dxdydz[0], dxdydz[1]);
        return new double[]{Math.toDegrees(sensorLoc[0]), Math.toDegrees(sensorLoc[1])};
    }

    /** Returns a flight profile with the requested name, or null if none is found.
     *
     * @param avc the configuration to search through
     * @param profileName name of requested profile (case insensitive)
     * @return a flight profile with the requested name, or the nominal profile if none is found.
     */
    public static FlightProfile getProfile(AirVehicleConfiguration avc, String profileName) {
        for (FlightProfile p : avc.getAlternateFlightProfiles()) {
            if (p.getName().equalsIgnoreCase(profileName)) {
                return p;
            }
        }
        return avc.getNominalFlightProfile();
    }

    /**
     * Returns a footprint graphic in geographic coordinates (y=latitude, x=longitude, degrees) for a given
     * sensor and maximum ground sample distance.
     *
     * @param gs current state of the gimbal
     * @param cc configuration for the camera
     * @param cs current state of the camera
     * @param avs current state of the UAVS
     * @return a shape that represents the four corners of the camera field of view.  Returns null if any
     * null parameters are passed.
     */
    public static Path2D getFootprint(GimbalState gs, CameraConfiguration cc, CameraState cs, AirVehicleState avs) {

        if (gs == null || cs == null || cc == null || avs == null) {
            return null;
        }

        double meterAlt = avs.getLocation().getAltitude();
        double phi = Math.toRadians(avs.getRoll());
        double theta = Math.toRadians(avs.getPitch());
        double psi = Math.toRadians(avs.getHeading());
        double radLat = Math.toRadians(avs.getLocation().getLatitude());
        double radLon = Math.toRadians(avs.getLocation().getLongitude());

        // get the height above terrain
        double elev = TerrainService.getElevation(avs.getLocation().getLatitude(), avs.getLocation().getLongitude());
        meterAlt = meterAlt - elev;

        double maxDist = NavUtils.distanceToHorizon(meterAlt);

        double aspectRatio = cc.getVideoStreamHorizontalResolution() / cc.getVideoStreamVerticalResolution();

        double epsilonX = Math.toRadians(gs.getAzimuth());
        double epsilonZ = Math.toRadians(gs.getElevation());
        double fovX = Math.toRadians(cs.getHorizontalFieldOfView() / 2d);
        double fovY = Math.toRadians(cs.getHorizontalFieldOfView() / 2d / aspectRatio);
        // default to 3x4 aspect ratio
        if (fovY == 0) {
            fovY = fovX * 0.75;
        }

        //takes the body quaternion at the current euler state and multiplies it by the
        //sensor boresight quaternion to find the inertial frame quaternion that defines the
        //boresight of the sensor.
        Quaternion center = Quaternion.multiply(new Quaternion(psi, theta, phi),
                new Quaternion(epsilonX, epsilonZ, 0));

        ArrayList<Location3D> cornerPoints = new ArrayList<Location3D>();
        cornerPoints.add(getLocation(center, radLat, radLon, meterAlt, -fovX, -fovY, maxDist));
        cornerPoints.add(getLocation(center, radLat, radLon, meterAlt, fovX, -fovY, maxDist));
        cornerPoints.add(getLocation(center, radLat, radLon, meterAlt, fovX, fovY, maxDist));
        cornerPoints.add(getLocation(center, radLat, radLon, meterAlt, -fovX, fovY, maxDist));

        return CmasiUtils.convertPoly(cornerPoints);
    }

    /** Computes the location of the sensor footprint in degrees (lat, lon).  This creates a unit vector
     * that describes moving away from the origin with a azimuth (body referenced) offset and a
     * elevation (body referenced) offset.  It forms a ray that is multiplied by the origin height to find an
     * intercept with the terrain.  Assumes a flat earth, constant height terrain equal to the origin height.
     *
     * @param q quaternion describing the angle of rotation
     * @param origin location of the origin
     * @param fovX the field-of-view in the x-direction (positive to the right)
     * @param fovY the field-of-view in the y-direction (positive upwards)
     * @param maxdist_meter maximum distance to draw the footprint in case the footprint
     * does not intersect the ground or intersects far away.
     * @return location of the sensor footprint in degrees (lat, lon)
     */
    private static Location3D getLocation(Quaternion q, double radLat, double radLon, double meterAlt,
            double fovX, double fovY, double maxdist_meter) {

        double[] dxdydz = q.getDxDyDz(1, Math.tan(fovX), Math.tan(fovY));

        double dist = meterAlt / dxdydz[2];
        
        // if angle is looking up, or distance is greater than max distance,
        // then clamp distance to the max distance specified.
        dist = (dist < 0 || dist > maxdist_meter) ? maxdist_meter : dist;

        dxdydz[0] = dxdydz[0] * dist;
        dxdydz[1] = dxdydz[1] * dist;

        double[] sensorLoc = NavUtils.simpleLatLon(radLat, radLon, meterAlt, dxdydz[0], dxdydz[1]);
        Location3D loc = new Location3D();
        loc.setLatitude(Math.toDegrees(sensorLoc[0]));
        loc.setLongitude(Math.toDegrees(sensorLoc[1]));
        loc.setAltitude(0);
        return loc;
    }

    /**
     * Returns the unique identifier associated with the object.  For many
     * CMASI objects, there is a unique number that identifies a particular
     * entity (e.g. AirVehicleConfigurations have VehicleID, Tasks have TaskID)
     * Returns -1 is no unique id exists for the type.
     *
     * @param obj the object to test
     * @return the unique identifier or -1 if no unique identifier applies.
     */
    public static long getUniqueId(LMCPObject obj) {
        if (obj instanceof Task) {
            return ((Task) obj).getTaskID();
        }
        if (obj instanceof AirVehicleConfiguration) {
            return ((AirVehicleConfiguration) obj).getID();
        }
        if (obj instanceof AirVehicleState) {
            return ((AirVehicleState) obj).getID();
        }
        if (obj instanceof AbstractZone) {
            return ((AbstractZone) obj).getZoneID();
        }
        if (obj instanceof MissionCommand) {
            return ((MissionCommand) obj).getVehicleID();
        }
        if (obj instanceof VehicleActionCommand) {
            return ((VehicleActionCommand) obj).getVehicleID();
        }
        if (obj instanceof EntityState) {
            return ((EntityState) obj).getID();
        }
        return -1;
    }


    /** Returns the distance between 2 points
     * 
     */
    public static double getDistance(Location3D pt1, Location3D pt2) {
        return NavUtils.distance(Math.toRadians(pt1.getLatitude()), Math.toRadians(pt1.getLongitude()),
                Math.toRadians(pt2.getLatitude()), Math.toRadians(pt2.getLongitude()) );
    }

    /**
     * Calculates slant distance between two points using great-circle Earth approximation and
     * change in altitude.
     * @param pt1 first point considered.
     * @param pt2 second point considered.
     * @return 3D distance in meters.
     */
    public static double getDistance3D(Location3D pt1, Location3D pt2) {
        return Math.hypot(getDistance(pt1, pt2), pt1.getAltitude() - pt2.getAltitude());
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */
