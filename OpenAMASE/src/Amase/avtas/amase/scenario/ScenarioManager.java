// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.scenario;

import afrl.cmasi.KeyValuePair;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import avtas.app.UserExceptions;
import avtas.amase.AmasePlugin;
import avtas.amase.window.AmaseWindow;
import avtas.app.AppEventManager;
import avtas.app.Context;
import avtas.lmcp.LMCPObject;
import avtas.util.WindowUtils;
import avtas.swing.UserNotice;
import java.io.File;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This class handles the reading of scenario files and Monitors the AMASE
 * scenario and provides easy access to scenario objects. An instance of this
 * class needs to be added to the {@link Context} in order to collect scenario
 * data. Other application components can get scenario data by accessing the
 * static methods.
 * <br/>
 * This class is meant to be a singleton in any simulation. Creating new
 * instances does not affect the underlying implementation, but an instance must
 * be created prior to any scenario events occurring.
 * <br/>
 *
 * @author AFRL/RQQD
 * @version 1.0
 */
public class ScenarioManager extends AmasePlugin {

    protected File src;
    protected AppEventManager eventManager;
    protected JFileChooser openChooser = null;
    static protected ScenarioManager instance = null;
    protected MessageManager messageManager = null;

    public ScenarioManager() {
        if (instance != null) {
            UserExceptions.showError(this, "Cannot create multiple scenario managers", null);
        }
        ScenarioManager.instance = this;
        instance.eventManager = AppEventManager.getDefaultEventManager();
        AppEventManager.getDefaultEventManager().addListener(this);

    }

    public ScenarioManager(AppEventManager mgr) {
        eventManager = mgr;
    }

    public static void initScenario(File file) {
        if (instance == null) {
            UserExceptions.showError(ScenarioManager.class, "Scenario Manager is not initialized.", null);
        }
        instance.src = file;
        ScenarioState.clearData();
        
        // call the garbage collector here to clear up unused objects in memory
        System.gc();

        Element element = ScenarioReader.readScenario(file, true, "AMASE/" + MessageManager.EVENTLIST_NAME);
        ScenarioState.setScenario(element, file);

        // initialize the event dispatching
        if (instance.messageManager == null) {
            instance.messageManager = new MessageManager();
        }
        instance.messageManager.resetReader(file);

        // tell the rest of the application about the new scenario
        ScenarioEvent scenEvent = new ScenarioEvent(file, element);
        instance.eventManager.fireEvent(scenEvent, instance);

        // publish a CMASI SessionStatus denoting set/reset of the scenario
        instance.publishResetMessage(scenEvent);

        // cue up the scenario by dispatching events that correspond to time = 0
        instance.step(0, 0);

    }

    protected void publishResetMessage(ScenarioEvent evt) {
        SessionStatus resetStatus = new SessionStatus();
        resetStatus.setState(SimulationStatusType.Reset);
        resetStatus.getParameters().add(new KeyValuePair("source", evt.getSourceFile().getPath()));

        // get the scenario start time (seconds since epoch)
        resetStatus.setStartTime( (long) (XMLUtil.getDouble(evt.getXML(), "ScenarioData/ScenarioStartTime", 0) * 1000) );
        resetStatus.setScenarioTime(resetStatus.getStartTime());

        eventManager.fireEvent(resetStatus, this);
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof SessionStatus) {
            SessionStatus ss = (SessionStatus) event;
            if (ss.getState() == SimulationStatusType.Reset) {
                File source = src;
                for (KeyValuePair kvp : ss.getParameters()) {
                    if (kvp.getKey().equals("Source")) {
                        source = new File(kvp.getValue());
                    }
                }
                if (source != null) {
                    initScenario(source);
                }
                return;
            }
        } 
        
        if (event instanceof LMCPObject) {
            ScenarioState.processLMCP((LMCPObject) event, ScenarioState.getTime());
        }
    }

    @Override
    public void step(double timestep, double sim_time) {
        if (messageManager != null && messageManager.hasMessages()) {
            LMCPObject obj;
            do {
                obj = messageManager.getNextEvent(sim_time);
                if (obj != null) {
                    ScenarioState.processLMCP(obj, sim_time);
                    eventManager.fireEvent(obj, this);
                }
            } while (obj != null);
        }
    }

    @Override
    public void getMenus(JMenuBar menubar) {

        final AmaseWindow parentFrame = (AmaseWindow) JOptionPane.getFrameForComponent(menubar);

        if (openChooser == null) {
            openChooser = new JFileChooser(".");
        }
        openChooser.addChoosableFileFilter(new FileNameExtensionFilter("AMASE Files", "xml", "zip"));
        openChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        openChooser.setFileFilter(openChooser.getChoosableFileFilters()[0]);
        openChooser.setMultiSelectionEnabled(false);

        JMenu fileMenu = WindowUtils.getMenu(menubar, "File");

        JMenuItem item = new JMenuItem("Open Scenario");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (openChooser.showOpenDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {
                    final File f = openChooser.getSelectedFile();
                    if (f != null) {
                        parentFrame.setTitle(parentFrame.getBaseTitle() + " - " + f.getName());
                        final UserNotice notice = new UserNotice("Loading Scenario.  Please Wait.", parentFrame);
                        notice.setVisible(true);
                        new Thread() {
                            @Override
                            public void run() {
                                ScenarioManager.initScenario(f);
                                notice.dispose();
                            }
                        }.start();

                    }
                }

            }
        });
        fileMenu.add(item);

        item = new JMenuItem(new AbstractAction("Use Empty Scenario") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File file = File.createTempFile("amase_blank_scenario", ".xml");
                    file.deleteOnExit();
                    Element el = new Element("AMASE");
                    el.toFile(file);
                    initScenario(file);
                } catch (IOException ex) {
                    UserExceptions.showError(instance, "Error creating scenario.", ex);
                }

            }
        });
        fileMenu.add(item);
    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
        src = ScenarioReader.getScenarioFile(cmdParams);
        if (src != null && src.exists()) {
            openChooser = new JFileChooser(src.getParentFile());
        }
    }

    @Override
    public void initializeComplete() {
        if (src != null) {
            initScenario(src);
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */