// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.setup;

import avtas.amase.AmasePlugin;
import avtas.amase.scenario.ScenarioEvent;
import avtas.app.Context;
import avtas.util.WindowUtils;
import avtas.xml.ui.XMLComposer;
import avtas.xml.Element;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 *  Sets up a menu for editing files that are links from the main scenario file.
 * 
 * @author AFRL/RQQD
 */
public class LinkedFileMenu extends AmasePlugin {

    JMenu linkedFileMenu = new JMenu("Edit Linked Files...");
    private String[] args = new String[]{};

    public LinkedFileMenu() {
    }

    @Override
    public void getMenus(JMenuBar menubar) {
        JMenu editMenu = WindowUtils.getMenu(menubar, "Edit");
        editMenu.add(linkedFileMenu);
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof ScenarioEvent) {
            linkedFileMenu.removeAll();
            ScenarioEvent se = (ScenarioEvent) event;
            setupLinks(se.getSourceFile(), se.getXML());
        }
    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
        this.args = cmdParams;
    }  
    

    private void setupLinks(File sourceFile, Element xml) {
        if (xml.getName().equals("Link")) {
            try {
                final File linkFile = new File(sourceFile.getParent(), xml.getAttribute("Source"));
                if (linkFile.exists()) {
                    linkedFileMenu.add(new AbstractAction(linkFile.getName()) {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JFrame f = XMLComposer.createComposerWindow(linkFile);
                            f.pack();
                            f.setVisible(true);
                        }
                    });
                }
            } catch (Exception ex) {
                System.err.println("Bad Source link in file.  " + ex.getMessage());
            }
        }
        for (Element e : xml.getChildElements()) {
            setupLinks(sourceFile, e);
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */