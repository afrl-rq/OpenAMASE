// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.scenario;

import avtas.app.UserExceptions;
import avtas.xml.Element;
import avtas.xml.XmlReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JOptionPane;

/**
 * This class handles the reading of scenario files.  Scenario files are XML formatted
 * files.
 * 
 * 
 * @author AFRL/RQQD
 * @version 1.0
 */
public class ScenarioReader {

    /** initializes the sim from an XML source.  This skips the ScenarioEventList section,
     *  but returns all other XML content.
     */
    public static Element readScenario(File file) {
        //return readScenario(file, true, "AMASE/ScenarioEventList");
        return readScenario(file, true);
    }

    /**
     * Reads a scenario file and returns an XML Element
     * @param file the file to read
     * @param processLinks set to true to expand linked file content into the Element
     * @param skipNodes XML elements to skip when reading in, by name (can be null or empty string)
     */
    public static Element readScenario(File file, boolean processLinks, String... skipNodes) {

        Element n;
        try {

            InputStream in;
            boolean isZip = false;

            if (file.getName().toUpperCase().endsWith(".ZIP")) {
                isZip = true;
            }

            if (isZip) {
                ZipInputStream zin = new ZipInputStream(new FileInputStream(file));
                ZipEntry entry = zin.getNextEntry();
                if (!entry.getName().equals("scenario.xml")) {
                    JOptionPane.showMessageDialog(null, "Zip File does not have proper content", "Warning", JOptionPane.WARNING_MESSAGE);
                }
                in = zin;
            }
            else {
                in = new FileInputStream(file);
            }
            
            n = XmlReader.readDocument(in, skipNodes);
            if (!n.getName().equals("AMASE")) {
                UserExceptions.showWarning(file.getAbsolutePath() + " may not be a proper simulation input file.  "
                        + "See the input file specification.");
            }

            // resolve any links that this file may have
            if (processLinks) {
                processLinks(file, n);
            }

            in.close();
            return n;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /** Reads a scenario file from the initialization parameters in the config file */
    public static File getScenarioFile(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if ("--scenario".equalsIgnoreCase(args[i]) && args.length > i + 1) {
                try {
                    File src = new File(args[i + 1]);
                    if (!src.exists()) {
                        JOptionPane.showMessageDialog(null, "Scenario File does not exist.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return null;
                    }
                    return src;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }
    
    /** adds all of the objects from the linked file to the object store.
     *  First, it looks for a file name with the absolute path given by the file. If that
     *  fails, it tries to load a file with the relative path given by the
     *  file, from the point of the source file being loaded.
     * @param file descriptor of the file to load.
     */
    static void processLinks(File parent, Element node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            if (!(node.get(i) instanceof Element)) {
                continue;
            }

            Element child = (Element) node.get(i);
            if (!child.getName().equals("Link")) {
                continue;
            }

            String source = child.getAttribute("Source");
            if (source == null) {
                continue;
            }

            File file = new File(source);
            // if this is a relative file link, then resolve it with relation to the
            // file containing the link.
            if (!file.isAbsolute()) {
                file = new File(parent, source);
            }

            Element newNode = Element.read(file);
            if (newNode != null) {
                // add the children of the linked node to the node
                node.set(i, newNode.getChildElements());
                // process all of the children to resolve additional links
                processLinks(file, newNode);
            }
        }
    }

    
//    public static void processLinks(Element parent, File parentFile) {
//        try {
//            for (Element el : parent.getChildElements()) {
//
//                if (el.getName().equals("Link")) {
//                    String linkFile = el.getAttr("Source", "");
//
//                    if (!linkFile.isEmpty()) {
//
//                        File f = new File(linkFile);
//                        if (!f.exists()) {
//                            File srcFile = new File(parentFile.toURI());
//                            f = new File(srcFile.getParentFile(), linkFile);
//                        }
//                        if (f.exists()) {
//                            Element n = XmlReader.readDocument(f);
//                            //recurse the element to resolve any additional "link" attributes
//                            processLinks(n, f);
//                            // add all contents of the linked file to the current location in the tree
//                            parent.set(parent.indexOf(el), n.getChildren());
//                        }
//                    }
//                }
//                // continue the processing of links on the next level of the tree
//                processLinks(el, parentFile);
//            }
//
//        } catch (Exception ex1) {
//            Logger.getLogger(ScenarioManager.class.getName()).log(Level.SEVERE, null, ex1);
//        }
//    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */