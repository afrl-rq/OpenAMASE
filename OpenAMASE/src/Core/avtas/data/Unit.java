// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.data;

import java.util.ArrayList;
import java.util.List;

/**
 * An enumeration describing various units to use when handling and converting numerical values.
 * 
 * @author AFRL/RQQD
 */
public enum Unit {

    /** Default Unit to use when an object has no units */
    NONE(1.0, 0, UnitType.None, "None"),
    // Linear
    /** Meters (base unit) */
    METER(1.0, 0, UnitType.Length, "Meter", "M"),
    
    /** Kilometers  ( 1 km = 1000 meters ) */
    KM(1000.0, 0, UnitType.Length, "KM"),
    
    /** Feet  ( 1 ft = 0.3048 meters ) */
    FEET(0.3048, 0, UnitType.Length, "Feet", "FT"),
    
    /** Nautical Miles (1 nm = 1852 meters) */
    NM(1852, 0, UnitType.Length, "NM"),
    /** Statute Miles ( 1 MI = 1609.344 meters) */
    MILE(1609.344, 0, UnitType.Length, "Mile", "Mi"),
    
    // Area
    /** Square Meters (base unit) */
    SQMETER(1.0, 0, UnitType.Area, "M^2"),
    
    /** Square Feet (1 SQ ft = 0.092903 SQ meter) */
    SQFEET(0.092903, 0, UnitType.Area, "Ft^2", "SqFt"),

    // Angle
    RADIANS(1.0, 0, UnitType.Angle, "Rad"),
    
    DEGREES(Math.PI/180.0, 0, UnitType.Angle, "Degrees", "Deg"),

    // Mass
    /** mass units in KG (base unit) */
    KG(1.0, 0, UnitType.Mass, "KG"),
    /** mass units in lbs ( 1 lb = 0.453592 KG) */
    LBS(0.453592, 0, UnitType.Mass, "Lbs"),
    // Velocity
    /** Meters per second (base unit) */
    MPS(1.0, 0, UnitType.Velocity, "MPS"),
    /** Feet per second */
    FPS(0.3048, 0, UnitType.Velocity, "FPS"),
    /** feet per minute */
    FPM(FPS.factor / 60., 0, UnitType.Velocity, "FPM"),
    /** nautical miles per hour */
    KNOTS(NM.factor / 3600., 0, UnitType.Velocity, "Kts"),
    /** statute miles per hour */
    MPH(MILE.factor / 3600., 0, UnitType.Velocity, "MPH"),
    /** kilometers per hour */
    KPH(KM.factor / 3600., 0, UnitType.Velocity, "KPH"),
    
    //Time
    /** base unit */
    SECONDS(1.0, 0, UnitType.Time, "Seconds", "Sec"),
    
    /** 1 minute = 60 sec */
    MINUTES(60, 0, UnitType.Time, "Minutes", "Min"),
    
    /** 1 hour = 3600 sec */
    HOURS(3600, 0, UnitType.Time, "Hour", "HR", "HRS"),
    
    //Force
    NEWTON(1.0, 0, UnitType.Force, "N"),
    /** pounds of force (1 LBF = 4.448222 Newton) */
    LBFORCE(4.448222, 0, UnitType.Force, "LBF"),
    
    //temp
    /** Kelvin (base unit) */
    KELVIN(1.0, 0, UnitType.Temp, "K", "Kelvin"),
    
    /** centigrade (C = K - 273.15) */
    CENTIGRADE(1.0, -273.15, UnitType.Temp, "C"),
    
    /** Fahrenheit ( F = 5/9K,  F = 9/5K - 459.67 ) */
    FAHRENHEIT(5.0/9.0, -459.67, UnitType.Temp, "F"),
    
    /** Rankine ( R = 5/9 K ) */
    RANKINE(5.0/9.0, 0, UnitType.Temp, "R"),
    
    
    // power
    /** Base Unit (Kg*m^2/s^3) **/
    WATT(1.0, 0, UnitType.Power, "W"),
    HP(745.699872, 0, UnitType.Power, "HP");
    
    
    
    
    public static final double PI = Math.PI;
    public static final double TWO_PI = Math.PI * 2.;
    public static final double HALF_PI = PI / 2.;


    /**
     * Defines different types of units.  For example: length, velocity, mass, etc.
     */
    public enum UnitType {

        Length,
        Velocity,
        Mass,
        Area,
        Volume,
        Density,
        Pressure,
        Force,
        Temp,
        Time,
        Angle,
        Power,
        None
    }
    /** Creates a new instance of Unit */
    protected UnitType type;
    protected double factor, offset;
    protected String[] labels;

    private Unit(double factor, double offset, UnitType type, String... labels) {
        this.factor = factor;
        this.offset = offset;
        this.type = type;
        this.labels = labels;
    }

    /** Converts the passed value from this Unit to the conversion Unit.  If the units are of incompatible
     *  type (i.e linear unit converting to an area) then zero is returned.
     *
     *  @param  value       Value to be converted.
     *  @param  convertTo   Units to convert to.
     *
     *  @return             The value, converted into the appropriate units or zero in the event of a <code>UnitType</code> mismatch.
     */
    public double convertTo(double value, Unit convertTo) throws NumberFormatException{
        if (convertTo.type == type) {
            return (value - offset) * factor / convertTo.factor + convertTo.offset;
        }
        throw new NumberFormatException("Unit: Invalid Unit Comparison");
    }
    
    
    /** Converts the value from the given unit to the "base" unit as defined in the enumeration.  Base units are typically
     *  the standard international unit [Meter, Kilogram, Second, Kelvin], as well as Radian for angles.
     *  For combination units, such as area or force, it converts to the combination of standard units.
     * 
     * @param value value to convert
     * @return the value in the "base" unit.
     */
    public double convertToBase(double value) {
        return (value - offset) * factor;
    }

    /** Converts the passed value to this Unit from the conversion Unit.  If the units are of incompatible
     *  type (i.e linear unit converting to an area) then zero is returned.
     *
     *  @param  value       Value to be converted.
     *  @param  convertFrom   Units to convert from.
     *
     *  @return             The value, converted into the appropriate units or zero in the event of a <code>UnitType</code> mismatch.
     */
    public double convertFrom(double value, Unit convertFrom) {
        if (convertFrom.type == type) {
            return (value - convertFrom.offset) * convertFrom.factor / factor + offset;
        }
        throw new NumberFormatException("Unit: Invalid Unit Comparison");
    }

    /**
     * Returns the label assigned to this unit.  If there is more than one label, then this returns the first one.
     *
     * @return      The <code>Unit</code> label.
     */
    public String toString() {
        return labels[0];
    }

    /** returns the type of unit (e.g. length, temp, etc) */
    public UnitType getUnitType() {
        return type;
    }
    
    /** Returns a unit that matches the label, either by unit name or the 
     * "label" value that is stored with the unit.
     */
    public static Unit getByLabel(String label) {
        for (Unit u : Unit.values()) {
            if (u.name().equalsIgnoreCase(label)) {
                return u;
            }
            for (String str : u.labels) {
                if (str.equalsIgnoreCase(label)) {
                    return u;
                }
            }
        }
        return null;
    }

    /**
     * Returns a list of all units in this class that have the corresponding type
     * @param type type of unit (length, area, temp, etc)
     * @return a list of units of the requested type
     */
    public static List<Unit> getAllUnits(UnitType type) {
        ArrayList<Unit> unitList = new ArrayList<Unit>();
        for (Unit u : Unit.values()) {
            if (u.getUnitType() == type) {
                unitList.add(u);
            }
        }
        return unitList;
    }
    

    /** clamps an angle to [-PI..PI] 
     * 
     *  @param input    Angle to be bounded
     *  @return         Bounded angle value.
     */
    public static double boundPi(double input) {
        return input - Math.floor((input + PI) / TWO_PI) * TWO_PI;
    }

    /** clamps an angle to [0..2PI]
     *  @param input    Angle to be bounded
     *  @return         Bounded angle value.
     */
    public static double bound2Pi(double input) {
        return input - Math.floor(input / TWO_PI) * TWO_PI;
    }

    /** clamps an angle to [-180..180]
     *  @param input    Angle to be bounded
     *  @return         Bounded angle value.
     */
    public static double bound180(double input) {
        return input - Math.floor((input + 180.) / 360.) * 360.;
    }

    /** clamps an angle to [0..360]
     *  @param input    Angle to be bounded
     *  @return         Bounded angle value.
     */
    public static double bound360(double input) {
        return input - Math.floor(input / 360.) * 360.;
    }
    
    /** Use this to match decimals */
    public static String DECIMAL_MATCHER = "[-+]?[0-9]*\\.?[0-9]*([eE][-+]?[0-9]*)?";

    public static void main(String[] args) {
        System.out.println(bound2Pi(3.5*PI));
    }


}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */