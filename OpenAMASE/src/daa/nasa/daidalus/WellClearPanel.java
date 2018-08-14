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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
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
    WellClearState vehicleWCState;
    
    // Show the heading conflict/recovery circle
    HeadingCircle headingGauge = new HeadingCircle();
    
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
        
//        JPanel textArea = new JPanel(new BorderLayout(5,0));
//        JPanel labelPanel = new JPanel(new GridLayout(0, 1));
//        
//        labelPanel.add(new JLabel("Altitude"));
//        labelPanel.add(new JLabel("Speed"));
//        labelPanel.add(new JLabel("Heading"));
//        labelPanel.add(new JLabel("Vert Speed"));
//        labelPanel.add(new JLabel("Nav Mode"));
//        
//        textArea.add(labelPanel, BorderLayout.WEST);
//        
//        JPanel fieldPanel = new JPanel(new GridLayout(0,1));
//        fieldPanel.add(altBox);
//        fieldPanel.add(speedBox);
//        fieldPanel.add(hdgBox);
//        fieldPanel.add(vsBox);
//        fieldPanel.add(modeBox);
//        textArea.add(fieldPanel, BorderLayout.CENTER);



        // Trying to layer the guage and icon
//        JLayeredPane layerPane = new JLayeredPane();
//        layerPane.add(headingGauge, JLayeredPane.DEFAULT_LAYER);
        
        
        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new BorderLayout());

        Image icon = IconTools.getFilledImage(IconManager.getIcon(avc), 24, 24, 2, getForeground(), color);
        String vehName = avc.getLabel().toUpperCase() + " (" + avc.getID() + ")";
        JLabel vehicleIcon = new JLabel(vehName, new ImageIcon(icon), JLabel.CENTER);
        vehicleIcon.setVerticalTextPosition(JLabel.BOTTOM);
        vehicleIcon.setHorizontalTextPosition(JLabel.CENTER);
        iconPanel.add(vehicleIcon, BorderLayout.CENTER);
//        iconPanel.add(Box.createHorizontalStrut(20), BorderLayout.EAST);
//        iconPanel.add(Box.createHorizontalStrut(20), BorderLayout.WEST);
        add(iconPanel, BorderLayout.NORTH);
        
//        layerPane.add(vehicleIcon, JLayeredPane.PALETTE_LAYER);
        
        JPanel headingPanel = new JPanel();        
        headingPanel.setLayout(new BorderLayout());
        headingPanel.add(headingGauge, BorderLayout.CENTER);    
//        add(headingPanel, BorderLayout.NORTH);
       

        // Build the bar panel section
        JPanel barPanel = new JPanel();
        barPanel.setLayout(new GridLayout(1, 3, 5, 5));
        barPanel.add(altitudeBar);
        barPanel.add(grdSpdBar);
        barPanel.add(vertSpdBar);

        add(barPanel, BorderLayout.CENTER);
//        add(new JSeparator(JSeparator.VERTICAL),BorderLayout.EAST);
        
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

    public void update(AirVehicleState avs) {

//        String ktsSpd = intFormat.format(Unit.MPS.convertTo(avs.getAirspeed(), Unit.KNOTS));
//        String ftAlt = intFormat.format(Unit.METER.convertTo(avs.getLocation().getAltitude(), Unit.FEET));
//        String fpmVs = intFormat.format(Unit.MPS.convertTo(avs.getVerticalSpeed(), Unit.FPM));
//
//        speedBox.setText(intFormat.format(avs.getAirspeed()) + " MPS ( " + ktsSpd + " KTS)");
//        altBox.setText(intFormat.format(avs.getLocation().getAltitude()) + " M ( " + ftAlt + " FT)");
//        hdgBox.setText(intFormat.format(Unit.bound360(avs.getHeading())));
//        vsBox.setText(intFormat.format(avs.getVerticalSpeed()) + " MPS ( " + fpmVs + " FPM)");
//        modeBox.setText(avs.getMode().toString());
        
        headingGauge.setOrientation(avs.getHeading());
        altitudeBar.setCurrent(avs.getLocation().getAltitude());
        grdSpdBar.setCurrent(avs.getGroundspeed());
        vertSpdBar.setCurrent(avs.getVerticalSpeed());        

        repaint();
    }
    
    public void update(DAIDALUSConfiguration daidalusCfg, WellClearState wcState) {
        this.vehicleWCState = wcState;
        
//        headingGauge.setOrientation(avs.getHeading());

        // Altitude - DAIDALUSConfiguration-------------------------------------
        altitudeBar.setMinMaxBarRange(daidalusCfg.getMinAltitude(), daidalusCfg.getMaxAltitude());
        altitudeBar.setUnitLabel("m");
        altitudeBar.setBottomLabel("");
        // ---------------------------------------------------------------------
        
        // Ground Speed - DAIDALUSConfiguration---------------------------------
        grdSpdBar.setMinMaxBarRange(daidalusCfg.getMinGroundSpeed(), daidalusCfg.getMaxGroundSpeed());
        grdSpdBar.setUnitLabel("m/s");
        grdSpdBar.setBottomLabel("");
        // ---------------------------------------------------------------------
        
        // Vertical Speed - DAIDALUSConfiguration-------------------------------
        vertSpdBar.setMinMaxBarRange(daidalusCfg.getMinVerticalSpeed(), daidalusCfg.getMaxVerticalSpeed());
        vertSpdBar.setUnitLabel("m/s");
        vertSpdBar.setBottomLabel("");
        // ---------------------------------------------------------------------
    }
    
    public void update(WellClearState wcState) {
        this.vehicleWCState = wcState;
        
//        headingGauge.setBands(wcState.getBands(BandType.HEADING), 
//                              wcState.getBands(BandType.RECOVERY_HEADING));
        
        altitudeBar.setBands(wcState.getBands(BandType.ALTITUDE), 
                             wcState.getBands(BandType.RECOVERY_ALTITUDE));
        
        grdSpdBar.setBands(wcState.getBands(BandType.GROUND_SPEED), 
                           wcState.getBands(BandType.RECOVERY_GROUND_SPEED));
        
        vertSpdBar.setBands(wcState.getBands(BandType.VERTICAL_SPEED), 
                            wcState.getBands(BandType.RECOVERY_VERTICAL_SPEED));
        
        repaint();
        
    }


       
    public static class BandBar extends JPanel {
        
        double current = 5500.515;          // Current vehicle level (prepacked for testing)
        double barInterval_max = 15240;     // Bar max interval value (prepacked for testing)
        double barInterval_min = 30;        // Bar min interval value (prepacked for testing)
        double barIntervalRange;        // Difference of min and max
        
        static int NUM_TICKS = 11;
        static int NUM_MAJOR_TICKS = 3; // Top, Mid, Btm    
        
        // Don't need, have access to WellClearState
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
        
        DecimalFormat df = new DecimalFormat("#.#");

        public BandBar(String title_str) {
            //setToolTipText("Energy: " + String.valueOf((int) (fuelpct)) + "%");
            this.barTopLabel = title_str;
            setPreferredSize(new Dimension(100, 400));
            setMinimumSize(getPreferredSize());
             
            barIntervalRange = barInterval_max - barInterval_min;
             
            // Default values until a DAIDALUS Config arrives
            this.barBtmLabel = "";
            this.barUnits = "";
             
//            this.current = 5;              // Current vehicle level (prepacked for testing)
//            this.barInterval_max = 10;     // Bar max interval value (prepacked for testing)
//            this.barInterval_min = 0;      // Bar min interval value (prepacked for testing)
             
            // In case there are multiple runs (as in playback) reset the panel
            repaint();
             
            // testing
//            conflictBandsList = testConflictBands().getBands();
//            recoveryBandsList = testRecoveryBands().getBands();
        }
        
//        public BandIntervals testConflictBands() {
//            List<BandIntervals.Band> testBandsList = new ArrayList<>(); 
//            List<BandIntervals> intervalList = new ArrayList<>();
//            
//            testBandsList.add(new BandIntervals.Band(9000, 15240, BandsRegion.FAR));
//            testBandsList.add(new BandIntervals.Band(30, 2000, BandsRegion.FAR));
//            testBandsList.add(new BandIntervals.Band(6000, 9000, BandsRegion.MID));
//            testBandsList.add(new BandIntervals.Band(2000, 4000, BandsRegion.MID));
//            testBandsList.add(new BandIntervals.Band(4000, 6000, BandsRegion.NEAR));
//                
//            return new BandIntervals(5500, testBandsList);
//        }
//        
//        public BandIntervals testRecoveryBands() {
//            List<BandIntervals.Band> testBandsList = new ArrayList<>(); 
//            List<BandIntervals> intervalList = new ArrayList<>();
//            
//            testBandsList.add(new BandIntervals.Band(3500, 8000));
//                
//            return new BandIntervals(5500, testBandsList);
//        }
       
        
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
        
        public String tickValue(int tickNumber) {
            if (tickNumber == 0) {
                return String.valueOf(barInterval_max);
            }
            else if (tickNumber == NUM_TICKS-1) {
                return String.valueOf(barInterval_min);
            }
            
            double incrementVal = barIntervalRange / (NUM_TICKS-1);            
            return String.valueOf(df.format(barInterval_max - (incrementVal * tickNumber)));
        }
        
        @Override
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x, y, width, height);
            panelWidth = this.getWidth();
            panelHt = this.getHeight();
            
            barHRng_max = (int)(getHeight() * 0.95);
            barHRng_min = (int)(getHeight() * 0.1);
            barHRng_diff = barHRng_max - barHRng_min;
            vertDiv = getWidth() / 6;
        }

        @Override
        public void paintComponent(Graphics g) {

            int strWidth;
            int strHt = g.getFontMetrics().getHeight();
            
            
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

                double upperPtBarPercent = ((band.upper - barInterval_min) / barIntervalRange);
                double lowerPtBarPercent = ((band.lower - barInterval_min) / barIntervalRange);

                double barHeight = ((upperPtBarPercent * barHRng_diff) - (lowerPtBarPercent * barHRng_diff));
                
                g.setColor(band.getColor());
                g.fillRect(vertDiv*2, (int)(barHRng_max - (upperPtBarPercent * barHRng_diff)), vertDiv, (int)barHeight);

            }
            
            // Draw the Recovery Bands
            for (BandIntervals.Band band : recoveryBandsList) {
                double upperPtBarPercent = ((band.upper - barInterval_min) / barIntervalRange);
                double lowerPtBarPercent = ((band.lower - barInterval_min) / barIntervalRange);
                
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
    
    public static class HeadingCircle extends JComponent {

        static final int strokeWidth = 16;
        static final BasicStroke scaleStroke = new BasicStroke(2);
        static final BasicStroke bandStroke = new BasicStroke(strokeWidth);
        double currentHeading_deg = 0;
        
        // Don't need, have access to WellClearState
        List<BandIntervals.Band> conflictBandsList = new ArrayList<>();
        List<BandIntervals.Band> recoveryBandsList = new ArrayList<>();
        
        int panelWidth, panelHt;
        int panelCtrX, panelCtrY;
        int circleCtrX, circleCtrY;
        
        double rad_roll = 0.;
        int width = 0;
        int height = 0;
        
        double tickOffset = 30;        
        
        
        static final Color OUTLINE = Color.BLACK;
        static final Color SKY = Colors.getColor("SkyBlue", Color.BLACK);
        static final Color GROUND = Colors.getColor("SaddleBrown", Color.BLACK);

        public HeadingCircle() {
            super();
            setPreferredSize(new Dimension(200, 250));
            setMinimumSize(getPreferredSize());
            width = 180;
            height = 180;
            
            // Temp for alignment
            super.setBorder(new LineBorder(OUTLINE));
        }
        
        public HeadingCircle(int width, int height) {
            super();
            setPreferredSize(new Dimension(200, 250));
            setMinimumSize(getPreferredSize());
            this.width = width;
            this.height = height;
            
            // Temp for alignment
            super.setBorder(new LineBorder(OUTLINE));
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
            return (aircraft_deg * -1) + 90;
        }
        
        public double degreesAircraftBand(double min, double max) {
            // Order is on purpose since the math degrees move in CW and aircraft in CCW
            return (min - max);
        }
        
        public void setBands(List<BandIntervals.Band> conflictBands,
                             List<BandIntervals.Band> recoveryBands) {
            this.conflictBandsList = conflictBands;
            this.recoveryBandsList = recoveryBands;
            repaint();
        }
        
        public Line2D.Double drawTicks(final int theta) {
            
            double radius = width/2; // height and width are assumed equal
            
            double insidePointX;
            double insidePointY;
            double outsidePointX;
            double outsidePointY;
            
            // To rotate around the center, must move around the center changing quadrants
            if (theta >= 0 && theta < 90) {
                insidePointX = circleCtrX + (Math.sin(Math.toRadians(theta)) * (radius - strokeWidth/2));
                insidePointY = circleCtrY - (Math.cos(Math.toRadians(theta)) * (radius - strokeWidth/2));
                outsidePointX = circleCtrX + (Math.sin(Math.toRadians(theta)) * (radius + strokeWidth/2));
                outsidePointY = circleCtrY - (Math.cos(Math.toRadians(theta)) * (radius + strokeWidth/2));
            } 
            else if (theta >= 90 && theta < 180) {
                insidePointX = circleCtrX + (Math.cos(Math.toRadians(theta-90)) * (radius - strokeWidth/2));
                insidePointY = circleCtrY + (Math.sin(Math.toRadians(theta-90)) * (radius - strokeWidth/2));
                outsidePointX = circleCtrX + (Math.cos(Math.toRadians(theta-90)) * (radius + strokeWidth/2));
                outsidePointY = circleCtrY + (Math.sin(Math.toRadians(theta-90)) * (radius + strokeWidth/2));            
            }
            else if (theta >= 180 && theta < 270) {
                insidePointX = circleCtrX - (Math.sin(Math.toRadians(theta-180)) * (radius - strokeWidth/2));
                insidePointY = circleCtrY + (Math.cos(Math.toRadians(theta-180)) * (radius - strokeWidth/2));
                outsidePointX = circleCtrX - (Math.sin(Math.toRadians(theta-180)) * (radius + strokeWidth/2));
                outsidePointY = circleCtrY + (Math.cos(Math.toRadians(theta-180)) * (radius + strokeWidth/2));                
            }
            else {
                insidePointX = circleCtrX - (Math.cos(Math.toRadians(theta-270)) * (radius - strokeWidth/2));
                insidePointY = circleCtrY - (Math.sin(Math.toRadians(theta-270)) * (radius - strokeWidth/2));
                outsidePointX = circleCtrX - (Math.cos(Math.toRadians(theta-270)) * (radius + strokeWidth/2));
                outsidePointY = circleCtrY - (Math.sin(Math.toRadians(theta-270)) * (radius + strokeWidth/2));                
            }
            
            return new Line2D.Double(insidePointX, insidePointY, outsidePointX, outsidePointY);
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
            int outsideCircleAnchorX = circleCtrX - (width/2);
            int outsideCircleAnchorY = circleCtrY - (height/2);
            
            
            g.drawOval(outsideCircleAnchorX, outsideCircleAnchorY, width, height);
            
            // Temporary for alignment
            g.drawLine(0, 0, panelWidth, panelHt);
            g.drawLine(panelWidth, 0, 0, panelHt);
            
            int insideCircleAnchorX = circleCtrX - (width-20)/2;
            int insideCircleAnchorY = circleCtrY - (height-20)/2;
            
            Ellipse2D.Double ring = new Ellipse2D.Double (outsideCircleAnchorX, outsideCircleAnchorY, width, height);
            Rectangle2D.Double ellipseBounds = new Rectangle2D.Double(outsideCircleAnchorX, outsideCircleAnchorY, width, height);
            
            
          
//            // Note that to get the 120 deg arc using stroke I have to end the shorten the arc and start it 5 degrees forward in the scale
//            Arc2D.Double conflictArc = new Arc2D.Double(ellipseBounds, degreesAircraftToDraw(5), degreesAircraftBand(0,120-10), 0);
//            Arc2D.Double conflictArc2 = new Arc2D.Double(ellipseBounds, degreesAircraftToDraw(125), degreesAircraftBand(0,120-10), 0);
//            Arc2D.Double conflictArc3 = new Arc2D.Double(ellipseBounds, degreesAircraftToDraw(245), degreesAircraftBand(0,120-10), 0);
            Graphics2D g2 = (Graphics2D)g;
            
            
//            g2.setColor(Color.GREEN);
//            g2.setStroke(bandStroke);
//            g2.draw(conflictArc);
//            
//            g2.setColor(Color.RED);            
//            g2.draw(conflictArc2);
//            
//            g2.setColor(Color.CYAN);            
//            g2.draw(conflictArc3);
            
//            g2.setColor(Color.BLACK);
//            g2.setStroke(scaleStroke);
//            g2.draw(conflictArc);

            // Draw Bands ------------------------------------------------------
            /* Note on the adjusted degrees: 
             * - The stroke makes a larger dot rather than a "wide line" therefore the termination of the line exceeds past the range.
             * - To counter this, it was determined that the a reduction in the length of the stroke by 5 deg on each side of the bar appears to 
             * resolve the distance.
             * - A new issue is if the band is not 10 degrees or more a special case must be determined.
             */
            g2.setStroke(bandStroke);
            // Draw the Conflict Bands
            for (BandIntervals.Band band : conflictBandsList) {
                double bandStart_deg = (band.lower + 5) % 360;          // Maintain 0 to 360
                double bandExtent_deg = band.upper - band.lower - 10;
                
                Arc2D.Double conflictArc;
                g2.setColor(band.getColor());
                
                // The band extent would be negative, draw a single point at the middle of the range.
                if ((band.upper - band.lower) < 10) {
                    double midPt = (band.upper - band.lower)/2;
                    conflictArc = new Arc2D.Double(ellipseBounds, degreesAircraftToDraw(midPt), degreesAircraftBand(midPt,1), 0);
                    
                }
                else {
                    conflictArc = new Arc2D.Double(ellipseBounds, degreesAircraftToDraw(bandStart_deg), degreesAircraftBand(bandStart_deg,bandExtent_deg), 0);
                }
                
                g2.draw(conflictArc);
            }
            
            // Draw the Recovery Bands
            for (BandIntervals.Band band : recoveryBandsList) {
                double bandStart_deg = (band.lower + 5) % 360;          // Maintain 0 to 360
                double bandExtent_deg = band.upper - band.lower - 10;
                
                Arc2D.Double recoveryArc;
                g2.setColor(band.getColor());
                
                // The band extent would be negative, draw a single point at the middle of the range.
                if ((band.upper - band.lower) < 10) {
                    double midPt = (band.upper - band.lower)/2;
                    recoveryArc = new Arc2D.Double(ellipseBounds, degreesAircraftToDraw(midPt), degreesAircraftBand(midPt,1), 0);
                    
                }
                else {
                    recoveryArc = new Arc2D.Double(ellipseBounds, degreesAircraftToDraw(bandStart_deg), degreesAircraftBand(bandStart_deg,bandExtent_deg), 0);
                }
                
                g2.draw(recoveryArc);
            }
            
            // -----------------------------------------------------------------
            
            // Draw the scale --------------------------------------------------
            g2.setColor(Color.BLACK);
            g2.setStroke(scaleStroke);
            g2.draw(ring);
            
            for (int theta = 0; theta < 360; theta += tickOffset) {
                Line2D.Double tickMark = drawTicks(theta);
                g2.draw(tickMark);                
                
                // TODO: Figure out how to write the values here
            }
            
            // Draw heading line - Use arc for ease of establishing the line
            
            Arc2D.Double HeadingLine = new Arc2D.Double(ellipseBounds, degreesAircraftToDraw(currentHeading_deg), 0, 2);
            g2.setColor(Color.BLACK);
            g2.setStroke(scaleStroke);
            g2.draw(HeadingLine);
            
            // Draw the aircraft icon
//           Image icon = IconTools.getFilledImage(IconManager.getIcon(avc), 24, 24, 2, altBox.getForeground(), color);
//           String vehName = avc.getLabel().toUpperCase() + " (" + avc.getID() + ")";        
//           JLabel vehicleIcon = new JLabel(vehName, new ImageIcon(icon), JLabel.CENTER);
//           vehicleIcon.setVerticalTextPosition(JLabel.BOTTOM);
//           vehicleIcon.setHorizontalTextPosition(JLabel.CENTER);
//           headingPanel.add(vehicleIcon, BorderLayout.CENTER);
            
            

        }

        public void setOrientation(double heading_deg) {
            this.currentHeading_deg = heading_deg;
            setToolTipText("Heading: " + (int) heading_deg);
            repaint();
        }
    }


}