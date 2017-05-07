// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on 19.03.2005
 */
package org.flexdock.plaf.resources.paint;

import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * @author Claudio Romano
 */
public interface Painter {
    public void paint(Graphics g, int width, int height, boolean active, JComponent titlebar);

    public PainterResource getPainterResource();
    public void setPainterResource(PainterResource painterResource);
}
