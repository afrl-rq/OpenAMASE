// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages.uxnative;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Remove an existing service from UxAS 
*/
public class KillService extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 4;

    public static final String SERIES_NAME = "UXNATIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149751333668345413L;
    public static final int SERIES_VERSION = 9;


    private static final String TYPE_NAME = "KillService";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.uxnative.KillService";

    /**  Globally unique ID for the service to be removed. If ServiceID == 0, then no service will be killed. ServiceID == -1 causes all services to be killed and UxAS to be shutdown. (Units: None)*/
    @LmcpType("int64")
    protected long ServiceID = 0L;

    
    public KillService() {
    }

    public KillService(long ServiceID){
        this.ServiceID = ServiceID;
    }


    public KillService clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            KillService newObj = new KillService();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Globally unique ID for the service to be removed. If ServiceID == 0, then no service will be killed. ServiceID == -1 causes all services to be killed and UxAS to be shutdown. (Units: None)*/
    public long getServiceID() { return ServiceID; }

    /**  Globally unique ID for the service to be removed. If ServiceID == 0, then no service will be killed. ServiceID == -1 causes all services to be killed and UxAS to be shutdown. (Units: None)*/
    public KillService setServiceID( long val ) {
        ServiceID = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ServiceID = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ServiceID);

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
        buf.append( ws + "<KillService Series=\"UXNATIVE\">\n");
        buf.append( ws + "  <ServiceID>" + String.valueOf(ServiceID) + "</ServiceID>\n");
        buf.append( ws + "</KillService>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        KillService o = (KillService) anotherObj;
        if (ServiceID != o.ServiceID) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
