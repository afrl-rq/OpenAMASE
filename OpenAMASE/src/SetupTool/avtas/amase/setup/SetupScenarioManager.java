// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.setup;

import avtas.amase.AmasePlugin;
import avtas.amase.scenario.ScenarioEvent;
import avtas.amase.scenario.ScenarioReader;
import avtas.amase.scenario.ScenarioState;
import avtas.amase.window.WindowService;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import avtas.app.Application;
import avtas.app.Context;
import avtas.lmcp.LMCPObject;
import avtas.util.WindowUtils;
import avtas.xml.Element;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Loads scenario files into the setup tool.
 *
 * @author AFRL/RQQD
 */
public class SetupScenarioManager extends AmasePlugin {

    //Element scenarioElement = null;
    AppEventManager eventManager = AppEventManager.getDefaultEventManager();
    //File scenarioFile = null;
    private Object currentScenEvent = null;
    public static final String TOP_LEVEL_NAME = "AMASE";
    public static final String EVENTLIST_NAME = "ScenarioEventList";
    // arguments used to create new instances of the setup tool
    private String[] args = null;
    private JFileChooser chooser = new JFileChooser(".");
    Date lastChange = new Date(0);
    Date lastSave = new Date(0);
    JCheckBox expandLinksCheckBox = new JCheckBox("Expand Linked Files");

    public SetupScenarioManager() {
        ScenarioState.setScenario(new Element(TOP_LEVEL_NAME), null);

        currentScenEvent = new ScenarioEvent(ScenarioState.getSourceFile(), ScenarioState.getScenario());

        expandLinksCheckBox.setToolTipText("Integrates linked content into the scenario and cannot be undone.");

        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Scenario Files", "xml", "XML"));
        chooser.setAccessory(expandLinksCheckBox);
    }

    /**
     * loads a new scenario file. If overwrite is true, then the existing
     * scenario information is removed.
     *
     * @param file file to load
     * @param loadLinkedFiles true if file links should be expanded
     */
    void loadScenario(File file, boolean loadLinkedFiles) {
        try {
            Element newEl = ScenarioReader.readScenario(file, loadLinkedFiles);

            ScenarioState.setScenario(newEl, file);
            eventManager.fireEvent(new ScenarioEvent(file, newEl), this);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            Logger.getLogger(SetupScenarioManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void clearAll(JFrame parentFrame) {
        Element scenElement = new Element(TOP_LEVEL_NAME);
        scenElement.addElement(EVENTLIST_NAME);
        this.currentScenEvent = new ScenarioEvent(null, scenElement);
        lastChange = new Date(0);
        lastSave = new Date(0);
        parentFrame.setTitle(parentFrame.getTitle().split(" - ")[0]);
        ScenarioState.setScenario(scenElement, null);
        if (eventManager != null) {
            eventManager.fireEvent(currentScenEvent, this);
        }
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof ScenarioEvent) {
            ScenarioEvent scenEvent = (ScenarioEvent) event;
            ScenarioState.setScenario(scenEvent.getXML(), scenEvent.getSourceFile());
            this.currentScenEvent = scenEvent;
            lastChange = new Date();
        }
    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
        this.args = cmdParams;
    }

    @Override
    public void applicationPeerAdded(Object peer) {
        if (peer instanceof AppEventListener && currentScenEvent != null) {
            ((AppEventListener) peer).eventOccurred(currentScenEvent);
        }
    }

    @Override
    public void initializeComplete() {
        File scenarioFile = ScenarioReader.getScenarioFile(args);
        if (scenarioFile != null) {
            loadScenario(scenarioFile, false);
        }
    }

    @Override
    public void getMenus(JMenuBar menubar) {

        final JFrame parentFrame = (JFrame) JOptionPane.getFrameForComponent(menubar);

//        parentFrame.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                checkQuit(parentFrame);
//            }
//        });

        JMenu fileMenu = WindowUtils.getMenu(menubar, "File");

        JMenuItem newWinItem = new JMenuItem("New Window");
        fileMenu.add(newWinItem);
        newWinItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (args != null) {
                    Application.createNewProcess(args);
                    //WINDOW_COUNT++;
                }
            }
        });

        JMenuItem newItem = new JMenuItem("New Scenario");
        fileMenu.add(newItem);
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (okToClear(parentFrame)) {
                    clearAll(parentFrame);
                }
            }
        });

        final JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save(parentFrame);
            }
        });
        fileMenu.add(saveItem);

        JMenuItem item = new JMenuItem("Save As");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File oldFile = ScenarioState.getSourceFile();
                ScenarioState.setSourceFile(null);
                if (!save(parentFrame)) {
                    ScenarioState.setSourceFile(oldFile);
                }
            }
        });
        fileMenu.add(item);

        // saves a copy of the current scenario without changing the current file
        item = new JMenuItem("Save A Copy..");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ans = chooser.showSaveDialog(parentFrame);
                if (ans == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    if (!file.getName().toLowerCase().endsWith(".xml")) {
                        file = new File(file.getAbsolutePath() + ".xml");
                    }
                    ScenarioState.getScenario().toFile(file);
                }

            }
        });
        fileMenu.add(item);

        item = new JMenuItem("Open Scenario");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (okToClear(parentFrame)) {
                    int ans = chooser.showOpenDialog(parentFrame);
                    if (ans == JFileChooser.APPROVE_OPTION) {
                        File f = chooser.getSelectedFile();
                        String[] titlesplit = parentFrame.getTitle().split(" - ");
                        parentFrame.setTitle(titlesplit[0] + " - " + f.getAbsolutePath());
                        loadScenario(f, expandLinksCheckBox.isSelected());
                    }
                }
            }
        });
        fileMenu.add(item);
    }

    boolean save(JFrame parentFrame) {
        if (ScenarioState.getSourceFile() == null) {
            int ans = chooser.showSaveDialog(parentFrame);
            if (ans == JFileChooser.APPROVE_OPTION) {
                ScenarioState.setSourceFile(chooser.getSelectedFile());
                if (!ScenarioState.getSourceFile().getName().toLowerCase().endsWith(".xml")) {
                    ScenarioState.setSourceFile( new File(ScenarioState.getSourceFile().getAbsolutePath() + ".xml") );
                }
            }
        }
        if (ScenarioState.getSourceFile() != null) {
            ScenarioState.getScenario().toFile(ScenarioState.getSourceFile());
            lastSave = new Date();
            String[] titlesplit = parentFrame.getTitle().split(" - ");
            parentFrame.setTitle(titlesplit[0] + " - " + ScenarioState.getSourceFile().getAbsolutePath());
            eventManager.fireEvent(new ScenarioEvent(ScenarioState.getSourceFile(), ScenarioState.getScenario()), this);
            return true;
        }
        return false;
    }

    boolean okToClear(JFrame parentFrame) {
        if (lastChange.after(lastSave)) {
            int ans = JOptionPane.showConfirmDialog(parentFrame,
                    "This will delete any changes made.", "Confirm Change",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            return ans == JOptionPane.OK_OPTION;
        }
        return true;
    }

    @Override
    public boolean requestShutdown() {

        if (lastChange.after(lastSave)) {
            int ans = JOptionPane.showConfirmDialog(WindowService.getMainWindow(),
                    "Save Changes Before Quitting?", "Confirm Quit",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ans == JOptionPane.YES_OPTION) {
                if (save(WindowService.getMainWindow())) {
                    return true;
                }

            }
            if (ans == JOptionPane.NO_OPTION) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }

    void checkQuit(JFrame parentFrame) {
        parentFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */