// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.window;

import avtas.app.UserExceptions;
import avtas.amase.AmasePlugin;
import avtas.amase.ui.IconTools;
import avtas.app.Context;
import avtas.app.ContextAdapter;
import avtas.app.SettingsManager;
import avtas.util.WindowUtils;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import avtas.xml.XmlReader;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

/**
 * A menu that includes items for the configuration of AMASE applications
 *
 * @author AFRL/RQQD
 */
public class AmaseMenu extends JMenu {

    JMenu pluginsMenu = new JMenu("Plugins");
    final HashMap<AmasePlugin, JMenu> pluginMenuMap = new HashMap<>();

    public AmaseMenu() {
        //ImageIcon icon = new ImageIcon(AmaseMenu.class.getResource("/resources/amase_menu.png"));
        Image image = IconTools.getFilledImage(IconTools.getDefaultIcon(), 14, 14, 2, getForeground(), getBackground() );
        setIcon(new ImageIcon(image));

        add(aboutMenu());
        add(addPluginMenu());
        add(saveConfigMenu());
        
        add(pluginsMenu);
        add(configFolderMenu());

        add(new JSeparator());
        add(exitMenu());

        Context.getDefaultContext().addObject(new ContextAdapter() {
            @Override
            public void applicationPeerAdded(Object peer) {
                if (peer instanceof AmasePlugin) {
                    getPluginMenu((AmasePlugin) peer);
                }
            }

            @Override
            public void applicationPeerRemoved(Object peer) {
                if (peer instanceof AmasePlugin) {
                    JMenu menu = pluginMenuMap.get(peer);
                    if (menu != null) {
                        pluginsMenu.remove(menu);
                    }
                }
            }
            
            
        });
    }

    @Override
    public void updateUI() {
        super.updateUI();
        Image image = IconTools.getFilledImage(IconTools.getDefaultIcon(), 16, 16, 2, getForeground(), getBackground() );
        setIcon(new ImageIcon(image));
    }
    
    

    /**
     * Returns a menu that can be used by {@link AmasePlugin} objects to add
     * user menus.
     * @param pi plugin for which the menu is requested
     * @return the menu that is associated with the given plugin.
     */
    public JMenu getPluginMenu(final AmasePlugin pi) {

        JMenu piMenu = pluginMenuMap.get(pi);
        if (piMenu != null) {
            return piMenu;
        }
        else {
            piMenu = new JMenu(pi.getPluginName());
            piMenu.add(new AbstractAction("Settings") {
                @Override
                public void actionPerformed(ActionEvent e) {

                    JPanel panel = pi.getSettingsPanel();
                    if (panel != null) {
                        Frame owner = JOptionPane.getFrameForComponent(AmaseMenu.this);
                        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
                        JDialog dialog = new JDialog(owner, pi.getPluginName() + " Settings");
                        dialog.setModalityType(Dialog.ModalityType.MODELESS);
                        dialog.setResizable(true);
                        dialog.add(panel);

                        dialog.pack();
                        dialog.setLocationRelativeTo(AmaseMenu.this.getTopLevelAncestor());
                        dialog.setVisible(true);
                    }
                }
            });
            piMenu.add(new AbstractAction("Remove Plugin") {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Frame owner = JOptionPane.getFrameForComponent(AmaseMenu.this);
                    int ans = JOptionPane.showConfirmDialog(owner, "Remove Plugin " + pi.getPluginName() + "?", "Confirm Removal", JOptionPane.YES_NO_OPTION );
                    if (ans == JOptionPane.YES_OPTION) {
                        
                        Context.getDefaultContext().removeObject(pi);
                    }
                }
            }); 
            pluginsMenu.add(piMenu);
            pluginMenuMap.put(pi, piMenu);
        }

        return piMenu;
    }

    AbstractAction versionMenu() {
        return new AbstractAction("Version") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Frame root = JOptionPane.getFrameForComponent(AmaseMenu.this);
                    File f = new File("./buildinfo.xml");
                    if (!f.exists()) {
                        JOptionPane.showMessageDialog(root, "No build information available");
                        return;
                    }
                    Element buildEl = Element.read(f);
                    Element dateEl = XMLUtil.getChild(buildEl, "Date");
                    if (dateEl != null) {
                        JOptionPane.showMessageDialog(root, "Build Date: " + dateEl.getText());
                    }

                } catch (Exception ex) {
                    Logger.getLogger(AmaseMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }
    
    AbstractAction addPluginMenu() {
        final String helpString = "<html><center>Enter the name of a Plugin using the full java class name.<br/><em>Example: avtas.amase.ExamplePlugin</em></center></html>";
        
        return new AbstractAction("Add Plugin") {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                String ans = JOptionPane.showInputDialog(JOptionPane.getFrameForComponent(AmaseMenu.this), helpString, 
                        "Add AMASE Plugin", JOptionPane.PLAIN_MESSAGE);
                if (ans != null) {
                    try {
                        Class c = Class.forName(ans);
                        if (AmasePlugin.class.isAssignableFrom(c)) {
                            Element el = new Element("Plugin");
                            el.setAttribute("Class", c.getCanonicalName());
                            Context.getDefaultContext().addPlugin(el);
                        }
                        else {
                            UserExceptions.showError(this, ans + " is not an AMASE Plugin. ", null);
                        }
                    } catch (ClassNotFoundException  ex) {
                        UserExceptions.showError(this, "Cannot create AMASE Plugin from " + ans , ex);
                    }
                }
            }
        };
    }
    
    AbstractAction saveConfigMenu() {
        return new AbstractAction("Save Plugin Info") {

            @Override
            public void actionPerformed(ActionEvent e) {
                Element el = Context.getDefaultContext().getConfiguration();
                if (el != null) {
                    File file = SettingsManager.getFile("Plugins.xml");
                    el.toFile(file);
                }
            }
        };
    }

    AbstractAction configurePluginsMenu() {
        return new AbstractAction("Configure Plugins") {
            @Override
            public void actionPerformed(ActionEvent e) {

                JLabel panel = new JLabel("Implement me!");
                JDialog dialog = new JDialog();
                dialog.setModal(true);
                dialog.setResizable(true);
                System.out.println(AmaseMenu.this.getTopLevelAncestor());
                dialog.add(panel);

                dialog.pack();
                dialog.setLocationRelativeTo(AmaseMenu.this.getTopLevelAncestor());
                dialog.setVisible(true);
            }
        };
    };

    
    AbstractAction configFolderMenu() {
        return new AbstractAction("Open Config Folder") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(SettingsManager.getSettingsDirectory().toFile().getCanonicalFile());
                } catch (IOException ex) {
                    UserExceptions.showError(this, "Cannot open configuration directory", ex);
                }
            }
        };
    }
    
    AbstractAction exitMenu() {
        return new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Context.getDefaultContext().requestShutdown();
            }
        };
    }
    
    AbstractAction aboutMenu() {
        return new AbstractAction("About AMASE") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JEditorPane pane = new JEditorPane(getClass().getResource("/About.html"));
                    pane.setEditable(false);
                    Element el = XmlReader.readDocument(getClass().getResourceAsStream("/amase_dk_version.xml"));
                    
                    JPanel topPanel = new JPanel(new BorderLayout(5, 5));
                    topPanel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
                    
                    topPanel.add(new JLabel("Based on " + XMLUtil.getValue(el, "title", "") + " " + XMLUtil.getValue(el, "version", "")), 
                            BorderLayout.NORTH);
                    topPanel.add(new JScrollPane(pane), BorderLayout.CENTER);
                    topPanel.setPreferredSize(new Dimension(550, 480));
                    
                    WindowUtils.showPlainDialog(JOptionPane.getFrameForComponent(AmaseMenu.this), topPanel, "About AMASE");
                    
                } catch (Exception ex) {
                    Logger.getLogger(AmaseMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }
    
    

    /**
     * Returns the AMASE menu on the given menu bar, or null if no AmaseMenu has
     * been added.
     *
     * @param menubar
     */
    public static AmaseMenu getAmaseMenu(JMenuBar menubar) {
        for (int i = 0; i < menubar.getMenuCount(); i++) {
            if (menubar.getMenu(i) instanceof AmaseMenu) {
                return (AmaseMenu) menubar.getMenu(i);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        JMenuBar menubar = new JMenuBar();
        f.setJMenuBar(menubar);
        menubar.add(new AmaseMenu());
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */