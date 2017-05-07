// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 1, 2005
 */
package org.flexdock.plaf.icons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.flexdock.plaf.Configurator;
import org.flexdock.plaf.PropertySet;
import org.flexdock.plaf.XMLConstants;

/**
 * @author Christopher Butler
 *
 */
public class IconResourceFactory implements XMLConstants {
    public static final String ICON_RESOURCE_KEY = "icon-resource";
    public static final String ICON_MAP_KEY = "icon-map";

    public static final String ACTION_KEY = "action";

    public static final String DEFAULT = "default";
    public static final String DISABLED = "disabled";
    public static final String HOVER = "hover";
    public static final String ACTIVE = "active";
    public static final String ACTIVE_HOVER = "active.hover";
    public static final String ACTIVE_DISABLED = "active.disabled";
    public static final String PRESSED = "pressed";

    public static final String DEFAULT_SELECTED = "default.selected";
    public static final String HOVER_SELECTED = "hover.selected";
    public static final String DISABLED_SELECTED = "disabled.selected";
    public static final String ACTIVE_SELECTED = "active.selected";
    public static final String ACTIVE_SELECTED_HOVER = "active.armed.selected";
    public static final String ACTIVE_DISABLED_SELECTED = "active.disabled.selected";
    public static final String PRESSED_SELECTED = "pressed.selected";

    public static final String TOOLTIP = "tooltip";
    public static final String TOOLTIP_SELECTED = "tooltip.selected";

    private static final HashMap RESOURCE_CACHE = new HashMap();
    private static final HashMap RESOURCE_MAP_CACHE = new HashMap();
    private static final HashSet BAD_RESOURCE_NAMES = new HashSet();

    public static IconMap getIconMap(String name) {
        if(Configurator.isNull(name) || BAD_RESOURCE_NAMES.contains(name))
            return null;

        IconMap map = (IconMap)RESOURCE_MAP_CACHE.get(name);
        if(map==null) {
            map = loadIconMap(name);
            if(map==null) {
                synchronized(BAD_RESOURCE_NAMES) {
                    BAD_RESOURCE_NAMES.add(name);
                }
            } else {
                synchronized(RESOURCE_MAP_CACHE) {
                    RESOURCE_MAP_CACHE.put(name, map);
                }
            }
        }
        return map;
    }

    public static IconResource getResource(String name) {
        if(Configurator.isNull(name))
            return null;

        IconResource icons = getCachedResource(name);
        if(icons==null) {
            icons = loadIcons(name);
            cacheResource(name, icons);
        }
        return icons;
    }

    private static IconResource getCachedResource(String name) {
        return (IconResource)RESOURCE_CACHE.get(name);
    }

    private static void cacheResources(IconMap map) {
        if(map!=null) {
            for(Iterator it=map.keySet().iterator(); it.hasNext();) {
                String key = (String)it.next();
                IconResource resource = map.getIcons(key);
                cacheResource(key, resource);
            }
        }
    }
    private static void cacheResource(String name, IconResource icons) {
        if(icons!=null) {
            synchronized(RESOURCE_CACHE) {
                RESOURCE_CACHE.put(name, icons);
            }
        }
    }

    private static IconResource loadIcons(String name) {
        PropertySet properties = Configurator.getProperties(name, ICON_RESOURCE_KEY);
        return createResource(properties);
    }

    private static IconResource createResource(PropertySet properties) {
        IconResource icons = new IconResource();

        icons.setIcon(properties.getIcon(DEFAULT));
        icons.setIconHover(properties.getIcon(HOVER));
        icons.setIconDisabled(properties.getIcon(DISABLED));
        icons.setIconActive(properties.getIcon(ACTIVE));
        icons.setIconActiveHover(properties.getIcon(ACTIVE_HOVER));
        icons.setIconActiveDisabled(properties.getIcon(ACTIVE_DISABLED));
        icons.setIconPressed(properties.getIcon(PRESSED));

        icons.setIconSelected(properties.getIcon(DEFAULT_SELECTED));
        icons.setIconSelectedDisabled(properties.getIcon(DISABLED_SELECTED));
        icons.setIconSelectedHover(properties.getIcon(HOVER_SELECTED));
        icons.setIconSelectedActive(properties.getIcon(ACTIVE_SELECTED));
        icons.setIconSelectedActiveHover(properties.getIcon(ACTIVE_SELECTED_HOVER));
        icons.setIconSelectedActiveDisabled(properties.getIcon(ACTIVE_DISABLED_SELECTED));
        icons.setIconSelectedPressed(properties.getIcon(PRESSED_SELECTED));

        icons.setAction(properties.getAction(ACTION_KEY));

        icons.setTooltip(properties.getString(TOOLTIP));
        icons.setTooltipSelected(properties.getString(TOOLTIP_SELECTED));

        return icons;
    }

    private static IconMap loadIconMap(String iconMapName) {
        PropertySet iconMapProperties = Configurator.getProperties(iconMapName, ICON_MAP_KEY);
        IconMap iconMap = new IconMap();

        ArrayList notCached = new ArrayList();
        for(Iterator it=iconMapProperties.keys(); it.hasNext();) {
            String fakeName = (String)it.next();
            String realName = iconMapProperties.getString(fakeName);

            // load all the cached icon resources
            IconResource iconResource = getCachedResource(realName);
            if(iconResource==null) {
                // track the non-cached icons
                notCached.add(fakeName);
            } else {
                iconMap.addIcons(fakeName, iconResource);
            }
        }
        // if all our resources were already cached, then we can return immediately
        if(notCached.size()==0)
            return iconMap;

        // otherwise, we need to load resources and put them in the cache.
        String[] fakeNames = (String[])notCached.toArray(new String[0]);
        String[] realNames = iconMapProperties.getStrings(fakeNames);
        // load the resources into memory
        HashMap resourceMap = loadIconResources(realNames);

        // now loop through and cache
        for(int i=0; i<fakeNames.length; i++) {
            String realName = iconMapProperties.getString(fakeNames[i]);
            IconResource iconResource = (IconResource)resourceMap.get(realName);
            // cache the resource for future use
            cacheResource(realName, iconResource);
            // add to the immediate icon map
            iconMap.addIcons(fakeNames[i], iconResource);
        }
        return iconMap;
    }



    private static HashMap loadIconResources(String[] iconNames) {
        PropertySet[] iconResources = Configurator.getProperties(iconNames, ICON_RESOURCE_KEY);
        HashMap map = new HashMap(iconResources.length);

        for(int i=0; i<iconResources.length; i++) {
            IconResource resource = createResource(iconResources[i]);
            map.put(iconResources[i].getName(), resource);
        }
        return map;
    }
}
