// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.map;

import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import avtas.xml.XMLUtil;
import avtas.amase.scenario.ScenarioEvent;
//import afrl.cmasi.Location3D;
import avtas.amase.AmasePlugin;
import avtas.app.SettingsManager;
import avtas.app.StatusPublisher;
import avtas.app.Context;
import avtas.map.GeoPointSelected;
import avtas.map.AnimatedMap;
import avtas.map.MapMouseListener;
import avtas.map.MapPanel;
import avtas.map.MapPopupListener;
import avtas.map.MapLayer;
import avtas.map.util.MapPoint;
import avtas.properties.PropertyEditor;
import avtas.terrain.TerrainService;
import avtas.util.WindowUtils;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import avtas.xml.Element;
import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Creates an application plugin for a 2D map for AMASE simulations.
 *
 * @author AFRL/RQQD
 */
public class MapPlugin extends AmasePlugin implements MapPopupListener, AppEventListener {

    protected DecimalFormat llFormat = new DecimalFormat("#.####");
    protected double clat = 0, clon = 0, scale = 0;
    protected AppEventManager eventManager = null;
    protected MapPanel map;
    protected Context context;

    /**
     * Constructor for the MapPlugin object. Creates a new {@link AnimatedMap}
     * and adds the MapPlugin object to it.
     */
    public MapPlugin() {

        this.eventManager = AppEventManager.getDefaultEventManager();
        setPluginName("Map Display");

        map = new AnimatedMap(40, -95, 120, 640, 480);
        map.addMapPopupListener(this);
        setupMouseEvents();

    }

    /**
     * Returns a menu with the current latitude and longitude information when
     * right-clicked. If "Copy Location" is selected from the menu, put
     * latitude, longitude and altitude into a Location object, convert it XML
     * format and copy to the clipboard.
     *
     * @param e the mouse event
     * @param lat latitude in degrees
     * @param lon longitude in degrees
     */
    @Override
    public void addPopupMenuItems(javax.swing.JPopupMenu menu, MouseEvent e, final double lat, final double lon) {
        JMenuItem locString = new JMenuItem();
        final String s = "Lat: " + llFormat.format(lat) + " Lon: " + llFormat.format(lon)
                + " Alt: " + TerrainService.getElevation((float) lat, (float) lon) + " m";
        locString.setText(s);
        JMenuItem locationCopy = new JMenuItem("Copy Location");
        locationCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WindowUtils.copyToClipboard(s);
                if (eventManager != null) {
                    eventManager.fireEvent(new MapPoint(lat, lon), MapPlugin.this);
                }
            }
        });

        JMenuItem snapshotMenu = new JMenuItem("Copy Map to Clipboard");
        snapshotMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                final BufferedImage img = new BufferedImage(getMap().getWidth(), getMap().getHeight(), BufferedImage.TYPE_INT_ARGB);
                getMap().print(img.createGraphics());
                clipboard.setContents(new Transferable() {
                    @Override
                    public DataFlavor[] getTransferDataFlavors() {
                        return new DataFlavor[]{DataFlavor.imageFlavor};
                    }

                    @Override
                    public boolean isDataFlavorSupported(DataFlavor flavor) {
                        return flavor.isMimeTypeEqual(DataFlavor.imageFlavor);
                    }

                    @Override
                    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                        return img;
                    }
                }, null);
            }
        });
        
        JMenuItem saveImageItem = new JMenuItem(new AbstractAction("Save Map Image") {

            @Override
            public void actionPerformed(ActionEvent e) {
                final BufferedImage img = new BufferedImage(getMap().getWidth(), getMap().getHeight(), BufferedImage.TYPE_INT_ARGB);
                getMap().print(img.createGraphics());
                JFileChooser chooser = WindowUtils.getFilteredChooser("png", new FileNameExtensionFilter("PNG", "png"));
                int ans = chooser.showSaveDialog(map); 
                if (ans == JFileChooser.APPROVE_OPTION) {
                    try {
                        ImageIO.write(img, "png", chooser.getSelectedFile());
                    } catch (IOException ex) {
                        Logger.getLogger(MapPlugin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        JMenuItem zoomItem = new JMenuItem(new AbstractAction("Zoom to Scenario Center") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (scale != 0) {
                    map.setCenter(MapPlugin.this.clat, MapPlugin.this.clon);
                    map.getProj().setLonWidth(scale);
                    map.project();
                }
            }
        });
        
        JMenuItem goToItem = new JMenuItem(new AbstractAction("Go To Location") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFormattedTextField latField = new JFormattedTextField(new DecimalFormat("#.#######"));
                JFormattedTextField lonField = new JFormattedTextField(new DecimalFormat("#.#######"));
                JPanel dialogPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                dialogPanel.add(new JLabel("Lat"));
                dialogPanel.add(latField);
                dialogPanel.add(new JLabel("Lon"));
                dialogPanel.add(lonField);
                
                latField.setToolTipText("Latitude in degrees");
                latField.setColumns(10);
                lonField.setToolTipText("Longitude in degrees");
                lonField.setColumns(10);
                
                int ans = JOptionPane.showConfirmDialog(map, dialogPanel, "Go to Location", JOptionPane.OK_CANCEL_OPTION);
                if (ans == JOptionPane.OK_OPTION) {
                    map.setCenter( ((Number) latField.getValue()).doubleValue(), ((Number) lonField.getValue()).doubleValue() );
                }
            }
        });

        menu.add(locString);
        menu.add(new JSeparator());
        menu.add(snapshotMenu);
        menu.add(saveImageItem);
        menu.add(locationCopy);
        menu.add(zoomItem);
        menu.add(goToItem);
        menu.add(makeToggleLayerMenu());
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof ScenarioEvent) {
            initScenario((ScenarioEvent) event);
            return;
        }
        super.eventOccurred(event);
    }

    public void initScenario(ScenarioEvent evt) {
        // if the AppEventManager loads a new file, then clear this layer.
        Element node = XMLUtil.getChild(evt.getXML(), "ScenarioData");
        // setup the lat and long origin and zoom level
        if (node != null) {
            node = XMLUtil.getChild(node, "SimulationView");
            if (node != null) {
                double lat = XMLUtil.getDoubleAttr(node, "Latitude", 0);
                double lon = XMLUtil.getDoubleAttr(node, "Longitude", 0);
                double scale = XMLUtil.getDoubleAttr(node, "LongExtent", 180);

                this.clat = lat;
                this.clon = lon;
                this.scale = scale;
                map.setCenter(lat, lon);
                map.getProj().setLonWidth(scale);
                map.project();

            }
        }
    }

    public MapPanel getMap() {
        return map;
    }

    @Override
    public Component getGui() {
        return map;
    }

    @Override
    public JPanel getSettingsPanel() {
        return PropertyEditor.getEditor(map.getProperties());
    }

    @Override
    public void getMenus(JMenuBar menubar) {
        JMenu menu = WindowUtils.getMenu(menubar, "Map");
        menu.add(makeToggleLayerMenu());
        menu.add(addLayerMenu());
    }

    public JMenu makeToggleLayerMenu() {
        final JMenu toggleLayers = new JMenu("Toggle Layers");
        toggleLayers.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                for (final MapLayer layer : map.getLayers()) {
                    final JCheckBoxMenuItem layerItem = new JCheckBoxMenuItem(layer.getDisplayName());
                    layerItem.setSelected(layer.isVisible());
                    layerItem.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            layer.setVisible(layerItem.isSelected());
                        }
                    });
                    toggleLayers.add(layerItem);
                }
            }

            public void menuDeselected(MenuEvent e) {
                toggleLayers.removeAll();
            }

            public void menuCanceled(MenuEvent e) {
                toggleLayers.removeAll();
            }
        });
        return toggleLayers;
    }

    protected JMenuItem addLayerMenu() {
        return new JMenuItem(new AbstractAction("Add Map Layer") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ans = JOptionPane.showInputDialog(map, "<html>Add a Map Layer.  Use the full java name for the layer<br/>"
                        + "<em>Example: com.example.ExampleMapLayer</em></html>");
                if (ans != null) {
                    try {
                        Class c = Class.forName(ans);
                        if (MapLayer.class.isAssignableFrom(c)) {
                            MapLayer layer = (MapLayer) c.newInstance();
                            map.add(layer);
                            if (context != null) {
                                context.addObject(layer);
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(map, "Could not create Map Layer: " + ans);
                    }

                }
            }
        });
    }

    public void setupMouseEvents() {
        map.addMapMouseListener(new MapMouseListener() {
            public void mouseMoved(MouseEvent e, double lat, double lon) {
                StatusPublisher.getDefault().setStatus("Lat: " + llFormat.format(lat) + " Lon: " + llFormat.format(lon));
            }

            public void mouseClicked(MouseEvent e, double lat, double lon) {
                if (eventManager != null) {
                    eventManager.fireEvent(new GeoPointSelected(lat, lon), this);
                }
            }

            public void mouseDragged(MouseEvent e, double lat, double lon) {
            }

            public void mousePressed(MouseEvent e, double lat, double lon) {
            }

            public void mouseReleased(MouseEvent e, double lat, double lon) {
            }
        });
    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {

        this.context = context;
        Element configNode = xml.getChild("Configuration");
        if (configNode != null) {
            map.initialize(SettingsManager.getAsXml(configNode.getText()));
        } else {
            map.initialize(XMLUtil.getChild(xml, "Map"));
        }

        for (MapLayer l : map.getLayers()) {
            context.addObject(l);
        }

        map.project();
    }

    @Override
    public void applicationPeerAdded(Object peer) {
        //map.add(peer);
    }

    @Override
    public void initializeComplete() {
        map.project();
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */