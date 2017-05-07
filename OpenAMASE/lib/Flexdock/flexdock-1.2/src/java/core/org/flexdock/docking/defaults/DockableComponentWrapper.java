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

import javax.swing.Icon;
import javax.swing.JComponent;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.DockingStub;
import org.flexdock.docking.adapter.DockingAdapter;
import org.flexdock.docking.event.DockingEvent;
import org.flexdock.docking.event.DockingListener;
import org.flexdock.docking.props.DockablePropertySet;
import org.flexdock.docking.props.PropertyManager;
import org.flexdock.util.SwingUtility;
import org.flexdock.util.Utilities;

/**
 * This class models a {@code Dockable} implementation for wrapping a
 * {@code Component}. It is essentially the simplest means to turning a generic
 * {@code Component} into a {@code Dockable} instance. Compound
 * {@code Dockables} may have separate child components that are responsible for
 * drag initiation, whereas another component is the actual drag source. This is
 * shown in the manner that a {@code JInternalFrame} would be a draggable
 * component, while the frame's title pane is the actual drag initiator.
 * <p>
 * The class, conversely, deals with the <i>simple</i> case, where a
 * {@code Component} itself must be docking-enabled.
 * {@code DockableComponentWrapper} wraps a {@code Component} and implements the
 * {@code Dockable} interface. Since the {@code Component} itself is being
 * docking-enabled, it serves as both the drag source and drag initiator. Thus,
 * {@code getComponent()} will return a reference to {@code 'this'} and
 * {@code getDragSources()} return a {@code List} containing the same
 * self-reference {@code Component}.
 * <p>
 * This class may be used by application code to enable docking capabilities on
 * a given {@code Component}. However, it is recommended that
 * {@code DockingManager.registerDockable(Component evtSrc, String desc)} be
 * used as a more automated, less invasive means of enabling docking on a
 * component.
 * {@code DockingManager.registerDockable(Component evtSrc, String desc)} will
 * automatically create a {@code DockableComponentWrapper} instance and register
 * the required drag listeners.
 *
 * @author Chris Butler
 */
public class DockableComponentWrapper implements Dockable {
    private Component dragSrc;

    private String persistentId;

    private ArrayList dockingListeners;

    private ArrayList dragListeners;

    private Hashtable clientProperties;

    private HashSet frameDragSources;

    /**
     * Creates a {@code DockableComponentWrapper} instance using the specified
     * source component, persistent ID, and docking description. This method is
     * used to create {@code Dockable} instances for simple {@code Components}
     * where the drag source and drag initiator are the same {@code Component}.
     * <p>
     * If {@code src} or {@code id} are {@code null}, then this method returns
     * a {@code null} reference.
     * <p>
     * {@code src} will be the {@code Component} returned by invoking
     * {@code getComponent()} on the resulting {@code Dockable} and will be
     * included in the {@code List} returned by {@code getDragSources()}.
     * {@code id} will be the value returned by invoking
     * {@code getPersistentId()} on the resulting {@code Dockable}.
     * {@code desc} may be used by the {@code Dockable} for descriptive purposes
     * (such as tab-text in a tabbed layout). It is not recommended to supply a
     * {@code null} value for {@code desc}, but doing so is not illegal.
     *
     * @param src
     *            the source component
     * @param id
     *            the persistent ID for the Dockable instance
     * @param desc
     *            the docking description
     * @return a new {@code DockableComponentWrapper} instance
     * @see Dockable#getComponent()
     * @see Dockable#getDragSources()
     * @see Dockable#getPersistentId()
     * @see DockingManager#registerDockable(Component, String)
     */
    public static DockableComponentWrapper create(Component src, String id,
            String desc) {
        if (src == null || id == null)
            return null;

        return new DockableComponentWrapper(src, id, desc);
    }

    public static DockableComponentWrapper create(DockingStub stub) {
        if (!(stub instanceof Component))
            return null;

        return create((Component) stub, stub.getPersistentId(), stub
                      .getTabText());
    }

    public static DockableComponentWrapper create(DockingAdapter adapter) {
        if (adapter == null)
            return null;

        Component comp = adapter.getComponent();
        String id = adapter.getPersistentId();
        String tabText = adapter.getTabText();
        DockableComponentWrapper dockable = create(comp, id, tabText);

        List dragSources = adapter.getDragSources();
        Set frameDragSources = adapter.getFrameDragSources();
        Icon icon = adapter.getDockbarIcon();

        if (dragSources != null) {
            dockable.getDragSources().clear();
            dockable.getDragSources().addAll(dragSources);
        }

        if (frameDragSources != null) {
            dockable.getFrameDragSources().clear();
            dockable.getFrameDragSources().addAll(frameDragSources);
        }

        if (icon != null)
            dockable.getDockingProperties().setDockbarIcon(icon);

        return dockable;
    }

    /**
     * @param src
     * @param id
     * @param desc
     * @param resizable
     */
    private DockableComponentWrapper(Component src, String id, String desc) {
        dragSrc = src;
        getDockingProperties().setDockableDesc(desc);
        persistentId = id;

        dockingListeners = new ArrayList(0);
        dragListeners = new ArrayList(1);

        // initialize the drag sources lists
        initDragListeners();
    }

    private void initDragListeners() {
        // by default, use the wrapped source component as the drag source
        // and assume there is no frame drag source defined
        Component draggable = dragSrc;
        Component frameDragger = null;

        // if the wrapped source component is a DockingStub, then
        // we'll be able to pull some extra data from it
        if (dragSrc instanceof DockingStub) {
            DockingStub stub = (DockingStub) dragSrc;
            Component c = stub.getDragSource();
            // if the stub defines a specific drag source, then
            // replace wrapped source component with the specified
            // drag source
            if (c != null)
                draggable = c;
            // if the stub defines a specified frame drag source, then
            // use it
            frameDragger = stub.getFrameDragSource();
        }

        // add the "docking" drag source to the list
        if (draggable != null)
            dragListeners.add(draggable);

        // add the floating frame drag source to the list
        if (frameDragger != null)
            getFrameDragSources().add(frameDragger);
    }

    private Hashtable getInternalClientProperties() {
        if (clientProperties == null)
            clientProperties = new Hashtable(2);
        return clientProperties;
    }

    /**
     * Returns the {@code Component} used to create this
     * {@code DockableComponentWrapper} instance.
     *
     * @return the {@code Component} used to create this
     *         {@code DockableComponentWrapper} instance.
     * @see Dockable#getComponent()
     * @see #create(Component, String, String)
     */
    public Component getComponent() {
        return dragSrc;
    }

    /**
     * Returns a {@code List} of {@code Components} used to initiate
     * drag-to-dock operation. By default, the returned {@code List} contains
     * the {@code Component} returned by {@code getComponent()}.
     *
     * @return a {@code List} of {@code Components} used to initiate
     *         drag-to-dock operation.
     * @see Dockable#getDragSources()
     * @see #getComponent()
     * @see #create(Component, String, String)
     */
    public List getDragSources() {
        return dragListeners;
    }

    /**
     * Returns the persistent ID of this {@code DockableComponentWrapper}
     * instance provided when this object was instantiated.
     *
     * @return the persistent ID of this {@code DockableComponentWrapper}
     * @see Dockable#getPersistentId()
     * @see #create(Component, String, String)
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
     * Returns the value of the property with the specified key. Only properties
     * added with {@code putClientProperty} will return a non-{@code null}
     * value. If {@code key} is {@code null}, a {@code null} reference is
     * returned.
     * <p>
     * If the {@code Component} returned by {@code getComponent()} is an
     * instance of {@code JComponent}, then this method will dispatch to that
     * {@code JComponent's} {@code getClientProperty(Object, Object)} method.
     * Otherwise, this {@code DockableComponentWrapper} will provide its own
     * internal mapping of client properties.
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

        return getInternalClientProperties().get(key);
    }

    /**
     * Adds an arbitrary key/value "client property" to this {@code Dockable}.
     * {@code null} values are allowed. If {@code key} is {@code null}, then no
     * action is taken.
     * <p>
     * If the {@code Component} returned by {@code getComponent()} is an
     * instance of {@code JComponent}, then this method will dispatch to that
     * {@code JComponent's} {@code putClientProperty(Object, Object)} method.
     * Otherwise, this {@code DockableComponentWrapper} will provide its own
     * internal mapping of client properties.
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
            Utilities.put(getInternalClientProperties(), key, value);
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
     * {@code this}.
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
