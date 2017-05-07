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
import java.util.Hashtable;

import org.flexdock.util.ResourceManager;
import org.w3c.dom.Document;

/**
 * A factory for transforming {@code Component}s into a class that contains
 * information about the component that is necessary for treating the component
 * as a {@code Dockable}.
 *
 * @author Christopher Butler
 * @author Karl Schaefer
 */
public class AdapterFactory {

    /**
     * A property key for determining whichi docking adapter to use.
     */
    public static final String ADAPTER_RESOURCE_KEY = "flexdock.adapters";

    /**
     * A constant representing the relative URI to the default docking adapter.
     *
     * @deprecated Scope is likely to become more restrictive (protected or
     *             private).
     */
    public static final String DEFAULT_ADAPTER_RESOURCE = "flexdock-adapters.xml";

    private static final Hashtable MAPPINGS_BY_CLASS = new Hashtable();

    /**
     * Loads the mappings for this factory.
     */
    public static void prime() {
        loadMappings();
    }

    /**
     * Creates the {@code DockingAdapter} for the given {@code Component}.
     *
     * @param comp
     *            the component to create an adapter for.
     * @return a docking adapter, or {@code null} if {@code comp} is null.
     */
    public static DockingAdapter getAdapter(Component comp) {
        if (comp == null)
            return null;

        AdapterMapping mapping = getMapping(comp);
        if (mapping == null)
            return null;

        DockingAdapter adapter = new DockingAdapter(comp, mapping);
        // validate the adapter before returning
        if (adapter.getComponent() == null || adapter.getPersistentId() == null)
            return null;

        return adapter;
    }

    private static AdapterMapping getMapping(Object obj) {
        String className = obj.getClass().getName();
        return (AdapterMapping) MAPPINGS_BY_CLASS.get(className);
    }

    private static void loadMappings() {
        String uri = System.getProperty(ADAPTER_RESOURCE_KEY);

        Document document = ResourceManager.getDocument(uri);

        // load the default if the Resource Manager failed to load the requested
        if (document == null) {
            document = ResourceManager.getDocument(DEFAULT_ADAPTER_RESOURCE);
        }

        if (document == null)
            //TODO this should probably throw a runtime exception
            return;

        MappingReader reader = new MappingReader();
        AdapterMapping[] mappings = reader.readMappings(document);
        for (int i = 0; i < mappings.length; i++) {
            MAPPINGS_BY_CLASS.put(mappings[i].getClassName(), mappings[i]);
        }
    }
}
