// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPopupMenu;

/**
 *
 * @author AFRL/RQQD
 */
public class MapPopupMenu {

    private List<MapPopupListener> menuList;

    /**
     * Creates a new instance of MapisPopupMenu
     */
    public MapPopupMenu() {
        menuList = new ArrayList<MapPopupListener>();
    }

    public void showMenu(MouseEvent e, double lat, double lon) {

        JPopupMenu menu = new JPopupMenu();
        for (MapPopupListener l : menuList) {
            int numItems = menu.getComponentCount();
            l.addPopupMenuItems(menu, e, lat, lon);
            if (menu.getComponentCount() > numItems) {
                menu.addSeparator();
            }
        }
        menu.show(e.getComponent(), e.getX(), e.getY());
    }

    public void addListener(MapPopupListener l) {
        if (!menuList.contains(l)) {
            menuList.add(l);
        }
    }

    public void removeListener(MapPopupListener l) {
            menuList.remove(l);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */