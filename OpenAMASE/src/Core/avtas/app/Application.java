// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.app;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import avtas.xml.Element;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;

;

/**
 * The base class for an ACES application. A context for the application is
 * created and initialized using the XML configuration file.
 *
 * @author AFRL/RQQD
 */
public class Application {

    private static String CONFIG_STR = "--config";
    private static String LAF_STR = "--UseLookAndFeel";
    private static String HELP_STR = "--help";
    private static String HELPSTR_TEXT = "Options:\n\n"
            + CONFIG_STR + "<config dir>  Load a AMASE application from a config set \n"
            + LAF_STR + "   Implements the default Java Look and Feel\n";

    /**
     * Creates the base application taking configuration strings as input. If no
     * configuration strings are supplied, a help string is printed. If the first
     * string given is "--config", the second string is read in and loaded as
     * the configuration directory. The
     * <code>.createApplication</code> which takes a configuration directory as input
     * is then called. If the first string is "--help", the
     * <code>helpString</code> is printed to the screen. The context is then
     * initialized.
     *
     * @param args The configuration strings.
     * @return The context that is created for the application.
     */
    public static Context createApplication(String[] args) {

        Context context = null;

        if (args.length == 0) {
            System.out.println(HELPSTR_TEXT);
            return null;
        }
        else {

            String value = getParamValue(LAF_STR, args);
            if (value != null) {
                loadLookAndFeel(value);
            }
            else {
                loadLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }


            value = getParamValue(CONFIG_STR, args);
            if (value != null) {
                try {
                    File file = new File(value);
                    context = createApplication(file, args);
                } catch (Exception ex) {
                    UserExceptions.showError(Application.class, "<html><p>XML Error. Cannot Configure Application</p><p>"
                            + ex.getMessage() + "</p></html>", ex);
                }
            }
            
            value = getParamValue(HELP_STR, args);
            if (value != null) {
                System.out.println(HELPSTR_TEXT);
            }
        }
        return context;
    }

    /**
     * Creates and initializes an application context given an XML configuration
     * file.
     *
     * @param configDir The AMASE configuration directory.
     * @return The context created for the application.
     */
    public static Context createApplication(File configDir, String[] args) {
        try {
            if (!configDir.isDirectory()) {
                UserExceptions.showError(Application.class, "Cannot load application.  Config directory does not exist", null);
                return null;
            }
            SettingsManager.setSettingsDirectory(configDir.getAbsolutePath());
            Context context = Context.getDefaultContext();
            
            context.addObject(AppEventManager.getDefaultEventManager());
            
            
            SplashUpdater updater = new SplashUpdater();
            
            Element pluginEl = SettingsManager.getAsXml("Plugins.xml");
            if (pluginEl != null) {
                context.initialize(pluginEl, args);
            }
            else {
                UserExceptions.showError(Application.class, "Cannot load application. Plugins file not found.", null);
            }
            return context;
        } catch (Exception ex) {
            UserExceptions.showError(Application.class, "<html><p>XML Error. Cannot Configure Application</p><p>"
                    + ex.getMessage() + "</p></html>", ex);
            return null;
        }
    }

    protected static void loadLookAndFeel(String lafClassName) {
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel(lafClassName);
        } catch (Exception ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates a new Java process based on the parameters of the currently
     * running process. The current java VM options, library path, and classpath
     * are given to the new process.
     *
     * @param args Arguments to pass to the main application
     */
    public static Process createNewProcess(String[] args) {
        try {
            List<String> cmdList = new ArrayList<String>();
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();

            String processName = File.separatorChar + "bin" + File.separatorChar + "java";

            cmdList.add(System.getProperty("java.home") + processName);

            // add all of the JVM input args, like xms, xmx
            cmdList.addAll(runtime.getInputArguments());

            // the system expects classpath to be two arguments
            cmdList.add("-cp");
            cmdList.add(runtime.getClassPath());

            // the system expects this to be one argument in the command line
            cmdList.add("-Djava.library.path=" + runtime.getLibraryPath());


            cmdList.add(Application.class.getName());

            for (String arg : args) {
                cmdList.add(arg);
            }

            for (String arg : cmdList) {
                System.out.print(arg + " ");
            }


            ProcessBuilder pb = new ProcessBuilder(cmdList);
            final Process p = pb.start();

            new Thread() {
                public void run() {
                    try {
                        while (true) {
                            if (p.getErrorStream().available() > 0) {
                                System.err.print((char) p.getErrorStream().read());
                            }
                            if (p.getInputStream().available() > 0) {
                                System.out.print((char) p.getInputStream().read());
                            }
                            sleep(1);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.start();

            return p;

        } catch (IOException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    protected static int findParam(String param, String[] params) {
        for (int i = 0; i < params.length; i++) {
            if (params[i].equals(param)) {
                return i;
            }
        }
        return -1;
    }

    protected static String getParamValue(String param, String[] params) {
        int index = findParam(param, params);
        if (index != -1 && index < params.length - 1) {
            return params[index + 1];
        }
        return null;
    }

    /**
     * Main method for the Application class. Sets the look and feel for the gui
     * and creates a
     * <code>Runnable</code> object for the event dispatching thread.
     *
     * @param args The configuration strings.
     */
    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createApplication(args);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */