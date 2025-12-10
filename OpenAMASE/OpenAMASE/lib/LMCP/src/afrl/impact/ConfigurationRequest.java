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
 Requests the latest configurations for vehicles in the system 
*/
public class ConfigurationRequest extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 32;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "ConfigurationRequest";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.ConfigurationRequest";

    /**  Vehicle IDs for which the corresponding configuration should be sent. If the list is empty, all available configurations should be sent in response. (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> VehicleID = new java.util.ArrayList<Long>();

    
    public ConfigurationRequest() {
    }



    public ConfigurationRequest clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            ConfigurationRequest newObj = new ConfigurationRequest();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public java.util.ArrayList<Long> getVehicleID() {
        return VehicleID;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types
        
        size += 2 + 8 * VehicleID.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        VehicleID.clear();
        int VehicleID_len = LMCPUtil.getUint16(in);
        for(int i=0; i<VehicleID_len; i++){
            VehicleID.add(LMCPUtil.getInt64(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putUint16(out, VehicleID.size());
        for(int i=0; i<VehicleID.size(); i++){
            LMCPUtil.putInt64(out, VehicleID.get(i));
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
        buf.append( ws + "<ConfigurationRequest Series=\"IMPACT\">\n");
        buf.append( ws + "  <VehicleID>\n");
        for (int i=0; i<VehicleID.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(VehicleID.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </VehicleID>\n");
        buf.append( ws + "</ConfigurationRequest>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        ConfigurationRequest o = (ConfigurationRequest) anotherObj;
         if (!VehicleID.equals( o.VehicleID)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
