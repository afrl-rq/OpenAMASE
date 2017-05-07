// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.setup;

import avtas.amase.AmasePlugin;
import avtas.amase.scenario.ScenarioEvent;
import avtas.app.AppEventManager;
import avtas.util.WindowUtils;
import avtas.xml.Element;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayDeque;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * Keeps a list of undo events
 * @author AFRL/RQQD
 */
public class UndoManager extends AmasePlugin {

    //ArrayList<Element> history = new ArrayList<Element>();
    ArrayDeque<Element> undoList = new ArrayDeque<Element>();
    ArrayDeque<Element> redoList = new ArrayDeque<Element>();
    Element current = null;
    int undoPtr = 0;
    static int MAX_UNDO = 20;
    AppEventManager eventManager = AppEventManager.getDefaultEventManager();
    File eventFile = null;

    void addUndoableEdit(Element el) {
        if (current != null) {
            undoList.push(current);
        }
        current = el.clone();
        while (undoList.size() > MAX_UNDO) {
            undoList.removeLast();
        }
        redoList.clear();
    }

    public void undo() {
        if (canUndo()) {
            if (current != null) {
                redoList.push(current);
            }
            Element el = undoList.pop();
            current = el;
            if (eventManager != null) {
                eventManager.fireEvent(new ScenarioEvent(eventFile, el.clone()), this);
            }
        }
    }

    public void redo() {
        if (canRedo()) {
            if (current != null) {
                undoList.push(current);
            }
            Element el = redoList.pop();
            current = el;
            if (eventManager != null) {
                eventManager.fireEvent(new ScenarioEvent(eventFile, el.clone()), this);
            }
        }
    }

    public boolean canUndo() {
        return !undoList.isEmpty();
    }

    public boolean canRedo() {
        return !redoList.isEmpty();
    }

    @Override
    public void getMenus(JMenuBar menubar) {
        
        JMenu editMenu = WindowUtils.getMenu(menubar, "Edit");

        final JMenuItem undoMenu = new JMenuItem(new AbstractAction("Undo") {

            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });
        editMenu.add(undoMenu);
        undoMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));

        final JMenuItem redoMenu = new JMenuItem(new AbstractAction("Redo") {

            @Override
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        });
        editMenu.add(redoMenu);
        redoMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));

        editMenu.addMenuListener(new MenuListener() {

            @Override
            public void menuSelected(MenuEvent e) {
                undoMenu.setEnabled(canUndo());
                redoMenu.setEnabled(canRedo());
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof ScenarioEvent) {
            ScenarioEvent scenEvent = (ScenarioEvent) event;
            addUndoableEdit(scenEvent.getXML());
            this.eventFile = scenEvent.getSourceFile();
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */