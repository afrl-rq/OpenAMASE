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
package org.flexdock.plaf.theme;

import java.util.HashMap;
import java.util.Properties;



import org.flexdock.plaf.Configurator;
import org.flexdock.plaf.IFlexViewComponentUI;
import org.flexdock.plaf.PlafManager;
import org.flexdock.plaf.PropertySet;
import org.flexdock.plaf.XMLConstants;
import org.w3c.dom.Element;

/**
 * @author Christopher Butler
 */
public class UIFactory implements XMLConstants {

    public static final String DEFAULT = "default";
    public static final String THEME_KEY = "theme";
    public static final String VIEW_KEY = "view-ui";
    public static final String TITLEBAR_KEY = "titlebar-ui";
    public static final String BUTTON_KEY = "button-ui";
    private static final HashMap VIEW_UI_CACHE = new HashMap();
    private static final HashMap TITLEBAR_UI_CACHE = new HashMap();
    private static final HashMap BUTTON_UI_CACHE = new HashMap();
    private static final HashMap THEME_UI_CACHE = new HashMap();

    public static ViewUI getViewUI(String name) {
        return (ViewUI)getUI(name, VIEW_UI_CACHE, VIEW_KEY, ViewUI.class);
    }

    public static ViewUI getViewUI(Properties p) {
        return (ViewUI)getUI(p, VIEW_UI_CACHE, VIEW_KEY, ViewUI.class);
    }

    public static TitlebarUI getTitlebarUI(String name) {
        return (TitlebarUI)getUI(name, TITLEBAR_UI_CACHE, TITLEBAR_KEY, TitlebarUI.class);
    }

    public static TitlebarUI getTitlebarUI(Properties p) {
        return (TitlebarUI)getUI(p, TITLEBAR_UI_CACHE, TITLEBAR_KEY, TitlebarUI.class);
    }

    public static ButtonUI getButtonUI(String name) {
        return (ButtonUI)getUI(name, BUTTON_UI_CACHE, BUTTON_KEY, ButtonUI.class);
    }

    public static ButtonUI getButtonUI(Properties p) {
        return (ButtonUI)getUI(p, BUTTON_UI_CACHE, BUTTON_KEY, ButtonUI.class);
    }



    public static Theme getTheme(String name) {
        if(Configurator.isNull(name))
            return null;

        Theme theme = (Theme)THEME_UI_CACHE.get(name);
        if(theme==null) {
            theme = loadTheme(name);
            if(theme!=null) {
                synchronized(THEME_UI_CACHE) {
                    THEME_UI_CACHE.put(name, theme);
                }
            }
        }
        return theme;
    }

    private static IFlexViewComponentUI getUI(Properties p, HashMap cache, String tagName, Class rootClass) {
        if(p==null || !p.containsKey(tagName))
            return null;

        String name = p.getProperty(tagName);
        return getUI(name, cache, tagName, rootClass);
    }

    private static IFlexViewComponentUI getUI(String name, HashMap cache, String tagName, Class rootClass) {
        if(Configurator.isNull(name))
            return null;

        IFlexViewComponentUI ui = (IFlexViewComponentUI)cache.get(name);
        if(ui==null) {
            ui = loadUI(name, tagName, rootClass);
            if(ui!=null) {
                synchronized(cache) {
                    cache.put(name, ui);
                }
            }
        }
        return ui;
    }

    private static IFlexViewComponentUI loadUI(String name, String tagName, Class rootClass) {
        PropertySet properties = Configurator.getProperties(name, tagName);
        String classname = properties.getString(CLASSNAME_KEY);
        Class implClass = loadUIClass(classname, rootClass);

        try {
            IFlexViewComponentUI ui = (IFlexViewComponentUI)implClass.newInstance();
            ui.setCreationParameters(properties);
            return ui;
        } catch(Exception e) {
            // we use public, no-argument constructors, so if this happens, we
            // have a configuration error.
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static Class loadUIClass(String classname, Class rootClass) {
        if(Configurator.isNull(classname))
            return rootClass;

        Class implClass = null;
        try {
            implClass = Class.forName(classname);
            if(!rootClass.isAssignableFrom(implClass)) {
                implClass = null;
            }
        } catch(ClassNotFoundException e) {
            System.err.println("Exception: " + e.getMessage());
            implClass = null;
        }
        return implClass==null? rootClass: implClass;
    }

    private static Theme loadTheme(String themeName) {
        HashMap map = Configurator.getNamedElementsByTagName(THEME_KEY);
        if(map==null)
            return null;
        return loadTheme(themeName, map);
    }

    private static Theme loadTheme(String themeName, HashMap cache) {
        Element themeElem = (Element)cache.get(themeName);
        if(themeElem==null)
            return null;

        // if we're an indirect reference to a different theme, then return that theme
        String redirect = themeElem.getAttribute(REFERENCE_KEY);
        if(!Configurator.isNull(redirect))
            return loadTheme(redirect, cache);

        // if we're a child of another theme, then load the parent and
        // add our properties afterward
        String parentName = themeElem.getAttribute(EXTENDS_KEY);
        Theme theme = Configurator.isNull(parentName)? new Theme(): loadTheme(parentName, cache);
        if(theme==null)
            theme = new Theme();

        String name = themeElem.getAttribute(NAME_KEY);
        String desc = themeElem.getAttribute(DESC_KEY);
        String view = themeElem.getAttribute(VIEW_KEY);

        theme.setName(name);
        theme.setDescription(desc);

        ViewUI viewUI = Configurator.isNull(view)? getViewUI(DEFAULT): getViewUI(view);
        TitlebarUI titlebarUI = viewUI==null? getTitlebarUI(DEFAULT): getTitlebarUI(viewUI.getPreferredTitlebarUI());
        ButtonUI buttonUI = titlebarUI==null? getButtonUI(DEFAULT): getButtonUI(titlebarUI.getPreferredButtonUI());

        theme.setViewUI(viewUI);
        theme.setTitlebarUI(titlebarUI);
        theme.setButtonUI(buttonUI);

        return theme;
    }

    public static Theme createTheme(Properties p) {
        if(p==null)
            return null;

        Theme base = getTheme(PlafManager.getSystemThemeName());

        ViewUI view = getViewUI(p);
        if(view==null)
            view = base.getViewUI();
        if(view==null)
            view = getViewUI(DEFAULT);

        TitlebarUI titlebar = getTitlebarUI(p);
        if(titlebar==null)
            titlebar = getTitlebarUI(view.getPreferredTitlebarUI());
        if(titlebar==null)
            titlebar = base.getTitlebarUI();
        if(titlebar==null)
            titlebar = getTitlebarUI(DEFAULT);

        ButtonUI button = getButtonUI(p);
        if(button==null)
            button = getButtonUI(titlebar.getPreferredButtonUI());
        if(button==null)
            button = base.getButtonUI();
        if(button==null)
            button = getButtonUI(DEFAULT);

        Theme theme = new Theme();
        theme.setName(p.getProperty(NAME_KEY, "custom"));
        theme.setDescription(p.getProperty(DESC_KEY, "Custom Theme"));
        theme.setViewUI(view);
        theme.setTitlebarUI(titlebar);
        theme.setButtonUI(button);
        return theme;
    }
}
