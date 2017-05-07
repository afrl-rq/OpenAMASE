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
import avtas.map.graphics.MapRect;

/**
 *
 * @author AFRL/RQQD
 */
public class RectEditor extends GraphicEditor<MapRect> {

    DragPoint ul = new DragPoint(this);
    DragPoint lr = new DragPoint(this);
    DragPoint ll = new DragPoint(this);
    DragPoint ur = new DragPoint(this);

    public RectEditor(MapRect rect) {
        super(rect);
        addDragPoints(ul, lr, ll, ur);
        resetEditor();
    }

    @Override
    public void pointDragged(DragPoint dragPoint) {
        if (getGraphic().getRotation() != 0) {
            getGraphic().setByCornerPoint(dragPoint.getLat(), dragPoint.getLon());
        }
        else {
            if (dragPoint == ul) {
                getGraphic().setRectFromCorners(ul.getLat(), ul.getLon(), lr.getLat(), lr.getLon());
            }
            else if (dragPoint == ll) {
                getGraphic().setRectFromCorners(ul.getLat(), ll.getLon(), ll.getLat(), lr.getLon());
            }
            else if (dragPoint == ur) {
                getGraphic().setRectFromCorners(ur.getLat(), ul.getLon(), lr.getLat(), ur.getLon());
            }
            else if (dragPoint == lr) {
                getGraphic().setRectFromCorners(ul.getLat(), ul.getLon(), lr.getLat(), lr.getLon());
            }
        }
        resetEditor();
    }

    @Override
    protected void resetEditor() {
        ul.setLat(getGraphic().getUL()[0]);
        ul.setLon(getGraphic().getUL()[1]);
        ur.setLat(getGraphic().getUR()[0]);
        ur.setLon(getGraphic().getUR()[1]);
        ll.setLat(getGraphic().getLL()[0]);
        ll.setLon(getGraphic().getLL()[1]);
        lr.setLat(getGraphic().getLR()[0]);
        lr.setLon(getGraphic().getLR()[1]);
    }

    @Override
    public void setBounds(double degLat1, double degLon1, double degLat2, double degLon2) {
        //System.out.println("[" + degLat1 + ", " + degLon1 + ", " + degLat2 + ", " + degLon2 + "]");
        getGraphic().setRectFromCorners(degLat1, degLon1, degLat2, degLon2);
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