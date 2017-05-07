// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.demos;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;



import org.flexdock.demos.util.DemoUtility;
import org.flexdock.util.SwingUtility;

/**
 * Generic launcher for all demos.
 * It allows us to deliver a single entry point demo via a runnable jar or JNLP launched
 * app that runs all of our demonstration apps.
 */
public class AllDemos extends JFrame {

    private static final String[] DEMO_CLASS_NAMES = new String[] {
        "org.flexdock.demos.maximizing.MaximizationDemo",
        "org.flexdock.demos.perspective.PerspectivesDemo",
        "org.flexdock.demos.perspective.XMLPerspectivesDemo",
        "org.flexdock.demos.raw.adapter.AdapterDemo",
        "org.flexdock.demos.raw.border.BorderDemo",
        "org.flexdock.demos.raw.CompoundDemo",
        "org.flexdock.demos.raw.elegant.ElegantDemo",
        "org.flexdock.demos.raw.jmf.JMFDemo",
        "org.flexdock.demos.raw.SimpleDemo",
        "org.flexdock.demos.raw.SplitPaneDemo",
        "org.flexdock.demos.raw.TabbedPaneDemo",
        "org.flexdock.demos.view.ViewDemo",
    };

    public AllDemos() {
        super("FlexDock Demos");

        TreeMap sorted_class_names = new TreeMap();
        for (int i = 0; i < DEMO_CLASS_NAMES.length; i++) {
            String full_class_name = DEMO_CLASS_NAMES[i];
            String just_class_name = full_class_name.substring(full_class_name
                                     .lastIndexOf('.') + 1);
            sorted_class_names.put(just_class_name, full_class_name);
        }

        getContentPane().setLayout(new GridLayout(0, 1, 3, 3));
        for (Iterator iter = sorted_class_names.entrySet().iterator(); iter
                .hasNext();) {
            Map.Entry entry = (Map.Entry) iter.next();

            final String full_class_name = (String) entry.getValue();
            final String just_class_name = (String) entry.getKey();

            JButton button = new JButton(just_class_name);
            button.setToolTipText("Runs " + full_class_name);

            getContentPane().add(button);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    launchClass(full_class_name);
                }
            });
        }

        pack();
    }

    private void launchClass(String full_class_name) {
        Class c;
        try {
            c = Class.forName(full_class_name);
            Method m = c.getMethod("main", new Class[] { String[].class });
            m.invoke(null, new Object[] { null });
        } catch (Throwable t) {
            String message = "Error occurred when calling main(String[]) on class " + full_class_name;
            DemoUtility.showErrorDialog(this, message, t);
        }
    }

    public static void main(String[] args) {
        try {
            final AllDemos a = new AllDemos();
            a.setDefaultCloseOperation(AllDemos.EXIT_ON_CLOSE);
            DemoUtility.setDemoDisableExitOnClose();
            SwingUtility.centerOnScreen(a);
            a.setVisible(true);

            for (int i = 0; i < args.length; i++) {
                final String full_class_name = args[i];
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.launchClass(full_class_name);
                    }
                });
            }
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(-1);
        }
    }
}
