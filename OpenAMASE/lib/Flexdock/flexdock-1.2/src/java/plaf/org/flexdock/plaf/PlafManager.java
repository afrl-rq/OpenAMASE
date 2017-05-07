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
package org.flexdock.plaf;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import org.flexdock.plaf.mappings.PlafMappingFactory;
import org.flexdock.plaf.theme.Theme;
import org.flexdock.plaf.theme.UIFactory;
import org.flexdock.util.RootWindow;
import org.flexdock.view.Button;
import org.flexdock.view.Titlebar;
import org.flexdock.view.View;

/**
 * @author Christopher Butler
 */
public class PlafManager {
    private static final String PREFERRED_THEME_KEY = "preferred.theme";

    private static final String UI_CHANGE_EVENT = "lookAndFeel";

    private static final Hashtable UI_DEFAULTS = new Hashtable();

    private static final Hashtable CUSTOM_THEMES = new Hashtable();

    static {
        initialize();
    }

    private static void initialize() {
        installPreferredTheme(false);
        // install an updater so we can keep up with changes in the installed
        // plaf
        UIManager.addPropertyChangeListener(new UiUpdater());
    }

    public static String getSystemThemeName() {
        return PlafMappingFactory.getInstalledPlafReference();
    }

    public static void installSystemTheme() {
        setPreferredTheme(getSystemThemeName());
    }

    public static void setPreferredTheme(Properties p) {
        String themeName = p == null ? null : p.getProperty(XMLConstants.NAME_KEY);
        if (themeName == null)
            throw new IllegalArgumentException(
                "Unable to find property 'name' in the supplied data set.");

        Theme theme = addCustomTheme(themeName, p);
        if (theme != null)
            setPreferredTheme(themeName, true);
    }

    public static void setPreferredTheme(String themeName) {
        setPreferredTheme(themeName, false);
    }

    public static void setPreferredTheme(String themeName, boolean reload) {
        String oldPref = (String) UI_DEFAULTS.get(PREFERRED_THEME_KEY);

        if (Configurator.isNull(themeName))
            UI_DEFAULTS.remove(PREFERRED_THEME_KEY);
        else
            UI_DEFAULTS.put(PREFERRED_THEME_KEY, themeName);

        String newPref = (String) UI_DEFAULTS.get(PREFERRED_THEME_KEY);

        // this will handle the case where we switch from null to something
        // else,
        // vice versa, or a new string value
        boolean themeChanged = oldPref != newPref
            && (oldPref == null || newPref == null || !oldPref.equals(newPref));
        if (reload || themeChanged)
            installPreferredTheme();
    }

    public static void installPreferredTheme(boolean update) {
        Theme theme = getPreferredTheme();

        UI_DEFAULTS.clear();
        setProperty(View.class, theme.getViewUI());
        setProperty(Titlebar.class, theme.getTitlebarUI());
        setProperty(Button.class, theme.getButtonUI());

        if (update) {
            RootWindow[] windows = RootWindow.getVisibleWindows();
            for (int i = 0; i < windows.length; i++)
                windows[i].updateComponentTreeUI();
        }
    }

    public static void installPreferredTheme() {
        installPreferredTheme(true);
    }

    private static Theme getPreferredTheme() {
        Theme theme = null;
        String themeName = (String) UI_DEFAULTS.get(PREFERRED_THEME_KEY);
        if (themeName != null) {
            theme = (Theme) CUSTOM_THEMES.get(themeName);
            if (theme == null)
                theme = UIFactory.getTheme(themeName);
        }
        if (theme == null)
            theme = UIFactory.getTheme(getSystemThemeName());
        if (theme == null)
            theme = UIFactory.getTheme(UIFactory.DEFAULT);
        return theme;
    }

    public static Theme addCustomTheme(String themeName, Properties p) {
        return loadCustomTheme(themeName, p, false);
    }

    public static Theme setCustomTheme(String themeName, Properties p) {
        return loadCustomTheme(themeName, p, true);
    }

    public static Theme loadCustomTheme(String themeName, Properties p,
                                        boolean exclusive) {
        if (Configurator.isNull(themeName) || p == null)
            return null;

        Theme theme = UIFactory.createTheme(p);
        if (theme != null) {
            theme.setName(themeName);
            if (exclusive)
                CUSTOM_THEMES.clear();
            CUSTOM_THEMES.put(themeName, theme);
        }
        return theme;
    }

    public static Theme removeCustomTheme(String themeName) {
        return Configurator.isNull(themeName) ? null : (Theme) CUSTOM_THEMES.remove(themeName);
    }

    private static void setProperty(Object key, Object value) {
        if (key != null && value != null)
            UI_DEFAULTS.put(key, value);
    }

    /**
     * Returns the appropriate {@code ComponentUI} implementation for
     * {@code target}. In case the component is a member of the installed look
     * and feel, this method first queries {@code UIManager.getUI(target)}
     * before attempting to resolve it locally.
     *
     * @param target
     *            the {@code JComponent} to return the {@code ComponentUI} for
     * @return the {@code ComponentUI} object for {@code target}
     * @throws NullPointerException
     *             if {@code target} is {@code null}
     * @see UIManager#getUI
     */
    public static ComponentUI getUI(JComponent target) {
        ComponentUI ui = /*UIManager.getUI(target);

if (ui == null) {
ui = */(ComponentUI) UI_DEFAULTS.get(target.getClass());
//        }

        return ui;
    }

    private static class UiUpdater implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if (UI_CHANGE_EVENT.equals(evt.getPropertyName())
                && evt.getOldValue() != evt.getNewValue()) {
                installPreferredTheme();
            }
        }
    }
}
