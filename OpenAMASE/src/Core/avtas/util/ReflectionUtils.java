// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A set of utilities that aid in manipulating and viewing objects via the
 * reflection API.
 *
 * @author AFRL/RQQD
 */
public class ReflectionUtils {

    /**
     * Creates a new object from the given classname and returns the object
     * created. This method will only work if the class can be instantiated with
     * the default zero-argument constructor.
     *
     * @param classname the name of the class to be instantiated.
     *
     * @return the object created.
     */
    public static Object createInstance(String classname) throws Exception {

        Class c = Class.forName(classname);
        //if there are no public constructors, assume this is a singleton and do a getInstance instead
        if (c.getConstructors().length == 0) {
            //probably should do more checks here, but anything falls to the "could not create" exception anyway
            return Class.forName(classname).getDeclaredMethod("getInstance").invoke(null);
        }
        Object o = Class.forName(classname).newInstance();
        return o;

    }

    /**
     * Creates a new object from the given classname and returns the object
     * created. This method attempts to call a constructor that corresponds to
     * the parameters given.
     *
     * @param classname the name of the class to be instantiated.
     *
     * @return the object created.
     */
    public static Object createInstance(String classname, Object... parameters) {
        try {
            Class c = Class.forName(classname);
            Class[] paramTypes = new Class[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                paramTypes[i] = parameters[i].getClass();
            }
            Constructor con = c.getConstructor(paramTypes);
            return con.newInstance(parameters);
        } catch (Exception e) {
            System.err.println("Could not create " + classname);
            return null;
        }
    }

    /**
     * Sets the value of a field using reflection.
     *
     * @param parent the object to change
     * @param fieldName the field to set
     * @param value the value that the field should be set to.
     * @return true if the value was successfully set.
     */
    public static boolean setFieldValue(Object parent, String fieldName, Object value) {
        if (parent == null) {
            return false;
        }
        Class c = parent.getClass();
        while (c != Object.class) {
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                if (f.getName().equals(fieldName)) {
                    try {
                        f.set(parent, value);
                        return true;
                    } catch (Exception ex) {
                        Logger.getLogger(ReflectionUtils.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            c = c.getSuperclass();
        }
        return false;
    }

    /**
     * Returns the value of a field from an object
     *
     * @param parent the object that contains the requested field.
     * @param fieldName name of the field to access
     * @return the value in the field, or null if the field does not exist.
     */
    public static Object getFieldValue(Object parent, String fieldName) {
        if (parent == null) {
            return null;
        }
        Class c = parent.getClass();
        while (c != Object.class) {
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                if (f.getName().equals(fieldName)) {
                    try {
                        return f.get(parent);
                    } catch (Exception ex) {
                        Logger.getLogger(ReflectionUtils.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            c = c.getSuperclass();
        }
        return null;
    }

    /**
     * returns all fields in the given class, including all of the fields of the
     * superclasses, in the order that they are declared from highest antecedent
     * downwards.
     *
     * @param c
     */
    public static ArrayList<Field> getAllFields(Class c) {
        ArrayList<Field> retList = new ArrayList<Field>();
        while (c != Object.class) {
            retList.addAll(0, Arrays.asList(c.getDeclaredFields()));
            c = c.getSuperclass();
        }
        return retList;
    }

    /**
     * Returns a field with the given name from the class or null if none is
     * found. This searches from lowest class upward.
     *
     * @param c the class to extract a field from
     * @param name the name of the field
     * @return the field, or null if no field is found.
     */
    public static Field getField(Class c, String name) {
        List<Field> list = getAllFields(c);
        Collections.reverse(list);
        for (Field f : list) {
            if (f.getName().equals(name)) {
                f.setAccessible(true);
                return f;
            }
        }
        return null;
    }

    /**
     * Copies all of the field values from one object to another. This requires
     * that the source object and the destination objects are the same class or
     * share a superclass.<br/>
     *
     * Warning: This method does not clone objects, so non-primitive types will
     * be referenced in both the source and destination object containers.
     *
     * @param copyFrom
     * @param copyTo
     */
    public static void copyFields(Object copyFrom, Object copyTo) {
        if (copyFrom == null || copyTo == null) {
            return;
        }
        Class fromClass = copyFrom.getClass();
        Class toClass = copyTo.getClass();

        // find a common class between the source and destination
        // or return if there is no compatibility
        if (!fromClass.isAssignableFrom(toClass)) {
            if (toClass.isAssignableFrom(fromClass)) {
                Class tmp = fromClass;
                toClass = fromClass;
                fromClass = tmp;
            } else {
                return;
            }
        }

        while (fromClass != Object.class) {
            for (Field f : getAllFields(fromClass)) {
                try {
                    if ((f.getModifiers() & (Modifier.FINAL | Modifier.STATIC)) != (Modifier.FINAL | Modifier.STATIC)) {
                        f.setAccessible(true);
                        f.set(copyTo, f.get(copyFrom));
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ReflectionUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            fromClass = fromClass.getSuperclass();
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */