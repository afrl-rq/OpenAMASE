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
import avtas.map.graphics.MapText;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 *
 * @author AFRL/RQQD
 */
public class TextEditor extends GraphicEditor<MapText> {
    

    public TextEditor(MapText mapText) {
        super(mapText);
    }

    @Override
    public void pointDragged(DragPoint dragPoint) {
    }
    
    

    @Override
    public void setBounds(double degLat1, double degLon1, double degLat2, double degLon2) {
        getGraphic().setLatLon(degLat1, degLon1);
    }

    @Override
    protected void resetEditor() {
    }

    @Override
    public void translateGraphic(double deltaLat, double deltaLon, Proj proj) {
        getGraphic().setLatLon(getGraphic().getLat() + deltaLat, getGraphic().getLon() + deltaLon);
    }

    @Override
    public void addPopupItems(MouseEvent e, double lat, double lon, JPopupMenu menu, final EditableLayer layer) {
        JMenuItem item = new JMenuItem(new AbstractAction("Set Text") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ans = JOptionPane.showInputDialog(layer.getMap(), "Set Text");
                getGraphic().setText(ans);
                layer.refresh();
            }
        });
        menu.add(item);
    }

    @Override
    public void createPoint(MouseEvent e, double lat, double lon, final EditableLayer layer) {
        getGraphic().setLatLon(lat, lon);
        layer.endEdit();
    }

    @Override
    public boolean contains(Point2D point) {
        return getGraphic().contains(point);
    }
    
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */