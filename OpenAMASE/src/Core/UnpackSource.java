// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;


/**
 * Unpacks files from a Jar to a directory selected by the user.  To make this automatically run when double clicking
 * a JAR, copy this class to the desired JAR and set it as the main class in the manifest.  This program
 * detects which jar file is its owner and unpacks that jar.  All contents except ".class" files are copied.
 * @author AFRL/RQQD
 */
public class UnpackSource {
    
    static String infoString = new String("<html>This utility unpacks the files <br/>"
            + "from this jar file and stores them in a<br/> "
            + "location chosen by the user.  Select a directory<br/>"
            + "for saving files</html>");
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new UnpackSource().unpack();
        } catch (Exception ex) {
            Logger.getLogger(UnpackSource.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public void unpack(){

        CodeSource cs = this.getClass().getProtectionDomain().getCodeSource();

        URL loc = cs.getLocation();

        try {
            if (loc.getProtocol().equals("file") && loc.getPath().toLowerCase().endsWith(".jar")) {

                File srcFile = new File(loc.getPath());

                JFileChooser chooser = new JFileChooser();
                chooser.setAccessory(new JLabel(infoString));
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int ans = chooser.showSaveDialog(null);
                if (ans == JFileChooser.APPROVE_OPTION) {

                    File outDir = chooser.getSelectedFile();
                    outDir.mkdirs();

                    ZipFile jarFile = new ZipFile(srcFile);
                    for (Enumeration<? extends ZipEntry> en = jarFile.entries(); en.hasMoreElements();) {
                        ZipEntry e = en.nextElement();
                        InputStream is = jarFile.getInputStream(e);
                        File outFile = new File(outDir, e.getName());
                        if (e.isDirectory()) {
                            outFile.mkdirs();
                        }
                        // don't unpack class files
                        else if (!e.getName().endsWith(".class")){
                            OutputStream os = new FileOutputStream(outFile);

                            byte[] buf = new byte[2048];
                            while (is.available() > 0) {
                                int read = is.read(buf);
                                os.write(buf, 0, read);
                            }
                            is.close();
                            os.close();
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Copied " + loc.getFile() + " files to " + outDir.getPath());
                    return;
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error getting source.  Is this program run from a JAR file?");
        }
        

    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */