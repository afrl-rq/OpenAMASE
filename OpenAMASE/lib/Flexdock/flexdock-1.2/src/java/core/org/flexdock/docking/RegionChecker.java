// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 11, 2005
 */
package org.flexdock.docking;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * This interface provides an API for determining the desired regional bounds
 * for a {@code Dockable} component. Implementing classes are responsible for
 * determining the bounds and proportional sizes for both docking regions and
 * sibling components.
 *
 * As a {@code Dockable} is dragged across a {@code DockingPort} containing
 * another embedded {@code Dockable}, a determination must be made as to where
 * the dragged {@code Dockable} will be docked within the target
 * {@code DockingPort} based upon the current mouse position relative to
 * the embedded {@code Dockable} underneath the mouse. Classes that implement
 * this interface are responsible for making such determinations.
 *
 * For example, if a {@code Dockable} is dragged over another {@code Dockable}
 * embedded within a {@code DockingPort}, and the current mouse position is
 * near the top edge of the embedded {@code Dockable}, the current
 * {@code RegionChecker} is responsible for determining whether the user is
 * attempting to dock in the {@code north}, {@code east}, {@code west}, or
 * {@code center} of the embedded {@code Dockable}. The visual
 * {@code DragPreview} displayed to the end user should reflect this
 * determination.
 *
 * Once the docking operation is complete and the layout has been split between
 * both {@code Dockables}, the actual percentage of space allotted to the new
 * {@code Dockable} in the layout, referred to as the "sibling", is also
 * determined by the current {@code RegionChecker} implementation.
 *
 * @author Christopher Butler
 * @author Mateusz Szczap
 */
public interface RegionChecker {

    /**
     * Default maximum region size.
     */
    float MAX_REGION_SIZE = .5F;

    /**
     * Default minimum region size.
     */
    float MIN_REGION_SIZE = .0F;

    /**
     * Default maximum sibling size.
     */
    float MAX_SIBILNG_SIZE = 1F;

    /**
     * Default minimum sibling size.
     */
    float MIN_SIBILNG_SIZE = .0F;

    /**
     * Default region size.
     */
    float DEFAULT_REGION_SIZE = .25F;

    /**
     * Default sibling size.
     */
    float DEFAULT_SIBLING_SIZE = .5F;

    /**
     * A key to find a system property that will override the default sibling
     * size in this interface.
     *
     * @see #DEFAULT_SIBLING_SIZE
     */
    String DEFAULT_SIBLING_SIZE_KEY = "default.sibling.size";

    /**
     * Returns the docking region of the supplied {@code Component} that
     * contains the coordinates of the specified {@code Point}. Valid return
     * values are those regions defined in {@code DockingConstants} and include
     * {@code CENTER_REGION}, {@code NORTH_REGION}, {@code SOUTH_REGION},
     * {@code EAST_REGION}, {@code WEST_REGION}, or {@code UNKNOWN_REGION}.
     *
     * @param component
     *            the {@code Component} whose region is to be examined.
     * @param point
     *            the coordinates whose region is to be determined.
     * @return the docking region containing the specified {@code Point}.
     */
    String getRegion(Component component, Point point);

    /**
     * Returns the rectangular bounds within the specified component that
     * represent it's {@code DockingConstants.NORTH_REGION}.
     *
     * @param component
     *            the {@code Component} whose north region is to be returned.
     * @return the bounds containing the north region of the specified
     *         {@code Component}.
     */
    Rectangle getNorthRegion(Component component);

    /**
     * Returns the rectangular bounds within the specified component that
     * represent it's {@code DockingConstants.SOUTH_REGION}.
     *
     * @param component
     *            the {@code Component} whose south region is to be returned.
     * @return the bounds containing the south region of the specified
     *         {@code Component}.
     */
    Rectangle getSouthRegion(Component component);

    /**
     * Returns the rectangular bounds within the specified component that
     * represent it's {@code DockingConstants.EAST_REGION}.
     *
     * @param component
     *            the {@code Component} whose east region is to be returned.
     * @return the bounds containing the east region of the specified
     *         {@code Component}.
     */
    Rectangle getEastRegion(Component component);

    /**
     * Returns the rectangular bounds within the specified component that
     * represent it's {@code DockingConstants.WEST_REGION}.
     *
     * @param component
     *            the {@code Component} whose west region is to be returned.
     * @return the bounds containing the west region of the specified
     *         {@code Component}.
     */
    Rectangle getWestRegion(Component component);

    /**
     * Returns the rectangular bounds within the specified component that
     * represent the specified region. Valid values for the {@code region}
     * parameter are those regions defined in {@code DockingConstants} and
     * include {@code NORTH_REGION}, {@code SOUTH_REGION}, {@code EAST_REGION},
     * and {@code WEST_REGION}. All other region values should result in this
     * method returning a {@code null} reference.
     *
     * @param component
     *            the {@code Component} whose region bounds are to be returned.
     * @param region
     *            the specified region that is to be examined.
     * @return the bounds containing the supplied region of the specified
     *         {@code Component}.
     */
    Rectangle getRegionBounds(Component component, String region);

    /**
     * Returns a percentage representing the amount of space allotted for the
     * specified region within the specified {@code Component}. For example, a
     * return value of 0.25F for NORTH_REGION implies that the top 25% of the
     * supplied {@code Component's} bounds rectangle is to be interpreted as the
     * {@code Component's} northern region. Valid values for the {@code region}
     * parameter are those regions defined in {@code DockingConstants} and
     * include {@code NORTH_REGION}, {@code SOUTH_REGION}, {@code EAST_REGION},
     * and {@code WEST_REGION}. All other region values should result in this
     * method returning the constant {@code DEFAULT_SIBLING_SIZE}.
     *
     * @param component
     *            the {@code Component} whose region is to be examined.
     * @param region
     *            the specified region that is to be examined.
     * @return the percentage of the specified {@code Component} allotted for
     *         the specified region.
     */
    float getRegionSize(Component component, String region);

    /**
     * A {@code Rectangle} representing the actual amount of space to allot for
     * sibling {@code Components} should they be docked into the specified
     * region. This method differs from
     * {@code getRegionBounds(Component c, String region)} in that
     * {@code getRegionBounds()} determines the amount to space used to check
     * whether a {@code Component's} docking will intersect with a particular
     * region, whereas this method returns the actual amount of space said
     * {@code Component} will take up after docking has been completed. Valid
     * values for the {@code region} parameter are those regions defined in
     * {@code DockingConstants} and include {@code NORTH_REGION},
     * {@code SOUTH_REGION}, {@code EAST_REGION}, and {@code WEST_REGION}.
     * All other region values should result in this method returning the
     * constant {@code DEFAULT_SIBLING_SIZE}.
     *
     * @param component
     *            the {@code Component} whose sibling bounds are to be returned.
     * @param region
     *            the specified region that is to be examined.
     * @return the bounds containing the sibling bounds desired for
     *         {@code Components} docked into the specified region of the of the
     *         specified {@code Component}.
     */
    Rectangle getSiblingBounds(Component component, String region);

    /**
     * Returns a percentage representing the amount of space allotted for
     * sibling {@code Components} to be docked within the specified region of
     * the supplied {@code Component}. This method differs from
     * {@code getRegionSize(Component c, String region)} in that
     * {@code getRegionSize()} determines the proportional space used to check
     * whether a {@code Component's} docking will intersect with a particular
     * region, whereas this method returns the proportional space said
     * {@code Component} will take up after docking has been completed. Valid
     * values for the {@code region} parameter are those regions defined in
     * {@code DockingConstants} and include {@code NORTH_REGION},
     * {@code SOUTH_REGION}, {@code EAST_REGION}, and {@code WEST_REGION}.
     * All other region values should result in this method returning the
     * constant {@code DEFAULT_SIBLING_SIZE}.
     *
     * @param component
     *            the {@code Component} whose sibling proportions are to be
     *            returned.
     * @param region
     *            the specified region that is to be examined.
     * @return the percentage of the specified {@code Component} allotted for
     *         sibling {@code Components} that are to be docked into the
     *         specified region.
     */
    float getSiblingSize(Component component, String region);
}
