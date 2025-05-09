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

import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

/**
 *
 * @author matt
 */
public class LMCPXMLReader {

    /** Attempts to read an LMCPObject from the given element.  To return an object, the element must
     *  contain an LMCPObject from a series that is registered with this factory.
     *
     * @param el the element that contains an XML representation of an LMCPObject.
     * @return the LMCPObject read from the element, or null if the element does not contain an LMCP object.
     */
    public static Object readXML(Element el) {

        for (LMCPEnum e : LMCPFactory.getLoadedSeries()) {
            if (el.getTagName().equalsIgnoreCase("NULL")) {
                return null;
            }
            if (!el.hasAttribute("Series")) {
                //System.err.println("LMCPXMLReader: Badly formatted XML Representation.");
                return null;
            }
            if (el.getAttribute("Series").equals(e.getSeriesName())) {
                try {
                    Package p = e.getClass().getPackage();
                    Class c = Class.forName(p.getName() + "." + el.getTagName());
                    Object o = c.newInstance();

                    ArrayList<Field> fields = new ArrayList<Field>();
                    Class cc = c;
                    do {
                        fields.addAll(Arrays.asList(cc.getDeclaredFields()));
                        cc = cc.getSuperclass();
                    } while (cc != LMCPObject.class);

                    NodeList childNodes = el.getChildNodes();
                    for (int i = 0; i < childNodes.getLength(); i++) {
                        Node child = childNodes.item(i);
                        if (child instanceof Element) {
                            String elName = child.getNodeName();
                            for (Field f : fields) {
                                if (f.getName().equals(elName)) {
                                    setValue((Element) child, f, o);
                                    break;
                                }
                            }
                        }
                    }

                    return o;

                } catch (Exception ex) {
                    Logger.getLogger(LMCPXMLReader.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        return null;
    }

    /**
     * Attempts to read an LMCPObject from the string.  The string must contain an LMCPObject
     * from a series that is registered with this factory.
     *
     * @param string the string that contains the LMCP Object in serialized form.
     * @return a new LMCPObject or null if the string does not contain an XML representation of an LMCPObject.
     */
    public static LMCPObject readXML(String string) throws Exception {
            InputSource is = new InputSource(new StringReader(string));
            Element el = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is).getDocumentElement();
            return (LMCPObject) readXML(el);

    }

    /**
     * Attempts to read an XML node and set the value read to the corresponding field.
     * @param el  The element to parse
     * @param f field to set in the object
     * @param obj the object to populate
     */
    private static void setValue(Element el, Field f, Object obj) {
        try {

            f.setAccessible(true);
            boolean isLMCPObj = LMCPObject.class.isAssignableFrom(f.getType());
            boolean isList = List.class.isAssignableFrom(f.getType());
            boolean isArray = f.getType().isArray();

            if (isArray) {
                Object[] items = readList(f, el);
                Object array = f.get(obj);
                int len = Array.getLength(array);
                for (int i = 0; i < items.length; i++) {
                    if (i < len) {
                        Array.set(array, i, items[i]);
                    }
                }
            } else if (isList) {
                Object[] items = readList(f, el);
                List flist = (List) f.get(obj);
                for (Object o : items) {
                    flist.add(o);
                }
            } else if (isLMCPObj) {
                NodeList list = el.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    if (list.item(i) instanceof Element) {
                        Object o = readXML((Element) list.item(i));
                        f.set(obj, o);
                        break;
                    }
                }

            } else {
                f.set(obj, readPrimitive(f.getType(), el));
            }

        } catch (Exception ex) {
            Logger.getLogger(LMCPXMLReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** reads objects from the XML node and return an array that includes all of the
     *  objects found.  This is called when parsing lists and fixed-length arrays.
     */
    private static Object[] readList(Field f, Element el) throws Exception {
        NodeList list = el.getChildNodes();
        ArrayList<Object> retList = new ArrayList<Object>();
        Class type = f.getType().getComponentType();
        if (type == null) {
            ParameterizedType ptype = (ParameterizedType) f.getGenericType();
            type = (Class) ptype.getActualTypeArguments()[0];
        }

        boolean isLMCPObj = LMCPObject.class.isAssignableFrom(type);

        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i) instanceof Element) {
                if (isLMCPObj) {
                    Object o = readXML((Element) list.item(i));
                    retList.add(o);
                } else {
                    Object o = readPrimitive(type, (Element) list.item(i));
                    retList.add(o);
                }
            }
        }

        return retList.toArray();
    }

    // reads a primitive (i.e. non-LMCP object) type from the XML node and returns it.
    private static Object readPrimitive(Class type, Node item) throws Exception {

        // get the "value" from the XML Element (the text value)
        NodeList list = item.getChildNodes();
        String val = "";
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i) instanceof Text) {
                val = list.item(i).getNodeValue();
                break;
            }
        }

        if (type.isEnum()) {
            Method m = type.getMethod("valueOf", String.class);
            Object enumVal = m.invoke(null, val);
            return enumVal;
        }

        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(val);
        } else if (type == long.class || type == Long.class) {
            return Long.parseLong(val);
        } else if (type == short.class || type == Short.class) {
            return Short.parseShort(val);
        } else if (type == float.class || type == Float.class) {
            return Float.parseFloat(val);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(val);
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(val);
        } else if (type == String.class || type == String.class) {
            return val;
        }

        return null;
    }
}
