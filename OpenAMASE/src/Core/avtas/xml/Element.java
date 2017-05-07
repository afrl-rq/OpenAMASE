// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements a basic XML Element. XML Elements can contain other elements or
 * comments as children.
 *
 * @author AFRL/RQQD
 */
public class Element extends XmlNode implements Cloneable {

    private List<Attribute> attrList = null;
    private List<XmlNode> childList = null;
    private String name;
    private String text = "";
    
    protected static class Attribute {
        String name = "", value = "";
    }

    public Element(String name) {
        this.name = name;
    }

    public Element(String name, Object value) {
        this.name = name;
        this.text = value == null ? null : String.valueOf(value);
    }

    public Element clone() {
        Element newEl = new Element(name);
        newEl.setText(text);
        if (attrList != null) {
            for (Attribute attr : attrList) {
                newEl.setAttribute(attr.name, attr.value);
            }
        }
        if (childList != null) {
            for (XmlNode n : childList) {
                newEl.add(n.clone());
            }
        }
        return newEl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Sets the text value of this element.
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        //childList.clear();
    }

    /**
     * Returns the underlying text value.
     */
    public String getText() {
        return text;
    }

    public XmlNode add(XmlNode child) {
        add(childList == null ? 0 : childList.size(), child);
        return child;
    }

    public Element addElement(Object name) {
        Element el = new Element(String.valueOf(name));
        add(el);
        return el;
    }

    public Comment addComment(String comment) {
        return (Comment) add(new Comment(comment));
    }

    public void add(int index, XmlNode child) {
        if (child.getParent() != null && child.getParent() != this) {
            child.getParent().remove(child);
        }
        child.setParent(this);
        if (childList == null) {
            childList = new ArrayList<>();
        }
        childList.add(index, child);
    }

    public XmlNode get(int index) {
        return childList == null ? null : childList.get(index);
    }

    public void set(int index, XmlNode child) {
        if (childList == null) {
            childList = new ArrayList<>();
        }
        child.setParent(this);
        XmlNode oldChild = childList.set(index, child);
        if (oldChild != null) oldChild.setParent(null);
    }

    public void set(int index, Collection<? extends XmlNode> children) {
        if (childList == null) {
            childList = new ArrayList<>();
        }
        XmlNode oldChild = childList.get(index);
        if (oldChild != null) {
            oldChild.setParent(null);
        }
        addAll(index, children);
        // remove the node that was in the list originally (since this is a set operation)
        remove(index + children.size());
    }

    public void addAll(Collection<? extends XmlNode> children) {
        for (XmlNode n : children) {
            n.setParent(this);
        }
        if (childList == null) {
            childList = new ArrayList<>();
        }
        childList.addAll(children);
    }

    public void addAll(int index, Collection<? extends XmlNode> children) {
        for (XmlNode n : children) {
            n.setParent(this);
        }
        if (childList == null) {
            childList = new ArrayList<>();
        }
        childList.addAll(index, children);
    }

    public void remove(int index) {
        XmlNode n = childList == null ? null : childList.remove(index);
        if (n != null) {
            n.setParent(null);
        }
    }

    public void remove(XmlNode child) {
        if (childList != null && childList.remove(child)) {
            child.setParent(null);
        }
    }

    public void removeAll(Collection<? extends XmlNode> c) {
        if (childList != null) {
            if (childList.removeAll(c)) {
                for (XmlNode n : c) {
                    n.setParent(null);
                }
            }
        }
    }

    public int indexOf(XmlNode child) {
        return childList == null ? -1 : childList.indexOf(child);
    }

    /**
     * returns true if this element contains the object.
     */
    public boolean contains(XmlNode o) {
        return childList == null ? false : childList.contains(o);
    }

    public void clear() {
        if (childList != null) {
            for (XmlNode n : getChildren()) {
                n.setParent(null);
            }
            childList.clear();
        }
    }

    /**
     * Returns the number of children
     */
    public int getChildCount() {
        return childList == null ? 0 : childList.size();
    }

    public boolean hasChildren() {
        return childList != null && !childList.isEmpty();
    }

    /**
     * Places an attribute in the attribute map. If there is an attribute with
     * the same name, this replaces it. Objects are converted to Strings for
     * storage in the map.
     */
    public void setAttribute(String name, Object value) {
        if (attrList == null) {
            attrList = new ArrayList<>();
        }
        for (Attribute attr : attrList) {
            if (attr.name.equals(name)) {
                attr.value = String.valueOf(value);
                return;
            }
        }
        Attribute attr = new Attribute();
        attr.name = name;
        attr.value = String.valueOf(value);
        attrList.add(attr);
    }

    /**
     * Returns the requested attribute, or null if no attribute with the given
     * name is contained in this element.
     */
    public String getAttribute(String name) {
        if (attrList != null) {
            for (Attribute attr : attrList) {
                if (attr.name.equals(name)) {
                    return attr.value;
                }
            }
        }
        return null;
    }
    
    /** Returns true if this element contains attributes. */
    public boolean hasAttributes() { 
        return attrList != null && !attrList.isEmpty();
    }
    
    /** Returns the number of attributes contained in this node. */
    public int getAttributeCount() {
        return attrList == null ? 0 : attrList.size();
    }
    
    /** Returns the name of the attribute at the given index. 
     * @see #getAttributeCount().
     * @param index
     */
    public String getAttributeName(int index) {
        return attrList.get(index).name;
    }
    
    /** Returns the value of the attribute at the given index. 
     * @see #getAttributeCount().
     * @param index
     */
    public String getAttributeValue(int index) {
        return attrList.get(index).value;
    }

    public void removeAttribute(String name) {
        if (attrList != null) {
            for (int i=0; i<attrList.size(); i++) {
                if (attrList.get(i).name.equals(name)) {
                    attrList.remove(i);
                    break;
                }
            }
        }
    }

    public void clearAttributes() {
        if (attrList != null) attrList.clear();
        attrList = null;
    }

    /**
     * Returns a list of child elements contained in this element. Changes to
     * the list are NOT reflected in this element, but changes in elements
     * contained in the list are reflected in children of this element.
     */
    public synchronized List<Element> getChildElements() {
        List<Element> retList = new ArrayList<Element>();
        if (childList != null) {
            for (XmlNode n : childList) {
                if (n instanceof Element) {
                    retList.add((Element) n);
                }
            }
        }
        return retList;
    }

    /**
     * Returns a list of children contained in this element. Changes to the list
     * are NOT reflected in this element, but changes in nodes contained in the
     * list are reflected in children of this element.
     */
    public synchronized List<XmlNode> getChildren() {
        return childList == null ? new ArrayList<XmlNode>() : new ArrayList<>(childList);
    }

    /**
     * Returns the first child node that matches the given name, or null if none
     * is found. This method uses regular expressions to find matches. A nested
     * node can be obtained by passing a path name with "/" separators.
     */
    public synchronized Element getChild(String regexName) {
        if (!hasChildren()) {
            return null;
        }
        String[] childNames = regexName.split("/", 2);
        if (childNames.length == 1) {
            for (XmlNode n : childList) {
                if (n instanceof Element) {
                    Element el = (Element) n;
                    if (el.getName().matches(regexName)) {
                        return el;
                    }
                }
            }
        }
        else {
            for (XmlNode n : childList) {
                if (n instanceof Element) {
                    Element el = (Element) n;
                    if (el.getName().matches(childNames[0])) {
                        return el.getChild(childNames[1]);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns a list of children that match the given name, or an empty list if
     * none are found. This method uses regular expressions to find matches.
     * Changes in the list have no effect on the parent node, but changes to
     * nodes within the list effect nodes in this node's child list. Nested
     * nodes can be obtained by passing a path name with "/" separators.
     */
    public synchronized List<Element> getChildren(String regexName) {
        List<Element> retList = new ArrayList<>();
        
        if (!hasChildren()) {
            return retList;
        }
        String[] childNames = regexName.split("/", 2);
        
        if (childNames.length == 1) {
            for (XmlNode n : childList) {
                if (n instanceof Element) {
                    Element el = (Element) n;
                    if (el.getName().matches(regexName)) {
                        retList.add(el);
                    }
                }
            }
        }
        else {
            for (XmlNode n : childList) {
                if (n instanceof Element) {
                    Element el = (Element) n;
                    if (el.getName().matches(childNames[0])) {
                        retList.addAll(el.getChildren(childNames[1]));
                    }
                }
            }
        }
        return retList;
    }

    // convenience methods for getting data as different types
    public String getAttr(String name, String defaultVal) {
        String attr = getAttribute(name);
        try {
            return attr == null ? defaultVal : attr;
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public int getIntAttr(String name, int defaultVal) {
        String attr = getAttr(name, null);
        try {
            return attr == null ? defaultVal : Integer.valueOf(attr);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public long getLongAttr(String name, long defaultVal) {
        String attr = getAttr(name, null);
        try {
            return attr == null ? defaultVal : Long.valueOf(attr);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public float getFloatAttr(String name, float defaultVal) {
        String attr = getAttribute(name);
        try {
            return attr == null ? defaultVal : Float.valueOf(attr);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public double getDoubleAttr(String name, double defaultVal) {
        String attr = getAttr(name, null);
        try {
            return attr == null ? defaultVal : Double.valueOf(attr);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public boolean getBoolAttr(String name, boolean defaultVal) {
        String attr = getAttr(name, null);
        try {
            return attr == null ? defaultVal : Boolean.valueOf(attr);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public int getIntValue(int defaultVal) {
        try {
            return (text == null | text.isEmpty()) ? defaultVal : Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public long getLongValue(long defaultVal) {
        try {
            return (text == null | text.isEmpty()) ? defaultVal : Long.parseLong(text);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public float getFloatValue(float defaultVal) {
        try {
            return (text == null | text.isEmpty()) ? defaultVal : Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public double getDoubleValue(int defaultVal) {
        try {
            return (text == null | text.isEmpty()) ? defaultVal : Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public String toString() {
        return toXML();
    }

    /**
     * Creates a new Element from a file.
     *
     * @param file file to read
     * @return a new element if the file exists and is valid.
     */
    public static Element read(File file) {
        try {
            return XmlReader.readDocument(file);
        } catch (Exception ex) {
            Logger.getLogger(XMLUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Creates a new Element from a string.
     *
     * @param xml string to read
     * @return a new element if the string contains valid XML.
     */
    public static Element read(String xml) {
        try {
            return XmlReader.readDocument(xml);
        } catch (Exception ex) {
            Logger.getLogger(XMLUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Returns a "pretty" representation of this element. (line returns and
     * indents)
     */
    public String toXML() {
        return XmlWriter.toFormattedString(this);
    }

    /**
     * Writes this XML Element out to a file, with default indents specified.
     *
     * @param file File for the XML output.
     */
    public void toFile(File file) {
        try {
            XmlWriter.writeToFile(file, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */