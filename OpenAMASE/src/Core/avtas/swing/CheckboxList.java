// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * An implementation of a list that uses checkboxes to render items. The view of
 * each item is based on the toString method. Checkbox selection indicates the
 * selection status of that item.
 *
 * @author AFRL/RQQD
 */
public class CheckboxList<T> extends JPanel {

    List<CheckBoxItem> itemList = new ArrayList<CheckBoxItem>();
    boolean singularSelection = false;

    public CheckboxList() {

        this(false);
    }
    
    /**
     * Creates a new CheckboxList.
     * @param singularSelection if true, this list can have at most one selected item
     */
    public CheckboxList(boolean singularSelection) {
        this.singularSelection = singularSelection;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(true);
        setBackground(UIManager.getColor("List.background"));
    }


    @Override
    public void updateUI() {
        setBackground(UIManager.getColor("List.background"));
        super.updateUI();
    }

    public void addItem(T item) {
        addItem(item, itemList.size());
    }

    public void addItem(T item, boolean initialSelection) {
        addItem(item, itemList.size());
        itemList.get(itemList.size() - 1).checkBox.setSelected(initialSelection);
        revalidate();
    }

    public void addItem(T item, int index) {
        CheckBoxItem cb = new CheckBoxItem(item);
        itemList.add(index, cb);
        add(cb.checkBox, index);
        revalidate();
    }

    public T setItem(int index, T item) {
        CheckBoxItem old = itemList.set(index, new CheckBoxItem(item));
        add(itemList.get(index).checkBox, index);
        remove(++index);
        revalidate();
        return old == null ? null : old.item;
    }

    public void setListData(T[] listData) {
        itemList.clear();
        removeAll();
        for (T o : listData) {
            addItem(o);
        }
    }

    public void setListData(List<T> listData) {
        itemList.clear();
        removeAll();
        for (T o : listData) {
            addItem(o);
        }
    }

    public T getItem(int index) {
        return itemList.get(index).item;
    }

    public T getItemAt(Point p) {
        Component c = getComponentAt(p);
        for (CheckBoxItem i : itemList) {
            if (i.checkBox == c) {
                return i.item;
            }
        }
        return null;
    }

    public List<T> getAllItems() {
        List<T> retList = new ArrayList<T>();
        for (CheckBoxItem i : itemList) {
            retList.add(i.item);
        }
        return retList;
    }

    public List<T> getSelectedValues() {
        List<T> retList = new ArrayList<T>();
        for (CheckBoxItem i : itemList) {
            if (i.checkBox.isSelected()) {
                retList.add(i.item);
            }
        }
        return retList;
    }

    public void removeItem(int index) {
        CheckBoxItem box = itemList.remove(index);
        remove(box.checkBox);
        revalidate();
    }

    public void clear() {
        itemList.clear();
        removeAll();
        revalidate();
    }

    public int indexOf(T item) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).item.equals(item)) {
                return i;
            }
        }
        return -1;
    }

    public int getLength() {
        return itemList.size();
    }

    // selection methods
    public void selectAll() {
        for (CheckBoxItem i : itemList) {
            i.checkBox.setSelected(true);
        }
        ListSelectionEvent e = new ListSelectionEvent(this, 0, itemList.size() - 1, false);
        for (ListSelectionListener l : listenerList.getListeners(ListSelectionListener.class)) {
            l.valueChanged(e);
        }
    }

    public void selectNone() {
        for (CheckBoxItem i : itemList) {
            i.checkBox.setSelected(false);
        }
        ListSelectionEvent e = new ListSelectionEvent(this, 0, itemList.size() - 1, false);
        for (ListSelectionListener l : listenerList.getListeners(ListSelectionListener.class)) {
            l.valueChanged(e);
        }
    }

    public void setSelection(int... indexes) {
        selectNone();
        addSelection(indexes);
    }

    public void setSelection(T... items) {
        List<T> selList = Arrays.asList(items);
        for (CheckBoxItem item : itemList) {
            item.checkBox.setSelected(selList.contains(item.item));
        }
    }

    public void setSelectionRange(int start, int end) {
        int[] vals = new int[end - start];
        for (int i = start; i < end; i++) {
            vals[i - start] = i;
        }
        setSelection(vals);
    }

    public void addSelection(int... indexes) {
        for (int i : indexes) {
            itemList.get(i).checkBox.setSelected(true);
            fireSelectionChanged(itemList.get(i));
        }
    }

    public void addSelectionRange(int start, int end) {
        int[] vals = new int[end - start];
        for (int i = start; i < end; i++) {
            vals[i - start] = i;
        }
        addSelection(vals);
    }

    public boolean selectItem(Object item) {
        for (CheckBoxItem i : itemList) {
            if (i.item.equals(item)) {
                i.checkBox.setSelected(true);
                return true;
            }
        }
        return false;
    }

    public boolean deselectItem(Object item) {
        for (CheckBoxItem i : itemList) {
            if (i.item.equals(item)) {
                i.checkBox.setSelected(false);
                return true;
            }
        }
        return false;
    }

    public int[] getSelectedIndices() {
        List<Integer> retList = new ArrayList<Integer>();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).checkBox.isSelected()) {
                retList.add(i);
            }
        }
        int[] retArray = new int[retList.size()];
        for (int i = 0; i < retList.size(); i++) {
            retArray[i] = retList.get(i);
        }

        return retArray;
    }

    public void addListSelectionListener(ListSelectionListener listener) {
        listenerList.add(ListSelectionListener.class, listener);
    }

    public void removeListSelectionListener(ListSelectionListener listener) {
        listenerList.remove(ListSelectionListener.class, listener);
    }

    void fireSelectionChanged(CheckBoxItem item) {
        if (singularSelection && item.checkBox.isSelected()) {
            for (CheckBoxItem tmp : itemList) {
                if (tmp != item)  {
                    tmp.checkBox.setSelected(false);
                }
            }
        }
        int index = itemList.indexOf(item);
        ListSelectionEvent e = new ListSelectionEvent(this, index, index, true);
        for (ListSelectionListener l : listenerList.getListeners(ListSelectionListener.class)) {
            l.valueChanged(e);
        }
    }


    class CheckBoxItem {

        public CheckBoxItem(T item) {
            checkBox = new JCheckBox(String.valueOf(item));
            checkBox.setOpaque(false);
            this.item = item;
            checkBox.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    fireSelectionChanged(CheckBoxItem.this);
                }
            });
        }
        JCheckBox checkBox;
        T item;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();

        CheckboxList list = new CheckboxList();

        list.addItem("this");
        list.addItem("is");
        list.addItem("a");
        list.addItem("test");

        list.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                System.out.println(e.getFirstIndex() + " - " + e.getLastIndex());
            }
        });

        f.add(new JScrollPane(list));
        f.pack();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */