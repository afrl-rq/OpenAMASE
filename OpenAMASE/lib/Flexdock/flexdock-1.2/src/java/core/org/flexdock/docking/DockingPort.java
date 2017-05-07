// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Copyright (c) 2004 Christopher M Butler
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.flexdock.docking;

import java.awt.Component;
import java.awt.Point;
import java.util.Set;

import org.flexdock.docking.event.DockingListener;
import org.flexdock.docking.event.DockingMonitor;
import org.flexdock.docking.props.DockingPortPropertySet;
import org.flexdock.docking.state.LayoutNode;

/**
 * This interface is designed to specify the API's required by
 * {@code DockingManager} for placing {@code Dockable} instances within a
 * container. A {@code DockingPort} is the parent container inside of which
 * {@code Dockable} instances may be placed.
 *
 * @author Chris Butler
 */
public interface DockingPort extends DockingListener, DockingMonitor {
    String INITIAL_TAB_POSITION = "DockingPort.init.tab.position";

    /**
     * Returns a boolean indicating whether or not docking is allowed within the
     * specified region. Used by {@code DockingManager} during drag operations.
     */
    boolean isDockingAllowed(Component comp, String region);

    /**
     * Removes all docked components from the {@code DockingPort}.
     */
    void clear();

    /**
     * Docks the specified Dockable in the specified region. The
     * {@code Dockable's} {@code getDockable()} component is used as the docking
     * component.
     */
    boolean dock(Dockable dockable, String region);

    /**
     * Docks the specified Component in the specified region. Returns
     * {@code true} for success and {@code false} for failure.
     */
    boolean dock(Component comp, String region);

    /**
     * Returns a reference to the currently docked component.
     */
    Component getDockedComponent();

    /**
     * Returns a reference to Dockable currently docked in the target region.
     * Returns null if there is no Dockable there. If a tabbed layout is
     * present, this method will return the Dockable in the currently selected
     * tab.
     */
    Dockable getDockable(String region);

    /**
     * Returns a reference to Component currently docked in the target region.
     * Returns null if there is no Component there. If a tabbed layout is
     * present, this method will return the Component in the currently selected
     * tab.
     */
    Component getComponent(String region);

    /**
     * Returns a {@code String} identifier that is unique within a JVM instance,
     * but persistent across JVM instances. This is used for configuration
     * mangement, allowing the JVM to recognize a {@code DockingPort} instance
     * within an application instance, persist the ID, and recall it in later
     * application instances. The ID should be unique within an appliation
     * instance so that there are no collisions with other {@code DockingPort}
     * instances, but it should also be consistent from JVM to JVM so that the
     * association between a {@code DockingPort} instance and its ID can be
     * remembered from session to session.
     */
    String getPersistentId();

    /**
     * Sets the persistent ID String to be returned by {@code getPersistentId()}.
     *
     * @param id
     *            the persistent ID to be applied.
     * @see #getPersistentId()
     */
    void setPersistentId(String id);

    /**
     * Indicates whether or not the specified component is a child component
     * docked within the {@code DockingPort}.
     */
    boolean isParentDockingPort(Component comp);

    /**
     * Removes the specified Component in from the {@code DockingPort}. Returns
     * {@code true} for success and {@code false} for failure.
     */
    boolean undock(Component comp);

    /**
     * Returns the region of this {@code DockingPort} containing the coordinates
     * within the specified {@code Point}. The return value will be one of the
     * regions specified in {@code org.flexdock.util.DockingConstants},
     * including {@code CENTER_REGION}, {@code NORTH_REGION},
     * {@code SOUTH_REGION}, {@code EAST_REGION}, {@code WEST_REGION}, or
     * {@code UNKNOWN_REGION}.
     *
     * @return the region containing the specified {@code Point}.
     */
    String getRegion(Point p);

    /**
     * Returns the value of the property with the specified key. Only properties
     * added with {@code putClientProperty} will return a non-{@code null}
     * value.
     *
     * @param key
     *            the being queried
     * @return the value of this property or {@code null}
     * @see javax.swing.JComponent#getClientProperty(java.lang.Object)
     */
    Object getClientProperty(Object key);

    /**
     * Adds an arbitrary key/value "client property" to this {@code DockingPort}.
     * {@code null} values are allowed.
     *
     * @see javax.swing.JComponent#putClientProperty(java.lang.Object,
     *      java.lang.Object)
     */
    void putClientProperty(Object key, Object value);

    /**
     * Returns a {@code DockingPortPropertySet} instance associated with this
     * {@code DockingPort}. Developers implementing the {@code DockingPort}
     * interface may or may not choose to provide their own
     * {@code DockingPortPropertySet} implementation for use with this method. A
     * default implementation is supplied by the framework and most
     * {@code DockingPort} implementations, including all implementations
     * provided by the framework, will return the default
     * {@code DockingPortPropertySet} via a call to
     * {@code org.flexdock.docking.props.PropertyManager}. Developers are
     * encouraged to take advantage of this by calling
     * {@code PropertyManager.getDockingPortPropertySet(this)}.
     *
     * @return the {@code DockingPortPropertySet} associated with this
     *         {@code DockingPort} This method may not return a {@code null}
     *         reference.
     * @see org.flexdock.docking.props.DockingPortPropertySet
     * @see org.flexdock.docking.props.PropertyManager#getDockingPortPropertySet(DockingPort)
     */
    DockingPortPropertySet getDockingProperties();

    /**
     * Returns the {@code DockingStrategy} instance used by this
     * {@code DockingPort} for docking operations.
     *
     * @see DockingStrategy
     */
    DockingStrategy getDockingStrategy();

    /**
     * Returns a {@code Set} of all {@code Dockables} presently contained by
     * this {@code DockingPort}.
     *
     * @return a {@code Set} of {@code Dockables} contained by this
     *         {@code DockingPort}. If the {@code DockingPort} contians no
     *         {@code Dockables}, and empty {@code Set} is returned. This
     *         method may not return a {@code null} reference.
     */
    Set getDockables();

    /**
     * Returns a boolean indicating whether or not this {@code DockingPort} is
     * nested within another {@code DockingPort}. If there are no other
     * {@code DockingPorts} within this {@code DockingPort's} container ancestor
     * hierarchy, then this method will return {@code true}. Otherwise, this
     * method will return {@code false}. If the this {@code DockingPort} is not
     * validated and/or is not part of a container hierarchy, this method should
     * return {@code true}.
     */
    boolean isRoot();

    /**
     * Examines a {@code LayoutNode} and constructs a corresponding component
     * hierarchy to match the specified layout. The supplied {@code LayoutNode}
     * will contain metadata describing a layout of {@code Dockables},
     * including relative sizes, split proportions, tabbing sequences, etc. This
     * {@code DockingPort} is reponsible for constructing a valid
     * {@code Dockable} component layout based upon the metadata contained
     * within the supplied {@code LayoutNode}
     *
     * @param node
     *            the {@code LayoutNode} describing the layout to construct
     * @see org.flexdock.docking.state.LayoutNode
     * @see #exportLayout()
     */
    void importLayout(LayoutNode node);

    /**
     * Returns a {@code LayoutNode} containing metadata that describes the
     * current layout contained within this {@code DockingPort}. The returned
     * {@code LayoutNode} should be structured such that a subsequent call to
     * {@code importLayout()} on the same {@code DockingPort} should construct a
     * visual component layout identical to that which currently exists in this
     * {@code DockingPort}
     *
     * @return a {@code LayoutNode} representing the current layout state within
     *         this {@code DockingPort}
     * @see org.flexdock.docking.state.LayoutNode
     * @see #importLayout(LayoutNode)
     */
    LayoutNode exportLayout();

    // --- Maximization

    /**
     * Asks this {@code DockingPort} to temporarily release its child
     * {@code Dockable} for use by another {@code DockingPort} to achieve
     * maximization. This method is called by {@code DockingManager} in the
     * course of maximizing a {@code Dockable}. Client code should not call
     * this method directly.
     * <p>
     * This {@code DockingPort} is expected to remove the specified dockable's
     * component from its swing container hierarchy. Also, this
     * {@code DockingPort} is expected to internally store enough information to
     * restore its current state after a subsequent call to
     * {@link #returnFromMaximization()}.
     *
     * @param dockable
     *            the {@code Dockable} that is requested to be maximized
     * @see DockingManager#toggleMaximized(Component)
     * @see DockingManager#toggleMaximized(Dockable)
     */
    void releaseForMaximization(Dockable dockable);

    /**
     * Notifies this {@code DockingPort} that the {@code Dockable} previously
     * released for maximization via a call to
     * {@link #releaseForMaximization(Dockable)} is now ready to be returned to
     * its original state inside this {@code DockingPort}. This method is
     * called by {@code DockingManager} in the course of restoring a maximized
     * {@code Dockable}. Client code should not call this method directly.
     *
     * @see DockingManager#toggleMaximized(Component)
     * @see DockingManager#toggleMaximized(Dockable)
     */
    void returnFromMaximization();

    /**
     * Asks this {@code DockingPort} to temporarily install the specified
     * {@code Dockable} and maximize its component. This method is called by
     * {@code DockingManager} in the course of maximizing a {@code Dockable}.
     * Client code should not call this method directly.
     * <p>
     * This {@code DockingPort} is expected to display the specified dockable's
     * component such that it occupies all (or the majority) of its screen
     * resources. Also, this {@code DockingPort} is expected to internally store
     * enough information to restore its current state after a subsequent call
     * to {@link #uninstallMaximizedDockable()}.
     *
     * @param dockable
     *            the {@code Dockable} that is requested to be maximized
     * @see DockingManager#toggleMaximized(Component)
     * @see DockingManager#toggleMaximized(Dockable)
     */
    void installMaximizedDockable(Dockable dockable);

    /**
     * Notifies this {@code DockingPort} that the {@code Dockable} previously
     * installed for maximization via a call to
     * {@link #installMaximizedDockable(Dockable)} should now be returned to its
     * original {@code DockingPort} and that this {@code DockingPort} should
     * return to its original state from before the call to
     * {@link #installMaximizedDockable(Dockable)}. This method is called by
     * {@code DockingManager} in the course of restoring a maximized
     * {@code Dockable}. Client code should not call this method directly.
     * <p>
     * This {@code DockingPort} is expected to remove the maximized dockable's
     * component from its swing container hierarchy.
     *
     * @see DockingManager#toggleMaximized(Component)
     * @see DockingManager#toggleMaximized(Dockable)
     */
    void uninstallMaximizedDockable();
}
