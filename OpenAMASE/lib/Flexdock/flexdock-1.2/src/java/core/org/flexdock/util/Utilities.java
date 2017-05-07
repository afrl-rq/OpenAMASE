// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Aug 29, 2004
 */
package org.flexdock.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;




/**
 * @author Christopher Butler
 */
public class Utilities {

    /**
     * A constant representing the Java version. This constant is {@code true}
     * if the version is 1.4.
     */
    public static final boolean JAVA_1_4 = isJavaVersion("1.4");

    /**
     * A constant representing the Java version. This constant is {@code true}
     * if the version is 1.5.
     */
    public static final boolean JAVA_1_5 = isJavaVersion("1.5");

    /**
     * A String representing the flexdock version. This constant is a string.
     */
    public static final String VERSION = "1.2.3";

    private Utilities() {
        // does nothing
    }

    /**
     * Returns an {@code int} value for the specified {@code String}. This
     * method calls {@code Integer.parseInt(String s)} and returns the resulting
     * {@code int} value. If any {@code Exception} is thrown, this method
     * returns a value of {@code 0}.
     *
     * @param data
     *            a {@code String} containing the {@code int} representation to
     *            be parsed
     * @return the integer value represented by the argument in decimal
     * @see #getInt(String, int)
     * @see Integer#parseInt(java.lang.String)
     */
    public static int getInt(String data) {
        return getInt(data, 0);
    }

    /**
     * Returns an {@code int} value for the specified {@code String}. This
     * method calls {@code Integer.parseInt(String s)} and returns the resulting
     * {@code int} value. If any {@code Exception} is thrown, this method
     * returns the value supplied by the {@code defaultValue} parameter.
     *
     * @param data
     *            a {@code String} containing the {@code int} representation to
     *            be parsed
     * @param defaultValue
     *            the value to return if an {@code Exception} is encountered.
     * @return the integer value represented by the argument in decimal
     * @see Integer#parseInt(java.lang.String)
     */
    public static int getInt(String data, int defaultValue) {
        if (data == null)
            return defaultValue;

        try {
            return Integer.parseInt(data);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Returns a {@code float} value for the specified {@code String}. This
     * method calls {@code Float.parseFloat(String s)} and returns the resulting
     * {@code float} value. If any {@code Exception} is thrown by
     * {@code parseFloat}, this method returns the value supplied by the
     * {@code defaultValue} parameter.
     *
     * @param data
     *            a {@code String} containing the {@code float} representation
     *            to be parsed
     * @param defaultValue
     *            the value to return if an {@code Exception} is encountered on
     *            the underlying parse mechanism.
     * @return the floating-point value represented by the argument in decimal
     * @see Float#parseFloat(java.lang.String)
     */
    public static float getFloat(String data, float defaultValue) {
        if (data == null)
            return defaultValue;

        try {
            return Float.parseFloat(data);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Returns {@code true} if the specified {@code String} is {@code null} or
     * contains only whitespace. Otherwise, returns {@code false}. The
     * whitespace check is performed by calling {@code trim()} and checking to
     * see if the trimmed string {@code length()} is zero.
     *
     * @param data
     *            the {@code String} to check for non-whitespace content
     * @return {@code true} if the specified {@code String} is {@code null} or
     *         contains only whitespace, {@code false} otherwise.
     */
    public static boolean isEmpty(String data) {
        return data == null ? true : data.trim().length() == 0;
    }

    /**
     * Returns an instance of the specified class name. If {@code className} is
     * {@code null}, then this method returns a {@code null} reference.
     * <p>
     * This method will try two different means of obtaining an instance of
     * {@code className}. First, it will attempt to resolve the {@code Class}
     * of {@code className} via {@code Class.forName(String className)}. It
     * will then use reflection to search for a method on the class named
     * {@code "getInstance()"}. If the method is found, then it is invoked and
     * the object instance is returned.
     * <p>
     * If there are any problems encountered while attempting to invoke
     * {@code getInstance()} on the specified class, the {@code Throwable} is
     * caught and this method dispatches to
     * {@code createInstance(String className, boolean failSilent)} with an
     * argument of {@code false} for {@code failSilent}.
     * {@code createInstance(String className, boolean failSilent)} will attempt
     * to invoke {@code newInstance()} on the {@code Class} for the specified
     * class name. If any {@code Throwable} is encountered during this process,
     * the value of {@code false} for {@code failSilent} will cause the stack
     * trace to be printed to the {@code System.err} and a {@code null}
     * reference will be returned.
     *
     * @param className
     *            the fully qualified name of the desired class.
     * @return an instance of the specified class
     * @see #getInstance(String, boolean)
     * @see #createInstance(String, boolean)
     * @see Class#forName(java.lang.String)
     * @see Class#getMethod(java.lang.String, java.lang.Class[])
     * @see Method#invoke(java.lang.Object, java.lang.Object[])
     * @see Class#newInstance()
     */
    public static Object getInstance(String className) {
        return getInstance(className, false);
    }

    /**
     * Returns an instance of the specified class name. If {@code className} is
     * {@code null}, then this method returns a {@code null} reference.
     * <p>
     * This method will try two different means of obtaining an instance of
     * {@code className}. First, it will attempt to resolve the {@code Class}
     * of {@code className} via {@code Class.forName(String className)}. It
     * will then use reflection to search for a method on the class named
     * {@code "getInstance()"}. If the method is found, then it is invoked and
     * the object instance is returned.
     * <p>
     * If there are any problems encountered while attempting to invoke
     * {@code getInstance()} on the specified class, the {@code Throwable} is
     * caught and this method dispatches to
     * {@code createInstance(String className, boolean failSilent)}, passing
     * the specified value for {@code failSilent}.
     * {@code createInstance(String className, boolean failSilent)} will attempt
     * to invoke {@code newInstance()} on the {@code Class} for the specified
     * class name. If any {@code Throwable} is encountered during this process,
     * the value of {@code failSilent} is checked to determine whether the stack
     * stack trace should be printed to the {@code System.err}. A {@code null}
     * reference will be returned if any problems are encountered.
     *
     * @param className
     *            the fully qualified name of the desired class.
     * @param failSilent
     *            {@code true} if the stack trace should <b>not</b> be printed
     *            to the {@code System.err} when a {@code Throwable} is caught,
     *            {@code false} otherwise.
     * @return an instance of the specified class
     * @see #createInstance(String, boolean)
     * @see Class#forName(java.lang.String)
     * @see Class#getMethod(java.lang.String, java.lang.Class[])
     * @see Method#invoke(java.lang.Object, java.lang.Object[])
     * @see Class#newInstance()
     */
    public static Object getInstance(String className, boolean failSilent) {
        if (className == null)
            return null;

        try {
            Class c = Class.forName(className);
            Method m = c.getMethod("getInstance", new Class[0]);
            return m.invoke(null, new Object[0]);
        } catch (Throwable e) {
            return createInstance(className, failSilent);
        }
    }

    /**
     * Creates and returns an instance of the specified class name using
     * {@code Class.newInstance()}. If {@code className} is {@code null}, then
     * this method returns a {@code null} reference. This dispatches to
     * {@code createInstance(String className, Class superType, boolean failSilent)}
     * with an argument of {@code null} for {@code superType} and {@code false}
     * for {@code failSilent}.
     * <p>
     * This method will attempt to resolve the {@code Class} of
     * {@code className} via {@code Class.forName(String className)}. No class
     * assignability checkes are performed because this method uses a
     * {@code null} {@code superType}.
     * <p>
     * Once the desired class has been resolved, a new instance of it is created
     * and returned by invoking its {@code newInstance()} method. If there are
     * any problems encountered during this process, the value of {@code false}
     * for {@code failSilent} will ensure the stack stack trace is be printed to
     * the {@code System.err}. A {@code null} reference will be returned if any
     * problems are encountered.
     *
     * @param className
     *            the fully qualified name of the desired class.
     * @return an instance of the specified class
     * @see #createInstance(String, Class, boolean)
     * @see Class#forName(java.lang.String)
     * @see Class#newInstance()
     */
    public static Object createInstance(String className) {
        return createInstance(className, null);
    }

    /**
     * Creates and returns an instance of the specified class name using
     * {@code Class.newInstance()}. If {@code className} is {@code null}, then
     * this method returns a {@code null} reference. The {@code failSilent}
     * parameter will determine whether error stack traces should be reported to
     * the {@code System.err} before this method returns {@code null}. This
     * method dispatches to
     * {@code createInstance(String className, Class superType, boolean failSilent)}
     * with an argument of {@code null} for {@code superType}.
     * <p>
     * This method will attempt to resolve the {@code Class} of
     * {@code className} via {@code Class.forName(String className)}. No class
     * assignability checkes are performed because this method uses a
     * {@code null} {@code superType}.
     * <p>
     * Once the desired class has been resolved, a new instance of it is created
     * and returned by invoking its {@code newInstance()} method. If there are
     * any problems encountered during this process, the value of
     * {@code failSilent} is checked to determine whether the stack stack trace
     * should be printed to the {@code System.err}. A {@code null} reference
     * will be returned if any problems are encountered.
     *
     * @param className
     *            the fully qualified name of the desired class.
     * @param failSilent
     *            {@code true} if the stack trace should <b>not</b> be printed
     *            to the {@code System.err} when a {@code Throwable} is caught,
     *            {@code false} otherwise.
     * @return an instance of the specified class
     * @see #createInstance(String, Class, boolean)
     * @see Class#forName(java.lang.String)
     * @see Class#newInstance()
     */
    public static Object createInstance(String className, boolean failSilent) {
        return createInstance(className, null, failSilent);
    }

    /**
     * Creates and returns an instance of the specified class name using
     * {@code Class.newInstance()}. If {@code className} is {@code null}, then
     * this method returns a {@code null} reference. If {@code superType} is
     * non-{@code null}, then this method will enforce polymorphic identity
     * via {@code Class.isAssignableFrom(Class cls)}. This method dispatches to
     * {@code createInstance(String className, Class superType, boolean failSilent)}
     * with an argument of {@code false} for {@code failSilent}.
     * <p>
     * This method will attempt to resolve the {@code Class} of
     * {@code className} via {@code Class.forName(String className)}. If
     * {@code superType} is non-{@code null}, then class identity is checked
     * by calling {@code superType.isAssignableFrom(c)} to ensure the resolved
     * class is an valid equivalent, descendent, or implementation of the
     * specified {@code className}. If this check fails, then a
     * {@code ClassCastException} is thrown and caught internally and this
     * method returns {@code null}. If {@code superType} is {@code null}, then
     * no assignability checks are performed on the resolved class.
     * <p>
     * Once the desired class has been resolved, a new instance of it is created
     * and returned by invoking its {@code newInstance()} method. If there are
     * any problems encountered during this process, the value of {@code false}
     * for {@code failSilent} will ensure the stack stack trace is be printed to
     * the {@code System.err}. A {@code null} reference will be returned if any
     * problems are encountered.
     *
     * @param className
     *            the fully qualified name of the desired class.
     * @param superType
     *            optional paramter used as a means of enforcing the inheritance
     *            hierarchy
     * @return an instance of the specified class
     * @see #createInstance(String, Class, boolean)
     * @see Class#forName(java.lang.String)
     * @see Class#isAssignableFrom(java.lang.Class)
     * @see Class#newInstance()
     */
    public static Object createInstance(String className, Class superType) {
        return createInstance(className, superType, false);
    }

    /**
     * Creates and returns an instance of the specified class name using
     * {@code Class.newInstance()}. If {@code className} is {@code null}, then
     * this method returns a {@code null} reference. If {@code superType} is
     * non-{@code null}, then this method will enforce polymorphic identity
     * via {@code Class.isAssignableFrom(Class cls)}. The {@code failSilent}
     * parameter will determine whether error stack traces should be reported to
     * the {@code System.err} before this method returns {@code null}.
     * <p>
     * This method will attempt to resolve the {@code Class} of
     * {@code className} via {@code Class.forName(String className)}. If
     * {@code superType} is non-{@code null}, then class identity is checked
     * by calling {@code superType.isAssignableFrom(c)} to ensure the resolved
     * class is an valid equivalent, descendent, or implementation of the
     * specified {@code className}. If this check fails, then a
     * {@code ClassCastException} is thrown and caught internally and this
     * method returns {@code null}. If {@code superType} is {@code null}, then
     * no assignability checks are performed on the resolved class.
     * <p>
     * Once the desired class has been resolved, a new instance of it is created
     * and returned by invoking its {@code newInstance()} method. If there are
     * any problems encountered during this process, the value of
     * {@code failSilent} is checked to determine whether the stack stack trace
     * should be printed to the {@code System.err}. A {@code null} reference
     * will be returned if any problems are encountered.
     *
     * @param className
     *            the fully qualified name of the desired class.
     * @param superType
     *            optional paramter used as a means of enforcing the inheritance
     *            hierarchy
     * @param failSilent
     *            {@code true} if the stack trace should <b>not</b> be printed
     *            to the {@code System.err} when a {@code Throwable} is caught,
     *            {@code false} otherwise.
     * @return an instance of the specified class
     * @see Class#forName(java.lang.String)
     * @see Class#isAssignableFrom(java.lang.Class)
     * @see Class#newInstance()
     */
    public static Object createInstance(String className, Class superType,
                                        boolean failSilent) {
        if (className == null)
            return null;

        try {
            Class c = Class.forName(className);
            if (superType != null && !superType.isAssignableFrom(c))
                throw new ClassCastException("'" + c.getName()
                                             + "' is not a type of " + superType + ".");
            return c.newInstance();
        } catch (Throwable e) {
            if (!failSilent)
                System.err.println("Exception: " +e.getMessage());
            return null;
        }
    }

    /**
     * Checks for equality between the two specified {@code Objects}. If both
     * arguments are the same {@code Object} reference using an {@code ==}
     * relationship, then this method returns {@code true}. Failing that check,
     * if either of the arguments is {@code null}, then the other must not be
     * and this method returns {@code false}. Finally, if both arguments are
     * non-{@code null} with different {@code Object} references, then this
     * method returns the value of {@code obj1.equals(obj2)}.
     * <p>
     * This method is the exact opposite of
     * {@code isChanged(Object oldObj, Object newObj)}.
     *
     * @param obj1
     *            the first {@code Object} to be checked for equality
     * @param obj2
     *            the second {@code Object} to be checked for equality
     * @return {@code true} if the {@code Objects} are equal, {@code false}
     *         otherwise.
     * @see #isChanged(Object, Object)
     * @see Object#equals(java.lang.Object)
     */
    public static boolean isEqual(Object obj1, Object obj2) {
        return !isChanged(obj1, obj2);
    }

    /**
     * Checks for inequality between the two specified {@code Objects}. If both
     * arguments are the same {@code Object} reference using an {@code ==}
     * relationship, then this method returns {@code false}. Failing that
     * check, if either of the arguments is {@code null}, then the other must
     * not be and this method returns {@code true}. Finally, if both arguments
     * are non-{@code null} with different {@code Object} references, then this
     * method returns the opposite value of {@code obj1.equals(obj2)}.
     * <p>
     * This method is the exact opposite of
     * {@code isEqual(Object obj1, Object obj2)}.
     *
     * @param oldObj
     *            the first {@code Object} to be checked for inequality
     * @param newObj
     *            the second {@code Object} to be checked for inequality
     * @return {@code false} if the {@code Objects} are equal, {@code true}
     *         otherwise.
     * @see #isEqual(Object, Object)
     * @see Object#equals(java.lang.Object)
     */
    public static boolean isChanged(Object oldObj, Object newObj) {
        if (oldObj == newObj)
            return false;

        if (oldObj == null || newObj == null)
            return true;

        return !oldObj.equals(newObj);
    }

    /**
     * Returns {@code true} if there is currently a {@code System} property with
     * the specified {@code key} whose value is "true". If the {@code System}
     * property does not exist, or the value is inequal to "true", this method
     * returns {@code false}. This method returns {@code false} if the
     * specified {@code key} parameter is {@code null}.
     *
     * @param key
     *            the {@code System} property to test.
     * @return {@code true} if there is currently a {@code System} property with
     *         the specified {@code key} whose value is "true".
     * @see System#getProperty(java.lang.String)
     * @see String#equals(java.lang.Object)
     * @deprecated Use {@link Boolean#getBoolean(String)}.
     */
    public static boolean sysTrue(String key) {
        String value = key == null ? null : System.getProperty(key);
        return value == null ? false : "true".equals(value);
    }

    /**
     * Puts the supplied {@code value} into the specified {@code Map} using the
     * specified {@code key}. This is a convenience method to automate
     * null-checks. A {@code value} parameter of {@code null} is interpreted as
     * a removal from the specified {@code Map} rather than an {@code put}
     * operation.
     * <p>
     * If either {@code map} or {@code key} are {@code null} then this method
     * returns with no action taken. If {@code value} is {@code null},
     * then this method calls {@code map.remove(key)}. Otherwise, this method
     * calls {@code map.put(key, value)}.
     *
     * @param map
     *            the {@code Map} whose contents is to be modified
     * @param key
     *            with which the specified value is to be associated.
     * @param value
     *            value to be associated with the specified key.
     * @see Map#put(java.lang.Object, java.lang.Object)
     * @see Map#remove(java.lang.Object)
     */
    public static void put(Map map, Object key, Object value) {
        if (map == null || key == null)
            return;

        if (value == null)
            map.remove(key);
        else
            map.put(key, value);
    }

    /**
     * Returns the value of the specified {@code fieldName} within the specified
     * {@code Object}. This is a convenience method for reflection hacks to
     * retrieve the value of protected, private, or package-private field
     * members while hiding the boilerplate reflection code within a single
     * utility method call. This method will return {@code true} if the
     * operation was successful and {@code false} if errors were encountered.
     * <p>
     * This method calls {@code obj.getClass()} to retrieve the {@code Class} of
     * the specified {@code Object}. It then retrieves the desired field by
     * calling the classes' {@code getDeclaredField(String name)} method,
     * passing the specified field name. If the field is deemed inaccessible via
     * it's {@code isAccessible()} method, then it is made accessible by calling
     * {@code setAccessible(true)}. The field value is set by invoking the
     * field's {@code set(Object obj, Object value)} method and passing the
     * original {@code Object} and {@code value} parameter as arguments. Before
     * returning, the field's accessibility is reset to its original state.
     * <p>
     * If either {@code obj} or {@code fieldName} are {@code null}, then this
     * method returns {@code false}.
     * <p>
     * It should be understood that this method will not function properly for
     * inaccessible fields in the presence of a {@code SecurityManager}. Nor
     * will it function properly for non-existent fields (if a field called
     * {@code fieldName} does not exist on the class). All {@code Throwables}
     * encountered by this method will be caught and eaten and the method will
     * return {@code false}. This works under the assumption that the operation
     * might likely fail because the method itself is, in reality, a convenience
     * hack. Therefore, specifics of any generated errors on the call stack are
     * discarded and only the final outcome ({@code true/false} of the
     * operation is deemed relevant. <b>If call stack data is required within
     * the application for any thrown exceptions, then this method should not be
     * used.}
     *
     * @param obj
     *            the object for which the represented field's value is to be
     *            modified
     * @param fieldName
     *            the name of the field to be set
     * @param value
     *            the new value for the field of {@code obj} being modified
     * @see Object#getClass()
     * @see Class#getDeclaredField(java.lang.String)
     * @see Field#isAccessible()
     * @see Field#setAccessible(boolean)
     * @see Field#set(Object, Object)
     */
    public static boolean setValue(Object obj, String fieldName, Object value) {
        if (obj == null || fieldName == null)
            return false;

        try {
            Class c = obj.getClass();
            Field field = c.getDeclaredField(fieldName);
            if (field.isAccessible()) {
                field.set(obj, value);
                return true;
            }

            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
            return true;
        } catch (Throwable t) {
            // don't report the error. the purpse of this method is to try to
            // access the field, but fail silently if we can't.
            return false;
        }
    }

    /**
     * Returns the value of the specified {@code fieldName} within the specified
     * {@code Object}. This is a convenience method for reflection hacks to
     * retrieve the value of protected, private, or package-private field
     * members while hiding the boilerplate reflection code within a single
     * utility method call.
     * <p>
     * This method calls {@code obj.getClass()} to retrieve the {@code Class} of
     * the specified {@code Object}. It then retrieves the desired field by
     * calling the classes' {@code getDeclaredField(String name)} method,
     * passing the specified field name. If the field is deemed inaccessible via
     * it's {@code isAccessible()} method, then it is made accessible by calling
     * {@code setAccessible(true)}. The return value is retrieved by invoking
     * the field's {@code get(Object obj)} method and passing the original
     * {@code Object} parameter as an argument. Before returning, the field's
     * accessibility is reset to its original state.
     * <p>
     * If either {@code obj} or {@code fieldName} are {@code null}, then this
     * method returns {@code null}.
     * <p>
     * It should be understood that this method will not function properly for
     * inaccessible fields in the presence of a {@code SecurityManager}. Nor
     * will it function properly for non-existent fields (if a field called
     * {@code fieldName} does not exist on the class). All {@code Throwables}
     * encountered by this method will be rethrown as
     * {@code IllegalAccessException}. For wrapped {@code Throwables}, the
     * original cause can be accessed via {@code IllegalAccessException's}
     * {@code getCause()} method.
     *
     * @param obj
     *            the object from which the represented field's value is to be
     *            extracted
     * @param fieldName
     *            the name of the field to be checked
     * @return the value of the represented field in object {@code obj};
     *         primitive values are wrapped in an appropriate object before
     *         being returned
     * @see Object#getClass()
     * @see Class#getDeclaredField(java.lang.String)
     * @see Field#isAccessible()
     * @see Field#setAccessible(boolean)
     * @see Field#get(java.lang.Object)
     * @see IllegalAccessException#getCause()
     */
    public static Object getValue(Object obj, String fieldName)
    throws IllegalAccessException {
        if (obj == null || fieldName == null)
            return null;

        try {
            Class c = obj.getClass();
            Field field = c.getDeclaredField(fieldName);
            if (field.isAccessible()) {
                return field.get(obj);
            }

            field.setAccessible(true);
            Object ret = field.get(obj);
            field.setAccessible(false);
            return ret;
        } catch (Throwable t) {
            if (t instanceof IllegalAccessException)
                throw (IllegalAccessException) t;

            IllegalAccessException e = new IllegalAccessException(t
                    .getMessage());
            e.initCause(t);
            throw e;
        }
    }

    /**
     * Puts the current {@code Thread} to sleep for the specified timeout. This
     * method calls {@code Thread.sleep(long millis)}, catching any thrown
     * {@code InterruptedException} and printing a stack trace to the
     * {@code System.err}.
     *
     * @param millis
     *            the length of time to sleep in milliseconds.
     * @see Thread#sleep(long)
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean isJavaVersion(String version) {
        if (version == null)
            return false;
        return System.getProperty("java.version").startsWith(version);
    }
}
