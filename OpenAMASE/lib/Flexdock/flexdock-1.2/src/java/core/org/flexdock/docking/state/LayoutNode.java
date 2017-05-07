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
package org.flexdock.docking.state;

import java.io.Serializable;

import javax.swing.tree.MutableTreeNode;

/**
 * @author Christopher Butler
 */
public interface LayoutNode extends MutableTreeNode, Cloneable, Serializable {

    Object getUserObject();

    Object getDockingObject();

    void add(MutableTreeNode child);

    Object clone();

}
