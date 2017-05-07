// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on May 24, 2005
 */
package org.flexdock.perspective;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.defaults.DefaultDockingStrategy;
import org.flexdock.docking.event.DockingEvent;
import org.flexdock.docking.event.DockingListener;
import org.flexdock.docking.floating.frames.DockingFrame;
import org.flexdock.docking.state.DockingPath;
import org.flexdock.docking.state.DockingState;
import org.flexdock.docking.state.FloatManager;
import org.flexdock.docking.state.MinimizationManager;
import org.flexdock.docking.state.tree.SplitNode;
import org.flexdock.util.DockingUtility;
import org.flexdock.util.RootWindow;
import org.flexdock.util.SwingUtility;

/**
 * @author Christopher Butler
 */
public class DockingStateListener extends DockingListener.Stub {

    private boolean isEnabled = true;

    public synchronized boolean isEnabled() {
        return isEnabled;
    }

    public synchronized void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public void dockingComplete(final DockingEvent dockingEvent) {
        if(!isEnabled()) {
            return;
        }

        Dockable dockable = dockingEvent.getDockable();
        DockingState info = getDockingState(dockable);
        // if docking has just completed, then we cannot be in a minimized state
        info.setMinimizedConstraint(MinimizationManager.UNSPECIFIED_LAYOUT_CONSTRAINT);

        // update the floating state
        RootWindow window = RootWindow.getRootContainer(dockable.getComponent());
        FloatManager floatManager = DockingManager.getLayoutManager().getFloatManager();
        Component frame = window==null? null: window.getRootContainer();
        if(frame instanceof DockingFrame) {
            String groupId = ((DockingFrame)window.getRootContainer()).getGroupName();
            floatManager.addToGroup(dockable, groupId);
        } else {
            floatManager.removeFromGroup(dockable);
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                updateState(dockingEvent);
            }
        });
    }

    public void undockingStarted(DockingEvent evt) {
        if(isEnabled()) {
            updateState(evt);
        }
    }

    public void updateState(Dockable dockable) {
        if(dockable==null)
            return;

        if(DockingUtility.isEmbedded(dockable)) {
            updateEmbedded(dockable);
        }
    }

    private void updateEmbedded(Dockable dockable) {
        if(!dockable.getComponent().isValid())
            return;

        updateCenterPoint(dockable);
        updateDockingPath(dockable);
        updateRelative(dockable);
    }

    private void updateState(DockingEvent evt) {
        updateState(evt.getDockable());
    }

    private float getSplitPaneRatio(Dockable dockable, String region) {
        // check to see if the dockable was in a split layout.  if so, get the deepest split
        // node we can find so we can grab the split proportion percentage.
        SplitNode lastSplitNode = DockingPath.createNode(dockable);
        if (lastSplitNode != null) {
            return lastSplitNode.getPercentage();
        }

        // if we couldn't determine the splitPane ratio using the DockingPath above, then
        // try the regionInsets
        Float ratioObject = dockable.getDockingProperties().getRegionInset(region);
        if (ratioObject != null) {
            return ratioObject.floatValue();
        }
        // if we still can't find a specified splitPane percentage, then use
        // the default value
        return DockingManager.getDefaultSiblingSize();
    }

    private void updateCenterPoint(Dockable dockable) {
        // get the center point of the dockable
        Component comp = dockable.getComponent();
        Point p = new Point(comp.getWidth()/2, comp.getHeight()/2);

        // convert it to a location on the rootPane
        RootWindow window = RootWindow.getRootContainer(comp);
        Container contentPane = window.getContentPane();
        p = SwingUtilities.convertPoint(comp, p, contentPane);

        // now, convert to a proportional location on the rootPane
        float x = (float)p.x/(float)contentPane.getWidth() * 100f;
        float y = (float)p.y/(float)contentPane.getHeight() * 100f;
        p.x = Math.round(x);
        p.y = Math.round(y);

        // store the center point
        DockingState info = getDockingState(dockable);
        info.setCenter(p);
    }

    private void updateDockingPath(Dockable dockable) {
        DockingState info = getDockingState(dockable);
        DockingPath path = DockingPath.create(dockable);
        info.setPath(path);
    }

    private void updateRelative(Dockable dockable) {
        DockingState info = getDockingState(dockable);
        DockingPort port = dockable.getDockingPort();

        // don't update relative info for tabbed layout, since we
        // technically have more than one relative.
        if(port.getDockedComponent()!=dockable.getComponent())
            return;

        Component comp = ((Component)port).getParent();
        // if we're not inside a split pane, then there is no relative
        if(!(comp instanceof JSplitPane)) {
            setNullRelative(info);
            return;
        }

        JSplitPane splitPane = (JSplitPane)comp;
        Component siblingComp = SwingUtility.getOtherComponent(splitPane, (Component)port);
        if(!(siblingComp instanceof DockingPort)) {
            setNullRelative(info);
            return;
        }

        Component otherDocked = ((DockingPort)siblingComp).getDockedComponent();
        Dockable sibling = otherDocked instanceof JSplitPane || otherDocked instanceof JTabbedPane? null: DockingManager.getDockable(otherDocked);
        if(sibling==null) {
            setNullRelative(info);
            return;
        }

        // if we got here, then we are definitely sharing a split layout with another dockable.
        String region = DefaultDockingStrategy.findRegion(dockable.getComponent());
        float ratio = SwingUtility.getDividerProportion(splitPane);

        // set the relative docking info
        info.setRelativeParent(sibling);
        info.setRegion(region);
        info.setSplitRatio(ratio);

        // make the sibling aware of us
        info = getDockingState(sibling);
        info.setRelativeParent(dockable);
        info.setRegion(DockingUtility.flipRegion(region));
        info.setSplitRatio(ratio);
    }

    private void setNullRelative(DockingState info) {
        info.setRelativeParentId((String)null);
        info.setSplitRatio(DockingConstants.UNINITIALIZED);
        info.setRegion(DockingConstants.CENTER_REGION);
    }

    private DockingState getDockingState(Dockable dockable) {
        return getPerspective().getDockingState(dockable, true);
    }

    private Perspective getPerspective() {
        return PerspectiveManager.getInstance().getCurrentPerspective();
    }

}
