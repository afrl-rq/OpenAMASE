// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 15, 2005
 */
package org.flexdock.docking.drag.effects;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingPort;
import org.flexdock.util.OsInfo;
import org.flexdock.util.ResourceManager;
import org.flexdock.util.Utilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Christopher Butler
 */
public class EffectsManager {
    private static final String CONFIG_URI = "org/flexdock/docking/drag/effects/drag-effects.xml";
    private static final Object LOCK = new Object();

    private static DragPreview DEFAULT_PREVIEW;
    private static DragPreview CUSTOM_PREVIEW;
    private static RubberBand DEFAULT_RUBBERBAND;
    private static RubberBand CUSTOM_RUBBERBAND;

    static {
        prime();
    }

    public static void prime() {
        Document config = ResourceManager.getDocument(CONFIG_URI);
        DEFAULT_PREVIEW = loadDefaultPreview(config);
        DEFAULT_RUBBERBAND = loadSystemRubberband(config);
    }

    public static RubberBand getRubberBand() {
        synchronized(LOCK) {
            return CUSTOM_RUBBERBAND==null? DEFAULT_RUBBERBAND: CUSTOM_RUBBERBAND;
        }
    }

    public static DragPreview getPreview(Dockable dockable, DockingPort target) {
        synchronized(LOCK) {
            return CUSTOM_PREVIEW==null? DEFAULT_PREVIEW: CUSTOM_PREVIEW;
        }
    }

    public static RubberBand setRubberBand(String implClass) {
        RubberBand rb = createRubberBand(implClass);
        if(implClass!=null && rb==null)
            return null;

        setRubberBand(rb);
        return rb;
    }

    public static void setRubberBand(RubberBand rubberBand) {
        synchronized(LOCK) {
            CUSTOM_RUBBERBAND = rubberBand;
        }
    }

    public DragPreview setPreview(String implClass) {
        DragPreview preview = createPreview(implClass);
        if(implClass!=null && preview==null)
            return null;

        setPreview(preview);
        return preview;
    }

    public static void setPreview(DragPreview preview) {
        synchronized(LOCK) {
            CUSTOM_PREVIEW = preview;
        }
    }

    private static final Document loadConfig() {
        return ResourceManager.getDocument(CONFIG_URI);
    }

    private static RubberBand createRubberBand(String implClass) {
        boolean failSilent = !Boolean.getBoolean(RubberBand.DEBUG_OUTPUT);
        return (RubberBand)Utilities.createInstance(implClass, RubberBand.class, failSilent);
    }

    private static DragPreview createPreview(String implClass) {
        return (DragPreview)Utilities.createInstance(implClass, DragPreview.class);
    }



    private static HashMap loadRubberBandInfoByOS(Document config) {
        HashMap map = new HashMap();

        Element root = (Element)config.getElementsByTagName("rubber-bands").item(0);
        map.put("default", root.getAttribute("default"));
        NodeList nodes = root.getElementsByTagName("os");

        for(int i=0; i<nodes.getLength(); i++) {
            Element osElem = (Element)nodes.item(i);
            String osName = osElem.getAttribute("name");
            NodeList items = osElem.getElementsByTagName("rubber-band");
            ArrayList classes = new ArrayList(items.getLength());
            map.put(osName, classes);
            for(int j=0; j<items.getLength(); j++) {
                Element classElem = (Element)items.item(j);
                String className = classElem.getAttribute("class");
                classes.add(className);
            }
        }
        return map;
    }

    private static RubberBand loadSystemRubberband(Document config) {
        List osList = OsInfo.getInstance().getOsNames();
        HashMap info = loadRubberBandInfoByOS(config);

        for(Iterator it=osList.iterator(); it.hasNext();) {
            String osName = (String)it.next();
            List classes = (List)info.get(osName);
            if(classes==null)
                continue;

            for(Iterator it2=classes.iterator(); it2.hasNext();) {
                String implClass = (String)it2.next();
                RubberBand rb = createRubberBand(implClass);
                if(rb!=null)
                    return rb;
            }
        }

        String implClass = (String)info.get("default");
        RubberBand rb = createRubberBand(implClass);
        return rb==null? new RubberBand(): rb;
    }

    private static DragPreview loadDefaultPreview(Document config) {
        Element root = (Element)config.getElementsByTagName("drag-previews").item(0);
        String previewClass = root.getAttribute("default");
        DragPreview preview = createPreview(previewClass);
        if(preview!=null)
            return preview;
        // unable to load the preview class.  return a no-op preview delegate instead.
        return new DefaultPreview() {
            public void drawPreview(Graphics2D g, Polygon poly, Dockable dockable, Map dragInfo) {
                // noop
            }
        };
    }

}
