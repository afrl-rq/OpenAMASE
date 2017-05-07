// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package amase.cmasi.converter;

import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Converts a scenario from CMASI 2 standard to CMASI 3.
 *
 * @author AFRL/RQQD
 */
public class ScenarioConverter {

    /**
     * All converter functions are annotated with this interface
     */
    @Retention(RetentionPolicy.RUNTIME)
    @interface ConverterFunction {
    };

    public static void runConverter(File input, File output) {
        System.out.println("converting " + input.getAbsolutePath());
        if (output == null) {
            File outputFolder = new File(input.getParentFile(), "converted_CMASIv3");
            if (!outputFolder.isDirectory()) {
                outputFolder.mkdir();
            }

            output = new File(outputFolder, input.getName());
        }

        Element scenarioEl = Element.read(input);
        if (scenarioEl == null) {
            return;
        }

        List<Method> converterFuncs = new ArrayList<>();
        Method[] methods = ScenarioConverter.class.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getAnnotation(ConverterFunction.class) != null) {
                converterFuncs.add(m);
            }
        }

        Element scenEventListEl = XMLUtil.getChild(scenarioEl, "ScenarioEventList");
        processElement(scenEventListEl, converterFuncs);

        scenarioEl.toFile(output);
        System.out.println("saved to " + output.getAbsolutePath());
        System.out.println("");
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ScenarioConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (args.length == 0) {
            final JFileChooser inputChooser = new JFileChooser(".");
            inputChooser.setDialogTitle("Choose scenario file(s) or directory of files to convert");
            inputChooser.setMultiSelectionEnabled(true);
            inputChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int ans = inputChooser.showOpenDialog(null);
            if (ans == JFileChooser.APPROVE_OPTION) {
                for (File f : inputChooser.getSelectedFiles()) {
                    processFile(f);
                }
            }
        }
    }

    protected static void processFile(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                processFile(child);
            }
        } else {
            runConverter(file, null);
        }
    }

    public static void processElement(Element el, List<Method> methods) {
        for (Method m : methods) {
            try {
                m.invoke(null, el);
                for (Element child : el.getChildElements()) {
                    processElement(child, methods);
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(ScenarioConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @ConverterFunction
    public static void convertLocations(Element el) {
        if (el.getName().equals("Location2D") && el.getAttribute("Series").equals("CMASI")) {
            el.setName("Location3D");
        }
    }

    @ConverterFunction
    public static void convertVehicleIDs(Element el) {
        if ("CMASI".equals(el.getAttribute("Series")) && (el.getName().equals("AirVehicleState") || el.getName().equals("AirVehicleConfiguration"))) {
            Element idEl = el.getChild("VehicleID");
            if (idEl != null) {
                idEl.setName("ID");
                System.out.println("converted aircraft ID field for " + idEl.getText());
            }
            Element posEl = el.getChild("Position");
            if (posEl != null) {
                posEl.setName("Location");
                System.out.println("converted \"Position\" to \"Location\" for aircraft " + idEl.getText());

            }
        }
    }

    @ConverterFunction
    public static void convertEntityInfo(Element el) {
        if ("ENTITIES".equals(el.getAttribute("Series")) && (el.getName().equals("EntityState") || el.getName().equals("EntityConfiguration"))) {
            el.setAttribute("Series", "CMASI");
            Element idEl = el.getChild("EntityID");
            if (idEl != null) {
                idEl.setName("ID");
            }
            Element labelEl = el.getChild("Name");
            if (labelEl != null) {
                labelEl.setName("Label");
            }

            System.out.println("converted entity " + idEl.getAttr("ID", null) + " to CMASI 3 format.");
        }
    }

    @ConverterFunction
    public static void convertTime(Element el) {
        if (el.getName().contains("Time")) {
            double dval = el.getDoubleValue(0);
            long lval = (long) (1000 * dval);
            el.setText(String.valueOf(lval));
            System.out.println("converted time from seconds to milliseconds");
        }
    }

    @ConverterFunction
    public static void convertGimbalPayloadList(Element el) {
        if (el.getName().equals("ContainedSensorList") && el.getParent().getName().equals("GimbalConfiguration")) {
            el.setName("ContainedPayloadList");
            System.out.println("Converted ContainedSensorList to ContainedPayloadList");
        }
    }

    @ConverterFunction
    public static void convertFollowPathCommand(Element el) {
        if (el.getAttr("Series", "").equals("ENTITIES") && el.getName().equals("FollowPathCommand")) {
            el.setAttribute("Series", "CMASI");
            System.out.println("Converted FollowPathCommand from ENTITIES to CMASI MDM");
        }
    }

    @ConverterFunction
    public static void convertPathWaypoint(Element el) {
        if (el.getAttr("Series", "").equals("ENTITIES") && el.getName().equals("PathWaypoint")) {
            el.setAttribute("Series", "CMASI");
            System.out.println("Converted FollowPathCommand from ENTITIES to CMASI MDM");
        }
    }

    @ConverterFunction
    public static void convertLoiterDuration(Element el) {
        if (el.getName().equals("LoiterAction")) {
            Element durationEl = el.getChild("Duration");
            if (durationEl != null) {
                double dval = el.getDoubleValue(0);
                long lval = (long) (1000 * dval);
                el.setText(String.valueOf(lval));
            }
            System.out.println("Converted time value in Loiter action from seconds to milliseconds");
        }
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */