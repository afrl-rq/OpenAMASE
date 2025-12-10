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
 This messages is used to syncronize the entity states, used for planning, to         facilitate assignment coordination. When an entity receives a {@link CoordinatedAutomationRequest}         message it's {@link AssignmentCoordinatorTask} task sends out a AssignmentCoordination message with the entity's planning state. 
*/
public class AssignmentCoordination extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 4;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "AssignmentCoordination";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.AssignmentCoordination";

    /**  Identifier of the associated CoordinatedAutomationRequest (Units: None)*/
    @LmcpType("int64")
    protected long CoordinatedAutomationRequestID = 0L;
    /**  The state of the entity that will used for planning/assignment of the given automation request. A valid AssignmentCoordination must define PlanningState (null not allowed). (Units: None)*/
    @LmcpType("PlanningState")
    protected uxas.messages.task.PlanningState PlanningState = new uxas.messages.task.PlanningState();

    
    public AssignmentCoordination() {
    }

    public AssignmentCoordination(long CoordinatedAutomationRequestID, uxas.messages.task.PlanningState PlanningState){
        this.CoordinatedAutomationRequestID = CoordinatedAutomationRequestID;
        this.PlanningState = PlanningState;
    }


    public AssignmentCoordination clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            AssignmentCoordination newObj = new AssignmentCoordination();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Identifier of the associated CoordinatedAutomationRequest (Units: None)*/
    public long getCoordinatedAutomationRequestID() { return CoordinatedAutomationRequestID; }

    /**  Identifier of the associated CoordinatedAutomationRequest (Units: None)*/
    public AssignmentCoordination setCoordinatedAutomationRequestID( long val ) {
        CoordinatedAutomationRequestID = val;
        return this;
    }

    /**  The state of the entity that will used for planning/assignment of the given automation request. A valid AssignmentCoordination must define PlanningState (null not allowed). (Units: None)*/
    public uxas.messages.task.PlanningState getPlanningState() { return PlanningState; }

    /**  The state of the entity that will used for planning/assignment of the given automation request. A valid AssignmentCoordination must define PlanningState (null not allowed). (Units: None)*/
    public AssignmentCoordination setPlanningState( uxas.messages.task.PlanningState val ) {
        PlanningState = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += LMCPUtil.sizeOf(PlanningState);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        CoordinatedAutomationRequestID = LMCPUtil.getInt64(in);

            PlanningState = (uxas.messages.task.PlanningState) LMCPUtil.getObject(in);

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, CoordinatedAutomationRequestID);
        LMCPUtil.putObject(out, PlanningState);

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
        buf.append( ws + "<AssignmentCoordination Series=\"UXTASK\">\n");
        buf.append( ws + "  <CoordinatedAutomationRequestID>" + String.valueOf(CoordinatedAutomationRequestID) + "</CoordinatedAutomationRequestID>\n");
        if (PlanningState!= null){
           buf.append( ws + "  <PlanningState>\n");
           buf.append( ( PlanningState.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </PlanningState>\n");
        }
        buf.append( ws + "</AssignmentCoordination>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        AssignmentCoordination o = (AssignmentCoordination) anotherObj;
        if (CoordinatedAutomationRequestID != o.CoordinatedAutomationRequestID) return false;
        if (PlanningState == null && o.PlanningState != null) return false;
        if ( PlanningState!= null && !PlanningState.equals(o.PlanningState)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
