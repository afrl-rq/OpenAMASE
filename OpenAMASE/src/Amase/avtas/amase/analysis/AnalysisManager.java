// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.amase.analysis;

import avtas.amase.AmasePlugin;
import avtas.amase.scenario.ScenarioEvent;
import avtas.amase.scenario.ScenarioState;
import avtas.amase.scenario.ScenarioState.EventWrapper;
import avtas.app.Context;
import avtas.app.UserExceptions;
import avtas.swing.UserNotice;
import avtas.xml.XMLUtil;
import avtas.util.ReflectionUtils;
import avtas.util.WindowUtils;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import avtas.xml.Element;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JMenuBar;

/**
 * The <code>AnalysisManager</code> class collects the reports of each analysis
 * module and presents them in a GUI interface.
 *
 * @author AFRL/RQQD
 */
public class AnalysisManager extends AmasePlugin {

    List<AnalysisClient> clientList = new ArrayList<>();
    private AnalysisManager thisAnalysisMgr = this;
    int data_index = 0;

    public AnalysisManager() {
    }

    /**
     * Gets the current analysis report for this module in XML format.
     *
     * @return The first element of the XML document representing the analysis
     * report.
     */
    public Element getAnalysisReportXML() {

        doAnalysis();

        Element topNode = new Element("AnalysisReport");
        for (AnalysisClient c : clientList) {
            Element tmp = ((AnalysisClient) c).getAnalysisReportXML();
            if (tmp != null) {
                if (tmp.getParent() != null) {
                    tmp.getParent().remove(tmp);
                }
                topNode.add(tmp);
            }
        }
        return topNode;
    }

    @Override
    public void getMenus(JMenuBar menubar) {

        JMenu analysisMenu = WindowUtils.getMenu(menubar, "Analysis");

        analysisMenu.add(new AbstractAction("Show Report") {
            @Override
            public void actionPerformed(ActionEvent e) {
                doAnalysis();
                JFrame ff = new JFrame("Analysis");
                AnalysisGUI analysisGui = new AnalysisGUI(thisAnalysisMgr);
                ff.add(analysisGui);
                ff.pack();
                ff.setVisible(true);
            }
        });

        analysisMenu.add(new AbstractAction("Run Analysis") {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetAnalysis();
                doAnalysis();
            }
        });

    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {

        List<Element> analysisClients = XMLUtil.getChildren(xml, "Analysis/PluginList/Plugin");
        for (Element el : analysisClients) {
            try {
                Object o = ReflectionUtils.createInstance(el.getText());
                if (o instanceof AnalysisClient) {
                    clientList.add((AnalysisClient) o);
                }
            } catch (Exception ex) {
                UserExceptions.showError(this, "Error creaing Analysis Plugin", ex);
            }
        }

    }

    protected void doAnalysis() {

        final List<EventWrapper> eventList = ScenarioState.getEventList();

        if (data_index >= eventList.size()) {
            return;
        }

        UserNotice notice = new UserNotice("Performing Analysis", null);
        notice.setVisible(true);

        for (int i = data_index; i < eventList.size(); i++) {
            for (AnalysisClient c : clientList) {
                notice.setText("Processing Event " + i + " of " + eventList.size());
                c.eventOccurred(eventList.get(i).event);
            }
        }

        data_index = eventList.size();
        notice.setVisible(false);

    }

    protected void resetAnalysis() {
        data_index = 0;
        for (AnalysisClient c : clientList) {
            c.resetAnalysis();
        }
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof ScenarioEvent) {
            resetAnalysis();
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */