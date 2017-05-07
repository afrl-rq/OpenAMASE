// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.objtree;

import avtas.amase.objtree.ObjectTreeNode.EnumNode;
import avtas.lmcp.LMCPEnum;
import avtas.lmcp.LMCPObject;
import avtas.lmcp.LMCPFactory;
import avtas.lmcp.LMCPXMLReader;
import avtas.util.ReflectionUtils;
import avtas.swing.PopupMenuAdapter;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import avtas.amase.objtree.ObjectTreeNode.ListNode;
import avtas.amase.objtree.ObjectTreeNode.ObjectNode;
import avtas.amase.objtree.ObjectTreeNode.PrimitiveNode;
import avtas.util.ObjectUtils;
import javax.swing.Action;
import javax.swing.ListSelectionModel;

/**
 * Sets up some actions that can manipulate the tree, such as drag-and-drop and
 * right-clicking.
 *
 * @author AFRL/RQQD
 */
public class LmcpObjectActions extends PopupMenuAdapter {

    private ObjectTree tree;
    private String copyObject = null;
    //private ObjectTreeTableModel model = null;

    private LmcpObjectActions() {
    }

    public static LmcpObjectActions addActions(ObjectTree tree) {
        LmcpObjectActions actions = new LmcpObjectActions();
        actions.tree = tree;
        //actions.model = (ObjectTreeTableModel) tree.getTreeTableModel();
        tree.addMouseListener(actions);
        actions.setupDragAndDrop();
        actions.setupHotkeys();
        return actions;
    }

    @Override
    public void setMenuContents(JPopupMenu menu, java.awt.Point p) {

        final TreePath path = tree.getPathForLocation(p.x, p.y);

        copyObject = getObjectFromClipboard();

        int row = tree.getRowForPath(path);
        if (row != -1) {
            tree.getSelectionModel().setSelectionInterval(row, row);
        }
        //tree.setSelectionPath(path);
        if (path == null) {
            return;
        }

        final ObjectTreeNode targetNode = (ObjectTreeNode) path.getLastPathComponent();


        if (targetNode instanceof ListNode) {
            setupListMenu((ListNode) targetNode, menu, path);
        }
        else if (targetNode instanceof ObjectNode) {
            menu.add(getReplaceMenu(targetNode));
            menu.add(getCopyAction(targetNode));
            addPasteAction(targetNode, menu);
        }
        else if (targetNode instanceof PrimitiveNode || targetNode instanceof EnumNode) {
            menu.add(getCopyAction(targetNode));
            addPasteAction(targetNode, menu);
        }

        if (targetNode.getParent() instanceof ListNode) {
            final ListNode node = (ListNode) targetNode.getParent();
            JMenuItem item = new JMenuItem("Delete");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    node.remove(targetNode);
                    tree.reload(node);
                }
            });
            menu.add(item);
        }
    }

    void setupHotkeys() {
        tree.registerKeyboardAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = tree.getSelectedRows();
                for (int row : rows) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getPathForRow(row).getLastPathComponent();
                    if (node.getParent() instanceof ListNode) {
                        ListNode list = (ListNode) node.getParent();
                        list.remove(node);
                        tree.reload(list);
                    }

                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_FOCUSED);
    }

    /**
     * sets up menus that are relevant for list nodes
     */
    void setupListMenu(final ListNode node, JPopupMenu popmenu, final TreePath path) {
        if (ObjectUtils.isPrimitive(node.type)) {
            popmenu.add(new AbstractAction("Add") {
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (node.type == String.class) {
                            node.addObject(node.type.getConstructor().newInstance());
                        }
                        else {
                            node.addObject(node.type.getConstructor(String.class).newInstance("0"));
                        }
                        tree.reload(node);
                    } catch (Exception ex) {
                        Logger.getLogger(LmcpObjectActions.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

        }
        else {
            JMenu menu = new JMenu("Add");
            popmenu.add(menu);
            ArrayList<Object> replacementList = getCompatibleTypes(node.type);
            for (final Object o : replacementList) {
                JMenuItem item = new JMenuItem(getDisplayString(o));
                item.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        node.addObject(o);
                        tree.expandPath(path);
                        tree.reload(node);
                    }
                });
                menu.add(item);
            }

            if (!node.type.isEnum() && !node.list.isEmpty()) {
                menu = new JMenu("Set Field Value");
                popmenu.add(menu);
                List<Field> fields = ReflectionUtils.getAllFields(node.type);
                for (final Field f : fields) {
                    // don't modify static values
                    if ((f.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
                        continue;
                    }

                    JMenuItem item = new JMenuItem(new AbstractAction(f.getName()) {
                        public void actionPerformed(ActionEvent e) {

                            if (f.getType().isEnum()) {
                                Object ans = JOptionPane.showInputDialog(tree, "Set value for field ", "Set value for field ",
                                        JOptionPane.PLAIN_MESSAGE, null, f.getType().getEnumConstants(), f.getType().getEnumConstants()[0]);
                                if (ans == null) {
                                    return;
                                }
                                for (int i = 0; i < node.getList().size(); i++) {
                                    Object obj = node.getList().get(i);
                                    if (obj == null) {
                                        return;
                                    }
                                    ReflectionUtils.setFieldValue(obj, f.getName(), ans);
                                    ((ObjectTreeNode) node.getChildAt(i)).setUserObject(obj);
                                }


                            }
                            else {
                                String ans = JOptionPane.showInputDialog(tree, "Set value for field " + f.getName());
                                if (ans == null) {
                                    return;
                                }
                                for (int i = 0; i < node.getList().size(); i++) {
                                    Object obj = node.getList().get(i);
                                    if (obj == null) {
                                        return;
                                    }
                                    ReflectionUtils.setFieldValue(obj, f.getName(), ObjectUtils.getValueOf(ans, f.getType()));
                                    ((ObjectTreeNode) node.getChildAt(i)).setUserObject(obj);
                                }
                            }
                            tree.reload(node);
                        }
                    });
                    menu.add(item);
                }
            }
            if (copyObject != null) {
                final Object obj = getObjectFromString(node.type, copyObject);
                if (obj != null) {
                    popmenu.add(new AbstractAction("Paste") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            node.addObject(obj);
                            tree.reload(node);
                        }
                    });
                }
            }

        }
    }

    JMenu getReplaceMenu(final ObjectTreeNode node) {
        final JMenu menu = new JMenu("Replace With");
        final ArrayList<Object> replacementList = getCompatibleTypes(node.getType());
        for (final Object o : replacementList) {
            JMenuItem item = new JMenuItem(getDisplayString(o));
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    node.setUserObject(o);
                    tree.reload(node);
                }
            });
            menu.add(item);
        }
        return menu;
    }

    Action getCopyAction(final ObjectTreeNode node) {
        return new AbstractAction("Copy") {
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                        new StringSelection(node.getValue().toString()), null);
            }
        };
    }

    void addPasteAction(final ObjectTreeNode node, JPopupMenu popmenu) {
        if (copyObject != null) {
            final Object obj = getObjectFromString(node.getType(), copyObject);
            if (obj != null) {
                popmenu.add(new AbstractAction("Paste") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        node.setUserObject(obj);
                        tree.reload(node);
                    }
                });
            }
        }
    }

    
    private void setupDragAndDrop() {
        tree.setDragEnabled(true);
        tree.setDropMode(DropMode.ON_OR_INSERT);
        tree.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tree.setTransferHandler(new TransferHandler() {
            TreePath[] dragPaths = null;

            @Override
            public int getSourceActions(JComponent c) {
                return COPY_OR_MOVE;
            }

            @Override
            public boolean canImport(TransferSupport support) {
                if (dragPaths == null) {
                    return false;
                }
                JTree.DropLocation loc = (JTree.DropLocation) support.getDropLocation();
                TreePath dropPath = loc.getPath();
                if (dropPath == null) {
                    return false;
                }
                if (dropPath.getLastPathComponent() instanceof ListNode) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }

                JTree.DropLocation loc = (JTree.DropLocation) support.getDropLocation();
                TreePath dropPath = loc.getPath();

                ListNode dropNode = (ListNode) dropPath.getLastPathComponent();
                for (TreePath p : dragPaths) {
                    if (p.getLastPathComponent() instanceof ObjectTreeNode) {
                        ObjectTreeNode moveNode = (ObjectTreeNode) p.getLastPathComponent();

                        if (dropNode.type.isAssignableFrom(moveNode.getType())) {
                            if (loc.getChildIndex() == -1) {
                                dropNode.add(moveNode);
                            }
                            else {
                                dropNode.insert(moveNode.getValue(), loc.getChildIndex());
                            }
                            if (moveNode.getParent() instanceof ListNode && support.getDropAction() == MOVE) {
                                ((ListNode) moveNode.getParent()).remove(moveNode);
                            }
                        }
                        tree.reload((ObjectTreeNode) moveNode.getParent());
                    }
                }
                tree.reload(dropNode);
                dragPaths = null;
                return true;
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                if (tree.getSelectedRow() != -1) {
                    ObjectTreeNode node = (ObjectTreeNode) tree.getPathForRow(tree.getSelectedRow()).getLastPathComponent();
                    StringSelection ss = new StringSelection(String.valueOf(node.getValue()));
                    return ss;
                }
                return new StringSelection(Arrays.toString(dragPaths));
            }

            @Override
            public void exportAsDrag(JComponent comp, InputEvent e, int action) {
                List<TreePath> paths = new ArrayList<>();
                for (int row : tree.getSelectedRows()) {
                    paths.add(tree.getPathForRow(row));
                }
                dragPaths = paths.toArray(new TreePath[]{});
                super.exportAsDrag(comp, e, action);
            }
        });
    }

    public static ArrayList<Object> getCompatibleTypes(Class origClass) {
        ArrayList<Object> retList = new ArrayList<Object>();
        if (LMCPObject.class.isAssignableFrom(origClass)) {
            for (LMCPEnum e : LMCPFactory.getLoadedSeries()) {
                Package packName = e.getClass().getPackage();
                for (String t : e.getAllTypes()) {
                    try {
                        Class tClass = Class.forName(packName.getName() + "." + t);
                        if (origClass.isAssignableFrom(tClass)) {
                            retList.add(tClass.newInstance());
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(LmcpObjectActions.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        else if (origClass.isEnum()) {
            Object[] enums = origClass.getEnumConstants();
            retList.addAll(Arrays.asList(enums));
        }
        else {
            try {
                retList.add(origClass.getConstructors()[0].newInstance("0"));
                retList.add(origClass.getConstructors()[0].newInstance());
            } catch (Exception ex) {
            }
        }
        return retList;
    }

    /**
     * Attempts to get an object from a string. First, it checks if an object is
     * an LMCPObject. If not, it tries to do a conversion to a primitive type.
     *
     * @param type the type of object that the value should contain
     * @param value the string representation of an object
     * @return the converted object, if it is of the type requested, or null if
     * it is not.
     */
    public static Object getObjectFromString(Class type, String value) {
        Object o = null;
        if (LMCPObject.class.isAssignableFrom(type)) {
            try {
                o = LMCPXMLReader.readXML(value);
                if (o != null) {
                    if (type.isInstance(o)) {
                        return o;
                    }
                    return null;
                }
            } catch (Exception ex) {
            }
        }

        if (ObjectUtils.isPrimitive(type)) {
            o = ObjectUtils.getValueOf(value, type);
            if (o != null) {
                return o;
            }
        }
        else if (type.isEnum()) {
            try {
                return Enum.valueOf(type, value);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static String getDisplayString(Object o) {
        if (o instanceof LMCPObject) {
            LMCPObject lmcpObj = (LMCPObject) o;
            return lmcpObj.getLMCPTypeName() + " (" + lmcpObj.getLMCPSeriesName() + ")";
        }
        else {
            return String.valueOf(o);
        }
    }

    private String getObjectFromClipboard() {
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (Exception ex) {
            return null;
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */