// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.objtree;

import avtas.amase.objtree.ObjectTreeNode.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.tree.TreeModelSupport;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

/**
 *
 * @author AFRL/RQQD
 */
public class ObjectTreeModel extends AbstractTreeTableModel {

    public ObjectTreeModel(Object obj) {

        super(new ObjectTreeNode.ObjectNode(obj));
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(Object o, int i) {
        ObjectTreeNode node = (ObjectTreeNode) o;
        ObjectTreeNode parent = (ObjectTreeNode) node.getParent();
        if (i == 0) {
            if (parent instanceof ListNode || parent instanceof ArrayNode)
                return node.getType().getSimpleName();
            else 
                return node.getName();
        }
        if (i == 1) {
            if (o instanceof PrimitiveNode) {
                return ((ObjectTreeNode) o).getValue();
            }
            if (node instanceof ListNode || node instanceof ArrayNode) {
                return "";
            }
            //if ( !(parent instanceof ListNode) && !(parent instanceof ArrayNode) ) {
            //    return node.getType().getSimpleName();
            //} 
            if (node instanceof ObjectNode) {
                return node.value == null ? "null" : node.getValue().getClass().getSimpleName();
            }
        }
        return "";
    }

    @Override
    public Object getChild(Object parent, int index) {
        return ((DefaultMutableTreeNode) parent).getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return ((DefaultMutableTreeNode) parent).getChildCount();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return ((DefaultMutableTreeNode) parent).getIndex((DefaultMutableTreeNode) child);
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Name";
            case 1:
                return "Value";
        }
        return "";
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        return isLeaf(node) && column == 1;
    }

    @Override
    public void setValueAt(Object value, Object node, int column) {
        ((DefaultMutableTreeNode) node).setUserObject(String.valueOf(value));
        modelSupport.firePathChanged(new TreePath(((DefaultMutableTreeNode) node).getPath()));
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        if (path.getLastPathComponent() instanceof ObjectTreeNode) {
            ((ObjectTreeNode) path.getLastPathComponent()).setUserObject(newValue);
            modelSupport.firePathChanged(path);
        }
    }

    public void reload(ObjectTreeNode node) {
        modelSupport.fireTreeStructureChanged(new TreePath(node.getPath()));
    }

    public TreeModelSupport getModelSupport() {
        return modelSupport;
    }

    public void addChild(ObjectTreeNode parent, ObjectTreeNode child) {
        parent.add(child);
        modelSupport.fireChildAdded(new TreePath(parent.getPath()), parent.getChildCount() - 1, child);
    }

    public void removeChild(ObjectTreeNode parent, ObjectTreeNode child) {
        int index = parent.getIndex(child);
        if (index != -1) {
            parent.remove(index);
            modelSupport.fireChildRemoved(new TreePath(parent.getPath()), index, child);
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */