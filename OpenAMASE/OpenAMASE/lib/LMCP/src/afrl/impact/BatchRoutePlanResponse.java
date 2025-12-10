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
 Batch Route plan response with vehicle-to-task timing reported. 
*/
public class BatchRoutePlanResponse extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 10;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "BatchRoutePlanResponse";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.BatchRoutePlanResponse";

    /**  Response ID matching request from ({@link BatchRoutePlanRequest}) (Units: None)*/
    @LmcpType("int64")
    protected long ResponseID = 0L;
    /**  Set of task-to-task timings for each requested vehicle (Units: None)*/
    @LmcpType("TaskTimingPair")
    protected java.util.ArrayList<afrl.impact.TaskTimingPair> VehicleTiming = new java.util.ArrayList<afrl.impact.TaskTimingPair>();

    
    public BatchRoutePlanResponse() {
    }

    public BatchRoutePlanResponse(long ResponseID){
        this.ResponseID = ResponseID;
    }


    public BatchRoutePlanResponse clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            BatchRoutePlanResponse newObj = new BatchRoutePlanResponse();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Response ID matching request from ({@link BatchRoutePlanRequest}) (Units: None)*/
    public long getResponseID() { return ResponseID; }

    /**  Response ID matching request from ({@link BatchRoutePlanRequest}) (Units: None)*/
    public BatchRoutePlanResponse setResponseID( long val ) {
        ResponseID = val;
        return this;
    }

    public java.util.ArrayList<afrl.impact.TaskTimingPair> getVehicleTiming() {
        return VehicleTiming;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(VehicleTiming);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ResponseID = LMCPUtil.getInt64(in);

        VehicleTiming.clear();
        int VehicleTiming_len = LMCPUtil.getUint16(in);
        for(int i=0; i<VehicleTiming_len; i++){
        VehicleTiming.add( (afrl.impact.TaskTimingPair) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ResponseID);
        LMCPUtil.putUint16(out, VehicleTiming.size());
        for(int i=0; i<VehicleTiming.size(); i++){
            LMCPUtil.putObject(out, VehicleTiming.get(i));
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
        buf.append( ws + "<BatchRoutePlanResponse Series=\"IMPACT\">\n");
        buf.append( ws + "  <ResponseID>" + String.valueOf(ResponseID) + "</ResponseID>\n");
        buf.append( ws + "  <VehicleTiming>\n");
        for (int i=0; i<VehicleTiming.size(); i++) {
            buf.append( VehicleTiming.get(i) == null ? ( ws + "    <null/>\n") : (VehicleTiming.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </VehicleTiming>\n");
        buf.append( ws + "</BatchRoutePlanResponse>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        BatchRoutePlanResponse o = (BatchRoutePlanResponse) anotherObj;
        if (ResponseID != o.ResponseID) return false;
         if (!VehicleTiming.equals( o.VehicleTiming)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
