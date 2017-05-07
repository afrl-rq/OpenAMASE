// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package amase.examples;

import afrl.cmasi.AirVehicleState;
import afrl.cmasi.SessionStatus;
import avtas.amase.AmasePlugin;
import avtas.amase.scenario.ScenarioEvent;
import avtas.app.EventFilter;
import avtas.app.EventSupport;

/**
 * This example shows how to use the {@link EventSupport} system to process events.
 * <p>
 * Event Support allows for more organized and cleaner looking code, but it incurs a 
 * small performance penalty.  In general, EventSupport is not used but is available
 * for developers who prefer a more compact style of event processing.
 * </p>
 * @see EventSupport
 * 
 * @author AFRL/RQQD
 */
public class EventSupportExample extends AmasePlugin {
    
    EventSupport eventSupport;

    public EventSupportExample() {
        setPluginName("Event Support Example");
        
        // sets up event support on this plugin.  (See note below)
        eventSupport = new EventSupport(this);
    }
    
    
    
    // this section shows how event filters can be used.  Event filters automatically 
    // are setup to receive events that correspond to a given java type (and sub type).
    // This is through the EventSupport class that is automatically setup in AmasePlugin.
    // To use event filters, just annotate any public method that has a single parameter
    // with "@EventFilter" to receive events of the type specified by the parameter.
    // To use event support, simply override the "eventOccurred" method and pass the
    // event to an EventSuppport instance.
    //
    // 
    
    @EventFilter
    public void event(SessionStatus status) {
        System.out.println(status.getState());
    }
    
    @EventFilter
    public void event(ScenarioEvent scenario) {
        System.out.println("loaded file: " + scenario.getSourceFile());
    }
    
    @EventFilter
    public void event(AirVehicleState state) {
        System.out.println("new aircraft " + state.getID());
    }
    

    /**
     * We need to override {@link #eventOccurred(java.lang.Object) } and pass the 
     * event to the eventSupport for this plugin.
     * @param event 
     */
    @Override
    public void eventOccurred(Object event) {
        eventSupport.processEvent(event);
    }
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */