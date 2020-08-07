// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package nasa.daidalus;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import larcfm.DAIDALUS.*;
import avtas.amase.ui.*;
import avtas.data.Unit;
import avtas.util.Colors;
import java.awt.BasicStroke;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JSeparator;


/**
 *
 * @author AFRL/RQQD
 */
public class WellClearPanel extends JPanel {
    
    double vehicleEntityID;
//    WellClearState vehicleWCState;
    
    // Show the heading conflict/recovery circle
    HeadingCircle headingGauge = new HeadingCircle(vehicleEntityID);
    
    // Establish the bar types
    BandBar altitudeBar = new BandBar("Altitude");
    BandBar grdSpdBar = new BandBar("Ground Speed");
    BandBar vertSpdBar = new BandBar("Vertical Speed");
    
    
    DecimalFormat intFormat = new DecimalFormat("#");    
    

    /** Creates a new instance of WellClearPanel */
    public WellClearPanel(AirVehicleConfiguration avc, Color color) {
        
        vehicleEntityID = avc.getID();
        
        setLayout(new BorderLayout(10, 5));
        setBorder(new EmptyBorder(5,5,5,5));

        JPanel statPanel = new JPanel();
        statPanel.setLayout(new GridLayout(0, 2));
       

        JPanel iconHeadingPanel = new JPanel();
        iconHeadingPanel.setLayout(new BoxLayout(iconHeadingPanel, BoxLayout.LINE_AXIS));
        iconHeadingPanel.setAlignmentY(CENTER_ALIGNMENT);
        
        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.PAGE_AXIS));
        iconPanel.setAlignmentX(CENTER_ALIGNMENT);
        iconPanel.setAlignmentY(CENTER_ALIGNMENT);

        Image icon = IconTools.getFilledImage(IconManager.getIcon(avc), 24, 24, 2, getForeground(), color);
        String vehName = avc.getLabel().toUpperCase() + " (" + avc.getID() + ")";
        JLabel vehicleIcon = new JLabel(vehName, new ImageIcon(icon), JLabel.CENTER);
        vehicleIcon.setVerticalTextPosition(JLabel.BOTTOM);
        vehicleIcon.setHorizontalTextPosition(JLabel.CENTER);
        
        iconPanel.add(vehicleIcon);
   
        iconHeadingPanel.add(Box.createHorizontalGlue());
        iconHeadingPanel.add(iconPanel);
        iconHeadingPanel.add(headingGauge);
        iconHeadingPanel.add(Box.createHorizontalGlue());
        
           
        add(iconHeadingPanel, BorderLayout.NORTH);

        // Build the bar panel section -----------------------------------------
        JPanel barPanel = new JPanel();
        barPanel.setLayout(new GridLayout(1, 3, 5, 5));
        barPanel.add(altitudeBar);
        barPanel.add(grdSpdBar);
        barPanel.add(vertSpdBar);

        
        add(barPanel, BorderLayout.CENTER);
        
    }


    public static void main(String[] args) {
        try {
            JFrame f = new JFrame();
            WellClearPanel panel = new WellClearPanel(new AirVehicleConfiguration(), Color.ORANGE);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(panel);
            f.pack();
            f.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void update(DAIDALUSConfiguration daidalusCfg, WellClearState wcState) {
//        this.vehicleWCState = wcState;
        
//        headingGauge.setOrientation(avs.getHeading());

        // Altitude - DAIDALUSConfiguration-------------------------------------
        altitudeBar.setMinMaxBarRange(daidalusCfg.getMinAltitude(), daidalusCfg.getMaxAltitude());
        altitudeBar.setUnitLabel("");
        altitudeBar.setBottomLabel("m");
        altitudeBar.setConfigFlag(true);
        // ---------------------------------------------------------------------
        
        // Ground Speed - DAIDALUSConfiguration---------------------------------
        grdSpdBar.setMinMaxBarRange(daidalusCfg.getMinGroundSpeed(), daidalusCfg.getMaxGroundSpeed());
        grdSpdBar.setUnitLabel("");
        grdSpdBar.setBottomLabel("m/s");
        grdSpdBar.setConfigFlag(true);
        // ---------------------------------------------------------------------
        
        // Vertical Speed - DAIDALUSConfiguration-------------------------------
        vertSpdBar.setMinMaxBarRange(daidalusCfg.getMinVerticalSpeed(), daidalusCfg.getMaxVerticalSpeed());
        vertSpdBar.setUnitLabel("");
        vertSpdBar.setBottomLabel("m/s");
        vertSpdBar.setConfigFlag(true);
        // ---------------------------------------------------------------------
        
        repaint();
    }
    
    public void update(WellClearState wcState) {
//        this.vehicleWCState = wcState;
        
        headingGauge.setOrientation(wcState.getCurrent(BandType.HEADING));              
        headingGauge.setBands(wcState.getBands(BandType.HEADING), 
                              wcState.getBands(BandType.RECOVERY_HEADING));
        
        altitudeBar.setCurrent(wcState.getCurrent(BandType.ALTITUDE));
        altitudeBar.setBands(wcState.getBands(BandType.ALTITUDE), 
                             wcState.getBands(BandType.RECOVERY_ALTITUDE));
        
        grdSpdBar.setCurrent(wcState.getCurrent(BandType.GROUND_SPEED));
        grdSpdBar.setBands(wcState.getBands(BandType.GROUND_SPEED), 
                           wcState.getBands(BandType.RECOVERY_GROUND_SPEED));
        
        vertSpdBar.setCurrent(wcState.getCurrent(BandType.VERTICAL_SPEED));
        vertSpdBar.setBands(wcState.getBands(BandType.VERTICAL_SPEED), 
                            wcState.getBands(BandType.RECOVERY_VERTICAL_SPEED));
        
        repaint();
        
    }

       
    public static class BandBar extends JPanel {
        
        double current;          // Current vehicle level (prepacked for testing)
        double barInterval_max;     // Bar max interval value (prepacked for testing)
        double barInterval_min;        // Bar min interval value (prepacked for testing)
        double barIntervalRange;        // Difference of min and max
        
        static int NUM_TICKS = 11;
//        static int NUM_MAJOR_TICKS = 3; // Top, Mid, Btm
        static int NUM_MAJOR_TICKS = 2; // Top, Btm    
        
        // Chose to hold the specific band information
        List<BandIntervals.Band> conflictBandsList = new ArrayList<>();
        List<BandIntervals.Band> recoveryBandsList = new ArrayList<>();
        
        static Color OUTLINE = Color.BLACK;
        static Color BACKGROUND = Color.LIGHT_GRAY;
        
        int panelWidth, panelHt;        // Max extents of the panel
        int barHRng_max, barHRng_min;   // Working range for bars in the panel
        int barHRng_diff;               // Working bar range length
        int vertDiv;                    // Vertical slices of the panel width
        
        String barTopLabel = "TEST TOP LABEL";
        String barBtmLabel = "TEST_BTM";
        String barUnits = "TEST_UNITS";
        
        // Draw the current information
        boolean isConfigLoaded = false;
        
        DecimalFormat df = new DecimalFormat("#.#");

        public BandBar(String title_str) {
            //setToolTipText("Energy: " + String.valueOf((int) (fuelpct)) + "%");
            this.barTopLabel = title_str;
            setPreferredSize(new Dimension(100, 250));
            setMinimumSize(getPreferredSize());
             
            barIntervalRange = barInterval_max - barInterval_min;
             
            // Default values until a DAIDALUS Config arrives
            this.barBtmLabel = "";
            this.barUnits = "";
            
            
            // Don't draw current information until a DAIDALUS config arrives
            this.isConfigLoaded = false;

            // In case there are multiple runs (as in playback) reset the panel
            repaint();
        }
        

        public void setBands(List<BandIntervals.Band> conflictBands,
                             List<BandIntervals.Band> recoveryBands) {
            this.conflictBandsList = conflictBands;
            this.recoveryBandsList = recoveryBands;
            repaint();
        }
        
        public void setTitleLabel(String title_str) {
            // already done in the constructor - shouldn't need to use
            this.barTopLabel = title_str;
        }
        
        public void setBottomLabel(String btm_str) {
            this.barBtmLabel = btm_str;
        }
        
        public void setUnitLabel(String unit_str) {
            this.barUnits = unit_str;
        }

        public void setCurrent(double currentVehicleVal) {
            this.current = currentVehicleVal;           
            setToolTipText(barTopLabel + ": " + String.valueOf(df.format(current)));
            repaint();
        }
                
        public void setMinMaxBarRange(double min, double max) {
            this.barInterval_min = min;
            this.barInterval_max = max;
            this.barIntervalRange = max - min;
        }
        
        public void setConfigFlag(boolean flag) {
            this.isConfigLoaded = flag;
        }
        
        public String tickValue(int tickNumber) {
            if (tickNumber == 0) {
                return String.valueOf(df.format(barInterval_max));
            }
            else if (tickNumber == NUM_TICKS-1) {
                return String.valueOf(df.format(barInterval_min));
            }
            
            double incrementVal = barIntervalRange / (NUM_TICKS-1);            
            return String.valueOf(df.format(barInterval_max - (incrementVal * tickNumber)));
        }
        
        public double boundPercent(double input) {
            if (input > 100.0) {
                return 100.0;
            }
            else if (input < 0.0) {
                return 0.0;
            }
            return input;
        }
        
        @Override
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x, y, width, height);
            panelWidth = this.getWidth();
            panelHt = this.getHeight();
            
            barHRng_max = (int)(getHeight() * 0.9);
            barHRng_min = (int)(getHeight() * 0.1);
            barHRng_diff = barHRng_max - barHRng_min;
            vertDiv = getWidth() / 6;
        }

        @Override
        public void paintComponent(Graphics g) {

            int strWidth;
            int strHt = g.getFontMetrics().getHeight();
            
//            Graphics2D g2 = (Graphics2D)g;
//            
//            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
//                               RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
            
            
            // color the background of the complete working panel
            g.setColor(getBackground());
            g.fillRect(0, 0, panelWidth, panelHt);
            
            // Add a background color the bars
            g.setColor(BACKGROUND);
            g.fillRect(vertDiv*2, barHRng_min, vertDiv*2, barHRng_diff);
            
            
            g.setColor(OUTLINE);
            strWidth = g.getFontMetrics().stringWidth(barBtmLabel);            
            g.drawString(barBtmLabel, (vertDiv*3) - (strWidth/2), panelHt - (panelHt-barHRng_max)/2 + (strHt/2));
            
            strWidth = g.getFontMetrics().stringWidth(barTopLabel);           
            g.drawString(barTopLabel, (vertDiv*3) - (strWidth/2), 0 + (barHRng_min/2)+1);
            
            strWidth = g.getFontMetrics().stringWidth(barUnits);
            g.drawString(barUnits, (vertDiv*3) - (strWidth/2), barHRng_min - (strHt/2));
            
            // Draw Bands ------------------------------------------------------
            // Draw the Conflict Bands
            for (BandIntervals.Band band : conflictBandsList) {

                double upper = (band.upper > barInterval_max)  ? barInterval_max : band.upper;
                double lower = (band.lower < barInterval_min)  ? barInterval_min : band.lower;
                
                double upperPtBarPercent = boundPercent((upper - barInterval_min) / barIntervalRange);
                double lowerPtBarPercent = boundPercent((lower - barInterval_min) / barIntervalRange);

                double barHeight = ((upperPtBarPercent * barHRng_diff) - (lowerPtBarPercent * barHRng_diff));
                
                g.setColor(band.getColor());
                g.fillRect(vertDiv*2, (int)(barHRng_max - (upperPtBarPercent * barHRng_diff)), vertDiv, (int)barHeight);

            }
            
            // Draw the Recovery Bands
            for (BandIntervals.Band band : recoveryBandsList) {
                
                double upper = (band.upper > barInterval_max)  ? barInterval_max : band.upper;
                double lower = (band.lower < barInterval_min)  ? barInterval_min : band.lower;
                
                double upperPtBarPercent = boundPercent((upper - barInterval_min) / barIntervalRange);
                double lowerPtBarPercent = boundPercent((lower - barInterval_min) / barIntervalRange);
                              
                double barHeight = ((upperPtBarPercent * barHRng_diff) - (lowerPtBarPercent * barHRng_diff));
                
                g.setColor(band.getColor());
                g.fillRect(vertDiv*3, (int)(barHRng_max - (upperPtBarPercent * barHRng_diff)), vertDiv, (int)barHeight);
            }
            
            // -----------------------------------------------------------------
            
            // Draw Scale ------------------------------------------------------
            // Spine Line
            g.setColor(OUTLINE);
            g.drawLine(vertDiv*3, barHRng_min, vertDiv*3, barHRng_max);
            
            // Ticks and numbers
            for (int tCnt = 0; tCnt < NUM_TICKS; tCnt++) {
                int vertLoc = barHRng_min + (barHRng_diff / (NUM_TICKS -1))*tCnt;
                boolean isMajorTick = (tCnt % ((NUM_TICKS-1)/(NUM_MAJOR_TICKS-1)) == 0);
                
                if (isMajorTick) {
                    g.drawLine(vertDiv*2, vertLoc, vertDiv*4-1, vertLoc);                    
                } 
                else {
                    g.drawLine((int)(vertDiv*2.8), vertLoc, (int)(vertDiv*3.2), vertLoc);
                }
                String tickVal_str = tickValue(tCnt);
                g.drawString(tickVal_str, (int)(vertDiv*4.2), vertLoc+(strHt/2)-2);
            }
            // -----------------------------------------------------------------
                       
            // Draw the current bar value
            if (isConfigLoaded) {
                g.setColor(Color.MAGENTA);
                double currentPercent = ((current - barInterval_min) / barIntervalRange);
                double currentBarLoc = barHRng_max - (barHRng_diff * currentPercent);
                g.drawLine((int)(vertDiv*1.8), (int)(currentBarLoc), (int)(vertDiv*4.2), (int)(currentBarLoc));

                // Add the current value to the left of the bars            
                String currVal_str = String.valueOf(df.format(current));
                strWidth = g.getFontMetrics().stringWidth(currVal_str);            
                g.drawString(currVal_str, (int)((vertDiv * 1.8) - strWidth -2), (int)(currentBarLoc +(strHt/2)-2));
            }
             

        }
    }
    
    public static class HeadingCircle extends JComponent {
        
        static final int TICK_LENGTH = 6;
        static final int ANCHOR_OFFSET = 12;
        static final BasicStroke scaleStroke = new BasicStroke(1);
        
        double currentHeading_deg = 0;
        
        // Chose to hold the specific band information 
        List<BandIntervals.Band> conflictBandsList = new ArrayList<>();
        List<BandIntervals.Band> recoveryBandsList = new ArrayList<>();
        
        int panelWidth, panelHt;
        int panelCtrX, panelCtrY;
        int circleCtrX, circleCtrY;
        
        int width = 0;
        int height = 0;
        
        double tickOffset = 30;
        
        DecimalFormat df = new DecimalFormat("#.##");

        public HeadingCircle(double vehID) {
            super();
            setPreferredSize(new Dimension(200, 175));
            setMinimumSize(getPreferredSize());
            width = 120;
            height = 120;
                                  
//            // Temp for alignment
//            super.setBorder(new LineBorder(OUTLINE));
//            width = 240;
//            height = 240;
        }
        
        /* The graphics draw sets 0.0 at east with positive numbers progressing counter-clockwise.
         * Aircraft 0.0 is due north with positive numbers progressing clockwise.
         *
         * Therefore, when an aircraft measure is given we should ensure it is bounded 
         * and that it is translated to draw coordinates.
         */
        public double degreesAircraftToDraw(double aircraft_deg) {
            // Shift 0.0 in AC to 0.0 to drawing ==> +90
            // Change direction of the numberline ==> * -1
            
//            return (aircraft_deg * -1) + 90;
            double rtnDrawAngle = 90 - aircraft_deg;
            if (rtnDrawAngle < 0) {
                return rtnDrawAngle + 360;
            }            
            return rtnDrawAngle;
        }
        
        public double degreesAircraftBand(double lower_deg, double upper_deg) {
            // return values are multipled by negative one since the math degrees
            // are positive in the clockwise (CW) direction but aircraft degrees
            // are positive in the counter-clockwise (CCW) direction
            
            // The band information is always provided in the proper order
            // - Only way that upper can be less than lower is if that end has restarted the circle
            // - Add 360 to upper to represent the band length
            if (upper_deg < lower_deg) {
                return -1.0 * ((upper_deg + 360) - lower_deg);
            }
            return -1.0 * (upper_deg - lower_deg);
            
        }
        
        public double actualToDisplay(double actual_deg) {
            double valToRtn_deg = actual_deg - currentHeading_deg;
            
            if (valToRtn_deg < 0.0) {
                return valToRtn_deg + 360;
            }
            else if (valToRtn_deg >= 360) {         // Shouldn't need
                return valToRtn_deg - 360;
            }
            
            return valToRtn_deg;
        }
        
        public void setBands(List<BandIntervals.Band> conflictBands,
                             List<BandIntervals.Band> recoveryBands) {
            this.conflictBandsList = conflictBands;
            this.recoveryBandsList = recoveryBands;
            repaint();
        }
                
        public void drawTicks(Graphics2D graphicsObj, final int theta, final double radius) {
           
            double insidePointX;
            double insidePointY;
            double outsidePointX;
            double outsidePointY;
            
            double[] labelCtrPtXY = new double[2];
            
            // To rotate around the center, must move around the center changing quadrants
            if (theta >= 0 && theta < 90) {
                insidePointX = circleCtrX + (Math.sin(Math.toRadians(theta)) * (radius - TICK_LENGTH/2));
                insidePointY = circleCtrY - (Math.cos(Math.toRadians(theta)) * (radius - TICK_LENGTH/2));
                outsidePointX = circleCtrX + (Math.sin(Math.toRadians(theta)) * (radius + TICK_LENGTH/2));
                outsidePointY = circleCtrY - (Math.cos(Math.toRadians(theta)) * (radius + TICK_LENGTH/2));
                
                labelCtrPtXY[0] = circleCtrX + (Math.sin(Math.toRadians(theta)) * (radius + TICK_LENGTH/2 +ANCHOR_OFFSET));
                labelCtrPtXY[1] = circleCtrY - (Math.cos(Math.toRadians(theta)) * (radius + TICK_LENGTH/2 +ANCHOR_OFFSET));
            } 
            else if (theta >= 90 && theta < 180) {
                insidePointX = circleCtrX + (Math.cos(Math.toRadians(theta-90)) * (radius - TICK_LENGTH/2));
                insidePointY = circleCtrY + (Math.sin(Math.toRadians(theta-90)) * (radius - TICK_LENGTH/2));
                outsidePointX = circleCtrX + (Math.cos(Math.toRadians(theta-90)) * (radius + TICK_LENGTH/2));
                outsidePointY = circleCtrY + (Math.sin(Math.toRadians(theta-90)) * (radius + TICK_LENGTH/2));
                
                labelCtrPtXY[0] = circleCtrX + (Math.cos(Math.toRadians(theta-90)) * (radius + TICK_LENGTH/2 +ANCHOR_OFFSET));
                labelCtrPtXY[1] = circleCtrY + (Math.sin(Math.toRadians(theta-90)) * (radius + TICK_LENGTH/2 +ANCHOR_OFFSET));
            }
            else if (theta >= 180 && theta < 270) {
                insidePointX = circleCtrX - (Math.sin(Math.toRadians(theta-180)) * (radius - TICK_LENGTH/2));
                insidePointY = circleCtrY + (Math.cos(Math.toRadians(theta-180)) * (radius - TICK_LENGTH/2));
                outsidePointX = circleCtrX - (Math.sin(Math.toRadians(theta-180)) * (radius + TICK_LENGTH/2));
                outsidePointY = circleCtrY + (Math.cos(Math.toRadians(theta-180)) * (radius + TICK_LENGTH/2));
                
                labelCtrPtXY[0] = circleCtrX - (Math.sin(Math.toRadians(theta-180)) * (radius + TICK_LENGTH/2 +ANCHOR_OFFSET));
                labelCtrPtXY[1] = circleCtrY + (Math.cos(Math.toRadians(theta-180)) * (radius + TICK_LENGTH/2 +ANCHOR_OFFSET)); 
            }
            else { // theta >= 270 && theta < 360
                insidePointX = circleCtrX - (Math.cos(Math.toRadians(theta-270)) * (radius - TICK_LENGTH/2));
                insidePointY = circleCtrY - (Math.sin(Math.toRadians(theta-270)) * (radius - TICK_LENGTH/2));
                outsidePointX = circleCtrX - (Math.cos(Math.toRadians(theta-270)) * (radius + TICK_LENGTH/2));
                outsidePointY = circleCtrY - (Math.sin(Math.toRadians(theta-270)) * (radius + TICK_LENGTH/2));
                
                labelCtrPtXY[0] = circleCtrX - (Math.cos(Math.toRadians(theta-270)) * (radius + TICK_LENGTH/2 +ANCHOR_OFFSET));
                labelCtrPtXY[1] = circleCtrY - (Math.sin(Math.toRadians(theta-270)) * (radius + TICK_LENGTH/2 +ANCHOR_OFFSET)); 
            }
            
            Line2D.Double tickLine = new Line2D.Double(insidePointX, insidePointY, outsidePointX, outsidePointY);
            graphicsObj.draw(tickLine);
            
//            // Testing
//            graphicsObj.setColor(Color.RED);
//            graphicsObj.drawLine((int)labelCtrPtXY[0], (int)labelCtrPtXY[1], (int)labelCtrPtXY[0], (int)labelCtrPtXY[1]);
//            graphicsObj.drawString(String.valueOf(theta), (int)labelCtrPtXY[0], (int)labelCtrPtXY[1]);
//            graphicsObj.setColor(Color.GREEN);
//            graphicsObj.drawLine((int)labelCtrPtXY[0], (int)labelCtrPtXY[1]-15, (int)labelCtrPtXY[0], (int)labelCtrPtXY[1]-15);            
//            graphicsObj.drawString(String.valueOf(theta), (int)labelCtrPtXY[0], (int)labelCtrPtXY[1]-15);
            
            graphicsObj.setColor(Color.BLACK);
            
            drawTickLabel(graphicsObj, theta, labelCtrPtXY);
        }
        
        public void drawTickLabel(Graphics2D graphicsObj, final int theta, final double[] coordinateXY) {
            int strHt = graphicsObj.getFontMetrics().getHeight();            
            int strWidth;
            
            int tickVal;
            
            graphicsObj.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                        RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
            
            if (theta >= 0.0 && theta <= 180) {
                tickVal = theta;
            } else {
                tickVal = -1* (360 - theta);
            }
            
            strWidth = graphicsObj.getFontMetrics().stringWidth(String.valueOf(tickVal));
            graphicsObj.drawString(String.valueOf(tickVal), (float)(coordinateXY[0] - strWidth/2), (float)(coordinateXY[1] + strHt/3));
            
        }
     
        @Override
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x, y, width, height);
            panelWidth = this.getWidth();
            panelHt = this.getHeight();
            
            panelCtrX = (int)(panelWidth / 2);
            panelCtrY = (int)(panelHt / 2);
            
            circleCtrX = panelCtrX;
            circleCtrY = panelCtrY;    
            
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            
            // Anchor of circle is the upper left of the bounding box
            int circleAnchorX = circleCtrX - (width/2);
            int circleAnchorY = circleCtrY - (height/2);
            
            
            g.drawOval(circleAnchorX, circleAnchorY, width, height);
            
            // Temporary for alignment
//            g.drawLine(0, 0, panelWidth, panelHt);
//            g.drawLine(panelWidth, 0, 0, panelHt);
            
//            int insideCircleAnchorX = circleCtrX - (width-20)/2;
//            int insideCircleAnchorY = circleCtrY - (height-20)/2;
            
            Ellipse2D.Double ring = new Ellipse2D.Double (circleAnchorX, circleAnchorY, width, height);
            Rectangle2D.Double ellipseBounds = new Rectangle2D.Double(circleAnchorX, circleAnchorY, width, height);

            Graphics2D g2 = (Graphics2D)g;
            
            // Draw Bands ------------------------------------------------------
            /* Note on the adjusted degrees: 
             * - The stroke makes a larger dot rather than a "wide line" therefore the termination of the line exceeds past the range.
             * - To counter this, it was determined that the a reduction in the length of the stroke by 5 deg on each side of the bar appears to 
             * resolve the distance.
             * - A new issue is if the band is not 10 degrees or more a special case must be determined.
             */
            // Draw the Conflict Bands
            for (BandIntervals.Band band : conflictBandsList) {
                
                Arc2D.Double conflictArc;
                g2.setColor(band.getColor());
                
//                System.out.println("-------------------------");
//                System.out.println("Conflict Band");
//                System.out.println("Curr Hdg: " + currentHeading_deg);
//                System.out.println("lower: " + band.lower);
//                System.out.println("upper: " + band.upper);
//                System.out.println("-------------------------");
                
                conflictArc = new Arc2D.Double(ellipseBounds, 
                                               degreesAircraftToDraw(actualToDisplay(band.lower)), 
                                               degreesAircraftBand(band.lower,band.upper), 
                                               Arc2D.PIE);
                g2.draw(conflictArc);
                g2.fill(conflictArc);
            }
            
            // Draw the Recovery Bands
            for (BandIntervals.Band band : recoveryBandsList) {
                
                Arc2D.Double recoveryArc;
                g2.setColor(band.getColor());
                recoveryArc = new Arc2D.Double(ellipseBounds, 
                                               degreesAircraftToDraw(actualToDisplay(band.lower)), 
                                               degreesAircraftBand(band.lower,band.upper), 
                                               Arc2D.PIE);
                g2.draw(recoveryArc);
                g2.fill(recoveryArc);
            }
            
            // -----------------------------------------------------------------
            
            // Draw the scale --------------------------------------------------
            g2.setColor(Color.BLACK);
            g2.setStroke(scaleStroke);
            g2.draw(ring);
            
            for (int theta = 0; theta < 360; theta += tickOffset) {
                drawTicks(g2, theta, width/2);
            }
            
//            System.out.println("--------------------------------------");
//            System.out.println("Heading Line - VID: " + vehicleEntityID);
//            System.out.println("Curr Hdg: " + currentHeading_deg);
//            System.out.println("degACtoD: " + degreesAircraftToDraw(currentHeading_deg));
//            System.out.println("actToDis: " + actualToDisplay(degreesAircraftToDraw(currentHeading_deg)));
//            System.out.println("--------------------------------------");

            // To draw heading line
            // 1) Get current heading - stored as currentHeading_deg
            // 2) Zero the current heading - use function actualToDisplay
            // 3) Convert the zero from AC degrees to Draw degrees - use function degreesAircraftToDraw
            Arc2D.Double HeadingLine = new Arc2D.Double(ellipseBounds, 
                                                        degreesAircraftToDraw(actualToDisplay(currentHeading_deg)), 
                                                        0, Arc2D.PIE);
            g2.setColor(Color.BLACK);
            g2.setStroke(scaleStroke);
            g2.draw(HeadingLine);

        }

        public void setOrientation(double heading_deg) {
            this.currentHeading_deg = heading_deg;           
            
            // Heading is always drawn at straight up.
            // - Therefore the heading must be adjusted to properly be displayed
            // - This is done in the paintComponent function
            setToolTipText("Actual Heading: " + df.format(heading_deg));
            
            repaint();
        }
    }


}