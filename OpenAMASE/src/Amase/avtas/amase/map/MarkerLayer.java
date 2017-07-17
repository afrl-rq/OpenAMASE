// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================


package avtas.amase.map;

import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapIcon;
import avtas.map.graphics.MapMarker;
import avtas.map.graphics.MapPoly;
import avtas.map.graphics.MapText;
import avtas.map.layers.GraphicsLayer;
import avtas.util.Colors;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import avtas.xml.XmlReader;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.xml.stream.XMLStreamException;

/**
 * Adds the ability to show objects on the map that are declared in an XML file
 * 
 * Example input file
 * <pre>
 * {@code
 * <MapData>
 *   <Marker>
 *    <path>optional path to marker icon</path>
 *    <size>pixel size</size>
 *    <lat>latitude in degrees</lat>
 *    <lon>longitude in degrees</lon>
 *    <fill>color name or rgb value</fill>
 *    <color>color name or rgb value</color>
 *   <Marker>
 *   
 *   <Path>
 *     <points>
 *        <point lat="deg" lon="deg">
 *     </points>
 *     <closed>true or false</closed>
 *     <fill>color name or rgb value</fill>
 *     <color>color name or rgb value</color>
 *   </Path>
 * 
 * </MapData>
 * }
 * </pre>
 *
 * @author Matt
 */
public class MarkerLayer extends GraphicsLayer {

    JFileChooser chooser = null;

    public MarkerLayer() {
    }

    @Override
    public void setConfiguration(Element node) {
        Element pathEl = node.getChild("Path");
        if (pathEl != null) {
            loadObjects(new File(pathEl.getText()));
        } else {
            loadObjects(node);
        }
    }

    @Override
    public void addPopupMenuItems(JPopupMenu menu, MouseEvent e, double lat, double lon) {
        menu.add(new AbstractAction("Add Map Data...") {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (chooser == null) {
                    chooser = new JFileChooser(".");
                }
                chooser.showOpenDialog(getMap());
                if (chooser.getSelectedFile() != null) {
                    loadObjects(chooser.getSelectedFile());
                }

            }
        });
    }

    void loadObjects(File file) {
        try {
            Element objectEl = XmlReader.readDocument(file);
            loadObjects(objectEl);
        } catch (XMLStreamException ex) {
            Logger.getLogger(MarkerLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void loadObjects(Element xml) {
        for (Element el : xml.getChildElements()) {
            addGraphic(el);
        }
        refresh();
    }

    MapGraphic addGraphic(Element xml) {

        MapGraphic g = null;

        double lat = XMLUtil.getDouble(xml, "lat", 0);
        double lon = XMLUtil.getDouble(xml, "lon", 0);

        switch (xml.getName()) {

            case "Marker":
                int size = XMLUtil.getInt(xml, "size", 5);
                String path = XMLUtil.getValue(xml, "path", "");

                if (!path.isEmpty()) {
                    try {
                        Image image = ImageIO.read(new File(path));
                        MapIcon icon = new MapIcon(image);
                        icon.setLatLon(lat, lon);
                        g = icon;

                        Element textEl = xml.getChild("text");
                        if (textEl != null) {
                            addText(textEl, lat, lon, 0, -5 - icon.getHeight());
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(MarkerLayer.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                    }
                } else {
                    MapMarker marker = new MapMarker(lat, lon, size);
                    g = marker;

                    Element textEl = xml.getChild("text");
                    if (textEl != null) {
                        addText(textEl, lat, lon, 0, -5 - size);
                    }
                }
                break;

            case "Poly":
                MapPoly poly = new MapPoly();
                poly.setPolygon(XMLUtil.getBool(xml, "closed", false));
                for (Element el : xml.getChildren("points/point")) {
                    poly.addPoint(el.getDoubleAttr("lat", 0), el.getDoubleAttr("lon", 0));
                }
                break;
        }

        if (g != null) {
            g.setPainter(Colors.getColor(XMLUtil.getValue(xml, "outline", ""), Color.BLACK), 1);
            g.setFill(Colors.getColor(XMLUtil.getValue(xml, "fill", ""), Color.WHITE));
        }

        add(g);

        return g;
    }

    protected void addText(Element xml, double lat, double lon, int offsetX, int offsetY) {
        String hpos = xml.getAttr("hpos", "center");
        String vpos = xml.getAttr("vpos", "center");
        String font = xml.getAttr("font", "Arial");
        String color = xml.getAttr("color", "white");
        float size = xml.getFloatAttr("size", 12f);

        MapText text = new MapText(xml.getText());
        text.setColor(Colors.getColor(color, Color.WHITE));
        text.setLatLon(lat, lon);
        text.setFont(Font.decode(font));
        text.setFont(text.getFont().deriveFont(size));
        text.setOffset(0, offsetY);

        add(text);

        switch (hpos) {
            case "center":
                text.setHorizontalAlignment(SwingConstants.CENTER);
                break;
            case "left":
                text.setHorizontalAlignment(SwingConstants.LEFT);
                break;
            case "right":
                text.setHorizontalAlignment(SwingConstants.RIGHT);
                break;
            default:
                text.setHorizontalAlignment(SwingConstants.CENTER);
        }

        switch (vpos) {
            case "top":
                text.setVerticalAlignment(SwingConstants.TOP);
                break;
            case "center":
                text.setVerticalAlignment(SwingConstants.CENTER);
                break;
            case "bottom":
                text.setVerticalAlignment(SwingConstants.BOTTOM);
                break;
            default:
                text.setVerticalAlignment(SwingConstants.CENTER);
                break;
        }
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */