// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 13, 2005
 */
package org.flexdock.dockbar;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.state.MinimizationManager;
import org.flexdock.plaf.common.border.SlideoutBorder;


/**
 * @author Christopher Butler
 */
public class Dockbar extends JPanel {
    protected int orientation;
    protected DockbarManager manager;
    protected ArrayList mDocks = new ArrayList();

    static {
        // make sure DockbarLabel is initialized
        Class c = DockbarLabel.class;
    }

    public static int getValidOrientation(int orient) {
        switch (orient) {
        case MinimizationManager.LEFT:
            return MinimizationManager.LEFT;
        case MinimizationManager.RIGHT:
            return MinimizationManager.RIGHT;
        case MinimizationManager.BOTTOM:
            return MinimizationManager.BOTTOM;
        default:
            return MinimizationManager.LEFT;
        }
    }

    public Dockbar(DockbarManager manager, int orientation) {
        this.manager = manager;
        setBorder(new SlideoutBorder());
        setOrientation(orientation);
    }

    void undock(Dockable dockable) {
        DockbarLabel label = getLabel(dockable);

        remove(label);
        mDocks.remove(label);
        getParent().validate();
        repaint();
    }

    public DockbarLabel getLabel(Dockable dockable) {
        if(dockable==null)
            return null;

        for (Iterator docks = mDocks.iterator(); docks.hasNext();) {
            DockbarLabel label = (DockbarLabel) docks.next();

            if (label.getDockable() == dockable)
                return label;
        } // for

        return null;
    }

    public boolean contains(Dockable dockable) {
        return getLabel(dockable)!=null;
    }

    public void dock(Dockable dockable) {
        if(dockable==null)
            return;

        DockbarLabel currentLabel = getLabel(dockable);
        if (currentLabel!=null) {
            currentLabel.setActive(false);
            return;
        }

        DockbarLabel newLabel = new DockbarLabel(dockable.getPersistentId(), getOrientation());
        add(newLabel);
        mDocks.add(newLabel);

        getParent().validate();
        repaint();
    }

    public int getOrientation() {
        return orientation;
    }

    private void setOrientation(int orientation) {
        orientation = getValidOrientation(orientation);
        this.orientation = orientation;

        Border border = getBorder();
        if(border instanceof SlideoutBorder)
            ((SlideoutBorder)border).setOrientation(orientation);

        int boxConstraint = orientation==MinimizationManager.TOP ||
                            orientation==MinimizationManager.BOTTOM? BoxLayout.LINE_AXIS: BoxLayout.PAGE_AXIS;
        setLayout(new BoxLayout(this, boxConstraint));
    }

    public Dimension getPreferredSize() {
        if(mDocks.size()==0)
            return new Dimension(0,0);

        DockbarLabel label = (DockbarLabel)getComponent(0);
        return label.getPreferredSize();
    }

    void activate(String dockableId, boolean lock) {
        if(manager!=null) {
            manager.setActiveDockable(dockableId);
            if(lock)
                manager.getActivationListener().lockViewpane();
        }
    }
}