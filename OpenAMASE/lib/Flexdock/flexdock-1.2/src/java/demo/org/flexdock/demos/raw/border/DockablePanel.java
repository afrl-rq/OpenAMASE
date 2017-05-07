// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.demos.raw.border;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.defaults.AbstractDockable;

public class DockablePanel extends JPanel {
    private String title;
    private JPanel dragInit;
    private Dockable dockableImpl;

    public DockablePanel(String title) {
        super(new BorderLayout());
        dragInit = new JPanel();
        dragInit.setBackground(getBackground().darker());
        dragInit.setPreferredSize(new Dimension(10, 10));
        add(dragInit, BorderLayout.EAST);
        setBorder(new TitledBorder(title));
        setTitle(title);
        dockableImpl = new DockableImpl();
        DockingManager.registerDockable(dockableImpl);
    }

    private void setTitle(String title) {
        this.title = title;
    }

    Dockable getDockable() {
        return dockableImpl;
    }

    public String getTitle() {
        return title==null? null: title.trim();
    }

    private class DockableImpl extends AbstractDockable {
        private DockableImpl() {
            super("dockable." + getTitle());
            // the titlebar will the the 'hot' component that initiates dragging
            getDragSources().add(dragInit);
            setTabText(getTitle());
        }

        public Component getComponent() {
            return DockablePanel.this;
        }
    }
}
