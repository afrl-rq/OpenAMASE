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
package org.flexdock.perspective;

import java.awt.Component;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.defaults.DockingSplitPane;
import org.flexdock.docking.state.LayoutNode;
import org.flexdock.docking.state.tree.DockableNode;
import org.flexdock.docking.state.tree.DockingPortNode;
import org.flexdock.docking.state.tree.SplitNode;
import org.flexdock.util.SwingUtility;

/**
 * @author Christopher Butler
 */
public class LayoutBuilder {
    private static final LayoutBuilder SINGLETON = new LayoutBuilder();

    public static LayoutBuilder getInstance() {
        return SINGLETON;
    }

    private LayoutBuilder() {

    }

    public LayoutNode createLayout(DockingPort port) {
        if(port==null)
            return null;
        return createLayoutImpl(port);
    }

    private LayoutNode createLayoutImpl(DockingPort port) {
        DockingPortNode node = new DockingPortNode();
        node.setUserObject(port);
        Component docked = port.getDockedComponent();
        link(node, docked);
        return node;
    }

    private LayoutNode createLayout(JSplitPane split) {
        String region = (String)SwingUtility.getClientProperty(split, DockingConstants.REGION);
        Component left = split.getLeftComponent();
        Component right = split.getRightComponent();

        float percent;
        if (split instanceof DockingSplitPane && ((DockingSplitPane) split).getPercent() != -1) {
            percent = (float) ((DockingSplitPane) split).getPercent();
        } else {
            percent = SwingUtility.getDividerProportion(split);
        }

        SplitNode node = new SplitNode(split.getOrientation(), 0, percent, null);
        node.setDockingRegion(region);

        link(node, left);
        link(node, right);

        return node;
    }

    private LayoutNode createLayout(Dockable dockable) {
        if(dockable==null)
            return null;

        DockableNode node = new DockableNode();
        node.setDockableId(dockable.getPersistentId());
        return node;
    }

    private LayoutNode[] createLayout(JTabbedPane tabs) {
        int len = tabs.getComponentCount();
        LayoutNode[] nodes = new LayoutNode[len];
        for(int i=0; i<len; i++) {
            Component comp = tabs.getComponent(i);
            Dockable dockable = DockingManager.getDockable(comp);
            nodes[i] = createLayout(dockable);
        }
        return nodes;
    }

    private void link(LayoutNode node, Component child) {
        if(child instanceof DockingPort) {
            LayoutNode childNode = createLayoutImpl((DockingPort)child);
            link(node, childNode);
        } else if(child instanceof JSplitPane) {
            LayoutNode childNode = createLayout((JSplitPane)child);
            link(node, childNode);
        } else if (child instanceof JTabbedPane) {
            LayoutNode[] children = createLayout((JTabbedPane)child);
            for(int i=0; i<children.length; i++)
                link(node, children[i]);
        } else {
            Dockable dockable = DockingManager.getDockable(child);
            LayoutNode childNode = createLayout(dockable);
            link(node, childNode);
        }
    }

    private void link(LayoutNode parent, LayoutNode child) {
        if(child!=null)
            parent.add(child);
    }
}
