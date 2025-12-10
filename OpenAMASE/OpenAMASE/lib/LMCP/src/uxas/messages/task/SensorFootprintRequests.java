// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages.task;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Request to compute a set of sensor footprints from possible vehicles in the system 
*/
public class SensorFootprintRequests extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 10;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "SensorFootprintRequests";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.SensorFootprintRequests";

    /**  Request ID for correlating to response (Units: None)*/
    @LmcpType("int64")
    protected long RequestID = 0L;
    /**  Request ID for correlating to response (Units: None)*/
    @LmcpType("FootprintRequest")
    protected java.util.ArrayList<uxas.messages.task.FootprintRequest> Footprints = new java.util.ArrayList<uxas.messages.task.FootprintRequest>();

    
    public SensorFootprintRequests() {
    }

    public SensorFootprintRequests(long RequestID){
        this.RequestID = RequestID;
    }


    public SensorFootprintRequests clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            SensorFootprintRequests newObj = new SensorFootprintRequests();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Request ID for correlating to response (Units: None)*/
    public long getRequestID() { return RequestID; }

    /**  Request ID for correlating to response (Units: None)*/
    public SensorFootprintRequests setRequestID( long val ) {
        RequestID = val;
        return this;
    }

    public java.util.ArrayList<uxas.messages.task.FootprintRequest> getFootprints() {
        return Footprints;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(Footprints);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        RequestID = LMCPUtil.getInt64(in);

        Footprints.clear();
        int Footprints_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Footprints_len; i++){
        Footprints.add( (uxas.messages.task.FootprintRequest) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, RequestID);
        LMCPUtil.putUint16(out, Footprints.size());
        for(int i=0; i<Footprints.size(); i++){
            LMCPUtil.putObject(out, Footprints.get(i));
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
        buf.append( ws + "<SensorFootprintRequests Series=\"UXTASK\">\n");
        buf.append( ws + "  <RequestID>" + String.valueOf(RequestID) + "</RequestID>\n");
        buf.append( ws + "  <Footprints>\n");
        for (int i=0; i<Footprints.size(); i++) {
            buf.append( Footprints.get(i) == null ? ( ws + "    <null/>\n") : (Footprints.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Footprints>\n");
        buf.append( ws + "</SensorFootprintRequests>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        SensorFootprintRequests o = (SensorFootprintRequests) anotherObj;
        if (RequestID != o.RequestID) return false;
         if (!Footprints.equals( o.Footprints)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
