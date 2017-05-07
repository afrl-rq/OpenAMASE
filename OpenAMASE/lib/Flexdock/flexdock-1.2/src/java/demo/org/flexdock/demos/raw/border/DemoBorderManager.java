// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.demos.raw.border;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.flexdock.docking.DockingPort;
import org.flexdock.docking.defaults.BorderManager;
import org.flexdock.docking.defaults.DefaultDockingPort;

public class DemoBorderManager implements BorderManager {
    private Border dummyBorder;

    DemoBorderManager() {
        dummyBorder = new TitledBorder("");
    }

    public void managePortNullChild(DockingPort port) {
        setBorder(port, dummyBorder);
    }

    public void managePortSimpleChild(DockingPort port) {
        if (port == null || port.getDockedComponent() == null)
            return;

        Component docked = port.getDockedComponent();
        setBorder(docked, null);
        setBorder(port, getDesiredBorder(docked));
    }

    public void managePortSplitChild(DockingPort port) {
        if (port == null || !(port.getDockedComponent() instanceof JSplitPane))
            return;

        // clear the borders on the split pane and divider
        JSplitPane split = (JSplitPane)port.getDockedComponent();
        clearSplitPaneBorder(split);

        // determine the borders for each child component
        Component left = split.getLeftComponent();
        Component right = split.getRightComponent();
        Border leftBorder = getDesiredBorder(left);
        Border rightBorder = getDesiredBorder(right);

        // set the borders for all interested parties
        setBorder(port, null);
        setBorder(left, null);
        setBorder(right, null);
        setBorder(getDocked(left), leftBorder);
        setBorder(getDocked(right), rightBorder);
    }

    public void managePortTabbedChild(DockingPort port) {
        setBorder(port, null);
        if (port == null || !(port.getDockedComponent() instanceof JTabbedPane))
            return;

        JTabbedPane tabs = (JTabbedPane) port.getDockedComponent();
        int tc = tabs.getTabCount();
        for (int i = 0; i < tc; i++)
            setBorder(tabs.getComponentAt(i), new TitledBorder(tabs.getTitleAt(i)));
    }



    private void clearSplitPaneBorder(JSplitPane split) {
        split.setBorder(null);
        if (split.getUI() instanceof BasicSplitPaneUI) {
            //  grab the divider from the UI and remove the border from it
            BasicSplitPaneDivider divider = ((BasicSplitPaneUI) split.getUI()).getDivider();
            if (divider != null && divider.getBorder() != null)
                divider.setBorder(null);
        }
    }

    private void setBorder(DockingPort port, Border border) {
        if (port instanceof JComponent)
            ((JComponent) port).setBorder(border);
    }

    private void setBorder(Component cmp, Border border) {
        if (cmp instanceof JComponent)
            ((JComponent) cmp).setBorder(border);
    }

    // pulls the title out of a DockablePanel to create a titled border.
    // returns the dummy border if a DockablePanel isn't found
    private Border getDesiredBorder(Component cmp) {
        if (cmp instanceof DefaultDockingPort)
            cmp = ((DefaultDockingPort) cmp).getDockedComponent();

        if (cmp instanceof DockablePanel) {
            String title = ((DockablePanel) cmp).getDockable().getDockingProperties().getDockableDesc();
            return new TitledBorder(title);
        }
        return dummyBorder;
    }

    // convenience method for drilling down into a 'potential' docking port
    private Component getDocked(Component c) {
        if (c instanceof DefaultDockingPort)
            return ((DefaultDockingPort) c).getDockedComponent();
        return c;
    }

}
