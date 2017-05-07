// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Keeps track of images by the URL to the image.  This prevents the same image from being
 * loaded many times (for instance, the use of many identical icons on a map or multiple
 * maps used in the same application).
 *
 * @author AFRL/RQQD
 */
public class ImageTracker {

    static HashMap<URL, WeakReference<Image>> imageMap = new HashMap<URL, WeakReference<Image>>();
    
    static Color CLEAR = new Color(0,0,0,0);

    public static Image getImage(URL url) {
        WeakReference<Image> image = imageMap.get(url);
        if (image == null || image.get() == null ) {
            try {
                BufferedImage bimg = ImageIO.read(url);
                image = new WeakReference<Image>(bimg);
                imageMap.put(url, image);
            } catch (IOException ex) {
                Logger.getLogger(ImageTracker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return image.get();
    }

    public static Image getImage(File file) {
        try {
            return getImage(file.toURI().toURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(ImageTracker.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void unloadAll() {
        for(WeakReference<Image> img : imageMap.values()) {
            if (img.get() != null)
                img.get().flush();
        }
        imageMap.clear();
        System.gc();
    }

    public static void unload(URL url) {
        WeakReference<Image> img = imageMap.remove(url);
        if (img != null && img.get()!= null) {
            img.get().flush();
            System.gc();
        }
    }

    public static void unload(File file) {
        try {
            unload(file.toURI().toURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(ImageTracker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static boolean validate(Image image) {
        if (image instanceof VolatileImage) {
            VolatileImage vimg = (VolatileImage) image;
            return !vimg.contentsLost();
        }
        return true;
    }
    
    static VolatileImage createVolatileImage(BufferedImage bimg) {
        
        int width = bimg.getWidth();
        int height = bimg.getHeight();
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        VolatileImage image = null;

        image = gc.createCompatibleVolatileImage(width, height, VolatileImage.TRANSLUCENT);

        int valid = image.validate(gc);

        if (valid == VolatileImage.IMAGE_INCOMPATIBLE) {
            image = createVolatileImage(bimg);
        }
        
        Graphics2D g = image.createGraphics();
        g.setBackground(CLEAR);
        g.clearRect(0,0, width, height);
        
        g.drawImage(bimg, 0, 0, null);
        
        valid = image.validate(gc);
        if (valid == VolatileImage.IMAGE_INCOMPATIBLE) {
            image = createVolatileImage(bimg);
        }

        return image;


    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */