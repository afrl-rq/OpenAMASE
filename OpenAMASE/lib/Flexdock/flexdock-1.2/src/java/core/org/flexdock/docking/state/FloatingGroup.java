// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on May 3, 2005
 */
package org.flexdock.docking.state;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.floating.frames.DockingFrame;
import org.flexdock.util.UUID;

/**
 * This class models a grouping of <code>Dockables</code> within a floating <code>DockingFrame</code>.
 * The <code>FloatingGroup</code> tracks the current screen bounds of the <code>DockingFrame</code> and
 * the ID for each <code>Dockable</code> within the group.  The <code>FloatingGroup</code>
 * may be persisted to external storage and recreated across JVM sessions.  This allows the
 * <code>DockingFrame</code> to be recreated and displayed with the previous screen bounds and all
 * of the <code>Dockables</code> contained within the group to be restored to their previous state
 * within the <code>DockingFrame</code>.
 * <br/>
 * In addition to providing persistent state across application sessions, the <code>FloatingGroup</code>
 * allows for a floating <code>Dockable</code> to be closed and then later restored to its original
 * floating state within the same JVM session.  The currently installed <code>FloatManager</code> may
 * use <code>FloatingGroups</code> to determine which visible <code>DockingFrame</code> into which to
 * restore a closed <code>Dockable</code>, or to create and display a new <code>DockingFrame</code>
 * to the same end on an as-needed basis.
 *
 * @author Christopher Butler
 */
@SuppressWarnings(value = { "serial" })
public class FloatingGroup implements Cloneable, Serializable {

    private String name;
    private Rectangle windowBounds;
    private transient DockingFrame frame;
    private HashSet dockables; // contains String dockableIds

    /**
     * Creates a new <code>FloatingGroup</code> with the specified <code>groupName</code>.  This
     * group may be looked up from the currently installed <code>FloatManager</code> using this
     * <code>groupName</code>.  <code>groupName</code> should be unique to this group.
     *
     * @param groupName the unique identifier for this <code>FloatingGroup</code>
     * @see FloatManager#getGroup(String)
     */
    public FloatingGroup(String groupName) {
        name = groupName==null? UUID.randomUUID().toString():groupName;
        dockables = new HashSet();
    }

    private FloatingGroup(String groupName, HashSet dockableSet) {
        name = groupName;
        dockables = dockableSet;
    }

    /**
     * Returns the cached screen bounds of the <code>DockingFrame</code> associated with this
     * <code>FloatingGroup</code>.  If no screen bounds have been previously cached, this method
     * returns <code>null</code>.  Otherwise, this method returns a clone of the cached
     * <code>Rectangle</code> so that its fields may not be directly modified.
     *
     * @return the cached screen bounds of the <code>DockingFrame</code> associated with this
     * <code>FloatingGroup</code>.
     * @see #setBounds(Rectangle)
     */
    public Rectangle getBounds() {
        return windowBounds==null? null: (Rectangle)windowBounds.clone();
    }

    /**
     * Sets the screen bounds representing the <code>DockingFrame</code> associated with this
     * <code>FloatingGroup</code>.  If <code>rect</code> is <code>null</code>, then the cached
     * screen bounds are set to <code>null</code>.  Otherwise, the field values are copied from the
     * specified <code>Rectangle</code> into the cached screen bounds rather than updating the
     * internal object reference.  This is done to prevent the cached screen bounds' fields from
     * subsequently being modified directly.
     *
     * @param rect the new screen bounds representing the <code>DockingFrame</code> associated with this
     * <code>FloatingGroup</code>.
     * @see #getBounds()
     */
    public void setBounds(Rectangle rect) {
        if(rect==null) {
            windowBounds = null;
        } else {
            if(windowBounds==null)
                windowBounds = (Rectangle)rect.clone();
            else
                windowBounds.setBounds(rect);
        }
    }

    /**
     * Returns the name of this <code>FloatingGroup</code>.  This value may be used as a key to lookup
     * this <code>FloatingGroup</code> from the currently installed <code>FloatManager</code> by
     * invoking its <code>getGroup(String groupName)</code> method.
     *
     * @return the name of this <code>FloatingGroup</code>.
     * @see #FloatingGroup(String)
     * @see FloatManager#getGroup(String)
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a reference to the <code>DockingFrame</code> associated with this <code>FloatingGroup</code>.
     * This method allows easy lookup to establish an association with a <code>Dockable</code> and
     * a <code>DockingFrame</code>.  When attempting to find an existing <code>DockingFrame</code> into which
     * to restore a hidden <code>Dockable</code>, the <code>Dockable's</code> <code>FloatingGroup</code>
     * may be looked up by the currently installed <code>FloatManager</code> by calling its
     * <code>getGroup(Dockable dockable)</code> method.  Once the group has been resolved, the actual
     * <code>DockingFrame</code> reference may be obtained by this method and the <code>Dockable</code>
     * may be restored to the screen.  Or, this method may return <code>null</code> and the
     * <code>DockingFrame</code> will have to be recreated before the <code>Dockable</code> can be restored.
     *
     * @return a reference to the <code>DockingFrame</code> associated with this <code>FloatingGroup</code>.
     * @see #setFrame(DockingFrame)
     * @see FloatManager#getGroup(Dockable)
     */
    public DockingFrame getFrame() {
        return frame;
    }

    /**
     * Sets a reference to the <code>DockingFrame</code> associated with this <code>FloatingGroup</code>.
     * This method allows help enable easy lookup to establish an association with a <code>Dockable</code> and
     * a <code>DockingFrame</code>.  When attempting to find an existing <code>DockingFrame</code> into which
     * to restore a hidden <code>Dockable</code>, the <code>Dockable's</code> <code>FloatingGroup</code>
     * may be looked up by the currently installed <code>FloatManager</code> by calling its
     * <code>getGroup(Dockable dockable)</code> method.  Once the group has been resolved, the actual
     * <code>DockingFrame</code> reference may be obtained by calling <code>getFrame()</code> and the
     * <code>Dockable</code> may be restored to the screen.  Or, <code>getFrame()</code>may return
     * <code>null</code> and the <code>DockingFrame</code> will have to be recreated before the
     * <code>Dockable</code> can be restored.  This method establishes the association between
     * <code>FloatingGroup</code> and <code>DockingFrame</code>.
     *
     * @param frame the <code>DockingFrame</code> to be associated with this <code>FloatingGroup</code>.
     * @see #getFrame()
     * @see FloatManager#getGroup(Dockable)
     */
    public void setFrame(DockingFrame frame) {
        this.frame = frame;
    }

    public void addDockable(String dockableId) {
        dockables.add(dockableId);
    }

    public Iterator getDockableIterator() {
        return this.dockables.iterator();
    }

    public void removeDockable(String dockableId) {
        dockables.remove(dockableId);
    }

    public int getDockableCount() {
        return dockables.size();
    }

    public void destroy() {
        dockables.clear();
        setFrame(null);
        setBounds(null);
    }

    public Object clone() {
        HashSet set = (HashSet)dockables.clone();
        FloatingGroup clone = new FloatingGroup(name, set);
        clone.frame = frame;
        clone.windowBounds = (Rectangle)windowBounds.clone();
        return clone;
    }

}
