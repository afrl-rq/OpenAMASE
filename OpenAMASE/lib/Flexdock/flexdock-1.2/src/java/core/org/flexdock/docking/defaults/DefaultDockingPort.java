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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.DockingStrategy;
import org.flexdock.docking.RegionChecker;
import org.flexdock.docking.activation.ActiveDockableTracker;
import org.flexdock.docking.event.DockingEvent;
import org.flexdock.docking.event.DockingListener;
import org.flexdock.docking.event.DockingMonitor;
import org.flexdock.docking.event.TabbedDragListener;
import org.flexdock.docking.event.hierarchy.DockingPortTracker;
import org.flexdock.docking.props.DockingPortPropertySet;
import org.flexdock.docking.props.PropertyChangeListenerFactory;
import org.flexdock.docking.props.PropertyManager;
import org.flexdock.docking.state.LayoutNode;
import org.flexdock.docking.state.tree.DockableNode;
import org.flexdock.docking.state.tree.DockingPortNode;
import org.flexdock.docking.state.tree.SplitNode;
import org.flexdock.util.DockingUtility;
import org.flexdock.util.LookAndFeelSettings;
import org.flexdock.util.SwingUtility;
import org.flexdock.util.UUID;
import org.flexdock.util.Utilities;

/**
 * This is a {@code Container} that implements the {@code DockingPort}
 * interface. It provides a default implementation of {@code DockingPort} to
 * allow ease of development within docking-enabled applications.
 * <p>
 * The {@code DefaultDockingPort} handles docking in one of three ways. If the
 * port is empty, then all incoming {@code Dockables} are docked to the CENTER
 * region. If the port is not empty, then all incoming {@code Dockables} docked
 * to the CENTER region are embedded within a {@code JTabbedPane}. All incoming
 * {@code Dockables} docked to an outer region (NORTH, SOUTH, EAST, and WEST) of
 * a non-empty port are placed into a split layout using a {@code JSplitPane}.
 * <p>
 * For centrally docked {@code Components}, the immediate child of the
 * {@code DefaultDockingPort} may or may not be a {@code JTabbedPane}. If
 * {@code isSingleTabAllowed()} returns {@code true} for the current
 * {@code DefaultDockingPort}, then the immediate child returned by
 * {@code getDockedComponent()} will return a {@code JTabbedPane} instance even
 * if there is only one {@code Dockable} embedded within the port. If there is a
 * single {@code Dockable} in the port, but {@code isSingleTabAllowed()} returns
 * {@code false}, then {@code getDockedComponent()} will return the
 * {@code Component} that backs the currently docked {@code Dockable}, returned
 * by the {@code Dockable's} {@code getComponent()} method.
 * {@code isSingleTabAllowed()} is a scoped property that may apply to this
 * port, all ports across the JVM, or all ports within a user defined scope.
 * {@code getDockedComponent()} will return a {@code JTabbedPane} at all times
 * if there is more than one centrally docked {@code Dockable} within the port,
 * and all docked {@code Components} will reside within the tabbed pane.
 * <p>
 * Components that are docked in the NORTH, SOUTH, EAST, or WEST regions are
 * placed in a {@code JSplitPane} splitting the layout of the
 * {@code DockingPort} between child components. Each region of the
 * {@code JSplitPane} contains a new {@code DefaultDockingPort}, which, in
 * turn, contains the docked components. In this situation,
 * {@code getDockedComponent()} will return a {@code JSplitPane} reference.
 * <p>
 * A key concept that drives the {@code DefaultDockingPort}, then, is the
 * notion that this {@code DockingPort} implementation may only ever have one
 * single child component, which may or may not be a wrapper for other child
 * components. Because {@code JSplitPane} contains child
 * {@code DefaultDockingPorts}, each of those {@code DefaultDockingPorts} is
 * available for further sub-docking operations.
 * <p>
 * Since a {@code DefaultDockingPort} may only contain one child component,
 * there is a container hierarchy to manage tabbed interfaces, split layouts,
 * and sub-docking. As components are removed from this hierarchy, the hierarchy
 * itself must be reevaluated. Removing a component from a child
 * {@code DefaultDockingPort} within a {@code JSplitPane} renders the child
 * {@code DefaultDockingPort} unnecessary, which, in turn, renders the notion of
 * splitting the layout with a {@code JSplitPane} unnecessary (since there are
 * no longer two components to split the layout between). Likewise, removing a
 * child component from a {@code JTabbedPane} such that there is only one child
 * left within the {@code JTabbedPane} removes the need for a tabbed interface
 * to begin with.
 * <p>
 * When the {@code DockingManager} removes a component from a
 * {@code DockingPort} via {@code DockingManager.undock(Dockable dockable)} it
 * uses a call to {@code undock()} on the current {@code DockingPort}.
 * {@code undock()} automatically handles the reevaluation of the container
 * hierarchy to keep wrapper-container usage at a minimum. Since
 * {@code DockingManager} makes this callback automatic, developers normally
 * will not need to call this method explicitly. However, when removing a
 * component from a {@code DefaultDockingPort} using application code,
 * developers should keep in mind to use {@code undock()} instead of
 * {@code remove()}.
 *
 * Border management after docking and undocking operations are accomplished
 * using a {@code BorderManager}. {@code setBorderManager()} may be used to set
 * the border manager instance and customize border management.
 *
 * @author Christopher Butler
 *
 */
@SuppressWarnings(value = { "serial" })
public class DefaultDockingPort extends JPanel implements DockingPort,
    DockingConstants {
    protected class PortLayout implements LayoutManager2, Serializable {
        /**
         * Returns the amount of space the layout would like to have.
         *
         * @param parent
         *            the Container for which this layout manager is being used
         * @return a Dimension object containing the layout's preferred size
         */
        public Dimension preferredLayoutSize(Container parent) {
            Dimension dd;
            Insets i = getInsets();

            if (dockedComponent != null) {
                dd = dockedComponent.getPreferredSize();
            } else {
                dd = parent.getSize();
            }

            return new Dimension(dd.width + i.left + i.right, dd.height + i.top
                                 + i.bottom);
        }

        /**
         * Returns the minimum amount of space the layout needs.
         *
         * @param parent
         *            the Container for which this layout manager is being used
         * @return a Dimension object containing the layout's minimum size
         */
        public Dimension minimumLayoutSize(Container parent) {
            Dimension dd;
            Insets i = getInsets();

            if (dockedComponent != null) {
                dd = dockedComponent.getMinimumSize();
            } else {
                dd = parent.getSize();
            }

            return new Dimension(dd.width + i.left + i.right, dd.height + i.top
                                 + i.bottom);
        }

        /**
         * Returns the maximum amount of space the layout can use.
         *
         * @param target
         *            the Container for which this layout manager is being used
         * @return a Dimension object containing the layout's maximum size
         */
        public Dimension maximumLayoutSize(Container target) {
            Dimension dd;
            Insets i = getInsets();

            if (dockedComponent != null) {
                dd = dockedComponent.getMaximumSize();
            } else {
                // This is silly, but should stop an overflow error
                dd = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE - i.top
                                   - i.bottom);
            }

            return new Dimension(dd.width + i.left + i.right, dd.height + i.top
                                 + i.bottom);
        }

        /**
         * Instructs the layout manager to perform the layout for the specified
         * container.
         *
         * @param parent
         *            the Container for which this layout manager is being used
         */
        public void layoutContainer(Container parent) {
            Rectangle b = getBounds();
            Insets i = getInsets();
            int w = b.width - i.right - i.left;
            int h = b.height - i.top - i.bottom;

            if (dockedComponent != null) {
                dockedComponent.setBounds(i.left, i.top, w, h);
            }
        }

        public void addLayoutComponent(String name, Component comp) {
        }

        public void removeLayoutComponent(Component comp) {
        }

        public void addLayoutComponent(Component comp, Object constraints) {
        }

        public float getLayoutAlignmentX(Container target) {
            return 0.0f;
        }

        public float getLayoutAlignmentY(Container target) {
            return 0.0f;
        }

        public void invalidateLayout(Container target) {
        }
    }

    private static final WeakHashMap COMPONENT_TITLES = new WeakHashMap();

    protected ArrayList dockingListeners;

    private Component dockedComponent;

    private BorderManager borderManager;

    private String persistentId;

    private boolean tabsAsDragSource;

    private boolean rootPort;

    private BufferedImage dragImage;

    private Timer timer;
    
    private Object lock = new Object();

    static {
        // setup PropertyChangeListenerFactory to respond to
        // DefaultDockingPort-specific
        // events
        PropertyChangeListenerFactory
        .addFactory(new DockablePropertyChangeHandler.Factory());
    }

    /**
     * Creates a new {@code DefaultDockingPort} with a persistent ID equal to
     * the {@code String} value of this a random UUID.
     *
     * @see org.flexdock.util.UUID
     */
    public DefaultDockingPort() {
        this(UUID.randomUUID().toString());
    }

    /**
     * Creates a new {@code DefaultDockingPort} with the specified persistent
     * ID. If {@code id} is {@code null}, then the {@code String} value of this
     * {@code Object's} hash code is used. The persistent ID will be the same
     * value returned by invoking {@code getPersistentId()} for this
     * {@code DefaultDockingPort}.
     *
     * @param id
     *            the persistent ID for the new {@code DefaultDockingPort}
     *            instance.
     */
    public DefaultDockingPort(String id) {
        setPersistentId(id);
        dockingListeners = new ArrayList(2);
        addDockingListener(this);

        DockingPortPropertySet props = getDockingProperties();
        props.setRegionChecker(new DefaultRegionChecker());

        // check container hierarchy to track root dockingports
        addHierarchyListener(DockingPortTracker.getInstance());

        // start out as a root dockingport
        rootPort = true;

        // configure layout
        setLayout(createLayout());

        //configure the default border manager
        setBorderManager(createBorderManager());
    }

    protected LayoutManager createLayout() {
        return new PortLayout();
    }

    /**
     * Creates a standard border manager for this docking port.
     * <p>
     * This method is called from the constructor.
     *
     * @return the border manager for this docking port.
     */
    protected BorderManager createBorderManager() {
        return new StandardBorderManager(new EmptyBorder(0, 0, 0, 0));
    }

    /**
     * Overridden to set the currently docked component. Should not be called by
     * application code.
     *
     * @param comp
     *            the component to be added
     */
    public Component add(Component comp) {
        return setComponent(comp);
    }

    /**
     * Overridden to set the currently docked component. Should not be called by
     * application code.
     *
     * @param comp
     *            the component to be added
     * @param index
     *            the position at which to insert the component, or {@code -1}
     *            to append the component to the end
     */
    public Component add(Component comp, int index) {
        return setComponent(comp);
    }

    /**
     * Overridden to set the currently docked component. Should not be called by
     * application code.
     *
     * @param comp
     *            the component to be added
     * @param constraints
     *            an object expressing layout contraints for this component
     */
    public void add(Component comp, Object constraints) {
        setComponent(comp);
    }

    /**
     * Overridden to set the currently docked component. Should not be called by
     * application code.
     *
     * @param comp
     *            the component to be added
     * @param constraints
     *            an object expressing layout contraints for this
     * @param index
     *            the position in the container's list at which to insert the
     *            component; {@code -1} means insert at the end
     */
    public void add(Component comp, Object constraints, int index) {
        setComponent(comp);
    }

    /**
     * Overridden to set the currently docked component. Should not be called by
     * application code.
     *
     * @param name
     *            the name of the {@code Component} to be added.
     * @param comp
     *            the {@code Component} to add.
     */
    public Component add(String name, Component comp) {
        return setComponent(comp);
    }

    private void addCmp(DockingPort port, Component c) {
        if (port instanceof Container)
            ((Container) port).add(c);
    }

    private void dockCmp(DockingPort port, Component c) {
        port.dock(c, CENTER_REGION);
    }

    /**
     * Returns {@code true} if docking is allowed for the specified
     * {@code Component} within the supplied {@code region}, {@code false}
     * otherwise. It is important to note that success of a docking operation
     * relies on many factors and a return value of {@code true} from this
     * method does not necessarily guarantee that a call to {@code dock()} will
     * succeed. This method merely indicates that the current
     * {@code DockingPort} does not have any outstanding reason to block a
     * docking operation with respect to the specified {@code Component} and
     * {@code region}.
     * <p>
     * If {@code comp} is {@code null} or {@code region} is invalid according to
     * {@code DockingManager.isValidDockingRegion(String region)}, then this
     * method returns {@code false}.
     * <p>
     * If this {@code DockingPort} is not already the parent {@code DockingPort}
     * for the specified {@code Component}, then this method returns
     * {@code true}.
     * <p>
     * If this {@code DockingPort} is already the parent {@code DockingPort} for
     * the specified {@code Component}, then a check is performed to see if
     * there is a tabbed layout. Tabbed layouts may contain multiple
     * {@code Dockables}, and thus the tab ordering may be rearranged, or
     * shifted into a split layout. If {@code comp} is the only docked
     * {@code Component} within this {@code DockingPort}, then this method
     * returns {@code false} since the layout cannot be rearranged. Otherwise,
     * this method returns {@code true}.
     *
     * @param comp
     *            the {@code Component} whose docking availability is to be
     *            checked
     * @param region
     *            the region to be checked for docking availability for the
     *            specified {@code Component}.
     * @return {@code true} if docking is allowed for the specified
     *         {@code Component} within the supplied {@code region},
     *         {@code false} otherwise.
     * @see DockingPort#isDockingAllowed(Component, String)
     * @see DockingManager#isValidDockingRegion(String)
     * @see #isParentDockingPort(Component)
     */
    public boolean isDockingAllowed(Component comp, String region) {
        if (comp == null || !isValidDockingRegion(region))
            return false;

        // allow any valid region if we're not already the parent
        // of the component we're checking
        if (!isParentDockingPort(comp))
            return true;

        // we already contain 'comp', so we're either a tabbed-layout, or
        // we contain 'comp' directly. If we contain 'comp' directly, then we
        // cannot logically move 'comp' to some other region within us, as it
        // already fills up our entire space.
        Component docked = getDockedComponent();
        if (!(docked instanceof JTabbedPane))
            // not a tabbed-layout, so we contain 'c' directly
            return false;

        JTabbedPane tabs = (JTabbedPane) docked;
        // if there is only 1 tab, then we already fill up the entire
        // dockingport space and cannot be moved elsewhere
        if (tabs.getTabCount() < 2)
            return false;

        // there is more than 1 tab present, so re-ordering is possible,
        // as well as changing regions
        return true;
    }

    /**
     * Checks the current state of the {@code DockingPort} and, if present,
     * issues the appropriate call to the assigned {@code BorderManager}
     * instance describing the container state. This method will issue a call to
     * 1 of the 4 following methods on the assigned {@code BorderManager}
     * instance, passing {@code this} as the method argument:
     * <p>
     * {@code managePortNullChild(DockingPort port)}
     * {@code managePortSimpleChild(DockingPort port)}
     * {@code managePortSplitChild(DockingPort port)}
     * {@code managePortTabbedChild(DockingPort port)}
     */
    final void evaluateDockingBorderStatus() {
        if (borderManager == null)
            return;

        Component docked = getDockedComponent();
        // check for the null-case
        if (docked == null)
            borderManager.managePortNullChild(this);
        // check for a split layout
        else if (docked instanceof JSplitPane)
            borderManager.managePortSplitChild(this);
        // check for a tabbed layout
        else if (docked instanceof JTabbedPane)
            borderManager.managePortTabbedChild(this);
        // otherwise, we have a simple case of a regular component docked within
        // us
        else
            borderManager.managePortSimpleChild(this);
    }

    /**
     * Returns the docking region within this {@code DockingPort} that contains
     * the specified {@code Point}. Valid return values are those regions
     * defined in {@code DockingConstants} and include {@code NORTH_REGION},
     * {@code SOUTH_REGION}, {@code EAST_REGION}, {@code WEST_REGION},
     * {@code CENTER_REGION}, and {@code UNKNOWN_REGION}.
     * <p>
     * If {@code location} is {@code null}, then {@code UNKNOWN_REGION} is
     * returned.
     * <p>
     * This method gets the {@code RegionChecker} for this {@code DockingPort}
     * by calling {@code getRegionChecker()}. It then attempts to locate the
     * {@code Dockable} at the specified {@code location} by calling
     * {@code getDockableAt(Point location)}.
     * <p>
     * This method defers processing to {@code getRegion(Component c, Point p)}
     * for the current {@code RegionChecker}. If a {@code Dockable} was found
     * at the specified {@code Point}, then the location of the {@code Point}
     * is translated to the coordinate system of the {@code Component} for the
     * embedded {@code Dockable} and that {@code Component} and modified
     * {@code Point} are passed into {@code getRegion(Component c, Point p)}}
     * for the current {@code RegionChecker}. If no {@code Dockable} was found,
     * then the specified {@code Point} is left unmodified and this
     * {@code DockingPort} and the supplied {@code Point} are passed to
     * {@code getRegion(Component c, Point p)}} for the current
     * {@code RegionChecker}.
     *
     * @param location
     *            the location within this {@code DockingPort} to examine for a
     *            docking region.
     * @return the docking region within this {@code DockingPort} that contains
     *         the specified {@code Point}
     * @see #getRegionChecker()
     * @see #getDockableAt(Point)
     * @see Dockable#getComponent()
     * @see RegionChecker#getRegion(Component, Point)
     */
    public String getRegion(Point location) {
        if (location == null)
            return UNKNOWN_REGION;

        RegionChecker regionChecker = getRegionChecker();
        Dockable d = getDockableAt(location);
        Component regionTest = this;

        if (d != null) {
            regionTest = d.getComponent();
            location = SwingUtilities.convertPoint(this, location, regionTest);
        }

        return regionChecker.getRegion(regionTest, location);
    }

    /**
     * Returns the {@code RegionChecker} currently used by this
     * {@code DockingPort}. This method retrieves the
     * {@code DockingPortPropertySet} instance for this {@code DockingPort} by
     * calling {@code getDockingProperties()}. It then returns by invoking
     * {@code getRegionChecker()} on the resolved {@code DockingPortPropertySet}.
     *
     * @return the {@code RegionChecker} currently used by this
     *         {@code DockingPort}.
     * @see #getDockingProperties()
     * @see DockingPortPropertySet#getRegionChecker()
     */
    public RegionChecker getRegionChecker() {
        return getDockingProperties().getRegionChecker();
    }

    /**
     * Returns the direct child {@code Dockable} located at the specified
     * {@code Point}. If {@code location} is {@code null}, or this
     * {@code DockingPort} is empty, then a {@code null} reference is returned.
     * <p>
     * If this {@code DockingPort} contains a split layout, then any nested
     * {@code Dockables} will be within a sub-{@code DockingPort} and not a
     * direct child of this {@code DockingPort}. Therefore, if
     * {@code getDockedComponent()} returns a {@code JSplitPane}, then this
     * method will return a {@code null} reference.
     * <p>
     * If this {@code DockingPort} contains a tabbed layout, then the
     * {@code JTabbedPane} returned by {@code getDockedComponent()} will be
     * checked for a {@code Dockable} at the specified {@code Point}.
     *
     * @param location
     *            the location within the {@code DockingPort} to test for a
     *            {@code Dockable}.
     * @return the direct child {@code Dockable} located at the specified
     *         {@code Point}.
     * @see #getDockedComponent()
     * @see DockingManager#getDockable(Component)
     * @see JTabbedPane#getComponentAt(int x, int y)
     */
    public Dockable getDockableAt(Point location) {
        if (location == null)
            return null;

        Component docked = getDockedComponent();
        if (docked == null || docked instanceof JSplitPane)
            return null;

        if (docked instanceof JTabbedPane) {
            JTabbedPane tabs = (JTabbedPane) docked;
            Component c = tabs.getComponentAt(location.x, location.y);
            return c instanceof Dockable ? (Dockable) c : DockingManager
                   .getDockable(c);
        }

        return DockingManager.getDockable(docked);
    }

    /**
     * Returns the {@code Component} currently docked within the specified
     * {@code region}.
     * <p>
     * If this {@code DockingPort} has either a single child {@code Dockable} or
     * a tabbed layout, then the supplied region must be {@code CENTER_REGION}
     * or this method will return a {@code null} reference. If there is a single
     * child {@code Dockable}, then this method will return the same
     * {@code Component} as returned by {@code getDockedComponent()}. If there
     * is a tabbed layout, then this method will return the {@code Component} in
     * the currently selected tab.
     * <p>
     * If this {@code DockingPort} has a split layout, then a check for
     * {@code CENTER_REGION} will return a {@code null} reference. For outer
     * regions ({@code NORTH_REGION}, {@code SOUTH_REGION},
     * {@code EAST_REGION}, or {@code WEST_REGION}), the supplied region
     * parameter must match the orientation of the embedded {@code JSplitPane}.
     * Thus for a vertically oriented split pane, checks for {@code EAST_REGION}
     * and {@code WEST_REGION} will return a {@code null} reference. Likewise,
     * for a horizontally oriented split pane, checks for {@code NORTH_REGION}
     * and {@code SOUTH_REGION} will return a {@code null} reference.
     * <p>
     * Outer regions are mapped to corresponding split pane regions.
     * {@code NORTH_REGION} maps to the split pane's top component,
     * {@code SOUTH_REGION} maps to the bottom, {@code EAST_REGION} maps to the
     * right, and {@code WEST_REGION} maps to the left. The sub-{@code DockingPort}
     * for the split pane region that corresponds to the specified
     * {@code region} parameter will be resolved and this method will return
     * that {@code Component} retrieved by calling its
     * {@code getDockedComponent()} method. <i>Note that the
     * {@code getDockedComponent()} call to a sub- {@code DockingPort} implies
     * that the {@code JTabbedPane} or {@code JSplitPane} for the sub-port may
     * be returned if the sub-port contains multiple {@code Dockables}.</i>
     * <p>
     * If this {@code DockingPort} is empty, then this method returns a
     * {@code null} reference.
     *
     * @param region
     *            the region to be checked for a docked {@code Component}
     * @return the {@code Component} docked within the specified region.
     * @see DockingPort#getComponent(String)
     * @see #getDockedComponent()
     */
    public Component getComponent(String region) {
        Component docked = getDockedComponent();
        if (docked == null)
            return null;

        if (docked instanceof JTabbedPane) {
            // they can only get tabbed dockables if they were checking the
            // CENTER region.
            if (!CENTER_REGION.equals(region))
                return null;

            JTabbedPane tabs = (JTabbedPane) docked;
            return tabs.getSelectedComponent();
        }

        if (docked instanceof JSplitPane) {
            // they can only get split dockables if they were checking an outer
            // region.
            if (CENTER_REGION.equals(region))
                return null;

            JSplitPane split = (JSplitPane) docked;

            // make sure the supplied regions correspond to the current
            // splitpane orientation
            boolean horizontal = split.getOrientation() == JSplitPane.HORIZONTAL_SPLIT;
            if (horizontal) {
                if (NORTH_REGION.equals(region) || SOUTH_REGION.equals(region))
                    return null;
            } else {
                if (EAST_REGION.equals(region) || WEST_REGION.equals(region))
                    return null;
            }

            boolean left = NORTH_REGION.equals(region)
                           || WEST_REGION.equals(region);
            Component c = left ? split.getLeftComponent() : split
                          .getRightComponent();
            // split panes only contain sub-dockingports. if 'c' is not a
            // sub-dockingport,
            // then something is really screwed up.
            if (!(c instanceof DockingPort))
                return null;

            // get the dockable contained in the sub-dockingport
            return ((DockingPort) c).getDockedComponent();
        }

        // we already checked the tabbed layout and split layout. all that's
        // left is the direct-child component itself. this will only ever
        // exist in the CENTER, so return it if they requested the CENTER
        // region.
        return CENTER_REGION.equals(region) ? docked : null;
    }

    /**
     * Returns the {@code Dockable} currently docked within the specified
     * {@code region}. This method dispatches to
     * {@code getComponent(String region)} to retrieve the {@code Component}
     * docked within the specified region and returns its associated
     * {@code Dockable} via {@code DockingManager.getDockable(Component comp)}.
     * <p>
     * There are somewhat strict semantics associated with retrieving the
     * {@code Component} in a particular docking region. API documentation for
     * {@code getComponent(String region)} should be referenced for a listing of
     * the rule set. If {@code region} is invalid according to
     * {@code DockingManager.isValidDockingRegion(String region)}, then this
     * method returns a {@code null} reference.
     *
     * @param region
     *            the region to be checked for a docked {@code Dockable}
     * @return the {@code Dockable} docked within the specified region.
     * @see DockingPort#getDockable(String)
     * @see #getComponent(String)
     * @see #getDockedComponent()
     * @see DockingManager#getDockable(Component)
     * @see DockingManager#isValidDockingRegion(String)
     */
    public Dockable getDockable(String region) {
        Component c = getComponent(region);
        return DockingManager.getDockable(c);
    }

    /**
     * If this method returns {@code null}, implementations may throw
     * NullPointerExceptions. Do not expect NPE checking.
     *
     * @return a valid JTabbedPane.
     */
    protected JTabbedPane createTabbedPane() {
        Insets oldInsets = UIManager
                           .getInsets(LookAndFeelSettings.TAB_PANE_BORDER_INSETS);
        int tabPlacement = getInitTabPlacement();

        int edgeInset = LookAndFeelSettings.getTabEdgeInset(tabPlacement);

        Insets newInsets = new Insets(0, 0, 0, 0);
        switch (tabPlacement) {
        case JTabbedPane.TOP:
            newInsets.top = edgeInset >= 0 ? edgeInset : oldInsets.top;
            break;
        case JTabbedPane.LEFT:
            newInsets.left = edgeInset >= 0 ? edgeInset : oldInsets.left;
            break;
        case JTabbedPane.BOTTOM:
            newInsets.bottom = edgeInset >= 0 ? edgeInset : oldInsets.bottom;
            break;
        case JTabbedPane.RIGHT:
            newInsets.right = edgeInset >= 0 ? edgeInset : oldInsets.right;
            break;
        }

        UIManager.put(LookAndFeelSettings.TAB_PANE_BORDER_INSETS, newInsets);
        JTabbedPane pane = new JTabbedPane();
        pane.setTabPlacement(tabPlacement);
        UIManager.put(LookAndFeelSettings.TAB_PANE_BORDER_INSETS, oldInsets);

        TabbedDragListener tdl = new TabbedDragListener();
        pane.addMouseListener(tdl);
        pane.addMouseMotionListener(tdl);
        return pane;
    }

    protected void updateTab(Dockable dockable) {
        Component docked = getDockedComponent();
        if (docked instanceof JTabbedPane) {
            JTabbedPane tabs = (JTabbedPane) docked;
            int index = tabs.indexOfComponent(dockable.getComponent());
            if (index > -1) {
                tabs.setIconAt(index, dockable.getDockingProperties()
                               .getTabIcon());
                tabs.setTitleAt(index, dockable.getDockingProperties()
                                .getDockableDesc());
            }
        }
    }

    /**
     * Returns the {@code DockingStrategy} used by this {@code DockingPort}.
     * This method dispatches to {@code getDockingStrategy(Object obj)},
     * passing {@code this} as an argument. By default,
     * {@code DefaultDockingStrategy} is used unless a different
     * {@code DockingStrategy} has been assigned by the end user for
     * {@code DefaultDockingPort}.
     *
     * @return the {@code DockingStrategy} used by this {@code DockingPort}.
     * @see DockingPort#getDockingStrategy()
     * @see DockingManager#getDockingStrategy(Object)
     */
    public DockingStrategy getDockingStrategy() {
        return DockingManager.getDockingStrategy(this);
    }

    /**
     * Removes all {@code Dockables} from this {@code DockingPort}. Internally,
     * this method dispatches to {@code removeAll()}. This ensures that not
     * only docked {@code Components} are removed, that that all wrapper
     * containers such as {@code JTabbedPanes}, {@code JSplitPanes}, and sub-{@code DockingPorts}
     * are removed as well.
     *
     * @see DockingPort#clear()
     * @see #removeAll()
     */
    public void clear() {
        removeAll();
    }

    /**
     * Docks the specified component within the specified region. This method
     * attempts to resolve the {@code Dockable} associated with the specified
     * {@code Component} by invoking
     * {@code DockingManager.getDockable(Component comp)}. Processing is then
     * dispatched to {@code dock(Dockable dockable, String region)}.
     * <p>
     * If no {@code Dockable} is resolved for the specified {@code Component},
     * then this method attempts to register the {@code Component} as a
     * {@code Dockable} automatically by calling
     * {@code DockingManager.registerDockable(Component comp)}.
     * <p>
     * If either {@code comp} or {@code region} region are {@code null}, then
     * this method returns {@code false}. Otherwise, this method returns a
     * boolean indicating the success of the docking operation based upon
     * {@code dock(Dockable dockable, String region)}.
     *
     * @param comp
     *            the {@code Component} to be docked within this
     *            {@code DockingPort}
     * @param region
     *            the region within this {@code DockingPort} to dock the
     *            specified {@code Component}
     * @return {@code true} if the docking operation was successful,
     *         {@code false} otherwise.
     * @see DockingPort#dock(Component, String)
     * @see #dock(Dockable, String)
     * @see DockingManager#getDockable(Component)
     * @see DockingManager#registerDockable(Component)
     */
    public boolean dock(Component comp, String region) {
        if (comp == null || region == null)
            return false;

        Dockable dockable = DockingManager.getDockable(comp);
        if (dockable == null)
            dockable = DockingManager.registerDockable(comp);

        return dock(dockable, region);
    }

    /**
     * Docks the specified {@code Dockable} within the specified region. The
     * {@code Component} used for docking is returned by calling
     * {@code getComponent()} on the specified {@code Dockable}. This method
     * returns {@code false} immediately if the specified {@code Dockable} is
     * {@code null} or if
     * {@code isDockingAllowed(Component comp, String region)} returns
     * {@code false}.
     * <p>
     * If this {@code DockingPort} is currently empty, then the {@code Dockable}
     * is docked into the {@code CENTER_REGION}, regardless of the supplied
     * {@code region} parameter's value.
     * <p>
     * If {@code isSingleTabAllowed()} returns {@code false} and the
     * {@code DockingPort} is emtpy, then the {@code Dockable} will be added
     * directly to the {@code DockingPort} and will take up all available space
     * within the {@code DockingPort}. In this case, subsequent calls to
     * {@code getDockedComponent()} will return the dockable {@code Component}.
     * <p>
     * If {@code isSingleTabAllowed()} returns {@code true} and the
     * {@code DockingPort} is emtpy, then a {@code JTabbedPane} will be added
     * directly to the {@code DockingPort} and will take up all available space
     * within the {@code DockingPort}. The dockable {@code Component} will be
     * added as a tab within the tabbed pane. In this case, subsequent calls to
     * {@code getDockedComponent()} will return the {@code JTabbedPane}.
     * <p>
     * If the {@code DockingPort} is <b>not</b> empty, and the specified region
     * is {@code CENTER_REGION}, then the dockable {@code Component} will be
     * added to the {@code JTabbedPane} returned by {@code getDockedComponent()}.
     * If this {@code DockingPort} only contained a single dockable
     * {@code Component} without a tabbed pane, then the currently docked
     * {@code Component} is removed, a {@code JTabbedPane} is created and added,
     * and both the old {@code Component} and the new one are added to the
     * {@code JTabbedPane}. In this case, subsequent calls to
     * {@code getDockedComponent()} will return the {@code JTabbedPane}.
     * <p>
     * If the {@code DockingPort} is <b>not</b> empty, and the specified region
     * is {@code NORTH_REGION}, {@code SOUTH_REGION}, {@code EAST_REGION}, or
     * {@code WEST_REGION}, then the currently docked {@code Component} is
     * removed and replaced with a {@code JSplitPane}. Two new
     * {@code DefaultDockingPorts} are created as sub-ports and are added to
     * each side of the {@code JSplitPane}. The previously docked
     * {@code Component} is docked to the CENTER_REGION of one of the sub-ports
     * and the new {@code Component} is added to the other. In this case,
     * subsequent calls to {@code getDockedComponent()} will return the
     * {@code JSplitPane}. In this fasion, the sub-ports will now be capable of
     * handling further sub-docking within the layout.
     * <p>
     * {@code JSplitPane} and sub-{@code DockingPort} creation are delegated to
     * the {@code DockingStrategy} returned by {@code getDockingStrategy()}.
     * Initial splitpane divider location is also controlled by this
     * {@code DockingStrategy}.
     *
     * @param dockable
     *            the {@code Dockable} to be docked within this
     *            {@code DockingPort}
     * @param region
     *            the region within this {@code DockingPort} to dock the
     *            specified {@code Dockable}
     * @return {@code true} if the docking operation was successful,
     *         {@code false} otherwise.
     * @see DockingPort#dock(Dockable, String)
     * @see #isDockingAllowed(Component, String)
     * @see #getDockedComponent()
     * @see #getDockingStrategy()
     * @see DockingStrategy#createDockingPort(DockingPort)
     * @see DockingStrategy#createSplitPane(DockingPort, String)
     * @see DockingStrategy#getInitialDividerLocation(DockingPort, JSplitPane)
     * @see DockingStrategy#getDividerProportion(DockingPort, JSplitPane)
     */
    public boolean dock(Dockable dockable, String region) {
        if (dockable == null)
            return false;

        Component comp = dockable.getComponent();
        if (comp == null || !isDockingAllowed(comp, region))
            return false;

        // can't dock the same component twice. This will also keep them from
        // moving CENTER to NORTH and that sort of thing, which would just be a
        // headache to manage anyway.
        Component docked = getDockedComponent();
        if (comp == docked)
            return false;

        // if there is nothing currently in the docking port, then we can only
        // dock into the CENTER region.
        if (docked == null)
            region = CENTER_REGION;

        String tabTitle = DockingUtility.getTabText(dockable);
        COMPONENT_TITLES.put(comp, tabTitle);

        if (!isSingleTabAllowed() && docked == null) {
            setComponent(comp);
            evaluateDockingBorderStatus();
            return true;
        }

        boolean success = CENTER_REGION.equals(region) ? dockInCenterRegion(comp)
                          : dockInOuterRegion(comp, region);

        if (success) {
            evaluateDockingBorderStatus();
            // if we docked in an outer region, then there is a new JSplitPane.
            // We'll want to divide it in half. this is done after
            // evaluateDockingBorderStatus(), so we'll know any border
            // modification that took place has already happened, and we can be
            // relatively safe about assumptions regarding our current insets.
            if (!CENTER_REGION.equals(region))
                resetSplitDividerLocation();
        }
        return success;
    }

    private void resetSplitDividerLocation() {
        Component c = getDockedComponent();
        if (c instanceof JSplitPane)
            deferSplitDividerReset((JSplitPane) c);
    }

    private void deferSplitDividerReset(final JSplitPane splitPane) {
        applySplitDividerLocation(splitPane);
        // we don't need to defer split divider location reset until after
        // a DockingSplitPane has rendered, since that class is able to figure
        // out its proper divider location by itself.
        if (splitPane instanceof DockingSplitPane) {
            return;
        }

        // check to see if we've rendered
        int size = SwingUtility.getSplitPaneSize(splitPane);
        if (splitPane.isVisible() && size > 0 && EventQueue.isDispatchThread()) {
            // if so, apply the split divider location and return
            applySplitDividerLocation(splitPane);
            splitPane.validate();
            return;
        }

        // otherwise, defer applying the divider location reset until
        // the split pane is rendered.
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                deferSplitDividerReset(splitPane);
            }
        });
    }

    private void applySplitDividerLocation(JSplitPane splitPane) {
        DockingStrategy strategy = DockingManager.getDockingStrategy(this);
        int loc = strategy.getInitialDividerLocation(this, splitPane);
        splitPane.setDividerLocation(loc);
    }

    private boolean dockInCenterRegion(Component comp) {
        Component docked = getDockedComponent();
        JTabbedPane tabs = null;

        if (docked instanceof JTabbedPane) {
            tabs = (JTabbedPane) docked;
            addTab(tabs, comp);
            tabs.revalidate();
            tabs.setSelectedIndex(tabs.getTabCount() - 1);
            return true;
        }

        tabs = createTabbedPane();
        // createTabbedPane() is protected and may be overridden, so we'll have
        // to check for a possible null case here. Though why anyone would
        // return a null, I don't know. Maybe we should throw a
        // NullPointerException instead.
        if (tabs == null)
            return false;

        // remove the currently docked component and add it to the tabbed pane
        if (docked != null) {
            remove(docked);
            addTab(tabs, docked);
        }

        // add the new component to the tabbed pane
        addTab(tabs, comp);

        // now add the tabbed pane back to the main container
        setComponent(tabs);
        tabs.setSelectedIndex(tabs.getTabCount() - 1);
        return true;
    }

    private void addTab(JTabbedPane tabs, Component comp) {
        String tabText = getValidTabTitle(tabs, comp);
        tabs.add(comp, tabText);
        Dockable d = DockingManager.getDockable(comp);
        if (d == null)
            return;

        Icon icon = d.getDockingProperties().getTabIcon();
        int indx = tabs.getTabCount() - 1;
        tabs.setIconAt(indx, icon);
    }

    private boolean dockInOuterRegion(Component comp, String region) {
        // cache the current size and cut it in half for later in the method.
        Dimension halfSize = getSize();
        halfSize.width /= 2;
        halfSize.height /= 2;

        // remove the old docked content. we'll be adding it to another
        // dockingPort.
        Component docked = getDockedComponent();
        remove(docked);

        // add the components to their new parents.
        DockingStrategy strategy = getDockingStrategy();
        DockingPort oldContent = strategy.createDockingPort(this);
        DockingPort newContent = strategy.createDockingPort(this);
        addCmp(oldContent, docked);
        dockCmp(newContent, comp);

        JSplitPane newDockedContent = strategy.createSplitPane(this, region);

        // put the ports in the correct order and add them to a new wrapper
        // panel
        DockingPort[] ports = putPortsInOrder(oldContent, newContent, region);

        if (ports[0] instanceof JComponent) {
            ((JComponent) ports[0]).setMinimumSize(new Dimension(0, 0));
        }
        if (ports[1] instanceof JComponent) {
            ((JComponent) ports[1]).setMinimumSize(new Dimension(0, 0));
        }

        if (ports[0] instanceof Component)
            newDockedContent.setLeftComponent((Component) ports[0]);
        if (ports[1] instanceof Component)
            newDockedContent.setRightComponent((Component) ports[1]);

        // set the split in the middle
        double ratio = .5;

        if (docked instanceof Dockable
                && newDockedContent instanceof DockingSplitPane) {
            Float siblingRatio = ((Dockable) docked).getDockingProperties()
                                 .getSiblingSize(region);
            if (siblingRatio != null) {
                ratio = siblingRatio.doubleValue();
            }

            ((DockingSplitPane) newDockedContent).setInitialDividerRatio(ratio);
        }
        newDockedContent.setDividerLocation(ratio);

        // now set the wrapper panel as the currently docked component
        setComponent(newDockedContent);
        // if we're currently showing, then we can exit now
        if (isShowing())
            return true;

        // otherwise, we have unrealized components whose sizes cannot be
        // determined until after we're visible. cache the desired size
        // values now for use later during rendering.
        double proportion = strategy.getDividerProportion(this,
                            newDockedContent);
        SwingUtility.putClientProperty((Component) oldContent,
                                       DefaultDockingStrategy.PREFERRED_PROPORTION, new Float(
                                           proportion));
        SwingUtility.putClientProperty((Component) newContent,
                                       DefaultDockingStrategy.PREFERRED_PROPORTION, new Float(
                                           1f - proportion));

        return true;
    }

    /**
     * Returns the child {@code Component} currently embedded within with
     * {@code DockingPort}. If the {@code DockingPort} is empty, then this
     * method returns a {@code null} reference. If there is a single
     * {@code Dockable} docked within it with no tabbed layout, then the
     * {@code Component} for that {@code Dockable} is returned per its
     * {@code getComponent()} method. If there is a tabbed layout present, then
     * a {@code JTabbedPane} is returned. If there is a split layout present,
     * then a {@code JSplitPane} is returned.
     *
     * @see DockingPort#getDockedComponent()
     */
    public Component getDockedComponent() {
        return dockedComponent;
    }

    // private JSplitPane getDockedSplitPane() {
    // Component docked = getDockedComponent();
    // return docked instanceof JSplitPane? (JSplitPane)docked: null;
    // }

    /**
     * Returns a {@code String} identifier that is unique to
     * {@code DockingPorts} within a JVM instance, but persistent across JVM
     * instances. This is used for configuration mangement, allowing the JVM to
     * recognize a {@code DockingPort} instance within an application instance,
     * persist the ID, and recall it in later application instances. The ID
     * should be unique within an appliation instance so that there are no
     * collisions with other {@code DockingPort} instances, but it should also
     * be consistent from JVM to JVM so that the association between a
     * {@code DockingPort} instance and its ID can be remembered from session to
     * session.
     * <p>
     * The value returned by this method will come from the most recent call to
     * {@code setPersistentId(String id)}. If
     * {@code setPersistentId(String id)} was invoked with a {@code null}
     * argument, then the {@code String} verion of this {@code DockingPort's}
     * hash code is used. Therefore, this method will never return a
     * {@code null} reference.
     *
     * @return the persistent ID for this {@code DockingPort}
     * @see DockingPort#getPersistentId()
     * @see #setPersistentId(String)
     * @see DockingManager#getDockingPort(String)
     */
    public String getPersistentId() {
        return persistentId;
    }

    /**
     * Sets the persisent ID to be used for this {@code DockingPort}. If
     * {@code id} is {@code null}, then the {@code String} value of this
     * {@code DockingPort's} hash code is used.
     * <p>
     * {@code DockingPorts} are tracked by persistent ID within
     * {@code DockingManager}. Whenever this method is called, the
     * {@code DockingManager's} tracking mechanism is automatically upated for
     * this {@code DockingPort}.
     *
     * @param id
     *            the persistent ID to be used for this {@code DockingPort}
     * @see #getPersistentId()
     * @see DockingManager#getDockingPort(String)
     * @see DockingPortTracker#updateIndex(DockingPort)
     */
    public void setPersistentId(String id) {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        persistentId = id;
        DockingPortTracker.updateIndex(this);
    }

    private String getValidTabTitle(JTabbedPane tabs, Component comp) {
        String title = (String) COMPONENT_TITLES.get(comp);
        if (title == null || title.trim().length() == 0)
            title = "null";

        int tc = tabs.getTabCount();
        int occurrances = 0;
        HashSet titles = new HashSet();
        String tmp = null;
        for (int i = 0; i < tc; i++) {
            tmp = tabs.getTitleAt(i).toLowerCase();
            titles.add(tmp);
            if (tmp.startsWith(title.toLowerCase()))
                occurrances++;
        }

        if (titles.contains(title) && occurrances > 0)
            title += occurrances;

        COMPONENT_TITLES.put(comp, title);
        return title;
    }

    /**
     * Returns {@code true} if single tabs are allowed within this
     * {@code DockingPort}, {@code false} otherwise.
     * <p>
     * Generally the tabbed interface does not appear until two or more
     * {@code Dockables} are docked to the {@code CENTER_REGION} of the
     * {@code DockingPort} and tabs are required to switch between them. When
     * there is only a single {@code Dockable} within the {@code DockingPort},
     * the default behavior for the dockable {@code Component} to take up all of
     * the space within the {@code DockingPort}.
     * <p>
     * If this method returns {@code true}, then a single {@code Dockable}
     * within this {@code DockingPort} will reside within a tabbed layout that
     * contains only one tab.
     * <p>
     * The value returned by this method is a scoped property. This means there
     * may be many different "scopes" at which the single-tab property may be
     * set. For instance, a "global" setting may override the individual setting
     * for this {@code DockingPort}, and this {@code DockingPort's} particular
     * setting may override the global default setting.
     * {@code org.flexdock.docking.props.PropertyManager} should be referenced
     * for further information on scoped properties.
     *
     * @return {@code true} if single tabs are allowed within this
     *         {@code DockingPort}, {@code false} otherwise.
     * @see #setSingleTabAllowed(boolean)
     * @see DockingManager#isSingleTabsAllowed()
     * @see DockingManager#setSingleTabsAllowed(boolean)
     * @see PropertyManager
     * @see DockingPortPropertySet#isSingleTabsAllowed()
     * @see DockingPortPropertySet#setSingleTabsAllowed(boolean)
     */
    public boolean isSingleTabAllowed() {
        return getDockingProperties().isSingleTabsAllowed().booleanValue();
    }

    /**
     * Sets the "single tab" property for this {@code DockingPort}, allowing or
     * disallowing a single {@code Dockable} within the {@code DockingPort} to
     * appear within a tabbed layout.
     * <p>
     * Generally the tabbed interface does not appear until two or more
     * {@code Dockables} are docked to the {@code CENTER_REGION} of the
     * {@code DockingPort} and tabs are required to switch between them. When
     * there is only a single {@code Dockable} within the {@code DockingPort},
     * the default behavior for the dockable {@code Component} to take up all of
     * the space within the {@code DockingPort}.
     * <p>
     * If the single tab property is set to {@code true}, then a single
     * {@code Dockable} within this {@code DockingPort} will reside within a
     * tabbed layout that contains only one tab.
     * <p>
     * The single tab property is a scoped property. This means there may be
     * many different "scopes" at which the single-tab property may be set. For
     * instance, a "global" setting may override the individual setting for this
     * {@code DockingPort}, and this {@code DockingPort's} particular setting
     * may override the global default setting. <b>This method applied a value
     * only to the local scope for this particular {@code DockingPort}.</b>
     * {@code org.flexdock.docking.props.PropertyManager} should be referenced
     * for further information on scoped properties.
     *
     * @param allowed
     *            {@code true} if a single-tabbed layout should be allowed,
     *            {@code false} otherwise
     * @see #isSingleTabAllowed()
     * @see DockingManager#setSingleTabsAllowed(boolean)
     * @see DockingManager#isSingleTabsAllowed()
     * @see PropertyManager
     * @see DockingPortPropertySet#setSingleTabsAllowed(boolean)
     * @see DockingPortPropertySet#isSingleTabsAllowed()
     */
    public void setSingleTabAllowed(boolean allowed) {
        getDockingProperties().setSingleTabsAllowed(allowed);
    }

    /**
     * Indicates whether or not the specified component is docked somewhere
     * within this {@code DefaultDockingPort}. This method returns {@code true}
     * if the specified {@code Component} is a direct child of the
     * {@code DefaultDockingPort} or is a direct child of a {@code JTabbedPane}
     * or {@code JSplitPane}that is currently the {@code DefaultDockingPort's}docked
     * component. Otherwise, this method returns {@code false}. If {@code comp}
     * is {@code null}, then then this method return {@code false}
     *
     * @param comp
     *            the Component to be tested.
     * @return a boolean indicating whether or not the specified component is
     *         docked somewhere within this {@code DefaultDockingPort}.
     * @see DockingPort#isParentDockingPort(java.awt.Component)
     * @see Component#getParent()
     * @see #getDockedComponent()
     */
    public boolean isParentDockingPort(Component comp) {
        if (comp == null)
            return false;

        Container parent = comp.getParent();
        // if the component has no parent, then it can't be docked within us
        if (parent == null)
            return false;

        // if we're the direct parent of this component, then we're the parent
        // docking port
        if (parent == this)
            return true;

        // if the component is directly inside our docked component, then we're
        // also considered its parent dockingPort
        return parent == getDockedComponent();
    }

    protected boolean isValidDockingRegion(String region) {
        return DockingManager.isValidDockingRegion(region);
    }

    private boolean isSingleComponentDocked() {
        Component c = getDockedComponent();
        // we have no docked component
        if (c == null)
            return false;

        // we do have a docked component. It'll be a splitpane, a tabbedpane,
        // or something else.

        // if it's a splitpane, then we definitely have more than one component
        // docked
        if (c instanceof JSplitPane)
            return false;

        // if it's a tabbed pane, then check the number of tabs on the pane
        if (c instanceof JTabbedPane) {
            return ((JTabbedPane) c).getTabCount() == 1;
        }

        // splitpane and tabbed pane are the only two subcontainers that signify
        // more than one docked component. if neither, then we only have one
        // component docked.
        return true;
    }

    protected Dockable getCenterDockable() {
        // can't have a CENTER dockable if there's nothing in the center
        if (!isSingleComponentDocked())
            return null;

        // get the component in the CENTER
        Component c = getDockedComponent();
        if (c instanceof JTabbedPane) {
            // if in a tabbed pane, get the first component in there.
            // (there will only be 1, since we've already passed the
            // isSingleComponentDocked() test)
            c = ((JTabbedPane) c).getComponent(0);
        }
        // return the Dockable instance associated with this component
        return DockingManager.getDockable(c);
    }

    private DockingPort[] putPortsInOrder(DockingPort oldPort,
                                          DockingPort newPort, String region) {
        if (NORTH_REGION.equals(region) || WEST_REGION.equals(region))
            return new DockingPort[] { newPort, oldPort };
        return new DockingPort[] { oldPort, newPort };
    }

    /**
     * This method completes with a call to
     * {@code evaluateDockingBorderStatus()} to allow any installed
     * {@code DefaultDockingStrategy} to handle container-state-related
     * behavior.
     */
    private void reevaluateContainerTree() {
        reevaluateDockingWrapper();
        reevaluateTabbedPane();

        evaluateDockingBorderStatus();
    }

    private void reevaluateDockingWrapper() {
        Component docked = getDockedComponent();
        Container parent = getParent();
        Container grandParent = parent == null ? null : parent.getParent();

        // added grandparent check up here so we will be able to legally embed a
        // DefaultDockingPort within a plain JSplitPane without triggering an
        // unnecessary remove()
        if (docked == null && parent instanceof JSplitPane
                && grandParent instanceof DefaultDockingPort) {
            // in this case, the docked component has disappeared (removed) and
            // our parent component is a wrapper for us and our child so that we
            // can share the root docking port with another component. since our
            // child is gone, there's no point in our being here anymore and our
            // sibling component shouldn't have to share screen real estate with
            // us anymore. we'll remove ourselves and notify the root docking
            // port that the component tree has been modified.
            parent.remove(this);
            ((DefaultDockingPort) grandParent).reevaluateContainerTree(); // LABEL
            // 1
        } else if (docked instanceof JSplitPane) {
            // in this case, we're the parent of a docking wrapper. this implies
            // that we're splitting our real estate between two components. (in
            // practice, we're actually the parent that was called above at
            // LABEL
            // 1).
            JSplitPane wrapper = (JSplitPane) docked;
            Component left = wrapper.getLeftComponent();
            Component right = wrapper.getRightComponent();

            // first, check to make sure we do in fact have 2 components. if so,
            // then we don't have
            // to go any further.
            if (left != null && right != null)
                return;

            // check to see if we have zero components. if so, remove everything
            // and return.
            if (left == right) {
                removeAll();
                return;
            }

            // if we got here, then one of our components has been removed (i.e.
            // LABEL 1). In this
            // case, we want to pull the remaining content out of its
            // split-wrapper and add it
            // as a direct child to ourselves.
            Component comp = left == null ? right : left;
            wrapper.remove(comp);

            // do some cleanup on the wrapper before removing it
            if (wrapper instanceof DockingSplitPane) {
                ((DockingSplitPane) wrapper).cleanup();
            }
            super.remove(wrapper);

            if (comp instanceof DefaultDockingPort)
                comp = ((DefaultDockingPort) comp).getDockedComponent();

            if (comp != null)
                setComponent(comp);
        }
    }

    private void reevaluateTabbedPane() {
        Component docked = getDockedComponent();
        if (!(docked instanceof JTabbedPane))
            return;

        JTabbedPane tabs = (JTabbedPane) docked;
        int tabCount = tabs.getTabCount();
        // we don't have to do anything special here if there is more than the
        // minimum number of allowable tabs
        int minTabs = isSingleTabAllowed() ? 0 : 1;
        if (tabCount > minTabs) {
            return;
        }

        // otherwise, pull out the component in the remaining tab (if it
        // exists), and add it to ourselves as a direct child (ditching the
        // JTabbedPane).
        Component comp = tabCount == 1 ? tabs.getComponentAt(0) : null;
        removeAll();
        if (comp != null)
            setComponent(comp);

        Container parent = getParent();
        Container grandParent = parent == null ? null : parent.getParent();
        // if our TabbedPane's last component was removed, then the TabbedPane
        // itself has now been removed. if we're a child port within a
        // JSplitPane
        // within another DockingPort, then we ourselved need to be removed from
        // the component tree, since we don't have any content.
        if (comp == null && parent instanceof JSplitPane
                && grandParent instanceof DefaultDockingPort) {
            parent.remove(this);
            ((DefaultDockingPort) grandParent).reevaluateContainerTree();
        }
    }

    /**
     * Overridden to decorate superclass method, keeping track of internal
     * docked-component reference.
     *
     * @param index
     *            the index of the component to be removed.
     * @see Container#remove(int)
     */
    public void remove(int index) {
        Component docked = getDockedComponent();
        Component comp = getComponent(index);
        super.remove(index);
        if (docked == comp)
            dockedComponent = null;
    }

    /**
     * Overridden to decorate superclass method, keeping track of internal
     * docked-component reference.
     *
     * @see Container#removeAll()
     */
    public void removeAll() {
        super.removeAll();
        dockedComponent = null;
    }

    /**
     * Sets the currently installed {@code BorderManager}. This method provides
     * a means of customizing border managment following any successful call to
     * {@code dock(Dockable dockable, String region)} or
     * {@code undock(Component comp)}, allowing cleanup of borders for nested
     * {@code Components} within the docking layout. {@code null} values are
     * allowed.
     *
     * @param mgr
     *            the {@code BorderManager} assigned to to manage docked
     *            component borders.
     * @see #getBorderManager()
     * @see BorderManager
     */
    public void setBorderManager(BorderManager mgr) {
        borderManager = mgr;
    }

    /**
     * Returns the currently intalled {@code BorderManager}. The
     * {@code BorderManager} is used any time a successful call to
     * {@code dock(Dockable dockable, String region)} or
     * {@code undock(Component comp)} has been issued to clean up borders for
     * nested {@code Components} within the docking layout. This method will
     * return a {@code null} reference if there is no {@code BorderManager}
     * installed.
     *
     * @return the currently installed {@code BorderManager}.
     * @see #setBorderManager(BorderManager)
     * @see BorderManager
     */
    public BorderManager getBorderManager() {
        return borderManager;
    }

    private Component setComponent(Component c) {
        if (getDockedComponent() != null)
            removeAll();

        dockedComponent = c;
        Component ret = super.add(dockedComponent);

        // calling doLayout here to properly set the component's size
        // validate throws an error
        doLayout();

        return ret;
    }

    /**
     * Undocks the specified {@code Component} and returns a boolean indicating
     * the success of the operation.
     * <p>
     * Since {@code DefaultDockingPort} may only contain one child component,
     * there i s a container hierarchy to manage tabbed interfaces, split
     * layouts, and sub-docking. As components are removed from this hierarchy,
     * the hierarchy itself must be reevaluated. Removing a component from a
     * child code>DefaultDockingPort} within a {@code JSplitPane} renders the
     * child {@code DefaultDockingPort} unnecessary, which, in turn, renders the
     * notion of splitting the layout with a {@code JSplitPane} unnecessary
     * (since there are no longer two components to split the layout between).
     * Likewise, removing a child component from a {@code JTabbedPane} such that
     * there is only one child left within the {@code JTabbedPane} removes the
     * need for a tabbed interface to begin with.
     * <p>
     * This method automatically handles the reevaluation of the container
     * hierarchy to keep wrapper-container usage at a minimum. Since
     * {@code DockingManager} makes this callback automatic, developers normally
     * will not need to call this method explicitly. However, when removing a
     * component from a {@code DefaultDockingPort} using application code,
     * developers should keep in mind to use this method instead of
     * {@code remove()}.
     *
     * @param comp
     *            the {@code Component} to be undocked.
     * @return a boolean indicating the success of the operation
     * @see DockingPort#undock(Component comp)
     * @see DockingManager#undock(Dockable)
     */
    public boolean undock(Component comp) {
        // can't undock a component that isn't already docked within us
        if (!isParentDockingPort(comp))
            return false;

        // remove the component
        comp.getParent().remove(comp);

        // reevaluate the container tree.
        reevaluateContainerTree();

        return true;
    }

    /**
     * Returns all {@code Dockables} docked within this {@code DockingPort} and
     * all sub-{@code DockingPorts}. The returned {@code Set} will contain
     * {@code Dockable} instances. If there are no {@code Dockables} present, an
     * empty {@code Set} will be returned. This method will never return a
     * {@code null} reference.
     *
     * @return all {@code Dockables} docked within this {@code DockingPort} and
     *         all sub-{@code DockingPorts}.
     * @see DockingPort#getDockables()
     */
    public Set getDockables() {
        // return ALL dockables, recursing to maximum depth
        return getDockableSet(-1, 0, null);
    }

    protected Set getDockableSet(int depth, int level, Class desiredClass) {
        Component c = getDockedComponent();

        if (c instanceof JTabbedPane) {
            JTabbedPane tabs = (JTabbedPane) c;
            int len = tabs.getTabCount();
            HashSet set = new HashSet(len);
            for (int i = 0; i < len; i++) {
                c = tabs.getComponentAt(i);
                if (isValidDockableChild(c, desiredClass)) {
                    if (c instanceof Dockable)
                        set.add(c);
                    else
                        set.add(DockingManager.getDockable(c));
                }
            }
            return set;
        }

        HashSet set = new HashSet(1);

        // if we have a split-layout, then we need to decide whether to get the
        // child viewSets. If 'depth' is less then zero, then it's implied we
        // want to recurse to get ALL child viewsets no matter how deep. If
        // 'depth' is greater than or equal to zero, we only want to go as deep
        // as the specified depth.
        if (c instanceof JSplitPane && (depth < 0 || level <= depth)) {
            JSplitPane pane = (JSplitPane) c;
            Component sub1 = pane.getLeftComponent();
            Component sub2 = pane.getRightComponent();

            if (sub1 instanceof DefaultDockingPort)
                set.addAll(((DefaultDockingPort) sub1).getDockableSet(depth,
                           level + 1, desiredClass));

            if (sub2 instanceof DefaultDockingPort)
                set.addAll(((DefaultDockingPort) sub2).getDockableSet(depth,
                           level + 1, desiredClass));
        }

        if (isValidDockableChild(c, desiredClass)) {
            if (c instanceof Dockable)
                set.add(c);
            else
                set.add(DockingManager.getDockable(c));
        }
        return set;
    }

    protected boolean isValidDockableChild(Component c, Class desiredClass) {
        return desiredClass == null ? DockingManager.getDockable(c) != null
               : desiredClass.isAssignableFrom(c.getClass());
    }

    /**
     * Adds a {@code DockingListener} to observe docking events for this
     * {@code DockingPort}. {@code null} arguments are ignored.
     *
     * @param listener
     *            the {@code DockingListener} to add to this {@code DockingPort}.
     * @see DockingMonitor#addDockingListener(DockingListener)
     * @see #getDockingListeners()
     * @see #removeDockingListener(DockingListener)
     */
    public void addDockingListener(DockingListener listener) {
        if (listener != null)
            dockingListeners.add(listener);
    }

    /**
     * Returns an array of all {@code DockingListeners} added to this
     * {@code DockingPort}. If there are no listeners present for this
     * {@code DockingPort}, then a zero-length array is returned.
     *
     * @return an array of all {@code DockingListeners} added to this
     *         {@code DockingPort}.
     * @see DockingMonitor#getDockingListeners()
     * @see #addDockingListener(DockingListener)
     * @see #removeDockingListener(DockingListener)
     */
    public DockingListener[] getDockingListeners() {
        return (DockingListener[]) dockingListeners
               .toArray(new DockingListener[0]);
    }

    /**
     * Removes the specified {@code DockingListener} from this
     * {@code DockingPort}. If the specified {@code DockingListener} is
     * {@code null}, or the listener has not previously been added to this
     * {@code DockingPort}, then no {@code Exception} is thrown and no action
     * is taken.
     *
     * @param listener
     *            the {@code DockingListener} to remove from this
     *            {@code DockingPort}
     * @see DockingMonitor#removeDockingListener(DockingListener)
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
     * Requests activation for the newly docked Dockable.
     *
     * @param evt
     *            the {@code DockingEvent} to respond to.
     * @see DockingListener#dockingComplete(DockingEvent)
     */
    public void dockingComplete(DockingEvent evt) {
        Dockable dockable = evt.getDockable();
        if (dockable == null || !isShowing() || evt.getNewDockingPort() != this)
            return;

        ActiveDockableTracker
        .requestDockableActivation(dockable.getComponent());
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
     * Returns a {@code DockingPortPropertySet} instance associated with this
     * {@code DockingPort}. This method returns the default implementation
     * supplied by the framework by invoking
     * {@code getDockingPortPropertySet(DockingPort port)} on
     * {@code org.flexdock.docking.props.PropertyManager} and supplying an
     * argument of {@code this}.
     *
     * @return the {@code DockingPortPropertySet} associated with this
     *         {@code DockingPort}. This method will not return a {@code null}
     *         reference.
     * @see DockingPortPropertySet
     * @see DockingPort#getDockingProperties()
     * @see org.flexdock.docking.props.PropertyManager#getDockingPortPropertySet(DockingPort)
     */
    public DockingPortPropertySet getDockingProperties() {
        return PropertyManager.getDockingPortPropertySet(this);
    }

    /**
     * Enables or disables drag support for docking operations on the tabs used
     * within an embedded tabbed layout. If tab-drag-source is enabled, then the
     * tab that corresponds to a {@code Dockable} within an embedded tabbed
     * layout will respond to drag events as if the tab were a component
     * included within the {@code List} returned by calling
     * {@code getDragSources()} on the {@code Dockable}. This allows dragging a
     * tab to initiate drag-to-dock operations.
     *
     * @param enabled
     *            {@code true} if drag-to-dock support should be enabled for
     *            tabs and their associated {@code Dockables}, {@code false}
     *            otherwise.
     * @see #isTabsAsDragSource()
     * @see Dockable#getDragSources()
     */
    public void setTabsAsDragSource(boolean enabled) {
        tabsAsDragSource = enabled;
    }

    /**
     * Returns {@code true} if drag-to-dock support is enabled for tabs and
     * their associated {@code Dockables}, {@code false} otherwise. If
     * tab-drag-source is enabled, then the tab that corresponds to a
     * {@code Dockable} within an embedded tabbed layout will respond to drag
     * events as if the tab were a component included within the {@code List}
     * returned by calling {@code getDragSources()} on the {@code Dockable}.
     * This allows dragging a tab to initiate drag-to-dock operations.
     *
     * @return {@code true} if drag-to-dock support is enabled for tabs and
     *         their associated {@code Dockables}, {@code false} otherwise.
     * @see #setTabsAsDragSource(boolean)
     * @see Dockable#getDragSources()
     */
    public boolean isTabsAsDragSource() {
        return tabsAsDragSource;
    }

    protected int getInitTabPlacement() {
        return getDockingProperties().getTabPlacement().intValue();
    }

    /**
     * Returns a boolean indicating whether or not this {@code DockingPort} is
     * nested within another {@code DockingPort}. If there are no other
     * {@code DockingPorts} within this {@code DockingPort's} container ancestor
     * hierarchy, then this method will return {@code true}. Otherwise, this
     * method will return {@code false}. If the this {@code DockingPort} is not
     * validated and/or is not part of a container hierarchy, this method should
     * return {@code true}.
     *
     * @return {@code false} if this {@code DockingPort} is nested within
     *         another {@code DockingPort}, {@code true} otherwise.
     * @see DockingPort#isRoot()
     */
    public boolean isRoot() {
        return rootPort;
    }

    /**
     * This method is used internally by the framework to notify
     * {@code DefaultDockingPorts} whether they are "root" {@code DockingPorts}
     * according to the rules specified by {@code isRoot()} on the
     * {@code DockingPort} interface. <b>This method should not be called by
     * application-level developers.</b> It will most likely be removed in
     * future versions and the logic contained herein will be managed by some
     * type of change listener.
     *
     * @param root
     *            {@code true} if this is a "root" {@code DockingPort},
     *            {@code false} otherwise.
     * @see #isRoot()
     * @see DockingPort#isRoot()
     */
    public void setRoot(boolean root) {
        this.rootPort = root;
    }

    /**
     * This method is used internally by the framework to notify
     * {@code DefaultDockingPorts} whether a drag operation is or is not
     * currently in progress and should not be called by application-level
     * developers. It will most likely be removed in future versions and the
     * logic contained herein will be managed by some type of change listener.
     *
     * @param inProgress
     *            {@code true} if a drag operation involving this
     *            {@code DockingPort} is currently in progress, {@code false}
     *            otherwise.
     */
    public void setDragInProgress(boolean inProgress) {
        if (inProgress && dragImage != null)
            return;

        if (!inProgress && dragImage == null)
            return;

        if (inProgress) {
            dragImage = SwingUtility.createImage(this);
        } else {
            dragImage = null;
        }
        repaint();
    }

    /**
     * Overridden to provide enhancements during drag operations. Some
     * {@code DragPreview} implementations may by able to supply a
     * {@code BufferedImage} for this {@code DockingPort} to use for painting
     * operations. This may be useful for cases in which the dimensions of
     * docked {@code Components} are altered in realtime during the drag
     * operation to provide a "ghost" image for the {@code DragPreview}. In
     * this case, visual feedback for altered subcomponents within this
     * {@code DockingPort} may be blocked in favor of a temporary
     * {@code BufferedImage} for the life of the drag operation.
     *
     * @param g
     *            the {@code Graphics} context in which to paint
     * @see JComponent#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        if (dragImage == null) {
            super.paint(g);
            return;
        }

        g.drawImage(dragImage, 0, 0, this);
    }

    /**
     * Returns a {@code LayoutNode} containing metadata that describes the
     * current layout contained within this {@code DefaultDockingPort}. The
     * {@code LayoutNode} returned by this method will be a
     * {@code DockingPortNode} that constitutes the root of a tree structure
     * containing various {@code DockingNode} implementations; specifically
     * {@code SplitNode}, {@code DockableNode}, and {@code DockingPortNode}.
     * Each of these nodes is {@code Serializable}, implying the
     * {@code LayoutNode} itself may be written to external storage and later
     * reloaded into this {@code DockingPort} via
     * {@code importLayout(LayoutNode node)}.
     *
     * @return a {@code LayoutNode} representing the current layout state within
     *         this {@code DockingPort}
     * @see DockingPort#importLayout(LayoutNode)
     * @see #importLayout(LayoutNode)
     * @see org.flexdock.docking.state.LayoutManager#createLayout(DockingPort)
     * @see LayoutNode
     * @see org.flexdock.docking.state.tree.DockingNode
     * @see DockingPortNode
     * @see SplitNode
     * @see DockableNode
     */
    public LayoutNode exportLayout() {
        return DockingManager.getLayoutManager().createLayout(this);
    }

    /**
     * Clears out the existing layout within this {@code DockingPort} and
     * reconstructs a new layout based upon the specified {@code LayoutNode}.
     * <p>
     * At present, this method can only handle {@code LayoutNodes} that have
     * been generated by {@code DefaultDockingPort's} {@code exportLayout()}
     * method. If the specified {@code LayoutNode} is {@code null} or is
     * otherwise <i>not</i> an instance of {@code DockingPortNode}, then this
     * method returns immediately with no action taken.
     * <p>
     * Otherwise, the necessary {@code Dockables} are docked within this
     * {@code DockingPort} and all subsequently generated sub-{@code DockingPorts}
     * in a visual configuration mandated by the tree structure modeled by the
     * specified {@code LayoutNode}.
     *
     * @param node
     *            the {@code LayoutNode} whose layout is to be instantiated
     *            within this {@code DockingPort}
     * @see DockingPort#importLayout(LayoutNode)
     * @see #exportLayout()
     * @see LayoutNode
     * @see org.flexdock.docking.state.tree.DockingNode
     * @see DockingPortNode
     * @see SplitNode
     * @see DockableNode
     */
    public void importLayout(LayoutNode node) {
        if (!(node instanceof DockingPortNode))
            return;

        node.setUserObject(this);
        ArrayList splitPaneResizeList = new ArrayList();
        constructLayout(node, splitPaneResizeList);
        deferSplitPaneValidation(splitPaneResizeList);
        revalidate();
    }

    private void constructLayout(LayoutNode node, ArrayList splitPaneResizeList) {
        // load the user object. this object isn't used here, but
        // LayoutNode should have a lazy-load mechanism for loading of
        // userObject at runtime. we just want to make sure the userObject has
        // been loaded before we proceed.
        Object obj = node.getUserObject();
        if (node instanceof SplitNode)
            splitPaneResizeList.add(node);

        for (Enumeration en = node.children(); en.hasMoreElements();) {
            LayoutNode child = (LayoutNode) en.nextElement();
            constructLayout(child, splitPaneResizeList);
        }

        if (node instanceof SplitNode)
            reconstruct((SplitNode) node);
        else if (node instanceof DockingPortNode)
            reconstruct((DockingPortNode) node);
    }

    private void reconstruct(DockingPortNode node) {
        DefaultDockingPort port = (DefaultDockingPort) node.getDockingPort();

        if (node.isSplit()) {
            SplitNode child = (SplitNode) node.getChildAt(0);
            JSplitPane split = child.getSplitPane();
            // float percentage = child.getPercentage();
            port.setComponent(split);
            port.evaluateDockingBorderStatus();
            return;
        }

        for (Enumeration en = node.children(); en.hasMoreElements();) {
            LayoutNode child = (LayoutNode) en.nextElement();
            if (child instanceof DockableNode) {
                Dockable dockable = ((DockableNode) child).getDockable();
                port.dock(dockable, CENTER_REGION);
            }
        }
    }

    private void reconstruct(SplitNode node) {
        JSplitPane split = node.getSplitPane();
        Component left = node.getLeftComponent();
        Component right = node.getRightComponent();
        split.setLeftComponent(left);
        split.setRightComponent(right);
    }

    private void deferSplitPaneValidation(final ArrayList splitNodes) {
	if (timer == null) {
	    timer = new Timer(15, new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			Runnable r = new Runnable() {
				public void run() {
				    synchronized (lock) {
					if (timer != null) {
					    processImportedSplitPaneValidation(splitNodes);
					}
				    }
				}
			    };
			EventQueue.invokeLater(r);
		    }
		});
	    timer.setRepeats(true);
	    timer.start();
	}
    }

    private void processImportedSplitPaneValidation(ArrayList splitNodes) {
        synchronized (lock) {
	    int len = splitNodes.size();
	    if (len == 0) {
		timer.stop();
		timer = null;
		return;
	    }
	    
	    for (int i = 0; i < len; i++) {
		((SplitNode) splitNodes.get(i)).getSplitPane().setVisible(false);
	    }
	    
	    // first, check to see if we're ready for rendering
	    SplitNode node = (SplitNode) splitNodes.get(0);
	    JSplitPane split = node.getSplitPane();
	    int size = split.getOrientation() == JSplitPane.HORIZONTAL_SPLIT ? split.getWidth() : split.getHeight();
	    // if we're not ready to render, then defer processing again until later
	    if (!split.isValid() || size == 0) {
		// try to validate first
		if (!split.isValid())
		    split.validate();
		// now redispatch
		return;
	    }
	    
	    timer.stop();
	    timer = null;
	    
	    // if we're ready to render, then loop through all the splitNodes and
	    // set the split dividers to their appropriate locations.
	    for (int i = 0; i < len; i++) {
		node = (SplitNode) splitNodes.get(i);
		split = node.getSplitPane();
		size = split.getOrientation() == JSplitPane.HORIZONTAL_SPLIT ? split.getWidth() : split.getHeight();
		float percent = node.getPercentage();
		int divLoc = (int) ((float) size * percent);
		// System.err.println("percent: " + percent + ", divLoc: " +
		// divLoc);
		split.setDividerLocation(percent);
		
		// make sure to invoke the installed BorderManager how that we have
		// a hierarchy of DockingPorts. otherwise, we may end up with some
		// ugly nested borders.
		DockingPort port = DockingUtility.getParentDockingPort(split);
		if (port instanceof DefaultDockingPort) {
		    ((DefaultDockingPort) port).evaluateDockingBorderStatus();
		}

		split.validate();
	    }
	    
	    for (int i = 0; i < len; i++) {
		((SplitNode) splitNodes.get(i)).getSplitPane().setVisible(true);
	    }
	}
    }

    // --- maximization

    // remember information to restore state after maximized dockable has left
    // us again
    private MaximizationInstallInfo maximizationInstallInfo;

    public void installMaximizedDockable(Dockable dockable) {
        if (maximizationInstallInfo != null) {
            throw new IllegalStateException("Already maximized");
        }
        maximizationInstallInfo = new MaximizationInstallInfo(
            getDockedComponent(), getBorder());

        Component newComponent = dockable.getComponent();
        setComponent(newComponent);
        evaluateDockingBorderStatus();
        revalidate();
    }

    public void uninstallMaximizedDockable() {
        if (maximizationInstallInfo == null) {
            throw new IllegalStateException("No dockable maximized.");
        }

        setComponent(maximizationInstallInfo.getContent());
        setBorder(maximizationInstallInfo.getBorder());
        maximizationInstallInfo = null;
        revalidate();
    }

    // remember necessary information to restore state after maximized dockable
    // returns
    private MaximizationReleaseInfo maximizationReleaseInfo;

    public void releaseForMaximization(Dockable dockable) {
        if (maximizationReleaseInfo != null) {
            throw new IllegalStateException(
                "Already released a Dockable for maximization.");
        }

        Component comp = dockable.getComponent();
        Border border = null;
        if (comp instanceof JComponent) {
            border = ((JComponent) comp).getBorder();
        }
        maximizationReleaseInfo = new MaximizationReleaseInfo(comp, border);

        Component docked = getDockedComponent();
        if (docked == null) {
            throw new IllegalStateException("DefaultDockingPort is empty.");
        } else if (docked instanceof JSplitPane) {
            // this should never happen since in that case this DockingPort
            // can't
            // be the direct parent of any dockable requesting maximization
            throw new IllegalStateException(
                "DefaultDockingPort does not directly contain a Dockable");
        } else if (docked instanceof JTabbedPane) {
            // this is the tricky case, we have to store layout of tabbed pane
            // to restore later
            JTabbedPane tabs = (JTabbedPane) docked;
            maximizationReleaseInfo.setTabIndex(getTabIndex(tabs, comp));
            tabs.remove(comp);
        } else {
            // check if our component is the one that requested maximization
            if (comp != docked) {
                throw new IllegalStateException(
                    "Dockable requesting maximization is not the one docked in this DefaultDockingPort.");
            }
            remove(comp);
        }

    }

    private int getTabIndex(JTabbedPane tabs, Component comp) {
        int tabCount = tabs.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            if (tabs.getComponentAt(i) == comp) {
                return i;
            }
        }
        return -1;
    }

    public void returnFromMaximization() {

        Component comp = maximizationReleaseInfo.getContent();
        if (comp instanceof JComponent) {
            ((JComponent) comp).setBorder(maximizationReleaseInfo.getBorder());
        }
        int tabIndex = maximizationReleaseInfo.getTabIndex();
        maximizationReleaseInfo = null;

        Component docked = getDockedComponent();
        if (docked != null && docked instanceof JTabbedPane) {
            // return dockable to its original (TODO) position in the
            // JTabbedPane
            JTabbedPane tabs = (JTabbedPane) docked;
            tabs.add(comp, getValidTabTitle(tabs, comp), tabIndex);
            tabs.setSelectedIndex(tabIndex);
        } else {
            // return dockable as direct child
            setComponent(comp);
        }

        revalidate();
    }

    private static class MaximizationInstallInfo {
        private final Component content;

        private final Border border;

        public MaximizationInstallInfo(Component content, Border border) {
            this.content = content;
            this.border = border;
        }

        public Border getBorder() {
            return border;
        }

        public Component getContent() {
            return content;
        }
    }

    private static class MaximizationReleaseInfo extends
        MaximizationInstallInfo {
        private int tabIndex;

        public MaximizationReleaseInfo(Component content, Border border) {
            super(content, border);
        }

        public int getTabIndex() {
            return tabIndex;
        }

        public void setTabIndex(int tabIndex) {
            this.tabIndex = tabIndex;
        }
    }
}
