// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.entity.modules;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.FlightProfile;
import avtas.amase.entity.EntityModel;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JOptionPane;

/**
 * Manages a set of design operation points for an aircraft.  Off-design behavior is determined
 * by interpolating between the design points.
 * <p>
 * This class is configured through a single {@link FlightProfile}
 * message that is passed by the {@link EntityModel} at creation time. A list of {@link FlightProfile}
 * objects are stored and interpolated at run-time based on requested forward speed and climb speed.
 *
 * @author AFRL/RQQD
 */
public class ProfileManager {

    private FlightProfile minLevel = null;
    private FlightProfile maxLevel = null;
    private FlightProfile maxClimb = null;
    private FlightProfile maxDescent = null;

    TreeSet<FlightProfile> climbList = new TreeSet<FlightProfile>(new Comparator<FlightProfile>() {
        @Override
        public int compare(FlightProfile o1, FlightProfile o2) {
            return o1.getVerticalSpeed() > o2.getVerticalSpeed() ? 1 : -1;
        }

    });

    TreeSet<FlightProfile> descentList = new TreeSet<FlightProfile>(new Comparator<FlightProfile>() {
        @Override
        public int compare(FlightProfile o1, FlightProfile o2) {
            return o1.getVerticalSpeed() < o2.getVerticalSpeed() ? 1 : -1;
        }

    });

    TreeSet<FlightProfile> levelList = new TreeSet<FlightProfile>(new Comparator<FlightProfile>() {
        @Override
        public int compare(FlightProfile o1, FlightProfile o2) {
            return o1.getAirspeed() > o2.getAirspeed() ? 1 : -1;
        }

    });

    public ProfileManager() {
    }

    /** Removes the profiles from the profile lists */
    public void clearProfiles() {
        climbList.clear();
        levelList.clear();
        descentList.clear();
        minLevel = null;
        maxLevel = null;
        maxClimb = null;
        maxDescent = null;
    }

    public void setProfiles(AirVehicleConfiguration avc) {
        clearProfiles();
        if (avc.getNominalFlightProfile() == null) {
            JOptionPane.showMessageDialog(null, "Warning: Aircraft " + avc.getID() + " is missing its nominal flight profile.",
                    "Profile Manager Warning", JOptionPane.ERROR_MESSAGE);
        }
        addProfile(avc.getNominalFlightProfile());
        for (FlightProfile fp : avc.getAlternateFlightProfiles()) {
            addProfile(fp);
        }

        if (maxClimb == null) maxClimb = avc.getNominalFlightProfile();
        if (maxDescent == null) maxDescent = avc.getNominalFlightProfile();
    }

    /** Called when the profile manager is configured using an {@link FlightProfile} object.
     *
     * @param profile the flight profile to add
     */
    public void addProfile(FlightProfile profile) {

        if (Math.abs(profile.getVerticalSpeed()) < Float.MIN_VALUE) {
            levelList.add(profile);
            if (minLevel == null || profile.getAirspeed() < minLevel.getAirspeed()) {
                minLevel = profile;
            }
            if (maxLevel == null || profile.getAirspeed() > maxLevel.getAirspeed()) {
                maxLevel = profile;
            }
        } else if (profile.getVerticalSpeed() > 0) {
            climbList.add(profile);
            if (maxClimb == null || profile.getVerticalSpeed() > maxClimb.getVerticalSpeed()) {
                maxClimb = profile;
            }
        } else {
            descentList.add(profile);
            if (maxDescent == null || profile.getVerticalSpeed() < maxDescent.getVerticalSpeed()) {
                maxDescent = profile;
            }
        }

    }

    /** Interpolates a list of flight profiles and returns a new {@link FlightProfile} based on
     * the interpolation results.  This method calls {@link #interpLevel(double) }, or 
     * {@link #interpClimbDive(double, double, java.util.Set)  } based on the value of vertSpeed.
     *
     * @param speed The requested forward airspeed in meters per second
     * @param vertspeed the requested vertical speed in meters per second (positive up)
     * @return An interpolated FlightProfile based on the requested values.
     */
    public FlightProfile getProfile(double speed, double vertspeed) {

        if (Math.abs(vertspeed) < 0.01) {
            return interpLevel(speed);
        } else if (vertspeed > 0) {
            return interpClimbDive(speed, vertspeed, climbList);
        } else {
            return interpClimbDive(speed, vertspeed, descentList);
        }
    }

    /** Interpolates between profiles to return the closest to the requested climb/descent rate.
     */
    protected FlightProfile interpClimbDive(double speed, double vertspeed, Set<FlightProfile> interpList) {

        FlightProfile vertProf = null;

        // find the closest climb/descent profile for the requested vertical speed
        if (vertspeed > maxClimb.getVerticalSpeed()) {
            return maxClimb;
        }
        else if(vertspeed < maxDescent.getVerticalSpeed()) {
            return maxDescent;
        } else {
            for (FlightProfile p : interpList) {
                if ( Math.abs(p.getVerticalSpeed()) > Math.abs(vertspeed) ) {
                    vertProf = p;
                    break;
                }
            }
        }

        FlightProfile levelProf = interpLevel(speed);
        if (vertProf == null) {
            return levelProf;
        }


        // determine how much of the climb/descent profile to use and how much of the level profile to use
        double frac = vertspeed / vertProf.getVerticalSpeed();

        FlightProfile ret = new FlightProfile();

        ret.setAirspeed((float) (frac * vertProf.getAirspeed() + (1 - frac) * levelProf.getAirspeed()));
        ret.setVerticalSpeed((float) (frac * vertProf.getVerticalSpeed()));
        ret.setPitchAngle((float) (frac * vertProf.getPitchAngle() + (1 - frac) * levelProf.getPitchAngle()));
        ret.setEnergyRate((float) (frac * vertProf.getEnergyRate() + (1 - frac) * levelProf.getEnergyRate()));
        ret.setMaxBankAngle((float) (frac * vertProf.getMaxBankAngle() + (1 - frac) * levelProf.getMaxBankAngle()));
        ret.setName("Interpolated");

        return ret;

    }

    /**
     * Interpolates a level flight condition for a given speed
     * @param speed forward speed in meters per second
     * @return An interpolated flight condition
     */
    private FlightProfile interpLevel(double speed) {

        if (speed >= maxLevel.getAirspeed()) {
            return maxLevel;
        }
        if (speed <= minLevel.getAirspeed()) {
            return minLevel;
        }

        FlightProfile low = null;
        FlightProfile high = null;

        for (FlightProfile p : levelList) {
            if (p.getAirspeed() < speed) {
                low = p;
            } else if (p.getAirspeed() >= speed) {
                high = p;
                break;
            }
        }

        if (low == null) {
            return high;
        }
        if (high == null) {
            return low;
        }

        double frac = (speed - low.getAirspeed()) / (high.getAirspeed() - low.getAirspeed());

        FlightProfile ret = new FlightProfile();
        ret.setAirspeed((float) speed);
        ret.setVerticalSpeed((float) (low.getVerticalSpeed() + frac * (high.getVerticalSpeed() - low.getVerticalSpeed())));
        ret.setPitchAngle((float) (low.getPitchAngle() + frac * (high.getPitchAngle() - low.getPitchAngle())));
        ret.setEnergyRate((float) (low.getEnergyRate() + frac * (high.getEnergyRate() - low.getEnergyRate())));
        ret.setMaxBankAngle((float) (low.getMaxBankAngle() + frac * (high.getMaxBankAngle() - low.getMaxBankAngle())));

        //System.out.println("high:\n" + high + "\nlow:\n" + low + "\ninterp:\n" + ret);
        return ret;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Level profiles\n");
        for (FlightProfile p : levelList) {
            buf.append(p.toString()).append("\n");
        }
        buf.append("Climb profiles\n");
        for (FlightProfile p : climbList) {
            buf.append(p.toString()).append("\n");
        }
        buf.append("Descent profiles\n");
        for (FlightProfile p : descentList) {
            buf.append(p.toString()).append("\n");
        }
        return buf.toString();
    }

    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */