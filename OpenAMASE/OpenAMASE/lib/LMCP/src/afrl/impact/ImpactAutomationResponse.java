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
 Automation response with specific IMPACT supporting fields 
*/
public class ImpactAutomationResponse extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 18;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "ImpactAutomationResponse";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.ImpactAutomationResponse";

    /**  Response ID matching request from ({@link ImpactAutomationRequest}) (Units: None)*/
    @LmcpType("int64")
    protected long ResponseID = 0L;
    /**  Automation response from the request. A valid ImpactAutomationResponse must define TrialResponse (null not allowed). (Units: None)*/
    @LmcpType("AutomationResponse")
    protected afrl.cmasi.AutomationResponse TrialResponse = new afrl.cmasi.AutomationResponse();
    /**  Associated play ID (Units: None)*/
    @LmcpType("int64")
    protected long PlayID = 0L;
    /**  Associated solution ID (Units: None)*/
    @LmcpType("int64")
    protected long SolutionID = 0L;
    /**  Flag indicating 'sandbox', i.e. not to be implemented (Units: None)*/
    @LmcpType("bool")
    protected boolean Sandbox = false;
    /**  Set of vehicle-to-task summaries that will be implemented by this automation response. Including timing, communication, and remaining energy (Units: None)*/
    @LmcpType("TaskSummary")
    protected java.util.ArrayList<afrl.impact.TaskSummary> Summaries = new java.util.ArrayList<afrl.impact.TaskSummary>();

    
    public ImpactAutomationResponse() {
    }

    public ImpactAutomationResponse(long ResponseID, afrl.cmasi.AutomationResponse TrialResponse, long PlayID, long SolutionID, boolean Sandbox){
        this.ResponseID = ResponseID;
        this.TrialResponse = TrialResponse;
        this.PlayID = PlayID;
        this.SolutionID = SolutionID;
        this.Sandbox = Sandbox;
    }


    public ImpactAutomationResponse clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            ImpactAutomationResponse newObj = new ImpactAutomationResponse();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Response ID matching request from ({@link ImpactAutomationRequest}) (Units: None)*/
    public long getResponseID() { return ResponseID; }

    /**  Response ID matching request from ({@link ImpactAutomationRequest}) (Units: None)*/
    public ImpactAutomationResponse setResponseID( long val ) {
        ResponseID = val;
        return this;
    }

    /**  Automation response from the request. A valid ImpactAutomationResponse must define TrialResponse (null not allowed). (Units: None)*/
    public afrl.cmasi.AutomationResponse getTrialResponse() { return TrialResponse; }

    /**  Automation response from the request. A valid ImpactAutomationResponse must define TrialResponse (null not allowed). (Units: None)*/
    public ImpactAutomationResponse setTrialResponse( afrl.cmasi.AutomationResponse val ) {
        TrialResponse = val;
        return this;
    }

    /**  Associated play ID (Units: None)*/
    public long getPlayID() { return PlayID; }

    /**  Associated play ID (Units: None)*/
    public ImpactAutomationResponse setPlayID( long val ) {
        PlayID = val;
        return this;
    }

    /**  Associated solution ID (Units: None)*/
    public long getSolutionID() { return SolutionID; }

    /**  Associated solution ID (Units: None)*/
    public ImpactAutomationResponse setSolutionID( long val ) {
        SolutionID = val;
        return this;
    }

    /**  Flag indicating 'sandbox', i.e. not to be implemented (Units: None)*/
    public boolean getSandbox() { return Sandbox; }

    /**  Flag indicating 'sandbox', i.e. not to be implemented (Units: None)*/
    public ImpactAutomationResponse setSandbox( boolean val ) {
        Sandbox = val;
        return this;
    }

    public java.util.ArrayList<afrl.impact.TaskSummary> getSummaries() {
        return Summaries;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 25; // accounts for primitive types
        size += LMCPUtil.sizeOf(TrialResponse);
        size += 2;
        size += LMCPUtil.sizeOfList(Summaries);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ResponseID = LMCPUtil.getInt64(in);

            TrialResponse = (afrl.cmasi.AutomationResponse) LMCPUtil.getObject(in);
        PlayID = LMCPUtil.getInt64(in);

        SolutionID = LMCPUtil.getInt64(in);

        Sandbox = LMCPUtil.getBool(in);

        Summaries.clear();
        int Summaries_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Summaries_len; i++){
        Summaries.add( (afrl.impact.TaskSummary) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ResponseID);
        LMCPUtil.putObject(out, TrialResponse);
        LMCPUtil.putInt64(out, PlayID);
        LMCPUtil.putInt64(out, SolutionID);
        LMCPUtil.putBool(out, Sandbox);
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
        buf.append( ws + "<ImpactAutomationResponse Series=\"IMPACT\">\n");
        buf.append( ws + "  <ResponseID>" + String.valueOf(ResponseID) + "</ResponseID>\n");
        if (TrialResponse!= null){
           buf.append( ws + "  <TrialResponse>\n");
           buf.append( ( TrialResponse.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </TrialResponse>\n");
        }
        buf.append( ws + "  <PlayID>" + String.valueOf(PlayID) + "</PlayID>\n");
        buf.append( ws + "  <SolutionID>" + String.valueOf(SolutionID) + "</SolutionID>\n");
        buf.append( ws + "  <Sandbox>" + String.valueOf(Sandbox) + "</Sandbox>\n");
        buf.append( ws + "  <Summaries>\n");
        for (int i=0; i<Summaries.size(); i++) {
            buf.append( Summaries.get(i) == null ? ( ws + "    <null/>\n") : (Summaries.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Summaries>\n");
        buf.append( ws + "</ImpactAutomationResponse>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        ImpactAutomationResponse o = (ImpactAutomationResponse) anotherObj;
        if (ResponseID != o.ResponseID) return false;
        if (TrialResponse == null && o.TrialResponse != null) return false;
        if ( TrialResponse!= null && !TrialResponse.equals(o.TrialResponse)) return false;
        if (PlayID != o.PlayID) return false;
        if (SolutionID != o.SolutionID) return false;
        if (Sandbox != o.Sandbox) return false;
         if (!Summaries.equals( o.Summaries)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
