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
import avtas.map.graphics.MapMarker;
import java.awt.event.MouseEvent;

/**
 * Creates an editor for MapMarker shapes
 * @author AFRL/RQQD
 */
public class MarkerEditor extends GraphicEditor<MapMarker>{

    DragPoint dp = new DragPoint(this);

    public MarkerEditor(MapMarker marker) {
        super(marker);
        addDragPoints(dp);
        resetEditor();
    }

    @Override
    public void pointDragged(DragPoint dragPoint) {
        getGraphic().setLat(dp.getLat());
        getGraphic().setLon(dp.getLon());
    }
    
    

    @Override
    public void setBounds(double degLat1, double degLon1, double degLat2, double degLon2) {
        getGraphic().setLat(degLat1);
        getGraphic().setLon(degLon1);
    }

    @Override
    protected void resetEditor() {
        dp.setLat(getGraphic().getLat());
        dp.setLon(getGraphic().getLon());
    }

    @Override
    public void translateGraphic(double deltaLat, double deltaLon, Proj proj) {
        getGraphic().setLat(getGraphic().getLat() + deltaLat);
        getGraphic().setLon(getGraphic().getLon() + deltaLon);
    }

    public void setPoint(double degLat, double degLon) {
        getGraphic().setLat(degLat);
        getGraphic().setLon(degLon);
        resetEditor();
    }

    @Override
    public void createPoint(MouseEvent e, double lat, double lon, final EditableLayer layer) {
        setPoint(lat, lon);
        layer.endEdit();
    }
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */