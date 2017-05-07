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
import java.awt.Window;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.WeakHashMap;

import javax.swing.SwingUtilities;



import org.flexdock.docking.activation.ActiveDockableListener;
import org.flexdock.docking.adapter.AdapterFactory;
import org.flexdock.docking.adapter.DockingAdapter;
import org.flexdock.docking.defaults.DefaultDockingStrategy;
import org.flexdock.docking.defaults.DockableComponentWrapper;
import org.flexdock.docking.drag.DragManager;
import org.flexdock.docking.drag.effects.DragPreview;
import org.flexdock.docking.drag.effects.EffectsManager;
import org.flexdock.docking.drag.effects.RubberBand;
import org.flexdock.docking.event.DockingEventHandler;
import org.flexdock.docking.event.hierarchy.DockingPortTracker;
import org.flexdock.docking.event.hierarchy.RootDockingPortInfo;
import org.flexdock.docking.floating.policy.FloatPolicyManager;
import org.flexdock.docking.props.DockablePropertySet;
import org.flexdock.docking.props.PropertyManager;
import org.flexdock.docking.state.DockingState;
import org.flexdock.docking.state.FloatManager;
import org.flexdock.docking.state.LayoutManager;
import org.flexdock.docking.state.MinimizationManager;
import org.flexdock.docking.state.PersistenceException;
import org.flexdock.event.EventManager;
import org.flexdock.event.RegistrationEvent;
import org.flexdock.util.ClassMapping;
import org.flexdock.util.DockingUtility;
import org.flexdock.util.ResourceManager;
import org.flexdock.util.RootWindow;
import org.flexdock.util.SwingUtility;
import org.flexdock.util.Utilities;

/**
 * This class is used as a public facade into the framework docking system. It
 * provides a straightforward public API for managing and manipulating the
 * various different subcomponents that make up the docking framework through a
 * single class. {@code DockingManager} cannot be instantiated. Rather, its
 * methods are accessed statically from within application code and it generally
 * defers processing to a set of abstract handlers hidden from the application
 * layer.
 *
 * Among {@code DockingManager's} responsibilities are as follows:
 *
 * <dl>
 * <dt>Maintaining a component repository.</dt>
 * <dd> All {@code Dockables} and {@code DockingPorts} are cached within an and
 * accessible through and internal registry. </dd>
 * <dt>Maintaining framework state.</dt>
 * <dd>{@code DockingManager} provides APIs for managing various different
 * global framework settings, including application-key, floating support,
 * auto-persistence, {@code LayoutManagers}, and {@code MinimizationManagers}.
 * </dd>
 * <dt>Behavioral auto-configuration.</dt>
 * <dd> {@code DockingManager} automatically adds and removes necessary event
 * listeners to enable/disable drag-to-dock behavior as components are
 * registered and unregistered. </dd>
 * <dt>Programmatic access to docking operations.</dt>
 * <dd> {@code DockingManager} provides public APIs for programmatically dock,
 * undock, minimize, persist, and load {@code Dockables} from storage. </dd>
 * </dl>
 *
 * @author Christopher Butler
 */
public class DockingManager implements DockingConstants {

    public static final String MINIMIZE_MANAGER = "minimize.manager";

    public static final String LAYOUT_MANAGER = "layout.manager";

    private static final String DEV_PROPS = "org/flexdock/util/dev-props.properties";

    private static final String CONFIG_PROPS = "org/flexdock/docking/flexdock-core.properties";

    private static final DockingManager SINGLETON = new DockingManager();

    private static final HashMap DOCKABLES_BY_ID = new HashMap();

    private static final WeakHashMap DOCKABLES_BY_COMPONENT = new WeakHashMap();

    private static final ClassMapping DOCKING_STRATEGIES = new ClassMapping(
        DefaultDockingStrategy.class, new DefaultDockingStrategy());

    // Map(DockingPort -> MaximizedState)
    private static final Map maximizedStatesByRootPort = new HashMap();

    private static Object persistentIdLock = new Object();

    private String defaultLayoutManagerClass;

    private String defaultMinimizeManagerClass;

    private DockingStrategy defaultDocker;

    private LayoutManager layoutManager;

    private MinimizationManager minimizeManager;

    private DockableFactory dockableFactory;

    private AutoPersist autoPersister;

    private float defaultSiblingSize;

    static {
        // call this method to preload any framework resources
        // we might need later
        init();
    }

    private static class AutoPersist extends Thread {
        private boolean enabled;

        public void run() {
            store();
        }

        private synchronized void store() {
            try {
                if (isEnabled())
                    storeLayoutModel();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (PersistenceException e) {
                e.printStackTrace();
            }
        }

        public synchronized boolean isEnabled() {
            return enabled;
        }

        public synchronized void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    private static class MaximizedState {
        private final Dockable dockable;

        private final DockingPort originalPort;

        public MaximizedState(Dockable dockable, DockingPort originalDockingPort) {
            this.dockable = dockable;
            this.originalPort = originalDockingPort;
        }

        public Dockable getDockable() {
            return dockable;
        }

        public DockingPort getOriginalPort() {
            return originalPort;
        }
    }

    private static void init() {
        // load the dev system properties
        Properties p = ResourceManager.getProperties(DEV_PROPS, true);
        if (p != null)
            System.getProperties().putAll(p);

        // prime the drag manager for use
        DragManager.prime();

        // ensure our Dockable adapters have been loaded
        AdapterFactory.prime();

        // make sure dockingEvents are properly intercepted
        EventManager.addHandler(new DockingEventHandler());
        EventManager.addListener(FloatPolicyManager.getInstance());

        Properties config = ResourceManager.getProperties(CONFIG_PROPS, true);
        DockingManager mgr = getDockingManager();
        // set the minimization manager
        mgr.defaultMinimizeManagerClass = config.getProperty(MINIMIZE_MANAGER);
        setMinimizeManager(mgr.defaultMinimizeManagerClass);
        // set the layout manager
        mgr.defaultLayoutManagerClass = config.getProperty(LAYOUT_MANAGER);
        setLayoutManager(mgr.defaultLayoutManagerClass);

        // setup tracking for the currently active dockable
        ActiveDockableListener.prime();

        // setup the default sibling size
        float siblingSize = Utilities.getFloat(System
                                               .getProperty(RegionChecker.DEFAULT_SIBLING_SIZE_KEY),
                                               RegionChecker.DEFAULT_SIBLING_SIZE);
        setDefaultSiblingSize(siblingSize);

        // setup auto-persistence
        Runtime.getRuntime().addShutdownHook(getDockingManager().autoPersister);
    }

    private DockingManager() {
        defaultDocker = new DefaultDockingStrategy();
        autoPersister = new AutoPersist();
    }

    private static DockingManager getDockingManager() {
        return SINGLETON;
    }

    public static void addDragSource(Dockable dockable, Component dragSrc) {
        List sources = dockable == null ? null : dockable.getDragSources();
        if (sources == null || dragSrc == null)
            return;

        if (!sources.contains(dragSrc)) {
            updateDragListeners(dockable);
        }
    }

    /**
     * Convenience method that removes the specified {@code Dockable} from the
     * layout. If the {@code Dockable}is embedded within the main application
     * frame or a floating dialog, it is removed from the container hierarchy.
     * If the {@code Dockable} is presently minimized, it is removed from the
     * current minimization manager. If the {@code Dockable} is already "closed"
     * or is {@code null}, no operation is performed. "Closing" a
     * {@code Dockable} only removes it from the visual layout. It does not
     * remove it from the internal {@code Dockable} registry and all underlying
     * {@code DockingState} information remains consistent so that the
     * {@code Dockable} may later be restored to its original location within
     * the application.
     *
     * @param dockable
     *            the {@code Dockable} to be closed.
     */
    public static void close(Dockable dockable) {
        if (dockable == null)
            return;

        if (isMaximized(dockable)) {
            toggleMaximized(dockable);
        }

        if (isDocked(dockable)) {
            undock(dockable);
        } else if (DockingUtility.isMinimized(dockable)) {
            getMinimizeManager().close(dockable);
        }
    }

    /**
     * Docks the specified {@code Component} into the CENTER region of the
     * specified {@code DockingPort}. If the {@code DockingManager} finds a
     * valid {@code Dockable} instance mapped to the specified {@code Component},
     * the {@code Dockable} will be docked into the {@code DockingPort}. If the
     * {@code Component} or {@code DockingPort} is {@code null}, or a valid
     * {@code Dockable} cannot be found for the specified {@code Component},
     * this method returns {@code false}. Otherwise, this method returns
     * {@code true} if the docking operation was successful and {@code false} if
     * the docking operation cannot be completed. This method defers processing
     * to {@code dock(Component dockable , DockingPort port, String region)}.
     *
     * @param dockable
     *            the {@code Component} to be docked.
     * @param port
     *            the {@code DockingPort} into which the specified
     *            {@code Component} will be docked.
     * @return {@code true} if the docking operation was successful,
     *         {@code false} otherwise.
     * @see #dock(Component, DockingPort, String)
     */
    public static boolean dock(Component dockable, DockingPort port) {
        return dock(dockable, port, CENTER_REGION);
    }

    /**
     * Docks the specified {@code Component} into the supplied region of the
     * specified {@code DockingPort}. If the {@code DockingManager} finds a
     * valid {@code Dockable} instance mapped to the specified {@code Component},
     * the {@code Dockable} will be docked into the {@code DockingPort}. If the
     * {@code Component} or {@code DockingPort} is {@code null}, or a valid
     * {@code Dockable} cannot be found for the specified {@code Component},
     * this method returns {@code false}. Otherwise, this method returns
     * {@code true} if the docking operation was successful and {@code false} if
     * the docking operation cannot be completed. This method defers processing
     * to {@code dock(Dockable dockable, DockingPort port, String region)}.
     *
     * @param dockable
     *            the {@code Component} to be docked.
     * @param port
     *            the {@code DockingPort} into which the specified
     *            {@code Component} will be docked.
     * @param region
     *            the region into which to dock the specified {@code Component}
     * @return {@code true} if the docking operation was successful,
     *         {@code false} if the docking operation cannot be completed.
     * @see #dock(Dockable, DockingPort, String)
     */
    public static boolean dock(Component dockable, DockingPort port,
                               String region) {
        Dockable d = resolveDockable(dockable);
        return dock(d, port, region);
    }

    /**
     * Docks the specified {@code Dockable} into the supplied region of the
     * specified {@code DockingPort}. If the {@code Dockable} or
     * {@code DockingPort} is {@code null}, this method returns {@code false}.
     * Otherwise, this method returns {@code true} if the docking operation was
     * successful and {@code false} if the docking operation cannot be
     * completed.
     *
     * This method determines the {@code DockingStrategy} to be used for the
     * specified {@code DockingPort} and defers processing to the
     * {@code DockingStrategy}. This method's return value will be based upon
     * the {@code DockingStrategy} implementation and is subject to conditions
     * such as whether the supplied region is deemed valid, whether the
     * {@code DockingStrategy} allows this particular {@code Dockable} to be
     * docked into the supplied region of the specified {@code DockingPort},
     * and so on. The {@code DockingStrategy} used is obtained by a call to
     * {@code getDockingStrategy(Object obj)} and may be controlled via
     * {@code setDockingStrategy(Class c, DockingStrategy strategy)}, supplying
     * a {@code DockingPort} implementation class and a customized
     * {@code DockingStrategy}.
     *
     * @param dockable
     *            the {@code Dockable} to be docked.
     * @param port
     *            the {@code DockingPort} into which the specified
     *            {@code Component} will be docked.
     * @param region
     *            the region into which to dock the specified {@code Dockable}
     * @return {@code true} if the docking operation was successful,
     *         {@code false} otherwise.
     * @see DockingStrategy#dock(Dockable, DockingPort, String)
     * @see #getDockingStrategy(Object)
     * @see #setDockingStrategy(Class, DockingStrategy)
     */
    public static boolean dock(Dockable dockable, DockingPort port,
                               String region) {
        if (dockable == null)
            return false;

        DockingStrategy strategy = getDockingStrategy(port);
        if (strategy != null) {
            return strategy.dock(dockable, port, region);
        }

        return false; // TODO think of changing it to runtime exception I
        // don't see a situation when there would be no docker.
    }

    private static Dockable resolveDockable(Component comp) {
        if (comp == null)
            return null;

        Dockable d = getDockable(comp);
        if (d == null)
            d = registerDockable(comp);
        return d;
    }

    /**
     * Docks the specified {@code Component} relative to another already-docked
     * {@code Component} in the CENTER region. Valid {@code Dockable} instances
     * are looked up for both {@code Component} parameters and processing is
     * deferred to {@code dock(Dockable dockable, Dockable parent)}. If a valid
     * {@code Dockable} cannot be resolved for either {@code Component}, then
     * this method returns {@code false}. The "parent" {@code Dockable} must
     * currently be docked. If not, this method will return {@code false}.
     * Otherwise, its parent {@code DockingPort} will be resolved and the new
     * {@code Dockable} will be docked into the {@code DockingPort} relative to
     * the "parent" {@code Dockable}.
     *
     * @param dockable
     *            the {@code Component} to be docked
     * @param parent
     *            the {@code Component} used as a reference point for docking
     * @return {@code true} if the docking operation was successful;
     *         {@code false} otherwise.
     * @see DockingManager#dock(Dockable, Dockable)
     */
    public static boolean dock(Component dockable, Component parent) {
        return dock(resolveDockable(dockable), resolveDockable(parent));
    }

    /**
     * Docks the specified {@code Dockable} relative to another already-docked
     * {@code Dockable} in the CENTER region. The "parent" {@code Dockable} must
     * currently be docked. If not, this method will return {@code false}.
     * Otherwise, its parent {@code DockingPort} will be resolved and the new
     * {@code Dockable} will be docked into the {@code DockingPort} relative to
     * the "parent" {@code Dockable}. This method defers processing to
     * {@code dock(Dockable dockable, Dockable parent, String region)} and
     * returns {@code false} if any of the input parameters are {@code null}.
     *
     * @param dockable
     *            the {@code Dockable} to be docked
     * @param parent
     *            the {@code Dockable} used as a reference point for docking
     * @return {@code true} if the docking operation was successful;
     *         {@code false} otherwise.
     * @see #dock(Dockable, Dockable, String)
     */
    public static boolean dock(Dockable dockable, Dockable parent) {
        return dock(dockable, parent, CENTER_REGION);
    }

    /**
     * Docks the specified {@code Component} relative to another already-docked
     * {@code Component} in the specified region. Valid {@code Dockable}
     * instances will be looked up for each of the {@code Component} parameters.
     * If a valid {@code Dockable} is not found for either {@code Component},
     * then this method returns {@code false}. The "parent" {@code Dockable}
     * must currently be docked. If not, this method will return {@code false}.
     * Otherwise, its parent {@code DockingPort} will be resolved and the new
     * {@code Dockable} will be docked into the {@code DockingPort} relative to
     * the "parent" {@code Dockable}. This method defers processing to
     * {@code dock(Component dockable, Component parent, String region, float proportion)}
     * and returns {@code false} if any of the input parameters are {@code null}.
     * If the specified region is other than CENTER, then a split layout should
     * result. This method supplies a split proportion of 0.5F, resulting in
     * equal distribution of space between the dockable and parent parameters if
     * docking is successful.
     *
     * @param dockable
     *            the {@code Component} to be docked
     * @param parent
     *            the {@code Component} used as a reference point for docking
     * @param region
     *            the relative docking region into which {@code dockable} will
     *            be docked
     * @return {@code true} if the docking operation was successful;
     *         {@code false} otherwise.
     * @see #dock(Component, Component, String, float)
     */
    public static boolean dock(Component dockable, Component parent,
                               String region) {
        return dock(dockable, parent, region, 0.5f);
    }

    /**
     * Docks the specified {@code Dockable} relative to another already-docked
     * {@code Dockable} in the specified region. The "parent" {@code Dockable}
     * must currently be docked. If not, this method will return {@code false}.
     * Otherwise, its parent {@code DockingPort} will be resolved and the new
     * {@code Dockable} will be docked into the {@code DockingPort} relative to
     * the "parent" {@code Dockable}. This method defers processing to
     * {@code dock(Dockable dockable, Dockable parent, String region, float proportion)}
     * and returns {@code false} if any of the input parameters are {@code null}.
     * If the specified region is other than CENTER, then a split layout should
     * result. This method supplies a split proportion of 0.5F, resulting in
     * equal distribution of space between the dockable and parent parameters if
     * docking is successful.
     *
     * @param dockable
     *            the {@code Dockable} to be docked
     * @param parent
     *            the {@code Dockable} used as a reference point for docking
     * @param region
     *            the docking region into which {@code dockable} will be docked
     * @return {@code true} if the docking operation was successful;
     *         {@code false} otherwise.
     * @see #dock(Dockable, Dockable, String, float)
     */
    public static boolean dock(Dockable dockable, Dockable parent, String region) {
        return dock(dockable, parent, region, 0.5f);
    }

    /**
     * Docks the specified {@code Component} relative to another already-docked
     * {@code Component} in the specified region with the specified split
     * proportion. Valid {@code Dockable} instances will be looked up for each
     * of the {@code Component} parameters. If a valid {@code Dockable} is not
     * found for either {@code Component}, then this method returns
     * {@code false}. The "parent" {@code Dockable} must currently be docked.
     * If not, this method will return {@code false}. Otherwise, its parent
     * {@code DockingPort} will be resolved and the new {@code Dockable} will be
     * docked into the {@code DockingPort} relative to the "parent"
     * {@code Dockable}. If the specified region is CENTER, then the
     * {@code proportion} parameter is ignored. Otherwise, a split layout should
     * result with the proportional space specified in the {@code proportion}
     * parameter allotted to the {@code dockable} argument. This method defers
     * processing to
     * {@code dock(Dockable dockable, Dockable parent, String region, float proportion)}.
     *
     * @param dockable
     *            the {@code Component} to be docked
     * @param parent
     *            the {@code Component} used as a reference point for docking
     * @param region
     *            the relative docking region into which {@code dockable} will
     *            be docked
     * @param proportion
     *            the proportional space to allot the {@code dockable} argument
     *            if the docking operation results in a split layout.
     * @return {@code true} if the docking operation was successful;
     *         {@code false} otherwise.
     */
    public static boolean dock(Component dockable, Component parent,
                               String region, float proportion) {
        Dockable newDockable = resolveDockable(dockable);
        Dockable parentDockable = resolveDockable(parent);
        return dock(newDockable, parentDockable, region, proportion);
    }

    /**
     * Docks the specified {@code Dockable} relative to another already-docked
     * {@code Dockable} in the specified region with the specified split
     * proportion. The "parent" {@code Dockable} must currently be docked. If
     * not, this method will return {@code false}. Otherwise, its parent
     * {@code DockingPort} will be resolved and the new {@code Dockable} will be
     * docked into the {@code DockingPort} relative to the "parent"
     * {@code Dockable}. If the specified region is CENTER, then the
     * {@code proportion} parameter is ignored. Otherwise, a split layout should
     * result with the proportional space specified in the {@code proportion}
     * parameter allotted to the {@code dockable} argument.
     *
     * @param dockable
     *            the {@code Dockable} to be docked
     * @param parent
     *            the {@code Dockable} used as a reference point for docking
     * @param region
     *            the docking region into which {@code dockable} will be docked
     * @param proportion
     *            the proportional space to allot the {@code dockable} argument
     *            if the docking operation results in a split layout.
     * @return {@code true} if the docking operation was successful;
     *         {@code false} otherwise.
     */
    public static boolean dock(Dockable dockable, Dockable parent,
                               String region, float proportion) {
        return DockingUtility
               .dockRelative(dockable, parent, region, proportion);
    }

    private static DockingStrategy findDockingStrategy(Dockable dockable) {
        DockingPort port = dockable.getDockingPort();
        DockingStrategy strategy = port == null ? null : port
                                   .getDockingStrategy();
        if (strategy == null) {
            DockingManager mgr = getDockingManager();
            strategy = mgr == null ? null : mgr.defaultDocker;
        }
        return strategy;
    }

    /**
     * Indicates whether the specified {@code Component} is currently docked.
     * This method looks up a parent {@code DockingPort} for the specified
     * {@code Component} via a call to
     * {@code getDockingPort(Component dockable)}. This method returns
     * {@code true} if a parent {@code DockingPort} is found and {@code false}
     * if no parent {@code DockingPort} is present. This method returns
     * {@code false} if the {@code Component} parameter is {@code null}.
     *
     * @param component
     *            the {@code Component} whose docking status is to be examined
     * @return {@code true} if the {@code Component} is currently docked;
     *         otherwise {@code false}.
     */
    public static boolean isDocked(Component component) {
        return getDockingPort(component) != null;
    }

    /**
     * Indicates whether the specified {@code Dockable} is currently docked.
     * This method looks up a parent {@code DockingPort} for the specified
     * {@code Dockable} via a call to {@code getDockingPort(Dockable dockable)}.
     * This method returns {@code true} if a parent {@code DockingPort} is found
     * and {@code false} if no parent {@code DockingPort} is present. This
     * method returns {@code false} if the {@code Dockable} parameter is
     * {@code null}.
     *
     * @param dockable
     *            the {@code Dockable} whose docking status is to be examined
     * @return {@code true} if the {@code Dockable} is currently docked;
     *         otherwise {@code false}.
     */
    public static boolean isDocked(Dockable dockable) {
        return getDockingPort(dockable) != null;
    }

    /**
     * Checks whether a supplied {@code Dockable} is docked within a supplied
     * {@code DockingPort} instance. Returns {@code true} if the
     * {@code DockingPort} contains the specified {@code Dockable};
     * {@code false} otherwise. This method returns {@code false} if either of
     * the input parameters are {@code null}.
     *
     * @param dockingPort
     *            the {@code DockingPort} to be tested
     * @param dockable
     *            the {@code Dockable} instance to be examined
     * @return {@code true} if the supplied {@code DockingPort} contains the
     *         specified {@code Dockable}; {@code false} otherwise.
     */
    public static boolean isDocked(DockingPort dockingPort, Dockable dockable) {
        return dockingPort == null || dockable == null ? false : dockingPort
               .isParentDockingPort(dockable.getComponent());
    }

    /**
     * Indicates whether global floating support is currently enabled. Defers
     * processing to {@code FloatPolicyManager.isGlobalFloatingEnabled()}.
     *
     * @return {@code true} if global floating support is enabled, {@code false}
     *         otherwise.
     * @see FloatPolicyManager#isGlobalFloatingEnabled()
     */
    public static boolean isFloatingEnabled() {
        return FloatPolicyManager.isGlobalFloatingEnabled();
    }

    /**
     * Indicates whether tabbed layouts are supported by default for
     * {@code DockingPorts} with a single {@code Dockable} in the CENTER region.
     * This is a global default setting and applies to any <cod>DockingPort}
     * that does not have a specific contradictory local setting.
     * <p>
     * This method defers processing to
     * {@code org.flexdock.docking.props.PropertyManager.getDockingPortRoot()}.
     * As such, there are multiple "scopes" at which this property may be
     * overridden.
     *
     * @return {@code true} if the default setting for {@code DockingPorts}
     *         allows a tabbed layout for a single {@code Dockable} in the
     *         CENTER region; {@code false} otherwise.
     * @see PropertyManager#getDockingPortRoot()
     * @see org.flexdock.docking.props.DockingPortPropertySet#isSingleTabsAllowed()
     */
    public static boolean isSingleTabsAllowed() {
        return PropertyManager.getDockingPortRoot().isSingleTabsAllowed()
               .booleanValue();
    }

    /**
     * Indicates whether the supplied parameter is considered a valid docking
     * region. Valid values are those defined in {@code DockingConstants} and
     * include {@code NORTH_REGION}, {@code SOUTH_REGION}, {@code EAST_REGION},
     * {@code WEST_REGION}, and {@code CENTER_REGION}. This method returns
     * {@code true} if the supplied parameter is equal to one of these values.
     *
     * @param region
     *            the region value to be tested
     * @return {@code true} if the supplied parameter is a valid docking region;
     *         {@code false} otherwise.
     */
    public static boolean isValidDockingRegion(String region) {
        return CENTER_REGION.equals(region) || NORTH_REGION.equals(region)
               || SOUTH_REGION.equals(region) || EAST_REGION.equals(region)
               || WEST_REGION.equals(region);
    }

    private static void updateDragListeners(Component dragSrc,
                                            DragManager listener) {
        MouseMotionListener motionListener = null;
        EventListener[] listeners = dragSrc.getMouseMotionListeners();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] instanceof DragManager) {
                motionListener = (MouseMotionListener) listeners[i];
                break;
            }
        }
        if (motionListener != listener) {
            if (motionListener != null)
                dragSrc.removeMouseMotionListener(motionListener);
            dragSrc.addMouseMotionListener(listener);
        }

        MouseListener mouseListener = null;
        listeners = dragSrc.getMouseListeners();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] instanceof DragManager) {
                mouseListener = (MouseListener) listeners[i];
                break;
            }
        }
        if (mouseListener != listener) {
            if (mouseListener != null)
                dragSrc.removeMouseListener(mouseListener);
            dragSrc.addMouseListener(listener);
        }
    }

    /**
     * Creates, registers, and returns a {@code Dockable} for the specified
     * {@code Component}. If the specified {@code Component} implements the
     * {@code Dockable} interface, then this method dispatches to
     * {@code registerDockable(Dockable dockable)}. Otherwise, this method
     * dispatches to {@code registerDockable(Component comp, String tabText)}.
     * <p>
     * This method attempts to resolve an appropriate value for {@code tabText}
     * by calling {@code getName()} on the specified {@code Component}. If the
     * resolved value is {@code null} or empty, then the value {@code "null"} is
     * used.
     * <p>
     * If {@code comp} is {@code null}, no exception is thrown and no action is
     * performed.
     *
     * @param comp
     *            the target component for the {@code Dockable}.
     * @return the {@code Dockable} that has been registered for the supplied
     *         {@code Component}
     * @see #registerDockable(Dockable)
     * @see #registerDockable(Component, String)
     */
    public static Dockable registerDockable(Component comp) {
        if (comp == null)
            return null;

        if (comp instanceof Dockable)
            return registerDockable((Dockable) comp);

        return registerDockable(comp, null, null);
    }

    private static String determineTabText(Component comp, String persistId) {
        String tabText = null;
        // if 'comp' is a DockingStub, then we may be able to
        // pull the tab text from it
        if (comp instanceof DockingStub) {
            tabText = ((DockingStub) comp).getTabText();
        } else {
            // if we can find an adapter mapping, then try to pull
            // the tab text from there
            DockingAdapter adapter = AdapterFactory.getAdapter(comp);
            if (adapter != null)
                tabText = adapter.getTabText();
        }

        // if 'comp' wasn't a DockingStub, or the stub returned a null tabText,
        // then try the component name
        if (tabText == null)
            tabText = comp.getName();

        // if tabText is still null, then use the persistentId
        if (tabText == null)
            tabText = persistId;

        // get rid of null and empty cases. use the string "null" if nothing
        // else can be found
        tabText = tabText == null ? "null" : tabText.trim();
        if (tabText.length() == 0)
            tabText = "null";

        return tabText;
    }

    /**
     * Creates a {@code Dockable} for the specified {@code Component} and
     * dispatches to {@code registerDockable(Dockable init)}. If {@code comp}
     * is {@code null}, no exception is thrown and no action is performed.
     *
     * @param comp
     *            the target component for the Dockable, both drag-starter and
     *            docking source
     * @param tabText
     *            the description of the docking source. Used as the tab-title
     *            of docked in a tabbed pane
     * @return the {@code Dockable} that has been registered for the supplied
     *         {@code Component}
     * @see #registerDockable(Dockable)
     */
    public static Dockable registerDockable(Component comp, String tabText) {
        return registerDockable(comp, tabText, null);
    }

    private static Dockable registerDockable(Component comp, String tabText,
            String dockingId) {
        if (comp == null)
            return null;

        if (tabText == null)
            tabText = determineTabText(comp, dockingId);

        Dockable dockable = getDockableForComponent(comp, tabText, dockingId);
        return registerDockable(dockable);
    }

    /**
     * Registers and initializes the specified {@code Dockable}. All
     * {@code Dockables} managed by the framework must, at some point, be
     * registered via this method. This method adds the {@code Dockable} to the
     * internal registry, allowing querying by ID and {@code Component}. Drag
     * listeners are added to the {@code Dockable} to enable drag-n-drop docking
     * support. Docking properties are also initialized for the {@code Dockable}.
     * This method fires a {@code RegistrationEvent} once the {@code Dockable}
     * has been registered. If the {@code Dockable} is {@code null}, no
     * {@code Exception} is thrown and no action is taken. The {@code Dockable}
     * returned by this method will be the same object passed in as an argument.
     *
     * @param dockable
     *            the Dockable that is being registered.
     * @return the {@code Dockable} that has been registered.
     * @see org.flexdock.event.RegistrationEvent
     */
    public static Dockable registerDockable(Dockable dockable) {
        if (dockable == null || dockable.getComponent() == null
                || dockable.getDragSources() == null)
            return null;

        if (dockable.getPersistentId() == null)
            throw new IllegalArgumentException(
                "Dockable must have a non-null persistent ID.");

        DOCKABLES_BY_COMPONENT.put(dockable.getComponent(), dockable);

        // flag the component as dockable, in case it doesn't
        // implement the interface directly
        Component c = dockable.getComponent();
        SwingUtility.putClientProperty(c, Dockable.DOCKABLE_INDICATOR,
                                       Boolean.TRUE);

        // add drag listeners
        updateDragListeners(dockable);

        // add the dockable as its own listener
        dockable.addDockingListener(dockable);

        // cache the dockable by ID
        DOCKABLES_BY_ID.put(dockable.getPersistentId(), dockable);

        // make sure we have docking-properties initialized (must come after
        // ID-caching)
        DockablePropertySet props = PropertyManager
                                    .getDockablePropertySet(dockable);

        // dispatch a registration event
        EventManager.dispatch(new RegistrationEvent(dockable,
                              DockingManager.SINGLETON, true));

        // return the dockable
        return dockable;
    }

    public static void unregisterDockable(Component comp) {
        Dockable dockable = getDockable(comp);
        unregisterDockable(dockable);
    }

    public static void unregisterDockable(String dockingId) {
        Dockable dockable = getDockableImpl(dockingId);
        unregisterDockable(dockable);
    }

    public static void unregisterDockable(Dockable dockable) {
        if (dockable == null)
            return;

        synchronized (DOCKABLES_BY_COMPONENT) {
            DOCKABLES_BY_COMPONENT.remove(dockable.getComponent());
        }

        // flag the component as dockable, in case it doesn't
        // implement the interface directly
        Component c = dockable.getComponent();
        SwingUtility.removeClientProperty(c, Dockable.DOCKABLE_INDICATOR);

        // remove the drag listeners
        removeDragListeners(dockable);

        // remove the dockable as its own listener
        dockable.removeDockingListener(dockable);

        // unlink the propertySet
        PropertyManager.removePropertySet(dockable);

        // remove the dockable by ID
        synchronized (DOCKABLES_BY_ID) {
            DOCKABLES_BY_ID.remove(dockable.getPersistentId());
        }

        // dispatch a registration event
        EventManager.dispatch(new RegistrationEvent(dockable,
                              DockingManager.SINGLETON, false));
    }

    /**
     * Removes the event listeners that manage drag-n-drop docking operations
     * from the specified {@code Component}. If the specific listeners are not
     * present, then no action is taken. Drag listeners used by the docking
     * system are of type {@code org.flexdock.docking.drag.DragManager}.
     *
     * @param comp
     *            the {@code Component} from which to remove drag listeners.
     * @see DragManager
     */
    public static void removeDragListeners(Component comp) {
        if (comp == null)
            return;

        MouseMotionListener motionListener = null;
        EventListener[] listeners = comp.getMouseMotionListeners();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] instanceof DragManager) {
                motionListener = (MouseMotionListener) listeners[i];
                break;
            }
        }
        if (motionListener != null) {
            comp.removeMouseMotionListener(motionListener);
        }

        MouseListener mouseListener = null;
        listeners = comp.getMouseListeners();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] instanceof DragManager) {
                mouseListener = (MouseListener) listeners[i];
                break;
            }
        }
        if (mouseListener != null) {
            comp.removeMouseListener(mouseListener);
        }
    }

    /**
     * Displays the specified {@code Dockable} in the application's docking
     * layout. If the {@code Dockable} has not previously been docked, a
     * suitable location is determined within the layout and the
     * {@code Dockable} is docked to that location. If the {@code Dockable} has
     * previously been docked within the layout and subsequently removed, as
     * with a call to {@code DockingManager.close()}, the {@code Dockable} will
     * be restored to its prior state within the layout. This method defers
     * processing to the {@code display(Dockable dockable)} method for the
     * currently installed {@code org.flexdock.docking.state.LayoutManager}.
     * The {@code LayoutManager} implementation is responsible for handling the
     * semantics of determining an initial docking location or restoring a
     * {@code Dockable} to its previous layout state. If the {@code Dockable}
     * parameter is {@code null}, no {@code Exception} is thrown and no action
     * is taken.
     *
     * @param dockable
     *            the {@code Dockable} to be displayed.
     * @return {@code true} if the {@code Dockable} was successfully displayed;
     *         {@code false} otherwise.
     * @see #getLayoutManager()
     * @see LayoutManager#display(Dockable)
     */
    public static boolean display(Dockable dockable) {
        return getLayoutManager().display(dockable);
    }

    /**
     * Displays the {@code Dockable} with the specified ID within the
     * application's docking layout. A valid {@code Dockable} is looked up for
     * the supplied ID. If none is found, this method returns {@code false}.
     * Otherwise, processing is dispatched to {@code display(Dockable dockable)}.
     * If the {@code Dockable} has not previously been docked, a suitable
     * location is determined within the layout and the {@code Dockable} is
     * docked to that location. If the {@code Dockable} has previously been
     * docked within the layout and subsequently removed, as with a call to
     * {@code DockingManager.close()}, the {@code Dockable} will be restored to
     * its prior state within the layout. This method defers processing to the
     * {@code display(Dockable dockable)} method for the currently installed
     * {@code org.flexdock.docking.state.LayoutManager}. The
     * {@code LayoutManager} implementation is responsible for handling the
     * semantics of determining an initial docking location or restoring a
     * {@code Dockable} to its previous layout state. If the {@code Dockable}
     * parameter is {@code null}, no {@code Exception} is thrown and no action
     * is taken.
     *
     * @param dockable
     *            the ID of the {@code Dockable} to be displayed.
     * @return {@code true} if the {@code Dockable} was successfully displayed;
     *         {@code false} otherwise.
     * @see #display(Dockable)
     * @see #getLayoutManager()
     * @see LayoutManager#display(Dockable)
     */
    public static boolean display(String dockable) {
        return getLayoutManager().display(getDockable(dockable));
    }

    private static String generatePersistentId(Object obj) {
        return generatePersistentId(obj, null);
    }

    private static String generatePersistentId(Object obj, String desiredId) {
        if (obj == null)
            return null;

        synchronized (persistentIdLock) {
            String pId = desiredId == null ? obj.getClass().getName()
                         : desiredId;
            StringBuffer baseId = new StringBuffer(pId);
            for (int i = 1; hasRegisteredDockableId(pId); i++) {
                baseId.append("_").append(i);
                pId = baseId.toString();
            }
            return pId;
        }
    }

    private static boolean hasRegisteredDockableId(String id) {
        return DOCKABLES_BY_ID.containsKey(id);
    }

    /**
     * Returns the {@code DockingStrategy} associated with the {@code Class} of
     * the {@code Object} parameter. This method returns {@code null} if the
     * parameter is {@code null}. Otherwise, the method retrieves the
     * {@code Object's} {@code Class} and dispatches to
     * {@code getDockingStrategy(Class classKey)}.
     * <p>
     * {@code DockingStrategy} association follows a strict inheritance chain
     * using {@code org.flexdock.util.ClassMapping}. If a mapping for
     * {@code obj.getClass()} is not found, then the superclass is tested, and
     * so on until {@code java.lang.Object} is reached. Thus, if a
     * {@code DockingStrategy} mapping of {@code Foo} exists for class
     * {@code Bar}, and class {@code Baz} extends {@code Bar}, then calling
     * this method for an instance of {@code Baz} will return an instance of
     * {@code Foo}. The inheritance chain is <i>strict</i> in the sense that
     * only superclasses are checked. Implemented interfaces are ignored.
     * <p>
     * If a class association is never found, then an instance of
     * {@code DefaultDockingStrategy} is returned.
     *
     * @param obj
     *            the object whose {@code DockingStrategy} association we wish
     *            to test
     * @return the {@code DockingStrategy} associated with the {@code Class}
     *         type of the {@code Object} parameter.
     * @see #getDockingStrategy(Class)
     * @see #setDockingStrategy(Class, DockingStrategy)
     * @see ClassMapping#getClassInstance(Class)
     */
    public static DockingStrategy getDockingStrategy(Object obj) {
        Class key = obj == null ? null : obj.getClass();
        return getDockingStrategy(key);
    }

    /**
     * Returns the {@code DockingStrategy} associated with specified
     * {@code Class}. This method returns {@code null} if the parameter is
     * {@code null}.
     * <p>
     * {@code DockingStrategy} association follows a strict inheritance chain
     * using {@code org.flexdock.util.ClassMapping}. If a mapping for
     * {@code classKey} is not found, then the superclass is tested, and so on
     * until {@code java.lang.Object} is reached. Thus, if a
     * {@code DockingStrategy} mapping of {@code Foo} exists for class
     * {@code Bar}, and class {@code Baz} extends {@code Bar}, then calling
     * this method for class {@code Baz} will return an instance of {@code Foo}.
     * The inheritance chain is <i>strict</i> in the sense that only
     * superclasses are checked. Implemented interfaces are ignored.
     * <p>
     * If a class association is never found, then an instance of
     * {@code DefaultDockingStrategy} is returned.
     *
     * @param classKey
     *            the {@code Class} whose {@code DockingStrategy} association we
     *            wish to test
     * @return the {@code DockingStrategy} associated with the specified
     *         {@code Class}.
     * @see #setDockingStrategy(Class, DockingStrategy)
     * @see ClassMapping#getClassInstance(Class)
     */
    public static DockingStrategy getDockingStrategy(Class classKey) {
        DockingStrategy strategy = (DockingStrategy) DOCKING_STRATEGIES
                                   .getClassInstance(classKey);
        return strategy;
    }

    /**
     * Returns an array of {@code RootWindows} known to the docking framework
     * that contain {@code DockingPorts}. Any {@code Frame}, {@code Applet},
     * {@code Dialog}, or {@code Window} that has a {@code DockingPort} added
     * as a descendent {@code Component} will automatically have an
     * {@code org.flexdock.util.RootWindow} wrapper instance associated with it.
     * This method will return an array of all known RootWindows that contain
     * {@code DockingPorts}. Ordering of the array may be based off of a
     * {@code java.util.Set} and is <b>not</b> guaranteed.
     *
     * @return an array of all known {@code RootWindows} that contain
     *         {@code DockingPorts}
     * @see RootWindow
     * @see DockingPortTracker#getDockingWindows()
     */
    public static RootWindow[] getDockingWindows() {
        Set windowSet = DockingPortTracker.getDockingWindows();
        return windowSet == null ? new RootWindow[0] : (RootWindow[]) windowSet
               .toArray(new RootWindow[0]);
    }

    /**
     * Returns the {@code DockingPort} with the specified ID. If the
     * {@code portId} parameter is {@code null}, or a {@code DockingPort} with
     * the specified ID is not found, a {@code null} reference is returned. This
     * method internally dispatches to
     * {@code org.flexdock.docking.event.hierarchy.DockingPortTracker.findById(String portId)}.
     * {@code portId} should match the value returned by a {@code DockingPort's}
     * {@code getPersistentId()} method.
     *
     * @param portId
     *            the ID of the {@code DockingPort} to be looked up
     * @return the {@code DockingPort} with the specified ID
     * @see DockingPort#getPersistentId()
     * @see DockingPortTracker#findById(String)
     */
    public static DockingPort getDockingPort(String portId) {
        return DockingPortTracker.findById(portId);
    }

    /**
     * Returns the "main" {@code DockingPort} within the application window
     * containing the specified {@code Component}. Just as desktop applications
     * will tend to have a "main" application window, perhaps surrounded with
     * satellite windows or dialogs, the "main" {@code DockingPort} within a
     * given window will be considered by the application developer to contain
     * the primary docking layout used by the enclosing window.
     * <p>
     * The {@code Component} parameter may or may not be a root window
     * container. If not, the ancestor window of {@code comp} is determined and
     * a set of docking ports encapsulated by a {@code RootDockingPortInfo}
     * instance is returned by a call to
     * {@code getRootDockingPortInfo(Component comp)}. The resolved
     * {@code RootDockingPortInfo} instance's main {@code DockingPort} is
     * returned via its method {@code getMainPort()}.
     * <p>
     * By default, the "main" {@code DockingPort} assigned to any
     * {@code RootDockingPortInfo} instance associated with a window will happen
     * to be the first root {@code DockingPort} detected for that window. In
     * essence, the default settings make this method identical to
     * {@code getRootDockingPort(Component comp)}. This, however, may be
     * altered by {@code RootDockingPortInfo's}
     * {@code setMainPort(String portId)} method based upon the needs of the
     * application developer. In contrast,
     * {@code getMainDockingPort(Component comp)} will always return the first
     * root {@code DockingPort} found within a window.
     * <p>
     * If {@code comp} is {@code null} or the root window cannot be resolved,
     * then this method returns a {@code null} reference. A {@code null}
     * reference is also returned if the root window does not contain any
     * {@code DockingPorts}.
     *
     * @param comp
     *            the {@code Component} whose root window will be checked for a
     *            main {@code DockingPort}
     * @return the main {@code DockingPort} within the root window that contains
     *         {@code comp}
     * @see #getRootDockingPortInfo(Component)
     * @see #getRootDockingPort(Component)
     * @see DockingPortTracker#getRootDockingPortInfo(Component)
     * @see RootDockingPortInfo#getMainPort()
     * @see RootDockingPortInfo#setMainPort(String)
     */
    public static DockingPort getMainDockingPort(Component comp) {
        RootDockingPortInfo info = getRootDockingPortInfo(comp);
        return info == null ? null : info.getMainPort();
    }

    /**
     * Returns the first root {@code DockingPort} found within the application
     * window containing the specified {@code Component}. A "root"
     * {@code DockingPort} is a {@code DockingPort} embedded within a
     * window/frame/applet/dialog that is not nested within any other parent
     * {@code DockingPorts}. The {@code Component} parameter may or may not be
     * a root window container itself. If not, the root window containing
     * {@code comp} is resolved and the first root {@code DockingPort} found
     * within it is returned. This method defers actual processing to
     * {@code org.flexdock.docking.event.hierarchy.DockingPortTracker.findByWindow(Component comp)}.
     * <p>
     * If {@code comp} is {@code null} or the root window cannot be resolved,
     * then this method returns a {@code null} reference. A {@code null}
     * reference is also returned if the root window does not contain any
     * {@code DockingPorts}.
     * <p>
     * This method differs from {@code getMainDockingPort(Component comp)} in
     * that the "main" {@code DockingPort} for a given window is configurable by
     * the application developer, whereas this method will always return the
     * "first" {@code DockingPort} found within the window. However, if the
     * "main" {@code DockingPort} has not been manually configured by the
     * application developer, then this method and
     * {@code getMainDockingPort(Component comp)} will exhibit identical
     * behavior.
     *
     * @param comp
     *            the {@code Component} whose root window will be checked for a
     *            root {@code DockingPort}
     * @return the first root {@code DockingPort} found within the root window
     *         that contains {@code comp}
     * @see #getMainDockingPort(Component)
     * @see DockingPortTracker#findByWindow(Component)
     * @see RootDockingPortInfo
     */
    public static DockingPort getRootDockingPort(Component comp) {
        return DockingPortTracker.findByWindow(comp);
    }

    /**
     * Returns the {@code RootDockingPortInfo} instance associated with the root
     * window containing the specified {@code Component}. The {@code Component}
     * parameter may or may not be a root window container itself. If not, the
     * root window containing {@code comp} is resolved and the
     * {@code RootDockingPortInfo} instance associated with the window is
     * returned. {@code RootDockingPortInfo} will contain information regarding
     * all of the "root" {@code DockingPorts} embedded within a root window
     * where a "root" {@code DockingPort} is any {@code DockingPort} embedded
     * within the window that does not have any other {@code DockingPort}
     * ancestors in it's container hierarchy.
     * <p>
     * If {@code comp} is {@code null} or the root window cannot be resolved,
     * then this method returns a {@code null} reference. A {@code null}
     * reference is also returned if the root window does not contain any
     * {@code DockingPorts}.
     * <p>
     * This method dispatches internally to
     * {@code org.flexdock.docking.event.hierarchy.DockingPortTracker.getRootDockingPortInfo(Component comp)}.
     *
     * @param comp
     *            the {@code Component} whose root window will be checked for an
     *            associated {@code RootDockingPortInfo}.
     * @return the {@code RootDockingPortInfo} instance associated with the root
     *         window containing {@code comp}.
     * @see RootDockingPortInfo
     * @see DockingPortTracker#getRootDockingPortInfo(Component)
     */
    public static RootDockingPortInfo getRootDockingPortInfo(Component comp) {
        return DockingPortTracker.getRootDockingPortInfo(comp);
    }

    /**
     * Sends the application's current layout model to external storage. This
     * method defers processing to the currently installed
     * {@code org.flexdock.docking.state.LayoutManager} by invoking its
     * {@code store()} method. If there is no {@code LayoutManager} installed,
     * then this method returns {@code false}.
     * <p>
     * The layout model itself, along with storage mechanism, is abstract and
     * dependent upon the particular {@code LayoutManager} implementation. As
     * such, it may be possible that the {@code LayoutManager} is unable to
     * persist the current layout state for non-Exceptional reasons. This method
     * returns {@code true} if the layout model was successfully stored and
     * {@code false} if the layout model could not be stored under circumstances
     * that do not generate an {@code Exception} (for instance, if there is no
     * persistence implementation currently installed). If a problem occurs
     * during the persistence process, an {@code IOException} is thrown.
     *
     * @return {@code true} if the current layout model was succesfully stored,
     *         {@code false} otherwise.
     * @throws IOException
     * @throws PersisterException
     * @see #getLayoutManager()
     * @see #setLayoutManager(LayoutManager)
     * @see LayoutManager#store()
     */
    public static boolean storeLayoutModel() throws IOException,
        PersistenceException {
        LayoutManager mgr = getLayoutManager();
        return mgr == null ? false : mgr.store();
    }

    /**
     * Loads a previously stored layout model into the currently installed
     * {@code LayoutManager}. This method defers processing to
     * {@code loadLayoutModel(boolean restore)} with an argument of
     * {@code false} to indicate that the stored data model should merely be
     * loaded into memory and the {@code LayoutManager} should not attempt to
     * subsequently restore the application view by synchronizing it against the
     * newly loaded data model.
     * <p>
     * The layout model itself, along with storage mechanism, is abstract and
     * dependent upon the particular {@code LayoutManager} implementation. As
     * such, it may be possible that the {@code LayoutManager} is unable to load
     * the previous layout state for non-Exceptional reasons. This method
     * returns {@code true} if the layout model was successfully loaded and
     * {@code false} if the layout model could not be loaded under circumstances
     * that do not generate an {@code Exception} (for instance, if there was no
     * previous layout model found in storage). If a problem occurs during the
     * loading process, an {@code IOException} is thrown.
     *
     * @return {@code true} if the current layout model was succesfully loaded,
     *         {@code false} otherwise.
     * @throws IOException
     * @throws PersisterException
     * @see #loadLayoutModel(boolean)
     * @see LayoutManager#load()
     */
    public static boolean loadLayoutModel() throws IOException,
        PersistenceException {
        return loadLayoutModel(false);
    }

    /**
     * Loads a previously stored layout model into the currently installed
     * {@code LayoutManager} and attempts to synchronize the application view
     * with the newly loaded layout model if the {@code restore} parameter is
     * {@code true}. If there is no currently installed {@code LayoutManager},
     * then this method returns {@code false}. If the {@code restore} parameter
     * is {@code true}, then this method defers processing to
     * {@code restoreLayout(boolean loadFromStorage)} with an argument of
     * {@code true}. Otherwise, this method defers processing to the currently
     * installed {@code org.flexdock.docking.state.LayoutManager} by invoking
     * its {@code load()} method.
     * <p>
     * The layout model itself, along with storage mechanism, is abstract and
     * dependent upon the particular {@code LayoutManager} implementation. As
     * such, it may be possible that the {@code LayoutManager} is unable to load
     * the previous layout state for non-Exceptional reasons. This method
     * returns {@code true} if the layout model was successfully loaded and
     * {@code false} if the layout model could not be loaded under circumstances
     * that do not generate an {@code Exception} (for instance, if there was no
     * previous layout model found in storage). If a problem occurs during the
     * loading process, an {@code IOException} is thrown.
     *
     * @return {@code true} if the current layout model was succesfully loaded,
     *         {@code false} otherwise.
     * @throws IOException
     * @throws PersisterException
     * @see #getLayoutManager()
     * @see #setLayoutManager(LayoutManager)
     * @see #restoreLayout(boolean)
     * @see LayoutManager#load()
     */
    public static boolean loadLayoutModel(boolean restore) throws IOException,
        PersistenceException {
        LayoutManager mgr = getLayoutManager();
        if (mgr == null)
            return false;

        return restore ? restoreLayout(true) : mgr.load();
    }

    /**
     * Synchronizes the application view with the current in-memory layout
     * model. This method defers processing to
     * {@code restoreLayout(boolean loadFromStorage)} with an argument of
     * {@code false}. This instructs the currently installed
     * {@code LayoutManager} to restore the application view to match the
     * current in-memory layout model without reloading from storage prior to
     * restoration. This method is useful for developers who choose to construct
     * a layout model programmatically and wish to "commit" it to the
     * application view, restoring their own in-memory layout model rather than
     * a model persisted in external storage.
     * <p>
     * If there is no {@code LayoutManager} currently installed, then this
     * method returns {@code false}.
     *
     * @return {@code true} if the in-memory layout model was properly restored
     *         to the application view, {@code false} otherwise.
     * @throws PersisterException
     * @see #restoreLayout(boolean)
     * @see #getLayoutManager()
     * @see #setLayoutManager(LayoutManager)
     * @see LayoutManager#restore(boolean)
     */
    public static boolean restoreLayout() {
        try {
            return restoreLayout(false);
        } catch (IOException e) {
            // shouldn't happen since we're not intending to load from storage
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
            return false;
        } catch (PersistenceException e) {
            // TODO Auto-generated catch block
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Synchronizes the application view with the current in-memory layout
     * model. This method defers processing to the currently installed
     * {@code org.flexdock.docking.state.LayoutManager} by invoking its
     * {@code restore(boolean loadFromStorage)} method. If there is no
     * {@code LayoutManager} currently installed, then this method returns
     * {@code false}.
     * <p>
     * If the {@code loadFromStorage} parameter is {@code true}, then the
     * {@code LayoutManager} is instructed to load any persisted layout model
     * from external storage into memory before synchronizing the application
     * view. If a problem occurs while loading from exernal storage, this method
     * throws an {@code IOException}.
     *
     * @param loadFromStorage
     *            instructs whether to load any layout model from external
     *            storage into memory before synchronizing the application view.
     * @return {@code true} if the in-memory layout model was properly restored
     *         to the application view, {@code false} otherwise.
     * @throws PersisterException
     * @see #getLayoutManager()
     * @see #setLayoutManager(LayoutManager)
     * @see LayoutManager#restore(boolean)
     */
    public static boolean restoreLayout(boolean loadFromStorage)
    throws IOException, PersistenceException {
        LayoutManager mgr = getLayoutManager();
        return mgr == null ? false : mgr.restore(loadFromStorage);
    }

    private static Dockable loadAndRegister(String id) {
        DockableFactory factory = id == null ? null
                                  : getDockingManager().dockableFactory;
        if (factory == null)
            return null;

        // the getDockableComponent() implementation may or may not
        // automatically register a dockable before returning.

        // first, try to get a Dockable from the factory
        Dockable dockable = factory.getDockable(id);
        if (dockable != null) {
            // check to see if the dockable is already registered.
            Dockable tmp = getDockableImpl(dockable.getPersistentId());
            if (tmp == null)
                registerDockable(dockable);
        }
        // if we couldn't find a dockable from the factory, then try getting
        // a component.
        else {
            Component comp = factory.getDockableComponent(id);
            // we already weren't able to get a Dockable from the factory. If
            // we couldn't get a Component either, then give up.
            if (comp == null)
                return null;

            // if the newly created dockable has not yet been registered,
            // then register it.
            dockable = getDockable(comp);
            if (dockable == null) {
                dockable = registerDockable(comp, null, id);
            }
        }

        return dockable;
    }

    private static Dockable getDragInitiator(Component c) {
        return getDockableForComponent(c, null, null);
    }

    private static Dockable getDockableForComponent(Component c, String desc,
            String dockingId) {
        if (c == null)
            return null;

        // return the dockable if it has already been registered
        Dockable dockable = getDockable(c);
        if (dockable != null)
            return dockable;

        // if we need to create a dockable, first try to do it with an adapter
        DockingAdapter adapter = AdapterFactory.getAdapter(c);
        if (adapter != null) {
            dockable = DockableComponentWrapper.create(adapter);
        }

        // if we weren't able to create from an adapter, then create the
        // dockable manually
        if (dockable == null) {
            if (c instanceof DockingStub) {
                dockable = DockableComponentWrapper.create((DockingStub) c);
            } else {
                String persistentId = dockingId == null ? generatePersistentId(c)
                                      : dockingId;
                dockable = DockableComponentWrapper.create(c, persistentId,
                           desc);
            }
        }

        // make sure the specified description is applied
        if (desc != null)
            dockable.getDockingProperties().setDockableDesc(desc);

        // cache the dockable for future use
        DOCKABLES_BY_COMPONENT.put(c, dockable);

        // now we can return
        return dockable;
    }

    /**
     * Returns the {@code DockingPort} that contains the specified
     * {@code Component}. If the {@code Component} is {@code null}, then a
     * {@code null} reference is returned.
     * <p>
     * This method will only return the immediate parent {@code DockingPort} of
     * the specified {@code Component} This means that the {@code DockingPort}
     * returned by this method will not only be an ancestor {@code Container} of
     * the specified {@code Component}, but invoking its
     * {@code isParentDockingPort(Component comp)} with the specified
     * {@code Component} will also return {@code true}. If both of these
     * conditions cannot be satisfied, then this method returns a {@code null}
     * reference.
     *
     * @param dockable
     *            the {@code Component} whose parent {@code DockingPort} is to
     *            be returned.
     * @return the imediate parent {@code DockingPort} that contains the
     *         specified {@code Component}.
     */
    public static DockingPort getDockingPort(Component dockable) {
        return DockingUtility.getParentDockingPort(dockable);
    }

    /**
     * Returns the {@code DockingPort} that contains the specified
     * {@code Dockable}. If the {@code Dockable} is {@code null}, then a
     * {@code null} reference is returned.
     * <p>
     * This method will only return the immediate parent {@code DockingPort} of
     * the specified {@code Dockable} This means that a check is performed for
     * the {@code Component} returned by the {@code Dockable's}
     * {@code getComponent()} method. The {@code DockingPort} returned by this
     * method will not only be an ancestor {@code Container} of this
     * {@code Component}, but invoking the {@code DockingPort's}
     * {@code isParentDockingPort(Component comp)} with the this
     * {@code Component} will also return {@code true}. If both of these
     * conditions cannot be satisfied, then this method returns a {@code null}
     * reference.
     *
     * @param dockable
     *            the {@code Dockable} whose parent {@code DockingPort} is to be
     *            returned.
     * @return the imediate parent {@code DockingPort} that contains the
     *         specified {@code Dockable}.
     */
    public static DockingPort getDockingPort(Dockable dockable) {
        return DockingUtility.getParentDockingPort(dockable);
    }

    /**
     * Returns the {@code Dockable} instance that models the specified
     * {@code Component}. The {@code Dockable} returned by this method will
     * return a reference to {@code comp} when its {@code getComponent()} method
     * is called. If {@code comp} is {@code null}, then this method will return
     * a {@code null} reference.
     * <p>
     * The association between {@code Dockable} and {@code Component} is
     * established internally during {@code registerDockable(Dockable dockable)}.
     * Thus, {@code registerDockable(Dockable dockable)} must have been called
     * previously for a mapping to be found and a {@code Dockable} to be
     * returned by this method. If no mapping is found for the specified
     * {@code Component}, then this method returns a {@code null} reference.
     *
     * @param comp
     *            the {@code Component} whose {@code Dockable} instance is to be
     *            returned.
     * @return the {@code Dockable} that models the specified {@code Component}
     * @see #registerDockable(Dockable)
     * @see Dockable#getComponent()
     */
    public static Dockable getDockable(Component comp) {
        return comp == null ? null : (Dockable) DOCKABLES_BY_COMPONENT
               .get(comp);
    }

    /**
     * Returns the {@code Dockable} instance with the specified ID. The
     * {@code Dockable} returned by this method will return a String equal
     * {@code id} when its {@code getPersistentId()} method is called. If
     * {@code id} is {@code null}, then this method will return a {@code null}
     * reference.
     * <p>
     * The association between {@code Dockable} and {@code id} is established
     * internally during {@code registerDockable(Dockable dockable)}. Thus,
     * {@code registerDockable(Dockable dockable)} must have been called
     * previously for a mapping to be found and a {@code Dockable} to be
     * returned by this method. If no mapping is found for the specified
     * {@code id}, then this method returns a {@code null} reference.
     *
     * @param id
     *            the persistent ID of the {@code Dockable} instance is to be
     *            returned.
     * @return the {@code Dockable} that has the specified perstent ID.
     * @see #registerDockable(Dockable)
     * @see Dockable#getPersistentId()
     */
    public static Dockable getDockable(String id) {
        if (id == null)
            return null;

        Dockable dockable = getDockableImpl(id);
        if (dockable == null)
            dockable = loadAndRegister(id);
        return dockable;
    }

    private static Dockable getDockableImpl(String id) {
        synchronized (DOCKABLES_BY_ID) {
            return id == null ? null : (Dockable) DOCKABLES_BY_ID.get(id);
        }
    }

    /**
     * Returns a {@code Set} of {@code String} IDs for all {@code Dockables}
     * registered with the framework. The IDs returned by this method will
     * correspond to the values returned for the {@code getPersistentId()}
     * method for each {@code Dockable} registered with the framework.
     *
     * {@code Dockable} IDs are cached during
     * {@code registerDockable(Dockable dockable)}. Thus, for an ID to appear
     * within the {@code Set} returned by this method, the corresponding
     * {@code Dockable} must have first been registered via
     * {@code registerDockable(Dockable dockable)}.
     *
     * If no {@code Dockables} have been registered with the framework, then an
     * empty {@code Set} is returned. This method will never return a
     * {@code null} reference.
     *
     * @return a {@code Set} of {@code String} IDs for all {@code Dockables}
     *         registered with the framework.
     * @see #registerDockable(Dockable)
     * @see Dockable#getPersistentId()
     */
    public static Set getDockableIds() {
        synchronized (DOCKABLES_BY_ID) {
            return new HashSet(DOCKABLES_BY_ID.keySet());
        }
    }

    /**
     * Returns the listener object responsible for managing drag-to-dock mouse
     * events for the specified {@code Dockable}. During registration, the
     * listener is added to each of the {@code Components} within the
     * {@code Dockable's} {@code getDragSources()} {@code List}. Thus, for this
     * method to return a valid {@code DragManager} instance, the
     * {@code Dockable} must first have been registered via
     * {@code registerDockable(Dockable dockable)}. If the specified
     * {@code Dockable} is {@code null} or its {@code getDragSources()} method
     * returns a {@code null}, or if the {@code Dockable} has not previously
     * been registered, this method will return a {@code null} reference.
     *
     * @param dockable
     *            the {@code Dockable} whose drag listener is to be returned.
     * @return the {@code DragManager} responsible for listening to an managing
     *         drag-related mouse events for the specified {@code Dockable}.
     * @see DragManager
     * @see Dockable#getDragSources()
     * @see #registerDockable(Dockable)
     */
    public static DragManager getDragListener(Dockable dockable) {
        if (dockable == null || dockable.getDragSources() == null)
            return null;

        for (Iterator it = dockable.getDragSources().iterator(); it.hasNext();) {
            Object obj = it.next();
            if (obj instanceof Component) {
                DragManager listener = getDragListener((Component) obj);
                if (listener != null)
                    return listener;
            }
        }
        return null;
    }

    private static DragManager getDragListener(Component c) {
        EventListener[] listeners = c.getMouseMotionListeners();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] instanceof DragManager)
                return (DragManager) listeners[i];
        }
        return null;
    }

    /**
     * Returns the currently installed {@code LayoutManager}. The
     * {@code LayoutManager} is responsible for managing docking layout state.
     * This includes tracking the state for all {@code Dockables} as they are
     * embedded, minimized, floated, or hidden. If a {@code Dockable} is
     * embedded, the {@code LayoutManager} is responsible for tracking its
     * position and size relative to other embedded {@code Dockables}. If
     * floating, the {@code LayoutManager} is responsible for supplying a
     * {@code FloatManager} to maintain {@code Dockable} groupings within
     * dialogs as well as dialog size and positioning.
     * <p>
     * The {@code LayoutManager} is responsible for providing a persistence
     * mechanism to save and restore layout states. Depending on the
     * {@code LayoutManager} implementation, it may or may not support multiple
     * layout models that may be loaded and switched between at runtime.
     * <p>
     * Because the {@code LayoutManager} is a critical piece of the docking
     * infrastructure, it is not possible to install a {@code null}
     * {@code LayoutManager}. Therefore, this method will always return a valid
     * {@code LayoutManager} and never a {@code null} reference.
     *
     * @return the currently installed {@code LayoutManager}
     * @see LayoutManager
     * @see #setLayoutManager(LayoutManager)
     * @see #setLayoutManager(String)
     */
    public static LayoutManager getLayoutManager() {
        return getDockingManager().layoutManager;
    }

    /**
     * Returns the currently installed {@code MinimizationManager}. The
     * {@code MinimizationManager} is responsible for minimizing and
     * unminimizing {@code Dockables}, removing from and restoring to the
     * embedded docking layout through the currently installed
     * {@code LayoutManager}.
     * <p>
     * The visual representation of a "minimized" {@code Dockable} is somewhat
     * abstract, although it is commonly expressed in user interfaces with the
     * disappearance of the {@code Dockable} from the layout and the addition of
     * a tab or label on one or more edges of the application window. The
     * {@code MinimizationManager} implementation itself is responsible for
     * interpreting the visual characteristics and behavior of a minimized
     * {@code Dockable}, but it must provide a "preview" feature to allow
     * viewing of minimized {@code Dockables}, on demand without actually
     * restoring them to the embedded docking layout. {@code Dockables} may or
     * may not have limited docking functionality while in minimized and/or
     * preview state, depending upon the {@code MinimizationManager}
     * implementation.
     * <p>
     * Because the {@code MinimizationManager} is a critical piece of the
     * docking infrastructure, it cannot be set to {@code null}. Therefore,
     * this method will always return a valid {@code MinimizationManager} and
     * never a {@code null} reference.
     *
     * @return the currently installed {@code MinimizationManager}.
     * @see MinimizationManager
     * @see #setMinimizeManager(MinimizationManager)
     * @see #setMinimizeManager(String)
     */
    public static MinimizationManager getMinimizeManager() {
        MinimizationManager mgr = getDockingManager().minimizeManager;
        return mgr == null ? MinimizationManager.DEFAULT_STUB : mgr;
    }

    /**
     * Returns the currently installed {@code FloatManager}. The
     * {@code FloatManager} is actually provided by the currently installed
     * {@code LayoutManager}. As such, this method is merely for convenience.
     * It internally obtains the installed {@code LayoutManager} via
     * {@code getLayoutManager()} and invokes its {@code getFloatManager()}
     * method.
     * <p>
     * The {@code FloatManager} maintains information relevant to floating
     * {@code Dockables} including grouping them together within dialogs and
     * tracking dialog size and position. The {@code FloatManager} is
     * responsible for generating new dialogs, parenting on the proper
     * application window(s), and sending {@code Dockables} to the proper
     * dialogs. It may be used by the {@code LayoutManager} to restore hidden
     * {@code Dockables} to proper floating state as needed.
     * <p>
     * Since the {@code FloatManager} is provided by the currently installed
     * {@code LayoutManager}, it cannot be set from within the
     * {@code DockingManager}. To change the installed {@code FloatManager},
     * one must work directly with the installed {@code LayoutManager}
     * implementation per its particular custom API.
     * <p>
     * Since the {@code FloatManager} is a critical piece of the docking
     * insfrastructure, this method will never return a {@code null} reference.
     *
     * @return the {@code FloatManager} provided by the currently installed
     *         {@code LayoutManager}
     * @see #getLayoutManager()
     * @see #setLayoutManager(LayoutManager)
     * @see LayoutManager#getFloatManager()
     */
    public static FloatManager getFloatManager() {
        return getLayoutManager().getFloatManager();
    }

    /**
     * Returns the {@code DockingState} for the {@code Dockable} with the
     * specified ID. The {@code DockingState} is used by the currently installed
     * {@code LayoutManager} to track information regarding a {@code Dockable's}
     * current state in the docking layout. This includes relative size and
     * positioning to other {@code Dockables}, minimization status, floating
     * status, and any other information used to track and potentially restore a
     * the {@code Dockable} to the layout if it is currently hidden.
     * <p>
     * The {@code Dockable} whose current {@code DockingState} is resolved will
     * map to the specified {@code dockableId} via its {@code getPersistentId()}
     * method. The semantics of this mapping relationship are the same as
     * {@code DockingManager.getDockable(String id)}. If a valid
     * {@code Dockable} cannot be found for the specified ID, then this method
     * returns a {@code null} reference.
     * <p>
     * The {@code DockingState} for any given {@code Dockable} is ultimately
     * managed by the currently installed {@code LayoutManager}. Therefore,
     * this method resolves the {@code LayoutManager} via
     * {@code getLayoutManager()} and defers processing to its
     * {@code getDockingState(String dockableId)} method.
     * <p>
     * The underlying {@code LayoutManager} does not provide any guarantees that
     * the same {@code DockingState} reference always will be returned for a
     * given {@code Dockable}; only that the returned {@code DockingState} will
     * accurately reflect the current state maintained by the
     * {@code LayoutManager} for that {@code Dockable}. For instance, if the
     * {@code LayoutManager} is capable of maintaining multiple layouts for an
     * application (as Eclipse does between perspectives), then the
     * {@code LayoutManager} may or may not maintain multiple
     * {@code DockingState} instances for a single {@code Dockable}, one within
     * each layout context. Therefore, it is not a good idea to cache references
     * to the {@code DockingState} instance returned by this method for future
     * use as the reference itself may possibly become stale over time depending
     * on the {@code LayoutManager} implementation.
     *
     * @param dockableId
     *            the persistent ID of the {@code Dockable} whose current
     *            {@code DockingState} is to be returned
     * @return the current {@code DockingState} maintained by the
     *         {@code LayoutManager} for the specified {@code Dockable}
     * @see DockingState
     * @see #getLayoutManager()
     * @see LayoutManager#getDockingState(String)
     * @see #getDockable(String)
     * @see Dockable#getPersistentId()
     */
    public static DockingState getDockingState(String dockableId) {
        return getLayoutManager().getDockingState(dockableId);
    }

    /**
     * Returns the {@code DockingState} for the specified {@code Dockable}. The
     * {@code DockingState} is used by the currently installed
     * {@code LayoutManager} to track information regarding a {@code Dockable's}
     * current state in the docking layout. This includes relative size and
     * positioning to other {@code Dockables}, minimization status, floating
     * status, and any other information used to track and potentially restore a
     * the {@code Dockable} to the layout if it is currently hidden.
     * <p>
     * If the {@code dockable} parameter is {@code null}, then this method
     * returns a {@code null} reference.
     * <p>
     * The {@code DockingState} for any given {@code Dockable} is ultimately
     * managed by the currently installed {@code LayoutManager}. Therefore,
     * this method resolves the {@code LayoutManager} via
     * {@code getLayoutManager()} and defers processing to its
     * {@code getDockingState(String dockableId)} method.
     * <p>
     * The underlying {@code LayoutManager} does not provide any guarantees that
     * the same {@code DockingState} reference always will be returned for a
     * given {@code Dockable}; only that the returned {@code DockingState} will
     * accurately reflect the current state maintained by the
     * {@code LayoutManager} for that {@code Dockable}. For instance, if the
     * {@code LayoutManager} is capable of maintaining multiple layouts for an
     * application (as Eclipse does between perspectives), then the
     * {@code LayoutManager} may or may not maintain multiple
     * {@code DockingState} instances for a single {@code Dockable}, one within
     * each layout context. Therefore, it is not a good idea to cache references
     * to the {@code DockingState} instance returned by this method for future
     * use as the reference itself may possibly become stale over time depending
     * on the {@code LayoutManager} implementation.
     *
     * @param dockable
     *            the {@code Dockable} whose current {@code DockingState} is to
     *            be returned
     * @return the current {@code DockingState} maintained by the
     *         {@code LayoutManager} for the specified {@code Dockable}
     * @see #getLayoutManager()
     * @see LayoutManager#getDockingState(String)
     */
    public static DockingState getDockingState(Dockable dockable) {
        return getLayoutManager().getDockingState(dockable);
    }

    /**
     * Returns the currently installed {@code DockableFactory}. The
     * {@code DockableFactory} installed by default is {@code null}. Therefore,
     * this method will return a {@code null} reference until the application
     * developer explicitly provides a {@code DockableFactory} implementation
     * via {@code setDockableFactory(DockableFactory factory)}.
     * <p>
     * Installing a {@code DockableFactory} allows FlexDock to seamlessly create
     * and register {@code Dockables} within {@code getDockable(String id)}.
     * Generally, {@code getDockable(String id)} will lookup the requested
     * {@code Dockable} within the internal registry. If not found, and there is
     * no {@code DockableFactory} installed, {@code getDockable(String id)}
     * returns a {@code null} reference. When a {@code DockableFactory} is
     * installed, however, failure to lookup a valid {@code Dockable} will cause
     * {@code getDockable(String id)} to invoke the installed
     * {@code DockableFactory's} {@code getDockable(String dockableId)} method,
     * transparently registering and returning the newly created
     * {@code Dockable} from {@code getDockable(String id)}.
     *
     * @return the currently installed {@code DockableFactory}
     * @see #getDockable(String)
     * @see DockableFactory#getDockable(String)
     */
    public static DockableFactory getDockableFactory() {
        return getDockingManager().dockableFactory;
    }

    /**
     * Enables and disables auto-persistence of the current docking layout model
     * when the application exits. Auto-persistence is disabled by default.
     * <p>
     * The {@code storeLayoutModel()} provides a means of manually sending the
     * docking layout model to some type of external storage. When the
     * {@code DockingManager} class loads, a shutdown hook is added to the
     * {@code Runtime}. If auto-persist is enabled when the JVM exits, the
     * shutdown hook automatically calls {@code storeLayoutModel()}, catching
     * and reporting any {@code IOExceptions} that may occur.
     *
     * @param enabled
     *            {@code true} if automatic persistence is desired;
     *            {@code false} otherwise.
     * @see #storeLayoutModel()
     * @see Runtime#addShutdownHook(java.lang.Thread)
     */
    public static void setAutoPersist(boolean enabled) {
        getDockingManager().autoPersister.setEnabled(enabled);
    }

    /**
     * Sets the divider location of the split layout containing the specified
     * dockable {@code Component}. The {@code Dockable} instance associated
     * with the specified {@code Component} is resolved via
     * {@code getDockable(Component comp)} and processing is dispatched to
     * {@code setSplitProportion(Dockable dockable, float proportion)}.
     * <p>
     * The resulting divider location will be a percentage of the split layout
     * size based upon the {@code proportion} parameter. Valid values for
     * {@code proportion} range from {@code 0.0F{@code  to {@code 1.0F}. For
     * example, a {@code proportion} of {@code 0.3F} will move the divider to
     * 30% of the "size" (<i>width</i> for horizontal split, <i>height</i>
     * for vertical split) of the split container that contains the specified
     * {@code Component}. If a {@code proportion} of less than {@code 0.0F} is
     * supplied, the value }0.0F} is used. If a {@code proportion} greater than
     * {@code 1.0F} is supplied, the value }1.0F} is used.
     * <p>
     * It is important to note that the split divider location is only a
     * percentage of the container size from left to right or top to bottom. A
     * {@code proportion} of {@code 0.3F} does not imply that {@code dockable}
     * itself will be allotted 30% of the available space. The split divider
     * will be moved to the 30% position of the split container regardless of
     * the region in which the specified {@code Component} resides (which may
     * possibly result in {@code dockable} being allotted 70% of the available
     * space).
     * <p>
     * This method should be effective regardless of whether the split layout in
     * question has been fully realized and is currently visible on the screen.
     * This should alleviate common problems associated with setting percentages
     * of unrealized {@code Component} dimensions, which are initially
     * {@code 0x0} before the {@code Component} has been rendered to the screen.
     * <p>
     * If the specified {@code Component} is {@code null}, then no
     * {@code Exception} is thrown and no action is taken. Identical behavior
     * occurs if a valid {@code Dockable} cannot be resolved for the specified
     * {@code Component}, or the {@code Dockable} does not reside within a
     * split layout.
     * <p>
     * If the {@code Dockable} resides within a tabbed layout, a check is done
     * to see if the tabbed layout resides within a parent split layout. If so,
     * the resolved split layout is resized. Otherwise no action is taken.
     *
     * @param dockable
     *            the {@code Component} whose containing split layout is to be
     *            resized.
     * @param proportion
     *            the percentage of containing split layout size to which the
     *            split divider should be set.
     * @see #setSplitProportion(Dockable, float)
     * @see #getDockable(Component)
     */
    public static void setSplitProportion(Component dockable, float proportion) {
        setSplitProportion(getDockable(dockable), proportion);
    }

    /**
     * Sets the divider location of the split layout containing the specified
     * dockable {@code Component}.
     * <p>
     * The resulting divider location will be a percentage of the split layout
     * size based upon the {@code proportion} parameter. Valid values for
     * {@code proportion} range from {@code 0.0F{@code  to {@code 1.0F}. For
     * example, a {@code proportion} of {@code 0.3F} will move the divider to
     * 30% of the "size" (<i>width</i> for horizontal split, <i>height</i>
     * for vertical split) of the split container that contains the specified
     * {@code Dockable}. If a {@code proportion} of less than {@code 0.0F} is
     * supplied, the value }0.0F} is used. If a {@code proportion} greater than
     * {@code 1.0F} is supplied, the value }1.0F} is used.
     * <p>
     * It is important to note that the split divider location is only a
     * percentage of the container size from left to right or top to bottom. A
     * {@code proportion} of {@code 0.3F} does not imply that {@code dockable}
     * itself will be allotted 30% of the available space. The split divider
     * will be moved to the 30% position of the split container regardless of
     * the region in which the specified {@code Dockable} resides (which may
     * possibly result in {@code dockable} being allotted 70% of the available
     * space).
     * <p>
     * This method should be effective regardless of whether the split layout in
     * question has been fully realized and is currently visible on the screen.
     * This should alleviate common problems associated with setting percentages
     * of unrealized {@code Component} dimensions, which are initially
     * {@code 0x0} before the {@code Component} has been rendered to the screen.
     * <p>
     * If the specified {@code Dockable} is {@code null}, then no
     * {@code Exception} is thrown and no action is taken. Identical behavior
     * occurs if the {@code Dockable} does not reside within a split layout.
     * <p>
     * If the {@code Dockable} resides within a tabbed layout, a check is done
     * to see if the tabbed layout resides within a parent split layout. If so,
     * the resolved split layout is resized. Otherwise no action is taken.
     *
     * @param dockable
     *            the {@code Dockable} whose containing split layout is to be
     *            resized.
     * @param proportion
     *            the percentage of containing split layout size to which the
     *            split divider should be set.
     * @see #getDockable(Component)
     */
    public static void setSplitProportion(Dockable dockable, float proportion) {
        DockingUtility.setSplitProportion(dockable, proportion);
    }

    /**
     * Sets the divider location of the split layout embedded within the
     * specified {@code DockingPort}. This method differs from both
     * {@code setSplitProportion(Component dockable, float proportion)} and
     * {@code setSplitProportion(Dockable dockable, float proportion)} in that
     * this method resolves the split layout embedded <b>within</b> the
     * specified {@code DockingPort}, whereas the other methods modify the
     * split layout <b>containing</b> their respective {@code Dockable}
     * parameters.
     * <p>
     * The resulting divider location will be a percentage of the split layout
     * size based upon the {@code proportion} parameter. Valid values for
     * {@code proportion} range from {@code 0.0F{@code  to {@code 1.0F}. For
     * example, a {@code proportion} of {@code 0.3F} will move the divider to
     * 30% of the "size" (<i>width</i> for horizontal split, <i>height</i>
     * for vertical split) of the split container embedded within the specified
     * {@code DockingPort}. If a {@code proportion} of less than {@code 0.0F}
     * is supplied, the value }0.0F} is used. If a {@code proportion} greater
     * than {@code 1.0F} is supplied, the value }1.0F} is used.
     * <p>
     * This method should be effective regardless of whether the split layout in
     * question has been fully realized and is currently visible on the screen.
     * This should alleviate common problems associated with setting percentages
     * of unrealized {@code Component} dimensions, which are initially
     * {@code 0x0} before the {@code Component} has been rendered to the screen.
     * <p>
     * If the specified {@code DockingPort} is {@code null}, then no
     * {@code Exception} is thrown and no action is taken. Identical behavior
     * occurs if the {@code DockingPort} does not contain split layout.
     *
     * @param port
     *            the {@code DockingPort} containing the split layout is to be
     *            resized.
     * @param proportion
     *            the percentage of split layout size to which the split divider
     *            should be set.
     */
    public static void setSplitProportion(DockingPort port, float proportion) {
        DockingUtility.setSplitProportion(port, proportion);
    }

    /**
     * Sets the currently installed {@code DockableFactory}. {@code null}
     * values for the {@code factory} parameter are acceptable.
     * <p>
     * Installing a {@code DockableFactory} allows FlexDock to seamlessly create
     * and register {@code Dockables} within {@code getDockable(String id)}.
     * Generally, {@code getDockable(String id)} will lookup the requested
     * {@code Dockable} within the internal registry. If not found, and there is
     * no {@code DockableFactory} installed, {@code getDockable(String id)}
     * returns a {@code null} reference. When a {@code DockableFactory} is
     * installed, however, failure to lookup a valid {@code Dockable} will cause
     * {@code getDockable(String id)} to invoke the installed
     * {@code DockableFactory's} {@code getDockable(String dockableId)} method,
     * transparently registering and returning the newly created
     * {@code Dockable} from {@code getDockable(String id)}.
     *
     * @param factory
     *            the {@code DockableFactory} to install
     * @see #getDockableFactory()
     * @see #getDockable(String)
     * @see DockableFactory#getDockable(String)
     */
    public static void setDockableFactory(DockableFactory factory) {
        getDockingManager().dockableFactory = factory;
    }

    /**
     * Sets the minimized state for the specified {@code Dockable}. This method
     * defers processing to
     * {@code setMinimized(Dockable dockable, boolean minimized, Component window)},
     * passing the current {@code Window} ancestor of the specified
     * {@code Dockable} as the {@code window} parameter. Minimization
     * processessing is ultimately deferred to the currently installed
     * {@code MinimizationManager} with a constraint of
     * {@code MinimizationManager.UNSPECIFIED_LAYOUT_CONSTRAINT}.
     * <p>
     * The current {@code MinimizationManager} is responsible for updating the
     * underlying {@code DockingState} model for the specified {@code Dockable}
     * as well as rendering its own interpretation of the corresponding visual
     * state on the screen. If the supplied {@code minimized} parameter matches
     * the current {@code DockingState}, the {@code MinimizationManager} is
     * responsible for providing the appropriate visual indications, or lack
     * thereof. If the specified {@code Dockable} is {@code null}, no
     * {@code Exception} is thrown and no action is taken.
     *
     * @param dockable
     *            the {@code Dockable} whose minimzed state is to be modified
     * @param minimized
     *            {@code true} if the specified {@code Dockable} should be
     *            minimized, {@code false} otherwise.
     * @see #setMinimized(Dockable, boolean, Component)
     * @see #getMinimizeManager()
     * @see MinimizationManager#setMinimized(Dockable, boolean, Component, int)
     * @see DockingState#getMinimizedConstraint()
     */
    public static void setMinimized(Dockable dockable, boolean minimized) {
        Component cmp = dockable == null ? null : dockable.getComponent();
        Window window = cmp == null ? null : SwingUtilities
                        .getWindowAncestor(cmp);
        setMinimized(dockable, minimized, window);
    }

    /**
     * Sets the minimized state for the specified {@code Dockable}. This method
     * defers processing to
     * {@code setMinimized(Dockable dockable, boolean minimizing, Component window, int constraint)},
     * passing {@code MinimizationManager.UNSPECIFIED_LAYOUT_CONSTRAINT} for the
     * {@code constraint} parameter. Minimization processessing is ultimately
     * deferred to the currently installed {@code MinimizationManager}.
     * <p>
     * The {@code window} parameter is passed to the {@code MinimizationManager}
     * to indicate that minimization should be handled with respect to the
     * specified root window, or the root window containing the specified
     * {@code Component}. {@code null} values are acceptable for this
     * parameter.
     * <p>
     * The current {@code MinimizationManager} is responsible for updating the
     * underlying {@code DockingState} model for the specified {@code Dockable}
     * as well as rendering its own interpretation of the corresponding visual
     * state on the screen. If the supplied {@code minimized} parameter matches
     * the current {@code DockingState}, the {@code MinimizationManager} is
     * responsible for providing the appropriate visual indications, or lack
     * thereof. If the specified {@code Dockable} is {@code null}, no
     * {@code Exception} is thrown and no action is taken.
     *
     * @param dockable
     *            the {@code Dockable} whose minimzed state is to be modified
     * @param minimized
     *            {@code true} if the specified {@code Dockable} should be
     *            minimized, {@code false} otherwise.
     * @param window
     *            the {@code Component} whose root window will be used by the
     *            underlying {@code MinimizationManager} for rendering the
     *            {@code Dockable} in its new minimized state.
     * @see #setMinimized(Dockable, boolean, Component, int)
     * @see #getMinimizeManager()
     * @see MinimizationManager#setMinimized(Dockable, boolean, Component, int)
     * @see DockingState#getMinimizedConstraint()
     */
    public static void setMinimized(Dockable dockable, boolean minimized,
                                    Component window) {
        setMinimized(dockable, minimized, window,
                     MinimizationManager.UNSPECIFIED_LAYOUT_CONSTRAINT);
    }

    /**
     * Sets the minimized state for the specified {@code Dockable}. This method
     * defers processing to
     * {@code setMinimized(Dockable dockable, boolean minimizing, Component window, int constraint)},
     * passing {@code null} for the {@code window} parameter. Minimization
     * processessing is ultimately deferred to the currently installed
     * {@code MinimizationManager}.
     * <p>
     * Valid values for the {@code constraint} parameter may be found on the
     * {@code MinimizationManager} interface and include
     * UNSPECIFIED_LAYOUT_CONSTRAINT, TOP, LEFT, BOTTOM, RIGHT, and CENTER.
     * However, constraint values must ultimately be interpreted by the current
     * {@code MinimizationManager} implementation and, thus any integer value
     * may theoretically be valid for {@code constraint}.
     * <p>
     * The current {@code MinimizationManager} is responsible for updating the
     * underlying {@code DockingState} model for the specified {@code Dockable}
     * as well as rendering its own interpretation of the corresponding visual
     * state on the screen. If the supplied {@code minimized} parameter matches
     * the current {@code DockingState}, the {@code MinimizationManager} is
     * responsible for providing the appropriate visual indications, or lack
     * thereof. If the specified {@code Dockable} is {@code null}, no
     * {@code Exception} is thrown and no action is taken.
     *
     * @param dockable
     *            the {@code Dockable} whose minimzed state is to be modified
     * @param minimizing
     *            {@code true} if the specified {@code Dockable} should be
     *            minimized, {@code false} otherwise.
     * @param constraint
     *            a value to indicate to the {@code MinimizationManager} desired
     *            rendering of the minimized {@code Dockable}
     * @see #setMinimized(Dockable, boolean, Component, int)
     * @see #getMinimizeManager()
     * @see MinimizationManager#setMinimized(Dockable, boolean, Component, int)
     * @see DockingState#getMinimizedConstraint()
     */
    public static void setMinimized(Dockable dockable, boolean minimizing,
                                    int constraint) {
        setMinimized(dockable, minimizing, null, constraint);
    }

    /**
     * Sets the minimized state for the specified {@code Dockable}. This method
     * defers processing to the currently installed {@code MinimizationManager}.
     * <p>
     * The {@code window} parameter is passed to the {@code MinimizationManager}
     * to indicate that minimization should be handled with respect to the
     * specified root window, or the root window containing the specified
     * {@code Component}. If a {@code null} values is supplied for this
     * parameter, the currently active window is used. If no currently active
     * window can be determined, then this method exits with no action taken.
     * <p>
     * The current {@code MinimizationManager} is responsible for updating the
     * underlying {@code DockingState} model for the specified {@code Dockable}
     * as well as rendering its own interpretation of the corresponding visual
     * state on the screen. If the supplied {@code minimized} parameter matches
     * the current {@code DockingState}, the {@code MinimizationManager} is
     * responsible for providing the appropriate visual indications, or lack
     * thereof. If the specified {@code Dockable} is {@code null}, no
     * {@code Exception} is thrown and no action is taken.
     * <p>
     * Valid values for the {@code constraint} parameter may be found on the
     * {@code MinimizationManager} interface and include
     * UNSPECIFIED_LAYOUT_CONSTRAINT, TOP, LEFT, BOTTOM, RIGHT, and CENTER.
     * However, constraint values must ultimately be interpreted by the current
     * {@code MinimizationManager} implementation and, thus any integer value
     * may theoretically be valid for {@code constraint}.
     *
     * @param dockable
     *            the {@code Dockable} whose minimzed state is to be modified
     * @param minimizing
     *            {@code true} if the specified {@code Dockable} should be
     *            minimized, {@code false} otherwise.
     * @param window
     *            the {@code Component} whose root window will be used by the
     *            underlying {@code MinimizationManager} for rendering the
     *            {@code Dockable} in its new minimized state.
     * @param constraint
     *            a value to indicate to the {@code MinimizationManager} desired
     *            rendering of the minimized {@code Dockable}
     * @see #getMinimizeManager()
     * @see MinimizationManager#setMinimized(Dockable, boolean, Component, int)
     * @see DockingState#getMinimizedConstraint()
     */
    public static void setMinimized(Dockable dockable, boolean minimizing,
                                    Component window, int constraint) {
        if (dockable == null)
            return;

        if (window == null)
            window = SwingUtility.getActiveWindow();
        if (window == null)
            return;

        getMinimizeManager().setMinimized(dockable, minimizing, window,
                                          constraint);
    }

    /**
     * Sets the "main" {@code DockingPort} within the application window
     * containing the specified {@code Component}. Just as desktop applications
     * will tend to have a "main" application window, perhaps surrounded with
     * satellite windows or dialogs, the "main" {@code DockingPort} within a
     * given window will be considered by the application developer to contain
     * the primary docking layout used by the enclosing window.
     * <p>
     * The {@code Component} parameter may or may not be a root window
     * container. If not, the ancestor window of {@code comp} is determined and
     * a set of docking ports encapsulated by a {@code RootDockingPortInfo}
     * instance is returned by a call to
     * {@code getRootDockingPortInfo(Component comp)}. The resolved
     * {@code RootDockingPortInfo} instance's main {@code DockingPort} is set
     * via its method {@code setMainPort(String portId)}.
     * <p>
     * By default, the "main" {@code DockingPort} assigned to any
     * {@code RootDockingPortInfo} instance associated with a window will happen
     * to be the first root {@code DockingPort} detected for that window. This
     * method is used to alter that setting.
     * <p>
     * If {@code comp} is {@code null} or the root window cannot be resolved,
     * then this method returns with no action taken.
     *
     * @param window
     *            the {@code Component} whose root window will be checked for a
     *            main {@code DockingPort}
     * @param portId
     *            the persistent ID of the {@code DockingPort} to use as the
     *            main {@code DockingPort} for the specified window.
     * @see #getRootDockingPortInfo(Component)
     * @see #getRootDockingPort(Component)
     * @see DockingPortTracker#getRootDockingPortInfo(Component)
     * @see RootDockingPortInfo#getMainPort()
     * @see RootDockingPortInfo#setMainPort(String)
     */
    public static void setMainDockingPort(Component window, String portId) {
        RootDockingPortInfo info = getRootDockingPortInfo(window);
        if (info != null)
            info.setMainPort(portId);
    }

    /**
     * Sets the currently installed {@code MinimizationManager}. The
     * {@code MinimizationManager} is responsible for minimizing and
     * unminimizing {@code Dockables}, removing from and restoring to the
     * embedded docking layout through the currently installed
     * {@code LayoutManager}.
     * <p>
     * The visual representation of a "minimized" {@code Dockable} is somewhat
     * abstract, although it is commonly expressed in user interfaces with the
     * disappearance of the {@code Dockable} from the layout and the addition of
     * a tab or label on one or more edges of the application window. The
     * {@code MinimizationManager} implementation itself is responsible for
     * interpreting the visual characteristics and behavior of a minimized
     * {@code Dockable}, but it must provide a "preview" feature to allow
     * viewing of minimized {@code Dockables}, on demand without actually
     * restoring them to the embedded docking layout. {@code Dockables} may or
     * may not have limited docking functionality while in minimized and/or
     * preview state, depending upon the {@code MinimizationManager}
     * implementation.
     * <p>
     * Because the {@code MinimizationManager} is a critical piece of the
     * docking infrastructure, it cannot be set to {@code null}. If a
     * {@code null} value is passed into this method, the default
     * {@code MinimizationManager} provided by the framework is used instead.
     *
     * @param mgr
     *            the {@code MinimizationManager} to be installed
     * @see MinimizationManager
     * @see #getMinimizeManager()
     * @see #setMinimizeManager(String)
     */
    public static void setMinimizeManager(MinimizationManager mgr) {
        DockingManager dockingManager = getDockingManager();
        if (mgr == null)
            // do not allow null minimization managers
            setMinimizeManager(dockingManager.defaultMinimizeManagerClass);
        else
            dockingManager.minimizeManager = mgr;
    }

    /**
     * Sets the currently installed {@code MinimizationManager} using the
     * specfied class name. An attempt is make to instantiate a
     * {@code MinimizationManager} based upon the supplied class name
     * {@code String}. If the class cannot be instaniated, a stacktrace is
     * reported to the System.err and the default {@code MinimizationManager}
     * supplied by the framework is used. If the {@code String} parameter is
     * {@code null}, no error occurs and the default
     * {@code MinimizationManager} is used. If the instantiated class is not a
     * valid instance of {@code MinimizationManager}, then a
     * {@code ClassCastException} is thrown.
     * <p>
     * The {@code MinimizationManager} is responsible for minimizing and
     * unminimizing {@code Dockables}, removing from and restoring to the
     * embedded docking layout through the currently installed
     * {@code LayoutManager}.
     * <p>
     * The visual representation of a "minimized" {@code Dockable} is somewhat
     * abstract, although it is commonly expressed in user interfaces with the
     * disappearance of the {@code Dockable} from the layout and the addition of
     * a tab or label on one or more edges of the application window. The
     * {@code MinimizationManager} implementation itself is responsible for
     * interpreting the visual characteristics and behavior of a minimized
     * {@code Dockable}, but it must provide a "preview" feature to allow
     * viewing of minimized {@code Dockables}, on demand without actually
     * restoring them to the embedded docking layout. {@code Dockables} may or
     * may not have limited docking functionality while in minimized and/or
     * preview state, depending upon the {@code MinimizationManager}
     * implementation.
     * <p>
     * Because the {@code MinimizationManager} is a critical piece of the
     * docking infrastructure, it cannot be set to {@code null}. If a
     * {@code null} value is passed into this method, the default
     * {@code MinimizationManager} provided by the framework is used instead.
     *
     * @param mgrClass
     *            the class name of the {@code MinimizationManager} to be
     *            installed
     * @see MinimizationManager
     * @see #getMinimizeManager()
     * @see #setMinimizeManager(String)
     */
    public static void setMinimizeManager(String mgrClass) {
        Object instance = Utilities.getInstance(mgrClass);
        setMinimizeManager((MinimizationManager) instance);
    }

    /**
     * Sets whether global floating support should be enabled. Defers processing
     * to
     * {@code FloatPolicyManager.setGlobalFloatingEnabled(boolean globalFloatingEnabled)}.
     *
     * @param enabled
     *            {@code true} if global floating support should be enabled,
     *            {@code false} otherwise.
     * @see FloatPolicyManager#setGlobalFloatingEnabled(boolean)
     * @see FloatPolicyManager#isGlobalFloatingEnabled()
     */
    public static void setFloatingEnabled(boolean enabled) {
        FloatPolicyManager.setGlobalFloatingEnabled(enabled);
    }

    public static void setDefaultPersistenceKey(String key) {
        getLayoutManager().setDefaultPersistenceKey(key);
    }

    public static String getDefaultPersistenceKey() {
        return getLayoutManager().getDefaultPersistenceKey();
    }

    /**
     * Sets whether tabbed layouts are supported by default for
     * {@code DockingPorts} with a single {@code Dockable} in the CENTER region.
     * This is a global default setting and applies to any {@code DockingPort}
     * that does not have a specific contradictory local setting.
     * <p>
     * This method defers processing to
     * {@code org.flexdock.docking.props.PropertyManager.getDockingPortRoot()}.
     * As such, there are multiple "scopes" at which this property may be
     * overridden.
     *
     * @param allowed
     *            {@code true} if the default setting for {@code DockingPorts}
     *            should allow a tabbed layout for a single {@code Dockable} in
     *            the CENTER region; {@code false} otherwise.
     * @see PropertyManager#getDockingPortRoot()
     * @see org.flexdock.docking.props.DockingPortPropertySet#setSingleTabsAllowed(boolean)
     */
    public static void setSingleTabsAllowed(boolean allowed) {
        PropertyManager.getDockingPortRoot().setSingleTabsAllowed(allowed);
    }

    /**
     * Sets the currently installed {@code LayoutManager}. The
     * {@code LayoutManager} is responsible for managing docking layout state.
     * This includes tracking the state for all {@code Dockables} as they are
     * embedded, minimized, floated, or hidden. If a {@code Dockable} is
     * embedded, the {@code LayoutManager} is responsible for tracking its
     * position and size relative to other embedded {@code Dockables}. If
     * floating, the {@code LayoutManager} is responsible for supplying a
     * {@code FloatManager} to maintain {@code Dockable} groupings within
     * dialogs as well as dialog size and positioning.
     * <p>
     * The {@code LayoutManager} is responsible for providing a persistence
     * mechanism to save and restore layout states. Depending on the
     * {@code LayoutManager} implementation, it may or may not support multiple
     * layout models that may be loaded and switched between at runtime.
     * <p>
     * Because the {@code LayoutManager} is a critical piece of the docking
     * infrastructure, it is not possible to install a {@code null}
     * {@code LayoutManager}. FlexDock provides a default {@code LayoutManager}
     * implementation. If this method is passed a {@code null} argument, the
     * default {@code LayoutManager} is used instead.
     *
     * @param mgr
     *            the {@code LayoutManager} to install.
     * @see LayoutManager
     * @see #setLayoutManager(String)
     * @see #getLayoutManager()
     */
    public static void setLayoutManager(LayoutManager mgr) {
        DockingManager dockingManager = getDockingManager();
        if (mgr == null)
            // do not allow a null layout manager.
            setLayoutManager(dockingManager.defaultLayoutManagerClass);
        else
            getDockingManager().layoutManager = mgr;
    }

    /**
     * Sets the currently installed {@code LayoutManager} using the specified
     * class name. An attempt is make to instantiate a {@code LayoutManager}
     * based upon the supplied class name {@code String}. If the class cannot
     * be instaniated, a stacktrace is reported to the System.err and the
     * default {@code LayoutManager} supplied by the framework is used. If the
     * {@code String} parameter is {@code null}, no error occurs and the
     * default {@code LayoutManager} is used. If the instantiated class is not a
     * valid instance of {@code LayoutManager}, then a
     * {@code ClassCastException} is thrown.
     * <p>
     * The {@code LayoutManager} is responsible for managing docking layout
     * state. This includes tracking the state for all {@code Dockables} as they
     * are embedded, minimized, floated, or hidden. If a {@code Dockable} is
     * embedded, the {@code LayoutManager} is responsible for tracking its
     * position and size relative to other embedded {@code Dockables}. If
     * floating, the {@code LayoutManager} is responsible for supplying a
     * {@code FloatManager} to maintain {@code Dockable} groupings within
     * dialogs as well as dialog size and positioning.
     * <p>
     * The {@code LayoutManager} is responsible for providing a persistence
     * mechanism to save and restore layout states. Depending on the
     * {@code LayoutManager} implementation, it may or may not support multiple
     * layout models that may be loaded and switched between at runtime.
     * <p>
     * Because the {@code LayoutManager} is a critical piece of the docking
     * infrastructure, it is not possible to install a {@code null}
     * {@code LayoutManager}. FlexDock provides a default {@code LayoutManager}
     * implementation. If this method is passed a {@code null} argument, the
     * default {@code LayoutManager} is used instead.
     *
     * @param mgrClass
     *            the class name of the {@code LayoutManager} to install.
     * @see LayoutManager
     * @see #setLayoutManager(LayoutManager)
     * @see #getLayoutManager()
     */
    public static void setLayoutManager(String mgrClass) {
        Object instance = Utilities.getInstance(mgrClass);
        setLayoutManager((LayoutManager) instance);
    }

    /**
     * Sets the {@code DockingStrategy} associated with specified {@code Class}.
     * This method returns with no action taken if the specified {@code Class}
     * paramter is {@code null}. If the {@code strategy} parameter is
     * {@code null} then any existing {@code DockingStrategy} association with
     * the specified }Class} is removed. Otherwise, a new
     * {@code DockingStrategy} association is added for the specified
     * {@code Class}.
     * <p>
     * {@code DockingStrategy} association follows a strict inheritance chain
     * using {@code org.flexdock.util.ClassMapping}. This means that the
     * association created by this method applies for the specified
     * {@code Class} and all direct subclasses, but associations for interfaces
     * are ignored. Associations also do not apply for subclasses that have
     * their own specific {@code DockingStrategy} mapping.
     * <p>
     *
     * @param classKey
     *            the {@code Class} whose {@code DockingStrategy} association we
     *            wish to set
     * @param strategy
     *            the {@code DockingStrategy} to be associated with the
     *            specified {@code Class}.
     * @see #getDockingStrategy(Class)
     * @see #getDockingStrategy(Object)
     * @see ClassMapping#addClassMapping(Class, Class, Object)
     * @see ClassMapping#removeClassMapping(Class)
     */
    public static void setDockingStrategy(Class classKey,
                                          DockingStrategy strategy) {
        if (classKey == null)
            return;

        if (strategy == null)
            DOCKING_STRATEGIES.removeClassMapping(classKey);
        else
            DOCKING_STRATEGIES.addClassMapping(classKey, strategy.getClass(),
                                               strategy);
    }

    /**
     * Undocks the specified {@code Dockable} from its parent
     * {@code DockingPort}. If the {@code Dockable} is {@code null}, or it
     * does not currently reside within a {@code DockingPort}, then this method
     * returns {@code false} with no action taken. Otherwise, this method
     * returns {@code true} if the undocking operation was successful and
     * {@code false} if the undocking operation could not be completed.
     *
     * This method determines the {@code DockingStrategy} to be used for
     * {@code DockingPort} containing the specified {@code Dockable} and defers
     * processing to the {@code undock(Dockable dockable)} method on the
     * {@code DockingStrategy}. This method's return value will be based upon
     * the {@code DockingStrategy} implementation returned by a call to
     * {@code getDockingStrategy(Object obj)}. The {@code DockingStrategy} used
     * may be controlled via
     * {@code setDockingStrategy(Class c, DockingStrategy strategy)}, supplying
     * a {@code DockingPort} implementation class and a customized
     * {@code DockingStrategy}.
     *
     * @param dockable
     *            the {@code Dockable} to be undocked.
     * @return {@code true} if the undocking operation was successful,
     *         {@code false} otherwise.
     * @see DockingStrategy#undock(Dockable)
     * @see #getDockingStrategy(Object)
     * @see #setDockingStrategy(Class, DockingStrategy)
     */
    public static boolean undock(Dockable dockable) {
        if (dockable == null)
            return false;

        DockingStrategy strategy = findDockingStrategy(dockable);
        if (strategy != null) {
            return strategy.undock(dockable);
        }

        return false; // TODO think of changing it to runtime exception I
        // don't see a situation when there would be no default docker.
    }

    public static boolean undock(final Component dockable) {
        return undock(resolveDockable(dockable));
    }

    /**
     * Ensures that a valid {@code DragManager} has been installed as a listener
     * for all of the specified {@code Dockable's} drag source
     * {@code Components}. This method invokes the {@code getDragSources()}
     * method on the specified {@code Dockable} and iterates over each
     * {@code Component} in the returned {@code List}. If any {@code Component}
     * does not have a valid {@code DragManager} listener installed, an
     * appropriate listener is added to enable drag-to-dock functionality.
     * <p>
     * This method is useful to application developers who manually attempt to
     * add new {@code Components} to a {@code Dockable's} drag source
     * {@code List}. However, it is not necessary to call this method unless
     * the drag source list has been updated <b>after</b> calling
     * {@code registerDockable(Dockable dockable)}, since
     * {@code registerDockable(Dockable dockable)} will automatically initialize
     * each drag source for the specified {@code Dockable}.
     * <p>
     * If the specified {@code Dockable} is {@code null}, then no
     * {@code Exception} is thrown and no action is taken.
     *
     * @param dockable
     *            the {@code Dockable} whose drag sources are to be checked for
     *            {@code DragManagers} and updated accordingly.
     * @see #registerDockable(Dockable)
     * @see Dockable#getDragSources()
     * @see DragManager
     */
    public static void updateDragListeners(Dockable dockable) {
        if (dockable == null)
            return;

        DragManager dragListener = getDragListener(dockable);
        if (dragListener == null) {
            dragListener = new DragManager(dockable);
        }

        for (Iterator it = dockable.getDragSources().iterator(); it.hasNext();) {
            Object obj = it.next();
            if (obj instanceof Component) {
                updateDragListeners((Component) obj, dragListener);
            }
        }
    }

    private static void removeDragListeners(Dockable dockable) {
        if (dockable == null)
            return;

        for (Iterator it = dockable.getDragSources().iterator(); it.hasNext();) {
            Object obj = it.next();
            if (obj instanceof Component) {
                removeDragListeners((Component) obj);
            }
        }
    }

    public static float getDefaultSiblingSize() {
        return getDockingManager().defaultSiblingSize;
    }

    public static void setDefaultSiblingSize(float size) {
        size = Math.max(size, 0);
        size = Math.min(size, 1);
        getDockingManager().defaultSiblingSize = size;
    }

    public static void setRubberBand(RubberBand rubberBand) {
        EffectsManager.setRubberBand(rubberBand);
    }

    public static void setDragPreview(DragPreview dragPreview) {
        EffectsManager.setPreview(dragPreview);
    }

    /**
     * Maximizes the {@code Dockable} associated with the specified component or
     * restores the {@code Dockable} if it is currently maximized. This method
     * forwards the request to {@link #toggleMaximized(Dockable)} after
     * obtaining the {@code Dockable} associated to the component via
     * {@link #getDockable(Component)}.
     *
     * @param comp
     * @see #toggleMaximized(Dockable)
     */
    public static void toggleMaximized(Component comp) {
        Dockable dockable = getDockable(comp);
        if (dockable == null) {
            return;
        }
        toggleMaximized(dockable);
    }

    /**
     * Maximizes the specified {@code Dockable} or restores the specified
     * {@code Dockable} if it is already maximized.
     * <p>
     * The scope of maximization is the <i>root</i> {@code DockingPort}. The
     * specified {@code Dockable}'s current {@code DockingPort} is asked to
     * temporarily lend the {@code Dockable} for maximization and the root
     * {@code DockingPort} is asked to temorarily host the {@code Dockable} and
     * display it such that it occupies all (or the majority) of its screen
     * resources. If the {@code Dockable} is already maximized, the root
     * {@code DockingPort} is asked to return to its original state and the
     * {@code Dockable} is returned to its original {@code DockingPort}.
     *
     * @param dockable
     */
    public static void toggleMaximized(Dockable dockable) {
        DockingPort rootPort = getRootDockingPort(dockable.getComponent());
        MaximizedState state = getMaximizedState(rootPort);
        if (state != null) {
            if (state.getDockable() != dockable) {
                throw new IllegalStateException(
                    "Can't maximize while different dockable is maximized");
                // maybe silently switch maximized dockables instead?
            }
            restoreFromMaximized(dockable, rootPort, state);
        } else {
            maximize(dockable, rootPort);
        }
    }

    public static boolean isMaximized(Dockable dockable) {
        DockingPort rootPort = getRootDockingPort(dockable.getComponent());
        MaximizedState state = getMaximizedState(rootPort);

        return state != null && state.getDockable().equals(dockable);
    }

    private static void maximize(Dockable dockable, DockingPort rootPort) {
        DockingPort originalPort = dockable.getDockingPort();
        MaximizedState state = new MaximizedState(dockable, originalPort);

        originalPort.releaseForMaximization(dockable);
        rootPort.installMaximizedDockable(dockable);

        maximizedStatesByRootPort.put(rootPort, state);
    }

    private static void restoreFromMaximized(Dockable dockable,
            DockingPort rootPort, MaximizedState state) {

        // restore original state in reverse order than maximizing it
        // (otherwise this will not work if original port and root port are
        // identical)
        rootPort.uninstallMaximizedDockable();
        state.getOriginalPort().returnFromMaximization();

        maximizedStatesByRootPort.remove(rootPort);
    }

    private static MaximizedState getMaximizedState(DockingPort rootPort) {
        return (MaximizedState) maximizedStatesByRootPort.get(rootPort);
    }
}
