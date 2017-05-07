// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 16, 2005
 */
package org.flexdock.docking.props;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;



import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingPort;
import org.flexdock.util.ClassMapping;
import org.flexdock.util.SwingUtility;

/**
 * @author Christopher Butler
 */
public class PropertyManager {

    public static final String DOCKABLE_PROPERTIES_KEY = DockablePropertySet.class.getName();
    public static final String DOCKINGPORT_PROPERTIES_KEY = DockingPortPropertySet.class.getName();
    private static final ClassMapping DOCKABLE_PROPS_MAPPING = new ClassMapping(ScopedDockablePropertySet.class, null);
    private static final HashMap DOCKABLE_CLIENT_PROPERTIES = new HashMap();

    public static DockingPortPropertySet getDockingPortRoot() {
        return ScopedDockingPortPropertySet.ROOT_PROPS;
    }

    public static DockablePropertySet getDockableRoot() {
        return ScopedDockablePropertySet.ROOT_PROPS;
    }

    public static void setDockablePropertyType(Class dockable, Class propType) {
        if(dockable==null || propType==null)
            return;

        if(!Dockable.class.isAssignableFrom(dockable) || !DockablePropertySet.class.isAssignableFrom(propType))
            return;

        DOCKABLE_PROPS_MAPPING.addClassMapping(dockable, propType);
    }

    public static DockablePropertySet getDockablePropertySet(Dockable dockable) {
        if(dockable==null)
            return null;

        Object obj = dockable.getClientProperty(DOCKABLE_PROPERTIES_KEY);
        if(!(obj instanceof DockablePropertySet)) {
            obj = createDockablePropertySet(dockable);
            linkPropertySet(dockable, (DockablePropertySet)obj);

        }
        return (DockablePropertySet)obj;
    }

    private static void linkPropertySet(Dockable dockable, DockablePropertySet propertySet) {
        dockable.putClientProperty(DOCKABLE_PROPERTIES_KEY, propertySet);
        PropertyChangeListener[] listeners = PropertyChangeListenerFactory.getListeners();
        for(int i=0; i<listeners.length; i++) {
            propertySet.addPropertyChangeListener(listeners[i]);
        }
    }

    public static void removePropertySet(Dockable dockable) {
        if(dockable!=null) {
            dockable.putClientProperty(DOCKABLE_PROPERTIES_KEY, null);
            synchronized(DOCKABLE_CLIENT_PROPERTIES) {
                DOCKABLE_CLIENT_PROPERTIES.remove(dockable.getPersistentId());
            }
        }
    }

    public static DockingPortPropertySet getDockingPortPropertySet(DockingPort port) {
        if(port==null)
            return null;

        Object obj = port.getClientProperty(DOCKINGPORT_PROPERTIES_KEY);
        if(!(obj instanceof DockingPortPropertySet)) {
            obj = new ScopedDockingPortPropertySet(4);
            port.putClientProperty(DOCKINGPORT_PROPERTIES_KEY, obj);
        }
        return (DockingPortPropertySet)obj;
    }

    public static Object getProperty(Object key, ScopedMap map) {
        if(key==null || map==null)
            return null;

        // first, check the global property list
        Object value = getProperty(key, map.getGlobals());
        // if not in the global list, check the locals
        if(value==null)
            value = getProperty(key, map.getLocals());
        // if not in the local list, check the defaults
        if(value==null)
            value = getProperty(key, map.getDefaults());
        // if not in the default list, check the root
        if(value==null)
            value = getProperty(key, map.getRoot());
        return value;
    }

    public static Object getClientProperty(Dockable dockable, Object key) {
        if(dockable==null || key==null)
            return null;

        Component comp = dockable.getComponent();
        if(comp instanceof JComponent) {
            return SwingUtility.getClientProperty(comp, key);
        }
        return getClientProperties(dockable).get(key);
    }

    public static void putClientProperty(Dockable dockable, Object key, Object value) {
        if(dockable==null || key==null)
            return;

        Component comp = dockable.getComponent();
        if(comp instanceof JComponent) {
            SwingUtility.putClientProperty(comp, key, value);
            return;
        }

        Hashtable table = getClientProperties(dockable);
        if(value==null) {
            table.remove(key);
        } else {
            table.put(key, value);
        }
    }

    private static Hashtable getClientProperties(Dockable dockable) {
        String dockableId = dockable.getPersistentId();
        synchronized(DOCKABLE_CLIENT_PROPERTIES) {
            Hashtable table = (Hashtable)DOCKABLE_CLIENT_PROPERTIES.get(dockableId);
            if(table==null) {
                table = new Hashtable(2);
                DOCKABLE_CLIENT_PROPERTIES.put(dockableId, table);
            }
            return table;
        }
    }

    private static DockablePropertySet createDockablePropertySet(Dockable d) {
        Class key = d.getClass();
        Class c = DOCKABLE_PROPS_MAPPING.getClassMapping(key);

        try {
            // get the constructor with the Dockable 'dockable' parameter
            Constructor[] constructors = c.getConstructors();
            for(int i=0; i<constructors.length; i++) {
                Class[] paramTypes = constructors[i].getParameterTypes();
                if(paramTypes.length!=1)
                    continue;

                Class param = paramTypes[0];
                if(Dockable.class.isAssignableFrom(param)) {
                    return (DockablePropertySet)constructors[i].newInstance(new Object[] {d});
                }

            }
            return null;
        } catch(Exception e) {
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static Object getProperty(Object key, Object map) {
        if(map instanceof Map) {
            return ((Map)map).get(key);
        }
        return null;
    }

    private static Object getProperty(Object key, List maps) {
        if(maps==null)
            return null;

        for(Iterator it=maps.iterator(); it.hasNext();) {
            Object map = it.next();
            Object value = getProperty(key, map);
            if(value!=null)
                return value;
        }
        return null;
    }

}
