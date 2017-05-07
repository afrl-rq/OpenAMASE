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
import avtas.map.Proj;
import avtas.map.graphics.MapGraphic;
import java.awt.Graphics2D;
import avtas.map.graphics.MapGraphicsList;
import avtas.map.graphics.Painter;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.Iterator;

/**
 * A layer used to contain MapGraphics.
 *
 * @author AFRL/RQQD
 * @param <T> The type of MapGraphic this layer contains.
 */
public class GraphicsLayer<T extends MapGraphic> extends MapLayer implements Iterable<T> {

    private final MapGraphicsList<T> graphicList = new MapGraphicsList<T>();
    protected BufferedImage backImage = null;

    /**
     * Creates a new instance of GraphicsLayer
     */
    public GraphicsLayer() {
    }

    @Override
    public void paint(Graphics2D g) {
        if (!isVisible()) {
            return;
        }
        graphicList.paint(g);
    }

    /**
     *
     * @return the graphic list for this layer
     */
    public MapGraphicsList<T> getList() {
        return graphicList;
    }

    /**
     * {@inheritDoc }
     *
     * @param proj
     */
    @Override
    public void project(Proj proj) {
        if (proj != null && isVisible()) {
            graphicList.project(proj);
        }
    }

    /**
     * Projects individual graphics using the last projection used for this
     * layer. If the projection is null, the graphics are not projected.
     *
     * @param graphics list of graphics to project
     */
    public void project(MapGraphic... graphics) {
        if (getProjection() != null) {
            for (MapGraphic graphic : graphics) {
                graphic.project(getProjection());
            }
        }
    }

    /**
     * Adds the graphic to this layer, if it is not already on this layer.
     *
     * @param graphic
     */
    public void add(T graphic) {
        if (!graphicList.contains(graphic)) {
            graphicList.add(graphic);
        }
    }

    /**
     * Adds a graphic at the given index in the list.
     */
    public void add(int index, T graphic) {
        graphicList.add(index, graphic);
    }

    /**
     * Removes the graphic if it is contained in this layer.
     *
     * @param graphic
     */
    public void remove(MapGraphic graphic) {
        graphicList.remove(graphic);
    }

    /**
     * Removes the graphic at the given index
     */
    public T remove(int index) {
        return graphicList.remove(index);
    }

    /**
     * Returns the graphic at the requested index in the list.
     *
     * @param index
     */
    public T get(int index) {
        return graphicList.get(index);
    }

    /**
     * Clears all of the graphics from the layer.
     */
    public void clear() {
        graphicList.clear();
    }

    /**
     * Returns the number of graphics in this layer.
     */
    public int size() {
        return graphicList.size();
    }

    /**
     * Sets the color and stroke width for all of the graphics in this layer.
     */
    public void setPaint(Color paint, double width) {
        graphicList.setPainter(paint, width);
    }

    /**
     * Sets the fill for all of the graphics in this layer.
     */
    public void setFill(Paint fill) {
        graphicList.setFill(fill);
    }

    /**
     * Sets the stroke and paint for drawing graphics for all of the graphics in
     * this layer.
     */
    public void setPainter(Painter painter) {
        graphicList.setPainter(painter);
    }

    public int indexOf(T graphic) {
        return graphicList.indexOf(graphic);
    }

    public T getByRefObject(Object refObj) {
        return graphicList.getByRefObject(refObj);
    }

    @Override
    public Iterator<T> iterator() {
        return graphicList.iterator();
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */