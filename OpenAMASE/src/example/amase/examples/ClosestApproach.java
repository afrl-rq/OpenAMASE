// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package amase.examples;

import afrl.cmasi.AirVehicleState;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import avtas.app.UserExceptions;
import avtas.amase.AmasePlugin;
import avtas.amase.scenario.ScenarioState;
import avtas.amase.util.CmasiNavUtils;
import avtas.plots.PlotUtils;
import avtas.util.WindowUtils;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.ui.InteractivePanel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;


/**
 * This plugin shows a more practical use of an AMASE plugin to perform an
 * analysis function. Here, we log the distance between two aircraft. When the
 * user requests a plot of the distance, we show a curve of the distance between
 * the two aircraft over time.
 * 
 * For this plugin to work, we require a scenario with at least 2 aircraft with 
 * IDs of (1) and (2).
 *
 * @author AFRL/RQQD
 */
public class ClosestApproach extends AmasePlugin {


    PlotUtils.PlotSeries2D data = new PlotUtils.PlotSeries2D("Relative Distance");
    

    public ClosestApproach() {
        setPluginName("Closest Approach Tool");
    }

    @Override
    public void eventOccurred(Object event) {
        
        // every time we get an update from the publisher, check for a 
        // running state or a reset state.  If reseting, then clear the
        // list.  Otherwise, update the distances between aircraft.
        if (event instanceof SessionStatus) {
            SessionStatus status = (SessionStatus) event;
            if (status.getState() == SimulationStatusType.Reset) {
                data.clear();
            } else {
                calculateDistance(status.getScenarioTime() / 1000d);
            }
        }
    }

    /**
     * This is where the logic is performed.  The lateral distance between 
     * aircraft is computed and stored.  
     * @param time 
     */
    protected void calculateDistance(double time) {
        AirVehicleState state1 = ScenarioState.getAirVehicleState(1L);
        AirVehicleState state2 = ScenarioState.getAirVehicleState(2L);

        if (state1 != null && state2 != null) {
            // Note the convenience method here from CmasiNavUtils.
            double distance = CmasiNavUtils.distance(state1.getLocation(), state2.getLocation());
            
            // add the time and the distance between the aircraft
            data.add(time, distance);
        }
    }

    /**
     * This provides a menu for the user to select the plotting routine
     * @param menubar 
     */
    @Override
    public void getMenus(JMenuBar menubar) {
        JMenu menu = WindowUtils.getMenu(menubar, "Closest Approach");
        menu.add(new AbstractAction("Show Plot") {

            @Override
            public void actionPerformed(ActionEvent e) {
                showPlot();
            }
        });
    }
    
    
    
    protected void showPlot() {
        
        if (data.size() == 0) {
            UserExceptions.showWarning("Cannot plot results.  No data collected.");
            return;
        }

        // create an X-Y line plot with the data
        InteractivePanel panel = PlotUtils.createLinePlot(data, "Relative Distance", "Time (sec)", "Distance (m)");
        
        // now show the plot in a separate window
        JFrame frame = new JFrame("Relative Distance");
        frame.add(panel);
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */