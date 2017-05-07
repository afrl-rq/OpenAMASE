// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.entity;

import avtas.data.Property;
import avtas.data.PropertyMap;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author AFRL/RQQD
 */
public class DataEditor extends JPanel{
    
    private final PropertyMap map;
    JTable table;
    PropertyTableModel model;
    
    
    public DataEditor(PropertyMap map) {
        this.map = map;
        model = new PropertyTableModel();
        table = new JTable(model);
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        add(new JButton(new AbstractAction("Refresh") {

            public void actionPerformed(ActionEvent e) {
                model.buildPropList();
                model.fireTableStructureChanged();
            }
        }), BorderLayout.SOUTH);
       
        
    }
    
    /**
     * Updates the text area by reloading the data in the <code>PropertyMap</code>
     */
    public void update() {
        TableModelEvent event = new TableModelEvent(model, 0, model.getRowCount()-1, 1);
        model.fireTableChanged(event);
        //model.fireTableDataChanged();
    }

    /**
     * Displays the <code>DataBrowser</code> frame.
     */
    public void showBrowserFrame() {
        JFrame f = new JFrame("Properties");
        f.add(this);
        f.pack();
        f.setVisible(true);
        update();
    }
    
    
    class PropertyTableModel extends AbstractTableModel {
        
        private Property[] props = null;

        public PropertyTableModel() {
            buildPropList();
        }
        
        public void buildPropList() {
            this.props = map.sortedView().toArray(new Property[]{});
        }
        
        public int getRowCount() {
            return props.length;
        }

        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int column) {
            switch(column) {
                case 0: return "Property";
                case 1: return "Value";
            }
            return "";
        }
        
        

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (props != null) {
                switch(columnIndex) {
                    case 0:
                        return props[rowIndex].getName();
                        
                    case 1:
                        return props[rowIndex].getValue();
                }
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 1) {
                props[rowIndex].setValue(aValue);
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }

        
        
        
        
    }
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */