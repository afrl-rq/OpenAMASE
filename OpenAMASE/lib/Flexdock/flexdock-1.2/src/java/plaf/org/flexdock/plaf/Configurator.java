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
package org.flexdock.plaf;

import java.util.HashMap;

import org.flexdock.plaf.resources.ResourceHandler;
import org.flexdock.plaf.resources.ResourceHandlerFactory;
import org.flexdock.util.ResourceManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Christopher Butler
 */
public final class Configurator implements XMLConstants {
    public static final String DEFAULT_PREFS_URI = "org/flexdock/plaf/flexdock-themes-default.xml";
    public static final String PREFS_URI = "flexdock-themes.xml";

    private Configurator() {
        //does nothing
    }

    public static Document loadUserPrefs() {
        return ResourceManager.getDocument(PREFS_URI);
    }

    public static Document loadDefaultPrefs() {
        return ResourceManager.getDocument(DEFAULT_PREFS_URI);
    }

    public static HashMap getNamedElementsByTagName(String tagName) {
        if(isNull(tagName))
            return null;

        HashMap cache = new HashMap(256);
        // load defaults
        Document defaults = Configurator.loadDefaultPrefs();
        loadNamedElementsByTagName(defaults, tagName, cache);
        // overwrite/add with user prefs
        Document user = Configurator.loadUserPrefs();
        loadNamedElementsByTagName(user, tagName, cache);

        return cache;
    }

    private static void loadNamedElementsByTagName(Document document, String tagName, HashMap cache) {
        if(document==null)
            return;

        NodeList elements = document.getElementsByTagName(tagName);

        for(int i=0; i<elements.getLength(); i++) {
            Element elem = (Element)elements.item(i);
            String key = elem.getAttribute(NAME_KEY);
            boolean inherit = "true".equals(elem.getAttribute(INHERITS_KEY));
            if(!isNull(key)) {

                if(inherit) {
                    // mark as overridden, so we don't overwrite it in the cache
                    Element oldValue = (Element)cache.get(key);
                    if(oldValue!=null) {
                        cache.put(OVERRIDDEN_KEY + key, oldValue);
                    }
                }
                cache.put(key, elem);
            }
        }
    }

    public static PropertySet[] getProperties(String tagName) {
        HashMap map = getNamedElementsByTagName(tagName);
        if(map==null)
            return new PropertySet[0];

        String[] names = (String[])map.keySet().toArray(new String[0]);
        return getProperties(names, map);
    }

    public static PropertySet getProperties(String name, String tagName) {
        HashMap map = getNamedElementsByTagName(tagName);
        if(map==null)
            return null;
        return getProperties(name, map);
    }

    public static PropertySet[] getProperties(String[] names, String tagName) {
        HashMap map = names==null? null: getNamedElementsByTagName(tagName);
        if(map==null)
            return new PropertySet[0];
        return getProperties(names, map);
    }

    public static PropertySet[] getProperties(String[] names, HashMap cache) {
        PropertySet[] properties = new PropertySet[names.length];
        for(int i=0; i<names.length; i++) {
            properties[i] = getProperties(names[i], cache);
        }
        return properties;
    }

    private static PropertySet getProperties(String elemName, HashMap cache) {
        Element elem = isNull(elemName)? null: (Element)cache.get(elemName);
        if(elem==null)
            return null;

        PropertySet set = new PropertySet();
        set.setName(elemName);

        // load all the parent properties first, so we can add/overwrite our own later
        String parentName = elem.getAttribute(EXTENDS_KEY);
        PropertySet parent = isNull(parentName)? null: getProperties(parentName, cache);
        if(parent!=null)
            set.setAll(parent);

        // check to see if we're supposed to inherit from an overridden element
        if("true".equalsIgnoreCase(elem.getAttribute(INHERITS_KEY))) {
            PropertySet overridden = getProperties(OVERRIDDEN_KEY + elemName, cache);
            if(overridden!=null)
                set.setAll(overridden);
        }

        // get the default handler name
        String propertyHandlerName = getPropertyHandlerName(elem);

        NodeList list = elem.getElementsByTagName(PROPERTY_KEY);
        int len = list.getLength();
        for(int i=0; i<len; i++) {
            elem = (Element)list.item(i);
            String key = elem.getAttribute(NAME_KEY);
            if(!isNull(key)) {
                String value = elem.getAttribute(VALUE_KEY);
                String handler = elem.getAttribute(HANDLER_KEY);
                Object resource = getResource(value, handler, propertyHandlerName);
                if(resource!=null) {
                    set.setProperty(key, resource);
                }
            }
        }
        return set;
    }

    private static String getPropertyHandlerName(Element elem) {
        String handlerName = elem.getAttribute(PROP_HANDLER_KEY);
        if(isNull(handlerName))
            handlerName = ResourceHandlerFactory.getPropertyHandler(elem.getTagName());
        return isNull(handlerName)? null: handlerName;
    }

    public static Object getResource(String stringValue, String currentHandlerName, String defaultHandlerName) {
        String handlerName = isNull(currentHandlerName)? defaultHandlerName: currentHandlerName;
        if(isNull(handlerName))
            return nullify(stringValue);

        ResourceHandler handler = ResourceHandlerFactory.getResourceHandler(handlerName);
        return handler==null? nullify(stringValue): handler.getResource(stringValue);
    }

    private static String nullify(String data) {
        return isNull(data)? null: data;
    }


    public static boolean isNull(String data) {
        return data == null || data.trim().length() == 0;
    }
}
