// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.layers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import avtas.map.image.MapScaledImage;
import avtas.properties.UserProperty;
import avtas.properties.XmlSerializer;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.awt.Image;

/**
 *
 * @author AFRL/RQQD
 */
public class WorldImageLayer extends ImageLayer {

    private boolean dynamicLoad = true;
    private String picMatcher = "(jpg)|(JPG)|(jpeg)|(JPEG)|(png)|(PNG)|(bmp)|(BMP)|(tif)|(TIF)";
    
    @UserProperty(Description = "Directory where images are stored.", DisplayName = "Image Directory",
            FileType = UserProperty.FileTypes.Directories)
    File Directory = null;

    /** Creates a new instance of WorldImageLayer */
    public WorldImageLayer() {
    }

    public WorldImageLayer(String imageDir) {
        setImageDir(new File(imageDir));
    }

    @Override
    public void setConfiguration(Element node) {
        setDynamicLoad(XMLUtil.getBool(node, "DynamicLoad", dynamicLoad));
        XmlSerializer.deserialize(node, this);
        setImageDir(Directory);
    }

    public void setImageDir(File dir) {
        try {
            this.Directory = dir;
            
            File[] tmpImageFiles = dir.listFiles(new FileFilter() {

                public boolean accept(File pathname) {
                    if (!pathname.getName().contains(".")){
                        return false;
                    }
                    String suff = pathname.getName().split("\\.", 2)[1];
                    if (suff.matches(picMatcher)) {
                        return true;
                    }
                    return false;
                }
            });

            if (tmpImageFiles == null) {
                return;
            }

            File[] tmpWorldFiles = new File[tmpImageFiles.length];
            for (int i = 0; i < tmpWorldFiles.length; i++) {
                String imgFileName = tmpImageFiles[i].getName();
                imgFileName = imgFileName.substring(0, imgFileName.length() - 4);
                tmpWorldFiles[i] = new File(dir + "/" + imgFileName);

            }

            for (int i = 0; i < tmpImageFiles.length; i++) {
                setImage(tmpImageFiles[i], tmpWorldFiles[i]);
            }


        } catch (Exception e) {
            e.printStackTrace(); System.exit(1);
        }
    }

    /**
     * Gets the image and image coordinates.  Lays the image in the map
     * @param imageFile A file that contains the image
     * @param worldFile The file that conforms to the ESRI world file format.
     */
    public void setImage(final File imageFile, final File worldFile) {

        try {
            String suffix = imageFile.getName().substring(imageFile.getName().lastIndexOf(".") + 1);
            ;

            ImageReader reader = ImageIO.getImageReadersBySuffix(suffix).next();
            reader.setInput(ImageIO.createImageInputStream(imageFile), true);

            int width = reader.getWidth(0);
            int height = reader.getHeight(0);

            float[] vals = getWorldFile(worldFile);

            if (isDynamicLoad()) {
                MapScaledImage raster = new MapScaledImage(imageFile, vals[5], vals[4],
                        vals[5] + vals[3] * height, vals[4] + vals[0] * width, width, height);
                addImage(raster);
            } else {
                Image img = ImageIO.read(imageFile);
                MapScaledImage raster = new MapScaledImage(img, vals[5], vals[4],
                        vals[5] + vals[3] * height, vals[4] + vals[0] * width);
                addImage(raster);
            }

        } catch (Exception ex) {
            ex.printStackTrace(); System.exit(1);
        }
    }

    public static float[] getWorldFile(File worldFile) {
        int numLines = 6;

        float[] rawvals = new float[numLines];

        try {

            BufferedReader reader = new BufferedReader(new FileReader(worldFile));
            for (int i = 0; i < numLines; i++) {
                String tmp = reader.readLine();
                rawvals[i] = Float.valueOf(tmp);
            }
        } catch (Exception ex) {
            ex.printStackTrace(); System.exit(1);
            return rawvals;
        }

        return rawvals;
    }

    /** Sets whether or not images are loaded and unloaded based on the current view.  
     *  If set to true, images are only loaded when they are in view, otherwise all images
     *  are loaded into memory at startup.
     * 
     * @param bool
     */
    private void setDynamicLoad(boolean bool) {
        this.dynamicLoad = bool;
    }

    /** returns true if images are set to load only when they are in the map view. */
    public boolean isDynamicLoad() {
        return dynamicLoad;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */