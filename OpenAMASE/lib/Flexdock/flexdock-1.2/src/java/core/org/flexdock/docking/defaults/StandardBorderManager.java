// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Copyright (c) 2004 Christopher M Butler
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
package org.flexdock.docking.defaults;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import org.flexdock.docking.DockingPort;

/**
 * This class provides a standard implementation of the {@code BorderManager}
 * interface. It is designed to avoid the negative visual effects caused by
 * nesting docked components that have individual borders. It accomplishes this
 * by establishing and maintaining a single border for all docked components
 *
 * This class encapsulates a {@code javax.swing.border.Border} instance, or a
 * {@code null} border reference, for application to a
 * {@code DefaultDockingPort} and its child components. If the
 * {@code DefaultDockingPort} has no child component, then
 * {@code managePortNullChild()} will apply the encapsulated border to the
 * {@code DefaultDockingPort} itself, rendering the visible outline of an empty
 * {@code DockingPort}. If the {@code DefaultDockingPort} has a generic
 * {@code Component} as its child, excluding a {@code JTabbedPane} or
 * {@code JSplitPane}, then the border for that component is set to
 * {@code null} and the encapsulated border is applied to the
 * {@code DockingPort} via {@code managePortSimpleChild()}. If the
 * {@code DefaultDockingPort} has a {@code JTabbedPane} as its child, then a
 * {@code null} border is set for the {@code JTabbedPane} and all of its child
 * components, and the encapsulated border is applied to the {@code DockingPort}.
 * This is accomplished by calling {@code managePortTabbedChild()}. Finally,
 * {@code managePortSplitChild()} will manage the border for a
 * {@code DefaultDockingPort} whose docked component is a {@code JSplitPane}.
 * This method removes all borders from the {@code DefaultDockingPort} and the
 * split pane divider and applies the encapsulated border to both left and right
 * child components of the {@code JSplitPane}.
 *
 * @author Christopher Butler
 */
public class StandardBorderManager implements BorderManager {
    private Border assignedBorder;

    /**
     * Creates a new {@code StandardBorderManager} with a {@code null} assigned
     * border.
     */
    public StandardBorderManager() {
    }

    /**
     * Creates a new {@code StandardBorderManager} with the specified assigned
     * border.
     *
     * @param border
     *            the currently assigned border.
     */
    public StandardBorderManager(Border border) {
        setBorder(border);
    }

    /**
     * Returns the currently assigned border.
     *
     * @return the currently assigned border.
     */
    public Border getBorder() {
        return assignedBorder;
    }

    /**
     * Sets the assigned border. Null values are acceptable.
     *
     * @param border
     *            the assigned border.
     */
    public void setBorder(Border border) {
        assignedBorder = border;
    }

    /**
     * Set the border on the supplied {@code DockingPort} to the currently
     * assigned border.
     *
     * @see BorderManager#managePortNullChild(DockingPort)
     */
    public void managePortNullChild(DockingPort port) {
        setBorder(port, assignedBorder);
    }

    /**
     * Removes any border from the {@code DockingPort's} docked component and
     * set the border on the {@code DockingPort} itself to the currently
     * assigned border.
     *
     * @see BorderManager#managePortSimpleChild(DockingPort)
     */
    public void managePortSimpleChild(DockingPort port) {
        if (port != null) {
            setBorder(port.getDockedComponent(), null);
            setBorder(port, assignedBorder);
        }
    }

    /**
     * Removes any border from the {@code DockingPort} itself and places the
     * currently assigned border on the two child components of the
     * {@code DockingPort's</code JSplitPane} child.
     *
     * @see BorderManager#managePortSplitChild(DockingPort)
     */
    public void managePortSplitChild(DockingPort port) {
        if (port == null || !(port.getDockedComponent() instanceof JSplitPane))
            return;

        setBorder(port, null);

        // clear the border from the split pane
        JSplitPane split = (JSplitPane) port.getDockedComponent();
        if (split.getUI() instanceof BasicSplitPaneUI) {
            // grab the divider from the UI and remove the border from it
            BasicSplitPaneDivider divider = ((BasicSplitPaneUI) split.getUI())
                                            .getDivider();
            if (divider != null && divider.getBorder() != null)
                divider.setBorder(null);
        }
        setBorder(split, null);

        // set the borders on each of the child components
        setSubComponentBorder(split.getLeftComponent(), assignedBorder);
        setSubComponentBorder(split.getRightComponent(), assignedBorder);
    }

    private void setSubComponentBorder(Component comp, Border border) {
        if (comp instanceof DefaultDockingPort)
            ((DefaultDockingPort) comp).evaluateDockingBorderStatus();
        else
            setBorder(comp, border);
    }

    /**
     * Removes any border from the {@code DockingPort's} docked
     * {@code JTabbedPane} component and sets the border on the
     * {@code DockingPort} itself to the currently assigned border.
     *
     * @see BorderManager#managePortTabbedChild(DockingPort)
     */
    public void managePortTabbedChild(DockingPort port) {
        managePortSimpleChild(port);
        if (port == null || !(port.getDockedComponent() instanceof JTabbedPane))
            return;

        // we need to use a special UI to remove the outline around a
        // JTabbedPane.
        // this UI will only allow the outline on the side of the JTabbedPane
        // that
        // contains the tabs.
        JTabbedPane tabs = (JTabbedPane) port.getDockedComponent();
        // if(!(tabs.getUI() instanceof SimpleTabbedPaneUI))
        // tabs.setUI(new SimpleTabbedPaneUI());

        // remove any borders from the tabPane children
        int tc = tabs.getTabCount();
        Component cmp = null;
        for (int i = 0; i < tc; i++) {
            cmp = tabs.getComponentAt(i);
            if (cmp instanceof JComponent)
                ((JComponent) cmp).setBorder(null);
        }
    }

    private void setBorder(Component cmp, Border border) {
        if (cmp instanceof JComponent)
            ((JComponent) cmp).setBorder(border);
    }

    private void setBorder(DockingPort port, Border border) {
        if (port instanceof JComponent)
            ((JComponent) port).setBorder(border);
    }

    private static class SimpleTabbedPaneUI extends BasicTabbedPaneUI {
        protected void paintContentBorderBottomEdge(Graphics g,
                int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
            if (tabPlacement == BOTTOM)
                super.paintContentBorderBottomEdge(g, tabPlacement,
                                                   selectedIndex, x, y, w, h);
        }

        protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement,
                int selectedIndex, int x, int y, int w, int h) {
            if (tabPlacement == LEFT)
                super.paintContentBorderLeftEdge(g, tabPlacement,
                                                 selectedIndex, x, y, w, h);
        }

        protected void paintContentBorderRightEdge(Graphics g,
                int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
            if (tabPlacement == RIGHT)
                super.paintContentBorderRightEdge(g, tabPlacement,
                                                  selectedIndex, x, y, w, h);
        }

        protected void paintContentBorderTopEdge(Graphics g, int tabPlacement,
                int selectedIndex, int x, int y, int w, int h) {
            if (tabPlacement == TOP)
                super.paintContentBorderTopEdge(g, tabPlacement, selectedIndex,
                                                x, y, w, h);
        }
    }
}
