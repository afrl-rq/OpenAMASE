// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.edit;

import avtas.util.NavUtils;
import avtas.map.Proj;
import avtas.map.util.WorldMath;
import avtas.map.graphics.MapEllipse;

/**
 *
 * @author AFRL/RQQD
 */
public class EllipseEditor extends GraphicEditor<MapEllipse> {

    private DragPoint top = new DragPoint(this);
    private DragPoint rt = new DragPoint(this);
    private DragPoint bot = new DragPoint(this);
    private DragPoint lt = new DragPoint(this);

    public EllipseEditor(MapEllipse ellipse) {
        super(ellipse);
        addDragPoints(top, rt, bot, lt);
        resetEditor();
    }

    @Override
    public void pointDragged(DragPoint dragPoint) {
        double mDist = NavUtils.distance(Math.toRadians(dragPoint.getLat()), Math.toRadians(dragPoint.getLon()),
                Math.toRadians(getGraphic().getCenterLat()), Math.toRadians(getGraphic().getCenterLon()));
        if (dragPoint == top || dragPoint == bot) {
            getGraphic().setRadius(getGraphic().getRadiusX(), mDist);
        } else if (dragPoint == lt || dragPoint == rt) {
            getGraphic().setRadius(mDist, getGraphic().getRadiusY());
        }
        resetEditor();
    }

    @Override
    protected void resetEditor() {
        MapEllipse ellipse = getGraphic();
        double clat = Math.toRadians(ellipse.getCenterLat());
        double clon = Math.toRadians(ellipse.getCenterLon());
        double rot_rad = Math.toRadians(ellipse.getRotation());
        
        double[] latlon = NavUtils.getLatLon(clat, clon, ellipse.getRadiusY(), rot_rad );
        top.setLon(Math.toDegrees(latlon[1]));
        top.setLat(Math.toDegrees(latlon[0]));
        
        latlon = NavUtils.getLatLon(clat, clon, -ellipse.getRadiusY(), rot_rad );
        bot.setLon(Math.toDegrees(latlon[1]));
        bot.setLat(Math.toDegrees(latlon[0]));
        
        latlon = NavUtils.getLatLon(clat, clon, ellipse.getRadiusX(), rot_rad + 0.5 * Math.PI);
        rt.setLon(Math.toDegrees(latlon[1]));
        rt.setLat(Math.toDegrees(latlon[0]));
        
        latlon = NavUtils.getLatLon(clat, clon, ellipse.getRadiusX(), rot_rad - 0.5 * Math.PI);
        lt.setLon(Math.toDegrees(latlon[1]));
        lt.setLat(Math.toDegrees(latlon[0]));
        
        
//        top.setLat(Math.toDegrees(NavUtils.getLat(clat, ellipse.getRadiusY(), 0)));
//        rt.setLon(Math.toDegrees(NavUtils.getLon(clat, clon, ellipse.getRadiusX(), 0)));
//        rt.setLat(ellipse.getCenterLat());
//        bot.setLon(ellipse.getCenterLon());
//        bot.setLat(Math.toDegrees(NavUtils.getLat(clat, -ellipse.getRadiusY(), 0)));
//        lt.setLon(Math.toDegrees(NavUtils.getLon(clat, clon, -ellipse.getRadiusX(), 0)));
//        lt.setLat(ellipse.getCenterLat());
    }

    @Override
    public void setBounds(double degLat1, double degLon1, double degLat2, double degLon2) {
        double cenLat = 0.5 * degLat1 + 0.5 * degLat2;
        double centLon = WorldMath.wrapLon(0.5 * degLon1 + 0.5 * degLon2);
        double radiusNS = NavUtils.POLAR_RADIUS_M * Math.abs(degLon1 - cenLat);
        double radiusEW = NavUtils.getRadius(Math.toRadians(cenLat)) * Math.abs(Math.toRadians(degLat1 - cenLat));
        getGraphic().setCenter(cenLat, centLon);
        getGraphic().setRadius(radiusEW, radiusNS);
        getGraphic().setRotation(0);
        resetEditor();
    }

    @Override
    public void translateGraphic(double dlat, double dlon, Proj proj) {
        getGraphic().setCenter(getGraphic().getCenterLat() + dlat, getGraphic().getCenterLon() + dlon);
        resetEditor();
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */