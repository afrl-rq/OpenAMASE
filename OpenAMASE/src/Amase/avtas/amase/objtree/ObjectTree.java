// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.objtree;

import avtas.util.ObjectUtils;
import avtas.util.WindowUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Enumeration;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 *
 * @author AFRL/RQQD
 */
public class ObjectTree extends JXTreeTable {

    public static String DECIMAL_MATCHER = "[-+]?[0-9]*\\.?[0-9]*([eE][-+]?[0-9]*)?";
    public static String INTEGER_MATCHER = "[-+]?[0-9]*";
    
    public ObjectTree(Object object) {
        this();
        setObject(object);
    }

    public ObjectTree() {
 
        addHighlighter(HighlighterFactory.createSimpleStriping());


        setDefaultEditor(Enum.class, new EnumEditor());
        setDefaultEditor(Float.class, new NumberEditor(DECIMAL_MATCHER));
        setDefaultEditor(Double.class, new NumberEditor(DECIMAL_MATCHER));
        setDefaultEditor(Number.class, new NumberEditor(INTEGER_MATCHER));
        
        setShowGrid(true, true);
        //setRootVisible(true);

    }
    
    public void setObject(Object object) {
        setTreeTableModel(new ObjectTreeModel(object));
    }
    
    public Object getObject() {
        return ((ObjectTreeNode) ((ObjectTreeModel) getTreeTableModel()).getRoot()).value;
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        Object o = getPathForRow(row).getLastPathComponent();
        //Object o = getTreeTableModel().getValueAt(row, getHierarchicalColumn());
        if (o instanceof ObjectTreeNode.ListNode || o instanceof ObjectTreeNode.ArrayNode) {
            return null;
        }
        if (o instanceof ObjectTreeNode) {
            Class type = ((ObjectTreeNode) o).getType();
            TableCellEditor ed = getDefaultEditor(ObjectUtils.convertPrimitive(type));
            return ed;
        }
        return null;
    }
    

    public void reload(ObjectTreeNode node) {
        TreePath path = new TreePath(node.getPath());
        Enumeration en = getExpandedDescendants(path);
        ((ObjectTreeModel) getTreeTableModel()).reload(node);

        if (en != null) {
            while (en.hasMoreElements()) {
                expandPath((TreePath) en.nextElement());
            }
        }
    }
    
    
    /**
     * Shows a dialog box with an object tree as its contents.  If the user selects OK,
     * the object changes are approved. and the passed object is returned.
     *
     * This dialog is modal, meaning that it blocks user input to the parent until this
     * dialog is dismissed.
     *
     * @param editObj  object to edit
     * @param parentComponent the parent from which to base this dialog (may be null)
     * @param title a title for the dialog
     * @return the edited object, or null if the user canceled the operation
     */
    public static Object showEditWindow(Object editObj, Component parentComponent, String title) {

        
        ObjectTree tree = new ObjectTree(editObj);
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setPreferredSize(new Dimension(320, 240));
        int ans = WindowUtils.showOKCancelDialog(parentComponent, scrollPane, title);
        
        return ans == JOptionPane.OK_OPTION ? editObj : null;
    }

    public static class EnumEditor extends DefaultCellEditor {

        public EnumEditor() {
            super(new JComboBox());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            Object node = ((JXTreeTable) table).getPathForRow(row).getLastPathComponent();
            Object[] enums = (((ObjectTreeNode) node).getType()).getEnumConstants();
            ((JComboBox) getComponent()).setModel(new DefaultComboBoxModel(enums));
            ((JComboBox) getComponent()).setSelectedItem(value);
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }
    }

    static class NumberEditor extends DefaultCellEditor {

        public NumberEditor(final String matcher) {
            super(new JTextField());
            final JTextField field = (JTextField) getComponent();

            field.getDocument().addUndoableEditListener(new UndoableEditListener() {
                @Override
                public void undoableEditHappened(UndoableEditEvent e) {
                    boolean ok = field.getText().matches(matcher);
                    if (!ok) {
                        e.getEdit().undo();
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
            });

        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */