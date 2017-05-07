// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.swing;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

/**
 * A MouseListener that allows users to easily manage creating a Pop-up menu.
 * This class should be added to components using the {@link JComponent#addMouseListener(java.awt.event.MouseListener)} method.
 * <br/>
 *
 * To prevent pop-ups after a drag event, also add this adapter to the component
 * using the {@link JComponent#addMouseMotionListener(java.awt.event.MouseMotionListener) }
 * method. <br/>
 *
 * Implement the
 * <code>setMenuContents()</code> method to fill the popup menu. 
 * 
 * @author AFRL/RQQD
 */
public abstract class PopupMenuAdapter extends MouseAdapter {

    private boolean dragging = false;
    JPopupMenu menu = null;

    public PopupMenuAdapter() {
    }

    /**
     * If this is a pop-up event, then the popup menu is created.
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            showPopup(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!dragging && e.isPopupTrigger()) {
            showPopup(e);
        }
        dragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        dragging = true;
    }

    /**
     * implemented by sub-classes to populate the menu with items that should be
     * shown.
     *
     * @param menu
     */
    public abstract void setMenuContents(JPopupMenu menu, java.awt.Point p);

    /**
     * Manages the display of the pop-up
     *
     * @param e
     */
    private void showPopup(MouseEvent e) {
        if (menu == null) {
            menu = new JPopupMenu();
        } else {
            menu.removeAll();
        }

        setMenuContents(menu, e.getPoint());
        if (e.getSource() instanceof Component) {
            menu.show((Component) e.getSource(), e.getX(), e.getY());
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */