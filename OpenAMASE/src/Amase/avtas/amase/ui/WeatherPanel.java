// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.ui;

import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import afrl.cmasi.WeatherReport;
import avtas.amase.AmasePlugin;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.RenderingHints;

/**
 *
 * @author AFRL/RQQD
 */
public class WeatherPanel extends AmasePlugin {

    PointerBox pointerBox = new PointerBox();
    JLabel windLabel = new JLabel("Wind Calm");
    JPanel panel = new JPanel();

    public WeatherPanel() {
        panel.add(pointerBox);
        panel.add(windLabel);
    }

    public void eventOccurred(Object evt) {
        if (evt instanceof WeatherReport) {
            WeatherReport wr = (WeatherReport) evt;
            setWindState(wr.getWindSpeed(), wr.getWindDirection());
        } else if (evt instanceof SessionStatus) {
            if (((SessionStatus) evt).getState() == SimulationStatusType.Reset) {
                setWindState(0, 0);
            }
        }
    }

    void setWindState(double magnitude_mps, double direction_deg) {
        if (magnitude_mps == 0) {
            windLabel.setText("Wind Calm");
        } else {
            windLabel.setText("Wind " + String.valueOf(direction_deg) + " deg,  "
                    + String.valueOf(magnitude_mps) + " m/s");
        }
        pointerBox.setAngle(-Math.toRadians((direction_deg + 180)));
    }

    static class PointerBox extends JComponent {

        private double angleRad = 0;
        private static int size = 24;
        private Image img;

        public PointerBox() {
            setPreferredSize(new Dimension(size + 2, size + 2));
            try {
                img = ImageIO.read(getClass().getResource("/resources/arrow.png"));
                img = img.getScaledInstance(size / 2, size - 2, Image.SCALE_SMOOTH);
            } catch (IOException ex) {
                ex.printStackTrace(); System.exit(1);
            }

        }

        public void setAngle(double angleRad) {
            this.angleRad = angleRad;
            repaint();
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.rotate(-angleRad, size / 2, size / 2);
            g2.drawImage(img, null, null);
        }
    }

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, new WeatherPanel());
    }

    // GUIPlugin methods
    public Component getGui() {
        return panel;
    }

    public JMenu[] getMenuItems() {
        return null;
    }

    public JComponent getToolBar() {
        return null;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */