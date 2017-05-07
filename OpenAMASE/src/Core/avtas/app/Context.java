// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.app;

import avtas.xml.XMLUtil;
import avtas.util.ReflectionUtils;
import java.util.ArrayList;
import java.util.List;
import avtas.xml.Element;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * This is a container class for building any application that consists of
 * modular components that are designed to be aware of other components in the
 * application. Any object can be added to the application context, but objects
 * that implement the {@link ContextListener} interface will be informed of the
 * addition or removal of other objects from the context. <br/>
 *
 * @author AFRL/RQQD
 */
public class Context {

    /**
     * A list of ContextContainers (name, object) in this context.
     */
    private List<ContextContainer> childList = new ArrayList<>();

    /**
     * the data that this application is initialized with
     */
    private Element topNode = null;
    /**
     * the string array that initialized the context
     */
    private String[] args = new String[]{};
    // a map of registered Contexts
    static final HashMap<String, Context> registeredContexts = new HashMap<>();
    static Context defaultContext = null;

    /**
     * Creates a new instance of AppGlobals
     */
    public Context() {
    }

    // static methods
    /**
     * Returns the default application-wide context.
     */
    public static Context getDefaultContext() {
        if (defaultContext == null) {
            defaultContext = new Context();
        }
        return defaultContext;
    }

    /**
     * returns a context that is stored with the given tag. If no context with
     * the tag exists, then one is created and stored in the internal map.
     *
     * @param tag a reference string by which the context is stored
     * @return the context associated with the given tag.
     */
    public static Context getContext(String tag) {
        Context c = registeredContexts.get(tag);
        if (c == null) {
            c = new Context();
            registeredContexts.put(tag, c);
        }
        return c;
    }

    /**
     * @param tag a reference string by which the context is stored
     * @return true if there is an context registered with the given tag
     */
    public static boolean hasContext(String tag) {
        return registeredContexts.containsKey(tag);
    }

    /**
     * Adds a context to the list of registered contexts. This should be used
     * with caution, as a context with the given tag may already exist in the
     * map. If a context already exists, then the new context is NOT added and
     * the old one is returned. To explicitly remove an context, use the
     * {@link #removeContext(java.lang.String)} method.
     *
     * @param tag a reference string by which the context is stored
     * @param c context to store
     * @return the passed context if there is no current context with the given
     * tag in the map, or the current context if there is.
     */
    public static Context putContext(String tag, Context c) {
        Context oldC = registeredContexts.get(tag);
        if (oldC != null) {
            return oldC;
        }
        registeredContexts.put(tag, c);
        return c;
    }

    /**
     * Explicitly removes a context with the given tag. This should be used with
     * caution as there may be other application components that are using the
     * context.
     *
     * @param tag the context to remove from the internal map
     * @return the context removed, or null if there was none by the given tag.
     */
    public static Context removeContext(String tag) {
        return registeredContexts.remove(tag);
    }

    /**
     * Adds an object to this application context. The name of the object is
     * stored according to the object simple class name. If the object already
     * exists in this context, or is null, then it is not added.
     *
     * @param child object to be added to the context.
     *
     * @return true if this object is added, false otherwise.
     */
    public synchronized boolean addObject(Object child) {
        String key = String.valueOf(System.identityHashCode(child));
        return addObject(new ContextContainer(key, child, null));

    }

//    // instance methods below
//    /**
//     * Adds an object to this application context. The name of the object is
//     * stored according to the object simple class name. If the object already
//     * exists in this context, or is null, then it is not added.
//     *
//     * <h3>Note</h3>
//     * if the object is successfully added to the context, then the underlying
//     * XML configuration is updated to reflect the added object. Calls to
//     * {@link #getConfiguration()} will return an element that includes this
//     * object and the configuration data associated with it.
//     *
//     * @param child object to be added to the context.
//     * @param config XML data which is passed to the object, if it is a
//     * {@link ContextListener}. Can be null.
//     *
//     *
//     * @return true if this object is added, false otherwise.
//     */
//    public synchronized boolean addObject(Object child, Element config) {
//        String key = String.valueOf(System.identityHashCode(child));
//        return addObject(new ContextContainer(key, child, config), config);
//    }
    /**
     * Adds a ContextContainer to this context with the given name and object.
     * If the object already exists in this context, or is null, then it is not
     * added. If the object is a ContextListener, then
     * {@link ContextListener#addedToApplication(avtas.app.Context, avtas.xml.Element, java.lang.String[])}
     * is called. All other {@link ContextListener}s are are informed of this
     * object's addition through {@link ContextListener#applicationPeerAdded(java.lang.Object)
     * }.
     *
     * @param container a container class that has the simple name and the
     * object of a context
     *
     * @return true if this object is added, false if otherwise
     */
    protected synchronized boolean addObject(ContextContainer container) {

        // The container does not contain an object
        if (container.getObject() == null) {
            return false;
        }

        // if an object with this name is already stored, return false;
        for (ContextContainer cc : childList) {
            if (cc.getName().equals(container.getName())) {
                return false;
            }
        }

        for (ContextContainer cc : childList) {
            if (cc.getObject() instanceof ContextListener) {
                ((ContextListener) cc.getObject()).applicationPeerAdded(container.getObject());
            }
        }

        if (container.getObject() instanceof ContextListener) {

            ContextListener pi = (ContextListener) container.getObject();

            // a change from older versions of AMASE.  The XML that is passed is only the data contained
            // under the node from which the plugin was created.
            if (container.getXML() != null) {
                pi.addedToApplication(this, container.getXML(), args);
            }

            for (ContextContainer cc : childList) {
                pi.applicationPeerAdded(cc.getObject());
            }
        }

        childList.add(container);
        StatusPublisher.getDefault().setStatus("Loaded: " + container.getObject().getClass() + " into the Context");
        return true;
    }

    /**
     * Attempts to remove an object from this context. If the object exists in
     * this context, and is a ContextListener, then {@link ContextListener#requestShutdown()
     * }
     * and then {@link ContextListener#shutdown() } are called. All other
     * {@link ContextListener}s are are informed of this object's removal
     * through {@link ContextListener#applicationPeerRemoved(java.lang.Object)
     * }.
     *
     * @param child The object to remove.
     * @return true If the object is removed successfully
     */
    public synchronized boolean removeObject(Object child) {

        for (ContextContainer cc : childList) {
            if (cc.getObject() == child) {
                return removeObject(cc);
            }
        }
        return false;
    }

    /**
     * Attempts to remove an object from this context. If the object exists in
     * this context, and is a ContextListener, then {@link ContextListener#shutdown()
     * }
     * is called. All other {@link ContextListener}s are are informed of this
     * object's removal through
     * {@link ContextListener#applicationPeerRemoved(java.lang.Object) }.
     *
     * @param container The object to remove.
     * @return true If the object is removed successfully
     */
    protected synchronized boolean removeObject(ContextContainer container) {

        childList.remove(container);
        Object obj = container.getObject();

        if (obj == null) {
            return true;
        }

        for (ContextContainer cc : childList) {
            if (cc.getObject() instanceof ContextListener) {
                ((ContextListener) cc.getObject()).applicationPeerRemoved(container.getObject());
            }
        }
        if (container.getObject() instanceof ContextListener) {
            if (((ContextListener) obj).requestShutdown()) {
                ((ContextListener) obj).shutdown();
            }
        }
        return true;
    }

    /**
     * Gets an object with a given name from the ContextContainers in the
     * childList.
     *
     * @param name Name of the object to be retrieved.
     *
     * @return A child that was stored with the given name, or null if the given
     * name is not in a ContextContainer in the list.
     */
    public Object getObject(String name) {
        for (ContextContainer cc : childList) {
            if (cc.getName().equals(name)) {
                return cc.getObject();
            }
        }
        return null;    // name not found in the list
    }

    /**
     * Get an array of objects which are instances of the specified class.
     *
     * @param classname Name of the class.
     *
     * @return An array of children of the specified class. If no class is found
     * or a bad class name is passed, an empty array is returned.
     */
    public synchronized Object[] getObjects(String classname) {
        List<Object> v = new ArrayList<>();
        try {
            Class c = Class.forName(classname);
            for (ContextContainer cc : childList) {
                if (c.isInstance(cc.getObject())) {
                    v.add(cc.getObject());
                }
            }
        } catch (ClassNotFoundException ex) {
            return null;
        }
        return v.toArray();

    }

    /**
     * Get all objects registered in this application.
     *
     * @return An array of the objects registered in the application.
     */
    public synchronized Object[] getObjects() {

        List<Object> v = new ArrayList<>();
        for (ContextContainer cc : childList) {
            v.add(cc.getObject());
        }

        return v.toArray();
    }

    /**
     * Initializes all of the plugins and stores the setup data in the setupNode
     * object.
     *
     * For a plugin to load correctly, it must contain an entry named "Class"
     * with a valid class name. An object must have a valid class name.
     * <br> Example Input: <br>
     * <code>
     *  &ltPlugins&gt<br>
     * &ltPlugin Class="myapp.SomeClass" Name="Foo"/&gt<br>
     * &lt/Plugins&gt<br>
     * </code>
     *
     * @param node XML node with application-specific configuration data
     * @param args Array of command-line style arguments to pass to plugins
     */
    public void initialize(Element node, String[] args) {

        // set the args and setup node, so that children can be informed of the setup data
        if (args == null) {
            args = new String[]{};
        }
        this.args = args;
        this.topNode = node;

        // add objects listed in the element, under the "Context" Section
        List<Element> piNodes = XMLUtil.getChildren(node, "Plugin");

        for (Element tmp : piNodes) {
            addPlugin(tmp);
        }

        // now call initializeComplete for all children
        Object[] objs = getObjects();
        for (Object o : objs) {
            if (o instanceof ContextListener) {
                ((ContextListener) o).initializeComplete();
            }
        }

        StatusPublisher.getDefault().setStatus("Initialize Complete");
    }

    /**
     * Adds an object to this application context. The name of the object is
     * stored according to the object simple class name. If the object already
     * exists in this context, or is null, then it is not added.
     *
     * <h3>Note</h3>
     * if the object is successfully added to the context, then the underlying
     * XML configuration is updated to reflect the added object. Calls to
     * {@link #getConfiguration()} will return an element that includes this
     * object and the configuration data associated with it.
     */
    public void addPlugin(Element pluginDef) {
        String className = XMLUtil.getAttr(pluginDef, "Class", "");
        String name = XMLUtil.getAttr(pluginDef, "Name", "");
        if (!className.isEmpty()) {
            try {
                Object o = ReflectionUtils.createInstance(className);
                if (name.equals("")) {
                    name = String.valueOf(System.identityHashCode(o));
                }
                addObject(new ContextContainer(name, o, pluginDef));
            } catch (Exception ex) {
                UserExceptions.showWarning(this, "Could not create object from: " + className, null);
            }
        }

    }

    /**
     * Gets the top-level node which stores initialization data for the
     * application. This is updated to reflect the currently loaded plugins that
     * have been added through {@link #addPlugin(avtas.xml.Element) } or {@link #initialize(avtas.xml.Element, java.lang.String[])
     * }.
     *
     * @return an updated configuration node reflecting plugins and related
     * information.
     */
    public Element getConfiguration() {
        if (topNode == null) {
            topNode = new Element("Plugins");
        }

        for (ContextContainer c : childList) {
            if (c.getXML() != null && !topNode.contains(c.getXML())) {
                topNode.add(c.getXML());
            }
        }

        for (Element el : topNode.getChildElements()) {
            if (el.getName().equals("Plugin")) {
                boolean contained = false;
                for (ContextContainer c : childList) {
                    if (Objects.equals(c.getXML(), el)) {
                        contained = true;
                    }
                }
                if (!contained) {
                    topNode.remove(el);
                }
            }
        }

        return topNode;
    }

    /**
     * Shutsdown the application if allowed to by the plugins. First, the
     * plugins are asked if it is OK to shutdown using the 
     * {@link ContextListener#requestShutdown() } method. If all plugins respond
     * with true, then the {@link ContextListener#shutdown() } method is
     * invoked.
     * <br/>
     * If all plugins agree to shutdown, the runtime is shutdown with a signal
     * of (0).
     *
     * @return false if a plugin blocks the shutdown.
     */
    public boolean requestShutdown() {
        boolean ok = true;
        for (ContextContainer cc : childList) {
            if (cc.getObject() instanceof ContextListener) {
                ok = ok && ((ContextListener) cc.getObject()).requestShutdown();
            }
        }
        if (ok) {
            for (ContextContainer cc : childList) {
                if (cc.getObject() instanceof ContextListener) {
                    ((ContextListener) cc.getObject()).shutdown();
                }
            }
            Runtime.getRuntime().exit(0);
        }
        return ok;
    }

    /**
     *
     * @author AFRL/RQQD
     */
    protected static class ContextContainer {

        private final String name;
        private final Object object;
        private final Element xml;

        public ContextContainer(String name, Object object, Element xml) {
            this.name = name;
            this.object = object;
            this.xml = xml;
        }

        public String getName() {
            return name;
        }

        public Object getObject() {
            return object;
        }

        private Element getXML() {
            return xml;
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */