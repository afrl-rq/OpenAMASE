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
import avtas.swing.PopupMenuAdapter;
import avtas.swing.PopupSupport;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

/**
 *
 * @author AFRL/RQQD
 */
public class UnitConverterPanel extends JPanel {

    JTextField inputField = new JTextField(20);
    JTextField outputField = new JTextField(20);
    JComboBox<Unit> fromBox = new JComboBox<>();
    JComboBox<Unit> toBox = new JComboBox<>();

    public UnitConverterPanel() {

        //setBorder(new TitledBorder("Unit Converter"));

        fromBox = new JComboBox<>(Unit.values());

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;

        constraints.gridx = 0;
        constraints.gridy = 0;
        add(new JLabel("From"), constraints);

        constraints.gridy = 1;
        add(new JLabel("To"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        add(fromBox, constraints);

        constraints.gridy = 1;
        add(toBox, constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        add(inputField, constraints);

        constraints.gridy = 1;
        add(outputField, constraints);
        
        
        JButton pasteButton = new JButton(new AbstractAction("Paste") {

            @Override
            public void actionPerformed(ActionEvent e) {
               String valStr = WindowUtils.copyFromClipboard();
               if (valStr != null && valStr.matches(Unit.DECIMAL_MATCHER)) {
                   inputField.setText(valStr);
               }
            }
        });
        constraints.gridy = 0;
        constraints.gridx = 3;
        add(pasteButton, constraints);
        
        JButton copyButton = new JButton(new AbstractAction("Copy") {

            @Override
            public void actionPerformed(ActionEvent e) {
               String resultStr = outputField.getText();
               if (!resultStr.isEmpty()) {
                   WindowUtils.copyToClipboard(resultStr);
               }
            }
        });
        constraints.gridy = 1;
        constraints.gridx = 3;
        add(copyButton, constraints);
        

        inputField.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                if (!inputField.getText().matches(Unit.DECIMAL_MATCHER)) {
                    e.getEdit().undo();
                    Toolkit.getDefaultToolkit().beep();
                }
                else {
                    convert();
                }
            }
        });

        fromBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                toBox.removeAllItems();
                Unit fromUnit = (Unit) fromBox.getSelectedItem();
                if (fromUnit != null) {
                    for (Unit u : Unit.getAllUnits(fromUnit.getUnitType())) {
                        if (u != fromUnit)
                            toBox.addItem(u);
                    }
                    toBox.setSelectedIndex(0);
                    convert();
                }
            }
        });

        toBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Unit toUnit = (Unit) toBox.getSelectedItem();
                if (toUnit != null) {
                    convert();
                }
            }
        });
        toBox.setToolTipText("A tip");

        outputField.setEditable(false);
        
        PopupSupport popsupport = new PopupSupport(outputField);
        popsupport.addPopupMenuAdapter(new PopupMenuAdapter() {
            @Override
            public void setMenuContents(JPopupMenu menu, java.awt.Point p) {
                menu.add(new AbstractAction("Copy") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (outputField.getSelectedText() == null || outputField.getSelectedText().isEmpty())
                            outputField.selectAll();
                        outputField.copy();
                    }
                });
            }
        });
        
        

    }

    double convert() {
        double fromVal = 0, toVal = 0;
        try {
            fromVal = Double.valueOf(inputField.getText());
        } catch (NumberFormatException ex) {
            return toVal;
        }

        Unit fromUnit = (Unit) fromBox.getSelectedItem();
        Unit toUnit = (Unit) toBox.getSelectedItem();

        if (fromUnit != null && toUnit != null) {
            toVal = fromUnit.convertTo(fromVal, toUnit);
            outputField.setText(String.valueOf(toVal));
        }
        return toVal;
    }

    public static void main(String[] args) {
        WindowUtils.showApplicationWindow(new UnitConverterPanel());
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */