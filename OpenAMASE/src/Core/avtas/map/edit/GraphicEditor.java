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
import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapGraphicsList;
import avtas.map.graphics.Painter;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.JPopupMenu;

/**
 * Base class for all active shapes. An active shape allows moving, resizing,
 * and, optionally, modification of the shape of the graphic.
 *
 * @author AFRL/RQQD
 */
public abstract class GraphicEditor<T extends MapGraphic> extends MapGraphicsList {

    //private boolean active = true;
    private MapGraphicsList<DragPoint> dragPoints = new MapGraphicsList<DragPoint>();
    private T graphic;
    private boolean active = false;
    
    protected static Painter defaultSelectedPainter = Painter.createOutlinePainter(Color.YELLOW, Color.WHITE, 3);
    
    protected Painter selectedPainter = defaultSelectedPainter;
    
    
    
    public GraphicEditor(T graphic) {
        if (graphic == null) {
            throw new NullPointerException("Null Graphic not allowed in editor.");
        }
        this.graphic = graphic;
        setActive(false);
        
        add(dragPoints);
        add(graphic);
    }
    
    public void setActive(boolean active) {
        this.active = active;
        dragPoints.setVisible(active);
    }
    
    public boolean isActive() {
        return active;
    }
    
    @Override
    public void setSelected(boolean selected) {
        graphic.setSelected(selected);
        super.setSelected(selected);
    }
    

    /**
     * returns an array of points that can be used to manipulate the shape
     */
    public MapGraphicsList<DragPoint> getDragPoints() {
        return dragPoints;
    }

    public T getGraphic() {
        return graphic;
    }

    public DragPoint getDragPoint(int x, int y) {
        for (DragPoint dp : dragPoints) {
            if (dp.getBounds().contains(x, y)) {
                return dp;
            }
        }
        return null;
    }

    protected void addDragPoints(DragPoint... points) {
        for (DragPoint p : points) {
            p.setEditor(this);
            dragPoints.add(p);
        }
    }

//    @Override
//    public void project(Proj view) {
//        dragPoints.project(view);
//        graphic.project(view);
//    }
//
//    @Override
//    public void paint(Graphics2D g) {
//        super.paint(g);
//        dragPoints.paint(g);
//        graphic.paint(g);
//    }

    public abstract void pointDragged(DragPoint dragPoint);

    /**
     * Resizes the shape to fit the bounds and returns it.
     *
     * @param degLat1 the latitude of the first point defining a bounds
     * @param degLon1 the longitude of the first point defining a bounds
     * @param degLat2 the latitude of the second point defining a bounds
     * @param degLon2 the longitude of the second point defining a bounds
     */
    public abstract void setBounds(double degLat1, double degLon1, double degLat2, double degLon2);

    /**
     * Resets the editor to conform to an updated shape
     */
    protected abstract void resetEditor();

    /**
     * requests that the associated graphic moves the given amount in screen
     * coordinates
     *
     * @param deltaLat change in ns-direction to move the graphic
     * @param deltaLon change in ew-direction to move the graphic
     * @param proj the current projection
     */
    public abstract void translateGraphic(double deltaLat, double deltaLon, Proj proj);
    
    /**
     * Allows the editor to return custom menu items for editing the graphic.
     * @param menu 
     */
    public void addPopupItems(MouseEvent e, double lat, double lon,
            JPopupMenu menu, EditableLayer layer) {
    }
    
    public void createPoint(MouseEvent e, double lat, double lon, EditableLayer layer) {}
    

    @Override
    public boolean onEdge(int screenX, int screenY, int maxDist) {
        return getGraphic() != null && getGraphic().onEdge(screenX, screenY, maxDist);
    }

    @Override
    public boolean contains(Point2D point) {
        return getGraphic() != null && getGraphic().getFill() != null 
                && getGraphic().contains(point);
    }
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */