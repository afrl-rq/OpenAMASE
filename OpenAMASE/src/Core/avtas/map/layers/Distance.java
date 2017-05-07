// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.layers;

import avtas.data.Unit;
import avtas.map.MapMouseListener;
import avtas.map.MapPopupListener;
import avtas.map.graphics.Decoration;
import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapGraphicsList;
import avtas.map.graphics.MapLine;
import avtas.map.graphics.MapText;
import avtas.map.util.WGS84;
import avtas.util.NavUtils;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

/**
 *
 * @author AFRL/RQQD
 */
public class Distance extends GraphicsLayer<MapGraphic> implements MapMouseListener, MapPopupListener {


    JMenu distMenu = new JMenu("Distance Tool");
    Unit distUnit = Unit.METER;
    DecimalFormat format = new DecimalFormat("#.###");
    MapGraphicsList<DistanceGraphic> distanceGraphics = new MapGraphicsList<>();
    public static int PRESS_DISTANCE = 4;
    DistanceGraphic activeGraphic = null;
    // 0 for start, 1 for end
    int dragEnd = 0;
    private final AbstractAction clearAction;
    private final JMenu unitsMenu;

    public Distance() {

        add(distanceGraphics);

        unitsMenu = new JMenu("Set Units");

        List<Unit> unitTypes = Unit.getAllUnits(Unit.UnitType.Length);
        for (final Unit u : unitTypes) {
            unitsMenu.add(new AbstractAction(u.name()) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    distUnit = u;
                    for (DistanceGraphic g : distanceGraphics) {
                        g.refreshText();
                    }
                    refresh();
                }
            });
        }

        clearAction = new AbstractAction("Clear All") {
            @Override
            public void actionPerformed(ActionEvent e) {
                distanceGraphics.clear();
                refresh();
            }
        };


    }

    public void mouseMoved(MouseEvent e, double lat, double lon) {
        if (activeGraphic != null) {
            if (dragEnd == 0) {
                activeGraphic.setStartPoint(lat, lon);
            } else {
                activeGraphic.setEndPoint(lat, lon);
            }
            refresh();
        }

    }

    @Override
    public void mousePressed(MouseEvent e, double lat, double lon) {

        if (activeGraphic != null) {
            activeGraphic = null;
            return;
        }

        double x, y;
        for (DistanceGraphic g : distanceGraphics) {
            x = getProjection().getX(g.distLine.getStartLon());
            y = getProjection().getY(g.distLine.getStartLat());

            if (e.getPoint().distance(x, y) < PRESS_DISTANCE) {
                activeGraphic = g;
                dragEnd = 0;
                return;
            }
            x = getProjection().getX(g.distLine.getEndLon());
            y = getProjection().getY(g.distLine.getEndLat());

            if (e.getPoint().distance(x, y) < PRESS_DISTANCE) {
                activeGraphic = g;
                dragEnd = 1;
                return;
            }
        }
        activeGraphic = null;
        dragEnd = 0;



    }


    @Override
    public void addPopupMenuItems(JPopupMenu menu, MouseEvent e, final double lat, final double lon) {

        menu.add(distMenu);

        distMenu.removeAll();

        distMenu.add(new AbstractAction("New Measurement") {
            @Override
            public void actionPerformed(ActionEvent e) {
                DistanceGraphic g = new DistanceGraphic();
                g.setStartPoint(lat, lon);
                distanceGraphics.add(g);
                activeGraphic = g;
                dragEnd = 1;
                refresh();
            }
        });

        for (final DistanceGraphic g : distanceGraphics) {
            if (g.distText.contains(e.getPoint())) {
                distMenu.add(new AbstractAction("Remove Measurement") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        distanceGraphics.remove(g);
                        refresh();
                    }
                });
            }
        }

        distMenu.add(unitsMenu);
        distMenu.add(clearAction);

    }

    class DistanceGraphic extends MapGraphicsList<MapGraphic> {

        MapText distText = new MapText();
        MapLine distLine = new MapLine(0, 0, 0, 0);

        public DistanceGraphic() {
            distText.setHorizontalAlignment(SwingConstants.CENTER);
            //distText.setOffset(0, 20);
            distText.setColor(Color.WHITE);
            distText.setFill(new Color(0, 0, 0, 150));
            distLine.setPainter(Color.WHITE, 1);
            add(distLine);
            add(distText);

            distLine.setStartDecoration(new Decoration(Decoration.DecorationType.Flat, 5));
            distLine.setEndDecoration(new Decoration(Decoration.DecorationType.Flat, 5));
        }

        public void setStartPoint(double lat, double lon) {
            distLine.setStartPt(lat, lon);
            refreshText();
            Distance.this.project(this);
        }

        public void setEndPoint(double lat, double lon) {
            distLine.setEndPt(lat, lon);
            refreshText();
            Distance.this.project(this);
        }

        protected void refreshText() {
            distText.setLatLon( 0.5 * (distLine.getStartLat() + distLine.getEndLat()), 0.5 * (distLine.getStartLon() + distLine.getEndLon()) );
            
            int distanceM = (int) NavUtils.distance(Math.toRadians(distLine.getStartLat()), Math.toRadians(distLine.getStartLon()),
                    Math.toRadians(distLine.getEndLat()), Math.toRadians(distLine.getEndLon()));
            
            double val = distUnit.convertFrom(distanceM, Unit.METER);
            distText.setText(format.format(val) + " " + distUnit.toString());
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */