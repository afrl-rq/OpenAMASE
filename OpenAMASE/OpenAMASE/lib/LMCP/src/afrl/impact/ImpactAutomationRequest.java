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
 Automation request specific to IMPACT (includes 'sandbox' feature and play/soln IDs) 
*/
public class ImpactAutomationRequest extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 17;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "ImpactAutomationRequest";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.ImpactAutomationRequest";

    /**  Request ID for correlating with response (Units: None)*/
    @LmcpType("int64")
    protected long RequestID = 0L;
    /**  Standard CMASI Automation request. A valid ImpactAutomationRequest must define TrialRequest (null not allowed). (Units: None)*/
    @LmcpType("AutomationRequest")
    protected afrl.cmasi.AutomationRequest TrialRequest = new afrl.cmasi.AutomationRequest();
    /**  Override nominal speed/alt. If a vehicle is not in list, its nominal speed/alt is used (Units: None)*/
    @LmcpType("SpeedAltPair")
    protected java.util.ArrayList<afrl.impact.SpeedAltPair> OverridePlanningConditions = new java.util.ArrayList<afrl.impact.SpeedAltPair>();
    /**  Associated play ID (Units: None)*/
    @LmcpType("int64")
    protected long PlayID = 0L;
    /**  Associated solution ID (Units: None)*/
    @LmcpType("int64")
    protected long SolutionID = 0L;
    /**  Flag indicating 'sandbox', i.e. not to be implemented (Units: None)*/
    @LmcpType("bool")
    protected boolean Sandbox = false;

    
    public ImpactAutomationRequest() {
    }

    public ImpactAutomationRequest(long RequestID, afrl.cmasi.AutomationRequest TrialRequest, long PlayID, long SolutionID, boolean Sandbox){
        this.RequestID = RequestID;
        this.TrialRequest = TrialRequest;
        this.PlayID = PlayID;
        this.SolutionID = SolutionID;
        this.Sandbox = Sandbox;
    }


    public ImpactAutomationRequest clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            ImpactAutomationRequest newObj = new ImpactAutomationRequest();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Request ID for correlating with response (Units: None)*/
    public long getRequestID() { return RequestID; }

    /**  Request ID for correlating with response (Units: None)*/
    public ImpactAutomationRequest setRequestID( long val ) {
        RequestID = val;
        return this;
    }

    /**  Standard CMASI Automation request. A valid ImpactAutomationRequest must define TrialRequest (null not allowed). (Units: None)*/
    public afrl.cmasi.AutomationRequest getTrialRequest() { return TrialRequest; }

    /**  Standard CMASI Automation request. A valid ImpactAutomationRequest must define TrialRequest (null not allowed). (Units: None)*/
    public ImpactAutomationRequest setTrialRequest( afrl.cmasi.AutomationRequest val ) {
        TrialRequest = val;
        return this;
    }

    public java.util.ArrayList<afrl.impact.SpeedAltPair> getOverridePlanningConditions() {
        return OverridePlanningConditions;
    }

    /**  Associated play ID (Units: None)*/
    public long getPlayID() { return PlayID; }

    /**  Associated play ID (Units: None)*/
    public ImpactAutomationRequest setPlayID( long val ) {
        PlayID = val;
        return this;
    }

    /**  Associated solution ID (Units: None)*/
    public long getSolutionID() { return SolutionID; }

    /**  Associated solution ID (Units: None)*/
    public ImpactAutomationRequest setSolutionID( long val ) {
        SolutionID = val;
        return this;
    }

    /**  Flag indicating 'sandbox', i.e. not to be implemented (Units: None)*/
    public boolean getSandbox() { return Sandbox; }

    /**  Flag indicating 'sandbox', i.e. not to be implemented (Units: None)*/
    public ImpactAutomationRequest setSandbox( boolean val ) {
        Sandbox = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 25; // accounts for primitive types
        size += LMCPUtil.sizeOf(TrialRequest);
        size += 2;
        size += LMCPUtil.sizeOfList(OverridePlanningConditions);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        RequestID = LMCPUtil.getInt64(in);

            TrialRequest = (afrl.cmasi.AutomationRequest) LMCPUtil.getObject(in);
        OverridePlanningConditions.clear();
        int OverridePlanningConditions_len = LMCPUtil.getUint16(in);
        for(int i=0; i<OverridePlanningConditions_len; i++){
        OverridePlanningConditions.add( (afrl.impact.SpeedAltPair) LMCPUtil.getObject(in));
        }
        PlayID = LMCPUtil.getInt64(in);

        SolutionID = LMCPUtil.getInt64(in);

        Sandbox = LMCPUtil.getBool(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, RequestID);
        LMCPUtil.putObject(out, TrialRequest);
        LMCPUtil.putUint16(out, OverridePlanningConditions.size());
        for(int i=0; i<OverridePlanningConditions.size(); i++){
            LMCPUtil.putObject(out, OverridePlanningConditions.get(i));
        }
        LMCPUtil.putInt64(out, PlayID);
        LMCPUtil.putInt64(out, SolutionID);
        LMCPUtil.putBool(out, Sandbox);

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
        buf.append( ws + "<ImpactAutomationRequest Series=\"IMPACT\">\n");
        buf.append( ws + "  <RequestID>" + String.valueOf(RequestID) + "</RequestID>\n");
        if (TrialRequest!= null){
           buf.append( ws + "  <TrialRequest>\n");
           buf.append( ( TrialRequest.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </TrialRequest>\n");
        }
        buf.append( ws + "  <OverridePlanningConditions>\n");
        for (int i=0; i<OverridePlanningConditions.size(); i++) {
            buf.append( OverridePlanningConditions.get(i) == null ? ( ws + "    <null/>\n") : (OverridePlanningConditions.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </OverridePlanningConditions>\n");
        buf.append( ws + "  <PlayID>" + String.valueOf(PlayID) + "</PlayID>\n");
        buf.append( ws + "  <SolutionID>" + String.valueOf(SolutionID) + "</SolutionID>\n");
        buf.append( ws + "  <Sandbox>" + String.valueOf(Sandbox) + "</Sandbox>\n");
        buf.append( ws + "</ImpactAutomationRequest>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        ImpactAutomationRequest o = (ImpactAutomationRequest) anotherObj;
        if (RequestID != o.RequestID) return false;
        if (TrialRequest == null && o.TrialRequest != null) return false;
        if ( TrialRequest!= null && !TrialRequest.equals(o.TrialRequest)) return false;
         if (!OverridePlanningConditions.equals( o.OverridePlanningConditions)) return false;
        if (PlayID != o.PlayID) return false;
        if (SolutionID != o.SolutionID) return false;
        if (Sandbox != o.Sandbox) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
