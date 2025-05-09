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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class LMCPObject implements java.io.Serializable, Cloneable {

    /** returns the size of this message (ushort value) not including the header or the checksum*/
    //public int calculateSize();

    /** packs the data into a java.nio.ByteBuffer */
    public abstract void pack(OutputStream out) throws IOException;

    /** unpacks data from a java.nio.ByteBuffer */
    public abstract void unpack(InputStream in) throws IOException;

    public int calcSize() {
        return 15; // accounts for series name (8), type (4), version (2), and null byte (1)
    }

    public LMCPObject clone() {
        try {
            return (LMCPObject) super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    public boolean equals(Object anotherObj) { return true; }

    public int hashCode() { return 0; }

    public abstract int getLMCPType();

    public abstract String getLMCPSeriesName();

    public abstract long getLMCPSeriesNameAsLong();

    public abstract int getLMCPSeriesVersion();

    public abstract String getLMCPTypeName();

    public abstract String getFullLMCPTypeName();

    public abstract String toXML(String ws);

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface LmcpType {
        String value();
    }

}
