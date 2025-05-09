// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package avtas.lmcp;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMLUtil {

    public static Element parse(File xmlFile) {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile).getDocumentElement();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } 
    }

    public static Element parse(InputStream xmlFile) {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile).getDocumentElement();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static Element parse(String xmlString) {
        try {
            InputSource is = new InputSource(new StringReader(xmlString));
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is).getDocumentElement();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } 
    }
    public static String write(Node node) {
        StringWriter sw = new StringWriter();
        StreamResult out = new StreamResult(sw);  
        DOMSource domsrc = new DOMSource(node);
        
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setAttribute("indent-number", new Integer(2));
            Transformer xform = factory.newTransformer();
            xform.setOutputProperty(OutputKeys.INDENT, "yes");
            xform.transform(domsrc, out);
        } catch (Exception ee) { ee.printStackTrace(); }
       
        return sw.toString();
    }
    public static String get(Node node, String name, String defaultVal) {
        try {
            node = getChild(node, name);
            // expects the first child to be a text node
            return node.getFirstChild().getNodeValue();
        } catch (Exception ex) {
            return defaultVal;
        }
    }
    public static String get(Node node, String name) {
        return get(node, name, "");
    }
    /** returns all child nodes with the given name. If a listName is not null, then
     *  this method returns nodes that are children of the first node encountered with a
     *  node name equal to listName and match itemName.
     */
    public static Node[] getList(Node parent, String listName, String itemName) {
        if (parent == null) return new Node[] {};
        
        if (listName != null && !listName.equals(""))
            return getList( getChild(parent, listName), null, itemName);
       
        return getChildren(parent, itemName);
    }
    
    public static boolean getBool(Node node, String key, boolean defaultValue) {
        try {
            return Boolean.valueOf( get(node, key) ).booleanValue();
        }catch (Exception e) { return defaultValue; }
    }
    public static double getDouble(Node node, String key, double defaultVal) {
        try {
            return Double.parseDouble( get(node, key) );
        } catch (Exception e) { return defaultVal; }
    }
    public static float getFloat(Node node, String key, float defaultVal) {
       try {
            return Float.parseFloat( get(node, key) );
        } catch (Exception e) { return defaultVal; }
    }
    public static int getInt(Node node, String key, int defaultVal) {
        try {
            return Integer.parseInt( get(node, key) );
        } catch (Exception e) { return defaultVal; }
    }
    
    public static long getLong(Node node, String key, long defaultVal) {
        try {
            return Long.parseLong( get(node, key) );
        } catch (Exception e) { return defaultVal; }
    }
    
    public static short getShort(Node node, String key, short defaultVal) {
        try {
            return Short.parseShort( get(node, key) );
        } catch (Exception e) { return defaultVal; }
    }
    private static void toProperties(Node node, Properties map, String leadingTag) {
       
        String tag = leadingTag + node.getNodeName();
        
        if ( node.getNodeValue() != null) {
            map.put(tag, node.getNodeValue() );
        }
        
        NodeList list = node.getChildNodes();
        for ( int i=0; i<list.getLength(); i++ ) {
            toProperties(list.item(i), map, tag + "/");
        }
        
    }
   
    /** returns the first child node with the given name, or null if none is found.
     *  To find a nested node, separate names with the "/" character.
     *  The passed string can contain regex expressions for pattern matching.
     */
    public static Node getChild(Node node, String regexChildName) {
        String[] childNames = regexChildName.split("/", 2);
        NodeList list = node.getChildNodes();
        
        for ( int i=0; i<list.getLength(); i++ ) {
            if (Pattern.matches(childNames[0], list.item(i).getNodeName())) {
                if ( childNames.length == 1)
                    return list.item(i);
                else
                    return getChild(list.item(i), childNames[1]);
            }
        }
        return null;
    }
   
    /**
     * Returns an array containing all children encountered with specified name, or null
     * if none are found. to find nesteded children, specify the childname as path separated by "/".
     * The passed string can contain regex expressions for pattern matching.
     */
    public static Node[] getChildren(Node node, String regexChildName) {
        
        ArrayList <Node> list = new ArrayList <Node>();
        String[] childNames = regexChildName.split("/", 2);
        
        NodeList nodelist = node.getChildNodes();
        
        for ( int i=0; i<nodelist.getLength(); i++ ) {
            if( Pattern.matches(childNames[0], nodelist.item(i).getNodeName()) ){
                if ( childNames.length == 1)
                    list.add(nodelist.item(i));
                else
                    list.addAll( Arrays.asList( getChildren(nodelist.item(i), childNames[1])) );
            }
        }
        
        return (Node[]) list.toArray( new Node[] {} );
    }
    
    /** looks for an attribute in this node, if none is found, then it returns the 
     *  default value.
     */
    public static String getAttribute(Node node, String attr, String defaultVal) {
        if (node != null && node.hasAttributes()) {
            Node attrNode = node.getAttributes().getNamedItem(attr);
            if (attrNode != null) 
                return attrNode.getNodeValue();
        }
        return defaultVal;
    }
    
}
