// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map;

import avtas.map.layers.BufferSupport;
import avtas.properties.UserProperty;
import avtas.properties.XmlSerializer;
import avtas.xml.Element;
import java.awt.Graphics2D;
import java.awt.Component;
import java.awt.event.MouseEvent;

/**
 * Base class for all map layers. A map layer is a container for grouping map
 * graphics or other objects to be included in the map.
 *
 * @author AFRL/RQQD
 */
public abstract class MapLayer implements MapMouseListener, MapPopupListener {

    protected MapPanel parentMap = null;
    private boolean visible = true;
    private Element node = null;
    Proj currentProjection = null;
    protected BufferSupport bufferSupport = null;

    public MapLayer() {
    }

    /**
     * Called by the map to paint this layer and its contents.
     *
     * @param g Graphics surface to paint onto.
     */
    public abstract void paint(Graphics2D g);

    /**
     * Called by the map when the view has changed.
     *
     * @param proj the latest map projection (view)
     */
    public abstract void project(Proj proj);

    /**
     * Sets the map that this layer belongs to. This is usually called by the
     * MapPanel when the layer is added.
     *
     * @param parent
     */
    public void setMap(MapPanel parent) {
        this.parentMap = parent;
    }

    /**
     * Returns the map that this layer belongs to, or null if this layer has not
     * been added to a map
     *
     * @return the map that this layer belongs to
     */
    public MapPanel getMap() {
        return parentMap;
    }

    /** Controls buffering of the layer.  If set to true, this layer will paint to 
     *  an intermediate buffer when refreshed or reprojected. The map will paint the
     *  buffered image upon repaint requests.  Buffering increases speed 
     *  for layers that perform numerous drawing operations but are seldom changed.  
     *  However, buffering also increases memory usage.
     * @param buffered 
     */
    public void setBuffered(boolean buffered) {
        if (buffered) {
            if (bufferSupport != null) {
                bufferSupport = new BufferSupport(this);
            }
        } else {
            bufferSupport = null;
        }
    }
    
    /** Returns true if this layer is buffered. */
    public boolean isBuffered() {
        return bufferSupport != null;
    }

    /**
     * Calls the parent map to redraw map contents. Use this to invoke the layer
     * to redraw graphics.
     */
    public void refresh() {
        if (parentMap != null) {
            setProjection(parentMap.getProj());
            parentMap.requestRepaint();
        }
    }

    /**
     * Sets the visibility of this layer
     *
     * @param visible true if the layer is to be visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
        refresh();
    }

    /**
     *
     * @return the visibility of this layer
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Returns the latest projection used on the layer, or null if the
     * projection has not been set.
     */
    public Proj getProjection() {
        return currentProjection;
    }

    /**
     * Used by the Map to set the current projection. This calls
     * {@link #project(avtas.map.Proj)}. This causes the layer to reproject.
     */
    public void setProjection(Proj proj) {
        this.currentProjection = proj;
        if (proj != null) {
            if (bufferSupport != null) {
                bufferSupport.project(proj);
            }
            else {
                project(proj);
            }
        }
    }

    /**
     * Used by the map panel to paint the layer. This should not be called
     * directly. Instead, use {@link #paint(java.awt.Graphics2D) }.
     *
     * @param g
     */
    public final void paintImpl(Graphics2D g) {
        if (bufferSupport != null) {
            bufferSupport.paint(g);
        }
        else {
            paint(g);
        }
    }

    /**
     * Optional layer initialization using XML. By default, this sets any
     * {@link UserProperty} values that exist in the layer. If child classes
     * override this method, then the super method should be called.
     */
    public void setConfiguration(Element node) {
        this.node = node;
        XmlSerializer.deserialize(node, this);
    }

    /**
     * Returns an XML element containing settings for this layer. Can be null.
     * By default, this returns the node used to configure the layer.
     */
    public Element getConfiguration() {
        return node;
    }

    /**
     * Returns a short display name for this layer. Subclasses can override this
     * to return more meaningful descriptions.
     */
    public String getDisplayName() {
        return getClass().getSimpleName();
    }

    /**
     * Returns a component that can configure the layer. Returns an editor that
     * has all of the layer's {@link UserProperty} values. Override this method
     * to return an alternative view.
     */
    public Component getSettingsView() {
        return new LayerPropertiesEditor(this);
    }

    @Override
    public void mouseClicked(MouseEvent e, double lat, double lon) {
    }

    @Override
    public void mouseDragged(MouseEvent e, double lat, double lon) {
    }

    @Override
    public void mouseMoved(MouseEvent e, double lat, double lon) {
    }

    @Override
    public void mousePressed(MouseEvent e, double lat, double lon) {
    }

    @Override
    public void mouseReleased(MouseEvent e, double lat, double lon) {
    }

    @Override
    public void addPopupMenuItems(javax.swing.JPopupMenu menu, MouseEvent e, double lat, double lon) {
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */