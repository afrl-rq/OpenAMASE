// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.map;

import afrl.cmasi.AbstractZone;
import afrl.cmasi.KeepInZone;
import afrl.cmasi.KeepOutZone;
import afrl.cmasi.RemoveZones;
import avtas.amase.objtree.ObjectTree;
import avtas.amase.scenario.ScenarioEvent;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapPoly;
import avtas.map.graphics.Painter;
import avtas.map.layers.GraphicsLayer;
import avtas.util.Colors;
import avtas.util.WindowUtils;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ListIterator;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

/**
 * A layer that displays CMASI zone shapes
 *
 * @author AFRL/RQQD
 */
public class ZoneLayer extends GraphicsLayer<MapGraphic> implements AppEventListener {

    //Color keepOutLine = Color.RED;
    Color keepOutFill = Colors.setAlpha(Color.RED, 0.3);
    //Color keepInLine = Color.YELLOW;
    Color keepInFill = Colors.setAlpha(Color.YELLOW, 0.3);
    //Stroke zoneStroke = new BasicStroke(1f);
    
    protected static Painter keepOutPainter = Painter.createOutlinePainter(Color.RED, Color.WHITE, 1f);
    protected static Painter keepInPainter = Painter.createOutlinePainter(Color.YELLOW, Color.WHITE, 1f);

    public ZoneLayer() {
        AppEventManager.getDefaultEventManager().addListener(this);
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof AbstractZone) {
            AbstractZone z = (AbstractZone) event;
            MapGraphic g = createGraphic(z);

            for (ListIterator<MapGraphic> it = getList().listIterator(); it.hasNext();) {
                if (((AbstractZone) it.next().getRefObject()).getZoneID() == z.getZoneID()) {
                    it.remove();
                }
            }
            if (g != null) {
                add(g);
                refresh();
            }

        }
        else if (event instanceof ScenarioEvent) {
            clear();
            refresh();
        }
        else if (event instanceof RemoveZones) {
            RemoveZones rz = (RemoveZones) event;
            for (long id : rz.getZoneList()) {
                int index = indexOf(getByRefObject(id));
                if (index != -1) {
                    remove(index);
                }
            }
        }
    }

    MapGraphic createGraphic(AbstractZone zone) {
        MapGraphic g = CmasiShapes.getMapShape(zone.getBoundary());
        if (zone instanceof KeepOutZone) {
            if (!(g instanceof MapPoly) || ((MapPoly) g).isPolygon()) {
                g.setFill(keepOutFill);
            }
            g.setPainter(keepOutPainter);
        }
        else if (zone instanceof KeepInZone) {
            g.setPainter(keepInPainter);
        }

        //g.setStroke(zoneStroke);
        g.setRefObject(zone);
        g.setName(zone.getClass().getSimpleName() + " " + zone.getZoneID());

        return g;
    }

    @Override
    public void addPopupMenuItems(javax.swing.JPopupMenu menu, MouseEvent e, double lat, double lon) {
        for (final MapGraphic g : this) {
            if (g.onEdge(e.getX(), e.getY(), 4) || (g.contains(e.getPoint()) && g.getFill() != null)) {
                menu.add(new JMenuItem(new AbstractAction(g.getName()) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ObjectTree tree = new ObjectTree(g.getRefObject());
                        tree.setEditable(false);
                        WindowUtils.showPlainDialog(getMap(), new JScrollPane(tree), g.getName());
                    }
                }));
            }
        }
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */