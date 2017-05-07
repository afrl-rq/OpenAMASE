// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.swing;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * Provides more robust popup menu support. Using this feature allows multiple
 * {@link PopupMenuAdapter}s to be registered to a popup menu.
 *
 * @author AFRL/RQQD
 */
public class PopupSupport implements PopupMenuListener {

    JPopupMenu menu;
    List<PopupMenuAdapter> listenerList = new ArrayList<>();

    public PopupSupport() {
    }

    public PopupSupport(JComponent comp) {
        setInvoker(comp);
    }
    
    public void setInvoker(JComponent comp) {
        if (comp.getComponentPopupMenu() != null) {
            menu = comp.getComponentPopupMenu();
        } else {
            menu = new JPopupMenu();
            comp.setComponentPopupMenu(menu);
        }

        menu.addPopupMenuListener(this);
    }

    public void addPopupMenuAdapter(PopupMenuAdapter l) {
        if (!listenerList.contains(l)) {
            listenerList.add(l);
        }
    }

    public boolean removePopupMenuAdapter(PopupMenuAdapter l) {
        return listenerList.remove(l);
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        if (menu.getInvoker() != null) {
            Point p = menu.getInvoker().getMousePosition();
            for (PopupMenuAdapter l : listenerList) {
                l.setMenuContents(menu, p);
            }
        }
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

        menu.removeAll();
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
        menu.removeAll();
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */