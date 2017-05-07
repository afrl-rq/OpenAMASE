// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Aug 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.flexdock.dockbar.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;

import org.flexdock.dockbar.Dockbar;
import org.flexdock.dockbar.DockbarLabel;
import org.flexdock.dockbar.DockbarManager;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.props.DockablePropertySet;
import org.flexdock.docking.props.PropertyChangeListenerFactory;
import org.flexdock.util.DockingUtility;

/**
 * @author Christopher Butler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DockablePropertyChangeHandler implements PropertyChangeListener {
    public static final DockablePropertyChangeHandler DEFAULT_INSTANCE = new DockablePropertyChangeHandler();

    public void propertyChange(PropertyChangeEvent evt) {
        if(!(evt.getSource() instanceof Dockable))
            return;

        Dockable dockable = (Dockable) evt.getSource();
        if(!DockingUtility.isMinimized(dockable))
            return;

        String pName = evt.getPropertyName();
        DockbarLabel label = getDockbarLabel(dockable);
        if(label==null)
            return;

        if (DockablePropertySet.TAB_ICON.equals(pName) || DockablePropertySet.DOCKBAR_ICON.equals(pName)) {
            Icon icon = dockable.getDockingProperties().getDockbarIcon();
            if(icon==null)
                icon = dockable.getDockingProperties().getTabIcon();
            label.setIcon(icon);
        } else if(DockablePropertySet.DESCRIPTION.equals(pName)) {
            label.setText(dockable.getDockingProperties().getDockableDesc());
        }
    }

    private DockbarLabel getDockbarLabel(Dockable dockable) {
        DockbarManager mgr = DockbarManager.getCurrent();
        Dockbar dockbar = mgr==null? null: mgr.getDockbar(dockable);
        return dockbar==null? null: dockbar.getLabel(dockable);
    }

    public static class Factory extends PropertyChangeListenerFactory {
        public PropertyChangeListener getListener() {
            return DEFAULT_INSTANCE;
        }
    }
}
