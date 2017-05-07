// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.natives;

import avtas.amase.entity.EntityModule;
import avtas.lmcp.LMCPFactory;
import avtas.lmcp.LMCPObject;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import avtas.xml.XmlWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads a native entity module.  The library that contains the native module as well as the
 * name of the native module should be specified in the XML content that is passed through the
 * {@link #initialize(avtas.xml.Element) } method.  See the documentation on the native module
 * system for more information.
 * 
 * @author AFRL/RQQD
 */
public class NativeModule extends EntityModule {

    long entity_id = 0;
    long modulePtr = 0;

    public NativeModule() {

    }

    @Override
    public void step(double timestep_sec, double simtime_sec) {
        if (modulePtr != -1) {
            nativeStep(modulePtr, entity_id, simtime_sec, timestep_sec);
        }
    }

    @Override
    public void initialize(Element xmlElement) {
        entity_id = getModel().getID();

        String libName = XMLUtil.getValue(xmlElement, "Library", "");
        if (!libName.isEmpty()) {
            System.loadLibrary(libName);
        }

        String moduleType = XMLUtil.getValue(xmlElement, "ModuleType", "");
        if (!moduleType.isEmpty()) {
            modulePtr = createNativeModule(moduleType, entity_id);
        }
        if (modulePtr != -1) {
            nativeInitialize(modulePtr, entity_id, XmlWriter.toCompactString(xmlElement));
        }
    }

    @Override
    public void modelEventOccurred(Object object) {
        if (modulePtr != -1) {
            if (object instanceof LMCPObject) {
                try {
                    byte[] bytes = LMCPFactory.packMessage((LMCPObject) object, false);
                    nativeModelEventOccured(modulePtr, entity_id, bytes);
                } catch (Exception ex) {
                    Logger.getLogger(NativeModule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    @Override
    public void applicationEventOccurred(Object object) {
        if (modulePtr != -1) {
            if (object instanceof LMCPObject) {
                try {
                    byte[] bytes = LMCPFactory.packMessage((LMCPObject) object, false);
                    nativeAppEventOccured(modulePtr, entity_id, bytes);
                } catch (Exception ex) {
                    Logger.getLogger(NativeModule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void shutdown() {
        if (modulePtr != -1) {
            nativeShutdown(modulePtr, entity_id);
        }
    }

    // methods called from the native side //
    void native_fire_model_event(byte[] bytes) {

        try {
            LMCPObject obj = LMCPFactory.getObject(bytes);
            if (obj != null) {
                fireModelEvent(obj);
            }
        } catch (Exception ex) {
            Logger.getLogger(NativeModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void native_fire_app_event(byte[] bytes) {

        try {
            LMCPObject obj = LMCPFactory.getObject(bytes);
            if (obj != null) {
                fireApplicationEvent(obj);
            }
        } catch (Exception ex) {
            Logger.getLogger(NativeModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // native methods (C/C++ side) 
    
    protected native long createNativeModule(String name, long entity_id);

    protected native void nativeStep(long modulePtr, long entity_id, double sim_time, double timestep_sec);

    protected native void nativeInitialize(long modulePtr, long entity_id, String str);

    protected native void nativeModelEventOccured(long modulePtr, long entity_id, byte[] bytes);

    protected native void nativeAppEventOccured(long modulePtr, long entity_id, byte[] bytes);

    protected native void nativeShutdown(long modulePtr, long entity_id);
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */