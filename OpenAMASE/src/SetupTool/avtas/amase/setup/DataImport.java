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
import avtas.app.AppEventManager;
import avtas.util.WindowUtils;
import avtas.xml.ui.XMLTree;
import avtas.xml.Element;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author AFRL/RQQD
 */
public class DataImport extends AmasePlugin {

    private AppEventManager eventManager = null;
    private ScenarioEvent currentScenario = null;
    private JPanel importPanel = new JPanel();
    private JFileChooser chooser;
    private XMLTree currentScenTree;
    private JFrame parentFrame = null;

    public DataImport() {
        currentScenTree = new XMLTree();
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Scenario Files", "xml", "XML"));
        eventManager = AppEventManager.getDefaultEventManager();
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof ScenarioEvent) {
            this.currentScenario = (ScenarioEvent) event;
        }
    }

    public File showOpenWindow() {
        int ans = chooser.showOpenDialog(importPanel);
        if (ans == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    public void showImportWindow() {
        if (currentScenario == null) {
            JOptionPane.showMessageDialog(parentFrame, "Can't Import Data");
            return;
        }
        
        currentScenTree.setRootNode(currentScenario.getXML().clone());

        JPanel currentScenPanel = new JPanel(new BorderLayout());
        currentScenPanel.setBorder(new TitledBorder("Current Scenario"));
        currentScenPanel.add(new JScrollPane(currentScenTree));

        final JTabbedPane importTabPane = new JTabbedPane();

        JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(importTabPane);
        splitPane.setRightComponent(currentScenPanel);
        splitPane.setDividerLocation(0.5);
        splitPane.setResizeWeight(0.5);
        splitPane.setPreferredSize(new Dimension(800, 600));
        
        
        final JDialog dialog = new JDialog(parentFrame, "Import Data");

        JPanel okCancelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));        

        okCancelPanel.add(new JButton(new AbstractAction("OK") {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (eventManager != null) {
                    ScenarioEvent scenEvent = new ScenarioEvent(currentScenario.getSourceFile(), currentScenTree.getRootNode());
                    eventManager.fireEvent(scenEvent, null);
                }
                dialog.dispose();
            }
        }));
        okCancelPanel.add(new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        }));
        
        okCancelPanel.add(new JButton(new AbstractAction("Open File") {

            @Override
            public void actionPerformed(ActionEvent e) {
                File f = showOpenWindow();
                if (f != null) {
                    XMLTree importTree = new XMLTree();
                    importTree.setEditable(false);
                    Element el = ScenarioReader.readScenario(f, false);
                    if (el != null) {
                        importTree.setRootNode(el);
                        importTabPane.addTab(f.getName(), null, new JScrollPane(importTree), f.getAbsolutePath());
                    }
                }
            }
        }));

        dialog.add(splitPane, BorderLayout.CENTER);
        dialog.add(okCancelPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);

    }

    @Override
    public void getMenus(JMenuBar menubar) {

        JMenu fileMenu = WindowUtils.getMenu(menubar, "File" );
        if (fileMenu == null) {
            fileMenu = new JMenu("File");
        }
        JMenuItem item = new JMenuItem("Import Data");
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showImportWindow();
            }
        });
        fileMenu.add(item);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */