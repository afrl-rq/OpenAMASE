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
import afrl.cmasi.CameraConfiguration;
import afrl.cmasi.GimbalConfiguration;
import afrl.cmasi.PayloadConfiguration;
import avtas.amase.entity.EntityModule;
import avtas.amase.util.CmasiUtils;
import java.util.HashMap;
import avtas.xml.Element;

/**
 * Manages the creation of a {@link CameraControl} object for each camera
 * present on the aircraft.
 *
 * @author AFRL/RQQD
 */
public class CameraManager extends EntityModule {

    HashMap<Long, CameraControl> cameraMap = new HashMap<>();
    Element xmlElement = null;

    public CameraManager() {
    }

    @Override
    public void step(double timestep_sec, double simtime_sec) {
        for (CameraControl cc : cameraMap.values()) {
            cc.step(timestep_sec, simtime_sec);
        }
    }

    @Override
    public void initialize(Element xmlElement) {
        this.xmlElement = xmlElement;
    }

    @Override
    public void modelEventOccurred(Object object) {
        if (object instanceof AirVehicleConfiguration) {
            cameraMap.clear();
            AirVehicleConfiguration avc = (AirVehicleConfiguration) object;
            for (PayloadConfiguration pc : avc.getPayloadConfigurationList()) {
                if (pc instanceof CameraConfiguration) {
                    CameraConfiguration cc = (CameraConfiguration) pc;
                    GimbalConfiguration gc = CmasiUtils.getGimbalForPayload(cc.getPayloadID(), avc);
                    CameraControl camControl = new CameraControl(cc, gc);
                    
                    camControl.initialize( getModel(), xmlElement);
                    cameraMap.put(cc.getPayloadID(), camControl);
                }
            }
        }
        for (CameraControl cc : cameraMap.values()) {
            cc.modelEventOccurred(object);
        }
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */