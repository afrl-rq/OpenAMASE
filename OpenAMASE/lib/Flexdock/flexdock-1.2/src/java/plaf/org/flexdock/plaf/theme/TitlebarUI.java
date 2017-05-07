// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Feb 27, 2005
 */
package org.flexdock.plaf.theme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.UIResource;

import org.flexdock.plaf.FlexViewComponentUI;
import org.flexdock.plaf.icons.IconMap;
import org.flexdock.plaf.icons.IconResource;
import org.flexdock.plaf.icons.IconResourceFactory;
import org.flexdock.plaf.resources.paint.Painter;
import org.flexdock.view.Button;
import org.flexdock.view.Titlebar;
import org.flexdock.view.View;

/**
 * @author Christopher Butler
 */
public class TitlebarUI extends FlexViewComponentUI {

    public static final String DEFAULT_HEIGHT = "default.height";
    public static final String FONT = "font";
    public static final String FONT_COLOR = "font.color";
    public static final String FONT_COLOR_ACTIVE = "font.color.active";
    public static final String BACKGROUND_COLOR = "bgcolor";
    public static final String BACKGROUND_COLOR_ACTIVE = "bgcolor.active";
    public static final String BORDER = "border";
    public static final String BORDER_ACTIVE = "border.active";
    public static final String PAINTER = "painter";
    public static final String INSETS = "insets";
    private static final String BUTTON_MARGIN = "button.margin";
    public static final String ICON_INSETS = "icon.insets";
    public static final String ANTIALIASING = "antialiasing";
    public static final int MINIMUM_HEIGHT = 12;

    protected Font font;
    protected Color activeFont;
    protected Color inactiveFont;
    protected Color activeBackground;
    protected Color inactiveBackground;
    protected Border activeBorder;
    protected Border inactiveBorder;
    protected IconMap defaultIcons;
    protected Painter painter;
    protected Insets insets;
    protected int buttonMargin;
    protected Insets iconInsets;
    protected Object antialiasing;
    protected int defaultHeight = MINIMUM_HEIGHT;


    public void installUI(JComponent c) {
        super.installUI(c);
        Dimension d = c.getPreferredSize();
        d.height = getDefaultHeight();
        c.setPreferredSize(d);

        reconfigureActions(c);

        if (font != null) {
            Font current = c.getFont();

            if (current == null || current instanceof UIResource) {
                c.setFont(new FontUIResource(font));
            }
        }
    }

    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
    }

    public void paint(Graphics g, JComponent jc) {
        Titlebar titlebar = (Titlebar) jc;
        paintBackground(g, titlebar);
        paintIcon(g, titlebar);
        paintTitle(g, titlebar);
        paintBorder(g, titlebar);
    }

    protected void paintBackground(Graphics g, Titlebar titlebar) {
        Rectangle paintArea = getPaintRect(titlebar);
        g.translate(paintArea.x, paintArea.y);
        painter.paint(g, paintArea.width, paintArea.height, titlebar.isActive(), titlebar);
        g.translate(-paintArea.x, -paintArea.y);
    }

    protected Rectangle getPaintRect(Titlebar titlebar) {
        if (getInsets() == null)
            return new Rectangle(0, 0, titlebar.getWidth(), titlebar.getHeight());

        Insets paintInsets = getInsets();
        return new Rectangle(paintInsets.left, paintInsets.top,
                             (titlebar.getWidth() - paintInsets.right - paintInsets.left), (titlebar.getHeight()
                                     - paintInsets.bottom - paintInsets.top));
    }

    protected void paintTitle(Graphics g, Titlebar titlebar) {
        if (titlebar.getText() == null)
            return;

        Graphics2D g2 = (Graphics2D) g;
        Object oldAAValue = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasing);

        g2.setFont(titlebar.getFont());
        Rectangle iconRect = getIconRect(titlebar);
        Rectangle paintRect = getPaintRect(titlebar);

        int x = getTextLocation(iconRect);

        //Center text vertically.
        FontMetrics fm = g.getFontMetrics();
        int y = (paintRect.height + fm.getAscent() - fm.getLeading() -
                 fm.getDescent()) / 2;

        Color c = getFontColor(titlebar.isActive());
        g2.setColor(c);
        g.translate(paintRect.x, paintRect.y);
        g2.drawString(titlebar.getText(), x, y);
        g.translate(-paintRect.x, -paintRect.y);


        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAAValue);
    }

    protected int getTextLocation(Rectangle iconRect) {
        if (iconRect.width > 0)
            return iconRect.x + iconRect.width + getRightIconMargin();
        return 5;
    }

    protected void paintIcon(Graphics g, Titlebar titlebar) {
        if (titlebar.getIcon() == null)
            return;

        Icon icon = titlebar.getIcon();
        Rectangle r = getIconRect(titlebar);

        Rectangle paintRect = getPaintRect(titlebar);
        g.translate(paintRect.x, paintRect.y);
        icon.paintIcon(titlebar, g, r.x, r.y);
        g.translate(-paintRect.x, -paintRect.y);
    }

    protected Rectangle getIconRect(Titlebar titlebar) {
        Icon icon = titlebar.getIcon();
        Rectangle r = new Rectangle(0, 0, 0, 0);
        if (icon == null)
            return r;

        Rectangle paintRect = getPaintRect(titlebar);

        r.x = getLeftIconMargin();
        r.width = icon.getIconWidth();
        r.height = icon.getIconHeight();
        r.y = (paintRect.height - r.height) / 2;
        return r;
    }

    protected int getLeftIconMargin() {
        return getIconInsets() == null ? 2 : getIconInsets().right;
    }

    protected int getRightIconMargin() {
        return getIconInsets() == null ? 2 : getIconInsets().right;
    }

    protected void paintBorder(Graphics g, Titlebar titlebar) {
        Border border = getBorder(titlebar);
        if (border != null) {
            Rectangle rectangle = getPaintRect(titlebar);
            g.translate(rectangle.x, rectangle.y);
            border.paintBorder(titlebar, g, 0, 0, rectangle.width, rectangle.height);
            g.translate(-rectangle.x, -rectangle.y);
        }
    }

    public void layoutComponents(Titlebar titlebar) {
        Rectangle rectangle = getPaintRect(titlebar);
        int margin = getButtonMargin();
        int h = rectangle.height - 2 * margin;
        int x = rectangle.width - margin - h;

        View view = titlebar.getView();
        Component[] c = titlebar.getComponents();
        for (int i = 0; i < c.length; i++) {
            // start out with the preferred width
            int width = c[i].getPreferredSize().width;
            if (c[i] instanceof Button) {
                // org.flexdock.view.Buttons will be rendered as squares
                width = h;
                // don't show the button if its corresponding action is blocked
                if(view!=null && view.isActionBlocked(((Button) c[i]).getActionName())) {
                    c[i].setBounds(0, 0, 0, 0);
                    continue;
                }
            }
            // layout the component over to the right
            c[i].setBounds(x, margin + rectangle.y, width, h);
            // move x to the left for the next component
            x -= width;
        }
    }

    public void configureAction(Action action) {
        if (action == null)
            return;

        IconResource icons = getIcons(action);
        if (icons != null) {
            action.putValue(ICON_RESOURCE, icons);
        }
    }

    private void reconfigureActions(JComponent c) {
        Component[] c1 = c.getComponents();
        for (int i = 0; i < c1.length; i++) {
            if (!(c1[i] instanceof Button))
                continue;
            Button b = (Button) c1[i];
            configureAction(b.getAction());
        }
    }

    protected Color getFontColor(boolean active) {
        Color c = active ? activeFont : inactiveFont;
        return c == null ? inactiveFont : c;
    }

    protected Color getBackgroundColor(boolean active) {
        Color color = active ? activeBackground : inactiveBackground;
        return color == null ? inactiveBackground : color;
    }

    protected Border getBorder(Titlebar titlebar) {
        boolean active = titlebar.isActive();
        return active ? activeBorder : inactiveBorder;
    }

    public int getDefaultHeight() {
        return defaultHeight;
    }

    public void setDefaultHeight(int defaultHeight) {
        defaultHeight = Math.max(defaultHeight, MINIMUM_HEIGHT);
        this.defaultHeight = defaultHeight;
    }

    public Dimension getPreferredSize() {
        return new Dimension(10, getDefaultHeight());
    }

    /**
     * @return Returns the activeBackground.
     */
    public Color getActiveBackground() {
        return activeBackground;
    }

    /**
     * @param activeBackground
     *            The activeBackground to set.
     */
    public void setActiveBackground(Color activeBackground) {
        this.activeBackground = activeBackground;
    }

    /**
     * @return Returns the activeFont.
     */
    public Color getActiveFont() {
        return activeFont;
    }

    /**
     * @param activeFont
     *            The activeFont to set.
     */
    public void setActiveFont(Color activeFont) {
        this.activeFont = activeFont;
    }

    /**
     * @return Returns the inactiveBackground.
     */
    public Color getInactiveBackground() {
        return inactiveBackground;
    }

    /**
     * @param inactiveBackground
     *            The inactiveBackground to set.
     */
    public void setInactiveBackground(Color inactiveBackground) {
        this.inactiveBackground = inactiveBackground;
    }

    /**
     * @return Returns the inactiveFont.
     */
    public Color getInactiveFont() {
        return inactiveFont;
    }

    /**
     * @param inactiveFont
     *            The inactiveFont to set.
     */
    public void setInactiveFont(Color inactiveFont) {
        this.inactiveFont = inactiveFont;
    }

    /**
     * @return Returns the font.
     */
    public Font getFont() {
        return font;
    }

    /**
     * @param font
     *            The font to set.
     */
    public void setFont(Font font) {
        this.font = font;
    }

    public IconMap getDefaultIcons() {
        return defaultIcons;
    }

    public void setDefaultIcons(IconMap defaultIcons) {
        this.defaultIcons = defaultIcons;
    }

    public void setDefaultIcons(String iconMapName) {
        IconMap map = IconResourceFactory.getIconMap(iconMapName);
        setDefaultIcons(map);
    }

    public IconResource getIcons(Action action) {
        String key = action == null ? null : (String) action.getValue(Action.NAME);
        return getIcons(key);
    }

    public IconResource getIcons(String key) {
        return defaultIcons == null ? null : defaultIcons.getIcons(key);
    }

    public Action getAction(String actionKey) {
        IconResource resource = getIcons(actionKey);
        Action action = resource==null? null: resource.getAction();
        if(action!=null)
            action.putValue(Action.NAME, actionKey);
        return action;
    }

    /**
     * @return Returns the inactiveBorder.
     */
    public Border getInactiveBorder() {
        return inactiveBorder;
    }

    /**
     * @param inactiveBorder
     *            The inactiveBorder to set.
     */
    public void setInactiveBorder(Border inactiveBorder) {
        this.inactiveBorder = inactiveBorder;
    }

    /**
     * @return Returns the activeBorder.
     */
    public Border getActiveBorder() {
        return activeBorder;
    }

    /**
     * @param activeBorder
     *            The activeBorder to set.
     */
    public void setActiveBorder(Border activeBorder) {
        this.activeBorder = activeBorder;
    }

    /**
     * @return Returns the iconInsets.
     */
    public Insets getIconInsets() {
        return iconInsets;
    }

    /**
     * @param iconInsets
     *            The iconInsets to set.
     */
    public void setIconInsets(Insets iconInsets) {
        this.iconInsets = iconInsets;
    }

    /**
     * @return Returns the buttonMargin.
     */
    public int getButtonMargin() {
        return buttonMargin;
    }

    /**
     * @param buttonMargin
     *            The buttonMargin to set.
     */
    public void setButtonMargin(int buttonMargin) {
        this.buttonMargin = buttonMargin;
    }

    /**
     * @return Returns the painterResource.
     */
    public Painter getPainter() {
        return painter;
    }

    /**
     * @param painter
     *            The painter to set.
     */
    public void setPainter(Painter painter) {
        this.painter = painter;
    }

    /**
     * @return Returns the insets.
     */
    public Insets getInsets() {
        return insets;
    }

    /**
     * @param insets
     *            The insets to set.
     */
    public void setInsets(Insets insets) {
        this.insets = insets;
    }

    /**
     * @return Returns the antialiasing.
     */
    public boolean isAntialiasing() {
        return antialiasing == RenderingHints.VALUE_ANTIALIAS_ON;
    }

    /**
     * @param antialias
     *            The antialias to set.
     */
    public void setAntialiasing(boolean antialias) {
        antialiasing = antialias ? RenderingHints.VALUE_ANTIALIAS_ON :
                       RenderingHints.VALUE_ANTIALIAS_OFF;
    }

    public void initializeCreationParameters() {
        setActiveBackground(creationParameters.getColor(BACKGROUND_COLOR_ACTIVE));
        setActiveFont(creationParameters.getColor(FONT_COLOR_ACTIVE));
        setInactiveBackground(creationParameters.getColor(BACKGROUND_COLOR));
        setInactiveFont(creationParameters.getColor(FONT_COLOR));
        setDefaultHeight(creationParameters.getInt(DEFAULT_HEIGHT));
        setFont(creationParameters.getFont(FONT));
        setInactiveBorder(creationParameters.getBorder(BORDER));
        setActiveBorder(creationParameters.getBorder(BORDER_ACTIVE));

        setDefaultIcons(creationParameters.getString(IconResourceFactory.ICON_MAP_KEY));
        setPainter((Painter) creationParameters.getProperty(PAINTER));
        setIconInsets((Insets) creationParameters.getProperty(ICON_INSETS));
        setButtonMargin(creationParameters.getInt(BUTTON_MARGIN));
        setPainter((Painter) creationParameters.getProperty(PAINTER));
        setInsets((Insets) creationParameters.getProperty(INSETS));
        setAntialiasing(creationParameters.getBoolean( ANTIALIASING));
    }

    public String getPreferredButtonUI() {
        return creationParameters.getString(UIFactory.BUTTON_KEY);
    }
}