// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Copyright (c) 2004 Andreas Ernst
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.flexdock.docking.floating.frames;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.state.FloatingGroup;
import org.flexdock.util.RootWindow;

/**
 * @author Andreas Ernst
 * @author Christopher Butler
 */
@SuppressWarnings(value = { "serial" })
public class DockingFrame extends JDialog implements DockingConstants {
    private static final BoundsMonitor BOUNDS_MONITOR = new BoundsMonitor();

    private FloatingDockingPort dockingPort;

    private String groupName;

    public static DockingFrame create(Component c, String groupName) {
        RootWindow rootWin = RootWindow.getRootContainer(c);
        Component window = rootWin.getRootContainer();
        if (window instanceof DockingFrame) {
            window = ((DockingFrame) window).getOwner();
        }

        //Applets are actually contained in a frame
        if (window instanceof Applet)
            window = SwingUtilities.windowForComponent(window);

        if (window instanceof Frame)
            return new DockingFrame((Frame) window, groupName);
        if (window instanceof Dialog)
            return new DockingFrame((Dialog) window, groupName);

        return null;
    }

    // constructor
    public DockingFrame(Frame owner, String groupName) {
        super(owner);
        initialize(groupName);
    }

    public DockingFrame(Dialog owner, String groupName) {
        super(owner);
        initialize(groupName);
    }

    // private

    private void initialize(String groupName) {
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        // TODO I am not sure null should be passed here,
        // maybe we should use our IDPersistentIdProvider
        dockingPort = new FloatingDockingPort(this, null);
        setContentPane(dockingPort);
        this.groupName = groupName;
        addComponentListener(BOUNDS_MONITOR);
    }

    // override

    protected JRootPane createRootPane() {
        return new RootPane(this);
    }

    public DockingPort getDockingPort() {
        return dockingPort;
    }

    public void addDockable(Dockable dockable) {
        if (dockable == null)
            return;

        dockingPort.dock(dockable, CENTER_REGION);
    }

    public void destroy() {
        setVisible(false);
        dockingPort = null;
        FloatingGroup group = getGroup();
        if (group != null)
            group.setFrame(null);
        dispose();
    }

    public String getGroupName() {
        return groupName;
    }

    public FloatingGroup getGroup() {
        return DockingManager.getFloatManager().getGroup(getGroupName());
    }

    private static class BoundsMonitor implements ComponentListener {

        public void componentHidden(ComponentEvent e) {
            // noop
        }

        public void componentMoved(ComponentEvent e) {
            updateBounds(e);
        }

        public void componentResized(ComponentEvent e) {
            updateBounds(e);
        }

        public void componentShown(ComponentEvent e) {
            updateBounds(e);
        }

        private void updateBounds(ComponentEvent evt) {
            Component c = (Component) evt.getComponent();
            if (!(c instanceof DockingFrame))
                return;

            DockingFrame frame = (DockingFrame) c;
            FloatingGroup group = frame.getGroup();
            if (group != null)
                group.setBounds(frame.getBounds());
        }
    }
}
