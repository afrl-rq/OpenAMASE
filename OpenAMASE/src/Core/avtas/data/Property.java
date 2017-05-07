// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.data;

/**
 * Defines a <code>Property</code> which is given by a name and a value.  
 *
 * @author AFRL/RQQD
 */
public class Property <T extends Object> {

    private String name;
    private Object value;

    /**
     * Creates a new <code>Property</code> from the given name and value.
     *
     * @param   name        The name of this <code>Property</code>.
     * @param   value       The value of this <code>Property</code>.
     */
    public Property(String name, T value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the name of the <code>Property</code>.
     *
     * @return      The name of the <code>Property</code>.
     */
    public String getName() {
        return name;
    }

    /**
     * Converts the value of this <code>Property</code> to a double.  If the
     * value is not a Number or a String, 0 is returned.
     *
     * @return          The double representation of this <code>Property</code>
     */
    public double asDouble() {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            return Double.valueOf((String) value);
        }
        return 0;
    }

    /**
     * Converts the value of this <code>Property</code> to a String.  If the value
     * is null, an empty String is returned.
     *
     * @return          The String representation of this <code>Property</code>.
     */
    public String asString() {
        return value == null ? "" : value.toString();
    }

    /**
     * Converts the value of this <code>Property</code> to an integer.  If the
     * value is not a Number or a String, 0 is returned.
     *
     * @return          The integer representation of this <code>Property</code>.
     */
    public int asInteger() {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            return Integer.valueOf((String) value);
        }
        return 0;
    }
    
    /**
     * Converts the value of this <code>Property</code> to a long.  If the
     * value is not a Number or a String, 0 is returned.
     *
     * @return          The long representation of this <code>Property</code>.
     */
    public long asLong() {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            return Long.valueOf((String) value);
        }
        return 0;
    }

    /**
     * Converts the value of this <code>Property</code> to a boolean.  If the
     * value is not a Boolean or a String, false is returned.
     *
     * @return          The boolean representation of this <code>Property</code>.
     */
    public boolean asBool() {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            return Boolean.valueOf((String) value);
        }
        return false;
    }
    
    public void setValue(T value) {
        this.value = value;
    }
    

    /** Converts a value from one unit to another and stores it.
     * @param   value       value to be stored
     * @param   fromUnit    the units of the passed value
     * @param   toUnit      the units to convert the value to
     */
    public void setValue(double value, Unit fromUnit, Unit toUnit) {
        this.value = fromUnit.convertTo( value, toUnit);
    }

    /**
     * Gets the value of this <code>Property</code>.
     *
     * @return      The value of this <code>Property</code>.
     */
    public T getValue() {
        return (T) value;
    }


    @Override
    public String toString() {
        return name + " = " + String.valueOf(value);
    }


}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */