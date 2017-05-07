// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 14, 2005
 */
package org.flexdock.plaf.common.border;

import java.awt.Insets;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * @author Christopher Butler
 */
public class CompoundEmptyBorder extends CompoundBorder {
    protected static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    protected boolean inner;

    public static CompoundEmptyBorder create(Border border, boolean inner) {
        return create(border, inner, null);
    }

    public static CompoundEmptyBorder create(Border border, boolean inner, Insets base) {
        if(base==null)
            base = new Insets(0, 0, 0, 0);

        MutableEmptyBorder empty = new MutableEmptyBorder(base.top, base.left, base.bottom, base.right);
        if(inner)
            return new CompoundEmptyBorder(border, empty, inner);
        return new CompoundEmptyBorder(empty, border, inner);
    }

    protected CompoundEmptyBorder(Border outer, Border inner, boolean emptyInner) {
        super(outer, inner);
        this.inner = emptyInner;
    }

    public boolean setEmptyInsets(Insets insets) {
        if(insets==null)
            insets = EMPTY_INSETS;
        return setEmptyInsets(insets.top, insets.left, insets.bottom, insets.right);
    }

    public boolean setEmptyInsets(int top, int left, int bottom, int right) {
        Border border = inner? getInsideBorder(): getOutsideBorder();
        return ((MutableEmptyBorder)border).updateInsets(top, left, bottom, right);
    }

    public Insets getEmptyInsets() {
        Border border = inner? getInsideBorder(): getOutsideBorder();
        MutableEmptyBorder empty = (MutableEmptyBorder)border;
        return empty.getInsetsCopy();
    }

    public Border getWrappedBorder() {
        return inner? getOutsideBorder(): getInsideBorder();
    }

    protected static class MutableEmptyBorder extends EmptyBorder {
        public MutableEmptyBorder(int top, int left, int bottom, int right) {
            super(top, left, bottom, right);
        }

        public MutableEmptyBorder(Insets borderInsets) {
            super(borderInsets);
        }

        private boolean updateInsets(int top, int left, int bottom, int right) {
            boolean changed = this.top!=top || this.left!=left || this.bottom!=bottom || this.right!=right;
            this.top = top;
            this.bottom = bottom;
            this.left = left;
            this.right = right;
            return changed;
        }

        private Insets getInsetsCopy() {
            return new Insets(top, left, bottom, right);
        }
    }
}
