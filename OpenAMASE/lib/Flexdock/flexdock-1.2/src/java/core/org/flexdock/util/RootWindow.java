// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/* Copyright (c) 2004 Christopher M Butler

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
package org.flexdock.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.JWindow;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

/**
 * This class provides an abstraction of root containers used in Swing. It
 * allows transparent use of methods common to {@code JFrame}, {@code JApplet},
 * {@code JWindow}, and {@code JDialog} without making an outward distinction
 * between the different container types. This is accomplished by wrapping the
 * root component.
 *
 * @author Chris Butler
 */
public class RootWindow {
    public static final Integer DEFAULT_MAXED_LAYER = new Integer(
        JLayeredPane.PALETTE_LAYER.intValue() - 10);

    private static final Map MAP_BY_ROOT_CONTAINER = new WeakHashMap();

    private LayoutManager maxedLayout;

    private Integer maximizationLayer;

    private WeakReference root;

    private HashMap clientProperties;

    private static Component getRoot(Component c) {
        if (c == null)
            return null;

        if (isValidRootContainer(c))
            return c;

        Container parent = c.getParent();
        while (parent != null && !isValidRootContainer(parent))
            parent = parent.getParent();

        return parent;
    }

    /**
     * Traverses the container hierarchy to locate the root container and
     * returns corresponding {@code RootSwingContainer}. If {@code c} is
     * {@code null}, a {@code null} reference is returned.
     *
     * @param c
     *            the container whose root we wish to find
     * @return the enclosing {@code RootSwingcontainer}
     */
    public static RootWindow getRootContainer(Component c) {
        Component root = getRoot(c);
        if (!isValidRootContainer(root))
            return null;

        RootWindow container = (RootWindow) MAP_BY_ROOT_CONTAINER.get(root);
        if (container == null) {
            container = new RootWindow(root);
            MAP_BY_ROOT_CONTAINER.put(root, container);
        }

        if (container.getRootContainer() != root)
            container.setRootContainer(root);

        return container;
    }

    /**
     * Indicates whether the supplied {@code Component} is, in fact, a root
     * Swing container.
     *
     * @param c
     *            the {@code Component} we wish to check
     */
    public static boolean isValidRootContainer(Component c) {
        return c != null
               && (c instanceof JFrame || c instanceof JApplet
                   || c instanceof JWindow || c instanceof JDialog);
    }

    public static RootWindow[] getVisibleWindows() {
        Frame[] frames = Frame.getFrames();
        HashSet cache = new HashSet(frames.length);
        for (int i = 0; i < frames.length; i++)
            populateWindowList(new RootWindow(frames[i]), cache, true);
        return (RootWindow[]) cache.toArray(new RootWindow[0]);
    }

    private static void populateWindowList(RootWindow win, HashSet winCache,
                                           boolean visOnly) {
        if (win == null || winCache.contains(win))
            return;

        if (visOnly && !win.getRootContainer().isVisible())
            return;

        winCache.add(win);
        Window[] children = win.getOwnedWindows();
        for (int i = 0; i < children.length; i++)
            populateWindowList(new RootWindow(children[i]), winCache, visOnly);
    }

    /**
     * Creates a new {@code RootSwingContainer} wrapping the specified
     * component.
     */
    protected RootWindow(Component root) {
        setMaximizationLayer(DEFAULT_MAXED_LAYER);
        setRootContainer(root);
        clientProperties = new HashMap();
    }

    /**
     * Returns the {@code contentPane} object for the wrapped component.
     *
     * @return the {@code contentPane} property
     */
    public Container getContentPane() {
        Container c = null;

        if (getRootContainer() instanceof RootPaneContainer) {
            c = ((RootPaneContainer) getRootContainer()).getContentPane();
        }

        return c;
    }

    /**
     * Returns the {@code glassPane} object for the wrapped component.
     *
     * @return the {@code glassPane} property
     */
    public Component getGlassPane() {
        Component c = null;

        if (getRootContainer() instanceof RootPaneContainer) {
            c = ((RootPaneContainer) getRootContainer()).getGlassPane();
        }

        return c;
    }

    /**
     * Returns the {@code layeredPane} object for the wrapped component.
     *
     * @return the {@code layeredPane} property
     */
    public JLayeredPane getLayeredPane() {
        JLayeredPane pane = null;

        if (getRootContainer() instanceof RootPaneContainer) {
            pane = ((RootPaneContainer) getRootContainer()).getLayeredPane();
        }

        return pane;
    }

    /**
     * Gets the location of the wrapped component in the form of a point
     * specifying the component's top-left corner in the screen's coordinate
     * space.
     *
     * @return An instance of {@code Point} representing the top-left corner of
     *         the component's bounds in the coordinate space of the screen.
     */
    public Point getLocationOnScreen() {
        return getRootContainer().getLocationOnScreen();
    }

    /**
     * Returns the layer associated with {@code Component} maximization.
     *
     * @return an {@code Integer} indicating the maximization layer property
     * @deprecated dead code last used in 0.2.0
     */
    public Integer getMaximizationLayer() {
        return maximizationLayer;
    }

    /**
     * Returns the {@code LayoutManager} associated with {@code Component}
     * maximization within the {@code RootSwingContainer}.
     *
     * @return a {@code LayoutManager} indicating the maximization layout
     *         property
     * @deprecated dead code last used in 0.2.0
     */
    public LayoutManager getMaximizedLayout() {
        return maxedLayout;
    }

    /**
     * Returns the the wrapped component. ({@code JFrame}, {@code JApplet},
     * etc...)
     *
     * @return the wrapped root container
     */
    public Component getRootContainer() {
        return (Component) root.get();
    }

    /**
     * Returns the {@code rootPane} object for the wrapped component.
     *
     * @return the {@code rootPane} property
     */
    public JRootPane getRootPane() {
        JRootPane pane = null;

        if (getRootContainer() instanceof RootPaneContainer) {
            pane = ((RootPaneContainer) getRootContainer()).getRootPane();
        }

        return pane;
    }

    /**
     * Convenience method that calls {@code revalidate()} on the current content
     * pane if it is a {@code JComponent}. If not, no action is taken.
     */
    public void revalidateContentPane() {
        Container c = getContentPane();
        if (c instanceof JComponent)
            ((JComponent) c).revalidate();
    }

    /**
     * Sets the {@code contentPane} property for the wrapped component.
     *
     * @param contentPane
     *            the {@code contentPane} object for the wrapped component
     */
    public void setContentPane(Container contentPane) {
        if (getRootContainer() instanceof RootPaneContainer) {
            ((RootPaneContainer) getRootContainer()).setContentPane(contentPane);
        }
    }

    /**
     * Sets the {@code glassPane} property for the wrapped component.
     *
     * @param glassPane
     *            the {@code glassPane} object for the wrapped component
     */
    public void setGlassPane(Component glassPane) {
        if (getRootContainer() instanceof RootPaneContainer) {
            ((RootPaneContainer) getRootContainer()).setGlassPane(glassPane);
        }
    }

    /**
     * Sets the {@code layeredPane} property for the wrapped component.
     *
     * @param layeredPane
     *            the {@code layeredPane} object for the wrapped component
     */
    public void setLayeredPane(JLayeredPane layeredPane) {
        if (getRootContainer() instanceof RootPaneContainer) {
            ((RootPaneContainer) getRootContainer()).setLayeredPane(layeredPane);
        }
    }

    /**
     * Return an array containing all the windows this window currently owns.
     *
     * @return all the windows currently owned by this root window.
     */
    public Window[] getOwnedWindows() {
        if (getRootContainer() instanceof JFrame)
            return ((JFrame) getRootContainer()).getOwnedWindows();
        else if (getRootContainer() instanceof JWindow)
            return ((JWindow) getRootContainer()).getOwnedWindows();
        else if (getRootContainer() instanceof JDialog)
            return ((JDialog) getRootContainer()).getOwnedWindows();
        else
            return new Window[0];
    }

    /**
     * Sets the layer associated with {@code Component} maximization within the
     * {@code RootSwingContainer}. If {@code layer} is {@code null},
     * DEFAULT_MAXED_LAYER is used instead.
     *
     * @param layer
     *            an {@code Integer} indicating the maximization layer property
     * @deprecated dead code last used in 0.2.0
     */
    public void setMaximizationLayer(Integer layer) {
        if (layer == null)
            layer = DEFAULT_MAXED_LAYER;
        maximizationLayer = layer;
    }

    /**
     * Sets the {@code LayoutManager} associated with {@code Component}
     * maximization within the {@code RootSwingContainer}.
     *
     * @param mgr
     *            the {@code LayoutManager} associated with {@code Component}
     *            maximization within the {@code RootSwingContainer}.
     * @deprecated dead code last used in 0.2.0
     */
    public void setMaximizedLayout(LayoutManager mgr) {
        maxedLayout = mgr;
    }

    /**
     * Sets the wrapped root container.
     *
     * @param root
     *            the new wrapped root container
     */
    protected void setRootContainer(Component root) {
        this.root = new WeakReference(root);
    }

    public void updateComponentTreeUI() {
        SwingUtilities.updateComponentTreeUI(getRootContainer());
        pack();
    }

    public void pack() {
        Component root = getRootContainer();
        if (root instanceof JFrame)
            ((JFrame) root).pack();
        else if (root instanceof JWindow)
            ((JWindow) root).pack();
        else if (root instanceof JDialog)
            ((JDialog) root).pack();
    }

    public void toFront() {
        Component root = getRootContainer();
        if (root instanceof JFrame)
            ((JFrame) root).toFront();
        else if (root instanceof JWindow)
            ((JWindow) root).toFront();
        else if (root instanceof JDialog)
            ((JDialog) root).toFront();
    }

    public boolean isActive() {
        Component root = getRootContainer();
        if (root instanceof JFrame)
            return ((JFrame) root).isActive();
        else if (root instanceof JWindow)
            return ((JWindow) root).isActive();
        else if (root instanceof JDialog)
            return ((JDialog) root).isActive();
        return false;
    }

    public Window getOwner() {
        Component root = getRootContainer();
        if (root instanceof JFrame)
            return ((JFrame) root).getOwner();
        else if (root instanceof JWindow)
            return ((JWindow) root).getOwner();
        else if (root instanceof JDialog)
            return ((JDialog) root).getOwner();
        return null;
    }

    public Rectangle getBounds() {
        return getRootContainer().getBounds();
    }

    public void putClientProperty(Object key, Object value) {
        if (key == null)
            return;

        if (value == null)
            clientProperties.remove(key);
        else
            clientProperties.put(key, value);
    }

    public Object getClientProperty(Object key) {
        return key == null ? null : clientProperties.get(key);
    }
}
