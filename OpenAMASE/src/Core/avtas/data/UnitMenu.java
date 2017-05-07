// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.data;

import avtas.util.WindowUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * Provides a contextual menu that allows unit conversion.  To use this class, add a listener
 * to the menu.  Before invoking the menu, set the initial value using the {@link #setValue(double) }
 * method.  Then invoke the menu.  When an action occurs, read the converted value by calling the
 * {@link #getValue() } method.
 *
 * @author AFRL/RQQD
 */
public class UnitMenu extends JMenu {

    double fromValue = 0;
    double toValue = 0;
    Unit fromUnit = null;
    Unit toUnit = null;

    public UnitMenu() {
        super("Convert...");
        setupSubMenus();
    }

    public UnitMenu(double val) {
        this();
        setValue(val);
    }

    private void setupSubMenus() {
        for (Unit.UnitType type : Unit.UnitType.values()) {
            JMenu typeMenu = new JMenu(type.name());
            add(typeMenu);
            for (final Unit fu : Unit.getAllUnits(type)) {

                JMenu fuMenu = new JMenu(fu.name());
                typeMenu.add(fuMenu);
                fuMenu.addMenuListener(new MenuListener() {

                    public void menuSelected(MenuEvent e) {
                        fromUnit = fu;
                    }

                    public void menuDeselected(MenuEvent e) {
                    }

                    public void menuCanceled(MenuEvent e) {
                    }
                });

                for (final Unit tu : Unit.getAllUnits(type)) {
                    if (tu != fu) {
                        JMenuItem tuMenu = new JMenuItem(tu.name());
                        fuMenu.add(tuMenu);
                        tuMenu.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                toUnit = tu;
                                fireConvertAction();
                            }
                        });
                    }
                }
            }
        }
    }

    public double getValue() {
        return toValue;
    }

    public void setValue(double value) {
        this.fromValue = value;
    }

    private void fireConvertAction() {
        if (fromUnit != null && toUnit != null) {
            this.toValue = fromUnit.convertTo(fromValue, toUnit);
        }
        fireActionPerformed(new ActionEvent(this, (int) (Math.random() * Integer.MAX_VALUE), getActionCommand()));
    }
    
    public static void main(String[] args) {
        JFrame frame = WindowUtils.showApplicationWindow(new JPanel());
        frame.setJMenuBar(new JMenuBar());
        frame.getJMenuBar().add(new UnitMenu());
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */