// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map;

import avtas.properties.PropertyEditor;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author AFRL/RQQD
 */
public class LayerPropertiesEditor extends JPanel{
    
    PropertyEditor editor;
    private final MapLayer layer;
    JButton commitButton = new JButton("Save");

    public LayerPropertiesEditor(MapLayer layer) {
        this.layer = layer;
        setLayout(new BorderLayout());
        editor = PropertyEditor.getEditor(layer);
        add(editor, BorderLayout.CENTER);
        add(commitButton, BorderLayout.SOUTH);
        
        commitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                commitProperties();
            }
        });
    }
    
    public void commitProperties() {
        layer.setConfiguration(layer.getConfiguration());
    }
    
    
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */