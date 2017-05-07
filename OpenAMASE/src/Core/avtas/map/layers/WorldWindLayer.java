// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.layers;

import avtas.map.Proj;
import avtas.map.image.MapScaledImage;
import java.io.File;
import java.io.FileFilter;
import javax.swing.JOptionPane;
import avtas.map.image.DDSImageReaderSpi;
import avtas.properties.UserProperty;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.awt.Component;
import javax.imageio.spi.IIORegistry;

;

/**
 * Reads World Wind (Java) style caches and shows imagery.
 * @author AFRL/RQQD
 */
public class WorldWindLayer extends GraphicsLayer<MapScaledImage> {

    @UserProperty(Description = "Directory containing images", DisplayName = "Cache Directory",
            FileType = UserProperty.FileTypes.Directories)
    private File CacheDirectory = null;
    
    private int maxLevel = 0;
    private double levelZeroLatSize = 2.25;
    private double levelZeroLonSize = 2.25;
    private int imageSizeX = 512;
    private int imageSizeY = 512;
    private String formatSuffix = ".dds";
    private double originLat = -90;
    private double originLon = -180;

    public WorldWindLayer() {
        // make sure the DDS reader is loaded into the ImageIO registry
        IIORegistry.getDefaultInstance().registerServiceProvider(new DDSImageReaderSpi());
    }

    public WorldWindLayer(String path) {
        this();
        setCacheDirectory(new File(path));
    }

    @Override
    public void setConfiguration(Element node) {
        setCacheDirectory(new File(XMLUtil.getValue(node, "CacheDirectory", "")));
    }


    /** Searches the worldwind cache directory for an XML layer specification file.  If none is found, then the
     *  layer may not load correctly.
     *
     * @param cacheDir
     */
    public void setCacheDirectory(File cacheDir) {
        try {
            this.CacheDirectory = cacheDir;
            if (cacheDir == null || !cacheDir.exists()) {
                return;
            }
            
            File[] xmlFiles = cacheDir.listFiles(new FileFilter() {

                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".xml");
                }
            });
            if (xmlFiles.length != 1) {
                System.err.println("WorldWindLayer: Directory is not a properly formatted WorldWind cache.");
                System.err.println(cacheDir.getPath());
            }
            File xmlFile = xmlFiles[0];
            Element el = Element.read(xmlFile);
            double dlat = XMLUtil.getDoubleAttr(el, "LevelZeroTileDelta/LatLon/latitude", 36);
            double dlon = XMLUtil.getDoubleAttr(el, "LevelZeroTileDelta/LatLon/longitude", 36);
            int numEmpty = XMLUtil.getIntAttr(el, "NumLevels/numEmpty", 0);
            // sets the image file size
            imageSizeX = XMLUtil.getIntAttr(el, "TileSize/Dimension/width", 512);
            imageSizeY = XMLUtil.getIntAttr(el, "TileSize/Dimension/height", 512);
            //set the origin of the tiles
            originLat = XMLUtil.getDoubleAttr(el, "TileOrigin/LatLon/latitude", -90);
            originLon = XMLUtil.getDoubleAttr(el, "TileOrigin/LatLon/longitude", -180);
            maxLevel = XMLUtil.getIntAttr(el, "NumLevels/count", 0);
            // get the file type.  For WWJ caches this is usually .dds, but other formats are possible
            formatSuffix = XMLUtil.getValue(el, "FormatSuffix", formatSuffix);
            // the first level (zero) texture size is determined by the LevelZeroTileDelta and the number of
            // "empty" levels. For instance, a LevelZeroTileDelta of 36 deg, and numEmpty value of 4 will
            // yield a 0-level delta of 2.25 deg.
            levelZeroLatSize = dlat * Math.pow(0.5, numEmpty);
            levelZeroLonSize = dlon * Math.pow(0.5, numEmpty);
        } catch (Exception ex) {
            String errorText = "<html><p>XML Error. Cannot Configure WorldWindLayer</p><p>"
                    + ex.getMessage() + "</p></html>";
            JOptionPane.showMessageDialog(null, errorText);
        }

    }

    public File getCacheDirectory() {
        return CacheDirectory;
    }
    
    

    @Override
    public void project(Proj proj) {


        clear();
        //MapGraphicsList<MapScaledImage> newList = new MapGraphicsList<MapScaledImage>();

        double compLevel = Math.log(imageSizeX / proj.getPixPerLon() / levelZeroLonSize) / Math.log(0.5);
        //System.out.println("comp level: " + compLevel);
        if (compLevel < 0) {
            compLevel = 0;
        }

        int topLevel = compLevel < maxLevel ? (int) compLevel : maxLevel;

        int imgCount = 0;


        // this process starts at the highest level (highest number) and works its way toward zero.
        // WorldWind caches are arranged as level/latnum/latnum_lonnum.dds.  this searches for tiles
        // that will fill the current view.  If a level completely covers the view area, then the
        // process stops.  Otherwise, the levels are traversed until the view is filled or level
        // zero is reached. Tiles are filled in vertical (lat) lines starting at the southwest corner.

        for (int level = topLevel; level >= 0; level--) {

            boolean missingTiles = false;

            File levelDir = new File(CacheDirectory, String.valueOf(level));
            if (!levelDir.exists()) {
                missingTiles = true;
                continue;
            }

            //System.out.println("processing level " + level);

            double dLon = levelZeroLonSize * Math.pow(0.5, level);
            double dLat = levelZeroLatSize * Math.pow(0.5, level);

            int lonNum = (int) ((proj.getWestLon() - originLon) / dLon);
            int latNum = (int) ((proj.getSouthLat() - originLat) / dLat);

            double startLon = originLon + lonNum * dLon;
            double startLat = originLat + latNum * dLat;
            File tmp = null;

            double lon = startLon;
            double lat = startLat;
            int latInc = 0;
            int lonInc = 0;

            while (lat < proj.getNorthLat()) {
                lon = startLon;
                lonInc = 0;
                File latDir = new File(levelDir, String.valueOf(latNum + latInc));

                if (latDir.exists()) {
                    while (lon < proj.getEastLon()) {
                        tmp = new File(latDir, (latNum + latInc) + "_" + (lonNum + lonInc) + formatSuffix);
                        //System.out.println("looking for " + tmp.getPath());
                        if (tmp.exists()) {
                            imgCount++;
                            MapScaledImage mapImg = new MapScaledImage(tmp, lat + dLat, lon, lat, lon + dLon);
                            add(0, mapImg);
                        }
                        else {
                            missingTiles = true;
                        }
                        lon += dLon;
                        lonInc++;
                    }
                }
                else {
                    missingTiles = true;
                }

                lat += dLat;
                latInc++;
            }

            // if the view has no missing tiles, then don't waste time on lower levels
            if (!missingTiles) {
                //System.out.println("Bottom Level: " + level);
                break;
            }

        }


        // once all the scaled rasters are added to the render list, tell the parent layer to project them.
        super.project(proj);

    }

    @Override
    public String getDisplayName() {
        return "WorldWind Layer (" + (CacheDirectory.exists() ? CacheDirectory.getName() : "") + ")";
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */