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
import avtas.xml.ui.XMLTree;
import avtas.xml.ui.XMLValueEditor;
import avtas.xml.XmlNode;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author AFRL/RQQD
 */
public class XMLTreePlugin extends AmasePlugin {

    boolean loadingFromThis = false;
    boolean edited = false;
    XMLTree tree = new XMLTree();
    XMLValueEditor valEditor = new XMLValueEditor(null);
    private AppEventManager eventManager = AppEventManager.getDefaultEventManager();
    private File scenarioFile = null;
    boolean externalSelection = false;

    public XMLTreePlugin() {

        tree.getModel().addTreeModelListener(new TreeModelListener() {

            @Override
            public void treeNodesChanged(TreeModelEvent e) {
                updateDataManager();
            }

            @Override
            public void treeNodesInserted(TreeModelEvent e) {
                updateDataManager();
            }

            @Override
            public void treeNodesRemoved(TreeModelEvent e) {
                updateDataManager();
            }

            @Override
            public void treeStructureChanged(TreeModelEvent e) {
                updateDataManager();
            }
        });

        valEditor.addPropertyChangeListener(XMLValueEditor.NODE_UPDATED, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                XMLTree.AbstractXMLNode node = (XMLTree.AbstractXMLNode) tree.getSelectionPath().getLastPathComponent();
                node.setXML(valEditor.getContent());
                ((DefaultTreeModel) tree.getModel()).reload(node);
                updateDataManager();
            }
        });

        tree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                valEditor.setContent(tree.getNode(e.getPath()));
                if (!externalSelection && !loadingFromThis) {
                    if (eventManager != null) {
                        eventManager.fireEvent(new SelectObjectEvent(tree.getNode(e.getPath())), XMLTreePlugin.this);
                    }
                }
                externalSelection = false;
                loadingFromThis = false;
            }
        });
    }

    @Override
    public Component getGui() {

        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        sp.setTopComponent(new JScrollPane(tree));
        sp.setBottomComponent(valEditor);
        sp.setResizeWeight(0.6);
        sp.setDividerLocation(0.6);

        return sp;
    }

    private void updateDataManager() {
        if (!loadingFromThis) {
            loadingFromThis = true;
            if (eventManager != null) {
                eventManager.fireEvent(new ScenarioEvent(scenarioFile, tree.getRootNode()), XMLTreePlugin.this);
            }
            loadingFromThis = false;
        }
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof ScenarioEvent) {
            ScenarioEvent sce = (ScenarioEvent) event;
            if (!loadingFromThis) {
                loadingFromThis = true;
                tree.setRootNode(sce.getXML());
                this.scenarioFile = sce.getSourceFile();
                loadingFromThis = false;
            }
        }
        else if (event instanceof SelectObjectEvent) {
            Object obj = ((SelectObjectEvent) event).getObject();
            if (obj instanceof XmlNode) {
                externalSelection = true;
                TreePath path = tree.getPath((XmlNode) obj);
                if (path != null) {
                    tree.setSelectionPath(path);
                }
                else {
                    tree.clearSelection();
                }
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */