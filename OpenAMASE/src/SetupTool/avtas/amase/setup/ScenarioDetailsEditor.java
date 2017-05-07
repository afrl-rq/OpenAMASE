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
import avtas.amase.map.MapPlugin;
import avtas.amase.scenario.ScenarioEvent;
import avtas.app.AppEventManager;
import avtas.util.WindowUtils;
import avtas.xml.XMLUtil;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import avtas.xml.Element;
import java.awt.Dialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

/**
 *
 * @author AFRL/RQQD
 */
public class ScenarioDetailsEditor extends AmasePlugin {

    JFormattedTextField viewLat = new JFormattedTextField(new DecimalFormat("#.####"));
    JFormattedTextField viewLong = new JFormattedTextField(new DecimalFormat("#.####"));
    JFormattedTextField viewExtent = new JFormattedTextField(new DecimalFormat("#.####"));
    JTextField scenarioName = new JTextField();
    TimeWidget duration = new TimeWidget();
    JFormattedTextField trackAgeOut;
    Element dataElement = null;
    MapPlugin mapPlugin = null;
    private ScenarioEvent scenarioEvent = null;
    private AppEventManager eventManager = AppEventManager.getDefaultEventManager();
    JPanel panel = new JPanel();

    public ScenarioDetailsEditor() {

        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        final JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridLayout(0, 2, 2, 2));

        fieldPanel.add(new JLabel("Scenario Name"));
        scenarioName.setColumns(15);
        fieldPanel.add(scenarioName);
        
        
        fieldPanel.add(new JLabel("Duration"));
        fieldPanel.add(duration);
        duration.setToolTipText("Scenario run time in seconds");
        
        fieldPanel.add(new JLabel());
        fieldPanel.add(new JLabel());
        fieldPanel.add(new JLabel("View Latitude"));
        fieldPanel.add(viewLat);
        viewLat.setToolTipText("Degrees");
        fieldPanel.add(new JLabel("View Longitude"));
        fieldPanel.add(viewLong);
        viewLong.setToolTipText("Degrees");
        
        fieldPanel.add(new JLabel("View LongExtent"));
        fieldPanel.add(viewExtent);
        viewExtent.setToolTipText("Degrees");
        
        fieldPanel.add(new JLabel());
//        fieldPanel.add(new JButton(new AbstractAction("Paste View") {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String str = WindowUtils.copyFromClipboard();
//                if (str != null) {
//                    String[] coordStr = str.split(",");
//                    if (coordStr.length == 4) {
//                        double[] coords = new double[4];
//                        for (int i=0; i<coords.length; i++) {
//                            coords[i] = Double.valueOf(coordStr[i]);
//                        }
//                        viewLat.setValue( (coords[0] + coords[2]) * 0.5);
//                        viewLong.setValue( (coords[1] + coords[3]) * 0.5);
//                        viewExtent.setValue(coords[3] - coords[1]);
//                    }
//                    else {
//                        JOptionPane.showMessageDialog(fieldPanel, "<html>Cannot paste view.  Invalid format.<br/>"
//                                + "  Format should be <em>northlat,westlon,soutlat,eastlon</em> in degrees.</html>");
//                    }
//                }
//            }
//        }));
        panel.add(fieldPanel, BorderLayout.NORTH);

    }

    void initFields(ScenarioEvent event) {
        this.scenarioEvent = event;
        dataElement = event.getXML().getChild("ScenarioData");
        if (dataElement == null) {
            dataElement = new Element("ScenarioData");
            scenarioEvent.getXML().add(dataElement);
        }

        duration.setValue(XMLUtil.getDouble(dataElement, "ScenarioDuration", 3600));
        scenarioName.setText(XMLUtil.getValue(dataElement, "ScenarioName", ""));
        viewLat.setValue(XMLUtil.getDoubleAttr(dataElement, "SimulationView/Latitude", 0));
        viewLong.setValue(XMLUtil.getDoubleAttr(dataElement, "SimulationView/Longitude", 0));
        viewExtent.setValue(XMLUtil.getDoubleAttr(dataElement, "SimulationView/LongExtent", 0));

    }

    void updateScenario() {
        try {
            duration.timeField.commitEdit();
            viewLat.commitEdit();
            viewLong.commitEdit();
            viewExtent.commitEdit();
        } catch (ParseException ex) { }
        
        if (dataElement != null) {
            XMLUtil.getOrAddElement(dataElement, "ScenarioDuration").setText(String.valueOf(duration.getValueSeconds()));
            XMLUtil.getOrAddElement(dataElement, "ScenarioName").setText(scenarioName.getText());
            XMLUtil.getOrAddElement(dataElement, "SimulationView").setAttribute("Latitude", String.valueOf(viewLat.getValue()));
            XMLUtil.getOrAddElement(dataElement, "SimulationView").setAttribute("Longitude", String.valueOf(viewLong.getValue()));
            XMLUtil.getOrAddElement(dataElement, "SimulationView").setAttribute("LongExtent", String.valueOf(viewExtent.getValue()));
            if (eventManager != null && dataElement != null) {
                eventManager.fireEvent(scenarioEvent, this);
            }
        }
    }

    public void eventOccurred(Object event) {
        if (event instanceof ScenarioEvent) {
            initFields((ScenarioEvent) event);
        }
    }

    @Override
    public void applicationPeerAdded(Object peer) {
        if (peer instanceof MapPlugin) {
            this.mapPlugin = (MapPlugin) peer;
        }
    }

    @Override
    public void getMenus(final JMenuBar menubar) {
        JMenu menu = WindowUtils.getMenu(menubar, "Scenario");
        menu.add(new AbstractAction("Edit Scenario Data") {

            @Override
            public void actionPerformed(ActionEvent e) {
                                
                JDialog dialog = new JDialog(JOptionPane.getFrameForComponent(menubar), "Edit Scenario Data", Dialog.ModalityType.MODELESS);
                dialog.add(panel, BorderLayout.CENTER);
                dialog.setModalityType(Dialog.ModalityType.MODELESS);
                dialog.setResizable(true);
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        updateScenario();
                    }
                });
                dialog.pack();
                dialog.setLocationRelativeTo(JOptionPane.getFrameForComponent(menubar));
                dialog.setVisible(true);
            }
        });
    }
    
    protected static class TimeWidget extends JPanel {
        JFormattedTextField timeField = new JFormattedTextField(new DecimalFormat("#.####"));
        JComboBox<String> unitSelector = new JComboBox<>(new String[]{"Secs", "Mins", "Hrs"});
        
        public TimeWidget() {
            setLayout(new BorderLayout());
            add(timeField, BorderLayout.CENTER);
            add(unitSelector, BorderLayout.EAST);
            
        }
        
        public double getValueSeconds() {
            return ((Number) timeField.getValue()).doubleValue() * Math.pow(60, unitSelector.getSelectedIndex() );
        }
        
        public void setValue(double timeValue) {
            if (timeValue >= 3600) {
                timeField.setValue(timeValue/3600);
                unitSelector.setSelectedIndex(2);
            }
            else if (timeValue > 60) {
                timeField.setValue(timeValue/60);
                unitSelector.setSelectedIndex(1);
            }
            else {
                timeField.setValue(timeValue);
                unitSelector.setSelectedIndex(0);
            }
        }
        
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */