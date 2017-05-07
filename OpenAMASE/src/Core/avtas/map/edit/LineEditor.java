// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.edit;

import avtas.map.Proj;
import avtas.map.graphics.MapLine;


/**
 *
 * @author AFRL/RQQD
 */
public class LineEditor extends GraphicEditor<MapLine>{

    DragPoint pt1 = new DragPoint(this);
    DragPoint pt2 = new DragPoint(this);

    public LineEditor(MapLine line) {
        super(line);
        addDragPoints(pt1, pt2);
        resetEditor();
    }

    @Override
    public void pointDragged(DragPoint dragPoint) {
        if (dragPoint == pt1) {
            getGraphic().setStartPt(dragPoint.getLat(), dragPoint.getLon());
        }
        else if (dragPoint == pt2) {
            getGraphic().setEndPt(dragPoint.getLat(), dragPoint.getLon());
        }
    }

    @Override
    protected void resetEditor() {
        pt1.setLat(getGraphic().getStartLat());
        pt1.setLon(getGraphic().getStartLon());
        pt2.setLat(getGraphic().getEndLat());
        pt2.setLon(getGraphic().getEndLon());
    }


    @Override
    public void setBounds(double degLat1, double degLon1, double degLat2, double degLon2) {
        getGraphic().setStartPt(degLat1, degLon1);
        getGraphic().setEndPt(degLat2, degLon2);
        resetEditor();
    }

    @Override
    public void translateGraphic(double dlat, double dlon, Proj proj) {
        MapLine line = getGraphic();
        line.setStartPt(line.getStartLat()+ dlat, line.getStartLon() + dlon);
        line.setEndPt(line.getEndLat() + dlat, line.getEndLon() + dlon);
        resetEditor();
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */