// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 14, 2005
 */
package org.flexdock.docking;

import javax.swing.JSplitPane;

import org.flexdock.docking.drag.DragOperation;

/**
 * This interface defines an API used by {@code DockingManager} and
 * {@code DockingPort} to support customizable behaviors during docking
 * operations. {@code DockingManager} will associate a {@code DockingStrategy}
 * with a particular {@code DockingPort} or {@code Dockable} class type. Calls
 * to {@code DockingManager.dock()} and {@code DockingManager.undock()} will be
 * deferred to the {@code DockingStrategy} associated with the parameters
 * supplied in the respective method calls. {@code DockingStrategies} are also
 * responsible for creating sub-DockingPorts and split panes for nested
 * {@code DockingPorts}
 *
 * Implementations of {@code DockingStrategy} are responsible for managing
 * component relationships between {@code Dockables} and parent containers. This
 * includes making determinations as to whether a particular docking operation
 * will be allowed for the specified parameters and the specifics of how a
 * particular {@code Dockable} may be removed from one parent
 * {@code Container} or {@code DockingPort} and added to another.
 * {@code DockingStrategy} may determine whether a call to {@code dock()}
 * implies an attempt to float a {@code Dockable} in a separate window.
 *
 * Because of the potentially large scope of responsibilities associated with a
 * {@code DockingStrategy}, implementations may range from being very simple to
 * highly complex. Although custom implementations of {@code DockingStrategy}
 * are not discouraged, the recommeded path is to subclass
 * {@code DefaultDockingStrategy} for consistency of behavior.
 *
 * {@code DockingStrategies} are associated with a particular type of
 * {@code Dockable} or {@code DockingPort} by calling
 * {@code DockingManager.setDockingStrategy(Class c, DockingStrategy strategy)}.
 * {@code DefaultDockingStrategy} is the default implementation used for all
 * classes that do not have a custom {@code DockingStrategy} registered.
 *
 * @author Christopher Butler
 */
public interface DockingStrategy {

    /**
     * Attempts to dock the specified {@code Dockable} into the supplied
     * {@code DockingPort} in the specified region. If docking is not possible
     * for the specified parameters, then the method returns {@code false} and
     * no action is taken. Since there is no {@code DragOperation} parameter
     * present, this method implies programmatic docking as opposed to docking
     * as a result of drag-events.
     *
     * @param dockable
     *            the {@code Dockable} we wish to dock
     * @param dockingPort
     *            the {@code DockingPort} into which we wish to dock
     * @param dockingRegion
     *            the region of the specified {@code DockingPort} into which we
     *            wish to dock.
     * @return whether or not the docking operation was successful.
     */
    boolean dock(Dockable dockable, DockingPort dockingPort,
                 String dockingRegion);

    /**
     * Attempts to dock the specified {@code Dockable} into the supplied
     * {@code DockingPort} in the specified region based upon the semantics of
     * the specified {@code DragOperation}.   If docking is not possible
     * for the specified parameters, then the method returns {@code false} and
     * no action is taken.
     *
     * @param dockable
     *            the {@code Dockable} we wish to dock
     * @param dockingPort
     *            the {@code DockingPort} into which we wish to dock
     * @param dockingRegion
     *            the region of the specified {@code DockingPort} into which we
     *            wish to dock.
     * @param operation
     *            the {@code DragOperation} describing the state of the
     *            application/mouse at the point in time in which we're
     *            attempting to dock.
     * @return whether or not the docking operation was successful.
     */
    boolean dock(Dockable dockable, DockingPort dockingPort,
                 String dockingRegion, DragOperation operation);

    /**
     * Undocks the specified {@code Dockable} instance from its containing
     * {@code DockingPort}.
     *
     * @param dockable
     *            the {@code Dockable} we wish to undock
     * @return {@code true} if the {@code Dockable} was successfully undocked.
     *         Otherwise, returns {@code false}.
     */
    boolean undock(Dockable dockable);

    /**
     * Creates and returns a new {@code DockingPort} instance based upon the
     * supplied {@code DockingPort} parameter. For layouts that support nested
     * {@code DockingPorts}, this method is useful for creating child
     * {@code DockingPorts} suitable for embedding within the base
     * {@code DockingPort}
     *
     * @param base
     *            the {@code DockingPort} off of which the returned instance
     *            will be based.
     * @return a new {@code DockingPort} instance based upon the supplied
     *         parameter.
     */
    DockingPort createDockingPort(DockingPort base);

    /**
     * Creates and returns a new {@code JSplitPane} instance based upon the
     * supplied parameters. The returned {@code JSplitPane} should be suitable
     * for embedding within the base {@code DockingPort} and its orientation
     * should reflect the supplied {@code region} parameter.
     *
     * @param base
     *            the {@code DockingPort} off of which the returned
     *            {@code JSplitPane} will be based.
     * @param region
     *            the region within the base {@code DockingPort} used to
     *            determine the orientation of the returned {@code JSplitPane}.
     * @return a new {@code JSplitPane} suitable for embedding within the base
     *         {@code DockingPort} parameter.
     */
    JSplitPane createSplitPane(DockingPort base, String region);

    /**
     * Returns the initial divider location to be used by the specified
     * {@code JSplitPane}. This method assumes that the {@code JSplitPane}
     * parameter is embedded within the specified {@code DockingPort} and that
     * is has been validated and its current dimensions are non-zero.
     *
     * @param dockingPort
     *            the {@code DockingPort} that contains, or will contain the
     *            specified {@code JSplitPane}.
     * @param splitPane
     *            the {@code JSplitPane} whose initial divider location is to be
     *            determined.
     * @return the desired divider location of the supplied {@code JSplitPane}.
     */
    int getInitialDividerLocation(DockingPort dockingPort, JSplitPane splitPane);

    /**
     * Returns the desired divider proportion of the specified
     * {@code JSplitPane} after rendering. This method assumes that the
     * {@code JSplitPane} parameter is, or will be embedded within the specified
     * {@code DockingPort}. This method does <b>not</b> assume that the
     * {@code JSplitPane} has been validated and that it's current dimensions
     * are non-zero.
     *
     * @param dockingPort
     *            the {@code DockingPort} that contains, or will contain the
     *            specified {@code JSplitPane}.
     * @param splitPane
     *            the {@code JSplitPane} whose initial divider location is to be
     *            determined.
     * @return the desired divider proportion of the supplied {@code JSplitPane}.
     */
    double getDividerProportion(DockingPort dockingPort, JSplitPane splitPane);
}
