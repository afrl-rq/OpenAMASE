// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.Objects;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author AFRL/RQQD
 */
public class WindowUtils {

    /**
     * Shows a plain dialog (no buttons) with the specified content.
     *
     * @param owner the component that will be blocked by this dialog (can be null)
     * @param content the content to display
     * @param title dialog window title
     */
    public static void showPlainDialog(Component owner, Object content, String title) {
        showPlainDialog(owner, content, title, true);
    }

    /**
     * Shows a plain dialog (no buttons) with the specified content.
     *
     * @param owner the component that will be blocked by this dialog (can be null)
     * @param content the content to display
     * @param title dialog window title
     * @param isModal flag denoting whether the dialog should block its parent window
     */
    public static void showPlainDialog(Component owner, Object content, String title, boolean isModal) {
        JOptionPane pane = new JOptionPane(content, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, new Object[]{}, null);
        JDialog dialog = pane.createDialog(owner, title);

        dialog.setResizable(true);
        dialog.setModal(isModal);
        dialog.pack();
        dialog.setVisible(true);
    }
    
    /**
     * Shows a dialog with "OK" and "Cancel" buttons and the specified content.
     *
     * @param owner the component that will be blocked by this dialog (can be null)
     * @param content the content to display
     * @param title dialog window title
     * @return {@link JOptionPane#OK_OPTION} or {@link JOptionPane#CANCEL_OPTION} depending on user selection
     */
    public static int showOKCancelDialog(Component owner, Object content, String title) {
        JOptionPane pane = new JOptionPane(content, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = pane.createDialog(owner, title);

        dialog.setResizable(true);
        dialog.setVisible(true);

        return Integer.valueOf(JOptionPane.OK_OPTION).equals(pane.getValue()) ? JOptionPane.OK_OPTION : JOptionPane.CANCEL_OPTION;
    }
   

    public static JFrame showApplicationWindow(JComponent contents) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(contents, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Returns a menu from the given menuBar. If the menu is not found, a new
     * one is created and added to the menubar. name is not found.
     * @param menuBar menu bar from which to obtain a menu
     * @param menuName name of the menu to obtain
     * @return the menu with the given name, or a new menu if a menu cannot be found.
     */
    public static JMenu getMenu(JMenuBar menuBar, String menuName) {
        if (menuBar == null) {
            return null;
        }
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            if (menu != null && Objects.equals(menu.getText(), menuName)) {
                return menu;
            }
        }
        JMenu menu = new JMenu(menuName);
        menuBar.add(menu);
        return menu;
    }

    /**
     * A file chooser that automatically puts a default extension on files
     * without them, and also asks the user to confirm overwrite actions.
     *
     * @param defaultExtension the extension to put on files if one is not
     * chosen during the selection process. If this is null or empty, no
     * extension will be added.
     * @param filters An optional set of filters to be added to the chooser to
     * limit the selection.
     * @return a chooser configured for save operations.
     */
    public static JFileChooser getFilteredChooser(final String defaultExtension, FileNameExtensionFilter... filters) {
        JFileChooser chooser = new JFileChooser() {
            @Override
            public void approveSelection() {
                if (defaultExtension != null && !defaultExtension.isEmpty()) {
                    String ext = defaultExtension.startsWith(".") ? defaultExtension : "." + defaultExtension;
                    if (getSelectedFile() != null && !getSelectedFile().getName().matches(".+\\..+")) {
                        setSelectedFile(new File(getSelectedFile().getParentFile(), getSelectedFile().getName() + ext));
                    }
                }
                if (getSelectedFile() != null && getSelectedFile().exists()) {
                    int ans = JOptionPane.showConfirmDialog(this, "File Exists.  Overwrite?", "Overwrite", JOptionPane.OK_CANCEL_OPTION);
                    if (ans == JOptionPane.OK_OPTION) {
                        super.approveSelection();
                    }
                }
                else {
                    super.approveSelection();
                }
            }
        };

        for (FileNameExtensionFilter filter : filters) {
            chooser.addChoosableFileFilter(filter);
        }
        if (filters.length > 0) {
            chooser.setFileFilter(filters[0]);
        }


        return chooser;
    }

    /** Copies contents as a string to the system clipboard */
    public static void copyToClipboard(Object contents) {
        Transferable xfer = new StringSelection(String.valueOf(contents));
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(xfer, null);
    }
    
    /** Gets contents from the System clipboard as a String. Returns null if there
      is no data on the clipboard or an error occurs. 
      */
    public static String copyFromClipboard() {
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (Exception ex) {
            return null;
        }
    }

    public static void main(String[] args) {
        showPlainDialog(null, "this is a test", "Test");
        System.out.println("done.");

        JFileChooser chooser = getFilteredChooser("xml", new FileNameExtensionFilter("XML Files", "xml"));
        chooser.showSaveDialog(null);
        System.out.println(chooser.getSelectedFile());
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */