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
import avtas.amase.scenario.ScenarioEvent;
import avtas.app.AppEventManager;
import avtas.xml.XMLUtil;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import avtas.xml.Element;

/**
 *
 * @author AFRL/RQQD
 */
public class TimeSlider extends AmasePlugin {

    JPanel sliderPanel = new JPanel();
    JFormattedTextField timeField = new JFormattedTextField(0.0);
    private AppEventManager eventManager = AppEventManager.getDefaultEventManager();
    private static double DEFAULT_DURATION = 7200;
    JSlider slider = new JSlider(0, (int) DEFAULT_DURATION);
    double time = 0;

    public TimeSlider() {
        sliderPanel.setLayout(new BorderLayout());
        slider.setValue(0);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        JPanel labelPanel = new JPanel();
        labelPanel.add(new JLabel("Time (sec):"));
        labelPanel.add(timeField);
        timeField.setColumns(5);
        sliderPanel.add(labelPanel, BorderLayout.EAST);
        sliderPanel.add(slider, BorderLayout.CENTER);

        timeField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                time = ( (Double) timeField.getValue());
                slider.setValue( (int) time );
                fireTimeEvent(time);
            }
        });
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                time = slider.getValue();
                timeField.setValue(slider.getValue());
                fireTimeEvent(time);
            }
        });
    }

    public void eventOccurred(Object event) {
        if (event instanceof ScenarioEvent) {
            Element el = ((ScenarioEvent) event).getXML();
            double dur = XMLUtil.getDouble(el, "ScenarioData/ScenarioDuration", DEFAULT_DURATION);
            slider.setMaximum( (int) dur);
        }
        else if (event instanceof SessionStatus) {
            double time = ((SessionStatus) event).getScenarioTime() / 1000d;
            timeField.setValue(time);
            slider.setValue((int) time);
            if (slider.getMaximum() < time) 
                slider.setMaximum( (int) time + 1);
            //slider.getLabelTable().put((int) time, String.valueOf(time) + " s");
        }
    }

    void fireTimeEvent(double time) {
        if (eventManager != null) {
            SessionStatus ss = new SessionStatus();
            ss.setState(SimulationStatusType.Paused);
            ss.setScenarioTime( (long) (time * 1000));
            eventManager.fireEvent(ss, this);
        }
    }

    @Override
    public Component getGui() {
        return sliderPanel;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */