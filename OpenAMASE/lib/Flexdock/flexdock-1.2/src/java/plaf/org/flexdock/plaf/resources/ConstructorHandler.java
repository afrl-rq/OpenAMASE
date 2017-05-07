// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 1, 2005
 */
package org.flexdock.plaf.resources;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.StringTokenizer;




/**
 * @author Christopher Butler
 */
public class ConstructorHandler extends ResourceHandler {

    private Constructor constructor;

    public ConstructorHandler(Constructor constructor) {
        this.constructor = constructor;
    }

    public Object getResource(String stringValue) {
        Object[] arguments = getArguments(stringValue);
        try {
            return constructor.newInstance(arguments);
        } catch(Exception e) {
            System.err.println("Exception: " +e.getMessage());
            return null;
        }
    }

    private Object[] getArguments(String data) {
        String[] supplied = parseArguments(data);
        Object[] arguments = new Object[supplied.length];
        Class[] paramTypes = constructor.getParameterTypes();
        if(arguments.length!=paramTypes.length)
            throw new IllegalArgumentException("Cannot match '" + data + "' to constructor " + constructor + ".");

        for(int i=0; i<paramTypes.length; i++) {
            arguments[i] = toObject(supplied[i], paramTypes[i]);
        }
        return arguments;
    }

    private Object toObject(String data, Class type) {
        if(type==int.class)
            return new Integer(data);
        if(type==long.class)
            return new Long(data);
        if(type==boolean.class)
            return new Boolean(data);
        if(type==float.class)
            return new Float(data);
        if(type==double.class)
            return new Double(data);
        if(type==byte.class)
            return new Byte(data);
        if(type==short.class)
            return new Short(data);

        return data;
    }

    private String[] parseArguments(String data) {
        if(!data.endsWith(","))
            data += ",";

        ArrayList args = new ArrayList();
        StringTokenizer st = new StringTokenizer(data, ",");
        while(st.hasMoreTokens()) {
            args.add(st.nextToken().trim());
        }
        return (String[])args.toArray(new String[0]);
    }
}
