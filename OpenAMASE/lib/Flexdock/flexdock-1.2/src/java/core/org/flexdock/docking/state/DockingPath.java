// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Apr 28, 2005
 */
package org.flexdock.docking.state;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Window;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;



import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.defaults.DockingSplitPane;
import org.flexdock.docking.state.tree.SplitNode;
import org.flexdock.util.DockingUtility;
import org.flexdock.util.SwingUtility;

/**
 * @author Christopher Butler
 */
@SuppressWarnings(value = { "serial" })
public class DockingPath implements Cloneable, DockingConstants, Serializable {

    public static final String RESTORE_PATH_KEY = "DockingPath.RESTORE_PATH_KEY";

    private transient String stringForm;
    private String rootPortId;
    private ArrayList nodes; // contains SplitNode objects
    private String siblingId;
    private boolean tabbed;

    public DockingPath() {
        nodes = new ArrayList();
    }

    public static DockingPath create(String dockableId) {
        Dockable dockable = findDockable(dockableId);
        return create(dockable);
    }

    public static DockingPath create(Dockable dockable) {
        if(dockable==null || !isDocked(dockable))
            return null;

        DockingPath path = new DockingPath(dockable);
        Component comp = dockable.getComponent();

        Container parent = comp.getParent();
        while(!isDockingRoot(parent)) {
            if(parent instanceof DockingPort) {
                SplitNode node = createNode((DockingPort)parent);
                path.addNode(node);
            }
            parent = parent.getParent();
        }
        if(isDockingRoot(parent))
            path.setRootPortId(((DockingPort)parent).getPersistentId());

        path.initialize();
        return path;
    }

    public static SplitNode createNode(Dockable dockable) {
        if(dockable==null)
            return null;

        Container parent = dockable.getComponent().getParent();
        return parent instanceof DockingPort? createNode((DockingPort)parent): null;
    }

    public static SplitNode createNode(DockingPort port) {
        if(port==null)
            return null;

        Component c = ((Component)port).getParent();
        JSplitPane split = c instanceof JSplitPane? (JSplitPane)c: null;
        if(split==null)
            return null;

        return createNode(port, split);
    }

    private static SplitNode createNode(DockingPort port, JSplitPane split) {
        int orientation = split.getOrientation();
        boolean topLeft = split.getLeftComponent()==port? true: false;

        int region = 0;
        String siblingId = null;
        if(topLeft) {
            region = orientation==JSplitPane.VERTICAL_SPLIT? TOP: LEFT;
            siblingId = getSiblingId(split.getRightComponent());
        } else {
            region = orientation==JSplitPane.VERTICAL_SPLIT? BOTTOM: RIGHT;
            siblingId = getSiblingId(split.getLeftComponent());
        }

        int size = orientation==JSplitPane.VERTICAL_SPLIT? split.getHeight(): split.getWidth();
        int divLoc = split.getDividerLocation();

        int testSize = 0;
        if (orientation == JSplitPane.VERTICAL_SPLIT) {
            testSize += split.getTopComponent().getHeight() + split.getBottomComponent().getHeight() + split.getDividerSize();
        } else {
            testSize += split.getLeftComponent().getWidth() + split.getRightComponent().getWidth() + split.getDividerSize();
        }

        float percentage;
        if (split instanceof DockingSplitPane && ((DockingSplitPane) split).getPercent() != -1) {
            percentage = (float) ((DockingSplitPane) split).getPercent();
        } else {
            percentage = (float)divLoc / (float)size;
        }

        return new SplitNode(orientation, region, percentage, siblingId);
    }

    private static String getSiblingId(Component c) {
        if(c instanceof DockingPort)
            c = ((DockingPort)c).getDockedComponent();

        Dockable dockable = findDockable(c);
        return dockable==null? null: dockable.getPersistentId();
    }



    private static boolean isDockingRoot(Container c) {
        return c instanceof DockingPort && ((DockingPort)c).isRoot();
    }

    public static DockingPath getRestorePath(Dockable dockable) {
        Object obj = dockable==null? null: dockable.getClientProperty(RESTORE_PATH_KEY);
        return obj instanceof DockingPath? (DockingPath)obj: null;
    }

    public static DockingPath updateRestorePath_(Dockable dockable, DockingPath restorePath) {
        if(dockable==null || restorePath==null)
            return null;
        dockable.putClientProperty(RESTORE_PATH_KEY, restorePath);
        return restorePath;
    }

    private DockingPath(Dockable dockable) {
        siblingId = findSiblingId(dockable);
        tabbed = dockable.getComponent().getParent() instanceof JTabbedPane;
        nodes = new ArrayList();
    }

    public boolean isTabbed() {
        return this.tabbed;
    }

    public void setTabbed(boolean isTabbed) {
        this.tabbed = isTabbed;
    }

    public String getSiblingId() {
        return this.siblingId;
    }

    public void setSiblingId(String siblingId) {
        this.siblingId = siblingId;
    }

    private DockingPath(String parent, boolean tabs, ArrayList nodeList) {
        siblingId = parent;
        tabbed = tabs;
        nodes = nodeList;
    }

    public List getNodes() {
        return nodes;
    }

    public DockingPort getRootPort() {
        return DockingManager.getDockingPort(rootPortId);
    }

    public String getRootPortId() {
        return this.rootPortId;
    }

    public void setRootPortId(String portId) {
        rootPortId = portId;
    }

    private void addNode(SplitNode node) {
        nodes.add(node);
    }

    private void initialize() {
        Collections.reverse(nodes);
    }

    private String findSiblingId(Dockable dockable) {
        Component comp = dockable.getComponent();
        JSplitPane split = comp.getParent() instanceof JSplitPane? (JSplitPane)comp.getParent(): null;
        if(split==null)
            return null;

        Component sibling = split.getLeftComponent();
        if(comp==sibling)
            sibling = split.getRightComponent();

        Dockable d = findDockable(sibling);
        return d==null? null: d.getPersistentId();
    }

    public String toString() {
        if(stringForm==null) {
            StringBuffer sb = new StringBuffer("/RootPort[id=").append(rootPortId).append("]");
            for(Iterator it=nodes.iterator(); it.hasNext();) {
                SplitNode node = (SplitNode)it.next();
                sb.append("/").append(node.toString());
            }
            sb.append("/Dockable");
            stringForm = sb.toString();
        }
        return stringForm;
    }

    public boolean restore(String dockable) {
        return restore(DockingManager.getDockable(dockable));
    }

    private DockingPort getRootDockingPort() {
        DockingPort port = DockingManager.getDockingPort(rootPortId);
        if(port!=null)
            return port;

        Window activeWindow = SwingUtility.getActiveWindow();
        return DockingManager.getRootDockingPort(activeWindow);
    }

    public boolean restore(Dockable dockable) {
        if(dockable==null || isDocked(dockable))
            return false;

        DockingPort rootPort = getRootDockingPort();
        String region = CENTER_REGION;
        if(nodes.size()==0) {
            return dockFullPath(dockable, rootPort, region);
        }

        DockingPort port = rootPort;
        for(Iterator it=nodes.iterator(); it.hasNext();) {
            SplitNode node = (SplitNode)it.next();
            Component comp = port.getDockedComponent();
            region = getRegion(node, comp);

            JSplitPane splitPane = comp instanceof JSplitPane? (JSplitPane)comp: null;
            // path was broken.  we have no SplitPane, or the SplitPane doesn't
            // match the orientation of the current node, meaning the path was
            // altered at this point.
            if(splitPane==null || splitPane.getOrientation()!=node.getOrientation()) {
                return dockBrokenPath(dockable, port, region, node);
            }

            // assume there is a transient sub-dockingPort in the split pane
            comp = node.getRegion()==LEFT || node.getRegion()==TOP? splitPane.getLeftComponent(): splitPane.getRightComponent();
            port = (DockingPort)comp;

            // move on to the next node
        }

        return dockFullPath(dockable, port, region);
    }



    private boolean dockBrokenPath(Dockable dockable, DockingPort port, String region, SplitNode ctrlNode) {
        Component current = port.getDockedComponent();
        if(current instanceof JSplitPane) {
            return dockExtendedPath(dockable, port, region, ctrlNode);
        }

        if(current instanceof JTabbedPane) {
            return dock(dockable, port, CENTER_REGION, null);
        }

        Dockable embedded = findDockable(current);
        if(embedded==null || tabbed) {
            return dock(dockable, port, CENTER_REGION, null);
        }

        String embedId = embedded.getPersistentId();
        SplitNode lastNode = getLastNode();
        if(embedId.equals(lastNode.getSiblingId())) {
            region = getRegion(lastNode, current);
            ctrlNode = lastNode;
        }

        return dock(dockable, port, region, ctrlNode);
    }

    private boolean dockFullPath(Dockable dockable, DockingPort port, String region) {
        // the docking layout was altered since the last time our dockable we embedded within
        // it, and we were able to fill out the full docking path.  this means there is already
        // something within the target dockingPort where we expect to dock our dockable.

        // first, check to see if we need to use a tabbed layout
        Component current = port.getDockedComponent();
        if(current instanceof JTabbedPane) {
            return dock(dockable, port, CENTER_REGION, null);
        }

        // check to see if we dock outside the current port or outside of it
        Dockable docked = findDockable(current);
        if(docked!=null) {
            Component comp = dockable.getComponent();
            if(port.isDockingAllowed(comp, CENTER_REGION)) {
                return dock(dockable, port, CENTER_REGION, null);
            }
            DockingPort superPort = (DockingPort)SwingUtilities.getAncestorOfClass(DockingPort.class, (Component)port);
            if(superPort!=null)
                port = superPort;
            return dock(dockable, port, region, getLastNode());
        }

        // if we were't able to dock above, then the path changes means our current path
        // does not extend all the way down into to docking layout.  try to determine
        // an extended path and dock into it
        return dockExtendedPath(dockable, port, region, getLastNode());
    }

    private boolean dockExtendedPath(Dockable dockable, DockingPort port, String region, SplitNode ctrlNode) {
        Component docked = port.getDockedComponent();

        //I don't think this code will matter any more, given the null check, but leaving for now.
        //null is returned when a dockingport is empty, so we need to dock to an empty port

        // if 'docked' is not a split pane, then I don't know what it is.  let's print a
        // stacktrace and see who sends in an error report.
        if(docked != null && !(docked instanceof JSplitPane)) {
            Throwable t = new Throwable("Docked: " + docked);
            System.err.println("Exception: "+t.getMessage());
            return false;
        }

        //begin code that matters.

        SplitNode lastNode = getLastNode();
        String lastSibling = lastNode==null? null: lastNode.getSiblingId();

        Set dockables = port.getDockables();
        for(Iterator it=dockables.iterator(); lastSibling!=null && it.hasNext();) {
            Dockable d = (Dockable)it.next();
            if(d.getPersistentId().equals(lastSibling)) {
                DockingPort embedPort = d.getDockingPort();
                String embedRegion = getRegion(lastNode, d.getComponent());
                return dock(dockable, embedPort, embedRegion, ctrlNode);
            }
        }


        return dock(dockable, port, region, ctrlNode);
    }

    private String getRegion(SplitNode node, Component dockedComponent) {
        if(dockedComponent==null)
            return CENTER_REGION;
        return DockingUtility.getRegion(node.getRegion());
    }

    public SplitNode getLastNode() {
        return nodes.size()==0? null: (SplitNode)nodes.get(nodes.size()-1);
    }

    private boolean dock(Dockable dockable, DockingPort port, String region, SplitNode ctrlNode) {
        boolean ret = DockingManager.dock(dockable, port, region);
        if(tabbed || ctrlNode==null)
            return ret;

        final float percent = ctrlNode.getPercentage();
        final Component docked = dockable.getComponent();

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                resizeSplitPane(docked, percent);
            }
        });
        return ret;
    }

    private void resizeSplitPane(Component comp, float percentage) {
        Container parent = comp.getParent();
        Container grandParent = parent==null? null: parent.getParent();
        if(!(grandParent instanceof JSplitPane))
            return;

        JSplitPane split = (JSplitPane)grandParent;
//              int splitSize = split.getOrientation()==DockingConstants.VERTICAL? split.getHeight(): split.getWidth();
//              int divLoc = (int)(percentage * (float)splitSize);
        split.setDividerLocation(percentage);
    }

    private static Dockable findDockable(Component c) {
        return DockingManager.getDockable(c);
    }

    private static Dockable findDockable(String id) {
        return DockingManager.getDockable(id);
    }

    private static boolean isDocked(Dockable dockable) {
        return DockingManager.isDocked(dockable);
    }

    public int getDepth() {
        return nodes.size();
    }

    public SplitNode getNode(int indx) {
        return indx<0 || indx>=getDepth()? null: (SplitNode)nodes.get(indx);
    }

    public Object clone() {
        ArrayList nodeList = null;
        if(nodes!=null) {
            nodeList = new ArrayList(nodes.size());
            for(Iterator it=nodes.iterator(); it.hasNext();) {
                SplitNode node = (SplitNode)it.next();
                nodeList.add(node.clone());
            }
        }

        DockingPath path = new DockingPath(siblingId, tabbed, nodeList);
        path.rootPortId = rootPortId;
        return path;
    }

}
