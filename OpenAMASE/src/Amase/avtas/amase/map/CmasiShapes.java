// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.map;

import afrl.cmasi.AbstractGeometry;
import afrl.cmasi.AltitudeType;
import afrl.cmasi.Circle;
import afrl.cmasi.Location3D;
import afrl.cmasi.Polygon;
import afrl.cmasi.Rectangle;
import avtas.map.graphics.MapCircle;
import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapPoly;
import avtas.map.graphics.MapRect;
import java.util.List;

/**
 *
 * @author AFRL/RQQD
 */
public class CmasiShapes {
    
    /**
     * Returns a shape object compatible with the 2D map.
     * @param geom
     * @return a map graphic corresponding to the CMASI shape.  Defaults to MapPoly if the geometry is null
     */
    public static MapGraphic getMapShape(AbstractGeometry geom) {
        if (geom instanceof Polygon) {
            MapPoly poly = new MapPoly();
            List<Location3D> points = ((Polygon) geom).getBoundaryPoints();
            for (Location3D loc : points) {
                poly.addPoint(loc.getLatitude(), loc.getLongitude());
            }
            poly.setPolygon(true);
            return poly;
        }
        else if(geom instanceof Rectangle) {
            Rectangle rect = (Rectangle) geom;
            //Location3D loc = rect.getCenterPoint();
//            double ulat = Math.toDegrees(NavUtils.getLat(Math.toRadians(loc.getLatitude()),
//                    rect.getHeight() * 0.5, 0));
//            double llat = Math.toDegrees(NavUtils.getLat(Math.toRadians(loc.getLatitude()),
//                    -rect.getHeight() * 0.5, 0));
//            double elon = Math.toDegrees(NavUtils.getLon(Math.toRadians(loc.getLatitude()),
//                    Math.toRadians(loc.getLongitude()), rect.getWidth() * 0.5, 0));
//            double wlon = Math.toDegrees(NavUtils.getLon(Math.toRadians(loc.getLatitude()),
//                    Math.toRadians(loc.getLongitude()), -rect.getWidth() * 0.5, 0));

            MapRect maprect = new MapRect();
            maprect.setRectFromCenter(rect.getCenterPoint().getLatitude(), rect.getCenterPoint().getLongitude(),
                    rect.getWidth(), rect.getHeight(), rect.getRotation());
            return maprect;
        }
        else if(geom instanceof Circle) {
            Circle circ = (Circle) geom;
            MapCircle mapcirc = new MapCircle(circ.getCenterPoint().getLatitude(), circ.getCenterPoint().getLongitude(), circ.getRadius());
            return mapcirc;
        }
        else {
            MapPoly poly = new MapPoly();
            poly.setPolygon(true);
            return poly;
        }
    }

    /**
     * Creates a CMASI shape from the equivalent map shape.  If the shape is not compatible, returns null.
     */
    public static AbstractGeometry mapShapeToCMASI(MapGraphic mapGraphic) {
        if (mapGraphic instanceof MapPoly) {
            MapPoly mapPoly = (MapPoly) mapGraphic;
            double[] latlons = mapPoly.getLatLons();
            Polygon cmasiPoly = new Polygon();
            for (int i=0; i<latlons.length; i+=2) {
                cmasiPoly.getBoundaryPoints().add(new Location3D(latlons[i], latlons[i+1], 0, AltitudeType.MSL));
            }
            return cmasiPoly;
        }
        else if (mapGraphic instanceof MapRect) {
            MapRect maprect = (MapRect) mapGraphic;
            return new Rectangle(new Location3D(maprect.getCenterLat(), maprect.getCenterLon(), 0, AltitudeType.MSL),
                    (float) maprect.getWidth(), (float) maprect.getHeight(), (float) maprect.getRotation());
        }
        else if (mapGraphic instanceof MapCircle) {
            MapCircle mapcirc = (MapCircle) mapGraphic;
            Circle cmasiCirc = new Circle(new Location3D(mapcirc.getCenterLat(), mapcirc.getCenterLon(), 0, AltitudeType.MSL), (float) mapcirc.getRadius());
            return cmasiCirc;
        }
        return null;
    }
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */