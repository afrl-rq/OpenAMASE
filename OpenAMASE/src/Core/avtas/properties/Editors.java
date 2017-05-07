// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.properties;

import avtas.swing.DefaultListEditor;
import avtas.swing.FileSelector;
import avtas.util.ObjectUtils;
import avtas.util.WindowUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.prompt.BuddyButton;
import org.jdesktop.swingx.prompt.BuddySupport;

/**
 *
 * @author AFRL/RQQD
 */
public class Editors {

    //public static String DECIMAL_MATCHER = "[-+]?[0-9]*\\.?[0-9]*([eE][-+]?[0-9]*)?";
    //public static String INTEGER_MATCHER = "[-+]?[0-9]*";
    public static interface Editor {

        public Object getValue();

        public void setValue(Object value);

        public Class getType();

        public Component getComponent();
    }

    public static Editor getEditor(Class c, UserProperty prop) {

        if ((c == Integer.class) || (c == Long.class) || (c == Short.class) || (c == Byte.class)
                || (c == int.class) || (c == long.class) || (c == short.class) || (c == byte.class)) {
            return new IntegerEditor(ObjectUtils.convertPrimitive(c));
            //return new NumberPropertyEditor(ObjectUtils.convertPrimitive(c));
        }
        if (c == Double.class || c == double.class || c == Float.class || c == float.class) {
            return new DecimalEditor(ObjectUtils.convertPrimitive(c));
        }
        if (c.isEnum()) {
            return new EnumEditor(c);
        }
        if (c == boolean.class || c == Boolean.class) {
            return new BooleanEditor();
        }
        if (c == File.class) {
            if (prop != null) {
                return new FileEditor(c, prop.FileType());
            } else {
                return new FileEditor(c, UserProperty.FileTypes.FilesAndDirectories);
            }
        }
        if (c == Color.class) {
            return new ColorEditor();
        }
        if (List.class.isAssignableFrom(c)) {
            return new ListEditor();
        }
        if (c == Font.class) {
            return new FontEditor();
        }
        if (Action.class.isAssignableFrom(c)) {
            return new ActionEditor();
        }

        return new StringEditor();
    }

    public static class FileEditor implements Editor {

        FileSelector selector;

        Class type;
        JPanel widget;

        public FileEditor(Class type, UserProperty.FileTypes fileType) {
            this.type = type;

            selector = new FileSelector(null, fileType.type);

            widget = buildWidget(selector);
        }

        @Override
        public File getValue() {
            return selector.getFile();
        }

        @Override
        public void setValue(Object value) {
            if (value instanceof File) {
                selector.setFile((File) value);
            }
        }

        @Override
        public Class<File> getType() {
            return type;
        }

        @Override
        public Component getComponent() {
            return widget;
        }

    }

    public static class IntegerEditor extends JTextField implements Editor {

        Class<? extends Number> type;

        public IntegerEditor(Class<? extends Number> type) {
            this.type = type;
            getDocument().addUndoableEditListener(new UndoableEditListener() {
                @Override
                public void undoableEditHappened(UndoableEditEvent e) {
                    if (!checkValue(getText()) && !getText().isEmpty()) {
                        e.getEdit().undo();
                    }
                }
            });
        }

        boolean checkValue(String value) {
            try {
                Long.parseLong(getText());
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }

        @Override
        public Object getValue() {
            String txt = getText();
            if (txt.isEmpty()) {
                txt = "0";
            }
            try {
                return getType().getConstructor(String.class).newInstance(txt);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(Editors.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        public void setValue(Object value) {
            setText(value instanceof Number ? Long.toString(((Number) value).longValue()) : "");
        }

        @Override
        public Class getType() {
            return type;
        }

        @Override
        public Component getComponent() {
            return this;
        }

    }

    public static class DecimalEditor extends IntegerEditor implements Editor {

        public DecimalEditor(Class<? extends Number> type) {
            super(type);
        }

        @Override
        boolean checkValue(String value) {
            try {
                Double.parseDouble(getText());
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }

        @Override
        public void setValue(Object value) {
            setText(value instanceof Number ? Double.toString(((Number) value).doubleValue()) : "");
        }

    }

//    public static class CustomNumberEditor extends NumberPropertyEditor {
//
//        public CustomNumberEditor(Class type, final String matcher) {
//            super(type);
//            final JTextField field = (JTextField) editor;
//
//            field.getDocument().addUndoableEditListener(new UndoableEditListener() {
//                @Override
//                public void undoableEditHappened(UndoableEditEvent e) {
//                    boolean ok = field.getText().matches(matcher);
//                    if (!ok) {
//                        e.getEdit().undo();
//                        Toolkit.getDefaultToolkit().beep();
//                    }
//                }
//            });
//
//            field.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    firePropertyChange(null, getValue());
//                }
//            });
//        }
//    }
    public static class EnumEditor<T extends Enum> extends JComboBox<T> implements Editor {

        private Class<T> enumType;

        public EnumEditor(Class<T> c) {
            super(c.getEnumConstants());
            this.enumType = c;
        }

        @Override
        public void setValue(Object value) {
            if (value != null && enumType.isInstance(value)) {
                setSelectedItem(value);
            }
        }

        @Override
        public T getValue() {
            return (T) getSelectedItem();
        }

        @Override
        public Component getComponent() {
            return this;
        }

        @Override
        public Class getType() {
            return enumType;
        }

    }

    public static class BooleanEditor extends JCheckBox implements Editor {

        @Override
        public Boolean getValue() {
            return isSelected();
        }

        @Override
        public void setValue(Object value) {
            setSelected(value instanceof Boolean && ((Boolean) value));
        }

        @Override
        public Component getComponent() {
            return this;
        }

        @Override
        public Class getType() {
            return Boolean.class;
        }

    }

    public static class ColorEditor extends JButton implements Editor {

        public ColorEditor() {
            addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Color c = JColorChooser.showDialog(ColorEditor.this, "Choose a Color", getBackground());
                    if (c != null) {
                        setBackground(c);
                    }
                }
            });
        }

        @Override
        public Color getValue() {
            return getBackground();
        }

        @Override
        public void setValue(Object value) {
            if (value instanceof Color) {
                super.setBackground((Color) value);
            }
        }

        @Override
        public Component getComponent() {
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.clearRect(0, 0, getWidth(), getHeight());
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        
        

        @Override
        public Class getType() {
            return Color.class;
        }

    }

    public static class StringEditor extends JTextField implements Editor {

        @Override
        public String getValue() {
            return getText();
        }

        @Override
        public void setValue(Object value) {
            setText(String.valueOf(value));
        }

        @Override
        public Component getComponent() {
            return this;
        }

        @Override
        public Class getType() {
            return String.class;
        }

    }

//    public static class CustomEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
//
//        JPanel panel;
//        JPanel widgetPanel = new JPanel();
//        JTextField label;
//        private Object value;
//
//        public CustomEditor() {
//            panel = new JPanel();
//            panel.setLayout(new BorderLayout(0, 0));
//            panel.setBorder(null);
//            label = new JTextField();
//            label.setBorder(null);
//            label.setOpaque(true);
//            panel.add(label, BorderLayout.CENTER);
//            JButton button = new JButton("...");
//            button.setMargin(new Insets(0, 0, 0, 0));
//
//            widgetPanel.setLayout(new BoxLayout(widgetPanel, BoxLayout.X_AXIS));
//            panel.add(widgetPanel, BorderLayout.EAST);
//            widgetPanel.add(button);
//        }
//
//        @Override
//        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//            setValue(value);
//            label.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
//            label.setFont(table.getFont());
//            return panel;
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            setValue(value);
//            label.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
//            label.setFont(table.getFont());
//            return panel;
//        }
//
//        public void setValue(Object value) {
//            this.value = value;
//            this.label.setText(String.valueOf(value));
//        }
//
//        @Override
//        public Object getCellEditorValue() {
//            return value;
//        }
//    }
    public static class ListEditor<T> extends JXTextField implements Editor {

        List<T> list = null;
        BuddyButton editBut;

        public ListEditor() {

            setEditable(false);
            editBut = new BuddyButton();
            editBut.setIcon(new ImageIcon(Editors.class.getResource("/resources/Edit16.png")));
            //textLabel.setEditable(false);
            addBuddy(editBut, BuddySupport.Position.RIGHT);
            editBut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DefaultListEditor ed = new DefaultListEditor(list);
                    ed.showUpDownButtons(true);
                    ed.showRemoveButton(true);

                    if (ed != null) {
                        //int old = list.hashCode();
                        JOptionPane.showMessageDialog(ListEditor.this, ed, "Edit List", JOptionPane.PLAIN_MESSAGE);
                        //List oldList = list;
                        list = new ArrayList<>(list);
                        setText(list == null ? "" : "[" + list.size() + " items]");
                        //firePropertyChange(oldList, list);
                    }

                }
            });

        }

        @Override
        public List<T> getValue() {
            return list;
        }

        @Override
        public void setValue(Object value) {
            if (value instanceof List) {
                this.list = (List) value;
                setText(list == null ? "" : "[" + list.size() + "] items");
            }
        }

        @Override
        public Component getComponent() {
            return this;
        }

        @Override
        public Class getType() {
            return list.getClass();
        }

    }

    public static class FontEditor extends JComboBox<String> implements Editor {

        public FontEditor() {
            super(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        }

        @Override
        public Object getValue() {
            return Font.decode((String) getSelectedItem());
        }

        @Override
        public void setValue(Object value) {
            if (value instanceof Font) {
                setSelectedItem(((Font) value).getName());
            }
        }

        @Override
        public Class getType() {
            return Font.class;
        }

        @Override
        public Component getComponent() {
            return this;
        }

    }

    public static class ActionEditor extends JButton implements Editor {

        @Override
        public Object getValue() {
            return getAction();
        }

        @Override
        public void setValue(Object value) {
            if (value instanceof Action) {
                setAction( (Action) value);
            }
        }

        @Override
        public Class getType() {
            return Action.class;
        }

        @Override
        public Component getComponent() {
            return this;
        }

    }

    public static void main(String[] args) {
        JPanel formPanel = new JPanel(new GridLayout(0, 2));

        TestClass tc = new TestClass();

        for (Field f : tc.getClass().getDeclaredFields()) {
            UserProperty prop = f.getAnnotation(UserProperty.class);
            if (prop != null) {
                formPanel.add(new JLabel(!prop.DisplayName().isEmpty() ? prop.DisplayName() : f.getName()));
                formPanel.add(Editors.getEditor(f.getType(), prop).getComponent());
            }
        }

        WindowUtils.showApplicationWindow(formPanel);
    }

    public static JPanel buildWidget(Component mainWidget, Action... actions) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();
        con.weightx = 1.0;
        con.gridx = con.gridy = 0;
        con.insets = new Insets(0, 0, 0, 5);
        con.anchor = GridBagConstraints.CENTER;
        con.fill = GridBagConstraints.BOTH;
        panel.add(mainWidget, con);

        con.weightx = 0;
        for (Action a : actions) {
            JButton button = new JButton(a);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setBorder(null);
            con.gridx++;
            panel.add(button, con);
        }
        return panel;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */