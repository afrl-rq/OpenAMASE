// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Feb 28, 2005
 */
package org.flexdock.plaf.icons;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.plaf.UIResource;

/**
 * @author Christopher Butler
 */
public class IconResource implements UIResource {
    private Icon icon;
    private Icon iconDisabled;
    private Icon iconHover;
    private Icon iconActive;
    private Icon iconActiveDisabled;
    private Icon iconActiveHover;
    private Icon iconPressed;

    private Icon iconSelected;
    private Icon iconSelectedDisabled;
    private Icon iconSelectedHover;
    private Icon iconSelectedActive;
    private Icon iconSelectedActiveHover;
    private Icon iconSelectedActiveDisabled;
    private Icon iconSelectedPressed;


    private Action action;

    private String tooltip;
    private String tooltipSelected;

    /**
     * @return Returns the icon.
     */
    public Icon getIcon() {
        return icon;
    }
    /**
     * @param icon The icon to set.
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
    }
    /**
     * @return Returns the iconActive.
     */
    public Icon getIconActive() {
        return iconActive;
    }
    /**
     * @param iconActive The iconActive to set.
     */
    public void setIconActive(Icon iconActive) {
        this.iconActive = iconActive;
    }
    /**
     * @return Returns the iconActiveHover.
     */
    public Icon getIconActiveHover() {
        return iconActiveHover;
    }
    /**
     * @param iconActiveHover The iconActiveHover to set.
     */
    public void setIconActiveHover(Icon iconActiveHover) {
        this.iconActiveHover = iconActiveHover;
    }
    /**
     * @return Returns the iconHover.
     */
    public Icon getIconHover() {
        return iconHover;
    }
    /**
     * @param iconHover The iconHover to set.
     */
    public void setIconHover(Icon iconHover) {
        this.iconHover = iconHover;
    }
    /**
     * @return Returns the iconPressed.
     */
    public Icon getIconPressed() {
        return iconPressed;
    }
    /**
     * @param iconPressed The iconPressed to set.
     */
    public void setIconPressed(Icon iconPressed) {
        this.iconPressed = iconPressed;
    }

    public Icon getIconSelected() {
        return iconSelected;
    }
    public void setIconSelected(Icon iconSelected) {
        this.iconSelected = iconSelected;
    }
    public Icon getIconSelectedActive() {
        return iconSelectedActive;
    }
    public void setIconSelectedActive(Icon iconSelectedActive) {
        this.iconSelectedActive = iconSelectedActive;
    }
    public Icon getIconSelectedActiveHover() {
        return iconSelectedActiveHover;
    }
    public void setIconSelectedActiveHover(Icon iconSelectedActiveHover) {
        this.iconSelectedActiveHover = iconSelectedActiveHover;
    }
    public Icon getIconSelectedHover() {
        return iconSelectedHover;
    }
    public void setIconSelectedHover(Icon iconSelectedHover) {
        this.iconSelectedHover = iconSelectedHover;
    }
    public Icon getIconSelectedPressed() {
        return iconSelectedPressed;
    }
    public void setIconSelectedPressed(Icon iconSelectedPressed) {
        this.iconSelectedPressed = iconSelectedPressed;
    }
    public String getTooltip() {
        return tooltip;
    }
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
    public String getTooltipSelected() {
        return tooltipSelected;
    }
    public void setTooltipSelected(String tooltipSelected) {
        this.tooltipSelected = tooltipSelected;
    }

    public Action getAction() {
        return action;
    }
    public void setAction(Action action) {
        this.action = action;
    }
    /**
     * @return Returns the iconDisabled.
     */
    public Icon getIconDisabled() {
        return iconDisabled;
    }
    /**
     * @param iconDisabled The iconDisabled to set.
     */
    public void setIconDisabled(Icon iconDisabled) {
        this.iconDisabled = iconDisabled;
    }

    /**
     * @return Returns the iconActiveDisabled.
     */
    public Icon getIconActiveDisabled() {
        return iconActiveDisabled;
    }
    /**
     * @param iconActiveDisabled The iconActiveDisabled to set.
     */
    public void setIconActiveDisabled(Icon iconActiveDisabled) {
        this.iconActiveDisabled = iconActiveDisabled;
    }
    /**
     * @return Returns the iconSelectedActiveDisabled.
     */
    public Icon getIconSelectedActiveDisabled() {
        return iconSelectedActiveDisabled;
    }
    /**
     * @param iconSelectedActiveDisabled The iconSelectedActiveDisabled to set.
     */
    public void setIconSelectedActiveDisabled(Icon iconSelectedActiveDisabled) {
        this.iconSelectedActiveDisabled = iconSelectedActiveDisabled;
    }
    /**
     * @return Returns the iconSelectedDisabled.
     */
    public Icon getIconSelectedDisabled() {
        return iconSelectedDisabled;
    }
    /**
     * @param iconSelectedDisabled The iconSelectedDisabled to set.
     */
    public void setIconSelectedDisabled(Icon iconSelectedDisabled) {
        this.iconSelectedDisabled = iconSelectedDisabled;
    }
}
