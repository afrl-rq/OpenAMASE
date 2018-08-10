package nasa.daidalus;

import avtas.amase.scenario.ScenarioState;
import larcfm.DAIDALUS.DAIDALUSConfiguration;
import larcfm.DAIDALUS.WellClearViolationIntervals;

import java.util.*;

public class WellClearState {
    private boolean isConfigured = false;
    private DAIDALUSConfiguration config;
    private Map<BandType, BandIntervals> typeToBands = new HashMap<>();
    private double msgArrivalTime;

    public boolean isConfigured() {
        return isConfigured;
    }

    public DAIDALUSConfiguration getConfig() {
        return config;
    }

    public List<BandIntervals.Band> getBands(BandType type) {
        if (typeToBands.containsKey(type)) {
            return typeToBands.get(type).getBands();
        }

        return new ArrayList<>();
    }
    
    public double getCurrent(BandType type) {
        if (typeToBands.containsKey(type)) {
            return typeToBands.get(type).getCurrent();
        }

        return 0;
    }

    public double getMsgTime() {
        return msgArrivalTime;
    }

    public void setDAIDALUSConfiguration(DAIDALUSConfiguration config) {
//        if (!isConfigured) {
            this.config = config;
            this.isConfigured = true;
//        }
    }

    public void setBands(WellClearViolationIntervals wcv, double msgTime) {
        typeToBands.clear();
        List<BandIntervals.Band> bands;

        // Store timestamp on receive
        msgArrivalTime = msgTime;

        // Heading ----------------------------------------------------------------------------------------------------
        // There should be an heading interval for every region. This is a critical failure if this doesn't hold.
        assert wcv.getWCVGroundHeadingIntervals().size() == wcv.getWCVGroundHeadingRegions().size();

        final Iterator<larcfm.DAIDALUS.GroundHeadingInterval> it = wcv.getWCVGroundHeadingIntervals().iterator();
        Iterator<larcfm.DAIDALUS.BandsRegion> itRegions = wcv.getWCVGroundHeadingRegions().iterator();

        bands = new ArrayList<>();
        while (it.hasNext() && itRegions.hasNext()) {
            double[] interval = it.next().getGroundHeadings();
            bands.add(new BandIntervals.Band(interval[0], interval[1], itRegions.next()));
        }

        typeToBands.put(BandType.HEADING, new BandIntervals(wcv.getCurrentHeading(), bands));

        // Heading Recovery Bands -------------------------------------------------------------------------------------
        final Iterator<larcfm.DAIDALUS.GroundHeadingRecoveryInterval> itGHR = wcv.getRecoveryGroundHeadingIntervals().iterator();

        bands = new ArrayList<>();
        while (itGHR.hasNext()) {
            double[] interval = itGHR.next().getRecoveryGroundHeadings();
            bands.add(new BandIntervals.Band(interval[0], interval[1]));
        }
        typeToBands.put(BandType.RECOVERY_HEADING, new BandIntervals(wcv.getCurrentHeading(), bands));


        // Altitude ---------------------------------------------------------------------------------------------------
        // There should be an altitude interval for every region. This is a critical failure if this doesn't hold.
        assert wcv.getWCVAlitudeIntervals().size() == wcv.getWCVAltitudeRegions().size();

        final Iterator<larcfm.DAIDALUS.AltitudeInterval> itAlt = wcv.getWCVAlitudeIntervals().iterator();
        itRegions = wcv.getWCVAltitudeRegions().iterator();

        // Make a new ArrayList container to populate altitude bands
        bands = new ArrayList<>();

        while (itAlt.hasNext() && itRegions.hasNext()) {
            double[] interval = itAlt.next().getAltitude();
            bands.add(new BandIntervals.Band(interval[0], interval[1], itRegions.next()));
        }

        typeToBands.put(BandType.ALTITUDE, new BandIntervals(wcv.getCurrentAltitude(), bands));

        // Altitude Recovery Bands ------------------------------------------------------------------------------------
        final Iterator<larcfm.DAIDALUS.AltitudeRecoveryInterval> itAltR = wcv.getRecoveryAltitudeIntervals().iterator();

        bands = new ArrayList<>();
        while (itAltR.hasNext()) {
            double[] interval = itAltR.next().getRecoveryAltitude();
            bands.add(new BandIntervals.Band(interval[0], interval[1]));
        }
        typeToBands.put(BandType.RECOVERY_ALTITUDE, new BandIntervals(wcv.getCurrentAltitude(), bands));


        // Ground Speed -----------------------------------------------------------------------------------------------
        // There should be an ground speed interval for every region. This is a critical failure if this doesn't hold.
        assert wcv.getWCVGroundSpeedIntervals().size() == wcv.getWCVGroundSpeedRegions().size();

        final Iterator<larcfm.DAIDALUS.GroundSpeedInterval> itGS = wcv.getWCVGroundSpeedIntervals().iterator();
        itRegions = wcv.getWCVGroundSpeedRegions().iterator();

        // Make a new ArrayList container to populate ground speed bands
        bands = new ArrayList<>();

        while (itGS.hasNext() && itRegions.hasNext()) {
            double[] interval = itGS.next().getGroundSpeeds();
            bands.add(new BandIntervals.Band(interval[0], interval[1], itRegions.next()));
        }

        typeToBands.put(BandType.GROUND_SPEED, new BandIntervals(wcv.getCurrentGoundSpeed(), bands));

        // Ground Speed Recovery Bands --------------------------------------------------------------------------------
        final Iterator<larcfm.DAIDALUS.GroundSpeedRecoveryInterval> itGSR = wcv.getRecoveryGroundSpeedIntervals().iterator();

        bands = new ArrayList<>();
        while (itGSR.hasNext()) {
            double[] interval = itGSR.next().getRecoveryGroundSpeeds();
            bands.add(new BandIntervals.Band(interval[0], interval[1]));
        }
        typeToBands.put(BandType.RECOVERY_GROUND_SPEED, new BandIntervals(wcv.getCurrentGoundSpeed(), bands));

        // Vertical Speed ---------------------------------------------------------------------------------------------
        // There should be an vertical speed interval for every region. This is a critical failure if this doesn't hold.
        assert wcv.getWCVVerticalSpeedIntervals().size() == wcv.getWCVVerticalSpeedRegions().size();

        final Iterator<larcfm.DAIDALUS.VerticalSpeedInterval> itVS = wcv.getWCVVerticalSpeedIntervals().iterator();
        itRegions = wcv.getWCVVerticalSpeedRegions().iterator();

        // Make a new ArrayList container to populate certical speed bands
        bands = new ArrayList<>();

        while (itVS.hasNext() && itRegions.hasNext()) {
            double[] interval = itVS.next().getVerticalSpeeds();
            bands.add(new BandIntervals.Band(interval[0], interval[1], itRegions.next()));
        }

        typeToBands.put(BandType.VERTICAL_SPEED, new BandIntervals(wcv.getCurrentVerticalSpeed(), bands));

        // Vertical Speed Recovery Bands ------------------------------------------------------------------------------
        final Iterator<larcfm.DAIDALUS.VerticalSpeedRecoveryInterval> itVSR = wcv.getRecoveryVerticalSpeedIntervals().iterator();

        bands = new ArrayList<>();
        while (itVSR.hasNext()) {
            double[] interval = itVSR.next().getRecoveryVerticalSpeed();
            bands.add(new BandIntervals.Band(interval[0], interval[1]));
        }
        typeToBands.put(BandType.RECOVERY_VERTICAL_SPEED, new BandIntervals(wcv.getCurrentVerticalSpeed(), bands));

    }
}