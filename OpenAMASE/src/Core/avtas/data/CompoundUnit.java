// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.data;

import avtas.util.ObjectUtils;
import avtas.data.Unit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A set of methods that allows the construction of complex units (i.e. units that are
 * made up of a mixture of units from the {@link Unit} enumeration.)  Compound units
 * are stated as strings, using the "*" "/", and "^" symbols to perform construction.
 * 
 * Example:<br/>
 * To represent the SI unit of force, Newton:
 * <pre>
 * <u>KgM</u>
 * S<sup>2</sup>
 * </pre> 
 * 
 * use the string: <code>KG*M/SEC^2</code> or <code>KG*M/SEC/SEC</code><br/>
 * 
 * This class recognizes 1/unit style relationships (i.e. <code>1/SEC</code>) as well
 * as negative exponents (i.e. <code>SEC^-1</code>).  Exponents must be non-zero  
 * integer numbers.
 * 
 * @author AFRL/RQQD
 */
public class CompoundUnit {

    enum Operator {

        Multiply, Divide, Power;

        public static Operator bySymbol(String symbol) {
            if (symbol.equals("*")) {
                return Multiply;
            }
            else if (symbol.equals("/")) {
                return Divide;
            }
            else if (symbol.equals("^")) {
                return Power;
            }
            throw new EnumConstantNotPresentException(Operator.class, symbol);
        }
    }
    List<Unit> units = new ArrayList<Unit>();
    List<Operator> ops = new ArrayList<Operator>();

    /**
     * Create a new unit from a string input.
     * @param unitStr 
     */
    public CompoundUnit(String unitStr) {
        parseString(unitStr, units, ops);
    }

    /** Convert a value from this unit to another unit.  The new unit requested must be compatible in terms
     * of all units that make up the compound unit.
     * 
     * @param toUnit  a {@link CompoundUnit} to convert to.
     * @param value the value (in this unit's terms) to convert
     * @return the converted value
     */
    public double convert(CompoundUnit toUnit, double value) {

        if (!ops.equals(toUnit.ops)) {
            throw new NumberFormatException("CompoundUnit: Unit Operations do not match");
        }
        if (units.size() != toUnit.units.size()) {
            throw new NumberFormatException("CompoundUnit: Incompatible unit type");
        }
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).getUnitType() != toUnit.units.get(i).getUnitType()) {
                throw new NumberFormatException("CompoundUnit: Unit type mismatch ");
            }
        }

        double cum_value = 1;
        Unit oldUnit, newUnit;

        for (int i = 0; i < ops.size(); i++) {
            oldUnit = units.get(i);
            newUnit = toUnit.units.get(i);

            double tmp_val = oldUnit.convertTo(1.0, newUnit);
            if (ops.get(i) == Operator.Multiply) {
                cum_value *= tmp_val;
            }
            else if (ops.get(i) == Operator.Divide) {
                cum_value /= tmp_val;
            }

        }

        return cum_value * value;
    }

    /** Converts the value from the given unit to the "base" unit as defined in the enumeration.  Base units are typically
     *  the standard international unit [Meter, Kilogram, Second, Kelvin], as well as Radian for angles.
     *  This method breaks down each component unit and converts to the base unit as defined in {@link Unit}.
     * 
     * @param value value to convert
     * @return the value in the "base" unit.
     */
    public double convertToBase(double value) {

        double cum_value = 1;

        for (int i = 0; i < ops.size(); i++) {

            double tmp_val = units.get(i).convertToBase(1.0);
            if (ops.get(i) == Operator.Multiply) {
                cum_value *= tmp_val;
            }
            else if (ops.get(i) == Operator.Divide) {
                cum_value /= tmp_val;
            }

        }

        return cum_value * value;
    }

    /**
     * Convenience method for converting between units.
     */
    public static double convert(CompoundUnit fromUnit, CompoundUnit toUnit, double value) throws NumberFormatException {
        return fromUnit.convert(toUnit, value);
    }

    /**
     * Converts between units.  This first creates units then calls {@link #convert(CompoundUnit, double) } on the "from"
     * unit.
     */
    public static double convert(String fromStr, String toStr, double value) throws NumberFormatException {
        CompoundUnit fromUnit = new CompoundUnit(fromStr);
        CompoundUnit toUnit = new CompoundUnit(toStr);
        return fromUnit.convert(toUnit, value);
    }

    /**
     * Creates a new CompoundUnit based on a string input.  This expands power "^" relationships
     * to series of "/" and "*" and stores units and operators for later use.
     */
    static void parseString(String str, List<Unit> units, List<Operator> ops) {

        // trim away any whitepsace
        str.replaceAll("\\s+", "");

        List<String> unitStrs = ObjectUtils.asList(str.split("[*/\\^]"));
        List<String> opsStrs = ObjectUtils.asList(str.split("\\w+|(\\-?\\d+)"));
        // leading op is "" due to the splitting method.  Remove it
        if (!opsStrs.isEmpty()) {
            opsStrs.remove(0);
        }



        // if there is a power symbol, then expand the operations depending on the
        // power value (must be an integer)
        for (int i = 0; i < opsStrs.size(); i++) {
            if (opsStrs.get(i).equals("^")) {
                opsStrs.remove(i);
                int pow = Integer.parseInt(unitStrs.remove(i + 1));
                String unit = unitStrs.get(i);
                String op = i == 0 ? "*" : opsStrs.get(i - 1);
                if (pow < 0) {
                    op = op.equals("*") ? "/" : "*";
                    if (i != 0) {
                        opsStrs.set(i - 1, op);
                    }
                    else {
                        opsStrs.add(op);
                        unitStrs.add(0, "1");
                    }
                    pow = -pow;
                }
                for (int j = 1; j < pow; j++) {
                    unitStrs.add(i + 1, unit);
                    opsStrs.add(i, op);
                }
                i += pow;
            }
        }

        // if this is not a 1/term type of unit, then add a multiply sign to the
        // front of the operations list (to enable proper building in the convert
        // method)
        if (!unitStrs.get(0).matches("1")) {
            ops.add(Operator.Multiply);
        }
        else {
            unitStrs.remove(0);
        }

        for (String op : opsStrs) {
            ops.add(Operator.bySymbol(op));
        }

        for (String u : unitStrs) {
            units.add(Unit.getByLabel(u));
        }

    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */