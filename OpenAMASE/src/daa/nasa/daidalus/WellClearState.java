package nasa.daidalus;

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
        assert !isConfigured;

        if (!isConfigured) {
            this.config = config;
            this.isConfigured = true;
        }
    }

    public void setBands(WellClearViolationIntervals wcv, double msgTime) {
        typeToBands.clear();
        List<BandIntervals.Band> bands;

        // TODO: store timestamp on receive
        msgArrivalTime = msgTime;

        // heading
        assert wcv.getWCVGroundHeadingIntervals().size() == wcv.getWCVGroundHeadingRegions().size();

        final Iterator<larcfm.DAIDALUS.GroundHeadingInterval> it = wcv.getWCVGroundHeadingIntervals().iterator();
        final Iterator<larcfm.DAIDALUS.BandsRegion> itRegions = wcv.getWCVGroundHeadingRegions().iterator();

        bands = new ArrayList<>();
        while (it.hasNext() && itRegions.hasNext()) {
            double[] interval = it.next().getGroundHeadings();
            bands.add(new BandIntervals.Band(interval[0], interval[1], itRegions.next()));
        }

        typeToBands.put(BandType.HEADING, new BandIntervals(wcv.getCurrentHeading(), bands));
    }
}