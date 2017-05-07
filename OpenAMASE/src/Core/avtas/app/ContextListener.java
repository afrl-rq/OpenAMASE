// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.app;

import avtas.xml.Element;

/**
 *
 * @author AFRL/RQQD
 */
public interface ContextListener {
    
    /** Called when this plugin is added to the application */
    public void addedToApplication(Context context, Element xml, String[] cmdParams);

    /** Called when other plugins are added to the application */
    public void applicationPeerAdded(Object peer);
    
    /** Called when other plugins are removed from the application */
    public void applicationPeerRemoved(Object peer);
    
    /** Called when all context plugins have been added and initialized. */
    public void initializeComplete();
    
    /**
     * A signal that the application is shutting down or that this component is being
     * removed from the application.  This is asking permission to 
     * shutdown from all of the plugins.  All plugins must return "true" to continue 
     * with shutdown.  This is designed to allow developers to include shutdown
     * warnings and opportunities to save work.  Override to perform shutdown checks.
     * 
     * @return true if it is OK to shutdown the app.
     */
    public boolean requestShutdown();
    
    /**
     * Called by the application when it is shutting down or when this component is removed
     * from the application.  Perform shutdown logic here.  To prevent an application from 
     * shutting down under certain conditions, override the {@link #requestShutdown() } method instead.
     */
    public void shutdown();
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */