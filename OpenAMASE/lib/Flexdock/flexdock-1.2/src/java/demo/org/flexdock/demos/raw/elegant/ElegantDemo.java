// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.demos.raw.elegant;

import java.awt.Container;

import javax.swing.JFrame;

import org.flexdock.demos.util.DemoUtility;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.defaults.DefaultDockingPort;
import org.flexdock.docking.defaults.StandardBorderManager;
import org.flexdock.plaf.common.border.ShadowBorder;


public class ElegantDemo extends JFrame implements DockingConstants {
    private ElegantPanel j2eeHierarchyView;
    private ElegantPanel j2eeNavView;
    private ElegantPanel consoleView;
    private ElegantPanel serversView;
    private ElegantPanel tasksView;
    private ElegantPanel searchView;
    private ElegantPanel synchronizeView;
    private ElegantPanel outlineView;
    private ElegantPanel editorView;

    private DockingPort rootDockingPort;

    public ElegantDemo() {
        super("Elegant Docking Demo");
        init();
    }

    private void init() {
        // create all of the dockable panels
        createViews();

        // create the dockingPort
        setContentPane((Container)getRootDockingPort());

        // initialize the layout
        initLayout();
    }

    private DockingPort getRootDockingPort() {
        if(rootDockingPort==null) {
            DefaultDockingPort port = new DefaultDockingPort();
            port.setBorderManager(new StandardBorderManager(new ShadowBorder()));
            rootDockingPort = port;
        }
        return rootDockingPort;
    }

    private void createViews() {
        j2eeHierarchyView = new ElegantPanel("J2EE Hierarchy");
        j2eeNavView = new ElegantPanel("J2EE Navigator");
        consoleView = new ElegantPanel("Console");
        serversView = new ElegantPanel("Servers");
        tasksView = new ElegantPanel("Tasks");
        searchView = new ElegantPanel("Search");
        synchronizeView = new ElegantPanel("Synchronize");
        outlineView = new ElegantPanel("Outline");
        editorView = new ElegantPanel("Editor");
    }

    private void initLayout() {
        DockingManager.setDefaultPersistenceKey("ElegantDemo.xml");

        try {
            if(!DockingManager.restoreLayout(true))
//			if(true)
                setupDefaultLayout();
        } catch(Exception e) {
            e.printStackTrace();
            setupDefaultLayout();
        }

        // remember to save the current layout state when the application
        // shuts down
        DockingManager.setAutoPersist(true);
    }

    private void setupDefaultLayout() {
        // make sure there is nothing within the root dockingport
        getRootDockingPort().clear();

        // setup 4 quadrants
        // dock the editor into the root dockingport
        DockingManager.dock(editorView, getRootDockingPort());
        // dock the hierarchy-view to the west of the editor
        editorView.dock(j2eeHierarchyView, WEST_REGION, 0.3f);
        // dock the outline to the south of the hierarchy
        j2eeHierarchyView.dock(outlineView, SOUTH_REGION, 0.3f);
        // dock the task-view to the south of the editor
        editorView.dock(tasksView, SOUTH_REGION, 0.3f);

        // tab the nav-view onto the hierarchy view
        j2eeHierarchyView.dock(j2eeNavView);

        // tab the rest of the views onto the task-view
        tasksView.dock(serversView);
        tasksView.dock(consoleView);
        tasksView.dock(searchView);
        tasksView.dock(synchronizeView);

        // resize the immediate splitPane child of the root dockingport
//		DockingManager.setSplitProportion(rootDockingPort, 0.3f);
        // resize the splitPane containing the hierarchy-view
//		DockingManager.setSplitProportion(j2eeHierarchyView, 0.75f);
        // resize the splitPane containing the editor
//		DockingManager.setSplitProportion(editorView, 0.75f);
    }

    public static void main(String[] args) {
        ElegantDemo demo = new ElegantDemo();
        DemoUtility.setCloseOperation(demo);
        demo.setSize(800, 600);
        demo.setVisible(true);
    }
}
