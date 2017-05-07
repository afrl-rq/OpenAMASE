// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.ui;

import avtas.xml.XMLUtil;
import avtas.amase.util.SimTimer;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import avtas.app.AppEventManager;
import avtas.amase.scenario.ScenarioEvent;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import avtas.amase.AmasePlugin;
import java.awt.Component;
import java.awt.Font;

/**
 *
 * @author AFRL/RQQD
 */
public class SimControls extends AmasePlugin {

    private float realtimeMult = 1;
    private SessionStatus lastStatus = null;
    private int prefWidth = 260;
    private int prefHeight = 40;
    private JLabel simTimeLabel = new JLabel("00:00:00.0");
    private DecimalFormat hhFormat = new DecimalFormat("00");
    private DecimalFormat ssFormat = new DecimalFormat("00.0");
    private double startTime = 0;
    private JProgressBar timeBar = new JProgressBar();
    private double endTime = Double.MAX_VALUE;
    private JToggleButton go,  pause,  reset;
    private AppEventManager mgr;
    ScenarioEvent scenEvent = null;
    JPanel panel = new JPanel();
    

    public SimControls() {
        
        setPluginName("Simulation Controls");
        
        this.mgr = AppEventManager.getDefaultEventManager();
        
        panel.setMaximumSize(new Dimension(prefWidth, prefHeight));
        panel.setPreferredSize(panel.getMaximumSize());

        ButtonGroup group = new ButtonGroup();

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        ImageIcon img = new ImageIcon(getClass().getResource("/resources/play-26.png"));

        go = new JToggleButton(img);
        go.setPreferredSize(new Dimension(img.getIconWidth() + 4, img.getIconHeight() + 4));
        group.add(go);
        go.setEnabled(false);

        go.setToolTipText("Go");
        go.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                    SimTimer.go();
            }
        });
        panel.add(go);

        img = new ImageIcon(getClass().getResource("/resources/pause-26.png"));
        pause = new JToggleButton(img);
        pause.setPreferredSize(new Dimension(img.getIconWidth() + 4, img.getIconHeight() + 4));
        group.add(pause);
        pause.setEnabled(false);

        pause.setToolTipText("Pause");
        pause.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                    SimTimer.pause();
            }
        });
        panel.add(pause);

        img = new ImageIcon(getClass().getResource("/resources/reset-26.png"));
        reset = new JToggleButton(img);
        reset.setEnabled(false);
        reset.setPreferredSize(new Dimension(img.getIconWidth() + 4, img.getIconHeight() + 4));
        group.add(reset);

        reset.setToolTipText("Reset");
        reset.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (mgr != null && scenEvent != null) {
                    SessionStatus status = new SessionStatus();
                    status.setState(SimulationStatusType.Reset);
                    mgr.fireEvent(status);
                }
            }
        });
        panel.add(reset);
        group.add(reset);

        final JComboBox realtimeBox = new JComboBox(new Object[]{"0.25x", "0.5x", "1x", "2x",
                    "5x", "10x", "20x", "50x", "100x"
                });
        final double[] multiples = new double[]{0.25, 0.5, 1, 2, 5, 10, 20, 50, 100};
        realtimeBox.setToolTipText("Realtime multiple");
        realtimeBox.setSelectedIndex(2);
        realtimeBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                realtimeMult = (float) multiples[realtimeBox.getSelectedIndex()];
                    SimTimer.setRealtimeMultiple(realtimeMult);
            }
        });
        panel.add(realtimeBox);

        simTimeLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        panel.add(simTimeLabel);
        panel.add(timeBar);
        
        panel.setMaximumSize(panel.getPreferredSize());

    }

    public String printTime(double time_sec) {
        int h = (int) Math.floor(time_sec / 3600);
        int m = (int) Math.floor((time_sec - h * 3600) / 60);
        double s = time_sec - h * 3600 - m * 60;
        //return hhFormat.format(h) + ":" + hhFormat.format(m) + ":" + ssFormat.format(s);
        return hhFormat.format(h) + ":" + hhFormat.format(m) + ":" + ssFormat.format(s) + " (" + ssFormat.format(time_sec) + ")";
    }

    public void initScenario(ScenarioEvent evt) {
        this.scenEvent = evt;
        go.setEnabled(true);
        pause.setEnabled(true);
        reset.setEnabled(true);
        endTime = XMLUtil.getDouble(evt.getXML(), "ScenarioData/ScenarioDuration", endTime);
        if (endTime < Double.MAX_VALUE) {
            timeBar.setToolTipText("Scenario End Time: " + printTime(endTime));
            timeBar.setMaximum((int) endTime);
        }
    }

    public void eventOccurred(Object evt) {
        if (evt instanceof ScenarioEvent) {
            initScenario((ScenarioEvent) evt);
            return;
        }
        else if (evt instanceof SessionStatus) {
            lastStatus = (SessionStatus) evt;
            
            if (lastStatus.getState() == SimulationStatusType.Paused) {
                pause.setSelected(true);
            }
            else if (lastStatus.getState() == SimulationStatusType.Running) {
                go.setSelected(true);
            }
            else if (lastStatus.getState() == SimulationStatusType.Reset) {
                startTime = lastStatus.getStartTime();
            }
            
            simTimeLabel.setText(printTime(lastStatus.getScenarioTime() / 1000d - startTime));
            timeBar.setValue((int) (lastStatus.getScenarioTime() / 1000d - startTime) );
            
        }
        
    }


    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new SimControls().getGui());
        f.pack();
        f.setVisible(true);
    }

    @Override
    public Component getGui() {
        return panel;
    }
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */