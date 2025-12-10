// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.vehicles;


import avtas.lmcp.LMCPObject;
import java.util.Arrays;

public class SeriesEnum implements avtas.lmcp.LMCPEnum {
 
    public static final String SERIES_NAME = "VEHICLES";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6216454340153722195L;
    public static final int SERIES_VERSION = 1;


    private static String[] name_list = new String[]{
        "GroundVehicleConfiguration",
        "GroundVehicleState",
        "SurfaceVehicleConfiguration",
        "SurfaceVehicleState",
        "StationarySensorConfiguration",
        "StationarySensorState"
    };

    public long getSeriesNameAsLong() { return SERIES_NAME_ID; }

    public String getSeriesName() { return SERIES_NAME; }

    public int getSeriesVersion() { return SERIES_VERSION; }

    public String getName(long type) {
        switch ((int) type) {
            case 1: return "GroundVehicleConfiguration";
            case 2: return "GroundVehicleState";
            case 3: return "SurfaceVehicleConfiguration";
            case 4: return "SurfaceVehicleState";
            case 5: return "StationarySensorConfiguration";
            case 6: return "StationarySensorState";

        }
        
        return "";
    }

    public long getType(String name) {
       if ( name.equals("GroundVehicleConfiguration")) return 1;
       if ( name.equals("GroundVehicleState")) return 2;
       if ( name.equals("SurfaceVehicleConfiguration")) return 3;
       if ( name.equals("SurfaceVehicleState")) return 4;
       if ( name.equals("StationarySensorConfiguration")) return 5;
       if ( name.equals("StationarySensorState")) return 6;

       
       return -1;
    }

    public LMCPObject getInstance(long type) {
        switch ((int) type) {
            case 1: return new GroundVehicleConfiguration();
            case 2: return new GroundVehicleState();
            case 3: return new SurfaceVehicleConfiguration();
            case 4: return new SurfaceVehicleState();
            case 5: return new StationarySensorConfiguration();
            case 6: return new StationarySensorState();

        }

        return null;
    }

    public java.util.Collection<String> getAllTypes() {
        return Arrays.asList(name_list);
    }



}
