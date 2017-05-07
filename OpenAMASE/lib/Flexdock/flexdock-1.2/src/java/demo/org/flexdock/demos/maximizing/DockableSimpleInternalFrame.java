// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.demos.maximizing;

import java.awt.Component;

import org.flexdock.docking.DockingManager;
import org.flexdock.docking.defaults.AbstractDockable;

public class DockableSimpleInternalFrame extends AbstractDockable {
    private Component component;

    public DockableSimpleInternalFrame(SimpleInternalFrame sif) {
        this(sif, sif.getTitle());
    }

    public DockableSimpleInternalFrame(SimpleInternalFrame sif, String id) {
        super(id);
        this.component = sif;
        getDragSources().add(sif.getDragHandle());
        getFrameDragSources().add(sif.getDragHandle());
        setTabText(sif.getTitle());
    }

    public Component getComponent() {
        return component;
    }

    public void dispose() {
        DockingManager.unregisterDockable(this);
        component = null;
    }
}
