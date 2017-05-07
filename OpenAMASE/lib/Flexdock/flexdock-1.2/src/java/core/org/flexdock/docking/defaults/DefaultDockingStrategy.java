// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 14, 2005
 */
package org.flexdock.docking.defaults;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.DockingStrategy;
import org.flexdock.docking.RegionChecker;
import org.flexdock.docking.defaults.DockingSplitPane;
import org.flexdock.docking.drag.DragManager;
import org.flexdock.docking.drag.DragOperation;
import org.flexdock.docking.event.DockingEvent;
import org.flexdock.docking.floating.frames.DockingFrame;
import org.flexdock.docking.floating.frames.FloatingDockingPort;
import org.flexdock.docking.props.DockablePropertySet;
import org.flexdock.docking.state.FloatManager;
import org.flexdock.event.EventManager;
import org.flexdock.util.DockingUtility;
import org.flexdock.util.RootWindow;
import org.flexdock.util.SwingUtility;

/**
 * @author Christopher Butler
 */
public class DefaultDockingStrategy implements DockingStrategy,
    DockingConstants {

    public static final String PREFERRED_PROPORTION = "DefaultDockingStrategy.PREFERRED_PROPORTION";

    private static double defaultResizeWeight = -1;
    private static boolean constantPercent;

    /**
     * Returns the specified {@code Dockable's} sibling {@code Dockable} within
     * the current docking layout. This method checks the parent
     * {@code DockingPort} of a given {@code Dockable} to see if it is split
     * equally with another {@code Dockable}. If so, the immediate sibling
     * {@code Dockable} is returned. If there are more than two
     * {@code Dockables} within the split layout, then the closest sibling
     * region is determined and this method dispatches to
     * {@code getSibling(Dockable dockable, String region)}.
     * <p>
     * If the specified {@code Dockable} is {@code null}, or there are no
     * siblings available in the docking layout, then this methdo returns a
     * {@code null} reference. If the specified {@code Dockable} is not
     * currently docked within a {@code DockingPort}, then this method returns
     * a {@code null} reference.
     *
     * @param dockable
     *            the {@code Dockable} whose sibling is to be returned
     * @return the sibling of the specified {@code Dockable} within the current
     *         docking layout.
     * @see Dockable#getDockingPort()
     * @see #getSibling(Dockable, String)
     */
    public static Dockable getSibling(Dockable dockable) {
        if (dockable == null)
            return null;

        DockingPort port = dockable.getDockingPort();
        String startRegion = findRegion(dockable.getComponent());
        String region = DockingUtility.flipRegion(startRegion);
        Dockable sibling = findDockable(port, dockable.getComponent(), region,
                                        startRegion);

        return sibling;
    }

    /**
     * Returns the sibling {@code Dockable} relative to the specified
     * {@code Dockable's} supplied region in the current docking layout. If
     * {@code dockable} is {@code null} or {@code region} is either invalid or
     * equal to {@code CENTER_REGION}, then this method returns a {@code null}
     * reference.
     * <p>
     * If the specified {@code Dockable} is in a {@code DockingPort} that
     * equally splits the layout between two {@code Dockables} in a fashion that
     * matches up with the specified region, then the immediate sibling
     * {@code Dockable} is returned. Otherwise, a fuzzy search is performed
     * throughout the docking layout for a {@code Dockable} that "looks like" it
     * is docked to the supplied region of the specified {@code Dockable} from a
     * visual standpoint.
     * <p>
     * For instance, a docking layout may consist of four quadrants <i>Dockable1</i>
     * (top-left), <i>Dockable2</i> (top-right), <i>Dockable3</i>
     * (bottom-left) and <i>Dockable4</i> (bottom-right). The layout is built
     * by docking <i>Dockable2>/i> to the {@code EAST_REGION} of <i>Dockable1</i>,
     * <i>Dockable3</i> to the {@code SOUTH_REGION} of <i>Dockable1</i>, and
     * <i>Dockable4</i> to the {@code SOUTH_REGION} of <i>Dockable2</i>.
     * Within this layout, <i>Dockable1</i> and <i>Dockable3</i> are immediate
     * siblings, as are <i>Dockable2</i> and <i>Dockable4</i>. Thus,
     * requesting sibling NORTH_REGION of <i>Dockable3</i> will easily yield
     * <i>Dockable1</i>. However, <i>Dockable3</i> has no immediate
     * {@code EAST_REGION} sibling. In this case, a fuzzy search through the
     * layout is performed to determine the visual sibling, and this method
     * returns <i>Dockable4</i>. Likewise, this method will return a
     * {@code null} reference for the {@code WEST_REGION} sibling of
     * <i>Dockable3}, since there are no {@code Dockables} in the visual layout
     * to the west of this {@code Dockable}.
     *
     * @param dockable
     *            the {@code Dockable} whose sibling is to be returned
     * @param region
     *            the region of the specified {@code Dockable} whose visual
     *            sibling is to be returned
     * @return the {@code Dockable} in the supplied region relative to the
     *         specified {@code Dockable}
     */
    public static Dockable getSibling(Dockable dockable, String region) {
        if (dockable == null || !DockingManager.isValidDockingRegion(region)
                || CENTER_REGION.equals(region))
            return null;

        DockingPort port = dockable.getDockingPort();
        String startRegion = findRegion(dockable.getComponent());
        Dockable sibling = findDockable(port, dockable.getComponent(), region,
                                        startRegion);

        return sibling;
    }

    private static Dockable findDockable(DockingPort port, Component self,
                                         String region, String startRegion) {
        if (port == null)
            return null;

        Component docked = port.getDockedComponent();
        // if we're not a split port, then there is no concept of 'outer
        // regions'.
        // jump up a level to find the parent split port
        if (!(docked instanceof JSplitPane)) {
            DockingPort superPort = DockingManager
                                    .getDockingPort((Component) port);
            return findDockable(superPort, self, region, startRegion);
        }

        Component sibling = port.getComponent(region);
        if (sibling == self) {
            if (!(self instanceof JSplitPane)) {
                DockingPort superPort = DockingManager
                                        .getDockingPort((Component) port);

                return findDockable(superPort, self, region, startRegion);
            }
            return null;
        }

        if (sibling instanceof JSplitPane) {
            // go one level deeper
            DockingPort subPort = DockingManager.getDockingPort(sibling);
            Component other = port.getComponent(DockingUtility
                                                .flipRegion(region));
            String subRegion = findSubRegion((JSplitPane) sibling, other,
                                             region, startRegion);
            return findDockable(subPort, self, subRegion, startRegion);
        }

        // if we have no direct sibling in the specified region, the jump
        // up a level.
        if (sibling == null) {
            DockingPort superPort = DockingManager
                                    .getDockingPort((Component) port);
            self = port.getDockedComponent();
            return findDockable(superPort, self, region, startRegion);
        }

        return DockingManager.getDockable(sibling);
    }

    private static String findSubRegion(JSplitPane split, Component other,
                                        String targetRegion, String baseRegion) {
        String region = DockingUtility.translateRegionAxis(split, targetRegion);
        if (!(other instanceof JSplitPane))
            return region;

        boolean translated = !targetRegion.equals(region);
        if (translated && !DockingUtility.isAxisEquivalent(region, baseRegion)) {
            region = DockingUtility.flipRegion(region);
        }

        return region;
    }

    /**
     * Returns the docking region within the current split docking layout
     * containing the specified {@code Component}. If {@code comp} is
     * {@code null}, then a {@code null} reference is returned. If {@code comp}
     * is not in a split layout, then {@code CENTER_REGION} is returned.
     * <p>
     * This method resolves the associated {@code Dockable} and
     * {@code DockingPort} for the specified {@code Component} and backtracks
     * through the docking layout to find a split layout. If a split layout is
     * found, then the region retured by this method is calculated relative to
     * its sibling in the layout.
     *
     * @param comp
     *            the {@code Component} whose region is to be returned
     * @return the region of the current split layout containing the specified
     *         {@code Dockable}
     */
    public static String findRegion(Component comp) {
        if (comp == null)
            return null;

        DockingPort port = DockingManager.getDockingPort(comp);
        Component docked = port.getDockedComponent();

        if (!(docked instanceof JSplitPane)) {
            // we didn't find a split pane, to check the grandparent dockingport
            DockingPort superPort = DockingManager
                                    .getDockingPort((Component) port);
            // if there was no grandparent DockingPort, then we're stuck with
            // the docked
            // component we already found. this can happen on the root
            // dockingport.
            docked = superPort == null ? docked : superPort
                     .getDockedComponent();
        }

        if (!(docked instanceof JSplitPane))
            return CENTER_REGION;

        JSplitPane split = (JSplitPane) docked;
        boolean horiz = split.getOrientation() == JSplitPane.HORIZONTAL_SPLIT;
        Component left = split.getLeftComponent();
        if (left == port) {
            return horiz ? WEST_REGION : NORTH_REGION;
        }
        return horiz ? EAST_REGION : SOUTH_REGION;

    }

    /**
     * Docks the specified {@code Dockable} into the supplied {@code region} of
     * the specified {@code DockingPort}. This method is meant for programmatic
     * docking, as opposed to realtime, event-based docking operations. As such,
     * it defers processing to
     * {@code dock(Dockable dockable, DockingPort port, String region, DragOperation token)},
     * passing a {@code null} argument for the {@code DragOperation} parameter.
     * This implies that there is no event-based drag operation currently in
     * progress to control the semantics of the docking operation, only that an
     * attempt should be made to dock the specified {@code Dockable} into the
     * specified {@code DockingPort}.
     * <p>
     * This method will return {@code false} if {@code dockable} or {@code port}
     * are {@code null}, or if {@code region} is not a valid region according
     * to the specified {@code DockingPort}. If a {@code Dockable} is currently
     * docked within the specified {@code DockingPort}, then that
     * {@code Dockable's} territorial properties are also checked and this
     * method may return {@code false} if the territory is blocked. Finally,
     * this method will return {@code false} if the specified {@code Dockable}
     * is already docked within the supplied region of the specified
     * <code.DockingPort}.
     *
     * @param dockable
     *            the {@code Dockable} we wish to dock
     * @param port
     *            the {@code DockingPort} into which we wish to dock
     * @param region
     *            the region of the specified {@code DockingPort} into which we
     *            wish to dock.
     * @return {@code true} if the docking operation was successful,
     *         {@code false}. otherwise.
     * @see #dock(Dockable, DockingPort, String, DragOperation)
     * @see Dockable#getDockingProperties()
     * @see DockablePropertySet#isTerritoryBlocked(String)
     */
    public boolean dock(Dockable dockable, DockingPort port, String region) {
        return dock(dockable, port, region, null);
    }

    /**
     * Docks the specified {@code Dockable} into the supplied {@code region} of
     * the specified {@code DockingPort}. This method is meant for realtime,
     * event-based docking based on an in-progress drag operation. It is not
     * recommended for developers to call this method programmatically, except
     * to pass in a {@code null} {@code DragOperation} argument. *
     * <p>
     * The {@code DragOperation} parameter, if present, will control the
     * semantics of the docking operation based upon current mouse position,
     * drag threshold, and a customizable drag context {@code Map}. For
     * instance, the {@code DragOperation} may contain information regarding the
     * {@code Dockable} over which the mouse is currently hovered, whether the
     * user is attempting to drag a {@code Dockable} outside the bounds of any
     * existing windows (perhaps in an attempt to float the {@code Dockable}),
     * or whether the current distance offset from the original drag point
     * sufficiently warrants a valid docking operation.
     * <p>
     * If the {@code DragOperation} is {@code null}, then this method will
     * attempt to programmatically dock the specified {@code Dockable} into the
     * supplied {@code region} of the specified {@code DockingPort} without
     * regard to external event-based criteria. This is in accordance with the
     * behavior specified by
     * {@code dock(Dockable dockable, DockingPort port, String region)}.
     *
     * This method will return {@code false} if {@code dockable} or {@code port}
     * are {@code null}, or if {@code region} is not a valid region according
     * to the specified {@code DockingPort}. If a {@code Dockable} is currently
     * docked within the specified {@code DockingPort}, then that
     * {@code Dockable's} territorial properties are also checked and this
     * method may return {@code false} if the territory is blocked. If a
     * {@code DragOperation} is present, then this method will return
     * {@code false} if the required drag threshold has not been exceeded.
     * Finally, this method will return {@code false} if the specified
     * {@code Dockable} is already docked within the supplied region of the
     * specified <code.DockingPort}.
     *
     * @param dockable
     *            the {@code Dockable} we wish to dock
     * @param port
     *            the {@code DockingPort} into which we wish to dock
     * @param region
     *            the region of the specified {@code DockingPort} into which we
     *            wish to dock.
     * @return {@code true} if the docking operation was successful,
     *         {@code false}. otherwise.
     * @see #dock(Dockable, DockingPort, String, DragOperation)
     * @see Dockable#getDockingProperties()
     * @see DockablePropertySet#isTerritoryBlocked(String)
     */
    public boolean dock(Dockable dockable, DockingPort port, String region,
                        DragOperation operation) {
        if (!isDockingPossible(dockable, port, region, operation))
            return false;

        if (!dragThresholdElapsed(operation))
            return false;

        // cache the old parent
        DockingPort oldPort = dockable.getDockingPort();

        // perform the drop operation.
        DockingResults results = dropComponent(dockable, port, region,
                                               operation);

        // perform post-drag operations
        DockingPort newPort = results.dropTarget;
        int evtType = results.success ? DockingEvent.DOCKING_COMPLETE
                      : DockingEvent.DOCKING_CANCELED;
        Map dragContext = DragManager.getDragContext(dockable);
        DockingEvent evt = new DockingEvent(dockable, oldPort, newPort,
                                            evtType, dragContext);
        // populate DockingEvent status info
        evt.setRegion(region);
        evt.setOverWindow(operation == null ? true : operation.isOverWindow());

        // notify the old docking port, new dockingport,and dockable
        Object[] evtTargets = { oldPort, newPort, dockable };
        EventManager.dispatch(evt, evtTargets);

        return results.success;
    }

    protected boolean dragThresholdElapsed(DragOperation token) {
        if (token == null || token.isPseudoDrag() || token.getStartTime() == -1)
            return true;

        long elapsed = System.currentTimeMillis() - token.getStartTime();
        // make sure the elapsed time of the drag is at least over .2 seconds.
        // otherwise, we'll probably be responding to inadvertent clicks (maybe
        // double-clicks)
        return elapsed > 200;
    }

    protected boolean isDockingPossible(Dockable dockable, DockingPort port,
                                        String region, DragOperation token) {
        // superclass blocks docking if the 'port' or 'region' are null. If
        // we've dragged outside
        // the bounds of the parent frame, then both of these will be null. This
        // is expected here and
        // we intend to float in this case.
        if (isFloatable(dockable, token))
            return true;

        // check to see if we're already floating and we're trying to drop into
        // the
        // same dialog.
        DockingPort oldPort = DockingManager.getDockingPort(dockable);
        if (oldPort instanceof FloatingDockingPort && oldPort == port) {
            // only allow this situation if we're not the *last* dockable
            // in the viewport. if we're removing the last dockable, then
            // the dialog will disappear before we redock, and we don't want
            // this
            // to happen.
            FloatingDockingPort floatingDockingPort = (FloatingDockingPort) oldPort;
            if (floatingDockingPort.getDockableCount() == 1)
                return false;
        }

        if (dockable == null || dockable.getComponent() == null || port == null)
            return false;

        if (!DockingManager.isValidDockingRegion(region))
            return false;

        Dockable docked = DockingManager.getDockable(port.getDockedComponent());
        if (docked == null)
            return true;

        // don't allow them to dock into this region if the territory there is
        // blocked.
        if (docked.getDockingProperties().isTerritoryBlocked(region)
                .booleanValue())
            return false;

        // check to see if we're already docked into this region.
        // get the parent dockingPort.
        Container container = docked.getComponent().getParent();
        // now get the grandparent dockingport
        DockingPort grandparent = DockingManager.getDockingPort(container);

        // if we don't share the grandparent dockingport, then we're definitely
        // not split in the same dockingport
        // across different region. in this case, it's ok to proceed with the
        // dock
        if (grandparent == null)
            return true;

        Component currentlyInRegion = grandparent.getComponent(region);
        // block docking if we're already the component docked within the
        // specified region
        if (currentlyInRegion == dockable.getComponent())
            return false;

        return true;
    }






    protected boolean isFloatable(Dockable dockable, DragOperation token) {
        // can't float null objects
        if (dockable == null || dockable.getComponent() == null
                || token == null)
            return false;

        // can't float on a fake drag operation
        if (token.isPseudoDrag())
            return false;

        // TODO: break this check out into a separate DropPolicy class.
        // should be any customizable criteria, not hardcoded to checking
        // for being outside the bounds of a window
        if (token.isOverWindow())
            return false;

        return true;
    }






    protected DockingResults dropComponent(Dockable dockable,
                                           DockingPort target, String region, DragOperation token) {
        if (isFloatable(dockable, token))
            return floatComponent(dockable, target, token);

        DockingResults results = new DockingResults(target, false);

        if (UNKNOWN_REGION.equals(region) || target == null) {
            return results;
        }

        Component docked = target.getDockedComponent();
        Component dockableCmp = dockable.getComponent();
        if (dockableCmp != null && dockableCmp == docked) {
            // don't allow docking the same component back into the same port
            return results;
        }

        // obtain a reference to the content pane that holds the target
        // DockingPort.
        // MUST happen before undock(), in case the undock() operation removes
        // the
        // target DockingPort from the container tree.
        Container contentPane = SwingUtility.getContentPane((Component) target);
        Point contentPaneLocation = token == null ? null : token
                                    .getCurrentMouse(contentPane);

        // undock the current Dockable instance from it's current parent
        // container
        undock(dockable);

        // when the original parent reevaluates its container tree after
        // undocking, it checks to see how
        // many immediate child components it has. split layouts and tabbed
        // interfaces may be managed by
        // intermediate wrapper components. When undock() is called, the docking
        // port
        // may decide that some of its intermedite wrapper components are no
        // longer needed, and it may get
        // rid of them. this isn't a hard rule, but it's possible for any given
        // DockingPort implementation.
        // In this case, the target we had resolved earlier may have been
        // removed from the component tree
        // and may no longer be valid. to be safe, we'll resolve the target
        // docking port again and see if
        // it has changed. if so, we'll adopt the resolved port as our new
        // target.
        if (contentPaneLocation != null && contentPane != null) {
            results.dropTarget = DockingUtility.findDockingPort(contentPane,
                                 contentPaneLocation);
            target = results.dropTarget;
        }

        results.success = target.dock(dockableCmp, region);
        SwingUtility.revalidate((Component) target);
        return results;
    }

    /**
     * Undocks the specified {@code Dockable} from it's parent
     * {@code DockingPort}. If {@code dockable} is {@code null} or is not
     * currently docked within a {@code DockingPort}, then this method returns
     * {@code false}.
     *
     * @param dockable
     *            the {@code Dockable} to be undocked.
     * @return {@code true} if the undocking operation was successful,
     *         {@code false} otherwise.
     * @see #dock(Dockable, DockingPort, String)
     */
    public boolean undock(Dockable dockable) {
        if (dockable == null)
            return false;

        Component dragSrc = dockable.getComponent();
        Container parent = dragSrc.getParent();
        RootWindow rootWin = RootWindow.getRootContainer(parent);

        // if there's no parent container, then we really don't have anything
        // from which to to
        // undock this component, now do we?
        if (parent == null)
            return false;

        boolean success = false;
        DockingPort dockingPort = DockingUtility.getParentDockingPort(dragSrc);

        // notify that we are about to undock
        Map dragContext = DragManager.getDragContext(dockable);
        DockingEvent dockingEvent = new DockingEvent(dockable, dockingPort,
                dockingPort, DockingEvent.UNDOCKING_STARTED, dragContext);
        EventManager.dispatch(dockingEvent);
        // if(dockingEvent.isConsumed())
        // return false;

        if (dockingPort != null) {
            // if 'dragSrc' is currently docked, then undock it instead of using
            // a
            // simple remove(). this will allow the DockingPort to do any of its
            // own
            // cleanup operations associated with component removal.
            success = dockingPort.undock(dragSrc);
        } else {
            // otherwise, just remove the component
            parent.remove(dragSrc);
            success = true;
        }

        if (rootWin != null) {
            SwingUtility.revalidate(rootWin.getContentPane());
            SwingUtility.repaint(rootWin.getContentPane());
        }

        if (success) {
            dockingEvent = new DockingEvent(dockable, dockingPort, dockingPort,
                                            DockingEvent.UNDOCKING_COMPLETE, dragContext);
            // notify the docking port and dockable
            Object[] evtTargets = { dockingPort, dockable };
            EventManager.dispatch(dockingEvent, evtTargets);
        }

        return success;
    }


    protected DockingResults floatComponent(Dockable dockable,
                                            DockingPort target, DragOperation token) {
        // otherwise, setup a new DockingFrame and retarget to the CENTER region
        DockingResults results = new DockingResults(target, false);

        // determine the bounds of the new frame
        Point screenLoc = token.getCurrentMouse(true);
        SwingUtility.add(screenLoc, token.getMouseOffset());
        Rectangle screenBounds = dockable.getComponent().getBounds();
        screenBounds.setLocation(screenLoc);

        // create the frame
        FloatManager mgr = DockingManager.getFloatManager();
        DockingFrame frame = mgr.floatDockable(dockable, dockable
                                               .getComponent(), screenBounds);

        // grab a reference to the frame's dockingPort for posterity
        results.dropTarget = frame.getDockingPort();

        results.success = true;
        return results;
    }


    protected static class DockingResults {
        public DockingResults(DockingPort port, boolean status) {
            dropTarget = port;
            success = status;
        }

        public DockingPort dropTarget;

        public boolean success;
    }














    /**
     * Returns a new {@code DefaultDockingPort} with characteristics similar to
     * the specified base {@code DockingPort}. If the base {@code DockingPort}
     * is a {@code DefaultDockingPort}, then the returned {@code DockingPort}
     * will share the base {@code DockingPort's} border manager and tabbed
     * drag-source flag. The returned {@code DockingPort's} {@code isRoot()}
     * method will return {@code false}.
     *
     * @param base
     *            the {@code DockingPort} off of which to base the returned
     *            {@code DockingPort}
     * @return a new {@code DefaultDockingPort} with characteristics similar to
     *         the specified base {@code DockingPort}.
     * @see DefaultDockingPort#getBorderManager()
     * @see DefaultDockingPort#setBorderManager(BorderManager)
     * @see DefaultDockingPort#isTabsAsDragSource()
     * @see DefaultDockingPort#setTabsAsDragSource(boolean)
     * @see DefaultDockingPort#setRoot(boolean)
     */
    public DockingPort createDockingPort(DockingPort base) {
        DockingPort port = createDockingPortImpl(base);

        if (port instanceof DefaultDockingPort
                && base instanceof DefaultDockingPort) {
            DefaultDockingPort newPort = (DefaultDockingPort) port;
            DefaultDockingPort ddp = (DefaultDockingPort) base;
            newPort.setBorderManager(ddp.getBorderManager());
            newPort.setTabsAsDragSource(ddp.isTabsAsDragSource());
            newPort.setRoot(false);
        }
        return port;
    }

    protected DockingPort createDockingPortImpl(DockingPort base) {
        return new DefaultDockingPort();
    }

    /**
     * Returns a new {@code DockingSplitPane} based on the specified
     * {@code DockingPort}. and region. Creation of the
     * {@code DockingSplitPane} is deferred to an internal protected method to
     * allow for overriding by subclasses. A client property is set on the
     * returned split pane with the key DockingConstants.REGION to indicate the
     * creation region of the split pane for non-{@code DockingSplitPanes}
     * returned by overriding subclasses.
     * <p>
     * This method determines the "elder" component of the split pane by
     * checking whether the new creation region is in the TOP or LEFT
     * (NORTH_REGION or WEST_REGION). If the creation region, representing where
     * the new {@code Dockable} will be docked, is <b>not</b> in the top or
     * left, then the elder {@code Component} in the split pane must be. This
     * information is used to initialize the resize weight of the split pane,
     * setting resize weight to {@code 1} if the elder is in the top or left of
     * the split pane and {@code 0} if not. This gives the elder
     * {@code Component} in the resulting split pane priority in the layout with
     * resizing the split pane.
     * <p>
     * If the creation region is {@code NORTH_REGION} or {@code SOUTH_REGION},
     * the returned split pane is initialized with a {@code VERTICAL_SPLIT}
     * orientation; otherwise a {@code HORIZONTAL_SPLIT} orientation is used.
     * <p>
     * Before returning, the border is removed from the split pane, its divider
     * size is set to 3, and if possible the border is removed from the split
     * pane divider. This is to avoid an excessive compound border effect for
     * embedded {@code Components} within the split pane that may have their own
     * borders.
     *
     * @param base
     *            the {@code DockingPort} off of which the returned
     *            {@code JSplitPane} will be based.
     * @param region
     *            the region within the base {@code DockingPort} used to
     *            determine the orientation of the returned {@code JSplitPane}.
     * @return a new {@code DockingSplitPane} based on the specified
     *         {@code DockingPort}. and region.
     * @see DockingSplitPane#DockingSplitPane(DockingPort, String)
     * @see #createSplitPaneImpl(DockingPort, String)
     * @see JSplitPane#setResizeWeight(double)
     */
    public JSplitPane createSplitPane(DockingPort base, String region) {
        JSplitPane split = createSplitPaneImpl(base, region);
        // mark the creation region on the split pane
        SwingUtility.putClientProperty(split, DockingConstants.REGION, region);

        double resizeWeight;
        if (defaultResizeWeight == -1) {
            // the creation region represents the "new" region, not the elder
            // region.
            // so if the creation region is NOT in the top left, then the elder
            // region is.
            boolean elderInTopLeft = !DockingUtility.isRegionTopLeft(region);
            resizeWeight = elderInTopLeft ? 1 : 0;
        } else {
            resizeWeight = defaultResizeWeight;
        }

        split.setResizeWeight(resizeWeight);
        if (constantPercent && split instanceof DockingSplitPane) {
            ((DockingSplitPane) split).setConstantPercent(true);
        }

        // determine the orientation
        int orientation = JSplitPane.HORIZONTAL_SPLIT;
        if (NORTH_REGION.equals(region) || SOUTH_REGION.equals(region))
            orientation = JSplitPane.VERTICAL_SPLIT;
        split.setOrientation(orientation);

        // remove the border from the split pane
        split.setBorder(null);

        // set the divider size for a more reasonable, less bulky look
        split.setDividerSize(3);
        split.setOneTouchExpandable(false);  //zw

        // check the UI. If we can't work with the UI any further, then
        // exit here.
        if (!(split.getUI() instanceof BasicSplitPaneUI))
            return split;

        // grab the divider from the UI and remove the border from it
        final BasicSplitPaneDivider divider = ((BasicSplitPaneUI) split.getUI())
                                              .getDivider();
        if (divider != null) {
            divider.setBorder(null);

            divider.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)
                    && e.getClickCount() == 2) {
                        // TODO should be not override, but placed logic here
                        ((JSplitPane) divider.getParent())
                        .resetToPreferredSizes();
                    }
                }
            });
        }

        return split;
    }

    protected JSplitPane createSplitPaneImpl(DockingPort base, String region) {
        return new DockingSplitPane(base, region);
    }


    /**
     * Returns the initial divider location to be used by the specified
     * {@code JSplitPane} when it is embedded within the specified
     * {@code DockingPort}. It is assumed that the {@code JSplitPane} parameter
     * is embedded within the specified {@code DockingPort}, is validated,
     * visible, and its dimensions are non-zero.
     * <p>
     * This method gets the "size" of the specified {@code DockingPort} based on
     * the orientation of the split pane (<i>width</i> for horizontal split,
     * <i>height</i> for vertical split) minus the {@code DockingPort's}
     * insets. It then dispatches to
     * {@code getDividerProportion(DockingPort port, JSplitPane splitPane)} to
     * determine the preferred proportion of the split pane divider. The
     * returned value for this method is the product of the {@code DockingPort}
     * size and the split proportion.
     * <p>
     * If either {@code port} or {@code splitPane} parameters are {@code null},
     * then this method returns {@code 0}.
     *
     * @param port
     *            the {@code DockingPort} that contains the specified
     *            {@code JSplitPane}.
     * @param splitPane
     *            the {@code JSplitPane} whose initial divider location is to be
     *            determined.
     * @return the desired divider location of the supplied {@code JSplitPane}.
     * @see DockingStrategy#getInitialDividerLocation(DockingPort, JSplitPane)
     * @see #getDividerProportion(DockingPort, JSplitPane)
     */
    public int getInitialDividerLocation(DockingPort port, JSplitPane splitPane) {
        if (port == null || splitPane == null)
            return 0;

        Container dockingPort = (Container) port;
        Insets in = dockingPort.getInsets();
        boolean vert = splitPane.getOrientation() == JSplitPane.VERTICAL_SPLIT;
        int inset = vert ? in.top + in.bottom : in.left + in.right;

        // get the dimensions of the DockingPort, minus the insets
        int portSize = vert ? dockingPort.getHeight() : dockingPort.getWidth();
        portSize -= inset;

        // get the divider proportion for the split pane and multiply by the
        // port size
        double proportion = getDividerProportion(port, splitPane);
        if (proportion < 0 || proportion > 1)
            proportion = 0.5d;

        return (int) (portSize * proportion);
    }

    /**
     * Returns the desired divider proportion of the specified
     * {@code JSplitPane} after rendering. This method assumes that the
     * {@code JSplitPane} parameter is, or will be embedded within the specified
     * {@code DockingPort}. This method does <b>not</b> assume that the
     * {@code JSplitPane} has been validated and that it's current dimensions
     * are non-zero.
     * <p>
     * If either {@code port} or {@code splitPane} parameters are {@code null},
     * then this method returns the default value of
     * {@code RegionChecker.DEFAULT_SIBLING_SIZE}. Otherwise the "elder"
     * component within the {@code JSplitPane} is determined to see if it is
     * contained within a sub-{@code DockingPort}. If the "elder"
     * {@code Component} cannot be determined, or it is not contained within a
     * sub-{@code DockingPort}, then the default value of
     * {@code RegionChecker.DEFAULT_SIBLING_SIZE} is returned.
     * <p>
     * If the "elder" {@code Component} is successfully resolved inside a sub-{@code DockingPort},
     * then a check is done on the sub-port for the client property
     * {@code DefaultDockingStrategy.PREFERRED_PROPORTION}. If this value is
     * found, then the primitive float version of it is returned.
     * <p>
     * Failing these checks, the {@code Dockable} is resolved for the "elder"
     * {@code Component} in the specified {@code JSplitPane} via
     * {@code DockingManager.getDockable(Component comp)}. If no
     * {@code Dockable} can be found, then
     * {@code RegionChecker.DEFAULT_SIBLING_SIZE} is returned.
     * <p>
     * Otherwise, the {@code DockingPortPropertySet} is retrieved from the
     * specified {@code DockingPort} and its {@code getRegionChecker()} method
     * is called. {@code getSiblingSize(Component c, String region)} is invoked
     * on the returned {@code RegionChecker} passing the "elder"
     * {@code Component} in the split pane and the creation region resolved for
     * the specified {@code JSplitPane}. This resolves the preferred sibling
     * size for the elder {@code Dockable} component. If the elder
     * {@code Component} is in the top/left of the split pane, then
     * {@code 1F-prefSize} is returned. Otherwise, the preferred sibling size is
     * returned.
     *
     * @param port
     *            the {@code DockingPort} that contains, or will contain the
     *            specified {@code JSplitPane}.
     * @param splitPane
     *            the {@code JSplitPane} whose initial divider location is to be
     *            determined.
     * @return the desired divider proportion of the supplied {@code JSplitPane}.
     * @see RegionChecker#DEFAULT_SIBLING_SIZE
     * @see #PREFERRED_PROPORTION
     * @see DockingManager#getDockable(Component)
     * @see RegionChecker#getSiblingSize(Component, String)
     */
    public double getDividerProportion(DockingPort port, JSplitPane splitPane) {
        if (port == null || splitPane == null)
            return DockingManager.getDefaultSiblingSize();

        Component elder = getElderComponent(splitPane);
        if (elder == null)
            return DockingManager.getDefaultSiblingSize();

        Float prefProp = getPreferredProportion(splitPane, elder);
        if (prefProp != null)
            return prefProp.doubleValue();

        if (elder instanceof DockingSplitPane) {
            elder = ((DockingSplitPane) elder).getElderComponent();
        }

        Dockable dockable = DockingManager.getDockable(elder);
        if (dockable != null) {
            // DockingSplitPane splitter = (DockingSplitPane)splitPane;
            RegionChecker rc = port.getDockingProperties().getRegionChecker();
            float prefSize = rc.getSiblingSize(dockable.getComponent(),
                                               getCreationRegion(splitPane));
            return isElderTopLeft(splitPane) ? 1f - prefSize : prefSize;
            // return prefSize;
        }

        return DockingManager.getDefaultSiblingSize();
    }

    protected String getCreationRegion(JSplitPane splitPane) {
        if (splitPane instanceof DockingSplitPane)
            return ((DockingSplitPane) splitPane).getRegion();
        return (String) SwingUtility.getClientProperty(splitPane,
                DockingConstants.REGION);
    }

    protected boolean isElderTopLeft(JSplitPane splitPane) {
        if (splitPane instanceof DockingSplitPane)
            return ((DockingSplitPane) splitPane).isElderTopLeft();
        String region = getCreationRegion(splitPane);
        // creation region represents the "new" region, not the "elder" region.
        // so if the "new" region is NOT the topLeft, then the "elder" is.
        return !DockingUtility.isRegionTopLeft(region);
    }

    protected Float getPreferredProportion(JSplitPane splitPane,
                                           Component controller) {
        // 'controller' is inside a dockingPort. re-reference to the parent
        // dockingPort.
        Container controllerPort = controller.getParent();
        return getPreferredProportion(controllerPort);
    }

    protected Component getElderComponent(JSplitPane splitPane) {
        if (splitPane instanceof DockingSplitPane)
            return ((DockingSplitPane) splitPane).getElderComponent();

        boolean inTopLeft = isElderTopLeft(splitPane);
        Component comp = inTopLeft ? splitPane.getLeftComponent() : splitPane
                         .getRightComponent();
        if (comp instanceof DockingPort)
            comp = ((DockingPort) comp).getDockedComponent();
        return comp;
    }


    protected static Float getPreferredProportion(Component c) {
        return c == null ? null : (Float) SwingUtility.getClientProperty(c,
                PREFERRED_PROPORTION);
    }

    public static void setDefaultResizeWeight(double rw) {
        defaultResizeWeight = rw;
    }

    public static void keepConstantPercentage(boolean cstPercent) {
        constantPercent = cstPercent;
    }
}
