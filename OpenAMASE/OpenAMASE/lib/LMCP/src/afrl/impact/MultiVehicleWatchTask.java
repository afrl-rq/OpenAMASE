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
 Multi vehicle overwatch task 
*/
public class MultiVehicleWatchTask extends afrl.cmasi.SearchTask {
    
    public static final int LMCP_TYPE = 27;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "MultiVehicleWatchTask";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.MultiVehicleWatchTask";

    /**  Entity ID to follow and watch (Units: None)*/
    @LmcpType("int64")
    protected long WatchedEntityID = 0L;
    /**  Number of vehicles to simultaneously overwatch the target (Units: None)*/
    @LmcpType("byte")
    protected short NumberVehicles = (byte)1;

    
    public MultiVehicleWatchTask() {
    }

    public MultiVehicleWatchTask(long TaskID, String Label, float RevisitRate, short Priority, boolean Required, long DwellTime, float GroundSampleDistance, long WatchedEntityID, short NumberVehicles){
        this.TaskID = TaskID;
        this.Label = Label;
        this.RevisitRate = RevisitRate;
        this.Priority = Priority;
        this.Required = Required;
        this.DwellTime = DwellTime;
        this.GroundSampleDistance = GroundSampleDistance;
        this.WatchedEntityID = WatchedEntityID;
        this.NumberVehicles = NumberVehicles;
    }


    public MultiVehicleWatchTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            MultiVehicleWatchTask newObj = new MultiVehicleWatchTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Entity ID to follow and watch (Units: None)*/
    public long getWatchedEntityID() { return WatchedEntityID; }

    /**  Entity ID to follow and watch (Units: None)*/
    public MultiVehicleWatchTask setWatchedEntityID( long val ) {
        WatchedEntityID = val;
        return this;
    }

    /**  Number of vehicles to simultaneously overwatch the target (Units: None)*/
    public short getNumberVehicles() { return NumberVehicles; }

    /**  Number of vehicles to simultaneously overwatch the target (Units: None)*/
    public MultiVehicleWatchTask setNumberVehicles( short val ) {
        NumberVehicles = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 9; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        WatchedEntityID = LMCPUtil.getInt64(in);

        NumberVehicles = LMCPUtil.getByte(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, WatchedEntityID);
        LMCPUtil.putByte(out, NumberVehicles);

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
        buf.append( ws + "<MultiVehicleWatchTask Series=\"IMPACT\">\n");
        buf.append( ws + "  <WatchedEntityID>" + String.valueOf(WatchedEntityID) + "</WatchedEntityID>\n");
        buf.append( ws + "  <NumberVehicles>" + String.valueOf(NumberVehicles) + "</NumberVehicles>\n");
        buf.append( ws + "  <DesiredWavelengthBands>\n");
        for (int i=0; i<DesiredWavelengthBands.size(); i++) {
        buf.append( ws + "  <WavelengthBand>" + String.valueOf(DesiredWavelengthBands.get(i)) + "</WavelengthBand>\n");
        }
        buf.append( ws + "  </DesiredWavelengthBands>\n");
        buf.append( ws + "  <DwellTime>" + String.valueOf(DwellTime) + "</DwellTime>\n");
        buf.append( ws + "  <GroundSampleDistance>" + String.valueOf(GroundSampleDistance) + "</GroundSampleDistance>\n");
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
        buf.append( ws + "</MultiVehicleWatchTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        MultiVehicleWatchTask o = (MultiVehicleWatchTask) anotherObj;
        if (WatchedEntityID != o.WatchedEntityID) return false;
        if (NumberVehicles != o.NumberVehicles) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)NumberVehicles;

        return hash + super.hashCode();
    }
    
}
