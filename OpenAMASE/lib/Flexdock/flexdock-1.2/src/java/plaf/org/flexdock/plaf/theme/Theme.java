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

/**
 * @author Christopher Butler
 */
public class Theme {
    private String name;
    private String description;

    private ViewUI viewUI;
    private TitlebarUI titlebarUI;
    private ButtonUI buttonUI;

    public ButtonUI getButtonUI() {
        return buttonUI;
    }

    public void setButtonUI(ButtonUI buttonUI) {
        this.buttonUI = buttonUI;
    }

    public TitlebarUI getTitlebarUI() {
        return titlebarUI;
    }

    public void setTitlebarUI(TitlebarUI titlebarUI) {
        this.titlebarUI = titlebarUI;
    }

    public ViewUI getViewUI() {
        return viewUI;
    }

    public void setViewUI(ViewUI viewUI) {
        this.viewUI = viewUI;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
