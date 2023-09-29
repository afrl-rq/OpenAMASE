// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.swing;

import javax.swing.SpinnerNumberModel;

/**
 *
 * @author AFRL/RQQD
 */
public class IntegerWrapModel extends SpinnerNumberModel {
    
    int min = 0;
    int max = 0;
    int interval = 0;
    
    int val = 0;
    
    public IntegerWrapModel() {
        this(0, 0, 100, 1);
        
    }
    
    public IntegerWrapModel(int val, int min, int max, int interval) {
        super();
        this.min = min;
        this.max = max;
        this.interval = interval;
        
        setValue(val);
    }
    
    public Object getPreviousValue() {
        if (val - interval >= min)
            return (val - interval);
        else return (max - interval);
    }
    
    public Object getNextValue() {
        if (val + interval < max)
            return (val + interval);
        else return min;
    }
    
    public void setValue(Object o) {
        if (o instanceof Number) {
            val = ((Number) o).intValue();
            super.fireStateChanged();
        }
    }
    
    public Object getValue() {
        return val;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */