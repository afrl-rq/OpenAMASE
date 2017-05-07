// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.demos.raw.elegant;

import java.awt.Component;

import javax.swing.JComponent;

import org.flexdock.docking.DockingManager;
import org.flexdock.docking.defaults.AbstractDockable;

public class DockableImpl extends AbstractDockable {
    private ElegantPanel panel;
    private JComponent dragInitiator;

    public DockableImpl(ElegantPanel dockable, JComponent dragInit, String id) {
        super(id);
        if(dockable==null)
            new IllegalArgumentException(
                "Cannot create DockableImpl with a null DockablePanel.");
        if(dragInit==null)
            new IllegalArgumentException(
                "Cannot create DockableImpl with a null drag initiator.");

        panel = dockable;
        dragInitiator = dragInit;
        setTabText(panel.getTitle());
        getDragSources().add(dragInit);
        getFrameDragSources().add(dockable.getTitlebar());
        DockingManager.registerDockable(this);
    }

    public Component getComponent() {
        return panel;
    }
}
