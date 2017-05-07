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
import avtas.app.AppEventManager;
import avtas.xml.ui.XMLEditPane;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import avtas.xml.Element;
import avtas.xml.XmlNode;

/**
 *
 * @author AFRL/RQQD
 */
public class XMLEditorPlugin extends AmasePlugin {

    boolean loadingFromThis = false;
    boolean edited = false;
    XMLEditPane editPane = new XMLEditPane();
    JButton updateButton;
    JButton revertButton;
    Element origXMLElement = null;
    private AppEventManager eventManager = AppEventManager.getDefaultEventManager();
    private File sourceFile = null;

    public XMLEditorPlugin() {

        setPluginName("XML View");

        editPane.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                edited = true;
                updateButton.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                edited = true;
                updateButton.setEnabled(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                edited = true;
                updateButton.setEnabled(true);
            }
        });

        updateButton = new JButton(new AbstractAction("Update Scenario") {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendScenarioUpdate();
            }
        });

        revertButton = new JButton(new AbstractAction("Revert Scenario") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkEdit() && origXMLElement != null) {
                    editPane.setXML(origXMLElement);
                    edited = false;
                }
            }
        });

    }

    @Override
    public Component getGui() {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.add(new JScrollPane(editPane), BorderLayout.CENTER);

        JPanel butPanel = new JPanel();
        butPanel.add(updateButton);
        butPanel.add(revertButton);
        panel.add(butPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void sendScenarioUpdate() {
        if (!loadingFromThis) {
            loadingFromThis = true;
            if (eventManager != null) {
                origXMLElement = editPane.getUpdatedXML();
                if (origXMLElement != null) {
                    eventManager.fireEvent(new ScenarioEvent(sourceFile, origXMLElement), this);
                    edited = false;
                    updateButton.setEnabled(false);
                }
            }
            loadingFromThis = false;
        }
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof ScenarioEvent) {
            ScenarioEvent sce = (ScenarioEvent) event;
            if (!loadingFromThis) {
                if (checkEdit()) {
                    int caretPos = editPane.getCaretPosition();
                    loadingFromThis = true;
                    origXMLElement = sce.getXML();
                    this.sourceFile = sce.getSourceFile();
                    editPane.setXML(origXMLElement);
                    edited = false;
                    updateButton.setEnabled(false);
                    if (caretPos <= editPane.getText().length()) {
                        editPane.setCaretPosition(caretPos);
                    }
                    loadingFromThis = false;


                }
            }
        } else if (event instanceof SelectObjectEvent) {
            SelectObjectEvent soe = (SelectObjectEvent) event;
            if (soe.getObject() instanceof XmlNode) {
                editPane.goTo((XmlNode) soe.getObject());
            }
        }
    }

    boolean checkEdit() {
        int ans = JOptionPane.YES_OPTION;
        if (edited) {
            editPane.requestFocusInWindow();
            ans = JOptionPane.showConfirmDialog(editPane, "XML has been edited manually.  Do you wish to overwrite changes?");
        }
        return ans == JOptionPane.YES_OPTION;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */