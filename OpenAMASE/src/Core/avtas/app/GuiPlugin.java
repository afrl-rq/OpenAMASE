// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.app;

import avtas.util.WindowUtils;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 * Provides a Component for user interaction. 
 * @author AFRL/RQQD
 */
public interface GuiPlugin {
    
    /**
     * Called by the application in order to get a GUI component. If your plugin
     * has a GUI component, then override this method and return the component
     * when requested.
     *
     * @return a component to be included in the application, or null if no GUI
     * component is desired.
     */
    public Component getGui();
    
    /**
     * Called by the application to return an icon associated with the GUI
     * component of this plugin. Override this method to return an icon that
     * will be shown in title bars and tabs in the GUI window.
     *
     * @return an icon to show, or null if no icon is desired.
     */
    public Icon getIcon();
    
     /**
     * allows the implementing class to add menus to the menubar.  Use
     * {@link WindowUtils#getMenu(javax.swing.JMenuBar, java.lang.String) }
     * to obtain a top-level menu by name.
     * @param menubar the menubar to which menus are added.
     */
    public void getMenus(JMenuBar menubar);

    /**
     * called by the application to set up the toolbar. Override this method to
     * add items to the parent window's tool bar.
     *
     * @param toolbar the toolbar to which tools should be added.
     */
    public void getToolbarItems(JToolBar toolbar);

    /**
     * Called by the application to get a GUI for editing settings. Override
     * this method to return a user-editable GUI for settings.
     *
     * @return a settings panel, or null if no settings panel is available.
     */
    public JPanel getSettingsPanel();
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */