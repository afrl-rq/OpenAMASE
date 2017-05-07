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
import avtas.util.WindowUtils;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 * Allows the user to view data as the simulation runs
 *
 * @author AFRL/RQQD
 */
public class EntityDataViewer extends JPanel {

    private final PropertyMap map;
    JXTable table;
    PropertyTableModel model;

    public EntityDataViewer(PropertyMap map) {
        this.map = map;
        model = new PropertyTableModel();
        table = new JXTable(model);
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel butPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        butPanel.add(new JButton(new AbstractAction("Update") {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        }));
        add(butPanel, BorderLayout.SOUTH);

        table.addHighlighter(HighlighterFactory.createSimpleStriping());
    }

    public void update() {
        model.buildPropList();
        model.fireTableStructureChanged();
    }

    class PropertyTableModel extends AbstractTableModel {

        private List<Property> props = null;

        public PropertyTableModel() {
            buildPropList();
        }

        public void buildPropList() {
            this.props = map.sortedView();
        }

        @Override
        public int getRowCount() {
            return props.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Property";
                case 1:
                    return "Value";
            }
            return "";
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (props != null) {
                switch (columnIndex) {
                    case 0:
                        return props.get(rowIndex).getName();

                    case 1:
                        return props.get(rowIndex).getValue();
                }
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 1) {
                props.get(rowIndex).setValue(aValue);
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }

    }

    public static void main(String[] args) {
        WindowUtils.showApplicationWindow(new EntityDataViewer(new EntityData()));
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */