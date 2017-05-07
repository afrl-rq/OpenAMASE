// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.app;


import avtas.xml.Comment;
import avtas.xml.Element;
import avtas.xml.XmlReader;
import avtas.xml.XmlWriter;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;


/**
 * An Amase Service that handles the storage and retrieval of settings. In
 * general, settings are stored in a folder that is specified at startup. As
 * individual resources (files) are requested by application components, the
 * settings directory is checked for the file.
 * <br/>
 * File references are passed using strings. The strings can be any valid
 * relative path. For instance, to get a file named "test.xml", the string
 * should be "test.xml". However, if requesting a file that is nested in
 * sub-directories, the the path should be given as "path/to/file/test.xml".
 *
 * <br/>
 * Uses the Java 7 NIO.2 file tools.
 *
 * @author AFRL/RQQD
 */
public class SettingsManager  {

    static Path application_directory = null;

    static {
        application_directory = Paths.get(".");
    }

    /**
     * Sets the settings directory for this application. If the directory does
     * not exist, then one is created, including antecedent directories, if
     * necessary. If the specified directory exists but is not a directory, an
     * error will be thrown.
     *
     * @param dir settings directory
     */
    public static void setSettingsDirectory(String dir) {
        Path path = Paths.get(dir);
        if (Files.exists(path) && !Files.isDirectory(path)) {
            UserExceptions.showError(SettingsManager.class, "", null);
        }
        else {
            application_directory = path;
            try {
                Files.createDirectories(path);
            } catch (IOException ex) {
                Logger.getLogger(SettingsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Retrieves the directory that contains application-specific resources.
     *
     * @return Application settings directory, or null if none has been
     * specified.
     */
    public static Path getSettingsDirectory() {
        return application_directory;
    }

    /**
     * Returns the requested resource as a path. This first searches the
     * application-specific settings directory. A new file will be created in
     * the application directory if none exists.
     *
     * @param relativePath the filename
     * @return a path to the requested file.
     */
    public static File getFile(String relativePath) {
        Path retPath;

        retPath = application_directory.resolve(relativePath);
        if (Files.exists(retPath)) {
            return retPath.toFile();
        }

        // create a new file if it can't be found
        retPath = application_directory.resolve(relativePath);
        try {
            Files.createFile(retPath);
            return retPath.toFile();
        } catch (IOException ex) {
            UserExceptions.showError(SettingsManager.class, "", ex);
            return null;
        }

    }

    /**
     * Returns true if a file with the given name exists in the application
     * settings directory.
     */
    public static boolean exists(String relativePath) {
        return Files.exists(application_directory.resolve(relativePath));
    }

    /**
     * Returns an input stream to a given file. If the file does not exist, this
     * returns null.
     *
     * @param relativePath the file to read.
     * @return an input stream for the file or null if the file doesn't exist.
     */
    public static InputStream getInputStream(String relativePath) {
        Path path = application_directory.resolve(relativePath);
        try {
            if (Files.exists(path))
                return Files.newInputStream(path);
        } catch (IOException ex) {
            UserExceptions.showError(SettingsManager.class, "", ex);
            return null;
        }
        return null;
    }

    /**
     * Returns the requested file as an XML Element. If the file does not exist,
     * returns an empty XML Element.
     *
     * @param relativePath file to read as an XML element
     * @return an XML element or null if the file is not found.
     */
    public static Element getAsXml(String relativePath) {
        if (exists(relativePath)) {
            try {
                return XmlReader.readDocument(getFile(relativePath));
            } catch (Exception ex) {
                UserExceptions.showError(SettingsManager.class, "Cannot parse file as XML", ex);
            }
        }
        return null;
    }

    /**
     * Returns an output stream for the given file. If the file doesn't exist in
     * the application directory, then one is created.
     *
     * @param relativePath
     * @return an output stream for the given file
     */
    public static OutputStream openOutputStream(String relativePath) {
        try {
            Path path = application_directory.resolve(relativePath);
            return Files.newOutputStream(path);
        } catch (IOException ex) {
            UserExceptions.showError(SettingsManager.class, "", ex);
            return null;
        }
    }

    /**
     * Reads the entire file contents and returns it as a byte array. If the
     * File doesn't exist, an empty byte array is returned.
     *
     * @param relativePath
     * @return the entire file as a byte array.
     */
    public static byte[] getFileData(String relativePath) {
        try {
            Path filePath = application_directory.resolve(relativePath);
            if (Files.exists(filePath))
                return Files.readAllBytes(filePath);
        } catch (IOException ex) {
            UserExceptions.showError(SettingsManager.class, "Error reading a file: " + relativePath, ex);
        }
        return new byte[]{};
    }

    /**
     * Writes all of the bytes to a file with the given name in the application
     * settings directory. If the file doesn't exist, then one is created.
     *
     * @param bytes
     * @param relativePath
     */
    public static void setFileData(byte[] bytes, String relativePath) {
        try {
            Path filePath = application_directory.resolve(relativePath);
            Files.write(filePath, bytes);
        } catch (IOException ex) {
            UserExceptions.showError(SettingsManager.class, "Error writing to a file: " + relativePath, ex);
        }
    }

    /**
     * Writes the given XML element to a file with the given name in the application
     * settings directory. If the file doesn't exist, then one is created.  The XML
     * is output in a "pretty" format with indents and line returns.
     *
     * @param xmlEl Top-level element to write
     * @param relativePath file name to which the XML is written
     * @param topComments comments that should precede the main element
     */
    public static void setFileData(Element xmlEl, String relativePath, Comment... topComments) {

        XmlWriter.writeToFile(application_directory.resolve(relativePath).toFile(), xmlEl);

    }

    public static File getFileChooser(Component parent, String title, String approveText) {
        JFileChooser chooser = new JFileChooser(application_directory.toFile());

        JRadioButton relBut = new JRadioButton("Use Relative Path");
        JRadioButton absBut = new JRadioButton("Use Absolute Path");
        JRadioButton copyBut = new JRadioButton("Copy to config folder");
        ButtonGroup bg = new ButtonGroup();
        bg.add(relBut);
        bg.add(absBut);
        bg.add(copyBut);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(new TitledBorder("File Reference"));
        buttonPanel.add(relBut);
        buttonPanel.add(absBut);
        buttonPanel.add(copyBut);

        chooser.setDialogTitle(title);
        chooser.setAccessory(buttonPanel);
        int ans = chooser.showDialog(parent, approveText);


        if (ans == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null) {
            Path origPath = chooser.getSelectedFile().toPath();
            if (relBut.isSelected()) {
                Path relPath = application_directory.relativize(origPath);
                return relPath.toFile();
            }
            if (absBut.isSelected()) {
                return chooser.getSelectedFile();
            }
            if (copyBut.isSelected()) {

                Path newPath = application_directory.resolve(origPath.getFileName());
                if (Files.exists(newPath)) {
                    JOptionPane.showMessageDialog(parent, "<html>Cannot copy <em>" + newPath.getFileName()
                            + "</em><br/>. File with the same name exists.</html>",
                            "File Copy Error", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    Files.copy(origPath, newPath);
                    return newPath.toFile();
                } catch (IOException ex) {
                    UserExceptions.showError(SettingsManager.class, "Copy File Error", ex);
                }
            }
        }

        return null;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */