// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map;

import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author AFRL/RQQD
 */
public class AnimatedMap extends MapPanel {

    /**
     * rate that the map is refreshed in frames/sec
     */
    private double refreshRate = 15;
    Timer timer = new Timer();
    AnimateThread currentThread = null;
    boolean repaintCalled = false;
    private long refreshMillis;

    public AnimatedMap() {
        super();
        resetTimer();
        setRefreshRate(refreshRate);
    }

    public AnimatedMap(double center_lat, double center_lon, double num_deg_x, int width, int height) {
        super(center_lat, center_lon, num_deg_x, width, height);
        setRefreshRate(refreshRate);
        resetTimer();
    }

    public void resetTimer() {
        //refreshMillis = (long) (1. / refreshRate * 1000);
        if (currentThread != null) {
            currentThread.cancel();
            timer.purge();
        }
        timer.schedule(new AnimateThread(), 0, refreshMillis);
    }

    public double getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(double refreshRate) {
        this.refreshRate = refreshRate;
        refreshMillis = (long) (1. / refreshRate * 1000);
        resetTimer();
    }

    @Override
    public void requestRepaint() {
        repaintCalled = true;
    }


    class AnimateThread extends TimerTask {

        @Override
        public void run() {
            if (repaintCalled) {
                repaintCalled = false;
                AnimatedMap.this.repaint(refreshMillis);
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */