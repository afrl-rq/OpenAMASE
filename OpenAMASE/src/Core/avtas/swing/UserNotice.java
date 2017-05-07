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
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.jdesktop.swingx.JXBusyLabel;

/**
 *
 * @author AFRL/RQQD
 */
public class UserNotice extends JDialog {

    static Color backColor = new Color(0, 0, 0, 150);
    static Color foreColor = Color.WHITE;
    static int minWidth = 200;
    JXBusyLabel label = new JXBusyLabel();
    Component parent;

    public UserNotice(String text, Component parent) {
        
        setUndecorated(true);

        JPanel backPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                g.clearRect(0, 0, getWidth(), getHeight());
                g.setColor(backColor);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        backPanel.setLayout(new BorderLayout());
        setContentPane(backPanel);

        this.parent = parent;

        setBackground(new Color(0, 0, 0, 0));
        add(label, BorderLayout.CENTER);

        label.setText(text);
        label.setForeground(foreColor);
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        label.setBorder(new EmptyBorder(10, 10, 10, 10));

        label.setBusy(true);

        pack();
        setLocationRelativeTo(parent);
        
    }

    public JXBusyLabel getLabel() {
        return label;
    }

    public void setText(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                label.setText(text);
                pack();
                setLocationRelativeTo(parent);
            }
        });

    }

    public static void main(String[] args) {
        try {
            final UserNotice waiter = new UserNotice("this is a a long test", null);
            waiter.setVisible(true);
            Thread.sleep(2000);
            waiter.setText("this is a really long text string to see if the window changes size");
            //waiter.showWindow(null);

        } catch (InterruptedException ex) {
            Logger.getLogger(UserNotice.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */