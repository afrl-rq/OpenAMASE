// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages;


import avtas.lmcp.LMCPObject;
import java.util.Arrays;

public class SeriesEnum implements avtas.lmcp.LMCPEnum {
 
    public static final String SERIES_NAME = "ALERTS";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4705211930599686144L;
    public static final int SERIES_VERSION = 1;


    private static String[] name_list = new String[]{
        "ZoneVertex",
        "Position2D",
        "ZoneViolation",
        "ActiveZoneViolation",
        "ImminentZoneViolation",
        "ProcessedZone"
    };

    public long getSeriesNameAsLong() { return SERIES_NAME_ID; }

    public String getSeriesName() { return SERIES_NAME; }

    public int getSeriesVersion() { return SERIES_VERSION; }

    public String getName(long type) {
        switch ((int) type) {
            case 1: return "ZoneVertex";
            case 2: return "Position2D";
            case 3: return "ZoneViolation";
            case 4: return "ActiveZoneViolation";
            case 5: return "ImminentZoneViolation";
            case 6: return "ProcessedZone";

        }
        
        return "";
    }

    public long getType(String name) {
       if ( name.equals("ZoneVertex")) return 1;
       if ( name.equals("Position2D")) return 2;
       if ( name.equals("ZoneViolation")) return 3;
       if ( name.equals("ActiveZoneViolation")) return 4;
       if ( name.equals("ImminentZoneViolation")) return 5;
       if ( name.equals("ProcessedZone")) return 6;

       
       return -1;
    }

    public LMCPObject getInstance(long type) {
        switch ((int) type) {
            case 1: return new ZoneVertex();
            case 2: return new Position2D();
            case 3: return new ZoneViolation();
            case 4: return new ActiveZoneViolation();
            case 5: return new ImminentZoneViolation();
            case 6: return new ProcessedZone();

        }

        return null;
    }

    public java.util.Collection<String> getAllTypes() {
        return Arrays.asList(name_list);
    }



}
