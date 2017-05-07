// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.validator;

import avtas.amase.AmasePlugin;
import avtas.amase.scenario.ScenarioState;
import avtas.amase.setup.SelectObjectEvent;
import avtas.amase.validator.TestResult.ResultType;
import avtas.amase.validator.tests.AirVehicleConfigurationTest;
import avtas.amase.validator.tests.AirVehicleStateTest;
import avtas.amase.validator.tests.UniqueIdTest;
import avtas.amase.validator.tests.ZoneTest;
import avtas.app.AppEventManager;
import avtas.app.Context;
import avtas.lmcp.LMCPObject;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author AFRL/RQQD
 */
public class ValidatorPlugin extends AmasePlugin implements HyperlinkListener {

    JPanel validatorPanel = new JPanel();
    private AppEventManager eventManager = null;
    JEditorPane outputPane = new JEditorPane("text/html", "");
    JPanel buttonPanel = new JPanel();
    private List<Test> tests = new ArrayList<>();
    private List<LMCPObject> events;
    ArrayList<TestResult> testResults = new ArrayList<TestResult>();

    public ValidatorPlugin() {

        this.eventManager = AppEventManager.getDefaultEventManager();
        setPluginName("Scenario Validator");
        setupValidatorPanel();

    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
        List<Element> testsEl = XMLUtil.getChildren(xml, "EventValidator/Tests");
        if (testsEl.isEmpty()) {
            // default tests to perform
            tests.add(new AirVehicleConfigurationTest());
            tests.add(new AirVehicleStateTest());
            tests.add(new ZoneTest());
            tests.add(new UniqueIdTest());
        }
        else {
            for (Element testEl : testsEl) {
                try {
                    Class c = Class.forName(testEl.getText());
                    Object test = c.newInstance();
                    if (test instanceof Test && !tests.contains(test)) {
                        tests.add((Test) test);
                    }

                } catch (Exception ex) {
                }

            }
        }
    }

    @Override
    public Component getGui() {
        return validatorPanel;
    }

    void setupValidatorPanel() {
        validatorPanel.setLayout(new BorderLayout());
        validatorPanel.add(new JScrollPane(outputPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

        outputPane.setPreferredSize(new Dimension(100, 100));

        buttonPanel.add(new JButton(new AbstractAction("Run Tests") {
            @Override
            public void actionPerformed(ActionEvent e) {
                runTests();
            }
        }));

        outputPane.setEditable(false);
        outputPane.addHyperlinkListener(this);

        validatorPanel.add(buttonPanel, BorderLayout.SOUTH);
    }


    void runTests() {
        outputPane.setText("Running Tests...");
        testResults.clear();

        new SwingWorker<String, Object>() {
            @Override
            protected String doInBackground() throws Exception {
                
                events = new ArrayList<>();
                for (ScenarioState.EventWrapper event : ScenarioState.getEventList()) {
                    events.add(event.event);
                }

                for (int i = 0; i < events.size(); i++) {
                    for (Test t : tests) {
                        t.runTest(events.get(i), events, i, testResults);
                    }
                }

                return getResults();
            }

            @Override
            protected void done() {
                try {
                    outputPane.setText(get());
                    outputPane.setCaretPosition(0);
                } catch (Exception ex) {
                    Logger.getLogger(ValidatorPlugin.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }.execute();

    }

    String getResults() {

        int errors = 0;
        int warnings = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<p style=\"font-family:sans-serif;font-size:11;\">");
        sb.append("[Validator Run: ").append(new Date()).append("]<br/>");

        for (TestResult ro : testResults) {
            int index = events.indexOf(ro.getTestObject());

            if (ro.result == ResultType.Warning) {
                ++warnings;
            }
            else if (ro.result == ResultType.Error) {
                ++errors;
            }

            if (index != -1) {
                Color color = ro.result == ResultType.Error ? Color.RED : Color.BLACK;
                String colorStr = "#" + Integer.toHexString(color.getRGB()).substring(2);
                sb.append("<br/>").append("<font color=\"").append(colorStr).append("\">");
                sb.append(" ").append(ro.result).append("</font>");
                sb.append(" ").append("<a href=\"").append(index).append("\">").append(ro.getTestObject().getClass().getSimpleName());
                sb.append("</a>");
                sb.append(" ").append(ro.getMessage());
            }
        }
        sb.append("<br/>Found ").append(errors).append(" Errors and ").append(warnings).append(" Warnings.<br/>");

        sb.append("</p></html>");
        return sb.toString();


    }


    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            int val = Integer.parseInt(e.getDescription());
            LMCPObject o = events.get(val);
            eventManager.fireEvent(new SelectObjectEvent(o), this);
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */