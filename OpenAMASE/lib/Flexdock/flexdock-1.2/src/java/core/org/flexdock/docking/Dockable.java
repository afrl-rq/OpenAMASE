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
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Set;

import org.flexdock.docking.event.DockingListener;
import org.flexdock.docking.event.DockingMonitor;
import org.flexdock.docking.props.DockablePropertySet;

/**
 * This interface is designed to specify the API's required by
 * {@code DockingManager} and {@code DockingPort} for dealing with dockable
 * components in a drag-n-drop fashion. A {@code Dockable} is the child
 * component that is docked into a {@code DockingPort}.
 *
 * @author Christopher Butler
 */
public interface Dockable extends DockingListener, DockingMonitor {

    /**
     * A constant property key to signify that a component is dockable.
     */
    String DOCKABLE_INDICATOR = "Dockable.DOCKABLE_INDICATOR";

    /**
     * Returns the value of the property with the specified key. Only properties
     * added with {@code putClientProperty} will return a non-{@code null}
     * value.
     *
     * @param key
     *            the key that is being queried
     * @return the value of this property or {@code null}
     * @see javax.swing.JComponent#getClientProperty(java.lang.Object)
     */
    Object getClientProperty(Object key);

    /**
     * Returns the Component that is to be dragged and docked. This may or may
     * not be included in the list returned by {@code getDragSources()}.
     * <p>
     * The framework performs indexing on the underlying {@code Component}.
     * Consequently, this method may <b>not</b> return a {@code null}
     * reference.
     *
     * @return the component wrapped by this dockable.
     */
    Component getComponent();

    /**
     * Returns the DockingPort within which this Dockable is currently docked.
     * If not currently docked, this method will return null.
     *
     * @return the docking port this dockable resides in, or {@code null} if the
     *         dockable is not currently docked (i.e. in the middle of a drag
     *         operation).
     */
    DockingPort getDockingPort();

    /**
     * Returns a {@code List} of the {@code Components} that are event sources
     * for drag operations. The list may or may not include the Component
     * returned by {@code getComponent()}.
     *
     * @return a list containing the components that may be used to drag this
     *         dockable.
     */
    List getDragSources();

    /**
     * Returns a {@code Set} of the {@code Components} that are used as frame
     * drag sources. When a {@code Dockable} is floated into an external frame,
     * that frame may or may not have a titlebar for repositioning. The
     * Components returned by this method will be setup with appropriate event
     * listeners such that dragging them will serve to reposition the containing
     * frame as if they were the frame titlebar. If a Component exists in both
     * the Set returned by this method and the List returned by
     * {@code getDragSources()}, the "frame reposition" behavior will supercede
     * any "drag-to-dock" behavior while the Dockable is in a floating state.
     *
     * @return a set containing the components that may be used to drag the
     *         frame this dockable resides in, if the dockable is floating.
     */
    Set getFrameDragSources();

    /**
     * Returns a {@code String} identifier that is unique within a JVM instance,
     * but persistent across JVM instances. This is used for configuration
     * mangement, allowing the JVM to recognize a {@code Dockable} instance
     * within an application instance, persist the ID, and recall it in later
     * application instances. The ID should be unique within an appliation
     * instance so that there are no collisions with other {@code Dockable}
     * instances, but it should also be consistent from JVM to JVM so that the
     * association between a {@code Dockable} instance and its ID can be
     * remembered from session to session.
     * <p>
     * The framework performs indexing on the persistent ID. Consequently, this
     * method may <b>not</b> return a {@code null} reference.
     *
     * @return the persistence id for this dockable. This id ensures that only
     *         one copy of a given dockable will exist.
     */
    String getPersistentId();

    /**
     * Adds an arbitrary key/value "client property" to this {@code Dockable}.
     * {@code null} values are allowed.
     *
     * @param key
     *            the new client property key.
     * @param value
     *            the new client property value; if <code>null</code> this
     *            method will remove the property.
     * @see javax.swing.JComponent#putClientProperty(java.lang.Object,
     *      java.lang.Object)
     */
    void putClientProperty(Object key, Object value);

    /**
     * Returns a {@code DockablePropertySet} instance associated with this
     * {@code Dockable}. Developers implementing the {@code Dockable} interface
     * may or may not choose to provide their own {@code DockablePropertySet}
     * implementation for use with this method. A default implementation is
     * supplied by the framework and most {@code Dockable} implementations,
     * including all implementations provided by the framework, will return the
     * default {@code DockablePropertySet} via a call to
     * {@code org.flexdock.docking.props.PropertyManager}. Developers are
     * encouraged to take advantage of this by calling
     * {@code PropertyManager.getDockablePropertySet(this)}.
     *
     * @return the {@code DockablePropertySet} associated with this
     *         {@code Dockable} This method may not return a {@code null}
     *         reference.
     * @see org.flexdock.docking.props.DockablePropertySet
     * @see org.flexdock.docking.props.PropertyManager#getDockablePropertySet(Dockable)
     */
    DockablePropertySet getDockingProperties();

    /**
     * Implements the semantics for docking an external {@code Dockable} to this
     * {@code Dockable} and returns a {@code boolean} indicating whether or not
     * the docking operation was successful. <p> The framework already
     * provides a default implementation for this method through
     * {@code DockingManager.dock(Dockable dockable, Dockable parent)}. While
     * users are free to provide their own implementation for this method, the
     * recommended approach is to use the default implementation with the
     * following line: <p> {@code return DockingManager.dock(dockable, this);}
     *
     * @param dockable
     *            the {@code Dockable} to dock relative to this {@code Dockable}
     * @return {@code true} if the docking operation was successful;
     *         {@code false} otherwise.
     * @see #dock(Dockable, String)
     * @see #dock(Dockable, String, float)
     * @see DockingManager#dock(Dockable, Dockable)
     */
    boolean dock(Dockable dockable);

    /**
     * Implements the semantics for docking an external {@code Dockable} to the
     * specified region of this {@code Dockable} and returns a {@code boolean}
     * indicating whether or not the docking operation was successful. If the
     * docking operation results in a split layout, this method should determine
     * an appropriate ratio of available space to allot to the new sibling
     * {@code Dockable}. <p> The framework already provides a default
     * implementation for this method through
     * {@code DockingManager.dock(Dockable dockable, Dockable parent, String region)}.
     * While users are free to provide their own implementation for this method,
     * the recommended approach is to use the default implementation with the
     * following line: <p>
     * {@code return DockingManager.dock(dockable, this, relativeRegion);}
     *
     * @param dockable
     *            the {@code Dockable} to dock relative to this {@code Dockable}
     * @param relativeRegion
     *            the docking region into which to dock the specified
     *            {@code Dockable}
     * @return {@code true} if the docking operation was successful;
     *         {@code false} otherwise.
     * @see #dock(Dockable, String, float)
     * @see DockingManager#dock(Dockable, Dockable, String)
     */
    boolean dock(Dockable dockable, String relativeRegion);

    /**
     * Implements the semantics for docking an external {@code Dockable} to the
     * specified region of this {@code Dockable} with the specified layout
     * ratio, returning a {@code boolean} indicating whether or not the docking
     * operation was successful. If the docking operation results in a split
     * layout, this method should use the specified {@code ratio} to determine
     * the amount of available space to allot to the new sibling
     * {@code Dockable}.
     * <p>
     * The framework already provides a default implementation for this method
     * through
     * {@code DockingManager.dock(Dockable dockable, Dockable parent, String region, float proportion)}.
     * While users are free to provide their own implementation for this method,
     * the recommended approach is to use the default implementation with the
     * following line:
     * <p>
     * {@code return DockingManager.dock(dockable, this, relativeRegion, ratio);}
     *
     * @param dockable
     *            the {@code Dockable} to dock relative to this {@code Dockable}
     * @param relativeRegion
     *            the docking region into which to dock the specified
     *            {@code Dockable}
     * @param ratio
     *            the proportion of available space in the resulting layout to
     *            allot to the new sibling {@code Dockable}.
     * @return {@code true} if the docking operation was successful;
     *         {@code false} otherwise.
     * @see DockingManager#dock(Dockable, Dockable, String, float)
     */
    boolean dock(Dockable dockable, String relativeRegion, float ratio);

    /**
     * Adds a PropertyChangeListener to the listener list. The listener is
     * registered for all bound properties of this class. Note that if this
     * Dockable is inheriting a bound property, then no event will be fired in
     * response to a change in the inherited property.
     * <p>
     * If listener is null, no exception is thrown and no action is performed.
     *
     * @param listener
     *            the PropertyChangeListener to be added
     *
     * @see #removePropertyChangeListener(PropertyChangeListener)
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Removes a PropertyChangeListener from the listener list. This method
     * should be used to remove PropertyChangeListeners that were registered for
     * all bound properties of this class.
     * <p>
     * If listener is null, no exception is thrown and no action is performed.
     *
     * @param listener
     *            the PropertyChangeListener to be removed
     *
     * @see #addPropertyChangeListener
     */
    void removePropertyChangeListener(PropertyChangeListener listener);
}
