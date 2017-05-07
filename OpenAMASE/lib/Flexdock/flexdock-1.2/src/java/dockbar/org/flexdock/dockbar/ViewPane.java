// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 18, 2005
 */
package org.flexdock.dockbar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;

import org.flexdock.dockbar.event.ResizeListener;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.state.MinimizationManager;
import org.flexdock.plaf.common.border.SlideoutBorder;

/**
 * @author Christopher Butler
 */
public class ViewPane extends JPanel {
    private static final Dimension RESIZE_DIMS = new Dimension(3, 3);
    private static final MouseInputAdapter EMPTY_MOUSE_LISTENER = new MouseInputAdapter() {};
    public static final int UNSPECIFIED_PREFERRED_SIZE = -1;
    private DockbarManager manager;
    private JPanel dragEdge;
    private int prefSize;
    private boolean locked;


    public ViewPane(DockbarManager mgr) {
        super(new BorderLayout(0, 0));
        setBorder(new SlideoutBorder());

        manager = mgr;
        prefSize = UNSPECIFIED_PREFERRED_SIZE;

        dragEdge = new JPanel();
        dragEdge.setPreferredSize(RESIZE_DIMS);

        ResizeListener listener = new ResizeListener(mgr);
        dragEdge.addMouseListener(listener);
        dragEdge.addMouseMotionListener(listener);

        updateOrientation();

        // intercept rouge mouse events so they don't fall
        // through to the content pane
        addMouseListener(EMPTY_MOUSE_LISTENER);
        addMouseMotionListener(EMPTY_MOUSE_LISTENER);
    }



    public void updateContents() {
        // remove the currently docked component
        Component[] children = getComponents();
        for(int i=0; i<children.length; i++) {
            if(children[i]!=dragEdge)
                remove(children[i]);
        }

        // add the new component
        Dockable d = manager.getActiveDockable();
        Component c = d==null? null: d.getComponent();
        if(c!=null)
            add(c, BorderLayout.CENTER);
    }


    public void updateOrientation() {
        Border border = getBorder();
        if(border instanceof SlideoutBorder)
            ((SlideoutBorder)border).setOrientation(manager.getActiveEdge());

        // update the drag edge
        remove(dragEdge);
        add(dragEdge, getEdgeRegion());
        dragEdge.setCursor(getResizeCursor());

        // revalidate
        revalidate();
    }

    private String getEdgeRegion() {
        int orientation = manager.getActiveEdge();
        switch(orientation) {
        case MinimizationManager.TOP:
            return BorderLayout.SOUTH;
        case MinimizationManager.BOTTOM:
            return BorderLayout.NORTH;
        case MinimizationManager.RIGHT:
            return BorderLayout.WEST;
        default:
            return BorderLayout.EAST;
        }
    }

    public Cursor getResizeCursor() {
        int orientation = manager.getActiveEdge();
        return orientation==MinimizationManager.LEFT ||
               orientation==MinimizationManager.RIGHT?
               Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR):
               Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
    }

    public int getPrefSize() {
        return prefSize;
    }
    public void setPrefSize(int prefSize) {
        this.prefSize = prefSize;
    }
    public boolean isLocked() {
        return locked;
    }
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
