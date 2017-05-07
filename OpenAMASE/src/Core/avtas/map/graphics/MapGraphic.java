// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.graphics;

import avtas.map.Proj;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Defines the base class for all map graphics.   
 *
 * @author AFRL/RQQD
 */
public abstract class MapGraphic {

    protected Shape screenShape = null;
    protected Painter painter = Painter.createPainter(Color.BLACK);
    protected Paint fill = null;
    
    protected Object refObject = null;
    protected MapGraphic parent = null;
    private boolean visible = true;
    private boolean projected = false;
    private String name = "";
    
    protected Painter selectedPainter = null;
    protected Paint selectedFill = null;
    protected boolean selected = false;
    
    Decoration startDecoration = null, endDecoration = null;


    /**
     * Called to transform the graphic from world coordinates to screen coordinates in the current view
     * @param proj the current projection (view)
     */
    public abstract void project(Proj proj);

    /**
     * Returns the boundary of the screen shape in window coordinates
     * @return the bounds of the current screen shape
     */
    public Rectangle getBounds() {
        return (screenShape == null || !isVisible()) ? null : screenShape.getBounds();
    }

    /**
     * Sets a reference object that can be associated with this graphic.
     *
     * @param obj the object to be associated with this graphic
     */
    public void setRefObject(Object obj) {
        this.refObject = obj;
    }

    /**
     * returns a reference object associated with this graphic, if one has been set.
     * @return a reference object associated with this graphic, if one has been set.
     */
    public Object getRefObject() {
        return this.refObject;
    }

    /**
     * @return true if this graphic has been transformed from world to screen coordinates
     */
    public boolean isProjected() {
        return projected;
    }

    /**
     * Sets the projected state of this graphic.  A projected graphic has been transformed from world to
     * screen coordinates in the current view and may also have passed a visibility test in the current view
     * @param isProjected state of the current projection
     */
    public void setProjected(boolean isProjected) {
        this.projected = isProjected;
    }

    /**
     * Returns true if this graphic is marked as visible
     * @return true if visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the graphic to be shown/not shown in the map
     * @param visible true if the graphic is to be visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Set name for identifying the MapGraphic
     *
     * @param n - string name
     */
    public void setName(String n) {
        name = n;
    }

    /**
     * returns the string name of this graphic, if one has been set.
     *
     * @return string name
     */
    public String getName() {
        return name;
    }
    
    /** returns the graphic that has been set as this graphic's parent, or null if none
     *  has been set.  A parent graphic is usually a {@link MapGraphicsList} but can be
     *  any {@link MapGraphic}. 
     * @return parent graphic
     */
    public MapGraphic getParent() {
        return parent;
    }
    
    /**
     * Sets this graphic's parent. A parent graphic is usually a {@link MapGraphicsList} but can be
     * any {@link MapGraphic}. 
     * @param g parent graphic
     */
    public void setParent(MapGraphic g) {
        this.parent = g;
    }

    
    public void paint(Graphics2D g) {
        if (screenShape == null || !isVisible()) {
            return;
        }
        Painter thisPainter = painter;
        Paint thisFill = fill;
        
        if (selected && selectedPainter != null) {
            thisPainter = selectedPainter;
        }
        if (selected && selectedFill != null) {
            thisFill = selectedFill;
        }
        
        if (thisFill != null) {
            g.setPaint(thisFill);
            g.fill(screenShape);
        }
        if (thisPainter != null) {
            thisPainter.paint(g, screenShape);
            
            if (startDecoration != null) {
                startDecoration.drawStartPoint(g, thisPainter, fill, screenShape);
            }
            if (endDecoration != null) {
                endDecoration.drawEndPoint(g, thisPainter, fill, screenShape);
            }
        } 
    }

    

    /**
     * Sets the fill color for filling graphics.  null value means no fill
     * @param fill the fill paint to apply to the graphic
     */
    public void setFill(Paint fill) {
        this.fill = fill;
    }

    /**
     * Sets the fill paint to use when the graphic is marked as selected.
     * @param selectedFill 
     */
    public void setSelectedFill(Paint selectedFill) {
        this.selectedFill = selectedFill;
    }

    /**
     * Returns the fill paint used when the graphic is selected.  May be null.
     * @return 
     */
    public Paint getSelectedFill() {
        return selectedFill;
    }
    
    

    /**
     * Returns the current paint used to fill the graphic, or null if no fill has
     * been specified.
     */
    public Paint getFill() { return this.fill; }

    /**
     * Returns the current painter used to draw this graphic
     */
    public Painter getPainter() { return this.painter; }

    /**
     * @return the painter used when the graphic is selected.  May be null.
     */
    public Painter getSelectedPainter() {
        return selectedPainter;
    }

    /**
     * 
     * @param selectedPainter outline painter used when the graphic is selected.  
     * May be null.
     */
    public void setSelectedPainter(Painter selectedPainter) {
        this.selectedPainter = selectedPainter;
    }
    
    
    

    /**
     * Sets the stroke and paint for drawing graphics.  If a painter exists, then it is used, otherwise
     * the stroke and paint values are used.
     * @param painter the painter to use
     */
    public void setPainter(Painter painter) {
        this.painter = painter;
    }
    
    /**
     * Sets the paint and line width to use for drawing graphics. 
     * @param paint
     */
    public void setPainter(Paint paint, double stroke) {
        setPainter(Painter.createPainter(paint, (float) stroke));
    }
    
    /**
     * Sets the paint and stroke to use for drawing graphics. 
     */
    public void setPainter(Paint paint, Stroke stroke) {
        setPainter(Painter.createPainter(paint, stroke));
    }

    /**
     * Sets the shape to be drawn on the screen.  This shape is transformed to the screen space from the
     * world space in the current projection.
     * @param screenShape the shape to be drawn on the screen (null indicates that the shape should not be drawn)
     */
    protected void setScreenShape(Shape screenShape) {
        this.screenShape = screenShape;
    }

    /** Sets the decoration for the start point of the shape.  (e.g. arrow head) */
    public void setStartDecoration(Decoration startDecoration) {
        this.startDecoration = startDecoration;
    }

    /** Sets the decoration for the end point of the shape.  (e.g. arrow head) */
    public void setEndDecoration(Decoration endDecoration) {
        this.endDecoration = endDecoration;
    }
    
    

    /** Returns the decoration for the start point of the shape */
    public Decoration getStartDecoration() {
        return startDecoration;
    }
    
    /** Returns the decoration for the start point of the shape */
    public Decoration getEndDecoration() {
        return endDecoration;
    }

    /**
     * @return true if the graphic has been marked as selected.
     */
    public boolean isSelected() {
        return selected;
    }
    
    /**
     * sets the selection state for the graphic.
     * @param selected the new selection state
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    /** Tests for intersection between this screen shape and a rectangle.
     *
     * @param otherShape the rectangle to test
     * @return true if the rectangle and this shape intersect.
     */
    public boolean intersects(Rectangle2D otherShape) {
        return screenShape == null ? false : screenShape.intersects(otherShape);
    }


    /** checks for a proximity between the shape edge and a point on the screen.  This
     *  flattens non-linear shapes, so accuracy varies based on the geometry type.
     * @param screenX x-direction window coordinate of the point to check.
     * @param screenY y-direction window coordinate of the point to check.
     * @param maxDist tolerance for hitting an edge (in pixels)
     * @return true if the screen point is within the tolerance distance of an edge of this graphic
     */
    public boolean onEdge(int screenX, int screenY, int maxDist) {
        if (screenShape == null) {
            return false;
        }
        // before iterating, check to see if we are in the ballpark
        //if (!screenShape.contains(screenX, screenY)) {
        //    return false;
        //}
        // flatten the path iterator to make checking for hits easier.
        PathIterator it = screenShape.getPathIterator(null, maxDist);
        double[] pts = new double[6];
        int type;
        double startX = 0, startY = 0;
        double origX = 0, origY = 0;

        while (!it.isDone()) {
            type = it.currentSegment(pts);

            switch (type) {
                case PathIterator.SEG_MOVETO:
                    origX = pts[0];
                    origY = pts[1];
                    break;
                case PathIterator.SEG_LINETO:
                    if (Line2D.ptSegDist(startX, startY, pts[0], pts[1], screenX, screenY) < maxDist) {
                        return true;
                    }
                    break;
                case PathIterator.SEG_CLOSE:
                    if (Line2D.ptSegDist(origX, origY, pts[0], pts[1], screenX, screenY) < maxDist) {
                        return true;
                    }
                    break;
            }
            startX = pts[0];
            startY = pts[1];
            it.next();
        }
        return false;
    }

    /**
     * Returns true if the shape contains the given point in screen coordinates.
     * @param point point given in the projected coordinate system of the map
     * @return true if the projected shape contains the point.
     */
    public boolean contains(Point2D point) {
        if (screenShape != null) {
            return screenShape.contains(point);
        }
        return false;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */