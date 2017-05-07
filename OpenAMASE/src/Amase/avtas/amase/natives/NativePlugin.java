// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.natives;

import avtas.amase.AmasePlugin;
import avtas.app.Context;
import avtas.app.SettingsManager;
import avtas.app.UserExceptions;
import avtas.lmcp.LMCPFactory;
import avtas.lmcp.LMCPObject;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import avtas.xml.XmlWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads a native application plugin.  The library that contains the native plugin as well as the
 * name of the native plugin should be specified in the XML content that is passed through the
 * {@link #addedToApplication(avtas.app.Context, avtas.xml.Element, java.lang.String[])  } method.  
 * See the documentation on the native plugin system for more information.
 * 
 * @author AFRL/RQQD
 */
public class NativePlugin extends AmasePlugin {

    long entity_id = 0;
    long pluginPtr = 0;

    public NativePlugin() {

    }

    @Override
    public void step(double timestep_sec, double simtime_sec) {
        if (pluginPtr != -1) {
            nativeStep(pluginPtr, simtime_sec, timestep_sec);
        }
    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {

        try {
        String libName = XMLUtil.getValue(xml, "Library", "");
        if (!libName.isEmpty()) {
            System.loadLibrary(libName);
        }

        String pluginType = XMLUtil.getValue(xml, "PluginType", "");
        if (!pluginType.isEmpty()) {
            pluginPtr = createNativePlugin(pluginType, entity_id);
        }
        if (pluginPtr != -1) {
            nativeInitialize(pluginPtr, XmlWriter.toCompactString(xml), SettingsManager.getSettingsDirectory().toAbsolutePath().toString());
        }
        else {
            UserExceptions.showError(this, "Could not create native plugin " + pluginType, null);
        }
        } catch (Exception ex) {
            UserExceptions.showError(this, "Error creating native plugin", ex);
        }
    }

    
    @Override
    public void eventOccurred(Object object) {
        if (pluginPtr != -1) {
            if (object instanceof LMCPObject) {
                try {
                    byte[] bytes = LMCPFactory.packMessage((LMCPObject) object, false);
                    nativeAppEventOccured(pluginPtr, bytes);
                } catch (Exception ex) {
                    Logger.getLogger(NativePlugin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void shutdown() {
        if (pluginPtr != -1) {
            nativeShutdown(pluginPtr);
        }
    }


    void native_fire_app_event(byte[] bytes) {

        try {
            LMCPObject obj = LMCPFactory.getObject(bytes);
            if (obj != null) {
                fireEvent(obj);
            }
        } catch (Exception ex) {
            Logger.getLogger(NativePlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // native methods (C/C++ side) 
    
    protected native long createNativePlugin(String name, long entity_id);

    protected native void nativeStep(long modulePtr, double sim_time, double timestep_sec);

    protected native void nativeInitialize(long modulePtr, String xml, String config_dir);

    protected native void nativeAppEventOccured(long modulePtr, byte[] bytes);

    protected native void nativeShutdown(long modulePtr);
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */