// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.layers;

import avtas.map.MapLayer;
import avtas.map.MapMouseListener;
import avtas.map.Proj;
import avtas.map.graphics.Painter;
import avtas.map.graphics.ScreenGraphic;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 * A layer that allows selection of an area on the map.
 * @author AFRL/RQQD
 */
public class RubberbandLayer extends MapLayer implements MapMouseListener {

    ScreenGraphic selectRect = null;
    private Point firstPt = null;
    public static final String RUBBERBAND_SELECTION_EVT = "RubberbandSelection";

    @Override
    public void paint(Graphics2D g) {
        if (selectRect != null) {
            selectRect.paint(g);
        }
    }

    @Override
    public void project(Proj proj) {
        if (selectRect != null) {
            selectRect.project(proj);
        }
    }

//    @Override
//    public void setMap(MapPanel parent) {
//        super.setMap(parent);
//        parent.addPropertyChangeListener(RUBBERBAND_SELECTION_EVT, new PropertyChangeListener() {
//
//            public void propertyChange(PropertyChangeEvent evt) {
//                System.out.println("new rect:" + evt.getNewValue());
//            }
//        });
//    }

    public void mouseMoved(MouseEvent e, double lat, double lon) {
    }

    public void mouseClicked(MouseEvent e, double lat, double lon) {
    }

    public void mouseDragged(MouseEvent e, double lat, double lon) {
        if (selectRect != null) {
            double dx = e.getX() - firstPt.getX();
            double dy = e.getY() - firstPt.getY();
            double ulx = dx > 0 ? firstPt.getX() : e.getX();
            double uly = dy > 0 ? firstPt.getY() : e.getY();

            if (e.isControlDown()) {
                int ddx = (int) Math.abs(dx);
                int ddy = (int) Math.abs(dy);
                selectRect.setShape(new Rectangle(
                        (int) (ulx -  ddx), (int) (uly - ddy), (int)(2*ddx), (int)(2*ddy)));
            }
            else {

                selectRect.setShape(new Rectangle((int) ulx, (int) uly,
                        (int) Math.abs(dx), (int) Math.abs(dy)));
            }

        }
        refresh();
    }

    public void mousePressed(MouseEvent e, double lat, double lon) {
        this.firstPt = e.getPoint();
        selectRect = new ScreenGraphic(new Rectangle(firstPt));
        selectRect.setPainter(Painter.createPainter(Color.GRAY, Painter.BasicDashStroke));
    }

    public void mouseReleased(MouseEvent e, double lat, double lon) {
        if (selectRect != null && getMap() != null) {
            Rectangle rect = (Rectangle) selectRect.getShape();
            getMap().putClientProperty("RubberbandSelection", new Rectangle((int) rect.getMinX(), (int) rect.getMinY(),
                    (int) (rect.getMaxX() - rect.getMinX()), (int) (rect.getMaxY() - rect.getMinY())));
        }
        selectRect = null;
        firstPt = null;
        refresh();
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */