// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 8, 2005
 */
package org.flexdock.docking.event;

import java.util.EventListener;

/**
 * @author Kevin Duffey
 * @author Christopher Butler
 */
public interface DockingListener extends EventListener {

    /**
     * Fired when docking of a <code>Dockable</code> has completed.
     *
     * @param evt
     *            the <code>DockingEvent</code> event which provides the
     *            source Dockable, the old DockingPort and the new DockingPort
     */
    void dockingComplete(DockingEvent evt);

    /**
     * Fired when docking of a <code>Dockable</code> is canceled during the operation.
     *
     * @param evt
     *            the <code>DockingEvent</code> event which provides the
     *            source Dockable, the old DockingPort and the new DockingPort
     */
    void dockingCanceled(DockingEvent evt);


    /**
     * Fired when the dragging of a <code>Dockable</code> has begun.
     *
     * @param evt
     *            the <code>DockingEvent</code> event which provides the
     *            source Dockable, the old DockingPort and the new DockingPort
     */
    void dragStarted(DockingEvent evt);


    /**
     * Fired when the dropping of a <code>Dockable</code> has begun at the release
     * of a drag-operation.
     *
     * @param evt
     *            the <code>DockingEvent</code> event which provides the
     *            source Dockable, the old DockingPort and the new DockingPort
     */
    void dropStarted(DockingEvent evt);

    void undockingComplete(DockingEvent evt);

    void undockingStarted(DockingEvent evt);

    class Stub implements DockingListener {

        public void dockingCanceled(DockingEvent evt) {}

        public void dockingComplete(DockingEvent evt) {}

        public void dragStarted(DockingEvent evt) {}

        public void dropStarted(DockingEvent evt) {}

        public void undockingComplete(DockingEvent evt) {}

        public void undockingStarted(DockingEvent evt) {}

    }

}