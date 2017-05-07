// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.window;

import avtas.app.UserExceptions;
import avtas.amase.AmasePlugin;
import avtas.app.Context;
import avtas.app.SettingsManager;
import avtas.util.WindowUtils;
import avtas.xml.Element;
import avtas.xml.XMLUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

/**
 *
 * @author AFRL/RQQD
 */
public class WindowService extends AmasePlugin {

    static final String settingsFile = "WindowService.xml";
    //List<WindowInfo> windowList = new ArrayList<>();
    static AmaseWindow mainWindow = null;
    static List<AmaseWindow> windowList = new ArrayList<>();
    static WindowService singleton = null;
    static Element dockLayout = null;

    public WindowService() {

        if (singleton != null) {
            UserExceptions.showError(this, "Trying to create multiple window Managers", null);
            return;
        }

        super.setPluginName("WindowService");

        loadSettings(SettingsManager.getAsXml(settingsFile));

    }

    @Override
    public void applicationPeerAdded(Object peer) {
        if (peer instanceof AmasePlugin) {
            DockManager.addGuiPlugin((AmasePlugin) peer);
            if (mainWindow != null) {
                ((AmasePlugin) peer).getMenus(mainWindow.getJMenuBar());
                ((AmasePlugin) peer).getToolbarItems(mainWindow.toolbar);
            }
        }
    }

    @Override
    public void applicationPeerRemoved(Object peer) {
        if (peer instanceof AmasePlugin) {
            DockManager.removeGuiPlugin((AmasePlugin) peer);
        }
        
        JMenuBar mainBar = mainWindow.getJMenuBar();
        AmaseMenu amaseMenu = AmaseMenu.getAmaseMenu(mainBar);
        
        mainBar.removeAll();
        
        mainBar.add(amaseMenu);
        
        for(Object obj : Context.getDefaultContext().getObjects("avtas.amase.AmasePlugin"))
        {
            //Check for WindowService and skip so that menus stay in same order
            if(obj instanceof WindowService)
                continue;
            
            ((AmasePlugin) obj).getMenus(mainBar);
            ((AmasePlugin) obj).getToolbarItems(mainWindow.toolbar);
        }
        
        this.getMenus(mainBar);
        mainWindow.repaint();
    }

    @Override
    public void initializeComplete() {

        if (dockLayout != null) {
            DockManager.setDockLayout(dockLayout);
        }
        if (mainWindow != null) {
            // add menus if there are any to add
            getMenus(mainWindow.getJMenuBar());
        }
        for (AmaseWindow win : windowList) {
            win.setVisible(true);
        }

    }

    public static void loadSettings(Element el) {


        if (el != null) {
            try {
                
                DockManager.showTitlebars(XMLUtil.getBool(el, "ShowTitlebars", true));

                List<Element> windowInfoList = XMLUtil.getChildren(el, "Window");

                for (Element winEl : windowInfoList) {

                    AmaseWindow window = null;
                    if (mainWindow == null) {
                        window = new AmaseWindow(true, false, true, true);
                        mainWindow = window;
                    }
                    else {
                        window = new AmaseWindow(false, false, false, false);
                    }

                    window.getWindowOptions().fromXml(winEl);
                    window.getWindowOptions().toWindow(window);

                    windowList.add(window);
                    //window.setVisible(true);

                }


                dockLayout = XMLUtil.getChild(el, "Layout");


            } catch (Exception ex) {
                UserExceptions.showError(WindowService.class, "Error Reading Settings File", ex);
            }
        }


        if (mainWindow == null) {
            // create a default window if none is defined.
            AmaseWindow window = new AmaseWindow(true, false, true, true);
            mainWindow = window;
            windowList.add(window);
            window.setSize(800, 600);
            //window.setVisible(true);
        }
    }

    AmaseWindow addNewWindow() {
        AmaseWindow window;

        if (mainWindow == null) {
            window = new AmaseWindow(true, false, true, true);
            mainWindow = window;
        }
        else {
            window = new AmaseWindow(false, false, false, false);
        }

        windowList.add(window);
        window.setVisible(true);

        return window;
    }

    public static void saveSettings() {

        Element el = new Element(WindowService.class.getSimpleName());
        
        el.addElement("ShowTitlebars").setText(Boolean.toString(DockManager.isShowingTitlebars()));

        for (AmaseWindow win : windowList) {
            el.add(win.getWindowOptions().toXml("Window"));
        }

        try {
            // save the current layout from the DockManager
            Element dockEl = DockManager.getDockLayout();
            el.addComment("Automatically generated layout information.  Do not edit by hand.");
            el.add(dockEl);

        } catch (Exception ex) {
            Logger.getLogger(WindowService.class.getName()).log(Level.SEVERE, null, ex);
        }

        File file = SettingsManager.getFile(settingsFile);
        el.toFile(file);
    }

    public static AmaseWindow getMainWindow() {
        return mainWindow;
    }

    public static List<AmaseWindow> getAllWindows() {
        return Collections.unmodifiableList(windowList);
    }

    /**
     * Sets the text displayed in the status bar at the bottom of the window.
     */
    public static void setStatusText(String text) {
        for (AmaseWindow win : windowList) {
            win.statusbar.setText(text);
        }
    }

    @Override
    public JPanel getSettingsPanel() {
        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());

        final JTabbedPane tabPane = new JTabbedPane();
        for (AmaseWindow win : windowList) {
            String tabName = win.getBaseTitle() + (win == mainWindow ? " (main)" : "");
            win.getWindowOptions().fromWindow(win);
            tabPane.addTab(tabName, win.getWindowOptions().getEditor());
        }
        panel.add(tabPane, BorderLayout.CENTER);

        JPanel buttonpanel = new JPanel();
        buttonpanel.add(new JButton(new AbstractAction("Remove") {
            @Override
            public void actionPerformed(ActionEvent e) {
                AmaseWindow win = windowList.get(tabPane.getSelectedIndex());
                if (win != mainWindow) {
                    windowList.remove(win);
                    tabPane.removeTabAt(tabPane.getSelectedIndex());
                }
                else {
                    JOptionPane.showMessageDialog(tabPane, "Cannot remove the main window.");
                }
            }
        }));
        buttonpanel.add(new JButton(new AbstractAction("Add New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                AmaseWindow win = addNewWindow();
                tabPane.addTab(win.getTitle(), win.getWindowOptions().getEditor());
            }
        }));

        panel.add(buttonpanel, BorderLayout.SOUTH);
        
        JPanel optionPanel = new JPanel();
        optionPanel.setBorder(new TitledBorder("Global Options"));
        panel.add(optionPanel, BorderLayout.NORTH);
        
        final JCheckBox showTitlebarCheck = new JCheckBox("Titlebars", DockManager.isShowingTitlebars());
        showTitlebarCheck.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DockManager.showTitlebars(showTitlebarCheck.isSelected());
            }
        });
        
        optionPanel.add(showTitlebarCheck);
        


        return panel;
    }

    @Override
    public void getMenus(JMenuBar menubar) {

        JMenu menu = WindowUtils.getMenu(menubar, "Layout");

        menu.add(new AbstractAction("Save Window Layouts") {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (AmaseWindow win : windowList) {
                    win.options.fromWindow(win);
                }
                saveSettings();
            }
        });
        
        JMenu windowsMenu = new JMenu("Windows");
        menu.add(windowsMenu);
        for (final AmaseWindow win : windowList) {
            JMenu winMenu = new JMenu(win.getBaseTitle());
            windowsMenu.add(winMenu);
            
            winMenu.add(new AbstractAction("Reset Layouts") {

                @Override
                public void actionPerformed(ActionEvent e) {
                    DockManager.resetView(win);
                }
            });
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */