// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/* Copyright (c) 2005 Andreas Ernst

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
package org.flexdock.dockbar;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.flexdock.dockbar.util.TextIcon;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.props.DockablePropertySet;
import org.flexdock.docking.state.MinimizationManager;
import org.flexdock.plaf.common.border.RoundedLineBorder;

/**
 * @author Andreas Ernst
 * @author Christopher Butler
 */
public class DockbarLabel extends JLabel implements MouseListener {
    private static final Insets[] INSETS = createInsets();
    private static final int[] ROTATIONS = createRotations();

    // instance data
    private String dockingId;

    private boolean mSelected = false;
//	private boolean mDragging = false;
    private RoundedLineBorder mBorder;
    private boolean mInPaint = false;
    private boolean mActive = false;
    private int mDefaultOrientation;


    private static Insets[] createInsets() {
        Insets[] insets = new Insets[5];
        insets[MinimizationManager.CENTER] = new Insets(1, 1, 1, 1);
        insets[MinimizationManager.LEFT] = new Insets(1, 1, 2, 1);
        insets[MinimizationManager.RIGHT] = new Insets(1, 1, 2, 1);
        insets[MinimizationManager.TOP] = new Insets(1, 1, 1, 2);
        insets[MinimizationManager.BOTTOM] = new Insets(1, 1, 1, 2);
        return insets;
    }

    private static int[] createRotations() {
        int[] rotations = new int[5];
        rotations[MinimizationManager.CENTER] = TextIcon.ROTATE_NONE;
        rotations[MinimizationManager.LEFT] = TextIcon.ROTATE_LEFT;
        rotations[MinimizationManager.RIGHT] = TextIcon.ROTATE_RIGHT;
        rotations[MinimizationManager.TOP] = TextIcon.ROTATE_NONE;
        rotations[MinimizationManager.BOTTOM] = TextIcon.ROTATE_NONE;
        return rotations;
    }

    public DockbarLabel(String dockableId) {
        this(dockableId, DockbarManager.DEFAULT_EDGE);
    }

    public DockbarLabel(String dockableId, int defaultOrientation) {
        dockingId = dockableId;

        mDefaultOrientation = Dockbar.getValidOrientation(defaultOrientation);
        mBorder = new RoundedLineBorder(Color.lightGray, 3);
        setBorder(new CompoundBorder(new EmptyBorder(new Insets(1, 1, 1, 1)), mBorder));

        addMouseListener(this);

        TextIcon icon = new TextIcon(this, 2, 1);
        setIcon(icon);
        updateIcon();
        icon.validate();
    }

    // stuff

    public Border getBorder() {
        return mInPaint ? null : super.getBorder();
    }

    // override

    public void paintComponent(Graphics g) {
        mInPaint = false;

        paintBorder(g);

        mInPaint = true;

        super.paintComponent(g);
    }

    public void paint(Graphics g) {
        mInPaint = true;

//		updateView();

        super.paint(g); // will call paintComponent, paintBorder

        mInPaint = false;
    }

    public void setActive(boolean active) {
        if (mActive != active) {
            mActive = active;

            updateBorder();

            repaint();
        } // if
    }

    private void updateBorder() {
        mBorder.setFilled(mSelected || mActive);
    }

    // protected

    protected void activate(boolean lock) {
        Dockbar dockbar = (Dockbar)SwingUtilities.getAncestorOfClass(Dockbar.class, this);
        if(dockbar!=null) {
            dockbar.activate(dockingId, lock);

        }
    }

    // private

    private void setSelected(boolean selected) {
        if (mSelected != selected) {
            mSelected = selected;

            updateBorder();

            repaint();
        } // if
    }

    protected void validateTree() {
        updateBorderInsets();
        updateIcon();
        super.validateTree();
    }

    private void updateIcon() {
        Object obj = getIcon();
        if(!(obj instanceof TextIcon))
            return;

        Dockable d = getDockable();
        DockablePropertySet p = d==null? null: d.getDockingProperties();
        if(p==null)
            return;

        int orientation = getOrientation();
        int rotation = ROTATIONS[orientation];
        Icon dockIcon = p.getDockbarIcon();
        if(dockIcon==null)
            dockIcon = p.getTabIcon();
        String text = p.getDockableDesc();

        TextIcon icon = (TextIcon)obj;
        icon.setIcon(dockIcon);
        icon.setText(text);
        icon.setRotation(rotation);
    }

    public void setIcon(Icon icon) {
        Icon currIcon = getIcon();
        if(currIcon instanceof TextIcon) {
            ((TextIcon)currIcon).setIcon(icon);
        } else {
            super.setIcon(icon);
        }
        revalidate();
    }

    public void setText(String text) {
        Icon currIcon = getIcon();
        if(currIcon instanceof TextIcon) {
            ((TextIcon)currIcon).setText(text);
        } else {
            super.setText(text);
        }
        revalidate();
    }


    private void updateBorderInsets() {
        Border border = super.getBorder();
        border = border instanceof CompoundBorder? ((CompoundBorder)border).getOutsideBorder(): null;
        EmptyBorder insetBorder = border instanceof EmptyBorder? (EmptyBorder)border: null;

        if(insetBorder!=null) {
            int orientation = getOrientation();
            Insets insets = INSETS[orientation];
            Insets borderInsets = insetBorder.getBorderInsets();
            borderInsets.top = insets.top;
            borderInsets.left = insets.left;
            borderInsets.bottom = insets.bottom;
            borderInsets.right = insets.right;
        }
    }

    public Dockable getDockable() {
        return DockingManager.getDockable(dockingId);
    }

    public int getOrientation() {
        Container cnt = getParent();
        if(cnt instanceof Dockbar)
            return ((Dockbar)cnt).getOrientation();
        return mDefaultOrientation;
    }

    public Dimension getPreferredSize() {
        Icon  tmp = getIcon();
        if(!(tmp instanceof TextIcon))
            return super.getPreferredSize();

        Insets insets = getInsets();
        TextIcon icon = (TextIcon)tmp;

        int w = insets.left + icon.getIconWidth() + insets.right;
        int h = insets.top + icon.getIconHeight() + insets.bottom;
        return new Dimension(w, h);
    }

    // override MouseListener

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {
        if(e.getButton() != MouseEvent.BUTTON1)
            return;

        activate(true);
    }

    public void mouseEntered(MouseEvent e) {
        activate(false);
    }

    public void mouseExited(MouseEvent e) {

    }



}

