// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Feb 26, 2005
 */
package org.flexdock.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.DockingStrategy;
import org.flexdock.docking.defaults.DefaultDockingStrategy;
import org.flexdock.docking.event.DockingEvent;
import org.flexdock.docking.event.DockingListener;
import org.flexdock.docking.props.DockablePropertySet;
import org.flexdock.docking.props.PropertyManager;
import org.flexdock.plaf.PlafManager;
import org.flexdock.plaf.theme.ViewUI;
import org.flexdock.util.DockingUtility;
import org.flexdock.util.ResourceManager;
import org.flexdock.util.SwingUtility;

/**
 * The {@code View} class is slightly incompatible with {@code JComponent}.
 * Similar to JFC/Swing top-level containers, a {@code View} contains only a
 * content pane {@code Container} and a {@code Titlebar}. The <b>content pane</b>
 * should contain all the components displayed by the {@code View}. As a
 * convenience {@code add} and its variants, {@code remove(Component)} and
 * {@code setLayout} have been overridden to forward to the
 * {@code contentPane} as necessary. This means you can write:
 *
 * <pre>
 * view.add(child);
 * </pre>
 *
 * And the child will be added to the contentPane. The content pane will always
 * be non-null. Attempting to set it to null will cause the View to throw an
 * exception. The default content pane will not have a layout manager set.
 *
 * @see javax.swing.JFrame
 * @see javax.swing.JRootPane
 *
 * @author Christopher Butler
 * @author Karl Schaefer
 */
public class View extends JComponent implements Dockable, DockingConstants {
    protected class ViewLayout implements LayoutManager2, Serializable {
        /**
         * Returns the amount of space the layout would like to have.
         *
         * @param parent
         *            the Container for which this layout manager is being used
         * @return a Dimension object containing the layout's preferred size
         */
        public Dimension preferredLayoutSize(Container parent) {
            Dimension rd, tpd;
            Insets i = getInsets();

            if (contentPane != null) {
                rd = contentPane.getPreferredSize();
            } else {
                rd = parent.getSize();
            }
            if (titlepane != null && titlepane.isVisible()) {
                tpd = titlepane.getPreferredSize();
            } else {
                tpd = new Dimension(0, 0);
            }
            return new Dimension(Math.max(rd.width, tpd.width) + i.left
                                 + i.right, rd.height + tpd.height + i.top + i.bottom);
        }

        /**
         * Returns the minimum amount of space the layout needs.
         *
         * @param parent
         *            the Container for which this layout manager is being used
         * @return a Dimension object containing the layout's minimum size
         */
        public Dimension minimumLayoutSize(Container parent) {
            Dimension rd, tpd;
            Insets i = getInsets();
            if (contentPane != null) {
                rd = contentPane.getMinimumSize();
            } else {
                rd = parent.getSize();
            }
            if (titlepane != null && titlepane.isVisible()) {
                tpd = titlepane.getMinimumSize();
            } else {
                tpd = new Dimension(0, 0);
            }
            return new Dimension(Math.max(rd.width, tpd.width) + i.left
                                 + i.right, rd.height + tpd.height + i.top + i.bottom);
        }

        /**
         * Returns the maximum amount of space the layout can use.
         *
         * @param target
         *            the Container for which this layout manager is being used
         * @return a Dimension object containing the layout's maximum size
         */
        public Dimension maximumLayoutSize(Container target) {
            Dimension rd, tpd;
            Insets i = getInsets();
            if (titlepane != null && titlepane.isVisible()) {
                tpd = titlepane.getMaximumSize();
            } else {
                tpd = new Dimension(0, 0);
            }
            if (contentPane != null) {
                rd = contentPane.getMaximumSize();
            } else {
                // This is silly, but should stop an overflow error
                rd = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE - i.top
                                   - i.bottom - tpd.height - 1);
            }
            return new Dimension(Math.min(rd.width, tpd.width) + i.left
                                 + i.right, rd.height + tpd.height + i.top + i.bottom);
        }

        /**
         * Instructs the layout manager to perform the layout for the specified
         * container.
         *
         * @param parent
         *            the Container for which this layout manager is being used
         */
        public void layoutContainer(Container parent) {
            Rectangle b = parent.getBounds();
            Insets i = getInsets();
            int contentY = 0;
            int w = b.width - i.right - i.left;
            int h = b.height - i.top - i.bottom;

            if (titlepane != null && titlepane.isVisible()) {
                Dimension mbd = titlepane.getPreferredSize();
                titlepane.setBounds(0, 0, w, mbd.height);
                contentY += mbd.height;
            }
            if (contentPane != null) {
                contentPane.setBounds(0, contentY, w, h - contentY);
            }
        }

        public void addLayoutComponent(String name, Component comp) {
        }

        public void removeLayoutComponent(Component comp) {
        }

        public void addLayoutComponent(Component comp, Object constraints) {
        }

        public float getLayoutAlignmentX(Container target) {
            return 0.0f;
        }

        public float getLayoutAlignmentY(Container target) {
            return 0.0f;
        }

        public void invalidateLayout(Container target) {
        }
    }

    public static final String uiClassID = "Flexdock.view";

    public static final String ACTION_TOGGLE_NEXT = "toggleNextView";
    public static final String ACTION_TOGGLE_PREVIOUS = "togglePreviousView";

    static final DockingStrategy VIEW_DOCKING_STRATEGY = createDockingStrategy();

    private String persistentId;

    protected Titlebar titlepane;

    protected Container contentPane;

    protected boolean contentPaneCheckingEnabled;

    protected ArrayList dockingListeners;

    // protected boolean active;
    protected ArrayList dragSources;

    protected HashSet frameDragSources;

    private transient HashSet blockedActions;

    static {
        DockingManager.setDockingStrategy(View.class, VIEW_DOCKING_STRATEGY);
        PropertyManager.setDockablePropertyType(View.class, ViewProps.class);
    }

    public View(String persistentId) {
        this(persistentId, null);
    }

    public View(String persistentId, String title) {
        this(persistentId, title, null);
    }

    public View(String persistentId, String title, String tabText) {
        if (persistentId == null)
            throw new IllegalArgumentException(
                "The 'persistentId' parameter cannot be null.");

        this.persistentId = persistentId;

        dragSources = new ArrayList(1);
        frameDragSources = new HashSet(1);
        dockingListeners = new ArrayList(1);

        setContentPane(createContentPane());
        setTitlebar(createTitlebar());
        setLayout(createLayout());
        setContentPaneCheckingEnabled(true);

        if (title == null)
            title = "";
        setTitle(title);

        if (tabText == null)
            tabText = title;
        setTabText(tabText);

        addHierarchyListener(new HierarchyListener() {
            public void hierarchyChanged(HierarchyEvent e) {
                clearButtonRollovers();
            }
        });

        updateUI();

        DockingManager.registerDockable((Dockable) this);

	getActionMap().put(ACTION_TOGGLE_NEXT, new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    SwingUtility.toggleFocus(+1);
		}
	    });
	getActionMap().put(ACTION_TOGGLE_PREVIOUS, new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    SwingUtility.toggleFocus(-1);
		}
	    });
    }

    private static DockingStrategy createDockingStrategy() {
        return new DefaultDockingStrategy() {
            protected DockingPort createDockingPortImpl(DockingPort base) {
                return new Viewport();
            }
        };
    }

    public static View getInstance(String viewId) {
        Dockable view = DockingManager.getDockable(viewId);
        return view instanceof View ? (View) view : null;
    }

    protected Container createContentPane() {
        return new JPanel();
    }

    protected LayoutManager createLayout() {
        return new ViewLayout();
    }

    protected Titlebar createTitlebar() {
        Titlebar t = new Titlebar();
        t.setView(this);

        return t;
    }

    public Container getContentPane() {
        return contentPane;
    }

    public Titlebar getTitlebar() {
        return titlepane;
    }

    public DockablePropertySet getDockingProperties() {
        return PropertyManager.getDockablePropertySet(this);
    }

    public ViewProps getViewProperties() {
        return (ViewProps) getDockingProperties();
    }

    public void addAction(Action action) {
        if (titlepane != null)
            titlepane.addAction(action);
    }

    public void addAction(String action) {
        if (titlepane != null)
            titlepane.addAction(action);
    }

    public void removeActions() {
        if (titlepane != null)
            titlepane.removeAllActions();
    }

    public void setIcon(Icon icon) {
        if (titlepane != null)
            titlepane.setIcon(icon);
    }

    public void setIcon(String imgUri) {
        Icon icon = imgUri == null ? null : ResourceManager.createIcon(imgUri);
        setIcon(icon);
    }

    /**
     * Sets the content pane for this view.
     *
     * @param c
     *            the container to use as the content pane.
     * @throws IllegalArgumentException
     *             if {@code c} is {@code null} or if {@code c} is the
     *             {@code titlepane}.
     * @see #titlepane
     * @see #getTitlePane()
     */
    public void setContentPane(Container c) throws IllegalArgumentException {
        if (c == null)
            throw new IllegalArgumentException(
                "Unable to set a null content pane.");
        if (c == titlepane)
            throw new IllegalArgumentException(
                "Cannot use the same component as both content pane and titlebar.");

        if (contentPane != null) {
            remove(contentPane);
        }
        contentPane = c;
        if (contentPane != null) {
            boolean checkingEnabled = isContentPaneCheckingEnabled();
            try {
                setContentPaneCheckingEnabled(false);
                add(contentPane);
            } finally {
                setContentPaneCheckingEnabled(checkingEnabled);
            }
        }
    }

    protected String getPreferredTitlebarUIName() {
        return ui instanceof ViewUI ? ((ViewUI) ui).getPreferredTitlebarUI()
               : null;
    }

    public void setTitlebar(Titlebar titlebar) {
        if (titlebar != null && titlebar == contentPane) {
            throw new IllegalArgumentException(
                "Cannot use the same component as both content pane and titlebar.");
        }
        if (titlepane != null) {
            remove(titlepane);
            titlepane.setView(null);
            dragSources.remove(titlepane);
            frameDragSources.remove(titlepane);
            DockingManager.removeDragListeners(titlepane);
        }
        titlepane = titlebar;
        if (titlepane != null) {
            boolean checkingEnabled = isContentPaneCheckingEnabled();
            try {
                setContentPaneCheckingEnabled(false);
                add(titlepane);
            } finally {
                setContentPaneCheckingEnabled(checkingEnabled);
            }

            dragSources.add(titlepane);
            frameDragSources.add(titlepane);
            DockingManager.updateDragListeners(this);
        }

    }

    protected Component getTitlePane() {
        return titlepane;
    }

    public void setTitle(String title) {
        if (titlepane != null) {
            titlepane.setText(title);
        }
    }

    public void setTitle(String title, boolean alsoTabText) {
        setTitle(title);

        if (alsoTabText) {
            setTabText(title);
        }
    }

    public String getTitle() {
        Titlebar tbar = getTitlebar();
        return tbar == null ? null : tbar.getText();
    }

    public void updateUI() {
        setUI(PlafManager.getUI(this));
    }

    public String getUIClassID() {
        return uiClassID;
    }

    /**
     * {@inheritDoc}
     */
    protected void addImpl(Component comp, Object constraints, int index) {
        if (comp instanceof Titlebar)
            ((Titlebar) comp).setView(this);

        if (isContentPaneCheckingEnabled()) {
            getContentPane().add(comp, constraints, index);
        } else {
            super.addImpl(comp, constraints, index);
        }
    }

    public void remove(Component comp) {
        if (comp == contentPane) {
            super.remove(comp);
        } else {
            getContentPane().remove(comp);
        }
    }

    public AbstractButton getActionButton(String actionName) {
        Titlebar tbar = getTitlebar();
        return tbar == null ? null : tbar.getActionButton(actionName);
    }

    public Component getComponent() {
        return this;
    }

    public List getDragSources() {
        return dragSources;
    }

    public Set getFrameDragSources() {
        return frameDragSources;
    }

    public String getPersistentId() {
        return persistentId;
    }

    public boolean isTerritoryBlocked(Dockable dockable, String region) {
        return getDockingProperties().isTerritoryBlocked(region).booleanValue();
    }

    public void setTerritoryBlocked(String region, boolean blocked) {
        getDockingProperties().setTerritoryBlocked(region, blocked);
    }

    public String getTabText() {
        String txt = getDockingProperties().getDockableDesc();
        return txt == null ? getTitle() : txt;
    }

    public void setTabText(String tabText) {
        getDockingProperties().setDockableDesc(tabText);
    }

    public Icon getTabIcon() {
        return getDockingProperties().getTabIcon();
    }

    public void setTabIcon(Icon icon) {
        getDockingProperties().setTabIcon(icon);
    }

    public boolean dock(Dockable dockable) {
        return dock(dockable, CENTER_REGION);
    }

    public DockingPort getDockingPort() {
        return DockingManager.getDockingPort((Dockable) this);
    }

    public Dockable getSibling(String region) {
        return DefaultDockingStrategy.getSibling(this, region);
    }

    public Viewport getViewport() {
        DockingPort port = getDockingPort();
        return port instanceof Viewport ? (Viewport) port : null;
    }

    public boolean dock(Dockable dockable, String relativeRegion) {
        return DockingManager.dock(dockable, this, relativeRegion);
    }

    public boolean dock(Dockable dockable, String relativeRegion, float ratio) {
        return DockingManager.dock(dockable, this, relativeRegion, ratio);
    }

    public void setActive(boolean b) {
        getViewProperties().setActive(b);
    }

    public boolean isActive() {
        return getViewProperties().isActive().booleanValue();
    }

    public void setActiveStateLocked(boolean b) {
        getViewProperties().setActiveStateLocked(b);
    }

    public boolean isActiveStateLocked() {
        return getViewProperties().isActiveStateLocked().booleanValue();
    }

    public boolean isMinimized() {
        return DockingUtility.isMinimized(this);
    }

    public int getMinimizedConstraint() {
        return DockingUtility.getMinimizedConstraint(this);
    }

    public void addDockingListener(DockingListener listener) {
        dockingListeners.add(listener);
    }

    public DockingListener[] getDockingListeners() {
        return (DockingListener[]) dockingListeners
               .toArray(new DockingListener[0]);
    }

    public void removeDockingListener(DockingListener listener) {
        dockingListeners.remove(listener);
    }

    public void dockingCanceled(DockingEvent evt) {
    }

    public void dockingComplete(DockingEvent evt) {
        setActionBlocked(DockingConstants.PIN_ACTION, isFloating());
        if (titlepane != null)
            titlepane.revalidate();
    }

    public void dragStarted(DockingEvent evt) {
    }

    public void dropStarted(DockingEvent evt) {
    }

    public void undockingComplete(DockingEvent evt) {
        clearButtonRollovers();
    }

    public void undockingStarted(DockingEvent evt) {
    }

    private void clearButtonRollovers() {
        if (titlepane == null)
            return;

        Component[] comps = titlepane.getComponents();
        for (int i = 0; i < comps.length; i++) {
            Button button = comps[i] instanceof Button ? (Button) comps[i]
                            : null;
            if (button != null) {
                button.getModel().setRollover(false);
            }
        }
    }

    public void setActionBlocked(String actionName, boolean blocked) {
        if (actionName == null)
            return;

        Set actions = getBlockedActions();
        if (blocked)
            actions.add(actionName);
        else {
            if (actions != null)
                actions.remove(actionName);
        }
    }

    public boolean isActionBlocked(String actionName) {
        return actionName == null || blockedActions == null ? false
               : blockedActions.contains(actionName);
    }

    private HashSet getBlockedActions() {
        if (blockedActions == null)
            blockedActions = new HashSet(1);
        return blockedActions;
    }

    public boolean isFloating() {
        return DockingUtility.isFloating(this);
    }

    protected String paramString() {
        return "id=" + getPersistentId() + "," + super.paramString();
    }

    /**
     * @return the contentPaneCheckingEnabled
     */
    protected boolean isContentPaneCheckingEnabled() {
        return contentPaneCheckingEnabled;
    }

    /**
     * @param contentPaneCheckingEnabled
     *            the contentPaneCheckingEnabled to set
     */
    protected void setContentPaneCheckingEnabled(
        boolean contentPaneCheckingEnabled) {
        this.contentPaneCheckingEnabled = contentPaneCheckingEnabled;
    }

    /**
     * Sets the <code>LayoutManager</code>. Overridden to conditionally
     * forward the call to the <code>contentPane</code>.
     *
     * @param manager
     *            the <code>LayoutManager</code>
     * @see #setContentPaneCheckingEnabled
     */
    public void setLayout(LayoutManager manager) {
        if (isContentPaneCheckingEnabled()) {
            getContentPane().setLayout(manager);
        } else {
            super.setLayout(manager);
        }
    }
}
