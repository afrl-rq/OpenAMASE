// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 */
package org.flexdock.docking.defaults;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.util.DockingUtility;
import org.flexdock.util.SwingUtility;

/**
 * @author Christopher Butler
 */
@SuppressWarnings(value = { "serial" })
public class DockingSplitPane extends JSplitPane implements DockingConstants {
    protected DockingPort dockingPort;

    protected String region;

    protected boolean dividerLocDetermined;

    protected boolean controllerInTopLeft;

    protected double initialDividerRatio = .5;

    protected double percent = -1;

    private int dividerHashCode = -1;

    private boolean constantPercent;

    /**
     * Creates a new {@code DockingSplitPane} for the specified
     * {@code DockingPort} with the understanding that the resulting
     * {@code DockingSplitPane} will be used for docking a {@code Dockable} into
     * the {@code DockingPort's} specified {@code region}. Neither {@code port}
     * or {@code region} may be {@code null}. {@code region} must be a valid
     * docking region as defined by {@code isValidDockingRegion(String region)}.
     *
     * @param port
     *            the {@code DockingPort} for which this
     *            {@code DockingSplitPane} is to be created.
     * @param region
     *            the region within the specified {@code DockingPort} for which
     *            this {@code DockingSplitPane} is to be created.
     * @throws {@code IllegalArgumentException}
     *             if either {@code port} is {@code null} or }region} is
     *             {@code null} or invalid.
     * @see DockingManager#isValidDockingRegion(String)
     */
    public DockingSplitPane(DockingPort port, String region) {
        if (port == null)
            throw new IllegalArgumentException("'port' cannot be null.");
        if (!DockingManager.isValidDockingRegion(region))
            throw new IllegalArgumentException("'" + region
                                               + "' is not a valid region.");

        this.region = region;
        this.dockingPort = port;
        // the controlling item is in the topLeft if our new item (represented
        // by the "region" string) is NOT in the topLeft.
        controllerInTopLeft = !DockingUtility.isRegionTopLeft(region);

        // set the proper resize weight
        int weight = controllerInTopLeft ? 1 : 0;
        setResizeWeight(weight);

        addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent pce) {
                    if (constantPercent && getUI() instanceof BasicSplitPaneUI) {
                        BasicSplitPaneUI ui = (BasicSplitPaneUI) getUI();
                        if (dividerHashCode != ui.getDivider().hashCode()) {
                            dividerHashCode = ui.getDivider().hashCode();
                            ui.getDivider().addMouseListener(new MouseAdapter() {

                                    public void mouseReleased(MouseEvent e) {
                                        DockingSplitPane.this.percent = SwingUtility.getDividerProportion(DockingSplitPane.this);
                                        DockingSplitPane.this.setResizeWeight(percent);
                                    }
                                });
                        }
                    }
                }
            });

	getActionMap().put("toggleFocus", new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    SwingUtility.toggleFocus(+1);
		}
	    });
    }

    public void setConstantPercent(boolean cstPercent) {
        if (cstPercent != constantPercent) {
            constantPercent = cstPercent;
        }
    }

    public void resetToPreferredSizes() {
        Insets i = getInsets();

        if (getOrientation() == VERTICAL_SPLIT) {
            int h = getHeight() - i.top - i.bottom - getDividerSize();
            int topH = getTopComponent().getPreferredSize().height;
            int bottomH = getBottomComponent().getPreferredSize().height;
            int extraSpace = h - topH - bottomH;

            // we have more space than necessary; resize to give each at least
            // preferred size
            if (extraSpace >= 0) {
                setDividerLocation(i.top + topH
                                   + ((int) (extraSpace * getResizeWeight() + .5)));
            }

            // TODO implement shrinking excess space to ensure that one has
            // preferred and nothing more
        } else {
            int w = getWidth() - i.left - i.right - getDividerSize();
            int leftH = getLeftComponent().getPreferredSize().width;
            int rightH = getRightComponent().getPreferredSize().width;
            int extraSpace = w - leftH - rightH;

            // we have more space than necessary; resize to give each at least
            // preferred size
            if (extraSpace >= 0) {
                setDividerLocation(i.left + leftH
                                   + ((int) (extraSpace * getResizeWeight() + .5)));
            }

            // TODO implement shrinking excess space to ensure that one has
            // preferred and nothing more
        }
    }

    public void setDividerLocation(double percent) {
        this.percent = percent;
        super.setDividerLocation(percent);
        setResizeWeight(percent);
    }

    public double getPercent() {
        if (constantPercent) {
            return percent;
        }

        return -1;
    }

    protected boolean isDividerSizeProperlyDetermined() {
        if (getDividerLocation() != 0)
            return true;
        return dividerLocDetermined;
    }

    /**
     * Returns the 'oldest' {@code Component} to have been added to this
     * {@code DockingSplitPane} as a result of a docking operation. A
     * {@code DockingSplitPane} is created based upon the need to share space
     * within a {@code DockingPort} between two {@code Dockables}. This happens
     * when a new {@code Dockable} is introduced into an outer region of a
     * {@code DockingPort} that already contains a {@code Dockable}. The
     * {@code Dockable} that was in the {@code DockingPort} prior to splitting
     * the layout is the 'elder' {@code Component} and, in many circumstances,
     * may be used to control initial divider location and resize weight.
     * <p>
     * If this split pane contains {@code DockingPorts} as its child components,
     * then this method will return the {@code Component} determined by calling
     * {@code getDockedComponent()} for the {@code DockingPort} in this split
     * pane's elder region.
     * <p>
     * The elder region of this {@code DockingSplitPane} is determined using the
     * value returned from {@code getRegion()}, where {@code getRegion()}
     * indicates the docking region of the 'new' {@code Dockable} for this
     * {@code DockingSplitPane}.
     *
     * @return the 'oldest' {@code Component} to have been added to this
     *         {@code DockingSplitPane} as a result of a docking operation.
     * @see #getRegion()
     * @see DockingPort#getDockedComponent()
     */
    public Component getElderComponent() {
        Component c = controllerInTopLeft ? getLeftComponent()
            : getRightComponent();
        if (c instanceof DockingPort)
            c = ((DockingPort) c).getDockedComponent();
        return c;
    }

    /**
     * Returns the docking region for which this {@code DockingSplitPane} was
     * created. A {@code DockingSplitPane} is created based upon the need to
     * share space within a {@code DockingPort} between two {@code Dockables}.
     * This happens when a new {@code Dockable} is introduced into an outer
     * region of a {@code DockingPort} that already contains a {@code Dockable}.
     * This method returns that outer region for which this
     * {@code DockingSplitPane} was created and may be used to control the
     * orientation of the split pane. The region returned by this method will be
     * the same passed into the {@code DockingSplitPane} constructor on
     * instantiation.
     *
     * @return the docking region for which this {@code DockingSplitPane} was
     *         created.
     * @see #DockingSplitPane(DockingPort, String)
     */
    public String getRegion() {
        return region;
    }

    /**
     * Indicates whether the 'oldest' {@code Component} to have been added to
     * this {@code DockingSplitPane} as a result of a docking operation is in
     * the TOP or LEFT side of the split pane. A {@code DockingSplitPane} is
     * created based upon the need to share space within a {@code DockingPort}
     * between two {@code Dockables}. This happens when a new {@code Dockable}
     * is introduced into an outer region of a {@code DockingPort} that already
     * contains a {@code Dockable}. The {@code Dockable} that was in the
     * {@code DockingPort} prior to splitting the layout is the 'elder'
     * {@code Component} and is returned by {@code getElderComponent()}. This
     * method indicates whether or not that {@code Component} is in the TOP or
     * LEFT side of this {@code DockingSplitPane}.
     * <p>
     * The elder region of this {@code DockingSplitPane} is determined using the
     * value returned from {@code getRegion()}, where {@code getRegion()}
     * indicates the docking region of the 'new' {@code Dockable} for this
     * {@code DockingSplitPane}.
     *
     * @return {@code true} if the 'oldest' {@code Component} to have been added
     *         to this {@code DockingSplitPane} is in the TOP or LEFT side of
     *         the split pane; {@code false} otherwise.
     * @see #getElderComponent()
     * @see #getRegion()
     */
    public boolean isElderTopLeft() {
        return controllerInTopLeft;
    }

    /**
     * Overridden to ensure proper divider location on initial rendering.
     * Sometimes, a split divider location is set as a proportion before the
     * split pane itself has been fully realized in the container hierarchy.
     * This results in a layout calculation based on a proportion of zero width
     * or height, rather than the desired proportion of width or height after
     * the split pane has been fully rendered. This method ensures that default
     * {@code JSplitPane} layout behavior is deferred until after the initial
     * dimensions of this split pane have been properly determined.
     *
     * @see java.awt.Container#doLayout()
     * @see JSplitPane#setDividerLocation(double)
     */
    public void doLayout() {
        // if they setup the docking configuration while the application
        // was first starting up, then the dividerLocation was calculated before
        // the container tree was visible, sized, validated, etc, so it'll be
        // stuck at zero. in that case, redetermine the divider location now
        // that we have valid container bounds with which to work.
        if (!isDividerSizeProperlyDetermined()) {
            // make sure this can only run once so we don't get a StackOverflow
            dividerLocDetermined = true;
            setDividerLocation(initialDividerRatio);
        }
        // continue the layout
        super.doLayout();
    }

    /**
     * Releases any internal references to external objects to aid garbage
     * collection. This method is {@code public} and may be invoked manually for
     * proactive memory management. Otherwise, this method is invoked by this
     * {@code DockingSplitPane's} {@code finalize()} method.
     */
    public void cleanup() {
        dockingPort = null;
    }

    /**
     * Sets the initial divider ration for creating split panes. The default
     * value is {@code 0.5}.
     *
     * @exception IllegalArgumentException
     *                if {@code ratio} is less than 0.0 or greater than 1.0.
     * @param ratio
     *            a ratio for determining weighting between the two sides of a
     *            split pane.
     */
    public void setInitialDividerRatio(double ratio) {
        if (ratio < 0.0 || ratio > 1.0) {
            throw new IllegalArgumentException("ratio (" + ratio
                                               + ") must be between [0.0,1,0] inclusive");
        }
        initialDividerRatio = ratio;
    }

    protected void finalize() throws Throwable {
        cleanup();
        super.finalize();
    }
}
