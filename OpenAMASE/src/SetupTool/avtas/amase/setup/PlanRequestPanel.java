// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.setup;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AutomationRequest;
import afrl.cmasi.RemoveEntities;
import afrl.cmasi.RemoveTasks;
import afrl.cmasi.RemoveZones;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import afrl.cmasi.Task;
import avtas.amase.objtree.ObjectTree;
import avtas.amase.scenario.ScenarioEvent;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import avtas.swing.CheckboxList;
import avtas.swing.CollapsableList;
import avtas.swing.PopupMenuAdapter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

/**
 *
 * @author AFRL/RQQD
 */
public class PlanRequestPanel extends JComponent implements AppEventListener {

    CollapsableList toplist = new CollapsableList();
    final CheckboxList<ElementContainer> aircraftList = new CheckboxList<ElementContainer>();
    final CheckboxList<ElementContainer> opRegionList = new CheckboxList<ElementContainer>(true);
    final CheckboxList<ElementContainer> taskList = new CheckboxList<ElementContainer>();
    final HashMap<String, AutomationRequest> existingPlanReqMap = new HashMap<String, AutomationRequest>();
    private AutomationRequest currentPlanRequest = new AutomationRequest();

    JButton createBut = new JButton("Create Request");
    JButton modifyBut = new JButton("Select All");
    JComboBox requestEditSelector = new JComboBox(new String[]{"Edit Existing Request"});
    AppEventManager eventMgr = AppEventManager.getDefaultEventManager();
    private double time = 0;

    public PlanRequestPanel() {
        Component uavCatList = toplist.add("Aircraft", aircraftList);
        Component taskCatList = toplist.add("Tasks", taskList);
        Component opRegionCatList = toplist.add("Operating Region", opRegionList);

        setLayout(new BorderLayout());
        add(new JScrollPane(toplist), BorderLayout.CENTER);

        setupContextMenu(uavCatList, aircraftList);
        setupContextMenu(taskCatList, taskList);
        setupContextMenu(opRegionList, opRegionList);

        setupButtons();
    }

    

    static void setupContextMenu(Component item, final CheckboxList list) {
        item.addMouseListener(new PopupMenuAdapter() {

            @Override
            public void setMenuContents(JPopupMenu menu, java.awt.Point p) {
                menu.add(new AbstractAction("Select All") {

                    public void actionPerformed(ActionEvent e) {
                        list.selectAll();
                    }
                });
                menu.add(new AbstractAction("Select None") {

                    public void actionPerformed(ActionEvent e) {
                        list.selectNone();
                    }
                });
            }
        });
    }

    public void addListItem(CheckboxList<ElementContainer> list, long id, String label) {
        List<ElementContainer> items = list.getAllItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id == id) {
                list.setItem(i, new ElementContainer(label, id));
                list.addSelection(i);
                return;
            }
        }
        list.addItem(new ElementContainer(label, id));
        list.addSelection(list.getLength() - 1);
    }

    public void removeListItem(CheckboxList<ElementContainer> list, long id) {
        for (int i = 0; i < list.getLength(); i++) {
            if (list.getItem(i).id == id) {
                list.removeItem(i);
            }
        }
    }

    void setupButtons() {

        JPanel buttonPanel = new JPanel(new GridLayout(0, 3));

        buttonPanel.add(new JButton(new AbstractAction("Select All") {

            @Override
            public void actionPerformed(ActionEvent e) {
                aircraftList.selectAll();
                taskList.selectAll();
            }
        }));
        buttonPanel.add(new JButton(new AbstractAction("Select None") {

            @Override
            public void actionPerformed(ActionEvent e) {
                aircraftList.selectNone();
                taskList.selectNone();
                opRegionList.selectNone();
            }
        }));


        JButton editButton = new JButton(new AbstractAction("Edit") {

            @Override
            public void actionPerformed(ActionEvent e) {
                fillPlanRequest(currentPlanRequest);
                AutomationRequest req = (AutomationRequest) ObjectTree.showEditWindow(currentPlanRequest,
                        PlanRequestPanel.this, "Edit Plan Request");
                if (req != null) {
                    currentPlanRequest = req;
                    setCheckboxes(req);
                }
            }
        });
        buttonPanel.add(editButton);

        JButton submitButton = new JButton(new AbstractAction("<html><B>Submit</B></html>") {

            @Override
            public void actionPerformed(ActionEvent e) {
                AutomationRequest pr = fillPlanRequest(currentPlanRequest.clone());
                if (eventMgr != null) {
                    eventMgr.fireEvent(pr, null);
                }
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(buttonPanel);
        controlPanel.add(submitButton);

        add(controlPanel, BorderLayout.SOUTH);

    }

    public AutomationRequest fillPlanRequest(AutomationRequest pr) {
        if (pr == null) {
            pr = new AutomationRequest();
        }
        else {
            pr.setOperatingRegion(0);
            pr.getEntityList().clear();
            pr.getTaskList().clear();
        }
        for (ElementContainer e : aircraftList.getSelectedValues()) {
            pr.getEntityList().add(e.id);
        }
        for (ElementContainer e : taskList.getSelectedValues()) {
            pr.getTaskList().add(e.id);
        }
        if (opRegionList.getSelectedValues().isEmpty()) {
            pr.setOperatingRegion(0);
        }
        else {
            pr.setOperatingRegion(opRegionList.getSelectedValues().get(0).id);
        }

        return pr;
    }

    void setCheckboxes(AutomationRequest pr) {
        this.currentPlanRequest = pr;
        if (pr == null) {
            return;
        }
        taskList.selectNone();
        for (long id : pr.getTaskList()) {
            taskList.selectItem(id);
        }
        for (long id : pr.getEntityList()) {
            aircraftList.selectItem(id);
        }
        for (ElementContainer e : opRegionList.getAllItems()) {
            if (e.id == pr.getOperatingRegion()) {
                opRegionList.selectItem(e);
            }
        }
    }

    public void clearAll() {
        aircraftList.clear();
        taskList.clear();
        opRegionList.clear();
        currentPlanRequest = new AutomationRequest();
        existingPlanReqMap.clear();
    }
    
    

    public void eventOccurred(Object event) {
        if (event instanceof AirVehicleConfiguration) {
            AirVehicleConfiguration avc = (AirVehicleConfiguration) event;
            addListItem(aircraftList, avc.getID(), avc.getLabel() + " (" + avc.getID() + ")");
        }
        else if (event instanceof Task) {
            Task task = (Task) event;
            addListItem(taskList, task.getTaskID(), task.getClass().getSimpleName() + " " + task.getTaskID());
        }
        else if (event instanceof RemoveTasks) {
            RemoveTasks rt = (RemoveTasks) event;
            for (long id : rt.getTaskList()) {
                removeListItem(taskList, id);
            }
        }
        else if (event instanceof RemoveZones) {
            RemoveZones rz = (RemoveZones) event;
            for (long id : rz.getZoneList()) {
                removeListItem(opRegionList, id);
            }
        }
        else if (event instanceof RemoveEntities) {
            RemoveEntities ra = (RemoveEntities) event;
            for (long id : ra.getEntityList()) {
                removeListItem(aircraftList, id);
            }
        }
        else if (event instanceof SessionStatus) {
            SessionStatus ss = (SessionStatus) event;
            this.time = ss.getScenarioTime() / 1000d;
            if (ss.getState() == SimulationStatusType.Reset) {
                clearAll();
            }
        }
        else if (event instanceof ScenarioEvent) {
            clearAll();
        }
        else if (event instanceof AutomationRequest) {
            AutomationRequest ar = (AutomationRequest) event;
            String tag = "Time = " + time;
            existingPlanReqMap.put(tag, ar);
            requestEditSelector.addItem(tag);
        }
    }

    static class ElementContainer {

        public long id;
        public String label;

        public ElementContainer(String label, long id) {
            this.id = id;
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */