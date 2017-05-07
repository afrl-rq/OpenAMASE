// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.app;

import java.awt.Dialog;
import java.util.logging.Level;
import javax.swing.JDialog;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 * A tool for giving users visual feedback regarding errors, warnings, and
 * information.
 *
 * @author AFRL/RQQD
 */
public class UserExceptions {

    public static void showError(Object source, String text, Throwable ex) {
        showNotice(source, text, ex, Level.SEVERE);
    }

    public static void showWarning(String text) {
        showNotice(null, text, null, Level.WARNING);
    }

    public static void showWarning(Object source, String text, Throwable ex) {
        showNotice(source, text, ex, Level.WARNING);
    }

    public static void showInformation(String text) {
        showNotice(null, text, null, Level.INFO);
    }

    private static void showNotice(Object source, String text, Throwable ex, Level level) {
        String levelName = level.getName().toLowerCase();
        levelName = Character.toUpperCase(levelName.charAt(0)) + levelName.substring(1);
        
        ErrorInfo info = new ErrorInfo(levelName, text, null, String.valueOf(source), ex, level, null);
        JXErrorPane pane = new JXErrorPane();
        pane.setErrorInfo(info);
        
        JDialog dialog = JXErrorPane.createDialog(null, pane);
        dialog.setTitle(levelName);
        dialog.setAlwaysOnTop(true);
        dialog.setModalityType(Dialog.ModalityType.MODELESS);
        dialog.setVisible(true);


    }

    public static void main(String[] args) {

        showInformation("<html><em>This</em> is some<br/> AMASE info</html>");
        showNotice(String.class, "this is a test", new Exception(), Level.SEVERE);

    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */