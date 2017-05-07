// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.validator.tests;

import afrl.cmasi.AbstractGeometry;
import afrl.cmasi.Circle;
import afrl.cmasi.Polygon;
import afrl.cmasi.Rectangle;

/**
 *
 * @author AFRL/RQQD
 */
public class GeometryCheck {
    
    static String geometryCheck(AbstractGeometry geom) {
        if (geom.getClass() == AbstractGeometry.class || geom == null) {
            return "Invalid Geometry defined.";
        }
        if (geom instanceof Polygon) {
            return ((Polygon) geom).getBoundaryPoints().size() > 3 ? "" : "Polygons need at least 3 points.";
        }
        else if (geom instanceof Circle) {
            return ((Circle) geom).getRadius() > 0 ? "" : "Circles need a radius > 0.";
        }
        else if (geom instanceof Rectangle) {
            Rectangle rect = (Rectangle) geom;
            if ( (rect.getHeight() <= 0) || (rect.getWidth() <= 0) ) {
                return "Rectangles need height and width > 0";
            }
            return "";
        }
        return "";
    }
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */