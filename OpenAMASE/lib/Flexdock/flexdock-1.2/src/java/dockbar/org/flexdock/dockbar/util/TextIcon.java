// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.dockbar.util;

/* Copyright (c) 2004 Andreas Ernst

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in the
Software without restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
Software, and to permit persons to whom the Software is furnished to do so, subject
to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. */

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;

/**
 * @author Andreas Ernst
 * @author Christopher Butler
 */
public class TextIcon implements Icon, PropertyChangeListener {
    // constants

    public static final int ROTATE_NONE    = 0x01;
    public static final int ROTATE_LEFT    = 0x02;
    public static final int ROTATE_RIGHT   = 0x04;

    private static final double NINETY_DEGREES = Math.toRadians(90.0);

    // instance data

    private Component mComponent;

    private String    mText;
    private Icon      mIcon;
    private int       mIconSpace;
    private int       mInset;
    private int       mWidth;
    private int       mHeight;
    private int       mCharHeight;
    private int       mDescent;
    private int       mRotation;


    // constructor

    /**
     * Creates a <code>TextIcon</code> for the specified <code>component</code>
     * with the specified <code>label</code>.
     * It sets the orientation to the provided value if it's legal for the string
     */
    public TextIcon(Component cmp, int space, int inset) {
        mComponent = cmp;
        mText     = "";
        mIcon      = null;
        mRotation  = ROTATE_NONE;
        mIconSpace = space;
        mInset     = inset;

        calcDimensions();

        mComponent.addPropertyChangeListener(this);
    }

    // public

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        if(text==null)
            text = "";

        boolean changed = isChanged(mText, text);
        mText = text;

        if(changed)
            recalcDimensions();
    }

    public void setIcon(Icon icon) {
        boolean changed = isChanged(mIcon, icon);
        mIcon = icon;

        if(changed)
            recalcDimensions();
    }

    public void setRotation(int rotation) {
        rotation = getValidRotation(rotation);
        boolean changed = rotation!=mRotation;
        mRotation = rotation;

        if(changed)
            recalcDimensions();
    }

    public static int getValidRotation(int rotation) {
        switch(rotation) {
        case ROTATE_LEFT:
            return ROTATE_LEFT;
        case ROTATE_RIGHT:
            return ROTATE_RIGHT;
        default:
            return ROTATE_NONE;
        }
    }

    private boolean isChanged(Object oldValue, Object newValue) {
        if(oldValue==newValue)
            return false;

        if(oldValue==null || newValue==null)
            return true;

        return !oldValue.equals(newValue);
    }

    // implement PropertyChangeListener

    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (prop.equals("font"))
            recalcDimensions();
    }

    // private

    private void recalcDimensions() {
        int w = getIconWidth();
        int h = getIconHeight();

        calcDimensions();

        if (w != getIconWidth() || h != getIconHeight())
            mComponent.invalidate();
    }

    private void calcDimensions() {
        FontMetrics fm = mComponent.getFontMetrics(mComponent.getFont());

        mCharHeight = fm.getAscent() + fm.getDescent();
        mDescent    = fm.getDescent();

        if (mRotation == ROTATE_NONE) {
            mHeight = Math.max(mCharHeight, mIcon != null ? mIcon.getIconHeight() : 0);
            mWidth  = fm.stringWidth(mText) + (mIcon != null ? (mIcon.getIconWidth() + mIconSpace) : 0);
        } // if
        else {
            mWidth  = Math.max(mCharHeight, mIcon != null ? mIcon.getIconHeight() : 0);
            mHeight = fm.stringWidth(mText) + (mIcon != null ? (mIcon.getIconWidth() + mIconSpace) : 0);
        } // else

        mWidth  += 2 * mInset;
        mHeight += 2 * mInset;
    }

    // Icon

    public int getIconWidth() {
        return mWidth;
    }

    public int getIconHeight() {
        return mHeight;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D)g;

        g2d.setColor(c.getForeground());
        g2d.setFont(c.getFont());

        if (mRotation == ROTATE_NONE) {
            if ( mIcon != null) {
                x += mInset;
                y += mInset;

                int iconHeight = mIcon.getIconHeight();

                mIcon.paintIcon(mComponent, g, x, y + (mHeight - 2 * mInset - iconHeight) / 2); // center vertically

                x += mIconSpace + mIcon.getIconWidth();
            } // if

            Object renderingHint = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.drawString(mText, x, mHeight- mDescent - mInset);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, renderingHint);

        } // if

        else if (mRotation == ROTATE_LEFT) { // Bottom up
            int translateX = x + mWidth  - mInset;
            int translateY = y + mHeight - mInset;

            g2d.translate(translateX, translateY);
            g2d.rotate(-NINETY_DEGREES);

            if (mIcon != null) {
                int iconHeight = mIcon.getIconHeight();
                int iconOffset = (mWidth - 2 * mInset - iconHeight) / 2; // center icon

                mIcon.paintIcon(mComponent, g2d, 0, -mIcon.getIconWidth() - iconOffset);
            } // if

            Object renderingHint = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.drawString(mText, mIcon != null ? mIcon.getIconHeight() + mIconSpace: 0, -mDescent);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, renderingHint);

            g2d.rotate(NINETY_DEGREES);
            g2d.translate(-translateX, -translateY);
        } // if

        else if (mRotation == ROTATE_RIGHT) { // top down
            int translateX = x + mInset;
            int translateY = y + mInset;

            g2d.translate(translateX, translateY);
            g2d.rotate(NINETY_DEGREES);

            if ( mIcon != null) {
                int iconHeight = mIcon.getIconHeight();
                int iconOffset = (mWidth - 2 * mInset - iconHeight) / 2; // center icon

                mIcon.paintIcon(mComponent, g2d, 0, -mIcon.getIconWidth() - iconOffset);
            } // if

            Object renderingHint = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.drawString(mText, mIcon != null ? mIcon.getIconHeight() + mIconSpace: 0, -mDescent);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, renderingHint);

            g2d.rotate(-NINETY_DEGREES);
            g2d.translate(-translateX, -translateY);
        } // if
    }

    public void validate() {
        calcDimensions();
    }
}