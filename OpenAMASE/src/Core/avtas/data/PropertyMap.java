// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Maintains a map of Properties. The purpose is to allow faster access to
 * elements in a
 * <code>Hashtable</code>. Properties in the map can be modified by using the
 * <code>setValue()</code> method for each Property. This eliminates the need
 * for time intensive map operations.
 *
 * @author AFRL/RQQD
 */
public class PropertyMap {

    private Map<String, Property> map = new HashMap<>();

    public PropertyMap() {
    }

    /**
     * Puts an entry into the map and returns the Property reference. If an
     * entry with the given key already exists, the existing entry is modified
     * and a reference to it is returned.
     *
     * @param key The key (or name) for the Property.
     * @param value The value of the Property.
     *
     * @return The Property entry with the given key
     */
    public Property put(String key, Object value) {
        Property e = map.get(key);
        if (e == null) {
            e = new Property(key, value);
            map.put(key, e);
            return e;
        }
        else {
            e.setValue(value);
            return e;
        }
    }

    /**
     * Removes all items from the map and removes all registered listeners.
     */
    public void clear() {
        map.clear();
    }

    /**
     * Puts the entry into the map.
     *
     * @param key The key for the map entry.
     * @param entry The Property entry for the map.
     */
    protected void putEntry(String key, Property entry) {
        map.put(key, entry);
    }

    /**
     * Puts all entries of the passed map into this map. Overwrites any
     * exisiting entries with the same key.
     *
     * @param thatmap Map of entries to be copied into this map.
     */
    public void putAll(PropertyMap thatmap) {
        this.map.putAll(thatmap.map);
    }

    /**
     * Returns the Property entry with the given key. If none exists in the map,
     * one is inserted.
     *
     * @param key Name of the Property to be retrieved.
     *
     * @return The Property entry with the given key
     */
    public Property get(String key) {
        return get(key, 0);
    }

    /**
     * Returns the Property entry with the given key. If none exists in the map,
     * one is inserted with the default value specified.
     *
     * @param key Name of the Property to be retrieved.
     * @param value Initial value to put if there is no key in the map.
     *
     * @return The Property entry with the given key
     */
    public Property get(String key, Object value) {
        Property e = map.get(key);
        if (e == null) {
            e = put(key, value);
        }
        return e;
    }

    /**
     * Gets the antecedents of a Property. Properties can be specified in a
     * hierarchical manner. For example: ParentProperty/ChildProperty. This
     * method returns all parent Properties for the Property named
     * <code>key</code>.
     *
     * @param key Name of the Property which antecendents are requested.
     * @return An array of the parent Properties.
     */
    public Property[] getAntecedents(String key) {
        String[] splits = key.split("/");
        if (splits.length == 1) return new Property[]{};
        String tmp = "";
        Property[] ret = new Property[splits.length - 1];
        for (int i = 0; i < splits.length - 1; i++) {
            tmp += splits[i];
            ret[i] = get(tmp);
            tmp += "/";
        }
        //System.out.println(Arrays.toString(ret));
        return ret;
    }

    /**
     * Returns an array of Properties consisting of the Property named
     * <code>key</code> and all child Properties.
     *
     * @param key The name of the parent Property.
     * @return An array of Properties consisting of Property <code>key</code>
     * and it's children.
     */
    public Property[] getAll(String key) {
        List<Property> retList = new ArrayList<Property>();
        for (Map.Entry<String, Property> p : map.entrySet()) {
            if (p.getKey().startsWith(key)) {
                retList.add(p.getValue());
            }
        }
        Property[] retArray = retList.toArray(new Property[]{});
        //System.out.println(Arrays.toString(retArray));
        return retArray;
    }

    /**
     * Returns true if this map contains a property with the corresponding key.
     *
     * @param key The Property key.
     */
    public boolean hasEntry(String key) {
        return map.containsKey(key);
    }

    /**
     * Returns a submap of objects that have "key" as a prefix. The Properties
     * contain the same name, but the key has the prefix removed.
     *
     * @param key Parent key for the Property.
     *
     * @return A submap containing all Properties that start with "key"
     */
    public PropertyMap subset(String key) {
        PropertyMap newprops = new PropertyMap();
        for (String s : map.keySet()) {
            if (s.startsWith(key)) {
                newprops.putEntry(s.substring(key.length()), map.get(s));
            }
        }
        return newprops;
    }

    /**
     * Returns the entries of the map in a sorted form (alphabetical).
     *
     * @return Collection of entries sorted in alphabetical order.
     */
    public List<Property> sortedView() {
        List<Property> list = new ArrayList(map.values());
        Collections.sort(list, new Comparator<Property>() {
            @Override
            public int compare(Property o1, Property o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return list;
    }

    /**
     * Returns a string representation of the
     * <code>PropertyMap</code>. The representation is in the form:
     *
     * <p>key1 = value1</p>
     * <p>key2 = value2</p>
     * <p>key3 = value3</p>
     *
     * @return The String representing the <code>PropertyMap</code>.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        SortedMap<String, Property> sortedMap = new TreeMap<String, Property>(map);
        for (Map.Entry<String, Property> e : sortedMap.entrySet()) {
            buf.append(e.getKey());
            buf.append(" = ").append(e.getValue().getValue()).append("\n");
        }
        return buf.toString();
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */