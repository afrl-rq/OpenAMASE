// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Feb 26, 2005
 */
package org.flexdock.test.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

import org.flexdock.docking.DockingConstants;
import org.flexdock.plaf.Configurator;
import org.flexdock.plaf.PlafManager;
import org.flexdock.plaf.theme.Theme;
import org.flexdock.plaf.theme.UIFactory;
import org.flexdock.util.SwingUtility;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;
import org.w3c.dom.Element;

/**
 * @author Christopher Butler
 * @author Claudio Romano
 */
public class ViewTest implements DockingConstants {

    private JList viewUIList;
    private JList titlebarUIList;
    private JList buttonUIList;
    private ThemeInfo themeInfo;

    public static void main(String[] args) {
        ViewTest windowTest = new ViewTest();
        windowTest.configureUI();
        windowTest.buildInterface();
    }

    private void configureUI() {
        //UIManager.installLookAndFeel( "Skin LookAndFeel", SkinLookAndFeel.class.getName());
        //UIManager.installLookAndFeel( "Plastic XP LookAndFeel", PlasticXPLookAndFeel.class.getName());

        SwingUtility.setPlaf("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    }

    private void buildInterface() {
        JFrame f = new JFrame();
        f.setJMenuBar(buildMenuBar());
        f.setContentPane(buildContent());
        f.setSize( new Dimension( 800,600));
        SwingUtility.centerOnScreen(f);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private JComponent buildContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        content.setPreferredSize(new Dimension(800, 600));

        Viewport viewport = new Viewport();
        content.add(viewport, BorderLayout.CENTER);

        View view1 = buildView("themeinfo.view", "Theme Info", buildThemeInfoPane());
        View view2 = buildView("plafchooser.view", "Plaf Chooser", buidViewContentPane());

        viewport.dock(view2);
        view2.dock( view1, EAST_REGION, .2f);

        return content;
    }

    private View buildView(String id, String name, JComponent component) {
        View view = new View(id, name);
        view.setIcon("org/flexdock/demos/view/titlebar/msvs001.png");
        view.addAction(new EmptyAction(CLOSE_ACTION));
        view.addAction(new EmptyAction(PIN_ACTION));
        view.setContentPane(component);

        return view;
    }


    private JMenuBar buildMenuBar() {
        JMenu menu;
        JMenuBar menuBar = new JMenuBar();
        menu = new JMenu("Available LookAndFeel's");

        LookAndFeelInfo[] lfInfos = UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < lfInfos.length; i++) {
            menu.add(new JMenuItem(new ChangeLookAndFeelAction(lfInfos[i])));
        }
        menuBar.add(menu);

        return menuBar;
    }

    private JComponent buidViewContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5));
        contentPane.add(buildLists(), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(new JButton(new ChangePlafAction()));
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        return contentPane;
    }

    private JComponent buildThemeInfoPane() {
        themeInfo = new ThemeInfo();
        //themeInfo.update(PlafManager.getPreferredTheme());
        return themeInfo.createPanel();
    }

    private JComponent buildLists() {
        viewUIList = new JList(getUIList("view-ui"));
        titlebarUIList = new JList(getUIList("titlebar-ui"));
        buttonUIList = new JList(getUIList("button-ui"));

        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        panel.add(createUIListComp("View", viewUIList));
        panel.add(createUIListComp("Titlebar", titlebarUIList));
        panel.add(createUIListComp("Button", buttonUIList));

        return panel;
    }

    private JComponent createUIListComp(String name, JList uiList) {
        uiList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));


        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.LIGHT_GRAY);
        header.setBorder( BorderFactory.createEmptyBorder(2,2,2,2));
        header.add(new JLabel(name), BorderLayout.WEST);

        uiList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        uiList.setPreferredSize(new Dimension(170, 100));

        panel.add(header, BorderLayout.NORTH);
        panel.add(uiList, BorderLayout.CENTER);

        return panel;
    }

    private Object[] getUIList(String tagName) {
        List tagNames = new ArrayList();
        HashMap elements = Configurator.getNamedElementsByTagName(tagName);
        for (Iterator it = elements.keySet().iterator(); it.hasNext();) {
            Element elem = (Element) elements.get(it.next());
            tagNames.add(elem.getAttribute(Configurator.NAME_KEY));
        }

        return tagNames.toArray();
    }

    private class ChangePlafAction extends AbstractAction {

        public ChangePlafAction() {
            putValue(Action.NAME, "Apply custom theme");
        }

        public void actionPerformed(ActionEvent arg0) {
            Properties p = new Properties();
            if( viewUIList.getSelectedValue() != null)
                p.setProperty(UIFactory.VIEW_KEY, viewUIList.getSelectedValue().toString());
            if( titlebarUIList.getSelectedValue() != null)
                p.setProperty(UIFactory.TITLEBAR_KEY, titlebarUIList.getSelectedValue().toString());
            if( buttonUIList.getSelectedValue() != null)
                p.setProperty(UIFactory.BUTTON_KEY, buttonUIList.getSelectedValue().toString());

            Theme theme = PlafManager.setCustomTheme("custom.theme", p);
            PlafManager.setPreferredTheme("custom.theme", true);
            themeInfo.update(theme);
            //PlafManager.installPreferredTheme();
        }

    }

    private  class ChangeLookAndFeelAction extends AbstractAction {

        private LookAndFeelInfo lfInfo;

        private ChangeLookAndFeelAction(LookAndFeelInfo lfInfo) {
            this.lfInfo = lfInfo;
            putValue(Action.NAME, lfInfo.getName());
        }

        public void actionPerformed(ActionEvent event) {
            SwingUtility.setPlaf(lfInfo.getClassName());
            PlafManager.setPreferredTheme("custom.theme", true);
        }

    }

    private static class EmptyAction extends AbstractAction {
        private EmptyAction(String name) {
            putValue(Action.NAME, name);
        }

        public void actionPerformed(ActionEvent e) {
        }
    }

    public static class ThemeInfo {
        private final JLabel view = new JLabel("View:");
        private final JLabel titlebar = new JLabel("Titlebar:");
        private final JLabel button = new JLabel("Button:");
        private final JLabel vView = new JLabel("");
        private final JLabel vTitlebar = new JLabel("");
        private final JLabel vButton = new JLabel("");

        public JPanel createPanel() {

            JPanel panel = new JPanel(null) {
                public void doLayout() {
                    int x = 10;
                    int row = 1;
                    int rowInc = 22;
                    int labelWeight = 60 + 10;
                    int valueWidth = 120;
                    int height = (int) view.getPreferredSize().getHeight();

                    view.setBounds(x, row * rowInc, labelWeight, height);
                    vView.setBounds(labelWeight + 10, row * rowInc, valueWidth, height);
                    row++;
                    titlebar.setBounds(x, row * rowInc, labelWeight, height);
                    vTitlebar.setBounds(labelWeight + 10, row * rowInc, valueWidth, height);
                    row++;
                    button.setBounds(x, row * rowInc, labelWeight, height);
                    vButton.setBounds(labelWeight + 10, row * rowInc, valueWidth, height);

                    setPreferredSize(new Dimension(400, 100));
                }
            };


            panel.add(view);
            panel.add(vView);
            panel.add(titlebar);
            panel.add(vTitlebar);
            panel.add(button);
            panel.add(vButton);

            return panel;
        }

        public void update(Theme theme) {
            vView.setText(theme.getViewUI().getCreationParameters().getName());
            vTitlebar.setText(theme.getTitlebarUI().getCreationParameters().getName());
            vButton.setText(theme.getButtonUI().getCreationParameters().getName());
        }
    }

}