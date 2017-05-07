// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.ui;

import avtas.util.WindowUtils;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author AFRL/RQQD
 */
public class IconTools {

    protected static Path2D defaultShape;
    

    private IconTools() {
    }

    /**
     * Creates an image from a 2D path. It is assumed that the path is drawn
     * around the origin. A size of 64x64 will ensure proper detail without
     * creating an oversized image.
     *
     * @param outline
     */
    public static BufferedImage createIcon(Path2D outline) {

        Rectangle bounds = outline.getBounds();
        BufferedImage source_image = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);


        Graphics2D g2 = source_image.createGraphics();
        setRenderingHints(g2);
        g2.translate(bounds.width / 2, bounds.height / 2);
        g2.setColor(Color.BLACK);
        g2.fill(outline);
        //g2.setColor(Color.BLACK);
        //g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        //g2.draw(outline);
        g2.dispose();
        
        return source_image;

    }

    public static BufferedImage getDefaultIcon() {
        return createIcon(getDefaultShape());
    }

    public static BufferedImage getScaledImage(Image image, int width, int height) {
        
        // scaling is nice only if we use getScaledImage().  Have to do this, then 
        // paint onto the buffered image.
        Image tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImg.createGraphics();
        
        g2.drawImage(tmp, 0, 0, width, height, null);
        g2.dispose();
        return newImg;
    }

    public static BufferedImage getOutlinedImage(Image image, int width, int height, int outline_width, Color color) {
        double scaled_outline = outline_width * image.getWidth(null) / width;
        BufferedImage img = createOutline(image, (int) scaled_outline + 1, color);
        BufferedImage scaledImage = getScaledImage(img, width, height);
        return scaledImage;
    }
    
    public static BufferedImage getFilledImage(Image image, int width, int height, int outline_width, Color outline, Color fill) {
        double scaled_outline = outline_width * image.getWidth(null) / width;
        BufferedImage img = createFill(image, (int) scaled_outline + 1, outline, fill);
        BufferedImage scaledImage = getScaledImage(img, width, height);
        return scaledImage;
        
        //Image scaledImage = getScaledImage(image, width, height);
        //BufferedImage img = createFill(scaledImage, outline_width, outline, fill);
        //return img;
    }

    
    protected static BufferedImage createFill(Image srcImage, int outlineSize, Color outline, Color fill) {
        
        BufferedImage fillImage = new BufferedImage(srcImage.getWidth(null) - 2*outlineSize, 
                srcImage.getHeight(null) - 2*outlineSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = fillImage.createGraphics();
        setRenderingHints(g2);
        
        g2.drawImage(srcImage, 0, 0, fillImage.getWidth(), fillImage.getHeight(), null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN));
        g2.setColor(fill);
        g2.fillRect(0, 0, fillImage.getWidth(), fillImage.getHeight());
        g2.dispose();
        
        // draw the mask underneath
        BufferedImage destImage = new BufferedImage(srcImage.getWidth(null), srcImage.getHeight(null), BufferedImage.TRANSLUCENT);

        g2 = destImage.createGraphics();
        setRenderingHints(g2);

        g2.drawImage(srcImage, 0, 0, null);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN));
        g2.setColor(outline);
        g2.fillRect(0, 0, destImage.getWidth(), destImage.getHeight());
        
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g2.drawImage(fillImage, outlineSize, outlineSize, null);
        g2.dispose();

        return destImage;
    }

    protected static BufferedImage createOutline(Image srcImage, int outlineSize, Color color) {


        //int blurRadius = outlineSize;
        int margin = outlineSize;

        // draw the mask underneath
        BufferedImage destImage = new BufferedImage(srcImage.getWidth(null) + 2*margin,
                srcImage.getHeight(null) + 2*margin, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = destImage.createGraphics();
        setRenderingHints(g2);

        g2.drawImage(srcImage, 0, 0,  destImage.getWidth(), destImage.getHeight(), null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN));
        g2.setColor(color);
        g2.fillRect(0, 0, destImage.getWidth(), destImage.getHeight());
        g2.dispose();

        // blur the mask to make the edges soft
        //ConvolveOp blurOp = makeBlurOp(outlineSize/2);
        //destImage = blurOp.filter(destImage, null);

        //draw the original image on top
        g2 = destImage.createGraphics();
        setRenderingHints(g2);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g2.drawImage(srcImage, margin, margin, null);
        
        g2.dispose();

        return destImage;

    }

    protected static ConvolveOp makeBlurOp(int size) {
        float[] data = new float[size * size];
        float value = 1.0f / (float) (size * size);
        for (int i = 0; i < data.length; i++) {
            data[i] = value;
        }

        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        return new ConvolveOp(new Kernel(size, size, data), ConvolveOp.EDGE_ZERO_FILL, hints);
    }

    /**
     * Returns the default AMASE "wedge" aircraft shape.
     */
    public static Path2D getDefaultShape() {
        if (defaultShape == null) {
            int width = 64, height = 64;
            Path2D path = new Path2D.Float();
            path.moveTo(0, -height);
            path.lineTo(width, height);
            path.lineTo(0, 0.4 * height);
            path.lineTo(-width, height);
            path.closePath();
            defaultShape = path;
        }
        return defaultShape;
    }
    
    
    static void setRenderingHints(Graphics2D g2) {
       
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    }

    public static void main(String[] args) {

        JPanel iconPanel = new JPanel();
        BufferedImage img = getDefaultIcon();
        JLabel label = new JLabel(new ImageIcon(getFilledImage(img, 32, 32, 5, Color.BLACK, Color.YELLOW)));
        iconPanel.add(label);
        WindowUtils.showApplicationWindow(iconPanel);
        
        WindowUtils.showApplicationWindow(new JLabel(new ImageIcon(getScaledImage(img, 24, 24))));
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */