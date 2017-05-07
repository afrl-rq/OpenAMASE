// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.app;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A facility for filtering events based on type. The user registers an object
 * to send events to specific methods that are annotated with an
 * {@link EventFilter} annotation type.
 *
 * <h2>Receiving Events</h2>
 *
 * When an object (client) is registered with {@link EventSupport}, the class
 * tree is traversed, looking for methods that include the {@link EventFilter}
 * annotation. When an event is processed, the best-matching method for the
 * given type is used. For example, if an event of type "Car" is fired, and the
 * class type "Car" extends "Vehicle", then first a method for type "Car" is
 * requested, if it is not found, then a method with parameter type "Vehicle" is
 * requested. Then, the this searches for interfaces implemented by the object, in 
 * the order they are declared.  the final check is for a method with parameter type
 * of "Object". If that is not found, then no event is reported to
 * that client.<br/>
 * <br/>
 * Be careful when overriding methods from a base class. If a base class method
 * is annotated with {@link EventFilter} and you override it, then you must also
 * annotate the new method with {@link EventFilter} to receive events.<br/>
 * <br/>
 * Events are processed by invoking the {@link #processEvent(java.lang.Object) }
 * method.<br/>
 * <br/>
 * EventSupport can be added to an {@link EventListener} to directly receive
 * events.
 *
 * <h2>Example using EventSupport</h2>
 *
 * <pre>
 * class MyClass implements EventListener{
 *
 *     EventSupport eventSupport;
 *
 *     public MyClass() {
 *        this.eventSupport = new EventSupport(this);
 *     }
 *
 *     public void eventOccurred(Object event) {
 *          // the event support will dispatch the event to the most
 *          // appropriate method
 *          this.eventSupport.processEvent(event);
 *     }
 *
 *     &#064EventFilter
 *     public void stringEvent(String str) {
 *        // will receive strings
 *     }
 *
 *     &#064EventFilter
 *     public void sendMeNumbers(Number num) {
 *        // will receive any type of number (double, float, int, etc)
 *     }
 *
 *     &#064EventFilter
 *     public void everythingElse(Object obj) {
 *        // including a java.lang.Object method catches all other events
 *     }
 *
 * }
 * </pre>
 *
 * @author AFRL/RQQD
 */
public class EventSupport implements AppEventListener {

    private final Object target;
    private final Class classType;
    HashMap<Class, Method> methodMap = new HashMap<>();

    /**
     * Creates a new EventSupport object. This scans the target object for
     * EventFilters and adds the methods to the method call map.
     *
     * @param target Object that the EventSupport supports.
     */
    public EventSupport(Object target) {
        this.target = target;

        Objects.requireNonNull(target, "Cannot use a null target for Event Filtering.");

        this.classType = target.getClass();

        Class tmpClass = this.classType;

        while (tmpClass != null) {
            for (Method m : tmpClass.getDeclaredMethods()) {
                if (m.getAnnotation(EventFilter.class) != null) {
                    if ((m.getModifiers() & Modifier.PUBLIC) != 0) {
                        Class[] paramTypes = m.getParameterTypes();
                        if (paramTypes.length != 1) {
                            UserExceptions.showWarning("Error attempting to register an Event callback for "
                                    + classType.getName() + "#" + m.getName() + ".  Wrong number of parameters.");
                        }
                        if (!methodMap.containsKey(paramTypes[0])) {
                            methodMap.put(paramTypes[0], m);
                        }
                    }
                }

            }
            tmpClass = tmpClass.getSuperclass();
        }

    }

    /**
     * Returns the best match for a given object type. This traverses the
     * hierarchy of the class, looking for a registered method for the given
     * type.
     */
    public Method getMethodForObject(Object obj) {
        Method m = null;

        Class objClass = obj.getClass();
        while (objClass != Object.class) {
            m = methodMap.get(objClass);
            if (m != null) {
                return m;
            }
            objClass = objClass.getSuperclass();
        }

        // if we can't find a direct type, search by interface
        if (m == null) {
            Class[] ifaces = obj.getClass().getInterfaces();
            for (Class c : ifaces) {
                m = methodMap.get(c);
                if (m != null) {
                    return m;
                }
            }
        }
        
        // try to get a default "Object" call.  This might be null.
        m = methodMap.get(Object.class);
        
        return m;
    }

    public void processEvent(Object event) {
        if (event != null) {
            Method m = getMethodForObject(event);
            if (m != null) {
                try {
                    m.invoke(target, event);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(EventSupport.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void eventOccurred(Object event) {
        processEvent(event);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */