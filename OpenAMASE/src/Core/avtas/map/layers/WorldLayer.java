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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import avtas.map.Proj;
import avtas.properties.UserProperty;
import avtas.properties.XmlSerializer;
import avtas.xml.Element;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AFRL/RQQD
 */
public class WorldLayer extends MapLayer {

    int imgWidth = 0;
    int imgHeight = 0;
    BufferedImage worldImage = null;
    //String imagePath = null;
    int dx = 0;
    int dy = 0;
    int np = 0;
    int dl = 0;
    boolean projected = false;
    @UserProperty(Description = "The worldwide image to display. ")
    File image = null;

    public WorldLayer() {
    }

    /**
     * Creates a new instance of WorldLayer
     */
    public WorldLayer(String filename) {
        try {
            image = new File(filename);
            worldImage = ImageIO.read(image);
            imgWidth = worldImage.getWidth(null);
            imgHeight = worldImage.getHeight(null);

        } catch (Exception e) {
        }
    }

    @Override
    public void setConfiguration(Element node) {
        XmlSerializer.deserialize(node, this);
        setImage(image);
    }

    public void setImage(File image) {
        if (image != null) {
            try {
                this.image = image;
                worldImage = ImageIO.read(image);
                imgWidth = worldImage.getWidth(null);
                imgHeight = worldImage.getHeight(null);
                refresh();
            } catch (IOException ex) {
                Logger.getLogger(WorldLayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public File getImage() {
        return image;
    }

    public void project(Proj proj) {

        if (proj == null) {
            projected = false;
            return;
        }

        np = (int) proj.getY(90);
        dl = (int) proj.getX(-180);

        // if we are zoomed in to an extreme level, don't paint the layer
        if (proj.getPixPerLat() > 1E6 || proj.getPixPerLat() > 1E6) {
            projected = false;
            return;
        }

        dy = (int) (proj.getPixPerLat() * 180);
        dx = (int) (proj.getPixPerLon() * 360);
        projected = true;

    }

    @Override
    public void paint(Graphics2D g) {
        if (worldImage == null) {
            return;
        }
        if (projected) {
            g.drawImage(worldImage, dl, np, dl + dx, np + dy, 0, 0, imgWidth, imgHeight, null);
        }
    }
//    @Override
//    public Element getConfiguration() {
//        return options.toXml();
//    }
//
//    @Override
//    public Component getSettingsView() {
//        return options.getEditor();
//    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */