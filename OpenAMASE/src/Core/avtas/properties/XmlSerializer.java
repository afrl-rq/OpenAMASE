// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.properties;

import avtas.util.ObjectUtils;
import avtas.util.ReflectionUtils;
import avtas.xml.Comment;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Transforms user properties to and from XML. Objects that have fields
 * annotated with {@link UserProperty} can be serialized and deserialized using
 * this class. Only fields marked as user properties will be handled. The
 * following types are supported:<br/><br/>
 * <ul>
 * <li>Primitives (int, long, double, float, byte, short, boolean, char) and
 * their Object-equivalents</li>
 * <li>Strings</li>
 * <li>Files. Files are reduced to string paths for storage. Relative paths are
 * preserved. </li>
 * <li>Paths. This NIO2 concept is handled similar to files</li>
 * <li>Colors. Written as a comma-separated string as [red, green, blue, alpha] in the range 0...255</li>
 * <li>Fonts. Fonts are written in the format : <em>name,style,size</em></li>
 * <li>nulls. Regardless of type, nulls are written as "null".</li>
 * <li>Lists.  Lists are supported, but must contain types that are listed above.</li>
 * </ul>
 * All fields are written as XML elements with names equal to the field name.
 * Avoid hiding superclass fields since the class hierarchy is scanned to get
 * all fields annotated with {@link UserProperty}.
 *
 * @author AFRL/RQQD
 */
public class XmlSerializer {

    /**
     * Writes user properties to an XML element. The element will have a name
     * that matches the class name of the object. Fields marked with a
     * {@link UserProperty} are written to the element as children.
     *
     * @param obj the object to serialize
     * @return an XML element containing user property data
     */
    public static Element serialize(Object obj) {

        String className = obj.getClass().getCanonicalName();
        if (className == null) {
            return null;
        }
        Element topEl = new Element(className);
        
        List<Field> fields = ReflectionUtils.getAllFields(obj.getClass());
        for (Field f : fields) {
            try {
                f.setAccessible(true);
                UserProperty attr = f.getAnnotation(UserProperty.class);
                if (attr == null) continue;

                if (!isValidType(f.getType())) {
                    continue;
                }

                if (!attr.Description().isEmpty()) {
                    topEl.add(new Comment(attr.Description()));
                }
                Element fieldEl = (Element) topEl.add(new Element(f.getName()));
                Object fObj = f.get(obj);
                if (fObj instanceof List) {
                    for (Object item : (List) fObj) {
                        if (item != null) {
                            Element itemEl = fieldEl.addElement(item.getClass().getSimpleName());
                            itemEl.setText(toString(item));
                        }
                    }
                }
                else {
                    fieldEl.setText(toString(f.get(obj)));
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(XmlSerializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return topEl;
    }

    /**
     * Returns a new object that is represented by the data in the element. Th
     * object must have a default constructor.
     *
     * @param el XML element containing the content
     * @return A new object or null if one could not be created.
     */
    public static Object deserialize(Element el) {

        String className = el.getName();
        try {
            Class c = Class.forName(className);
            Object o = c.newInstance();
            for (Element child : el.getChildElements()) {
                
                Field f = ReflectionUtils.getField(c, child.getName());
                if (f == null) {
                    continue;
                }
                
                if (List.class.isAssignableFrom(f.getType())) {
                    List list = (List) f.get(o);
                    if (list == null) {
                        list = new ArrayList();
                    }
                    else {
                        list.clear();
                    }
                    for (Element listChild : child.getChildElements()) {
                        Class childClass = getFullClass(listChild.getName());
                        Object val = ObjectUtils.getValueOf(listChild.getText(), childClass);
                        list.add(val);
                    }
                }
                
                else if (f != null) {
                    f.set(o, fromString(f.getType(), child.getText()));
                }
            }
            return o;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(XmlSerializer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Fills an object's user properties with the values from the XML element.
     *
     * @param el Element containing user data
     * @param owner the object that is to be modified
     */
    public static void deserialize(Element el, Object owner) {

        Class c = owner.getClass();
        try {
            for (Element child : el.getChildElements()) {
                Field f = ReflectionUtils.getField(c, child.getName());
                if (f != null) {
                    f.set(owner, fromString(f.getType(), child.getText()));
                }
            }
        } catch (IllegalAccessException ex) {
            Logger.getLogger(XmlSerializer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String toString(Object val) {
        if (val == null) {
            return "null";
        }
        if (val instanceof File) {
            return ((File) val).getPath();
        }
        else if (val instanceof Color) {
            Color c = (Color) val;
            return c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "," + c.getAlpha();
        }
        else if (val instanceof Font) {
            Font font = (Font) val;
            return font.getName() + "," + font.getStyle() + "," + font.getSize();
        }
        else return String.valueOf(val);
    }

    public static Object fromString(Class type, String val) {

        if (val.equals("null")) {
            return null;
        }
        if (type == File.class) {
            return new File(val);
        }
        else if (type == Color.class) {
            String[] cvals = val.split(",");
            int r = 0, g = 0, b = 0, a = 0;
            if (cvals.length > 0) r = Integer.valueOf(cvals[0]);
            if (cvals.length > 1) g = Integer.valueOf(cvals[1]);
            if (cvals.length > 2) b = Integer.valueOf(cvals[2]);
            if (cvals.length > 3) a = Integer.valueOf(cvals[3]);
            return new Color(r, g, b, a);
        }
        else if (type == Font.class) {
            String[] s = val.split(",");
            Font font = new Font(s[0], Integer.valueOf(s[1]), Integer.valueOf(s[2]));
            return font;
        }
        else return ObjectUtils.getValueOf(val, type);

    }

    static boolean isValidType(Class c) {
        return ObjectUtils.isPrimitive(c) || c == String.class || c == Color.class 
                || c == Font.class || c == File.class || c == Path.class || List.class.isAssignableFrom(c)
                || c.isEnum();
    }
    
    static Class getFullClass(String alias) {
        try {
            switch (alias) {
                case "String" : return String.class;
                case "File" : return File.class;
                case "Path" : return Path.class;
                case "Color" : return Color.class;
                case "Font" : return Font.class;
            }
            
            return Class.forName(alias);
        
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(XmlSerializer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void main(String[] args) {
        TestClass obj = new TestClass();

        Element el = XmlSerializer.serialize(obj);
        System.out.println(el.toXML());

        Object newObj = XmlSerializer.deserialize(el);
        System.out.println(ObjectUtils.writeFields(newObj));
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */