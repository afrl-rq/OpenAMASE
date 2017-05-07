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
import avtas.map.MapPanel;
import avtas.map.Proj;
import avtas.map.image.MapScaledImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides buffering by rendering an image in a separate renderThread, then
 * requesting a repaint on the map when finished.
 * <br/>
 * This allows for any layer to be buffered. By adding buffering, map
 * operations such as pan, zoom, or drawing other layers is not interupted because
 * a slow layer, such as imagery, has not loaded.  During animation periods, buffering
 * can significantly reduce CPU usage by using image copy operations, rather than
 * Java2D drawing commands.
 * <br/>
 * To Use BufferSupport, set layers to buffer graphics using {@link MapLayer#setBuffered(boolean) }.
 * <br/>
 * In configuration files, buffering can be controlled using the <code>Buffered="True"</code> attribute
 * in the layer element.<br/>
 * <code> {@literal 
 * <Layer Class="class.name" Buffered="True"/>
 * }</code>
 *
 *
 * @author AFRL/RQQD
 */
public class BufferSupport {

    private RenderThread renderThread = null;
    private MapScaledImage frontGraphic = null;
    private MapScaledImage backGraphic = null;
    private BufferedImage backRaster = null;
    public static long sleep_period = 200; // milliseconds
    protected MapLayer layer;
    protected MapPanel map;
    static final Color CLEAR = new Color(0, 0, 0, 0);

    /**
     * Surrounds the given layer with a buffered layer
     */
    public BufferSupport(MapLayer layer) {
        this.layer = layer;
    }

    @Override
    protected void finalize() throws Throwable {
        if (backRaster != null) {
            backRaster.flush();
        }
        if (frontGraphic != null && frontGraphic.getImage() != null) {
            frontGraphic.getImage().flush();
        }
    }
    
    

    public void paint(Graphics2D g) {
        if (frontGraphic != null) {
            frontGraphic.paint(g);
        }
    }

    public void project(Proj v) {

        if (frontGraphic != null) {
            frontGraphic.project(v);
        }

        if (renderThread != null) {
            renderThread.go = false;
            renderThread = null;
        }

        renderThread = new RenderThread(v);
        renderThread.start();

    }

    /**
     * A thread that renders a new image in the background, then requests a
     * repaint when finished. If the process is interrupted by an project()
     * event, then the thread is killed.
     */
    class RenderThread extends Thread {

        private Proj view;
        private boolean go = true;

        public RenderThread(Proj view) {
            this.view = view;

        }

        @Override
        public void run() {

            // start out with a delay.  Multiple projection operations may be occuring.  Give the 
            // thread a chance to cancel if a new projection has been done.
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(BufferSupport.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (go) {
                if (backRaster == null || backRaster.getWidth() != view.getWidth() || backRaster.getHeight() != view.getHeight()) {
                    if (backRaster != null) {
                        backRaster.flush();
                    }
                    backRaster = new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
                    System.out.println("created new image");
                }
            }

            if (go) {
                backGraphic = new BackgroundImage(backRaster, view.getNorthLat(), view.getWestLon(),
                        view.getWidth() / view.getPixPerLon(), view.getHeight() / view.getPixPerLat());
                backGraphic.project(view);
            }

            if (go) {
                layer.project(view);
            }

            if (go) {
                Graphics2D g = backRaster.createGraphics();
                g.setBackground(CLEAR);
                g.clearRect(0, 0, view.getWidth(), view.getHeight());
                layer.paint(g);
                g.dispose();
            }

            if (go) {

                // swap the images
                if (frontGraphic != null)
                    backRaster = (BufferedImage) frontGraphic.getImage();

                // repaint the map with the newly projected and painted graphics
                frontGraphic = backGraphic;
                if (map != null) {
                    map.requestRepaint();
                }
            }

        }
    }

    /**
     * A custom scaled image that allows for larger (greater than 180 deg)
     * longitudinal widths
     */
    protected class BackgroundImage extends MapScaledImage {

        public BackgroundImage(Image img, double ullat, double ullon, double lonwidth, double latheight) {
            setImage(img);
            this.ullat = ullat;
            this.ullon = ullon;
            this.dlon = lonwidth;
            this.dlat = latheight;
        }

        @Override
        public void project(Proj proj) {
            if (proj == null) {
                setProjected(false);
                return;
            }
            setScreenShape(new Rectangle2D.Double(proj.getX(ullon), proj.getY(ullat),
                    proj.getPixPerLon() * dlon, proj.getPixPerLat() * dlat));
            setProjected(true);
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */