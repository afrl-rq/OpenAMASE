// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.shapefile;

import gov.nasa.worldwind.formats.shapefile.Shapefile;

/**
 *
 * @author AFRL/RQQD
 */
public enum ShapeType {

    POLYLINE(3),
    POLYGON(5),
    POLYLINE_Z(13),
    POLYGON_Z(15);

    public final int shapeId;

    ShapeType(int shapeId) {
        this.shapeId = shapeId;
    }

    public static ShapeType fromId(int id) {
        for (ShapeType st : ShapeType.values()) {
            if (st.shapeId == id) {
                return st;
            }
        }
        return null;
    }

    /** Returns an enum for the equivalent Worldwind Shapefile type. */
    public static ShapeType fromString(String type) {
        String newType = "";
        if (Shapefile.isPolylineType(type)) {
            newType = "POLYLINE";
        }
        else if (Shapefile.isPolygonType(type)) {
            newType = "POLYGON";
        }
        else if (Shapefile.isPointType(type)) {
            newType = "POINT";
        }
        
        if (Shapefile.isZType(type)) {
            newType = newType + "_Z";
        }
        
        return ShapeType.valueOf(newType);
    }
};

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */