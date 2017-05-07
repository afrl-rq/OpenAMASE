// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.demos.util;

import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DemoUtility {
    public static void setDemoDisableExitOnClose() {
        System.setProperty("disable.system.exit", "true");
    }

    public static void setCloseOperation(JFrame f) {
        if (!Boolean.getBoolean("disable.system.exit"))
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        else
            f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    /**
     * Opens a JOptionPane with the error message and formatted stack trace of the throwable in a scrollable text area.
     *
     * @param c
     *            optional argument for parent component to open modal error
     *            dialog relative to
     * @param error_message
     *            short string description of failure, must be non-null
     * @param t
     *            the throwable that's being reported, must be non-null
     */
    public static void showErrorDialog(Component c, String error_message,
                                       Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println(error_message);
        pw.print("Exception is: ");
        t.printStackTrace(pw);
        pw.flush();

        JTextArea ta = new JTextArea(sw.toString(), 15, 60);
        JScrollPane sp = new JScrollPane(ta);
        JOptionPane.showMessageDialog(c, sp, error_message,
                                      JOptionPane.ERROR_MESSAGE);
    }
}
