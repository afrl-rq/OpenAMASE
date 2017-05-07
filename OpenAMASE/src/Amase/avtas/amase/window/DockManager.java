// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.window;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import avtas.app.UserExceptions;
import avtas.amase.AmasePlugin;
import avtas.xml.Element;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
//import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
//import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.defaults.AbstractDockable;
import org.flexdock.docking.defaults.DefaultDockingPort;
import org.flexdock.docking.state.PersistenceException;
import org.flexdock.perspective.Perspective;
import org.flexdock.perspective.persist.PerspectiveModel;
import org.flexdock.perspective.persist.xml.XMLPersister;
import org.flexdock.plaf.common.border.ShadowBorder;
import org.flexdock.plaf.resources.paint.GradientPainter;
import org.flexdock.util.DockingUtility;
import org.flexdock.view.Button;

/**
 * A docking component for AMASE windows.
 *
 * @author AFRL/RQQD
 */
public class DockManager {

    static final List<Window> windowList = new ArrayList<>();
    static final HashMap<AmasePlugin, AmaseDockable> pluginMap = new HashMap<>();
    static Border view_border = new ShadowBorder();
    static Element currentLayout = null;
    protected static final String config_file = "WindowLayout.xml";
    private static boolean showTitlebar = true;

    private DockManager() {
        
    }

    /**
     * Adds a Plugin to the docking framework.
     *
     * @param pi Plugin to add
     */
    public static void addGuiPlugin(AmasePlugin pi) {

        Component gui = pi.getGui();
        if (gui == null) {
            return;
        }

        AmaseDockable view = pluginMap.get(pi);
        if (view == null) {

            // generate a key for the plugin
            String origKey = pi.getClass().getCanonicalName();
            String key = origKey;
            int i = 1;
            Dockable existingPort = DockingManager.getDockable(key);
            while (existingPort != null) {
                key = origKey + "_" + i;
                i++;
                existingPort = DockingManager.getDockable(key);
            }

            view = new AmaseDockable(gui, pi.getPluginName(), pi.getIcon(), key);
            view.showTitlebar(showTitlebar);
            DockingManager.registerDockable(view);
            pluginMap.put(pi, view);

            // add a button to get the settings for the plugin
            view.addAction(createSettingsAction(pi, view));
            //view.addAction(View.PIN_ACTION);

            if (!windowList.isEmpty()) {
                DockingPort vp = (DockingPort) DockingManager.getRootDockingPort(windowList.get(0));
                if (vp != null) {
                    vp.dock((Dockable) view, DockingConstants.EAST_REGION);
                }
            }
        }
    }

    /**
     * This makes the dock manager aware of a Window. When the window is
     * registered, a new docking content area is created, but not added to the
     * window. The user controls how the content area is laid out in the window.
     */
    public static DefaultDockingPort addWindow(Window win) {

        if (!windowList.contains(win)) {

            DefaultDockingPort port = new DefaultDockingPort();
            port.setBorderManager(null);
            //port.setBorder(new EmptyBorder(5, 5, 5, 5));

            windowList.add(win);

            return port;
        }
        return null;
    }

    /**
     * De-registers a window from the framework. This DOES NOT close the window
     *
     * @param win window to remove from framework
     */
    public static void removeWindow(Window win) {
        windowList.remove(win);
    }

    /**
     * Removes the Plugin from the docking framework.
     *
     * @param pi plugin to remove
     */
    public static void removeGuiPlugin(AmasePlugin pi) {
        AmaseDockable view = pluginMap.remove(pi);
        if (view != null) {
            view.getDockingPort().undock(view.getComponent());
            DockingManager.unregisterDockable((Dockable) view);
        }
    }

    /** Resets the view to a default state */
    public static void resetView(Window window) {
        Set<AmaseDockable> dockedComps = new HashSet<>();
        DockingPort port = DockingManager.getRootDockingPort(window);
        if (port != null) {
            getComponents(port, dockedComps);
            port.clear();
            for (AmaseDockable c : dockedComps) {
                port.dock((Dockable) c, DockingConstants.CENTER_REGION);
            }
        }
    }
    
    public static void showTitlebars(boolean show) {
        for (AmaseDockable pi : pluginMap.values()) {
            pi.showTitlebar(show);
        }
        
        showTitlebar = show;
    }
    
    public static boolean isShowingTitlebars() {
        return showTitlebar;
    }

    static void getComponents(DockingPort port, Set<AmaseDockable> comps) {
        for (Object o : port.getDockables()) {
            Dockable d = (Dockable) o;
            if (d instanceof DockingPort) {
                getComponents((DockingPort) d, comps);
            }
            else if (d instanceof AmaseDockable) {
                comps.add( (AmaseDockable) d);
            }
        }
    }

    /**
     * Returns an XML representation of the current layout of plugins
     */
    public static Element getDockLayout() {
        Element layoutEl = new Element("Layout");
        try {
            for (Window win : windowList) {
                DockingPort viewport = DockingManager.getRootDockingPort(win);
                if (viewport != null) {
                    StringBuilder builder = new StringBuilder();
                    try {
                        Perspective persp = new Perspective("default", "default");
                        persp.cacheLayoutState(viewport);
                        PerspectiveModel info = new PerspectiveModel(persp.getName(), persp.getName(), new Perspective[]{persp});

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        XMLPersister.newDefaultInstance().store(bos, info);
                        builder.append(new String(bos.toByteArray()));
                    } catch (IOException | PersistenceException ex) {
                        UserExceptions.showWarning(DockManager.class, "Could not save state of GUI layout. ", ex);
                    }
                    Element winEl = Element.read(builder.toString());
                    layoutEl.add(winEl);
                }
            }
            return layoutEl;
        } catch (Exception ex) {
            UserExceptions.showWarning(DockManager.class, "Could not save state of GUI layout. ", ex);
            return layoutEl;
        }
    }

    /**
     * Sets the layout of components according to a stored XML string
     */
    public static void setDockLayout(Element el) {

        List<Element> winEls = el.getChildElements();

        for (int i = 0; i < winEls.size(); i++) {
            if (i >= windowList.size()) {
                break;
            }

            Window win = windowList.get(i);
            Element winEl = winEls.get(i);

            try {
                DockingPort viewport = DockingManager.getMainDockingPort(win);
                if (viewport == null) {
                    return;
                }

                String xmlStr = winEl.toXML();
                PerspectiveModel model;
                try (ByteArrayInputStream bis = new ByteArrayInputStream(xmlStr.getBytes())) {
                    model = XMLPersister.newDefaultInstance().load(bis);
                }
                Perspective p = model.getPerspectives()[0];

                for (Dockable dock : p.getDockables()) {
                    if (dock != null) {
                        viewport.dock(dock, DockingConstants.CENTER_REGION);
                    }
                }

                viewport.importLayout(p.getLayout().getRestorationLayout());

            } catch (IOException | PersistenceException ex) {
                UserExceptions.showError(DockingManager.class, "Cannot load window layout", ex);
            }
        }

        // add in any plugins that are not specified in the layout.
        for (Entry<AmasePlugin, AmaseDockable> entry : pluginMap.entrySet()) {
            DockingPort root = DockingManager.getMainDockingPort(entry.getValue().getComponent());
            if (root == null && !windowList.isEmpty()) {
                root = DockingManager.getRootDockingPort(windowList.get(0));
                root.dock((Dockable) entry.getValue(), DockingConstants.EAST_REGION);
            }
        }

        currentLayout = el;

    }

    /**
     * Returns the view associated with the plugin, or null if none is found.
     */
    public static AmaseDockable getView(AmasePlugin pi) {
        return pluginMap.get(pi);
    }

//    /**
//     * Adds an action button to the view's title bar.
//     */
//    public static void addAction(View view, Action action) {
//        view.getTitlebar().addAction(action);
//    }
    static Action createSettingsAction(final AmasePlugin pi, final AmaseDockable view) {

        Icon icon = new ImageIcon(DockManager.class.getResource("/resources/settings.png"));

        return new AbstractAction("Settings", icon) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window win = JOptionPane.getFrameForComponent(view.getComponent());
                JDialog f = new JDialog(win);
                f.setTitle("Settings: " + pi.getPluginName());

                Component comp = pi.getSettingsPanel();

                if (comp != null) {
                    f.add(comp);
                }
                else {
                    f.add(new JLabel("<No Settings for this Plugin>"));
                }

                f.setModal(true);

                f.pack();
                f.setLocationRelativeTo(win);
                f.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                f.setVisible(true);
            }
        };

    }

    public static class AmaseDockable extends AbstractDockable {

        JPanel panel;
        JPanel titlebar;
        GradientPainter painter = new GradientPainter();

        public AmaseDockable(Component comp, String title, Icon icon, String id) {
            super(id);

            panel = new JPanel(new BorderLayout());
            titlebar = createTitlebar(title, icon);
            panel.add(titlebar, BorderLayout.NORTH);

            panel.add(comp, BorderLayout.CENTER);


            getDragSources().add(titlebar);
            
            if (icon != null) {
                getDockingProperties().setDockbarIcon(icon);
                getDockingProperties().setTabIcon(icon);
            }

            setTabText(title);
            panel.setBorder(view_border);
        }
        
        public boolean isTitlebarShowing() {
            return titlebar.isVisible();
        }
        
        public void showTitlebar(boolean show) {
            titlebar.setVisible(show);
            panel.revalidate();
        }


        public Button addAction(Action action) {
            Button button = new Button(action);
            titlebar.add(button);
            Dimension prefSize = button.getPreferredSize();
            prefSize.setSize(prefSize.getHeight(), prefSize.getHeight());
            button.setPreferredSize(prefSize);
            return button;
        }

        private JPanel createTitlebar(String title, Icon icon) {
            
            JPanel titlePanel = new JPanel() {
                
                Color shadow = new Color( 75, 75, 75);
                
                @Override
                public void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setPaint(new GradientPaint(new Point(0,0), shadow, new Point(getWidth(), 0), getBackground()));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
                
            };
            titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
            titlePanel.setBorder(new EmptyBorder(2, 2, 2, 2));
            JLabel lbl = new JLabel(title);
            if (icon != null) {
                lbl.setIcon(icon);
            }
            //lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            lbl.setForeground(Color.WHITE);
            titlePanel.add(lbl);
            titlePanel.add(Box.createGlue());

            lbl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() > 1) {
                        DockingManager.setMinimized(AmaseDockable.this, !DockingUtility.isMinimized(AmaseDockable.this));
                    }
                }
            });

            getDragSources().add(lbl);
            return titlePanel;
        }

        @Override
        public Component getComponent() {
            return panel;
            
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */