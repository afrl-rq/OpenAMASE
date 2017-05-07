// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 10, 2005
 */
package org.flexdock.util;

import java.awt.Component;

import javax.swing.JRootPane;

import org.flexdock.docking.Dockable;

/**
 * This is a utility class for small, short-lived object instances used to find
 * nested {@code Components}. It models a relationship between nested
 * {@code Components} within an AWT {@code Container} hierarchy such that, given
 * a starting {@code Component} from which to search, it will find two
 * {@code Components} nested within each other of specific class types. The
 * "deeper" component will be a descendent of the "parent" component.
 * <p>
 * For example, given a {@code JTextField} within a content frame, the
 * application logic may need to check to see if the text field resides within a
 * certain application-specific container set. Perhaps all {@code JTables}
 * embedded within {@code JSplitPanes} are significant within the particular
 * application. The
 * {@code find(Component searchSrc, Class childClass, Class parentClass)} method
 * on this class will be able to return a {@code NestedComponents} instance
 * indicating whether the specified text field resides within a {@code JTable}
 * that is embedded within a {@code JSplitPane}.
 * <p>
 * Although perhaps a bit contrived, this example shows a generic use for this
 * class. The FlexDock framework itself has a particular interest in
 * {@code Dockable} components that are embedded within {@code DockingPorts},
 * especially during drag operations. As a {@code Dockable} is dragged over an
 * {@code DockingPort}, this class allows the framework to determine with a
 * single object instance any {@code Dockables} currently embedded within the
 * target {@code DockingPort}, starting with the deepest {@code Component} at
 * the current mouse point during the drag.
 * <p>
 * This classes' member fields are {@code public} and may be both accessed and
 * modified by external code as needed within their particular usage context.
 * This is by design for ease of use within the FlexDock framework.
 * Consequently, instances of this class should only be used for short-lived
 * operations. Since its member fields may be modified publicly, instances of
 * this class should not be cached, nor should its member values be indexed as
 * they are subject to arbitrary changes over the long term.
 *
 * @author Christopher Butler
 */
public class NestedComponents {
    public Component searchSrc;

    public Component child;

    public Component parent;

    /**
     * Creates and returns a new {@code NestedComponents} instance, searching
     * the parent {@code Container} hierarcy of the specified {@code searchSrc}
     * for an ancestor of type {@code childClass} and a more senior ancestor of
     * type {@code parentClass}.
     * <p>
     * If either {@code searchSrc}, {@code childClass}, or {@code parentClass}
     * is {@code null}, this method returns {@code null}.
     * <p>
     * If {@code searchSrc} is an instanceof {@code childClass}, then the
     * {@code child} field on the resulting {@code NestedComponents} will be
     * equal (==) to the {@code searchSrc} field. If {@code searchSrc} is an
     * instanceof {@code parentClass}, then the {@code parent} field on the
     * resulting {@code NestedComponents} will be equal (==) to the
     * {@code searchSrc} field. If an instance of {@code parentClass} is found
     * before {@code childClass}, this the resulting {@code NestedComponents}
     * instance will have a {@code null} {@code child} field.
     *
     * @param searchSrc
     *            the {@code Component} from which to start searching for parent
     *            {@code Containers}.
     * @param childClass
     *            the {@code Class} of the desired "child" {@code Component}
     * @param parentClass
     *            the {@code Class} of the desired "parent" {@code Component}
     * @return a new {@code NestedComponents} instance based upon the specified
     *         parameters.
     */
    public static NestedComponents find(Component searchSrc, Class childClass,
                                        Class parentClass) {
        if (searchSrc == null || childClass == null || parentClass == null)
            return null;

        NestedComponents nest = new NestedComponents(searchSrc, null, null);

        Component c = searchSrc;
        while (c != null && !(c instanceof JRootPane)) {
            if (nest.child == null && isInstanceOf(c, childClass)) {
                nest.child = c;
            } else if (isParentContainer(c, parentClass)) {
                nest.parent = c;
                break;
            }
            c = c.getParent();
        }

        return nest;
    }

    private static boolean isParentContainer(Component c, Class parentClass) {
        if (parentClass == RootWindow.class) {
            return RootWindow.isValidRootContainer(c);
        } else
            return parentClass.isAssignableFrom(c.getClass());
    }

    private static boolean isInstanceOf(Object obj, Class clazz) {
        if (clazz.isAssignableFrom(obj.getClass()))
            return true;

        // special case
        if (clazz == Dockable.class) {
            return DockingUtility.isDockable(obj);
        }

        return false;
    }

    private NestedComponents(Component searchSrc, Component child,
                             Component parent) {
        this.searchSrc = searchSrc;
        this.child = child;
        this.parent = parent;
    }

    /**
     * Returns {@code true} if both {@code child} and {@code parent} fields are
     * non-{@code null}; {@code false} otherwise.
     *
     * @return {@code true} if both {@code child} and {@code parent} fields are
     *         non-{@code null}; {@code false} otherwise.
     */
    public boolean isFull() {
        return child != null && parent != null;
    }

    /**
     * Overridden to match the {@code equals()} method.
     *
     * @return a hash code value for this object.
     * @see #equals(Object)
     */
    public int hashCode() {
        int h = searchSrc.hashCode();
        h += child == null ? 0 : child.hashCode();
        h += parent == null ? 0 : parent.hashCode();
        return h;
    }

    /**
     * Returns {@code true} if the specified {@code Object} is a
     * {@code NestedComponents} instance and all shares all of the same field
     * references (==) as this {@code NestedComponents} for field
     * {@code searchSrc}, {@code child}, and {@code parent}.
     *
     * @param obj
     *            the {@code Object} to test for equality
     * @return {@code true} if the specified {@code Object} is "equal" to this
     *         {@code NestedComponents} instance; {@code false} otherwise.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof NestedComponents))
            return false;

        NestedComponents other = (NestedComponents) obj;
        return searchSrc == other.searchSrc && child == other.child
               && parent == other.parent;
    }
}
