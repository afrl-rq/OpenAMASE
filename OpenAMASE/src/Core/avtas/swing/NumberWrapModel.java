// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.swing;

import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Implements a spinner model that wraps numbers when the minimum or maximum is reached.
 * @author AFRL/RQQD
 */
public class NumberWrapModel extends SpinnerNumberModel {

    public NumberWrapModel() {
        this(0, 0, 100, 1);

    }

    public NumberWrapModel(double val, double min, double max, double interval) {
        super(val, min, max, interval);
    }

    public NumberWrapModel(int val, int min, int max, int interval) {
        super(val, min, max, interval);
    }

    public Object getPreviousValue() {
        Object val = super.getPreviousValue();
        if (val == null) {
            return getMaximum();
        }
        return val;
    }

    public Object getNextValue() {
        Object val = super.getNextValue();
        if (val == null) {
            return getMinimum();
        }
        return val;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.add(new JSpinner(new NumberWrapModel(10, 0, 100, 2)));
        f.pack();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */