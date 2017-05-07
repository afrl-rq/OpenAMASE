// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Aug 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.flexdock.dockbar.layout;

import java.awt.Insets;

import javax.swing.border.Border;

import org.flexdock.docking.state.MinimizationManager;
import org.flexdock.plaf.common.border.CompoundEmptyBorder;

/**
 * @author Christopher Butler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InsetBorder extends CompoundEmptyBorder {
    private boolean leftEdge;
    private boolean rightEdge;
    private boolean bottomEdge;

    protected InsetBorder(Border outer, Border inner, boolean emptyInner) {
        super(outer, inner, emptyInner);
    }

    public static InsetBorder createBorder(Border border, boolean inner) {
        return createBorder(border, inner, null);
    }

    public static InsetBorder createBorder(Border border, boolean inner, Insets base) {
        if(base==null)
            base = new Insets(0, 0, 0, 0);

        MutableEmptyBorder empty = new MutableEmptyBorder(base.top, base.left, base.bottom, base.right);
        if(inner)
            return new InsetBorder(border, empty, inner);
        return new InsetBorder(empty, border, inner);
    }

    public void toggleEdge(int edge, boolean on) {
        switch(edge) {
        case MinimizationManager.LEFT:
            leftEdge = on;
            break;
        case MinimizationManager.RIGHT:
            rightEdge = on;
            break;
        case MinimizationManager.BOTTOM:
            bottomEdge = on;
            break;
        }
    }
    /**
     * @return Returns the bottomEdge.
     */
    public boolean isBottomEdge() {
        return bottomEdge;
    }
    /**
     * @param bottomEdge The bottomEdge to set.
     */
    public void setBottomEdge(boolean bottomEdge) {
        this.bottomEdge = bottomEdge;
    }
    /**
     * @return Returns the leftEdge.
     */
    public boolean isLeftEdge() {
        return leftEdge;
    }
    /**
     * @param leftEdge The leftEdge to set.
     */
    public void setLeftEdge(boolean leftEdge) {
        this.leftEdge = leftEdge;
    }
    /**
     * @return Returns the rightEdge.
     */
    public boolean isRightEdge() {
        return rightEdge;
    }
    /**
     * @param rightEdge The rightEdge to set.
     */
    public void setRightEdge(boolean rightEdge) {
        this.rightEdge = rightEdge;
    }

    void clearEdges() {
        setLeftEdge(false);
        setRightEdge(false);
        setBottomEdge(false);
    }

    public boolean setEmptyInsets(int top, int left, int bottom, int right) {
        left = isLeftEdge()? left: 0;
        right = isRightEdge()? right: 0;
        bottom = isBottomEdge()? bottom: 0;
        return super.setEmptyInsets(top, left, bottom, right);
    }
}
