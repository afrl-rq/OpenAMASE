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
            Used to request a must-fly task with optional action list.        
*/
public class MustFlyTask extends afrl.cmasi.Task {
    
    public static final int LMCP_TYPE = 37;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "MustFlyTask";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.MustFlyTask";

    /**  Point that vehicle must fly through. Null position not allowed. (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D Position = new afrl.cmasi.Location3D();
    /**  Denotes whether altitude should be used in this MustFlyTask. If false, the aircraft should maintain altitude according to previous commands. (Units: None)*/
    @LmcpType("bool")
    protected boolean UseAltitude = true;

    
    public MustFlyTask() {
    }

    public MustFlyTask(long TaskID, String Label, float RevisitRate, short Priority, boolean Required, afrl.cmasi.Location3D Position, boolean UseAltitude){
        this.TaskID = TaskID;
        this.Label = Label;
        this.RevisitRate = RevisitRate;
        this.Priority = Priority;
        this.Required = Required;
        this.Position = Position;
        this.UseAltitude = UseAltitude;
    }


    public MustFlyTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            MustFlyTask newObj = new MustFlyTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Point that vehicle must fly through. Null position not allowed. (Units: None)*/
    public afrl.cmasi.Location3D getPosition() { return Position; }

    /**  Point that vehicle must fly through. Null position not allowed. (Units: None)*/
    public MustFlyTask setPosition( afrl.cmasi.Location3D val ) {
        Position = val;
        return this;
    }

    /**  Denotes whether altitude should be used in this MustFlyTask. If false, the aircraft should maintain altitude according to previous commands. (Units: None)*/
    public boolean getUseAltitude() { return UseAltitude; }

    /**  Denotes whether altitude should be used in this MustFlyTask. If false, the aircraft should maintain altitude according to previous commands. (Units: None)*/
    public MustFlyTask setUseAltitude( boolean val ) {
        UseAltitude = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 1; // accounts for primitive types
        size += LMCPUtil.sizeOf(Position);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
            Position = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        UseAltitude = LMCPUtil.getBool(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putObject(out, Position);
        LMCPUtil.putBool(out, UseAltitude);

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
        buf.append( ws + "<MustFlyTask Series=\"CMASI\">\n");
        if (Position!= null){
           buf.append( ws + "  <Position>\n");
           buf.append( ( Position.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Position>\n");
        }
        buf.append( ws + "  <UseAltitude>" + String.valueOf(UseAltitude) + "</UseAltitude>\n");
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
        buf.append( ws + "</MustFlyTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        MustFlyTask o = (MustFlyTask) anotherObj;
        if (Position == null && o.Position != null) return false;
        if ( Position!= null && !Position.equals(o.Position)) return false;
        if (UseAltitude != o.UseAltitude) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
