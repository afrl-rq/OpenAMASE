// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.setup;

import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import avtas.amase.AmasePlugin;
import avtas.amase.objtree.LmcpObjectActions;
import avtas.amase.objtree.ObjectTree;
import avtas.app.AppEventManager;
import avtas.lmcp.LMCPEnum;
import avtas.lmcp.LMCPFactory;
import avtas.lmcp.LMCPObject;
import avtas.swing.PopupMenuAdapter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/**
 *
 * @author AFRL/RQQD
 */
public class EventEditorPlugin extends AmasePlugin {

    JPanel topPanel = new JPanel();
    JSplitPane splitpane;
    EventListEditor listEditor;
    JComboBox combobox;
    ObjectTree tree = new ObjectTree();
    boolean selfUpdating = false;

    public EventEditorPlugin() {

        setPluginName("Event Editor");

        splitpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitpane.setDividerLocation(0.5);
        splitpane.setResizeWeight(0.5);

        listEditor = new EventListEditor();

        splitpane.setTopComponent(listEditor.panel);

        setupEditor();
        splitpane.setBottomComponent(new JScrollPane(tree));

        topPanel.setLayout(new BorderLayout(5, 5));
        topPanel.add(splitpane, BorderLayout.CENTER);

        setupCombobox();
        topPanel.add(combobox, BorderLayout.SOUTH);

        topPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

    }

    @Override
    public Component getGui() {
        return topPanel;
    }



    /**
     * sets up a combobox for creating new events based on the current library
     * of LMCP objects
     */
    void setupCombobox() {
        combobox = new JComboBox();
        combobox.addItem("Add an Event");
        for (LMCPEnum e : LMCPFactory.getLoadedSeries()) {
            combobox.addItem(e.getSeriesName());
            for (String type : e.getAllTypes()) {
                combobox.addItem(e.getInstance(e.getType(type)));
            }
        }
        combobox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (combobox.getSelectedItem() instanceof LMCPObject) {
                    Object o = ((LMCPObject) combobox.getSelectedItem()).clone();
                    AppEventManager.getDefaultEventManager().fireEvent(o);
                    AppEventManager.getDefaultEventManager().fireEvent(new SelectObjectEvent(o));
                }
                combobox.setSelectedIndex(0);
            }
        });

        combobox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof LMCPObject) {
                    setText("    " + ((LMCPObject) value).getLMCPTypeName());
                }
                else {
                    setText(" " + String.valueOf(value));
                    setFont(getFont().deriveFont(Font.BOLD));
                }
                return this;
            }
        });
    }

    void setupEditor() {
        tree.addMouseListener(new PopupMenuAdapter() {
            @Override
            public void setMenuContents(JPopupMenu menu, java.awt.Point p) {
                menu.add(new AbstractAction("Refresh") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setObject(tree.getObject());
                    }
                });
            }
        });


        // allow for copy and paste of LMCP objects
        LmcpObjectActions.addActions(tree);
    }

    void fireUpdatedObject() {
        selfUpdating = true;
        if (tree.getObject() != null) {
            Object obj = tree.getObject();
            AppEventManager.getDefaultEventManager().fireEvent(obj, this);
        }
        selfUpdating = false;
    }

    void setObject(Object obj) {
        selfUpdating = true;
        tree.setObject(obj);
        addListener();
        selfUpdating = false;
    }

    void addListener() {
        tree.getTreeTableModel().addTreeModelListener(new TreeModelListener() {
            @Override
            public void treeNodesChanged(TreeModelEvent e) {
                treeChanged();
            }

            @Override
            public void treeNodesInserted(TreeModelEvent e) {
                treeChanged();
            }

            @Override
            public void treeNodesRemoved(TreeModelEvent e) {
                treeChanged();
            }

            @Override
            public void treeStructureChanged(TreeModelEvent e) {
                treeChanged();
            }

            void treeChanged() {
                if (!selfUpdating)
                    fireUpdatedObject();
            }
        });
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof SessionStatus) {
            if (((SessionStatus) event).getState() == SimulationStatusType.Reset) {
                setObject(null);
            }
        }
        else if (event instanceof SelectObjectEvent && !selfUpdating) {
            Object obj = ((SelectObjectEvent) event).getObject();
            if (obj instanceof LMCPObject) {
                setObject(obj);
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */