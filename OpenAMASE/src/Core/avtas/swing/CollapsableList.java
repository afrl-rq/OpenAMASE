// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.Border;


/*
 * CollapsableList.java
 *
 * Created on August 11, 2005, 11:16 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
/**
 *
 * @author AFRL/RQQD
 */
public class CollapsableList extends JPanel {

    Icon openedIcon = null;
    Icon closedIcon = null;
    Border itemBorder = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.WHITE);
    JPanel listPanel = new JPanel();

//    public static Icon PLUS_ICON = com.sun.java.swing.plaf.windows.WindowsTreeUI.CollapsedIcon.createCollapsedIcon();
//    public static Icon MINUS_ICON = com.sun.java.swing.plaf.windows.WindowsTreeUI.CollapsedIcon.createExpandedIcon();
//    
//    public static Icon HANDLE_RT_ICON = javax.swing.plaf.metal.MetalIconFactory.getTreeControlIcon(true);
//    public static Icon HANDLE_DN_ICON = javax.swing.plaf.metal.MetalIconFactory.getTreeControlIcon(false);
    public CollapsableList() {
        this(UIManager.getIcon("Tree.expandedIcon"), UIManager.getIcon("Tree.collapsedIcon"));

    }

    public CollapsableList(Icon openedIcon, Icon closedIcon) {
        super();
        this.openedIcon = openedIcon;
        this.closedIcon = closedIcon;
        setLayout(new BorderLayout());
        add(listPanel, BorderLayout.NORTH);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
    }

    public Component add(String name, Component c) {
        CollapsableListItem item = new CollapsableListItem(openedIcon, closedIcon, name, true, c, itemBorder);
        add(item);
        return item.header;
    }

    public Component add(JComponent header, Component c) {
        CollapsableListItem item = new CollapsableListItem(openedIcon, closedIcon, header, true, c, itemBorder);
        add(item);
        return item.header;
    }

    public Component add(Component c) {
        if (c instanceof CollapsableListItem) {
            Component cc = listPanel.add(c);
            revalidate();
            return c;
        }
        else {
            String name = "Item " + (getComponentCount() + 1);
            Component cc = listPanel.add(new CollapsableListItem(openedIcon, closedIcon, name, true, c, itemBorder));
            revalidate();
            return c;
        }
    }

    public void remove(Component c) {
        for (Component cc : getComponents()) {
            if (cc instanceof CollapsableListItem
                    && ((CollapsableListItem) cc).getChild().equals(c)) {
                listPanel.remove(cc);
                revalidate();
            }
        }
    }

    @Override
    public void remove(int index) {
        listPanel.remove(index);
        revalidate();
    }

    public void add(int index, String name, Component c) {
        add(new CollapsableListItem(openedIcon, closedIcon, name, true, c, itemBorder), index);
    }

    public void hideItem(Component c) {
        for (Component cc : getComponents()) {
            if (cc instanceof CollapsableListItem && ((CollapsableListItem) cc).getChild().equals(c)) {
                ((CollapsableListItem) cc).hideChild();
                return;
            }
        }
    }

    /** Sets the default border for each row added to the list */
    public void setItemBorder(Border border) {
        this.itemBorder = border;
        for (Component c : getComponents()) {
            if (c instanceof CollapsableListItem) {
                ((CollapsableListItem) c).setBorder(border);
            }
        }
    }

    public void hideAll() {
        for (Component cc : getComponents()) {
            if (cc instanceof CollapsableListItem) {
                ((CollapsableListItem) cc).hideChild();
            }
        }
    }

    public void showItem(Component c) {
        for (Component cc : getComponents()) {
            if (cc instanceof CollapsableListItem && ((CollapsableListItem) cc).getChild().equals(c)) {
                ((CollapsableListItem) cc).showChild();
                return;
            }
        }
    }

    public void showAll() {
        for (Component cc : getComponents()) {
            if (cc instanceof CollapsableListItem) {
                ((CollapsableListItem) cc).showChild();
            }
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        CollapsableList list = new CollapsableList();
        list.add(new JTree());
        f.add(list);
        f.pack();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static class CollapsableListItem extends JComponent implements ActionListener, MouseListener {

        JCheckBox but = null;
        Component child;
        MouseEvent lastClick = null;
        private final JComponent header;

        public CollapsableListItem(Icon openIcon, Icon closedIcon, String name, boolean showChild, Component child, Border border) {
            this(openIcon, closedIcon, new JLabel(name), showChild, child, border);
        }

        public CollapsableListItem(Icon openIcon, Icon closedIcon, JComponent header, boolean showChild, Component child, Border border) {
            super();
            this.child = child;
            this.header = header;

            but = new JCheckBox(closedIcon);
            but.setSelectedIcon(openIcon);
            but.setSelected(true);
            //but.setMaximumSize( new Dimension(openIcon.getIconWidth(), openIcon.getIconHeight()));
            but.addActionListener(this);


            JPanel labelPanel = new JPanel(new BorderLayout());
            labelPanel.add(but, BorderLayout.WEST);
            labelPanel.add(header, BorderLayout.CENTER);
            labelPanel.addMouseListener(this);
            labelPanel.setBorder(border);

            setLayout(new BorderLayout());
            add(labelPanel, BorderLayout.NORTH);

            if (child != null) {
                if (!showChild) hideChild();
                JPanel childPanel = new JPanel(new BorderLayout());
                childPanel.add(Box.createHorizontalStrut(10), BorderLayout.WEST);
                childPanel.add(child, BorderLayout.CENTER);
                add(childPanel, BorderLayout.CENTER);
            }

        }

        public void showChild() {
            child.setVisible(true);
            but.setSelected(true);
        }

        public void hideChild() {
            child.setVisible(false);
            but.setSelected(false);
        }

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (isShowingChild())
                hideChild();
            else
                showChild();
        }

        public boolean isShowingChild() {
            if (child == null) return false;
            if (child.isVisible())
                return true;
            else return false;
        }

        public Component getChild() {
            return this.child;
        }

        public void setChild(Component child) {
            remove(this.child);
            this.child = child;
            add(child);
        }

        public void mouseReleased(java.awt.event.MouseEvent e) {
        }

        public void mousePressed(java.awt.event.MouseEvent e) {
        }

        public void mouseExited(java.awt.event.MouseEvent e) {
        }

        public void mouseEntered(java.awt.event.MouseEvent e) {
        }

        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getClickCount() > 1) {
                if (isShowingChild())
                    hideChild();
                else
                    showChild();
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */