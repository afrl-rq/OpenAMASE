// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map;

import avtas.app.UserExceptions;
import avtas.map.util.WorldMath;

import avtas.properties.XmlSerializer;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JComponent;

/**
 * Basic two dimensional map class.
 *
 * @author AFRL/RQQD
 */
public class MapPanel extends JComponent implements MouseMotionListener, MouseWheelListener, MouseListener {

    protected final List<MapLayer> layers = new ArrayList<>();
    protected final List<MapMouseListener> mouseListeners = new ArrayList<>();
    // a list of listeners for this projection
    protected final List<ProjListener> projListeners = new ArrayList<>();
    protected MapPopupMenu popupMenu = new MapPopupMenu();
    public Proj proj = null;
    protected MouseEvent lastEvent = null;
    protected Element configurationNode = null;
    protected boolean projecting = false;

    public static enum RenderType {

        /**
         * Paints the map in a way that allows continuous scrolling around the
         * globe
         */
        WrapWorld,
        /**
         * Faster painting method that does not allow continuous global
         * scrolling
         */
        NearestHemisphere
    }
    MapProperties properties = new MapProperties(this);

    /**
     * Creates a new instance of
     * <code>MapPanel</code> with the center set to (0,0) and the number of x
     * degrees, width and height all set to one.
     */
    public MapPanel() {
        this(0, 0, 1, 1, 1);
        setLayout(null);
        setIgnoreRepaint(true);
    }

    /**
     * Creates a new instance of
     * <code>MapPanel</code>.
     *
     * @param center_lat The latitude of the center point.
     * @param center_lon The longitude of the center point.
     * @param num_deg_x The number of degrees in the x direction (map scale).
     * @param width The width of the map.
     * @param height The height of the map.
     */
    public MapPanel(double center_lat, double center_lon, double num_deg_x,
            int width, int height) {

        proj = new EqualRect(center_lat, center_lon, width, height, num_deg_x * 2);

        setPreferredSize(new Dimension(width, height));
        setCenter(center_lat, center_lon);

        properties.setPreferredWidth(width);
        properties.setPreferredHeight(height);
        properties.setCenterLat(center_lat);
        properties.setCenterLon(center_lon);
        properties.setLonWidth(num_deg_x);

        setLayout(null);

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        setDoubleBuffered(true);
        setIgnoreRepaint(true);

        setFocusable(true);
    }

    public MapProperties getProperties() {
        return properties;
    }

    /**
     * Initializes the map from an XML node. Sets the size, center point and
     * scale of the map. Also creates all map layers and adds them to a list of
     * layers.
     *
     * @param node The XML node with map data.
     */
    public void initialize(Element node) {

        this.configurationNode = node;

        if (node != null)
            properties.fromXml(node);

        getProj().setSize(properties.getPreferredWidth(), properties.getPreferredHeight());
        getProj().setCenter(properties.getCenterLat(), properties.getCenterLon());
        getProj().setLonWidth(properties.getLonWidth());
        setBackground(properties.getBackgroundColor());

        if (configurationNode != null) {
            Element layerNode = configurationNode.getChild("Layers");

            if (layerNode != null) {
                List<Element> childNodes = layerNode.getChildElements();
                for (int i = 0; i < childNodes.size(); i++) {
                    Element mapNode = childNodes.get(i);
                    MapLayer l = makeLayer(mapNode);
                    if (l != null) {

                        l.setBuffered(mapNode.getBoolAttr("Buffered", false));
                        add(l);

                        l.setConfiguration(mapNode);
                        l.setVisible(XMLUtil.getBoolAttr(mapNode, "Visible", true));
                    }
                }
            }
        }

        //setBackground(Colors.getColor(XMLUtil.getValue(node, "BackgroundColor", "BLACK"), Color.BLACK));
        project();
    }

    /**
     * Creates a
     * <code>GraphicsLayer</code> object based on the class name in the XML file
     * and calls its
     * <code>initialize</code> method.
     *
     * @param node
     * @return a MapLayer created from the XML node
     */
    public static MapLayer makeLayer(Element node) {

        try {
            String className = XMLUtil.getAttr(node, "Class", "");
            if (!className.isEmpty()) {
                Class cl = Class.forName(className);
                Object o = cl.newInstance();

                if (o instanceof MapLayer) {
                    return (MapLayer) o;
                }
            }
            return null;
        } catch (Exception ex) {
            UserExceptions.showError(MapPanel.class, "Error creating Map Layer", ex);
            return null;
        }
    }

    /**
     * Returns the XML node used to configure the map
     */
    public Element getConfiguration() {

        // set the top-level data
        configurationNode = XmlSerializer.serialize(properties);
        configurationNode.setName("Map");

        Element layersEl = configurationNode.addElement("Layers");
        for (MapLayer layer : layers) {
            Element layerEl = layer.getConfiguration();
            if (layerEl == null) {
                layerEl = new Element("");
            }
            layerEl.setName("Layer");
            layerEl.setAttribute("Class", layer.getClass());
            layerEl.setAttribute("Visible", String.valueOf(layer.isVisible()));
            layersEl.add(layerEl);
        }

        return configurationNode;
    }

    /**
     * Sets the location of the specified layer in the list of map layers. the
     * position value must be in the range [0..layers.size()-1].
     *
     * @param layer layer to be moved
     * @param pos new position to which the layer is moved
     */
    public void setLayerPosition(MapLayer layer, int pos) {
        if (pos >= 0 && pos < layers.size()) {
            int index = layers.indexOf(layer);
            if (index >= 0) {
                Collections.swap(layers, pos, index);
                repaint();
            }
        }
    }

    /**
     * Sets the way the map is painted. For continuous scrolling across the
     * globe, use {@link RenderType#WrapWorld}. For faster repaints, use
     * {@link RenderType#NearestHemisphere} which only paints the current
     * hemisphere of the map.
     *
     * @param renderType
     */
    public void setRenderType(RenderType renderType) {
        properties.setRenderType(renderType);
        repaint();
    }

    public RenderType getRenderType() {
        return properties.getRenderType();
    }

    /**
     * Gets the map projection.
     *
     * @return The map projection.
     */
    public Proj getProj() {
        return proj;
    }

    /**
     * Adds a
     * <code>MapMouseListener</code> to the list of listeners.
     *
     * @param l The <code>MapMouseListener</code>.
     */
    public void addMapMouseListener(MapMouseListener l) {
        if (!mouseListeners.contains(l))
            mouseListeners.add(l);
    }

    public void removeMapMouseListener(MapMouseListener l) {
        mouseListeners.remove(l);
    }

    public void addMapPopupListener(MapPopupListener l) {
        popupMenu.addListener(l);
    }

    public void removeMapPopupListener(MapPopupListener l) {
        popupMenu.removeListener(l);
    }

    /**
     * Adds the component to the appropriate list and then to the overall
     * <code>Container</code>. Constraints are defined by the layout manager.
     *
     * @param comp The <code>Component</code> to be added.
     * @param constraints The constraints for the <code>Component</code>.
     * @param index The position in the <code>Container's</code> list at which
     * to insert the <code>Component</code>.
     */
    protected void addImpl(Component comp, Object constraints, int index) {
        if (comp instanceof MapMouseListener) {
            addMapMouseListener((MapMouseListener) comp);
        }
        if (comp instanceof MapPopupListener) {
            popupMenu.addListener((MapPopupListener) comp);
        }
        if (comp instanceof ProjListener) {
            addProjListener((ProjListener) comp);
        }
        super.addImpl(comp, constraints, index);
    }

    /**
     * Adds a map layer to the map.
     */
    public void add(MapLayer layer) {
        if (layers.add(layer)) {
            layer.setMap(this);

            if (layer instanceof MapMouseListener) {
                addMapMouseListener((MapMouseListener) layer);
            }
            if (layer instanceof MapPopupListener) {
                popupMenu.addListener((MapPopupListener) layer);
            }
            project();
        }
    }

    public void remove(MapLayer layer) {
        if (layers.remove(layer)) {
            layer.setMap(null);

            if (layer instanceof MapMouseListener) {
                removeMapMouseListener((MapMouseListener) layer);
            }
            if (layer instanceof MapPopupListener) {
                removeMapPopupListener((MapPopupListener) layer);
            }

            repaint();
        }

    }

    /**
     * adds a ProjListener to the listener list
     */
    public void addProjListener(ProjListener l) {
        if (!projListeners.contains(l)) {
            projListeners.add(l);
        }
    }

    /**
     * removes a ProjListener from the listener list
     */
    public void removeProjListener(ProjListener l) {
        projListeners.remove(l);
    }

    @Override
    protected void paintComponent(Graphics g) {


        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        //g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (proj.getRotation() != 0)
            g2.rotate(-proj.getRotation(), 0.5 * getWidth(), 0.5 * getHeight());

        for (int i = 0; i < layers.size(); i++) {


            MapLayer l = layers.get(i);
            if (!l.isVisible()) {
                continue;
            }

            Graphics2D layerGraphics = (Graphics2D) g2.create(); 
            l.paintImpl(layerGraphics);


            if (properties.getRenderType() == RenderType.WrapWorld) {

                int dateline = WorldMath.getDateline(proj);
                int worldwidth = (int) (proj.getPixPerLon() * 360);

                if (dateline >= proj.getWidth() / 2) {
                    layerGraphics.translate(worldwidth, 0);
                    l.paintImpl(layerGraphics);
                    layerGraphics.translate(-worldwidth, 0);
                }
                else {
                    layerGraphics.translate(-worldwidth, 0);
                    l.paintImpl(layerGraphics);
                    layerGraphics.translate(worldwidth, 0);
                }
            }

            layerGraphics.dispose();
        }
        g2.dispose();
    }

    /**
     * called by MapLayer as part of the refresh mechanism.
     *
     * @see AnimatedMap
     */
    public void requestRepaint() {
        repaint();
    }

    /**
     * Zooms the map in by adjusting the longitudinal width. If the zoom factor
     * is zero, no change is made. This function zooms the view while
     * maintaining the current center point of the view.
     *
     * @param factor The zoom factor.
     */
    public void zoom(double factor) {
        if (factor == 0) {
            return;
        }
        proj.setLonWidth(proj.getWidth() / proj.getPixPerLon() / factor);
        project();
    }

    /**
     * Zooms the map in by adjusting the longitudinal width. If the zoom factor
     * is zero, no change is made. If zooming in, this function zooms the view
     * while re-centering on the location of the mouse.
     *
     * @param factor The zoom factor.
     */
    public void zoom(double factor, MouseEvent mouseEvent) {
        if (factor == 0) {
            return;
        }

        if (factor > 0) {
            double dx = (0.5 * proj.getWidth() - mouseEvent.getX()) * (factor - 1);
            double dy = (0.5 * proj.getHeight() - mouseEvent.getY()) * (factor - 1);

            double rot = proj.getRotation();
            double dlon = (dx * Math.cos(rot) - dy * Math.sin(rot)) / proj.getPixPerLon();
            double dlat = (dy * Math.cos(rot) + dx * Math.sin(rot)) / proj.getPixPerLat();

            proj.setCenter(proj.getCenterLat() + dlat, proj.getCenterLon() - dlon);
        }

        proj.setLonWidth(proj.getWidth() / proj.getPixPerLon() / factor);
        project();
    }

    /**
     * Moves the center of the map projection by dx and dy in the x and y
     * directions.
     *
     * @param dx The change in the x direction.
     * @param dy The change in the y direction.
     */
    public void pan(int dx, int dy) {

        double rot = proj.getRotation();

        double dlon = (dx * Math.cos(rot) - dy * Math.sin(rot)) / proj.getPixPerLon();
        double dlat = (dy * Math.cos(rot) + dx * Math.sin(rot)) / proj.getPixPerLat();

        proj.setCenter(proj.getCenterLat() + dlat, proj.getCenterLon() - dlon);
        project();
    }

    /**
     * Sets the center of the map projection.
     *
     * @param lat The latitude of the new center.
     * @param lon The longitude of the new center.
     */
    public void setCenter(double lat, double lon) {
        proj.setCenter(lat, lon);
        project();
    }

    /**
     * Sets the bounds of the map projection and the
     * <code>JComponent</code>.
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        proj.setSize(width, height);
        project();
        super.setBounds(x, y, width, height);
    }

    /**
     * Sets the map projection.
     *
     * @param proj The map projection.
     */
    public void setProjection(Proj proj) {
        this.proj = proj;

        project();
    }

    /**
     * Projects the map by projecting each
     * <code>GraphicsLayer</code> and repainting the
     * <code>JComponent</code>.
     */
    public void project() {
        projecting = true;
        synchronized (layers) {
            for (MapLayer l : layers) {
                l.setProjection(proj);
            }
        }
        for (ProjListener p : projListeners) {
            p.project(proj);
        }

        projecting = false;
        repaint();

    }

    /**
     * Sets the rotation angle and projects the map.
     *
     * @param deg_rot The desired rotation angle.
     */
    public void setRotation(double deg_rot) {
        proj.setRotation(Math.toRadians(deg_rot));
        project();
    }

    /**
     * Gets the current rotation angle.
     *
     * @return The current rotation angle.
     */
    public double getRotation() {
        return proj.getRotation();
    }

    /**
     * Gets the
     * <code>Array</code> of
     * <code>MapLayers</code> for this map.
     *
     * @return The array of layers.
     */
    public MapLayer[] getLayers() {
        return layers.toArray(new MapLayer[]{});
    }

    /**
     * Gets the lat, lon position on the map for the location of the mouse and
     * passes the
     * <code>MouseEvent</code> and the lat, lon position to all registered
     * <code>MapMouseListeners</code>.
     *
     * @param e The <code>MouseEvent</code>
     */
    public void mouseMoved(MouseEvent e) {
        lastEvent = null;

        double lon = proj.getLon(e.getX(), e.getY());
        double lat = proj.getLat(e.getX(), e.getY());

        if (proj != null) {
            for (MapMouseListener l : mouseListeners) {
                l.mouseMoved(e, lat, lon);
            }

        }
    }

    /**
     * Called whenever the user clicks and drags the mouse over the map.
     * Computes the change in the x and y position (dx, dy) over the dragging
     * motion. If buttons 1 and 3 are pressed, the map zooms in. If only button
     * 3 is pressed, the map pans over dx and dy. Otherwise, the
     * <code>mouseDragged</code> method is called for each registered mouse
     * listener.
     *
     * @param e The <code>MouseEvent</code>.
     */
    public void mouseDragged(MouseEvent e) {
        if (lastEvent == null) {
            lastEvent = e;
            return;
        }

        int dx = e.getX() - lastEvent.getX();
        int dy = e.getY() - lastEvent.getY();

        int bothDown = (MouseEvent.BUTTON1_MASK | MouseEvent.BUTTON3_MASK);

        if ((e.getModifiers() & bothDown) == bothDown) {
            zoom(Math.pow(1.05, -dy), e);
        }
        else if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) == MouseEvent.BUTTON3_MASK) {
            pan(dx, dy);
        }
        else if (proj != null) {
            double lon = proj.getLon(e.getX(), e.getY());
            double lat = proj.getLat(e.getX(), e.getY());
            for (MapMouseListener l : mouseListeners) {
                l.mouseDragged(e, lat, lon);
            }
        }

        lastEvent = e;

    }

    /**
     * Called whenever a mouse button is released. If button 1 is released,
     * computes the lat, lon position of the mouse and calls the
     * <code>mouseReleased</code> method for each mouseListener.
     *
     * @param e The <code>MouseEvent</code>.
     */
    public void mouseReleased(MouseEvent e) {
        if (proj == null) {
            return;
        }
        if (e.isPopupTrigger() && lastEvent == null) {
            double lon = proj.getLon(e.getX(), e.getY());
            double lat = proj.getLat(e.getX(), e.getY());
            popupMenu.showMenu(e, lat, lon);
        }
        else if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) {
            double lon = proj.getLon(e.getX(), e.getY());
            double lat = proj.getLat(e.getX(), e.getY());
            for (MapMouseListener l : mouseListeners) {
                l.mouseReleased(e, lat, lon);
            }
        }
    }

    /**
     * Called whenever a mouse button is pressed. If button 1 is pressed,
     * computes the lat, lon position of the mouse and calls
     * <code>mousePressed</code> for each registered mouseListener.
     *
     * @param e The <code>MouseEvent</code>.
     */
    public void mousePressed(MouseEvent e) {
        if (proj == null) {
            return;
        }
        if (e.isPopupTrigger()) {
            double lon = proj.getLon(e.getX(), e.getY());
            double lat = proj.getLat(e.getX(), e.getY());
            popupMenu.showMenu(e, lat, lon);
        }
        else if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) {
            double lon = proj.getLon(e.getX(), e.getY());
            double lat = proj.getLat(e.getX(), e.getY());
            for (MapMouseListener l : mouseListeners) {
                l.mousePressed(e, lat, lon);
            }
        }
    }

    /**
     * Called when a mouse exits a component (currently does nothing).
     *
     * @param e The <code>MouseEvent</code>.
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Called when a mouse enters a component (currently does nothing).
     *
     * @param e The <code>MouseEvent</code>.
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Called when a mouse button is clicked (pressed and released). If button 2
     * is clicked, pan over half of the width and height. If button 3 is
     * pressed, show a popup menu at that location. Otherwise, call
     * <code>mouseClicked</code> for each registered mouseListener.
     *
     * @param e The <code>MouseEvent</code>.
     */
    public void mouseClicked(MouseEvent e) {

        if (e.isPopupTrigger() && proj != null) {
            double lon = proj.getLon(e.getX(), e.getY());
            double lat = proj.getLat(e.getX(), e.getY());
            popupMenu.showMenu(e, lat, lon);
        }
        else if ((e.getModifiers() & MouseEvent.BUTTON2_MASK) == MouseEvent.BUTTON2_MASK && proj != null) {
            pan(-e.getX() + getWidth() / 2, -e.getY() + getHeight() / 2);

        }
        else {
            if (proj != null) {
                double lon = proj.getLon(e.getX(), e.getY());
                double lat = proj.getLat(e.getX(), e.getY());
                for (MapMouseListener l : mouseListeners) {
                    l.mouseClicked(e, lat, lon);
                }
            }
        }
    }

    /**
     * Called whenever the mouse wheel is moved. Zooms in on the map based on
     * the rotation of the wheel.
     *
     * @param e The <code>MouseEvent</code>.
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoom(Math.pow(1.1, -e.getWheelRotation()), e);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */