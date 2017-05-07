// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on 2005-03-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.flexdock.perspective;



import java.io.Serializable;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.state.DockingState;
import org.flexdock.docking.state.LayoutNode;
import org.flexdock.event.EventManager;
import org.flexdock.perspective.event.LayoutListener;
import org.flexdock.perspective.event.PerspectiveEvent;

/**
 * @author Mateusz Szczap
 */
public class Perspective implements Cloneable, Serializable {
    private String m_persistentId;
    private String m_perspectiveName;
    private Layout m_layout;
    private LayoutSequence m_initalSequence;


    public Perspective(String persistentId, String perspectiveName) {
        this(persistentId, perspectiveName, false);
    }

    /**
     * @param persistentId
     * @param perspectiveName
     * @param defaultMode
     * @throws IllegalArgumentException
     *             if {@code persistentId} or {@code perspectiveName} is
     *             {@code null}.
     */
    public Perspective(String persistentId, String perspectiveName, boolean defaultMode) {
        if (persistentId == null) throw new IllegalArgumentException("persistentId cannot be null");
        if (perspectiveName == null) throw new IllegalArgumentException("perspectiveName cannot be null");
        m_persistentId = persistentId;
        m_perspectiveName = perspectiveName;
        m_layout = new Layout();
    }

    public String getName() {
        return m_perspectiveName;
    }

    public String getPersistentId() {
        return m_persistentId;
    }

    public void addDockable(String dockableId) {
        getLayout().add(dockableId);
    }

    public boolean removeDockable(String dockableId) {
        return (getLayout().remove(dockableId) != null);
    }

    public Dockable getDockable(String dockableId) {
        return (Dockable) getLayout().getDockable(dockableId);
    }

    public void addLayoutListener(LayoutListener listener) {
        getLayout().addListener(listener);
    }

    public void removeLayoutListener(LayoutListener listener) {
        getLayout().removeListener(listener);
    }

    public Dockable[] getDockables() {
        return getLayout().getDockables();
    }

    public DockingState getDockingState(String dockable) {
        return getLayout().getDockingState(dockable, false);
    }

    public DockingState getDockingState(Dockable dockable) {
        return getLayout().getDockingState(dockable, false);
    }

    public DockingState getDockingState(String dockable, boolean load) {
        return getLayout().getDockingState(dockable, load);
    }

    public DockingState getDockingState(Dockable dockable, boolean load) {
        return getLayout().getDockingState(dockable, load);
    }

    public LayoutSequence getInitialSequence() {
        return getInitialSequence(false);
    }

    public LayoutSequence getInitialSequence(boolean create) {
        if(m_initalSequence==null && create)
            m_initalSequence = new LayoutSequence();
        return m_initalSequence;
    }

    public void setInitialSequence(LayoutSequence sequence) {
        m_initalSequence = sequence;
    }

    public Layout getLayout() {
        return m_layout;
    }

    public void setLayout(Layout layout) {
        m_layout = layout;
    }

    public void reset(DockingPort port) {
        if(m_initalSequence!=null) {
            m_initalSequence.apply(port);

            Layout layout = getLayout();
            if(layout!=null) {
                layout.update(m_initalSequence);
                EventManager.getInstance().dispatchEvent(new PerspectiveEvent(this, null, PerspectiveEvent.RESET));
            }
        }
    }

    public void load(DockingPort port) {
        Layout layout = getLayout();
        if(layout.isInitialized()) {
            layout.apply(port);
            EventManager.getInstance().dispatchEvent(new PerspectiveEvent(this, null, PerspectiveEvent.RESET));
        } else {
            reset(port);
        }
    }

    public void unload() {
        Dockable[] dockables = getLayout().getDockables();
        for(int i=0; i<dockables.length; i++) {
            DockingManager.close(dockables[i]);
        }
    }

    public void cacheLayoutState(DockingPort port) {
        if(port!=null) {
            Layout layout = getLayout();
            LayoutNode node = port.exportLayout();
            layout.setRestorationLayout(node);
        }
    }

    public Object clone() {
        Perspective clone = new Perspective(m_persistentId, m_perspectiveName);
        clone.m_layout = (Layout)m_layout.clone();
        clone.m_initalSequence = m_initalSequence==null? null: (LayoutSequence)m_initalSequence.clone();
        return clone;
    }

}
