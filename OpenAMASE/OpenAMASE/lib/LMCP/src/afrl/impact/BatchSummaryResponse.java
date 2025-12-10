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
 Batch summary response as a list of all vehicle-to-task information 
*/
public class BatchSummaryResponse extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 13;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "BatchSummaryResponse";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.BatchSummaryResponse";

    /**  Response ID matching request from ({@link BatchSummaryRequest}) (Units: None)*/
    @LmcpType("int64")
    protected long ResponseID = 0L;
    /**  Set of vehicle-to-task and task-to-task summaries including timing, communication, and remaining energy (Units: None)*/
    @LmcpType("TaskSummary")
    protected java.util.ArrayList<afrl.impact.TaskSummary> Summaries = new java.util.ArrayList<afrl.impact.TaskSummary>();

    
    public BatchSummaryResponse() {
    }

    public BatchSummaryResponse(long ResponseID){
        this.ResponseID = ResponseID;
    }


    public BatchSummaryResponse clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            BatchSummaryResponse newObj = new BatchSummaryResponse();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Response ID matching request from ({@link BatchSummaryRequest}) (Units: None)*/
    public long getResponseID() { return ResponseID; }

    /**  Response ID matching request from ({@link BatchSummaryRequest}) (Units: None)*/
    public BatchSummaryResponse setResponseID( long val ) {
        ResponseID = val;
        return this;
    }

    public java.util.ArrayList<afrl.impact.TaskSummary> getSummaries() {
        return Summaries;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(Summaries);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ResponseID = LMCPUtil.getInt64(in);

        Summaries.clear();
        int Summaries_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Summaries_len; i++){
        Summaries.add( (afrl.impact.TaskSummary) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ResponseID);
        LMCPUtil.putUint16(out, Summaries.size());
        for(int i=0; i<Summaries.size(); i++){
            LMCPUtil.putObject(out, Summaries.get(i));
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
        buf.append( ws + "<BatchSummaryResponse Series=\"IMPACT\">\n");
        buf.append( ws + "  <ResponseID>" + String.valueOf(ResponseID) + "</ResponseID>\n");
        buf.append( ws + "  <Summaries>\n");
        for (int i=0; i<Summaries.size(); i++) {
            buf.append( Summaries.get(i) == null ? ( ws + "    <null/>\n") : (Summaries.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Summaries>\n");
        buf.append( ws + "</BatchSummaryResponse>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        BatchSummaryResponse o = (BatchSummaryResponse) anotherObj;
        if (ResponseID != o.ResponseID) return false;
         if (!Summaries.equals( o.Summaries)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
