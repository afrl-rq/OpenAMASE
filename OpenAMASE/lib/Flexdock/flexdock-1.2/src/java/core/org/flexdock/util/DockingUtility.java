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
package org.flexdock.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.defaults.DefaultRegionChecker;
import org.flexdock.docking.props.DockablePropertySet;
import org.flexdock.docking.state.DockingState;
import org.flexdock.docking.state.MinimizationManager;

/**
 * @author Christopher Butler
 */
public class DockingUtility implements DockingConstants {
    private DockingUtility() {
        // does nothing
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
     * @param d
     *            the {@code Dockable} whose parent {@code DockingPort} is to be
     *            returned.
     * @return the imediate parent {@code DockingPort} that contains the
     *         specified {@code Dockable}.
     * @see #getParentDockingPort(Component)
     */
    public static DockingPort getParentDockingPort(Dockable d) {
        return d == null ? null : getParentDockingPort(d.getComponent());
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
     * @param comp
     *            the {@code Component} whose parent {@code DockingPort} is to
     *            be returned.
     * @return the immediate parent {@code DockingPort} that contains the
     *         specified {@code Component}.
     */
    public static DockingPort getParentDockingPort(Component comp) {
        DockingPort port = comp == null ? null : (DockingPort) SwingUtilities
                           .getAncestorOfClass(DockingPort.class, comp);
        if (port == null)
            return null;

        return port.isParentDockingPort(comp) ? port : null;
    }

    /**
     * Returns {@code true} if the specified {@code DockingPort} has an ancestor
     * {@code DockingPort}; {@code false} otherwise. If the specified
     * {@code DockingPort} is {@code null}, then this method returns
     * {@code false}.
     *
     * @param dockingPort
     *            the {@code DockingPort} to check for an ancestor port
     * @return {@code true} if the specified {@code DockingPort} has an ancestor
     *         {@code DockingPort}; {@code false} otherwise.
     * @see SwingUtilities#getAncestorOfClass(java.lang.Class,
     *      java.awt.Component)
     */
    public static boolean isSubport(DockingPort dockingPort) {
        return dockingPort == null ? false : SwingUtilities.getAncestorOfClass(
                   DockingPort.class, (Component) dockingPort) != null;
    }

    /**
     * Returns the deepest {@code DockingPort} within the specified
     * {@code Container} at the specified {@code location}. If either
     * {@code container} or {@code location} are {@code null}, then this method
     * returns {@code null}.
     * <p>
     * This method will find the deepest {@code Component} within the specified
     * container that the specified {@code Point} via
     * {@code SwingUtilities.getDeepestComponentAt(Component parent, int x, int y)}.
     * If no {@code Component} is resovled, then this method returns
     * {@code null}. If the resolved {@code Component} is a {@code DockingPort},
     * then it is returned. Otherwise, the {@code Component's}
     * {@code DockingPort} ancestor is resovled and returned from
     * {@code SwingUtilities.getAncestorOfClass(Class c, Component comp)},
     * passing {@code DockingPort.class} for the ancestor class parameter.
     *
     * @param container
     *            the {@code Container} within which to find a
     *            {@code DockingPort}.
     * @param location
     *            the point within the specified {@code Container} at which to
     *            search for a {@code DockingPort}.
     * @return the deepest {@code DockingPort} within the specified
     *         {@code Container} at the specified {@code location}.
     * @see SwingUtilities#getDeepestComponentAt(java.awt.Component, int, int)
     * @see SwingUtilities#getAncestorOfClass(java.lang.Class,
     *      java.awt.Component)
     */
    public static DockingPort findDockingPort(Container container,
            Point location) {
        if (container == null || location == null)
            return null;

        Component deepestComponent = SwingUtilities.getDeepestComponentAt(
                                         container, location.x, location.y);
        if (deepestComponent == null)
            return null;

        // we're assured here that the deepest component is both a Component and
        // DockingPort in
        // this case, so we're okay to return here.
        if (deepestComponent instanceof DockingPort)
            return (DockingPort) deepestComponent;

        // getAncestorOfClass() will either return a null or a Container that is
        // also an instance of
        // DockingPort. Since Container is a subclass of Component, we're fine
        // in returning both
        // cases.
        return (DockingPort) SwingUtilities.getAncestorOfClass(
                   DockingPort.class, deepestComponent);
    }

    /**
     * Returns the specified {@code region's} cross-axis equivalent region in
     * accordance with the orientation used by the specified {@code JSplitPane}.
     * If the {@code JSplitPane} is {@code null}, or the specified
     * {@code region} is invalid according to
     * {@code DockingManager.isValidDockingRegion(String region)}, then this
     * method returns {@code null}.
     *
     * {@code NORTH_REGION} and {@code SOUTH_REGION} are considered "vertical"
     * regions, while {@code WEST_REGION} and {@code EAST_REGION} are considered
     * horizontal regions. If the {@code JSplitPane} orientation matches the
     * specified {@code region} orientation, then the original {@code region}
     * value is returned. For instance, if the specified {@code region} is
     * {@code EAST_REGION}, and the {@code JSplitPane} is of a horizontal
     * orientation, then there is no need to translate the {@code region}
     * parameter across axes since its current axis is already horizontal. In
     * this case, {@code EAST_REGION} would be returned by this method.
     * <p>
     * If the axis of the specified {@code region} does not match the
     * orientation of the {@code JSplitPane}, then the region is translated to
     * its cross-axis equivalent and returns. In this case, {@code NORTH_REGION}
     * will be translated to {@code WEST_REGION}, {@code SOUTH_REGION} to
     * {@code EAST_REGION}, {@code WEST_REGION} to {@code NORTH_REGION}, and
     * {@code EAST_REGION} to {@code SOUTH_REGION}. {@code CENTER_REGION} is
     * never altered.
     *
     * @param splitPane
     *            the {@code JSplitPane} whose orientation is to be used as a
     *            target axis
     * @param region
     *            the docking region to translate to the target axis
     * @return the specified {@code region's} cross-axis equivalent region in
     *         accordance with the orientation used by the specified
     *         {@code JSplitPane}.
     * @see DockingManager#isValidDockingRegion(String)
     * @see JSplitPane#getOrientation()
     * @see #isAxisEquivalent(String, String)
     */
    public static String translateRegionAxis(JSplitPane splitPane, String region) {
        if (splitPane == null || !DockingManager.isValidDockingRegion(region))
            return null;

        boolean horizontal = splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT;
        if (horizontal) {
            if (NORTH_REGION.equals(region))
                region = WEST_REGION;
            else if (SOUTH_REGION.equals(region))
                region = EAST_REGION;
        } else {
            if (WEST_REGION.equals(region))
                region = NORTH_REGION;
            else if (EAST_REGION.equals(region))
                region = SOUTH_REGION;
        }
        return region;
    }

    /**
     * Returns the opposite docking region of the specified {@code region}. For
     * {@code NORTH_REGION}, this method returns {@code SOUTH_REGION}. For
     * {@code SOUTH_REGION}, this method returns {@code NORTH_REGION}. For
     * {@code EAST_REGION}, this method returns {@code WEST_REGION}. For
     * {@code WEST_REGION}, this method returns {@code EAST_REGION}. For
     * {@code CENTER_REGION} or an invalid region, as specified by
     * {@code DockingManager.isValidDockingRegion(String region)}, this method
     * return {@code CENTER_REGION}.
     *
     * @param region
     *            the region whose opposite is to be returned.
     * @return the opposite docking region of the specified {@code region}.
     * @see DockingManager#isValidDockingRegion(String)
     */
    public static String flipRegion(String region) {
        if (!DockingManager.isValidDockingRegion(region)
                || CENTER_REGION.equals(region))
            return CENTER_REGION;

        if (NORTH_REGION.equals(region))
            return SOUTH_REGION;

        if (SOUTH_REGION.equals(region))
            return NORTH_REGION;

        if (EAST_REGION.equals(region))
            return WEST_REGION;

        return EAST_REGION;
    }

    /**
     * Tests for region equivalency between the specified region parameters
     * across horizontal and vertical axes. If either {@code region} or
     * {@code otherRegion} are {@code null} or invalid according to
     * {@code DockingManager.isValidDockingRegion(String region)}, then this
     * method returns {@code false}.
     * <p>
     * Equivalency within the same axis means that the two specified regions are
     * the same value, as each region is unique within its axis. Thus, this
     * method returns {@code true} if {@code region.equals(otherRegion)} returns
     * {@code true}. This includes {@code CENTER_REGION}, which is axis
     * independent.
     * <p>
     * {@code CENTER_REGION} is not an axis equivalent to any region other than
     * itself since it is the only docking region that does not correspond to a
     * horizontal or vertical axis. If either the specified {@code region} or
     * {@code otherRegion} is {@code CENTER_REGION} and the other is not, then
     * this method returns {@code false}.
     * <p>
     * Equivalancy across axes follows a top-to-left and bottom-to-right
     * mapping. In this fashion, {@code NORTH_REGION} and {@code WEST_REGION}
     * are equivalent and {@code SOUTH_REGION} and {@code EAST_REGION} are
     * equivalent. These combination will return {@code true} for this method.
     * All other region combinatinos will cause this method to return
     * {@code false}.
     *
     * @param region
     *            the first region to check for equivalency
     * @param otherRegion
     *            the second region to check for equivalency
     * @return {@code true} if the two specified regions are equal or cross-axis
     *         equivalents, {@code false} otherwise.
     * @see DockingManager#isValidDockingRegion(String)
     */
    public static boolean isAxisEquivalent(String region, String otherRegion) {
        if (!DockingManager.isValidDockingRegion(region)
                || !DockingManager.isValidDockingRegion(otherRegion))
            return false;

        if (region.equals(otherRegion))
            return true;

        if (CENTER_REGION.equals(region))
            return false;

        if (NORTH_REGION.equals(region))
            return WEST_REGION.equals(otherRegion);
        if (SOUTH_REGION.equals(region))
            return EAST_REGION.equals(otherRegion);
        if (EAST_REGION.equals(region))
            return SOUTH_REGION.equals(otherRegion);
        if (WEST_REGION.equals(region))
            return NORTH_REGION.equals(otherRegion);

        return false;
    }

    /**
     * Returns {@code true} if the specified {@code region} is equal to either
     * {@code NORTH_REGION} or {@code WEST_REGION}. Returns {@code false}
     * otherwise.
     *
     * @param region
     *            the {@code region} to check for top or left equivalency
     * @return {@code true} if the specified {@code region} is equal to either
     *         {@code NORTH_REGION} or {@code WEST_REGION}; {@code false}
     *         otherwise.
     * @see DockingConstants#NORTH_REGION
     * @see DockingConstants#WEST_REGION
     */
    public static boolean isRegionTopLeft(String region) {
        return NORTH_REGION.equals(region) || WEST_REGION.equals(region);
    }

    /**
     * Returns the {@code String} docking region for the specified orientation
     * constant. {@code LEFT} maps to {@code WEST_REGION}, {@code RIGHT} maps
     * to {@code EAST_REGION}, {@code TOP} maps to {@code NORTH_REGION},
     * {@code BOTTOM} maps to {@code SOUTH_REGION}, and {@code CENTER} maps to
     * {@code CENTER_REGION}. All other integer values will cause this method
     * to return {@code UNKNOWN_REGION}.
     * <p>
     * All constants, both integer an {@code String} values, can be found on the
     * {@code DockingConstants} interface.
     *
     * @param regionType
     *            the orientation constant to translate into a docking region
     * @return the {@code String} docking region for the specified orientation
     *         constant.
     * @see DockingConstants#LEFT
     * @see DockingConstants#RIGHT
     * @see DockingConstants#TOP
     * @see DockingConstants#BOTTOM
     * @see DockingConstants#CENTER
     * @see DockingConstants#WEST_REGION
     * @see DockingConstants#EAST_REGION
     * @see DockingConstants#NORTH_REGION
     * @see DockingConstants#SOUTH_REGION
     * @see DockingConstants#CENTER_REGION
     * @see DockingConstants#UNKNOWN_REGION
     */
    public static String getRegion(int regionType) {
        switch (regionType) {
        case LEFT:
            return WEST_REGION;
        case RIGHT:
            return EAST_REGION;
        case TOP:
            return NORTH_REGION;
        case BOTTOM:
            return SOUTH_REGION;
        case CENTER:
            return CENTER_REGION;
        default:
            return UNKNOWN_REGION;
        }
    }

    /**
     * Returns {@code true} if the specified {@code Dockable} is currently
     * minimized; {@code false} otherwise. If the {@code Dockable} is
     * {@code null}, then this method returns {@code false}.
     * <p>
     * This method retrieves the current {@code DockingState} instance
     * associated with the {@code Dockable} and calls it's {@code isMinimized()}
     * method to return. {@code DockingState} for the specified {@code Dockable}
     * is queried by calling {@code getDockingState(Dockable dockable)} on the
     * {@code DockingManager's} currently installed {@code LayoutManager}.
     *
     * @param dockable
     *            the {@code Dockable} whose minimized state is to be returned
     * @return {@code true} if the specified {@code Dockable} is currently
     *         minimized; {@code false} otherwise.
     * @see DockingState#isMinimized()
     * @see DockingManager#getLayoutManager()
     * @see org.flexdock.docking.state.LayoutManager#getDockingState(Dockable)
     */
    public static boolean isMinimized(Dockable dockable) {
        if (dockable == null)
            return false;

        DockingState info = getDockingState(dockable);
        return info == null ? false : info.isMinimized();
    }

    /**
     * Returns an {@code int} value representing the current minimization
     * constraint for the specified {@code Dockable}. If the {@code Dockable}
     * is {@code null}, then this method returns
     * {@code MinimizationManager.UNSPECIFIED_LAYOUT_CONSTRAINT}.
     * <p>
     * This method retrieves the current {@code DockingState} instance
     * associated with the {@code Dockable} and calls it's
     * {@code getMinimizedConstraint()} method to return. {@code DockingState}
     * for the specified {@code Dockable} is queried by calling
     * {@code getDockingState(Dockable dockable)} on the
     * {@code DockingManager's} currently installed {@code LayoutManager}.
     *
     * @param dockable
     *            the {@code Dockable} whose minimized constraint is to be
     *            returned
     * @return an {@code int} value representing the current minimization
     *         constraint for the specified {@code Dockable}
     * @see MinimizationManager#UNSPECIFIED_LAYOUT_CONSTRAINT
     * @see DockingState#getMinimizedConstraint()()
     * @see DockingManager#getLayoutManager()
     * @see org.flexdock.docking.state.LayoutManager#getDockingState(Dockable)
     */
    public static int getMinimizedConstraint(Dockable dockable) {
        int defaultConstraint = MinimizationManager.UNSPECIFIED_LAYOUT_CONSTRAINT;
        DockingState info = dockable == null ? null : getDockingState(dockable);
        return info == null ? defaultConstraint : info.getMinimizedConstraint();
    }

    private static DockingState getDockingState(Dockable dockable) {
        return DockingManager.getLayoutManager().getDockingState(dockable);
    }

    /**
     * Docks the specified {@code Dockable} relative to another already-docked
     * {@code Dockable} in the specified region. The "parent" {@code Dockable}
     * must currently be docked. If not, this method will return {@code false}.
     * Otherwise, its parent {@code DockingPort} will be resolved and the new
     * {@code Dockable} will be docked into the {@code DockingPort} relative to
     * the "parent" {@code Dockable}. This method defers processing to
     * {@code dockRelative(Dockable dockable, Dockable parent, String relativeRegion, float ratio)}
     * passing {@code UNSPECIFIED_SIBLING_PREF} for the {@code ratio} parameter.
     * <p>
     * This method returns {@code false} if any of the input parameters are
     * {@code null} or if the specified {@code region} is invalid according to
     * {@code DockingManager.isValidDockingRegion(String region)}. If the
     * specified region is other than CENTER, then a split layout should result.
     *
     * @param dockable
     *            the {@code Dockable} to be docked
     * @param parent
     *            the {@code Dockable} used as a reference point for docking
     * @param relativeRegion
     *            the docking region into which {@code dockable} will be docked
     * @return {@code true} if the docking operation was successful;
     *         {@code false} otherwise.
     * @see #dockRelative(Dockable, Dockable, String, float)
     * @see DockingManager#isValidDockingRegion(String)
     * @see Dockable#getDockingPort()
     * @see DockingManager#dock(Dockable, DockingPort, String)
     */
    public static boolean dockRelative(Dockable dockable, Dockable parent,
                                       String relativeRegion) {
        return dockRelative(parent, dockable, relativeRegion,
                            UNSPECIFIED_SIBLING_PREF);
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
     * <p>
     * This method returns {@code false} if any of the input parameters are
     * {@code null} or if the specified {@code region} is invalid according to
     * {@code DockingManager.isValidDockingRegion(String region)}.
     *
     * @param dockable
     *            the {@code Dockable} to be docked
     * @param parent
     *            the {@code Dockable} used as a reference point for docking
     * @param relativeRegion
     *            the docking region into which {@code dockable} will be docked
     * @param ratio
     *            the proportional space to allot the {@code dockable} argument
     *            if the docking operation results in a split layout.
     * @return {@code true} if the docking operation was successful;
     *         {@code false} otherwise.
     * @see DockingManager#isValidDockingRegion(String)
     * @see Dockable#getDockingPort()
     * @see DockingManager#dock(Dockable, DockingPort, String)
     */
    public static boolean dockRelative(Dockable dockable, Dockable parent,
                                       String relativeRegion, float ratio) {
        if (parent == null || dockable == null
                || !DockingManager.isValidDockingRegion(relativeRegion))
            return false;

        // set the sibling preference
        setSiblingPreference(parent, relativeRegion, ratio);

        DockingPort port = parent.getDockingPort();
        if (port != null)
            return DockingManager.dock(dockable, port, relativeRegion);

        return false;
    }

    private static void setSiblingPreference(Dockable src, String region,
            float size) {
        if (size == UNSPECIFIED_SIBLING_PREF || CENTER_REGION.equals(region)
                || !DockingManager.isValidDockingRegion(region))
            return;

        size = DefaultRegionChecker.validateSiblingSize(size);
        src.getDockingProperties().setSiblingSize(region, size);
    }

    /**
     * Returns {@code true} if the specified {@code Dockable} is currently
     * docked within a floating dialog. This method returns {@code false} if the
     * {@code Dockable} is presently, minimized, hidden, docked within the main
     * application layout, or if the {@code Dockable} parameter is {@code null}.
     * <p>
     * This method retrieves the current {@code DockingState} instance
     * associated with the {@code Dockable} and calls it's {@code isFloating()}
     * method to return. {@code DockingState} for the specified {@code Dockable}
     * is queried by calling {@code getDockingState(Dockable dockable)} on the
     * {@code DockingManager's} currently installed {@code LayoutManager}.
     *
     * @param dockable
     *            the {@code Dockable} whose floating state is to be returned
     * @return {@code true} if the specified {@code Dockable} is currently
     *         floating; {@code false} otherwise.
     * @see DockingState#isFloating()
     * @see DockingManager#getLayoutManager()
     * @see org.flexdock.docking.state.LayoutManager#getDockingState(Dockable)
     */
    public static boolean isFloating(Dockable dockable) {
        DockingState info = getDockingState(dockable);
        return info == null ? false : info.isFloating();
    }

    /**
     * Returns {@code true} if the specified {@code Dockable} is currently
     * docked within a {@code DockingPort}. This method returns {@code false}
     * if the {@code Dockable} is presently floating, minimized, hidden, or if
     * the {@code Dockable} parameter is {@code null}.
     *
     * @param dockable
     *            the {@code Dockable} whose embedded state is to be returned
     * @return {@code true} if the specified {@code Dockable} is currently
     *         docked within a {@code DockingPort}; {@code false} otherwise.
     * @see DockingManager#isDocked(Dockable)
     * @see #isFloating(Dockable)
     */
    public static boolean isEmbedded(Dockable dockable) {
        return dockable == null ? false : DockingManager.isDocked(dockable)
               && !isFloating(dockable);
    }

    /**
     * Sets the divider location of the split layout embedded within the
     * specified {@code DockingPort}. This method differs from both
     * {@code setSplitProportion(Dockable dockable, float proportion)} in that
     * this method resolves the split layout embedded <b>within</b> the
     * specified {@code DockingPort}, whereas the other method modifies the
     * split layout <b>containing</b> its respective {@code Dockable}
     * parameter.
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
     * @see SwingUtility#setSplitDivider(JSplitPane, float)
     */
    public static void setSplitProportion(DockingPort port, float proportion) {
        if (port == null)
            return;

        Component comp = port.getDockedComponent();
        if (comp instanceof JSplitPane)
            SwingUtility.setSplitDivider((JSplitPane) comp, proportion);
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
     * @see SwingUtility#setSplitDivider(JSplitPane, float)
     */
    public static void setSplitProportion(Dockable dockable, float proportion) {
        if (dockable == null)
            return;

        Component comp = dockable.getComponent();
        Container parent = comp.getParent();
        if (parent instanceof JTabbedPane) {
            parent = parent.getParent();
        }
        if (!(parent instanceof DockingPort))
            return;

        Container grandParent = parent.getParent();
        if (grandParent instanceof JSplitPane)
            SwingUtility.setSplitDivider((JSplitPane) grandParent, proportion);
    }

    /**
     * Returns the text to be used by a {@code Dockable} as a tab label within a
     * tabbed layout. This method retrieves the associated
     * {@code DockablePropertySet} by calling {@code getDockingProperties()} on
     * the specified {@code Dockable}. It then returns the value retrieved from
     * calling {@code getDockableDesc()} on the {@code DockablePropertySet}
     * instance. If the specified {@code Dockable} is {@code null}, then this
     * method returns {@code null}.
     *
     * @param dockable
     *            the {@code Dockable} whose tab-text is to be returned
     * @return the text to be used by a {@code Dockable} as a tab label within a
     *         tabbed layout.
     * @see Dockable#getDockingProperties()
     * @see DockablePropertySet#getDockableDesc()
     */
    public static String getTabText(Dockable dockable) {
        DockablePropertySet props = dockable == null ? null : dockable
                                    .getDockingProperties();
        return props == null ? null : props.getDockableDesc();
    }

    /**
     * Returns {@code true} if the specific {@code Object} is a {@code Dockable}.
     * If {@code obj instanceof Dockable} is {@code true}, then this method
     * returns {@code true}. A {@code null} parameter will cause this method to
     * return {@code false}.
     * <p>
     * Registered {@code Dockable} components, if they are {@code JComponents},
     * will also have a {@code Boolean} client property present with the key
     * {@code Dockable.DOCKABLE_INDICATOR}, used by dockable
     * {@code JComponents} that don't implement the {@code Dockable} interface
     * directly, but acquire docking capabilities through a separate wrapper
     * {@code Dockable} implementation. For these components, the
     * {@code instanceof} check is insufficient since the valid {@code Dockable}
     * is implemented by a separate class. Therefore, if the {@code instanceof}
     * check fails, and the supplied {@code Object} parameter is a
     * {@code JComponent}, a client property with the key
     * {@code Dockable.DOCKABLE_INDICATOR} is checked for a value of
     * {@code Boolean.TRUE}. If the client property is present, then this
     * method returns {@code true}.
     *
     * @param obj
     *            the {@code Object} to be checked to see if it represents a
     *            valid {@code Dockable}
     * @return {@code true} if the specific {@code Object} is a {@code Dockable}
     * @see Dockable#DOCKABLE_INDICATOR
     * @see Boolean#TRUE
     * @see javax.swing.JComponent#getClientProperty(java.lang.Object)
     */
    public static boolean isDockable(Object obj) {
        if (obj == null)
            return false;

        // if the object directly implements Dockable, then we can return from
        // here.
        if (obj instanceof Dockable)
            return true;

        // if the object is a JComponent, but not a Dockable implementation,
        // then check its
        // client property indicator
        if (obj instanceof JComponent) {
            Component comp = (Component) obj;
            return SwingUtility.getClientProperty(comp,
                                                  Dockable.DOCKABLE_INDICATOR) == Boolean.TRUE;
        }

        // they may have a heavyweight Component that does not directly
        // implement Dockable.
        // in this case, Component does not have client properties we can check.
        // we'll have to
        // check directly with the DockingManager.
        if (obj instanceof Component) {
            Component comp = (Component) obj;
            return DockingManager.getDockable(comp) != null;
        }

        return false;
    }

    public static Dockable getAncestorDockable(Component comp) {
        if (comp == null)
            return null;

        if (isDockable(comp))
            return DockingManager.getDockable(comp);

        Container parent = comp.getParent();
        while (parent != null && !(parent instanceof JRootPane)) {
            if (isDockable(parent))
                return DockingManager.getDockable(parent);
            parent = parent.getParent();
        }
        return null;
    }

    public static boolean isActive(Dockable dockable) {
        if (dockable == null)
            return false;
        return dockable.getDockingProperties().isActive().booleanValue();
    }
}
