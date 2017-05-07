// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Feb 28, 2005
 */
package org.flexdock.plaf.mappings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;



import org.flexdock.plaf.Configurator;
import org.flexdock.plaf.XMLConstants;
import org.w3c.dom.Element;

/**
 * @author Christopher Butler
 */
public class PlafMappingFactory implements XMLConstants {

    public static final String PLAF_KEY = "plaf";
    private static final HashMap PLAF_MAPPINGS = loadPlafMappings();

    public static List getAvailablePlafNames() {
        return new ArrayList(PLAF_MAPPINGS.keySet());
    }

    public static String getInstalledPlafReference() {
        LookAndFeel currentPlaf = UIManager.getLookAndFeel();
        if(currentPlaf==null)
            return null;

        String key = currentPlaf.getClass().getName();
        return getPlafReference(key);
    }

    public static String getPlafReference(String key) {
        if(key==null)
            return null;

        Object value = PLAF_MAPPINGS.get(key);
        if(value instanceof String)
            return (String)value;

        // if not a String, then we must have a RefResolver
        if(value instanceof RefResolver) {
            RefResolver resolver = (RefResolver)value;
            return resolver.getRef(key);
        }
        return null;
    }

    private static HashMap loadPlafMappings() {
        HashMap elements = Configurator.getNamedElementsByTagName(PLAF_KEY);
        HashMap mappings = new HashMap(elements.size());

        for(Iterator it=elements.keySet().iterator(); it.hasNext();) {
            String key = (String)it.next();
            Element elem = (Element)elements.get(key);

            String name = elem.getAttribute(NAME_KEY);
            String ref = elem.getAttribute(REFERENCE_KEY);
            String resolver = elem.getAttribute(HANDLER_KEY);
            Object value = createPlafMapping(ref, resolver);
            mappings.put(name, value);
        }
        return mappings;
    }


    private static Object createPlafMapping(String refName, String resolverName) {
        if(Configurator.isNull(resolverName))
            return refName;

        RefResolver resolver = null;
        try {
            Class clazz = Class.forName(resolverName);
            // must be a type of PlafBasedViewResolver
            resolver = (RefResolver)clazz.newInstance();
        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
            return refName;
        }

        // setup the default value on the resolver and return
        resolver.setDefaultRef(refName);
        return resolver;
    }
}
