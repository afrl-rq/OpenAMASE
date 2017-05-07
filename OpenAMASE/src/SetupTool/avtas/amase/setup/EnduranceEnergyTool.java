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
import avtas.util.WindowUtils;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.AbstractAction;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.jdesktop.swingx.JXFormattedTextField;

/**
 * Provides a simple tool for converting between energy rates and endurance.
 * @author AFRL/RQQD
 */
public class EnduranceEnergyTool extends AmasePlugin {

    JPanel panel = new JPanel();
    JXFormattedTextField energyField = new JXFormattedTextField();
    JXFormattedTextField enduranceField = new JXFormattedTextField();

    String introText = "<html><p align=\"center\">Converts between endurance and energy usage<br/>"
            + "for setting flight profile energy usage values.</p></html>";

    public EnduranceEnergyTool() {

        energyField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#.####"))));
        enduranceField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#.##"))));

        energyField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                update(energyField);
            }
        });

        enduranceField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update(enduranceField);
            }
        });

        panel.setLayout(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        panel.add(new JLabel(introText, JLabel.CENTER), BorderLayout.NORTH);

        JPanel subPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(subPanel, BorderLayout.SOUTH);

        subPanel.add(new JLabel("Endurance (hours)", JLabel.CENTER));
        subPanel.add(new JLabel("Energy (%/sec)", JLabel.CENTER));
        subPanel.add(enduranceField);
        subPanel.add(energyField);

    }

    @Override
    public void getMenus(JMenuBar menubar) {
        final JMenu menu = WindowUtils.getMenu(menubar, "Tools");
        menu.add(new AbstractAction("Endurance-Energy Tool") {

            @Override
            public void actionPerformed(ActionEvent e) {
                WindowUtils.showPlainDialog(JOptionPane.getFrameForComponent(menu), panel, "Endurance-Energy Tool", false);
            }
        });
    }

    protected void update(JFormattedTextField src) {

        try {
            double value = Double.parseDouble(src.getText());

            if (src == energyField) {
                double endurance = 1. / value * 100 / 3600.;
                endurance = Math.round(endurance * 100) / 100.;
                enduranceField.setValue(endurance);
            } else if (src == enduranceField) {
                double energy = 100. / (value * 3600);
                energy = Math.round(energy * 100000) / 100000.;
                energyField.setValue(energy);
            }
        } catch (NumberFormatException ex) {
            src.setText("");
        }

    }

    public static void main(String[] args) {
        WindowUtils.showApplicationWindow(new EnduranceEnergyTool().panel);
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */