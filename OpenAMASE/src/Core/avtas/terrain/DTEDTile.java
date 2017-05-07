// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.terrain;

import avtas.app.UserExceptions;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An interface to DTED files.  Files must conform to to MIL-PRF-89020-B
 * for headers, data types, and post-spacing for the given level and location.
 * 
 * @author AFRL/RQQD
 */
public class DTEDTile {

    public final static int UHL = 80;
    public final static int DSI = 648;
    public final static int ACC = 2700;
    public final int lat;
    public final int lon;
    public final double dlat;
    public final double dlon;
    public final int level;
    public final int numlats;
    public final int numlons;
    public final File file;

    private RandomAccessFile rf;
    //BinaryBufferedFile rf;
    short[][] data;

    public DTEDTile(File file) {
        try {
            rf = new RandomAccessFile(file, "r");
        } catch (Exception ex) {
            Logger.getLogger(DTEDTile.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.file = file;
        this.lat = readLat();
        this.lon = readLon();
        this.level = getLevel(file);

        this.dlat = readLatSpacing();
        this.dlon = readLonSpacing();

        numlats = readNumLats();
        numlons = readNumLons();

        data = new short[numlons][];


    }

    /**
     * returns elevation in meters for a given lat/lon in degrees.
     * @param lat latitude in degrees
     * @param lon longitude in degrees
     * @return elevation in meters
     */
    public short getElevation(double lat, double lon) {
        if (Math.floor(lat) != this.lat || Math.floor(lon) != this.lon) {
            return 0;
        }

        double rlat = lat - this.lat;
        double rlon = lon - this.lon;
        int lat_index = (int) Math.round(rlat * (numlats - 1));
        int lon_index = (int) Math.round(rlon * (numlons - 1));

        // ensure that the column of data has been read from the file
        readData(lon_index);

        return data[lon_index][lat_index];
    }

    public short[][] getAllElevations() {
        for (int i = 0; i < numlons; i++) {
            if (data[i] == null) {
                readData(i);
            }
        }
        return data;
    }

    /**
     * Computes an interpolated elevation based on the proportional location of the
     * lat, lon point to its nearest neighbors on four sides.
     * @param lat latitude in degrees
     * @param lon longitude in degrees
     * @return elevation in meters
     */
    public double getElevationInterp(double lat, double lon) {
        if (Math.floor(lat) != this.lat || Math.floor(lon) != this.lon) {
            return 0;
        }

        double rlat = (lat - this.lat) * (numlats - 1);
        double rlon = (lon - this.lon) * (numlons - 1);

        int floor_lat_index = (int) Math.floor(rlat);
        int floor_lon_index = (int) Math.floor(rlon);
        int ceil_lat_index = (int) Math.ceil(rlat);
        int ceil_lon_index = (int) Math.ceil(rlon);

        // ensure that the data columns have been read.  Ceil and floor indicies
        // should always be within the bounds of the tile, since the "floor" test on
        // lat, lon is performed at the beginning of the method.
        readData(floor_lon_index);
        readData(ceil_lon_index);

        // proportions of lat, lon from the bottom left corner
        double lat_diff = rlat - floor_lat_index;
        double lon_diff = rlon - floor_lon_index;

        // get heights surrounding the [lat, lon].  This starts at the lower-left of
        // the rectangle and moves counter-clockwise
        short h1 = data[floor_lon_index][floor_lat_index];
        short h2 = data[floor_lon_index][ceil_lat_index];
        short h3 = data[ceil_lon_index][ceil_lat_index];
        short h4 = data[ceil_lon_index][floor_lat_index];

        // calculate the inerpolated height based on the proportional distance from the
        // four corners
        double h = (h2 * lat_diff + h1 * (1 - lat_diff)) * (1 - lon_diff)
                + (h3 * lat_diff + h4 * (1 - lat_diff)) * lon_diff;

        return h;
    }

    /**
     * Reads the data from the dted file (if it has not been read already) and
     * stores it in memory.
     */
    public void readData(int column) {
        if (data[column] == null) {
            try {
                //skip the DTED headers
                //and preceeding columns (column = header + 2 * numlats + checksum)
                //skip the sentinel (1), block count (3), lat (2), lon (2)
                rf.seek(UHL + DSI + ACC + column * (12 + 2 * numlats) + 8);

                // create the column of data
                short[] col = new short[numlats];
                data[column] = col;
                short h;

                byte[] bytes = new byte[2 * numlats];
                rf.readFully(bytes);

                // read in all of the short values
                for (int j = 0; j < numlats; j++) {
                    h = (short) ((bytes[j * 2] << 8) | (bytes[j * 2 + 1] & 0xff));
                    col[j] = h == Short.MIN_VALUE ? 0 : h;
                }

                // skip the checksum (4)
                //rf.skipBytes(4);

            } catch (Exception ex) {
                UserExceptions.showError(this, "Cannot read file " + file.getPath(), ex);
                Logger.getLogger(DTEDTile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /** sets the data to null to allow for garbage collection */
    public void dumpData() {
        data = new short[numlons][];
    }

    /** Reads the latitude value according to the stored ASCII value in the UHL section of the
     *  dted file.  See the DTED specification for details
     *
     * @return latitude according to the UHL in decimal degrees
     */
    protected int readLat() {
        try {
            int lat = readInt(12, 3);
            rf.seek(19);
            char h = (char) rf.readByte();
            lat = (h == 'N' || h == 'n') ? lat : -lat;
            return lat;
        } catch (IOException ex) {
            Logger.getLogger(DTEDTile.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }

    }

    /** Reads the longitude value according to the stored ASCII value in the UHL section of the
     *  dted file.  See the DTED specification for details
     *
     * @return longitude according to the UHL in decimal degrees
     */
    protected int readLon() {
        try {
            int lon = readInt(4, 3);
            rf.seek(11);
            char h = (char) rf.readByte();
            lon = (h == 'E' || h == 'e') ? lon : -lon;
            return lon;
        } catch (IOException ex) {
            Logger.getLogger(DTEDTile.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    protected double readLonSpacing() {
        return readInt(20, 4) / 3600.0;
    }

    protected double readLatSpacing() {
        return readInt(24, 4) / 3600.0;
    }

    protected int readNumLons() {
        return readInt(47, 4);
    }

    protected int readNumLats() {
        return readInt(51, 4);
    }

    static int getLevel(File file) {
        String name = file.getName();
        return Integer.parseInt(name.substring(name.length() - 1));
    }

    protected int readInt(int position, int length) {
        try {
            rf.seek(position);
            byte[] bytes = new byte[length];
            rf.readFully(bytes);
            return Integer.parseInt(new String(bytes));
        } catch (IOException ex) {
            Logger.getLogger(DTEDTile.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    /**
     * Returns the spacing between posts in the longitudinal direction for
     * the given level and latitude.
     * @param level DTED level (0, 1, or 2)
     * @param lat latitude in degrees
     * @return post spacing in meters
     */
    static double getLonPostSpacing(int level, double lat) {

        int zone_mult = 1;
        int abs_lat = (int) Math.abs(lat);

        if (abs_lat >= 80) {
            zone_mult = 6;
        } else if (abs_lat >= 75) {
            zone_mult = 4;
        } else if (abs_lat >= 70) {
            zone_mult = 3;
        } else if (abs_lat >= 50) {
            zone_mult = 2;
        }

        return getPostSpacing(level) * zone_mult;
    }

    /**
     * Returns the latitudinal post spacing for the requested level.  Note that
     * longitudinal spacing varies based on zone. (see DTED standard).  For zone
     * I, lat spacing equals lon spacing.
     * @param level DTED level requested.
     * @return spacing of height posts in latitudinal direction (degrees)
     */
    public static double getPostSpacing(int level) {
        switch (level) {
            case 0:
                return 30.0 / 3600.;
            case 1:
                return 3.0 / 3600.;
            case 2:
                return 1.0 / 3600.;
            default:
                return 30.0 / 3600.;
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */