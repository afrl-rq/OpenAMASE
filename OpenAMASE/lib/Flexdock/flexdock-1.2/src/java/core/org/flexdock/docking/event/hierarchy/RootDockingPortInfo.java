// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 28, 2005
 */
package org.flexdock.docking.event.hierarchy;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.flexdock.docking.DockingPort;
import org.flexdock.util.RootWindow;

/**
 * @author Christopher Butler
 */
public class RootDockingPortInfo {
    private WeakReference windowRef;
    private ArrayList rootPorts;
    private HashMap portsById;
    private String mainPortId;

    public RootDockingPortInfo(RootWindow window) {
        windowRef = new WeakReference(window);
        rootPorts = new ArrayList(2);
        portsById = new HashMap(2);
    }

    public RootWindow getWindow() {
        return (RootWindow)windowRef.get();
    }

    private boolean containsPortId(DockingPort port) {
        return port==null? false: contains(port.getPersistentId());
    }

    public boolean contains(String portId) {
        return portId==null? false: portsById.containsKey(portId);
    }

    public boolean contains(DockingPort port) {
        return port==null? false: portsById.containsValue(port);
    }

    public synchronized void add(DockingPort port) {
        if(containsPortId(port))
            return;

        portsById.put(port.getPersistentId(), port);
        rootPorts.add(port);
    }

    public synchronized void remove(DockingPort port) {
        if(port==null)
            return;

        String key = port.getPersistentId();
        if(!contains(key)) {
            key = null;
            for(Iterator it=portsById.keySet().iterator(); it.hasNext();) {
                String tmpKey = (String)it.next();
                DockingPort tmp = (DockingPort)portsById.get(tmpKey);
                if(tmp==port) {
                    key = tmpKey;
                    break;
                }
            }
        }

        if(key!=null)
            portsById.remove(key);
        rootPorts.remove(port);
    }

    public int getPortCount() {
        return rootPorts.size();
    }

    public DockingPort getPort(int indx) {
        return indx<getPortCount()? (DockingPort)rootPorts.get(indx): null;
    }

    public DockingPort getPort(String portId) {
        return (DockingPort)portsById.get(portId);
    }

    public void setMainPort(String portId) {
        mainPortId = portId;
    }

    public DockingPort getMainPort() {
        DockingPort port = mainPortId==null? null: getPort(mainPortId);
        if(port==null) {
            port = getPortCount()>0? getPort(0): null;
        }
        return port;

    }


}
