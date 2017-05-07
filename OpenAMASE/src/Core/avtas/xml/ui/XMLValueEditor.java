// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.xml.ui;

import avtas.swing.PopupMenuAdapter;
import avtas.xml.Comment;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import avtas.xml.Element;
import avtas.xml.XmlNode;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author AFRL/RQQD
 */
public class XMLValueEditor extends JPanel {

    public static final String NODE_UPDATED = "Content_UPDATED";
    XmlNode XmlNode = null;

    public XMLValueEditor(XmlNode XmlNode) {
        setContent(XmlNode);
    }

    public XmlNode getContent() {
        return XmlNode;
    }

    public void setContent(XmlNode XmlNode) {

        removeAll();

        this.XmlNode = XmlNode;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(300, 400));

        if (XmlNode instanceof Element) {
            final Element el = (Element) XmlNode;

            final JTextField nameField = new JTextField(el.getName());
            nameField.setBorder(new TitledBorder("Name"));
            add(nameField);
            nameField.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    fireContentUpdated(el);
                }
            });

            final AttributeTable table = new AttributeTable(el);
            add(table);
            table.setBorder(new TitledBorder("Attributes"));

            final JTextArea textArea = new JTextArea(el.getText());
            textArea.setPreferredSize(new Dimension(300, 100));
            add(textArea);
            textArea.setBorder(new TitledBorder("Value"));
            if (!el.getChildElements().isEmpty()) {
                textArea.setEnabled(false);
            }

            JPanel butPanel = new JPanel();
            add(butPanel);
            JButton b = new JButton("Update");
            b.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (!nameField.getText().isEmpty()) {
                        el.setName(nameField.getText());
                    }
                    if (textArea.isEnabled()) {
                        el.setText(textArea.getText());
                    }
                    el.clearAttributes();

                    for(Entry<String, String> entry : table.getAttributes().entrySet()) {
                        el.setAttribute(entry.getKey(), entry.getValue());
                    }

                    fireContentUpdated(el);
                }
            });
            butPanel.add(b);
        }

        if (XmlNode instanceof Comment) {
            final Comment comment = (Comment) XmlNode;


            final JTextArea textArea = new JTextArea(comment.getComment());
            textArea.setPreferredSize(new Dimension(300, 100));
            add(textArea);
            textArea.setBorder(new TitledBorder("Comment"));

            JPanel butPanel = new JPanel();
            add(butPanel);
            JButton b = new JButton("Update");
            b.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (textArea.isEnabled()) {
                        comment.setComment(textArea.getText());
                    }

                    fireContentUpdated(comment);
                }
            });
            butPanel.add(b);
        }
    }

    void fireContentUpdated(XmlNode n) {
        firePropertyChange(NODE_UPDATED, null, n);
    }

    static class AttributeTable extends JPanel {

        private final Element el;
        JTable attrTable = new JTable();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Name", "Value"}, 0);

        public AttributeTable(Element el) {
            setLayout(new BorderLayout(5, 5));
            setPreferredSize(new Dimension(300, 200));
            attrTable.setModel(model);
            attrTable.setFillsViewportHeight(true);
            model.setColumnCount(2);
            this.el = el;
            for (int i=0; i<el.getAttributeCount(); i++) {
                model.addRow(new Object[]{el.getAttributeName(i), el.getAttributeValue(i)});
            }
            add(new JScrollPane(attrTable), BorderLayout.CENTER);

            final Action addAction = new AbstractAction("Add") {
                public void actionPerformed(ActionEvent e) {
                    model.addRow(new Object[]{"", ""});
                }
            };
            
            final Action removeAction = new AbstractAction("Remove") {
                public void actionPerformed(ActionEvent e) {
                    int row = attrTable.getSelectedRow();
                    if (row != -1) {
                        model.removeRow(row);
                    }
                }
            };

            attrTable.addMouseListener(new PopupMenuAdapter() {

                @Override
                public void setMenuContents(JPopupMenu menu, java.awt.Point p) {
                    int row = attrTable.rowAtPoint(p);
                    if (row != -1) {
                        menu.add(new JMenuItem(removeAction));
                    }
                    menu.add(new JMenuItem(addAction));
                }
            });



        }

        public Map<String, String> getAttributes() {

            Map<String, String> attrMap = new HashMap<String, String>();


            for (int i = 0; i < attrTable.getRowCount(); i++) {
                attrMap.put((String) attrTable.getValueAt(i, 0), (String) attrTable.getValueAt(i, 1));


            }
            return attrMap;

        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */