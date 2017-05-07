// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================


package avtas.amase.ui;

import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import avtas.map.GeoPointSelected;
import avtas.util.WindowUtils;
import java.awt.FlowLayout;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import org.jdesktop.swingx.JXFormattedTextField;
import org.jdesktop.swingx.JXTextField;

/**
 * Provides the user with an interface to input coordinates.  Responds
 * to {@link GeoPointSelected} events as well.
 * @author AFRL/RQQD
 */
public class LatLonPanel extends JPanel {
    
    JToggleButton mapButton;
    JXFormattedTextField latField, lonField;
    
    public LatLonPanel() {
        latField = new JXFormattedTextField("Latitude");
        lonField = new JXFormattedTextField("Longitude");
        latField.setColumns(8);
        lonField.setColumns(8);
        
        mapButton = new JToggleButton(new ImageIcon(getClass().getResource("/resources/MapMarker-50.png")));
        mapButton.setBorderPainted(false);
        mapButton.setMargin(new Insets(0, 0, 0, 0));
        mapButton.setToolTipText("Pick from map");
        
        AppEventManager.getDefaultEventManager().addListener(new AppEventListener() {
            @Override
            public void eventOccurred(Object event) {
                if (mapButton.isSelected() && event instanceof GeoPointSelected) {
                    GeoPointSelected pt = (GeoPointSelected) event;
                    latField.setText(String.valueOf(pt.getLatitude()));
                    lonField.setText(String.valueOf(pt.getLongitude()));
                }
            }
        });
        
        
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        add(latField);
        add(lonField);
        add(mapButton);
    }
    
    public Double getLatitude() {
        try {
            return Double.valueOf(latField.getText());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
     public Double getLongitude() {
        try {
            return Double.valueOf(lonField.getText());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        WindowUtils.showApplicationWindow(new LatLonPanel());
        
        for (int i=0; i<100; i++) {
            AppEventManager.getDefaultEventManager().fireEvent(new GeoPointSelected(Math.random() * 360 - 180, Math.random() * 180 - 90));
            Thread.sleep(1000);
        }
    }
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */