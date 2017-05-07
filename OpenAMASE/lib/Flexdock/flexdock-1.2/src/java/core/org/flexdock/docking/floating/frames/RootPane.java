// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

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

package org.flexdock.docking.floating.frames;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

/**
 * @author Andreas Ernst
 * @author Christopher Butler
 */
@SuppressWarnings(value = { "serial" })
public class RootPane extends JRootPane implements MouseListener, MouseMotionListener {
    private static int CORNER_MARGIN = 5;

    // instance data
    private int currentResizeRegion;
    private Rectangle mMouseLimits;
    private Cursor mLastCursor;
    private Point offset;
    private DockingFrame frame;

    // constructor

    RootPane(DockingFrame frame) {
        this.frame = frame;

        setBorder(new RootPaneBorder());
        addMouseListener(this);
        addMouseMotionListener(this);
        offset = new Point();
    }

    // private

    void ensureValidScreenBounds(Point attemptedPoint) {
        // 'attemptedPoint' is the current "attempted" mousepoint based off of a drag-event.
        // here, we check to see if the attempted mousepoint exceeds our valid minimum frame
        // bounds such that 'attemptedPoint' logically expresses a new frame-rect which
        // is smaller than the accepted minimum bounds for our frame.  if 'attemptedPoint'
        // exceeds any of these bounds, we adjust the values to match the ceiling/floor
        // of the acceptable frame-rect appropriately before returning and actually resizing
        // the frame.

        // x
        if (attemptedPoint.x < mMouseLimits.x)
            attemptedPoint.x = mMouseLimits.x;
        else if (attemptedPoint.x > (mMouseLimits.x + mMouseLimits.width))
            attemptedPoint.x = mMouseLimits.x + mMouseLimits.width;

        // y
        if (attemptedPoint.y < mMouseLimits.y)
            attemptedPoint.y = mMouseLimits.y;
        else if (attemptedPoint.y > (mMouseLimits.y + mMouseLimits.height))
            attemptedPoint.y = mMouseLimits.y + mMouseLimits.height;
    }

    void computeMouseLimits(Point p) {
        // Called at the very start of a drag operation on the pane edges, not
        // repeatedly during the drag.  This method computes two things:
        // 1) The acceptable mouse limits (Rectangle) during the drag.  These have
        //    different meanings, depending on which corner or edge is being dragged,
        //    but generally translate into bounds that prevent us from doing things
        //    like, for instance, dragging the SOUTH_EAST corner anywhere above the
        //    NORTH or left of WEST frame edges (taking min-frame-size into account).
        // 2) The current mouse offset relative to the frame-edge about to be dragged.
        //    Subsequent MOUSE_DRAGGED events will report the location of the mouse,
        //    not the actual frame-edge.  We will use this offset to translate from the
        //    mouse to the frame-edge.

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mMouseLimits = new Rectangle(0, 0, screenSize.width, screenSize.height);
        Rectangle currFrameRect = frame.getBounds(); // in screen-coordinates
        Dimension minSize = getLayout().minimumLayoutSize(getParent());

        int minWidth = minSize.width;
        int minHeight = minSize.height;

        switch (currentResizeRegion) {
        case Cursor.NW_RESIZE_CURSOR:
            mMouseLimits.width = currFrameRect.x + currFrameRect.width - minWidth - mMouseLimits.x;
            mMouseLimits.height = currFrameRect.y + currFrameRect.height - minHeight - mMouseLimits.y;
            offset.setLocation(-p.x, -p.y);
            break;

        case Cursor.N_RESIZE_CURSOR:
            mMouseLimits.height = currFrameRect.y + currFrameRect.height - minHeight - mMouseLimits.y;
            offset.setLocation(0, -p.y);
            break;

        case Cursor.NE_RESIZE_CURSOR:
            mMouseLimits.x = currFrameRect.x + minWidth;
            mMouseLimits.height = currFrameRect.y + currFrameRect.height - minHeight - mMouseLimits.y;
            offset.setLocation(getWidth() - p.x, -p.y);
            break;


        case Cursor.E_RESIZE_CURSOR:
            mMouseLimits.x = currFrameRect.x + minWidth;
            offset.setLocation(getWidth() - p.x, 0);
            break;

        case Cursor.SE_RESIZE_CURSOR:
            mMouseLimits.y = currFrameRect.y + minHeight;
            mMouseLimits.x = currFrameRect.x + minWidth;
            offset.setLocation(getWidth() - p.x, getHeight() - p.y);
            break;

        case Cursor.S_RESIZE_CURSOR:
            mMouseLimits.y = currFrameRect.y + minHeight;
            offset.setLocation(0, getHeight() - p.y);
            break;

        case Cursor.SW_RESIZE_CURSOR:
            mMouseLimits.y = currFrameRect.y + minHeight;
            mMouseLimits.width = currFrameRect.x + currFrameRect.width - minWidth - mMouseLimits.x;
            offset.setLocation(-p.x, getHeight() - p.y);
            break;

        case Cursor.W_RESIZE_CURSOR:
            mMouseLimits.width = currFrameRect.x + currFrameRect.width - minWidth - mMouseLimits.x;
            offset.setLocation(-p.x, 0);
            break;

        } // switch
    }

    private int getCursor(Point p) {
        Insets insets = getInsets();

        // left

        if (p.x <= insets.left) {
            if (p.y <= CORNER_MARGIN)
                return Cursor.NW_RESIZE_CURSOR;
            else if (p.y >= getHeight() - CORNER_MARGIN)
                return Cursor.SW_RESIZE_CURSOR;
            else
                return Cursor.W_RESIZE_CURSOR;
        } // if

        // right

        else if (p.x >= getWidth() - insets.right) {
            if (p.y <= CORNER_MARGIN)
                return Cursor.NE_RESIZE_CURSOR;
            else if (p.y >= getHeight() - CORNER_MARGIN)
                return Cursor.SE_RESIZE_CURSOR;
            else
                return Cursor.E_RESIZE_CURSOR;
        } // if

        // top

        else if (p.y <= insets.top) {
            if (p.x <= CORNER_MARGIN)
                return Cursor.NW_RESIZE_CURSOR;
            else if (p.x >= getWidth() - CORNER_MARGIN)
                return Cursor.NE_RESIZE_CURSOR;
            else
                return Cursor.N_RESIZE_CURSOR;
        } // if

        // bottom

        else if (p.y >= getHeight() - insets.bottom) {
            if (p.x <= CORNER_MARGIN)
                return Cursor.SW_RESIZE_CURSOR;
            else if (p.x >= getWidth() - CORNER_MARGIN)
                return Cursor.SE_RESIZE_CURSOR;
            else
                return Cursor.S_RESIZE_CURSOR;
        } // if

        else
            return Cursor.DEFAULT_CURSOR;
    }







    // implement MouseListener, MouseMotionListener

    public void mousePressed(MouseEvent e) {
        currentResizeRegion = getCursor(e.getPoint());
        computeMouseLimits(e.getPoint());
    }

    public void mouseDragged(MouseEvent e) {
        if(currentResizeRegion==0)
            return;

        Point p = (Point) e.getPoint().clone();

        p.x += offset.x;
        p.y += offset.y;

        SwingUtilities.convertPointToScreen(p, this);

        ensureValidScreenBounds(p);

        Rectangle bounds = frame.getBounds();

        switch (currentResizeRegion) {
        case Cursor.NW_RESIZE_CURSOR:
            frame.setBounds(p.x, p.y, bounds.width + bounds.x - p.x, bounds.height + bounds.y - p.y);
            break;

        case Cursor.N_RESIZE_CURSOR:
            frame.setBounds(bounds.x, p.y, bounds.width, bounds.height + bounds.y - p.y);
            break;

        case Cursor.NE_RESIZE_CURSOR:
            frame.setBounds(bounds.x, p.y, p.x - bounds.x, bounds.height + bounds.y - p.y);
            break;

        case Cursor.W_RESIZE_CURSOR:
            frame.setBounds(p.x, bounds.y, bounds.x + bounds.width - p.x, bounds.height);
            break;

        case Cursor.E_RESIZE_CURSOR:
            frame.setBounds(bounds.x, bounds.y, p.x - bounds.x, bounds.height);
            break;

        case Cursor.SW_RESIZE_CURSOR:
            frame.setBounds(p.x, bounds.y, bounds.width + bounds.x - p.x, p.y - bounds.y);
            break;

        case Cursor.S_RESIZE_CURSOR:
            frame.setBounds(bounds.x, bounds.y, bounds.width, p.y - bounds.y);
            break;

        case Cursor.SE_RESIZE_CURSOR:
            frame.setBounds(bounds.x, bounds.y, p.x - bounds.x, p.y - bounds.y);
            break;
        } // switch

        setCursor(Cursor.getPredefinedCursor(currentResizeRegion));

        frame.validate();

    }

    public void mouseReleased(MouseEvent e) {
        currentResizeRegion = 0;
    }

    public void mouseMoved(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(getCursor(e.getPoint())));
    }

    public void mouseEntered(MouseEvent e) {
        if (currentResizeRegion == 0) // no dragging on!
            mLastCursor = getCursor();
    }

    public void mouseExited(MouseEvent e) {
        setCursor(mLastCursor);
    }

    // defaults

    public void mouseClicked(MouseEvent e) {
    }


    protected LayoutManager createRootLayout() {
        return new RootPaneLayout(this);
    }
}
