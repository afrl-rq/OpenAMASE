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
package org.flexdock.docking.state;

import java.awt.Component;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;

/**
 * @author Christopher Butler
 */
public interface MinimizationManager {

    MinimizationManager DEFAULT_STUB = new Stub();

    int UNSPECIFIED_LAYOUT_CONSTRAINT = -1;

    int TOP = DockingConstants.TOP;

    int LEFT = DockingConstants.LEFT;

    int BOTTOM = DockingConstants.BOTTOM;

    int RIGHT = DockingConstants.RIGHT;

    int CENTER = DockingConstants.CENTER;

    boolean close(Dockable dockable);

    void preview(Dockable dockable, boolean locked);

    void setMinimized(Dockable dockable, boolean minimized, Component window, int constraint);

    class Stub implements MinimizationManager {
        public boolean close(Dockable dockable) {
            return false;
        }

        public void preview(Dockable dockable, boolean locked) {}

        public void setMinimized(Dockable dockable, boolean minimized, Component window, int edge) {}

    }

}
