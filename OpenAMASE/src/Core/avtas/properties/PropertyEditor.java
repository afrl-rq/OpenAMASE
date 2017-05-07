// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.properties;

import avtas.properties.Editors.Editor;
import avtas.util.WindowUtils;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author AFRL/RQQD
 */
public class PropertyEditor extends JPanel {

    private Object editObj;
    GridBagConstraints gbc;
    HashMap<Field, Editor> editorsMap = new HashMap<>();

    public PropertyEditor() {
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        //gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 5, 5);

    }

    public void setEditObject(Object editObj) {
        this.editObj = editObj;

        removeAll();
        editorsMap.clear();
        if (editObj == null) {
            return;
        }

        gbc.gridy = 0;

        for (Field f : editObj.getClass().getDeclaredFields()) {
            UserProperty prop = f.getAnnotation(UserProperty.class);
            if (prop != null) {
                try {
                    f.setAccessible(true);
                    Editor ed = Editors.getEditor(f.getType(), prop);
                    ed.setValue(f.get(editObj));

                    editorsMap.put(f, ed);

                    gbc.gridx = 0;
                    gbc.weightx = 0;
                    if (!Action.class.isAssignableFrom(f.getType())) {
                        JLabel label = new JLabel(!prop.DisplayName().isEmpty() ? prop.DisplayName() : f.getName());
                        add(label, gbc);
                        if (!prop.Description().isEmpty()) {
                            label.setToolTipText(prop.Description());
                        }
                    }

                    gbc.gridx = 1;
                    gbc.weightx = 1;
                    add(ed.getComponent(), gbc);
                    gbc.gridy++;
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(PropertyEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        revalidate();
    }

    public Object getEditObj() {
        return editObj;
    }

    public void applyChanges() {
        for (Field f : editorsMap.keySet()) {

            try {
                Method m = editObj.getClass().getMethod("set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1), f.getType());
                if (m != null) {
                    m.invoke(editObj, editorsMap.get(f).getValue());
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                try {
                    f.set(editObj, editorsMap.get(f).getValue());
                } catch (IllegalAccessException | SecurityException ex2) {
                    //Logger.getLogger(PropertyEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    public static PropertyEditor getEditor(Object editObj) {

        PropertyEditor ed = new PropertyEditor();
        ed.setEditObject(editObj);
        return ed;

    }
    
    /**
     * Returns a JPanel that contains a property editor initialized for the 
     * given object.  The panel also includes an "Apply" button for applying changes.
     * @param editObj the object to edit.
     * @return a JPanel that contains a property editor initialized for the 
     * given object.
     */
    public static JPanel getEditorPanel(Object editObj) {
        JPanel panel = new JPanel();
        final PropertyEditor ed = getEditor(editObj);
        panel.setLayout(new BorderLayout(5,5));
        panel.add(ed, BorderLayout.CENTER);
        panel.setBorder(new EmptyBorder(5,5,5,5));
        panel.add(new JButton(new AbstractAction("Apply") {

            @Override
            public void actionPerformed(ActionEvent e) {
                ed.applyChanges();
                //System.out.println(XmlSerializer.serialize(ed.getEditObj()).toXML());
            }
        }), BorderLayout.SOUTH);
        
        return panel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            WindowUtils.showApplicationWindow(getEditorPanel(new TestClass()));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(PropertyEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */