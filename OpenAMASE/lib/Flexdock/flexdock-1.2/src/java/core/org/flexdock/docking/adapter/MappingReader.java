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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;



import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Christopher Butler
 * @deprecated This class will become package-private.
 */
public class MappingReader {

    public static final String ADAPTER_KEY = "adapter";

    public static final String CLASS_KEY = "class";

    public static final String METHOD_KEY = "method";

    public static final String FLEXDOCK_KEY = "flexdock";

    public static final String CLIENT_KEY = "client";

    public AdapterMapping[] readMappings(Document mappingDoc) {
        if (mappingDoc == null)
            return new AdapterMapping[0];

        NodeList nodes = mappingDoc.getElementsByTagName(ADAPTER_KEY);
        ArrayList mappings = new ArrayList(nodes.getLength());

        for (int i = 0; i < nodes.getLength(); i++) {
            Element elem = (Element) nodes.item(i);
            String className = elem.getAttribute(CLASS_KEY);
            if (className != null) {
                AdapterMapping mapping = createMapping(elem);
                mapping.setClassName(className);
                mappings.add(mapping);
            }
        }

        return (AdapterMapping[]) mappings.toArray(new AdapterMapping[0]);
    }

    private AdapterMapping createMapping(Element adapterElem) {
        NodeList nodes = adapterElem.getElementsByTagName(METHOD_KEY);
        String className = adapterElem.getAttribute(CLASS_KEY);
        Properties p = new Properties();

        for (int i = 0, len = nodes.getLength(); i < len; i++) {
            Element elem = (Element) nodes.item(i);
            String key = elem.getAttribute(FLEXDOCK_KEY);
            String value = elem.getAttribute(CLIENT_KEY);
            if (key != null && value != null)
                p.setProperty(key, value);
        }

        return createMapping(p);
    }

    private AdapterMapping createMapping(Properties props) {
        Method[] setters = AdapterMapping.class.getMethods();
        AdapterMapping mapping = new AdapterMapping();

        for (int i = 0; i < setters.length; i++) {
            String methodName = setters[i].getName();
            if (!methodName.startsWith("set"))
                continue;

            String key = Character.toLowerCase(methodName.charAt(3))
                         + methodName.substring(4);
            String clientMethod = props.getProperty(key);
            if (clientMethod == null)
                continue;

            Method setter = setters[i];
            try {
                setter.invoke(mapping, new Object[] { clientMethod });
            } catch (Exception e) {
                System.err.println("Exception: " +e.getMessage());
            }
        }

        return mapping;
    }
}
