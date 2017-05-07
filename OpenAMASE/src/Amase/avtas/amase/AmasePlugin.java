// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase;


import avtas.app.EventSupport;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import avtas.app.Context;
import avtas.app.ContextListener;
import avtas.app.EventFilter;
import avtas.app.GuiPlugin;
import avtas.app.TimerClient;
import avtas.xml.Element;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 *
 * @author AFRL/RQQD
 */
public class AmasePlugin implements AppEventListener,
        ContextListener, TimerClient, GuiPlugin{
    
    String pluginName = "";

    public AmasePlugin() {
        pluginName = getClass().getName();
    }

    public AmasePlugin(String name) {
        this.pluginName = name;
    }

    /**
     * Handles application events.  This does nothing unless overridden by a subclass
     * @param event 
     */
    @Override
    public void eventOccurred(Object event) {
    }
    
    /**
     * Dispatches an event to the overall application.  <br/>
     * Note: This does not pass the event back to this plugin.  To make this plugin aware of
     * the event, call {@link #eventOccurred(java.lang.Object) } on this plugin.<br/>
     * This is a convenience call to {@link AppEventManager#getDefaultEventManager()}
     * @param event 
     */
    public void fireEvent(Object event) {
        AppEventManager.getDefaultEventManager().fireEvent(event, this);
    }

    /**
     * Called by the application to get a String display name for GUI purposes
     * including menus and title bars.
     */
    public String getPluginName() {
        return pluginName;
    }

    /**
     * Sets the display name for GUI purposes including menus and title bars.
     */
    public void setPluginName(String name) {
        this.pluginName = name;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
        // Override to do something useful
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void applicationPeerAdded(Object peer) {
        // Override to do something useful
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void applicationPeerRemoved(Object peer) {
        // Override to do something useful
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void initializeComplete() {
        // Override to do something useful
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void step(double timestep, double sim_time) {
        // Override to do something useful
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void timerStateChanged(TimerState state, double sim_time) {
        // Override to do something useful
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Component getGui() {
        return null;
    }

    /**
     * {@inheritDoc }
     */
    public Icon getIcon() {
        return null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void getMenus(JMenuBar menubar) {
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void getToolbarItems(JToolBar toolbar) {
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public JPanel getSettingsPanel() {
        return null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean requestShutdown() {
        return true;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void shutdown() {
    }
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */