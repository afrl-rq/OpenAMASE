// ====================================================================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Autonomous Controls Branch
// 
// Copyright (c) 2018 Government of the United State of America, as represented by the Secretary of the Air Force.
// No copyright is claimed in the United States under Title 17, U.S. Code.  All Other Rights Reserved.
// ====================================================================================================================

package nasa.daidalus;

import afrl.cmasi.AirVehicleState;
import afrl.cmasi.Location3D;
import afrl.cmasi.SessionStatus;
import avtas.amase.scenario.ScenarioState;
import avtas.amase.util.CmasiNavUtils;
import avtas.app.AppEventListener;
import avtas.map.graphics.*;
import avtas.map.layers.GraphicsLayer;
import larcfm.DAIDALUS.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

public class WellClearLayer extends GraphicsLayer<MapGraphic> implements AppEventListener {
    private Map<Long, WellClearState> idToVehicleState = new HashMap<>();

    private static final double FIRST_BAR_OFFSET_HDG_DEG = 50;
    private static final double SECOND_BAR_OFFSET_HDG_DEG = 55;
    private static final double TICK_BAR_LENGTH_M = 100;
    private static final double BAND_WIDTH = 3; 

    // This function finds the distance from the bar anchor point to the given point.
    // -> It is used to get the Location3D point for drawing in world coordinates.
    private double findRelativeDistanceFromAnchor(final double anchorHeight_m, final double givenHeight_m, final double barLength_m) {
        return ((anchorHeight_m - givenHeight_m) / anchorHeight_m) * barLength_m;
    }

    // This function finds the total range of values that the indication bar must accurately display
    // - It is important that the max and min ranges are properly defined 
    private double findBarRange(final double maxValue, final double minVal) {
        assert maxValue >= minVal;

        return (maxValue - minVal);
    }

    // This function finds the percentage of the total bar length 
    private double findPercentInRange(final double maxValue, final double minVal, final double currentVal) {
        double barRange = findBarRange(maxValue,minVal);

        // Normalize the bar minimum value to find the percentage
        return (currentVal - minVal) / barRange;
    }

    // This creates the MapLine to place the current value tick onto the bar
    private MapLine drawBarTickinWorldCord(final Location3D btmAnchorPt, final double barWorldCordDistance, final double percentage) {

        final double distanceFromTopAnchor = barWorldCordDistance * percentage;
        final Location3D currentTickLoc = CmasiNavUtils.getPoint(btmAnchorPt,distanceFromTopAnchor,0);
        final Location3D leftCurrTickLoc = CmasiNavUtils.getPoint(currentTickLoc,TICK_BAR_LENGTH_M,270);
        final Location3D rightCurrTickLoc = CmasiNavUtils.getPoint(currentTickLoc,TICK_BAR_LENGTH_M,90);

        return new MapLine(leftCurrTickLoc.getLatitude(), leftCurrTickLoc.getLongitude(),
                         rightCurrTickLoc.getLatitude(), rightCurrTickLoc.getLongitude());
    }

    // This returns the Location3D in the world coordinate where the percentage would exist based from the bottom achor point
    private Location3D getLocInWorldCord(final Location3D btmAnchorPt, final double barWorldCordDistance, final double percentage) {

        final double distanceFromTopAnchor = barWorldCordDistance * percentage;

        return CmasiNavUtils.getPoint(btmAnchorPt,distanceFromTopAnchor,0);
    }

    // This generates a MapLine that represents the band interval on the appropriate (altitude, ground speed, vertical speed) display bar
    private MapLine drawIntervalLine(final Location3D btmAnchorPt, final double maxValue, final double minVal, final double worldDist, final BandIntervals.Band band) {
      
        // Find the percentage for the upper and lower points 
        final double upperPtBarPercent = findPercentInRange(maxValue, minVal, band.upper);
        final double lowerPtBarPercent = findPercentInRange(maxValue, minVal, band.lower);

        // Find the world cordinates of the interval endpoints
        final Location3D upperIntervalPt = getLocInWorldCord(btmAnchorPt, worldDist, upperPtBarPercent);
        final Location3D lowerIntervalPt = getLocInWorldCord(btmAnchorPt, worldDist, lowerPtBarPercent);

        return new MapLine(upperIntervalPt.getLatitude(), upperIntervalPt.getLongitude(),
                         lowerIntervalPt.getLatitude(), lowerIntervalPt.getLongitude());
    }


    @Override
    public void eventOccurred(Object event) {
        if (event instanceof DAIDALUSConfiguration) {
            DAIDALUSConfiguration config = (DAIDALUSConfiguration) event;
            Long id = new Long(config.getEntityId());

            WellClearState state;
            if (idToVehicleState.containsKey(id)) {
                state = idToVehicleState.get(id);
            } else {
                state = new WellClearState();
            }            
            
            // Update the object
            state.setDAIDALUSConfiguration(config);
            idToVehicleState.put(id, state);
            
        } else if (event instanceof WellClearViolationIntervals) {
            WellClearViolationIntervals intervals = (WellClearViolationIntervals) event;
            Long id = new Long(intervals.getEntityId());

            WellClearState state;
            if (idToVehicleState.containsKey(id)) {
                state = idToVehicleState.get(id);
            } else {
                state = new WellClearState();
            }
            
            state.setBands((WellClearViolationIntervals) event, ScenarioState.getTime());
            idToVehicleState.put(id, state);
            
        } else if (event instanceof SessionStatus) {
            clear(); // remove all prior graphic objects

            // Band display
            for (Map.Entry<Long, WellClearState> entry : idToVehicleState.entrySet()) {
                WellClearState wellClearState = entry.getValue();
                if (wellClearState.isConfigured()) {
                    AirVehicleState airVehicleState = ScenarioState.getAirVehicleState(entry.getKey());

                    if (airVehicleState != null) {
                        Location3D location = airVehicleState.getLocation();                 

                        // show circle at DTRH
                        final double radius_m = wellClearState.getConfig().getMinHorizontalRecovery(); // DTHR m
                        MapCircle circle = new MapCircle(location.getLatitude(), location.getLongitude(), radius_m);
                        circle.setPainter(Color.WHITE, 1);
                        getList().add(circle);

                        // show current heading
                        Location3D endPoint = CmasiNavUtils.getPoint(location, radius_m, wellClearState.getCurrent(BandType.HEADING));
                        MapLine headingLine = new MapLine(location.getLatitude(), location.getLongitude(),
                                                          endPoint.getLatitude(), endPoint.getLongitude());
                        headingLine.setPainter(Color.WHITE, 1);
                        getList().add(headingLine);

                        // Build the Altitude bar -------------------------------------------------------------------------------------
                        // > Draw it to the "left" of the heading band circle
                        // Find the distance to the bar points from the location
                        final double closeBarVertexDist = radius_m / Math.cos(Math.toRadians(FIRST_BAR_OFFSET_HDG_DEG));
                        Location3D altTopPt = CmasiNavUtils.getPoint(location, closeBarVertexDist, (360 - FIRST_BAR_OFFSET_HDG_DEG));
                        Location3D altBtmPt = CmasiNavUtils.getPoint(location, closeBarVertexDist, (180 + FIRST_BAR_OFFSET_HDG_DEG));
                        MapLine altLine = new MapLine(altTopPt.getLatitude(), altTopPt.getLongitude(),
                                                      altBtmPt.getLatitude(), altBtmPt.getLongitude());
                        altLine.setPainter(Color.WHITE, 1);
                        getList().add(altLine);

                        // Find the distance between the altitude line points and associate with the interval range to normalize
                        final double altLineDist_m = CmasiNavUtils.distance(altTopPt, altBtmPt);

                        // To reduce the number of function calls              
                        final double tmpMaxAltitude_m = wellClearState.getConfig().getMaxAltitude();
                        final double tmpMinAltitude_m = wellClearState.getConfig().getMinAltitude();

                        final double currAltBarPercent = findPercentInRange(tmpMaxAltitude_m, tmpMinAltitude_m, location.getAltitude());
                        MapLine currAltLine = drawBarTickinWorldCord(altBtmPt, altLineDist_m, currAltBarPercent);
                        currAltLine.setPainter(Color.CYAN, 1);
                        getList().add(currAltLine);

                        // Build the Ground Speed bar -------------------------------------------------------------------------------
                        // > Uses the same distance on the other side of the circle
                        Location3D grdSpdTopPt = CmasiNavUtils.getPoint(location, closeBarVertexDist, FIRST_BAR_OFFSET_HDG_DEG);
                        Location3D grdSpdBtmPt = CmasiNavUtils.getPoint(location, closeBarVertexDist, (180 - FIRST_BAR_OFFSET_HDG_DEG));
                        MapLine grdSpdLine = new MapLine(grdSpdTopPt.getLatitude(), grdSpdTopPt.getLongitude(),
                                                         grdSpdBtmPt.getLatitude(), grdSpdBtmPt.getLongitude());
                        grdSpdLine.setPainter(Color.WHITE, 1);
                        getList().add(grdSpdLine);

                        // Find the distance between the ground speed line points and associate with the interval range to normalize
                        final double grdSpdLineDist_m = CmasiNavUtils.distance(grdSpdTopPt, grdSpdBtmPt);

                        // To reduce the number of function calls                                        
                        final double tmpMaxGrdSpd = wellClearState.getConfig().getMaxGroundSpeed();  
                        final double tmpMinGrdSpd = wellClearState.getConfig().getMinGroundSpeed();

                        final double currGrdSpdBarPercent = findPercentInRange(tmpMaxGrdSpd, tmpMinGrdSpd, airVehicleState.getGroundspeed());
                        MapLine currGrdSpdLine = drawBarTickinWorldCord(grdSpdBtmPt, grdSpdLineDist_m, currGrdSpdBarPercent);
                        currGrdSpdLine.setPainter(Color.CYAN, 1);
                        getList().add(currGrdSpdLine);

                        // Build the Vertical Speed bar -----------------------------------------------------------------------------
                        // > find the farther hypotenuse distance
                        final double farBarVertexDist = radius_m / Math.cos(Math.toRadians(SECOND_BAR_OFFSET_HDG_DEG));
                        Location3D vertSpdTopPt = CmasiNavUtils.getPoint(location, farBarVertexDist, SECOND_BAR_OFFSET_HDG_DEG);
                        Location3D vertSpdBtmPt = CmasiNavUtils.getPoint(location, farBarVertexDist, (180 - SECOND_BAR_OFFSET_HDG_DEG));
                        MapLine vertSpdLine = new MapLine(vertSpdTopPt.getLatitude(), vertSpdTopPt.getLongitude(),
                                                          vertSpdBtmPt.getLatitude(), vertSpdBtmPt.getLongitude());
                        vertSpdLine.setPainter(Color.WHITE, 1);
                        getList().add(vertSpdLine);

                        // Find the distance between the ground speed line points and associate with the interval range to normalize
                        final double vertSpdLineDist_m = CmasiNavUtils.distance(vertSpdTopPt, vertSpdBtmPt);

                        // To reduce the number of function calls                                        
                        final double tmpMaxVertSpd = wellClearState.getConfig().getMaxVerticalSpeed();  
                        final double tmpMinVertSpd = wellClearState.getConfig().getMinVerticalSpeed();

                        final double currVertSpdBarPercent = findPercentInRange(tmpMaxVertSpd, tmpMinVertSpd, airVehicleState.getVerticalSpeed());
                        MapLine currVertSpdLine = drawBarTickinWorldCord(vertSpdBtmPt, vertSpdLineDist_m, currVertSpdBarPercent);
                        currVertSpdLine.setPainter(Color.CYAN, 1);
                        getList().add(currVertSpdLine);


                        // This ensures that "stale bands" are not drawn. 
                        // When no messages received within a second of the last the band is considered stale. 
                        // - The default draw color in maintained, stale bands are not drawn.
                        if ((ScenarioState.getTime() - wellClearState.getMsgTime()) < 1) {

                            for (BandIntervals.Band band : wellClearState.getBands(BandType.HEADING)) {
                                
                                // TODO: check whether band enclosing true north is possible, and if handle correctly
                                MapArc arc = new MapArc(location.getLatitude(), location.getLongitude(), band.lower,
                                        band.upper - band.lower, radius_m);
                                arc.setPainter(band.getColor(), BAND_WIDTH);
                                getList().add(arc);
                            }

                            for (BandIntervals.Band band : wellClearState.getBands(BandType.ALTITUDE)) {
                                // System.out.println(ScenarioState.getTime() + ": Entered into BandType.ALTITUDE");

                                MapLine altIntLine = drawIntervalLine(altBtmPt, tmpMaxAltitude_m, tmpMinAltitude_m, altLineDist_m, band);
                                altIntLine.setPainter(band.getColor(), BAND_WIDTH);
                                getList().add(altIntLine);

                            }

                            for (BandIntervals.Band band : wellClearState.getBands(BandType.GROUND_SPEED)) {
                                // System.out.println(ScenarioState.getTime() + ": Entered into BandType.GROUND_SPEED");

                                MapLine grdSpdIntLine = drawIntervalLine(grdSpdBtmPt, tmpMaxGrdSpd, tmpMinGrdSpd, grdSpdLineDist_m, band);
                                grdSpdIntLine.setPainter(band.getColor(), BAND_WIDTH);
                                getList().add(grdSpdIntLine);
                            }

                            for (BandIntervals.Band band : wellClearState.getBands(BandType.VERTICAL_SPEED)) {
                                // System.out.println(ScenarioState.getTime() + ": Entered into BandType.VERTICAL_SPEED");

                                MapLine vertSpdIntLine = drawIntervalLine(vertSpdBtmPt, tmpMaxVertSpd, tmpMinVertSpd, vertSpdLineDist_m, band);
                                vertSpdIntLine.setPainter(band.getColor(), BAND_WIDTH);
                                getList().add(vertSpdIntLine);
                            }

                            // Draw the Recovery Bands ----------------------------------------------------------------
                            // Recovery Heading
                            for (BandIntervals.Band band : wellClearState.getBands(BandType.RECOVERY_HEADING)) {
                                MapArc arc = new MapArc(location.getLatitude(), location.getLongitude(), band.lower,
                                                        band.upper - band.lower, (radius_m*0.95));
                                arc.setPainter(band.getColor(), BAND_WIDTH);
                                getList().add(arc);
                            }

                            // Recovery Altitude
                            for (BandIntervals.Band band : wellClearState.getBands(BandType.RECOVERY_ALTITUDE)) {
                                MapLine altIntLine = drawIntervalLine(altBtmPt, tmpMaxAltitude_m, tmpMinAltitude_m, altLineDist_m, band);
                                altIntLine.setPainter(band.getColor(), BAND_WIDTH);
                                getList().add(altIntLine);
                            }

                            //Recovery Ground Speed
                            for (BandIntervals.Band band : wellClearState.getBands(BandType.RECOVERY_GROUND_SPEED)) {
                                MapLine grdSpdIntLine = drawIntervalLine(grdSpdBtmPt, tmpMaxGrdSpd, tmpMinGrdSpd, grdSpdLineDist_m, band);
                                grdSpdIntLine.setPainter(band.getColor(), BAND_WIDTH);
                                getList().add(grdSpdIntLine);
                            }

                            //Recovery Vertical Speed
                            for (BandIntervals.Band band : wellClearState.getBands(BandType.RECOVERY_VERTICAL_SPEED)) {
                                MapLine vertSpdIntLine = drawIntervalLine(vertSpdBtmPt, tmpMaxVertSpd, tmpMinVertSpd, vertSpdLineDist_m, band);
                                vertSpdIntLine.setPainter(band.getColor(), BAND_WIDTH);
                                getList().add(vertSpdIntLine);
                            }

                        }
                    }   
                }
            }

            // transform all graphics from world coordinates to screen coordinates
            project(getProjection());
        }
    }
}

/* Distribution A. Approved for public release.
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */
