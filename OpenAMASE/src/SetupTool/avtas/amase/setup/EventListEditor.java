// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.setup;

import avtas.amase.objtree.ObjectTree;
import avtas.amase.scenario.ScenarioEvent;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import avtas.xml.XMLUtil;
import avtas.lmcp.LMCPObject;
import avtas.lmcp.LMCPXMLReader;
import avtas.swing.PopupMenuAdapter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import avtas.xml.Element;
import avtas.xml.XmlNode;
import avtas.xml.XmlReader;
import avtas.xml.XmlWriter;

/**
 *
 * @author AFRL/RQQD
 */
public class EventListEditor implements AppEventListener {

    private Element eventElement = null;
    private ScenarioEvent scenarioEvent = null;
    private JList msgListGUI = new JList();
    private List<Element> selectedEvents = new ArrayList<>();
    private AppEventManager eventManager = AppEventManager.getDefaultEventManager();
    private boolean externalSelection = false;

    JPanel panel = new JPanel();

    public EventListEditor() {

        AppEventManager.getDefaultEventManager().addListener(this);

        panel.setLayout(new BorderLayout());

        msgListGUI.setCellRenderer(new EventListRenderer());
        msgListGUI.setFixedCellWidth(150);
        msgListGUI.setFixedCellHeight(50);
        // this forces a wider initial view
        msgListGUI.setPrototypeCellValue("A message name that is long");

        // event list gui setup
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(new JScrollPane(msgListGUI), BorderLayout.CENTER);

        panel.add(new JScrollPane(listPanel), BorderLayout.CENTER);

        // setup user functionality
        setupRightClick();
        setupDragging();
        setupHotkeys();

        // add a selection listener to publish selection events
        msgListGUI.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedEvents = (List<Element>) msgListGUI.getSelectedValuesList();
                if (!externalSelection) {
                    Object o = msgListGUI.getSelectedValue();
                    if (o != null) {
                        eventManager.fireEvent(new SelectObjectEvent(o), this);
                    }
                }
                externalSelection = false;
            }
        });

        refreshList();
    }

    public void setupHotkeys() {
        msgListGUI.registerKeyboardAction(deleteAction, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_FOCUSED);
    }

    public void setupRightClick() {
        msgListGUI.addMouseListener(new PopupMenuAdapter() {

            @Override
            public void setMenuContents(JPopupMenu menu, java.awt.Point p) {
                int index = msgListGUI.locationToIndex(p);
                Rectangle r = msgListGUI.getCellBounds(index, index);
                if (index != -1) {
                    if (!msgListGUI.isSelectedIndex(index)) {
                        msgListGUI.setSelectedIndex(index);
                    }

                }

                if (r != null && r.contains(p)) {
                    menu.add(setTimeAction);

                    menu.add(deleteAction);
                    menu.add(cutAction);
                    menu.add(copyAction);
                    menu.add(editAction);
                }
                if (!XmlTransferUtils.getObjectsFromClipboard().isEmpty()) {
                    menu.add(pasteAction);
                }

            }
        });
    }

    void refreshList() {

        Object selectedEvent = msgListGUI.getSelectedValue();

        eventElement = null;
        msgListGUI.updateUI();
        msgListGUI.clearSelection();

        if (scenarioEvent == null) {
            scenarioEvent = new ScenarioEvent(null, new Element(SetupScenarioManager.TOP_LEVEL_NAME));
        }
        if (scenarioEvent.getXML() == null) {
            scenarioEvent = new ScenarioEvent(scenarioEvent.getSourceFile(),
                    new Element(SetupScenarioManager.TOP_LEVEL_NAME));
        }

        eventElement = scenarioEvent.getXML().getChild(SetupScenarioManager.EVENTLIST_NAME);
        if (eventElement == null) {
            eventElement = new Element(SetupScenarioManager.EVENTLIST_NAME);
            scenarioEvent.getXML().add(eventElement);
        }

        msgListGUI.setListData(eventElement.getChildElements().toArray());

        msgListGUI.updateUI();
        if (selectedEvent != null) {
            msgListGUI.setSelectedValue(selectedEvent, true);
        }
    }

    @Override
    public void eventOccurred(Object evt) {
        if (evt instanceof ScenarioEvent) {
            ScenarioEvent scenEvent = (ScenarioEvent) evt;
            this.scenarioEvent = scenEvent;
            refreshList();
        } else if (evt instanceof SelectObjectEvent) {
            SelectObjectEvent soe = (SelectObjectEvent) evt;
            for (int i = 0; i < msgListGUI.getModel().getSize(); i++) {
                if (msgListGUI.getModel().getElementAt(i) == soe.getObject()) {
                    externalSelection = true;
                    msgListGUI.setSelectedValue(soe.getObject(), true);
                    externalSelection = false;
                }
            }
        }
    }

    private void setupDragging() {
        msgListGUI.setDragEnabled(true);
        msgListGUI.setDropMode(DropMode.INSERT);
        msgListGUI.setTransferHandler(new TransferHandler() {

            @Override
            public int getSourceActions(JComponent c) {
                return COPY_OR_MOVE;
            }

            @Override
            public boolean canImport(TransferSupport support) {
                //return !XmlTransferUtils.getObjectsFromTransfer(support.getTransferable()).isEmpty();
                return !selectedEvents.isEmpty();
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }

                //List<Element> transferObjs = XmlTransferUtils.getObjectsFromTransfer(support.getTransferable());
                List<Element> transferObjs = selectedEvents;

                JList.DropLocation loc = (JList.DropLocation) support.getDropLocation();
                int row = loc.getIndex();
                int addToRow = 0;
                if (row == -1) {
                    return false;
                }
                if (row >= msgListGUI.getModel().getSize()) {
                    addToRow = 1;
                    row--;
                }

                // the item that is in the position where the drop occurs
                Element dropObj = (Element) msgListGUI.getModel().getElementAt(row);

                if (eventManager != null) {
                    if (row == -1) {
                        row = eventElement.getChildCount() - 1;
                    }
                    if (support.getDropAction() == MOVE) {
                        eventElement.removeAll(transferObjs);
                        int insertRow = eventElement.indexOf(dropObj);
                        for (Element e : transferObjs) {
                            fixTime(e, insertRow + addToRow);
                        }
                        eventElement.addAll(insertRow + addToRow, transferObjs);
                    } else if (support.getDropAction() == COPY) {
                        int insertRow = eventElement.indexOf(dropObj);
                        for (int i = 0; i < transferObjs.size(); i++) {
                            Element newEvent = (Element) transferObjs.get(i).clone();
                            fixTime(newEvent, insertRow + addToRow);
                            eventElement.add(insertRow + addToRow + i, newEvent);
                        }
                    }

                    eventManager.fireEvent(scenarioEvent, this);

                    for (Element el : transferObjs) {
                        int r = eventElement.indexOf(el);
                        if (r != -1) {
                            msgListGUI.addSelectionInterval(r, r);
                        }
                    }
                }
                return true;
            }

            @Override
            public void exportAsDrag(JComponent comp, InputEvent e, int action) {
                super.exportAsDrag(comp, e, action);
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                if (msgListGUI.getSelectedValue() != null) {
                    return XmlTransferUtils.createTransfer(selectedEvents);
                }
                return null;
            }
        });
    }

    /**
     * Fixes the time value of an element that is to be inserted into the list
     * at the given index.
     *
     * @param el
     * @param index
     */
    void fixTime(Element event, int index) {

        if (eventElement == null) {
            return;
        }

        List<Element> eventList = eventElement.getChildElements();
        double prevTime = 0, nextTime = 0, time = 0;
        time = XMLUtil.getDoubleAttr(event, "Time", 0);
        if (index > 0) {
            prevTime = XMLUtil.getDoubleAttr(eventList.get(index - 1), "Time", 0);
        }
        if (index < eventElement.getChildCount() - 1) {
            nextTime = XMLUtil.getDoubleAttr(eventList.get(index), "Time", 0);
        }
        if (time < prevTime) {
            event.setAttribute("Time", String.valueOf(prevTime));
        }
        if (time > nextTime) {
            event.setAttribute("Time", String.valueOf(nextTime));
        }
    }

    void sortList() {
        if (eventElement != null) {
            List<Element> tmpList = new ArrayList<Element>(eventElement.getChildElements());
            Collections.sort(tmpList, new Comparator<Element>() {

                @Override
                public int compare(Element o1, Element o2) {
                    double t1 = XMLUtil.getDoubleAttr(o1, "Time", 0);
                    double t2 = XMLUtil.getDoubleAttr(o2, "Time", 0);
                    return Double.compare(t1, t2);
                }
            });
            eventElement.clear();
            eventElement.addAll(tmpList);
        }
    }

    /**
     * Renders the event using its simple name and time of event
     */
    public class EventListRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Element) {
                Element el = (Element) value;
                setText(el.getName() + " (" + XMLUtil.getAttr(el, "Time", "0") + " sec)");
            } else {
                setText(String.valueOf(value));
            }
            return super.getListCellRendererComponent(list, getText(), index, isSelected, cellHasFocus);

        }
    }
    // Actions
    Action deleteAction = new AbstractAction("Delete") {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (eventManager != null) {
                eventElement.removeAll(selectedEvents);
                eventManager.fireEvent(scenarioEvent, this);
            }
        }

        @Override
        public boolean isEnabled() {
            return !selectedEvents.isEmpty();
        }
    };
    Action cutAction = new AbstractAction("Cut") {

        @Override
        public void actionPerformed(ActionEvent e) {
            eventElement.removeAll(selectedEvents);
            XmlTransferUtils.copyObjectsToClipboard(selectedEvents);
            if (eventManager != null) {
                eventManager.fireEvent(scenarioEvent, null);
            }
        }

        @Override
        public boolean isEnabled() {
            return !selectedEvents.isEmpty();
        }
    };
    Action copyAction = new AbstractAction("Copy") {

        @Override
        public void actionPerformed(ActionEvent e) {
            List<XmlNode> copiedEvents = new ArrayList<XmlNode>();
            for (Element el : selectedEvents) {
                copiedEvents.add(el.clone());
            }
            XmlTransferUtils.copyObjectsToClipboard(copiedEvents);
        }

        @Override
        public boolean isEnabled() {
            return !selectedEvents.isEmpty();
        }
    };
    Action pasteAction = new AbstractAction("Paste") {

        @Override
        public void actionPerformed(ActionEvent e) {
            List<XmlNode> copyList = XmlTransferUtils.getObjectsFromClipboard();
            int index = msgListGUI.getSelectedIndex();

            for (XmlNode n : copyList) {

                n = n.clone();
                if (n instanceof Element) {
                    Element el = (Element) n;
                    fixTime(el, index);
                    try {
                        LMCPXMLReader.readXML(el.toXML());
                    } catch (Exception ex) {
                        continue;
                    }
                    if (index != -1) {
                        eventElement.add(++index, n);
                    } else {
                        eventElement.add(n);
                    }
                }

            }

            if (eventManager != null) {
                eventManager.fireEvent(scenarioEvent, null);
            }
        }
    };
    Action setTimeAction = new AbstractAction("Set Time...") {

        @Override
        public void actionPerformed(ActionEvent e) {
            String ans = JOptionPane.showInputDialog(msgListGUI, "Set Time (sec)", "Set Time", JOptionPane.PLAIN_MESSAGE);
            if (ans != null) {
                try {
                    double time = Double.parseDouble(ans);
                    for (Element el : selectedEvents) {
                        el.setAttribute("Time", ans);
                    }
                    sortList();
                    if (eventManager != null) {
                        eventManager.fireEvent(scenarioEvent, this);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(msgListGUI, ans + " is not a number");
                }

            }
        }
    };
    Action editAction = new AbstractAction("Edit Event") {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!selectedEvents.isEmpty()) {
                try {
                    Element el = selectedEvents.get(0);
                    Object o = LMCPXMLReader.readXML(XmlWriter.toCompactString(el));
                    if (o instanceof LMCPObject) {
                        o = ObjectTree.showEditWindow(o, panel, "Edit " + o.getClass().getSimpleName());
                        if (o != null) {
                            int index = eventElement.indexOf(el);
                            if (index != -1) {
                                eventElement.set(index, XmlReader.readDocument(((LMCPObject) o).toXML("")));
                            }
                            if (eventManager != null) {
                                eventManager.fireEvent(scenarioEvent);
                            }
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(EventListEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    };
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */