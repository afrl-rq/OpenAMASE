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
import afrl.cmasi.GimbalConfiguration;
import afrl.cmasi.PayloadConfiguration;
import avtas.amase.entity.EntityModule;
import java.util.HashMap;
import avtas.xml.Element;

/**
 * Manages the creation of a {@link GimbalControl} object for each gimbal
 * present on the aircraft.
 *
 * @author AFRL/RQQD
 */
public class GimbalManager extends EntityModule {

    HashMap<Long, GimbalControl> gimbalMap = new HashMap<Long, GimbalControl>();
    Element xmlElement = null;

    public GimbalManager() {
    }

    @Override
    public void step(double timestep_sec, double simtime_sec) {
        for (GimbalControl gc : gimbalMap.values()) {
            gc.step(timestep_sec, simtime_sec);
        }
    }

    @Override
    public void initialize(Element xmlElement) {
        this.xmlElement = xmlElement;
    }

    @Override
    public void modelEventOccurred(Object object) {
        if (object instanceof AirVehicleConfiguration) {
            gimbalMap.clear();
            for (PayloadConfiguration pc : ((AirVehicleConfiguration) object).getPayloadConfigurationList()) {
                if (pc instanceof GimbalConfiguration) {
                    GimbalControl gc = new GimbalControl((GimbalConfiguration) pc);
                    gc.initialize(getModel(), xmlElement);
                    gimbalMap.put(pc.getPayloadID(), gc);
                }
            }
        }
        for (GimbalControl gc : gimbalMap.values()) {
            gc.modelEventOccurred(object);
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */