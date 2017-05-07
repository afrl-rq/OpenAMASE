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

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.state.LayoutNode;

/**
 * @author Christopher Butler
 */
public abstract class DockingNode extends DefaultMutableTreeNode implements LayoutNode, DockingConstants {

    public Object getUserObject() {
        Object obj = super.getUserObject();
        if(obj==null) {
            obj = getDockingObject();
            setUserObject(obj);
        }
        return obj;
    }

    public abstract Object getDockingObject();

    protected abstract DockingNode shallowClone();

    public Object clone() {
        return deepClone();
    }

    public DockingNode deepClone() {
        DockingNode clone = shallowClone();
        for(Enumeration en=children(); en.hasMoreElements();) {
            DockingNode child = (DockingNode)en.nextElement();
            clone.add(child.deepClone());
        }
        return clone;
    }

}
