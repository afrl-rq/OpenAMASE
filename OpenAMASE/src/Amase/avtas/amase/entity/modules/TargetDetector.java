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
import afrl.cmasi.CameraConfiguration;
import afrl.cmasi.CameraState;
import afrl.cmasi.EntityConfiguration;
import afrl.cmasi.EntityState;
import afrl.cmasi.Location3D;
import afrl.cmasi.PayloadConfiguration;
import afrl.cmasi.PayloadState;
import afrl.cmasi.perceive.EntityPerception;
import avtas.amase.entity.EntityModule;
import avtas.amase.util.CmasiUtils;
import avtas.amase.util.CmasiNavUtils;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Mimicks the detection of targets by a human operator using visual (EO/IR) sensors
 * @author AFRL/RQQD
 */
public class TargetDetector extends EntityModule {

    HashMap<Long, EntityState> targetStateMap = new HashMap<>();
    HashMap<Long, Double> timeFirstSeenMap = new HashMap<>();
    HashMap<Long, Double> timeLastSeenMap = new HashMap<>();
    HashMap<Long, CameraState> cameraStateMap = new HashMap<>();
    HashMap<Long, CameraConfiguration> cameraConfigMap = new HashMap<>();
    double simTime = 0;
    AppEventListener targetStateListener = null;
    double secTimeout = 10;
    double minTimeInView = 0;
    double minGSD = 1.0;  //required gsd to record a "hit"
    AppEventManager eventManager = null;
    private long vehicleId = 0;
    double testStep = 0.5;
    double lastTest = 0;
    
    EntityState latestState = null;
    

    public TargetDetector() {
    }

    @Override
    public void step(double timestep_sec, double simtime_sec) {
        
        // only do this test at the rate specified above.  Target detection
        // is CPU intensive
        simTime += timestep_sec;
        if (simTime >= lastTest + testStep) {
            updateDetections(simTime);
            lastTest = simTime;
        }
    }

    @Override
    public void initialize(Element xmlElement) {
        simTime = 0;
        
        // setup the parameters for the plugin
        secTimeout = XMLUtil.getDouble(xmlElement, "TrackTimeout", secTimeout);
        minTimeInView = XMLUtil.getDouble(xmlElement, "MinTimeInView", minTimeInView);
        minGSD = XMLUtil.getDouble(xmlElement, "MinGSD", minGSD);

    }

    @Override
    public void modelEventOccurred(Object object) {
        if (object instanceof EntityConfiguration) {
            EntityConfiguration avc = (EntityConfiguration) object;
            this.vehicleId = avc.getID();
            for (PayloadConfiguration pc : avc.getPayloadConfigurationList()) {
                if (pc instanceof CameraConfiguration) {
                    cameraConfigMap.put(pc.getPayloadID(), (CameraConfiguration) pc);
                }
            }
        }
        else if (object instanceof AirVehicleState) {
            AirVehicleState avs = (AirVehicleState) object;
            for (PayloadState ps : avs.getPayloadStateList()) {
                if (ps instanceof CameraState) {
                    CameraState cs = (CameraState) ps;
                    if (cs.getFootprint() != null) {
                        cameraStateMap.put(cs.getPayloadID(), cs);
                    }
                }
            }
            latestState = avs;
        }
        
    }


    // application-wide events
    @Override
    public void applicationEventOccurred(Object event) {
        if (event instanceof EntityState) {
            EntityState es = (EntityState) event;
            targetStateMap.put(es.getID(), es);
        }
    }

 
    void updateDetections(double secSimTime) {

        // if we aren't in track mode, then don't continue with checking
        //if (!parentModel.getData().trackOn.asBool()) {
        //    return;
        //}

        for (EntityState t : targetStateMap.values()) {
            

            Double timeLastSeen = timeLastSeenMap.get(t.getID());

            if (timeLastSeen != null) {
                // if the target has not been for more seconds than the max timeout, set the time in view to zero */
                if (secTimeout != 0 && secSimTime - timeLastSeen > secTimeout) {
                    timeLastSeenMap.remove(t.getID());
                    timeFirstSeenMap.remove(t.getID());
                }
            }

            for (Entry<Long, CameraState> cameraState : cameraStateMap.entrySet()) {
                
                Path2D cameraPoly = CmasiUtils.convertPoly(cameraState.getValue().getFootprint());
                boolean seen = cameraPoly.contains(t.getLocation().getLongitude(), t.getLocation().getLatitude());
                
                // compute the GSD at the point of the target to determine if we continue with detection calculation
                CameraConfiguration cc = cameraConfigMap.get(cameraState.getKey());
                if (cc == null) {
                    continue;
                }
                double gsd = computeGSD(latestState.getLocation(), t.getLocation(), cameraState.getValue(), cc);
                if (gsd > minGSD) {
                    continue;
                }

                Double timeFirstSeen = timeFirstSeenMap.get(t.getID());
                
                if (seen) {
                    if (timeFirstSeen == null) {
                        timeFirstSeen = simTime;
                        timeFirstSeenMap.put(t.getID(), simTime);
                    }
                    
                    double timeSeen = simTime - timeFirstSeen;
                    timeLastSeenMap.put(t.getID(), simTime); 

                    // if it has been seen for long enough, add it to the report
                    if (timeSeen >= minTimeInView) {
                        EntityPerception detection = createPerception(t, vehicleId, cameraState.getKey(), simTime);
                        if (eventManager != null) {
                            eventManager.fireEvent(detection);
                        }
                    }
                }
            }
        }
    }

    static EntityPerception createPerception(EntityState state, long vehicleId, long sensorId, double time) {

        EntityPerception perception = new EntityPerception();
        perception.setPerceivedEntityID(state.getID());
        perception.setPerceiverID(vehicleId);
        perception.setVelocityValid(false);
        perception.setLocation(state.getLocation().clone());
        perception.setTimeLastSeen( (long) (time * 1000) );
        perception.setAttitudeValid(false);

        return perception;
    }
    
    
    
    public static double computeGSD(Location3D sourceLoc, Location3D destLoc, CameraState cs, CameraConfiguration cc) {

        // distance from aircraft to point on ground
        double dist = CmasiNavUtils.distance(sourceLoc, destLoc);
        
        // make that a 3d distance
        dist = Math.hypot(dist, sourceLoc.getAltitude() - destLoc.getAltitude() );

        // angle created for one pixel (fov / num pixels)
        double alpha = Math.toRadians(cs.getHorizontalFieldOfView()) / cc.getVideoStreamHorizontalResolution();

        // approx distance that is covered by the pixel (horizontal)  This is the ground sample distance
        return dist * Math.sin(alpha);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */