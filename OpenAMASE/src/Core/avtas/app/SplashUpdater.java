// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.app;

import avtas.xml.Element;
import avtas.xml.XMLUtil;
import avtas.xml.XmlReader;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.SplashScreen;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author AFRL/RQQD
 */
public class SplashUpdater implements StatusListener {

    SplashScreen splash = SplashScreen.getSplashScreen();
    Graphics2D g;
    Rectangle2D splashBounds;

    String dk_string = "";
    GlyphVector dk_vector = null;

    public SplashUpdater() {
        StatusPublisher.getDefault().addListener(this);
        if (splash != null) {
            g = splash.createGraphics();
            splashBounds = splash.getBounds();
        }
        try {
            Element el = XmlReader.readDocument(getClass().getResource("/amase_dk_version.xml").openStream());
            dk_string = "Based on " + XMLUtil.getValue(el, "title", "") + " " + XMLUtil.getValue(el, "version", "");
            splash = SplashScreen.getSplashScreen();
            if (splash != null) {
                Rectangle clip = new Rectangle(50, 220, 550, 50);
                g.setComposite(AlphaComposite.Clear);
                g.fill(clip);
                g.setPaintMode();
                g.setColor(Color.BLACK);
                GlyphVector v = g.getFont().createGlyphVector(g.getFontRenderContext(), dk_string);
                Rectangle2D vr = v.getLogicalBounds();
                
                int x = (int) (clip.getCenterX() - 0.5 * vr.getWidth());
                int y = (int) (clip.getCenterY() + 0.5 * vr.getHeight());

                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
                g.setClip(clip);
                g.drawString(dk_string, x, y);
            }
        } catch (Exception ex) {
            System.out.println("oops");
            //Logger.getLogger(SplashUpdater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void statusUpdate(String status) {
        splash = SplashScreen.getSplashScreen();
        if (splash != null) {
            Rectangle clip = new Rectangle(50, 200, 550, 20);
            g.setComposite(AlphaComposite.Clear);
            g.fill(clip);
            g.setPaintMode();
            g.setColor(Color.BLACK);
            GlyphVector v = g.getFont().createGlyphVector(g.getFontRenderContext(), status);
            Rectangle2D vr = v.getLogicalBounds();

            int x = (int) (clip.getCenterX() - 0.5 * vr.getWidth());
            int y = (int) (clip.getCenterY() + 0.5 * vr.getHeight());

            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
            g.setClip(clip);
            g.drawString(status, x, y);

            splash.update();
        } else {
            StatusPublisher.getDefault().removeListener(this);
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */