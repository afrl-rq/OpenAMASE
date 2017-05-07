// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.entity;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.MissionCommand;
import afrl.cmasi.RemoveEntities;
import avtas.xml.XMLUtil;
import avtas.app.AppEventManager;
import avtas.amase.scenario.ScenarioEvent;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import afrl.cmasi.VehicleActionCommand;
import afrl.cmasi.WeatherReport;
import afrl.cmasi.EntityConfiguration;
import afrl.cmasi.EntityState;
import avtas.app.UserExceptions;
import avtas.amase.AmasePlugin;
import avtas.app.Context;
import avtas.app.SettingsManager;
import avtas.util.ReflectionUtils;
import avtas.util.WindowUtils;
import java.util.ArrayList;
import java.util.List;
import avtas.xml.Element;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * Central control for driving entities in the simulation. This maintains a list
 * of {@link EntityModel entity models} and informs the models of new events in
 * the system. A new model is created when EntityControl is informed of an
 * {@link EntityConfiguration} or {@link AirVehicleConfiguration} message that
 * has a new EntityID.
 * <p>
 * EntityControl is the top-level object that is added to the simulation context
 * in the simulation configuration. There should only be one EntityControl
 * instance in an application. An exception will be thrown if more than one
 * instance is added to the context.
 *
 * @author AFRL/RQQD
 */
public class EntityControl extends AmasePlugin {

    private final List<EntityModel> entityModels = new ArrayList<>();
    private AppEventManager mgr;
    private WeatherReport weatherReport = new WeatherReport();
    List<Element> configSetups = new ArrayList<>();
    List<Element> scenarioSetups = new ArrayList<>();
    List<Long> scenarioExclusionList = new ArrayList<>();
    List<Long> configExclusionList = new ArrayList<>();
    protected static EntityControl instance = null;
    protected static final String config_file = "EntityControl.xml";

    public EntityControl() {
        if (instance != null) {
            UserExceptions.showError(this, "Trying to create two instances of " + getClass().getName(), null);
        }
        else {
            instance = this;
            mgr = AppEventManager.getDefaultEventManager();

            // try to load data from the config file, if it exists.
            Element el = SettingsManager.getAsXml(config_file);
            if (el != null) {
                configSetups.addAll(el.getChildElements());
                this.configExclusionList = getExclusions(el);
            }

        }
        mgr = AppEventManager.getDefaultEventManager();
    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
        // load config data from the XML section under entity control.
        configSetups.addAll(xml.getChildElements());
        this.configExclusionList.addAll(getExclusions(xml));

    }

    /**
     * Called by the {@link #eventOccurred(Object)} method to
     * initialize a scenario.
     *
     *
     * @param evt
     */
    public void initScenario(ScenarioEvent evt) {

        // inform each model of a shutdown
        for (EntityModel em : entityModels) {
            em.shutdown();
        }
        // always clear the list when initialized
        entityModels.clear();

        scenarioSetups.clear();
        Element entityEl = evt.getXML().getChild("EntityControl");
        if (entityEl != null) {
            scenarioSetups.addAll(entityEl.getChildElements());
            this.scenarioExclusionList = getExclusions(entityEl);
        }

    }

    List<Long> getExclusions(Element sourceEl) {
        List<Long> idList = new ArrayList<>();
        for (Element el : XMLUtil.getChildren(sourceEl, "ExclusionList/ID")) {
            idList.add(Long.valueOf(el.getText()));
        }
        return idList;
    }

    /**
     * Loads an EntityModel for an Aircraft. First, this searches for a module
     * set corresponding to the aircraft id. If none is found, the
     * DefaultAircraft module list is loaded.
     *
     * @param aircraft
     * @return a new EntityModel or null if this aircraft is set to be excluded
     * from EntityControl.
     */
    protected EntityModel createEntityModel(AirVehicleConfiguration aircraft) {

        if (configExclusionList.contains(aircraft.getID()) || scenarioExclusionList.contains(aircraft.getID())) {
            return null;
        }

        // create the model and add it to the model list
        EntityModel model = new EntityModel(aircraft);
        model.setEventManager(mgr);
        entityModels.add(model);
        loadModules(model, "");

        model.fireModelEvent(aircraft);
        if (weatherReport != null) {
            model.fireModelEvent(weatherReport);
        }

        return model;
    }

    /**
     * Loads an EntityModel for an entity. First, this searches for a module set
     * corresponding to the entity id, then the entity type. If none is found,
     * the DefaultEntity module list is loaded.
     *
     * @param entity
     * @return a new EntityModel or null if this entity is set to be excluded
     * from EntityControl.
     */
    protected EntityModel createEntityModel(EntityConfiguration entity) {
        if (configExclusionList.contains(entity.getID()) || scenarioExclusionList.contains(entity.getID())) {
            return null;
        }

        // create the model and add it to the model list
        EntityModel model = new EntityModel(entity);
        model.setEventManager(mgr);
        entityModels.add(model);
        loadModules(model, entity.getEntityType());

        model.fireModelEvent(entity);
        if (weatherReport != null) {
            model.fireModelEvent(weatherReport);
        }

        return model;
    }

    /**
     * Loads modules for the given model. The entity model is configured using
     * data that was set in the Configuration or the scenario file. The order of
     * precedence is as follows:
     * <ol>
     * <li>ID. The unique ID for the entity/aircraft</li>
     * <li>Type. The type of entity (not supported for aircraft) </li>
     * <li>Class. The full class name for the configuration used to create the
     * entity/aircraft</li>
     * </ol>
     * If one of the above is not found, then the
     * <code>DefaultAircraft</code> or
     * <code>DefaultEntity</code> sections are used.
     */
    protected EntityModel loadModules(EntityModel model, String type) {


        // temporarily consolidate the entity setups into a single list
        List<Element> entitySetups = new ArrayList<>();
        if (scenarioSetups != null) {
            entitySetups.addAll(scenarioSetups);
        }
        if (configSetups != null) {
            entitySetups.addAll(configSetups);
        }

        //iterate through the list, checking for ID
        for (Element el : entitySetups) {
            if (XMLUtil.getLongAttr(el, "ID", -1) == model.getID()) {
                loadModules(el, model);
                return model;
            }
        }
        // then check for the "type" of entity
        if (type != null && !type.isEmpty()) {
            for (Element el : entitySetups) {
                if (XMLUtil.getAttr(el, "Type", "").equals(type)) {
                    loadModules(el, model);
                    return model;
                }
            }
        }
        // then check for "Class" of entity.  This is the java classpath of the class that represents the entity
        String classpath = model.getConfiguration().getClass().getName();
        for (Element el : entitySetups) {
            if (XMLUtil.getAttr(el, "Class", "").equals(classpath)) {
                loadModules(el, model);
                return model;
            }
        }

        // look for a "DefaultAircraft" node if this is an aircraft
        if (model.getConfiguration() instanceof AirVehicleConfiguration) {
            for (Element el : entitySetups) {
                if (el.getName().equals("DefaultAircraft")) {
                    loadModules(el, model);
                    return model;
                }
            }
        }
        // look for a "DefaultEntity" node if this is an entity
        else if (model.getConfiguration() instanceof EntityConfiguration) {
            for (Element el : entitySetups) {
                if (el.getName().equals("DefaultEntity")) {
                    loadModules(el, model);
                    return model;
                }
            }
        }


        return model;
    }

    /**
     * creates and load the specified modules into an entity model
     */
    void loadModules(Element setupEl, EntityModel model) {
        for (Element moduleEl : XMLUtil.getChildren(setupEl, "Module")) {
            try {
                Object module = ReflectionUtils.createInstance(XMLUtil.getAttr(moduleEl, "Class", ""));
                if (module instanceof EntityModule) {
                    model.moduleList.add((EntityModule) module);
                    ((EntityModule) module).initialize(model, moduleEl);
                }
            } catch (Exception ex) {
                UserExceptions.showError(this, "Could not create module", ex);
            }
        }
    }

    /**
     * Processes events dispatched by the application. Currently, this means
     * ScenarioEvents and LMCP objects.
     *
     * @param evt the event being dispatched.
     */
    @Override
    public void eventOccurred(Object evt) {
        if (evt instanceof ScenarioEvent) {
            initScenario((ScenarioEvent) evt);
        }
        else if (evt instanceof SessionStatus) {
            SessionStatus ss = (SessionStatus) evt;
            if (ss.getState() == SimulationStatusType.Running) {
                sendStates();
            }

        }
        else if (evt instanceof EntityConfiguration) {
            EntityConfiguration ec = (EntityConfiguration) evt;
            EntityModel m = getModel(ec.getID());
            if (m == null) {
                createEntityModel(ec);
            }

        }
        else if (evt instanceof AirVehicleConfiguration) {
            AirVehicleConfiguration avc = (AirVehicleConfiguration) evt;
            EntityModel m = getModel(avc.getID());
            if (m == null) {
                createEntityModel(avc);
            }
        }
        else if (evt instanceof EntityState) {
            EntityState es = (EntityState) evt;
            EntityModel m = getModel(es.getID());
            if (m != null) {
                m.setInitialState(es);
            }
        }
        else if (evt instanceof AirVehicleState) {
            AirVehicleState avs = (AirVehicleState) evt;
            EntityModel m = getModel(avs.getID());
            if (m != null) {
                m.setInitialState(avs);
            }
        }
        else if (evt instanceof VehicleActionCommand) {
            VehicleActionCommand vac = (VehicleActionCommand) evt;
            EntityModel m = getModel(vac.getVehicleID());
            if (m != null) {
                m.fireModelEvent(evt);
            }
        }
        else if (evt instanceof MissionCommand) {
            MissionCommand vac = (MissionCommand) evt;
            EntityModel m = getModel(vac.getVehicleID());
            if (m != null) {
                m.fireModelEvent(evt);
            }
        }
        else if (evt instanceof RemoveEntities) {
            RemoveEntities rm = (RemoveEntities) evt;
            for (long id : rm.getEntityList()) {
                EntityModel m = getModel(id);
                if (m != null) {
                    m.shutdown();
                    entityModels.remove(m);
                }
            }
        }
        else if (evt instanceof WeatherReport) {
            this.weatherReport = (WeatherReport) evt;
            for (EntityModel m : entityModels) {
                m.fireModelEvent(evt);
            }
        }
    }

    protected EntityModel getModel(long id) {
        // avoided shorthand for loop to protect against ConcurrentModificationException
        for (int i = 0; i < entityModels.size(); ++i) {
            if (entityModels.get(i).getID() == id) {
                return entityModels.get(i);
            }
        }
        return null;
    }

    /**
     * Sends a copy of the current AirVehicleState or EntityState for each
     * aircraft/entity to the rest of the application. Called whenever a
     * {@link SessionStatus} message is received.
     *
     */
    public void sendStates() {
        if (mgr == null) {
            return;
        }

        // avoided shorthand for loop to protect against ConcurrentModificationException
        for (int i = 0; i < entityModels.size(); ++i) {
            EntityState state = entityModels.get(i).getState();
            if (state != null)
                mgr.fireEvent(state, this);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void step(double timestep_sec, double simTime_sec) {
        // avoided shorthand for loop to protect against ConcurrentModificationException
        for (int i = 0; i < entityModels.size(); ++i) {
            entityModels.get(i).update(timestep_sec, simTime_sec);
        }
    }

    @Override
    public void timerStateChanged(TimerState state, double sim_time) {
        if (state == TimerState.Paused) {
            for (int i = 0; i < entityModels.size(); ++i) {
                entityModels.get(i).pause();
            }
        }
    }

    @Override
    public void getMenus(final JMenuBar menubar) {
        JMenu menu = WindowUtils.getMenu(menubar, "Simulation");
        JMenu mfMenu = new JMenu("Entity Control");
        menu.add(mfMenu);

        final JMenu monitorMenu = new JMenu("View Data");
        mfMenu.add(monitorMenu);
        monitorMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                for (final EntityModel m : entityModels) {
                    monitorMenu.add(new AbstractAction("Entity " + m.getID()) {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JFrame d = new JFrame();
                            d.setTitle("Entity " + m.getID() + " Data");
                            d.setSize(480, 640);
                            d.add(new EntityDataViewer(m.getData()));
                            d.setResizable(true);
                            d.setLocationRelativeTo(JOptionPane.getFrameForComponent(menubar));
                            d.setVisible(true);
                        }
                    });
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                monitorMenu.removeAll();
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                monitorMenu.removeAll();
            }
        });

    }
    
    /**
     * Returns the {@link EntityData} associated with the given entity ID.  Returns
     * null if no entity data is found for the requested entity.  This will return
     * null if the EntityControl plugin is not loaded into the application or 
     * the entity is not controlled via EntityControl.
     * @param entityId ID of requested entity
     * @return {@link EntityData} associated with the entity (can be null) 
     */
    public static EntityData getData(long entityId) {
        if (EntityControl.instance != null) {
            EntityModel model = instance.getModel(entityId);
            if (model != null) {
                return model.getData();
            }
        }
        return null;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */