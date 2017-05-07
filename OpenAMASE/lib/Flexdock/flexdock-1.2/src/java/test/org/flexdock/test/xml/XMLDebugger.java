// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Jun 26, 2005
 */
package org.flexdock.test.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.flexdock.util.Utilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * @author Christopher Butler
 */
public class XMLDebugger {

    public static void println(Object obj) {
        if(obj==null) {
            System.out.println("null");
            return;
        }

        XMLDebugger debugger = new XMLDebugger();
        String xml = debugger.getXML(obj);
        System.out.println(xml);
    }

    public String getXML(Object obj) {
        if(obj==null)
            return null;

        Document document = createDocument();
        Element rootElem = document.getDocumentElement();
        buildXML(obj, document, rootElem);

        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            if(Utilities.JAVA_1_5) {
                factory.setAttribute("indent-number", new Integer(4));
            }
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            DOMSource src = new DOMSource(document);
            transformer.transform(src, result);
            return writer.toString();

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    private void buildXML(Object obj, Document document, Element parentElem) {
        String elemName = getName(obj);
        Element objElem = document.createElement(elemName);

        if(isBasic(obj)) {
            objElem.setAttribute("value", obj.toString());
            parentElem.appendChild(objElem);
            return;
        }


        Field[] fields = getFields(obj);
        for(int i=0; i<fields.length; i++) {
            String modifiers = Modifier.toString(fields[i].getModifiers());
            if(modifiers.indexOf("static")!=-1 || modifiers.indexOf("transient")!=-1)
                continue;

            Object fieldValue = getValue(fields[i], obj);
            String fieldName = fields[i].getName();

            if(fieldValue==null) {
                objElem.setAttribute(fieldName, "null");
            } else if(isBasic(fieldValue)) {
                objElem.setAttribute(fieldName, fieldValue.toString());
            } else if(fieldValue instanceof Collection) {
                buildXML((Collection)fieldValue, document, objElem, fieldName);
            } else if(fieldValue instanceof Object[]) {
                buildXML((Object[])fieldValue, document, objElem, fieldName);
            } else if(fieldValue instanceof Map) {
                buildXML((Map)fieldValue, document, objElem, fieldName);
            } else {
                buildXML(fieldValue, document, objElem);
            }
        }
        try {
            parentElem.appendChild(objElem);
        } catch(NullPointerException e) {
            System.out.println();
            throw e;
        }
    }



    private void buildXML(Collection collection, Document document, Element parentElem, String fieldName) {
        Element objElem = document.createElement(fieldName);
        objElem.setAttribute("type", "collection");

        for(Iterator it=collection.iterator(); it.hasNext();) {
            Object obj = it.next();
            buildXML(obj, document, objElem);
        }

        parentElem.appendChild(objElem);
    }

    private void buildXML(Map map, Document document, Element parentElem, String fieldName) {
        Element objElem = document.createElement(fieldName);
        objElem.setAttribute("type", "map");

        for(Iterator it=map.keySet().iterator(); it.hasNext();) {
            Object key = it.next();
            Object value = map.get(key);
            Object obj = new MapEntry(key, value);
            buildXML(obj, document, objElem);
        }

        parentElem.appendChild(objElem);
    }

    private void buildXML(Object[] objects, Document document, Element parentElem, String fieldName) {
        Element objElem = document.createElement(fieldName);
        objElem.setAttribute("type", "array");

        for(int i=0; i<objects.length; i++) {
            buildXML(objects[i], document, objElem);
        }

        parentElem.appendChild(objElem);
    }



    private static Field[] getFields(Object obj) {
        Class c = obj.getClass();
        Field[] fields = c.getDeclaredFields();
        if(obj instanceof DefaultMutableTreeNode) {
            Field children = getField(obj, "children");
            if(children!=null) {
                Field[] tmp = new Field[fields.length+1];
                System.arraycopy(fields, 0, tmp, 0, fields.length);
                tmp[tmp.length-1] = children;
                fields = tmp;
            }
        }
        return fields;
    }



    private static Field getField(Object obj, String fieldName) {
        Class c = obj.getClass();

        while(true) {
            try {
                return c.getDeclaredField(fieldName);
            } catch(Exception e) {
                if(c==Object.class)
                    return null;
                c = c.getSuperclass();
            }
        }
    }

    private static Object getValue(Field field, Object owner) {
        boolean toggle = !field.isAccessible();

        if(toggle)
            field.setAccessible(true);

        Object obj = null;
        try {
            obj = field.get(owner);
        } catch(Throwable t) {
            t.printStackTrace();
        }

        if(toggle)
            field.setAccessible(false);

        return obj;
    }

    private static String getName(Object obj) {
        Class clazz = obj.getClass();

        String name = clazz.getName();
        int indx = name.lastIndexOf('.');
        if(indx!=-1)
            name = name.substring(indx+1, name.length());

        indx = name.lastIndexOf('$');
        if(indx!=-1)
            name = name.substring(indx+1, name.length());
        return name;
    }

    private static boolean isBasic(Object obj) {
        return obj.getClass().isPrimitive() || obj.getClass().getName().startsWith("java.lang");
    }

    private static Document createDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            StringReader reader = new StringReader("<?xml version=\"1.0\"?><ObjectXML></ObjectXML>");
            InputSource src = new InputSource(reader);
            return builder.parse(src);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static class MapEntry {
        private Object key;
        private Object value;
        public MapEntry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    public static class DataObject {
        private Object data;

        public DataObject(Object obj) {
            data = obj;
        }
    }

}
