// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on 15.04.2005
 */
package org.flexdock.plaf.theme.skinlf;

import java.awt.Graphics;

import javax.swing.JComponent;

import org.flexdock.plaf.resources.paint.DefaultPainter;

import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;

/**
 * @author Claudio Romano
 */
public class SkinLFPainter extends DefaultPainter {
    public void paint(Graphics g, int width, int height, boolean active, JComponent titlebar) {
        SkinLookAndFeel.getSkin().getFrame().paintTop(g, titlebar, active, "");
    }
}
