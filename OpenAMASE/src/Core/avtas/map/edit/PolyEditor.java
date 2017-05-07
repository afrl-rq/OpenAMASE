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
import avtas.map.graphics.MapPoly;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author AFRL/RQQD
 */
public class PolyEditor extends GraphicEditor<MapPoly> {

    public PolyEditor(MapPoly poly) {
        super(poly);
        resetEditor();
    }

    @Override
    public void pointDragged(DragPoint dragPoint) {
        getGraphic().clear();
        for (DragPoint p : getDragPoints()) {
            getGraphic().addPoint(p.getLat(), p.getLon());
        }
    }

    @Override
    protected void resetEditor() {
        getDragPoints().clear();
        double[] latlons = getGraphic().getLatLons();
        for (int i = 0; i < latlons.length; i += 2) {
            addDragPoints(new DragPoint(latlons[i], latlons[i + 1], this));
        }
    }

    @Override
    public void setBounds(double degLat1, double degLon1, double degLat2, double degLon2) {
        getGraphic().addPoint(degLat2, degLon2);
    }

    @Override
    public void translateGraphic(double dlat, double dlon, Proj proj) {
        double[] pts = getGraphic().getLatLons();
        getGraphic().clear();
        for (int i = 0; i < pts.length; i += 2) {
            getGraphic().addPoint(pts[i] + dlat, pts[i + 1] + dlon);
        }
        resetEditor();
    }

    public void addPoint(double degLat, double degLon) {
        getGraphic().addPoint(degLat, degLon);
        resetEditor();
    }
    
    @Override
    public void createPoint(MouseEvent e, double lat, double lon, final EditableLayer layer) {
        addPoint(lat, lon);
    }

    public void insertPoint(double degLat, double degLon) {
        double[] latlons = getGraphic().getLatLons();
        double rlat = Math.toRadians(degLat);
        double rlon = Math.toRadians(degLon);

        for (int i = 0; i < latlons.length - 3; i += 2) {
            double lat1 = Math.toRadians(latlons[i]);
            double lon1 = Math.toRadians(latlons[i + 1]);
            double lat2 = Math.toRadians(latlons[i + 2]);
            double lon2 = Math.toRadians(latlons[i + 3]);

            double dpts = NavUtils.distance(lat1, lon1, lat2, lon2);
            double d1 = NavUtils.distance(rlat, rlon, lat1, lon1);
            double d2 = NavUtils.distance(rlat, rlon, lat2, lon2);

            double normdist = Math.abs((d1 + d2 - dpts) / dpts);

            if (normdist < 0.02) {
                getGraphic().insertPoint(degLat, degLon, i / 2 + 1);
                break;
            }
        }
        resetEditor();
    }

    @Override
    public void addPopupItems(MouseEvent e, final double lat, final double lon,
            JPopupMenu menu, final EditableLayer layer) {
        
        final DragPoint dp = getDragPoint(e.getX(), e.getY());
        if (dp != null) {
            JMenuItem mi = new JMenuItem("Remove Point");
            mi.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deletePoint(dp);
                    layer.fireListenEvent(PolyEditor.this, EditListener.EDIT_END);
                    layer.refresh();
                }
            });
            menu.add(mi);
        }
        else {
            JMenuItem mi = new JMenuItem("Add Point");
            mi.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addPoint(lat, lon);
                    layer.fireListenEvent(PolyEditor.this, EditListener.EDIT_END);
                    layer.refresh();
                }
            });
            menu.add(mi);

            menu.add(new JMenuItem(new AbstractAction("Insert Point") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    insertPoint(lat, lon);
                    layer.refresh();
                }
            }));
            
            menu.add(new JMenuItem(new AbstractAction("End Edit") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    layer.endEdit();
                }
            }));
        }
    }

    /**
     * Deletes the point in the polygon based on the index value
     *
     * @param dp the drag point corresponding to the point to be deleted.
     */
    public void deletePoint(DragPoint dp) {
        getDragPoints().remove(dp);
        getGraphic().clear();
        for (DragPoint p : getDragPoints()) {
            getGraphic().addPoint(p.getLat(), p.getLon());
        }
    }

    public int getNumPoints() {
        return getGraphic().getNumPoints();
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */