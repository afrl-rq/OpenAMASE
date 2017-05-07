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

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.border.Border;




/**
 * @author Christopher Butler
 */
public class PropertySet {

    private HashMap properties;
    private String name;

    public PropertySet() {
        properties = new HashMap();
    }

    public PropertySet(int size) {
        properties = new HashMap(size);
    }

    public void setAll(PropertySet set) {
        if(set!=null)
            properties.putAll(set.properties);
    }

    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public Color getColor(String key) {
        Object property = getProperty(key);
        return property instanceof Color? (Color)property: null;
    }

    public Font getFont(String key) {
        Object property = getProperty(key);
        return property instanceof Font? (Font)property: null;
    }

    public Image getImage(String key) {
        Object property = getProperty(key);
        return property instanceof Image? (Image)property: null;
    }

    public Icon getIcon(String key) {
        Object property = getProperty(key);
        return property instanceof Icon? (Icon)property: null;
    }

    public Action getAction(String key) {
        Object property = getProperty(key);
        return property instanceof Action? (Action)property: null;
    }

    public String getString(String key) {
        Object property = getProperty(key);
        return property instanceof String? (String)property: null;
    }

    public Border getBorder(String key) {
        Object property = getProperty(key);
        return property instanceof Border? (Border)property: null;
    }

    public String[] getStrings(String[] keys) {
        if(keys==null)
            return null;

        String[] values = new String[keys.length];
        for(int i=0; i<values.length; i++)
            values[i] = getString(keys[i]);
        return values;
    }

    public int getInt(String key) {
        String string = getString(key);
        if(string==null)
            return 0;

        try {
            return Integer.parseInt(string);
        } catch(NumberFormatException e) {
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public Integer getInteger(String key) {
        String string = getString(key);
        if(string==null)
            return null;

        try {
            return new Integer(string);
        } catch(NumberFormatException e) {
            return null;
        }
    }

    public boolean getBoolean( String key) {
        String string = getString(key);
        if(string==null)
            return false;

        try {
            return Boolean.valueOf( string).booleanValue();
        } catch(NumberFormatException e) {
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Iterator keys() {
        return properties.keySet().iterator();
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    public int size() {
        return properties.size();
    }

    public List getNumericKeys() {
        return getNumericKeys(false);
    }

    public List getNumericKeys(boolean sort) {
        ArrayList list = new ArrayList(size());
        for(Iterator it=properties.keySet().iterator(); it.hasNext();) {
            String key = (String)it.next();
            if(isNumeric(key)) {
                list.add(key);
            }
        }

        if(sort) {
            Collections.sort(list, new NumericStringSort());
        }

        return list;
    }

    private boolean isNumeric(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public Class toClass(String key) throws ClassNotFoundException {
        String type = getString(key);
        return resolveClass(type);
    }

    protected Class resolveClass(String className) throws ClassNotFoundException {
        if(className==null)
            return null;

        if("int".equals(className))
            return int.class;
        if("long".equals(className))
            return long.class;
        if("boolean".equals(className))
            return boolean.class;
        if("float".equals(className))
            return float.class;
        if("double".equals(className))
            return double.class;
        if("byte".equals(className))
            return byte.class;
        if("short".equals(className))
            return short.class;

        return Class.forName(className);
    }

    public String toString() {
        return "PropertySet[name=\"" + name + "\"; hashmap=" + properties + "]";
    }

    private static class NumericStringSort implements Comparator {

        public int compare(Object o1, Object o2) {
            int i1 = Integer.parseInt((String)o1);
            int i2 = Integer.parseInt((String)o2);
            return i1-i2;
        }
    }
}
