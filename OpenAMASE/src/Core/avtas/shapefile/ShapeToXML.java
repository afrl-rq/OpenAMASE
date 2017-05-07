// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.shapefile;

import avtas.xml.XMLUtil;
import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import javax.swing.JFileChooser;
import avtas.xml.Element;
import java.util.List;

/**
 * A simple utility for converting a directory of shape files to an XML node.
 * @author AFRL/RQQD
 */
public class ShapeToXML {

    private static FileFilter shpFilter = new FileFilter() {

        public boolean accept(File pathname) {
            return pathname.getAbsolutePath().toLowerCase().endsWith(".shp");
        }
    };
    private static DecimalFormat locFormatter = new DecimalFormat("#.######");

    /**
     * Convert a shapefile to an XML node.
     * @param shapeDir
     * @return An XML representation of the shapes
     */
    public static Element convert(File shapeDir) {

        Element topNode = new Element("Shapes");
        topNode.add(new avtas.xml.Comment("The following shapes are defined in degrees lat, lon and "));
        topNode.add(new avtas.xml.Comment("meter height"));

        File[] files;
        if (shapeDir.isDirectory()) {
            files = shapeDir.listFiles(shpFilter);
        } else {
            files = new File[]{shapeDir};
        }

        List<EsriShape> polys;
        Element shpEl;
        
        for (File f : files) {
            System.out.println("processing " + f.getName());
            polys = ShapeUtils.getPolyGeometry(f);
            for (EsriShape p : polys) {
                shpEl = new Element("Shape");
                topNode.add(shpEl);
                //shpEl.setAttribute("Elevation", locFormatter.format(p.getMeterHeight()));
                Element ptEl;
                for (int i = 0; i < p.getY().length; i++) {
                    ptEl = new Element("Vertex");
                    shpEl.add(ptEl);
                    ptEl.setAttribute("Latitude", locFormatter.format(p.getY()[i]));
                    ptEl.setAttribute("Longitude", locFormatter.format(p.getX()[i]));
                }
            }

        }

        return topNode;
    }

    /**
     * Write a given shapefile to an XML file.
     * @param shapeFile     The input shapefile.
     * @param writeToFile   The XML file.
     */
    public static void writeToXML(File shapeFile, File writeToFile) {
        Element el = convert(shapeFile);
        el.toFile(writeToFile);

    }
    
    public static void main(String[] args) {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setDialogTitle("Choose a directory of shape files or single shape file");
        int ans = chooser.showOpenDialog(null);
        if (ans == JFileChooser.APPROVE_OPTION) {
            File readFile = chooser.getSelectedFile();
            chooser.setDialogTitle("Save XML file to...");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            ans = chooser.showSaveDialog(null);
            if (ans == JFileChooser.APPROVE_OPTION) {
                writeToXML(readFile, chooser.getSelectedFile());
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */