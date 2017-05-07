// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on 17.04.2005
 */
package org.flexdock.plaf.theme.skinlf;

import java.awt.Graphics;
import java.awt.Rectangle;

import org.flexdock.plaf.theme.TitlebarUI;
import org.flexdock.view.Titlebar;

import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;

/**
 * @author Claudio Romano
 */
public class SkinLFTitlebarUI extends TitlebarUI {

    protected void paintBackground(Graphics g, Titlebar titlebar) {
        Rectangle paintArea = getPaintRect(titlebar);
        g.translate(paintArea.x, paintArea.y);
        SkinLookAndFeel.getSkin().getFrame().paintTop(g, titlebar, titlebar.isActive(), titlebar.getText());
        g.translate(-paintArea.x, -paintArea.y);
    }


    public int getDefaultHeight() {
        return SkinLookAndFeel.getSkin().getFrame().getTopPreferredSize().height;
    }
}
