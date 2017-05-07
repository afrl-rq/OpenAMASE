// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Jun 24, 2005
 */
package org.flexdock.docking.adapter;

/**
 * This POJO contains values for an adapted components dockable requirements.
 *
 * @author Christopher Butler
 */
public class AdapterMapping {
    private String className;

    private String dragSource;

    private String dragSourceList;

    private String frameDragSource;

    private String frameDragSourceList;

    private String persistentId;

    private String tabText;

    private String dockbarIcon;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDockbarIcon() {
        return dockbarIcon;
    }

    public void setDockbarIcon(String dockbarIcon) {
        this.dockbarIcon = dockbarIcon;
    }

    public String getDragSource() {
        return dragSource;
    }

    public void setDragSource(String dragSource) {
        this.dragSource = dragSource;
    }

    public String getDragSourceList() {
        return dragSourceList;
    }

    public void setDragSourceList(String dragSourceList) {
        this.dragSourceList = dragSourceList;
    }

    public String getFrameDragSource() {
        return frameDragSource;
    }

    public void setFrameDragSource(String frameDragSource) {
        this.frameDragSource = frameDragSource;
    }

    public String getFrameDragSourceList() {
        return frameDragSourceList;
    }

    public void setFrameDragSourceList(String frameDragSourceList) {
        this.frameDragSourceList = frameDragSourceList;
    }

    public String getPersistentId() {
        return persistentId;
    }

    public void setPersistentId(String persistentId) {
        this.persistentId = persistentId;
    }

    public String getTabText() {
        return tabText;
    }

    public void setTabText(String tabText) {
        this.tabText = tabText;
    }
}
