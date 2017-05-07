// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.util;

import avtas.util.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility class to help with manipulating and identifying objects
 * @author AFRL/RQQD
 */
public class ObjectUtils {

    /** A null-safe check of equivalency between two objects.
     * 
     * @param o1 first object to check
     * @param o2 second object to check
     * @return true if <code>o1.equals(o2)</code> or <code>o2.equals(o1)</code> or both are null
     */
    public static boolean equals(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        }
        else if (o1 == null) {
            return o2.equals(o1);
        }
        else {
            return o1.equals(o2);
        }
    }
    
    /**
     * Returns true if the given object equals any of the other objects.
     * @param o1 object under test
     * @param others objects to test against
     * @return true if the object matches any of the objects being tested against
     */
    public static boolean equalsAny(Object o1, Object... others) {
        for (Object o : others) {
            if (equals(o1, o)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns true if the passed string is empty (length of zero) or is null)
     * @param string
     * @return true if the passed string is empty (length of zero) or is null)
     */
    public static boolean isNullString(String string) {
        if (string != null) {
            return string.isEmpty();
        }
        return true;
    }

    /** Returns true if the class type is primitive or one of the primitive equivalents
    (e.g.) a java.lang.Integer is an equivalent to "int"
     */
    public static boolean isPrimitive(Class t) {
        return t.isPrimitive()
                || t.equals(Boolean.class)
                || t.equals(Character.class)
                || t.equals(Integer.class)
                || t.equals(Long.class)
                || t.equals(Short.class)
                || t.equals(Float.class)
                || t.equals(Double.class)
                || t.equals(Byte.class)
                || t.equals(String.class);
    }

    /** converts a primitive class type to its corresponding Object type.
     *
     * @param primClass the primitive class type
     * @return the object-equivalent class type
     */
    public static Class convertPrimitive(Class primClass) {
        if (primClass == int.class) {
            return Integer.class;
        }
        if (primClass == char.class) {
            return Character.class;
        }
        if (primClass == long.class) {
            return Long.class;
        }
        if (primClass == short.class) {
            return Short.class;
        }
        if (primClass == double.class) {
            return Double.class;
        }
        if (primClass == float.class) {
            return Float.class;
        }
        if (primClass == byte.class) {
            return Byte.class;
        }
        if (primClass == boolean.class) {
            return Boolean.class;
        }
        return primClass;
    }

    /** Converts an object type into the corresponding primitive type. 
     *  (e.g. Integer to int).  If there is no corresponding primitive, then 
     *  this returns the original class.
     */
    public static Class getPrimitiveClass(Class c) {
        if (c == Integer.class) {
            return int.class;
        }
        if (c == Long.class) {
            return long.class;
        }
        if (c == Double.class) {
            return double.class;
        }
        if (c == Float.class) {
            return float.class;
        }
        if (c == Short.class) {
            return short.class;
        }
        if (c == Byte.class) {
            return byte.class;
        }
        if (c == Character.class) {
            return char.class;
        }
        return c;
    }

    /**
     * Converts a string to the requested type, or returns null if the string
     * cannot be converted to the type.
     * @param value value to convert
     * @param type the type requested.  This can be primitive or the object-equivalent
     * to a primitive type.
     * @return a new instance of the requested type, or null if the string cannot
     * be converted to the requested type.
     */
    public static Object getValueOf(String value, Class type) {
        try {
            if (type == float.class || type == Float.class) {
                return Float.parseFloat(value);
            }
            if (type == double.class || type == Double.class) {
                return Double.parseDouble(value);
            }
            if (type == int.class || type == Integer.class) {
                int radix = 10;
                if (value.startsWith("0x")) {
                    value = value.substring(2);
                    radix = 16;
                }
                return (int) Long.parseLong(value, radix);
            }
            if (type == long.class || type == Long.class) {
                int radix = 10;
                if (value.startsWith("0x")) {
                    value = value.substring(2);
                    radix = 16;
                }
                return Long.parseLong(value, radix);
            }
            if (type == short.class || type == Short.class) {
                int radix = 10;
                if (value.startsWith("0x")) {
                    value = value.substring(2);
                    radix = 16;
                }
                return (short) Long.parseLong(value, radix);
            }
            if (type == byte.class || type == Byte.class) {
                int radix = 10;
                if (value.startsWith("0x")) {
                    value = value.substring(2);
                    radix = 16;
                }
                return (byte) Long.parseLong(value, radix);
            }
        } catch (NumberFormatException ex) {
            return 0;
        }
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        if (type.isEnum()) {
            return Enum.valueOf(type, value);
        }
        // return the passed value if none of the above apply
        return value;
    }

    /** Returns true if the class type is not a primitive or one of the primitive equivalents in java */
    public static boolean isObject(Class c) {
        return !isPrimitive(c);
    }

    /** Returns true if the class type is or extends java.util.List */
    public static boolean isList(Class c) {
        return List.class.isAssignableFrom(c);
    }
    
    /** Prints out a list in a similar style to {@link java.util.Arrays} */
    public static String toString(Collection<?> collection) {
        return Arrays.toString(collection.toArray());
    }
    
    /** returns a new list that is *NOT* backed by the array, so operations 
     *  such as add() and remove() are valid.
     * @param <T> Type of list to create
     * @param array array of objects passed
     * @return a new list containing the objects from the array
     */
    public static <T> ArrayList<T> asList(T[] array) {
        ArrayList<T> list = new ArrayList<T>();
        for (T o : array) {
            list.add(o);
        }
        return list;
    }
    
    
    /** Writes out an object's fields in the format "name=value" for all fields, 
     *  including inherited fields.
     */
    public static String writeFields(Object o) {
        if (o == null) {
            return "null";
}
        StringBuilder b = new StringBuilder();
        List<Field> fields = ReflectionUtils.getAllFields(o.getClass());
        for (Field f : fields) {
            try {
                f.setAccessible(true);
                b.append(f.getName()).append("=").append(f.get(o)).append("\n");
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(ObjectUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return b.toString();
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */