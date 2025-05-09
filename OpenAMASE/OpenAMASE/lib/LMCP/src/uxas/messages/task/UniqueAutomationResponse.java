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
 Patches CMASI automation response to add a unique identifier 
*/
public class UniqueAutomationResponse extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 9;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "UniqueAutomationResponse";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.UniqueAutomationResponse";

    /**  Identifier for tracking requests and responses. Must match RequestID from corresponding UniqueAutomationRequest (Units: None)*/
    @LmcpType("int64")
    protected long ResponseID = 0L;
    /**  Original automation request without a unique identifier. A valid UniqueAutomationResponse must define OriginalResponse (null not allowed). (Units: None)*/
    @LmcpType("AutomationResponse")
    protected afrl.cmasi.AutomationResponse OriginalResponse = new afrl.cmasi.AutomationResponse();
    /**  The final states of entities when the plan is completed. (Units: None)*/
    @LmcpType("PlanningState")
    protected java.util.ArrayList<uxas.messages.task.PlanningState> FinalStates = new java.util.ArrayList<uxas.messages.task.PlanningState>();

    
    public UniqueAutomationResponse() {
    }

    public UniqueAutomationResponse(long ResponseID, afrl.cmasi.AutomationResponse OriginalResponse){
        this.ResponseID = ResponseID;
        this.OriginalResponse = OriginalResponse;
    }


    public UniqueAutomationResponse clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            UniqueAutomationResponse newObj = new UniqueAutomationResponse();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Identifier for tracking requests and responses. Must match RequestID from corresponding UniqueAutomationRequest (Units: None)*/
    public long getResponseID() { return ResponseID; }

    /**  Identifier for tracking requests and responses. Must match RequestID from corresponding UniqueAutomationRequest (Units: None)*/
    public UniqueAutomationResponse setResponseID( long val ) {
        ResponseID = val;
        return this;
    }

    /**  Original automation request without a unique identifier. A valid UniqueAutomationResponse must define OriginalResponse (null not allowed). (Units: None)*/
    public afrl.cmasi.AutomationResponse getOriginalResponse() { return OriginalResponse; }

    /**  Original automation request without a unique identifier. A valid UniqueAutomationResponse must define OriginalResponse (null not allowed). (Units: None)*/
    public UniqueAutomationResponse setOriginalResponse( afrl.cmasi.AutomationResponse val ) {
        OriginalResponse = val;
        return this;
    }

    public java.util.ArrayList<uxas.messages.task.PlanningState> getFinalStates() {
        return FinalStates;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += LMCPUtil.sizeOf(OriginalResponse);
        size += 2;
        size += LMCPUtil.sizeOfList(FinalStates);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ResponseID = LMCPUtil.getInt64(in);

            OriginalResponse = (afrl.cmasi.AutomationResponse) LMCPUtil.getObject(in);
        FinalStates.clear();
        int FinalStates_len = LMCPUtil.getUint16(in);
        for(int i=0; i<FinalStates_len; i++){
        FinalStates.add( (uxas.messages.task.PlanningState) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ResponseID);
        LMCPUtil.putObject(out, OriginalResponse);
        LMCPUtil.putUint16(out, FinalStates.size());
        for(int i=0; i<FinalStates.size(); i++){
            LMCPUtil.putObject(out, FinalStates.get(i));
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
        buf.append( ws + "<UniqueAutomationResponse Series=\"UXTASK\">\n");
        buf.append( ws + "  <ResponseID>" + String.valueOf(ResponseID) + "</ResponseID>\n");
        if (OriginalResponse!= null){
           buf.append( ws + "  <OriginalResponse>\n");
           buf.append( ( OriginalResponse.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </OriginalResponse>\n");
        }
        buf.append( ws + "  <FinalStates>\n");
        for (int i=0; i<FinalStates.size(); i++) {
            buf.append( FinalStates.get(i) == null ? ( ws + "    <null/>\n") : (FinalStates.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </FinalStates>\n");
        buf.append( ws + "</UniqueAutomationResponse>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        UniqueAutomationResponse o = (UniqueAutomationResponse) anotherObj;
        if (ResponseID != o.ResponseID) return false;
        if (OriginalResponse == null && o.OriginalResponse != null) return false;
        if ( OriginalResponse!= null && !OriginalResponse.equals(o.OriginalResponse)) return false;
         if (!FinalStates.equals( o.FinalStates)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
