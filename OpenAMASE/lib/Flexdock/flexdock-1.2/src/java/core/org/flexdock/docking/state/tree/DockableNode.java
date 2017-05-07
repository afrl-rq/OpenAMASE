// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on May 28, 2005
 */
package org.flexdock.docking.state.tree;

import javax.swing.tree.MutableTreeNode;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;

/**
 * @author Christopher Butler
 */
public class DockableNode extends DockingNode {

    private String dockableId;

    public DockableNode() {
    }

    private DockableNode(String id) {
        dockableId = id;
    }

    public String getDockableId() {
        return dockableId;
    }

    public void setDockableId(String dockableId) {
        this.dockableId = dockableId;
    }

    public Dockable getDockable() {
        return DockingManager.getDockable(dockableId);
    }

    public void add(MutableTreeNode newChild) {
        // noop
    }

    public Object getDockingObject() {
        return getDockable();
    }

    protected DockingNode shallowClone() {
        return new DockableNode(dockableId);
    }

}
