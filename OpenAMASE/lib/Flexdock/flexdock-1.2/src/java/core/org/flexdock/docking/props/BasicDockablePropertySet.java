// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 16, 2005
 */
package org.flexdock.docking.props;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;

import javax.swing.Icon;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.util.TypedHashtable;
import org.flexdock.util.Utilities;

/**
 * @author Christopher Butler
 */
@SuppressWarnings(value = { "serial" })
public class BasicDockablePropertySet extends TypedHashtable implements DockablePropertySet, DockingConstants {
    private String dockingId;
    private PropertyChangeSupport changeSupport;

    public static String getRegionInsetKey(String region) {
        if(NORTH_REGION.equals(region))
            return REGION_SIZE_NORTH;
        if(SOUTH_REGION.equals(region))
            return REGION_SIZE_SOUTH;
        if(EAST_REGION.equals(region))
            return REGION_SIZE_EAST;
        if(WEST_REGION.equals(region))
            return REGION_SIZE_WEST;
        return null;
    }

    public static String getSiblingSizeKey(String region) {
        if(NORTH_REGION.equals(region))
            return SIBLING_SIZE_NORTH;
        if(SOUTH_REGION.equals(region))
            return SIBLING_SIZE_SOUTH;
        if(EAST_REGION.equals(region))
            return SIBLING_SIZE_EAST;
        if(WEST_REGION.equals(region))
            return SIBLING_SIZE_WEST;
        return null;
    }

    public static String getTerritoryBlockedKey(String region) {
        if(NORTH_REGION.equals(region))
            return TERRITORY_BLOCKED_NORTH;
        if(SOUTH_REGION.equals(region))
            return TERRITORY_BLOCKED_SOUTH;
        if(EAST_REGION.equals(region))
            return TERRITORY_BLOCKED_EAST;
        if(WEST_REGION.equals(region))
            return TERRITORY_BLOCKED_WEST;
        if(CENTER_REGION.equals(region))
            return TERRITORY_BLOCKED_CENTER;
        return null;
    }

    public BasicDockablePropertySet(Dockable dockable) {
        super();
        init(dockable);
    }

    public BasicDockablePropertySet(int initialCapacity, Dockable dockable) {
        super(initialCapacity);
        init(dockable);
    }

    public BasicDockablePropertySet(int initialCapacity, float loadFactor, Dockable dockable) {
        super(initialCapacity, loadFactor);
        init(dockable);
    }

    public BasicDockablePropertySet(Map t, Dockable dockable) {
        super(t);
        init(dockable);
    }

    private void init(Dockable dockable) {
        this.dockingId = dockable==null? null: dockable.getPersistentId();
        Object changeSrc = dockable==null? (Object)this: dockable;
        changeSupport = new PropertyChangeSupport(changeSrc);
    }















    public Icon getDockbarIcon() {
        return (Icon)get(DOCKBAR_ICON);
    }

    public Icon getTabIcon() {
        return (Icon)get(TAB_ICON);
    }

    public String getDockableDesc() {
        return (String)get(DESCRIPTION);
    }

    public Boolean isDockingEnabled() {
        return getBoolean(DOCKING_ENABLED);
    }

    public Boolean isActive() {
        return getBoolean(ACTIVE);
    }

    public Boolean isMouseMotionListenersBlockedWhileDragging() {
        return getBoolean(MOUSE_MOTION_DRAG_BLOCK);
    }


    public Float getRegionInset(String region) {
        String key = getRegionInsetKey(region);
        return key==null? null: (Float)get(key);
    }

    public Float getSiblingSize(String region) {
        String key = getSiblingSizeKey(region);
        return key==null? null: (Float)get(key);
    }

    public Boolean isTerritoryBlocked(String region) {
        String key = getTerritoryBlockedKey(region);
        return key==null? null: (Boolean)get(key);
    }

    public Float getDragThreshold() {
        return getFloat(DRAG_THRESHOLD);
    }

    public Float getPreviewSize() {
        return getFloat(PREVIEW_SIZE);
    }












    public void setDockbarIcon(Icon icon) {
        Icon oldValue = getDockbarIcon();
        put(DOCKBAR_ICON, icon);
        firePropertyChange(DOCKBAR_ICON, oldValue, icon);
    }

    public void setTabIcon(Icon icon) {
        Icon oldValue = getTabIcon();
        put(TAB_ICON, icon);
        firePropertyChange(TAB_ICON, oldValue, icon);
    }

    public void setDockableDesc(String dockableDesc) {
        String oldValue = getDockableDesc();
        put(DESCRIPTION, dockableDesc);
        firePropertyChange(DESCRIPTION, oldValue, dockableDesc);
    }

    public void setDockingEnabled(boolean enabled) {
        put(DOCKING_ENABLED, enabled);
    }

    public void setActive(boolean active) {
        Boolean oldValue = isActive();
        if(oldValue==null)
            oldValue = Boolean.FALSE;

        put(ACTIVE, active);
        firePropertyChange(ACTIVE, oldValue.booleanValue(), active);
    }

    public void setMouseMotionListenersBlockedWhileDragging(boolean blocked) {
        put(MOUSE_MOTION_DRAG_BLOCK, blocked);
    }

    public void setRegionInset(String region, float inset) {
        String key = getRegionInsetKey(region);
        if(key!=null) {
            Float f = new Float(inset);
            put(key, f);
        }
    }

    public void setSiblingSize(String region, float size) {
        String key = getSiblingSizeKey(region);
        if(key!=null) {
            Float f = new Float(size);
            put(key, f);
        }
    }

    public void setTerritoryBlocked(String region, boolean blocked) {
        String key = getTerritoryBlockedKey(region);
        if(key!=null) {
            Boolean bool = blocked? Boolean.TRUE: Boolean.FALSE;
            put(key, bool);
        }
    }


    public void setDragTheshold(float threshold) {
        threshold = Math.max(threshold, 0);
        put(DRAG_THRESHOLD, threshold);
    }

    public void setPreviewSize(float previewSize) {
        previewSize = Math.max(previewSize, 0f);
        previewSize = Math.min(previewSize, 1f);
        put(PREVIEW_SIZE, previewSize);
    }

    /**
     * @return Returns the dockingId.
     */
    public String getDockingId() {
        return dockingId;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String property, Object oldValue, Object newValue) {
        if(Utilities.isChanged(oldValue, newValue))
            changeSupport.firePropertyChange(property, oldValue, newValue);
    }

    protected void firePropertyChange(String property, int oldValue, int newValue) {
        if(oldValue!=newValue)
            changeSupport.firePropertyChange(property, oldValue, newValue);
    }

    protected void firePropertyChange(String property, boolean oldValue, boolean newValue) {
        if(oldValue!=newValue)
            changeSupport.firePropertyChange(property, oldValue, newValue);
    }
}
