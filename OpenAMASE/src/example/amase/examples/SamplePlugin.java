// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package amase.examples;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.SessionStatus;
import avtas.amase.AmasePlugin;
import avtas.amase.scenario.ScenarioEvent;
import avtas.amase.ui.AircraftColors;
import avtas.amase.ui.IconManager;
import avtas.amase.ui.IconTools;
import avtas.app.Context;
import avtas.app.ContextListener;
import avtas.util.WindowUtils;
import avtas.xml.Element;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Implements some common AMASE plugin methods to show functionality.  The methods in
 * this example override those in {@link AmasePlugin}.  None of the methods are 
 * required to be implemented by plugins, so only implement methods as required.
 * @author AFRL/RQQD
 */
public class SamplePlugin extends AmasePlugin {
    
    JPanel guiPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    public SamplePlugin() {
        // set the plugin name to something meaningful for GUI and configuration purposes
        setPluginName("Sample Plugin");
        
        // lay things out top to bottom using the GridBagLayout (see Swing tutorial for info)
        guiPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        
        constraints.ipadx = 5;
        constraints.ipady = 5;
        
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        guiPanel.add(new JLabel("Aircraft in the Scenario"), constraints);
        
        constraints.gridy = 1;
        guiPanel.add(buttonPanel, constraints);

    }

    
    /**
     * Handle some events.  This is a core aspect of AMASE.  Events are handled by overriding
     * this method in AmasePlugin.
     * @param event 
     */
    @Override
    public void eventOccurred(Object event) {
        if (event instanceof SessionStatus) {
            SessionStatus ss = (SessionStatus) event;
            System.out.println("Time: " + ss.getScenarioTime() / 1000d + ", state: " + ss.getState());
        }
        else if (event instanceof AirVehicleConfiguration) {
            AirVehicleConfiguration avc = (AirVehicleConfiguration) event;
            System.out.println("aircraft " + avc.getID() + " added to scenario.");
            addAircraftButton(avc);
        }
        else if (event instanceof ScenarioEvent) {
            ScenarioEvent scenario = (ScenarioEvent) event;
            System.out.println("scenario " + scenario.getSourceFile().getName() + " loaded.");
            buttonPanel.removeAll();
        }
    }
    
    
    public void addAircraftButton(AirVehicleConfiguration avc) {
        // get the image that corresponds to this aircraft
        Image image = IconManager.getIcon(avc);
        
        // create a stylized image using a white border
        BufferedImage aircraftImage = IconTools.getFilledImage(image, 32, 32, 2, Color.WHITE, AircraftColors.getColor(avc.getID()));
        
        // create a button with the image and add it to the panel
        JButton button = new JButton( avc.getLabel(), new ImageIcon(aircraftImage) );
        buttonPanel.add(button);
        buttonPanel.revalidate();
    }

    /**
     * Plugins can return GUI components to display.  
     * @return the panel created above.
     */
    @Override
    public Component getGui() {
        return guiPanel;
    }

    /** 
     * Return an icon for user display purposes.
     */
    @Override
    public Icon getIcon() {
        // icons should be no more than 24 x 24 pixels
        return new ImageIcon(SamplePlugin.class.getResource("/amase/examples/star.png"));
    }

    /**
     * If there are things for the user to set, return a GUI element using this method.
     */
    @Override
    public JPanel getSettingsPanel() {
        JPanel panel = new JPanel();
        panel.add(new JCheckBox("Sample Setting"));
        return panel;
    }

    /**
     * Plugins can added menus to the application.  
     * @param menubar 
     */
    @Override
    public void getMenus(final JMenuBar menubar) {
        // this method returns a top-level menu named "Test" if it exists, or creates
        // the menu and adds it to the menubar.
        JMenu menu = WindowUtils.getMenu(menubar, "Test");
        
        // add a menu item to the menu
        menu.add(new AbstractAction("Click Me") {

            @Override
            public void actionPerformed(ActionEvent e) {
                // to display a message window centered on the application window,
                // we use a couple of convenience methods from JOptionPane.
                Frame owner = JOptionPane.getFrameForComponent(menubar);
                JOptionPane.showMessageDialog(owner, "You clicked me.");
            }
        });
    }
    
    
    

    
    // the following methods are meant to inform the plugin of application-wide activities.

    
    /**
     * Provides information to the plugin when added to the application.  
     * @see ContextListener
     */
    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
        System.out.println("added to application.");
        // we echo anything that is added to the plugin's configuration as an example 
        // of how to access this information
        System.out.println("Configuration information for Sample Plugin:");
        System.out.println(xml.toXML());
    }

    /** Called when all of the plugins have been added to the application. */
    @Override
    public void initializeComplete() {
        System.out.println("initialize complete.");
    }
    
    /**
     * Informs the plugin of other plugins added to the application.
     * @see ContextListener
     */
    @Override
    public void applicationPeerAdded(Object peer) {
        System.out.println("peer " + peer.getClass() + " added. ");
    }

    /**
     * Informs the plugin of other plugins removed from the application.  
     * @see ContextListener
     */
    @Override
    public void applicationPeerRemoved(Object peer) {
        System.out.println("peer " + peer.getClass() + " removed. ");
    }
    
    

    /**
     * Called by the application when shutting down.  Returning true means it is 
     * OK to shutdown, false stops the shutdown.  this is where the plugin can 
     * show a confirmation window to the user.
     */
    @Override
    public boolean requestShutdown() {
        System.out.println("Shutting down.");
        return true;
    }

    /**
     * The application is shutting down.  Perform shutdown logic here.
     */
    @Override
    public void shutdown() {
        System.out.println("Goodbye.");
    }
    
    
    
    
    
    // these methods are used by the timer system in AMASE

    /**
     * Informs the plugin of timer increments.  This is the high-resolution timer.  In addition,
     * AMASE usually publishes SessionStatus messages over the event channel at a lower
     * publish rate.
     */
    @Override
    public void step(double timestep, double sim_time) {
        System.out.println("time: " + sim_time + " , step: " + timestep);
    }

    /**
     * Called by the timer when its state changes, such as run, pause, or reset.
     */
    @Override
    public void timerStateChanged(TimerState state, double sim_time) {
        System.out.println("timer state: " + state);
    }

    
    
    
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */