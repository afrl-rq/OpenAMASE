// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.docking.state;

import java.awt.Point;
import java.io.Serializable;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;

/**
 *
 * @author Mateusz Szczap
 */
@SuppressWarnings(value = { "serial" })
public class DockingState implements Cloneable, Serializable, DockingConstants {

    private String m_dockableId;

    private String m_relativeParentId;

    private String m_region = UNKNOWN_REGION;

    private float m_splitRatio = UNINITIALIZED_RATIO;

    private String m_floatingGroup;

    //if the view is minimized we store the dockbar edge to which it is minimized
    private int m_minimizedConstraint = MinimizationManager.UNSPECIFIED_LAYOUT_CONSTRAINT;

    private DockingPath m_dockingPath;

    private int centerX = DockingConstants.UNINITIALIZED;

    private int centerY = DockingConstants.UNINITIALIZED;

    public DockingState(String dockableId) {
        this.m_dockableId = dockableId;
    }

    public Dockable getDockable() {
        return DockingManager.getDockable(m_dockableId);
    }

    public String getDockableId() {
        return m_dockableId;
    }

    public float getSplitRatio() {
        return m_splitRatio;
    }

    public void setSplitRatio(float ratio) {
        m_splitRatio = ratio;
    }

    public String getRegion() {
        return m_region;
    }

    public void setRegion(String region) {
        m_region = region;
    }

    public int getMinimizedConstraint() {
        return m_minimizedConstraint;
    }

    public String getFloatingGroup() {
        return m_floatingGroup;
    }

    public boolean isFloating() {
        return m_floatingGroup!=null;
    }

    public boolean isMinimized() {
        return m_minimizedConstraint!=MinimizationManager.UNSPECIFIED_LAYOUT_CONSTRAINT;
    }

    public boolean hasDockingPath() {
        return m_dockingPath!=null;
    }

    public DockingPath getPath() {
        return m_dockingPath;
    }

    public void setPath(DockingPath path) {
        m_dockingPath = path;
    }

    public void setMinimizedConstraint(int constraint) {
        m_minimizedConstraint = constraint;
        if(constraint!=MinimizationManager.UNSPECIFIED_LAYOUT_CONSTRAINT) {
            m_floatingGroup = null;
        }
    }

    public void setFloatingGroup(String group) {
        m_floatingGroup = group;
        if(group!=null) {
            m_minimizedConstraint = MinimizationManager.UNSPECIFIED_LAYOUT_CONSTRAINT;
        }
    }

    public Dockable getRelativeParent() {
        return DockingManager.getDockable(m_relativeParentId);
    }

    public String getRelativeParentId() {
        return m_relativeParentId;
    }

    public void setRelativeParent(Dockable parent) {
        String parentId = parent==null? null: parent.getPersistentId();
        setRelativeParentId(parentId);
    }

    public void setRelativeParentId(String relativeParentId) {
        m_relativeParentId = relativeParentId;
    }

    public String toString() {
        return "DockingState[id=" + m_dockableId +
               "; center=[" + centerX + "%," + centerY + "%]" +
               "; parent=" + m_relativeParentId +
               "; region=" + m_region + "; ratio=" + m_splitRatio +
               "; float=" + m_floatingGroup + "; minimization=" + m_minimizedConstraint + "; ]";
    }

    public int getCenterX() {
        return centerX;
    }
    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }
    public int getCenterY() {
        return centerY;
    }
    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public void setCenter(Point p) {
        centerX = p==null? 0: p.x;
        centerY = p==null? 0: p.y;
    }

    public Point getCenterPoint() {
        if (this.centerX == DockingConstants.UNINITIALIZED || this.centerY == DockingConstants.UNINITIALIZED) {
            return null;
        }
        return new Point(this.centerX, this.centerY);
    }

    public boolean hasCenterPoint() {
        return (centerX != DockingConstants.UNINITIALIZED && centerY != DockingConstants.UNINITIALIZED);
    }

    public Object clone() {
        DockingState dockingStateClone = new DockingState(m_dockableId);

        dockingStateClone.m_relativeParentId = m_relativeParentId;
        dockingStateClone.m_region = m_region;
        dockingStateClone.m_splitRatio = m_splitRatio;
        dockingStateClone.m_floatingGroup = m_floatingGroup;
        dockingStateClone.m_minimizedConstraint = m_minimizedConstraint;
        dockingStateClone.m_dockingPath = m_dockingPath==null? null: (DockingPath)m_dockingPath.clone();
        dockingStateClone.centerX = centerX;
        dockingStateClone.centerY = centerY;

        return dockingStateClone;
    }

}
