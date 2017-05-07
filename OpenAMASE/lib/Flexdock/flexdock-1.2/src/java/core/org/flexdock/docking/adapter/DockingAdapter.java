// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Jun 24, 2005
 */
package org.flexdock.docking.adapter;

import java.awt.Component;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;

/**
 * @author Christopher Butler
 */
//TODO shouldn't this implement Dockable?
public class DockingAdapter {
    private static final Class[] EMPTY_PARAMS = {};
    private static final Object[] EMPTY_ARGS = {};

    private Component component;
    private AdapterMapping mapping;

    DockingAdapter(Component comp, AdapterMapping mapping) {
        component = comp;
        this.mapping = mapping;
    }

    public Component getComponent() {
        return component;
    }

    public List getDragSources() {
        // first, try to get a list of drag sources
        Object obj = get(component, mapping.getDragSourceList());
        if(obj instanceof List)
            return (List)obj;

        // if we couldn't find a list, then try to get an individual drag source
        // and create a List from it
        obj = get(component, mapping.getDragSource());
        if(obj instanceof Component) {
            ArrayList list = new ArrayList(1);
            list.add(obj);
            return list;
        }

        // if both attempts failed, then return null
        return null;
    }

    public Set getFrameDragSources() {
        // first, try to get a set of frame drag sources
        Object obj = get(component, mapping.getFrameDragSourceList());
        if(obj instanceof Set)
            return (Set)obj;

        // if we couldn't find a set, then try to get an individual
        // frame drag source and create a Set from it
        obj = get(component, mapping.getFrameDragSource());
        if(obj instanceof Component) {
            HashSet set = new HashSet(1);
            set.add(obj);
            return set;
        }

        // if both attempts failed, then return null
        return null;
    }

    public String getPersistentId() {
        Object obj = get(component, mapping.getPersistentId());
        return obj instanceof String? (String)obj: null;
    }

    public Icon getDockbarIcon() {
        Object obj = get(component, mapping.getDockbarIcon());
        return obj instanceof Icon? (Icon)obj: null;
    }

    public String getTabText() {
        Object obj = get(component, mapping.getTabText());
        return obj instanceof String? (String)obj: null;
    }

    private Object get(Object obj, String methodName) {
        if(obj==null || methodName==null)
            return null;

        try {
            Class c = obj.getClass();
            Method method = c.getMethod(methodName, EMPTY_PARAMS);
            return method.invoke(obj, EMPTY_ARGS);
        } catch(Throwable t) {
            // ignore.
            return null;
        }
    }
}
