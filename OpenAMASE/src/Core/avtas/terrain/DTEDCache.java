// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.terrain;

import avtas.util.NavUtils;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Provides a single access point to DTED data.  This cache can handle levels
 * 0, 1, and 2 DTED. For simplicity and speed, only files that conform to MIL-PRF-
 * 89020-B are handled.  Directories added to the cache must conform to the
 * standard of longitude subdirectories and latitude files (see the standard for
 * more information).
 *
 * When requests are made, it is assumed that the user requests the highest resolution
 * available for the requested area. The cache is searched from highest level to
 * lowest.
 *
 * @author AFRL/RQQD
 */
public class DTEDCache {

    static final String LON_PATTERN = "[eEwW]\\d+";
    static final String LAT_PATTERN = "[nNsS]\\d+[.][dD][tT]\\d";
    HashMap<Integer, HashMap<Integer, HashMap<Integer, DTEDTile>>> tileMap =
            new HashMap<Integer, HashMap<Integer, HashMap<Integer, DTEDTile>>>();
    private int maxCacheSize = 8;
    DTEDTile current_tile = null;
    ArrayDeque<DTEDTile> cachedTiles = new ArrayDeque<DTEDTile>();

    /**
     * Returns a tile containing the requested [lat, lon] or null if no tile
     * exists that encapsulates the coordinates.  This method searches from
     * highest level (2) to lowest (0).
     * @param lat requested latitude (degrees)
     * @param lon requested longitude (degrees)
     * @return tile that encloses the [lat, lon] or null if none exists.
     */
    public DTEDTile getTile(double lat, double lon) {

        int intLat = (int) Math.floor(lat);
        int intLon = (int) Math.floor(lon);

        if (current_tile != null && current_tile.lat == intLat && current_tile.lon == intLon) {
            return current_tile;
        }

        HashMap<Integer, HashMap<Integer, DTEDTile>> levelMap = null;
        HashMap<Integer, DTEDTile> lonMap = null;

        for (int i = 2; i >= 0; i--) {
            levelMap = tileMap.get(i);
            if (levelMap != null) {
                lonMap = levelMap.get(intLon);
                if (lonMap != null) {
                    DTEDTile t = lonMap.get(intLat);
                    if (t != null) {
                        addToCache(t);
                        current_tile = t;
                        return t;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns the elevation for the requested point.  This method searches
     * from highest to lowest resolution.
     *
     * @param lat latitude in degrees
     * @param lon longitude in degrees
     * @return height in meters
     */
    public short getElevation(double lat, double lon) {
        DTEDTile tile = getTile(lat, lon);
        if (tile != null) {
            return tile.getElevation(lat, lon);
        }
        return 0;
    }

    /**
     * Returns an array of elevations that occupy a rectangle specified
     * by (ullon,ullat) to (lrlon,lrlat).  An arc angle step size is required
     * since there may be multiple levels of DTED present, or there are files
     * that cross DTED step-size zones within a level.  The array is zero-filled
     * for points that have no enclosing file in the cache.
     *
     * WARNING:  This currently does not handle crossing the dateline
     *
     * @param ullat upper-left latitude (degrees)
     * @param ullon upper-left longitude (degrees)
     * @param lrlat lower-right latitude (degrees)
     * @param lrlon lower-right longitude (degrees)
     * @param arc_step arc-angle step-size (degrees)
     * @return An array of [lon][lat] points containing heights.  Array starts from SW corner and 
     * moves to the East, with columns of latitudes going from South to North.
     */
    public short[][] getElevations(double ullat, double ullon,
            double lrlat, double lrlon, double arc_step) {

        if (ullat < lrlat) {
            double tmp = lrlat;
            lrlat = ullat;
            ullat = tmp;
        }
        if (lrlon < ullon) {
            double tmp = ullon;
            ullon = lrlon;
            lrlon = tmp;
        }

        int numlats = (int) ((ullat - lrlat) / arc_step);
        numlats = numlats == 0 ? 1 : numlats;
        int numlons = (int) ((lrlon - ullon) / arc_step);
        numlons = numlons == 0 ? 1 : numlons;

        int pts_per_deg = (int) (1. / arc_step);
        double lat, lon;
        short[][] posts = new short[numlons][numlats];
        int xs = 0, ys = 0;
        int xe = numlons, ye = numlats;
        DTEDTile tile;

        while (xs < numlons) {
            lon = ullon + xs * arc_step;
            xe = xs + (int) ( (Math.ceil(lon) - lon) * pts_per_deg );
            xe = xe > numlons ? numlons : xe;
            while (ys < numlats) {
                lat = lrlat + ys * arc_step;
                ye = ys + (int) ( (Math.ceil(lat) - lat) * pts_per_deg );
                ye = ye > numlats ? numlats : ye;
                tile = getTile(lat, lon);
                for (int i = xs; i < xe; i++) {
                    if (tile == null) {
                        Arrays.fill(posts[i], ys, ye, (short) 0);
                        continue;
                    }
                    lon = ullon + i * arc_step;
                    for (int j = ys; j < ye; j++) {
                        lat = lrlat + j * arc_step;
                        posts[i][j] = tile.getElevation(lat, lon);
                    }
                }
                ys = ye + 1;
            }
            ys = 0;
            xs = xe + 1;
        }

        return posts;
    }

    /**
     * Computes an interpolated elevation based on the proportional location of the
     * lat, lon point to its nearest neighbors on four sides. 
     * @param lat latitude in degrees
     * @param lon longitude in degrees
     * @return elevation in meters
     */
    public double getElevationInterp(double lat, double lon) {
        DTEDTile tile = getTile(lat, lon);
        if (tile != null) {
            return tile.getElevationInterp(lat, lon);
        }
        return 0;
    }

    /**
     * Adds a DTED directory to the cache.  A DTED directory should be organized
     * using longitude folders and latitude files.  Files are expected to be named
     * according to DTED naming conventions.  Levels 0, 1, and 2 are supported
     * automatically.
     * @param dir directory to add.
     */
    public void addDirectory(File dir) {
        if (!dir.isDirectory()) {
            return;
        }
        File[] lonList = dir.listFiles();
        for (File lonDir : lonList) {
            if (!lonDir.isDirectory()) {
                continue;
            }
            if (!lonDir.getName().matches(LON_PATTERN)) {
                continue;
            }
            File[] latList = lonDir.listFiles();
            for (File latFile : latList) {
                if (!latFile.getName().matches(LAT_PATTERN)) {
                    continue;
                }

                DTEDTile tile = new DTEDTile(latFile);

                // get the level map from the tile map
                HashMap<Integer, HashMap<Integer, DTEDTile>> levelMap = tileMap.get(tile.level);
                if (levelMap == null) {
                    levelMap = new HashMap<Integer, HashMap<Integer, DTEDTile>>();
                    tileMap.put(tile.level, levelMap);
                }

                // now find the longitude map
                HashMap<Integer, DTEDTile> lonMap = levelMap.get(tile.lon);
                if (lonMap == null) {
                    lonMap = new HashMap<Integer, DTEDTile>();
                    levelMap.put(tile.lon, lonMap);
                }

                // put the tile into the longitude map
                lonMap.put(tile.lat, tile);
            }
        }
        current_tile = null;
    }

    /**
     * Returns an array of heights from the start point to the end point specified.
     * Spacing of posts is based on the requested DTED level.
     * @param lat1 start latitude (degrees)
     * @param lon1 start longitude (degrees)
     * @param lat2 end latitude (degrees)
     * @param lon2 end longitude (degrees)
     * @param level DTED level for step spacing (0, 1, or 2)
     * @return a list of elevations from start point to end point.
     */
    public short[] getLineElevs(double lat1, double lon1,
            double lat2, double lon2, int level) {

        // set the arc_step based on the level
        double arc_step = DTEDTile.getPostSpacing(level);

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;
        double total_dist = Math.hypot(dlon, dlat);

        int num_steps = (int) (total_dist / arc_step);
        num_steps = num_steps == 0 ? 1 : num_steps;

        double lat_step = dlat / num_steps;
        double lon_step = dlon / num_steps;

        short[] elevs = new short[num_steps + 1];
        double lat = 0, lon = 0;

        for (int i = 0; i <= num_steps; i++) {
            lat = lat1 + i * lat_step;
            lon = lon1 + i * lon_step;
            elevs[i] = getElevation(lat, lon);
        }

        return elevs;
    }


    /**
     * Computes the intercept point of a ray onto the terrain.  This method uses
     * a ray-tracing process starting at the origin and moving in steps according
     * to the specified level of DTED for analysis.  The computation continues until
     * the ray intercepts the terrain, or a maximum distance is reached. For each
     * step, nearest-neighbor interpolation is used.  A ground-plane intercept
     * interpolation takes place at the end, to improve the accuracy of the final
     * intercept point.
     *
     * @param startLat start latitude of the ray (degrees)
     * @param startLon start longitude of the ray (degrees)
     * @param startAlt start altitude of the ray (meters above ellipsoid)
     * @param heading_rad heading of the ray (radians, east of north)
     * @param slope_rad slope of the ray (radians, positive above horizon)
     * @param maxDist max distance to compute. If there is no terrain intercept
     *        If zero, the approximate distance to horizon is used.
     * @param level the dted level to use for post spacing (0, 1, 2)
     * @return array of [lat, lon, height] of intercept point.
     */
    public double[] getInterceptPoint(double startLat, double startLon, double startAlt,
            double heading_rad, double slope_rad, double maxDist, int level) {

        double tanSlope = Math.tan(slope_rad);

        // don't go beyond the max distance of the horizon.  Note that this means distant mountains may not
        // be considered in the calculation
        if (slope_rad >= 0 || maxDist <= 0) {
            maxDist = NavUtils.distanceToHorizon(startAlt);
        }

        // clip max distance to the intercept with the ground plane
        maxDist = Math.min(maxDist, Math.abs(startAlt / tanSlope));
        // max sure that max distance is at least 1 step (1000 meters)
        //maxDist = Math.max(maxDist, 1000); 

        // once max distance is sorted out, compute the height at max distance
        //double endAlt = startAlt + maxDist * tanSlope;

        double[] ll = NavUtils.getLatLon(Math.toRadians(startLat), Math.toRadians(startLon),
                maxDist, heading_rad);
        double lat2 = Math.toDegrees(ll[0]);
        double lon2 = Math.toDegrees(ll[1]);

        int numsteps = (int) (Math.hypot(lat2-startLat, lon2-startLon) / DTEDTile.getPostSpacing(level));
        
        if (numsteps == 0) {
            return new double[] { startLat, startLon, getElevation(startLat, startLon)};
        }
        
        double latstep = (lat2 - startLat) / numsteps;
        double lonstep = (lon2 - startLon) / numsteps;
        
        

        double hstep = -startAlt/numsteps;

        double h2 = 0; // current height
        double h1 = startAlt; // last height
        double hterr1 = 0; // last terrain height
        double hterr2 = 0; //terrain height
        double lat = 0,lon = 0;
        
        for (int i=0; i<numsteps; i++) {
            lat = startLat + latstep * i;
            lon = startLon + lonstep * i;
            hterr2 = getElevation(lat, lon);
            h2 = startAlt + hstep * i;
            if (h2 <= hterr2) {
                // the actual step is estimated based on the height of the intercepted terrain
                double step_ratio = (hterr1 - h1) / (h2 - h1 - hterr2 + hterr1);
                lat = startLat + latstep * (i - 1 + step_ratio);
                lon = startLon + lonstep * (i - 1 + step_ratio);
                double hterr = hterr1 + (hterr2 - hterr1) * step_ratio;
                return new double[] { lat, lon, hterr};
            }
            h1 = h2; // set last height to current height
            hterr1 = hterr2;
        }
        return new double[] { lat2, lon2, getElevation(lat2, lon2)};
    }
    

    /**
     * Performs a line-of-sight test between two points.  This method traces a Rhumb
     * line from start to end point and tests for interception with the terrain.  Start
     * and end heights can be specified (e.g. antenna-to-antenna LOS) and must be
     * given as height above ground-level.
     *
     * @param lat1 start latitude (degrees)
     * @param lon1 start longitude (degrees)
     * @param h1 height of start point above ground (meters)
     * @param lat2 end latitude (degrees)
     * @param lon2 end longitude (degrees)
     * @param h2 height of end point above ground (meters)
     * @param level DTED level to use for interpolation (0, 1, or 2)
     * @return true if the end point is in the line-of-sight of the start point.
     */
    public boolean isLineOfSight(double lat1, double lon1, double h1,
            double lat2, double lon2, double h2, int level) {

        int numsteps = (int) (Math.hypot(lat2-lat1, lon2-lon1) / DTEDTile.getPostSpacing(level));
        double latstep = (lat2 - lat1) / numsteps;
        double lonstep = (lon2 - lon1) / numsteps;

        double hstart = getElevation(lat1, lon1) + h1;
        double hend = getElevation(lat2, lon2) + h2;
        double hstep = (hend - hstart)/numsteps;

        for (int i=0; i<numsteps; i++) {
            double lat = lat1 + latstep * i;
            double lon = lon1 + lonstep * i;
            double hterr = getElevation(lat, lon);
            double hray = hstart + hstep * i;
            if (hray < hterr) {
                return false;
            }
        }

        return true;
    }

    

    /**
     * Adds a tile to the list of "current" tiles.  This also checks the cache
     * and removes old tiles if over cache limit.
     * @param t  Tile to add
     */
    private void addToCache(DTEDTile t) {
        cachedTiles.remove(t);
        cachedTiles.addFirst(t);

        if (cachedTiles.size() > maxCacheSize && !cachedTiles.isEmpty()) {
            t = cachedTiles.removeLast();
            if (t != null) {
                t.dumpData();
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */