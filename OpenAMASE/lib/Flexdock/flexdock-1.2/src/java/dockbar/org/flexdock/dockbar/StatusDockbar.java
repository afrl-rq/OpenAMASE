// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.dockbar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.state.MinimizationManager;
import org.flexdock.plaf.common.border.SlideoutBorder;

/**
 * A special dockbar which can be used to hold a statusbar panel at the bottom.
 *
 * @author Wolfgang Zitzelsberger
 */
public class StatusDockbar extends Dockbar {
    private JPanel labelPanel;

    private JComponent statusBarComponent;

    public StatusDockbar(DockbarManager manager, int orientation) {
        super(manager, orientation);
        labelPanel = new JPanel();
        setOrientation(orientation);
        setLayout(new BorderLayout());
        super.add(labelPanel);
    }

    public void setStatusBarComponent(JComponent c) {
        statusBarComponent = c;
        add(statusBarComponent, BorderLayout.SOUTH);
    }

    public JComponent getStatusBarComponent() {
        return statusBarComponent;
    }

    public Component add(Component c) {
        return labelPanel.add(c);
    }

    void undock(Dockable dockable) {
        DockbarLabel label = getLabel(dockable);

        labelPanel.remove(label);
        mDocks.remove(label);
        getParent().validate();
        repaint();
    }

    private void setOrientation(int orientation) {
        orientation = getValidOrientation(orientation);
        this.orientation = orientation;

        Border border = labelPanel.getBorder();
        if (border instanceof SlideoutBorder)
            ((SlideoutBorder) border).setOrientation(orientation);

        int boxConstraint = orientation == MinimizationManager.TOP
                            || orientation == MinimizationManager.BOTTOM ? BoxLayout.LINE_AXIS
                            : BoxLayout.PAGE_AXIS;
        labelPanel.setLayout(new BoxLayout(labelPanel, boxConstraint));
    }

    public Dimension getPreferredSize() {
        if (statusBarComponent == null || statusBarComponent.getComponentCount() == 0)
            if (mDocks.size() == 0)
                return new Dimension(0, 0);
            else
                return labelPanel.getComponent(0).getPreferredSize();

        if (labelPanel.getComponentCount() == 0)
            return statusBarComponent.getPreferredSize();

        DockbarLabel label = (DockbarLabel) labelPanel.getComponent(0);
        return new Dimension(label.getPreferredSize().width
                             + statusBarComponent.getPreferredSize().width, label
                             .getPreferredSize().height
                             + statusBarComponent.getPreferredSize().height);
    }

}
