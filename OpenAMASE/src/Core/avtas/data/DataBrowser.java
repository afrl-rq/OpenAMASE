// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.data;

import avtas.swing.PopupMenuAdapter;
import avtas.swing.PopupSupport;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A simple data browser for an application.  Data in a <code>PropertyMap</code>
 * is converted to a string and displayed in a text area.
 *
 * @author AFRL/RQQD
 */
public class DataBrowser extends JPanel {
    
    PropertyMap data;
    JTextArea area;

    /**
     * Creates a new <code>DataBrowser</code> from the given <code>PropertyMap</code>.
     *
     * @param data
     */
    public DataBrowser(PropertyMap data) {
        setLayout(new GridLayout(0, 1));
        this.data = data;
        area = new JTextArea();
        area.setFont(Font.decode(Font.MONOSPACED));
        JScrollPane pane = (new JScrollPane(area));
        pane.setPreferredSize(new Dimension(400, 300));
        area.setText(data.toString());
        add(pane);
        
        PopupSupport popup = new PopupSupport(area);
        popup.addPopupMenuAdapter(new PopupMenuAdapter() {
            @Override
            public void setMenuContents(JPopupMenu menu, Point p) {
                menu.add(new AbstractAction("Refresh") {

                    public void actionPerformed(ActionEvent e) {
                        update();
                    }
                });
            }
        });
        
        
    }

    /**
     * Updates the text area by reloading the data in the <code>PropertyMap</code>
     */
    public void update() {
            area.setText(data.toString());
    }

    /**
     * Displays the <code>DataBrowser</code> frame.
     */
    public void showBrowserFrame() {
        JFrame f = new JFrame();
        f.add(this);
        f.pack();
        f.setVisible(true);
        update();
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */