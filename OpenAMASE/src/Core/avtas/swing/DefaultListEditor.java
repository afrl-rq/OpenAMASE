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
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author AFRL/RQQD
 */
public class DefaultListEditor<T> extends ListEditor<T> {

    private final List<T> list;

    public DefaultListEditor(List<T> itemList) {
        this.list = itemList;
    }
    
    @Override
    public void itemsSwapped(int index1, int index2) {
        Collections.swap(list, index1, index2);
        updateView();
    }

    @Override
    public void itemsRemoved(List<T> items, int... indicies) {
        list.removeAll(items);
        updateView();
    }

    public List<T> getItems() {
        return list;
    }

    @Override
    public T getItem(int index) {
        return list.get(index);
    }

    @Override
    public int getNumItems() {
        return list.size();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final List<String> list = new ArrayList<>();
        final DefaultListEditor<String> editor = new DefaultListEditor(list);

        list.add("Mary");
        list.add("Lamb");
        list.add("Fleece");
        list.add("Snow");
        editor.updateView();

        editor.showUpDownButtons(true);
        editor.showRemoveButton(true);

        editor.addButton(new JButton(new AbstractAction("Add Item") {
            @Override
            public void actionPerformed(ActionEvent e) {
                list.add("test");
                editor.updateView();
            }
        }));
        
        //editor.setButtonLocation(BorderLayout.WEST);

        frame.add(editor);
        frame.setSize(640, 480);
        frame.setVisible(true);

    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */