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
package org.flexdock.plaf.resources;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



import org.flexdock.plaf.Configurator;
import org.flexdock.plaf.PropertySet;
import org.flexdock.plaf.XMLConstants;
import org.w3c.dom.Element;

/**
 * @author Christopher Butler
 */
public class ResourceHandlerFactory implements XMLConstants {

    private static final HashMap RESOURCE_HANDLERS = loadResourceHandlers();
    private static final HashMap PROPERTY_HANDLERS = loadPropertyHandlers();

    public static ResourceHandler getResourceHandler(String handlerName) {
        return (ResourceHandler)RESOURCE_HANDLERS.get(handlerName);
    }

    public static String getPropertyHandler(String propertyType) {
        return (String)PROPERTY_HANDLERS.get(propertyType);
    }

    private static HashMap loadResourceHandlers() {
        HashMap elements = Configurator.getNamedElementsByTagName(HANDLER_KEY);
        HashMap handlers = new HashMap(elements.size());

        for(Iterator it=elements.keySet().iterator(); it.hasNext();) {
            String key = (String)it.next();
            Element elem = (Element)elements.get(key);

            String name = elem.getAttribute(NAME_KEY);
            String className = elem.getAttribute(VALUE_KEY);
            ResourceHandler handler = createResourceHandler(className);
            if(handler!=null)
                handlers.put(name, handler);
        }
        // add constructor handlers to the set
        HashMap constructors = loadConstructors();
        handlers.putAll(constructors);

        return handlers;
    }

    private static ResourceHandler createResourceHandler(String className) {
        if(Configurator.isNull(className))
            return null;

        try {
            Class clazz = Class.forName(className);
            return (ResourceHandler)clazz.newInstance();
        } catch(Exception e) {
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static HashMap loadPropertyHandlers() {
        HashMap elements = Configurator.getNamedElementsByTagName(PROP_HANDLER_KEY);
        HashMap propHandlers = new HashMap(elements.size());

        for(Iterator it=elements.keySet().iterator(); it.hasNext();) {
            String key = (String)it.next();
            Element elem = (Element)elements.get(key);

            String tagName = elem.getAttribute(NAME_KEY);
            String handlerName = elem.getAttribute(VALUE_KEY);
            if(!Configurator.isNull(tagName) && !Configurator.isNull(handlerName))
                propHandlers.put(tagName, handlerName);
        }
        return propHandlers;
    }

    private static HashMap loadConstructors() {
        PropertySet[] constructors = Configurator.getProperties(CONSTRUCTOR_KEY);
        HashMap map = new HashMap(constructors.length);

        for(int i=0; i<constructors.length; i++) {
            ConstructorHandler handler = createConstructorHandler(constructors[i]);
            if(handler!=null) {
                map.put(constructors[i].getName(), handler);
            }
        }
        return map;
    }

    private static ConstructorHandler createConstructorHandler(PropertySet props) {
        String className = props.getString(CLASSNAME_KEY);
        if(Configurator.isNull(className))
            return null;

        try {
            List argKeys = props.getNumericKeys(true);
            ArrayList params = new ArrayList(argKeys.size());
            for(Iterator it=argKeys.iterator(); it.hasNext();) {
                String key = (String)it.next();
                Class paramType = props.toClass(key);
                if(!paramType.isPrimitive() && paramType!=String.class)
                    throw new IllegalArgumentException("ConstructorHandler can only parse primitive and String arguments: " + paramType);
                params.add(paramType);
            }

            Class type = Class.forName(className);
            Class[] paramTypes = (Class[])params.toArray(new Class[0]);
            Constructor constructor = type.getConstructor(paramTypes);

            return new ConstructorHandler(constructor);

        } catch(Exception e) {
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
