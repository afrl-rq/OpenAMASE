// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.cmasi;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Collection of extrusions/zones that define operating region 
*/
public class OperatingRegion extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 39;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "OperatingRegion";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.OperatingRegion";

    /**  Operating region ID (Units: None)*/
    @LmcpType("int64")
    protected long ID = 0L;
    /**  Abstract Zone IDs in which the entity must remain during operation (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> KeepInAreas = new java.util.ArrayList<Long>();
    /**  Keep Out Zone IDs that an entity must remain out of during operation (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> KeepOutAreas = new java.util.ArrayList<Long>();

    
    public OperatingRegion() {
    }

    public OperatingRegion(long ID){
        this.ID = ID;
    }


    public OperatingRegion clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            OperatingRegion newObj = new OperatingRegion();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Operating region ID (Units: None)*/
    public long getID() { return ID; }

    /**  Operating region ID (Units: None)*/
    public OperatingRegion setID( long val ) {
        ID = val;
        return this;
    }

    public java.util.ArrayList<Long> getKeepInAreas() {
        return KeepInAreas;
    }

    public java.util.ArrayList<Long> getKeepOutAreas() {
        return KeepOutAreas;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        
        size += 2 + 8 * KeepInAreas.size();
        
        size += 2 + 8 * KeepOutAreas.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ID = LMCPUtil.getInt64(in);

        KeepInAreas.clear();
        int KeepInAreas_len = LMCPUtil.getUint16(in);
        for(int i=0; i<KeepInAreas_len; i++){
            KeepInAreas.add(LMCPUtil.getInt64(in));
        }
        KeepOutAreas.clear();
        int KeepOutAreas_len = LMCPUtil.getUint16(in);
        for(int i=0; i<KeepOutAreas_len; i++){
            KeepOutAreas.add(LMCPUtil.getInt64(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ID);
        LMCPUtil.putUint16(out, KeepInAreas.size());
        for(int i=0; i<KeepInAreas.size(); i++){
            LMCPUtil.putInt64(out, KeepInAreas.get(i));
        }
        LMCPUtil.putUint16(out, KeepOutAreas.size());
        for(int i=0; i<KeepOutAreas.size(); i++){
            LMCPUtil.putInt64(out, KeepOutAreas.get(i));
        }

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
        buf.append( ws + "<OperatingRegion Series=\"CMASI\">\n");
        buf.append( ws + "  <ID>" + String.valueOf(ID) + "</ID>\n");
        buf.append( ws + "  <KeepInAreas>\n");
        for (int i=0; i<KeepInAreas.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(KeepInAreas.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </KeepInAreas>\n");
        buf.append( ws + "  <KeepOutAreas>\n");
        for (int i=0; i<KeepOutAreas.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(KeepOutAreas.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </KeepOutAreas>\n");
        buf.append( ws + "</OperatingRegion>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        OperatingRegion o = (OperatingRegion) anotherObj;
        if (ID != o.ID) return false;
         if (!KeepInAreas.equals( o.KeepInAreas)) return false;
         if (!KeepOutAreas.equals( o.KeepOutAreas)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
