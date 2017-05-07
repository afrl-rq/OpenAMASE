// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Jun 23, 2005
 */
package org.flexdock.docking;

import java.awt.Component;

/**
 * @author Christopher Butler
 */
public interface DockingStub {

    /**
     * Returns the {@code Component} that is the event source for drag
     * operations. The component may or may not be the same as the Component
     * returned by {@code getFrameDragSource()}.
     *
     * @see #getFrameDragSource()
     */
    Component getDragSource();

    /**
     * Returns the {@code Component} that is used as a frame drag source. When
     * this {@code DockingStub} is floated into an external frame, that frame
     * may or may not have a titlebar for repositioning. The Component returned
     * by this method will be setup with appropriate event listeners such that
     * dragging them will serve to reposition the containing frame as if they
     * were the frame titlebar. If the Component returned by this method and the
     * one returned by {@code getDragSource()} is the same, then then "frame
     * reposition" behavior will supercede any "drag-to-dock" behavior while
     * this stub is in a floating state.
     *
     * @see #getDragSource()
     */
    Component getFrameDragSource();

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
     */
    String getPersistentId();

    /**
     * Gets the tab text for this class.
     *
     * @return the text placed in a {@code JTabbedPane} tab.
     * @see javax.swing.JTabbedPane
     */
    String getTabText();

}
