// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.cmasi;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Used to define a loiter request from the automation service. 
*/
public class LoiterTask extends afrl.cmasi.Task {
    
    public static final int LMCP_TYPE = 34;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "LoiterTask";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.LoiterTask";

    /**  The desired loiter action to take. Must not be null. (Units: none)*/
    @LmcpType("LoiterAction")
    protected afrl.cmasi.LoiterAction DesiredAction = new afrl.cmasi.LoiterAction();

    
    public LoiterTask() {
    }

    public LoiterTask(long TaskID, String Label, float RevisitRate, short Priority, boolean Required, afrl.cmasi.LoiterAction DesiredAction){
        this.TaskID = TaskID;
        this.Label = Label;
        this.RevisitRate = RevisitRate;
        this.Priority = Priority;
        this.Required = Required;
        this.DesiredAction = DesiredAction;
    }


    public LoiterTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            LoiterTask newObj = new LoiterTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The desired loiter action to take. Must not be null. (Units: none)*/
    public afrl.cmasi.LoiterAction getDesiredAction() { return DesiredAction; }

    /**  The desired loiter action to take. Must not be null. (Units: none)*/
    public LoiterTask setDesiredAction( afrl.cmasi.LoiterAction val ) {
        DesiredAction = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types
        size += LMCPUtil.sizeOf(DesiredAction);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
            DesiredAction = (afrl.cmasi.LoiterAction) LMCPUtil.getObject(in);

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putObject(out, DesiredAction);

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
        buf.append( ws + "<LoiterTask Series=\"CMASI\">\n");
        if (DesiredAction!= null){
           buf.append( ws + "  <DesiredAction>\n");
           buf.append( ( DesiredAction.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </DesiredAction>\n");
        }
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
        buf.append( ws + "</LoiterTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        LoiterTask o = (LoiterTask) anotherObj;
        if (DesiredAction == null && o.DesiredAction != null) return false;
        if ( DesiredAction!= null && !DesiredAction.equals(o.DesiredAction)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
