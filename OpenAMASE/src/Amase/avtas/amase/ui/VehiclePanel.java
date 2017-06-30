// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.ui;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import avtas.data.Unit;
import avtas.util.Colors;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.text.DecimalFormat;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;

/**
 *
 * @author AFRL/RQQD
 */
public class VehiclePanel extends JPanel {

    MiniAttitude attitude = new MiniAttitude();
    MiniFuel fuelgauge = new MiniFuel();
    JLabel altBox  = new JLabel("A really long value");
    JLabel speedBox  = new JLabel("0");
    JLabel hdgBox  = new JLabel("0");
    JLabel vsBox  = new JLabel("0");
    JLabel modeBox  = new JLabel("-");
    JToggleButton vehButton;
    DecimalFormat intFormat = new DecimalFormat("#");

    /** Creates a new instance of VehiclePanel */
    public VehiclePanel(AirVehicleConfiguration avc, Color color) {
        setLayout(new BorderLayout(10, 5));
        setBorder(new EmptyBorder(5,5,5,5));

        JPanel statPanel = new JPanel();
        statPanel.setLayout(new GridLayout(0, 2));
        
        JPanel textArea = new JPanel(new BorderLayout(5,0));
        JPanel labelPanel = new JPanel(new GridLayout(0, 1));
        
        labelPanel.add(new JLabel("Altitude"));
        labelPanel.add(new JLabel("Speed"));
        labelPanel.add(new JLabel("Heading"));
        labelPanel.add(new JLabel("Vert Speed"));
        labelPanel.add(new JLabel("Nav Mode"));
        
        textArea.add(labelPanel, BorderLayout.WEST);
        
        JPanel fieldPanel = new JPanel(new GridLayout(0,1));
        fieldPanel.add(altBox);
        fieldPanel.add(speedBox);
        fieldPanel.add(hdgBox);
        fieldPanel.add(vsBox);
        fieldPanel.add(modeBox);
        textArea.add(fieldPanel, BorderLayout.CENTER);

        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new BorderLayout());

        Image icon = IconTools.getFilledImage(IconManager.getIcon(avc), 24, 24, 2, altBox.getForeground(), color);
        String vehName = avc.getLabel().toUpperCase() + " (" + avc.getID() + ")";
        JLabel vehicleIcon = new JLabel(vehName, new ImageIcon(icon), JLabel.CENTER);
        vehicleIcon.setVerticalTextPosition(JLabel.BOTTOM);
        vehicleIcon.setHorizontalTextPosition(JLabel.CENTER);
        iconPanel.add(vehicleIcon, BorderLayout.CENTER);
        iconPanel.add(Box.createHorizontalStrut(20), BorderLayout.EAST);
        iconPanel.add(Box.createHorizontalStrut(20), BorderLayout.WEST);

        JPanel gaugePanel = new JPanel();
        gaugePanel.setLayout(new GridLayout(0, 1, 5, 5));
        gaugePanel.add(attitude);
        gaugePanel.add(fuelgauge);
        
        add(iconPanel, BorderLayout.WEST);
        add(textArea, BorderLayout.CENTER);
        add(gaugePanel, BorderLayout.EAST);

        
    }


    public static void main(String[] args) {
        try {
            JFrame f = new JFrame();
            VehiclePanel panel = new VehiclePanel(new AirVehicleConfiguration(), Color.ORANGE);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(panel);
            f.pack();
            f.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace(); System.exit(1);
        }
    }

    public void update(AirVehicleState avs) {

        String ktsSpd = intFormat.format(Unit.MPS.convertTo(avs.getAirspeed(), Unit.KNOTS));
        String ftAlt = intFormat.format(Unit.METER.convertTo(avs.getLocation().getAltitude(), Unit.FEET));
        String fpmVs = intFormat.format(Unit.MPS.convertTo(avs.getVerticalSpeed(), Unit.FPM));

        speedBox.setText(intFormat.format(avs.getAirspeed()) + " MPS ( " + ktsSpd + " KTS)");
        altBox.setText(intFormat.format(avs.getLocation().getAltitude()) + " M ( " + ftAlt + " FT)");
        hdgBox.setText(intFormat.format(Unit.bound360(avs.getHeading())));
        vsBox.setText(intFormat.format(avs.getVerticalSpeed()) + " MPS ( " + fpmVs + " FPM)");
        modeBox.setText(avs.getMode().toString());
        attitude.setOrientation(Math.toRadians(avs.getPitch()), Math.toRadians(avs.getRoll()));
        fuelgauge.setFuel(avs.getEnergyAvailable());

        repaint();
    }


    public static class MiniAttitude extends JComponent {

        double rad_pitch = 0.;
        double rad_roll = 0.;
        int cx = 0;
        int cy = 0;
        static final Color OUTLINE = Color.BLACK;
        static final Color SKY = Colors.getColor("SkyBlue", Color.BLACK);
        static final Color GROUND = Colors.getColor("SaddleBrown", Color.BLACK);

        public MiniAttitude() {
            super();
            setPreferredSize(new Dimension(50, 50));
            setMinimumSize(getPreferredSize());
            cx = 25;
            cy = 25;
            super.setBorder(new LineBorder(OUTLINE));
        }

        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x, y, width, height);
            cx = width / 2;
            cy = height / 2;
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.translate(cx, cy);
            g2.rotate(-rad_roll);
            g2.translate(0, rad_pitch / Math.PI * cy * 2);

            g2.setColor(SKY);
            g2.fillRect(-2 * cx, -2 * cy, 4 * cx, 4 * cy);
            g2.setColor(GROUND);
            g2.fillRect(-2 * cx, 0, 4 * cx, 4 * cy);
            g2.dispose();

            g.setColor(OUTLINE);
            g.drawLine(0, cy, cx / 2, cy);
            g.drawLine(cx + cx / 2, cy, 2 * cx, cy);
            
            

        }

        public void setOrientation(double rad_pitch, double rad_roll) {
            this.rad_pitch = rad_pitch;
            this.rad_roll = rad_roll;
            setToolTipText("Pitch: " + ((int) Math.toDegrees(rad_pitch)) + " Roll: " + ((int) Math.toDegrees(rad_roll)));
            repaint();
        }
    }

    public static class MiniFuel extends JPanel {

        double fuelpct = 50;
        static Color OUTLINE = Color.BLACK;
        static Color FUEL = Color.GREEN;
        static Color BACKGROUND = Color.LIGHT_GRAY;
        int hh, ww;
        String fuelLabel = "50%";

        public MiniFuel() {
            setToolTipText("Energy: " + String.valueOf((int) (fuelpct)) + "%");
        }

        @Override
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x, y, width, height);
            hh = getHeight();
            ww = getWidth() / 4;
        }

        @Override
        public void paintComponent(Graphics g) {

            int fheight = (int) (hh * fuelpct / 100.);

            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(BACKGROUND);
            g.fillRect(ww, 0, ww * 2, hh);
            g.setColor(FUEL);
            g.fillRect(ww, hh - fheight, ww * 2, hh);
            
            g.setColor(OUTLINE);
            int sw = g.getFontMetrics().stringWidth(fuelLabel);
            g.drawString(fuelLabel, 2* ww - sw/2, hh/2);
            
            g.drawRect(ww, 0, 2*ww, hh-1);

        }

        public void setFuel(double fuelpct) {
            this.fuelpct = fuelpct;
            setToolTipText("Energy: " + String.valueOf((int) (fuelpct)) + "%");
            fuelLabel = String.valueOf((int) (fuelpct));
            repaint();
        }
    }


}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */