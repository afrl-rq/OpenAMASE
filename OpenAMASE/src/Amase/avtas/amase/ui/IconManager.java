// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.ui;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.EntityConfiguration;
import avtas.amase.AmasePlugin;
import avtas.amase.window.AmaseMenu;
import avtas.app.Context;
import avtas.app.SettingsManager;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.awt.event.ActionEvent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JMenuBar;

/**
 *
 * @author AFRL/RQQD
 */
public class IconManager extends AmasePlugin {

    static final String icons_file = "EntityIcons.xml";
    static final HashMap<Long, BufferedImage> idMap = new HashMap<>();
    static final HashMap<String, BufferedImage> nameMap = new HashMap<>();
    static final HashMap<String, BufferedImage> typeMap = new HashMap<>();

    public IconManager() {

        setPluginName("IconManager");
        Element el = SettingsManager.getAsXml(icons_file);
        if (el != null) {
            loadIcons(el);
        }
    }

    void loadIcons(Element el) {
        el = el.getChild("IconMap");
        if (el != null) {
            for (Element entryEl : el.getChildElements()) {

                long id = XMLUtil.getLongAttr(entryEl, "ID", -1);
                String path = entryEl.getAttr("Path", null);
                String type = entryEl.getAttr("Type", null);
                String name = entryEl.getAttr("Name", null);

                if (path != null) {
                    BufferedImage icon = createIcon(path);
                    if (icon != null) {

                        if (id != -1) {
                            idMap.put(id, icon);
                        }
                        if (type != null) {
                            typeMap.put(type, icon);
                        }
                        if (name != null) {
                            nameMap.put(name, icon);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
        loadIcons(xml);
    }

    @Override
    public void getMenus(JMenuBar menubar) {
        AmaseMenu menu = AmaseMenu.getAmaseMenu(menubar);
        if (menu != null) {
            menu.getPluginMenu(this).add(createFileAction);
        }
    }
    AbstractAction createFileAction = new AbstractAction("Create Icons File") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!SettingsManager.exists(icons_file)) {
                Element el = new Element("EntityIcons");
                el.addComment("Stores a map of entity IDs to icons.  Icons should follow generic path rules.");
                el.addComment("Relative paths are valid entries.");
                el.addComment("Icons can be referenced by entity Type, Name, or ID");
                Element mapEl = el.addElement("IconMap");
                mapEl.addComment("Example: <Entry ID=\"1\" Path=\"path/to/icon/file/in/classpath\" />");
                mapEl.addComment("Example: <Entry Type=\"Car\" Path=\"path/to/icon/file/in/classpath\" />");
                SettingsManager.setFileData(el.toXML().getBytes(), icons_file);
            }
        }
    };

    static BufferedImage createIcon(String path) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            return img;
        } catch (IOException ex) {
            Logger.getLogger(IconManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Returns a registered icon for the given entity, or the default icon if
     * none is found. This first searches by ID, then name, then type.
     */
    public static BufferedImage getIcon(EntityConfiguration ec) {
        if (idMap.containsKey(ec.getID())) {
            return idMap.get(ec.getID());
        }
        if (nameMap.containsKey(ec.getLabel())) {
            return nameMap.get(ec.getLabel());
        }
        if (typeMap.containsKey(ec.getEntityType())) {
            return typeMap.get(ec.getEntityType());
        }
        return IconTools.getDefaultIcon();
    }

    /**
     * Returns a registered icon for the given aircraft, or the default icon is
     * none is found. This first searches by ID, then label.
     */
    public static BufferedImage getIcon(AirVehicleConfiguration avc) {
        if (idMap.containsKey(avc.getID())) {
            return idMap.get(avc.getID());
        }
        if (nameMap.containsKey(avc.getLabel())) {
            return nameMap.get(avc.getLabel());
        }

        return IconTools.getDefaultIcon();
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */