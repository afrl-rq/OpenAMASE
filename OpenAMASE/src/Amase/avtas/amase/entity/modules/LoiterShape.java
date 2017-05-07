// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.entity.modules;

import afrl.cmasi.LoiterAction;
import afrl.cmasi.LoiterDirection;
import afrl.cmasi.LoiterType;
import avtas.util.NavUtils;

import static java.lang.Math.*;

/**
 * Implements a loiter pattern based on the type of loiter action
 *
 * Circular:
 * A circular loiter is defined using a center point and a radius. the steering algorithm
 * commands a heading to maintain the radius distance away from the center point.
 *
 * Race track:
 *
 * The loiter pattern is defined as:
 * clockwise: [turn1, point2, turn2, point4]
 * counter-clockwise: [turn1, point3, turn2, point1]
 * 
 * The loiter is defined as a race track, with two straight-legs and two 180 degree turns.
 * The track is configured so that the axis is drawn from the center of turn2 to the 
 * center of turn1, with the centerpoint of that line defining the midpoint in the 
 * straight-leg.  The straight legs are straight lines that are drawn between points. 
 * leg1 = [1,2], or leg2 = [3,4].
 * 
 * Point1 is defined as the point that is 90 degrees from the center of turn1.  Each subsequent
 * point is the one found by moving clockwise on the racetrack from the previous point.
 *
 * Figure-Eight:
 *
 * The loiter pattern is defined as:
 * clockwise: [turn1, point3, turn2, point4]
 * counter-clockwise: [turn1, point2, turn2, point1]
 *
 * The figure-eight is implemented as a racetrack with crossed straight-aways. the total length
 * of a figure-eight is computed as 4*R + L, where R is radius and L is length.  If an L of zero
 * is specified, this approximates two circular loiters that are tangent at a point on the axis
 * of the loiter.
 * 
 * @author AFRL/RQQD
 */
public class LoiterShape {

    private double[] pt1, pt2, pt3, pt4, turn1, turn2;
    private int currentPt;
    private double[][] points = new double[4][2];
    private double meterLegLen2;
    private double meterRadius;
    private boolean firstTime = true;
    private static final int TURN1 = 0;
    private static final int TURN2 = 2;
    private static final int LEG1 = 1;
    private static final int LEG2 = 3;
    private final LoiterAction action;
    private LoiterDirection direction;
    private double radAxis;

    public LoiterShape(LoiterAction action) {
        this.action = action;
        buildLoiterShape();
    }

    /** sets up the loiter shape, and sets the first point of entry based on vehicle position. */
    public void buildLoiterShape() {

        this.meterRadius = action.getRadius();
        double radCenterLat = Math.toRadians(action.getLocation().getLatitude());
        double radCenterLon = Math.toRadians(action.getLocation().getLongitude());
        this.direction = action.getDirection() == LoiterDirection.VehicleDefault ? LoiterDirection.Clockwise :
            action.getDirection();

        if (action.getLoiterType() == LoiterType.Circular) {
            points[0] = new double[]{ radCenterLat,radCenterLon };
            return;
        }

        this.meterLegLen2 = action.getLength() / 2f;
        if (action.getLoiterType() == LoiterType.FigureEight) {
            meterLegLen2 += meterRadius;
        }

        this.radAxis = Math.toRadians(action.getAxis());
        

        //center of upper turn circle
        turn1 = NavUtils.getLatLon(radCenterLat, radCenterLon, meterLegLen2, radAxis);
        //center of lower turn circle
        turn2 = NavUtils.getLatLon(radCenterLat, radCenterLon, meterLegLen2, radAxis + PI);

        points[TURN1] = turn1;
        points[TURN2] = turn2;

        if (action.getLoiterType() == LoiterType.Racetrack) {
            if (direction == LoiterDirection.Clockwise) {
                points[LEG1] = NavUtils.getLatLon(turn2[0], turn2[1], meterRadius, radAxis + PI / 2f);
                points[LEG2] = NavUtils.getLatLon(turn1[0], turn1[1], meterRadius, radAxis - PI / 2f);
            } else {
                points[LEG1] = NavUtils.getLatLon(turn2[0], turn2[1], meterRadius, radAxis - PI / 2f);
                points[LEG2] = NavUtils.getLatLon(turn1[0], turn1[1], meterRadius, radAxis + PI / 2f);
            }
        } else if (action.getLoiterType() == LoiterType.FigureEight) {
            if (direction == LoiterDirection.Clockwise) {
                points[LEG1] = NavUtils.getLatLon(turn2[0], turn2[1], meterRadius, radAxis - PI / 2f);
                points[LEG2] = NavUtils.getLatLon(turn1[0], turn1[1], meterRadius, radAxis - PI / 2f);
            } else {
                points[LEG1] = NavUtils.getLatLon(turn2[0], turn2[1], meterRadius, radAxis + PI / 2f);
                points[LEG2] = NavUtils.getLatLon(turn1[0], turn1[1], meterRadius, radAxis + PI / 2f);
            }
        }

    }

    /** main update loop. This sets the autopilot commands */
    public double compute(double radLat, double radLon) {

        // if this is a basic circular loiter, compute the turn and return
        if (action.getLoiterType() == LoiterType.Circular || action.getLoiterType() == LoiterType.VehicleDefault) {
            return loiterCircle(points[0][0], points[0][1], radLat, radLon, meterRadius, direction);
        }

        // race track and figure-eights below

        if (firstTime) {
            currentPt = indexOfClosestPoint(points, radLat, radLon);
            firstTime = false;
        }

        //System.out.println("current point: " + currentPt);

        double az = 0;
        double[] pt = points[currentPt];

        // if the current point is turn1, then move to the next point if the azimuth
        // between the vehicle and the turn center is < +/- 90 degrees
        if (currentPt == TURN1) {
            az = loiterCircle(pt[0], pt[1], radLat, radLon, meterRadius, direction);
            double azCenter = NavUtils.headingBetween(radLat, radLon, pt[0], pt[1]);
            if (cos(azCenter - radAxis) > 0) {
                currentPt = getNextPoint(currentPt);
            }
        }
        // if the current point is turn2, then move to the next point if the azimuth
        // between the vehicle and the turn center is > +/- 90 degrees
        else if (currentPt == TURN2) {
            LoiterDirection dir = direction;
            if (action.getLoiterType() == LoiterType.FigureEight) {
                dir = direction == LoiterDirection.CounterClockwise ? LoiterDirection.Clockwise : LoiterDirection.CounterClockwise;
            }
            az = loiterCircle(pt[0], pt[1], radLat, radLon, meterRadius, dir);
            double azCenter = NavUtils.headingBetween(radLat, radLon, pt[0], pt[1]);
            if (cos(azCenter - radAxis) < 0) {
                currentPt = getNextPoint(currentPt);
            }
        } 
        // this is the point is behind the vehicle, based on cosines between the
        // azimuth to the point and the axis of the loiter.
        // if the point is is point2 or point3 (index of 1 in the orderOfPoints)
        else if (currentPt == LEG1) {
            az = NavUtils.headingBetween(radLat, radLon, pt[0], pt[1]);
            if (cos(az - radAxis) > 0) {
                currentPt = getNextPoint(currentPt);
            }
        } // if the point is point1 or point4
        else if (currentPt == LEG2) {
            az = NavUtils.headingBetween(radLat, radLon, pt[0], pt[1]);
            if (cos(az - radAxis) < 0) {
                currentPt = getNextPoint(currentPt);
            }
        }


        return az;
    }

    /**
     * Computes the commanded heading angle given the radius of the loiter circle,
     * the center of the commanded loiter, and the current location of the aircraft
     * 
     * @param radCircleLat center latitude of the loiter (radians)
     * @param radCircleLon center longitude of the loiter (radians)
     * @param radLat  current latitude of the aircraft (radians)
     * @param radLon  current longitude of the aircraft (radians)
     * @param meterRadius radius of the loiter
     * @param direction turning direction of the loiter
     * @return computed aircraft heading in radians
     */
    public static double loiterCircle(double radCircleLat, double radCircleLon,
            double radLat, double radLon, double meterRadius, LoiterDirection direction) {
        // if clockwise loiter, ideal aspect angle = 90 deg, counter-clockwise = -90
        double aa_ideal = (direction == LoiterDirection.Clockwise) ? -Math.PI / 2f : Math.PI / 2f;

        double psi_p = NavUtils.headingBetween(radLat, radLon, radCircleLat, radCircleLon);
        double dist = NavUtils.distance(radLat, radLon, radCircleLat, radCircleLon);

        double r = meterRadius;

        // add a little turn bias to make up for the diminishing return in commanded heading as the
        // vehicle approaches the turn circle
        //aa_ideal -= 0.2 * Math.signum(aa_ideal);

        double psi_cmd;
        if (dist > 2 * r) {
            psi_cmd = psi_p;
        } else {
            double dist_norm = (2 * r - dist) / r;
            //double dist_norm = (2 * r - dist)/ r * 0.95;// * Math.PI/2f;
            psi_cmd = psi_p + aa_ideal * dist_norm;
        }

        return psi_cmd;
    }

    protected int indexOfClosestPoint(double[][] points, double radLat, double radLon) {
        int shortest = 0;
        double last = NavUtils.distance(points[0][0], points[0][1], radLat, radLon);
        for (int i = 1; i < points.length; i++) {
            if (NavUtils.distance(points[i][0], points[i][1], radLat, radLon) < last) {
                shortest = i;
            }
        }
        return shortest;
    }

    protected int getNextPoint(int currentPoint) {

        if (currentPoint == 3) {
            return 0;
        } else {
            return currentPoint + 1;
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */