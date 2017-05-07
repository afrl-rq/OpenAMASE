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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Enables editing of lists through a GUI interface. This editor presents the
 * user with a JList and a set of controls to manipulate the JList.
 *
 * @author AFRL/RQQD
 */
public abstract class ListEditor<T> extends JPanel implements ListCellRenderer<T> {

    JList<T> listView;
    EditableListModel listModel;
    JPanel buttonPanel;
    JButton upButton = null, downButton = null, removeButton = null;
    //List<ButtonEntry> buttonList = new ArrayList<>();
    DefaultListCellRenderer renderer = new DefaultListCellRenderer();
    JPanel sidePanel;
    static int inset = 5;

    public static enum BehaviorType {

        SINGLE,
        MULTIPLE,
        NONE
    }

    public ListEditor() {

        setLayout(new BorderLayout(inset, inset));
        setBorder(new EmptyBorder(inset, inset, inset, inset));

        listView = new JList<>();
        JScrollPane scrollPane = new JScrollPane(listView);
        listView.setPreferredSize(new Dimension(240, 240));
        add(scrollPane);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, inset, inset));

        sidePanel = new JPanel(new BorderLayout(inset, inset));
        add(sidePanel, BorderLayout.EAST);
        sidePanel.add(buttonPanel, BorderLayout.NORTH);

        listModel = new EditableListModel();
        listView.setModel(listModel);


        listView.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int[] sels = getSelectedIndicies();



                if (upButton != null) {
                    upButton.setEnabled(sels.length == 1 && sels[0] > 0);
                }
                if (downButton != null) {
                    downButton.setEnabled(sels.length == 1 && sels[0] < getNumItems() - 1);
                }
                if (removeButton != null) {
                    removeButton.setEnabled(sels.length > 0);
                }

                // notify the class of the new selection
                selectionChanged(sels);

            }
        });

        listView.setCellRenderer(this);

    }

    /**
     * Sets the location of the button panel relative to the list. Use the
     * values from {@link BorderLayout}. Values accepted are one of:      {@link BorderLayout#EAST}, 
     * {@link BorderLayout#WEST}, {@link BorderLayout#NORTH}, {@link BorderLayout#SOUTH}.
     *
     * @param location Location to place the button panel.
     */
    public void setButtonLocation(String location) {
        if (location.toUpperCase().matches("(NORTH|SOUTH|EAST|WEST)")) {
            remove(sidePanel);
            add(sidePanel, location);
            revalidate();
        }

    }

    /**
     * Called by the internal model when item indicies are swapped.
     */
    protected abstract void itemsSwapped(int fromIndex, int toIndex);

    /**
     * Called by the internal model when items are removed.
     */
    protected abstract void itemsRemoved(List<T> items, int... indicies);

    /**
     * Returns an item at the given index.
     */
    public abstract T getItem(int index);

    /**
     * Returns the number of items in the list.
     */
    public abstract int getNumItems();

    /**
     * Returns a list renderer. Subclasses can override this to return a new
     * renderer component. the default implementation returns a JLabel. This
     * method follows the same pattern as {@link ListCellRenderer}.
     *
     * @param list list component being rendered.
     * @param item item being rendered
     * @param index index of item rendered
     * @param isSelected the selected property of the item
     * @param cellHasFocus focus property of the item.
     * @return A component to render the item. Returns a JLabel by default.
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends T> list, T item, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) renderer.getListCellRendererComponent(list, item, index, isSelected, cellHasFocus);
        label.setFont(label.getFont().deriveFont(Font.PLAIN));
        label.setBorder(new EmptyBorder(0,2,0,2));
        return label;
    }

    /**
     * Called when the selection changed on the list. Override this to do
     * something useful.
     */
    public void selectionChanged(int... selection) {
    }

    /**
     * Requests an update on the list.
     */
    public void updateView() {
        listModel.updateModel();
    }

    public int[] getSelectedIndicies() {
        return listView.getSelectedIndices();
    }

    public List<T> getSelectedItems() {
        return listView.getSelectedValuesList();
    }

    public void showUpDownButtons(boolean show) {

        if (!show) {
            if (upButton != null) {
                remove(upButton);
                upButton = null;
            }
            if (downButton != null) {
                remove(downButton);
                downButton = null;
            }
            return;
        }

        if (upButton == null) {
            upButton = new JButton(new AbstractAction("Move Up") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int index = listView.getSelectedIndex();
                    if (index > 0) {
                        itemsSwapped(index, index - 1);
                        listView.setSelectedIndex(index - 1);
                    }
                }
            });
            addButton(upButton);
            upButton.setEnabled(listView.getSelectedIndices().length == 1
                    && listView.getSelectedIndex() > 0);
        }


        if (downButton == null) {
            downButton = new JButton(new AbstractAction("Move Down") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int index = listView.getSelectedIndex();
                    if (index < getNumItems() - 1) {
                        itemsSwapped(index, index + 1);
                        listView.setSelectedIndex(index + 1);
                    }
                }
            });
            addButton(downButton);
            downButton.setEnabled(listView.getSelectedIndices().length == 1
                    && listView.getSelectedIndex() < getNumItems() - 1);
        }

    }

    public void showRemoveButton(boolean show) {
        if (!show) {
            if (removeButton != null) {
                remove(removeButton);
                removeButton = null;
            }
            return;
        }

        if (removeButton == null) {
            removeButton = new JButton(new AbstractAction("Remove") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    itemsRemoved(listView.getSelectedValuesList(), listView.getSelectedIndices());
                }
            });
            addButton(removeButton);
            removeButton.setEnabled(getSelectedIndicies().length > 0);
        }
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    public JButton getUpButton() {
        return upButton;
    }

    public JButton getDownButton() {
        return downButton;
    }

    public void addButton(AbstractButton button) {
        buttonPanel.add(button);
        revalidate();
    }

    /**
     * Removes a button from the button list
     */
    public void removeButton(AbstractButton button) {
        buttonPanel.remove(button);
        revalidate();
    }

    /**
     * Adds a gap between buttons
     */
    public void addSpace() {
        buttonPanel.add(new JPanel());
        revalidate();
    }

    /**
     * Programmatically sets the selection in the list.
     */
    public void setSelectedItems(int... items) {
        listView.setSelectedIndices(items);
    }

    /**
     * Sets the enabled feature of a button. Determines if a button is enabled
     * when the selection on the list changes.
     *
     * @param button
     * @param type determines whether the button is enabled for single entry,
     * multiple entry, or no entry.
     */
    public void setButtonBehavior(final AbstractButton button, final BehaviorType type) {
        for (ListSelectionListener l : listView.getListSelectionListeners()) {

            if (l instanceof ButtonBehaviorControl) {
                if (((ButtonBehaviorControl) l).button == button) {
                    listView.removeListSelectionListener(l);
                }
            }
        }
        listView.addListSelectionListener(new ButtonBehaviorControl(button, type));
    }

    protected class EditableListModel extends AbstractListModel {

        public EditableListModel() {
        }

        @Override
        public int getSize() {
            return getNumItems();
        }

        @Override
        public Object getElementAt(int index) {
            return getItem(index);
        }

        public void updateModel() {
            fireContentsChanged(this, 0, getNumItems() - 1);
        }
    }

    static class ButtonBehaviorControl implements ListSelectionListener {

        private final AbstractButton button;
        private final BehaviorType type;

        public ButtonBehaviorControl(AbstractButton button, BehaviorType type) {
            this.button = button;
            this.type = type;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int[] sels = ((JList) e.getSource()).getSelectedIndices();
            if (type == BehaviorType.SINGLE) {
                button.setEnabled(sels.length == 1);
            }
            else if (type == BehaviorType.MULTIPLE) {
                button.setEnabled(sels.length > 0);
            }
            else {
                button.setEnabled(true);
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */