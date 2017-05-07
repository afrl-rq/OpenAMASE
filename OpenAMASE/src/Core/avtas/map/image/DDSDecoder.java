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
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Reads a Direct-X texture file (usually a .dds extension) and returns a buffered image with its contents.  Currently,
 * this class will read DXT1 and DXT3 style files, but has not been thoroughly tested.
 *
 * This is based on work found in the WorldWind forums (www.forum.worldwindcentral.com) posted by remleduff on 12 Jun 2009.
 *
 * @author AFRL/RQQD
 */
public class DDSDecoder {

    private static final int DDPF_FOURCC = 0x0004;
    private static final int DDSCAPS_TEXTURE = 0x1000;
    protected static final int HEADER_SIZE = 128;


    /** Reads the file header.  This is here to help understand what is contained in the header.  The whole header is not
     *  read since the extraction depends only on DXT type and image size.
     * @param buffer
     * @return size of image
     */
    protected static Dimension readHeaderDxt(ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.rewind();
        byte[] magic = new byte[4];
        buffer.get(magic);
        assert new String(magic).equals("DDS ");

        int version = buffer.getInt();
        assert version == 124;

        int flags = buffer.getInt();
        int height = buffer.getInt();
        int width = buffer.getInt();
        int pixels = buffer.getInt(); // ???
        int depth = buffer.getInt();
        int mipmaps = buffer.getInt();

        buffer.position(buffer.position() + 44); // 11 unused double-words

        int pixelFormatSize = buffer.getInt(); // ???
        int fourCC = buffer.getInt();
        assert fourCC == DDPF_FOURCC;

        byte[] format = new byte[4];
        buffer.get(format);
        //System.out.println(new String(format));

        int bpp = buffer.getInt(); // bits per pixel for RGB (non-compressed) formats
        buffer.getInt(); // rgb bit masks for RGB formats
        buffer.getInt(); // rgb bit masks for RGB formats
        buffer.getInt(); // rgb bit masks for RGB formats
        buffer.getInt(); // alpha mask for RGB formats

        int unknown = buffer.getInt();
        assert unknown == DDSCAPS_TEXTURE;
        int ddsCaps = buffer.getInt(); // ???
        buffer.position(buffer.position() + 12);
        return new Dimension(width, height);
    }

    protected static int getDXTVersion(ByteBuffer buf) {
        char verChar = buf.getChar(87);
        
        if (verChar == '1') return 1;
        if (verChar == '3') return 3;
        return 0;
    }

    protected static Dimension getImageSize(ByteBuffer buf) {
        int h = buf.getInt(12);
        int w = buf.getInt(16);
        return new Dimension(w, h);
    }

    /** Reads a DDS image file and returns a buffered image.
     *
     * @param file the file to read from
     * @return a buffered image containing the image data, or null if there is an error.
     */
    public static BufferedImage readDxt(File file) {
        try {
            ByteBuffer buf = ByteBuffer.allocate((int)file.length());
            FileInputStream fis = new FileInputStream(file);
            int len = fis.read(buf.array());
            if (len != (int) file.length()) {
                throw new IOException("File reading error.");
            }
            return readDxt(buf);
        } catch (Exception ex) {
            Logger.getLogger(DDSDecoder.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /** Method for extracting an image from a byte buffer that contains the entire image file (header + image data) */
    public static BufferedImage readDxt(ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int dxtVer = getDXTVersion(buffer);
        Dimension dimension = getImageSize(buffer);
        buffer.position(HEADER_SIZE);
        if (dxtVer == 1)
            return readDxt1Buffer(buffer, dimension.width, dimension.height);
        else if (dxtVer == 3) {
            return readDxt3Buffer(buffer, dimension.width, dimension.height);
        }
        return null;
    }

    protected static BufferedImage readDxt3(ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        Dimension dimension = readHeaderDxt(buffer);

        return readDxt3Buffer(buffer, dimension.width, dimension.height);
    }

    protected static BufferedImage readDxt1(ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        Dimension dimension = readHeaderDxt(buffer);

        return readDxt1Buffer(buffer, dimension.width, dimension.height);
    }

    protected static BufferedImage readDxt3Buffer(ByteBuffer buffer, int width, int height) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int[] pixels = new int[16];
        int[] alphas = new int[16];

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        Color24[] lookupTable = new Color24[] {new Color24(),new Color24(),new Color24(),new Color24()} ;

        int numTilesWide = width / 4;
        int numTilesHigh = height / 4;
        for (int i = 0; i < numTilesHigh; i++) {
            for (int j = 0; j < numTilesWide; j++) {
                // Read the alpha table.
                long alphaData = buffer.getLong();
                for (int k = alphas.length - 1; k >= 0; k--) {
                    alphas[k] = (int) (alphaData >>> (k * 4)) & 0xF; // Alphas are just 4 bits per pixel
                    alphas[k] <<= 4;
                }

                short minColor = buffer.getShort();
                short maxColor = buffer.getShort();
                
                expandLookupTable(lookupTable, minColor, maxColor);

                int colorData = buffer.getInt();

                for (int k = pixels.length - 1; k >= 0; k--) {
                    int colorCode = (colorData >>> k * 2) & 0x03;
                    pixels[k] = (alphas[k] << 24) | getPixel888(multiplyAlpha(lookupTable[colorCode], alphas[k]));
                }

                result.setRGB(j * 4, i * 4, 4, 4, pixels, 0, 4);
            }
        }
        return result;
    }

    protected static BufferedImage readDxt1Buffer(ByteBuffer buffer, int width, int height) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int[] pixels = new int[16];

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Color24[] lookupTable = new Color24[] {new Color24(),new Color24(),new Color24(),new Color24()} ;

        int numTilesWide = width / 4;
        int numTilesHigh = height / 4;
        for (int i = 0; i < numTilesHigh; i++) {
            for (int j = 0; j < numTilesWide; j++) {

                short minColor = buffer.getShort();
                short maxColor = buffer.getShort();
               
                expandLookupTable(lookupTable, minColor, maxColor);

                int colorData = buffer.getInt();

                for (int k = pixels.length - 1; k >= 0; k--) {
                    int colorCode = (colorData >>> k * 2) & 0x03;
                    //pixels[k] = (alphas[k] << 24) | getPixel888(multiplyAlpha(lookupTable[colorCode], 256));
                    pixels[k] = getPixel888(lookupTable[colorCode]);
                }

                result.setRGB(j * 4, i * 4, 4, 4, pixels, 0, 4);
            }
        }
        return result;
    }

    private static Color24 multiplyAlpha(Color24 color, int alpha) {
        //Color result = new Color();

        double alphaF = alpha / 256.0;

        Color24 c = new Color24();
        
        c.r = (int) (color.r * alphaF);
        c.g = (int) (color.g * alphaF);
        c.b = (int) (color.b * alphaF);
        
        return c;
    }

    protected static Color24 getColor565(Color24 color, int pixel) {
        //Color color = new Color();

        color.r = (int) (((long) pixel) & 0xf800) >>> 8;
        color.g = (int) (((long) pixel) & 0x07e0) >>> 3;
        color.b = (int) (((long) pixel) & 0x001f) << 3;
        

        return color;
    }

    private static void expandLookupTable(Color24[] result, short minColor, short maxColor) {

        getColor565(result[0], minColor);
        getColor565(result[1], maxColor);

        result[2].r = (2 * result[0].r + result[1].r + 1) / 3;
        result[2].g = (2 * result[0].g + result[1].g + 1) / 3;
        result[2].b = (2 * result[0].b + result[1].b + 1) / 3;

        result[3].r = (result[0].r + 2 * result[1].r + 1) / 3;
        result[3].g = (result[0].g + 2 * result[1].g + 1) / 3;
        result[3].b = (result[0].b + 2 * result[1].b + 1) / 3;

    }

    protected static int getPixel888(Color24 color) {
        return color.r << 16 | color.g << 8 | color.b;
    }
    
    static class Color24 {
        int r, g, b;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */