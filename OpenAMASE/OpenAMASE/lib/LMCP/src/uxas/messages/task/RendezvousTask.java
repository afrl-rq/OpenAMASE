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
 This task will synchronize all involved vehicles at a specified location             (or set of locations) at the same time.             Loiter patterns are used to ensure that when the latest             vehicle arrives, the others will be in the proper positions             and orientations to satisfy the location specification             simultaneously. 
*/
public class RendezvousTask extends afrl.cmasi.Task {
    
    public static final int LMCP_TYPE = 2;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "RendezvousTask";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.RendezvousTask";

    /**  Number of vehicles that will participate in the rendezvous. To restrict the available participants, use the 'EligibleEntities' field. If the number of participants is exactly equal to the number of eligible entities, then this task will only produce a single option that ensures those exact entities meet at the rendezvous location. (Units: None)*/
    @LmcpType("byte")
    protected short NumberOfParticipants = (byte)0;
    /**  The desired rendezvous states of the entities. If the EntityID fields are set to 0, then any eligible vehicle can be used at that location. Only used when 'MultiLocationRendezvous' is set to 'true'. If the EntityID fields are non-zero, they must correspond to an eligible vehicle and only that particular entity will be used at that position. (Units: None)*/
    @LmcpType("PlanningState")
    protected java.util.ArrayList<uxas.messages.task.PlanningState> RendezvousStates = new java.util.ArrayList<uxas.messages.task.PlanningState>();

    
    public RendezvousTask() {
    }

    public RendezvousTask(long TaskID, String Label, float RevisitRate, short Priority, boolean Required, short NumberOfParticipants){
        this.TaskID = TaskID;
        this.Label = Label;
        this.RevisitRate = RevisitRate;
        this.Priority = Priority;
        this.Required = Required;
        this.NumberOfParticipants = NumberOfParticipants;
    }


    public RendezvousTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            RendezvousTask newObj = new RendezvousTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Number of vehicles that will participate in the rendezvous. To restrict the available participants, use the 'EligibleEntities' field. If the number of participants is exactly equal to the number of eligible entities, then this task will only produce a single option that ensures those exact entities meet at the rendezvous location. (Units: None)*/
    public short getNumberOfParticipants() { return NumberOfParticipants; }

    /**  Number of vehicles that will participate in the rendezvous. To restrict the available participants, use the 'EligibleEntities' field. If the number of participants is exactly equal to the number of eligible entities, then this task will only produce a single option that ensures those exact entities meet at the rendezvous location. (Units: None)*/
    public RendezvousTask setNumberOfParticipants( short val ) {
        NumberOfParticipants = val;
        return this;
    }

    public java.util.ArrayList<uxas.messages.task.PlanningState> getRendezvousStates() {
        return RendezvousStates;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 1; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(RendezvousStates);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        NumberOfParticipants = LMCPUtil.getByte(in);

        RendezvousStates.clear();
        int RendezvousStates_len = LMCPUtil.getUint16(in);
        for(int i=0; i<RendezvousStates_len; i++){
        RendezvousStates.add( (uxas.messages.task.PlanningState) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putByte(out, NumberOfParticipants);
        LMCPUtil.putUint16(out, RendezvousStates.size());
        for(int i=0; i<RendezvousStates.size(); i++){
            LMCPUtil.putObject(out, RendezvousStates.get(i));
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
        buf.append( ws + "<RendezvousTask Series=\"UXTASK\">\n");
        buf.append( ws + "  <NumberOfParticipants>" + String.valueOf(NumberOfParticipants) + "</NumberOfParticipants>\n");
        buf.append( ws + "  <RendezvousStates>\n");
        for (int i=0; i<RendezvousStates.size(); i++) {
            buf.append( RendezvousStates.get(i) == null ? ( ws + "    <null/>\n") : (RendezvousStates.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </RendezvousStates>\n");
        buf.append( ws + "  <TaskID>" + String.valueOf(TaskID) + "</TaskID>\n");
        buf.append( ws + "  <Label>" + String.valueOf(Label) + "</Label>\n");
        buf.append( ws + "  <EligibleEntities>\n");
        for (int i=0; i<EligibleEntities.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(EligibleEntities.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </EligibleEntities>\n");
        buf.append( ws + "  <RevisitRate>" + String.valueOf(RevisitRate) + "</RevisitRate>\n");
        buf.append( ws + "  <Parameters>\n");
        for (int i=0; i<Parameters.size(); i++) {
            buf.append( Parameters.get(i) == null ? ( ws + "    <null/>\n") : (Parameters.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Parameters>\n");
        buf.append( ws + "  <Priority>" + String.valueOf(Priority) + "</Priority>\n");
        buf.append( ws + "  <Required>" + String.valueOf(Required) + "</Required>\n");
        buf.append( ws + "</RendezvousTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        RendezvousTask o = (RendezvousTask) anotherObj;
        if (NumberOfParticipants != o.NumberOfParticipants) return false;
         if (!RendezvousStates.equals( o.RendezvousStates)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)NumberOfParticipants;

        return hash + super.hashCode();
    }
    
}
