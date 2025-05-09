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
 A CMASI automation request (with Identifier) that requires coordination among         the eligible entities. Note: entities must have a {@link AssignmentCoordinatorTask} task        running to utilize this this message.
*/
public class CoordinatedAutomationRequest extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 5;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "CoordinatedAutomationRequest";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.CoordinatedAutomationRequest";

    /**  Identifier for tracking requests and coordination (Units: None)*/
    @LmcpType("int64")
    protected long RequestID = 0L;
    /**  This is the maximum time to wait for {@link AssignmentCoordination} messages from the other eligible entities, before proceeding with the assignment. Note: the value of -1 indicates that there is no limit. If there are no eligble entites listed in the {@link CoordinatedAutomationRequest}, then {@link MaximumResponseTime} is the amount of time the {@link AssignmentCoordinatorTask} waits for {@link AssignmentCoordination} messages before proceeding with the assignment. Note: if there are no eligble entities and the {@link MaximumResponseTime} is -1, then the {@link AssignmentCoordination} proceeds with the assignment immediately. (Units: milliseconds)*/
    @LmcpType("int64")
    protected long MaximumResponseTime = -1L;
    /**  Original automation request without a unique identifier. A valid CoordinatedAutomationRequest must define OriginalRequest (null not allowed). (Units: None)*/
    @LmcpType("AutomationRequest")
    protected afrl.cmasi.AutomationRequest OriginalRequest = new afrl.cmasi.AutomationRequest();
    /**  The initial states of entities used in planning. Note: if an eligible entity does not have a PlanningState, then it must be obtained from the entity in an {@link AssignmentCoordination} message. (Units: None)*/
    @LmcpType("PlanningState")
    protected java.util.ArrayList<uxas.messages.task.PlanningState> PlanningStates = new java.util.ArrayList<uxas.messages.task.PlanningState>();

    
    public CoordinatedAutomationRequest() {
    }

    public CoordinatedAutomationRequest(long RequestID, long MaximumResponseTime, afrl.cmasi.AutomationRequest OriginalRequest){
        this.RequestID = RequestID;
        this.MaximumResponseTime = MaximumResponseTime;
        this.OriginalRequest = OriginalRequest;
    }


    public CoordinatedAutomationRequest clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            CoordinatedAutomationRequest newObj = new CoordinatedAutomationRequest();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Identifier for tracking requests and coordination (Units: None)*/
    public long getRequestID() { return RequestID; }

    /**  Identifier for tracking requests and coordination (Units: None)*/
    public CoordinatedAutomationRequest setRequestID( long val ) {
        RequestID = val;
        return this;
    }

    /**  This is the maximum time to wait for {@link AssignmentCoordination} messages from the other eligible entities, before proceeding with the assignment. Note: the value of -1 indicates that there is no limit. If there are no eligble entites listed in the {@link CoordinatedAutomationRequest}, then {@link MaximumResponseTime} is the amount of time the {@link AssignmentCoordinatorTask} waits for {@link AssignmentCoordination} messages before proceeding with the assignment. Note: if there are no eligble entities and the {@link MaximumResponseTime} is -1, then the {@link AssignmentCoordination} proceeds with the assignment immediately. (Units: milliseconds)*/
    public long getMaximumResponseTime() { return MaximumResponseTime; }

    /**  This is the maximum time to wait for {@link AssignmentCoordination} messages from the other eligible entities, before proceeding with the assignment. Note: the value of -1 indicates that there is no limit. If there are no eligble entites listed in the {@link CoordinatedAutomationRequest}, then {@link MaximumResponseTime} is the amount of time the {@link AssignmentCoordinatorTask} waits for {@link AssignmentCoordination} messages before proceeding with the assignment. Note: if there are no eligble entities and the {@link MaximumResponseTime} is -1, then the {@link AssignmentCoordination} proceeds with the assignment immediately. (Units: milliseconds)*/
    public CoordinatedAutomationRequest setMaximumResponseTime( long val ) {
        MaximumResponseTime = val;
        return this;
    }

    /**  Original automation request without a unique identifier. A valid CoordinatedAutomationRequest must define OriginalRequest (null not allowed). (Units: None)*/
    public afrl.cmasi.AutomationRequest getOriginalRequest() { return OriginalRequest; }

    /**  Original automation request without a unique identifier. A valid CoordinatedAutomationRequest must define OriginalRequest (null not allowed). (Units: None)*/
    public CoordinatedAutomationRequest setOriginalRequest( afrl.cmasi.AutomationRequest val ) {
        OriginalRequest = val;
        return this;
    }

    public java.util.ArrayList<uxas.messages.task.PlanningState> getPlanningStates() {
        return PlanningStates;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types
        size += LMCPUtil.sizeOf(OriginalRequest);
        size += 2;
        size += LMCPUtil.sizeOfList(PlanningStates);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        RequestID = LMCPUtil.getInt64(in);

        MaximumResponseTime = LMCPUtil.getInt64(in);

            OriginalRequest = (afrl.cmasi.AutomationRequest) LMCPUtil.getObject(in);
        PlanningStates.clear();
        int PlanningStates_len = LMCPUtil.getUint16(in);
        for(int i=0; i<PlanningStates_len; i++){
        PlanningStates.add( (uxas.messages.task.PlanningState) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, RequestID);
        LMCPUtil.putInt64(out, MaximumResponseTime);
        LMCPUtil.putObject(out, OriginalRequest);
        LMCPUtil.putUint16(out, PlanningStates.size());
        for(int i=0; i<PlanningStates.size(); i++){
            LMCPUtil.putObject(out, PlanningStates.get(i));
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
        buf.append( ws + "<CoordinatedAutomationRequest Series=\"UXTASK\">\n");
        buf.append( ws + "  <RequestID>" + String.valueOf(RequestID) + "</RequestID>\n");
        buf.append( ws + "  <MaximumResponseTime>" + String.valueOf(MaximumResponseTime) + "</MaximumResponseTime>\n");
        if (OriginalRequest!= null){
           buf.append( ws + "  <OriginalRequest>\n");
           buf.append( ( OriginalRequest.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </OriginalRequest>\n");
        }
        buf.append( ws + "  <PlanningStates>\n");
        for (int i=0; i<PlanningStates.size(); i++) {
            buf.append( PlanningStates.get(i) == null ? ( ws + "    <null/>\n") : (PlanningStates.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </PlanningStates>\n");
        buf.append( ws + "</CoordinatedAutomationRequest>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        CoordinatedAutomationRequest o = (CoordinatedAutomationRequest) anotherObj;
        if (RequestID != o.RequestID) return false;
        if (MaximumResponseTime != o.MaximumResponseTime) return false;
        if (OriginalRequest == null && o.OriginalRequest != null) return false;
        if ( OriginalRequest!= null && !OriginalRequest.equals(o.OriginalRequest)) return false;
         if (!PlanningStates.equals( o.PlanningStates)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
