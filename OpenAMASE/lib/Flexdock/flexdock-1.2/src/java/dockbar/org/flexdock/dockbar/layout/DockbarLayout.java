// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 21, 2005
 */
package org.flexdock.dockbar.layout;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.border.Border;

import org.flexdock.dockbar.Dockbar;
import org.flexdock.dockbar.DockbarManager;
import org.flexdock.dockbar.ViewPane;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.props.DockablePropertySet;
import org.flexdock.docking.state.MinimizationManager;
import org.flexdock.util.Utilities;

/**
 * @author Christopher Butler
 */
public class DockbarLayout {
    public static final int MINIMUM_VIEW_SIZE = 20;
    private static final int[] EDGES = {
        MinimizationManager.LEFT, MinimizationManager.RIGHT,
        MinimizationManager.BOTTOM
    };

    private DockbarManager manager;
    private JComponent leftEdgeGuide;
    private JComponent rightEdgeGuide;
    private JComponent bottomEdgeGuide;

    public DockbarLayout(DockbarManager mgr) {
        manager = mgr;
    }



    public void layout() {
        // makes sure our current insets are up to date before trying to layout
        // the dockbars.  otherwise, they will be layed out with improper bounds.
        updateInsets();

        Rectangle rect = DockbarLayoutManager.getManager().getLayoutArea(manager);
        int rightX = rect.x + rect.width;
        int bottomY = rect.y + rect.height;

        Dockbar leftBar = manager.getLeftBar();
        Dockbar rightBar = manager.getRightBar();
        Dockbar bottomBar = manager.getBottomBar();

        Dimension leftPref = leftBar.getPreferredSize();
        Dimension rightPref = rightBar.getPreferredSize();
        Dimension bottomPref = bottomBar.getPreferredSize();

        // set the dockbar bounds
        leftBar.setBounds(rect.x, rect.y, leftPref.width, rect.height-bottomPref.height);
        rightBar.setBounds(rightX-rightPref.width, rect.y, rightPref.width, rect.height-bottomPref.height);
        // use complete window width for proper statusbar support
        bottomBar.setBounds(rect.x, bottomY-bottomPref.height, rect.width, bottomPref.height);
        layoutViewpane();
    }


    public int getDesiredViewpaneSize() {
        Dockable dockable = manager.getActiveDockable();
        if(dockable==null)
            return 0;

        Rectangle rect = DockbarLayoutManager.getManager().getViewArea(manager, dockable);
        DockablePropertySet props = dockable.getDockingProperties();

        // determine what percentage of the viewable area we want the viewpane to take up
        float viewSize = props.getPreviewSize().floatValue();
        int edge = manager.getActiveEdge();
        if(edge==MinimizationManager.LEFT || edge==MinimizationManager.RIGHT) {
            return (int)(((float)rect.width)*viewSize);
        }
        return (int)(((float)rect.height)*viewSize);
    }

    protected void layoutViewpane() {
        ViewPane viewPane = manager.getViewPane();
        Dockable dockable = manager.getActiveDockable();
        if(dockable==null) {
            viewPane.setBounds(0, 0, 0, 0);
            return;
        }

        int edge = manager.getActiveEdge();
        int viewpaneSize = viewPane.getPrefSize();
        if(viewpaneSize==ViewPane.UNSPECIFIED_PREFERRED_SIZE)
            viewpaneSize = getDesiredViewpaneSize();

        Rectangle rect = DockbarLayoutManager.getManager().getViewArea(manager, dockable);
        if(edge==MinimizationManager.LEFT || edge==MinimizationManager.RIGHT) {
            if(edge==MinimizationManager.RIGHT) {
                rect.x = rect.x + rect.width - viewpaneSize;
            }
            rect.width = viewpaneSize;
        } else {
            if(edge==MinimizationManager.BOTTOM) {
                rect.y = rect.y + rect.height - viewpaneSize;
            }
            rect.height = viewpaneSize;
        }
        viewPane.setBounds(rect);
    }


    private void updateInsets() {
        Insets emptyInsets = getEmptyInsets();
        boolean changed = resetGuideBorders();

        HashSet borderSet = new HashSet(3);
        add(borderSet, getInsetBorder(MinimizationManager.LEFT));
        add(borderSet, getInsetBorder(MinimizationManager.RIGHT));
        add(borderSet, getInsetBorder(MinimizationManager.BOTTOM));

        HashSet guideSet = new HashSet(3);
        add(guideSet, getCurrentEdgeGuide(MinimizationManager.LEFT));
        add(guideSet, getCurrentEdgeGuide(MinimizationManager.RIGHT));
        add(guideSet, getCurrentEdgeGuide(MinimizationManager.BOTTOM));

        for(Iterator it=borderSet.iterator(); it.hasNext();) {
            InsetBorder border = (InsetBorder)it.next();
            changed = border.setEmptyInsets(emptyInsets) || changed;
        }

        if(changed) {
            for(Iterator it=guideSet.iterator(); it.hasNext();) {
                JComponent guide = (JComponent)it.next();
                guide.revalidate();
            }
        }
    }

    private boolean resetGuideBorders() {
        boolean changed = resetGuide(MinimizationManager.LEFT);
        changed = resetGuide(MinimizationManager.RIGHT) || changed ;
        changed = resetGuide(MinimizationManager.BOTTOM) || changed ;

        toggleInsetBorder(MinimizationManager.LEFT);
        toggleInsetBorder(MinimizationManager.RIGHT);
        toggleInsetBorder(MinimizationManager.BOTTOM);

        return changed;
    }

    private boolean resetGuide(int edge) {
        JComponent currGuide = getCurrentEdgeGuide(edge);
        JComponent newGuide = queryEdgeGuide(edge);
        boolean changed = false;

        if(Utilities.isChanged(currGuide, newGuide)) {
            changed = true;
            // if the edge-guide has changed, then we're going to try to remove the old
            // InsetBorder from the previous edge-guide
            Border b = currGuide==null? null: currGuide.getBorder();
            InsetBorder fakeBorder = b instanceof InsetBorder? (InsetBorder)b: null;
            // check to see if the previous edge-guide already had an InsetBorder
            if(fakeBorder!=null) {
                // replace the InsetBorder with the old edge-guide's real border
                Border realBorder = fakeBorder.getWrappedBorder();
                currGuide.setBorder(realBorder);
                fakeBorder.toggleEdge(edge, false);
            }
        }

        // if there is no new edge-guide, then we can't set an InsetBorder for it
        if(newGuide==null)
            return setCurrentEdgeGuide(edge, newGuide) || changed;

        Border border = newGuide.getBorder();
        // if the new edge-guide doesn't have an InsetBorder, then install one
        if(!(border instanceof InsetBorder)) {
            changed = true;
            InsetBorder insetBorder = InsetBorder.createBorder(border, true, new Insets(-1, -1, -1, -1));
            newGuide.setBorder(insetBorder);
        }

        // make sure our edge tracking on the InsetBorder is cleared out.
        // we will rebuild it later on
        InsetBorder insetBorder = (InsetBorder)newGuide.getBorder();
        insetBorder.clearEdges();

        // update the edge-guide reference
        return setCurrentEdgeGuide(edge, newGuide) || changed;
    }

    private void toggleInsetBorder(int edge) {
        InsetBorder border = getInsetBorder(edge);
        if(border!=null) {
            ((InsetBorder)border).toggleEdge(edge, true);
        }
    }

    private InsetBorder getInsetBorder(int edge) {
        JComponent comp = getCurrentEdgeGuide(edge);
        Border border = comp==null? null: comp.getBorder();
        return border instanceof InsetBorder? (InsetBorder)border: null;
    }


    private Insets getEmptyInsets() {
        return new Insets(0, getLeftInset(), getBottomInset(), getRightInset());
    }

    private int getLeftInset() {
        return getDockbarInset(manager.getLeftBar());
    }

    private int getRightInset() {
        return getDockbarInset(manager.getRightBar());
    }

    private int getBottomInset() {
        return getDockbarInset(manager.getBottomBar());
    }

    private int getDockbarInset(Dockbar dockbar) {
        boolean visible = dockbar.isVisible();
        if(!visible)
            return 0;

        Dimension dim = dockbar.getPreferredSize();
        if(dockbar==manager.getLeftBar() || dockbar==manager.getRightBar())
            return dim.width;
        return dim.height;
    }

    private JComponent queryEdgeGuide(int constraint) {
        return DockbarLayoutManager.getManager().getEdgeGuide(manager, constraint);
    }

    private JComponent getCurrentEdgeGuide(int constraint) {
        switch(constraint) {
        case MinimizationManager.LEFT:
            return leftEdgeGuide;
        case MinimizationManager.RIGHT:
            return rightEdgeGuide;
        case MinimizationManager.BOTTOM:
            return bottomEdgeGuide;
        }
        return null;
    }

    private boolean setCurrentEdgeGuide(int constraint, JComponent comp) {
        boolean changed = getCurrentEdgeGuide(constraint)==comp;
        switch(constraint) {
        case MinimizationManager.LEFT:
            leftEdgeGuide = comp;
            break;
        case MinimizationManager.RIGHT:
            rightEdgeGuide = comp;
            break;
        case MinimizationManager.BOTTOM:
            bottomEdgeGuide = comp;
            break;
        }
        return changed;
    }

    private void add(Set set, Object obj) {
        if(obj!=null)
            set.add(obj);
    }
}
