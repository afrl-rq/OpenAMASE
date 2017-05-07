// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.scenario;

import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import avtas.amase.AmasePlugin;
import avtas.amase.objtree.ObjectTree;
import avtas.amase.scenario.ScenarioState.EventWrapper;
import avtas.lmcp.LMCPObject;
import avtas.util.WindowUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.StringValue;

/**
 *
 * @author AFRL/RQQD
 */
public class ScenarioEventViewer extends AmasePlugin {

    protected JPanel mainPanel;
    protected List<Class> filterList = new ArrayList<>();
    protected JComboBox<String> filterBox;
    protected DefaultComboBoxModel<String> filterBoxModel = new DefaultComboBoxModel<>();
    EventSelectionModel model = new EventSelectionModel();
    protected JXList eventList;
    protected ObjectTree objectTree;
    protected JLabel numEventsLabel;
    protected int numEvents = 0;
    protected static String SHOW_ALL_EVENTS = "Show All Events";
    protected static String BASELINE_EVENTS_LABEL = "Number of Events : ";
    protected static DecimalFormat timeFormat = new DecimalFormat("#.###");
    protected static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    protected static RowFilter showAllFilter = new RowFilter() {
        @Override
        public boolean include(RowFilter.Entry entry) {
            return true;
        }
    };
    private double startTime = 0;

    public ScenarioEventViewer() {

        setPluginName("Scenario Event Viewer");

        eventList = new JXList(model, true);

        eventList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        eventList.setCellRenderer(new DefaultListRenderer(new StringValue() {
            @Override
            public String getString(Object o) {
                EventWrapper wrapper = (EventWrapper) o;
                if (wrapper.time > 1E6) {
                    return wrapper.event.getClass().getSimpleName() + " (" + dateFormat.format(new Date( (long) ( (wrapper.time - startTime) * 1000) )) + ")";
                }
                else 
                    return wrapper.event.getClass().getSimpleName() + " (" + timeFormat.format(wrapper.time - startTime) + " sec)";
            }
        }));

        eventList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Object selObj = eventList.getSelectedValue();
                if (selObj instanceof EventWrapper) {
                    objectTree.setObject(((EventWrapper) selObj).event);
                } else {
                    objectTree.setObject(null);
                }
            }
        });

        filterBox = new JComboBox<>(filterBoxModel);

        filterBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEventList();
                int selIndex = filterBox.getSelectedIndex();
                if (selIndex <= 0) {
                    eventList.setRowFilter(showAllFilter);
                } else {
                    final Class cl = filterList.get(filterBox.getSelectedIndex() - 1);
                    eventList.setRowFilter(new RowFilter<Object, Integer>() {
                        @Override
                        public boolean include(RowFilter.Entry<? extends Object, ? extends Integer> entry) {
                            EventWrapper event = (EventWrapper) entry.getValue(entry.getIdentifier());
                            return event.event.getClass() == cl;
                        }
                    });
                }
            }
        });

        objectTree = new ObjectTree(null);
        objectTree.setEditable(false);

        numEventsLabel = new JLabel(BASELINE_EVENTS_LABEL);

        JPanel eventPanel = new JPanel(new BorderLayout());
        eventPanel.add(new JScrollPane(eventList), BorderLayout.CENTER);

        JPanel filterBoxPanel = new JPanel();
        filterBoxPanel.setBorder(new TitledBorder("Filter Events By Type"));
        filterBoxPanel.add(filterBox);
        eventPanel.add(filterBoxPanel, BorderLayout.SOUTH);

        JSplitPane userPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        userPanel.setLeftComponent(eventPanel);
        userPanel.setRightComponent(new JScrollPane(objectTree));
        userPanel.setResizeWeight(.5);
        userPanel.setDividerLocation(0.5);

        mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        mainPanel.add(userPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.add(numEventsLabel);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(new JButton(new AbstractAction("Refresh") {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEventList();
            }
        }));
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        initializeView();

    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof SessionStatus) {
            SessionStatus status = (SessionStatus) event;
            if (status.getState() == SimulationStatusType.Reset) {
                startTime = status.getStartTime();
                initializeView();
            }
            if (status.getState() != SimulationStatusType.Running) {
                updateEventList();
            }
        } else if (event instanceof LMCPObject) {
            numEvents++;
            numEventsLabel.setText(BASELINE_EVENTS_LABEL + numEvents);
            addFilter((LMCPObject) event);
        }
    }

    protected void updateEventList() {
        model.update();
    }

    protected void initializeView() {
        numEvents = 0;
        model = new EventSelectionModel();
        eventList.setModel(model);
        filterList.clear();
        filterBoxModel.removeAllElements();
        filterBoxModel.addElement(SHOW_ALL_EVENTS);
        eventList.setRowFilter(showAllFilter);
        numEventsLabel.setText(BASELINE_EVENTS_LABEL + numEvents);
    }

    protected void addFilter(LMCPObject obj) {
        if (!filterList.contains(obj.getClass())) {
            String filterName = obj.getClass().getSimpleName() + " (" + obj.getLMCPSeriesName() + ")";
            filterBoxModel.addElement(filterName);
            filterList.add(obj.getClass());
        }
    }

    @Override
    public void getMenus(final JMenuBar menubar) {
        JMenu menu = WindowUtils.getMenu(menubar, "Scenario");
        menu.add(new AbstractAction("Show Scenario Events") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new JFrame("Scenario Events");
                f.add(mainPanel);
                f.setSize(640, 480);
                f.setLocationRelativeTo(JOptionPane.getFrameForComponent(menubar));
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                updateEventList();
                f.setVisible(true);
            }
        });

    }

    static class EventSelectionModel extends AbstractListModel<EventWrapper> {

        int size = 0;

        @Override
        public int getSize() {
            int size = ScenarioState.getEventList().size();
            return size;
        }

        @Override
        public EventWrapper getElementAt(int index) {
            return ScenarioState.getEventList().get(index);
        }

        public void update() {
            int lastSize = size;
            size = ScenarioState.getEventList().size();
            if (size > lastSize) {
                fireIntervalAdded(this, lastSize, size - 1);
            } else {
                fireContentsChanged(this, 0, size - 1);
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */