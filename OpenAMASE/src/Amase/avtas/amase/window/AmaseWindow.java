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
import avtas.app.StatusListener;
import avtas.app.StatusPublisher;
import avtas.app.Context;
import avtas.properties.UserProperties;
import avtas.properties.UserProperty;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import org.flexdock.docking.defaults.DefaultDockingPort;

/**
 * Implements a window with some default features used in AMASE applications.
 *
 * @author AFRL/RQQD
 */
public class AmaseWindow extends JFrame {

    JMenuBar menuBar = new JMenuBar();
    JToolBar toolbar = new JToolBar();
    JLabel statusbar = new JLabel(" ");
    DefaultDockingPort dockPanel = null;
    final WindowOptions options = new WindowOptions();
    boolean isMainWindow;
    static int margin = 10;

//    public static enum Placement {
//
//        TOP, LEFT, RIGHT;
//
//        String getBorderLayoutLabel() {
//            switch (this) {
//                case TOP:
//                    return BorderLayout.NORTH;
//                case LEFT:
//                    return BorderLayout.WEST;
//                case RIGHT:
//                    return BorderLayout.EAST;
//                default:
//                    return BorderLayout.NORTH;
//            }
//        }
//    }

    public AmaseWindow() {
        this(false, false, false, false);
    }

    public AmaseWindow(boolean isMainWindow, boolean showToolbar, boolean showMenubar, boolean showStatusbar) {
        options.showMenuBar = showMenubar;
        options.showToolBar = showToolbar;
        options.showStatusBar = showStatusbar;

        if (isMainWindow) {
            this.isMainWindow = true;

            if (WindowService.mainWindow != null && WindowService.mainWindow != this) {
                UserExceptions.showWarning("trying to create more than one main window");
            }

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if (!Context.getDefaultContext().requestShutdown()) {
                        e.getWindow().setVisible(true);
                    }
                }
            });
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            menuBar.add(new AmaseMenu());

        }

        StatusPublisher.getDefault().addListener(new StatusListener() {
            @Override
            public void statusUpdate(String status) {
                statusbar.setText(status);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                WindowService.windowList.remove((AmaseWindow) e.getWindow());
            }
        });

        this.isMainWindow = isMainWindow;

        setJMenuBar(menuBar);

        JPanel internalPanel = new JPanel(new BorderLayout(margin, margin));
        setContentPane(internalPanel);
        internalPanel.setBorder(new EmptyBorder(margin, margin, margin, margin));

        add(toolbar, BorderLayout.NORTH);
        add(statusbar, BorderLayout.SOUTH);

        dockPanel = DockManager.addWindow(this);
        if (dockPanel != null)
            add(dockPanel, BorderLayout.CENTER);

        showToolbar(options.showToolBar);
        showStatusbar(options.showStatusBar);
        showMenubar(options.showMenuBar);
    }

    public void showToolbar(boolean show) {
        this.toolbar.setVisible(show);
        revalidate();
    }

    public void showStatusbar(boolean show) {
        this.statusbar.setVisible(show);
        revalidate();
    }

    public void showMenubar(boolean show) {
        this.menuBar.setVisible(show);
        revalidate();
    }

    public void setToolbarPlacement(String loc) {
        
        if (loc != null && loc.length()>1) {
            loc = loc.substring(0,1).toUpperCase() + loc.substring(1).toLowerCase();
            loc = loc.matches("(North|East|West)") ? loc : BorderLayout.NORTH;
        }
        else {
            loc = BorderLayout.NORTH;
        }
        toolbar.setOrientation(loc.equals(BorderLayout.EAST) || loc.equals(BorderLayout.WEST) ? JToolBar.VERTICAL : JToolBar.HORIZONTAL);
        getContentPane().add(toolbar, loc);
    }

    /**
     * Sets the baseline title for this window. Applications can modify the
     * title at runtime (such as appending a file path) but it does not affect
     * the base title.
     *
     * @param title Title to set.
     */
    public void setBaseTitle(String title) {
        this.options.baseTitle = title;
        setTitle(options.baseTitle);
    }

    /**
     * Returns the base title, which is the title that is displayed prior to
     * augmentation (such as appending the path of the current file).
     */
    public String getBaseTitle() {
        return options.baseTitle;
    }

    /**
     * Sets the title of the window by appending the base title with the given
     * String.
     *
     * @param text
     */
    public void appendTitle(String text) {
        setTitle(options.baseTitle + " " + text);
    }

    public static AmaseWindow getOwnerWindow(Component comp) {
        Frame f = JOptionPane.getFrameForComponent(comp);
        return f instanceof AmaseWindow ? (AmaseWindow) f : null;
    }

    public WindowOptions getWindowOptions() {
        return options;
    }

    public static void main(String[] args) {
        AmaseWindow win = new AmaseWindow(true, true, true, true);
        win.setSize(640, 480);
        win.setVisible(true);
    }

    public static class WindowOptions extends UserProperties {

        @UserProperty(Description = "Shows the menu bar.")
        public boolean showMenuBar = true;
        @UserProperty(Description = "Shows a toolbar.")
        public boolean showToolBar = false;
        @UserProperty(Description = "Sets the orientation of the toolbar.  [North, West, East]")
        public String toolbarPlacement = BorderLayout.NORTH;
        @UserProperty(Description = "Shows a status bar at the bottom of the window.")
        public boolean showStatusBar = true;
        @UserProperty(Description = "The baseline title that is shown in the title bar.")
        public String baseTitle = "AMASE";
        @UserProperty(Description = "Preferred Width of window.")
        public int width = 0;
        @UserProperty(Description = "Preferred Height of window.")
        public int height = 0;
        @UserProperty(Description = "An Icon to show.  Specify the path using java classpath URL conventions.")
        public String icon = null;
        @UserProperty(Description = "x coordinate of the top left corner of the window.")
        public int x = 0;
        @UserProperty(Description = "y coordinate of the top left corner of the window.")
        public int y = 0;
        @UserProperty(Description = "If true, the window fills the screen")
        public boolean fullscreen = false;

        public void fromWindow(AmaseWindow window) {
            baseTitle = (window.getBaseTitle());
            width = (window.getWidth());
            height = (window.getHeight());
            x = window.getX();
            y = window.getY();
            fullscreen = (window.getExtendedState() & Frame.MAXIMIZED_BOTH) != 0;
            showMenuBar = window.getJMenuBar().isVisible();
            showToolBar = window.toolbar.isVisible();
            showStatusBar = window.statusbar.isVisible();
            toolbarPlacement = (String) ((BorderLayout) window.getContentPane().getLayout()).getConstraints(window.toolbar);
            if (toolbarPlacement == null) {
                toolbarPlacement = BorderLayout.NORTH;
            }
        }

        public void toWindow(AmaseWindow window) {
            window.setTitle(baseTitle);
            window.setSize(width, height);
            window.setLocation(x, y);
            window.showMenubar(showMenuBar);
            window.showToolbar(showToolBar);
            window.showStatusbar(showStatusBar);
            window.setToolbarPlacement(toolbarPlacement);
            if (fullscreen) {
                window.setExtendedState(window.getExtendedState() | Frame.MAXIMIZED_BOTH);
            }
            try {
                if (icon != null)
                    window.setIconImage(ImageIO.read(getClass().getResource((icon))));
            } catch (IOException ex) {
                UserExceptions.showWarning(this, "Cannot load window icon " + icon, ex);
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */