// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 16, 2005
 */
package org.flexdock.util;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author Christopher Butler
 */
@SuppressWarnings(value = { "serial" })
public class TypedHashtable extends Hashtable {

    /**
     * Constructs a new, empty <code>TypedHashtable</code> with a default initial capacity (11)
     * and load factor of <code>0.75</code>.
     */
    public TypedHashtable() {
        super();
    }

    /**
     * Constructs a new, empty <code>TypedHashtable</code> with the specified initial capacity
     * and default load factor of <code>0.75</code>.
     *
     * @param     initialCapacity   the initial capacity of the hashtable.
     * @exception IllegalArgumentException if the initial capacity is less
     *              than zero.
     */
    public TypedHashtable(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs a new, empty <code>TypedHashtable</code> with the specified initial
     * capacity and the specified load factor.
     *
     * @param      initialCapacity   the initial capacity of the hashtable.
     * @param      loadFactor        the load factor of the hashtable.
     * @exception  IllegalArgumentException  if the initial capacity is less
     *             than zero, or if the load factor is nonpositive.
     */
    public TypedHashtable(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * Constructs a new <code>TypedHashtable</code> with the same mappings as the given
     * Map.  The hashtable is created with an initial capacity sufficient to
     * hold the mappings in the given Map and a default load factor of <code>0.75</code>.
     *
     * @param t the map whose mappings are to be placed in this map.
     * @throws NullPointerException if the specified map is null.
     */
    public TypedHashtable(Map t) {
        super(t);
    }

    /**
     * Maps the specified <code>key</code> to the specified boolean <code>value</code> in this
     * hashtable.  Since hashtables require <code>Object</code> values, this method will put either
     * <code>Boolean.TRUE</code> or <code>Boolean.FALSE</code> in the hashtable.  If <code>key</code> is
     * <code>null</code> then this method returns with no action taken.
     *
     * @param key the hashtable key.
     * @param value the boolean value to be mapped to the specified <code>key</code>.
     * @see Boolean#TRUE
     * @see Boolean#FALSE
     * @see #put(Object, Object)
     */
    public void put(Object key, boolean value) {
        Boolean b = value? Boolean.TRUE: Boolean.FALSE;
        put(key, b);
    }

    /**
     * Maps the specified <code>key</code> to the specified byte <code>value</code> in this
     * hashtable.  Since hashtables require <code>Object</code> values, this method will wrap the
     * specified byte in a <code>java.lang.Byte</code> before inserting.  If <code>key</code> is
     * <code>null</code> then this method returns with no action taken.
     *
     * @param key the hashtable key.
     * @param value the byte value to be mapped to the specified <code>key</code>.
     * @see Byte#Byte(byte)
     * @see #put(Object, Object)
     */
    public void put(Object key, byte value) {
        put(key, new Byte(value));
    }

    /**
     * Maps the specified <code>key</code> to the specified short <code>value</code> in this
     * hashtable.  Since hashtables require <code>Object</code> values, this method will wrap the
     * specified short in a <code>java.lang.Short</code> before inserting.  If <code>key</code> is
     * <code>null</code> then this method returns with no action taken.
     *
     * @param key the hashtable key.
     * @param value the short value to be mapped to the specified <code>key</code>.
     * @see Short#Short(short)
     * @see #put(Object, Object)
     */
    public void put(Object key, short value) {
        put(key, new Short(value));
    }

    /**
     * Maps the specified <code>key</code> to the specified int <code>value</code> in this
     * hashtable.  Since hashtables require <code>Object</code> values, this method will wrap the
     * specified int in a <code>java.lang.Integer</code> before inserting.  If <code>key</code> is
     * <code>null</code> then this method returns with no action taken.
     *
     * @param key the hashtable key.
     * @param value the int value to be mapped to the specified <code>key</code>.
     * @see Integer#Integer(int)
     * @see #put(Object, Object)
     */
    public void put(Object key, int value) {
        put(key, new Integer(value));
    }

    /**
     * Maps the specified <code>key</code> to the specified long <code>value</code> in this
     * hashtable.  Since hashtables require <code>Object</code> values, this method will wrap the
     * specified long in a <code>java.lang.Long</code> before inserting.  If <code>key</code> is
     * <code>null</code> then this method returns with no action taken.
     *
     * @param key the hashtable key.
     * @param value the long value to be mapped to the specified <code>key</code>.
     * @see Long#Long(long)
     * @see #put(Object, Object)
     */
    public void put(Object key, long value) {
        put(key, new Long(value));
    }

    /**
     * Maps the specified <code>key</code> to the specified float <code>value</code> in this
     * hashtable.  Since hashtables require <code>Object</code> values, this method will wrap the
     * specified float in a <code>java.lang.Float</code> before inserting.  If <code>key</code> is
     * <code>null</code> then this method returns with no action taken.
     *
     * @param key the hashtable key.
     * @param value the float value to be mapped to the specified <code>key</code>.
     * @see Float#Float(float)
     * @see #put(Object, Object)
     */
    public void put(Object key, float value) {
        put(key, new Float(value));
    }

    /**
     * Maps the specified <code>key</code> to the specified double <code>value</code> in this
     * hashtable.  Since hashtables require <code>Object</code> values, this method will wrap the
     * specified double in a <code>java.lang.Double</code> before inserting.  If <code>key</code> is
     * <code>null</code> then this method returns with no action taken.
     *
     * @param key the hashtable key.
     * @param value the double value to be mapped to the specified <code>key</code>.
     * @see Double#Double(double)
     * @see #put(Object, Object)
     */
    public void put(Object key, double value) {
        put(key, new Double(value));
    }

    /**
     * Maps the specified <code>key</code> to the specified char <code>value</code> in this
     * hashtable.  Since hashtables require <code>Object</code> values, this method will wrap the
     * specified char in a <code>java.lang.Character</code> before inserting.  If <code>key</code> is
     * <code>null</code> then this method returns with no action taken.
     *
     * @param key the hashtable key.
     * @param value the char value to be mapped to the specified <code>key</code>.
     * @see Character#Character(char)
     * @see #put(Object, Object)
     */
    public void put(Object key, char value) {
        put(key, new Character(value));
    }

    /**
     * Maps the specified <code>key</code> to the specified
     * <code>value</code> in this hashtable. If the <code>key</code> is <code>null</code>, then this
     * method returns with no action taken.  If the <code>value</code> is <code>null</code>, then
     * this method removes any existing mapping in the hashtable for the specified <code>key</code>
     * by calling <code>remove(Object key)</code>.
     *
     * The value can be retrieved by calling the <code>get(Object key)</code> method
     * with a key that is equal to the original key.
     *
     * @param key the hashtable key.
     * @param value the value.
     * @see Object#equals(Object)
     * @see #get(Object)
     * @see Hashtable#remove(java.lang.Object)
     */
    public Object put(Object key, Object value) {
        if(value==null)
            return super.remove(key);
        else
            return super.put(key, value);
    }





    /**
     * Returns the boolean value associated with the specified <code>key</code> in this hashtable.
     * This method attempts to cast the value in this hashtable for the specified <code>key</code> to
     * a <code>java.lang.Boolean</code> and invoke its <code>booleanValue()</code> method.
     * If the key does not exist in the hashtable, or it maps to a non-<code>Boolean</code> value,
     * then this method returns the specified <code>defaultValue</code>.
     *
     * @param key the hashtable key
     * @param defaultValue the value to return if a valid boolean cannot be found for the specified key
     * @return the boolean value associated with the specified <code>key</code> in this hashtable.
     * @see #put(Object, boolean)
     * @see #getBoolean(Object)
     * @see Boolean#booleanValue()
     */
    public boolean get(Object key, boolean defaultValue) {
        Object obj = get(key);
        return obj instanceof Boolean? defaultValue: ((Boolean)obj).booleanValue();
    }

    /**
     * Returns the byte value associated with the specified <code>key</code> in this hashtable.
     * This method attempts to cast the value in this hashtable for the specified <code>key</code> to
     * a <code>java.lang.Byte</code> and invoke its <code>byteValue()</code> method.
     * If the key does not exist in the hashtable, or it maps to a non-<code>Byte</code> value,
     * then this method returns the specified <code>defaultValue</code>.
     *
     * @param key the hashtable key
     * @param defaultValue the value to return if a valid byte cannot be found for the specified key
     * @return the byte value associated with the specified <code>key</code> in this hashtable.
     * @see #put(Object, byte)
     * @see #getByte(Object)
     * @see Byte#byteValue()
     */
    public byte get(Object key, byte defaultValue) {
        Object obj = get(key);
        return obj instanceof Byte? defaultValue: ((Byte)obj).byteValue();
    }

    /**
     * Returns the short value associated with the specified <code>key</code> in this hashtable.
     * This method attempts to cast the value in this hashtable for the specified <code>key</code> to
     * a <code>java.lang.Short</code> and invoke its <code>shortValue()</code> method.
     * If the key does not exist in the hashtable, or it maps to a non-<code>Short</code> value,
     * then this method returns the specified <code>defaultValue</code>.
     *
     * @param key the hashtable key
     * @param defaultValue the value to return if a valid short cannot be found for the specified key
     * @return the short value associated with the specified <code>key</code> in this hashtable.
     * @see #put(Object, short)
     * @see #getShort(Object)
     * @see Short#shortValue()
     */
    public short get(Object key, short defaultValue) {
        Object obj = get(key);
        return obj instanceof Short? defaultValue: ((Short)obj).shortValue();
    }

    /**
     * Returns the int value associated with the specified <code>key</code> in this hashtable.
     * This method attempts to cast the value in this hashtable for the specified <code>key</code> to
     * a <code>java.lang.Integer</code> and invoke its <code>intValue()</code> method.
     * If the key does not exist in the hashtable, or it maps to a non-<code>Integer</code> value,
     * then this method returns the specified <code>defaultValue</code>.
     *
     * @param key the hashtable key
     * @param defaultValue the value to return if a valid int cannot be found for the specified key
     * @return the int value associated with the specified <code>key</code> in this hashtable.
     * @see #put(Object, int)
     * @see #getInt(Object)
     * @see Integer#intValue()
     */
    public int get(Object key, int defaultValue) {
        Object obj = get(key);
        return obj instanceof Integer? defaultValue: ((Integer)obj).intValue();
    }

    /**
     * Returns the long value associated with the specified <code>key</code> in this hashtable.
     * This method attempts to cast the value in this hashtable for the specified <code>key</code> to
     * a <code>java.lang.Long</code> and invoke its <code>longValue()</code> method.
     * If the key does not exist in the hashtable, or it maps to a non-<code>Long</code> value,
     * then this method returns the specified <code>defaultValue</code>.
     *
     * @param key the hashtable key
     * @param defaultValue the value to return if a valid long cannot be found for the specified key
     * @return the long value associated with the specified <code>key</code> in this hashtable.
     * @see #put(Object, long)
     * @see #getLong(Object)
     * @see Long#longValue()
     */
    public long get(Object key, long defaultValue) {
        Object obj = get(key);
        return obj instanceof Long? defaultValue: ((Long)obj).longValue();
    }

    /**
     * Returns the float value associated with the specified <code>key</code> in this hashtable.
     * This method attempts to cast the value in this hashtable for the specified <code>key</code> to
     * a <code>java.lang.Float</code> and invoke its <code>floatValue()</code> method.
     * If the key does not exist in the hashtable, or it maps to a non-<code>Float</code> value,
     * then this method returns the specified <code>defaultValue</code>.
     *
     * @param key the hashtable key
     * @param defaultValue the value to return if a valid float cannot be found for the specified key
     * @return the float value associated with the specified <code>key</code> in this hashtable.
     * @see #put(Object, float)
     * @see #getFloat(Object)
     * @see Float#floatValue()
     */
    public float get(Object key, float defaultValue) {
        Object obj = get(key);
        return obj instanceof Float? defaultValue: ((Float)obj).floatValue();
    }

    /**
     * Returns the double value associated with the specified <code>key</code> in this hashtable.
     * This method attempts to cast the value in this hashtable for the specified <code>key</code> to
     * a <code>java.lang.Double</code> and invoke its <code>doubleValue()</code> method.
     * If the key does not exist in the hashtable, or it maps to a non-<code>Double</code> value,
     * then this method returns the specified <code>defaultValue</code>.
     *
     * @param key the hashtable key
     * @param defaultValue the value to return if a valid double cannot be found for the specified key
     * @return the double value associated with the specified <code>key</code> in this hashtable.
     * @see #put(Object, double)
     * @see #getDouble(Object)
     * @see Double#doubleValue()
     */
    public double get(Object key, double defaultValue) {
        Object obj = get(key);
        return obj instanceof Double? defaultValue: ((Double)obj).doubleValue();
    }

    /**
     * Returns the char value associated with the specified <code>key</code> in this hashtable.
     * This method attempts to cast the value in this hashtable for the specified <code>key</code> to
     * a <code>java.lang.Character</code> and invoke its <code>charValue()</code> method.
     * If the key does not exist in the hashtable, or it maps to a non-<code>Character</code> value,
     * then this method returns the specified <code>defaultValue</code>.
     *
     * @param key the hashtable key
     * @param defaultValue the value to return if a valid char cannot be found for the specified key
     * @return the char value associated with the specified <code>key</code> in this hashtable.
     * @see #put(Object, char)
     * @see #getChar(Object)
     * @see Character#charValue()
     */
    public char get(Object key, char defaultValue) {
        Object obj = get(key);
        return obj instanceof Character? defaultValue: ((Character)obj).charValue();
    }


    /**
     * Retrieves the value to which the specified key is mapped in this hashtable and casts to a
     * <code>java.lang.String</code> before returning.  If the specified <code>key</code> maps to an
     * object type other than a <code>String</code> value, then this method throws a
     * <code>ClassCastException</code>.
     *
     * @param   key a key in the hashtable.
     * @return  the <code>String</code> value to which the key is mapped in this hashtable;
     * <code>null</code> if the key is not mapped to any value in this hashtable.
     * @throws  NullPointerException  if the key is <code>null</code>.
     * @throws ClassCastException if the returns value is not a <code>String</code>
     * @see #put(Object, Object)
     */
    public String getString(Object key) {
        return (String)get(key);
    }

    /**
     * Retrieves the value to which the specified key is mapped in this hashtable and casts to a
     * <code>java.lang.Boolean</code> before returning.  If the specified <code>key</code> maps to an
     * object type other than a <code>Boolean</code> value, then this method throws a
     * <code>ClassCastException</code>.
     *
     * @param   key a key in the hashtable.
     * @return  the <code>Boolean</code> value to which the key is mapped in this hashtable;
     * <code>null</code> if the key is not mapped to any value in this hashtable.
     * @throws  NullPointerException  if the key is <code>null</code>.
     * @throws ClassCastException if the returns value is not a <code>Boolean</code>
     * @see #put(Object, Object)
     */
    public Boolean getBoolean(Object key) {
        return (Boolean)get(key);
    }

    /**
     * Retrieves the value to which the specified key is mapped in this hashtable and casts to a
     * <code>java.lang.Byte</code> before returning.  If the specified <code>key</code> maps to an
     * object type other than a <code>Byte</code> value, then this method throws a
     * <code>ClassCastException</code>.
     *
     * @param   key a key in the hashtable.
     * @return  the <code>Byte</code> value to which the key is mapped in this hashtable;
     * <code>null</code> if the key is not mapped to any value in this hashtable.
     * @throws  NullPointerException  if the key is <code>null</code>.
     * @throws ClassCastException if the returns value is not a <code>Byte</code>
     * @see #put(Object, Object)
     */
    public Byte getByte(Object key) {
        return (Byte)get(key);
    }

    /**
     * Retrieves the value to which the specified key is mapped in this hashtable and casts to a
     * <code>java.lang.Short</code> before returning.  If the specified <code>key</code> maps to an
     * object type other than a <code>Short</code> value, then this method throws a
     * <code>ClassCastException</code>.
     *
     * @param   key a key in the hashtable.
     * @return  the <code>Short</code> value to which the key is mapped in this hashtable;
     * <code>null</code> if the key is not mapped to any value in this hashtable.
     * @throws  NullPointerException  if the key is <code>null</code>.
     * @throws ClassCastException if the returns value is not a <code>Short</code>
     * @see #put(Object, Object)
     */
    public Short getShort(Object key) {
        return (Short)get(key);
    }

    /**
     * Retrieves the value to which the specified key is mapped in this hashtable and casts to a
     * <code>java.lang.Integer</code> before returning.  If the specified <code>key</code> maps to an
     * object type other than a <code>Integer</code> value, then this method throws a
     * <code>ClassCastException</code>.
     *
     * @param   key a key in the hashtable.
     * @return  the <code>Integer</code> value to which the key is mapped in this hashtable;
     * <code>null</code> if the key is not mapped to any value in this hashtable.
     * @throws  NullPointerException  if the key is <code>null</code>.
     * @throws ClassCastException if the returns value is not a <code>Integer</code>
     * @see #put(Object, Object)
     */
    public Integer getInt(Object key) {
        return (Integer)get(key);
    }

    /**
     * Retrieves the value to which the specified key is mapped in this hashtable and casts to a
     * <code>java.lang.Long</code> before returning.  If the specified <code>key</code> maps to an
     * object type other than a <code>Long</code> value, then this method throws a
     * <code>ClassCastException</code>.
     *
     * @param   key a key in the hashtable.
     * @return  the <code>Long</code> value to which the key is mapped in this hashtable;
     * <code>null</code> if the key is not mapped to any value in this hashtable.
     * @throws  NullPointerException  if the key is <code>null</code>.
     * @throws ClassCastException if the returns value is not a <code>Long</code>
     * @see #put(Object, Object)
     */
    public Long getLong(Object key) {
        return (Long)get(key);
    }

    /**
     * Retrieves the value to which the specified key is mapped in this hashtable and casts to a
     * <code>java.lang.Float</code> before returning.  If the specified <code>key</code> maps to an
     * object type other than a <code>Float</code> value, then this method throws a
     * <code>ClassCastException</code>.
     *
     * @param   key a key in the hashtable.
     * @return  the <code>Float</code> value to which the key is mapped in this hashtable;
     * <code>null</code> if the key is not mapped to any value in this hashtable.
     * @throws  NullPointerException  if the key is <code>null</code>.
     * @throws ClassCastException if the returns value is not a <code>Float</code>
     * @see #put(Object, Object)
     */
    public Float getFloat(Object key) {
        return (Float)get(key);
    }

    /**
     * Retrieves the value to which the specified key is mapped in this hashtable and casts to a
     * <code>java.lang.Double</code> before returning.  If the specified <code>key</code> maps to an
     * object type other than a <code>Double</code> value, then this method throws a
     * <code>ClassCastException</code>.
     *
     * @param   key a key in the hashtable.
     * @return  the <code>Double</code> value to which the key is mapped in this hashtable;
     * <code>null</code> if the key is not mapped to any value in this hashtable.
     * @throws  NullPointerException  if the key is <code>null</code>.
     * @throws ClassCastException if the returns value is not a <code>Double</code>
     * @see #put(Object, Object)
     */
    public Double getDouble(Object key) {
        return (Double)get(key);
    }

    /**
     * Retrieves the value to which the specified key is mapped in this hashtable and casts to a
     * <code>java.lang.Character</code> before returning.  If the specified <code>key</code> maps to an
     * object type other than a <code>Character</code> value, then this method throws a
     * <code>ClassCastException</code>.
     *
     * @param   key a key in the hashtable.
     * @return  the <code>Character</code> value to which the key is mapped in this hashtable;
     * <code>null</code> if the key is not mapped to any value in this hashtable.
     * @throws  NullPointerException  if the key is <code>null</code>.
     * @throws ClassCastException if the returns value is not a <code>Character</code>
     * @see #put(Object, Object)
     */
    public Character getChar(Object key) {
        return (Character)get(key);
    }

}
