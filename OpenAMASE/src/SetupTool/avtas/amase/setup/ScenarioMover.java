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
import avtas.amase.scenario.ScenarioState;
import avtas.app.UserExceptions;
import avtas.amase.ui.LatLonPanel;
import avtas.util.NavUtils;
import avtas.util.WindowUtils;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author AFRL/RQQD
 */
public class ScenarioMover extends AmasePlugin {

    double newCenterLat;
    double newCenterLon;

    JButton goButton = new JButton("Convert");

    LatLonPanel llpanel;

    JPanel topPanel = new JPanel(new BorderLayout());

    double oldCenterLat;
    double oldCenterLon;


    JDialog dialog = null;


    public ScenarioMover() {
        buildGui();
    }

    @Override
    public void getMenus(final JMenuBar menubar) {
        WindowUtils.getMenu(menubar, "Scenario").add(new AbstractAction("Move Scenario") {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (dialog != null && dialog.isVisible()) {
                    dialog.toFront();
                } else {
                    dialog = new JDialog(JOptionPane.getFrameForComponent(menubar), "Move Scenario", false);
                    dialog.add(topPanel);
                    dialog.pack();
                    dialog.setVisible(true);
                    dialog.setLocationRelativeTo(dialog.getOwner());
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                }
            }
        });

    }

    void buildGui() {

        topPanel.setLayout(new GridBagLayout());

        llpanel = new LatLonPanel();
        llpanel.setBorder(new TitledBorder("New Centerpoint"));

        GridBagConstraints con = new GridBagConstraints();
        con.gridx = 0;
        con.gridy = 0;
        con.fill = GridBagConstraints.HORIZONTAL;
        con.insets = new Insets(5, 5, 5, 5);
        con.anchor = GridBagConstraints.CENTER;

        con.gridy++;
        con.gridx = 0;
        con.gridwidth = 2;
        con.fill = GridBagConstraints.BOTH;
        topPanel.add(llpanel, con);

        goButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                go();
            }
        });

        con.gridy++;
        con.gridx = 1;
        con.fill = GridBagConstraints.NONE;
        con.anchor = GridBagConstraints.LINE_END;
        topPanel.add(goButton, con);

    }


    void go() {

        try {
            newCenterLat = Math.toRadians(llpanel.getLatitude());
            newCenterLon = Math.toRadians(llpanel.getLongitude());

            Element scenarioEl = ScenarioState.getScenario().clone();

            Element oldCenterEl = XMLUtil.getChild(scenarioEl, "ScenarioData/SimulationView");
            if (oldCenterEl == null) {
                JOptionPane.showMessageDialog(topPanel, "Cannot move scenario.  Center not defined", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            oldCenterLat = Math.toRadians(XMLUtil.getDoubleAttr(oldCenterEl, "Latitude", 0));
            oldCenterLon = Math.toRadians(XMLUtil.getDoubleAttr(oldCenterEl, "Longitude", 0));

            oldCenterEl.setAttribute("Latitude", Math.toDegrees(newCenterLat));
            oldCenterEl.setAttribute("Longitude", Math.toDegrees(newCenterLon));
            
            double longExtent = Math.toRadians(XMLUtil.getDoubleAttr(oldCenterEl, "LongExtent", 0));
            longExtent = longExtent * (NavUtils.getRadius(oldCenterLat) / NavUtils.getRadius(newCenterLat));
            oldCenterEl.setAttribute("LongExtent", Math.toDegrees(longExtent));
            

            convertLocationValues(scenarioEl);

            fireEvent(new ScenarioEvent(null, scenarioEl));
        } catch (Exception ex) {
            UserExceptions.showError(this, "Error converting Scenario", ex);
        }
    }

    /**
     * Traverses the scenario file looking for elements with the name "Latitude"
     * and "Longitude". This will convert the text content of those elements
     * using the same linear distance offset in North and East axes from the
     * center point of the scenario. This assumes the coordinates are in
     * degrees.
     *
     * @param el
     */
    protected void convertLocationValues(Element el) {
        for (Element child : el.getChildElements()) {
            Element latEl = child.getChild("Latitude");
            Element lonEl = child.getChild("Longitude");

            if (latEl != null && lonEl != null) {
                double oldLat = Math.toRadians(Double.valueOf(latEl.getText()));
                double oldLon = Math.toRadians(Double.valueOf(lonEl.getText()));

                double dist_m = NavUtils.distance(oldCenterLat, oldCenterLon, oldLat, oldLon);
                double heading_rad = NavUtils.headingBetween(oldCenterLat, oldCenterLon, oldLat, oldLon);

                double[] newPt = NavUtils.getLatLon(newCenterLat, newCenterLon, dist_m, heading_rad);

                latEl.setText(String.valueOf(Math.toDegrees(newPt[0])));
                lonEl.setText(String.valueOf(Math.toDegrees(newPt[1])));
            }

            convertLocationValues(child);

        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ScenarioMover mover = new ScenarioMover();
        frame.add(mover.topPanel);
        frame.pack();
        frame.setVisible(true);

    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */