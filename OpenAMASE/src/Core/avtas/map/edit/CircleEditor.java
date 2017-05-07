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
import avtas.map.graphics.MapCircle;

/**
 *
 * @author AFRL/RQQD
 */
public class CircleEditor extends GraphicEditor<MapCircle> {

    private DragPoint top = new DragPoint(this);
    private DragPoint rt = new DragPoint(this);
    private DragPoint bot = new DragPoint(this);
    private DragPoint lt = new DragPoint(this);

    public CircleEditor(MapCircle circle) {
        super(circle);
        addDragPoints(top, rt, bot, lt);
        resetEditor();
    }

    @Override
    public void pointDragged(DragPoint dragPoint) {
        double mDist = NavUtils.distance( Math.toRadians(dragPoint.getLat()), Math.toRadians(dragPoint.getLon()),
                Math.toRadians(getGraphic().getCenterLat()), Math.toRadians(getGraphic().getCenterLon()));
        getGraphic().setRadius(mDist);
        resetEditor();
    }

    @Override
    protected void resetEditor() {
        MapCircle circle = getGraphic();
        double clat = Math.toRadians(circle.getCenterLat());
        double clon = Math.toRadians(circle.getCenterLon());
        top.setLon(circle.getCenterLon());
        top.setLat( Math.toDegrees(NavUtils.getLat(clat, circle.getRadius(), 0)));
        rt.setLon(Math.toDegrees(NavUtils.getLon(clat, clon, circle.getRadius(), 0)));
        rt.setLat(circle.getCenterLat());
        bot.setLon(circle.getCenterLon());
        bot.setLat(Math.toDegrees(NavUtils.getLat(clat, -circle.getRadius(), 0)));
        lt.setLon(Math.toDegrees(NavUtils.getLon(clat, clon, -circle.getRadius(), 0)));
        lt.setLat(circle.getCenterLat());
    }

    @Override
    public void setBounds(double degLat1, double degLon1, double degLat2, double degLon2) {
        double centLat = 0.5 * degLat1 + 0.5 * degLat2;
        double centLon = WorldMath.wrapLon(0.5 * degLon1 + 0.5 * degLon2);
        double radius = NavUtils.distance(Math.toRadians(centLat), Math.toRadians(centLon),
                Math.toRadians(degLat2), Math.toRadians(degLon2));
        //double radius = NavUtils.POLAR_RADIUS_M * Math.abs(Math.toRadians(degLat1 - centLat));
        getGraphic().setCenter(centLat, centLon);
        getGraphic().setRadius(radius);
        resetEditor();
    }

    
    public void translateGraphic(double dlat, double dlon, Proj proj) {
        getGraphic().setCenter(getGraphic().getCenterLat() + dlat, getGraphic().getCenterLon() + dlon);
        resetEditor();
    }


}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */