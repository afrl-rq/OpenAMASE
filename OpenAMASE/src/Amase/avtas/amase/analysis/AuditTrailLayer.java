// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.analysis;

import afrl.cmasi.AirVehicleState;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import avtas.amase.analysis.AuditTrailLayer.TrailGraphic;
import avtas.amase.objtree.ObjectTree;
import avtas.amase.scenario.ScenarioState;
import avtas.amase.ui.AircraftColors;
import avtas.app.AppEventListener;
import avtas.amase.ui.AircraftList;
import avtas.lmcp.LMCPObject;
import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapPoly;
import avtas.map.layers.GraphicsLayer;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Adds a trail to the aircraft
 *
 * @author AFRL/RQQD
 */
public class AuditTrailLayer extends GraphicsLayer<TrailGraphic> implements AppEventListener {

    /**
     * Number of seconds between trail updates
     */
    double resolution = 1;
    /**
     * Number of seconds to track the trail
     */
    double timeToTrack = 7200;
    /**
     * If true, updates all trails as the simulation runs
     */
    boolean trackAll = false;
    JPanel settingPanel = new JPanel();
    final JSpinner timeToTrackSpinner = new JSpinner(new SpinnerNumberModel(timeToTrack, 0, 7200, 1));
    final JSpinner resolutionSpinner = new JSpinner(new SpinnerNumberModel(resolution, 0, 30, 1));
    final JCheckBox liveUpdateBox = new JCheckBox("Live Update");
    final AircraftList aircraftList = new AircraftList();
    final JFrame dialog = new JFrame("Audit Trail Settings");
    public static final DecimalFormat timeFormat = new DecimalFormat("#.##");

    public AuditTrailLayer() {

        settingPanel.setLayout(new GridBagLayout());

        GridBagConstraints con = new GridBagConstraints();
        con.insets = new Insets(5, 5, 5, 5);
        con.gridy = 0;
        con.gridx = 0;

        //settingPanel.add(trackAllBox, con);
        //con.gridy++;
        settingPanel.add(new JLabel("Tracking Time (s)"), con);
        con.gridx = 1;
        settingPanel.add(timeToTrackSpinner, con);
        con.gridy++;
        con.gridx = 0;
        settingPanel.add(new JLabel("Trail Resolution (s)"), con);
        con.gridx = 1;
        settingPanel.add(resolutionSpinner, con);
        con.gridy++;
        con.gridwidth = 2;
        settingPanel.add(liveUpdateBox, con);

        con.gridy++;
        con.gridx = 0;
        con.gridwidth = 2;
        con.gridheight = 5;
        con.fill = GridBagConstraints.BOTH;
        settingPanel.add(new JScrollPane(aircraftList), con);

        con.gridy += con.gridheight;
        con.gridx = 0;
        con.gridheight = 1;
        con.gridwidth = 1;
        JButton clearButton = new JButton(new AbstractAction("Clear Trails") {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (MapGraphic g : getList()) {
                    ((TrailGraphic) g).clear();
                }
                refresh();
            }
        });
        settingPanel.add(clearButton, con);

        JButton updateButton = new JButton(new AbstractAction("Update Trails") {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (MapGraphic g : getList()) {
                    updateHistory();
                }
                refresh();
            }
        });
        con.gridx = 1;
        settingPanel.add(updateButton, con);

        resolutionSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                resolution = ((Number) resolutionSpinner.getValue()).doubleValue();
            }
        });

        timeToTrackSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                timeToTrack = ((Number) timeToTrackSpinner.getValue()).intValue();
            }
        });

//        trackAllBox.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                trackAll = trackAllBox.isSelected();
//                aircraftList.setEnabled(!trackAllBox.isSelected());
//            }
//        });
        aircraftList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                int[] indicies = aircraftList.getSelectedIndices();
                List<Long> idList = new ArrayList<>();
                for (int i = 0; i < indicies.length; i++) {
                    idList.add(aircraftList.getModel().getElementAt(i).getID());
                }
                setTrackedAircraft(idList);
            }
        });
        
        // prep the dialog box
        dialog.add(settingPanel, BorderLayout.CENTER);

    }

    @Override
    public void setConfiguration(Element node) {
        trackAll = XMLUtil.getBool(node, "TrackTrails", trackAll);
        resolution = XMLUtil.getDouble(node, "TimeResolution", resolution);
        timeToTrack = XMLUtil.getDouble(node, "TimeToTrack", timeToTrack);
        liveUpdateBox.setSelected(trackAll);
    }

    protected void updateHistory() {
        for (TrailGraphic trail : getList()) {
            trail.clear();
            for (int i = ScenarioState.getEventList().size() - 1; i >= 0; i--) {
                LMCPObject e = ScenarioState.getEventList().get(i).event;
                if (e instanceof AirVehicleState) {
                    AirVehicleState avs = (AirVehicleState) e;
                    if (Long.valueOf(avs.getID()) == trail.getRefObject()) {
                        if (trail.timeList.isEmpty() || avs.getTime() > trail.timeList.get(0) - timeToTrack) {
                            if (trail.timeList.isEmpty() || avs.getTime() < trail.timeList.get(trail.timeList.size() - 1) - resolution) {
                                trail.addPoint(avs.getLocation().getLatitude(), avs.getLocation().getLongitude(), avs.getTime());
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public void eventOccurred(Object event) {

        if (event instanceof AirVehicleState && liveUpdateBox.isSelected()) {
            AirVehicleState state = (AirVehicleState) event;
            TrailGraphic trail = (TrailGraphic) getList().getByRefObject(state.getID());
            if (trail == null && trackAll) {
                addTrackedAircraft(state.getID());
            }
            if (trail != null) {
                if (trail.timeList.isEmpty() || (state.getTime() - trail.timeList.get(0) >= resolution)) {
                    trail.insertPoint(state.getLocation().getLatitude(), state.getLocation().getLongitude(), state.getTime(), 0);

                    // trim the map based on time
                    while (trail.getNumPoints() > 0 && trail.getTime(0) < (state.getTime() - timeToTrack)) {
                        trail.deletePoint(trail.getNumPoints() - 1);
                    }

                    trail.project(getProjection());
                }
            }
        }
        if (event instanceof SessionStatus) {
            SessionStatus status = (SessionStatus) event;
            if (status.getState() == SimulationStatusType.Reset) {
                getList().clear();
            }
        }
    }

    protected void addTrackedAircraft(long id) {

        if (getList().getByRefObject(id) == null) {
            TrailGraphic trail = new TrailGraphic();
            trail.setRefObject(id);
            getList().add(trail);
            trail.setPainter(AircraftColors.getColor(id), 1);
        }
    }

    protected void removeTrackedAircraft(long id) {
        MapGraphic g = getList().getByRefObject(id);
        if (g != null) {
            getList().remove(g);
        }
    }

    protected void setTrackedAircraft(List<Long> vehicleIds) {

        for (Iterator<TrailGraphic> it = getList().iterator(); it.hasNext();) {
            if (!vehicleIds.contains(it.next().getRefObject())) {
                it.remove();
            }
        }
        for (Long id : vehicleIds) {
            addTrackedAircraft(id);
        }

    }

    @Override
    public Component getSettingsView() {
        return settingPanel;
    }

    @Override
    public void addPopupMenuItems(JPopupMenu menu, MouseEvent e, double lat, double lon) {
        menu.add(new AbstractAction("Audit Trails...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!dialog.isVisible()) {
                    dialog.pack();
                    dialog.setLocationRelativeTo(getMap());
                    dialog.setVisible(true);
                }
                else {
                    dialog.toFront();
                }
            }
        });

        for (TrailGraphic trail : getList()) {
            if (trail.onEdge(e.getX(), e.getY(), 4)) {
                int index = trail.getNearestPoint(e.getX(), e.getY());
                if (index != -1) {
                    double time = trail.timeList.get(index);
                    final AirVehicleState avs = ScenarioState.getAirVehicleState((Long) trail.getRefObject(), time);
                    if (avs != null) {
                        menu.add(new AbstractAction("Vehicle " + avs.getID() + " trail at time " + timeFormat.format(time)) {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                ObjectTree.showEditWindow(avs, getMap(), "State for Vehicle " + avs.getID());
                            }
                        });

                    }
                }
            }
        }
    }

    public static class TrailGraphic extends MapPoly {

        List<Double> timeList = new ArrayList<>();

        @Override
        public void addPoint(double lat, double lon, double time) {
            super.addPoint(lat, lon);
            timeList.add(time);
        }

        public void insertPoint(double lat, double lon, double time, int index) {
            super.insertPoint(lat, lon, index);
            timeList.add(index, time);
        }

        public double getTime(int index) {
            return timeList.get(index);
        }

        @Override
        public void deletePoint(int index) {
            super.deletePoint(index);
            timeList.remove(index);
        }

        @Override
        public void clear() {
            super.clear();
            timeList.clear();
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */