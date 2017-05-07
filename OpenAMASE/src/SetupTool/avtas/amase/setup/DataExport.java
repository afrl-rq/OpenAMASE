// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.setup;

import avtas.amase.AmasePlugin;
import avtas.amase.scenario.ScenarioEvent;
import avtas.util.WindowUtils;
import avtas.xml.XMLUtil;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import avtas.xml.Element;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

/**
 *
 * @author AFRL/RQQD
 */
public class DataExport extends AmasePlugin {

    JTree tree = new JTree();
    JFileChooser chooser = new JFileChooser();
    private ScenarioEvent scenarioEvent = null;

    public DataExport() {
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);

        tree.setCellRenderer(renderer);
        tree.setEditable(false);
        tree.setPreferredSize(new Dimension(300, tree.getPreferredSize().height));

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tree.getRowForLocation(e.getX(), e.getY());

                if (row != -1) {
                    TreePath path = tree.getPathForRow(row);
                    SelectNode node = (SelectNode) path.getLastPathComponent();
                    node.click(e, tree.getRowBounds(row));
                    ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(node);
                }
            }
        });
    }

    @Override
    public void getMenus(final JMenuBar menubar) {

        JMenu menu = WindowUtils.getMenu(menubar, "File");
        menu.add(new JMenuItem(new AbstractAction("Export Data") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (scenarioEvent != null)
                    showExportWindow( (JFrame) JOptionPane.getFrameForComponent(menubar), 
                            scenarioEvent.getXML().clone());
            }
        }));
        menu.addSeparator();
    }

    void showExportWindow(JFrame parentFrame, final Element xmlElement) {

        final SelectNode topNode = new SelectNode(xmlElement);
        tree.setModel(new DefaultTreeModel(topNode));

        final JDialog dialog = new JDialog(parentFrame, "Export Data", true);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.add(new JScrollPane(tree), BorderLayout.CENTER);
        JPanel selectPanel = new JPanel();

        JButton exportBut = new JButton(new AbstractAction("Export") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ans = chooser.showSaveDialog(tree);
                if (ans == JFileChooser.APPROVE_OPTION) {
                    if (chooser.getSelectedFile() != null) {
                        topNode.removeUnselected();
                        topNode.el.toFile(chooser.getSelectedFile());
                        dialog.setVisible(false);
                    }
                }
            }
        });
        selectPanel.add(exportBut);

        selectPanel.add(new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        }));

        panel.add(selectPanel, BorderLayout.SOUTH);


        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);

    }

    protected DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            if (value instanceof SelectNode) {
                SelectNode node = (SelectNode) value;
                JPanel p = new JPanel();
                p.setBackground(tree.getBackground());
                JCheckBox box = node.checkBox;
                box.setBackground(tree.getBackground());
                p.add(box);
                JLabel label = new JLabel(node.el.getName());
                p.add(label);
                p.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                tree.setRowHeight(box.getPreferredSize().height);
                return p;
            }
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        }
    };

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof ScenarioEvent) {
            this.scenarioEvent = (ScenarioEvent) event;
        }
    }

    protected static class SelectNode extends DefaultMutableTreeNode {

        Element el;
        JCheckBox checkBox = null;

        public SelectNode(Element el) {
            super(el.getName());
            this.el = el;
            checkBox = new JCheckBox();
            checkBox.setSelected(true);
            for (Element child : el.getChildElements()) {
                SelectNode childNode = new SelectNode(child);
                add(childNode);
            }

            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    for (int i = 0; i < getChildCount(); i++) {
                        SelectNode child = (SelectNode) getChildAt(i);
                        child.checkBox.setSelected(checkBox.isSelected());
                    }
                }
            });
        }

        void click(MouseEvent e, Rectangle rowBounds) {
            if (checkBox.contains(e.getPoint().x - rowBounds.x, e.getPoint().y - rowBounds.y)) {
                checkBox.doClick();
            }
        }

        public void removeUnselected() {
            for (int i = 0; i < getChildCount(); i++) {
                SelectNode child = (SelectNode) getChildAt(i);
                if (!child.checkBox.isSelected()) {
                    el.remove(child.el);
                }
                child.removeUnselected();
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */