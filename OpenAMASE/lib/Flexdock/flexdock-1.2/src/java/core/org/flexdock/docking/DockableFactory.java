// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on May 26, 2005
 */
package org.flexdock.docking;

import java.awt.Component;

/**
 * This interface is designed to provide an API for allowing the
 * {@code DockingManager} to obtain {@code Dockable} instances on the fly. It
 * has a single method, {@code getDockableComponent(String dockableId)},
 * responsible for returning {@code Component} instances, possibly creating and
 * registering {@code Dockables} in the process.
 *
 * Implementations of this interface will be application-specific and may be
 * plugged into the {@code DockingManager} via the call
 * {@code DockingManager.setDockableFactory(myFactory)}. Throughout the
 * framework, FlexDock makes many calls to
 * {@code DockingManager.getDockable(String id)} under the assumption that at
 * some point, the requested {@code Dockable} instance has been registered via
 * {@code DockingManager.registerDockable(Dockable dockable)}.
 *
 * In the event that a {@code Dockable} with the specified ID has never been
 * formally registered, the {@code DockingManager} will check for a factory via
 * {@code DockingManager.getDockableFactory()}. If a factory is present, its
 * {@code getDockableComponent()} method is invoked. If a valid
 * {@code Component} is returned from {@code getDockableComponent()}, the
 * DockingManager will attempt to register it as a {@code Dockable} and return
 * the {@code Dockable}.
 *
 * {@code DockableFactory} implementations are especially useful for
 * applications with persisted layouts where the {@code Dockables} required
 * during a layout restoration may be constructed automatically on demand by the
 * framework.
 *
 * @author Christopher Butler
 */
public interface DockableFactory {

    /**
     * Returns a {@code Component} for the specified Dockable ID, possibly
     * creating and registering a {@code Dockable} in the process.
     *
     * @param dockableId
     *            the ID for the requested dockable {@code Component}
     * @return the {@code Component} for the specified ID
     */
    Component getDockableComponent(String dockableId);

    /**
     * Returns a {@code Dockable} for the specified Dockable ID, possibly
     * creating and registering it in the process.
     *
     * @param dockableId
     *            the ID for the requested {@code Dockable}
     * @return the {@code Dockable} for the specified ID
     */
    Dockable getDockable(String dockableId);

    /**
     * An empty implementation of {@code DockableFactory}.
     */
    public static class Stub implements DockableFactory {

        /**
         * {@inheritDoc}
         *
         * @return {@code null}.
         */
        public Dockable getDockable(String dockableId) {
            return null;
        }

        /**
         * {@inheritDoc}
         *
         * @return {@code null}.
         */
        public Component getDockableComponent(String dockableId) {
            return null;
        }
    }
}
