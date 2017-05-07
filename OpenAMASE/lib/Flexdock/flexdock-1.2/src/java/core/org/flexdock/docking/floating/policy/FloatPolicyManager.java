// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on May 31, 2005
 */
package org.flexdock.docking.floating.policy;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.drag.DragManager;
import org.flexdock.docking.event.DockingEvent;
import org.flexdock.docking.event.DockingListener;

/**
 * This class provides centralized control over the framework's floating
 * behavior. This includes global behavior and behavior local to each individual
 * docking operation.
 * <p>
 * This class contains a method {@code isGlobalFloatingEnabled()} that indicates
 * whether global floating support is enabled. If global floating support is
 * disabled, then the setting governs all docking operations and blocks floating
 * in a global sense. If global floating support is enabled, then floating is
 * allowed or disallowed on an individual operation-by-operation basis through a
 * set of {@code FloatPolicy} implementations.
 * <p>
 * The default setting for global floating support is {@code false}. This,
 * however, may be controlled by the system property {@code FLOATING_ALLOWED}.
 * If the framework starts up with a case-sensitive {@code String} value of
 * {@code "true"} for this system property, then global floating support will be
 * turned on by default. Otherwise, global floating support may be modified via
 * {@code setGlobalFloatingEnabled(boolean globalFloatingEnabled)}.
 * <p>
 * This class provides methods {@code addPolicy(FloatPolicy policy)} and
 * {@code removePolicy(FloatPolicy policy)}, allowing the user to implement
 * custom behavior to control floating support for individual docking operations
 * on an event-by-event basis. By default, the {@code FloatPolicyManager} has a
 * single {@code FloatPolicy} installed of type {@code DefaultFloatPolicy}.
 *
 * @author Christopher Butler
 */
public class FloatPolicyManager extends DockingListener.Stub {
    private static final FloatPolicyManager SINGLETON = new FloatPolicyManager();

    /**
     * Key constant used within the drag context {@code Map} to indicate whether
     * floating is allowed for a given drag operation.
     *
     * @see DragManager#getDragContext(Dockable)
     */
    public static final String FLOATING_ALLOWED = "FloatPolicyManager.FLOATING_ALLOWED";

    /**
     * System property key used during framework initialization to determine the
     * default setting for global floating support.
     */
    public static final String GLOBAL_FLOATING_ENABLED = "global.floating.enabled";

    private Vector policies;

    private boolean globalFloatingEnabled;

    /**
     * Returns a singleton instance of the {@code FloatPolicyManager} class.
     *
     * @return a singleton instance of the {@code FloatPolicyManager} class.
     */
    public static FloatPolicyManager getInstance() {
        return SINGLETON;
    }

    private FloatPolicyManager() {
        policies = new Vector();
        addPolicy(DefaultFloatPolicy.getInstance());
        globalFloatingEnabled = Boolean.getBoolean(GLOBAL_FLOATING_ENABLED);
    }

    /**
     * This method catches {@code DockingEvents} per the {@code DockingListener}
     * interface at the start of a drag operation and initializes floating
     * support within the the context {@code Map} of the drag operation. This
     * method retrieves the {@code Dockable} for the event via its
     * {@code getDockable()} method. It also retrieves the drag context
     * {@code Map} for the {@code DockingEvent} by invoking its
     * {@code getDragContext()} method. This map is the same {@code Map}
     * returned by {@code DragManager.getDragContext(Dockable dockable)}. It
     * then calls {@code isPolicyFloatingSupported(Dockable dockable)} for the
     * {@code Dockable} and places either {@code Boolean.TRUE} or
     * {@code Boolean.FALSE} within the drag context {@code Map}, caching the
     * value for use throughout the drag operation to avoid successive
     * iterations through the entire installed {@code FloatPolicy} collection.
     * The {@code Map}-key used is {@code FLOATING_ALLOWED}.
     *
     * @param evt
     *            the {@code DockingEvent} whose drag context is to be
     *            initialized for floating support
     * @see DockingEvent#getDragContext()
     * @see DockingEvent#getDockable()
     * @see #isPolicyFloatingSupported(Dockable)
     * @see #FLOATING_ALLOWED
     */
    public void dragStarted(DockingEvent evt) {
        Map context = evt.getDragContext();
        Dockable d = evt.getDockable();
        Boolean allowed = isPolicyFloatingSupported(d) ? Boolean.TRUE
                          : Boolean.FALSE;
        context.put(FLOATING_ALLOWED, allowed);
    }

    /**
     * This method catches {@code DockingEvents} per the {@code DockingListener}
     * interface at the end of a drag operation and determines whether or not to
     * block attempts to float within the docking operation.
     * <p>
     * If {@code evt.isOverWindow()} returns {@code true}, then the drop
     * operation is over an existing window and will be interpreted as an
     * attempt to dock within the window, not an attempt to float into a new
     * dialog. In this case, this method returns immediately with no action
     * taken.
     * <p>
     * This method calls {@code isFloatingAllowed(Dockable dockable)} using the
     * {@code DockingEvent's} {@code Dockable}, retrieved from
     * {@code getDockable()}. If this method returns {@code false}, then the
     * {@code DockingEvent} is consumed and this method returns.
     * <p>
     * If {@code isFloatingAllowed(Dockable dockable)} returns {@code true},
     * then the internal {@code FloatPolicy} collection is iterated through,
     * allowing each installed {@code FloatPolicy} to confirm the drop operation
     * via {@code isFloatDropAllowed(DockingEvent evt)}. If any of the
     * installed {@code FloatPolicies} returns {@code false} for
     * {@code isFloatDropAllowed(DockingEvent evt)}, then the
     * {@code DockingEvent} is consumed and the method exits.
     * <p>
     * If this method completes without the {@code DockingEvent} being consumed,
     * then the docking operation will proceed and attempts to float will be
     * allowed.
     *
     * @param evt
     *            the {@code DockingEvent} to be examined for floating support
     * @see DockingEvent#isOverWindow()
     * @see DockingEvent#getDockable()
     * @see DockingEvent#consume()
     * @see #isFloatingAllowed(Dockable)
     * @see FloatPolicy#isFloatDropAllowed(DockingEvent)
     */
    public void dropStarted(DockingEvent evt) {
        if (evt.isOverWindow())
            return;

        if (!isFloatingAllowed(evt.getDockable())) {
            evt.consume();
            return;
        }

        for (Iterator it = policies.iterator(); it.hasNext();) {
            FloatPolicy policy = (FloatPolicy) it.next();
            if (!policy.isFloatDropAllowed(evt)) {
                evt.consume();
                return;
            }
        }
    }

    /**
     * Indicates whether floating is allowed for the specified {@code Dockable}.
     * If {@code dockable} is {@code null}, this method returns {@code false}.
     * <p>
     * This method first calls
     * {@code DragManager.getDragContext(Dockable dockable)} to see if a drag
     * operation is in progress. If so, it returns the {@code boolean} value
     * contained within the drag context map using the key
     * {@code FLOATING_ALLOWED}. If no mapping exists for the specified key,
     * this method returns {@code false}.
     * <p>
     * If no drag operation is currently in progress and no drag context can be
     * found, this method dispatches to
     * {@code isPolicyFloatingSupported(Dockable dockable)}, which iterates
     * through all installed {@code FloatPolicies} to determine whether floating
     * support is allowed.
     *
     * @param dockable
     *            the {@code Dockable} whose floating support is to be checked
     * @return {@code true} if floating is allowed for the specified
     *         {@code Dockable}; {@code false} otherwise.
     * @see DragManager#getDragContext(Dockable)
     * @see #getInstance()
     * @see #isPolicyFloatingSupported(Dockable)
     * @see #FLOATING_ALLOWED
     */
    public static boolean isFloatingAllowed(Dockable dockable) {
        if (dockable == null)
            return false;

        Map context = DragManager.getDragContext(dockable);
        if (context == null)
            return getInstance().isPolicyFloatingSupported(dockable);

        Boolean floatAllowed = (Boolean) context.get(FLOATING_ALLOWED);
        return floatAllowed == null ? true : floatAllowed.booleanValue();
    }

    /**
     * Indicates whether floating is allowed for the specified {@code Dockable}
     * strictly by checking the installed {@code FloatPolicies}. If
     * {@code dockable} is {@code null}, this method returns {@code false}
     * immediately without checking through the installed {@code FloatPolicies}.
     * <p>
     * This method iterates through all installed {@code FloatPolicies} to
     * determine whether floating support is allowed. If any {@code FloatPolicy}
     * within the internal collection returns {@code false} from its
     * {@code isFloatingAllowed(Dockable dockable)} method, this method returns
     * {@code false}. Otherwise, this method returns {@code true}.
     *
     * @param dockable
     *            the {@code Dockable} whose floating support is to be checked
     * @return {@code true} if floating is allowed for the specified
     *         {@code Dockable}; {@code false} otherwise.
     * @see FloatPolicy#isFloatingAllowed(Dockable)
     */
    public boolean isPolicyFloatingSupported(Dockable dockable) {
        if (dockable == null)
            return false;

        for (Iterator it = policies.iterator(); it.hasNext();) {
            FloatPolicy policy = (FloatPolicy) it.next();
            if (!policy.isFloatingAllowed(dockable))
                return false;
        }
        return true;
    }

    /**
     * Adds the specified {@code FloatPolicy} to the internal policy collection.
     * This {@code FloatPolicy} will now take part in framework determinations
     * as to whether floating should be supported during docking operations. If
     * {@code policy} is {@code null}, no action is taken.
     *
     * @param policy
     *            the {@code FloatPolicy} to add to the system
     * @see #removePolicy(FloatPolicy)
     */
    public void addPolicy(FloatPolicy policy) {
        if (policy != null)
            policies.add(policy);
    }

    /**
     * Removes the specified {@code FloatPolicy} from the internal policy
     * collection. {@code FloatPolicy} will no longer take part in framework
     * determinations as to whether floating should be supported during docking
     * operations. If {@code policy} is {@code null} or was not previously
     * installed, no action is taken.
     *
     * @param policy
     *            the {@code FloatPolicy} to remove from the system
     * @see #addPolicy(FloatPolicy)
     */
    public void removePolicy(FloatPolicy policy) {
        if (policy != null)
            policies.remove(policy);
    }

    /**
     * Returns a global setting used to control default framework floating
     * behavior. If this method returns {@code false}, all floating support for
     * the entire framework is turned off. If this method returns {@code true},
     * then floating support for individual docking operations is deferred to
     * the installed {@code FloatPolicies}.
     *
     * @return {@code true} if global floating support is enabled; {@code false}
     *         otherwise.
     * @see #setGlobalFloatingEnabled(boolean)
     */
    public static boolean isGlobalFloatingEnabled() {
        return getInstance().globalFloatingEnabled;
    }

    /**
     * Sets the global setting used to control default framework floating
     * behavior. If {@code globalFloatingEnabled} is {@code false}, all
     * floating support for the entire framework is turned off. If
     * {@code globalFloatingEnabled} is {@code true}, then floating support for
     * individual docking operations is deferred to the installed
     * {@code FloatPolicies}.
     *
     * @param globalFloatingEnabled
     *            {@code true} if global floating support is to be enabled;
     *            {@code false} otherwise.
     * @see #isGlobalFloatingEnabled()
     */
    public static void setGlobalFloatingEnabled(boolean globalFloatingEnabled) {
        getInstance().globalFloatingEnabled = globalFloatingEnabled;
    }
}
