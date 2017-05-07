// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.swing;

import avtas.properties.Editors;
import avtas.properties.UserProperty;
import avtas.util.WindowUtils;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.prompt.BuddyButton;
import org.jdesktop.swingx.prompt.BuddySupport;

/**
 * A GUI that lets the user select a file or folder.
 *
 * @author AFRL/RQQD
 */
public class FileSelector extends JXTextField {
       

    JFileChooser chooser = new JFileChooser();
    JPanel pathTypePanel = new JPanel();
    JRadioButton absbut, relbut;
    int fileType = JFileChooser.FILES_AND_DIRECTORIES;
    
    
    /**
     * Creates a file selector for files only and no starting file.
     */
    public FileSelector() {
        this(null, JFileChooser.FILES_ONLY);
    }

    /**
     * Creates a new File Selector widget.  
     * @param startingFile the starting file or directory to list in the field.
     * @param validType Use values from  {@link JFileChooser}: {@link JFileChooser#FILES_AND_DIRECTORIES}, {@link JFileChooser#FILES_ONLY},
     * or {@link JFileChooser#DIRECTORIES_ONLY}.
     */
    public FileSelector(File startingFile, int validType) {
        
        setFileType(validType);
        setFile(startingFile);

        BuddyButton clearButton = new BuddyButton();
        clearButton.setIcon(new ImageIcon(Editors.class.getResource("/resources/Close16.png")));
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setText("");
            }
        });
        
        BuddyButton chooseButton = new BuddyButton();
        if (fileType == JFileChooser.DIRECTORIES_ONLY) {
            chooseButton.setIcon(new ImageIcon(Editors.class.getResource("/resources/Folder16.png")));
        } else {
            chooseButton.setIcon(new ImageIcon(Editors.class.getResource("/resources/File16.png")));
        }

        chooseButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int ans = chooser.showDialog(FileSelector.this, "Select File");
                if (ans == JFileChooser.APPROVE_OPTION) {
                    if (relbut.isSelected()) {
                        setText(Paths.get(".").toAbsolutePath().relativize(chooser.getSelectedFile().toPath()).toString());
                    } else {
                        setText(chooser.getSelectedFile().toString());
                    }
                }
            }
        });

        addBuddy(chooseButton, BuddySupport.Position.RIGHT);
        addBuddy(clearButton, BuddySupport.Position.RIGHT);

        pathTypePanel.setLayout(new BoxLayout(pathTypePanel, BoxLayout.Y_AXIS));
        pathTypePanel.setBorder(new TitledBorder("File Type"));
        ButtonGroup bg = new ButtonGroup();
        absbut = new JRadioButton("Absolute");
        relbut = new JRadioButton("Relative");
        bg.add(absbut);
        bg.add(relbut);
        absbut.setSelected(true);
        pathTypePanel.add(absbut);
        pathTypePanel.add(relbut);

        chooser.setAccessory(pathTypePanel);
        chooser.setFileSelectionMode(fileType);
        
        
        
    }
    
    public File getFile() {
        return getText().isEmpty() ? null : new File(getText());
    }
    
    public void setFile(File file) {
        if (file == null) {
            setText("");
        }
        else if (file.isDirectory() && fileType == JFileChooser.FILES_ONLY) {
            setStartingDirectory(file);
        }
        else {
            setText(file.toString());
        }
    }
    
    public void setStartingDirectory(File dir) {
        dir = dir == null ? new File(".") : dir.isDirectory() ? dir : dir.getParentFile();
        chooser.setCurrentDirectory(dir);
    }
    
    /**
     * Sets the type of files that can be selected.  Use values from 
     * {@link JFileChooser}: {@link JFileChooser#FILES_AND_DIRECTORIES}, {@link JFileChooser#FILES_ONLY},
     * or {@link JFileChooser#DIRECTORIES_ONLY}.
     * @param type 
     */
    public void setFileType(int type) {
        this.fileType = type;
    }
    
    
    public static void main(String[] args) {
        WindowUtils.showApplicationWindow(new FileSelector(new File("c:/users/matt/desktop/test.xml"), JFileChooser.DIRECTORIES_ONLY));
    }
    
    
}



/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */