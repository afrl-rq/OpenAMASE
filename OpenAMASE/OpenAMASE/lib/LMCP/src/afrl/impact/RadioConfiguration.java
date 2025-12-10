// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.impact;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Indicates the radio specification carried by an entity 
*/
public class RadioConfiguration extends afrl.cmasi.PayloadConfiguration {
    
    public static final int LMCP_TYPE = 2;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "RadioConfiguration";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.RadioConfiguration";

    /**  The range of the radio (Units: meters)*/
    @LmcpType("real32")
    protected float Range = (float)1500.0;
    /**  The vehicle's rally point during loss of comm contingency. When set to null, no defined loss of comm behavior. (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D RallyPoint = null;
    /**  Time after loss of communications the vehicle should return to its rally point (Units: milliseconds)*/
    @LmcpType("int64")
    protected long Timeout = 120000L;

    
    public RadioConfiguration() {
    }

    public RadioConfiguration(long PayloadID, String PayloadKind, float Range, afrl.cmasi.Location3D RallyPoint, long Timeout){
        this.PayloadID = PayloadID;
        this.PayloadKind = PayloadKind;
        this.Range = Range;
        this.RallyPoint = RallyPoint;
        this.Timeout = Timeout;
    }


    public RadioConfiguration clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            RadioConfiguration newObj = new RadioConfiguration();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The range of the radio (Units: meters)*/
    public float getRange() { return Range; }

    /**  The range of the radio (Units: meters)*/
    public RadioConfiguration setRange( float val ) {
        Range = val;
        return this;
    }

    /**  The vehicle's rally point during loss of comm contingency. When set to null, no defined loss of comm behavior. (Units: None)*/
    public afrl.cmasi.Location3D getRallyPoint() { return RallyPoint; }

    /**  The vehicle's rally point during loss of comm contingency. When set to null, no defined loss of comm behavior. (Units: None)*/
    public RadioConfiguration setRallyPoint( afrl.cmasi.Location3D val ) {
        RallyPoint = val;
        return this;
    }

    /**  Time after loss of communications the vehicle should return to its rally point (Units: milliseconds)*/
    public long getTimeout() { return Timeout; }

    /**  Time after loss of communications the vehicle should return to its rally point (Units: milliseconds)*/
    public RadioConfiguration setTimeout( long val ) {
        Timeout = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 12; // accounts for primitive types
        size += LMCPUtil.sizeOf(RallyPoint);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        Range = LMCPUtil.getReal32(in);

            RallyPoint = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        Timeout = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putReal32(out, Range);
        LMCPUtil.putObject(out, RallyPoint);
        LMCPUtil.putInt64(out, Timeout);

    }

    public int getLMCPType() { return LMCP_TYPE; }

    public String getLMCPSeriesName() { return SERIES_NAME; }

    public long getLMCPSeriesNameAsLong() { return SERIES_NAME_ID; }

    public int getLMCPSeriesVersion() { return SERIES_VERSION; };

    public String getLMCPTypeName() { return TYPE_NAME; }

    public String getFullLMCPTypeName() { return FULL_LMCP_TYPE_NAME; }

    public String toString() {
        return toXML("");
    }

    public String toXML(String ws) {
        StringBuffer buf = new StringBuffer();
        buf.append( ws + "<RadioConfiguration Series=\"IMPACT\">\n");
        buf.append( ws + "  <Range>" + String.valueOf(Range) + "</Range>\n");
        if (RallyPoint!= null){
           buf.append( ws + "  <RallyPoint>\n");
           buf.append( ( RallyPoint.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </RallyPoint>\n");
        }
        buf.append( ws + "  <Timeout>" + String.valueOf(Timeout) + "</Timeout>\n");
        buf.append( ws + "  <PayloadID>" + String.valueOf(PayloadID) + "</PayloadID>\n");
        buf.append( ws + "  <PayloadKind>" + String.valueOf(PayloadKind) + "</PayloadKind>\n");
        buf.append( ws + "  <Parameters>\n");
        for (int i=0; i<Parameters.size(); i++) {
            buf.append( Parameters.get(i) == null ? ( ws + "    <null/>\n") : (Parameters.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Parameters>\n");
        buf.append( ws + "</RadioConfiguration>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        RadioConfiguration o = (RadioConfiguration) anotherObj;
        if (Range != o.Range) return false;
        if (RallyPoint == null && o.RallyPoint != null) return false;
        if ( RallyPoint!= null && !RallyPoint.equals(o.RallyPoint)) return false;
        if (Timeout != o.Timeout) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)Range;

        return hash + super.hashCode();
    }
    
}
