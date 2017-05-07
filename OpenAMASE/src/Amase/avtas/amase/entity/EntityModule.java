// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.entity;

import afrl.cmasi.AltitudeType;
import afrl.cmasi.Location3D;
import avtas.app.AppEventListener;
import avtas.xml.Element;

/**
 * A module that can be used in MultiFlight and EntityModel. Provides some
 * convenient access methods to reduce boilerplate code.
 *
 * @author AFRL/RQQD
 */
public abstract class EntityModule {

    EntityModel model = null;
    protected EntityData data;
    
    AppEventListener appListener = new AppEventListener() {
        @Override
        public void eventOccurred(Object event) {
            applicationEventOccurred(event);
        }
    };

    public EntityModule() {
    }

    /**
     * Returns the model that owns this module.
     */
    public EntityModel getModel() {
        return model;
    }

    /**
     * Returns the EntityData used in this model.
     */
    public EntityData getData() {
        return data;
    }

    /**
     * Initializes the model. Override this method to do something useful
     */
    public void initialize(Element xmlElement) {
    }

    /**
     * Override this method to public void modelEventOccured(Object event) {
     *
     * }
     *
     *
     * /** Fires an event at the model level. All modules will get the event
     * except this one.
     */
    public void fireModelEvent(Object event) {
        if (getModel() != null) {
            getModel().fireModelEvent(event, this);
        }
    }

    /**
     * Fires an event at the application level.
     */
    public void fireApplicationEvent(Object event) {
        if (getModel() != null) {
            getModel().getEventManger().fireEvent(event, getModel());
        }
    }

    /**
     * Override this to get Application-level events
     */
    public void applicationEventOccurred(Object event) {
    }

    /**
     * Override this method to get Model-level events
     */
    public void modelEventOccurred(Object event) {
    }

    public Location3D getLocation() {
        return new Location3D(Math.toDegrees(data.lat.asDouble()), Math.toDegrees(data.lon.asDouble()), (float) data.alt.asDouble(), AltitudeType.MSL);
    }

    /**
     * Override this to get timer updates
     * @param timestep_sec current timestep in seconds.
     * @param simtime_sec current simulation time in seconds.
     */
    public void step(double timestep_sec, double simtime_sec) {
    }
    
    /** Override this to perform logic whenever the simulation pauses. */
    public void pause(){
    }

    /** Override this to perform logic when the module is destroyed.  This occurs when a 
     *  new scenario is loaded.
     */
    public void shutdown() {
    }

    public final void initialize(EntityModel parentModel, Element xmlElement) {
        this.model = parentModel;
        this.data = parentModel.getData();
        parentModel.getEventManger().addListener(appListener);
        initialize(xmlElement);
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */