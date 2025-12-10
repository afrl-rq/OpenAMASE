// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package larcfm.DAIDALUS;


import avtas.lmcp.LMCPObject;
import java.util.Arrays;

public class SeriesEnum implements avtas.lmcp.LMCPEnum {
 
    public static final String SERIES_NAME = "DAIDALUS";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4918292825567417683L;
    public static final int SERIES_VERSION = 1;


    private static String[] name_list = new String[]{
        "GroundHeadingInterval",
        "GroundSpeedInterval",
        "VerticalSpeedInterval",
        "AltitudeInterval",
        "GroundHeadingRecoveryInterval",
        "GroundSpeedRecoveryInterval",
        "VerticalSpeedRecoveryInterval",
        "AltitudeRecoveryInterval",
        "WellClearViolationIntervals",
        "DAIDALUSConfiguration"
    };

    public long getSeriesNameAsLong() { return SERIES_NAME_ID; }

    public String getSeriesName() { return SERIES_NAME; }

    public int getSeriesVersion() { return SERIES_VERSION; }

    public String getName(long type) {
        switch ((int) type) {
            case 1: return "GroundHeadingInterval";
            case 2: return "GroundSpeedInterval";
            case 3: return "VerticalSpeedInterval";
            case 4: return "AltitudeInterval";
            case 5: return "GroundHeadingRecoveryInterval";
            case 6: return "GroundSpeedRecoveryInterval";
            case 7: return "VerticalSpeedRecoveryInterval";
            case 8: return "AltitudeRecoveryInterval";
            case 9: return "WellClearViolationIntervals";
            case 10: return "DAIDALUSConfiguration";

        }
        
        return "";
    }

    public long getType(String name) {
       if ( name.equals("GroundHeadingInterval")) return 1;
       if ( name.equals("GroundSpeedInterval")) return 2;
       if ( name.equals("VerticalSpeedInterval")) return 3;
       if ( name.equals("AltitudeInterval")) return 4;
       if ( name.equals("GroundHeadingRecoveryInterval")) return 5;
       if ( name.equals("GroundSpeedRecoveryInterval")) return 6;
       if ( name.equals("VerticalSpeedRecoveryInterval")) return 7;
       if ( name.equals("AltitudeRecoveryInterval")) return 8;
       if ( name.equals("WellClearViolationIntervals")) return 9;
       if ( name.equals("DAIDALUSConfiguration")) return 10;

       
       return -1;
    }

    public LMCPObject getInstance(long type) {
        switch ((int) type) {
            case 1: return new GroundHeadingInterval();
            case 2: return new GroundSpeedInterval();
            case 3: return new VerticalSpeedInterval();
            case 4: return new AltitudeInterval();
            case 5: return new GroundHeadingRecoveryInterval();
            case 6: return new GroundSpeedRecoveryInterval();
            case 7: return new VerticalSpeedRecoveryInterval();
            case 8: return new AltitudeRecoveryInterval();
            case 9: return new WellClearViolationIntervals();
            case 10: return new DAIDALUSConfiguration();

        }

        return null;
    }

    public java.util.Collection<String> getAllTypes() {
        return Arrays.asList(name_list);
    }



}
