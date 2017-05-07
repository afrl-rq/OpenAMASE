// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Aug 29, 2004
 */
package org.flexdock.test.drag;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.flexdock.docking.drag.effects.EffectsManager;
import org.flexdock.docking.drag.effects.RubberBand;



/**
 * @author marius
 */
public class RubberBandTest extends JFrame {
    private RubberBand rubberBand;
    private Point offset;

    private RubberBandTest() {
        super("Rubber Band Test");
        rubberBand = EffectsManager.getRubberBand();
        setContentPane(createContentPane());
    }

    private JPanel createContentPane() {
        JPanel jp = new JPanel();
        JLabel lbl = new JLabel("Drag Me");
        DragListener dl = new DragListener();
        lbl.addMouseListener(dl);
        lbl.addMouseMotionListener(dl);
        lbl.setBackground(Color.gray);
        lbl.setOpaque(true);
        jp.add(lbl);
        return jp;

    }

    private void startDrag(MouseEvent me) {
        Point p = me.getPoint();
        SwingUtilities.convertPointToScreen(p, (Component)me.getSource());
        Point win = getLocationOnScreen();
        offset = new Point(win.x-p.x, win.y-p.y);
    }

    private void processDrag(MouseEvent me) {
        Rectangle r = getBounds();
        r.setLocation(getMousePoint(me));
        rubberBand.paint(r);
    }

    private void stopDrag(MouseEvent me) {
        rubberBand.clear();
        setLocation(getMousePoint(me));
        offset = null;
    }

    private Point getMousePoint(MouseEvent me) {
        Point p = me.getPoint();
        SwingUtilities.convertPointToScreen(p, (Component)me.getSource());
        p.x += offset.x;
        p.y += offset.y;
        return p;
    }

    private class DragListener extends MouseAdapter implements MouseMotionListener {
        public void mouseDragged(MouseEvent me) {
            processDrag(me);
        }
        public void mouseMoved(MouseEvent me) {
            // do nothing
        }

        public void mousePressed(MouseEvent me) {
            startDrag(me);
        }

        public void mouseReleased(MouseEvent me) {
            stopDrag(me);
        }
    }


    public static void main(String[] args) {
        JFrame f = new RubberBandTest();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(100, 100);
        f.setVisible(true);
    }

}
