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
import avtas.map.graphics.MapArc;


/**
 *
 * @author AFRL/RQQD
 */
public class ArcEditor extends GraphicEditor<MapArc> {
    
    DragPoint startArc = new DragPoint(this);
    DragPoint endArc = new DragPoint(this);

    //MapArc arc;

    public ArcEditor(MapArc arc) {
        super(arc);
        addDragPoints(startArc, endArc);
    }

    @Override
    public void pointDragged(DragPoint dragPoint) {
        if (dragPoint == startArc) {
            //getGraphic().setStartAngle( Angle.Radian(
            //        WorldMath.azimuthBetween(getGraphic().getCenter(), startArc.getLocation())));
            resetEditor();
        }
        else if (dragPoint == endArc) {
            //getGraphic().setEndAngle( Angle.Radian(
            //        WorldMath.azimuthBetween(getGraphic().getCenter(), startArc.getLocation())));
            resetEditor();
        }
    }

    @Override
    protected void resetEditor() {

        double radCenLat = Math.toRadians(getGraphic().getCenterLat());
        double radCenLon = Math.toRadians(getGraphic().getCenterLon());
        double radStartAngle = Math.toRadians(getGraphic().getStartAngle());

        double[] loc = NavUtils.getLatLon(radCenLat, radCenLon, getGraphic().getRadius(), radStartAngle);
        startArc.setLat(Math.toDegrees(loc[0]));
        startArc.setLon(Math.toDegrees(loc[1]));
        loc = NavUtils.getLatLon(radCenLat, radCenLon, getGraphic().getRadius(),
                radStartAngle + Math.toRadians(getGraphic().getArcExtent()));
        endArc.setLat(Math.toDegrees(loc[0]));
        endArc.setLon(Math.toDegrees(loc[1]));
    }

    @Override
    public void setBounds(double degLat1, double degLon1, double degLat2, double degLon2) {

    }

    @Override
    public void translateGraphic(double dlat, double dlon, Proj proj) {
        getGraphic().setCenter(getGraphic().getCenterLat() + dlat, getGraphic().getCenterLon() + dlon);
        resetEditor();
    }


}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */