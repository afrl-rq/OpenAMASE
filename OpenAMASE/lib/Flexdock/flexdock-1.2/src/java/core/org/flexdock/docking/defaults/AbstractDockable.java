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
package org.flexdock.docking.defaults;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.event.DockingEvent;
import org.flexdock.docking.event.DockingListener;
import org.flexdock.docking.props.DockablePropertySet;
import org.flexdock.docking.props.PropertyManager;
import org.flexdock.util.SwingUtility;
import org.flexdock.util.Utilities;

/**
 * Provides a default implementation of the {@code Dockable} interface. This
 * class should be extended by any application that wishes to make use of the
 * {@code Dockable} interface without the need for writing out an implementation
 * for every method that isn't explicitly used.
 *
 * @author Christopher Butler
 */
public abstract class AbstractDockable implements Dockable {
    private String persistentId;

    private ArrayList dockingListeners;

    private ArrayList dragListeners;

    private Hashtable clientProperties;

    private HashSet frameDragSources;

    /**
     * Creates a new {@code AbstractDockable} instance. This constructor is
     * meant to be invoked by subclasses as it initializes the
     * {@code Dockable's} persistent ID and drag sources.
     *
     * @param id
     *            the persistent ID of the resulting {@code Dockable}
     * @see Dockable#getPersistentId()
     */
    public AbstractDockable(String id) {
        persistentId = id;
        dockingListeners = new ArrayList(2);
        dragListeners = new ArrayList();
        clientProperties = new Hashtable(2);

        dragListeners.add(getComponent());
    }

    /**
     * Returns the {@code Component} used to back this {@code Dockable}
     * instance.
     *
     * @return the {@code Component} used to back this {@code Dockable}
     *         instance.
     * @see Dockable#getComponent()
     */
    public abstract Component getComponent();

    /**
     * Returns a {@code List} of {@code Components} used to initiate
     * drag-to-dock operation. By default, the returned {@code List} contains
     * the {@code Component} returned by {@code getComponent()}.
     *
     * @return a {@code List} of {@code Components} used to initiate
     *         drag-to-dock operation.
     * @see Dockable#getDragSources()
     * @see #getComponent()
     */
    public List getDragSources() {
        return dragListeners;
    }

    /**
     * Returns the persistent ID of this {@code Dockable} instance provided when
     * this object was instantiated.
     *
     * @return the persistent ID of this {@code Dockable}
     * @see Dockable#getPersistentId()
     * @see #AbstractDockable(String)
     */
    public String getPersistentId() {
        return persistentId;
    }

    /**
     * Returns a {@code HashSet} of {@code Components} used as frame drag
     * sources when this {@code Dockable} is floating in a non-decorated
     * external dialog. The {@code HashSet} returned by this method is initially
     * empty. Because it is mutable, however, new {@code Components} may be
     * added to it.
     *
     * @return a {@code HashSet} of {@code Components} used as frame drag
     *         sources when this {@code Dockable} is floating in a non-decorated
     *         external dialog.
     * @see Dockable#getFrameDragSources()
     */
    public Set getFrameDragSources() {
        if (frameDragSources == null)
            frameDragSources = new HashSet();
        return frameDragSources;
    }

    /**
     * Sets the {@code String} to be used for tab labels when this
     * {@code Dockable} is embedded within a tabbed layout. {@code null} values
     * are discouraged, but not illegal.
     *
     * @param tabText
     *            the {@code String} to be used for tab labels when this
     *            {@code Dockable} is embedded within a tabbed layout.
     */
    public void setTabText(String tabText) {
        getDockingProperties().setDockableDesc(tabText);
    }

    /**
     * Returns the {@code String} used for tab labels when this {@code Dockable}
     * is embedded within a tabbed layout. It is possible for this method to
     * return a {@code null} reference.
     *
     * @return tabText the {@code String} used for tab labels when this
     *         {@code Dockable} is embedded within a tabbed layout.
     */
    public String getTabText() {
        return getDockingProperties().getDockableDesc();
    }

    /**
     * No operation. Provided as a method stub to fulfull the
     * {@code DockingListener} interface contract.
     *
     * @param evt
     *            the {@code DockingEvent} to respond to.
     * @see DockingListener#dockingCanceled(DockingEvent)
     */
    public void dockingCanceled(DockingEvent evt) {
    }

    /**
     * No operation. Provided as a method stub to fulfull the
     * {@code DockingListener} interface contract.
     *
     * @param evt
     *            the {@code DockingEvent} to respond to.
     * @see DockingListener#dockingComplete(DockingEvent)
     */
    public void dockingComplete(DockingEvent evt) {
    }

    /**
     * No operation. Provided as a method stub to fulfull the
     * {@code DockingListener} interface contract.
     *
     * @param evt
     *            the {@code DockingEvent} to respond to.
     * @see DockingListener#dragStarted(DockingEvent)
     */
    public void dragStarted(DockingEvent evt) {
    }

    /**
     * No operation. Provided as a method stub to fulfull the
     * {@code DockingListener} interface contract.
     *
     * @param evt
     *            the {@code DockingEvent} to respond to.
     * @see DockingListener#dropStarted(DockingEvent)
     */
    public void dropStarted(DockingEvent evt) {
    }

    /**
     * No operation. Provided as a method stub to fulfull the
     * {@code DockingListener} interface contract.
     *
     * @param evt
     *            the {@code DockingEvent} to respond to.
     * @see DockingListener#undockingComplete(DockingEvent)
     */
    public void undockingComplete(DockingEvent evt) {

    }

    /**
     * No operation. Provided as a method stub to fulfull the
     * {@code DockingListener} interface contract.
     *
     * @param evt
     *            the {@code DockingEvent} to respond to.
     * @see DockingListener#undockingStarted(DockingEvent)
     */
    public void undockingStarted(DockingEvent evt) {
    }

    /**
     * Adds a {@code DockingListener} to observe docking events for this
     * {@code Dockable}. {@code null} arguments are ignored.
     *
     * @param listener
     *            the {@code DockingListener} to add to this {@code Dockable}.
     * @see #getDockingListeners()
     * @see #removeDockingListener(DockingListener)
     */
    public void addDockingListener(DockingListener listener) {
        if (listener != null)
            dockingListeners.add(listener);
    }

    /**
     * Returns an array of all {@code DockingListeners} added to this
     * {@code Dockable}. If there are no listeners present for this
     * {@code Dockable}, then a zero-length array is returned.
     *
     * @return an array of all {@code DockingListeners} added to this
     *         {@code Dockable}.
     * @see #addDockingListener(DockingListener)
     * @see #removeDockingListener(DockingListener)
     */
    public DockingListener[] getDockingListeners() {
        return (DockingListener[]) dockingListeners
               .toArray(new DockingListener[0]);
    }

    /**
     * Removes the specified {@code DockingListener} from this {@code Dockable}.
     * If the specified {@code DockingListener} is {@code null}, or the
     * listener has not previously been added to this {@code Dockable}, then no
     * {@code Exception} is thrown and no action is taken.
     *
     * @param listener
     *            the {@code DockingListener} to remove from this
     *            {@code Dockable}
     * @see #addDockingListener(DockingListener)
     * @see #getDockingListeners()
     */
    public void removeDockingListener(DockingListener listener) {
        if (listener != null)
            dockingListeners.remove(listener);
    }

    /**
     * Returns the value of the property with the specified key. Only properties
     * added with {@code putClientProperty} will return a non-{@code null}
     * value. If {@code key} is {@code null}, a {@code null} reference is
     * returned.
     * <p>
     * If the {@code Component} returned by {@code getComponent()} is an
     * instance of {@code JComponent}, then this method will dispatch to that
     * {@code JComponent's} {@code getClientProperty(Object, Object)} method.
     * Otherwise, this {@code Dockable} will provide its own internal mapping of
     * client properties.
     *
     * @param key
     *            the key that is being queried
     * @return the value of this property or {@code null}
     * @see Dockable#getClientProperty(Object)
     * @see javax.swing.JComponent#getClientProperty(java.lang.Object)
     */
    public Object getClientProperty(Object key) {
        if (key == null)
            return null;

        Component c = getComponent();
        if (c instanceof JComponent)
            return ((JComponent) c).getClientProperty(key);

        return clientProperties.get(key);
    }

    /**
     * Adds an arbitrary key/value "client property" to this {@code Dockable}.
     * {@code null} values are allowed. If {@code key} is {@code null}, then no
     * action is taken.
     * <p>
     * If the {@code Component} returned by {@code getComponent()} is an
     * instance of {@code JComponent}, then this method will dispatch to that
     * {@code JComponent's} {@code putClientProperty(Object, Object)} method.
     * Otherwise, this {@code Dockable} will provide its own internal mapping of
     * client properties.
     *
     * @param key
     *            the new client property key
     * @param value
     *            the new client property value; if {@code null} this method
     *            will remove the property
     * @see Dockable#putClientProperty(Object, Object)
     * @see javax.swing.JComponent#putClientProperty(java.lang.Object,
     *      java.lang.Object)
     */
    public void putClientProperty(Object key, Object value) {
        if (key == null)
            return;

        Component c = getComponent();
        if (c instanceof JComponent) {
            SwingUtility.putClientProperty(c, key, value);
        } else {
            Utilities.put(clientProperties, key, value);
        }
    }

    /**
     * Returns a {@code DockablePropertySet} instance associated with this
     * {@code Dockable}. This method returns the default implementation
     * supplied by the framework by invoking
     * {@code getDockablePropertySet(Dockable dockable)} on
     * {@code org.flexdock.docking.props.PropertyManager} and supplying an
     * argument of {@code this}.
     *
     * @return the {@code DockablePropertySet} associated with this
     *         {@code Dockable}. This method will not return a {@code null}
     *         reference.
     * @see org.flexdock.docking.props.DockablePropertySet
     * @see Dockable#getDockingProperties()
     * @see org.flexdock.docking.props.PropertyManager#getDockablePropertySet(Dockable)
     */
    public DockablePropertySet getDockingProperties() {
        return PropertyManager.getDockablePropertySet(this);
    }

    /**
     * Returns the {@code DockingPort} within which this {@code Dockable} is
     * currently docked. If not currently docked, this method will return
     * {@code null}.
     * <p>
     * This method defers processing to
     * {@code getDockingPort(Dockable dockable)}, passing an argument of
     * {@code this}. This {@code DockingPort} returned is based upon the
     * {@code Component} returned by this {@code Dockable's} abstract
     * {@code getComponent()} method.
     *
     * @return the {@code DockingPort} within which this {@code Dockable} is
     *         currently docked.
     * @see Dockable#getDockingPort()
     * @see DockingManager#getDockingPort(Dockable)
     */
    public DockingPort getDockingPort() {
        return DockingManager.getDockingPort(this);
    }

    /**
     * Provides the default {@code Dockable} implementation of
     * {@code dock(Dockable dockable)} by calling and returning
     * {@code DockingManager.dock(Dockable dockable, Dockable parent)}.
     * {@code 'this'} is passed as the {@code parent} parameter.
     *
     * @param dockable
     *            the {@code Dockable} to dock relative to this {@code Dockable}
     * @return {@code true} if the docking operation was successful;
     *         {@code false} otherwise.
     * @see Dockable#dock(Dockable)
     * @see DockingManager#dock(Dockable, Dockable)
     */
    public boolean dock(Dockable dockable) {
        return DockingManager.dock(dockable, this);
    }

    /**
     * Provides the default {@code Dockable} implementation of
     * {@code dock(Dockable dockable, String relativeRegion)} by calling and
     * returning
     * {@code DockingManager.dock(Dockable dockable, Dockable parent, String region)}.
     * {@code 'this'} is passed as the {@code parent} parameter.
     *
     * @param dockable
     *            the {@code Dockable} to dock relative to this {@code Dockable}
     * @param relativeRegion
     *            the docking region into which to dock the specified
     *            {@code Dockable}
     * @return {@code true} if the docking operation was successful;
     *         {@code false} otherwise.
     * @see Dockable#dock(Dockable, String)
     * @see DockingManager#dock(Dockable, Dockable, String)
     */
    public boolean dock(Dockable dockable, String relativeRegion) {
        return DockingManager.dock(dockable, this, relativeRegion);
    }

    /**
     * Provides the default {@code Dockable} implementation of
     * {@code dock(Dockable dockable, String relativeRegion, float ratio)} by
     * calling and returning
     * {@code DockingManager.dock(Dockable dockable, Dockable parent, String region, float proportion)}.
     * {@code 'this'} is passed as the {@code parent} parameter.
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
    public boolean dock(Dockable dockable, String relativeRegion, float ratio) {
        return DockingManager.dock(dockable, this, relativeRegion, ratio);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        getDockingProperties().addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        getDockingProperties().removePropertyChangeListener(listener);
    }
}
