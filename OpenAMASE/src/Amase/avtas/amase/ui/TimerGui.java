// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.ui;

import afrl.cmasi.SimulationStatusType;
import avtas.amase.util.SimTimer;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import javax.swing.Box;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author AFRL/RQQD
 */
public class TimerGui extends JPanel {

    JFormattedTextField frameField = new JFormattedTextField(new DecimalFormat("#.#"));
    JFormattedTextField publishField = new JFormattedTextField(new DecimalFormat("#.#"));

    public TimerGui(SimTimer timer) {
        frameField.setColumns(3);
        publishField.setColumns(3);
        
        frameField.setValue(SimTimer.getFramerate());
        publishField.setValue(SimTimer.getPublishRate());
        
        frameField.setToolTipText("The rate at which the timer will update modules");
        publishField.setToolTipText("The rate at which the timer publishes SessionStatus messages");
        
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        
        constraints.insets = new Insets(5, 5, 5, 5);
        JLabel frameLabel = new JLabel("Frame Rate (1/sec)");
        frameLabel.setToolTipText(frameField.getToolTipText());
        add(frameLabel, constraints);
        
        constraints.gridx = 1;
        constraints.weightx = 2;
        add(frameField, constraints);
        
        if (SimTimer.getStatus() == SimulationStatusType.Running) {
            constraints.gridy++;
            constraints.gridx = 0;
            constraints.gridwidth = 2;
            constraints.anchor = GridBagConstraints.CENTER;
            add(new JLabel("<html><b>Warning: </b><em>Timer must be paused to set frame rate</em></html>"), constraints);
        }
        
        constraints.weightx = 1;
        constraints.gridy++;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        
        JLabel pubLabel = new JLabel("Publish Rate (1/sec)");
        pubLabel.setToolTipText(publishField.getToolTipText());
        add(pubLabel, constraints);
        
        constraints.gridx = 1;
        constraints.weightx = 2;
        add(publishField, constraints);
    }

    public double getFrameRate() {
        return ((Number) frameField.getValue()).doubleValue();
    }

    public double getPublishRate() {
        return ((Number) publishField.getValue()).doubleValue();
    }

    public static void showDialog(Component parent, SimTimer timer) {
        TimerGui gui = new TimerGui(timer);
        int ans = JOptionPane.showConfirmDialog(
                parent, 
                gui, 
                "Timer Options", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE, null);
        
        if (ans == JOptionPane.YES_OPTION) {
            SimTimer.setFramerate(gui.getFrameRate());
            SimTimer.setPublishRate(gui.getPublishRate());
        }
    }
    
    public static void main(String[] args) {
        showDialog(null, new SimTimer(10, 5));
        System.exit(0);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */