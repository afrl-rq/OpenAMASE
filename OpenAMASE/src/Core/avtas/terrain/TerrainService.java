// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.terrain;

import java.io.File;

/**
 * A wrapper for a single DTEDCache.  Methods accessed here pass-through to a single
 * instance of a DTEDCache.  This exposes DTEDCache services to the entire application.
 * Changes to the cache (such as adding a directory) affect all requests made to this
 * service.
 *
 * @author AFRL/RQQD
 */
public class TerrainService {

    static DTEDCache cache = new DTEDCache();

    /** Returns a reference to the underlying cache */
    public static DTEDCache getCache() {
        return cache;
    }

    /**
     * A static interface to {@link DTEDCache#getElevation(double, double) }.
     */
    public static short getElevation(double lat, double lon) {
        return cache.getElevation(lat, lon);
    }

    /**
     * A static interface to {@link DTEDCache#getElevationInterp(double, double) }.
     */
    public static double getElevationInterp(double lat, double lon) {
        return cache.getElevationInterp(lat, lon);
    }

    /**
     * A static interface to {@link DTEDCache#getElevations(double, double, double, double, double) }.
     */
    public static short[][] getElevations(double ullat, double ullon,
            double lrlat, double lrlon, double arc_step) {

        return cache.getElevations(ullat, ullon, lrlat, lrlon, arc_step);
    }

    /**
     * A static interface to {@link DTEDCache#addDirectory(java.io.File)  }.
     */
    public static void addDirectory(File dir) {
        cache.addDirectory(dir);
    }


    /**
     * A static interface to {@link DTEDCache#getLineElevs(double, double, double, double, int)  }
     */
    public static short[] getLineElevs(double lat1, double lon1,
            double lat2, double lon2, int level) {

        return getLineElevs(lat1, lon1, lat2, lon2, level);
    }

    /**
     * A static interface to {@link DTEDCache#getInterceptPoint(double, double, double, double, double, double, int)  }.
     */
    public static double[] getInterceptPoint(double startLat, double startLon, double startAlt,
            double heading_rad, double slope_rad, double maxDist, int level) {

        return cache.getInterceptPoint(startLat, startLon, startAlt, heading_rad, slope_rad, maxDist, level);
    }

    /**
     * A static interface to {@link DTEDCache#isLineOfSight(double, double, double, double, double, double, int) }.
     */
    public static boolean isLineOfSight(double lat1, double lon1, double h1,
            double lat2, double lon2, double h2, int level) {
        return cache.isLineOfSight(lat1, lon1, h1, lat2, lon2, h2, level);
    }

    /**
     * A static interface to {@link ViewShedGenerator#getLOSCoverage(avtas.terrain.DTEDCache, double, double, double, double, double, int, avtas.terrain.ViewShedObserver)  }.
     */
    public static boolean[][] getLOSCoverage(double lat, double lon, double range, double h1, double h2, int level, ViewShedObserver observer) {
        return ViewShedGenerator.getLOSCoverage(cache, lat, lon, range, h1, h2, level, observer);
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */