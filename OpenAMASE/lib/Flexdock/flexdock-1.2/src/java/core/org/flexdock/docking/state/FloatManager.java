// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on May 26, 2005
 */
package org.flexdock.docking.state;

import java.awt.Component;
import java.awt.Rectangle;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.floating.frames.DockingFrame;

/**
 * This interface defines the API used for floating and grouping <code>Dockables</code>.  Classes
 * implementing this interface will be responsible for sending <code>Dockables</code> into
 * <code>DockingFrames</code> and managing the grouping of floating <code>Dockables</code>.
 * <br/>
 * Sending a <code>Dockable</code> into a floating <code>DockingFrame</code> is relatively straightforward
 * when supplied the <code>Dockable</code> and a dialog owner.  However, state must be maintained
 * for each <code>FloatingGroup</code> to allow the system to track which <code>Dockables</code>
 * share the same floating dialog.  If a floating <code>Dockable</code> is closed and subsequently
 * restored to its previous floating state, the <code>FloatManager</code> must be able to determine
 * whether an existing dialog is already present or a new dialog must be created into which the
 * <code>Dockable</code> may be restored.  <code>FloatingGroups</code> are used to track which
 * dialogs contain which <code>Dockables</code>.  <code>FloatManager</code> implementations must
 * manage the addition to and removal of <code>Dockables</code> from appropriate <code>FloatingGroups</code>
 * and, in turn, use these <code>FloatingGroups</code> to resolve or create the necessary
 * <code>DockingFrames</code> during float-operations.
 *
 * @author Christopher Butler
 */
public interface FloatManager {

    FloatManager DEFAULT_STUB = new Stub();

    FloatingGroup getGroup(String groupName);

    FloatingGroup getGroup(Dockable dockable);

    void addToGroup(Dockable dockable, String groupId);

    void removeFromGroup(Dockable dockable);

    DockingFrame floatDockable(Dockable dockable, Component frameOwner);

    DockingFrame floatDockable(Dockable dockable, Component frameOwner, Rectangle screenBounds);

    public static class Stub implements FloatManager {

        public void addToGroup(Dockable dockable, String groupId) {
        }

        public DockingFrame floatDockable(Dockable dockable, Component frameOwner, Rectangle screenBounds) {
            return null;
        }

        public DockingFrame floatDockable(Dockable dockable, Component frameOwner) {
            return null;
        }

        public FloatingGroup getGroup(Dockable dockable) {
            return null;
        }

        public FloatingGroup getGroup(String groupName) {
            return null;
        }

        public void removeFromGroup(Dockable dockable) {
        }

    }

}