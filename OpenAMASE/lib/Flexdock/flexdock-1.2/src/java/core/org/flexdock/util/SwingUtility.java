// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Aug 23, 2004
 */
package org.flexdock.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.flexdock.docking.DockingPort;
import org.flexdock.docking.defaults.DefaultDockingPort;
import org.flexdock.docking.defaults.DockingSplitPane;

import com.l2fprod.gui.plaf.skin.Skin;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;

/**
 * @author Christopher Butler
 */
public class SwingUtility {

    public static Component toComponent(Object obj) {
        return obj instanceof Component? (Component)obj: null;
    }

    public static void revalidate(Component comp) {
        if (comp instanceof JComponent)
            ((JComponent) comp).revalidate();
    }

    public static void repaint(Component comp) {
        if(comp instanceof JComponent)
            ((JComponent)comp).repaint();
    }

    public static void drawRect(Graphics g, Rectangle r) {
        if(g==null || r==null)
            return;

        g.drawRect(r.x, r.y, r.width, r.height);
    }

    public static DockingPort[] getChildPorts(DockingPort port) {
        if(!(port instanceof DefaultDockingPort))
            return new DockingPort[0];

        DefaultDockingPort parent = (DefaultDockingPort)port;
        Component docked = parent.getDockedComponent();
        if(!(docked instanceof JSplitPane))
            return new DockingPort[0];

        JSplitPane split = (JSplitPane)docked;
        DockingPort left = null;
        DockingPort right = null;
        if(split.getLeftComponent() instanceof DockingPort)
            left = (DockingPort)split.getLeftComponent();
        if(split.getRightComponent() instanceof DockingPort)
            right = (DockingPort)split.getRightComponent();

        if(left==null && right==null)
            return new DockingPort[0];
        if(left==null)
            return new DockingPort[] {right};
        if(right==null)
            return new DockingPort[] {left};
        return new DockingPort[] {left, right};

    }

    public static Point[] getPoints(Rectangle rect) {
        return getPoints(rect, null);
    }

    public static Point[] getPoints(Rectangle rect, Component convertFromScreen) {
        if(rect==null)
            return null;

        Rectangle r = (Rectangle)rect.clone();
        Point p = r.getLocation();
        if(convertFromScreen!=null)
            SwingUtilities.convertPointFromScreen(p, convertFromScreen);

        r.setLocation(p);

        return new Point[] {
                   p,
                   new Point(p.x + r.width, p.y),
                   new Point(p.x + r.width, p.y+r.height),
                   new Point(p.x, p.y+r.height)
               };
    }

    public static final void centerOnScreen(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = window.getSize();

        if (windowSize.height > screenSize.height)
            windowSize.height = screenSize.height;

        if (windowSize.width > screenSize.width)
            windowSize.width = screenSize.width;

        window.setLocation((screenSize.width - windowSize.width) / 2,
                           (screenSize.height - windowSize.height) / 2);
    }

    public static void center(Window window, Component parent) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Rectangle bounds = new Rectangle(parent.getLocationOnScreen(), parent.getSize());

        int w = window.getWidth();
        int h = window.getHeight();

        // center according to parent

        int x = ((int) bounds.getCenterX()) - w / 2;
        int y = ((int) bounds.getCenterY()) - h / 2;

        // does it fit on screen?

        if (x < 0)
            x = 0;
        else if (x + w > screenSize.getWidth())
            x = ((int) screenSize.getWidth()) - w;

        if (y < 0)
            y = 0;
        else if (y + h > screenSize.getHeight())
            y = ((int) screenSize.getHeight()) - h;

        // done

        window.setBounds(x, y, w, h);
    }

    public static Container getContentPane(Component c) {
        RootWindow rootWin = RootWindow.getRootContainer((Component)c);
        return rootWin==null? null: rootWin.getContentPane();
    }

    public static void setPlaf(Class lookAndFeelClass) {
        String className = lookAndFeelClass==null? null: lookAndFeelClass.getName();
        setPlaf(className);
    }

    public static void setPlaf(String lookAndFeelClassName) {
        if(loadSkinLF(lookAndFeelClassName))
            return;

        try {
            UIManager.setLookAndFeel(lookAndFeelClassName);
        } catch(Exception e) {
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean loadSkinLF(String themePack) {
        if(themePack==null || !themePack.endsWith(".zip") || !isSkinLFInstalled())
            return false;

        try {
            Skin skin = SkinLookAndFeel.loadThemePack(ResourceManager.getResource(themePack));
            SkinLookAndFeel.setSkin(skin);
            UIManager.setLookAndFeel(SkinLookAndFeel.class.getName());
            return true;
        } catch(Exception e) {
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void add(Point p1, Point p2) {
        if(p1!=null && p2!=null) {
            p1.x += p2.x;
            p1.y += p2.y;
        }
    }

    public static void subtract(Point p1, Point p2) {
        if(p1!=null && p2!=null) {
            p1.x -= p2.x;
            p1.y -= p2.y;
        }
    }

    public static void translate(Component src, Polygon poly, Component dest) {
        if(src==null || poly==null || dest==null)
            return;

        Rectangle srcRect = src.getBounds();
        srcRect.setLocation(0, 0);

        Rectangle destRect = SwingUtilities.convertRectangle(src, srcRect, dest);

        int deltaX = destRect.x - srcRect.x;
        int deltaY = destRect.y - srcRect.y;
        int len = poly.npoints;

        for(int i=0; i<len; i++) {
            poly.xpoints[i] += deltaX;
            poly.ypoints[i] += deltaY;
        }
    }

    public static void focus(final Component c) {
        RootWindow window = RootWindow.getRootContainer(c);
        if(window==null)
            return;

        Component root = window.getRootContainer();
        Component comp = c;
        for(Component parent=comp.getParent(); parent!=root; parent=comp.getParent()) {
            if(parent instanceof JTabbedPane) {
                ((JTabbedPane)parent).setSelectedComponent(comp);
            }
            comp = parent;
        }


        EventQueue.invokeLater(new Runnable() {
            public void run() {
                c.requestFocus();
            }
        });
    }


    public static Component getNearestFocusableComponent(Component c) {
        return getNearestFocusableComponent(c, null);
    }

    public static Component getNearestFocusableComponent(Component c, Container desiredRoot) {
        if(c==null)
            c = desiredRoot;
        if(c==null)
            c = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();

        boolean cachedFocusCycleRoot = false;
        // make the desiredRoot into a focusCycleRoot
        if(desiredRoot!=null) {
            cachedFocusCycleRoot = desiredRoot.isFocusCycleRoot();
            if(!cachedFocusCycleRoot)
                desiredRoot.setFocusCycleRoot(true);
        }

        Container focusRoot = null;
        if(c instanceof Container) {
            Container cnt = (Container)c;
            focusRoot = cnt.isFocusCycleRoot(cnt)? cnt: cnt.getFocusCycleRootAncestor();
        } else
            focusRoot = c.getFocusCycleRootAncestor();

        Component focuser = null;
        if (focusRoot != null)
            //zw, remarked - selected component should become focused
            //focuser = focusRoot.getFocusTraversalPolicy().getLastComponent(focusRoot);
            focuser = c; //zw, added - selected component should become focused

        // restore the desiredRoot to its previous state
        if(desiredRoot!=null && !cachedFocusCycleRoot) {
            desiredRoot.setFocusCycleRoot(cachedFocusCycleRoot);
        }
        return focuser;
    }

    public static void activateWindow(Component c) {
        RootWindow window = RootWindow.getRootContainer(c);
        if(window!=null && !window.isActive())
            window.toFront();
    }

    /* Color utility */

    /**
    * @param color
    * @param factor
    * @return a new color, brighter than the one passed as argument by a percentage factor
    *
    * <br>author Cyril Gambis  - [Mar 17, 2005]
    */
    public static Color brighter(Color color, double factor) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        int i = (int) (1.0 / (1.0 - factor));
        if (red == 0 && green == 0 && blue == 0) {
            return new Color(i, i, i);
        }
        if (red > 0 && red < i) {
            red = i;
        }
        if (green > 0 && green < i) {
            green = i;
        }
        if (blue > 0 && blue < i) {
            blue = i;
        }

        return new Color(Math.min((int) (red / factor), 255), Math.min((int) (green / factor), 255), Math.min((int) (blue / factor), 255));
    }

    /**
     * @param color
     * @param factor
     * @return a new color, darker than the one passed as argument by a percentage factor
     *
     * <br>author Cyril Gambis  - [Mar 17, 2005]
     */
    public static Color darker(Color color, double factor) {
        return new Color(Math.max((int) (color.getRed() * factor), 0), Math.max((int) (color.getGreen() * factor), 0), Math.max((int) (color.getBlue() * factor), 0));
    }

    /**
     * @param color
     * @return the grey color corresponding to the color passed as parameter
     *
     * <br>author Cyril Gambis  - [Mar 17, 2005]
     */
    public static Color grayScale(Color color) {
        int grayTone = ((color.getRed() + color.getGreen() + color.getBlue())/3);
        return new Color(grayTone, grayTone, grayTone);
    }

    public static BufferedImage createImage(Component comp) {
        if(comp==null)
            return null;

        BufferedImage image = (BufferedImage)comp.createImage(comp.getWidth(), comp.getHeight());
        Graphics g = image.createGraphics();
        comp.paintAll(g);
        return image;
    }

    public static float getDividerProportion(JSplitPane splitPane) {
        if(splitPane==null)
            return 0;

        int size = splitPane.getOrientation()==JSplitPane.HORIZONTAL_SPLIT? splitPane.getWidth(): splitPane.getHeight();
        int divLoc = splitPane.getDividerLocation();
        return size==0? 0: (float)divLoc/((float)size - splitPane.getDividerSize());
    }

    public static Component getOtherComponent(JSplitPane split, Component current) {
        if(split==null || current==null)
            return null;

        Component other = split.getLeftComponent();
        if(other==current)
            other = split.getRightComponent();
        return other;
    }

    public static void putClientProperty(Component c, Object key, Object value) {
        if(c instanceof JComponent) {
            ((JComponent)c).putClientProperty(key, value);
        }
    }


    public static Object getClientProperty(Component c, Object key) {
        if(c instanceof JComponent) {
            return ((JComponent)c).getClientProperty(key);
        }
        return null;
    }

    public static void removeClientProperty(Component c, Object key) {
        if(c instanceof JComponent) {
            ((JComponent)c).putClientProperty(key, null);
        }
    }

    public static Window getActiveWindow() {
        KeyboardFocusManager mgr = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        return mgr.getActiveWindow();
    }

    public static int getSplitPaneSize(JSplitPane splitPane) {
        if(splitPane==null)
            return 0;

        boolean horiz = splitPane.getOrientation()==JSplitPane.HORIZONTAL_SPLIT? true: false;
        return horiz? splitPane.getWidth(): splitPane.getHeight();
    }

    /**
     * Moves the supplied <code>JSplitPane</code> divider to the specified <code>proportion</code>.
     * Valid values for <code>proportion</code> range from <code>0.0F<code>
     * to <code>1.0F</code>.  For example, a <code>proportion</code> of <code>0.3F</code> will move the
     * divider to 30% of the "size" (<i>width</i> for horizontal split, <i>height</i> for vertical split) of the
     * split container that contains the specified <code>Dockable</code>.  If a <code>proportion</code> of less
     * than <code>0.0F</code> is supplied, the value </code>0.0F</code> is used.  If a <code>proportion</code>
     * greater than <code>1.0F</code> is supplied, the value </code>1.0F</code> is used.
     * <br/>
     * This method should be effective regardless of whether the split layout in question has been fully realized
     * and is currently visible on the screen.  This should alleviate common problems associated with setting
     * percentages of unrealized <code>Component</code> dimensions, which are initially <code>0x0</code> before
     * the <code>Component</code> has been rendered to the screen.
     * <br/>
     * If the specified <code>JSplitPane</code> is <code>null</code>, then this method returns with no action
     * taken.
     *
     * @param split the <code>JSplitPane</code> whose divider location is to be set.
     * @param proportion a double-precision floating point value that specifies a percentage,
     * from zero (top/left) to 1.0 (bottom/right)
     * @see #getSplitPaneSize(JSplitPane)
     * @see JSplitPane#setDividerLocation(double)
     */
    public static void setSplitDivider(final JSplitPane split, float proportion) {
        if(split==null)
            return;

        proportion = Math.max(0f, proportion);
        final float percent = Math.min(1f, proportion);
        int size = getSplitPaneSize(split);

        if(split.isVisible() && size>0 && EventQueue.isDispatchThread()) {
            split.setDividerLocation(proportion);
            split.validate();
            return;
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                setSplitDivider(split, percent);
            }
        });
    }

    public static boolean isSkinLFInstalled() {
        return LookAndFeelSettings.isSkinLFSupported();

    }

    public static void toggleFocus(int direction) {
	KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        Component focused = manager.getFocusOwner();
	Component newFocused = getNext(focused, direction);
	if (newFocused != null) {
	    SwingUtility.focus(newFocused);
	}
    }

    private static Component getNext(Component comp, int direction) {
	Component next = null;
	JTabbedPane tab;
	if (comp instanceof JTabbedPane) {
	    tab = (JTabbedPane) comp;
	} else {
	    tab = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, comp);
	}

	if (tab != null) {
	    int index = tab.getSelectedIndex();
	    if (direction > 0 && index < tab.getTabCount() - 1) {
		tab.setSelectedIndex(index + 1);
		next = tab;
	    } else if (direction <= 0 && index > 0) {
		tab.setSelectedIndex(index - 1);
		next = tab;
	    } else {
		DefaultDockingPort port = (DefaultDockingPort) SwingUtilities.getAncestorOfClass(DefaultDockingPort.class, tab);
		next = getNext(port, direction);
	    }
	} else {
	    DockingSplitPane pane = (DockingSplitPane) SwingUtilities.getAncestorOfClass(DockingSplitPane.class, comp);
	    if (pane == null) {
		return getFirstComponent(comp, direction);
	    }
	    
	    Component left = pane.getLeftComponent();
	    Component right = pane.getRightComponent();
	    
	    if (SwingUtilities.isDescendingFrom(comp, left)) {
		if (direction > 0) {
		    next = getFirstComponent(right, direction);
		} else {
		    DefaultDockingPort port = (DefaultDockingPort) SwingUtilities.getAncestorOfClass(DefaultDockingPort.class, pane);
		    next = getNext(port, direction);
		}
	    } else {
		if (direction > 0) {
		    DefaultDockingPort port = (DefaultDockingPort) SwingUtilities.getAncestorOfClass(DefaultDockingPort.class, pane);
		    next = getNext(port, direction);
		} else {
		    next = getFirstComponent(left, direction);
		}
	    }
	}

	return next;
    }

    private static Component getFirstComponent(Component c, int direction) {
	while (c instanceof DefaultDockingPort) {
	    c = ((DefaultDockingPort) c).getDockedComponent();
	    if (c instanceof DockingSplitPane) {
		DockingSplitPane pane = (DockingSplitPane) c;
		c = direction > 0 ? pane.getLeftComponent() : pane.getRightComponent();
	    }
	}

	return c;
    }
}
