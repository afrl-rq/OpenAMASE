// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

/**
 * A reader implementation for reading DDS-formatted images.
 * 
 * For this to work with ImageIO, load the reader into the registry using:<br/>
 * <code>IIORegistry.getDefaultInstance().registerServiceProvider(new DDSImageReaderSpi());</code>
 * 
 * @author AFRL/RQQD
 */
public class DDSReader extends ImageReader {

    private ImageInputStream iis = null;
    static final int HEADER_SIZE = 128;
    ByteBuffer header = null;

    public DDSReader(DDSImageReaderSpi spi) {
        super(spi);
    }
    
    protected DDSReader(ImageInputStream is) {
        super(null);
        this.iis = is;
    }

    @Override
    public void setInput(Object input, boolean seekForwardOnly, boolean ignoreMetadata) {

        super.setInput(input, seekForwardOnly, ignoreMetadata);
        this.iis = (ImageInputStream) input;

    }

    @Override
    public int getNumImages(boolean allowSearch) throws IOException {
        return 1;
    }

    @Override
    public int getWidth(int imageIndex) throws IOException {
        checkHeader();
        return header.getInt(12);
    }

    @Override
    public int getHeight(int imageIndex) throws IOException {
        checkHeader();
        return header.getInt(16);
    }

    @Override
    public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex) throws IOException {
        ArrayList<ImageTypeSpecifier> list = new ArrayList<ImageTypeSpecifier>();
        list.add(ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_ARGB));
        return list.iterator();
    }

    @Override
    public IIOMetadata getStreamMetadata() throws IOException {
        return null;
    }

    @Override
    public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
        return null;
    }

    @Override
    public BufferedImage read(int imageIndex, ImageReadParam param) throws IOException {
        checkHeader();
        int w = getWidth(imageIndex);
        int h = getHeight(imageIndex);
        
        char ver = header.getChar(87);

        if (ver == '1') {
            return readDxt1Buffer(w, h);
        }
        else if (ver == '3') {
            return readDxt3Buffer(w, h);
        }
        return null;
    }

    void checkHeader() throws IOException{
        if (header == null) {
            header = ByteBuffer.allocate(HEADER_SIZE);
            header.order(ByteOrder.LITTLE_ENDIAN);
            iis.readFully(header.array());
        }
    }

    BufferedImage readDxt1Buffer(int width, int height) throws IOException {

        ByteBuffer buf = ByteBuffer.allocate(width/4 * height/4 * 8);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        iis.readFully(buf.array());

        int[] pixels = new int[16];

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Color24[] lookupTable = new Color24[]{new Color24(), new Color24(), new Color24(), new Color24()};

        int numTilesWide = width / 4;
        int numTilesHigh = height / 4;
        for (int i = 0; i < numTilesHigh; i++) {
            for (int j = 0; j < numTilesWide; j++) {


                short minColor = buf.getShort();
                short maxColor = buf.getShort();

                expandLookupTable(lookupTable, minColor, maxColor);

                int colorData = buf.getInt();

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

    BufferedImage readDxt3Buffer(int width, int height) throws IOException {
        
        ByteBuffer buf = ByteBuffer.allocate(width/4 * height/4 * 16);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        iis.readFully(buf.array());

        int[] pixels = new int[16];
        int[] alphas = new int[16];

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Color24[] lookupTable = new Color24[]{new Color24(), new Color24(), new Color24(), new Color24()};
        Color24 alphaMult = new Color24();

        int numTilesWide = width / 4;
        int numTilesHigh = height / 4;
        for (int i = 0; i < numTilesHigh; i++) {
            for (int j = 0; j < numTilesWide; j++) {


                // Read the alpha table.
                long alphaData = buf.getLong();
                for (int k = alphas.length - 1; k >= 0; k--) {
                    alphas[k] = (int) (alphaData >>> (k * 4)) & 0xF; // Alphas are just 4 bits per pixel
                    alphas[k] <<= 4;
                }

                short minColor = buf.getShort();
                short maxColor = buf.getShort();

                expandLookupTable(lookupTable, minColor, maxColor);

                int colorData = buf.getInt();

                for (int k = pixels.length - 1; k >= 0; k--) {
                    int colorCode = (colorData >>> k * 2) & 0x03;
                    multiplyAlpha(alphaMult, lookupTable[colorCode], alphas[k]);
                    pixels[k] = (alphas[k] << 24) | getPixel888(alphaMult);
                }

                result.setRGB(j * 4, i * 4, 4, 4, pixels, 0, 4);
            }
        }

        return result;
    }

    private static void multiplyAlpha(Color24 ret, Color24 color, int alpha) {
        //Color result = new Color();

        double alphaF = alpha / 256.0;

        ret.r = (int) (color.r * alphaF);
        ret.g = (int) (color.g * alphaF);
        ret.b = (int) (color.b * alphaF);

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

    static int getPixel888(Color24 color) {
        return color.r << 16 | color.g << 8 | color.b;
    }

    static class Color24 {

        int r, g, b;
    }
    
    /** A convenience method for reading a DDS image directly from a file.  */
    public static BufferedImage readImage(File file) {
        try {
            FileImageInputStream fis = new FileImageInputStream(new RandomAccessFile(file, "r"));
            DDSReader reader = new DDSReader(fis);
            return reader.read(0);
        } catch (IOException ex) {
            Logger.getLogger(DDSReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
        
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */