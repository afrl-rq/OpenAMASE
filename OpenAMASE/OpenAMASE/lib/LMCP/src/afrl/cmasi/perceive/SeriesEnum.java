// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.cmasi.perceive;


import avtas.lmcp.LMCPObject;
import java.util.Arrays;

public class SeriesEnum implements avtas.lmcp.LMCPEnum {
 
    public static final String SERIES_NAME = "PERCEIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5784119745305990725L;
    public static final int SERIES_VERSION = 1;


    private static String[] name_list = new String[]{
        "EntityPerception",
        "TrackEntityAction",
        "TrackEntityTask"
    };

    public long getSeriesNameAsLong() { return SERIES_NAME_ID; }

    public String getSeriesName() { return SERIES_NAME; }

    public int getSeriesVersion() { return SERIES_VERSION; }

    public String getName(long type) {
        switch ((int) type) {
            case 1: return "EntityPerception";
            case 2: return "TrackEntityAction";
            case 3: return "TrackEntityTask";

        }
        
        return "";
    }

    public long getType(String name) {
       if ( name.equals("EntityPerception")) return 1;
       if ( name.equals("TrackEntityAction")) return 2;
       if ( name.equals("TrackEntityTask")) return 3;

       
       return -1;
    }

    public LMCPObject getInstance(long type) {
        switch ((int) type) {
            case 1: return new EntityPerception();
            case 2: return new TrackEntityAction();
            case 3: return new TrackEntityTask();

        }

        return null;
    }

    public java.util.Collection<String> getAllTypes() {
        return Arrays.asList(name_list);
    }



}
