// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages.route;


import avtas.lmcp.LMCPObject;
import java.util.Arrays;

public class SeriesEnum implements avtas.lmcp.LMCPEnum {
 
    public static final String SERIES_NAME = "ROUTE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5931053054693474304L;
    public static final int SERIES_VERSION = 4;


    private static String[] name_list = new String[]{
        "GraphNode",
        "GraphEdge",
        "GraphRegion",
        "RouteConstraints",
        "RouteRequest",
        "RoutePlanRequest",
        "RoutePlan",
        "RoutePlanResponse",
        "RouteResponse",
        "EgressRouteRequest",
        "EgressRouteResponse",
        "RoadPointsConstraints",
        "RoadPointsRequest",
        "RoadPointsResponse"
    };

    public long getSeriesNameAsLong() { return SERIES_NAME_ID; }

    public String getSeriesName() { return SERIES_NAME; }

    public int getSeriesVersion() { return SERIES_VERSION; }

    public String getName(long type) {
        switch ((int) type) {
            case 1: return "GraphNode";
            case 2: return "GraphEdge";
            case 3: return "GraphRegion";
            case 4: return "RouteConstraints";
            case 5: return "RouteRequest";
            case 6: return "RoutePlanRequest";
            case 7: return "RoutePlan";
            case 8: return "RoutePlanResponse";
            case 9: return "RouteResponse";
            case 10: return "EgressRouteRequest";
            case 11: return "EgressRouteResponse";
            case 12: return "RoadPointsConstraints";
            case 13: return "RoadPointsRequest";
            case 14: return "RoadPointsResponse";

        }
        
        return "";
    }

    public long getType(String name) {
       if ( name.equals("GraphNode")) return 1;
       if ( name.equals("GraphEdge")) return 2;
       if ( name.equals("GraphRegion")) return 3;
       if ( name.equals("RouteConstraints")) return 4;
       if ( name.equals("RouteRequest")) return 5;
       if ( name.equals("RoutePlanRequest")) return 6;
       if ( name.equals("RoutePlan")) return 7;
       if ( name.equals("RoutePlanResponse")) return 8;
       if ( name.equals("RouteResponse")) return 9;
       if ( name.equals("EgressRouteRequest")) return 10;
       if ( name.equals("EgressRouteResponse")) return 11;
       if ( name.equals("RoadPointsConstraints")) return 12;
       if ( name.equals("RoadPointsRequest")) return 13;
       if ( name.equals("RoadPointsResponse")) return 14;

       
       return -1;
    }

    public LMCPObject getInstance(long type) {
        switch ((int) type) {
            case 1: return new GraphNode();
            case 2: return new GraphEdge();
            case 3: return new GraphRegion();
            case 4: return new RouteConstraints();
            case 5: return new RouteRequest();
            case 6: return new RoutePlanRequest();
            case 7: return new RoutePlan();
            case 8: return new RoutePlanResponse();
            case 9: return new RouteResponse();
            case 10: return new EgressRouteRequest();
            case 11: return new EgressRouteResponse();
            case 12: return new RoadPointsConstraints();
            case 13: return new RoadPointsRequest();
            case 14: return new RoadPointsResponse();

        }

        return null;
    }

    public java.util.Collection<String> getAllTypes() {
        return Arrays.asList(name_list);
    }



}
