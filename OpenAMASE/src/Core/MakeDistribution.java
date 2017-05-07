// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import avtas.xml.Element;
import avtas.xml.XmlWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates a zip file that contains all of the contents of the given directory.  The zip is labeled as: <Archive Name>-YYYYMMDD_HHMMZ.zip where
 * "Archive Name" is the designated label for the archive and the time/date is in universal time coordinates.  The
 * process is invoked using the following command:<br/>
 *
 * MakeDistribution <Archive Name> [list of contents]<br/>
 *
 * the list of contents is a list of files or folders that are to be added to the archive.  If folders are listed, all
 * contents are added to the archive recursively.<br/>
 *
 * Note that files and folders are added according to their path name.  Therefore, MakeDistribution should be run in the
 * top level of the directory that contains all of the desired contents.  Relative paths (such as "..") and absolute paths
 * (such as "/folder_name") will be added to the archive according to the path specified.  This can cause problems during
 * zip deflation.<br/>
 *
 * This process will also denote the build date and time in a file labeled "buildinfo.xml" that can be used by applications
 * to present the build information to the user.
 *
 * @author AFRL/RQQD
 */
public class MakeDistribution {

    static String appName = "Default";
    static Date date = new Date();
    static DecimalFormat numberFormat = new DecimalFormat("00");

    /** Creates a zip archive with the requested name and contents.
     *
     * @param args A list of arguments.  See the class  
     */
    public static void makeDistribution(String[] args) {
        main(args);
    }

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                appName = args[1];
            }
            else {
                System.out.println("Usage: <Directory to copy> <Archive Name> [list of files and or directories to exclude]");
                System.out.println("Press any key to quit.");
                System.in.read();
            }

            updateBuildInfo();
            
            File currentDir = new File(args[0]);
            List<File> fileList = new ArrayList(Arrays.asList(currentDir.listFiles()));
            for (File f : fileList) {
                System.out.println("file: " + f.getName());
            }
            
            
            for (int i=2; i<args.length; i++) {
                String arg = args[i];
                for (int j = 0; j < fileList.size(); j++) {
                    if (fileList.get(j).getName().equals(arg)) {
                        fileList.remove(j);
                        System.out.println("excluding " + arg);
                        break;
                    } 
                }
            }

            date = new Date();
            File file = new File(appName + "-" + getDateString() + ".zip");
            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(file));
            
            for (File f : fileList) {
                zipFile(f.getName(), zout);
            }

            zout.close();

            System.out.println("Created Zip file at " + file.getAbsolutePath());

        } catch (IOException ex) {
            Logger.getLogger(MakeDistribution.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void updateBuildInfo() {

        Element el = new Element("Build");
        DateFormat format = DateFormat.getDateTimeInstance();
        el.add(new Element("Date", format.format(date)));
        el.toFile(new File("buildinfo.xml"));

    }

    public static String getDateString() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return c.get(Calendar.YEAR) + numberFormat.format(c.get(Calendar.MONTH)+1) + numberFormat.format(c.get(Calendar.DAY_OF_MONTH))
                + "_" + numberFormat.format(c.get(Calendar.HOUR_OF_DAY)) + numberFormat.format(c.get(Calendar.MINUTE)) + "Z";
    }


    public static void zipFile(String file, ZipOutputStream zos) {
        File f = new File(file);
        if (f.isDirectory()) {
            zipDir(f, zos);
        }
        else if (f.exists()) {
            zipFile(f, zos);
        }
        else {
            System.out.println("Cannot find " + f.getPath());
        }
    }

    public static void zipDir(File zipDir, ZipOutputStream zos) {
        try {

            System.out.println("adding folder " + zipDir);

            //get a listing of the directory content
            String[] dirList = zipDir.list();

            //loop through dirList, and zip the files
            for (int i = 0; i < dirList.length; i++) {

                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    //if the File object is a directory, call this
                    //function again to add its content recursively
                    File filePath = new File(f.getPath());
                    zipDir(filePath, zos);
                    //loop again
                    continue;
                }
                zipFile(f, zos);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void zipFile(File f, ZipOutputStream zos) {
        int bytesIn = 0;
        FileInputStream fis = null;
        byte[] readBuffer = new byte[2156];
        try {
            fis = new FileInputStream(f);
            ZipEntry anEntry = new ZipEntry(f.getPath());
            System.out.println("adding " + f.getPath() );
            try {
                //place the zip entry in the ZipOutputStream object
                zos.putNextEntry(anEntry);
            } catch (IOException ex) {
                Logger.getLogger(MakeDistribution.class.getName()).log(Level.SEVERE, null, ex);
            }
            //now write the content of the file to the ZipOutputStream
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            //close the Stream
            fis.close();
        } catch (Exception ex) {
            Logger.getLogger(MakeDistribution.class.getName()).log(Level.SEVERE, null, ex);
        } 

    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */