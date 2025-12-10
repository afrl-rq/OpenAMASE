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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class LMCPUtil {


    /** reads a string from the input stream using LMCP rules. */
    public static String getString(InputStream in) throws IOException {
        int len = getUint16(in);
        byte[] strBytes = new byte[len];
        in.read(strBytes);
        return new String( strBytes );
    }


    /** Writes a string to the output stream using LMCP rules */
    public static void putString(OutputStream out, String str) throws IOException {
        if (str == null) {
            putUint16(out, 0);
        }
        else {
            putUint16(out, str.length());
            out.write(str.getBytes());
        }
    }

    /** Writes an LMCP object to an output stream according to the LMCP rules. */
    public static void putObject(OutputStream out, LMCPObject o) throws IOException {
        if (o == null) {
            putBool(out, false);
        }
        else {
            putBool(out, true);
            putInt64(out, o.getLMCPSeriesNameAsLong());
            putUint32(out, o.getLMCPType());
            putUint16(out, o.getLMCPSeriesVersion());
            o.pack(out);
        }
    }

    /** Serializes an LMCP object to a byte array, then returns the array. */
    public static byte[] writeObjectToBytes(LMCPObject o) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        putObject(out, o);
        return out.toByteArray();
    }


    /** returns the next LMCP object found in the stream.
     *
     * @return a newly created and de-serialized LMCP object, or null if the stream
     * does not contain a properly formatted LMCP object.
     * @throws IOException
     */
    public static LMCPObject getObject(InputStream in) throws IOException {
        boolean exists = getBool(in);
        if (exists) {
            LMCPObject o = LMCPFactory.createObject( getInt64(in), getUint32(in), getUint16(in) );
            if (o != null) {
               o.unpack(in);
               return o;
            }
            else {
               return null;
            } 
        }
        else {
            return null;
        }
    }

    /** returns the next LMCP object found in the byte array
     *
     * @param bytes the byte array containing a serialized LMCP object
     * @return a newly created and de-serialized LMCP object, or null if the byte array
     * does not contain a properly formatted LMCP object.
     * @throws IOException
     */
    public static LMCPObject readObject(byte[] bytes) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        return getObject(in);
    }

    //////// Methods for getting primative types ////////////
    public static boolean getBool(ByteBuffer buf) {
        return buf.get() != 0;
    }

    public static boolean getBool(InputStream is) throws IOException {
        return is.read() != 0;
    }

    public static void putBool(ByteBuffer buf, boolean value) {
        buf.put((byte) (value == true ? 1 : 0));
    }

    public static void putBool(OutputStream os, boolean value) throws IOException{
        os.write( value == true ? 1 : 0 );
    }

    /** puts an unsigned 1 byte integer into a byte buffer */
    public static void putByte(java.nio.ByteBuffer buf, short value) {
        buf.put((byte) (value & 0xff));
    }

    /** puts an unsigned 1 byte integer into a byte buffer */
    public static void putByte(OutputStream os, short value) throws IOException {
        os.write( (value & 0xff) );
    }

    /** returns (as a short) an unsigned byte from a byte buffer */
    public static short getByte(java.nio.ByteBuffer buf) {
        return (short) (buf.get() & 0xff);
    }

    /** returns (as a short) an unsigned byte*/
    public static short getByte(InputStream is) throws IOException {
        return (short) (is.read());
    }

    public static char getChar(ByteBuffer buf) {
        return (char) buf.get();
    }

    public static char getChar(InputStream is) throws IOException {
        return (char) is.read();
    }

    public static void putChar(ByteBuffer buf, char value) {
        buf.put((byte) value);
    }

    public static void putChar(OutputStream os, char value) throws IOException{
        os.write( value );
    }

    public static float getReal32(ByteBuffer buf) {
        return buf.getFloat();
    }

    public static float getReal32(InputStream is) throws IOException {
        return Float.intBitsToFloat(getInt32(is));
    }

    public static void putReal32(ByteBuffer buf, float value) {
        buf.putFloat(value);
    }

    public static void putReal32(OutputStream os, float value) throws IOException{
        putInt32(os, Float.floatToIntBits(value));
    }

    public static double getReal64(ByteBuffer buf) {
        return buf.getDouble();
    }

    public static double getReal64(InputStream is) throws IOException{
        return Double.longBitsToDouble(getInt64(is));
    }

    public static void putReal64(ByteBuffer buf, double value) {
        buf.putDouble(value);
    }

    public static void putReal64(OutputStream os, double value) throws IOException{
        putInt64(os, Double.doubleToLongBits(value));
    }

    public static int getInt32(ByteBuffer buf) {
        return buf.getInt();
    }

    public static int getInt32(InputStream is) throws IOException {
        int ch1 = is.read();
        int ch2 = is.read();
        int ch3 = is.read();
        int ch4 = is.read();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    public static void putInt32(ByteBuffer buf, int value) {
        buf.putInt(value);
    }

    public static void putInt32(OutputStream os, int value) throws IOException {
        os.write((value >>> 24) & 0xFF);
        os.write((value >>> 16) & 0xFF);
        os.write((value >>> 8) & 0xFF);
        os.write((value >>> 0) & 0xFF);
    }

    public static long getInt64(ByteBuffer buf) {
        return buf.getLong();
    }

    public static long getInt64(InputStream is) throws IOException{
        int read = 0;
        byte[] readBuffer = new byte[8];
        while(read < 8) {
            read = is.read(readBuffer, read, 8 - read);
        }
        return (((long) readBuffer[0] << 56) +
                ((long) (readBuffer[1] & 255) << 48) +
                ((long) (readBuffer[2] & 255) << 40) +
                ((long) (readBuffer[3] & 255) << 32) +
                ((long) (readBuffer[4] & 255) << 24) +
                ((readBuffer[5] & 255) << 16) +
                ((readBuffer[6] & 255) << 8) +
                ((readBuffer[7] & 255) << 0));
    }

    public static void putInt64(ByteBuffer buf, long value) {
        buf.putLong(value);
    }

    public static void putInt64(OutputStream os, long v) throws IOException {
        byte[] writeBuffer = new byte[8];
        writeBuffer[0] = (byte)(v >>> 56);
        writeBuffer[1] = (byte)(v >>> 48);
        writeBuffer[2] = (byte)(v >>> 40);
        writeBuffer[3] = (byte)(v >>> 32);
        writeBuffer[4] = (byte)(v >>> 24);
        writeBuffer[5] = (byte)(v >>> 16);
        writeBuffer[6] = (byte)(v >>>  8);
        writeBuffer[7] = (byte)(v >>>  0);
        os.write(writeBuffer, 0, 8);
    }

    public static short getInt16(ByteBuffer buf) {
        return buf.getShort();
    }

    public static short getInt16(InputStream is) throws IOException {
        int ch1 = is.read();
        int ch2 = is.read();
        return (short)((ch1 << 8) + (ch2 << 0));
    }

    public static void putInt16(ByteBuffer buf, short value) {
        buf.putShort(value);
    }

    public static void putInt16(OutputStream os, short value) throws IOException{
        os.write((value >>> 8) & 0xFF);
        os.write((value >>> 0) & 0xFF);
    }

    /** puts an unsigned 2 byte integer into a byte buffer */
    public static void putUint16(java.nio.ByteBuffer buf, int value) {
        buf.putShort((short) (value & 0x0000ffff));
    }

    /** returns (as an int) an unsigned 2 byte integer from a byte buffer */
    public static int getUint16(java.nio.ByteBuffer buf) {
        return buf.getShort() & 0x0000ffff;
    }

    /** puts an unsigned 2 byte integer  */
    public static void putUint16(OutputStream os, int value) throws IOException{
        putInt16(os, (short) value);
    }

    /** returns (as an int) an unsigned 2 byte integer  */
    public static int getUint16(InputStream is) throws IOException {
        int ch1 = is.read();
        int ch2 = is.read();
        return ((ch1 << 8) + (ch2 << 0));
    }

    /** puts a 4 byte unsigned integer  */
    public static void putUint32(java.nio.ByteBuffer buf, long value) {
        buf.putInt((int) (value & 0x00000000ffffffffL));
    }

    /** puts an unsigned 2 byte integer  */
    public static void putUint32(OutputStream os, long value) throws IOException{
        putInt32(os, (int) value);
    }

    /** returns (as a long) an unsigned 4 byte integer from a byte buffer */
    public static long getUint32(java.nio.ByteBuffer buf) {
        return buf.getInt() & 0x00000000ffffffffL;
    }

    /** returns (as a long) an unsigned 4 byte integer */
    public static long getUint32(InputStream is) throws IOException {
        return getInt32(is) & 0x00000000ffffffffL;
    }

    /** returns the packed size of the passed string */
    public static int sizeOfString(String str) {
        return str == null ? 2 : 2 + str.length();
    }

    /** returns the size of the LMCP-packed bytes for an object */
    public static int sizeOf(Object obj) {
        if (obj == null) {
            return 1;
        }
        else if (obj instanceof LMCPObject) {
            return ((LMCPObject) obj).calcSize();
        }
        else if (obj instanceof Enum) {
            return 4;
        }
        else if (obj instanceof String) {
            return sizeOfString((String) obj);
        }
        else return 0;
    }

    /** returns the size of the passed list (use only for LMCP object lists) */
    public static int sizeOfList(java.util.ArrayList<? extends LMCPObject> list) {
        int size = 0;
        for (int i=0; i<list.size(); i++) {
            size += sizeOf(list.get(i));
        }
        return size;
    }

    /** returns the size of the passed array (use only for LMCP object arrays) */
    public static int sizeOfArray(Object[] array) {
        int size = 0;
        for (int i=0; i<array.length; i++) {
            size += sizeOf(array[i]);
        }
        return size;
    }

}
