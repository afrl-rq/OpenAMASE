// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This class contains a set of convenience functions for working with Nodes
 * associated with a DOM Tree.
 *
 * org.w3c.dom.Element types are loosely referred to as "nodes" in this class.
 *
 * @author AFRL/RQQD
 */
public class XMLUtil {

    /**
     * Returns the first child node with the given name, or null if none is
     * found. To find a nested node, separate names with the "/" character. The
     * passed string can contain regex expressions for pattern matching.
     *
     * @param node Node to be searched.
     * @return Child node with the given name or null if no child is found.
     */
    public static Element getChild(Element node, String regexChildName) {
        String[] childNames = regexChildName.split("/", 2);
        if (!node.hasChildren()) {
            return null;
        }
        List list = node.getChildren();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof Element) {
                Element el = (Element) list.get(i);
                if (Pattern.matches(childNames[0], el.getName())) {
                    if (childNames.length == 1) {
                        return el;
                    } else {
                        return getChild(el, childNames[1]);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns an array containing all children encountered with specified name,
     * or null if none are found. to find nesteded children, specify the
     * childname as path separated by "/". The passed string can contain regex
     * expressions for pattern matching.
     *
     * @param node The node to be searched.
     * @param regexChildName The child name to search for, or a regex.
     *
     * @return The list of child elements or an empty <code>ArrayList</code> if
     * it is not found.
     */
    public static List<Element> getChildren(Element node, String regexChildName) {

        if (node == null || !node.hasChildren()) {
            return new ArrayList<Element>();
        }

        ArrayList<Element> list = new ArrayList<Element>();
        String[] childNames = regexChildName.split("/", 2);

        List nodelist = node.getChildren();

        for (int i = 0; i < nodelist.size(); i++) {
            if (nodelist.get(i) instanceof Element) {
                Element el = (Element) nodelist.get(i);
                if (Pattern.matches(childNames[0], el.getName())) {
                    if (childNames.length == 1) {
                        list.add(el);
                    } else {
                        list.addAll(getChildren(el, childNames[1]));
                    }
                }
            }
        }
        return list;
    }

    /**
     * Returns an XML text child as an integer.
     *
     * @param node Parent element node.
     * @param childName Child to look for.
     * @param defaultVal Default value to be returned.
     *
     * @return The text child converted to an integer or the default value.
     */
    public static int getInt(Element node, String childName, int defaultVal) {
        String text = getValue(node, childName, "");
        return text.isEmpty() ? defaultVal : Integer.parseInt(text);
    }

    /**
     * returns an XML text child as a long
     *
     * @param node Parent element node.
     * @param childName Child to look for.
     * @param defaultVal Default value to be returned.
     *
     * @return The text child converted to a long or the default value.
     */
    public static long getLong(Element node, String childName, long defaultVal) {
        String text = getValue(node, childName, "");
        return text.isEmpty() ? defaultVal : Long.parseLong(text);
    }

    /**
     * returns an XML text child as a double
     *
     * @param node Parent element node.
     * @param childName Child to look for.
     * @param defaultVal Default value to be returned.
     *
     * @return The text child converted to a double or the default value.
     */
    public static double getDouble(Element node, String childName, double defaultVal) {
        String text = getValue(node, childName, "");
        return text.isEmpty() ? defaultVal : Double.parseDouble(text);
    }

    /**
     * returns an XML text child as a float
     *
     * @param node Parent element node.
     * @param childName Child to look for.
     * @param defaultVal Default value to be returned.
     *
     * @return The text child converted to a float or the default value.
     */
    public static float getFloat(Element node, String childName, float defaultVal) {
        String text = getValue(node, childName, "");
        return text.isEmpty() ? defaultVal : Float.parseFloat(text);
    }

    /**
     * returns an XML text child as a short
     *
     * @param node Parent element node.
     * @param childName Child to look for.
     * @param defaultVal Default value to be returned.
     *
     * @return The text child converted to a short or the default value.
     */
    public static short getShort(Element node, String childName, short defaultVal) {
        String text = getValue(node, childName, "");
        return text.isEmpty() ? defaultVal : Short.parseShort(text);
    }

    /**
     * returns an XML text child as a boolean.
     *
     * @param node Parent element node.
     * @param childName Child to look for.
     * @param defaultVal Default value to be returned.
     *
     * @return The text child converted to a boolean or the default value.
     */
    public static boolean getBool(Element node, String childName, boolean defaultVal) {
        return Boolean.parseBoolean(getValue(node, childName, Boolean.toString(defaultVal)));
    }

    /**
     * returns the text contents of the requested node. If the node does not
     * exist, is not an Element, or its first child is not text, then the
     * default value is returned.
     *
     * @param node The Element that contains the child.
     * @param name The child element to search for.
     * @param defaultVal the default string to return if the child does not
     * exist or does not have a text node as its first child.
     * @return The text of the requested node or the default value.
     */
    public static String getValue(Element node, String name, String defaultVal) {
        if (node == null) {
            return defaultVal;
        }
        Element child = getChild(node, name);
        if (child != null) {
            String text = child.getText().trim();
            return (text.isEmpty() || text == null) ? defaultVal : text;
        }
        return defaultVal;
    }

    /**
     * returns an XML attribute value as an integer.
     *
     * @param node The parent node.
     * @param attrName The name of the attribiute to be converted.
     * @param defaultVal The default value to be returned.
     * @return The integer representation of the attribute with the given name.
     */
    public static int getIntAttr(Element node, String attrName, int defaultVal) {
        String attr = getAttr(node, attrName, "");
        return attr.isEmpty() ? defaultVal : Integer.parseInt(attr);
    }

    /**
     * returns an XML attribute value as a long.
     *
     * @param node The parent node.
     * @param attrName The name of the attribiute to be converted.
     * @param defaultVal The default value to be returned.
     * @return The long representation of the attribute with the given name.
     */
    public static long getLongAttr(Element node, String attrName, long defaultVal) {
        String attr = getAttr(node, attrName, "");
        return attr.isEmpty() ? defaultVal : Long.parseLong(attr);
    }

    /**
     * returns an XML attribute value as a double
     *
     * @param node The parent node.
     * @param attrName The name of the attribiute to be converted.
     * @param defaultVal The default value to be returned.
     * @return The double representation of the attribute with the given name.
     */
    public static double getDoubleAttr(Element node, String attrName, double defaultVal) {
        String attr = getAttr(node, attrName, "");
        return attr.isEmpty() ? defaultVal : Double.parseDouble(attr);
    }

    /**
     * returns an XML attribute value as a float.
     *
     * @param node The parent node.
     * @param attrName The name of the attribiute to be converted.
     * @param defaultVal The default value to be returned.
     * @return The float representation of the attribute with the given name.
     */
    public static float getFloatAttr(Element node, String attrName, float defaultVal) {
        String attr = getAttr(node, attrName, "");
        return attr.isEmpty() ? defaultVal : Float.parseFloat(attr);
    }

    /**
     * returns an XML attribute value as a short
     *
     * @param node The parent node.
     * @param attrName The name of the attribiute to be converted.
     * @param defaultVal The default value to be returned.
     * @return The short representation of the attribute with the given name.
     */
    public static short getShortAttr(Element node, String attrName, short defaultVal) {
        String attr = getAttr(node, attrName, "");
        return attr.isEmpty() ? defaultVal : Short.parseShort(attr);
    }

    /**
     * returns an XML attribute value as a boolean
     */
    public static boolean getBoolAttr(Element node, String attrName, boolean defaultVal) {
        return Boolean.parseBoolean(getAttr(node, attrName, Boolean.toString(defaultVal)));
    }

    /**
     * looks for an attribute in this node, if none is found, then it returns
     * the default value.
     *
     * @param node The parent node.
     * @param attr The name of the attribute desired.
     * @param defaultVal The default value to be returned.
     * @return The desired attribute object or null if none could be found.
     */
    public static String getAttr(Element node, String attr, String defaultVal) {
        if (node == null) {
            return defaultVal;
        }
        String[] attrSplit = attr.split("/");
        for (int i = 0; i < attrSplit.length - 1; i++) {
            node = node.getChild(attrSplit[i]);
            if (node == null) {
                return defaultVal;
            }
        }
        String attrVal = node.getAttribute(attrSplit[attrSplit.length - 1]);
        if (attrVal != null) {
            return attrVal;
        }
        return defaultVal;
    }

    /**
     * Returns the first child node with the given name. If none is found, then
     * this will add an element with the given name and return it.<br/>
     * <br/>
     * To find (or create) a nested node, separate names with the "/" character.
     *
     * @param parent Node to be searched.
     * @return Child node with the given name.
     */
    public static Element getOrAddElement(Element parent, String name) {
        Element el = parent;
        Element child = null;
        for (String n : name.split("/")) {
            child = el.getChild(n);
            if (child == null) {
                child = el.addElement(n);
            }
            el = child;
        }
        return child;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */