// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 8, 2005
 */
package org.flexdock.docking.event;

import java.awt.AWTEvent;
import java.awt.Component;
import java.util.Map;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.event.Event;

/**
 * @author Kevin Duffey
 * @author Christopher Butler
 */
@SuppressWarnings(value = { "serial" })
public class DockingEvent extends Event {
    public static final int DRAG_STARTED = 0;
    public static final int DROP_STARTED = 1;
    public static final int DOCKING_COMPLETE = 2;
    public static final int DOCKING_CANCELED = 3;
    public static final int UNDOCKING_COMPLETE = 4;
    public static final int UNDOCKING_STARTED = 5;

    private DockingPort oldPort;
    private DockingPort newPort;
    private boolean consumed;
    private AWTEvent trigger;
    private String region;
    private boolean overWindow;
    private Map dragContext;

    /**
     * Constructor to create a DockingEvent object with the provided Dockable,
     * the originating docking part, the destination docking port and whether
     * the dock is completed or canceled.
     */
    public DockingEvent(Dockable source, DockingPort oldPort, DockingPort newPort, int eventType, Map context) {
        this(source, oldPort, newPort, eventType, null, context);
    }

    /**
     * Constructor to create a DockingEvent object with the provided Dockable,
     * the originating docking part, the destination docking port and whether
     * the dock is completed or canceled.
     */
    public DockingEvent(Dockable source, DockingPort oldPort, DockingPort newPort, int eventType, AWTEvent trigger, Map context) {
        super(source, eventType);
        this.oldPort = oldPort;
        this.newPort = newPort;
        this.trigger = trigger;
        this.region = UNKNOWN_REGION;
        dragContext = context;
        setOverWindow(true);
    }

    /**
     * Returns the old docking port which the source <code>Dockable</code> was
     * originally docked to.
     *
     * @return DockingPort the old docking port
     */
    public DockingPort getOldDockingPort() {
        return oldPort;
    }

    /**
     * Returns the new docking port the source <code>Dockable</code> has been
     * docked to.
     *
     * @return DockingPort the new docking port
     */
    public DockingPort getNewDockingPort() {
        return newPort;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void consume() {
        this.consumed = true;
    }

    public AWTEvent getTrigger() {
        return trigger;
    }

    public void setTrigger(AWTEvent trigger) {
        this.trigger = trigger;
    }

    public Object getTriggerSource() {
        return trigger == null ? null : trigger.getSource();
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        if (!DockingManager.isValidDockingRegion(region))
            region = UNKNOWN_REGION;
        this.region = region;
    }

    public boolean isOverWindow() {
        return overWindow;
    }

    public void setOverWindow(boolean overWindow) {
        this.overWindow = overWindow;
    }

    public Dockable getDockable() {
        return (Dockable)getSource();
    }

    public Component getComponent() {
        return getDockable().getComponent();
    }

    public Map getDragContext() {
        return dragContext;
    }
}
