// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package avtas.lmcp;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.io.EOFException;


/** A Factory class that reads LMCP objects, creates new LMCP objects, and
    wraps LMCP objects to create messages.
*/
public class LMCPFactory {

    /** internal map of all series enums (for object creation).  Uses the "long" version of SeriesName */
    protected java.util.HashMap<Long, LMCPEnum> seriesEnums = new java.util.HashMap<Long, LMCPEnum>();

    /** bytes size in bytes */
    public static final int HEADER_SIZE = 8;

    /** "LMCP" control string expressed as an integer **/
    public static final int LMCP_CONTROL_STR = 0x4c4d4350;
    
    /** checksum size in bytes */
    public static final int CHECKSUM_SIZE = 4;

    /** Default instance of LMCPFactory */
    protected static final LMCPFactory DEFAULT_FACTORY = new LMCPFactory();

    static {
        { LMCPEnum e = new larcfm.DAIDALUS.SeriesEnum();
        addSeries(e); } 
        { LMCPEnum e = new uxas.messages.task.SeriesEnum();
        addSeries(e); } 
        { LMCPEnum e = new afrl.cmasi.SeriesEnum();
        addSeries(e); } 
        { LMCPEnum e = new afrl.vehicles.SeriesEnum();
        addSeries(e); } 
        { LMCPEnum e = new uxas.messages.SeriesEnum();
        addSeries(e); } 
        { LMCPEnum e = new afrl.impact.SeriesEnum();
        addSeries(e); } 
        { LMCPEnum e = new uxas.messages.route.SeriesEnum();
        addSeries(e); } 
        { LMCPEnum e = new uxas.messages.uxnative.SeriesEnum();
        addSeries(e); } 
        { LMCPEnum e = new afrl.cmasi.perceive.SeriesEnum();
        addSeries(e); } 

    }

    /** Returns an LMCP message that is created by reading an array of bytes from some input
     *  source.  The message header items are read, the root object is created, and the
     *  checksum is validated.  
     *  @return an LMCPObject or null if the root object type is not defined.
     */
    public static LMCPObject getObject(byte[] bytes) throws Exception {

        if (bytes == null || bytes.length < HEADER_SIZE) {
            throw new Exception("LMCP Factory Exception: Null buffer or not enough bytes in buffer");
        }
        if (!validate(bytes)) {
            throw new Exception("LMCP Factory Exception: Checksum does not match");
        }

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);

        if ( LMCPUtil.getInt32(in) != LMCP_CONTROL_STR) {
            throw new Exception("LMCP Factory Exception: This does not appear to be a proper LMCP message.");
        }
        if ( LMCPUtil.getInt32(in) > bytes.length - HEADER_SIZE - CHECKSUM_SIZE) {
            throw new Exception("LMCP Factory Exception: not enough bytes in buffer to create object.");
        }

        return LMCPUtil.getObject(in);
    }

    /** used to add a series to the factory */
    public static void addSeries(LMCPEnum seriesEnum) {
        long seriesId = seriesEnum.getSeriesNameAsLong();
        if (!DEFAULT_FACTORY.seriesEnums.containsKey(seriesId)) {
            DEFAULT_FACTORY.seriesEnums.put(seriesId, seriesEnum);
        }
    }

    /** Convenience method for getting a view of the Series enums that are loaded into the factory. */
    public static java.util.Collection<LMCPEnum> getLoadedSeries() {
        return DEFAULT_FACTORY.seriesEnums.values();
    }

    /** creates a new instance of an object with the given type id and series id.
        returns null if there is no corresponding object.
    */
    public static LMCPObject createObject(long series_id, long object_type, int version) {
        LMCPEnum e = DEFAULT_FACTORY.seriesEnums.get(series_id);
        if (e != null) {
            if (e.getSeriesVersion() == version) {
                return e.getInstance(object_type);
            }
        }
        return null;
    }


    /** returns the name of the struct specified by the given type and series name */
    public static String getName(long object_type, String series_name) {
        for (LMCPEnum e : DEFAULT_FACTORY.seriesEnums.values() ) {
            if (e.getSeriesName().equals(series_name)) {
                 return e.getName(object_type);
            }
        }
        return null;
    }

    /** returns a type id for the given struct name and series name */
    public static long getType(String name, String series_name) {
        for (LMCPEnum e : DEFAULT_FACTORY.seriesEnums.values() ) {
            if (e.getSeriesName().equals(series_name)) {
                 return e.getType(name);
            }
        }
        return -1;
    }

    public static byte[] packMessage(LMCPObject rootObject, boolean calculateChecksum) throws Exception{

        if (rootObject == null) return null;

        int size = rootObject.calcSize();
        LMCPOutputStream buf = new LMCPOutputStream( size + HEADER_SIZE + CHECKSUM_SIZE );

        LMCPUtil.putUint32(buf, LMCP_CONTROL_STR);
        LMCPUtil.putUint32(buf, size);
        LMCPUtil.putObject(buf, rootObject);

        long cs = calculateChecksum ? calculateChecksum(buf.getInternalBuffer()) : 0;
        LMCPUtil.putUint32(buf, cs);

        if(!calculateChecksum)
            return buf.toByteArray();
        
        // every time a checksum is requested, it is going out over the network
        // so update with sentinelization
        String attributes = rootObject.getFullLMCPTypeName();
        attributes += "$lmcp|" + rootObject.getFullLMCPTypeName();
        attributes += "||0|0$";
        String sentinel = "+=+=+=+=";
        sentinel += String.valueOf(attributes.length() + size + HEADER_SIZE + CHECKSUM_SIZE);
        sentinel += "#@#@#@#@";
        
        byte[] attributeBytes = attributes.getBytes();
        byte[] payloadBytes = buf.toByteArray();
        byte[] addressedPayload = new byte[attributeBytes.length + payloadBytes.length];
        System.arraycopy(attributeBytes, 0, addressedPayload, 0, attributeBytes.length);
        System.arraycopy(payloadBytes, 0, addressedPayload, attributeBytes.length, payloadBytes.length);
        
        // sentinelized checksum
        int val = 0;
        for(int i=0; i<addressedPayload.length; i++) {
            val += (addressedPayload[i] & 0xFF);
        }
        
        byte[] sentinelBytes = sentinel.getBytes();
        byte[] frontBytes = new byte[sentinelBytes.length + addressedPayload.length];
        System.arraycopy(sentinelBytes, 0, frontBytes, 0, sentinelBytes.length);
        System.arraycopy(addressedPayload, 0, frontBytes, sentinelBytes.length, addressedPayload.length);
        
        // add footer bytes
        String footer = "!%!%!%!%" + String.valueOf(val) + "?^?^?^?^";
        byte[] footerBytes = footer.getBytes();
        byte[] completeMessage = new byte[frontBytes.length + footerBytes.length];
        System.arraycopy(frontBytes, 0, completeMessage, 0, frontBytes.length);
        System.arraycopy(footerBytes, 0, completeMessage, frontBytes.length, footerBytes.length);
        
        return completeMessage;
    }

    public static void packMessage(OutputStream out, LMCPObject rootObject, 
        boolean calculateChecksum) throws Exception{

        out.write(packMessage(rootObject, calculateChecksum));
    }


    /** returns the size of a message that is represented by the given byte array. */
    public static long getSize(byte[] bytes) {
        long size = 0;
        size |= (bytes[4] & 0xFF);
        size <<= 8;
        size |= (bytes[5] & 0xFF);
        size <<= 8;
        size |= (bytes[6] & 0xFF);
        size <<= 8;
        size |= (bytes[7] & 0xFF);
        return size;
    }


    /** returns a message read from an java.io.InputStream object.  
     */
    public static LMCPObject getObject(InputStream is) throws Exception{
        byte[] bytes = getMessageBytes(is);
        return getObject(bytes);
    }
    
    /** returns an array of bytes corresponding to the first message encountered in 
     *  the input stream. The method blocks until all of the bytes are read. 
     */
    public static byte[] getMessageBytes(InputStream is) throws Exception {
        /** New UxAS scheme for TCP streams
         * ----------[start]--------------
         */
        // read bytes off the input stream until sentinel characters are seen
        int nextbyte = 0;
        while('#' != (char) nextbyte)
        {
            nextbyte = is.read();
            if(nextbyte < 0)
                throw new EOFException();
        }
        
        // the next 7 characters should match what we want
        byte [] sentinelbytes = new byte[7];
        int numsentinel = is.read(sentinelbytes);
        if(numsentinel < 0)
            throw new EOFException();
        boolean isValid = (numsentinel == 7);
        for(int i=0; i<7 && isValid; i++)
        {
            char testchar = '@';
            if(i%2 == 1)
                testchar = '#';
            if(sentinelbytes[i] != testchar)
                isValid = false;
        }
        
        // didn't match the sentinel sequence, so try again from the top
        if(!isValid)
        {
            return getMessageBytes(is);
        }
        
        // now read characters off until the *second* '$' symbol
        nextbyte = 0;
        while('$' != (char) nextbyte)
        {
            nextbyte = is.read();
            if(nextbyte < 0)
                throw new EOFException();
        }
        nextbyte = 0;
        while('$' != (char) nextbyte)
        {
            nextbyte = is.read();
            if(nextbyte < 0)
                throw new EOFException();
        }
        
        /** New UxAS scheme for TCP streams
         * ----------[ end ]--------------
         */
  
        byte[] bytes = new byte[HEADER_SIZE];
        int i = 0;
        while(i <bytes.length) {
            int tmp = is.read(bytes, i, bytes.length-i);
            if (tmp < 0)
                throw new EOFException();
            i += tmp;
        }
        
        // retrieves the "size" value in BIG_ENDIAN order
        int size = (int) getSize(bytes);
                
        byte[] buf = new byte[ size + HEADER_SIZE + CHECKSUM_SIZE];
        System.arraycopy(bytes, 0, buf, 0, bytes.length);
                
        i = bytes.length;
        while(i <buf.length) {
            int tmp = is.read(buf, i, buf.length-i);
            if (tmp < 0)
                throw new EOFException();
            i += tmp;
        }
                
        return buf;
    }


    /** Calculates the checksum.  This should be called after pack().
     *  The checksum sums all bytes in the packet between 0 and 
     *  length - CHECKSUM_SIZE.
     */
    public static long calculateChecksum(byte[] bytes) {
        long val = 0;
        for(int i=0; i<bytes.length - CHECKSUM_SIZE; i++) {
            val += (bytes[i] & 0xFF);
        }
        return val & 0x00000000ffffffffL;
    }

    /** checks the bytebuffer's checksum value against the calculated checksum 
     *  returns true if the calculated and stored values match, or if the buffer value is
     *  zero (indicating that checksum was not calculated.  This method rewinds the buffer and 
     *  returns it to LIMIT - 4 bytes (start position of checksum)
     */
    public static boolean validate(byte[] bytes) {
        // retrieves the checksum value in BIG_ENDIAN order
        long cs = 0;
        int len = bytes.length;
        cs |= (bytes[len-4] & 0xFF); 
        cs <<= 8;
        cs |= (bytes[len-3] & 0xFF);
        cs <<= 8;
        cs |= (bytes[len-2] & 0xFF);
        cs <<= 8;
        cs |= (bytes[len-1] & 0xFF);

        return (cs == 0) || (calculateChecksum(bytes) == cs);
    }

    /** Representation of a byte array output stream that provides access to the internal byte
     *  buffer.
     */
    static class LMCPOutputStream extends ByteArrayOutputStream {

        public LMCPOutputStream(int size) {
            super(size);
        }

        public byte[] getInternalBuffer() {
            return super.buf;
        }

    }


}
   
