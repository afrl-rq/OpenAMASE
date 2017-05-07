// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 4, 2005
 */
package org.flexdock.test.perspective;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.flexdock.demos.util.VSNetStartPage;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.util.SwingUtility;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;

/**
 * @author Christopher Butler
 * @author Mateusz Szczap
 */
public class ViewRestorationTest extends JFrame implements DockingConstants {

    private static View view1 = null;
    private static View view2 = null;
    private static View view3 = null;
    private static View view4 = null;

    public static void main(String[] args) throws Exception {
//		Skin theSkinToUse = SkinLookAndFeel.loadThemePack("themepack.zip");
//        SkinLookAndFeel.setSkin(theSkinToUse);

//		http://dev.l2fprod.com/javadoc/com/l2fprod/gui/plaf/skin/SkinLookAndFeel.html
//		SwingUtility.setPlaf("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");
        SwingUtility.setPlaf("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//		SwingUtility.setPlaf("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
//		SwingUtility.setPlaf("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");

        JFrame f = new ViewRestorationTest();
        f.setSize(800, 600);
        SwingUtility.centerOnScreen(f);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public ViewRestorationTest() {
        super("Simple Show Viewport Demo");
        setContentPane(createContentPane());
        setJMenuBar(createApplicationMenuBar());
    }

    private JPanel createContentPane() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        Viewport viewport = new Viewport();
        panel.add(viewport, BorderLayout.CENTER);

        View startPage = createStartPage();

        view1 = createView("solution.explorer", "Solution Explorer");
        view2 = createView("task.list", "Task List");
        view3 = createView("class.view", "Class View");
        view4 = createView("message.log", "Message Log");

        viewport.dock(startPage);
        startPage.dock(view1, WEST_REGION, .3f);
        startPage.dock(view2, SOUTH_REGION, .3f);
        startPage.dock(view4, EAST_REGION, .3f);
        view1.dock(view3, SOUTH_REGION, .3f);

        return panel;
    }

    private View createView(String id, String text) {
        View view = new View(id, text);
        view.addAction(DockingConstants.CLOSE_ACTION);

        JPanel p = new JPanel();
        p.setBorder(new LineBorder(Color.GRAY, 1));

        JTextField t = new JTextField(text);
        t.setPreferredSize(new Dimension(100, 20));
        p.add(t);
        view.setContentPane(p);

        return view;
    }

    private JMenuBar createApplicationMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu showViewMenu = new JMenu("Show View");

        showViewMenu.add(createShowViewActionFor(view1));
        showViewMenu.add(createShowViewActionFor(view2));
        showViewMenu.add(createShowViewActionFor(view3));
        showViewMenu.add(createShowViewActionFor(view4));

        menuBar.add(showViewMenu);

        return menuBar;
    }

    private Action createShowViewActionFor(View commonView) {
        ShowViewAction showViewAction = new ShowViewAction(commonView.getPersistentId());
        showViewAction.putValue(Action.NAME, commonView.getTitle());

        return showViewAction;
    }

    private class ShowViewAction extends AbstractAction {

        private String m_commonView = null;

        private ShowViewAction(String commonView) {
            m_commonView = commonView;
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            DockingManager.display(m_commonView);
        }

    }

    private View createStartPage() {
        String id = "startPage";
        View view = new View(id, null, null);
        view.setTerritoryBlocked(CENTER_REGION, true);
        view.setTitlebar(null);
        view.setContentPane(new VSNetStartPage());
        return view;
    }
}
