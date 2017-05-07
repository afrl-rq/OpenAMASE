// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on 20.03.2005
 */
package org.flexdock.plaf.resources.paint;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 * @author Claudio Romano
 */
public class DefaultPainter implements Painter {
    public static final Color DEFAULT_BG_COLOR = UIManager.getColor( "Panel.background");
    public static final Color DEFAULT_BG_COLOR_ACTIVE = UIManager.getColor( "InternalFrame.activeTitleBackground");

    protected PainterResource painterResource;

    public void paint(Graphics g, int width, int height, boolean active, JComponent titlebar) {
        Color c = getBackgroundColor(active);

        g.setColor(c);
        g.fillRect(0, 0, width, height);

    }

    protected Color getBackgroundColor(boolean active) {
        return active ? getBackgroundColorActive() :  getBackgroundColorInactive();
    }

    protected Color getBackgroundColorInactive() {
        return painterResource.getBgColor()==null ? DEFAULT_BG_COLOR : painterResource.getBgColor();
    }

    protected Color getBackgroundColorActive( ) {
        return painterResource.getBgColorActive()==null ? DEFAULT_BG_COLOR_ACTIVE : painterResource.getBgColorActive();
    }

    /**
     * @return Returns the painterResource.
     */
    public PainterResource getPainterResource() {
        return painterResource;
    }

    /**
     * @param painterResource The painterResource to set.
     */
    public void setPainterResource(PainterResource painterResource) {
        this.painterResource = painterResource;
    }

}