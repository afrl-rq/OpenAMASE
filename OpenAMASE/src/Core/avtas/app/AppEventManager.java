// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.app;

import avtas.amase.AmasePlugin;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The class which controls the firing of application events within an
 * application. It keeps a list of all registered AppEventListeners and ensures
 * that all objects in the list are informed when a new application event occurs
 * and that application events are fired in the right order.<br/>
 * 
 * @author AFRL/RQQD
 */
public class AppEventManager extends AmasePlugin {

    /**
     * list of AppEventListeners registered to this manager
     */
    final List<AppEventListener> listeners = new ArrayList<AppEventListener>();
    /**
     * flag to denote that the Event Manager is in the process of executing an
     * event.
     */
    boolean eventOccuring = false;
    /**
     * holding area for events that are queued waiting for another event to
     * finish
     */
    ArrayDeque<EventWrapper> waitList = new ArrayDeque<EventWrapper>();
    /**
     * a bit that controls event dispatch
     */
    boolean lock = false;
    Object consume = null;
    static final HashMap<String, AppEventManager> registeredManagers = new HashMap<String, AppEventManager>();
    static AppEventManager defaultManager = null;

    /**
     * Creates a new instance of AppEventManager
     */
    public AppEventManager() {
        //eventThread.start();
    }

    /**
     * Returns the default event manager.
     */
    public static AppEventManager getDefaultEventManager() {
        if (defaultManager == null) {
            defaultManager = new AppEventManager();
        }
        return defaultManager;
    }

    /**
     * returns an event manager that is stored with the given tag. If no event
     * manager with the tag exists, then one is created and stored in the
     * internal map.
     *
     * @param tag a reference string by which the event manager is stored
     * @return the event manager associated with the given tag.
     */
    public static AppEventManager getEventManager(String tag) {
        AppEventManager mgr = registeredManagers.get(tag);
        if (mgr == null) {
            mgr = new AppEventManager();
            registeredManagers.put(tag, mgr);
        }
        return mgr;
    }

    /**
     * @param tag a reference string by which the event manager is stored
     * @return true if there is an event manager registered with the given tag
     */
    public static boolean hasEventManager(String tag) {
        return registeredManagers.containsKey(tag);
    }

    /**
     * Adds an event manager to the list of registered managers. This should be
     * used with caution, as an event manager with the given tag may already
     * exist in the map. If an event manager already exists, then the new event
     * manager is NOT added and the old one is returned. To explicitly remove an
     * event manager, use the {@link #removeEventManager(java.lang.String) }
     * method.
     *
     * @param tag a reference string by which the event manager is stored
     * @param mgr the manager to store
     * @return the passed event manager if there is no current manager with the
     * given tag in the map, or the current event manager if there is.
     */
    public static AppEventManager putEventManager(String tag, AppEventManager mgr) {
        AppEventManager oldMgr = registeredManagers.get(tag);
        if (oldMgr != null) {
            return oldMgr;
        }
        registeredManagers.put(tag, mgr);
        return mgr;
    }

    /**
     * Explicitly removes an event manager with the given tag. This should be
     * used with caution as there may be other application components that are
     * using the event manager.
     *
     * @param tag the event manager to remove from the internal map
     * @return the event manager removed, or null if there was none by the given
     * tag.
     */
    public static AppEventManager removeEventManager(String tag) {
        return registeredManagers.remove(tag);
    }

    /**
     * Fires an event to all registered listeners. It does not pass the event
     * back to the source of the event. All events are queued and dispatched
     * after any previous events have finished.
     *
     * @param evt The event to fire.
     * @param eventSource source object that created the event. May be null.
     * When the event is fired, it is not passed to an object that equals (==)
     * the source object.
     */
    public void fireEvent(Object evt, Object eventSource) {
        EventWrapper wrapper = new EventWrapper(evt, eventSource);
        synchronized(waitList) {
            waitList.addLast(wrapper);
        }
        dispatchObjects();
    }

    /**
     * Fires an event to all registered listeners. All events are queued and
     * dispatched after any previous events have finished.
     *
     * @param evt The event to fire.
     */
    public void fireEvent(Object evt) {
        EventWrapper wrapper = new EventWrapper(evt, null);
        synchronized(waitList) {
            waitList.addLast(wrapper);
        }
        dispatchObjects();
    }

    /**
     * Fires an AppEvent immediately to all registered listeners. It does not
     * pass the event back to the source of the event. This method does not
     * queue messages to be sent in the order that they are received.
     *
     * @param evt The event to fire.
     * @param eventSource source object that created the event. May be null.
     * When the event is fired, it is not passed to an object that equals (==)
     * the source object.
     */
    public void fireEventNow(Object evt, Object eventSource) {
        synchronized(listeners) {
            for (AppEventListener l : listeners) {
                if (l != eventSource) {
                    l.eventOccurred(evt);
                }
            }
        }
    }

    /**
     * Stops event dispatch until an unlock() call is made.
     */
    public void lock() {
        lock = true;
    }

    /**
     * Resumes event dispatch.
     */
    public void unlock() {
        lock = false;
        dispatchObjects();
    }

    /**
     * Returns the value of
     * <code>lock</code>.
     *
     * @return true if the event manager is currently blocked from dispatching
     * events.
     */
    public boolean isLocked() {
        return lock;
    }

    /**
     * Adds an
     * <code>AppEventListener</code> to the listener list. This method calls
     * <code>setEventManager</code> on the client in order to register this
     * manager as the client's DataManager.
     *
     * @param l The AppEventListener to be added.
     */
    public boolean addListener(AppEventListener l) {
        synchronized(listeners) {
            if (!listeners.contains(l)) {
                listeners.add(l);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the listener from the listener list, if it is on the list. If the
     * listener is removed, the
     * <code>setDataManager</code> method is called with "null" as the
     * DataManager.
     *
     * @param l The listener to remove.
     * @return true if the listener was removed, false otherwise.
     */
    public boolean removeListener(AppEventListener l) {
        synchronized(listeners) {
            return listeners.remove(l);
        }
    }

    /**
     * Returns a list of all registered Listeners
     *
     * @return The list of registered event listeners
     */
    public List<AppEventListener> getListeners() {
        //cpw: For now return a shallow copy of the list, user shouldn't modify elements of the container!
        //TODO: Return a deep copy of the list here.
        return new ArrayList<AppEventListener>(listeners);
    }

    /**
     * Routine that sends out the events. This method insures the proper order
     * of event firing by dispatching events from the front of the queue while
     * events are added to the back. Event dispatch is prevented while
     * <code>lock</code> is true.
     */
    private void dispatchObjects() {
        // Separate synchronization on "this" to allow early return from this function and proper protection of
        // "eventOccuring" primitive type (cannot synchronize on these).
        synchronized(this) {
            if (eventOccuring) {
                return;
            }
            eventOccuring = true;
        }
        while (!lock) {
            synchronized(waitList) {
                EventWrapper e = waitList.poll();
                if (e == null) {
                    break;
                }
                synchronized(listeners) {
                    for (int i = 0; i < listeners.size(); i++) {
                        if (consume == e.event) {
                            consume = null;
                            break;
                        }
                        AppEventListener l = listeners.get(i);
                        if (l != e.source) {
                            l.eventOccurred(e.event);
                        }
                    }
                }
            }
        }
        consume = null;
        synchronized(this) {
            eventOccuring = false;
        }
    }

    /**
     * Tells the event manager to stop dispatching the current event. This only
     * works when called during the dispatch of an event. If this is invoked,
     * the current event is not passed to any other listeners.
     */
    public void consume(Object event) {
        if (eventOccuring) {
            consume = event;
        }
    }

    @Override
    public void applicationPeerAdded(Object peer) {
        if (peer instanceof AppEventListener) {
            AppEventListener l = (AppEventListener) peer;
            addListener(l);
        }
    }

    @Override
    public void applicationPeerRemoved(Object peer) {
        AppEventListener l = (AppEventListener) peer;
        removeListener(l);
    }


    public static class EventWrapper {

        public Object event = null;
        public Object source = null;

        public EventWrapper(Object event, Object eventSource) {
            this.event = event;
            this.source = eventSource;
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */